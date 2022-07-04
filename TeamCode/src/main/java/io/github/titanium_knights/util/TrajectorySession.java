package io.github.titanium_knights.util;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import io.github.titanium_knights.roadrunner.drive.SampleMecanumDrive;
import io.github.titanium_knights.roadrunner.trajectorysequence.TrajectorySequence;
import io.github.titanium_knights.roadrunner.trajectorysequence.TrajectorySequenceBuilder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

import static io.github.titanium_knights.util.JSONParsingUtils.addStepToTrajectory;
import static io.github.titanium_knights.util.JSONParsingUtils.parsePose;

interface State {
    void start(TrajectorySession session, Runnable resume);
    void update(TrajectorySession session, Runnable resume);
}

class TrajectorySequenceState implements State {
    TrajectorySequence trajectorySequence;

    TrajectorySequenceState(TrajectorySequence sequence) {
        this.trajectorySequence = sequence;
    }

    @Override
    public void start(TrajectorySession session, Runnable resume) {
        session.getDrive().followTrajectorySequenceAsync(trajectorySequence);
    }

    @Override
    public void update(TrajectorySession session, Runnable resume) {
        if (!session.getDrive().isBusy()) {
            resume.run();
        }
    }
}

class InterruptingCallbackState implements State {
    String name;

    InterruptingCallbackState(String name) {
        this.name = name;
    }

    @Override
    public void start(TrajectorySession session, Runnable resume) {

    }

    @Override
    public void update(TrajectorySession session, Runnable resume) {
        session.dispatchInterruptingCallback(name, resume);
    }
}

public class TrajectorySession {
    public static boolean STRICT_MODE = false;

    private final Map<String, Runnable> callbacks = new HashMap<>();
    private final Map<String, Consumer<Runnable>> interruptingCallbacks = new HashMap<>();
    private final ArrayList<State> states = new ArrayList<>();
    private Integer currentIndex;
    private Runnable resumeFunction;
    private SampleMecanumDrive drive;

    void addState(State state) {
        states.add(state);
    }

    void dispatchCallback(String name) {
        if (callbacks.containsKey(name)) {
            Objects.requireNonNull(callbacks.get(name)).run();
        } else if (STRICT_MODE) {
            throw new IllegalArgumentException(String.format("Callback \"%s\" not found", name));
        }
    }

    void dispatchInterruptingCallback(String name, Runnable resume) {
        if (callbacks.containsKey(name)) {
            Objects.requireNonNull(interruptingCallbacks.get(name)).accept(resume);
        } else if (STRICT_MODE) {
            throw new IllegalArgumentException(String.format("Interrupting callback \"%s\" not found", name));
        }
    }

    public void registerCallback(String name, Runnable runnable) {
        callbacks.put(name, runnable);
    }

    public void registerInterruptingCallback(String name, Consumer<Runnable> consumer) {
        interruptingCallbacks.put(name, consumer);
    }

    SampleMecanumDrive getDrive() {
        return this.drive;
    }

    public void setDrive(SampleMecanumDrive drive) {
        this.drive = drive;
    }

    private void nextState() {
        currentIndex++;
        if (currentIndex == states.size()) {
            currentIndex = null;
            return;
        }

        int stateIndex = currentIndex;
        resumeFunction = () -> {
            if (this.currentIndex != stateIndex) {
                throw new IllegalStateException("Attempt to call resume function a second time");
            }

            nextState();
        };
        states.get(currentIndex).start(this, resumeFunction);
    }

    public void start() {
        if (currentIndex != null) throw new IllegalStateException("Attempt to start already-running session");
        if (drive == null) throw new IllegalStateException("Attempt to start trajectory session without a drive");
        if (states.size() > 0) {
            currentIndex = -1;
            nextState();
        }
    }

    public boolean update() {
        drive.update();
        if (currentIndex != null) {
            if (currentIndex >= states.size()) return false;
            states.get(currentIndex).update(this, resumeFunction);
        }

        return true;
    }

    public void run(LinearOpMode opMode) {
        start();
        //noinspection StatementWithEmptyBody
        while (opMode.opModeIsActive() && update()) {}
    }

    public static TrajectorySession buildFromJSON(SampleMecanumDrive drive, String json) {
        try {
            JSONObject root = new JSONObject(json);
            Pose2d start = parsePose(root.getJSONObject("start"));
            JSONArray steps = root.getJSONArray("steps");
            TrajectorySequenceBuilder builder = null;
            TrajectorySession session = new TrajectorySession();

            for (int i = 0; i < steps.length(); i++) {
                JSONObject step = steps.getJSONObject(i);
                String type = step.getString("type");
                if (type.equals("interrupt")) {
                    if (builder != null) {
                        TrajectorySequence sequence = builder.build();
                        session.addState(new TrajectorySequenceState(sequence));
                        start = sequence.end();
                        builder = null;
                    }
                    session.addState(new InterruptingCallbackState(step.getString("name")));
                    if (step.has("pos")) {
                        start = parsePose(step.getJSONObject("pos"));
                    }
                    continue;
                }

                if (builder == null) {
                    builder = drive.trajectorySequenceBuilder(start);
                }

                if (type.equals("marker")) {
                    String name = step.getString("callback");
                    builder = builder.addTemporalMarker(() -> {
                        session.dispatchCallback(name);
                    });
                    continue;
                }

                builder = addStepToTrajectory(builder, step);
            }

            if (builder != null) {
                session.addState(new TrajectorySequenceState(builder.build()));
            }

            return session;
        } catch (JSONException e) {
            throw new RuntimeException(String.format("Unable to parse path: %s", e.getMessage()));
        }
    }
}

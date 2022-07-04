package io.github.titanium_knights.util;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import io.github.titanium_knights.roadrunner.trajectorysequence.TrajectorySequence;
import io.github.titanium_knights.roadrunner.trajectorysequence.TrajectorySequenceBuilder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.function.Function;
import java.util.stream.Collectors;

public class JSONParsingUtils {
    private JSONParsingUtils() {}

    public static Vector2d parseVector(JSONObject obj) throws JSONException {
        return new Vector2d(obj.getDouble("x"), obj.getDouble("y"));
    }

    public static Pose2d parsePose(JSONObject obj) throws JSONException {
        return new Pose2d(obj.getDouble("x"), obj.getDouble("y"), Math.toRadians(obj.getDouble("heading")));
    }

    public static TrajectorySequenceBuilder addStepToTrajectory(TrajectorySequenceBuilder builder, JSONObject step) throws JSONException {
        String type = step.getString("type");
        switch (type) {
            case "lineTangent":
                builder = builder.lineTo(parseVector(step.getJSONObject("pos")));
                break;
            case "lineConstant":
                builder = builder.lineToConstantHeading(parseVector(step.getJSONObject("pos")));
                break;
            case "lineLinear":
                builder = builder.lineToLinearHeading(parsePose(step.getJSONObject("pos")));
                break;
            case "lineSpline":
                builder = builder.lineToSplineHeading(parsePose(step.getJSONObject("pos")));
                break;
            case "splineTangent":
                builder = builder.splineTo(parseVector(step.getJSONObject("pos")), Math.toRadians(step.getDouble("tangent")));
                break;
            case "splineConstant":
                builder = builder.splineToConstantHeading(parseVector(step.getJSONObject("pos")), Math.toRadians(step.getDouble("tangent")));
                break;
            case "splineLinear":
                builder = builder.splineToLinearHeading(parsePose(step.getJSONObject("pos")), Math.toRadians(step.getDouble("tangent")));
                break;
            case "splineSpline":
                builder = builder.splineToSplineHeading(parsePose(step.getJSONObject("pos")), Math.toRadians(step.getDouble("tangent")));
                break;
            case "setTangent":
                builder = builder.setTangent(Math.toRadians(step.getDouble("tangent")));
                break;
            case "turn":
                builder = builder.turn(Math.toRadians(step.getDouble("angle")));
                break;
            case "wait":
                builder = builder.waitSeconds(step.getDouble("seconds"));
                break;
            default:
                throw new IllegalArgumentException(String.format("Unknown step type: %s", type));
        }

        return builder;
    }

    public static TrajectorySequence simulateFromJSON(Function<Pose2d, TrajectorySequenceBuilder> supplyBuilder, String json) throws JSONException {
        JSONObject root = new JSONObject(json);
        JSONArray steps = root.getJSONArray("steps");
        Pose2d start = parsePose(root.getJSONObject("start"));
        TrajectorySequenceBuilder builder = supplyBuilder.apply(start);

        for (int i = 0; i < steps.length(); i++) {
            JSONObject step = steps.getJSONObject(i);
            String type = step.getString("type");
            if (type.equals("interrupt")) {
                String name = step.getString("callback");
                builder = builder.addTemporalMarker(() -> {
                    System.out.printf("Interrupting callback triggered: %s\n", name);
                }).waitSeconds(0.1);
                if (step.has("pos")) {
                    builder = builder.lineToLinearHeading(parsePose(step.getJSONObject("pos")));
                }
                continue;
            }

            if (type.equals("marker")) {
                String name = step.getString("callback");
                builder = builder.addTemporalMarker(() -> {
                    System.out.printf("Callback triggered: %s\n", name);
                });
                continue;
            }

            builder = addStepToTrajectory(builder, step);
        }

        return builder.build();
    }

    public static String stringFromResource(String name) {
        try {
            InputStream stream = JSONParsingUtils.class.getResourceAsStream(name);
            assert stream != null;
            InputStreamReader reader = new InputStreamReader(stream, StandardCharsets.UTF_8);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String result = bufferedReader.lines().collect(Collectors.joining("\n"));
            bufferedReader.close();
            reader.close();
            stream.close();

            return result;
        } catch (IOException e) {
            throw new RuntimeException(String.format("Could not read %s: %s", name, e.getMessage()));
        }
    }
}

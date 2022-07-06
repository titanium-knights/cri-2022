package io.github.titanium_knights.util;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.Constraints;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.SampleMecanumDrive;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;
import io.github.titanium_knights.roadrunner.trajectorysequence.TrajectorySequence;
import io.github.titanium_knights.roadrunner.trajectorysequence.TrajectorySequenceBuilder;
import io.github.titanium_knights.roadrunner.trajectorysequence.sequencesegment.TrajectorySegment;
import io.github.titanium_knights.roadrunner.trajectorysequence.sequencesegment.TurnSegment;
import io.github.titanium_knights.roadrunner.trajectorysequence.sequencesegment.WaitSegment;

import java.util.function.Function;
import java.util.stream.Collectors;

public class MeepMeepConversion {
    private MeepMeepConversion() {}

    public static com.noahbres.meepmeep.roadrunner.trajectorysequence.TrajectorySequence convert(TrajectorySequence tkSequence) {
        return new com.noahbres.meepmeep.roadrunner.trajectorysequence.TrajectorySequence(
                tkSequence.sequenceList.stream().map(sequenceSegment -> {
                    if (sequenceSegment instanceof TrajectorySegment) {
                        return new com.noahbres.meepmeep.roadrunner.trajectorysequence.TrajectorySegment(((TrajectorySegment)sequenceSegment).getTrajectory());
                    }

                    if (sequenceSegment instanceof TurnSegment) {
                        TurnSegment turnSegment = (TurnSegment) sequenceSegment;
                        return new com.noahbres.meepmeep.roadrunner.trajectorysequence.TurnSegment(
                                turnSegment.getStartPose(),
                                turnSegment.getTotalRotation(),
                                turnSegment.getMotionProfile(),
                                turnSegment.getMarkers()
                        );
                    }

                    if (sequenceSegment instanceof WaitSegment) {
                        WaitSegment waitSegment = (WaitSegment) sequenceSegment;
                        return new com.noahbres.meepmeep.roadrunner.trajectorysequence.WaitSegment(
                                waitSegment.getStartPose(),
                                waitSegment.getDuration(),
                                waitSegment.getMarkers()
                        );
                    }

                    throw new IllegalArgumentException("Unsupported segment type");
                }).collect(Collectors.toList())
        );
    }

    public static RoadRunnerBotEntity botForTrajectorySequence(MeepMeep meepMeep, double width, double height, Constraints constraints, Function<Function<Pose2d, TrajectorySequenceBuilder>, TrajectorySequence> buildSequence) {
        TrajectorySequence sequence = buildSequence.apply(startPose -> new TrajectorySequenceBuilder(
                startPose,
                null,
                SampleMecanumDrive.getVelocityConstraint(constraints.getMaxVel(), constraints.getMaxAngVel(), constraints.getTrackWidth()),
                SampleMecanumDrive.getAccelerationConstraint(constraints.getMaxAccel()),
                constraints.getMaxAngVel(),
                constraints.getMaxAngAccel()
        ));

        return new DefaultBotBuilder(meepMeep)
                .setDimensions(width, height)
                .setConstraints(constraints)
                .followTrajectorySequence(MeepMeepConversion.convert(sequence));
    }

    public static RoadRunnerBotEntity botForTrajectorySequence(MeepMeep meepMeep, Function<Function<Pose2d, TrajectorySequenceBuilder>, TrajectorySequence> buildSequence) {
        return botForTrajectorySequence(
                meepMeep,
                12,
                18,
                new Constraints(52.48291908330528, 52.48291908330528, Math.toRadians(261.482587826087), Math.toRadians(261.482587826087), 11.5),
                buildSequence
        );
    }
}

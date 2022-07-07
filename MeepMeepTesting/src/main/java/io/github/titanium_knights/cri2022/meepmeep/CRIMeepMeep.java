package io.github.titanium_knights.cri2022.meepmeep;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.noahbres.meepmeep.MeepMeep;
import io.github.titanium_knights.roadrunner.trajectorysequence.TrajectorySequence;
import io.github.titanium_knights.roadrunner.trajectorysequence.TrajectorySequenceBuilder;
import io.github.titanium_knights.util.JSONParsingUtils;
import io.github.titanium_knights.util.MeepMeepConversion;

import java.io.IOException;
import java.util.function.Function;

public class CRIMeepMeep {
    private CRIMeepMeep() {}

    public static MeepMeep create() {
        return new MeepMeep(500)
                .setDarkMode(true)
                .setBackground(MeepMeep.Background.FIELD_FREIGHTFRENZY_ADI_DARK)
                .setBackgroundAlpha(0.95f);
    }

    public static MeepMeep create(Function<Function<Pose2d, TrajectorySequenceBuilder>, TrajectorySequence> buildSequence) {
        MeepMeep meepMeep = create();
        return meepMeep.addEntity(MeepMeepConversion.botForTrajectorySequence(meepMeep, buildSequence));
    }

    public static MeepMeep create(String filename) {
        String path = JSONParsingUtils.stringFromResource(filename);
        return CRIMeepMeep.create(makeBuilder -> JSONParsingUtils.simulateFromJSON(makeBuilder, path));
    }
}

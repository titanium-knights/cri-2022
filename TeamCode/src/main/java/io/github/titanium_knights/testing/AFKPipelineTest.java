package io.github.titanium_knights.testing;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import piplines.AFKPipeline;

@Autonomous(name = "CV Test/Calibration", group = "Test")
public class AFKPipelineTest extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId",
                "id", hardwareMap.appContext.getPackageName());
        WebcamName webcamName = hardwareMap.get(WebcamName.class, "Webcam 1");
        OpenCvCamera camera = OpenCvCameraFactory.getInstance().createWebcam(webcamName, cameraMonitorViewId);
        AFKPipeline pipeline = new AFKPipeline(telemetry);

        camera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener()
        {
            @Override
            public void onOpened()
            {
                // Usually this is where you'll want to start streaming from the camera (see section 4)
                camera.startStreaming(1920, 1080, OpenCvCameraRotation.UPRIGHT);
                camera.setPipeline(pipeline);
            }
            @Override
            public void onError(int errorCode)
            {
                /*
                 * This will be called if the camera could not be opened
                 */
            }
        });

        FtcDashboard.getInstance().startCameraStream(camera, 0);
        telemetry = new MultipleTelemetry(FtcDashboard.getInstance().getTelemetry(), telemetry);

        waitForStart();

        while (opModeIsActive()) {
            String position;
            AFKPipeline.CapstonePosition pos = pipeline.getAnalysis();
            if (pos == AFKPipeline.CapstonePosition.LEFT) {
                position = "left";
            } else if (pos == AFKPipeline.CapstonePosition.RIGHT) {
                position = "right";
            } else {
                position = "center";
            }
            telemetry.addData("position", position);
            telemetry.update();
        }
    }
}

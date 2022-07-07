package io.github.titanium_knights.util;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import piplines.AFKPipeline;

public class CapstoneVision {
    OpenCvCamera camera;
    public AFKPipeline pipeline;
    public CapstoneVision(HardwareMap hardwareMap, Telemetry telemetry) {
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId",
                "id", hardwareMap.appContext.getPackageName());
        WebcamName webcamName = hardwareMap.get(WebcamName.class, "Webcam 1");
        camera = OpenCvCameraFactory.getInstance().createWebcam(webcamName, cameraMonitorViewId);
        pipeline = new AFKPipeline(telemetry);

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
    }
    public int getPosition(){
        AFKPipeline.CapstonePosition capstonePosition = pipeline.getAnalysis();
        if(capstonePosition == AFKPipeline.CapstonePosition.CENTER){
            return 1;
        }
        else if (capstonePosition == AFKPipeline.CapstonePosition.LEFT) {
            return 0;
        }
        else{
            return 2;
        }
    }
}

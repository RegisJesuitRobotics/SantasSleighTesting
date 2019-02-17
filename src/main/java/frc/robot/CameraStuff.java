package frc.robot;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.cscore.UsbCamera;

public class CameraStuff extends IterativeRobot {

    public void cameraInit() {
        UsbCamera frontCamera = CameraServer.getInstance().startAutomaticCapture();
        frontCamera.setResolution(640, 480);
        frontCamera.setFPS(30);
    }
}
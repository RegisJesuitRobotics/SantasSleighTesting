package frc.robot;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class LimitSwitch {
	// Limit Switch for the hatch
	DigitalInput limitSwitch;
	// Relay HatchMotor
	PlaystationController m_playStationController;

	public LimitSwitch(PlaystationController playstationController) {
		// HatchMotor = new Relay();
		limitSwitch = new DigitalInput(2);
		m_playStationController = playstationController;
	}

	public void moveUntilTouch() {
		String HatchMotorStatus = "Not Running";
		SmartDashboard.putString("Hatch Detector", HatchMotorStatus);
		while (m_playStationController.ButtonL1() == true) {
			SmartDashboard.putString("Hatch Detector", HatchMotorStatus);
			if (limitSwitch.get() == false) {
				// HatchMotor.set(Relay.Value.kOff);
				HatchMotorStatus = "Hatch In";
			} else {
				// HatchMotor.set(Relay.Value.kOn);
				HatchMotorStatus = "Hatch Pulling In";
			}
		}
		HatchMotorStatus = "Not Running";
	}
}
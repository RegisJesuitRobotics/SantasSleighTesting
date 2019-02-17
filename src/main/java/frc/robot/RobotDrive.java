package frc.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

public class RobotDrive {
	WPI_TalonSRX RightMotor, LeftMotor;
	PlaystationController m_playStationController;
	boolean HasBeenStopped;
	double Limiter;
	int nextSpeedLevel;

	public RobotDrive(PlaystationController playStationController) {
		m_playStationController = playStationController;
		HasBeenStopped = false;
		LeftMotor = new WPI_TalonSRX(1);
		RightMotor = new WPI_TalonSRX(0);
		Limiter = 0.83245;
		nextSpeedLevel = 3;

	}

	public void Slowtoggle() {

		if (m_playStationController.ButtonReleaseTriangle() == true) {

			if (nextSpeedLevel == 1) {
				Limiter = 0.2;
				nextSpeedLevel = 3;

			} else if (nextSpeedLevel == 2) {
				Limiter = 0.35;
				nextSpeedLevel = 1;

			} else if (nextSpeedLevel == 3) {
				Limiter = 0.83245;
				nextSpeedLevel = 2;
			}

		}
	}

	public void Drive() {

		double RightTrigger = m_playStationController.RightTrigger();
		double LeftTrigger = m_playStationController.LeftTrigger();
		double LeftStick = m_playStationController.LeftStickXAxis();
		boolean isTriangle = m_playStationController.ButtonTriangle();
		double Deadzone = 0.1;
		double RightPower = 1;
		double LeftPower = 1;
		double Power;
		double turn = 2 * LeftStick;
		Power = RightTrigger - LeftTrigger;
		String move = "";
		if (LeftStick > Deadzone) {

			LeftPower = Power;
			RightPower = Power - (turn * Power);
			move = "Left Turn ";
		} else if (LeftStick < -Deadzone) {

			LeftPower = Power + (turn * Power);
			RightPower = Power;
			move = "Right Turn ";
		} else {
			LeftPower = Power;
			RightPower = Power;
			move = "Straight ";
		}
		LeftMotor.set(-LeftPower * Limiter);
		RightMotor.set(RightPower * Limiter);
	}
}

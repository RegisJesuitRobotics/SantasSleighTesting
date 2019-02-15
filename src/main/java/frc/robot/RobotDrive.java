package frc.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import java.awt.Event;

public class RobotDrive extends Subsystem{
	WPI_TalonSRX RightMotor, LeftMotor;
	// Encoder LeftEncoder, RightEncoder;
	PlaystationController m_playStationController;
	// STUFF IN THE DIO IS ANALOG INPUT
	AnalogInput LeftUltraSonic, Laser, m_RetroReflectiveSensor, LineFollower;
	boolean HasBeenStopped;
	
	@Override
	protected void initDefaultCommand() {
		
	}
	

	public void spin() {
	System.out.println("Running");
	Robot.m_robotdrive.LeftMotor.set(-0.3);
	Robot.m_robotdrive.RightMotor.set(-0.3);
	try {
		wait(2000);
	} catch (Exception e) {
	//TODO: handle exception
	}
	Robot.m_robotdrive.LeftMotor.set(0);
	Robot.m_robotdrive.RightMotor.set(0);
	}
	public RobotDrive(PlaystationController playStationController) {
		super("robotDrive");
		m_playStationController = playStationController;
		HasBeenStopped = false;
		LeftMotor = new WPI_TalonSRX(0);
		RightMotor = new WPI_TalonSRX(1);
		LeftUltraSonic = new AnalogInput(2);
		Laser = new AnalogInput(0);
		// m_RetroReflectiveSensor = new AnalogInput(3);
		LineFollower = new AnalogInput(3);

		





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
		double Limiter = 0.83245;
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
		// if (isTriangle == true) {
		// LeftEncoder.reset();
		// RightEncoder.reset();
		// }
		LeftMotor.set(-LeftPower * Limiter);
		RightMotor.set(RightPower * Limiter);

	}

	public void fullSequence() {
		HasBeenStopped = false;
		if (m_playStationController.ButtonR1() == true) {
			FindWhiteLine();
			hatchAlign(Direction.Right, 0.4);
			approachHatch();
		}
	}

	public void AlignButton() {
		if (m_playStationController.ButtonCircle() == true) {
			hatchAlign(Direction.Right, .4);
		}

	}

	public void ApproachButton() {
		if (m_playStationController.ButtonTriangle() == true) {
			approachHatch();
		}

	}

	public void FindWhiteLineButton() {
		if (m_playStationController.ButtonX() == true) {
			FindWhiteLine();
		}
	}

	public void getSensorData() {
		SmartDashboard.putNumber("left voltage", LeftUltraSonic.getVoltage());
		SmartDashboard.putNumber("left get", LeftUltraSonic.getValue());
		SmartDashboard.putNumber("left average voltage", LeftUltraSonic.getAverageVoltage());
		SmartDashboard.putNumber("left average get", LeftUltraSonic.getAverageValue());
		SmartDashboard.putNumber("Math Distance", getUltraSonicInches());
		SmartDashboard.putNumber("Laser Voltage", Laser.getVoltage());
		SmartDashboard.putNumber("Retro Bayyybeeeeee ", m_RetroReflectiveSensor.getVoltage());

	}

	// UltraSonic Sensor begins to become unreliable around 13 inches away, Sensor
	// also only reads if its 2 inches away
	// This returns distances with 2 decimal points of accuracy
	private double getUltraSonicInches() {
		double proximity = (LeftUltraSonic.getVoltage() + 0.33104) / 0.17741;
		// SmartDashboard.putNumber("Status", proximity);
		return (double) Math.round(proximity * 1000) / 1000;

	}

	public void DriveForward(double speed) {
		LeftMotor.set(-speed);
		RightMotor.set(speed);
	}

	// THIS ONE IMPORTANT
	public void FindWhiteLine() {
		while (true && HasBeenStopped == false) {
			DriveForward(.15);
			if ((m_RetroReflectiveSensor.getVoltage() < 3)) {
				break;
			}
			if ((m_playStationController.ButtonSquare() == true)) {
				HasBeenStopped = true;
				break;
			}
		}
		moveAnAmount(.05, 1);
		stop();
	}

	public void moveUntilTouch() {

		DriveForward(.15);
		// untilTouch(dgtl);

		stop();

	}

	public void hatchAlign(Direction direction, double speed) {

		if (HasBeenStopped == false) {

			SmartDashboard.putString("Status", "startedTurning");
			double leftProximity = getUltraSonicInches();
			SmartDashboard.putNumber("Status", leftProximity);

			while (leftProximity >= 30 || leftProximity <= 2) {
				spin(direction, speed);
				SmartDashboard.putNumber("Status", leftProximity);
				leftProximity = getUltraSonicInches();
				if (m_playStationController.ButtonSquare()) {
					HasBeenStopped = true;
					break;
				}
			}
			stop();

			SmartDashboard.putString("Status", "stoppedTurning");
			double oldLeftProximity = getUltraSonicInches();
			spin(direction, speed);
			while (oldLeftProximity != getUltraSonicInches() && HasBeenStopped == false) {
				SmartDashboard.putNumber("Left Proximity", getUltraSonicInches());
				SmartDashboard.putString("Status2", "secondLoop");

				spin(direction, speed);
				oldLeftProximity = leftProximity;
				leftProximity = getUltraSonicInches();
				SmartDashboard.putNumber("Old Left Proximity", oldLeftProximity);
				SmartDashboard.putNumber("Left Proximity", leftProximity);
			}
			stop();
			SmartDashboard.putNumber("Old Left Proximity", oldLeftProximity);
			SmartDashboard.putString("Status2", "stopped Second Loop");
		}
	}

	public void spin(Direction direction, double speed) {
		if (direction == Direction.Right) {
			LeftMotor.set(-speed);
			RightMotor.set(-speed);
		} else {
			LeftMotor.set(speed);
			RightMotor.set(speed);
		}
	}

	public void stop() {
		LeftMotor.set(0);
		RightMotor.set(0);
	}

	public void approachHatch() {
		if (HasBeenStopped == false) {
			SmartDashboard.putString("Status3", "stopped Second Loop");
			double proximityOutOfRange = 30.042;
			double leftProximity = getUltraSonicInches();
			while (leftProximity == proximityOutOfRange) {
				DriveForward(0.4);
				leftProximity = getUltraSonicInches();
				if (m_playStationController.ButtonSquare()) {
					HasBeenStopped = true;
					break;
				}
			}
			while (leftProximity > 4 && leftProximity != proximityOutOfRange) {
				SmartDashboard.putString("Status3", "inside Loop");
				DriveForward(0.2);
				leftProximity = getUltraSonicInches();
				SmartDashboard.putNumber("Left Proximity", leftProximity);
				if (m_playStationController.ButtonSquare()) {
					HasBeenStopped = true;
					break;
				}
			}
			SmartDashboard.putString("Status3", "stop");
			stop();
		}
	}

	public void moveAnAmount(double speed, double time) {
		LeftMotor.set(speed);
		RightMotor.set(speed);
		Timer.delay(time);
		stop();

	}

	public void getWhiteLine() {
		//2.5 is the threshold
		SmartDashboard.putNumber("Line Follower Value", LineFollower.getVoltage());
	}
}

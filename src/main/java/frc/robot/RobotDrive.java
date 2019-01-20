package frc.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class RobotDrive {
	WPI_TalonSRX RightMotor, LeftMotor;
	// Encoder LeftEncoder, RightEncoder;
	PlaystationController m_playStationController;
	// STUFF IN THE DIO IS ANALOG INPUT
	AnalogInput LeftUltraSonic, Laser;
	
	//GRADLE WE LOVE U SORRRY FOR OUR SINS

	public RobotDrive(PlaystationController playStationController) {
		m_playStationController = playStationController;
		LeftMotor = new WPI_TalonSRX(0);
		RightMotor = new WPI_TalonSRX(1);
		LeftUltraSonic = new AnalogInput(2);

		/*
		 * Encoder code // LeftEncoder = new Encoder(0, 1, true,
		 * Encoder.EncodingType.k2X); // RightEncoder = new Encoder(2, 3, false,
		 * Encoder.EncodingType.k2X); // proximity = new Ultrasonic(5,5); //
		 * .setAutomaticMode(true);
		 * 
		 * // LeftEncoder.reset(); // RightEncoder.reset();
		 * 
		 * // double wheelRadius = 2;// inches // double wheelCircumference =
		 * (3.14159265359 * wheelRadius * wheelRadius); //
		 * LeftEncoder.setDistancePerPulse(wheelCircumference); //
		 * RightEncoder.setDistancePerPulse(wheelCircumference);
		 * 
		 * // int MinRate = 10; // LeftEncoder.setMinRate(MinRate); //
		 * LeftEncoder.setSamplesToAverage(7); // RightEncoder.setMinRate(MinRate); //
		 * RightEncoder.setSamplesToAverage(7);
		 */
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
		// 	LeftEncoder.reset();
		// 	RightEncoder.reset();
		// }
		LeftMotor.set(-LeftPower * Limiter);
		RightMotor.set(RightPower * Limiter);

	}

	// public void EncoderTest() {
	// 	SmartDashboard.putNumber("LeEncode", LeftEncoder.get());
	// 	SmartDashboard.putNumber("RiEncode", RightEncoder.get());

	// }

	public void getSensorData() {
		SmartDashboard.putNumber("left voltage", LeftUltraSonic.getVoltage());
		SmartDashboard.putNumber("left get", LeftUltraSonic.getValue());
		SmartDashboard.putNumber("left average voltage", LeftUltraSonic.getAverageVoltage());
		SmartDashboard.putNumber("left average get", LeftUltraSonic.getAverageValue());
		SmartDashboard.putNumber("Math Distance", getUltraSonicInches());
		SmartDashboard.putNumber("Laser Voltage", Laser.getVoltage());
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

	public void FindWhiteLine() {
		/*
		
		*/

		boolean isWhiteLineFound = false;

		while (!isWhiteLineFound) {
			// Drive forward
			// Read from sensor
			// if sensor finds white tape
			// isWhiteLineFound = true;
			// stop moving forward
		}
	}

	public void hatchAlign(Direction direction, double speed) {
		SmartDashboard.putString("Status", "startedTurning");
		double leftProximity = getUltraSonicInches();
		SmartDashboard.putNumber("Status", leftProximity);
		while (leftProximity >= 30 || leftProximity <= 2) {
			spin(direction, speed);
			SmartDashboard.putNumber("Status", leftProximity);
			leftProximity = getUltraSonicInches();
		}
		stop();
		SmartDashboard.putString("Status", "stoppedTurning");
		double oldLeftProximity = getUltraSonicInches();
		spin(direction, speed);
		while (oldLeftProximity != getUltraSonicInches()) {
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
		SmartDashboard.putString("Status3", "stopped Second Loop");
		double proximityOutOfRange = 30.042;
		double leftProximity = getUltraSonicInches();
		while (leftProximity == proximityOutOfRange) {
			DriveForward(0.4);
			leftProximity = getUltraSonicInches();
		}
		while (leftProximity > 4 && leftProximity != proximityOutOfRange) {
			SmartDashboard.putString("Status3", "inside Loop");
			DriveForward(0.2);
			leftProximity = getUltraSonicInches();
			SmartDashboard.putNumber("Left Proximity", leftProximity);
		}
		SmartDashboard.putString("Status3", "stop");
		stop();

	}
}

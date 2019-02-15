package frc.robot;

import edu.wpi.first.wpilibj.command.Command;


public class Spin extends Command{
    public Spin() {
        super("Spin", Robot.m_robotdrive);
        System.out.println("Spin constructor");

    }
    @Override
        protected void initialize() {
            super.initialize();
            System.out.println("Spin made it too initialize");
        }
@Override
protected boolean isFinished() {
return false;
}
@Override
 protected void execute() {
	System.out.println("Spin made it too execute");

    Robot.m_robotdrive.spin();
 }


}



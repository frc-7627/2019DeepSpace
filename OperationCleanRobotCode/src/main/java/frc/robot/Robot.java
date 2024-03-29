/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import frc.robot.subsystems.Drive;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.Joystick;

import javax.management.timer.Timer;
import edu.wpi.first.cameraserver.CameraServer;


/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private static final String kMoveAuto = "Move Forward";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();

  Joystick stick = new Joystick(0);
  Joystick flightStick = new Joystick(1);

  Spark leftMotor = new Spark(1);
  Spark rightMotor = new Spark(0);
  Spark arm = new Spark(9);
  Spark elevator = new Spark(4);

  Drive driveTrain =  new Drive(leftMotor, rightMotor);
  Boolean superFast = false;
  Timer autoTimer = new Timer();

  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
  @Override
  public void robotInit() {
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    m_chooser.addOption("Move Forward", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);

	  CameraServer.getInstance().startAutomaticCapture(); 
  }

  /**
   * This function is called every robot packet, no matter the mode. Use
   * this for items like diagnostics that you want ran during disabled,
   * autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before
   * LiveWindow and SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
  }

  /**
   * This autonomous (along with the chooser code above) shows how to select
   * between different autonomous modes using the dashboard. The sendable
   * chooser code works with the Java SmartDashboard. If you prefer the
   * LabVIEW Dashboard, remove all of the chooser code and uncomment the
   * getString line to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional comparisons to
   * the switch structure below with additional strings. If using the
   * SendableChooser make sure to add them to the chooser code above as well.
   */
  @Override
  public void autonomousInit() {
    m_autoSelected = m_chooser.getSelected();
    // m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
    switch (m_autoSelected) {

      case kCustomAuto:
        // Put custom auto code here
        autoTimer.start();
        if (autoTimer.get() < 3) {
          driveTrain.tankDrive(-.5, .5, false); //360 spinning bois
        } else {
          driveTrain.tankDrive(0, 0, false);
          autoTimer.stop();
        }
        break;

      case kMoveAuto:
        autoTimer.start();
        if (autoTimer.get() < 3) {
          driveTrain.tankDrive(.5, .5, false); //Move forward
        } else {
          driveTrain.tankDrive(0, 0, false);
          autoTimer.stop();
        }
        break;

      case kDefaultAuto:
      default:
        // Put default auto code here
        periodic();
        break;
    }
  }

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {
    periodic();
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }

  /*************************
   ******ROBOT CODE!!*******
   ************************/

  public void periodic() {
    //Custom function to run in both auto and teleop

    if(flightStick.getRawButtonPressed(6) && flightStick.getRawButton(4)) {
      //Shut down all running motors
      arm.stopMotor();
      driveTrain.shutdown();

    } else {
        double leftPos = stick.getRawAxis(1);
        double rightPos = stick.getRawAxis(3);
        double flightArmPos = flightStick.getRawAxis(2);
        double flightElevatorPos = flightStick.getRawAxis(1);

        if (stick.getRawButtonPressed(1)) {
          superFast = !superFast; //speedy boi
        }
        SmartDashboard.putBoolean("SuperFasty Mode", superFast);

        arm.set(flightArmPos);
        elevator.set(flightElevatorPos);
        driveTrain.tankDrive(leftPos, rightPos, superFast);
    }
  }
}

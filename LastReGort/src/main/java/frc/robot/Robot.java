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

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;

import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

import edu.wpi.first.wpilibj.Encoder;


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
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();

  double diameter = 6/12; // 6 inch wheels
  double dist =0.5*3.14/1024;  // ft per pulse
  //Define an encoder on Channels 0 and 1 without reversing direction and 4x encoding type  

//Set the distance per pulse to the predetermined distance
  
 //counter for movement 
  //Counter motorCounter = new Counter(new DigitalInput(2));
  //private double position = 0;
 
  /*Encoder creaton*/
  Encoder elevOpEncoder = new Encoder(0, 1, false, Encoder.EncodingType.k4X);
  Encoder grabMagEncoder = new Encoder(2,3, false, Encoder.EncodingType.k4X);



  //UnDesired encoder positon for elevator
  // 100 WILL CHANGE **************************************************************************************************************
    int ToHighPosition = 100;
  //Desired encorder position for elevator
	double desiredPosition = 0;


  	// motor controls
	Spark m_frontleft = new Spark(1);
	SpeedControllerGroup m_left = new SpeedControllerGroup(m_frontleft);
	Spark m_frontright = new Spark(0);
	SpeedControllerGroup m_right = new SpeedControllerGroup(m_frontright);
	
	DifferentialDrive m_drive = new DifferentialDrive(m_left, m_right);
	
	// elevator controls
	Spark forwardElevator = new Spark(3); //moves arm forward and back
	Spark verticalElevator = new Spark(4); //moves arm up and down

	Spark wheelGrabber = new Spark(6); //moves wheels on arm
	Spark arm7 = new Spark(7);
	Spark arm8 = new Spark(8); //opens and closes arm
	Spark arm9 = new Spark(9); //opens and closes arm
	
	Joystick stick = new Joystick(0);
	Joystick flightStick = new Joystick(1);

	//Timers
	Timer armMoverTimer = new Timer();
	double armMovingTime = 3;

	//If elevator breaks, press a button to activate manual control
	Boolean manualControl = false;
	Boolean isArmClosed = false;
	Boolean isArmOpen = true;

	Boolean superFast = true;

  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
  @Override
  public void robotInit() {
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);

	  CameraServer.getInstance().startAutomaticCapture(); 
	  desiredPosition = elevOpEncoder.getDistance();

	  elevOpEncoder.reset();
	  elevOpEncoder.setDistancePerPulse(1.0/360);
	  elevOpEncoder.setSamplesToAverage(5); 

	  grabMagEncoder.setDistancePerPulse(dist);

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
        break;
      case kDefaultAuto:
      default:
        // Put default auto code here
        break;
	}
	
	
	if(flightStick.getRawButtonPressed(6) && flightStick.getRawButton(4)){
		forwardElevator.stopMotor();
		verticalElevator.stopMotor();
		wheelGrabber.stopMotor();
		arm7.stopMotor();
		arm8.stopMotor();
		arm9.stopMotor();
 
	   } else {
 
	   //System.out.println("Set Value: " + desiredPosition);
	   //System.out.println("Encoder Value: " + elevOpEncoder.get());
	   System.out.println("Elevator Encoder Value: " + elevOpEncoder.getDistance());
	   System.out.println("Magnet Encoder Value: " + grabMagEncoder.getDistance());
	 
	 /*if (armMover.get() > 0) {
		 position += motorCounter.getDistance();
	  } else {
		 position -= motorCounter.getDistance();
	  }
	  motorCounter.reset(); */
	 
	 // 100 is not the final number
 
	 /*if (Math.abs(position) >= 100){
		  armMover.set(0.0);
	  }*/
	  
 
	 // NOTE: anything with the word "flight" in it is referring the flightstick, which is a big joystick.
			 
		 double leftJoystickPosition = -0.65 * stick.getRawAxis(1);
		 double rightJoystickPosition = -0.7 * stick.getRawAxis(5);
 
		 double flightElevatorPos = flightStick.getRawAxis(1);
		 double flightArmPos = flightStick.getRawAxis(2);
 
		 //Enable manual mode when both button 6 on the flightsick and select button "button 8" on the controller are pressed
		 /*if (flightStick.getRawButtonPressed(6)) {
			 manualControl = !manualControl; //toggles manual and autonomous control
			 System.out.println("Toggled manual control to " + manualControl);
		 }*/

		 if (stick.getRawButtonPressed(1)){ //A button
			superFast = !superFast;
		}

		if (superFast) {
			leftJoystickPosition = -.95 * stick.getRawAxis(1);
			rightJoystickPosition = -1 * stick.getRawAxis(1);
		}
 
		 if (stick.getRawButtonPressed(2) && stick.getRawButton(4)){
			 System.out.println("Reset encoder value to zero."); //Pressing x button will reset encoder value
			 elevOpEncoder.reset();
		 }
 
		 /* if (manualControl) {
			 //Move to down when button 12 is pressed on flightstick if the limit switch is not pressesd
			 if ( limitSwitch.get() == false &&  flightStick.getRawButton(12) && elevOpEncoder.get() > -100) {
				 verticalElevator.set(-.5);
			 }
			 
			 if (flightStick.getRawButton(12) && elevOpEncoder.get() <= -100 && grabMagEncoder.get() >= 100) {
				 verticalElevator.set(-.5);
			 }
		 
			 //Move up when button 8 is pressed on flightstick and if it is not to high
			 else if (elevOpEncoder.get() < ToHighPosition - 5 && flightStick.getRawButton(8)){
				 verticalElevator.set(.5);
			 }
		 
			 /Stop elevator when button 10 is pressed on flightstick
			 else if (flightStick.getRawButton(10)){
				 verticalElevator.set(0);
			 } 
			 else {
				 verticalElevator.set(0);
			 }
	 
   } */
		 /*if(Math.abs(flightElevatorPos) < 0.1) { 
			 
			 if (elevOpEncoder.getDistance() >= desiredPosition - .05 && elevOpEncoder.getDistance() <= desiredPosition + .05) {
				 verticalElevator.set(0.0);
			 
			 }  else if(elevOpEncoder.getDistance() > desiredPosition) {
				 verticalElevator.set(-.5);
			 
			 }else if(elevOpEncoder.getDistance() < desiredPosition) {
				  verticalElevator.set(.5);
			 }
 
				 //Move to middle bay height when button 10 is pressed on flightstick
			 if (flightStick.getRawButtonPressed(10)){
				 desiredPosition = 30;  //Update this number with correct number of revolutions
			 }
 
				 //Move to bottom bay height when button 12 is pressed on flightstick
			 if (flightStick.getRawButtonPressed(12)){
				 desiredPosition = 10;  //Update
			 }
 
			 //Move to top bay height when button 8 is pressed on flightstick
			 if (flightStick.getRawButtonPressed(8)){
				 desiredPosition = 60;  //Update this number once number of rotations is known
			 }
			 
		 } 
   }*/
		  if (elevOpEncoder.getDistance() <= 0 && !flightStick.getRawButton(7))
		  {
			 //elevator fine adjustment
			 desiredPosition = elevOpEncoder.getDistance();
			 verticalElevator.set(.75 * -flightElevatorPos);
			 
		 } else if (elevOpEncoder.getDistance() >= 0 && flightElevatorPos > 0) {
			 verticalElevator.set(0.65 * -flightElevatorPos);
		 }
		 else if (flightStick.getRawButton(7))
		 {
			 desiredPosition = elevOpEncoder.getDistance();
			 verticalElevator.set(.4 * -flightElevatorPos);
		 } else {
			 verticalElevator.set(0);
		 }
		 
		
		 //stops arm from going forward
 
		 if (flightStick.getRawButton(2)){
			 wheelGrabber.set(-.75);
		 } else if (flightStick.getRawButton(1)){
			 wheelGrabber.set(1);
		 } else {
			 wheelGrabber.set(0);
		 }
 
		 /* if (flightArmPos < .1) { //Runs this if the arm is not being controlled manually
 
		 //Open arm when button 9 on the flightstick is pressed if arm is in the closed position and arms are not moving
		 if (flightStick.getRawButtonPressed(9) && isArmClosed && !isArmOpen){
			 arm8.set(.5 * 0.9);
			 arm9.set(.5 * 0.95);
			 arm7.set(.5);
 
			 isArmClosed = false;
			 armMoverTimer.start();
			 armMovingTime = 3; //how long it takes to open up for ball
		 } 
 
		 //Open arm when button 11 on the flightstick is pressed if arm is in the closed position and arms are not moving
		 if (flightStick.getRawButtonPressed(11) && isArmClosed && !isArmOpen){
			 arm8.set(.5 * 0.9);
			 arm9.set(.5 * 0.95)6
			 arm7.set(.5);
 
			 isArmClosed = false;
			 armMoverTimer.start();
			 armMovingTime = 1; //how long it takes to open up for plate
		 } 
 
		 //Close arms if button 7 is pressed on the flight stick if arms are open already and not moving
		 if (flightStick.getRawButtonPressed(7) && isArmOpen && !isArmClosed){
			 arm8.set(-.5 * 0.9);
			 arm9.set(-.5 * 0.95);
			 arm7.set(-.5);
 
			 isArmOpen = false;
			 armMoverTimer.start();
		 } 
 
		 
	 } else { */
 
		 if(flightStick.getRawButton(3))
		 {
			 arm8.set(.5 * 0.9 * flightArmPos);
			 arm7.set(.5 * 0.95 * flightArmPos);
 
			 if (flightArmPos <= 0) {
				 arm9.set(.65 * flightArmPos);
			 } else {
				 arm9.set(.5 * 0.95 * flightArmPos);
			 }
		 }
		 if(!flightStick.getRawButton(3))
		 {
			 arm8.set(0);
			 arm7.set(0);
			 if (flightArmPos <= 0) {
				 arm9.set(.65 * flightArmPos);
			 } else {
				 arm9.set(.5 * 0.95 * flightArmPos);
			 }
		 }
		 //}
		 //Move arms for one second then stop motors.  Direction is determiend by buttons 3 and 5.  Booleans to allow button inputs reset.
		 if (armMoverTimer.get() >= armMovingTime){
			 if(arm7.getSpeed() > 0 || arm8.getSpeed() > 0 || arm9.getSpeed() > 0)
				 isArmOpen = true;
			 else
				 isArmClosed = true;
 
			 arm8.set(0);
			 arm9.set(0);
			 arm7.set(0);
 
			 armMoverTimer.stop();
			 armMoverTimer.reset();
		 }
		 
		 m_drive.tankDrive(leftJoystickPosition, rightJoystickPosition);
		 
 
		 //if (grabMagEncoder.getDistance() >= -100) { 
			 //Limits based encoder or no limits based on if we get the encoder
		 /* } else {
			 forwardElevator.set(0);
		 } */
 
		 if ((flightStick.getPOV() > 315 || flightStick.getPOV() < 45) && flightStick.getPOV() != -1) {
			 forwardElevator.set(.5);
		 } else if (flightStick.getPOV() > 135 && flightStick.getPOV() < 225) {
			 forwardElevator.set(-.3);
		 } else if (flightStick.getPOV() == -1){
			 forwardElevator.set(0);
		 }
	 } 
  }

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {
	if(flightStick.getRawButtonPressed(6) && flightStick.getRawButton(4)){
	   forwardElevator.stopMotor();
	   verticalElevator.stopMotor();
	   wheelGrabber.stopMotor();
	   arm7.stopMotor();
	   arm8.stopMotor();
	   arm9.stopMotor();

	  } else {

	  //System.out.println("Set Value: " + desiredPosition);
	  //System.out.println("Encoder Value: " + elevOpEncoder.get());
	  System.out.println("Elevator Encoder Value: " + elevOpEncoder.getDistance());
	  System.out.println("Magnet Encoder Value: " + grabMagEncoder.getDistance());
	
	/*if (armMover.get() > 0) {
		position += motorCounter.getDistance();
	 } else {
		position -= motorCounter.getDistance();
	 }
	 motorCounter.reset(); */
	
	// 100 is not the final number

	/*if (Math.abs(position) >= 100){
		 armMover.set(0.0);
	 }*/
	 

	// NOTE: anything with the word "flight" in it is referring the flightstick, which is a big joystick.
    		
		double leftJoystickPosition = -0.65 * stick.getRawAxis(1);
		double rightJoystickPosition = -0.7 * stick.getRawAxis(5);

		double flightElevatorPos = flightStick.getRawAxis(1);
		double flightArmPos = flightStick.getRawAxis(2);

		//Enable manual mode when both button 6 on the flightsick and select button "button 8" on the controller are pressed
		/*if (flightStick.getRawButtonPressed(6)) {
			manualControl = !manualControl; //toggles manual and autonomous control
			System.out.println("Toggled manual control to " + manualControl);
		}*/

		if (stick.getRawButtonPressed(1)){ //A button
			superFast = !superFast;
		}

		if (superFast) {
			leftJoystickPosition = -.95 * stick.getRawAxis(1);
			rightJoystickPosition = -1 * stick.getRawAxis(1);
		}

		if (stick.getRawButtonPressed(2) && stick.getRawButton(4)){
			System.out.println("Reset encoder value to zero."); //Pressing x button will reset encoder value
			elevOpEncoder.reset();
		}

		/* if (manualControl) {
			//Move to down when button 12 is pressed on flightstick if the limit switch is not pressesd
			if ( limitSwitch.get() == false &&  flightStick.getRawButton(12) && elevOpEncoder.get() > -100) {
				verticalElevator.set(-.5);
			}
			
			if (flightStick.getRawButton(12) && elevOpEncoder.get() <= -100 && grabMagEncoder.get() >= 100) {
				verticalElevator.set(-.5);
			}
		
			//Move up when button 8 is pressed on flightstick and if it is not to high
			else if (elevOpEncoder.get() < ToHighPosition - 5 && flightStick.getRawButton(8)){
				verticalElevator.set(.5);
			}
		
			/Stop elevator when button 10 is pressed on flightstick
			else if (flightStick.getRawButton(10)){
				verticalElevator.set(0);
			} 
			else {
				verticalElevator.set(0);
			}
	
  } */
		/*if(Math.abs(flightElevatorPos) < 0.1) { 
			
			if (elevOpEncoder.getDistance() >= desiredPosition - .05 && elevOpEncoder.getDistance() <= desiredPosition + .05) {
				verticalElevator.set(0.0);
			
			}  else if(elevOpEncoder.getDistance() > desiredPosition) {
				verticalElevator.set(-.5);
			
			}else if(elevOpEncoder.getDistance() < desiredPosition) {
				 verticalElevator.set(.5);
			}

				//Move to middle bay height when button 10 is pressed on flightstick
			if (flightStick.getRawButtonPressed(10)){
				desiredPosition = 30;  //Update this number with correct number of revolutions
			}

				//Move to bottom bay height when button 12 is pressed on flightstick
			if (flightStick.getRawButtonPressed(12)){
				desiredPosition = 10;  //Update
			}

			//Move to top bay height when button 8 is pressed on flightstick
			if (flightStick.getRawButtonPressed(8)){
				desiredPosition = 60;  //Update this number once number of rotations is known
			}
			
		} 
  }*/
		 if (elevOpEncoder.getDistance() <= 0 && !flightStick.getRawButton(7))
		 {
			//elevator fine adjustment
			desiredPosition = elevOpEncoder.getDistance();
			verticalElevator.set(.75 * -flightElevatorPos);
			
		} else if (elevOpEncoder.getDistance() >= 0 && flightElevatorPos > 0) {
			verticalElevator.set(0.65 * -flightElevatorPos);
		}
		else if (flightStick.getRawButton(7))
		{
			desiredPosition = elevOpEncoder.getDistance();
			verticalElevator.set(.4 * -flightElevatorPos);
		} else {
			verticalElevator.set(0);
		}
		
	   
		//stops arm from going forward

		if (flightStick.getRawButton(2)){
			wheelGrabber.set(-.75);
		} else if (flightStick.getRawButton(1)){
			wheelGrabber.set(1);
		} else {
			wheelGrabber.set(0);
		}

		/* if (flightArmPos < .1) { //Runs this if the arm is not being controlled manually

		//Open arm when button 9 on the flightstick is pressed if arm is in the closed position and arms are not moving
		if (flightStick.getRawButtonPressed(9) && isArmClosed && !isArmOpen){
			arm8.set(.5 * 0.9);
			arm9.set(.5 * 0.95);
			arm7.set(.5);

			isArmClosed = false;
			armMoverTimer.start();
			armMovingTime = 3; //how long it takes to open up for ball
		} 

		//Open arm when button 11 on the flightstick is pressed if arm is in the closed position and arms are not moving
		if (flightStick.getRawButtonPressed(11) && isArmClosed && !isArmOpen){
			arm8.set(.5 * 0.9);
			arm9.set(.5 * 0.95)6
			arm7.set(.5);

			isArmClosed = false;
			armMoverTimer.start();
			armMovingTime = 1; //how long it takes to open up for plate
		} 

		//Close arms if button 7 is pressed on the flight stick if arms are open already and not moving
		if (flightStick.getRawButtonPressed(7) && isArmOpen && !isArmClosed){
			arm8.set(-.5 * 0.9);
			arm9.set(-.5 * 0.95);
			arm7.set(-.5);

			isArmOpen = false;
			armMoverTimer.start();
		} 

		
	} else { */

		if(flightStick.getRawButton(3))
		{
			arm8.set(.5 * 0.9 * flightArmPos);
			arm7.set(.5 * 0.95 * flightArmPos);

			if (flightArmPos <= 0) {
				arm9.set(.65 * flightArmPos);
			} else {
				arm9.set(.5 * 0.95 * flightArmPos);
			}
		}
		if(!flightStick.getRawButton(3))
		{
			arm8.set(0);
			arm7.set(0);
			if (flightArmPos <= 0) {
				arm9.set(.65 * flightArmPos);
			} else {
				arm9.set(.5 * 0.95 * flightArmPos);
			}
		}
		//}
		//Move arms for one second then stop motors.  Direction is determiend by buttons 3 and 5.  Booleans to allow button inputs reset.
		if (armMoverTimer.get() >= armMovingTime){
			if(arm7.getSpeed() > 0 || arm8.getSpeed() > 0 || arm9.getSpeed() > 0)
				isArmOpen = true;
			else
				isArmClosed = true;

			arm8.set(0);
			arm9.set(0);
			arm7.set(0);

			armMoverTimer.stop();
			armMoverTimer.reset();
		}
		
		m_drive.tankDrive(leftJoystickPosition, rightJoystickPosition);
		

		//if (grabMagEncoder.getDistance() >= -100) { 
			//Limits based encoder or no limits based on if we get the encoder
		/* } else {
			forwardElevator.set(0);
		} */

		if ((flightStick.getPOV() > 315 || flightStick.getPOV() < 45) && flightStick.getPOV() != -1) {
			forwardElevator.set(.5);
		} else if (flightStick.getPOV() > 135 && flightStick.getPOV() < 225) {
			forwardElevator.set(-.3);
		} else if (flightStick.getPOV() == -1){
			forwardElevator.set(0);
		}
	}
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }
}

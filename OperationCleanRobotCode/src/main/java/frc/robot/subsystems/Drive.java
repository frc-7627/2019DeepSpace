package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Spark;

public class Drive {
    private Spark leftMotor1;
    private Spark leftMotor3;

    private Spark rightMotor2;
    private Spark rightMotor4;

    public Drive (Spark leftMotor1, Spark rightMotor2) {
        //Initializes for 2 wheel drive

        this.leftMotor1 = leftMotor1;
        this.rightMotor2 = rightMotor2;
    }

    public Drive(Spark leftMotor1, Spark rightMotor2, Spark leftMotor3, Spark rightMotor4) {
        //Initializes for 4 wheel drive

        this.leftMotor1 = leftMotor1;
        this.leftMotor3 = leftMotor3;

        this.rightMotor2 = rightMotor2;
        this.rightMotor4 = rightMotor4;
    }

    public void tankDrive(double leftValue, double rightValue, boolean superFast) {
        if (superFast) {
            leftMotor1.set(leftValue * -.95);
            rightMotor2.set(rightValue * -1);

            if (leftMotor3 != null && rightMotor4 != null) {
                leftMotor3.set(leftValue * -.95);
                rightMotor4.set(rightValue * -1);
            }
        } else {
            leftMotor1.set(leftValue * -.65);
            rightMotor2.set(rightValue * -.7);

            if (leftMotor3 != null && rightMotor4 != null) {
                leftMotor3.set(leftValue * -.65);
                rightMotor4.set(rightValue * -.7);
            }
        }
    }

    public void shutdown() {
        leftMotor1.stopMotor();
        rightMotor2.stopMotor();

        if (leftMotor3 != null && rightMotor4 != null) {
            leftMotor3.stopMotor();
            rightMotor4.stopMotor();
        }
        System.out.println("All motors have shut down.");
    }
}
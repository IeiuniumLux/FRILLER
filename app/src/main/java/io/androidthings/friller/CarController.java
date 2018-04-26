package io.androidthings.friller;

import android.util.Log;

import com.google.android.things.contrib.driver.motorhat.MotorHat;

import java.io.IOException;


public class CarController {

    private static final String TAG = CarController.class.getSimpleName();

    public static final byte GO_FORWARD = 0;
    public static final byte TURN_LEFT = 1;
    public static final byte TURN_RIGHT = 2;
    public static final byte GO_BACK = 3;
    public static final byte STOP = 4;
    public static final byte POP_UP = 5;
    public static final byte RETRACT = 6;


    private static final int LEFT_MOTOR = 1;
    private static final int RIGHT_MOTOR = 3;

    private static final int BACK_MOTOR = 0;
    private static final int FRONT_MOTOR = 2;

    private static final int[] DRIVE_MOTORS = {LEFT_MOTOR, RIGHT_MOTOR};
    private static final int[] SPIKE_MOTORS = {BACK_MOTOR, FRONT_MOTOR};
    private static final int[] ALL_MOTORS = {BACK_MOTOR, LEFT_MOTOR, FRONT_MOTOR, RIGHT_MOTOR};

    private static final int SPEED_NORMAL = 100;
    private static final int SPEED_TRANSFORM = 255;
    private static final int SPEED_TURNING_INSIDE = 70;
    private static final int SPEED_TURNING_OUTSIDE = 250;

    private MotorHat mMotorHat;

    public CarController(MotorHat motorHat) {
        mMotorHat = motorHat;
    }

    public void shutDown() {
        stop();
    }

    // Motor controls

    public boolean onCarCommand(int command) {
        switch (command) {
            case GO_FORWARD:
                return goForward();
            case GO_BACK:
                return goBackward();
            case STOP:
                return stop();
            case TURN_LEFT:
                return turnLeft();
            case TURN_RIGHT:
                return turnRight();
            case POP_UP:
                return popUp();
            case RETRACT:
                return retract();
        }
        return false;
    }

    private boolean popUp() {
        return setSpeed(SPEED_TRANSFORM, SPIKE_MOTORS) && setMotorState(MotorHat.MOTOR_STATE_CW, SPIKE_MOTORS);
    }

    private boolean retract() {
        return setSpeed(SPEED_TRANSFORM, SPIKE_MOTORS) && setMotorState(MotorHat.MOTOR_STATE_CCW, SPIKE_MOTORS);
    }

    private boolean goForward() {
        return setSpeed(SPEED_NORMAL, DRIVE_MOTORS) && setMotorState(MotorHat.MOTOR_STATE_CCW, DRIVE_MOTORS);
    }

    private boolean goBackward() {
        return setSpeed(SPEED_NORMAL, DRIVE_MOTORS) && setMotorState(MotorHat.MOTOR_STATE_CW, DRIVE_MOTORS);
    }

    private boolean stop() {
        return setMotorState(MotorHat.MOTOR_STATE_RELEASE, ALL_MOTORS);
    }

    private boolean turnLeft() {
        return turn(LEFT_MOTOR, RIGHT_MOTOR);
    }

    private boolean turnRight() {
        return turn(RIGHT_MOTOR, LEFT_MOTOR);
    }

    private boolean setMotorState(int state, int... motors) {
        try {
            if (motors != null && motors.length > 0) {
                for (int motor : motors) {
                    mMotorHat.setMotorState(motor, state);
                }
            }
            return true;
        } catch (IOException e) {
            Log.e(TAG, "Error setting motor state", e);
            return false;
        }
    }

    private boolean turn(int insideMotor, int outsideMotor) {
        try {
            setMotorState(MotorHat.MOTOR_STATE_CW, DRIVE_MOTORS);
            mMotorHat.setMotorSpeed(insideMotor, SPEED_TURNING_INSIDE);
            mMotorHat.setMotorSpeed(outsideMotor, SPEED_TURNING_OUTSIDE);
            return true;
        } catch (IOException e) {
            Log.e(TAG, "Error setting motor state", e);
            return false;
        }
    }

    private boolean setSpeed(int speed, int... motors) {
        try {
            if (motors != null && motors.length > 0) {
                for (int motor : motors) {
                    mMotorHat.setMotorSpeed(motor, speed);
                }
            }
            return true;
        } catch (IOException e) {
            Log.e(TAG, "Error setting speed", e);
            return false;
        }
    }

    public void setWheelSpeed(int leftMotorSpeed, int rightMotorSpeed, int state) {
        try {
            mMotorHat.setMotorState(LEFT_MOTOR, state);
            mMotorHat.setMotorState(RIGHT_MOTOR, state);
            mMotorHat.setMotorSpeed(LEFT_MOTOR, leftMotorSpeed);
            mMotorHat.setMotorSpeed(RIGHT_MOTOR, rightMotorSpeed);
        } catch (IOException e) {
            Log.e(TAG, "Right motor error", e);
        }
    }
}
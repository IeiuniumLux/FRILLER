package io.androidthings.friller;

import io.androidthings.friller.UDPServer.UDPListener;

import android.app.Activity;
import android.os.Bundle;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.nio.ByteBuffer;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.KeyEvent;

import com.google.android.things.contrib.driver.motorhat.MotorHat;


public class MainActivity extends Activity implements UDPListener {
    private static final String TAG = MainActivity.class.getSimpleName();

    public static final String I2C_DEVICE_NAME = "I2C1";
    public static final int UDP_PORT = 8000;

    private MotorHat mMotorHat;
    private CarController mCarController;

    private UDPServer mUDPServer = null;

    private static boolean mIsOpen = false;

    private float mY = 0.0f;
    private float mX = 0.0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            mMotorHat = new MotorHat(I2C_DEVICE_NAME);
        } catch (IOException e) {
            throw new RuntimeException("Failed to create MotorHat", e);
        }
        mCarController = new CarController(mMotorHat);

        mUDPServer = new UDPServer(UDP_PORT, this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mUDPServer != null)
            mUDPServer.abort();

        if (mCarController != null) {
            mCarController.shutDown();
        }

        if (mMotorHat != null) {
            try {
                mMotorHat.close();
            } catch (IOException e) {
                Log.e(TAG, "Error closing MotorHat", e);
            } finally {
                mMotorHat = null;
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return handleKeyCode(keyCode) || super.onKeyDown(keyCode, event);
    }

    // For testing commands via adb shell inputs
    private boolean handleKeyCode(int keyCode) {
        if (mCarController != null) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_F: //34
                    mCarController.onCarCommand(CarController.GO_FORWARD);
                    Log.d(TAG, "GO_FORWARD");
                    return true;
                case KeyEvent.KEYCODE_B: //30
                    mCarController.onCarCommand(CarController.GO_BACK);
                    Log.d(TAG, "GO_BACK");
                    return true;
                case KeyEvent.KEYCODE_L: //40
                    mCarController.onCarCommand(CarController.TURN_LEFT);
                    Log.d(TAG, "TURN_LEFT");
                    return true;
                case KeyEvent.KEYCODE_R: //46
                    mCarController.onCarCommand(CarController.TURN_RIGHT);
                    Log.d(TAG, "TURN_RIGHT");
                    return true;
                case KeyEvent.KEYCODE_S: //47
                    mCarController.onCarCommand(CarController.STOP);
                    Log.d(TAG, "STOP");
                    return true;
                case KeyEvent.KEYCODE_O: //43
                    if (mIsOpen) return false;
                    mCarController.onCarCommand(CarController.POP_UP);
                    Log.d(TAG, "OPEN");
                    new CountDownTimer(320, 1000) {
                        public void onTick(long millisUntilFinished) {
//                            try {
//                                if (mFrontSwitch.getValue() || mBackSwitch.getValue()) {
//                                    Log.d(TAG, "TRUE");
//                                }
//                            } catch (IOException e) {
//                                Log.e(TAG, "Error reading GPIO");
//                            }
                        }

                        public void onFinish() {
                            mCarController.onCarCommand(CarController.STOP);
                            mIsOpen = true;
                            Log.d(TAG, "STOP");
                        }
                    }.start();
                    return true;
                case KeyEvent.KEYCODE_C: //31
                    if (!mIsOpen) return false;
                    mCarController.onCarCommand(CarController.RETRACT);
                    Log.d(TAG, "CLOSE");
                    new CountDownTimer(320, 1000) {
                        public void onTick(long millisUntilFinished) {
                        }

                        public void onFinish() {
                            mCarController.onCarCommand(CarController.STOP);
                            mIsOpen = false;
                            Log.d(TAG, "STOP");
                        }
                    }.start();
                    return true;
            }
        }
        return false;
    }

    @Override
    public void onPacketReceived(DatagramPacket packet) {
        int _inByteIndex = -1; 		// in-coming bytes counter
        char oscControl; 			// control in TouchOSC sending the message
        int[] oscMsg = new int[11]; // buffer for incoming OSC packet

        try {
            InputStream inputStream = new ByteArrayInputStream(packet.getData(), 0, packet.getLength());
            int inByte = inputStream.read();

            // An OSC address pattern is a string beginning with the character forward slash '/'
            if (inByte == 47) {
                _inByteIndex = 0; // a new message received so set array index to 0
                inByte = inputStream.read();
            }
            // Decimal ASCII values for T = 84 | S = 83 | B = 66 | C = 67
            if (_inByteIndex == 0 && (inByte == 84 || inByte == 83 || inByte == 66 || inByte == 67)) {
                switch (inByte) {
                    case (84): // Throttle
                        oscControl = 'T';
                        break;
                    case (83): // Steering
                        oscControl = 'S';
                        break;
                    case (66): // Button
                        oscControl = 'B';
                        break;
                    case (67): // Change
                        oscControl = 'C';
                        break;
                    default:
                        oscControl = ' ';
                }

                for (; _inByteIndex < 10; _inByteIndex++) {
                    inByte = inputStream.read();
                    oscMsg[_inByteIndex] = inByte; // add the byte to the array
                }

                if (_inByteIndex == 10) { // end of the OSC message
                    byte[] byte_array = new byte[4];
                    byte_array[0] = (byte) oscMsg[9]; // reverse bytes order to decode message
                    byte_array[1] = (byte) oscMsg[8];
                    byte_array[2] = (byte) oscMsg[7];
                    byte_array[3] = (byte) oscMsg[6];
                    ByteBuffer byteBuffer = ByteBuffer.allocate(byte_array.length);
                    byteBuffer.put(byte_array);

                    float value = getOSCValue(byteBuffer.array());

                    switch (oscControl) {
                        case ('T'): // Throttle
                            mY = value;
                            differentialDrive(mX, mY);
                            break;
                        case ('S'): // Steering
                            mX = 0 - value;
                            differentialDrive(mX, mY);
                            break;
                        case ('B'): // Stop Button
                            mCarController.onCarCommand(CarController.STOP);
                            break;
                        case ('C'): // Change
                            Log.d(TAG, String.valueOf(value));
                            if (mIsOpen && value < 1) {
                                mCarController.onCarCommand(CarController.RETRACT);
                                Log.d(TAG, "CLOSE");
                            } else if (!mIsOpen && value > 0) {
                                mCarController.onCarCommand(CarController.POP_UP);
                                Log.d(TAG, "OPEN");
                            } else {
                                break;
                            }
                            this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    new CountDownTimer(320, 1000) {
                                        public void onTick(long millisUntilFinished) {

                                        }

                                        public void onFinish() {
                                            mCarController.onCarCommand(CarController.STOP);
                                            mIsOpen = (mIsOpen) ? false : true;
                                            Log.d(TAG, "STOP");
                                        }
                                    }.start();
                                }
                            });

                            break;
                    }
                }
            }
        } catch (IOException e) {
            Log.e("onPacketReceived()", e.getMessage());
        }
    }

    private float getOSCValue(byte[] byte_array_4) {
        int ret = 0;
        for (int i = 0; i < 4; i++) {
            int b = (int) byte_array_4[i];
            if (i < 3 && b < 0) {
                b = 256 + b;
            }
            ret += b << (i * 8);
        }
        return Float.intBitsToFloat(ret);
    }

    private void differentialDrive(float x, float y) {
        float V = (255-Math.abs(x)) * (y/255) + y;
        float W = (255-Math.abs(y)) * (x/255) + x;
        float leftMotor = (V - W) / 2;
        float rightMotor = (V + W) / 2;
        if (y >= 0) {
            mCarController.setWheelSpeed((int)leftMotor, (int)rightMotor, MotorHat.MOTOR_STATE_CCW);
        } else {
            mCarController.setWheelSpeed((int)Math.abs(leftMotor), (int)Math.abs(rightMotor), MotorHat.MOTOR_STATE_CW);
        }
    }
}
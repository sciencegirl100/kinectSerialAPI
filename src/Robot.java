import java.io.UnsupportedEncodingException;
import java.util.Date;
import edu.ufl.digitalworlds.j4k.J4KSDK;
import edu.ufl.digitalworlds.j4k.Skeleton;
import jssc.*;

public class Robot extends J4KSDK {
    public final static int SPINE_BASE=0; 
    public final static int SPINE_MID=1; 
    public final static int NECK=2; 
    public final static int HEAD=3; 
    public final static int SHOULDER_LEFT=4; 
    public final static int ELBOW_LEFT=5; 
    public final static int WRIST_LEFT=6; 
    public final static int HAND_LEFT=7; 
    public final static int SHOULDER_RIGHT=8; 
    public final static int ELBOW_RIGHT=9; 
    public final static int WRIST_RIGHT=10; 
    public final static int HAND_RIGHT=11; 
    public final static int HIP_LEFT=12; 
    public final static int KNEE_LEFT=13; 
    public final static int ANKLE_LEFT=14; 
    public final static int FOOT_LEFT=15; 
    public final static int HIP_RIGHT=16; 
    public final static int KNEE_RIGHT=17; 
    public final static int ANKLE_RIGHT=18; 
    public final static int FOOT_RIGHT=19; 
    public final static int SPINE_SHOULDER=20; 
    public final static int HAND_TIP_LEFT=21; 
    public final static int THUMB_LEFT=22; 
    public final static int HAND_TIP_RIGHT=23; 
    public final static int THUMB_RIGHT=24; 
    public final static int JOINT_COUNT=25; 
	int counter=0;
	long time=0;
	public static SerialPort serialPort = new SerialPort("COM3");
	int realAngle = 0;
	
	@Override
	public void onSkeletonFrameEvent(boolean[] skeleton_tracked, float[] positions, float[] orientations, byte[] joint_status){
		float[] elbowPoint = new float[2];
		float[] wristPoint = new float[2];
		float angle;
		System.out.println("A new skeleton frame was received.");
		Skeleton[] skeletons = new Skeleton[getSkeletonCountLimit()];
		skeletons = getSkeletons(); 
		//System.out.println("X: " + skeletons[0].get3DJointX(ELBOW_RIGHT) + " Y: " + skeletons[0].get3DJointY(ELBOW_RIGHT));
		//System.out.println("X: " + skeletons[0].get3DJointX(WRIST_RIGHT) + " Y: " + skeletons[0].get3DJointY(WRIST_RIGHT));
		elbowPoint[0] = skeletons[0].get3DJointX(ELBOW_RIGHT);
		elbowPoint[1] = skeletons[0].get3DJointY(ELBOW_RIGHT);
		wristPoint[0] = skeletons[0].get3DJointX(WRIST_RIGHT);
		wristPoint[1] = skeletons[0].get3DJointY(WRIST_RIGHT);
		angle = (float)Math.atan((elbowPoint[1] - wristPoint[1])/(elbowPoint[0] - wristPoint[0]));
		System.out.println("ANGLE Skele0: " + (angle/1.5)*100);
		realAngle = (int)((angle/1.5)*100)+90; //this goes from 0 to 180
		realAngle-=15;
		System.out.println("\t" + (realAngle) + "\t" + (angle/1.5)*100);
		if (realAngle >= 0 && realAngle <= 179) {
			try {
				serialPort.writeString((char)realAngle + "");
			} catch (SerialPortException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void onDepthFrameEvent(short[] depth_frame, byte[] body_index, float[] xyz, float[] uv) {
		System.out.println("A new depth frame was received.");
		if(counter==0) {
			time=new Date().getTime();
		}
		counter+=1;
	}
	
	public static void main(String[] args) {
		if(System.getProperty("os.arch").toLowerCase().indexOf("64")<0){
			System.out.println("WARNING: You are running a 32bit version of Java.");
			System.out.println("This may reduce significantly the performance of this application.");
			System.out.println("It is strongly adviced to exit this program and install a 64bit version of Java.\n");
		}
		try {
		    serialPort.openPort();
		    serialPort.setParams(SerialPort.BAUDRATE_115200,
		                         SerialPort.DATABITS_8,
		                         SerialPort.STOPBITS_1,
		                         SerialPort.PARITY_NONE);
		    serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_IN | 
		                                  SerialPort.FLOWCONTROL_RTSCTS_OUT);
		    System.out.println("Serial Open");
		    serialPort.writeString("\n", "US-ASCII");
		    System.out.println("Wrote Serial");
		} catch (SerialPortException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("ERR");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("ERR");
		}
		System.out.println("This program will run for about 20 seconds.");
		Robot kinect=new Robot();
		kinect.start(J4KSDK.SKELETON);
		try {Thread.sleep(200000);} catch (InterruptedException e) {}
		kinect.stop();		
		System.out.println("FPS: "+kinect.counter*1000.0/(new Date().getTime()-kinect.time));
	}

	@Override
	public void onColorFrameEvent(byte[] arg0) {
		// TODO Auto-generated method stub
		
	}	
}

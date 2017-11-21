import java.util.Date;
import edu.ufl.digitalworlds.j4k.J4KSDK;
import edu.ufl.digitalworlds.j4k.Skeleton;
import javax.comm.*;
import java.util.*;

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
	int realAngle = 0;
	int oldAngle = 0;
	String PortName = "COM3";
	Enumeration portIdentifiers = CommPortIdentifier.getPortIdentifiers();
	CommPortIdentifier portId = null;
	 while (portIdentifiers.hasMoreElements()){
	     CommPortIdentifier pid = (CommPortIdentifier) portIdentifiers.nextElement();
	     if(pid.getPortType() == CommPortIdentifier.PORT_SERIAL &&
	        pid.getName().equals(PortName)){
	         portId = pid;
	         break;
	     }
	 }
	 if(portId == null){
	     System.err.println("Could not find serial port " + PortName);
	     System.exit(1);
	 }
	 
	@Override
	public void onSkeletonFrameEvent(boolean[] skeleton_tracked, float[] positions, float[] orientations, byte[] joint_status){
		float[] elbowPoint = new float[2];
		float[] wristPoint = new float[2];
		float angle;
		System.out.println("A new skeleton frame was received.");
		Skeleton[] skeletons = new Skeleton[getSkeletonCountLimit()];
		skeletons = getSkeletons(); 
		System.out.println("X: " + skeletons[0].get3DJointX(ELBOW_RIGHT) + " Y: " + skeletons[0].get3DJointY(ELBOW_RIGHT));
		System.out.println("X: " + skeletons[0].get3DJointX(WRIST_RIGHT) + " Y: " + skeletons[0].get3DJointY(WRIST_RIGHT));
		elbowPoint[0] = skeletons[0].get3DJointX(ELBOW_RIGHT);
		elbowPoint[1] = skeletons[0].get3DJointY(ELBOW_RIGHT);
		wristPoint[0] = skeletons[0].get3DJointX(WRIST_RIGHT);
		wristPoint[1] = skeletons[0].get3DJointY(WRIST_RIGHT);
		angle = (float)Math.atan((elbowPoint[1] - wristPoint[1])/(elbowPoint[0] - wristPoint[0]));
		System.out.println("ANGLE Skele0: " + (angle/1.5)*100);
		elbowPoint[0] = skeletons[1].get3DJointX(ELBOW_RIGHT);
		elbowPoint[1] = skeletons[1].get3DJointY(ELBOW_RIGHT);
		wristPoint[0] = skeletons[1].get3DJointX(WRIST_RIGHT);
		wristPoint[1] = skeletons[1].get3DJointY(WRIST_RIGHT);
		angle = (float)Math.atan((elbowPoint[1] - wristPoint[1])/(elbowPoint[0] - wristPoint[0]));
		realAngle = (int)((angle/1.5)*100);
		int difference = realAngle - oldAngle;
		if((difference > 5) || (difference < -5)){
			int moveMotorThisWay;
			int moveMotorThisManyDegrees;
			oldAngle = realAngle;
			if (difference > 0){
				moveMotorThisWay = 1;
				moveMotorThisManyDegrees = difference;
			}else{
				moveMotorThisWay = 0;
				moveMotorThisManyDegrees = difference*-1;
			}
			System.out.println(moveMotorThisWay + " " + moveMotorThisManyDegrees);
			
		}
	}

	@Override
	public void onColorFrameEvent(byte[] color_frame) {
		System.out.println("A new color frame was received.");
	}
	
	@Override
	public void onDepthFrameEvent(short[] depth_frame, byte[] body_index, float[] xyz, float[] uv) {
		System.out.println("A new depth frame was received.");
		if(counter==0)
			time=new Date().getTime();
		counter+=1;
	}
	
	public static void main(String[] args) {
		if(System.getProperty("os.arch").toLowerCase().indexOf("64")<0){
			System.out.println("WARNING: You are running a 32bit version of Java.");
			System.out.println("This may reduce significantly the performance of this application.");
			System.out.println("It is strongly adviced to exit this program and install a 64bit version of Java.\n");
		}
		System.out.println("This program will run for about 20 seconds.");
		Robot kinect=new Robot();
		kinect.start(J4KSDK.SKELETON);
		try {Thread.sleep(20000);} catch (InterruptedException e) {}
		kinect.stop();		
		System.out.println("FPS: "+kinect.counter*1000.0/(new Date().getTime()-kinect.time));
	}	
}

package alpay.com.codenotesinteractive.simulation;

import alpay.com.codenotesinteractive.R;

public class SimulationParameters {

    public static boolean showTapTarget = true;

    public static final String SIMULATION1_NAME = "Inclined Plane";
    public static final String SIMULATION2_NAME = "Constant Acceleration";
    public static final String SIMULATION3_NAME = "Ohm's Law";
    public static final String SIMULATION4_NAME = "Pulley System";
    public static final String SIMULATION5_NAME = "Lever System";

    public static final String SIMULATION1_DETAIL = "Motion on an inclined plane with constant velocity and the corresponding forces. " +
            "Depending on the selected radio button the app will show a springscale from which you can read the necessary force, " +
            "or the vectors of the weight force with its two components (parallel and normal to the plane), " +
            "the normal force, the frictional force and the force which is necessary for the motion.";

    public static final String SIMULATION2_DETAIL = "A car moving with constant acceleration. " +
            "You can vary the values of initial position, initial velocity and acceleration. " +
            "Three digital clocks indicate the elapsed time since the start. " +
            "As soon as the car has reached the green or red barrier with its front bumper, the corresponding clock will stop. " +
            "Both barriers can be dragged left or right.";

    public static final String SIMULATION3_DETAIL = "Simple circuit containing one resistor. " +
            "In addition there is a voltmeter (parallel to the resistor) and an ammeter (in series with the resistor).";

    public static final String SIMULATION4_DETAIL = "You can raise or lower the load." +
            "You can change the weight of the load and the hanging pulley(s)" +
            "Inputs higher than the spring scale limit (10 N) are automatically changed.";

    public static final String SIMULATION5_DETAIL = "Symmetrical lever with some mass pieces each of which has a weight of 1.0 N.";

    public static final int INCLINED_PLANE_SIMULATION = 1001;
    public static final int CONSTANT_ACCELERATION_SIMULATION = 1002;
    public static final int OHMS_LAW_SIMULATION = 1003;
    public static final int PULLEY_SIMULATION = 1004;
    public static final int LEVER_SIMULATION = 1005;

    public static final int EXPERIMENT = 50;
    public static final int ANGLE = 51;
    public static final int FRICTION = 52;
    public static final int WEIGHT = 53;
    public static final int POSITION = 54;
    public static final int VELOCITY = 55;
    public static final int ACCELERATION = 56;
    public static final int PULLEY_WEIGHT = 57;

    public static double CONSTANT_ACCELERATION_SCREEN_SIZE = 520;
    public static double PULLEY_SCREEN_SIZE = 620;
    public static double INCLINED_PLANE_SCREEN_SIZE = 650;
    public static double OHMS_LAW_SCREEN_SIZE = 650;
    public static double LEVER_SCREEN_SIZE = 540;

    public static int INCLINED_PLANE_IMAGE = R.drawable.inclined_plane;
    public static int CONSTANT_ACCELERATION_IMAGE = R.drawable.constant_acceleration;
    public static int OHMS_LAW_IMAGE = R.drawable.ohms_law;
    public static int PULLEY_IMAGE = R.drawable.pulley;
    public static int LEVER_IMAGE = R.drawable.lever;

    public static int[] images = {INCLINED_PLANE_IMAGE, CONSTANT_ACCELERATION_IMAGE, OHMS_LAW_IMAGE, PULLEY_IMAGE, LEVER_IMAGE};

    public static String[] CONSTANT_ACCELERATION_PARAMETER_TEXTS = {"Position", "Velocity", "Acceleration"};
    public static String[] INCLINED_PLANE_PARAMETER_TEXTS = {"Angle", "Weight", "Friction"};
    public static String[] PULLEY_PARAMETER_TEXTS = {"Weight", "Pulley Weight"};

}

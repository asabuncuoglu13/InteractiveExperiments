package alpay.com.codenotesinteractive.simulation;

import alpay.com.codenotesinteractive.R;

public class SimulationParameters {
    public static final int INCLINED_PLANE_SIMULATION = 1001;
    public static final int CONSTANT_ACCELERATION_SIMULATION = 1002;
    public static final int OHMS_LAW_SIMULATION = 1003;
    public static final int ANGLE = 51;
    public static final int FRICTION = 52;
    public static final int WEIGHT = 53;
    public static final int POSITION = 54;
    public static final int VELOCITY = 55;
    public static final int ACCELERATION = 56;
    public static final int AMPER = 54;
    public static final int VOLT = 55;
    public static final int RESISTANCE = 56;

    public static double CONSTANT_ACCELERATION_SCREEN_SIZE = 520;
    public static double INCLINED_PLANE_SCREEN_SIZE = 650;
    public static double OHMS_LAW_SCREEN_SIZE = 650;

    public static int INCLINED_PLANE_IMAGE = R.drawable.inclined_plane;
    public static int CONSTANT_ACCELERATION_IMAGE = R.drawable.constant_acceleration;
    public static int OHMS_LAW_IMAGE = R.drawable.ohms_law;

    public static int[] images = {INCLINED_PLANE_IMAGE, CONSTANT_ACCELERATION_IMAGE, OHMS_LAW_IMAGE};

    public static String[] CONSTANT_ACCELERATION_PARAMETER_TEXTS = {"Position", "Velocity", "Acceleration"};

}

package alpay.com.codenotesinteractive.simulation;

import android.content.Context;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Simulation {

    public static final List<SimulationItem> ITEMS = new ArrayList<SimulationItem>();
    public static final Map<String, SimulationItem> ITEM_MAP = new HashMap<String, SimulationItem>();
    private static final String TAG = "Simulation";

    static {
        // Add some sample items.
        addItem(new SimulationItem(String.valueOf(SimulationParameters.INCLINED_PLANE_SIMULATION),
                SimulationParameters.SIMULATION1_NAME, SimulationParameters.SIMULATION1_DETAIL));
        addItem(new SimulationItem(String.valueOf(SimulationParameters.CONSTANT_ACCELERATION_SIMULATION),
                SimulationParameters.SIMULATION2_NAME, SimulationParameters.SIMULATION2_DETAIL));
        addItem(new SimulationItem(String.valueOf(SimulationParameters.OHMS_LAW_SIMULATION),
                SimulationParameters.SIMULATION3_NAME, SimulationParameters.SIMULATION3_DETAIL));
        addItem(new SimulationItem(String.valueOf(SimulationParameters.PULLEY_SIMULATION),
                SimulationParameters.SIMULATION4_NAME, SimulationParameters.SIMULATION4_DETAIL));
        addItem(new SimulationItem(String.valueOf(SimulationParameters.LEVER_SIMULATION),
                SimulationParameters.SIMULATION5_NAME, SimulationParameters.SIMULATION5_DETAIL));
        addItem(new SimulationItem(String.valueOf(SimulationParameters.INCLINED_CANVAS_SIMULATION),
                SimulationParameters.SIMULATION6_NAME, SimulationParameters.SIMULATION6_DETAIL));
    }

    private static void addItem(SimulationItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static int getSimulationID(double[] params) {
        int simulationID = -1;
        for (int i = 0; i < params.length - 1; i++) {
            if (params[i] == SimulationParameters.EXPERIMENT) {
                simulationID = (int) params[i + 1] + 1000;
            }
        }
        return simulationID;
    }

    public static class SimulationItem {
        public final String id;
        public Drawable image;
        public final String content;
        public final String details;

        public SimulationItem(String id, String content, String details) {
            this.id = id;
            this.content = content;
            this.details = details;
        }

        public void setImageID(Context context, int ID) {
            this.image = context.getResources().getDrawable(ID);
        }

        @Override
        public String toString() {
            return content;
        }
    }
}

package alpay.com.codenotesinteractive.simulation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Simulation {

    public static final List<SimulationItem> ITEMS = new ArrayList<SimulationItem>();
    public static final Map<String, SimulationItem> ITEM_MAP = new HashMap<String, SimulationItem>();

    static {
        // Add some sample items.
        addItem(new SimulationItem(String.valueOf(SimulationParameters.INCLINED_PLANE_SIMULATION), "Inclined Plane", "Experiment about Inclined Plane"));
        addItem(new SimulationItem(String.valueOf(SimulationParameters.CONSTANT_ACCELERATION_SIMULATION), "Constant Acceleration", "Experiment about Constant Acceleration"));
    }

    private static void addItem(SimulationItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class SimulationItem {
        public final String id;
        public final String content;
        public final String details;

        public SimulationItem(String id, String content, String details) {
            this.id = id;
            this.content = content;
            this.details = details;
        }

        @Override
        public String toString() {
            return content;
        }
    }
}

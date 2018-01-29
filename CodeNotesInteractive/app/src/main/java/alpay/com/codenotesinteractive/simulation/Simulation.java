package alpay.com.codenotesinteractive.simulation;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
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
        addItem(new SimulationItem(String.valueOf(SimulationParameters.INCLINED_PLANE_SIMULATION),"Inclined Plane", "Experiment about Inclined Plane"));
        addItem(new SimulationItem(String.valueOf(SimulationParameters.CONSTANT_ACCELERATION_SIMULATION), "Constant Acceleration", "Experiment about Constant"));
        addItem(new SimulationItem(String.valueOf(SimulationParameters.OHMS_LAW_SIMULATION), "Ohm's Law", "Experiment about Ohm's Law"));
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

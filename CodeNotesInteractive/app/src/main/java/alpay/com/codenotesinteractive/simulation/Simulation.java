package alpay.com.codenotesinteractive.simulation;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import alpay.com.codenotesinteractive.R;
import alpay.com.codenotesinteractive.simulation.simulation_fragments.ConstantAccelerationSimulationFragment;
import alpay.com.codenotesinteractive.simulation.simulation_fragments.InclinedPlaneSimulationFragment;
import alpay.com.codenotesinteractive.simulation.simulation_fragments.OhmsLawSimulationFragment;
import alpay.com.codenotesinteractive.simulation.simulation_fragments.PulleySimulationFragment;
import alpay.com.codenotesinteractive.simulation.simulation_fragments.SimulationListFragment;


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
    }

    private static void addItem(SimulationItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    public static void callSimulationFragment(FragmentActivity fragmentActivity, int simulationID, double[] parameters)
    {
        InclinedPlaneSimulationFragment inclinedPlaneSimulationFragment;
        ConstantAccelerationSimulationFragment constantAccelerationSimulationFragment;
        OhmsLawSimulationFragment ohmsLawSimulationFragment;
        SimulationListFragment simulationListFragment;
        PulleySimulationFragment pulleySimulationFragment;
        FragmentTransaction ft = fragmentActivity.getSupportFragmentManager().beginTransaction();
        if(simulationID >0)
        {
            if(parameters != null)
            {
                if (simulationID == SimulationParameters.INCLINED_PLANE_SIMULATION) {
                    inclinedPlaneSimulationFragment = new InclinedPlaneSimulationFragment();
                    inclinedPlaneSimulationFragment.setParameters(parameters);
                    ft.replace(R.id.fragment_container, inclinedPlaneSimulationFragment);
                } else if (simulationID == SimulationParameters.CONSTANT_ACCELERATION_SIMULATION) {
                    constantAccelerationSimulationFragment = new ConstantAccelerationSimulationFragment();
                    constantAccelerationSimulationFragment.setParameters(parameters);
                    ft.replace(R.id.fragment_container, constantAccelerationSimulationFragment);
                }else if (simulationID == SimulationParameters.OHMS_LAW_SIMULATION) {
                    ohmsLawSimulationFragment = new OhmsLawSimulationFragment();
                    ft.replace(R.id.fragment_container, ohmsLawSimulationFragment);
                }
                else if (simulationID == SimulationParameters.PULLEY_SIMULATION) {
                    pulleySimulationFragment = new PulleySimulationFragment();
                    pulleySimulationFragment.setParameters(parameters);
                    ft.replace(R.id.fragment_container, pulleySimulationFragment);
                }
            }else
            {
                if (simulationID == SimulationParameters.INCLINED_PLANE_SIMULATION) {
                    inclinedPlaneSimulationFragment = new InclinedPlaneSimulationFragment();
                    ft.replace(R.id.fragment_container, inclinedPlaneSimulationFragment);
                } else if (simulationID == SimulationParameters.CONSTANT_ACCELERATION_SIMULATION) {
                    constantAccelerationSimulationFragment = new ConstantAccelerationSimulationFragment();
                    ft.replace(R.id.fragment_container, constantAccelerationSimulationFragment);
                }else if (simulationID == SimulationParameters.OHMS_LAW_SIMULATION) {
                    ohmsLawSimulationFragment = new OhmsLawSimulationFragment();
                    ft.replace(R.id.fragment_container, ohmsLawSimulationFragment);
                }
                else if (simulationID == SimulationParameters.PULLEY_SIMULATION) {
                    pulleySimulationFragment = new PulleySimulationFragment();
                    ft.replace(R.id.fragment_container, pulleySimulationFragment);
                }
            }
        }
        else
        {
            simulationListFragment = new SimulationListFragment();
            ft.replace(R.id.fragment_container, simulationListFragment);
        }
        ft.addToBackStack(null);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
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

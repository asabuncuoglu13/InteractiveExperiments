package alpay.com.codenotesinteractive;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Display;
import android.view.WindowManager;

import alpay.com.codenotesinteractive.simulation.SimulationParameters;
import alpay.com.codenotesinteractive.simulation.simulation_fragments.ConstantAccelerationSimulationFragment;
import alpay.com.codenotesinteractive.simulation.simulation_fragments.InclinedPlaneSimulationFragment;
import alpay.com.codenotesinteractive.simulation.simulation_fragments.OhmsLawSimulationFragment;
import alpay.com.codenotesinteractive.simulation.simulation_fragments.SimulationListFragment;

public class Utility {

    public static int getScale(Activity activity, double simulation_size){
        Display display = ((WindowManager) activity.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int width = display.getWidth();
        Double val = new Double(width)/new Double(simulation_size);
        val = val * 100d;
        return val.intValue();
    }

    public static boolean isNetworkAvailable(Context context) {
        if (context == null) {
            return false;
        }
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    public static void callSimulationFragment(FragmentActivity fragmentActivity, int simulationID, double[] parameters)
    {
        InclinedPlaneSimulationFragment inclinedPlaneSimulationFragment;
        ConstantAccelerationSimulationFragment constantAccelerationSimulationFragment;
        OhmsLawSimulationFragment ohmsLawSimulationFragment;
        SimulationListFragment simulationListFragment;
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
}

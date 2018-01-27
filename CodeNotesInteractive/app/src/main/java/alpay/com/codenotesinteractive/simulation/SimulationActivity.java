package alpay.com.codenotesinteractive.simulation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import java.util.HashMap;

import alpay.com.codenotesinteractive.BaseActivity;
import alpay.com.codenotesinteractive.R;
import alpay.com.codenotesinteractive.simulation.simulation_fragments.ConstantAccelerationSimulationFragment;
import alpay.com.codenotesinteractive.simulation.simulation_fragments.InclinedPlaneSimulationFragment;
import alpay.com.codenotesinteractive.simulation.simulation_fragments.SimulationListFragment;
import alpay.com.codenotesinteractive.simulation.simulation_fragments.SimulationRecyclerViewAdapter;

public class SimulationActivity extends AppCompatActivity implements SimulationListFragment.OnListFragmentInteractionListener{


    private HashMap<String, int[]> parameterMap;
    int[] parameters = null;
    int simulation = 1001;
    InclinedPlaneSimulationFragment inclinedPlaneSimulationFragment;
    ConstantAccelerationSimulationFragment constantAccelerationSimulationFragment;
    SimulationListFragment simulationListFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onfragment);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            parameters = bundle.getIntArray("output");
            simulation = bundle.getInt("simulationID");
        }
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if(parameters != null)
        {
            if(simulation == SimulationParameters.INCLINED_PLANE_SIMULATION) {
                inclinedPlaneSimulationFragment = new InclinedPlaneSimulationFragment();
                inclinedPlaneSimulationFragment.setParameters(parameters);
                ft.replace(R.id.fragment_container, inclinedPlaneSimulationFragment);
            }
            else if(simulation == SimulationParameters.CONSTANT_ACCELERATION_SIMULATION)
            {
                constantAccelerationSimulationFragment = new ConstantAccelerationSimulationFragment();
                constantAccelerationSimulationFragment.setParameters(parameters);
                ft.replace(R.id.fragment_container, constantAccelerationSimulationFragment);
            }
        }else
        {
            simulationListFragment = new SimulationListFragment();
            ft.replace(R.id.fragment_container, simulationListFragment);
        }
        ft.addToBackStack(null);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }

    @Override
    public void onListFragmentInteraction(Simulation.SimulationItem item) {
        int simulationID = Integer.valueOf(item.id);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if(simulationID == SimulationParameters.INCLINED_PLANE_SIMULATION) {
            inclinedPlaneSimulationFragment = new InclinedPlaneSimulationFragment();
            ft.replace(R.id.fragment_container, inclinedPlaneSimulationFragment);
        }
        else if(simulationID == SimulationParameters.CONSTANT_ACCELERATION_SIMULATION)
        {
            constantAccelerationSimulationFragment = new ConstantAccelerationSimulationFragment();
            ft.replace(R.id.fragment_container, constantAccelerationSimulationFragment);
        }
        ft.addToBackStack(null);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent newIntent = new Intent(this, BaseActivity.class);
        startActivity(newIntent);
    }


}

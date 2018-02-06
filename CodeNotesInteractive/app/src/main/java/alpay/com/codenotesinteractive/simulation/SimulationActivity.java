package alpay.com.codenotesinteractive.simulation;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import alpay.com.codenotesinteractive.R;
import alpay.com.codenotesinteractive.simulation.simulation_fragments.SimulationListFragment;

public class SimulationActivity extends AppCompatActivity implements SimulationListFragment.OnListFragmentInteractionListener{

    double[] parameters = new double[20];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onfragment);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            parameters = bundle.getDoubleArray("output");
        }
        Simulation.callSimulationFragment(this, -1, parameters);
    }

    @Override
    public void onListFragmentInteraction(Simulation.SimulationItem item) {
        int simulationID = Integer.valueOf(item.id);
        Simulation.callSimulationFragment(this, simulationID, parameters);
    }

}

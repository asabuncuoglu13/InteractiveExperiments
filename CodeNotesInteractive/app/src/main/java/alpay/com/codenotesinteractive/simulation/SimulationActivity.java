package alpay.com.codenotesinteractive.simulation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import alpay.com.codenotesinteractive.BaseActivity;
import alpay.com.codenotesinteractive.R;
import alpay.com.codenotesinteractive.simulation.simulation_fragments.WebSimulationFragment;

public class SimulationActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simulation);

        WebSimulationFragment simuFrag = new WebSimulationFragment();
        simuFrag.setSimulation("inclinedplane");
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, simuFrag);
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

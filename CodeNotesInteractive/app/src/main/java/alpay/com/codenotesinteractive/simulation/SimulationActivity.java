package alpay.com.codenotesinteractive.simulation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import alpay.com.codenotesinteractive.BaseActivity;
import alpay.com.codenotesinteractive.R;
import alpay.com.codenotesinteractive.simulation.simulation_fragments.WebSimulationFragment;

public class SimulationActivity extends AppCompatActivity {

    int[] parameters;
    int angle, weight, friction;
    int angle_code = 51;
    int friction_code = 52;
    int weight_code = 53;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onfragment);

        Bundle bundle = getIntent().getExtras();
        WebSimulationFragment simuFrag = new WebSimulationFragment();
        if (bundle != null) {
            parameters = bundle.getIntArray("output");
            int cnt = 0;
            for(int i : parameters)
            {
                if(i==0)
                {
                    break;
                }
                if(i == angle_code)
                {
                    angle = parameters[cnt+1];
                }
                if(i == weight_code)
                {
                    weight = parameters[cnt+1];
                }
                if(i == friction_code)
                {
                    friction = parameters[cnt+1];
                }
                cnt++;
            }
            simuFrag.setParameters(angle, weight, friction);
        }

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

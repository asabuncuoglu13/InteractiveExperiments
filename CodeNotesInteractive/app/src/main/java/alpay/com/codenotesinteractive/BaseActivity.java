package alpay.com.codenotesinteractive;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetView;

import alpay.com.codenotesinteractive.chat.ChatFragment;
import alpay.com.codenotesinteractive.compiler.CompilerFragment;
import alpay.com.codenotesinteractive.simulation.Simulation;
import alpay.com.codenotesinteractive.simulation.simulation_fragments.ConstantAccelerationSimulationFragment;
import alpay.com.codenotesinteractive.simulation.simulation_fragments.InclinedPlaneSimulationFragment;
import alpay.com.codenotesinteractive.simulation.simulation_fragments.OhmsLawSimulationFragment;
import alpay.com.codenotesinteractive.simulation.simulation_fragments.SimulationListFragment;


public class BaseActivity extends AppCompatActivity implements SimulationListFragment.OnListFragmentInteractionListener {


    ChatFragment chatFragment;
    SimulationListFragment simulationListFragment;
    InclinedPlaneSimulationFragment inclinedPlaneSimulationFragment;
    ConstantAccelerationSimulationFragment constantAccelerationSimulationFragment;
    OhmsLawSimulationFragment ohmsLawSimulationFragment;
    CompilerFragment compilerFragment;
    ViewPager viewPager;
    BottomNavigationView bottomNavigation;
    MenuItem prevMenuItem;
    static boolean showTapTarget = true;
    static boolean largeScreen = false;
    static boolean experimentOn = false;

    static final String STATE_TAP = "tapstate";
    static final String STATE_SCREEN = "screenstate";
    static final String STATE_EXPERIMENT = "experimentstate";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            showTapTarget = savedInstanceState.getBoolean(STATE_TAP);
            largeScreen = savedInstanceState.getBoolean(STATE_SCREEN);
            experimentOn = savedInstanceState.getBoolean(STATE_EXPERIMENT);
        }
        setContentView(R.layout.activity_base);
        chatFragment = new ChatFragment();
        simulationListFragment = new SimulationListFragment();
        compilerFragment = new CompilerFragment();

        if (findViewById(R.id.fragment_chat_container) != null) {
            largeScreen = true;
            findViewById(R.id.bottom_navigation).setVisibility(View.GONE);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_chat_container, chatFragment);
            ft.addToBackStack(null);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.commit();
        }

        if(!largeScreen){
            findViewById(R.id.bottom_navigation).setVisibility(View.VISIBLE);}

        if (showTapTarget) {
            TapTargetView.showFor(this,                 // `this` is an Activity
                    TapTarget.forView(findViewById(R.id.fragment_container), getString(R.string.tap_target_title), getString(R.string.tap_target_detail))
                            // All options below are optional
                            .outerCircleColor(R.color.colorPrimaryDark)      // Specify a color for the outer circle
                            .outerCircleAlpha(0.96f)            // Specify the alpha amount for the outer circle
                            .drawShadow(true)                   // Whether to draw a drop shadow or not
                            .cancelable(true)                  // Whether tapping outside the outer circle dismisses the view
                            .tintTarget(true)                   // Whether to tint the target view's color
                            .transparentTarget(true)           // Specify whether the target is transparent (displays the content underneath)
                            .targetRadius(10),                  // Specify the target radius (in dp)
                    new TapTargetView.Listener() {          // The listener can listen for regular clicks, long clicks or cancels
                        @Override
                        public void onTargetClick(TapTargetView view) {
                            super.onTargetClick(view);      // This call is optional
                            //
                        }
                    });
        }

        showTapTarget = false;


        bottomNavigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                findViewById(R.id.viewpager).setVisibility(View.VISIBLE);
                switch (item.getItemId()) {
                    case R.id.action_code:
                        viewPager.setCurrentItem(2);
                        break;
                    case R.id.action_simulation:
                        viewPager.setCurrentItem(1);
                        break;
                    case R.id.action_chat:
                        viewPager.setCurrentItem(0);
                        break;
                    default:
                        break;
                }
                return true;
            }
        });

        viewPager = findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                } else {
                    bottomNavigation.getMenu().getItem(2).setChecked(false);
                }

                bottomNavigation.getMenu().getItem(position).setChecked(true);
                prevMenuItem = bottomNavigation.getMenu().getItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        if (!Utility.isNetworkAvailable(this)) {
            Toast.makeText(this, R.string.connection_error, Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current game state
        savedInstanceState.putBoolean(STATE_EXPERIMENT, experimentOn);
        savedInstanceState.putBoolean(STATE_TAP, showTapTarget);
        savedInstanceState.putBoolean(STATE_SCREEN, largeScreen);
        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onListFragmentInteraction(Simulation.SimulationItem item) {
        int simulationID = Integer.valueOf(item.id);
        experimentOn = true;
        findViewById(R.id.baseactivity_view).setVisibility(View.GONE);
        Utility.callSimulationFragment(this, simulationID, null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.action_howto:
                Intent intent = new Intent(this, HowToActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
        return true;
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        if (!largeScreen) {
            adapter.addFragment(chatFragment);
        }
        adapter.addFragment(simulationListFragment);
        adapter.addFragment(compilerFragment);
        viewPager.setAdapter(adapter);
    }


}

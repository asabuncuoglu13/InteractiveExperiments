package alpay.com.codenotesinteractive;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.hololo.tutorial.library.TutorialActivity;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;

import alpay.com.codenotesinteractive.simulation.Simulation;
import alpay.com.codenotesinteractive.simulation.SimulationParameters;
import alpay.com.codenotesinteractive.simulation.simulation_fragments.SimulationListFragment;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class HomeActivity extends AppCompatActivity implements SimulationListFragment.OnListFragmentInteractionListener {

    static boolean largeScreen = false;
    static boolean experimentOn = false;
    static boolean comingFromHomeScreen = true;
    static final String STATE_SCREEN = "screenstate";
    static final String STATE_EXPERIMENT = "experimentstate";
    public Drawer navigationDrawer;

    @Nullable
    @OnClick(R.id.button_fab)
    public void setFABBehaviour(){
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        setNavigationDrawer();
        if (savedInstanceState != null) {
            largeScreen = savedInstanceState.getBoolean(STATE_SCREEN);
            experimentOn = savedInstanceState.getBoolean(STATE_EXPERIMENT);
        }
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            selectFragmentFromChatBundle( bundle.getString("reply"));
        }
        if(comingFromHomeScreen)
        {
            selectFragmentWithCategoryID(FragmentManager.Category.NOTE.id);
            comingFromHomeScreen = false;
        }
        if (!Utility.isNetworkAvailable(this)) {
            Toast.makeText(this, R.string.connection_error, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        comingFromHomeScreen = true;
    }

    public void setNavigationDrawer() {
        final Toolbar toolbar = (Toolbar) findViewById(R.id.activity_main_toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        navigationDrawer = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withHeader(R.layout.header)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.bottom_menu_chat).withIdentifier(FragmentManager.Category.CHAT.id),
                        new PrimaryDrawerItem().withName(R.string.bottom_menu_simulation).withIdentifier(FragmentManager.Category.SIMULATION.id),
                        new PrimaryDrawerItem().withName(R.string.bottom_menu_codenotes).withIdentifier(FragmentManager.Category.PROGRAMMING.id),
                        new PrimaryDrawerItem().withName(R.string.bottom_menu_notes).withIdentifier(FragmentManager.Category.NOTE.id)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if (drawerItem != null) {
                            if (drawerItem instanceof Nameable) {
                                toolbar.setTitle(((Nameable) drawerItem).getName().getText(HomeActivity.this));
                                selectFragmentWithCategoryID((int) drawerItem.getIdentifier());
                            }
                        }

                        return false;
                    }
                })
                .build();
        this.navigationDrawer.getRecyclerView().setVerticalScrollBarEnabled(false);
    }

    public void selectFragmentFromChatBundle(String reply) {
        if (reply.contains("How-To-Guide")) {
            Intent intent = new Intent(this, TutorialActivity.class);
            startActivity(intent);
        }
        else if (reply.contains("Coding-Area")) {
            selectFragmentWithCategoryID(FragmentManager.Category.PROGRAMMING.id);
        }
        else if (reply.contains("Simulation-Area")) {
            selectFragmentWithCategoryID(FragmentManager.Category.SIMULATION.id);
        }
        else if (reply.contains("Ohms-Law-Experiment")) {
            selectFragmentFromSimulationID(SimulationParameters.OHMS_LAW_SIMULATION);
        }
        else if (reply.contains("Inclined-Plane-Experiment")) {
            selectFragmentFromSimulationID(SimulationParameters.INCLINED_PLANE_SIMULATION);
        }
        else if (reply.contains("Constant-Acceleration-Experiment")) {
            selectFragmentFromSimulationID(SimulationParameters.CONSTANT_ACCELERATION_SIMULATION);
        }
    }

    public void selectFragmentWithCategoryID(int id) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (id == FragmentManager.Category.CHAT.id && FragmentManager.Category.currentCategoryID != FragmentManager.Category.CHAT.id) {
            ft.replace(R.id.fragment_container_home, FragmentManager.FRAGMENT_TYPE.CHAT_FRAGMENT.getFragment());
            FragmentManager.Category.currentCategoryID = 1;
        }
        if (id == FragmentManager.Category.SIMULATION.id && FragmentManager.Category.currentCategoryID != FragmentManager.Category.SIMULATION.id) {
            ft.replace(R.id.fragment_container_home, FragmentManager.FRAGMENT_TYPE.SIMULATION_LIST_FRAGMENT.getFragment());
            FragmentManager.Category.currentCategoryID = 2;
        }
        if (id == FragmentManager.Category.PROGRAMMING.id && FragmentManager.Category.currentCategoryID != FragmentManager.Category.PROGRAMMING.id) {
            ft.replace(R.id.fragment_container_home, FragmentManager.FRAGMENT_TYPE.CODENOTES_FRAGMENT.getFragment());
            FragmentManager.Category.currentCategoryID = 3;
        }
        if (id == FragmentManager.Category.NOTE.id && FragmentManager.Category.currentCategoryID != FragmentManager.Category.NOTE.id) {
            ft.replace(R.id.fragment_container_home, FragmentManager.FRAGMENT_TYPE.STUDY_NOTES_FRAGMENT.getFragment());
            FragmentManager.Category.currentCategoryID = 4;
        }
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }

    public void selectFragmentFromSimulationID(int id) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (id == FragmentManager.FRAGMENT_TYPE.CONSTANTACCELERATIONSIMULATION_FRAGMENT.getFragmentID()) {
            ft.replace(R.id.fragment_container_home, FragmentManager.FRAGMENT_TYPE.CONSTANTACCELERATIONSIMULATION_FRAGMENT.getFragment());
        }else if (id == FragmentManager.FRAGMENT_TYPE.INCLINEDPLANECANVAS_FRAGMENT.getFragmentID()) {
            ft.replace(R.id.fragment_container_home, FragmentManager.FRAGMENT_TYPE.INCLINEDPLANECANVAS_FRAGMENT.getFragment());
        }else if (id == FragmentManager.FRAGMENT_TYPE.INCLINEDPLANESIMULATION_FRAGMENT.getFragmentID()) {
            ft.replace(R.id.fragment_container_home, FragmentManager.FRAGMENT_TYPE.INCLINEDPLANESIMULATION_FRAGMENT.getFragment());
        }else if (id == FragmentManager.FRAGMENT_TYPE.LEVERSIMULATION_FRAGMENT.getFragmentID()) {
            ft.replace(R.id.fragment_container_home, FragmentManager.FRAGMENT_TYPE.LEVERSIMULATION_FRAGMENT.getFragment());
        }else if (id == FragmentManager.FRAGMENT_TYPE.OHMSLAWSIMULATION_FRAGMENT.getFragmentID()) {
            ft.replace(R.id.fragment_container_home, FragmentManager.FRAGMENT_TYPE.OHMSLAWSIMULATION_FRAGMENT.getFragment());
        }else if (id == FragmentManager.FRAGMENT_TYPE.PULLEYSIMULATION_FRAGMENT.getFragmentID()) {
            ft.replace(R.id.fragment_container_home, FragmentManager.FRAGMENT_TYPE.PULLEYSIMULATION_FRAGMENT.getFragment());
        }
        FragmentManager.Category.currentCategoryID = 0;
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean(STATE_EXPERIMENT, experimentOn);
        savedInstanceState.putBoolean(STATE_SCREEN, largeScreen);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onListFragmentInteraction(Simulation.SimulationItem item) {
        int simulationID = Integer.valueOf(item.id);
        experimentOn = true;
        selectFragmentFromSimulationID(simulationID);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_menu, menu);
        return true;
    }

    public interface OnFilterChangedListener {
        void onFilterChanged(long filter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        comingFromHomeScreen = true;
        switch (item.getItemId()) {
            case R.id.action_howto:
                Intent intent = new Intent(this, HowToActivity.class);
                startActivity(intent);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {
        if (navigationDrawer.isDrawerOpen()) {
            navigationDrawer.closeDrawer();
            return;
        }
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, R.string.text_exit_from_app, Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

}

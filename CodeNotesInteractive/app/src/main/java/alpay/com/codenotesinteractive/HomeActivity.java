package alpay.com.codenotesinteractive;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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


public class HomeActivity extends AppCompatActivity implements SimulationListFragment.OnListFragmentInteractionListener {

    Drawer navigationDrawer;
    boolean largeScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        largeScreen = findViewById(R.id.fragment_chat_container) != null;
        if (bundle != null)
            selectFragmentFromChatBundle(bundle.getString("reply"));
        if (!Utility.isNetworkAvailable(this))
            Toast.makeText(this, R.string.connection_error, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        setNavigationDrawer();
        if (largeScreen) {
            setLargeScreenView();
            return;
        } else {
            setNormalScreenView();
            return;
        }
    }

    public void setNormalScreenView() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container_home, FragmentManager.FRAGMENT_TYPE.STUDY_NOTES_FRAGMENT.getFragment());
        FragmentManager.Category.currentCategoryID = 4;
        ft.commit();
    }

    public void setLargeScreenView() {
        FragmentTransaction ftr = getSupportFragmentManager().beginTransaction();
        ftr.replace(R.id.fragment_chat_container, FragmentManager.FRAGMENT_TYPE.CHAT_FRAGMENT.getFragment());
        ftr.commit();

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container_home, FragmentManager.FRAGMENT_TYPE.STUDY_NOTES_FRAGMENT.getFragment());
        FragmentManager.Category.currentCategoryID = 4;
        ft.commit();

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
                        new PrimaryDrawerItem().withName(R.string.bottom_menu_notes).withIdentifier(FragmentManager.Category.NOTE.id),
                        new PrimaryDrawerItem().withName(R.string.bottom_menu_simulation).withIdentifier(FragmentManager.Category.SIMULATION.id),
                        new PrimaryDrawerItem().withName(R.string.bottom_menu_codenotes).withIdentifier(FragmentManager.Category.PROGRAMMING.id)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if (drawerItem != null) {
                            if (drawerItem instanceof Nameable && FragmentManager.Category.currentCategoryID != drawerItem.getIdentifier()) {
                                toolbar.setTitle(((Nameable) drawerItem).getName().getText(HomeActivity.this));
                                selectFragmentWithCategoryID((int) drawerItem.getIdentifier());
                            }
                        }
                        return false;
                    }
                })
                .build();
        if (!largeScreen)
            navigationDrawer.addItem(new PrimaryDrawerItem().withName(R.string.bottom_menu_chat).withIdentifier(FragmentManager.Category.CHAT.id));
        this.navigationDrawer.getRecyclerView().setVerticalScrollBarEnabled(false);
    }

    public void selectFragmentFromChatBundle(String reply) {
        if (reply.contains("How-To-Guide")) {
            Intent intent = new Intent(this, TutorialActivity.class);
            startActivity(intent);
        } else if (reply.contains("Coding-Area")) {
            selectFragmentWithCategoryID(FragmentManager.Category.PROGRAMMING.id);
        } else if (reply.contains("Simulation-Area")) {
            selectFragmentWithCategoryID(FragmentManager.Category.SIMULATION.id);
        } else if (reply.contains("Ohms-Law-Experiment")) {
            selectFragmentFromSimulationID(SimulationParameters.OHMS_LAW_SIMULATION);
        } else if (reply.contains("Inclined-Plane-Experiment")) {
            selectFragmentFromSimulationID(SimulationParameters.INCLINED_PLANE_SIMULATION);
        } else if (reply.contains("Constant-Acceleration-Experiment")) {
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
        ft.addToBackStack("CategoryFragment");
        ft.commit();
    }

    public void selectFragmentFromSimulationID(int id) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (id == FragmentManager.FRAGMENT_TYPE.CONSTANTACCELERATIONSIMULATION_FRAGMENT.getFragmentID()) {
            ft.replace(R.id.fragment_container_home, FragmentManager.FRAGMENT_TYPE.CONSTANTACCELERATIONSIMULATION_FRAGMENT.getFragment());
        } else if (id == FragmentManager.FRAGMENT_TYPE.INCLINEDPLANECANVAS_FRAGMENT.getFragmentID()) {
            ft.replace(R.id.fragment_container_home, FragmentManager.FRAGMENT_TYPE.INCLINEDPLANECANVAS_FRAGMENT.getFragment());
        } else if (id == FragmentManager.FRAGMENT_TYPE.INCLINEDPLANESIMULATION_FRAGMENT.getFragmentID()) {
            ft.replace(R.id.fragment_container_home, FragmentManager.FRAGMENT_TYPE.INCLINEDPLANESIMULATION_FRAGMENT.getFragment());
        } else if (id == FragmentManager.FRAGMENT_TYPE.LEVERSIMULATION_FRAGMENT.getFragmentID()) {
            ft.replace(R.id.fragment_container_home, FragmentManager.FRAGMENT_TYPE.LEVERSIMULATION_FRAGMENT.getFragment());
        } else if (id == FragmentManager.FRAGMENT_TYPE.OHMSLAWSIMULATION_FRAGMENT.getFragmentID()) {
            ft.replace(R.id.fragment_container_home, FragmentManager.FRAGMENT_TYPE.OHMSLAWSIMULATION_FRAGMENT.getFragment());
        } else if (id == FragmentManager.FRAGMENT_TYPE.PULLEYSIMULATION_FRAGMENT.getFragmentID()) {
            ft.replace(R.id.fragment_container_home, FragmentManager.FRAGMENT_TYPE.PULLEYSIMULATION_FRAGMENT.getFragment());
        }
        FragmentManager.Category.currentCategoryID = 0;
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.addToBackStack("SimulationFragment");
        ft.commit();
    }

    @Override
    public void onListFragmentInteraction(Simulation.SimulationItem item) {
        int simulationID = Integer.valueOf(item.id);
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

    @Override
    public void onBackPressed() {
        if (navigationDrawer.isDrawerOpen()) {
            navigationDrawer.closeDrawer();
            return;
        }
        int count = getFragmentManager().getBackStackEntryCount();
        if (count <= 1) {
            super.onBackPressed();
        } else {
            getFragmentManager().popBackStack();
        }
    }

}

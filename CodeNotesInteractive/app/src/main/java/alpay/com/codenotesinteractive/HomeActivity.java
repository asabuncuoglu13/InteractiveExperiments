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

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;

import alpay.com.codenotesinteractive.chat.ChatFragment;
import alpay.com.codenotesinteractive.compiler.CompilerFragment;
import alpay.com.codenotesinteractive.simulation.Simulation;
import alpay.com.codenotesinteractive.simulation.simulation_fragments.SimulationListFragment;


public class HomeActivity extends AppCompatActivity implements SimulationListFragment.OnListFragmentInteractionListener {

    ChatFragment chatFragment;
    SimulationListFragment simulationListFragment;
    CompilerFragment compilerFragment;
    static boolean largeScreen = false;
    static boolean experimentOn = false;
    static final String STATE_SCREEN = "screenstate";
    static final String STATE_EXPERIMENT = "experimentstate";

    public static enum Category {
        CHAT(1),
        SIMULATION(2),
        PROGRAMMING(3);
        public final int id;
        static int currentCategoryID;
        Category(int id) {
            this.id = id;
        }
    }

    public Drawer navigationDrawer;
    private OnFilterChangedListener onFilterChangedListener;
    public void setOnFilterChangedListener(OnFilterChangedListener onFilterChangedListener) {
        this.onFilterChangedListener = onFilterChangedListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            largeScreen = savedInstanceState.getBoolean(STATE_SCREEN);
            experimentOn = savedInstanceState.getBoolean(STATE_EXPERIMENT);
        }
        setContentView(R.layout.activity_home);
        chatFragment = new ChatFragment();
        simulationListFragment = new SimulationListFragment();
        compilerFragment = new CompilerFragment();
        setNavigationDrawer();
        selectFragmentWithCategoryID(Category.CHAT.id);
        if (!Utility.isNetworkAvailable(this)) {
            Toast.makeText(this, R.string.connection_error, Toast.LENGTH_LONG).show();
        }
    }

    public void setNavigationDrawer()
    {
        final Toolbar toolbar = (Toolbar) findViewById(R.id.activity_main_toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        navigationDrawer = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withHeader(R.layout.header)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.bottom_menu_chat).withIdentifier(Category.CHAT.id),
                        new PrimaryDrawerItem().withName(R.string.bottom_menu_simulation).withIdentifier(Category.SIMULATION.id),
                        new SectionDrawerItem().withName(R.string.bottom_menu_codenotes).withIdentifier(Category.PROGRAMMING.id)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {

                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if (drawerItem != null) {
                            if (drawerItem instanceof Nameable) {
                                toolbar.setTitle(((Nameable) drawerItem).getName().getText(HomeActivity.this));
                                selectFragmentWithCategoryID((int) drawerItem.getIdentifier());
                            }
                            if (onFilterChangedListener != null) {
                                onFilterChangedListener.onFilterChanged(drawerItem.getIdentifier());
                            }
                        }

                        return false;
                    }
                })
                .build();

        //disable scrollbar :D it's ugly
        this.navigationDrawer.getRecyclerView().setVerticalScrollBarEnabled(false);
    }

    public void selectFragmentWithCategoryID(int id)
    {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if(id == Category.CHAT.id && Category.currentCategoryID != Category.CHAT.id)
        {
            ft.replace(R.id.fragment_container_home, chatFragment);
            Category.currentCategoryID = 1;
        }
        else if(id == Category.SIMULATION.id && Category.currentCategoryID != Category.SIMULATION.id)
        {
            ft.replace(R.id.fragment_container_home, simulationListFragment);
            Category.currentCategoryID = 2;
        }
        else if(id == Category.PROGRAMMING.id && Category.currentCategoryID != Category.PROGRAMMING.id)
        {
            ft.replace(R.id.fragment_container_home, compilerFragment);
            Category.currentCategoryID = 3;
        }
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
        Simulation.callSimulationFragment(this, simulationID, null);
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
        super.onBackPressed();
        if (navigationDrawer.isDrawerOpen())
        {
            navigationDrawer.closeDrawer();
        }
    }

}

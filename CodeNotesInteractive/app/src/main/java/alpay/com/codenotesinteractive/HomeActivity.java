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
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;
import alpay.com.codenotesinteractive.chat.ChatFragment;
import alpay.com.codenotesinteractive.compiler.CompilerFragment;
import alpay.com.codenotesinteractive.simulation.Simulation;
import alpay.com.codenotesinteractive.simulation.simulation_fragments.SimulationListFragment;
import alpay.com.codenotesinteractive.studynotes.StudyNotesFragment;
import butterknife.BindView;
import butterknife.ButterKnife;


public class HomeActivity extends AppCompatActivity implements SimulationListFragment.OnListFragmentInteractionListener {

    @BindView(R.id.activity_main_toolbar) Toolbar toolbar;

    ChatFragment chatFragment;
    SimulationListFragment simulationListFragment;
    CompilerFragment compilerFragment;
    StudyNotesFragment studyNotesFragment;

    static boolean largeScreen = false;
    static boolean experimentOn = false;
    static final String STATE_SCREEN = "screenstate";
    static final String STATE_EXPERIMENT = "experimentstate";

    public enum Category {
        CHAT(1),
        SIMULATION(2),
        PROGRAMMING(3),
        NOTE(4);

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
        ButterKnife.bind(this);
        createFragments();
        setNavigationDrawer();
        selectFragmentWithCategoryID(Category.CHAT.id);
        if (!Utility.isNetworkAvailable(this)) {
            Toast.makeText(this, R.string.connection_error, Toast.LENGTH_LONG).show();
        }
    }

    public void createFragments()
    {
        chatFragment = new ChatFragment();
        simulationListFragment = new SimulationListFragment();
        compilerFragment = new CompilerFragment();
        studyNotesFragment = new StudyNotesFragment();
    }

    public void setNavigationDrawer()
    {
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        navigationDrawer = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withHeader(R.layout.header)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.bottom_menu_chat).withIdentifier(Category.CHAT.id),
                        new PrimaryDrawerItem().withName(R.string.bottom_menu_simulation).withIdentifier(Category.SIMULATION.id),
                        new PrimaryDrawerItem().withName(R.string.bottom_menu_codenotes).withIdentifier(Category.PROGRAMMING.id),
                        new PrimaryDrawerItem().withName(R.string.bottom_menu_notes).withIdentifier(Category.NOTE.id)
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
        else if(id == Category.NOTE.id && Category.currentCategoryID != Category.NOTE.id)
        {
            ft.replace(R.id.fragment_container_home, studyNotesFragment);
            Category.currentCategoryID = 4;
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

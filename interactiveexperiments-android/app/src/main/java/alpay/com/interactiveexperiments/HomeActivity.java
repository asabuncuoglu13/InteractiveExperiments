package alpay.com.interactiveexperiments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;

import io.fabric.sdk.android.Fabric;
import java.util.Arrays;
import java.util.List;

import alpay.com.interactiveexperiments.blockly.BlocklyActivity;
import alpay.com.interactiveexperiments.home.CategoryRecyclerViewAdapter;
import alpay.com.interactiveexperiments.home.HomeFragment;
import alpay.com.interactiveexperiments.simulation.Simulation;
import alpay.com.interactiveexperiments.simulation.SimulationParameters;
import alpay.com.interactiveexperiments.simulation.simulation_fragments.SimulationListFragment;
import alpay.com.interactiveexperiments.util.FragmentManager;
import alpay.com.interactiveexperiments.util.Utility;
import butterknife.ButterKnife;


public class HomeActivity extends AppCompatActivity implements SimulationListFragment.OnListFragmentInteractionListener, HomeFragment.OnListFragmentInteractionListener {

    Drawer navigationDrawer;
    boolean largeScreen;
    List<AuthUI.IdpConfig> providers;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    ActionBar toolbar;
    Bundle bundle;
    static final int RC_SIGN_IN = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        bundle = getIntent().getExtras();
        mAuth = FirebaseAuth.getInstance();
        providers = Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build());
        prepareView();
        if (!Utility.isNetworkAvailable(this))
            Toast.makeText(this, R.string.connection_error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mAuth != null && mUser != null) {
            mUser = mAuth.getCurrentUser();
            updateViewWithUserAccount(mUser);
        } else {
            updateViewForNoAccount();
        }
    }

    private void prepareView() {
        largeScreen = findViewById(R.id.fragment_chat_container) != null;
        toolbar = getSupportActionBar();
        toolbar.setHomeButtonEnabled(true);
        toolbar.setDisplayHomeAsUpEnabled(true);
        toolbar.setHomeAsUpIndicator(R.drawable.ic_hamburger);
        if (largeScreen)
            setLargeScreenView();
        else
            setNormalScreenView();
    }

    public void updateViewWithUserAccount(FirebaseUser user) {
        createNavigationBuilderWithAccount();
        if (navigationDrawer.getDrawerItem(FragmentManager.Category.LOGIN.id) != null)
            navigationDrawer.removeItem(FragmentManager.Category.LOGIN.id);
        if (navigationDrawer.getDrawerItem(FragmentManager.Category.LOGOUT.id) == null)
            navigationDrawer.addItem(new PrimaryDrawerItem().withIcon(R.drawable.ic_logout_sketch).withName(R.string.menu_logout).withIdentifier(FragmentManager.Category.LOGOUT.id));
    }

    public void updateViewForNoAccount() {
        createNavigationBuilderForNoAccount();
        if (navigationDrawer.getDrawerItem(FragmentManager.Category.LOGOUT.id) != null)
            navigationDrawer.removeItem(FragmentManager.Category.LOGOUT.id);
        if (navigationDrawer.getDrawerItem(FragmentManager.Category.LOGIN.id) == null)
            navigationDrawer.addItem(new PrimaryDrawerItem().withIcon(R.drawable.ic_google_sketch).withName(R.string.menu_login).withIdentifier(FragmentManager.Category.LOGIN.id));
    }


    public void setNormalScreenView() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (bundle != null) {
            selectFragmentFromChatBundle(bundle.getString("reply"));
        } else {
            ft.replace(R.id.fragment_container_home, FragmentManager.FRAGMENT_TYPE.HOME_FRAGMENT.getFragment());
            FragmentManager.Category.currentCategoryID = FragmentManager.Category.HOME.id;
            ft.commit();
        }
    }

    public void setLargeScreenView() {
        FragmentTransaction ftr = getSupportFragmentManager().beginTransaction();
        ftr.replace(R.id.fragment_chat_container, FragmentManager.FRAGMENT_TYPE.CHAT_FRAGMENT.getFragment());
        ftr.commit();

        if (bundle != null) {
            selectFragmentFromChatBundle(bundle.getString("reply"));
        } else {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_container_home, FragmentManager.FRAGMENT_TYPE.HOME_FRAGMENT.getFragment());
            FragmentManager.Category.currentCategoryID = FragmentManager.Category.HOME.id;
            ft.commit();
        }

    }

    public void createNavigationBuilderWithAccount() {
        AccountHeader accountHeader = createAccountHeader(mUser.getDisplayName(), mUser.getEmail(), mUser.getPhotoUrl());
        navigationDrawer = new DrawerBuilder()
                .withActivity(this)
                .withDisplayBelowStatusBar(false)
                .withTranslucentStatusBar(false)
                .withDrawerLayout(R.layout.material_drawer_fits_not)
                .withHeader(R.layout.header)
                .withAccountHeader(accountHeader)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if (drawerItem != null) {
                            if (drawerItem instanceof Nameable && FragmentManager.Category.currentCategoryID != drawerItem.getIdentifier()) {
                                toolbar.setTitle(((Nameable) drawerItem).getName().getText(HomeActivity.this));
                                chooseCategoryAction((int) drawerItem.getIdentifier());
                            }
                        }
                        return false;
                    }
                })
                .build();
        setNavigationItems();
    }

    public void createNavigationBuilderForNoAccount() {
        navigationDrawer = new DrawerBuilder()
                .withActivity(this)
                .withDisplayBelowStatusBar(false)
                .withTranslucentStatusBar(false)
                .withHeader(R.layout.header)
                .withDrawerLayout(R.layout.material_drawer_fits_not)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if (drawerItem != null) {
                            if (drawerItem instanceof Nameable && FragmentManager.Category.currentCategoryID != drawerItem.getIdentifier()) {
                                toolbar.setTitle(((Nameable) drawerItem).getName().getText(HomeActivity.this));
                                chooseCategoryAction((int) drawerItem.getIdentifier());
                            }
                        }
                        return false;
                    }
                })
                .build();
        setNavigationItems();
    }

    public void setNavigationItems() {
        navigationDrawer.addItem(new PrimaryDrawerItem().withIcon(R.drawable.ic_home_sketch).withName(R.string.menu_home).withIdentifier(FragmentManager.Category.HOME.id));
        if (!largeScreen)
            navigationDrawer.addItem(new PrimaryDrawerItem().withIcon(R.drawable.ic_chatting_speech_bubbles).withName(R.string.menu_chat).withIdentifier(FragmentManager.Category.CHAT.id));
        navigationDrawer.addItem(new PrimaryDrawerItem().withIcon(R.drawable.ic_take_notes).withName(R.string.menu_studynotes).withIdentifier(FragmentManager.Category.NOTE.id));
        navigationDrawer.addItem(new PrimaryDrawerItem().withIcon(R.drawable.ic_microscope_sketch).withName(R.string.menu_simulation).withIdentifier(FragmentManager.Category.SIMULATION.id));
        navigationDrawer.addItem(new PrimaryDrawerItem().withIcon(R.drawable.ic_laptop_sketch).withName(R.string.menu_blockly).withIdentifier(FragmentManager.Category.BLOCKLY.id));
        navigationDrawer.addItem(new PrimaryDrawerItem().withIcon(R.drawable.ic_card).withName(R.string.menu_teachingcards).withIdentifier(FragmentManager.Category.TEACHINGCARDS.id));
        navigationDrawer.addItem(new PrimaryDrawerItem().withIcon(R.drawable.ic_feedback).withName(R.string.menu_feedback).withIdentifier(FragmentManager.Category.SENDFEEDBACK.id));
        navigationDrawer.addItem(new PrimaryDrawerItem().withIcon(R.drawable.ic_logout_sketch).withName(R.string.menu_logout).withIdentifier(FragmentManager.Category.LOGOUT.id));
        navigationDrawer.getRecyclerView().setVerticalScrollBarEnabled(false);
    }

    public AccountHeader createAccountHeader(String name, String email, Uri uri) {
        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.header)
                .addProfiles(new ProfileDrawerItem().withName(name).withEmail(email).withIcon(uri))
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                        return false;
                    }
                })
                .build();
        return headerResult;
    }

    public void selectFragmentFromChatBundle(String reply) {
        if(reply == null)
            return;
        if (reply.contains("How-To-Guide")) {
            Intent intent = new Intent(this, TutorialApplicationActivity.class);
            startActivity(intent);
        } else if (reply.contains("Coding-Area")) {
            chooseCategoryAction(FragmentManager.Category.BLOCKLY.id);
        } else if (reply.contains("Simulation-Area")) {
            chooseCategoryAction(FragmentManager.Category.SIMULATION.id);
        } else if (reply.contains("Ohms-Law-Experiment")) {
            selectFragmentFromSimulationID(SimulationParameters.OHMS_LAW_SIMULATION);
        } else if (reply.contains("Inclined-Plane-Experiment")) {
            selectFragmentFromSimulationID(SimulationParameters.INCLINED_PLANE_SIMULATION);
        } else if (reply.contains("Constant-Acceleration-Experiment")) {
            selectFragmentFromSimulationID(SimulationParameters.CONSTANT_ACCELERATION_SIMULATION);
        } else if (reply.contains("Inclined-Sensor-Experiment")) {
            selectFragmentFromSimulationID(SimulationParameters.INCLINED_CANVAS_SIMULATION);
        } else if (reply.contains("Lever-Experiment")) {
            selectFragmentFromSimulationID(SimulationParameters.LEVER_SIMULATION);
        }  else if (reply.contains("Pulley-Experiment")) {
            selectFragmentFromSimulationID(SimulationParameters.PULLEY_SIMULATION);
        } else if (reply.contains("Note-Area")) {
            chooseCategoryAction(FragmentManager.Category.NOTE.id);
        }
    }

    public void chooseCategoryAction(int id) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (id == FragmentManager.Category.LOGIN.id) {
            navigationDrawer.closeDrawer();
            loginAction();
        }

        if (id == FragmentManager.Category.LOGOUT.id) {
            navigationDrawer.closeDrawer();
            logoutAction();
        }
        if (id == FragmentManager.Category.BLOCKLY.id) {
            Intent intent = new Intent(this, BlocklyActivity.class);
            startActivity(intent);
        }

        if (id == FragmentManager.Category.SENDFEEDBACK.id) {
            Intent intent = new Intent(this, SendFeedbackActivity.class);
            startActivity(intent);
        }

        if (id == FragmentManager.Category.TEACHINGCARDS.id) {
            ft.replace(R.id.fragment_container_home, FragmentManager.FRAGMENT_TYPE.TEACHINGCARDS_FRAGMENT.getFragment());
            FragmentManager.Category.currentCategoryID = FragmentManager.Category.TEACHINGCARDS.id;
        }

        if (id == FragmentManager.Category.HOME.id && FragmentManager.Category.currentCategoryID != FragmentManager.Category.HOME.id) {
            ft.replace(R.id.fragment_container_home, FragmentManager.FRAGMENT_TYPE.HOME_FRAGMENT.getFragment());
            FragmentManager.Category.currentCategoryID = FragmentManager.Category.HOME.id;
        }

        if (id == FragmentManager.Category.CHAT.id && FragmentManager.Category.currentCategoryID != FragmentManager.Category.CHAT.id) {
            ft.replace(R.id.fragment_container_home, FragmentManager.FRAGMENT_TYPE.CHAT_FRAGMENT.getFragment());
            FragmentManager.Category.currentCategoryID = FragmentManager.Category.CHAT.id;
        }

        if (id == FragmentManager.Category.SIMULATION.id && FragmentManager.Category.currentCategoryID != FragmentManager.Category.SIMULATION.id) {
            ft.replace(R.id.fragment_container_home, FragmentManager.FRAGMENT_TYPE.SIMULATION_LIST_FRAGMENT.getFragment());
            FragmentManager.Category.currentCategoryID = FragmentManager.Category.SIMULATION.id;
        }
        if (id == FragmentManager.Category.NOTE.id && FragmentManager.Category.currentCategoryID != FragmentManager.Category.NOTE.id) {
            ft.replace(R.id.fragment_container_home, FragmentManager.FRAGMENT_TYPE.STUDY_NOTES_FRAGMENT.getFragment());
            FragmentManager.Category.currentCategoryID = FragmentManager.Category.NOTE.id;
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
    public void onListFragmentInteraction(CategoryRecyclerViewAdapter.Category item) {
        String categoryName = item.name;
        chooseCategoryAction(FragmentManager.Category.SIMULATION.id);
        Toast.makeText(this, "You select: " + categoryName, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_howto:
                Intent intent = new Intent(this, TutorialApplicationActivity.class);
                startActivity(intent);
                return true;
            case android.R.id.home:
                if (navigationDrawer.isDrawerOpen())
                    navigationDrawer.closeDrawer();
                else
                    navigationDrawer.openDrawer();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void loginAction() {
        startActivityForResult(AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build(), RC_SIGN_IN);
    }

    private void logoutAction() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        updateViewForNoAccount();
                        Toast.makeText(getBaseContext(), R.string.logout_completed, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if (resultCode == RESULT_OK) {
                mUser = FirebaseAuth.getInstance().getCurrentUser();
                updateViewWithUserAccount(mUser);
            } else {
                Toast.makeText(this, R.string.response_type_error + response.getError().getErrorCode(), Toast.LENGTH_SHORT).show();
            }
        }
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

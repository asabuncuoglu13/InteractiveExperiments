package alpay.com.codenotesinteractive;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetView;

import alpay.com.codenotesinteractive.chat.ChatActivity;
import alpay.com.codenotesinteractive.compiler.CompilerActivity;
import alpay.com.codenotesinteractive.simulation.SimulationActivity;


public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        TapTargetView.showFor(this,                 // `this` is an Activity
                TapTarget.forView(findViewById(R.id.bottom_navigation), getString(R.string.bottom_target_title), getString(R.string.bottom_target_detail))
                        // All options below are optional
                        .outerCircleColor(R.color.colorPrimaryDark)      // Specify a color for the outer circle
                        .outerCircleAlpha(0.96f)            // Specify the alpha amount for the outer circle
                        .drawShadow(true)                   // Whether to draw a drop shadow or not
                        .cancelable(true)                  // Whether tapping outside the outer circle dismisses the view
                        .tintTarget(true)                   // Whether to tint the target view's color
                        .transparentTarget(false)           // Specify whether the target is transparent (displays the content underneath)
                        .targetRadius(60),                  // Specify the target radius (in dp)
                new TapTargetView.Listener() {          // The listener can listen for regular clicks, long clicks or cancels
                    @Override
                    public void onTargetClick(TapTargetView view) {
                        super.onTargetClick(view);      // This call is optional
                        //
                    }
                });

        BottomNavigationView bnv = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bnv.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_chat:
                        Intent chatintent = new Intent(BaseActivity.this, ChatActivity.class);
                        startActivity(chatintent);
                        break;
                    case R.id.action_code:
                        Intent codeintent = new Intent(BaseActivity.this, CompilerActivity.class);
                        startActivity(codeintent);
                        break;
                    case R.id.action_simulation:
                        Intent simintent = new Intent(BaseActivity.this, SimulationActivity.class);
                        startActivity(simintent);
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
    }


}

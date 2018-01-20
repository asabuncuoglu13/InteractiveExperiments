package alpay.com.codenotesinteractive;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import alpay.com.codenotesinteractive.chat.ChatActivity;
import alpay.com.codenotesinteractive.compiler.CompilerActivity;
import alpay.com.codenotesinteractive.simulation.SimulationActivity;


public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
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

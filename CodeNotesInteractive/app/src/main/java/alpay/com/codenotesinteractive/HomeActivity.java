package alpay.com.codenotesinteractive;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import alpay.com.codenotesinteractive.chat.ChatActivity;


public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_chat:
                                Intent chatintent = new Intent(HomeActivity.this, ChatActivity.class);
                                startActivity(chatintent);
                                break;
                            case R.id.action_code:
                                break;
                            case R.id.action_simulation:
                                Intent simintent = new Intent(HomeActivity.this, ChatActivity.class);
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

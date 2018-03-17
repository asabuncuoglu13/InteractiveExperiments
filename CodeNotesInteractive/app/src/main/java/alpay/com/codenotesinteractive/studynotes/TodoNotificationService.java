package alpay.com.codenotesinteractive.studynotes;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.UUID;

import alpay.com.codenotesinteractive.R;

public class TodoNotificationService extends IntentService {
    public static final String TODOTEXT = "com.avjindersekhon.todonotificationservicetext";
    public static final String TODOUUID = "com.avjindersekhon.todonotificationserviceuuid";
    private String mTodoText;
    private UUID mTodoUUID;
    private Context mContext;

    public TodoNotificationService(){
        super("TodoNotificationService");
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        mTodoText = intent.getStringExtra(TODOTEXT);
        mTodoUUID = (UUID)intent.getSerializableExtra(TODOUUID);

        Log.d("OskarSchindler", "onHandleIntent called");
        NotificationManager manager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        Notification notification = new Notification.Builder(this)
                .setContentTitle(mTodoText)
                .setSmallIcon(R.drawable.ic_done_white_24dp)
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_SOUND)
                .build();

        manager.notify(100, notification);

    }
}

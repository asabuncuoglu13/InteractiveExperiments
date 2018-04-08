package alpay.com.codenotesinteractive;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import alpay.com.codenotesinteractive.util.Feedback;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SendFeedbackActivity extends AppCompatActivity {


    DatabaseReference ref;

    @BindView(R.id.feedbackTitleEditText)
    EditText feedbackTitleEditText;

    @BindView(R.id.feedbackDetailEditText)
    EditText feedbackDetailEditText;

    @BindView(R.id.formView)
    ScrollView formView;

    @BindView(R.id.successView)
    LinearLayout successView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_feedback);
        ButterKnife.bind(this);
        getSupportActionBar().setTitle(R.string.sendfeedback_activity_name);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        ref = FirebaseDatabase.getInstance().getReference();
        ref.keepSynced(true);

    }

    @Nullable
    @OnClick(R.id.sendFeedBackButton)
    protected void sendFeedBack() {
        String feedbackTitle = feedbackTitleEditText.getText().toString();
        String feedbackDetail = feedbackDetailEditText.getText().toString();

        if (feedbackTitle.equals("")) {
            printToastMessage(R.string.sendfeedback_formtitle_required);
            return;
        }
        if (feedbackDetail.equals("")) {
            printToastMessage(R.string.sendfeedback_formcontent_required);
            return;
        }
        sendFeedbackToFirebase(feedbackTitle, feedbackDetail);
        changeToSuccessView();
    }

    @Nullable
    @OnClick(R.id.feedbackSentOkButton)
    protected void backToLatestScreen() {
        super.onBackPressed();
    }

    protected void sendFeedbackToFirebase(String title, String detail) {
        Feedback feedback = new Feedback(title, detail);
        ref.child("userFeedback").push().setValue(feedback);
    }

    protected void changeToSuccessView() {
        formView.setVisibility(View.GONE);
        successView.setVisibility(View.VISIBLE);
    }

    private void printToastMessage(int resID) {
        Toast.makeText(this, getResources().getString(resID), Toast.LENGTH_SHORT).show();
    }

}

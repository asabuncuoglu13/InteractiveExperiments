package alpay.com.codenotesinteractive;

import android.graphics.Color;
import android.os.Bundle;

import com.hololo.tutorial.library.Step;
import com.hololo.tutorial.library.TutorialActivity;


public class TutorialCodeNotesActivity extends TutorialActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setPrevText(getResources().getString(R.string.previous)); // Previous button text
        setNextText(getResources().getString(R.string.next)); // Next button text
        setFinishText(getResources().getString(R.string.finish)); // Finish button text
        setCancelText(getResources().getString(R.string.cancel)); // Cancel button text

        addFragment(new Step.Builder()
                .setContent(getString(R.string.tutorial_content_1))
                .setBackgroundColor(Color.parseColor("#afb42b")) // int background color
                .setDrawable(R.drawable.tutorial1) // int top drawable
                .build());

        addFragment(new Step.Builder()
                .setContent(getString(R.string.tutorial_content_2))
                .setBackgroundColor(Color.parseColor("#5c6bc0")) // int background color
                .setDrawable(R.drawable.tutorial2) // int top drawable
                .build());

        addFragment(new Step.Builder()
                .setContent(getString(R.string.tutorial_content_3))
                .setBackgroundColor(Color.parseColor("#8c0032")) // int background color
                .setDrawable(R.drawable.tutorial3) // int top drawable
                .build());

        addFragment(new Step.Builder()
                .setContent(getString(R.string.tutorial_content_4))
                .setBackgroundColor(Color.parseColor("#d32f2f")) // int background color
                .setDrawable(R.drawable.tutorial4) // int top drawable
                .build());

        addFragment(new Step.Builder()
                .setContent(getString(R.string.tutorial_content_5))
                .setBackgroundColor(Color.parseColor("#c43a00")) // int background color
                .setDrawable(R.drawable.tutorial5) // int top drawable
                .build());

        addFragment(new Step.Builder()
                .setContent(getString(R.string.tutorial_content_6))
                .setBackgroundColor(Color.parseColor("#0297a7")) // int background color
                .setDrawable(R.drawable.tutorial6) // int top drawable
                .build());

    }

    @Override
    public void finishTutorial() {
        finish();
    }

    @Override
    public void onBackPressed() {
        finishTutorial();
    }

}
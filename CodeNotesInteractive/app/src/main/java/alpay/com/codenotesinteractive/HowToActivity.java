package alpay.com.codenotesinteractive;

import android.content.Intent;
import android.os.Bundle;

import com.hololo.tutorial.library.Step;
import com.hololo.tutorial.library.TutorialActivity;

public class HowToActivity extends TutorialActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setPrevText(getResources().getString(R.string.previous)); // Previous button text
        setNextText(getResources().getString(R.string.next)); // Next button text
        setFinishText(getResources().getString(R.string.finish)); // Finish button text
        setCancelText(getResources().getString(R.string.cancel)); // Cancel button text

        addFragment(new Step.Builder().setTitle(getResources().getString(R.string.tut1_welcome_title))
                .setContent(getResources().getString(R.string.tut1_welcome_content))
                .setBackgroundColor(getResources().getColor(R.color.tutorial_page1)) // int background color
                .setDrawable(R.drawable.ic_launcher_color) // int top drawable
                .build());

        addFragment(new Step.Builder().setTitle(getResources().getString(R.string.tut2_experiment_list_title))
                .setContent(getResources().getString(R.string.tut2_experiment_list_content))
                .setBackgroundColor(getResources().getColor(R.color.tutorial_page2)) // int background color
                .setDrawable(R.drawable.inclined_plane) // int top drawable
                .build());

        addFragment(new Step.Builder().setTitle(getResources().getString(R.string.tut3_choose_experiment_title))
                .setContent(getResources().getString(R.string.tut3_choose_experiment_content))
                .setBackgroundColor(getResources().getColor(R.color.tutorial_page3)) // int background color
                .setDrawable(R.drawable.ohms_law) // int top drawable
                .build());

        addFragment(new Step.Builder().setTitle(getResources().getString(R.string.tut4_parameters_title))
                .setContent(getResources().getString(R.string.tut4_parameters_content))
                .setBackgroundColor(getResources().getColor(R.color.tutorial_page4)) // int background color
                .setDrawable(R.drawable.ohms_law) // int top drawable
                .build());

        addFragment(new Step.Builder().setTitle(getResources().getString(R.string.tut5_coding_parameters_title))
                .setContent(getResources().getString(R.string.tut5_coding_parameters_content))
                .setBackgroundColor(getResources().getColor(R.color.tutorial_page5)) // int background color
                .setDrawable(R.drawable.ohms_law) // int top drawable
                .build());

        addFragment(new Step.Builder().setTitle(getResources().getString(R.string.tut6_experiment_result_title))
                .setContent(getResources().getString(R.string.tut6_experiment_result_content))
                .setBackgroundColor(getResources().getColor(R.color.tutorial_page6)) // int background color
                .setDrawable(R.drawable.ohms_law) // int top drawable
                .build());

        addFragment(new Step.Builder().setTitle(getResources().getString(R.string.tut7_chatbot_title))
                .setContent(getResources().getString(R.string.tut7_chatbot_content))
                .setBackgroundColor(getResources().getColor(R.color.tutorial_page7)) // int background color
                .setDrawable(R.drawable.ohms_law) // int top drawable
                .build());

        addFragment(new Step.Builder().setTitle(getResources().getString(R.string.tut8_navigation_title))
                .setContent(getResources().getString(R.string.tut8_navigation_title))
                .setBackgroundColor(getResources().getColor(R.color.tutorial_page8)) // int background color
                .setDrawable(R.drawable.ohms_law) // int top drawable
                .build());

        addFragment(new Step.Builder().setTitle(getResources().getString(R.string.tut9_finish_title))
                .setContent(getResources().getString(R.string.tut9_finish_content))
                .setBackgroundColor(getResources().getColor(R.color.tutorial_page9)) // int background color
                .setDrawable(R.drawable.ohms_law) // int top drawable
                .build());
    }

    @Override
    public void finishTutorial() {
        Intent intent = new Intent(this, BaseActivity.class);
        startActivity(intent);
    }
}

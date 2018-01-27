package alpay.com.codenotesinteractive.simulation.simulation_fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import alpay.com.codenotesinteractive.R;
import alpay.com.codenotesinteractive.simulation.SimulationParameters;

public class ConstantAccelerationSimulationFragment extends Fragment implements View.OnClickListener{

    public View view;
    private WebView webView;

    private String simulationName = "";
    public int forceview_selection = -1;
    public int slowmotion_selection = -1;
    private EditText positionText;
    private EditText velocityText;
    private EditText accelerationText;
    public int[] parameters = new int[3];
    private static final String TAG = "ConstantAccSimulation";

    public ConstantAccelerationSimulationFragment() {

    }

    public void setParameters(int[] params) {
        for(int i= 0; i<params.length-1; i++)
        {
            if(params[i] == SimulationParameters.POSITION)
            {
                parameters[0] = params[i+1];
            }
            if(params[i] == SimulationParameters.VELOCITY)
            {
                parameters[1] = params[i+1];
            }
            if(params[i] == SimulationParameters.ACCELERATION)
            {
                parameters[2] = params[i+1];
            }
        }
    }


    public void setSimulation(String simulationName)
    {
        this.simulationName = simulationName;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_web_simulation, container, false);
        webView = (WebView) view.findViewById(R.id.web_view);
        webView.setWebChromeClient(new WebChromeClient() {});
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webView.addJavascriptInterface(new JavaScriptInterface(this.getContext()), "Android");

        positionText = (EditText) view.findViewById(R.id.parameter1);
        velocityText = (EditText) view.findViewById(R.id.parameter2);
        accelerationText = (EditText) view.findViewById(R.id.parameter3);

        positionText.setHint(R.string.position);
        velocityText.setHint(R.string.velocity);
        accelerationText.setHint(R.string.acceleration);


        view.findViewById(R.id.setParameters).setOnClickListener(this);
        view.findViewById(R.id.resetButton).setOnClickListener(this);

        webView.loadUrl("file:///android_asset/ConstantAcceleration/index.html");

        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        return view;
    }

    public class JavaScriptInterface {
        Context mContext;
        JavaScriptInterface(Context c) {
            mContext = c;
        }
        @JavascriptInterface
        public int getPosition() {
            return parameters[0];
        }
        @JavascriptInterface
        public int getVelocity() {
            return parameters[1];
        }
        @JavascriptInterface
        public int getAcceleration() {
            return parameters[2];
        }
    }

    public int[] getParameters()
    {
        String p_text = positionText.getText().toString();
        String a_text = accelerationText.getText().toString();
        String v_text = velocityText.getText().toString();

        if(!(p_text.matches("") || v_text.matches("") || a_text.matches("")))
        {
            int[] params = new int[3];
            params[0] = Integer.valueOf(p_text);
            params[1] = Integer.valueOf(v_text);
            params[2] = Integer.valueOf(a_text);
            Toast.makeText(this.getContext(), "Parameters are set to: Position: "+ params[0] +", Velocity: "+ params[1] +", Acceleration: "+ params[2], Toast.LENGTH_SHORT).show();
            return params;
        }else
        {
            Toast.makeText(this.getContext(), R.string.all_text_required, Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    public void setParametersToDefault()
    {
        accelerationText.setText("");
        velocityText.setText("");
        positionText.setText("");

        parameters[0] = 0; //position
        parameters[1] = 10; //velocity
        parameters[2] = 1; //acceleration
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if(i == R.id.forceview_selection)
        {
            boolean checked = ((RadioButton) view).isChecked();
            // Check which radio button was clicked
            switch(view.getId()) {
                case R.id.forcevector:
                    if (checked)
                        forceview_selection = 1;
                    break;
                case R.id.springscale:
                    if (checked)
                        forceview_selection = 2;
                    break;
            }
        }else if(i == R.id.slow_motion)
        {
            slowmotion_selection = 1;
        }else if(i == R.id.setParameters)
        {
            parameters = getParameters();
        }else if(i == R.id.resetButton)
        {
            setParametersToDefault();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(getActivity().findViewById(R.id.baseactivity_view) != null)
            getActivity().findViewById(R.id.baseactivity_view).setVisibility(View.VISIBLE);
    }
}
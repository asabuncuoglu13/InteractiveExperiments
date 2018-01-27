package alpay.com.codenotesinteractive.simulation.simulation_fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
    private int p, v, a;
    private static final String TAG = "InclinedPlaneSimulationFragment";

    public ConstantAccelerationSimulationFragment() {

    }

    public void setParameters(int[] params) {
        int cnt = 0;
        for(int i : params)
        {
            if(i==0)
            {
                break;
            }
            if(i == SimulationParameters.POSITION)
            {
                this.p = parameters[cnt+1];
            }
            if(i == SimulationParameters.VELOCITY)
            {
                this.v = parameters[cnt+1];
            }
            if(i == SimulationParameters.ACCELERATION)
            {
                this.a = parameters[cnt+1];
            }
            cnt++;
        }
        parameters[0] = p;
        parameters[1] = v;
        parameters[2] = a;
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
            p = Integer.valueOf(p_text);
            v = Integer.valueOf(v_text);
            a = Integer.valueOf(a_text);
            Toast.makeText(this.getContext(), "Parameters are set to: Angle: "+ p +", Weight: "+ v +", Coeff. Of Friction: "+ a, Toast.LENGTH_SHORT).show();
            int[] params = new int[3];
            params[0] = p;
            params[1] = v;
            params[2] = a;
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

        parameters[0] = 25; //angle
        parameters[1] = 2; //weight
        parameters[2] = 1; //friction
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
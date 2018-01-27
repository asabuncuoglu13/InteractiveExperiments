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
    private EditText weightText;
    private EditText frictionText;
    private EditText angleText;
    public int[] parameters = new int[3];
    private int a, w, f;
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
            if(i == SimulationParameters.ANGLE)
            {
                this.a = parameters[cnt+1];
            }
            if(i == SimulationParameters.WEIGHT)
            {
                this.w = parameters[cnt+1];
            }
            if(i == SimulationParameters.FRICTION)
            {
                this.f = parameters[cnt+1];
            }
            cnt++;
        }
        parameters[0] = a;
        parameters[1] = w;
        parameters[2] = f;
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

        weightText = (EditText) view.findViewById(R.id.weight);
        frictionText = (EditText) view.findViewById(R.id.coeff_friction);
        angleText = (EditText) view.findViewById(R.id.incline_angle);
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
        public int getAngle() {
            return parameters[0];
        }
        @JavascriptInterface
        public int getWeight() {
            return parameters[1];
        }
        @JavascriptInterface
        public int getFriction() {
            return parameters[2];
        }
        @JavascriptInterface
        public int getForceViewSelection() {
            return forceview_selection;
        }
        @JavascriptInterface
        public int getSlowMotionSelection() {
            return forceview_selection;
        }
    }

    public int[] getParameters()
    {
        String w_text = weightText.getText().toString();
        String a_text = angleText.getText().toString();
        String f_text = frictionText.getText().toString();

        if(!(w_text.matches("") || f_text.matches("") || a_text.matches("")))
        {
            a = Integer.valueOf(a_text);
            w = Integer.valueOf(w_text);
            f = Integer.valueOf(f_text);
            Toast.makeText(this.getContext(), "Parameters are set to: Angle: "+a+", Weight: "+w+", Coeff. Of Friction: "+f, Toast.LENGTH_SHORT).show();
            int[] params = new int[3];
            params[0] = a;
            params[1] = w;
            params[2] = f;
            return params;
        }else
        {
            Toast.makeText(this.getContext(), R.string.all_text_required, Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    public void setParametersToDefault()
    {
        angleText.setText("");
        frictionText.setText("");
        weightText.setText("");

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
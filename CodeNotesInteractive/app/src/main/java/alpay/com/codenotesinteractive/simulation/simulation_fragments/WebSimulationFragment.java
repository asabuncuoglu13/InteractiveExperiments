package alpay.com.codenotesinteractive.simulation.simulation_fragments;


import android.content.Context;
import android.content.Intent;
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

public class WebSimulationFragment extends Fragment implements View.OnClickListener{

    public View view;
    private WebView webView;
    private String simulationName = "";
    public int forceview_selection = 0;
    private EditText weightText;
    private EditText frictionText;
    private EditText angleText;
    public int[] parameters;
    private static final String TAG = "WebSimulationFragment";

    public WebSimulationFragment() {
        // Required empty public constructor
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


        if(simulationName.compareTo("inclinedplane") == 0)
            webView.loadUrl("file:///android_asset/InclinedPlane/index.html");
        else
            webView.loadUrl("file:///android_asset/nothing-to-see-here.gif");

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
    }

    public int[] getParameters()
    {
        int a, w, f;

        String w_text = weightText.getText().toString();
        String a_text = angleText.getText().toString();
        String f_text = frictionText.getText().toString();

        if(!(w_text.matches("") || f_text.matches("") || a_text.matches("")))
        {
            a = Integer.valueOf(a_text);
            w = Integer.valueOf(w_text);
            f = Integer.valueOf(f_text);
            Log.d(TAG, "Values of awf: "+a+" "+w+" "+f);
            Toast.makeText(this.getContext(), "Values of awf: "+a+" "+w+" "+f, Toast.LENGTH_SHORT).show();
            int[] params = {a,w,f};
            return params;
        }else
        {
            Toast.makeText(this.getContext(), R.string.all_text_required, Toast.LENGTH_SHORT).show();
            return null;
        }
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
        }else if(i == R.id.setParameters)
        {
            parameters = getParameters();
        }else if(i == R.id.resetButton)
        {

        }
    }
}

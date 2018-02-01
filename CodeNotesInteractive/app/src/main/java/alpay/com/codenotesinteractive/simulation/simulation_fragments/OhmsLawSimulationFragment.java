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

import alpay.com.codenotesinteractive.R;
import alpay.com.codenotesinteractive.Utility;
import alpay.com.codenotesinteractive.simulation.SimulationParameters;

public class OhmsLawSimulationFragment extends Fragment implements View.OnClickListener {

    public View view;
    private WebView webView;

    private String simulationName = "";
    public int[] parameters = {0, 0, 0}; // angle, weight, friction
    private static final String TAG = "OhmsLaw";

    public OhmsLawSimulationFragment() {

    }

    public void setParameters(int[] params) {
        for (int i = 0; i < params.length - 1; i++) {
            if (params[i] == SimulationParameters.ANGLE) {
                parameters[0] = params[i + 1];
            }
            if (params[i] == SimulationParameters.WEIGHT) {
                parameters[1] = params[i + 1];
            }
            if (params[i] == SimulationParameters.FRICTION) {
                parameters[2] = params[i + 1];
            }
        }
    }


    public void setSimulation(String simulationName) {
        this.simulationName = simulationName;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_web_simulation, container, false);
        view.findViewById(R.id.parameter_layout).setVisibility(View.GONE);
        webView = (WebView) view.findViewById(R.id.web_view);
        webView.setWebChromeClient(new WebChromeClient() {
        });
        webView.setPadding(0, 0, 0, 0);
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.setInitialScale(Utility.getScale(getActivity(), SimulationParameters.OHMS_LAW_SCREEN_SIZE));
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webView.addJavascriptInterface(new JavaScriptInterface(this.getContext()), "Android");
        webView.loadUrl("file:///android_asset/OhmsLaw/index.html");
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
        public int getAmper() {
            return parameters[0];
        }

        @JavascriptInterface
        public int getVolt() {
            return parameters[1];
        }

        @JavascriptInterface
        public int getResistance() {
            return parameters[2];
        }
    }


    public void setParametersToDefault() {
        parameters[0] = 0; //amper
        parameters[1] = 0; //volt
        parameters[2] = 1; //resistance
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (getActivity().findViewById(R.id.baseactivity_view) != null)
            getActivity().findViewById(R.id.baseactivity_view).setVisibility(View.VISIBLE);
    }
}
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
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetView;

import org.angmarch.views.NiceSpinner;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import alpay.com.codenotesinteractive.R;
import alpay.com.codenotesinteractive.Utility;
import alpay.com.codenotesinteractive.simulation.SimulationParameters;

public class PulleySimulationFragment extends Fragment implements View.OnClickListener {

    public View view;
    private WebView webView;
    private String simulationName = "";
    public double[] parameters = {0.0, 0.0}; // weight, pulley_weight
    private static final String TAG = "PulleySimulation";

    public PulleySimulationFragment() {

    }

    public void setParameters(double[] params) {
        for (int i = 0; i < params.length - 1; i++) {
            if (params[i] == SimulationParameters.PULLEY_WEIGHT) {
                parameters[0] = params[i + 1];
            }
            if (params[i] == SimulationParameters.WEIGHT) {
                parameters[1] = params[i + 1];
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
        TextView paramtext1 = (TextView) view.findViewById(R.id.param1_text);
        TextView paramtext2 = (TextView) view.findViewById(R.id.param2_text);
        paramtext1.setText(SimulationParameters.PULLEY_PARAMETER_TEXTS[0]);
        paramtext2.setText(SimulationParameters.PULLEY_PARAMETER_TEXTS[1]);
        if (SimulationParameters.showTapTarget) {
            TapTargetView.showFor(getActivity(),                 // `this` is an Activity
                    TapTarget.forView(view.findViewById(R.id.setParameters), getString(R.string.tap_target_title), getString(R.string.tap_target_detail))
                            // All options below are optional
                            .outerCircleColor(R.color.colorPrimaryDark)      // Specify a color for the outer circle
                            .outerCircleAlpha(0.96f)            // Specify the alpha amount for the outer circle
                            .drawShadow(true)                   // Whether to draw a drop shadow or not
                            .cancelable(true)                  // Whether tapping outside the outer circle dismisses the view
                            .tintTarget(true)                   // Whether to tint the target view's color
                            .transparentTarget(true)           // Specify whether the target is transparent (displays the content underneath)
                            .targetRadius(10),                  // Specify the target radius (in dp)
                    new TapTargetView.Listener() {          // The listener can listen for regular clicks, long clicks or cancels
                        @Override
                        public void onTargetClick(TapTargetView view) {
                            super.onTargetClick(view);      // This call is optional
                            //
                        }
                    });
        }

        SimulationParameters.showTapTarget = false;
        webView = (WebView) view.findViewById(R.id.web_view);
        webView.setWebChromeClient(new WebChromeClient() {
        });
        webView.setPadding(0, 0, 0, 0);
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.setInitialScale(Utility.getScale(getActivity(), SimulationParameters.PULLEY_SCREEN_SIZE));
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webView.addJavascriptInterface(new JavaScriptInterface(this.getContext()), "Android");

        NiceSpinner weight_spinner = (NiceSpinner) view.findViewById(R.id.param1_spinner);
        final List<Double> weight_dataset = new LinkedList<>(Arrays.asList(12.0, 14.0, 16.0));
        weight_spinner.attachDataSource(weight_dataset);

        weight_spinner.addOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                parameters[0] = weight_dataset.get(position);
            }
        });

        NiceSpinner pulleyweight_spinner = (NiceSpinner) view.findViewById(R.id.param2_spinner);
        final List<Double> pulleyweight_dataset = new LinkedList<>(Arrays.asList(4.0, 6.0, 8.0));
        pulleyweight_spinner.attachDataSource(pulleyweight_dataset);

        pulleyweight_spinner.addOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                parameters[1] = pulleyweight_dataset.get(position);
            }
        });

        view.findViewById(R.id.param3_spinner).setVisibility(View.GONE);

        view.findViewById(R.id.setParameters).setOnClickListener(this);

        webView.loadUrl("file:///android_asset/Pulley/index.html");
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
        public double getPulleyWeight() {
            return parameters[0];
        }

        @JavascriptInterface
        public double getWeight() {
            return parameters[1];
        }

    }

    public double[] getParameters() {

        if (parameters[0] > 0 && parameters[1] > 0) {
            return parameters;
        } else {
            Toast.makeText(this.getContext(), R.string.all_text_required, Toast.LENGTH_SHORT).show();
            return parameters;
        }
    }

    public void setParametersToDefault() {
        parameters[0] = 10; //weight
        parameters[1] = 5; //pulley_weight
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.setParameters) {
            parameters = getParameters();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (getActivity().findViewById(R.id.baseactivity_view) != null)
            getActivity().findViewById(R.id.baseactivity_view).setVisibility(View.VISIBLE);
    }
}
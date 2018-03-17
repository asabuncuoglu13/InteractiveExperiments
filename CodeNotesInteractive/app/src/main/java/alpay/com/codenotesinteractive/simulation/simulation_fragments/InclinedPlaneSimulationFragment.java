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

public class InclinedPlaneSimulationFragment extends Fragment implements View.OnClickListener {

    public View view;
    private WebView webView;
    private String simulationName = "";
    public double[] parameters = {0.0, 0.0, 0.0}; // angle, weight, friction
    private static final String TAG = "InclinedPlaneSimulation";

    public InclinedPlaneSimulationFragment() {

    }

    public void setParameters(double[] params) {
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
        TextView paramtext1 = (TextView) view.findViewById(R.id.param1_text);
        TextView paramtext2 = (TextView) view.findViewById(R.id.param2_text);
        TextView paramtext3 = (TextView) view.findViewById(R.id.param3_text);
        paramtext1.setText(SimulationParameters.INCLINED_PLANE_PARAMETER_TEXTS[0]);
        paramtext2.setText(SimulationParameters.INCLINED_PLANE_PARAMETER_TEXTS[1]);
        paramtext3.setText(SimulationParameters.INCLINED_PLANE_PARAMETER_TEXTS[2]);
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
        webView.setInitialScale(Utility.getScale(getActivity(), SimulationParameters.INCLINED_PLANE_SCREEN_SIZE));
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webView.addJavascriptInterface(new JavaScriptInterface(this.getContext()), "Android");

        NiceSpinner angle_spinner = (NiceSpinner) view.findViewById(R.id.param1_spinner);
        final List<Double> angle_dataset = new LinkedList<>(Arrays.asList(5.0, 7.0, 9.0));
        angle_spinner.attachDataSource(angle_dataset);

        angle_spinner.addOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                parameters[0] = angle_dataset.get(position);
            }
        });

        NiceSpinner weight_spinner = (NiceSpinner) view.findViewById(R.id.param2_spinner);
        final List<Double> weight_dataset = new LinkedList<>(Arrays.asList(2.0, 3.0, 4.0));
        weight_spinner.attachDataSource(weight_dataset);

        weight_spinner.addOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                parameters[1] = weight_dataset.get(position);
            }
        });

        NiceSpinner friction_spinner = (NiceSpinner) view.findViewById(R.id.param3_spinner);
        final List<Double> friction_dataset = new LinkedList<>(Arrays.asList(0.2, 0.4, 0.6, 0.8, 1.0));
        friction_spinner.attachDataSource(friction_dataset);

        friction_spinner.addOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                parameters[2] = friction_dataset.get(position);
            }
        });

        view.findViewById(R.id.setParameters).setOnClickListener(this);

        webView.loadUrl("file:///android_asset/InclinedPlane/index.html");
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
        public double getAngle() {
            return parameters[0];
        }

        @JavascriptInterface
        public double getWeight() {
            return parameters[1];
        }

        @JavascriptInterface
        public double getFriction() {
            return parameters[2];
        }
    }

    public double[] getParameters() {

        if (parameters[0] > 0 && parameters[1] > 0 && parameters[2] > 0) {
            return parameters;
        } else {
            Toast.makeText(this.getContext(), R.string.all_text_required, Toast.LENGTH_SHORT).show();
            return parameters;
        }
    }

    public void setParametersToDefault() {
        parameters[0] = 25; //angle
        parameters[1] = 2; //weight
        parameters[2] = 1; //friction
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.setParameters) {
            parameters = getParameters();
        }
    }

}
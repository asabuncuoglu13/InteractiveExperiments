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
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Toast;

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
        view.findViewById(R.id.resetButton).setOnClickListener(this);

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
        } else if (i == R.id.resetButton) {
            setParametersToDefault();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (getActivity().findViewById(R.id.baseactivity_view) != null)
            getActivity().findViewById(R.id.baseactivity_view).setVisibility(View.VISIBLE);
    }
}
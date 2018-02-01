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
import android.widget.TextView;
import android.widget.Toast;

import com.franmontiel.fullscreendialog.FullScreenDialogFragment;

import org.angmarch.views.NiceSpinner;
import org.w3c.dom.Text;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import alpay.com.codenotesinteractive.R;
import alpay.com.codenotesinteractive.Utility;
import alpay.com.codenotesinteractive.simulation.Simulation;
import alpay.com.codenotesinteractive.simulation.SimulationParameters;

public class ConstantAccelerationSimulationFragment extends Fragment implements View.OnClickListener {

    public View view;
    private WebView webView;

    private String simulationName = "";
    public double[] parameters = {0.0, 0.0, 0.0};
    private FullScreenDialogFragment dialogFragment;
    private static final String TAG = "ConstantAccSimulation";

    public ConstantAccelerationSimulationFragment() {

    }

    public void setParameters(int[] params) {
        for (int i = 0; i < params.length - 1; i++) {
            if (params[i] == SimulationParameters.POSITION) {
                parameters[0] = params[i + 1];
            }
            if (params[i] == SimulationParameters.VELOCITY) {
                parameters[1] = params[i + 1];
            }
            if (params[i] == SimulationParameters.ACCELERATION) {
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
        TextView positiontext = (TextView) view.findViewById(R.id.param1_text);
        TextView velocitytext = (TextView) view.findViewById(R.id.param2_text);
        TextView accelerationtext = (TextView) view.findViewById(R.id.param3_text);
        positiontext.setText(SimulationParameters.CONSTANT_ACCELERATION_PARAMETER_TEXTS[0]);
        velocitytext.setText(SimulationParameters.CONSTANT_ACCELERATION_PARAMETER_TEXTS[1]);
        accelerationtext.setText(SimulationParameters.CONSTANT_ACCELERATION_PARAMETER_TEXTS[2]);

        webView = (WebView) view.findViewById(R.id.web_view);
        webView.setWebChromeClient(new WebChromeClient() {
        });
        webView.setPadding(0, 0, 0, 0);
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.setInitialScale(Utility.getScale(getActivity(), SimulationParameters.CONSTANT_ACCELERATION_SCREEN_SIZE));
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);

        NiceSpinner position_spinner = (NiceSpinner) view.findViewById(R.id.param1_spinner);
        final List<Double> position_dataset = new LinkedList<>(Arrays.asList(5.0, 7.0, 9.0));
        position_spinner.attachDataSource(position_dataset);

        position_spinner.addOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "onItemClick: " + position_dataset.get(position));
                parameters[0] = position_dataset.get(position);
            }
        });

        NiceSpinner velocity_spinner = (NiceSpinner) view.findViewById(R.id.param2_spinner);
        final List<Double> velocity_dataset = new LinkedList<>(Arrays.asList(2.0, 3.0, 4.0));
        velocity_spinner.attachDataSource(velocity_dataset);

        velocity_spinner.addOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "onItemClick: " + velocity_dataset.get(position));
                parameters[1] = velocity_dataset.get(position);
            }
        });

        NiceSpinner acceleration_spinner = (NiceSpinner) view.findViewById(R.id.param3_spinner);
        final List<Double> acceleration_dataset = new LinkedList<>(Arrays.asList(0.2, 0.4, 0.6, 0.8, 1.0));
        acceleration_spinner.attachDataSource(acceleration_dataset);

        acceleration_spinner.addOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "onItemClick: " + acceleration_dataset.get(position));
                parameters[2] = acceleration_dataset.get(position);
            }
        });

        WebSettings webSettings = webView.getSettings();
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webView.addJavascriptInterface(new JavaScriptInterface(this.getContext()), "Android");

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
        public double getPosition() {
            return parameters[0];
        }

        @JavascriptInterface
        public double getVelocity() {
            return parameters[1];
        }

        @JavascriptInterface
        public double getAcceleration() {
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
        parameters[0] = 0; //position
        parameters[1] = 10; //velocity
        parameters[2] = 1; //acceleration
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
package alpay.com.codenotesinteractive.simulation.simulation_fragments;


import android.content.Context;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import alpay.com.codenotesinteractive.R;
import alpay.com.codenotesinteractive.Utility;
import alpay.com.codenotesinteractive.simulation.SimulationParameters;
import butterknife.BindView;
import butterknife.OnClick;

public class ConstantAccelerationSimulationFragment extends BaseJavaScriptSimulationFragment {

    public View view;
    public double[] parameters = {0.0, 0.0, 0.0}; // weight, pulley_weight

    @Nullable
    @BindView(R.id.textInputParameter1)
    TextInputEditText paramtext1;

    @Nullable
    @BindView(R.id.textInputParameter2)
    TextInputEditText paramtext2;

    @Nullable
    @BindView(R.id.textInputParameter3)
    TextInputEditText paramtext3;


    @Override
    public void setWebView() {
        super.urlString = "file:///android_asset/ConstantAcceleration/index.html";
        super.setWebView();
        super.webView.addJavascriptInterface(new JavaScriptInterface(this.getContext()), "Android");
        super.webView.setInitialScale(Utility.getScale(getActivity(), SimulationParameters.CONSTANT_ACCELERATION_SCREEN_SIZE));
        prepareViews();
    }

    @Override
    public void setParametersWithCoding(double[] params) {
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

    public void prepareViews() {
        paramtext1.setHint(SimulationParameters.CONSTANT_ACCELERATION_PARAMETER_TEXTS[0]);
        paramtext2.setHint(SimulationParameters.CONSTANT_ACCELERATION_PARAMETER_TEXTS[1]);
        paramtext3.setHint(SimulationParameters.CONSTANT_ACCELERATION_PARAMETER_TEXTS[2]);
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

    @Nullable
    @OnClick(R.id.setParameters)
    public void setParameters() {
        if (paramtext1.getText().toString() == "" || paramtext2.getText().toString() == "" || paramtext3.getText().toString() == "") {
            Toast.makeText(getActivity(), R.string.tap_target_detail, Toast.LENGTH_SHORT).show();
            return;
        } else {
            parameters[0] = Double.valueOf(paramtext1.getText().toString());
            parameters[1] = Double.valueOf(paramtext2.getText().toString());
            parameters[2] = Double.valueOf(paramtext3.getText().toString());
        }
    }
}
package alpay.com.interactiveexperiments.simulation.simulation_fragments;


import android.content.Context;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import ai.api.util.StringUtils;
import alpay.com.interactiveexperiments.R;
import alpay.com.interactiveexperiments.simulation.SimulationParameters;
import alpay.com.interactiveexperiments.util.Utility;
import butterknife.BindView;
import butterknife.OnClick;

public class PulleySimulationFragment extends BaseJavaScriptSimulationFragment {

    public View view;
    public double[] parameters = {0.0, 0.0}; // weight, pulley_weight

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
        super.setWebView();
        super.webView.addJavascriptInterface(new JavaScriptInterface(this.getContext()), "Android");
        super.webView.loadUrl("file:///android_asset/Pulley/index.html");
        super.webView.setInitialScale(Utility.getScale(getActivity(), SimulationParameters.PULLEY_SCREEN_SIZE));
        prepareViews();
    }

    @Override
    public void setParametersWithCoding(double[] params) {
        for (int i = 0; i < params.length - 1; i++) {
            if (params[i] == SimulationParameters.PULLEY_WEIGHT) {
                parameters[0] = params[i + 1];
            }
            if (params[i] == SimulationParameters.WEIGHT) {
                parameters[1] = params[i + 1];
            }
        }
    }

    public void prepareViews() {
        // Inflate the layout for this fragment
        paramtext1.setHint(SimulationParameters.PULLEY_PARAMETER_TEXTS[0]);
        paramtext2.setHint(SimulationParameters.PULLEY_PARAMETER_TEXTS[1]);
        paramtext3.setVisibility(View.GONE);
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

    @Nullable
    @OnClick(R.id.setParameters)
    public void setParameters() {
        if (StringUtils.isEmpty(paramtext1.getText().toString()) || StringUtils.isEmpty(paramtext2.getText().toString())) {
            Toast.makeText(getActivity(), R.string.paramter_error, Toast.LENGTH_SHORT).show();
            return;
        } else {
            parameters[0] = Double.valueOf(paramtext1.getText().toString());
            parameters[1] = Double.valueOf(paramtext2.getText().toString());
        }
    }

}
package alpay.com.interactiveexperiments.simulation.simulation_fragments;


import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;

import alpay.com.interactiveexperiments.R;
import alpay.com.interactiveexperiments.simulation.SimulationParameters;
import alpay.com.interactiveexperiments.util.Utility;
import butterknife.BindView;

public class LeverSimulationFragment extends BaseJavaScriptSimulationFragment {

    @Nullable
    @BindView(R.id.parameter_layout)
    LinearLayout parameterLayout;

    @Override
    public void setWebView() {
        super.setWebView();
        super.webView.setInitialScale(Utility.getScale(getActivity(), SimulationParameters.LEVER_SCREEN_SIZE));
        super.webView.loadUrl("file:///android_asset/Lever/index.html");
        parameterLayout.setVisibility(View.INVISIBLE);
    }

    @Override
    public void setParametersWithCoding(double[] parameters) {
        //This simulation does not have coding cards
    }
}
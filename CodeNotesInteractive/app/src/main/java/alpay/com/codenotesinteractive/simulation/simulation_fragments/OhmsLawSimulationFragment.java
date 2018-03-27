package alpay.com.codenotesinteractive.simulation.simulation_fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;

import alpay.com.codenotesinteractive.R;
import alpay.com.codenotesinteractive.Utility;
import alpay.com.codenotesinteractive.simulation.Simulation;
import alpay.com.codenotesinteractive.simulation.SimulationParameters;
import butterknife.BindView;

public class OhmsLawSimulationFragment extends BaseJavaScriptSimulationFragment {

    @Nullable
    @BindView(R.id.parameter_layout)
    LinearLayout parameterLayout;

    @Override
    public void setWebView() {;
        super.urlString = "file:///android_asset/OhmsLaw/index.html";
        super.setWebView();
        super.webView.setInitialScale(Utility.getScale(getActivity(), SimulationParameters.OHMS_LAW_SCREEN_SIZE));
        parameterLayout.setVisibility(View.INVISIBLE);
    }

    @Override
    public void setParametersWithCoding(double[] parameters) {
        //This simulation does not have coding module.
    }
}
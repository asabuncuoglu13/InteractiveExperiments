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

public class LeverSimulationFragment extends Fragment implements View.OnClickListener {

    public View view;
    private WebView webView;

    private String simulationName = "";
    private static final String TAG = "Lever";

    public LeverSimulationFragment() {

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
        webView.setInitialScale(Utility.getScale(getActivity(), SimulationParameters.LEVER_SCREEN_SIZE));
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webView.loadUrl("file:///android_asset/Lever/index.html");
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        return view;
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
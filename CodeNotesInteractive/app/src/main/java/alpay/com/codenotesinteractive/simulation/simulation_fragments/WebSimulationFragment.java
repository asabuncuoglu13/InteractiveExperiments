package alpay.com.codenotesinteractive.simulation.simulation_fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

import alpay.com.codenotesinteractive.R;

public class WebSimulationFragment extends Fragment {

    public View view;
    private WebView webView;
    private String simulationName = "";

    public WebSimulationFragment() {
        // Required empty public constructor
    }


    public void setSimulation(String simulationName)
    {
        this.simulationName = simulationName;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_web_simulation, container, false);
        webView = (WebView) view.findViewById(R.id.web_view);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        if(simulationName.compareTo("inclinedplane") == 0)
            webView.loadUrl("http://www.walter-fendt.de/html5/phen/inclinedplane_en.htm");
        else
            webView.loadUrl("file:///android_asset/nothing-to-see-here.gif");

        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        return view;
    }

}

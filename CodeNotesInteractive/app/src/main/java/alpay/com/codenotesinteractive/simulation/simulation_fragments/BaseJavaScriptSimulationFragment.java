package alpay.com.codenotesinteractive.simulation.simulation_fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.FrameLayout;

import alpay.com.codenotesinteractive.R;
import alpay.com.codenotesinteractive.Utility;
import alpay.com.codenotesinteractive.simulation.SimulationParameters;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseJavaScriptSimulationFragment extends Fragment {

    @BindView(R.id.web_view)
    WebView webView;
    String urlString;

    Unbinder unbinder;

    public abstract void setParametersWithCoding(double[] parameters);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_web_simulation, container, false);
        unbinder = ButterKnife.bind(this, view);
        setWebView();
        return view;
    }


    public void setWebView()
    {
        webView.setWebChromeClient(new WebChromeClient() {
        });
        webView.setPadding(0, 0, 0, 0);
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.setInitialScale(Utility.getScale(getActivity(), SimulationParameters.OHMS_LAW_SCREEN_SIZE));
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webView.loadUrl(urlString);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
    }

}

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
import android.widget.EditText;
import android.widget.RadioButton;

import alpay.com.codenotesinteractive.R;

public class WebSimulationFragment extends Fragment implements View.OnClickListener{

    public View view;
    private WebView webView;
    private String simulationName = "";
    public int forceview_selection = 0;
    private EditText weightText;
    private EditText frictionText;
    private EditText angleText;

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
        webView.setWebChromeClient(new WebChromeClient() {});
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webView.addJavascriptInterface(new JavaScriptInterface(this.getContext()), "Android");

        if(simulationName.compareTo("inclinedplane") == 0)
            webView.loadUrl("file:///android_asset/InclinedPlane/index.html");
        else
            webView.loadUrl("file:///android_asset/nothing-to-see-here.gif");

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
        public int getFromAndroid() {
            return 20;
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if(i == R.id.forceview_selection)
        {
            boolean checked = ((RadioButton) view).isChecked();
            // Check which radio button was clicked
            switch(view.getId()) {
                case R.id.forcevector:
                    if (checked)
                        forceview_selection = 1;
                        break;
                case R.id.springscale:
                    if (checked)
                        forceview_selection = 2;
                        break;
            }
        }else if(i == R.id.startButton)
        {
            JavaScriptInterface jif = new JavaScriptInterface(this.getContext());
            jif.getFromAndroid();
        }else if(i == R.id.resetButton)
        {

        }
    }
}

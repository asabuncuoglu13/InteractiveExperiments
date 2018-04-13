package alpay.com.codenotesinteractive.simulation.simulation_fragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import alpay.com.codenotesinteractive.R;
import alpay.com.codenotesinteractive.chat.ChatDialogFragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseJavaScriptSimulationFragment extends Fragment {

    @BindView(R.id.web_view)
    WebView webView;
    Unbinder unbinder;
    FloatingActionButton floatingActionButton;

    public abstract void setParametersWithCoding(double[] parameters);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_web_simulation, container, false);
        setFABAction();
        unbinder = ButterKnife.bind(this, view);
        setWebView();
        return view;
    }

    private void setFABAction() {
        floatingActionButton = (FloatingActionButton) getActivity().findViewById(R.id.button_fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @SuppressWarnings("deprecation")
            @Override
            public void onClick(View v) {
                showChatDialog();
            }
        });
        floatingActionButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_question_chat));
    }

    void showChatDialog() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        DialogFragment newFragment = ChatDialogFragment.newInstance();
        newFragment.show(ft, "dialog");
    }

    public void setWebView() {
        WebSettings webSettings = webView.getSettings();
        webView.setWebChromeClient(new WebChromeClient() {
        });
        webView.setPadding(0, 0, 0, 0);
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
    }

    @Override
    public void onDestroy() {
        webView.destroy();
        floatingActionButton.setOnClickListener(null);
        super.onDestroy();
        unbinder.unbind();
    }
}

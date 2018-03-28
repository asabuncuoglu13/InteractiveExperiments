package alpay.com.codenotesinteractive.home;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import alpay.com.codenotesinteractive.R;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class HomeFragment extends Fragment {


    Unbinder unbinder;
    View view;

    public interface OnCategoryListFragmentInteractionListener {
        void onCategoryListFragmentInteraction(CategoryRecyclerViewAdapter.Category item);
    }

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);

        return view;
    }


    @Override
    public void onDestroy() {
        unbinder.unbind();
        super.onDestroy();
    }
}

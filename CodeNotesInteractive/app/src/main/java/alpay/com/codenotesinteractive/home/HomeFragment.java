package alpay.com.codenotesinteractive.home;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import alpay.com.codenotesinteractive.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class HomeFragment extends Fragment implements CategoryRecyclerViewAdapter.OnCategoryListFragmentInteractionListener{


    Unbinder unbinder;
    View view;
    @BindView(R.id.categorySelectView)
    RecyclerView categoryRecyclerView;
    CategoryRecyclerViewAdapter.OnCategoryListFragmentInteractionListener categoryListFragmentInteractionListener;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        prepareCategoryItems();
        return view;
    }

    public void prepareCategoryItems()
    {
        CategoryRecyclerViewAdapter.Category.ITEMS.add(new CategoryRecyclerViewAdapter.Category(getActivity(),"Hello", R.drawable.ball));
        CategoryRecyclerViewAdapter.Category.ITEMS.add(new CategoryRecyclerViewAdapter.Category(getActivity(),"Hello", R.drawable.ball));
        categoryRecyclerView.setAdapter(new CategoryRecyclerViewAdapter(CategoryRecyclerViewAdapter.Category.ITEMS, categoryListFragmentInteractionListener));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof CategoryRecyclerViewAdapter.OnCategoryListFragmentInteractionListener) {
            categoryListFragmentInteractionListener = (CategoryRecyclerViewAdapter.OnCategoryListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        categoryListFragmentInteractionListener = null;
    }


    @Override
    public void onDestroy() {
        unbinder.unbind();
        super.onDestroy();
    }

    @Override
    public void onCategoryListFragmentInteraction(CategoryRecyclerViewAdapter.Category item) {

    }
}

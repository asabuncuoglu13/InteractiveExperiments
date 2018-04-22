package alpay.com.interactiveexperiments.home;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import alpay.com.interactiveexperiments.R;
import alpay.com.interactiveexperiments.util.FragmentManager;

/*
Note: There is a Fragment Call inside this this Fragment.
ButterKnife causes error at onDestroy() method. Do not use it.
 */

public class HomeFragment extends Fragment implements View.OnClickListener {

    View view;
    CategoryRecyclerViewAdapter categoryAdapter;
    RecyclerView categoryRecyclerView;
    OnListFragmentInteractionListener mListener;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        categoryRecyclerView = (RecyclerView) view.findViewById(R.id.categorySelectView);
        view.findViewById(R.id.homeNotesText).setOnClickListener(this);
        view.findViewById(R.id.homeCategoriesText).setOnClickListener(this);
        prepareCategoryItems();
        fillStudyNotesContainer();
        return view;
    }

    public void prepareCategoryItems() {
        CategoryRecyclerViewAdapter.Category.ITEMS = new ArrayList<CategoryRecyclerViewAdapter.Category>();
        CategoryRecyclerViewAdapter.Category.ITEMS.add(new CategoryRecyclerViewAdapter.Category(getActivity(), "Mechanical Systems", R.drawable.ic_mechanicalsystems));
        CategoryRecyclerViewAdapter.Category.ITEMS.add(new CategoryRecyclerViewAdapter.Category(getActivity(), "Electronic Systems", R.drawable.ic_electornicsystems));
        CategoryRecyclerViewAdapter.Category.ITEMS.add(new CategoryRecyclerViewAdapter.Category(getActivity(), "Other Systems", R.drawable.ic_easter_egg));
        categoryAdapter = new CategoryRecyclerViewAdapter(CategoryRecyclerViewAdapter.Category.ITEMS, mListener);
        categoryRecyclerView.setAdapter(categoryAdapter);
    }

    public void fillStudyNotesContainer() {
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.homefragment_notecontainer, FragmentManager.FRAGMENT_TYPE.STUDY_NOTES_FRAGMENT.getFragment());
        ft.commit();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(CategoryRecyclerViewAdapter.Category item);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.homeCategoriesText || i == R.id.categorySelectView) {
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_container_home, FragmentManager.FRAGMENT_TYPE.SIMULATION_LIST_FRAGMENT.getFragment());
            FragmentManager.Category.currentCategoryID = FragmentManager.Category.SIMULATION.id;
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.addToBackStack("CategoryFragment");
            ft.commit();
        }
        if (i == R.id.homeNotesText || i == R.id.homefragment_notecontainer) {
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_container_home, FragmentManager.FRAGMENT_TYPE.STUDY_NOTES_FRAGMENT.getFragment());
            FragmentManager.Category.currentCategoryID = FragmentManager.Category.NOTE.id;
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.addToBackStack("CategoryFragment");
            ft.commit();
        }
    }
}

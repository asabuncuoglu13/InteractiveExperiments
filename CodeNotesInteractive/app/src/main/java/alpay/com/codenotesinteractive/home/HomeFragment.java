package alpay.com.codenotesinteractive.home;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import alpay.com.codenotesinteractive.FragmentManager;
import alpay.com.codenotesinteractive.R;

/*
Note: There is a Fragment Call inside this this Fragment.
ButterKnife causes error at onDestroy() method. Do not use it.
 */

public class HomeFragment extends Fragment {

    View view;
    CategoryRecyclerViewAdapter categoryAdapter;
    RecyclerView categoryRecyclerView;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        categoryRecyclerView = (RecyclerView) view.findViewById(R.id.categorySelectView);
        prepareCategoryItems();
        fillStudyNotesContainer();
        return view;
    }

    public void prepareCategoryItems() {
        CategoryRecyclerViewAdapter.Category.ITEMS.add(new CategoryRecyclerViewAdapter.Category(getActivity(), "Mechanical Systems", R.drawable.ic_mechanicalsystems));
        CategoryRecyclerViewAdapter.Category.ITEMS.add(new CategoryRecyclerViewAdapter.Category(getActivity(), "Electronic Systems", R.drawable.ic_electornicsystems));
        CategoryRecyclerViewAdapter.Category.ITEMS.add(new CategoryRecyclerViewAdapter.Category(getActivity(), "Other Systems", R.drawable.ic_easter_egg));
        categoryAdapter = new CategoryRecyclerViewAdapter(CategoryRecyclerViewAdapter.Category.ITEMS);
        categoryRecyclerView.setAdapter(categoryAdapter);
    }

    public void fillStudyNotesContainer() {
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.homefragment_notecontainer, FragmentManager.FRAGMENT_TYPE.STUDY_NOTES_FRAGMENT.getFragment());
        ft.commit();
    }
}

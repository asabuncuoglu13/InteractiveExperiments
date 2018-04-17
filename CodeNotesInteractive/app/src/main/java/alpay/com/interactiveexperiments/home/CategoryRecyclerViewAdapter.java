package alpay.com.interactiveexperiments.home;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import alpay.com.interactiveexperiments.R;

public class CategoryRecyclerViewAdapter extends RecyclerView.Adapter<CategoryRecyclerViewAdapter.ViewHolder> {

    private final List<Category> mValues;
    private final HomeFragment.OnListFragmentInteractionListener mListener;
    private int mColumnCount = 1;

    public CategoryRecyclerViewAdapter(List<Category> items, HomeFragment.OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public CategoryRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.category_cardview, parent, false);
        ViewHolder vh = new ViewHolder(view);
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(new CategoryRecyclerViewAdapter(Category.ITEMS, mListener));
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mNameVıew.setText(mValues.get(position).name);
        holder.mImageView.setImageDrawable(mValues.get(position).image);
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public View mView;
        public TextView mNameVıew;
        public ImageView mImageView;
        public Category mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mNameVıew = (TextView) view.findViewById(R.id.categoryName);
            mImageView = (ImageView) view.findViewById(R.id.categoryDrawable);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mNameVıew.getText() + "'";
        }
    }

    public static class Category {
        public static final List<Category> ITEMS = new ArrayList<Category>();
        public String name;
        public Drawable image;

        public Category(Context context, String name, int imageID) {
            this.name = name;
            setImage(context, imageID);
        }

        public void setImage(Context context, int ID) {
            this.image = context.getResources().getDrawable(ID);
        }

        @Override
        public String toString() {
            return name;
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mValues.size();
    }
}
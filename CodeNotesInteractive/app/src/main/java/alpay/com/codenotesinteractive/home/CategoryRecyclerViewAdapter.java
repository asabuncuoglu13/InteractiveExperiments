package alpay.com.codenotesinteractive.home;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import alpay.com.codenotesinteractive.R;

public class CategoryRecyclerViewAdapter extends RecyclerView.Adapter<CategoryRecyclerViewAdapter.ViewHolder> {

    private final List<Category> mValues;
    private final OnCategoryListFragmentInteractionListener mListener;
    private Context context;

    public interface OnCategoryListFragmentInteractionListener {
        void onCategoryListFragmentInteraction(CategoryRecyclerViewAdapter.Category item);
    }

    public CategoryRecyclerViewAdapter(List<Category> items, OnCategoryListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_cardview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mContentView.setText(mValues.get(position).name);
        holder.mImageView.setImageDrawable(mValues.get(position).image);
        //holder.mImageView.setImageResource(R.drawable.ic_image);
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onCategoryListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public View mView;
        public TextView mContentView;
        public TextView mDetailView;
        public ImageView mImageView;
        public Category mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mContentView = (TextView) view.findViewById(R.id.content);
            mDetailView = (TextView) view.findViewById(R.id.detail);
            mImageView = (ImageView) view.findViewById(R.id.card_image);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
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
}

package alpay.com.codenotesinteractive.home;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import alpay.com.codenotesinteractive.R;

public class CategoryRecyclerViewAdapter extends RecyclerView.Adapter<CategoryRecyclerViewAdapter.ViewHolder> {

    private final List<Category> mValues;
    private final HomeFragment.OnCategoryListFragmentInteractionListener mListener;
    private Context context;

    public CategoryRecyclerViewAdapter(List<Category> items, HomeFragment.OnCategoryListFragmentInteractionListener listener) {
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
        public final String id;
        public String name;
        public Drawable image;

        public Category(String id, String name, Drawable image) {
            this.id = id;
            this.name = name;
            this.image= image;
        }

        public void setImageID(Context context, int ID) {
            this.image = context.getResources().getDrawable(ID);
        }

        @Override
        public String toString() {
            return name;
        }
    }
}

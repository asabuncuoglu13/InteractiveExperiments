package alpay.com.interactiveexperiments.simulation.simulation_fragments;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import alpay.com.interactiveexperiments.R;
import alpay.com.interactiveexperiments.simulation.Simulation.SimulationItem;
import alpay.com.interactiveexperiments.simulation.SimulationParameters;
import alpay.com.interactiveexperiments.simulation.simulation_fragments.SimulationListFragment.OnListFragmentInteractionListener;

public class SimulationRecyclerViewAdapter extends RecyclerView.Adapter<SimulationRecyclerViewAdapter.ViewHolder> {

    private final List<SimulationItem> mValues;
    private final OnListFragmentInteractionListener mListener;
    private Context context;

    public SimulationRecyclerViewAdapter(List<SimulationItem> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.simulation_cardview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        mValues.get(position).setImageID(context, SimulationParameters.images[position]);
        holder.mItem = mValues.get(position);
        holder.mContentView.setText(mValues.get(position).content);
        holder.mDetailView.setText(mValues.get(position).details);
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

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public View mView;
        public TextView mContentView;
        public TextView mDetailView;
        public ImageView mImageView;
        public SimulationItem mItem;

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
}

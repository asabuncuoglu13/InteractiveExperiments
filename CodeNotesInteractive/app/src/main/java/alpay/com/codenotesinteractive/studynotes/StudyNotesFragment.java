package alpay.com.codenotesinteractive.studynotes;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import alpay.com.codenotesinteractive.R;

import static android.app.Activity.RESULT_CANCELED;
import static android.content.Context.ALARM_SERVICE;
import static android.content.Context.MODE_PRIVATE;

public class StudyNotesFragment extends Fragment {

    public View view;
    private RecyclerViewEmptySupport mRecyclerView;
    private FloatingActionButton mAddToDoItemFAB;
    private ArrayList<StudyNoteItem> mStudyNoteItemsArrayList;
    private CoordinatorLayout mCoordLayout;
    public static final String TODOITEM = "MainFragment";
    private BasicListAdapter adapter;
    private static final int REQUEST_ID_TODO_ITEM = 100;
    private StudyNoteItem mJustDeletedStudyNoteItem;
    private int mIndexOfDeletedToDoItem;
    public static final String DATE_TIME_FORMAT_12_HOUR = "MMM d, yyyy  h:mm a";
    public static final String DATE_TIME_FORMAT_24_HOUR = "MMM d, yyyy  k:mm";
    public static final String FILENAME = "todoitems.json";
    private StoreRetrieveData storeRetrieveData;
    public ItemTouchHelper itemTouchHelper;
    private CustomRecyclerScrollViewListener customRecyclerScrollViewListener;
    public static final String SHARED_PREF_DATA_SET_CHANGED = "datasetchanged";
    public static final String CHANGE_OCCURED = "changeoccured";


    public static ArrayList<StudyNoteItem> getLocallyStoredData(StoreRetrieveData storeRetrieveData) {
        ArrayList<StudyNoteItem> items = null;
        try {
            items = storeRetrieveData.loadFromFile();

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        if (items == null) {
            items = new ArrayList<>();
        }
        return items;

    }

    @Override
    public void onStart() {
        super.onStart();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREF_DATA_SET_CHANGED, MODE_PRIVATE);
        if (sharedPreferences.getBoolean(CHANGE_OCCURED, false)) {
            mStudyNoteItemsArrayList = getLocallyStoredData(storeRetrieveData);
            adapter = new BasicListAdapter(mStudyNoteItemsArrayList);
            mRecyclerView.setAdapter(adapter);
            setAlarms();

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(CHANGE_OCCURED, false);
            editor.apply();
        }
    }

    private void setAlarms() {
        if (mStudyNoteItemsArrayList != null) {
            for (StudyNoteItem item : mStudyNoteItemsArrayList) {
                if (item.hasReminder() && item.getToDoDate() != null) {
                    if (item.getToDoDate().before(new Date())) {
                        item.setToDoDate(null);
                        continue;
                    }
                    Intent i = new Intent(getActivity(), NotificationService.class);
                    i.putExtra(NotificationService.TODOUUID, item.getIdentifier());
                    i.putExtra(NotificationService.TODOTEXT, item.getToDoText());
                    scheduleNotification(getNotification("Hello"), i, item.getIdentifier().hashCode(), item.getToDoDate().getTime());
                }
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_todonotes, container, false);

        final FloatingActionButton floatingActionButton = (FloatingActionButton) getActivity().findViewById(R.id.button_fab);
        floatingActionButton.setVisibility(View.VISIBLE);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREF_DATA_SET_CHANGED, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(CHANGE_OCCURED, false);
        editor.apply();

        storeRetrieveData = new StoreRetrieveData(getActivity(), FILENAME);
        mStudyNoteItemsArrayList = getLocallyStoredData(storeRetrieveData);
        adapter = new BasicListAdapter(mStudyNoteItemsArrayList);
        setAlarms();

        mCoordLayout = (CoordinatorLayout) view.findViewById(R.id.myCoordinatorLayout);
        mAddToDoItemFAB = (FloatingActionButton) view.findViewById(R.id.addToDoItemFAB);
        mAddToDoItemFAB.setOnClickListener(new View.OnClickListener() {

            @SuppressWarnings("deprecation")
            @Override
            public void onClick(View v) {
                Intent newTodo = new Intent(getActivity(), AddToDoActivity.class);
                StudyNoteItem item = new StudyNoteItem("", false, null);
                int color = ColorGenerator.MATERIAL.getRandomColor();
                item.setTodoColor(color);
                newTodo.putExtra(TODOITEM, item);
                startActivityForResult(newTodo, REQUEST_ID_TODO_ITEM);
            }
        });

        mRecyclerView = (RecyclerViewEmptySupport) view.findViewById(R.id.toDoRecyclerView);
        mRecyclerView.setEmptyView(view.findViewById(R.id.toDoEmptyView));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        customRecyclerScrollViewListener = new CustomRecyclerScrollViewListener() {
            @Override
            public void show() {
                mAddToDoItemFAB.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
            }
            @Override
            public void hide() {
                CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) mAddToDoItemFAB.getLayoutParams();
                int fabMargin = lp.bottomMargin;
                mAddToDoItemFAB.animate().translationY(mAddToDoItemFAB.getHeight() + fabMargin).setInterpolator(new AccelerateInterpolator(2.0f)).start();
            }
        };
        mRecyclerView.addOnScrollListener(customRecyclerScrollViewListener);
        ItemTouchHelper.Callback callback = new ItemTouchHelperClass(adapter);
        itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
        mRecyclerView.setAdapter(adapter);
        return view;
    }

    private Notification getNotification(String content) {
        Notification.Builder builder = new Notification.Builder(getActivity());
        builder.setContentTitle("Scheduled Notification");
        builder.setContentText(content);
        builder.setSmallIcon(R.drawable.ic_add_alarm_grey_200_24dp);
        builder.setDefaults(Notification.DEFAULT_SOUND);
        return builder.build();
    }

    private void scheduleNotification(Notification notification, Intent i, int requestCode, long timeInMillis) {
        Intent notificationIntent = new Intent(getActivity(), NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, 1);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED && requestCode == REQUEST_ID_TODO_ITEM) {
            StudyNoteItem item = (StudyNoteItem) data.getSerializableExtra(TODOITEM);
            if (item.getToDoText().length() <= 0) {
                return;
            }
            boolean existed = false;

            if (item.hasReminder() && item.getToDoDate() != null) {
                Intent i = new Intent(getActivity(), NotificationService.class);
                i.putExtra(NotificationService.TODOTEXT, item.getToDoText());
                i.putExtra(NotificationService.TODOUUID, item.getIdentifier());
                scheduleNotification(getNotification("Hello"), i, item.getIdentifier().hashCode(), item.getToDoDate().getTime());
            }

            for (int i = 0; i < mStudyNoteItemsArrayList.size(); i++) {
                if (item.getIdentifier().equals(mStudyNoteItemsArrayList.get(i).getIdentifier())) {
                    mStudyNoteItemsArrayList.set(i, item);
                    existed = true;
                    adapter.notifyDataSetChanged();
                    break;
                }
            }
            if (!existed) {
                addToDataStore(item);
            }


        }
    }

    private AlarmManager getAlarmManager() {
        return (AlarmManager) getActivity().getSystemService(ALARM_SERVICE);
    }

    private boolean doesPendingIntentExist(Intent i, int requestCode) {
        PendingIntent pi = PendingIntent.getService(getActivity(), requestCode, i, PendingIntent.FLAG_NO_CREATE);
        return pi != null;
    }

    private void deleteAlarm(Intent i, int requestCode) {
        if (doesPendingIntentExist(i, requestCode)) {
            PendingIntent pi = PendingIntent.getService(getActivity(), requestCode, i, PendingIntent.FLAG_NO_CREATE);
            pi.cancel();
            getAlarmManager().cancel(pi);
            Log.d("OskarSchindler", "PI Cancelled " + doesPendingIntentExist(i, requestCode));
        }
    }

    private void addToDataStore(StudyNoteItem item) {
        mStudyNoteItemsArrayList.add(item);
        adapter.notifyItemInserted(mStudyNoteItemsArrayList.size() - 1);

    }

    public class BasicListAdapter extends RecyclerView.Adapter<BasicListAdapter.ViewHolder> implements ItemTouchHelperClass.ItemTouchHelperAdapter {
        private ArrayList<StudyNoteItem> items;

        @Override
        public void onItemMoved(int fromPosition, int toPosition) {
            if (fromPosition < toPosition) {
                for (int i = fromPosition; i < toPosition; i++) {
                    Collections.swap(items, i, i + 1);
                }
            } else {
                for (int i = fromPosition; i > toPosition; i--) {
                    Collections.swap(items, i, i - 1);
                }
            }
            notifyItemMoved(fromPosition, toPosition);
        }

        @Override
        public void onItemRemoved(final int position) {
            mJustDeletedStudyNoteItem = items.remove(position);
            mIndexOfDeletedToDoItem = position;
            Intent i = new Intent(getActivity(), NotificationService.class);
            deleteAlarm(i, mJustDeletedStudyNoteItem.getIdentifier().hashCode());
            notifyItemRemoved(position);

            String toShow = "Todo";
            Snackbar.make(mCoordLayout, "Deleted " + toShow, Snackbar.LENGTH_LONG)
                    .setAction("UNDO", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            items.add(mIndexOfDeletedToDoItem, mJustDeletedStudyNoteItem);
                            if (mJustDeletedStudyNoteItem.getToDoDate() != null && mJustDeletedStudyNoteItem.hasReminder()) {
                                Intent i = new Intent(getActivity(), NotificationService.class);
                                i.putExtra(NotificationService.TODOTEXT, mJustDeletedStudyNoteItem.getToDoText());
                                i.putExtra(NotificationService.TODOUUID, mJustDeletedStudyNoteItem.getIdentifier());
                                scheduleNotification(getNotification("Hello"), i, mJustDeletedStudyNoteItem.getIdentifier().hashCode(), mJustDeletedStudyNoteItem.getToDoDate().getTime());
                            }
                            notifyItemInserted(mIndexOfDeletedToDoItem);
                        }
                    }).show();
        }

        @Override
        public BasicListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_circle_try, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final BasicListAdapter.ViewHolder holder, final int position) {
            StudyNoteItem item = items.get(position);
            int bgColor = getResources().getColor(R.color.background);
            int todoTextColor = getResources().getColor(R.color.textColorDark);
            holder.linearLayout.setBackgroundColor(bgColor);

            if (item.hasReminder() && item.getToDoDate() != null) {
                holder.mToDoTextview.setMaxLines(1);
                holder.mTimeTextView.setVisibility(View.VISIBLE);
            } else {
                holder.mTimeTextView.setVisibility(View.GONE);
                holder.mToDoTextview.setMaxLines(2);
            }
            holder.mToDoTextview.setText(item.getToDoText());
            holder.mToDoTextview.setTextColor(todoTextColor);

            TextDrawable myDrawable = TextDrawable.builder().beginConfig()
                    .textColor(getResources().getColor(R.color.textColorLight))
                    .useFont(Typeface.DEFAULT)
                    .toUpperCase()
                    .endConfig()
                    .buildRound(item.getToDoText().substring(0, 1), item.getTodoColor());

            holder.mColorImageView.setImageDrawable(myDrawable);
            if (item.getToDoDate() != null) {
                String timeToShow;
                if (android.text.format.DateFormat.is24HourFormat(getActivity())) {
                    timeToShow = AddToDoActivity.formatDate(DATE_TIME_FORMAT_24_HOUR, item.getToDoDate());
                } else {
                    timeToShow = AddToDoActivity.formatDate(DATE_TIME_FORMAT_12_HOUR, item.getToDoDate());
                }
                holder.mTimeTextView.setText(timeToShow);
            }


        }

        @Override
        public int getItemCount() {
            return items.size();
        }
        BasicListAdapter(ArrayList<StudyNoteItem> items) {
            this.items = items;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            View mView;
            LinearLayout linearLayout;
            TextView mToDoTextview;
            ImageView mColorImageView;
            TextView mTimeTextView;

            public ViewHolder(View v) {
                super(v);
                mView = v;
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        StudyNoteItem item = items.get(ViewHolder.this.getAdapterPosition());
                        Intent i = new Intent(getActivity(), AddToDoActivity.class);
                        i.putExtra(TODOITEM, item);
                        startActivityForResult(i, REQUEST_ID_TODO_ITEM);
                    }
                });
                mToDoTextview = (TextView) v.findViewById(R.id.toDoListItemTextview);
                mTimeTextView = (TextView) v.findViewById(R.id.todoListItemTimeTextView);
                mColorImageView = (ImageView) v.findViewById(R.id.toDoListItemColorImageView);
                linearLayout = (LinearLayout) v.findViewById(R.id.listItemLinearLayout);
            }

        }
    }

    private void saveDate() {
        try {
            storeRetrieveData.saveToFile(mStudyNoteItemsArrayList);
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        try {
            storeRetrieveData.saveToFile(mStudyNoteItemsArrayList);
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mRecyclerView.removeOnScrollListener(customRecyclerScrollViewListener);
    }

}



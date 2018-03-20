package alpay.com.codenotesinteractive.chat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ai.api.AIListener;
import ai.api.AIServiceException;
import ai.api.android.AIConfiguration;
import ai.api.android.AIDataService;
import ai.api.android.AIService;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;
import ai.api.model.Result;
import alpay.com.codenotesinteractive.HowToActivity;
import alpay.com.codenotesinteractive.R;
import alpay.com.codenotesinteractive.compiler.CompilerActivity;
import alpay.com.codenotesinteractive.simulation.SimulationActivity;
import alpay.com.codenotesinteractive.simulation.SimulationParameters;

public class ChatFragment extends Fragment implements AIListener, View.OnClickListener {

    RecyclerView recyclerView;
    EditText editText;
    RelativeLayout addBtn;
    DatabaseReference ref;
    FirebaseRecyclerAdapter<ChatMessage, ChatViewHolder> adapter;
    Boolean flagFab = true;
    Boolean translateToTurkish = false;

    public View view;
    private AIService aiService;
    AIDataService aiDataService;
    AIRequest aiRequest;
    final AIConfiguration config = new AIConfiguration("cbecec58c68d40a3b4fbdd71723c4a34",
            AIConfiguration.SupportedLanguages.English,
            AIConfiguration.RecognitionEngine.System);


    public ChatFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_chat, container, false);
        final FloatingActionButton floatingActionButton = (FloatingActionButton) getActivity().findViewById(R.id.button_fab);
        floatingActionButton.setVisibility(View.GONE);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        //database.setPersistenceEnabled(true);
        ActivityCompat.requestPermissions(this.getActivity(), new String[]{android.Manifest.permission.RECORD_AUDIO}, 1);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        editText = (EditText) view.findViewById(R.id.editText);
        addBtn = (RelativeLayout) view.findViewById(R.id.addBtn);

        recyclerView.setHasFixedSize(true);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        ref = FirebaseDatabase.getInstance().getReference();
        ref.keepSynced(true);

        aiService = AIService.getService(this.getContext(), config);
        aiService.setListener(this);


        aiDataService = new AIDataService(this.getContext(), config);
        aiRequest = new AIRequest();

        addBtn.setOnClickListener(this);


        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ImageView fab_img = (ImageView) view.findViewById(R.id.fab_img);
                Bitmap img = BitmapFactory.decodeResource(getResources(), R.drawable.ic_send_white_24dp);
                Bitmap img1 = BitmapFactory.decodeResource(getResources(), R.drawable.ic_translate);

                if (s.toString().trim().length() != 0 && flagFab) {
                    ImageViewAnimatedChange(getActivity(), fab_img, img);
                    flagFab = false;

                } else if (s.toString().trim().length() == 0) {
                    ImageViewAnimatedChange(getActivity(), fab_img, img1);
                    flagFab = true;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        adapter = new FirebaseRecyclerAdapter<ChatMessage, ChatViewHolder>(ChatMessage.class, R.layout.message_listview, ChatViewHolder.class, ref.child("chat")) {
            @Override
            protected void populateViewHolder(ChatViewHolder viewHolder, ChatMessage model, int position) {

                if (model.getMsgUser().equals("user")) {
                    viewHolder.rightText.setText(model.getMsgText());
                    viewHolder.rightText.setVisibility(View.VISIBLE);
                    viewHolder.leftText.setVisibility(View.GONE);
                } else {
                    viewHolder.leftText.setText(model.getMsgText());
                    viewHolder.rightText.setVisibility(View.GONE);
                    viewHolder.leftText.setVisibility(View.VISIBLE);
                }
            }
        };

        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int msgCount = adapter.getItemCount();
                int lastVisiblePosition = linearLayoutManager.findLastCompletelyVisibleItemPosition();
                if (lastVisiblePosition == -1 ||
                        (positionStart >= (msgCount - 1) &&
                                lastVisiblePosition == (positionStart - 1))) {
                    recyclerView.scrollToPosition(positionStart);
                }

            }
        });

        recyclerView.setAdapter(adapter);
        return view;
    }


    public void ImageViewAnimatedChange(Context c, final ImageView v, final Bitmap new_image) {
        final Animation anim_out = AnimationUtils.loadAnimation(c, R.anim.zoom_out);
        final Animation anim_in = AnimationUtils.loadAnimation(c, R.anim.zoom_in);
        anim_out.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                v.setImageBitmap(new_image);
                anim_in.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                    }
                });
                v.startAnimation(anim_in);
            }
        });
        v.startAnimation(anim_out);
    }

    @Override
    public void onResult(AIResponse response) {
        Result result = response.getResult();

        String message = result.getResolvedQuery();
        ChatMessage chatMessage0 = new ChatMessage(message, "user");
        ref.child("chat").push().setValue(chatMessage0);

        String reply = result.getFulfillment().getSpeech();
        ChatMessage chatMessage = new ChatMessage(reply, "bot");
        ref.child("chat").push().setValue(chatMessage);
    }

    private void handleNavigation(String reply) {
        if (reply.contains("How-To-Guide")) {
            Intent intent = new Intent(getActivity(), HowToActivity.class);
            startActivity(intent);
            return;
        } else if (reply.contains("Coding-Area")) {
            Intent intent = new Intent(getActivity(), CompilerActivity.class);
            startActivity(intent);
            return;
        } else if (reply.contains("Simulation-Area")) {
            Intent intent = new Intent(getActivity(), SimulationActivity.class);
            startActivity(intent);
            return;
        } else if (reply.contains("Ohms-Law-Experiment")) {
            Intent intent = new Intent(getActivity(), SimulationActivity.class);
            intent.putExtra("simulationID", SimulationParameters.OHMS_LAW_SIMULATION);
            startActivity(intent);
            return;
        } else if (reply.contains("Inclined-Plane-Experiment")) {
            Intent intent = new Intent(getActivity(), SimulationActivity.class);
            intent.putExtra("simulationID", SimulationParameters.INCLINED_PLANE_SIMULATION);
            startActivity(intent);
            return;
        } else if (reply.contains("Constant-Acceleration-Experiment")) {
            Intent intent = new Intent(getActivity(), SimulationActivity.class);
            intent.putExtra("simulationID", SimulationParameters.CONSTANT_ACCELERATION_SIMULATION);
            startActivity(intent);
            return;
        }
    }

    @Override
    public void onError(ai.api.model.AIError error) {

    }

    @Override
    public void onAudioLevel(float level) {

    }

    @Override
    public void onListeningStarted() {

    }

    @Override
    public void onListeningCanceled() {

    }

    @Override
    public void onListeningFinished() {

    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.addBtn) {
            String message = editText.getText().toString().trim();
            if (!message.equals("")) {
                ChatMessage chatMessage = new ChatMessage(message, "user");
                ref.child("chat").push().setValue(chatMessage);

                aiRequest.setQuery(message);
                new AsyncTask<AIRequest, Void, AIResponse>() {

                    @Override
                    protected AIResponse doInBackground(AIRequest... aiRequests) {
                        final AIRequest request = aiRequests[0];
                        try {
                            final AIResponse response = aiDataService.request(aiRequest);
                            return response;
                        } catch (AIServiceException e) {
                        } catch (NumberFormatException n) {
                            Toast.makeText(getContext(), R.string.response_type_error, Toast.LENGTH_SHORT).show();
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(AIResponse response) {
                        if (response != null) {
                            Result result = response.getResult();
                            String reply = result.getFulfillment().getSpeech();
                            ChatMessage chatMessage = new ChatMessage(reply, "bot");
                            ref.child("chat").push().setValue(chatMessage);
                            handleNavigation(reply);
                        }
                    }
                }.execute(aiRequest);
            } else {
                aiService.startListening();
            }
            editText.setText("");
        }

    }
}

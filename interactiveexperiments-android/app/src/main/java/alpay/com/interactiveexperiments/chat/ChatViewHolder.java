package alpay.com.interactiveexperiments.chat;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import alpay.com.interactiveexperiments.R;

public class ChatViewHolder extends RecyclerView.ViewHolder  {

    TextView leftText,rightText;

    public ChatViewHolder(View itemView){
        super(itemView);

        leftText = (TextView)itemView.findViewById(R.id.leftText);
        rightText = (TextView)itemView.findViewById(R.id.rightText);


    }
}
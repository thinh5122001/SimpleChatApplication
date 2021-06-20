package com.example.chatapplication.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapplication.Model.MessageModel;
import com.example.chatapplication.R;
import com.github.curioustechizen.ago.RelativeTimeTextView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    @NonNull
    private static final int MSG_TYPE_LEFT = 0;
    private static final int MSG_TYPE_RIGHT= 1;
    private List<MessageModel>messagelists;

    public MessageAdapter(List<MessageModel> messagelists) {
        this.messagelists = messagelists;
    }

    public void addMessage(MessageModel message)
    {
        messagelists.add(message);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.server_message_layout, parent, false);
        return new ViewHolder(itemView);


    }

    @Override
    public void onBindViewHolder( MessageAdapter.ViewHolder holder, int position) {
        MessageModel message = messagelists.get(position);
        holder.avatarIMG.setImageResource(R.drawable.ic_man);
        holder.textContentTV.setText(message.getTextContent());
        holder.timeTV.setReferenceTime(System.currentTimeMillis());
    }

    @Override
    public int getItemCount() {
        return messagelists.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView avatarIMG;
        TextView textContentTV;
        RelativeTimeTextView timeTV;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            avatarIMG = itemView.findViewById(R.id.avatarIMG);
            textContentTV = itemView.findViewById(R.id.textContentTV);
            timeTV = itemView.findViewById(R.id.timeTV);
        }

    }


}

package com.example.chatapplication.Model;

import android.widget.ImageView;

public class MessageModel {
    private String time;
    private ImageView avatarIMG;
    private String textContent;
    public MessageModel(String content)
    {
        this.textContent = content;
    }
    public MessageModel(String time, ImageView avatarIMG, String textContent) {
        this.time = time;
        this.avatarIMG = avatarIMG;
        this.textContent = textContent;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public ImageView getAvatarIMG() {
        return avatarIMG;
    }

    public void setAvatarIMG(ImageView avatarIMG) {
        this.avatarIMG = avatarIMG;
    }

    public String getTextContent() {
        return textContent;
    }

    public void setTextContent(String textContent) {
        this.textContent = textContent;
    }
}

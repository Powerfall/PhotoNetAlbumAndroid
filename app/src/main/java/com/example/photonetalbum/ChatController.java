package com.example.photonetalbum;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class ChatController {
    private LinearLayout chat;
    ArrayList<TextView> users = new ArrayList<TextView>();

    protected void onCreate(Context con, View page) {
        chat = page.findViewById(R.id.layoutChat);
        //users.add(new TextView(con).setText("s"));
        //TextView a = new TextView(con);
        //a.setText("asdasd");
        //chat.addView(a);
    }
}

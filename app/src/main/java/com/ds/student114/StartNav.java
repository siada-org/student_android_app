package com.ds.student114;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

/**
 * Created by Admin on 20.10.2015.
 */
public class StartNav extends Activity implements OnClickListener {

    ImageButton profile_btn;
    ImageButton chat_btn;
    ImageButton events_btn;
    ImageButton help_btn;


    @Override
    public void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_nav);

        profile_btn = (ImageButton) findViewById(R.id.img_button_profile);
        profile_btn.setOnClickListener(this);

        chat_btn = (ImageButton) findViewById(R.id.img_button_chat);
        chat_btn.setOnClickListener(this);

        events_btn = (ImageButton) findViewById(R.id.img_button_events);
        events_btn.setOnClickListener(this);

        help_btn = (ImageButton) findViewById(R.id.img_button_files);
        help_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.img_button_profile:
                Intent intent = new Intent(this, MainWindow.class);
                startActivity(intent);
                break;
            case R.id.img_button_files:
                Intent intent2 = new Intent(this, FilesActivity.class);
                startActivity(intent2);
                break;
            case R.id.img_button_events:
                Intent intent3 = new Intent(this, EventsActivity.class);
                startActivity(intent3);
                break;
            case R.id.img_button_chat:
                Intent intent4 = new Intent(this, ChatActivity.class);
                startActivity(intent4);
                break;
            default:
                break;
        }

    }
}

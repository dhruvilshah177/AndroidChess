package com.rutgers.androidchess46;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


/**
 * The real main Activity
 *
 * @author Dhruvil Shah
 * @author Drashti Mehta
 */
public class MainActivity extends ActivityBase {
    Button button_PlayChess;
    Button button_ListGames;
    Button button_Playback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button_PlayChess = (Button) findViewById(R.id.buttonPlayChess);;
        button_ListGames = (Button) findViewById(R.id.buttonListGames);;
        button_Playback  = (Button) findViewById(R.id.buttonPlayback);;
        button_PlayChess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(MainActivity.this, ChessActivity.class);
                startActivity(intent);
            }
        });
        button_ListGames.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(MainActivity.this, ListgameActivity.class);
                startActivity(intent);
            }
        });
        if (guiG==null) {
            button_Playback.setVisibility(View.INVISIBLE);
        }
        else {
            button_Playback.setVisibility(View.VISIBLE);
        }
        button_Playback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(MainActivity.this, PlaybackActivity.class);
                intent.putExtra(FILENAME, LASTGAME);
                startActivity(intent);
            }
        });
    }
}

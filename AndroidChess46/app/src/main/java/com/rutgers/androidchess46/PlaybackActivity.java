package com.rutgers.androidchess46;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import controller.GuiGame;
import controller.GuiInstruction;


/**
 * Main activity for replaying games
 *
 * @author Dhruvil Shah
 * @author Drashti Mehta
 */
public class PlaybackActivity extends ChessBoardActivity implements View.OnClickListener {
    Button btnForward;
    Button btnBackward;
    Button btnPlay;
    Button btnBack;

    public static GuiGame guiGameForPlayBack = null;
    int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playback);
        player = (ImageView) findViewById(R.id.player);
        message = (TextView) findViewById(R.id.message);
        checkDraw = (CheckBox) findViewById(R.id.checkDraw);
        checkDraw.setVisibility(View.INVISIBLE);
        btnForward = (Button) findViewById(R.id.btnForward);
        btnBackward = (Button) findViewById(R.id.btnBackward);
        btnPlay = (Button) findViewById(R.id.btnPlay);
        btnBack = (Button) findViewById(R.id.btnBack);
        btnForward.setOnClickListener(this);
        btnBackward.setOnClickListener(this);
        btnPlay.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        doInitChessBoard(null, null);
        Intent intent = getIntent();
        String fileName = intent.getExtras().getString(FILENAME);
        if (fileName!=null) {
            if (fileName.equalsIgnoreCase(LASTGAME)) {
                if (guiG!=null) {
                    guiGameForPlayBack = guiG;
                    message.setText(LASTGAME);
                }
                else {
                    if (guiGameForPlayBack==null) {
                        message.setText("No game to replay");
                    }
                }
            }
            else {
                Object obj = readObjectFromFile(getFilesDir(), fileName);
                if (obj!=null) {
                    guiGameForPlayBack = (GuiGame) obj;
                    message.setText(getTitleFromFileName(fileName));
                }
                else {
                    if (guiGameForPlayBack==null) {
                        message.setText("Could not load game to replay");
                    }
                }
            }
        }
        else {
            if (guiG!=null) {
                guiGameForPlayBack = guiG;
                message.setText(LASTGAME);
            }
            else {
                if (guiGameForPlayBack==null) {
                    message.setText("No game to replay");
                }
            }
        }
        index = 0;
    }

    @Override
    public void onClick(View v) {
        if (v instanceof Button) {
            if (v==btnForward) {
                if (guiGameForPlayBack!=null && index>=0 && index<guiGameForPlayBack.getMoveCount()) {
                    GuiInstruction guiInst = guiGameForPlayBack.getAt(index);
                    index++;
                    goGUI(guiInst.getMove());
                    setPlayer(!guiInst.isWhite(), false, null);
                    message.setText(guiInst.getMessage());
                }
            }
            else if (v==btnBackward) {
                if (guiGameForPlayBack!=null && index>=1 && index<=guiGameForPlayBack.getMoveCount()) {
                    index--;
                    GuiInstruction guiInst = guiGameForPlayBack.getAt(index);
                    goGUI(guiInst.getRollback());
                    if (index==0) {
                        setPlayer(true, false, null);
                        message.setText("");
                    }
                    else {
                        GuiInstruction guiInstBefore = guiGameForPlayBack.getAt(index-1);;
                        if (guiInstBefore!=null) {
                            setPlayer(!guiInstBefore.isWhite(), false, null);
                            message.setText(guiInstBefore.getMessage());
                        }
                    }
                }
            }
            else if (v==btnPlay) {
                Intent intent = new Intent(PlaybackActivity.this, ChessActivity.class);
                startActivity(intent);
            }
            else if (v==btnBack) {
                Intent intent = new Intent(PlaybackActivity.this, MainActivity.class);
                startActivity(intent);
            }
        }
    }
}

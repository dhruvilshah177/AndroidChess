package com.rutgers.androidchess46;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import controller.AndroidChessController;
import controller.Move;
import controller.Outcome;
import model.Player;

/**
 * Main activity for playing chess
 *
 * @author Dhruvil Shah
 * @author Drashti Mehta
 */
public class ChessActivity extends ChessBoardActivity implements View.OnClickListener, IOnDialogClick, DialogInterface.OnClickListener {
    CharSequence pieces[] = new CharSequence[] {"Queen", "Rook", "Bishop", "Knight"};

    Button btn_Rollback;
    Button btn_AI;
    Button btn_Resign;
    Button btn_Save;
    Button btn_New;
    Button btn_Back;
    Button btn_Draw;
    Button btn_PlayBack;

    private DialogGetGameTitle diaglogGetName;
    private AlertDialog.Builder piecePickerBuilder;
    AndroidChessController controller;

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        DialogGetGameTitle diag = (DialogGetGameTitle)dialog;
        String szTemp = diag.getGameTitle();

        szTemp = szTemp==null ? "" : szTemp;

        if (szTemp!=null) {
            szTemp = szTemp.trim();
            if (szTemp.length()==0) {
                szTemp = "Not_Set";
            }

            String out = serializeAddress(getFilesDir(), szTemp, guiG);
        }
    };

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chess);

        player      = (ImageView) findViewById(R.id.player);
        message     = (TextView) findViewById(R.id.message);
        checkDraw   = (CheckBox) findViewById(R.id.checkDraw);

        btn_Rollback = (Button) findViewById(R.id.btnRollback);
        btn_AI       = (Button) findViewById(R.id.btnAI);
        btn_Resign   = (Button) findViewById(R.id.btnResign);
        btn_Save     = (Button) findViewById(R.id.btnSave);
        btn_New      = (Button) findViewById(R.id.btnNew);
        btn_Back     = (Button) findViewById(R.id.btnBack);
        btn_Draw     = (Button) findViewById(R.id.btnDraw);
        btn_PlayBack = (Button) findViewById(R.id.btnPlayBack);

        btn_Rollback.setOnClickListener(this);
        btn_AI.setOnClickListener(this);
        btn_Resign.setOnClickListener(this);
        btn_Save.setOnClickListener(this);
        btn_New.setOnClickListener(this);
        btn_Back.setOnClickListener(this);
        btn_Draw.setOnClickListener(this);
        btn_PlayBack.setOnClickListener(this);

        diaglogGetName = new DialogGetGameTitle();
        piecePickerBuilder = new AlertDialog.Builder(this);

        controller = new AndroidChessController();
        doInitChessBoard(this, btn_Draw);

        btn_Save.setEnabled(false);
        btn_PlayBack.setEnabled(false);
    }

    @Override
    public void onClick(View v) {
        if (v instanceof ImageView) {
            if (controller.gameEnded()) {}
            else {
                ImageView iv = (ImageView)v;
                String szTag = (String)iv.getTag();
                int file = szTag.charAt(0)-'0';
                int rank = szTag.charAt(1)-'0';

                String tagSelected = getSelected();

                if (tagSelected!=null) {
                    if (isSelected(szTag)) {
                        doDeselectP();
                    }
                    else {
                        int fileSelected    = tagSelected.charAt(0)-'0';
                        int rankSelected    = tagSelected.charAt(1)-'0';
                        char pieceSelected  = tagSelected.charAt(3);

                        boolean isProposingDraw = checkDraw.isChecked();

                        Move move = new Move(file, rank, fileSelected, rankSelected, isProposingDraw);
                        doMove(move, pieceSelected=='P' && ((rank==0) || (rank==7)) );
                    }
                }
                else {
                    char colorPlayer    = szTag.charAt(2);

                    Player currentPlayer = controller.getCurrentPlayer();
                    if (colorPlayer=='W' && currentPlayer.isWhitePlayer()) {
                        doSelectP(szTag);
                    }
                    else if (colorPlayer=='B' && !currentPlayer.isWhitePlayer()) {
                        doSelectP(szTag);
                    }
                }
            }
        }
        else if (v instanceof Button) {
            if (!controller.gameEnded() && v==btn_Rollback) {
                Move move = new Move("Rollback");
                doMove(move, false);
            }
            else if (!controller.gameEnded() && v==btn_AI) {
                Move move = new Move("AI");
                doMove(move, false);
            }
            else if (!controller.gameEnded() && v==btn_Draw) {
                Move move = new Move("draw");
                doMove(move, false);
            }
            else if (!controller.gameEnded() && v==btn_Resign) {
                Move move = new Move("resign");
                doMove(move, false);
            }
            else if (v==btn_Save) {
                diaglogGetName.show(getFragmentManager(), "Get Game Title");
            }
            else if (v==btn_New) {
                message.setText("New");

                controller = new AndroidChessController();
                doCleanupChessBoard(btn_Draw);
                btn_Save.setEnabled(false);
                btn_PlayBack.setEnabled(false);
            }
            else if (v==btn_Back) {
                Intent intent = new Intent(ChessActivity.this, MainActivity.class);
                startActivity(intent);
            }
            else if (v==btn_PlayBack) {
                Intent intent = new Intent(ChessActivity.this, PlaybackActivity.class);
                intent.putExtra(FILENAME, LASTGAME);
                startActivity(intent);
            }
            else {}
        }
    }


    @Override
    public void onClick(DialogInterface dialog, int which) {
        Move.Promotion promotion = Move.Promotion.NONE;
        if (which==0) {
            promotion = Move.Promotion.Queen;
        }
        else if (which==1) {
            promotion = Move.Promotion.Rook;
        }
        else if (which==2) {
            promotion = Move.Promotion.Bishop;
        }
        else if (which==3) {
            promotion = Move.Promotion.Knight;
        }
        else {
            promotion = Move.Promotion.Rook;
        }

        m_move.setPromotion(promotion);

        _doMove(m_move);
    }


    Move m_move;
    private void doMove(Move move, boolean isPromotion) {
        if (isPromotion) {
            m_move = move;
            piecePickerBuilder.setTitle("Promote the pawn to:");
            piecePickerBuilder.setItems(pieces, this);
            piecePickerBuilder.show();
        }
        else {
            _doMove(move);
        }
    }
    private void _doMove(Move move) {
        Outcome outcome = controller.doMove(move);

        if (outcome.isOK()) {
            if (outcome.getGuiInstruction()!=null) {
                goGUI(outcome.getGuiInstruction());

                Player currentPlayer = controller.getCurrentPlayer();
                setPlayer(currentPlayer.isWhitePlayer(), outcome.isProposingDraw(), btn_Draw);
            }

            message.setText(outcome.getReason());

            if (controller.gameEnded()) {
                guiG = controller.gGame;
                controller.gGame = null;

                diaglogGetName.show(getFragmentManager(), "Get Game Title");

                btn_Save.setEnabled(true);
                btn_PlayBack.setEnabled(true);
            }
        }
        else {
            message.setText(outcome.getReason());
        }
    }
}

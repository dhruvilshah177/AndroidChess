package com.rutgers.androidchess46;


import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Base Activity for main functions
 *
 * @author Dhruvil Shah
 * @author Drashti Mehta
 */
public class ChessBoardActivity extends ActivityBase {
    public static final String P_BR = "BR";
    public static final String P_BN = "BN";
    public static final String P_BB = "BB";
    public static final String P_BQ = "BQ";
    public static final String P_BK = "BK";
    public static final String P_BP = "BP";
    public static final String P_WR = "WR";
    public static final String P_WN = "WN";
    public static final String P_WB = "WB";
    public static final String P_WQ = "WQ";
    public static final String P_WK = "WK";
    public static final String P_WP = "WP";
    public static final String P_NO = "--";

    ImageView player;
    TextView message;
    CheckBox checkDraw;

    private ImageView[][] i_views = new ImageView[8][8];
    private String szSelectedOne = null;


    protected void setBackground(int file, int rank, boolean isSelected) {
        if (isSelected) {
            i_views[file][rank].setBackgroundColor(Color.BLUE);
        }
        else {
            if (((file + rank) % 2) == 0) {
                i_views[file][rank].setBackgroundColor(getResources().getColor(R.color.boarddark));
            } else {
                i_views[file][rank].setBackgroundColor(getResources().getColor(R.color.boardlight));
            }
        }
    }


    protected String getSelected() {
        return szSelectedOne;
    }


    protected boolean isSelected(String szTag) {
        if (szSelectedOne==null) {
            return false;
        }
        else {
            return szSelectedOne.charAt(0)==szTag.charAt(0) && szSelectedOne.charAt(1)==szTag.charAt(1);
        }
    }


    protected void doSelectP(String szTag) {
        int file        = szTag.charAt(0)-'0';
        int rank        = szTag.charAt(1)-'0';
        //
        doDeselectP();
        //
        szSelectedOne = szTag;
        setBackground(file, rank, true);
    }
    protected void doDeselectP() {
        if (szSelectedOne!=null) {
            int file        = szSelectedOne.charAt(0)-'0';
            int rank        = szSelectedOne.charAt(1)-'0';
            //
            szSelectedOne = null;
            setBackground(file, rank, false);
        }
    }


    protected void setPlayer(boolean isWhite, boolean isProposedDraw, Button btn_Draw) {
        if (isWhite) {
            player.setImageResource(R.drawable.whiteking);
        }
        else {
            player.setImageResource(R.drawable.blackking);
        }
        //
        checkDraw.setChecked(false);
        //
        if (btn_Draw!=null) {
            if (isProposedDraw) {
                btn_Draw.setEnabled(true);
                btn_Draw.setBackgroundColor(Color.GREEN);
            }
            else {
                btn_Draw.setEnabled(false);
                btn_Draw.setBackgroundColor(Color.WHITE);
            }
        }
    }

    protected void goGUI(String instruction) {
        String[] temps = instruction.split("\\;");
        for (int i=0; i<temps.length; i++) {
            String szOne = temps[i].trim();
            //
            doDeselectP();
            //
            if (szOne.length()>=5 && szOne.charAt(0)=='M') {                    //move
                moveP(szOne.charAt(3)-'0', szOne.charAt(4)-'0', szOne.charAt(1)-'0', szOne.charAt(2)-'0');
            }
            else if (szOne.length()>=5 && szOne.charAt(0)=='A') {               //add
                addP(szOne.substring(1,3), szOne.charAt(3)-'0', szOne.charAt(4)-'0');
            }
            else if (szOne.length()>=3 && szOne.charAt(0)=='R') {               //remove
                removeP(szOne.charAt(1)-'0', szOne.charAt(2)-'0');
            }
        }
    }
    protected void removeP(int file, int rank) {
        String szTag = "" + file + "" + rank + P_NO;
        //
        i_views[file][rank].setImageResource(R.drawable.transparent);
        i_views[file][rank].setTag(szTag);
    }
    protected void moveP(int fileOld, int rankOld, int fileNew, int rankNew) {
        String szOldTag = (String) i_views[fileOld][rankOld].getTag();
        String szNewTag = (String)i_views[fileNew][rankNew].getTag();
        //
        if (szOldTag.indexOf(P_BR)>=0) {
            i_views[fileOld][rankOld].setImageResource(R.drawable.transparent);
            i_views[fileOld][rankOld].setTag(szOldTag.substring(0,2) + P_NO);
            //
            i_views[fileNew][rankNew].setImageResource(R.drawable.blackrook);
            i_views[fileNew][rankNew].setTag(szNewTag.substring(0,2) + szOldTag.substring(2));
        }
        else if (szOldTag.indexOf(P_BN)>=0) {
            i_views[fileOld][rankOld].setImageResource(R.drawable.transparent);
            i_views[fileOld][rankOld].setTag(szOldTag.substring(0,2) + P_NO);
            //
            i_views[fileNew][rankNew].setImageResource(R.drawable.blackknight);
            i_views[fileNew][rankNew].setTag(szNewTag.substring(0,2) + szOldTag.substring(2));
        }
        else if (szOldTag.indexOf(P_BB)>=0) {
            i_views[fileOld][rankOld].setImageResource(R.drawable.transparent);
            i_views[fileOld][rankOld].setTag(szOldTag.substring(0,2) + P_NO);
            //
            i_views[fileNew][rankNew].setImageResource(R.drawable.blackbishop);
            i_views[fileNew][rankNew].setTag(szNewTag.substring(0,2) + szOldTag.substring(2));
        }
        else if (szOldTag.indexOf(P_BQ)>=0) {
            i_views[fileOld][rankOld].setImageResource(R.drawable.transparent);
            i_views[fileOld][rankOld].setTag(szOldTag.substring(0,2) + P_NO);
            //
            i_views[fileNew][rankNew].setImageResource(R.drawable.blackqueen);
            i_views[fileNew][rankNew].setTag(szNewTag.substring(0,2) + szOldTag.substring(2));
        }
        else if (szOldTag.indexOf(P_BK)>=0) {
            i_views[fileOld][rankOld].setImageResource(R.drawable.transparent);
            i_views[fileOld][rankOld].setTag(szOldTag.substring(0,2) + P_NO);
            //
            i_views[fileNew][rankNew].setImageResource(R.drawable.blackking);
            i_views[fileNew][rankNew].setTag(szNewTag.substring(0,2) + szOldTag.substring(2));
        }
        else if (szOldTag.indexOf(P_BP)>=0) {
            i_views[fileOld][rankOld].setImageResource(R.drawable.transparent);
            i_views[fileOld][rankOld].setTag(szOldTag.substring(0,2) + P_NO);
            //
            i_views[fileNew][rankNew].setImageResource(R.drawable.blackpawn);
            i_views[fileNew][rankNew].setTag(szNewTag.substring(0,2) + szOldTag.substring(2));
        }
        else if (szOldTag.indexOf(P_WR)>=0) {
            i_views[fileOld][rankOld].setImageResource(R.drawable.transparent);
            i_views[fileOld][rankOld].setTag(szOldTag.substring(0,2) + P_NO);
            //
            i_views[fileNew][rankNew].setImageResource(R.drawable.whiterook);
            i_views[fileNew][rankNew].setTag(szNewTag.substring(0,2) + szOldTag.substring(2));
        }
        else if (szOldTag.indexOf(P_WN)>=0) {
            i_views[fileOld][rankOld].setImageResource(R.drawable.transparent);
            i_views[fileOld][rankOld].setTag(szOldTag.substring(0,2) + P_NO);
            //
            i_views[fileNew][rankNew].setImageResource(R.drawable.whiteknight);
            i_views[fileNew][rankNew].setTag(szNewTag.substring(0,2) + szOldTag.substring(2));
        }
        else if (szOldTag.indexOf(P_WB)>=0) {
            i_views[fileOld][rankOld].setImageResource(R.drawable.transparent);
            i_views[fileOld][rankOld].setTag(szOldTag.substring(0,2) + P_NO);
            //
            i_views[fileNew][rankNew].setImageResource(R.drawable.whitebishop);
            i_views[fileNew][rankNew].setTag(szNewTag.substring(0,2) + szOldTag.substring(2));
        }
        else if (szOldTag.indexOf(P_WQ)>=0) {
            i_views[fileOld][rankOld].setImageResource(R.drawable.transparent);
            i_views[fileOld][rankOld].setTag(szOldTag.substring(0,2) + P_NO);
            //
            i_views[fileNew][rankNew].setImageResource(R.drawable.whitequeen);
            i_views[fileNew][rankNew].setTag(szNewTag.substring(0,2) + szOldTag.substring(2));
        }
        else if (szOldTag.indexOf(P_WK)>=0) {
            i_views[fileOld][rankOld].setImageResource(R.drawable.transparent);
            i_views[fileOld][rankOld].setTag(szOldTag.substring(0,2) + P_NO);
            //
            i_views[fileNew][rankNew].setImageResource(R.drawable.whiteking);
            i_views[fileNew][rankNew].setTag(szNewTag.substring(0,2) + szOldTag.substring(2));
        }
        else if (szOldTag.indexOf(P_WP)>=0) {
            i_views[fileOld][rankOld].setImageResource(R.drawable.transparent);
            i_views[fileOld][rankOld].setTag(szOldTag.substring(0,2) + P_NO);
            //
            i_views[fileNew][rankNew].setImageResource(R.drawable.whitepawn);
            i_views[fileNew][rankNew].setTag(szNewTag.substring(0,2) + szOldTag.substring(2));
        }
        else if (szOldTag.indexOf(P_NO)>=0) {
        }
        else {
        }
    }
    protected void addP(String type, int file, int rank) {
        String szTag = "" + file + "" + rank + type.toUpperCase();
        //
        if (type.equalsIgnoreCase(P_BR)) {
            i_views[file][rank].setImageResource(R.drawable.blackrook);
            i_views[file][rank].setTag(szTag);
        }
        else if (type.equalsIgnoreCase(P_BN)) {
            i_views[file][rank].setImageResource(R.drawable.blackknight);
            i_views[file][rank].setTag(szTag);
        }
        else if (type.equalsIgnoreCase(P_BB)) {
            i_views[file][rank].setImageResource(R.drawable.blackbishop);
            i_views[file][rank].setTag(szTag);
        }
        else if (type.equalsIgnoreCase(P_BQ)) {
            i_views[file][rank].setImageResource(R.drawable.blackqueen);
            i_views[file][rank].setTag(szTag);
        }
        else if (type.equalsIgnoreCase(P_BK)) {
            i_views[file][rank].setImageResource(R.drawable.blackking);
            i_views[file][rank].setTag(szTag);
        }
        else if (type.equalsIgnoreCase(P_BP)) {
            i_views[file][rank].setImageResource(R.drawable.blackpawn);
            i_views[file][rank].setTag(szTag);
        }
        else if (type.equalsIgnoreCase(P_WR)) {
            i_views[file][rank].setImageResource(R.drawable.whiterook);
            i_views[file][rank].setTag(szTag);
        }
        else if (type.equalsIgnoreCase(P_WN)) {
            i_views[file][rank].setImageResource(R.drawable.whiteknight);
            i_views[file][rank].setTag(szTag);
        }
        else if (type.equalsIgnoreCase(P_WB)) {
            i_views[file][rank].setImageResource(R.drawable.whitebishop);
            i_views[file][rank].setTag(szTag);
        }
        else if (type.equalsIgnoreCase(P_WQ)) {
            i_views[file][rank].setImageResource(R.drawable.whitequeen);
            i_views[file][rank].setTag(szTag);
        }
        else if (type.equalsIgnoreCase(P_WK)) {
            i_views[file][rank].setImageResource(R.drawable.whiteking);
            i_views[file][rank].setTag(szTag);
        }
        else if (type.equalsIgnoreCase(P_WP)) {
            i_views[file][rank].setImageResource(R.drawable.whitepawn);
            i_views[file][rank].setTag(szTag);
        }
        else if (type.equalsIgnoreCase(P_NO)) {
        }
        else {
        }
    }


    protected void doInitChessBoard(View.OnClickListener lister, Button btn_Draw) {
        if (message!=null) {
            message.setText("");
        }
        //
        if (btn_Draw!=null) {
            btn_Draw.setEnabled(false);
            btn_Draw.setBackgroundColor(Color.WHITE);
        }
        //
        i_views[0][7] = (ImageView) findViewById(R.id.iv07);	i_views[0][7].setTag("07"+P_BR);
        i_views[1][7] = (ImageView) findViewById(R.id.iv17);	i_views[1][7].setTag("17"+P_BN);
        i_views[2][7] = (ImageView) findViewById(R.id.iv27);	i_views[2][7].setTag("27"+P_BB);
        i_views[3][7] = (ImageView) findViewById(R.id.iv37);	i_views[3][7].setTag("37"+P_BQ);
        i_views[4][7] = (ImageView) findViewById(R.id.iv47);	i_views[4][7].setTag("47"+P_BK);
        i_views[5][7] = (ImageView) findViewById(R.id.iv57);	i_views[5][7].setTag("57"+P_BB);
        i_views[6][7] = (ImageView) findViewById(R.id.iv67);	i_views[6][7].setTag("67"+P_BN);
        i_views[7][7] = (ImageView) findViewById(R.id.iv77);	i_views[7][7].setTag("77"+P_BR);
        //
        i_views[0][6] = (ImageView) findViewById(R.id.iv06);	i_views[0][6].setTag("06"+P_BP);
        i_views[1][6] = (ImageView) findViewById(R.id.iv16);	i_views[1][6].setTag("16"+P_BP);
        i_views[2][6] = (ImageView) findViewById(R.id.iv26);	i_views[2][6].setTag("26"+P_BP);
        i_views[3][6] = (ImageView) findViewById(R.id.iv36);	i_views[3][6].setTag("36"+P_BP);
        i_views[4][6] = (ImageView) findViewById(R.id.iv46);	i_views[4][6].setTag("46"+P_BP);
        i_views[5][6] = (ImageView) findViewById(R.id.iv56);	i_views[5][6].setTag("56"+P_BP);
        i_views[6][6] = (ImageView) findViewById(R.id.iv66);	i_views[6][6].setTag("66"+P_BP);
        i_views[7][6] = (ImageView) findViewById(R.id.iv76);	i_views[7][6].setTag("76"+P_BP);
        //
        i_views[0][5] = (ImageView) findViewById(R.id.iv05);	i_views[0][5].setTag("05"+P_NO);
        i_views[1][5] = (ImageView) findViewById(R.id.iv15);	i_views[1][5].setTag("15"+P_NO);
        i_views[2][5] = (ImageView) findViewById(R.id.iv25);	i_views[2][5].setTag("25"+P_NO);
        i_views[3][5] = (ImageView) findViewById(R.id.iv35);	i_views[3][5].setTag("35"+P_NO);
        i_views[4][5] = (ImageView) findViewById(R.id.iv45);	i_views[4][5].setTag("45"+P_NO);
        i_views[5][5] = (ImageView) findViewById(R.id.iv55);	i_views[5][5].setTag("55"+P_NO);
        i_views[6][5] = (ImageView) findViewById(R.id.iv65);	i_views[6][5].setTag("65"+P_NO);
        i_views[7][5] = (ImageView) findViewById(R.id.iv75);	i_views[7][5].setTag("75"+P_NO);
        //
        i_views[0][4] = (ImageView) findViewById(R.id.iv04);	i_views[0][4].setTag("04"+P_NO);
        i_views[1][4] = (ImageView) findViewById(R.id.iv14);	i_views[1][4].setTag("14"+P_NO);
        i_views[2][4] = (ImageView) findViewById(R.id.iv24);	i_views[2][4].setTag("24"+P_NO);
        i_views[3][4] = (ImageView) findViewById(R.id.iv34);	i_views[3][4].setTag("34"+P_NO);
        i_views[4][4] = (ImageView) findViewById(R.id.iv44);	i_views[4][4].setTag("44"+P_NO);
        i_views[5][4] = (ImageView) findViewById(R.id.iv54);	i_views[5][4].setTag("54"+P_NO);
        i_views[6][4] = (ImageView) findViewById(R.id.iv64);	i_views[6][4].setTag("64"+P_NO);
        i_views[7][4] = (ImageView) findViewById(R.id.iv74);	i_views[7][4].setTag("74"+P_NO);
        //
        i_views[0][3] = (ImageView) findViewById(R.id.iv03);	i_views[0][3].setTag("03"+P_NO);
        i_views[1][3] = (ImageView) findViewById(R.id.iv13);	i_views[1][3].setTag("13"+P_NO);
        i_views[2][3] = (ImageView) findViewById(R.id.iv23);	i_views[2][3].setTag("23"+P_NO);
        i_views[3][3] = (ImageView) findViewById(R.id.iv33);	i_views[3][3].setTag("33"+P_NO);
        i_views[4][3] = (ImageView) findViewById(R.id.iv43);	i_views[4][3].setTag("43"+P_NO);
        i_views[5][3] = (ImageView) findViewById(R.id.iv53);	i_views[5][3].setTag("53"+P_NO);
        i_views[6][3] = (ImageView) findViewById(R.id.iv63);	i_views[6][3].setTag("63"+P_NO);
        i_views[7][3] = (ImageView) findViewById(R.id.iv73);	i_views[7][3].setTag("73"+P_NO);
        //
        i_views[0][2] = (ImageView) findViewById(R.id.iv02);	i_views[0][2].setTag("02"+P_NO);
        i_views[1][2] = (ImageView) findViewById(R.id.iv12);	i_views[1][2].setTag("12"+P_NO);
        i_views[2][2] = (ImageView) findViewById(R.id.iv22);	i_views[2][2].setTag("22"+P_NO);
        i_views[3][2] = (ImageView) findViewById(R.id.iv32);	i_views[3][2].setTag("32"+P_NO);
        i_views[4][2] = (ImageView) findViewById(R.id.iv42);	i_views[4][2].setTag("42"+P_NO);
        i_views[5][2] = (ImageView) findViewById(R.id.iv52);	i_views[5][2].setTag("52"+P_NO);
        i_views[6][2] = (ImageView) findViewById(R.id.iv62);	i_views[6][2].setTag("62"+P_NO);
        i_views[7][2] = (ImageView) findViewById(R.id.iv72);	i_views[7][2].setTag("72"+P_NO);
        //
        i_views[0][1] = (ImageView) findViewById(R.id.iv01);	i_views[0][1].setTag("01"+P_WP);
        i_views[1][1] = (ImageView) findViewById(R.id.iv11);	i_views[1][1].setTag("11"+P_WP);
        i_views[2][1] = (ImageView) findViewById(R.id.iv21);	i_views[2][1].setTag("21"+P_WP);
        i_views[3][1] = (ImageView) findViewById(R.id.iv31);	i_views[3][1].setTag("31"+P_WP);
        i_views[4][1] = (ImageView) findViewById(R.id.iv41);	i_views[4][1].setTag("41"+P_WP);
        i_views[5][1] = (ImageView) findViewById(R.id.iv51);	i_views[5][1].setTag("51"+P_WP);
        i_views[6][1] = (ImageView) findViewById(R.id.iv61);	i_views[6][1].setTag("61"+P_WP);
        i_views[7][1] = (ImageView) findViewById(R.id.iv71);	i_views[7][1].setTag("71"+P_WP);
        //
        i_views[0][0] = (ImageView) findViewById(R.id.iv00);	i_views[0][0].setTag("00"+P_WR);
        i_views[1][0] = (ImageView) findViewById(R.id.iv10);	i_views[1][0].setTag("10"+P_WN);
        i_views[2][0] = (ImageView) findViewById(R.id.iv20);	i_views[2][0].setTag("20"+P_WB);
        i_views[3][0] = (ImageView) findViewById(R.id.iv30);	i_views[3][0].setTag("30"+P_WQ);
        i_views[4][0] = (ImageView) findViewById(R.id.iv40);	i_views[4][0].setTag("40"+P_WK);
        i_views[5][0] = (ImageView) findViewById(R.id.iv50);	i_views[5][0].setTag("50"+P_WB);
        i_views[6][0] = (ImageView) findViewById(R.id.iv60);	i_views[6][0].setTag("60"+P_WN);
        i_views[7][0] = (ImageView) findViewById(R.id.iv70);	i_views[7][0].setTag("70"+P_WR);
        //
        for (int file = 0; file <= 7; file++) {
            for (int rank = 0; rank <= 7; rank++) {
                setBackground(file, rank, false);
                //
                if (lister!=null) {
                    i_views[file][rank].setOnClickListener(lister);
                }
            }
        }
    }


    protected void doCleanupChessBoard(Button btn_Draw) {
        setPlayer(true, false, btn_Draw);
        //
        if (message!=null) {
            message.setText("");
        }
        //
        i_views[0][7].setImageResource(R.drawable.blackrook);	i_views[0][7].setTag("07"+P_BR);
        i_views[1][7].setImageResource(R.drawable.blackknight);	i_views[1][7].setTag("17"+P_BN);
        i_views[2][7].setImageResource(R.drawable.blackbishop);	i_views[2][7].setTag("27"+P_BB);
        i_views[3][7].setImageResource(R.drawable.blackqueen);	i_views[3][7].setTag("37"+P_BQ);
        i_views[4][7].setImageResource(R.drawable.blackking);	i_views[4][7].setTag("47"+P_BK);
        i_views[5][7].setImageResource(R.drawable.blackbishop);	i_views[5][7].setTag("57"+P_BB);
        i_views[6][7].setImageResource(R.drawable.blackknight);	i_views[6][7].setTag("67"+P_BN);
        i_views[7][7].setImageResource(R.drawable.blackrook);	i_views[7][7].setTag("77"+P_BR);
        //
        i_views[0][6].setImageResource(R.drawable.blackpawn);	i_views[0][6].setTag("06"+P_BP);
        i_views[1][6].setImageResource(R.drawable.blackpawn);	i_views[1][6].setTag("16"+P_BP);
        i_views[2][6].setImageResource(R.drawable.blackpawn);	i_views[2][6].setTag("26"+P_BP);
        i_views[3][6].setImageResource(R.drawable.blackpawn);	i_views[3][6].setTag("36"+P_BP);
        i_views[4][6].setImageResource(R.drawable.blackpawn);	i_views[4][6].setTag("46"+P_BP);
        i_views[5][6].setImageResource(R.drawable.blackpawn);	i_views[5][6].setTag("56"+P_BP);
        i_views[6][6].setImageResource(R.drawable.blackpawn);	i_views[6][6].setTag("66"+P_BP);
        i_views[7][6].setImageResource(R.drawable.blackpawn);	i_views[7][6].setTag("76"+P_BP);
        //
        i_views[0][5].setImageResource(R.drawable.transparent);	i_views[0][5].setTag("05"+P_NO);
        i_views[1][5].setImageResource(R.drawable.transparent);	i_views[1][5].setTag("15"+P_NO);
        i_views[2][5].setImageResource(R.drawable.transparent);	i_views[2][5].setTag("25"+P_NO);
        i_views[3][5].setImageResource(R.drawable.transparent);	i_views[3][5].setTag("35"+P_NO);
        i_views[4][5].setImageResource(R.drawable.transparent);	i_views[4][5].setTag("45"+P_NO);
        i_views[5][5].setImageResource(R.drawable.transparent);	i_views[5][5].setTag("55"+P_NO);
        i_views[6][5].setImageResource(R.drawable.transparent);	i_views[6][5].setTag("65"+P_NO);
        i_views[7][5].setImageResource(R.drawable.transparent);	i_views[7][5].setTag("75"+P_NO);
        //
        i_views[0][4].setImageResource(R.drawable.transparent);	i_views[0][4].setTag("04"+P_NO);
        i_views[1][4].setImageResource(R.drawable.transparent);	i_views[1][4].setTag("14"+P_NO);
        i_views[2][4].setImageResource(R.drawable.transparent);	i_views[2][4].setTag("24"+P_NO);
        i_views[3][4].setImageResource(R.drawable.transparent);	i_views[3][4].setTag("34"+P_NO);
        i_views[4][4].setImageResource(R.drawable.transparent);	i_views[4][4].setTag("44"+P_NO);
        i_views[5][4].setImageResource(R.drawable.transparent);	i_views[5][4].setTag("54"+P_NO);
        i_views[6][4].setImageResource(R.drawable.transparent);	i_views[6][4].setTag("64"+P_NO);
        i_views[7][4].setImageResource(R.drawable.transparent);	i_views[7][4].setTag("74"+P_NO);
        //
        i_views[0][3].setImageResource(R.drawable.transparent);	i_views[0][3].setTag("03"+P_NO);
        i_views[1][3].setImageResource(R.drawable.transparent);	i_views[1][3].setTag("13"+P_NO);
        i_views[2][3].setImageResource(R.drawable.transparent);	i_views[2][3].setTag("23"+P_NO);
        i_views[3][3].setImageResource(R.drawable.transparent);	i_views[3][3].setTag("33"+P_NO);
        i_views[4][3].setImageResource(R.drawable.transparent);	i_views[4][3].setTag("43"+P_NO);
        i_views[5][3].setImageResource(R.drawable.transparent);	i_views[5][3].setTag("53"+P_NO);
        i_views[6][3].setImageResource(R.drawable.transparent);	i_views[6][3].setTag("63"+P_NO);
        i_views[7][3].setImageResource(R.drawable.transparent);	i_views[7][3].setTag("73"+P_NO);
        //
        i_views[0][2].setImageResource(R.drawable.transparent);	i_views[0][2].setTag("02"+P_NO);
        i_views[1][2].setImageResource(R.drawable.transparent);	i_views[1][2].setTag("12"+P_NO);
        i_views[2][2].setImageResource(R.drawable.transparent);	i_views[2][2].setTag("22"+P_NO);
        i_views[3][2].setImageResource(R.drawable.transparent);	i_views[3][2].setTag("32"+P_NO);
        i_views[4][2].setImageResource(R.drawable.transparent);	i_views[4][2].setTag("42"+P_NO);
        i_views[5][2].setImageResource(R.drawable.transparent);	i_views[5][2].setTag("52"+P_NO);
        i_views[6][2].setImageResource(R.drawable.transparent);	i_views[6][2].setTag("62"+P_NO);
        i_views[7][2].setImageResource(R.drawable.transparent);	i_views[7][2].setTag("72"+P_NO);
        //
        i_views[0][1].setImageResource(R.drawable.whitepawn);	i_views[0][1].setTag("01"+P_WP);
        i_views[1][1].setImageResource(R.drawable.whitepawn);	i_views[1][1].setTag("11"+P_WP);
        i_views[2][1].setImageResource(R.drawable.whitepawn);	i_views[2][1].setTag("21"+P_WP);
        i_views[3][1].setImageResource(R.drawable.whitepawn);	i_views[3][1].setTag("31"+P_WP);
        i_views[4][1].setImageResource(R.drawable.whitepawn);	i_views[4][1].setTag("41"+P_WP);
        i_views[5][1].setImageResource(R.drawable.whitepawn);	i_views[5][1].setTag("51"+P_WP);
        i_views[6][1].setImageResource(R.drawable.whitepawn);	i_views[6][1].setTag("61"+P_WP);
        i_views[7][1].setImageResource(R.drawable.whitepawn);	i_views[7][1].setTag("71"+P_WP);
        //
        i_views[0][0].setImageResource(R.drawable.whiterook);	i_views[0][0].setTag("00"+P_WR);
        i_views[1][0].setImageResource(R.drawable.whiteknight);	i_views[1][0].setTag("10"+P_WN);
        i_views[2][0].setImageResource(R.drawable.whitebishop);	i_views[2][0].setTag("20"+P_WB);
        i_views[3][0].setImageResource(R.drawable.whitequeen);	i_views[3][0].setTag("30"+P_WQ);
        i_views[4][0].setImageResource(R.drawable.whiteking);	i_views[4][0].setTag("40"+P_WK);
        i_views[5][0].setImageResource(R.drawable.whitebishop);	i_views[5][0].setTag("50"+P_WB);
        i_views[6][0].setImageResource(R.drawable.whiteknight);	i_views[6][0].setTag("60"+P_WN);
        i_views[7][0].setImageResource(R.drawable.whiterook);	i_views[7][0].setTag("70"+P_WR);
        //
        for (int file = 0; file <= 7; file++) {
            for (int rank = 0; rank <= 7; rank++) {
                setBackground(file, rank, false);
            }
        }
    }
}

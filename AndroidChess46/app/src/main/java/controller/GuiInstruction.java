package controller;

import java.io.Serializable;

/**
 * GUI instruction for a move (including rollback)
 *
 * @author Dhruvil Shah
 * @author Drashti Mehta
 */

public class GuiInstruction implements Serializable {

    public String m;
    public String rollB;

    public boolean isWhite;
    public String msg;

    public GuiInstruction(String _m, String _rollB) {
        m            = _m;
        rollB        = _rollB;
        isWhite         = false;
        msg         = "UKN";
    }


    public String getMove() {
        return m;
    }

    public String getRollback() {
        return rollB;
    }

    public boolean isWhite() {
        return isWhite;
    }

    public String getMessage() {
        return msg;
    }

    public void setWhite(boolean white) {
        isWhite = white;
    }

    public void setMessage(String message) {
        this.msg = message;
    }
}

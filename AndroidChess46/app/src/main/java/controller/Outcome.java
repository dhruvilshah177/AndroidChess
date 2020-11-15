package controller;

/**
 * Outcome of a move.
 *
 * @author Dhruvil Shah
 * @author Drashti Mehta
 */
public class Outcome {

    public String getGuiInstruction() {
        return guiInst;
    }

    public void setGuiInstruction(String guiInstruct) {
        this.guiInst = guiInstruct;
    }

    private String guiInst;

    /**
     * If move was successful
     */
    private boolean isSuccess;

    /**
     * Reason behind an invalid move
     */
    private String 	reason;

    /**
     * If opponent is in check after move
     */
    private boolean isOpponentInCheck = false;

    /**
     * If opponent is in checkmate after move
     */
    private boolean isOpponentCheckMate	= false;

    /**
     * If opponent is in stalemate after move
     */
    private boolean isOpponentStaleMate	= false;

    public boolean isProposingDraw() {
        return isProposingDraw;
    }

    public void setProposingDraw(boolean proposingD) { isProposingDraw = proposingD; }

    /**
     * If proposing draw
     */
    private boolean isProposingDraw	= false;

    /**
     * @param inBool True if move is successful
     * @param inString Reason of invalid move
     */
    public Outcome(boolean inBool, String inString) {
        isSuccess = inBool;
        reason 	= inString;
    }

    /**
     * @return True if move is successful
     */
    public boolean isOK() {
        return isSuccess;
    }

    /**
     * @return Reason of invalid move
     */
    public String getReason() {
        return reason;
    }

    /**
     * @return Reason of invalid move
     */
    public void setReason(String input) {
        reason = input;
    }

    /**
     * @return True if opponent is in check after move
     */
    public boolean isOpponentInCheck() {
        return isOpponentInCheck;
    }

    /**
     * Set isOpponentInCheck to true.
     */
    public void setOpponentInCheck() {
        this.isOpponentInCheck = true;
    }

    /**
     * @return True if opponent is in checkmate after move
     */
    public boolean isOpponentCheckMate() {
        return isOpponentCheckMate;
    }

    /**
     * Set isOpponentCheckMate to true.
     */
    public void setOpponentCheckMate() {
        this.isOpponentCheckMate = true;
    }

    /**
     * @return True if opponent is in stalemate after move
     */
    public boolean isOpponentStaleMate() {
        return isOpponentStaleMate;
    }

    /**
     * Set isOpponentStaleMate to true.
     */
    public void setOpponentStaleMate() {
        this.isOpponentStaleMate = true;
    }
}
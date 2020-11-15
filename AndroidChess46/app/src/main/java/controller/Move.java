package controller;

import java.util.Arrays;
import java.util.List;

import model.BoardIndex;

/**
 * Represents a move by a player.
 *
 * @author Dhruvil Shah
 * @author Drashti Mehta
 */
public class Move {

    /**
     * All the possible choices a player has when promoting a pawn.
     */
    public enum Promotion {
        NONE, Knight, Queen, Rook, Bishop
    }

    /**
     * Current position.
     */
    private BoardIndex currentBoardI = null;

    /**
     * Target position.
     */
    private BoardIndex targetBoardI  = null;

    /**
     * In the case that player resigns.
     */
    private boolean isResign = false;

    /**
     * In the case that the player accepts draw proposal.
     */
    private boolean isDraw = false;

    /**
     * In the case that the player proposes draw.
     */
    private boolean isAskingDraw = false;

    /**
     * Artificial Intelligence.
     */
    private boolean isAI = false;

    /**
     * Rollback.
     */
    private boolean isRollback = false;

    /**
     * In the case that the player chooses promotion.
     */
    private Promotion promotion;

    /**
     * In the case that the move is valid.
     */
    private boolean isValid = true;

    /**
     * Give reason for invalid move.
     */
    private String reason = "OK";

    /**
     * @return Current position.
     */
    public BoardIndex getCurrentBoardIndex() {
        return currentBoardI;
    }

    /**
     * @return Target position.
     */
    public BoardIndex getTargetBoardIndex() {
        return targetBoardI;
    }

    /**
     * @return True if player resigns.
     */
    public boolean isResign() {
        return isResign;
    }

    /**
     * @return True if player accepts the draw proposal.
     */
    public boolean isDraw() {
        return isDraw;
    }

    /**
     * @return True if player proposes draw.
     */
    public boolean isAskingDraw() {
        return isAskingDraw;
    }

    /**
     * @return True if AI.
     */
    public boolean isAI() {
        return isAI;
    }

    /**
     * @return True if Rollback.
     */
    public boolean isRollback() {
        return isRollback;
    }

    /**
     * @return Promotion if player chooses promotion.
     */
    public Promotion getPromotion() {
        return promotion;
    }

    /**
     * @return Promotion if player chooses promotion.
     */
    public void setPromotion(Promotion input) {
        promotion = input;
    }

    /**
     * @return True if valid move.
     */
    public boolean isValid() {
        return isValid;
    }

    /**
     * @return Reason of invalid move.
     */
    public String getReason() {
        return reason;
    }


    public Move(int toFile, int toRank, int fromFile, int fromRank, boolean _isAskingDraw) {
        currentBoardI   = new BoardIndex(fromFile, fromRank);
        targetBoardI    = new BoardIndex(toFile, toRank);
        isResign            = false;
        isDraw              = false;
        isAskingDraw        = _isAskingDraw;
        isAI               	= false;
        isRollback          = false;
        promotion           = Promotion.NONE;
    }

    /**
     * @param input Player input.
     */
    public Move(String input) {
        promotion = Promotion.NONE;
        List<String> tokens = Arrays.asList(input.split("\\s+"));
        String first = tokens.get(0).trim().toUpperCase();
        if (first.equalsIgnoreCase("resign")) {
            isResign = true;
        }
        else if (first.equalsIgnoreCase("draw")) {
            isDraw = true;
        }
        else if (first.equalsIgnoreCase("AI")) {
            isAI = true;
        }
        else if (first.equalsIgnoreCase("Rollback")) {
            isRollback = true;
        }
        else if (first.length() == 2) {
            char charFile1 = first.charAt(0);
            char charRank1 = first.charAt(1);
            if (charFile1 >= 'A' && charFile1 <= 'H' && charRank1 >= '1' && charRank1 <= '8') {
                currentBoardI = new BoardIndex(charFile1-'A', charRank1 - '1');
            }
            else {
                isValid = false;
                reason = "Please enter again - illegal input";
            }
            if (tokens.size() >= 2) {
                String second = tokens.get(1).trim().toUpperCase();
                if (second.length() == 2) {
                    char charFile2 = second.charAt(0);
                    char charRank2 = second.charAt(1);
                    if (charFile2 >= 'A' && charFile2 <= 'H' && charRank2 >= '1' && charRank2 <= '8') {
                        targetBoardI = new BoardIndex(charFile2 - 'A', charRank2 - '1');
                    }
                }
                else {
                    isValid = false;
                    reason = "Illegal input, enter again";
                }
                if (tokens.size() >= 3) {
                    String third = tokens.get(2).trim();
                    if (third.equalsIgnoreCase("draw?")) {
                        isAskingDraw = true;
                    }
                    else if (third.equalsIgnoreCase("Q")) {
                        promotion = Promotion.Queen;
                    }
                    else if (third.equalsIgnoreCase("N")) {
                        promotion = Promotion.Knight;
                    }
                    else if (third.equalsIgnoreCase("R")) {
                        promotion = Promotion.Rook;
                    }
                    else if (third.equalsIgnoreCase("B")) {
                        promotion = Promotion.Bishop;
                    }
                }
            }
            else {
                isValid = false;
                reason = "Please enter again - illegal input";
            }
        }
        else {
            isValid = false;
            reason = "Please enter again - illegal input";
        }
    }

    public void setSourceAndTargetForAIMove(BoardIndex source, BoardIndex target) {
        currentBoardI = new BoardIndex(source) ;
        targetBoardI  = new BoardIndex(target);
    }
}
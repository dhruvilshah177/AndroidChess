package model;

/**
 * Class for players.
 *
 * @author Dhruvil Shah
 * @author Drashti Mehta
 */
public class Player {

    /**
     * Color of the player - true if white, false if black.
     */
    private boolean isWhiteP;

    /**
     * @param _isWhiteP Player
     */
    public Player(boolean _isWhiteP) {
        isWhiteP = _isWhiteP;
    }

    /**
     * @return Prompt for player to enter moves.
     */
    public String getPrompt() {
        return (isWhiteP ? "White" : "Black") + "'s move: ";
    }

    /**
     * @return True if it is a White player, false if is a Black player.
     */
    public boolean isWhitePlayer() {
        return isWhiteP;
    }
}
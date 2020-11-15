package controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * Collection of all GUI instructions for the game; could be saved to file for later replay
 *
 * @author Dhruvil Shah
 * @author Drashti Mehta
 */
public class GuiGame implements Serializable {
    public List<GuiInstruction> moves;

    public GuiGame() {
        moves = new ArrayList<>();
    }

    public void addOne(GuiInstruction input) {
        moves.add(input);
    }

    public GuiInstruction rollbackOne() {
        if (moves.size()>=1) {
            return moves.remove(moves.size() - 1);
        }
        else {
            return null;
        }
    }

    public GuiInstruction getAt(int i) {
        if (i>=0 && i<=moves.size()-1) {
            return moves.get(i);
        }
        else {
            return null;
        }
    }

    public int getMoveCount() {
        return moves.size();
    }
}

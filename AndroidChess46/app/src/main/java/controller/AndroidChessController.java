package controller;

import java.util.ArrayList;
import java.util.List;

import model.Chessboard;
import model.Player;

/**
 * The main controller of the app
 *
 * @author Dhruvil Shah
 * @author Drashti Mehta
 */
public class AndroidChessController {

    public Chessboard chessB;
    public GuiGame gGame;
    public boolean iswhiteTurn;
    public boolean isDrawProposed;
    public boolean isCheck;
    public String sresult;
    public List<Player> players;

    public AndroidChessController() {
        chessB = new Chessboard();
        players = new ArrayList<>();
        players.add(new Player(true));
        players.add(new Player(false));
        gGame = new GuiGame();
        iswhiteTurn    = true;
        isDrawProposed  = false;
        isCheck         = false;
        sresult        = null;
    }

    public Outcome doMove(Move m) {
        Outcome outcome;
        if (m.isValid()) {
            if (m.isDraw()) {
                if (m.isResign()) {
                    sresult = !getCurrentPlayer().isWhitePlayer() ? "White wins" : "Black wins";
                    outcome = new Outcome(true, sresult);
                    GuiInstruction guiInst = new GuiInstruction("NOMOVE", "NOMOVE");
                    guiInst.setMessage(sresult);
                    gGame.addOne(guiInst);
                    outcome.setReason(sresult);
                }
                else if (isDrawProposed) {
                    sresult = "Draw";
                    outcome = new Outcome(true, sresult);
                    GuiInstruction guiInst = new GuiInstruction("NOMOVE", "NOMOVE");
                    guiInst.setMessage(sresult);
                    gGame.addOne(guiInst);
                    outcome.setReason(sresult);
                }
                else {
                    outcome = new Outcome(false, "Please try again - illegal move");
                }
            }
            else {
                outcome = chessB.doMove(m, getCurrentPlayer().isWhitePlayer());
                if (outcome.isOK()) {
                    if (m.isRollback()) {
                        GuiInstruction g = gGame.rollbackOne();
                        outcome.setGuiInstruction(g.getRollback());
                        switchPlayer();
                    }
                    else {
                        LastMoveForRollback lastMove = chessB.getLastMoveForRollback();
                        GuiInstruction g = lastMove.getGUIInstruction();
                        g.setWhite(getCurrentPlayer().isWhitePlayer());
                        gGame.addOne(g);
                        outcome.setGuiInstruction(g.getMove());
                        if (outcome.isOpponentCheckMate()) {
                            sresult = getCurrentPlayer().isWhitePlayer() ? "White wins" : "Black wins";
                            outcome.setReason(sresult);
                        }
                        if (outcome.isOpponentStaleMate()) {
                            sresult = "Draw";
                            outcome.setReason(sresult);
                        }
                        else {
                            switchPlayer();
                            isDrawProposed = m.isAskingDraw();
                            outcome.setProposingDraw(isDrawProposed);
                            isCheck = outcome.isOpponentInCheck();
                            if (isCheck) {
                                outcome.setReason("Check");
                            }
                        }
                        g.setMessage(outcome.getReason());
                    }
                }
                else {
                    outcome = new Outcome(false, "Please try again - illegal move");
                }
            }
        }
        else {
            outcome = new Outcome(false, "Please try again - illegal move");
        }
        return outcome;
    }


    /**
     * Check whose turn it is - black or white.
     */
    public void switchPlayer() {
        iswhiteTurn = !iswhiteTurn;
    }

    /**
     * @return The current player.
     */
    public Player getCurrentPlayer() {
        return players.get(iswhiteTurn ? 0 : 1);
    }

    public boolean gameEnded() {
        return sresult!=null;
    }


    /**
     * @param parameter Input file.
     */
    public void run(String parameter) {
        boolean isCheck = false;
        boolean isDrawProposed = false;
        while (true) {
            String input;
            {
                if (isCheck) {
                    System.out.println("Check");
                    System.out.println();
                }
                System.out.print(getCurrentPlayer().getPrompt());
                System.out.println();
            }

            Move m = new Move("");
            if (m.isValid()) {
                if (m.isDraw()) {
                    if (isDrawProposed) {
                        System.out.println("Draw");
                        break;
                    }
                    System.out.println("Please try again - Illegal move");
                    System.out.println();
                }
                else if (m.isResign()) {
                    System.out.println(!getCurrentPlayer().isWhitePlayer() ? "White wins" : "Black wins");
                    break;
                }
                else {
                    Outcome outcome = chessB.doMove(m, getCurrentPlayer().isWhitePlayer());
                    if (outcome.isOK()) {
                        System.out.println();
                        if (outcome.isOpponentStaleMate()) {
                            System.out.println("Stalemate");
                            System.out.println();
                            System.out.println("Draw");
                            break;
                        }
                        if (outcome.isOpponentCheckMate()) {
                            System.out.println("Checkmate");
                            System.out.println();
                            System.out.println(getCurrentPlayer().isWhitePlayer() ? "White wins" : "Black wins");
                            break;
                        }
                        else {
                            switchPlayer();
                            isDrawProposed = m.isAskingDraw();
                            isCheck = outcome.isOpponentInCheck();
                        }
                    }
                    else {
                        System.out.println("Please try again - illegal move");
                        System.out.println();
                    }
                }
            }
            else {
                System.out.println("Please try again - illegal move");
                System.out.println();
            }
        }
    }
}
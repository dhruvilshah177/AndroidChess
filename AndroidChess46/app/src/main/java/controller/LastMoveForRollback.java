package controller;

import model.BoardIndex;
import model.Piece;

/**
 * Holds Last move for potential rollback
 *
 * @author Dhruvil Shah
 * @author Drashti Mehta
 */
public class LastMoveForRollback {

	public BoardIndex 	sourceI;
	public BoardIndex 	targetI;
	public BoardIndex 	sourceIndexR;
	public BoardIndex 	targetIndexR;
	public boolean		isMove;
	public Piece		removedReg;
	public Piece		removedEnPass;
	public Piece		removedisProm;
	public String		promotionTo;
	public boolean		isRegularMove;
	public boolean		isCastling;
	public boolean		isEnPassant;
	public boolean		isPromotion;
	public Piece 		lastTwoStepPawnMove	= null;
	public Piece 		lastTwoStepPawnMovePrev	= null;


	public void doInit(boolean isRollback) {
		sourceI			= null;
		targetI			= null;
		sourceIndexR	= null;
		targetIndexR	= null;
		isMove			= false;
		removedReg		= null;
		removedEnPass	= null;
		removedisProm	= null;
		promotionTo		= null;
		isRegularMove	= false;
		isCastling		= false;
		isEnPassant		= false;
		isPromotion		= false;

		if (isRollback) {
			lastTwoStepPawnMove	= lastTwoStepPawnMovePrev;
			lastTwoStepPawnMovePrev	= null;
		}
		else {
			lastTwoStepPawnMovePrev	= lastTwoStepPawnMove;
			lastTwoStepPawnMove	= null;
		}
	}

	/**
	 * @return Pawn who moved 2 steps in the last move
	 */
	public Piece getLastTwoStepMovePawn() {
		return lastTwoStepPawnMove;
	}

	public GuiInstruction getGUIInstruction() {
		String move;
		String back;
		if (isRegularMove) {
			move 	= "M" + targetI.toString() + sourceI.toString();
			back	= "M" + sourceI.toString() + targetI.toString();
			if (removedReg!=null) {
				back = back + ";A" + removedReg.toString() + removedReg.getBoardIndex().toString();
			}
		}
		else if (isCastling) {
			move 	= "M" + targetI.toString() + sourceI.toString() + ";" + "M" + targetIndexR.toString() + sourceIndexR.toString();
			back	= "M" + sourceI.toString() + targetI.toString() + ";" + "M" + sourceIndexR.toString() + targetIndexR.toString();
		}
		else if (isEnPassant) {
			move 	= "M" + targetI.toString() + sourceI.toString();
			move 	= move + ";R" + removedEnPass.getBoardIndex().toString();
			back	= "M" + sourceI.toString() + targetI.toString();
			back 	= back + ";A" + removedEnPass.toString() + removedEnPass.getBoardIndex().toString();
		}
		else if (isPromotion) {
			move 	= "R" + sourceI.toString();
			move 	= move + ";A" + promotionTo + targetI.toString();
			back	= "A" + removedisProm.toString() + sourceI.toString();
			if (removedReg!=null) {
				back = back + ";A" + removedReg.toString() + removedReg.getBoardIndex().toString();
			}
			else {
				back = back + ";R" + targetI.toString();
			}
		}
		else {
			move = "";
			back = "";
		}
		return new GuiInstruction(move, back);
	}
}




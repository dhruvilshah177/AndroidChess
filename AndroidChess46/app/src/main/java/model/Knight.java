package model;

import controller.DirectionDistance;
import controller.Move.Promotion;
import controller.Outcome;
import controller.SourceAndTarget;

/**
 * This class represents the Knight.
 *
 * @author Dhruvil Shah
 * @author Drashti Mehta
 */
public class Knight extends Piece{

	/**
	 * @param isWhite Color of piece - true if white, false if black.
	 * @param f File of current location
	 * @param r Rank of current location
	 * @param chessB Chessboard
	 */
	public Knight(boolean isWhite, int f, int r,  Chessboard chessB) {
		super(isWhite, f, r, chessB);
		chessB.getPieceMap().put(getKey(), this);
	}

	/**
	 * @param isWhite Color of piece - true if white, false if black.
	 * @param fileR Input File and Rank
	 * @param chessB Chessboard
	 */
	public Knight(boolean isWhite, String fileR, Chessboard chessB) {
		super(isWhite, fileR, chessB);
		chessB.getPieceMap().put(getKey(), this);
	}

	/**
	 * @param targetI Target index a piece moves too
	 * @param promotion Promotion. Pawn only.
	 * @return Outcome of the move
	 */
	public Outcome doMove(BoardIndex targetI, Promotion promotion) {
		return doMoveInternal(targetI, true);
	}


	/**
	 * @param targetI Target index a piece moves too
	 * @return Outcome if the piece can attack target
	 */
	public Outcome doAttack(BoardIndex targetI) {
		return doMoveInternal(targetI, false);
	}


	/**
	 * @param targetI Target index a piece moves too
	 * @param doMove True if piece perform move, false checks if piece can attack target
	 * @return Outcome of the move
	 */
	private Outcome doMoveInternal(BoardIndex targetI, boolean doMove) {
		Outcome outcome;
		BoardIndex currentIndex = getBoardIndex();
		DirectionDistance dd = new DirectionDistance(currentIndex.fileI, currentIndex.rankI, targetI.fileI, targetI.rankI);
		if (dd.isKnightly()) {
			if (isTargetEmptyOrLegal(targetI)) {
				if (doMove) {
					boolean isLegal = doActualMove(targetI, false);
					if (isLegal) {
						outcome = new Outcome(true, "OK");
					}
					else {
						outcome = new Outcome(false, "Please try again - illegal move");
					}
				}
				else {
					outcome = new Outcome(true, "OK");
				}
			}
			else {
				outcome = new Outcome(false, "Please try again - illegal move");
			}
		}
		else {
			outcome = new Outcome(false, "Please try again - illegal move");
		}
		return outcome;
	}

	/**
	 * @return True if Knight has legal move for checkmate and stalemate.
	 */
	@Override
	public boolean hasLegalMove() {
		return hasLegalMoveOneDirectionOneStep(1, 2)!=null
				|| hasLegalMoveOneDirectionOneStep(1, -2)!=null
				|| hasLegalMoveOneDirectionOneStep(2, 1)!=null
				|| hasLegalMoveOneDirectionOneStep(2, -1)!=null
				|| hasLegalMoveOneDirectionOneStep(-1, 2)!=null
				|| hasLegalMoveOneDirectionOneStep(-1, -2)!=null
				|| hasLegalMoveOneDirectionOneStep(-2, 1)!=null
				|| hasLegalMoveOneDirectionOneStep(-2, -1)!=null;
	}



	@Override
	public SourceAndTarget getOneLegalMove() {
		SourceAndTarget sat = null;
		sat = hasLegalMoveOneDirectionOneStep(1, 2);
		if (sat!=null) {
			return sat;
		}
		sat = hasLegalMoveOneDirectionOneStep(1, -2);
		if (sat!=null) {
			return sat;
		}
		sat = hasLegalMoveOneDirectionOneStep(2, 1);
		if (sat!=null) {
			return sat;
		}
		sat = hasLegalMoveOneDirectionOneStep(2, -1);
		if (sat!=null) {
			return sat;
		}
		sat = hasLegalMoveOneDirectionOneStep(-1, 2);
		if (sat!=null) {
			return sat;
		}
		sat = hasLegalMoveOneDirectionOneStep(-1, -2);
		if (sat!=null) {
			return sat;
		}
		sat = hasLegalMoveOneDirectionOneStep(-2, 1);
		if (sat!=null) {
			return sat;
		}
		sat = hasLegalMoveOneDirectionOneStep(-2, -1);
		if (sat!=null) {
			return sat;
		}
		return sat;
	}

	/**
	 * @return The board output for the Knight.
	 */
	@Override
	public String toString() {
		return super.toString() + "N";
	}
}
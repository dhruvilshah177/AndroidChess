package model;

import controller.DirectionDistance;
import controller.Move.Promotion;
import controller.Outcome;
import controller.SourceAndTarget;

/**
 * Class for the Pawn.
 *
 * @author Dhruvil Shah
 * @author Drashti Mehta
 */
public class Pawn extends Piece{

	/**
	 * @param isWhite Color of piece - true if white, false if black.
	 * @param f File of current location
	 * @param r Rank of current location
	 * @param chessB Chessboard
	 */
	public Pawn(boolean isWhite, int f, int r, Chessboard chessB) {
		super(isWhite, f, r, chessB);
		chessB.getPieceMap().put(getKey(), this);
	}

	/**
	 * @param isWhite Color of piece - true if white, false if black.
	 * @param fileR Input File and Rank
	 * @param chessB Chessboard
	 */
	public Pawn(boolean isWhite, String fileR, Chessboard chessB) {
		super(isWhite, fileR, chessB);
		chessB.getPieceMap().put(getKey(), this);
	}

	/**
	 * @param targetI Target position of a move
	 * @param promotion Promotion. Pawn only.
	 * @return Outcome of the move.
	 */
	@Override
	public Outcome doMove(BoardIndex targetI, Promotion promotion) {
		return doMoveInternal(targetI, true, false, promotion);
	}

	/**
	 * @param targetI Target position of a move
	 * @return Outcome if the piece can attack target
	 */
	public Outcome doAttack(BoardIndex targetI) {
		Promotion promotion;
		promotion = Promotion.NONE;
		return doMoveInternal(targetI, false, false, promotion);
	}

	/**
	 * @param targetI Target index a piece moves too
	 * @param doMove True if piece perform move, false checks if piece can attack target.
	 * @param rollback If true, rollback after checking move
	 * @param promotion Promotion when pawn reaches correct rank.
	 * @return Outcome of the move
	 */
	private Outcome doMoveInternal(BoardIndex targetI, boolean doMove, boolean rollback, Promotion promotion) {
		Outcome outcome;
		BoardIndex currentIndex = getBoardIndex();
		DirectionDistance dd = new DirectionDistance(currentIndex.fileI, currentIndex.rankI, targetI.fileI, targetI.rankI);
		if (doMove && dd.isPawOneStep(isWhite())) {
			if (isTargetEmpty(targetI)) {
				if (doMove) {
					boolean isPromotion = dd.isPawnPromotion(isWhite());
					boolean isLegal;
					if (isPromotion) {
						isLegal = doActualMoveIsPromotion(targetI, promotion, rollback);
					}
					else {
						isLegal = doActualMove(targetI, rollback);
					}
					outcome = isLegal ? new Outcome(true, "OK") : new Outcome(false, "Please try again - illegal move");
				}
				else {
					outcome = new Outcome(true, "OK");
				}
			}
			else {
				outcome = new Outcome(false, "Illegal move, try again");
			}
		}
		else if (dd.isPawnKill(isWhite())) {
			if (isTargetLegal(targetI)) {
				if (doMove) {
					boolean isPromotion = dd.isPawnPromotion(isWhite());
					boolean isLegal;
					if (isPromotion) {
						isLegal = doActualMoveIsPromotion(targetI, promotion, rollback);
					}
					else {
						isLegal = doActualMove(targetI, rollback);
					}
					outcome = isLegal ? new Outcome(true, "OK") : new Outcome(false, "Please try again - illegal move");
				}
				else {
					outcome = new Outcome(true, "OK");
				}
			}
			else if (isTargetEmpty(targetI) && dd.isEnPassant(isWhite())) {
				BoardIndex enPassantIndex = new BoardIndex(targetI.fileI, currentIndex.rankI);
				if (isTargetLegalEnPassant(enPassantIndex)) {
					if (doMove) {
						boolean isLegal = doActualMoveIsEnPassant(targetI, enPassantIndex, rollback);
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
		}
		else if (doMove && dd.isPawnTwoStep(isWhite())) {
			if (isTargetEmpty(targetI) && isTargetEmpty(new BoardIndex(currentIndex.fileI, (currentIndex.rankI+targetI.rankI)/2))) {
				if (doMove) {
					boolean isLegal = doActualMove(targetI, rollback);
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
	 * @return True if Pawn has legal move.
	 */
	@Override
	public boolean hasLegalMove() {
		if (isWhite()) {
			return 		isLegalMove(0, 1)!=null
					|| 	isLegalMove(0, 2)!=null
					|| 	isLegalMove(1, 1)!=null
					|| 	isLegalMove(-1, 1)!=null;
		}
		return 		isLegalMove(0, -1)!=null
				|| 	isLegalMove(0, -2)!=null
				|| 	isLegalMove(1, -1)!=null
				|| 	isLegalMove(-1, -1)!=null;
	}

	@Override
	public SourceAndTarget getOneLegalMove() {
		SourceAndTarget sat = null;
		if (isWhite()) {
			sat = isLegalMove(0, 1);
			if (sat!=null) {
				return sat;
			}
			sat = isLegalMove(0, 2);
			if (sat!=null) {
				return sat;
			}
			sat = isLegalMove(1, 1);
			if (sat!=null) {
				return sat;
			}
			sat = isLegalMove(-1, 1);
			if (sat!=null) {
				return sat;
			}
			return sat;
		}
		else {
			sat = isLegalMove(0, -1);
			if (sat!=null) {
				return sat;
			}
			sat = isLegalMove(0, -2);
			if (sat!=null) {
				return sat;
			}
			sat = isLegalMove(1, -1);
			if (sat!=null) {
				return sat;
			}
			sat = isLegalMove(-1, -1);
			if (sat!=null) {
				return sat;
			}
			return sat;
		}
	}

	/**
	 * @param deltaFile Change in file for each step.
	 * @param deltaRank Change in rank for each step.
	 * @return True if legal move after change, false if illegal move.
	 */
	private SourceAndTarget isLegalMove(int deltaFile, int deltaRank) {
		BoardIndex currentIndex = getBoardIndex();
		SourceAndTarget legalMove = null;
		int file = currentIndex.fileI + deltaFile;
		int rank = currentIndex.rankI + deltaRank;
		if (file >= 0 && file < 8 && rank >= 0 && rank < 8) {
			BoardIndex targetIndex = new BoardIndex(file, rank);
			Promotion promotion;
			promotion = Promotion.NONE;
			Outcome outcome = doMoveInternal(targetIndex, true, true, promotion);
			if (outcome.isOK()) {
				legalMove = new SourceAndTarget(currentIndex, targetIndex);
			}
			else {
			}
		}

		return legalMove;
	}

	/**
	 * @return The board output for Pawn.
	 */
	@Override
	public String toString() {
		return super.toString() + "P";
	}
}
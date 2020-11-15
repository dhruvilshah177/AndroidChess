package model;

import controller.DirectionDistance;
import controller.Move.Promotion;
import controller.Outcome;
import controller.SourceAndTarget;

import java.util.Map;

/**
 * This class represents the King.
 *
 * @author Dhruvil Shah
 * @author Drashti Mehta
 */
public class King extends Piece {

	/**
	 * @param isWhite Color of piece - true if white, false if black.
	 * @param f File of current location
	 * @param r Rank of current location
	 * @param chessB Chessboard
	 */
	public King(boolean isWhite, int f, int r, Chessboard chessB) {
		super(isWhite, f, r, chessB);
		chessB.getPieceMap().put(getKey(), this);
	}

	/**
	 * @param isWhite Color of piece - true if white, false if black.
	 * @param fileR Input File and Rank
	 * @param chessB Chessboard
	 */
	public King(boolean isWhite, String fileR, Chessboard chessB) {
		super(isWhite, fileR, chessB);
		chessB.getPieceMap().put(getKey(), this);
	}

	/**
	 * @param targetI Target index a piece moves too
	 * @param promotion Promotion. Only valid for pawns
	 * @return Outcome of move
	 */
	@Override
	public Outcome doMove(BoardIndex targetI, Promotion promotion) {
		return doMoveInternal(targetI, true);
	}

	/**
	 * @param targetI Target index a piece moves too
	 * @return Outcome if piece can attack target
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
		Map<String, Piece> pieceMap = chessb.getPieceMap();
		BoardIndex currentIndex = getBoardIndex();
		DirectionDistance dd = new DirectionDistance(currentIndex.fileI, currentIndex.rankI, targetI.fileI, targetI.rankI);
		if (dd.isRegal()) {
			if (isTargetEmptyOrLegal(targetI)) {
				if (doMove) {
					boolean isLegal = doActualMove(targetI, false);
					if (isLegal) {
						outcome = new Outcome(true, "OK");
					}
					else {
						outcome = new Outcome(false, "Illegal move, try again");
					}
				}
				else {
					outcome = new Outcome(true, "OK");
				}
			}
			else {
				outcome = new Outcome(false, "Illegal move, try again");
			}
		}

		else if (dd.isCastlingQS(isWhite()) || dd.isCastlingKS(isWhite())) {
			BoardIndex rookSourceI;
			BoardIndex rookTargetI;

			if (isWhite()) {
				if (dd.isCastlingQS(isWhite())) {
					rookSourceI = new BoardIndex(0, 0);
					rookTargetI = new BoardIndex(3, 0);
				}
				else {
					rookSourceI = new BoardIndex(7, 0);
					rookTargetI = new BoardIndex(5, 0);
				}
			}
			else if (dd.isCastlingQS(isWhite())) {
				rookSourceI = new BoardIndex(0, 7);
				rookTargetI = new BoardIndex(3, 7);
			}
			else {
				rookSourceI = new BoardIndex(7, 7);
				rookTargetI = new BoardIndex(5, 7);
			}

			Piece rook = pieceMap.get(rookSourceI.getKey());

			if (!isMoved() && (rook!=null) && !rook.isMoved()) {
				DirectionDistance ddCastling = new DirectionDistance(currentIndex.fileI, currentIndex.rankI, rookSourceI.fileI, rookSourceI.rankI);
				if (isNoneInBetween(ddCastling)) {
					boolean isUnderAttack1 = chessb.isUnderAttack(isWhite(), currentIndex);
					boolean isUnderAttack2 = chessb.isUnderAttack(isWhite(), targetI);
					boolean isUnderAttack3 = chessb.isUnderAttack(isWhite(), new BoardIndex((currentIndex.fileI+targetI.fileI)/2, currentIndex.rankI));
					if (!isUnderAttack1 && !isUnderAttack2 && !isUnderAttack3) {
						if (doMove) {
							boolean isLegal = doActualMoveCastling(targetI, rookSourceI, rookTargetI);
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
			else {
				outcome = new Outcome(false, "Please try again - illegal move");
			}
		}
		else {
			return new Outcome(false, "Please try again - illegal move");
		}
		return outcome;
	}

	/**
	 * @return True if piece has legal move for checkmate and stalemate, false otherwise.
	 */
	@Override
	public boolean hasLegalMove() {
		return hasLegalMoveOneDirectionOneStep(0, 1)!=null
				|| hasLegalMoveOneDirectionOneStep(0, -1)!=null
				|| hasLegalMoveOneDirectionOneStep(1, 0)!=null
				|| hasLegalMoveOneDirectionOneStep(-1, 0)!=null
				|| hasLegalMoveOneDirectionOneStep(1, 1)!=null
				|| hasLegalMoveOneDirectionOneStep(1, -1)!=null
				|| hasLegalMoveOneDirectionOneStep(-1, 1)!=null
				|| hasLegalMoveOneDirectionOneStep(-1, -1)!=null;
	}


	@Override
	public SourceAndTarget getOneLegalMove() {
		SourceAndTarget sat = null;
		sat = hasLegalMoveOneDirectionOneStep(0, 1);
		if (sat!=null) {
			return sat;
		}
		sat = hasLegalMoveOneDirectionOneStep(0, -1);
		if (sat!=null) {
			return sat;
		}
		sat = hasLegalMoveOneDirectionOneStep(1, 0);
		if (sat!=null) {
			return sat;
		}
		sat = hasLegalMoveOneDirectionOneStep(-1, 0);
		if (sat!=null) {
			return sat;
		}
		sat = hasLegalMoveOneDirectionOneStep(1, 1);
		if (sat!=null) {
			return sat;
		}
		sat = hasLegalMoveOneDirectionOneStep(1, -1);
		if (sat!=null) {
			return sat;
		}
		sat = hasLegalMoveOneDirectionOneStep(-1, 1);
		if (sat!=null) {
			return sat;
		}
		sat = hasLegalMoveOneDirectionOneStep(-1, -1);
		if (sat!=null) {
			return sat;
		}
		return sat;
	}

	/**
	 * @return The board output for the King.
	 */
	@Override
	public String toString() {
		return super.toString() + "K";
	}
}
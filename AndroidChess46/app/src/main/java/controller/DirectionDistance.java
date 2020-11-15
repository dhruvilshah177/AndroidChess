package controller;

/**
 * The direction and dist of each move.
 *
 * @author Dhruvil Shah
 * @author Drashti Mehta
 */
public class DirectionDistance {

	/**
	 * Represents the different directions a piece can move in.
	 */
	public enum Direction {
		NONE, NORTH, SOUTH, EAST, WEST, NW, NE, SW, SE
	}

	/**
	 * Source to target dist.
	 */
	private int dist;

	/**
	 * Direction in which the piece is moved.
	 */
	private Direction direct;

	/**
	 * updated file for each step.
	 */
	private int deltaF;

	/**
	 * updated rank for each step.
	 */
	private int deltaR;

	/**
	 * save the source file.
	 */
	private int m_sourceF;

	/**
	 * save the source rank.
	 */
	private int m_sourceR;

	/**
	 * save the target file.
	 */
	private int m_targetF;

	/**
	 * save the target rank.
	 */
	private int m_targetR;

	/**
	 * @param sourceF Current file.
	 * @param sourceR Current rank.
	 * @param targetF Target file
	 * @param targetR Target rank
	 */

	public DirectionDistance(int sourceF, int sourceR, int targetF, int targetR) {
		direct = Direction.NONE;
		m_sourceF 	= sourceF;
		m_sourceR	= sourceR;
		m_targetF	= targetF;
		m_targetR	= targetR;

		int dFile = targetF - sourceF;
		int dRank = targetR - sourceR;

		if (dFile == 0) {
			dist = Math.abs(dRank);
			if (dRank > 0) {
				direct	= Direction.NORTH;
				deltaF	= 0;
				deltaR	= 1;
			}
			else {
				direct	= Direction.SOUTH;
				deltaF	= 0;
				deltaR	= -1;
			}
		}
		else if (dRank == 0) {
			dist = Math.abs(dFile);
			if (dFile > 0) {
				direct	= Direction.EAST;
				deltaF	= 1;
				deltaR	= 0;
			}
			else {
				direct	= Direction.WEST;
				deltaF	= -1;
				deltaR	= 0;
			}
		}
		else if (Math.abs(dRank) == Math.abs(dFile)) {
			dist = Math.abs(dFile);
			if (dFile > 0 && dRank > 0) {
				direct	= Direction.NE;
				deltaF	= 1;
				deltaR	= 1;
			}
			else if (dFile > 0 && dRank < 0) {
				direct	= Direction.SE;
				deltaF	= 1;
				deltaR	= -1;
			}
			else if (dFile < 0 && dRank > 0) {
				direct	= Direction.NW;
				deltaF	= -1;
				deltaR	= 1;
			}
			else {
				direct	= Direction.SW;
				deltaF	= -1;
				deltaR	= -1;
			}
		}
	}


	/**
	 * @return Source to target dist.
	 */
	public int getDistance() {
		return dist;
	}


	/**
	 * @return updated file for each step
	 */
	public int getDeltaFile() {
		return deltaF;
	}


	/**
	 * @return updated rank for each step
	 */
	public int getDeltaRank() {
		return deltaR;
	}


	/**
	 * @return True if move is valid in every direction, false for illegal move.
	 */
	public boolean isValid() {
		return direct != Direction.NONE;
	}


	/**
	 * @return True if piece moved along the grid.
	 */
	public boolean isParallel() {
		return direct == Direction.NORTH || direct == Direction.SOUTH || direct == Direction.WEST || direct == Direction.EAST;
	}


	/**
	 * @return True if piece moved diagonally.
	 */
	public boolean isDiagonal() {
		return direct == Direction.NW || direct == Direction.NE || direct == Direction.SW || direct == Direction.SE;
	}

	/**
	 * @return True if valid moves are knight.
	 */
	public boolean isKnightly() {
		return	(m_targetF == m_sourceF + 1 && m_targetR == m_sourceR + 2 ) ||
				(m_targetF == m_sourceF + 1 && m_targetR == m_sourceR - 2 ) ||
				(m_targetF == m_sourceF + 2 && m_targetR == m_sourceR + 1 ) ||
				(m_targetF == m_sourceF + 2 && m_targetR == m_sourceR - 1 ) ||
				(m_targetF == m_sourceF - 1 && m_targetR == m_sourceR + 2 ) ||
				(m_targetF == m_sourceF - 1 && m_targetR == m_sourceR - 2 ) ||
				(m_targetF == m_sourceF - 2 && m_targetR == m_sourceR + 1 ) ||
				(m_targetF == m_sourceF - 2 && m_targetR == m_sourceR - 1 );
	}


	/**
	 * @return (King) True if piece moves in all directions 1 step.
	 */
	public boolean isRegal() {
		return isValid() && dist==1;
	}

	/**
	 * @param isWhite Color of the piece - true if white, false if black.
	 * @return True only if the King's side castling move is valid.
	 */
	public boolean isCastlingKS(boolean isWhite) {
		if (isWhite) {
			return m_sourceR == 0 && dist == 2 && direct==Direction.EAST;
		}
		return m_sourceR == 7 && dist == 2 && direct==Direction.EAST;
	}

	/**
	 * @param isWhite Color of the piece - true if white, false if black.
	 * @return True only if the Queen's side castling move is valid.
	 */
	public boolean isCastlingQS(boolean isWhite) {
		if (isWhite) {
			return m_sourceR == 0 && dist == 2 && direct ==Direction.WEST;
		}
		return m_sourceR == 7 && dist == 2 && direct ==Direction.WEST;
	}

	/**
	 * @param isWhite Color of the piece - true if white, false if black.
	 * @return True if pawn moves one step.
	 */
	public boolean isPawOneStep(boolean isWhite) {
		if (isWhite) {
			return direct == Direction.NORTH && dist == 1;
		}
		return direct == Direction.SOUTH && dist == 1;
	}

	/**
	 * @param isWhite Color of the piece - true if white, false if black.
	 * @return True if pawn moves 2 steps.
	 */
	public boolean isPawnTwoStep(boolean isWhite) {
		if (isWhite) {
			return direct == Direction.NORTH && dist == 2 && m_sourceR == 1;
		}
		return direct == Direction.SOUTH && dist == 2 && m_sourceR == 6;
	}

	/**
	 * @param isWhite Color of the piece - true if white, false if black.
	 * @return True if En passant is valid.
	 */
	public boolean isEnPassant(boolean isWhite) {
		if (isWhite) {
			return m_sourceR == 4 &&  dist == 1 && (direct == Direction.NW || direct == Direction.NE);
		}
		return m_sourceR == 3 && dist == 1 && (direct == Direction.SW || direct == Direction.SE);
	}

	/**
	 * @param isWhite Color of the piece - true if white, false if black.
	 * @return True if pawn is killed.
	 */
	public boolean isPawnKill(boolean isWhite) {
		if (isWhite) {
			return dist == 1 && (direct == Direction.NW || direct == Direction.NE);
		}
		return dist == 1 && (direct == Direction.SW || direct == Direction.SE);
	}

	/**
	 * @param isWhite Color of the piece - true if white, false if black.
	 * @return True if pawn is promoted.
	 */

	public boolean isPawnPromotion(boolean isWhite) {
		return isWhite ? m_targetR == 7 : m_targetR == 0;
	}
}
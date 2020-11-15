package controller;

import model.BoardIndex;


/**
 * Sources and Targets
 *
 * @author Dhruvil Shah
 * @author Drashti Mehta
 */
public class SourceAndTarget {
	BoardIndex sourceI;
	BoardIndex targetI;


	private SourceAndTarget() {
		sourceI = null;
		targetI	= null;
	}

	public SourceAndTarget(BoardIndex s, BoardIndex t) {
		sourceI = new BoardIndex(s);
		targetI	= new BoardIndex(t);
	}


	public BoardIndex getSource() {
		return sourceI;
	}


	public BoardIndex getTarget() {
		return targetI;
	}
}

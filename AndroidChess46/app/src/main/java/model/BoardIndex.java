package model;

/**
 * Index of a piece.
 *
 * @author Dhruvil Shah
 * @author Drashti Mehta
 */
public class BoardIndex {

    /**
     * @param fileIndex File
     * @param rankIndex Rank
     * @return Creates key for map of pieces.
     */
    public static String getKey(int fileIndex, int rankIndex) {
        return "" + fileIndex + "-" + rankIndex;
    }

    /**
     * File
     */
    public int fileI;

    /**
     * Rank
     */
    public int rankI;

    /**
     * @param _fileI File
     * @param _rankI Rank
     */
    public BoardIndex(int _fileI, int _rankI) {
        fileI = _fileI;
        rankI = _rankI;
    }
    public BoardIndex(BoardIndex input) {
        fileI = input.fileI;
        rankI = input.rankI;
    }

    /**
     * @param obj Same position.
     * @return True if in same position, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof BoardIndex)) {
            return false;
        }
        return this.fileI == ((BoardIndex)obj).fileI && this.rankI == ((BoardIndex)obj).rankI;
    }

    /**
     * @return Key for map of pieces.
     */
    public String getKey() {
        return getKey(fileI, rankI);
    }


    public String toString() {
        return "" + fileI + "" + rankI;
    }
}
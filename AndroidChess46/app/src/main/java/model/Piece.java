package model;

import java.util.Map;

import controller.DirectionDistance;
import controller.LastMoveForRollback;
import controller.Move.Promotion;
import controller.Outcome;
import controller.SourceAndTarget;

/**
 * Parent Class of all the pieces.
 *
 * @author Dhruvil Shah
 * @author Drashti Mehta
 */
public abstract class Piece {

    /**
     * Position of a piece.
     */
    private BoardIndex boardI = null;

    /**
     * Color of a piece - true if white, false if black.
     */
    private boolean isWhite = false;

    /**
     * Checks if piece is previously moved. Needed for castling.
     */
    private boolean isMoved = false;

    /**
     * Chessboard the piece belongs too.
     */
    protected Chessboard chessb = null;


    /**
     * @param _isWhite True if piece is white
     * @param f File
     * @param r Rank
     * @param chessB Chessboard
     */
    protected Piece(boolean _isWhite, int f, int r, Chessboard chessB) {
        boardI = new BoardIndex(f, r);
        isWhite = _isWhite;
        chessb = chessB;
    }

    /**
     * @param _isWhite True if piece is white
     * @param fileR Input File and Rank
     * @param chessB Chessboard
     */
    protected Piece(boolean _isWhite, String fileR, Chessboard chessB) {
        char charFile = fileR.toUpperCase().trim().charAt(0);
        char charRank = fileR.toUpperCase().trim().charAt(1);
        boardI = new BoardIndex(charFile-'A', charRank - '1');
        isWhite = _isWhite;
        chessb = chessB;
    }

    /**
     * @return True if piece is previously moved.
     */
    public boolean isMoved() {
        return isMoved;
    }


    /**
     * @return True if piece is white.
     */
    public boolean isWhite() {
        return isWhite;
    }


    /**
     * @return Position of a piece.
     */
    public BoardIndex getBoardIndex() {
        return boardI;
    }


    /**
     * @param input Position of a piece
     */
    protected void setBoardIndex(BoardIndex input) {
        boardI = input;
    }


    /**
     * @param targetIndex Target position of a move
     * @param promotion Promotion. Pawn only.
     * @return Outcome of the move
     */
    abstract public Outcome doMove(BoardIndex targetIndex, Promotion promotion);


    /**
     * @param targetIndex Target position of a move
     * @return If this piece can attack target.
     */
    abstract public Outcome doAttack(BoardIndex targetIndex);


    /**
     * @return True if piece has legal move
     */
    abstract public boolean hasLegalMove();

    /**
     * @return True if piece has legal move
     */
    abstract public SourceAndTarget getOneLegalMove();

    /**
     * @param targetI Target position of a move
     * @param rollback If true, rollback after checking move. Used for checking checkmate and stalemate.
     * @return True if piece can move. False if move is illegal as King is in check after move.
     */
    protected boolean doActualMove(BoardIndex targetI, boolean rollback) {
        Map<String, Piece> pieceMap = chessb.getPieceMap();
        BoardIndex sourceIndex = getBoardIndex();
        Piece target = pieceMap.get(targetI.getKey());
        pieceMap.remove(getKey());
        setBoardIndex(targetI);
        pieceMap.put(getKey(), this);
        Piece king =  isWhite() ? chessb.getWhiteKing() : chessb.getBlackKing();
        boolean isKingUnderAttack = chessb.isUnderAttack(isWhite(), king.getBoardIndex());
        if (isKingUnderAttack || rollback) {
            pieceMap.remove(getKey());
            setBoardIndex(sourceIndex);
            pieceMap.put(getKey(), this);
            if (target!=null) {
                pieceMap.put(target.getKey(), target);
            }
        }
        else {
            LastMoveForRollback lastMove = chessb.getLastMoveForRollback();
            lastMove.doInit(false);
            if (this instanceof Pawn) {
                int dFile = targetI.fileI - sourceIndex.fileI;
                int dRank = targetI.rankI - sourceIndex.rankI;
                if ((dFile == 0) && Math.abs(dRank) == 2) {
                    lastMove.lastTwoStepPawnMove = this;
                }
                else {
                    lastMove.lastTwoStepPawnMove = null;
                }
            }
            else {
                lastMove.lastTwoStepPawnMove = null;
            }
            lastMove.isRegularMove = true;
            lastMove.sourceI = new BoardIndex(sourceIndex);
            lastMove.targetI = new BoardIndex(targetI);
            lastMove.isMove = isMoved;
            lastMove.removedReg = target;
            if (!isMoved) {
                isMoved = true;
            }
        }
        return !isKingUnderAttack;
    }

    /**
     * @param targetIndex Target position of a move
     * @param rookSourceIndex Current position of rook
     * @param rookTargetIndex Target position of rook
     * @return True if King and Rook can castle. Always return true as King will not be under attack when castling begins.
     */
    protected boolean doActualMoveCastling(BoardIndex targetIndex, BoardIndex rookSourceIndex, BoardIndex rookTargetIndex) {
        Map<String, Piece> pieceMap = chessb.getPieceMap();
        BoardIndex sourceIndex = getBoardIndex();
        pieceMap.remove(getKey());
        setBoardIndex(targetIndex);
        pieceMap.put(getKey(), this);
        Piece rook = pieceMap.remove(rookSourceIndex.getKey());
        rook.setBoardIndex(rookTargetIndex);
        pieceMap.put(rook.getKey(), rook);
        {	LastMoveForRollback lastMove = chessb.getLastMoveForRollback();
            lastMove.doInit(false);
            lastMove.isCastling = true;
            lastMove.sourceI = new BoardIndex(sourceIndex);
            lastMove.targetI = new BoardIndex(targetIndex);
            lastMove.sourceIndexR = new BoardIndex(rookSourceIndex);
            lastMove.targetIndexR = new BoardIndex(rookTargetIndex);
        }
        return true;
    }

    /**
     * @param targetIndex Target position of a move
     * @param enPassantIndex Index of pawn to be removed after en passant.
     * @param rollback True if rollback is needed
     * @return True if pawn can en passant
     */
    protected boolean doActualMoveIsEnPassant(BoardIndex targetIndex, BoardIndex enPassantIndex, boolean rollback) {
        Map<String, Piece> pieceMap = chessb.getPieceMap();
        BoardIndex sourceIndex = getBoardIndex();
        pieceMap.remove(getKey());
        setBoardIndex(targetIndex);
        pieceMap.put(getKey(), this);
        Piece killedPaw = pieceMap.get(enPassantIndex.getKey());
        pieceMap.remove(enPassantIndex.getKey());
        Piece king = isWhite() ? chessb.getWhiteKing() : chessb.getBlackKing();
        boolean isKingUnderAttack = chessb.isUnderAttack(isWhite(), king.getBoardIndex());
        if (isKingUnderAttack || rollback) {
            pieceMap.remove(getKey());
            setBoardIndex(sourceIndex);
            pieceMap.put(getKey(), this);
            pieceMap.put(killedPaw.getKey(), killedPaw);
        }
        else {
            LastMoveForRollback lastMove = chessb.getLastMoveForRollback();
            lastMove.doInit(false);
            lastMove.isEnPassant = true;
            lastMove.sourceI = new BoardIndex(sourceIndex);
            lastMove.targetI = new BoardIndex(targetIndex);
            lastMove.isMove	= isMoved;
            lastMove.removedEnPass 	= killedPaw;
            if (!isMoved) {
                isMoved = true;
            }
        }
        return !isKingUnderAttack;
    }

    /**
     * @param targetI Target position of a move
     * @param promotion Promotion
     * @param rollB True if rollback is needed
     * @return True if promotion is successful
     */
    protected boolean doActualMoveIsPromotion(BoardIndex targetI, Promotion promotion, boolean rollB) {
        Map<String, Piece> pieceMap = chessb.getPieceMap();
        BoardIndex sourceIndex = getBoardIndex();
        Piece target = pieceMap.get(targetI.getKey());
        pieceMap.remove(getKey());
        Piece promPiece;
        if (promotion == Promotion.Bishop) {
            promPiece = new Bishop(isWhite(), targetI.fileI, targetI.rankI, chessb);
        }
        else if (promotion == Promotion.Rook) {
            promPiece = new Rook(isWhite(), targetI.fileI, targetI.rankI, chessb);
        }
        else if (promotion == Promotion.Knight) {
            promPiece = new Knight(isWhite(), targetI.fileI, targetI.rankI, chessb);
        }
        else {
            promPiece = new Queen(isWhite(), targetI.fileI, targetI.rankI, chessb);
        }
        Piece king = isWhite() ? chessb.getWhiteKing() : chessb.getBlackKing();
        boolean isKingUnderAttack = chessb.isUnderAttack(isWhite(), king.getBoardIndex());
        if (isKingUnderAttack || rollB) {
            pieceMap.put(getKey(), this);
            if (target!=null) {
                pieceMap.put(target.getKey(), target);
            }
            else {
                pieceMap.remove(targetI.getKey());
            }
        }
        else {
            LastMoveForRollback lastMove = chessb.getLastMoveForRollback();
            lastMove.doInit(false);
            lastMove.isPromotion = true;
            lastMove.sourceI = new BoardIndex(sourceIndex);
            lastMove.targetI = new BoardIndex(targetI);
            lastMove.removedReg = target;
            lastMove.removedisProm = this;
            lastMove.promotionTo = promPiece.toString();

            if (!isMoved) {
                isMoved = true;
            }
        }
        return !isKingUnderAttack;
    }




    public static boolean doRollback(Chessboard chessBoard) {
        LastMoveForRollback lastMove = chessBoard.getLastMoveForRollback();
        Map<String, Piece> pieceMap = chessBoard.getPieceMap();
        if (lastMove.isRegularMove) {
            Piece target = pieceMap.get(lastMove.targetI.getKey());
            //
            pieceMap.remove(target.getKey());
            target.setBoardIndex(lastMove.sourceI);
            pieceMap.put(target.getKey(), target);
            target.isMoved = lastMove.isMove;
            if (lastMove.removedReg!=null) {
                pieceMap.put(lastMove.removedReg.getKey(), lastMove.removedReg);
            }
            lastMove.doInit(true);
            return true;
        }
        else if (lastMove.isCastling) {
            Piece target 		= pieceMap.get(lastMove.targetI.getKey());
            pieceMap.remove(target.getKey());
            target.setBoardIndex(lastMove.sourceI);
            pieceMap.put(target.getKey(), target);
            Piece targetRook 	= pieceMap.get(lastMove.targetIndexR.getKey());
            pieceMap.remove(targetRook.getKey());
            targetRook.setBoardIndex(lastMove.sourceIndexR);
            pieceMap.put(targetRook.getKey(), targetRook);
            lastMove.doInit(true);
            return true;
        }
        else if (lastMove.isEnPassant) {
            Piece target = pieceMap.get(lastMove.targetI.getKey());
            pieceMap.remove(target.getKey());
            target.setBoardIndex(lastMove.sourceI);
            pieceMap.put(target.getKey(), target);
            target.isMoved = lastMove.isMove;
            pieceMap.put(lastMove.removedEnPass.getKey(), lastMove.removedEnPass);
            lastMove.doInit(true);
            return true;
        }
        else if (lastMove.isPromotion) {
            Piece target = pieceMap.get(lastMove.targetI.getKey());
            pieceMap.remove(target.getKey());
            pieceMap.put(lastMove.removedisProm.getKey(), lastMove.removedisProm);
            if (lastMove.removedReg!=null) {
                pieceMap.put(lastMove.removedReg.getKey(), lastMove.removedReg);
            }
            lastMove.doInit(true);
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * @return Board output for white and black pieces.
     */
    @Override
    public String toString() {
        return isWhite ? "W" : "B";
    }

    /**
     * @return Key for the map of pieces.
     */
    public String getKey() {
        return "" + boardI.fileI + "-" + boardI.rankI;
    }

    /**
     * @param targetIndex Target position of a move
     * @return True if target is empty or an opponent piece
     */
    public boolean isTargetEmptyOrLegal(BoardIndex targetIndex) {
        Piece targetPiece = chessb.getPieceMap().get(targetIndex.getKey());
        return (targetPiece == null) || (isWhite()==!targetPiece.isWhite());
    }

    /**
     * @param targetIndex Target position of a move
     * @return True if target is empty
     */
    public boolean isTargetEmpty(BoardIndex targetIndex) {
        Piece targetPiece = chessb.getPieceMap().get(targetIndex.getKey());
        return targetPiece == null;
    }

    /**
     * @param targetIndex Target position of a move
     * @return True if target is an opponent piece
     */
    public boolean isTargetLegal(BoardIndex targetIndex) {
        Piece targetPiece = chessb.getPieceMap().get(targetIndex.getKey());
        return (targetPiece != null) && (isWhite()==!targetPiece.isWhite());
    }

    /**
     * @param targetIndex Target position of a move
     * @return True if en passant target is present
     */
    public boolean isTargetLegalEnPassant(BoardIndex targetIndex) {
        LastMoveForRollback lastMove = chessb.getLastMoveForRollback();
        Piece targetPiece = chessb.getPieceMap().get(targetIndex.getKey());
        Piece LastTwoStepMovePaw = lastMove.getLastTwoStepMovePawn();
        return (targetPiece != null) && targetPiece==LastTwoStepMovePaw;
    }

    /**
     * @param dd Direction and Distance
     * @return True if no pieces are between this piece and target
     */
    protected boolean isNoneInBetween(DirectionDistance dd) {
        Map<String, Piece> pieceMap = chessb.getPieceMap();
        boolean out = true;
        for (int i = 1; i < dd.getDistance(); i++) {
            int deltaFile = dd.getDeltaFile();
            int deltaRank = dd.getDeltaRank();
            BoardIndex oneIndex = new BoardIndex(boardI.fileI + i * deltaFile, boardI.rankI + i * deltaRank);
            Piece onePiece = pieceMap.get(oneIndex.getKey());
            if (onePiece!=null) {
                out = false;
                break;
            }
        }
        return out;
    }

    /**
     * @param deltaFile Change in file for each step
     * @param deltaRank Change in rank for each step
     * @return True if there is at least 1 legal move along 1 direction
     */
    protected SourceAndTarget hasLegalMoveOneDirection(int deltaFile, int deltaRank) {
        BoardIndex sourceIndex = getBoardIndex();
        Map<String, Piece> pieceMap = chessb.getPieceMap();
        SourceAndTarget legalMove = null;
        for (int file = sourceIndex.fileI + deltaFile, rank = sourceIndex.rankI + deltaRank; (file >= 0 && file < 8 && rank >= 0 && rank < 8); file = file + deltaFile, rank = rank + deltaRank) {
            BoardIndex targetIndex = new BoardIndex(file, rank);
            boolean isLegalMove;
            Piece targetPiece = pieceMap.get(targetIndex.getKey());
            if (targetPiece != null && targetPiece.isWhite() == isWhite()) {			//piece same color
                break;
            }
            else if ((targetPiece != null && targetPiece.isWhite() != isWhite())) {		//piece different color
                isLegalMove = doActualMove(targetIndex, true);
                if (!isLegalMove) {
                    break;
                }
            }
            else {
                isLegalMove = doActualMove(targetIndex, true);
            }
            if (isLegalMove) {
                legalMove = new SourceAndTarget(sourceIndex, targetIndex);
                break;
            }
        }
        return legalMove;
    }

    /**
     * @param deltaFile Change in file for each step
     * @param deltaRank Change in rank for each step
     * @return True if is legal for 1 step in 1 direction
     */
    protected SourceAndTarget hasLegalMoveOneDirectionOneStep(int deltaFile, int deltaRank) {
        BoardIndex sourceIndex = getBoardIndex();
        Map<String, Piece> pieceMap = chessb.getPieceMap();
        SourceAndTarget legalMove = null;
        for (int file=sourceIndex.fileI+deltaFile, rank=sourceIndex.rankI+deltaRank; (file>=0 && file<8 && rank>=0 && rank<8); file=100, rank=-100) {
            BoardIndex targetIndex = new BoardIndex(file, rank);
            Piece targetPiece = pieceMap.get(targetIndex.getKey());
            if (targetPiece != null && targetPiece.isWhite() == isWhite()) {
                break;
            }
            boolean isLegalMove = doActualMove(targetIndex, true);
            if (isLegalMove) {
                legalMove = new SourceAndTarget(sourceIndex, targetIndex);;
                break;
            }
        }
        return legalMove;
    }
}
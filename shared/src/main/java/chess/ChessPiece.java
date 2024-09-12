package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private ChessGame.TeamColor pieceColor;
    private PieceType pieceType;
    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor; //sets color based on passed in color
        this.pieceType = type; //sets type based on passed in type
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return pieceType;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<>();
        //get the position of piece
        int row = myPosition.getRow();
        int col = myPosition.getColumn();

        //setup functions for each pieces move
        switch (pieceType) {
            case KING:
                addKingMoves(board, row, col, moves);
                break;
            case BISHOP:
                addBishopMoves(board, row, col, moves);
                break;

        }

        return moves;
    }
    // Checks if a position is in the chess board
    private boolean isValidPosition(int row, int col) {
        return row >= 1 && row <= 8 && col >= 1 && col <= 8;
    }
    // Checks if the position is empty or an enemy
    private boolean isEmptyOrEnemy(ChessBoard board, int row, int col) {
        ChessPiece piece = board.getPiece(new ChessPosition(row, col));
        return piece == null || piece.getTeamColor() != this.pieceColor;
    }

    // Checks if the position is occupied by an opponent's piece
    private boolean isOpponent(ChessBoard board, int row, int col) {
        ChessPiece piece = board.getPiece(new ChessPosition(row, col));
        return piece != null && piece.getTeamColor() != this.pieceColor;
    }


    private void addKingMoves(ChessBoard board, int row, int col, Collection<ChessMove> moves) {
        int[] directions = {-1, 0, 1}; // King can move in any direction by 1 square
        for (int dr : directions) {
            for (int dc : directions) {
                if (dr == 0 && dc == 0) continue; // Skip the current position
                int newRow = row + dr;
                int newCol = col + dc;
                if (isValidPosition(newRow, newCol) && isEmptyOrEnemy(board, newRow, newCol)) {
                    moves.add(new ChessMove(new ChessPosition(row, col), new ChessPosition(newRow, newCol), null)); //move from current to new row and column
                }
            }
        }
    }


    private void addBishopMoves(ChessBoard board, int row, int col, Collection<ChessMove> moves) {
        int[] directions = {-1, 1}; // Bishop moves diagonally
        for (int dr : directions) {
            for (int dc : directions) {
                for (int step = 1; ; step++) {
                    int newRow = row + dr * step;
                    int newCol = col + dc * step;
                    if (!isValidPosition(newRow, newCol)) break;
                    if (isEmptyOrEnemy(board, newRow, newCol)) {
                        moves.add(new ChessMove(new ChessPosition(row, col), new ChessPosition(newRow, newCol), null));
                        if (board.getPiece(new ChessPosition(newRow, newCol)) != null) break; // Stop if there's a piece in the way
                    } else {
                        break;
                    }
                }
            }
        }
    }









    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ChessPiece that = (ChessPiece) obj;
        return pieceColor == that.pieceColor && pieceType == that.pieceType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, pieceType);
    }

}

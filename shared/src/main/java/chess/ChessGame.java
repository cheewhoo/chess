package chess;

import java.util.*;


/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private TeamColor currentTeam;
    private ChessBoard board;

    public ChessGame() {
        this.currentTeam = TeamColor.WHITE;
        this.board = new ChessBoard();
        board.resetBoard();

    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return this.currentTeam;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.currentTeam = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */

    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece selectedPiece = board.getPiece(startPosition);
        if (selectedPiece == null) {
            return Collections.emptyList();
        }
        Set<ChessMove> otherMoves = new HashSet<>(selectedPiece.pieceMoves(board, startPosition));
        trashMoves(otherMoves);
        return otherMoves;
    }

    private void trashMoves(Set<ChessMove> moves) {
        moves.removeIf(move -> {
            ChessPiece originalStart = board.getPiece(move.getStartPosition());
            ChessPiece originalEnd = board.getPiece(move.getEndPosition());
            board.addPiece(move.getEndPosition(), originalStart);
            board.addPiece(move.getStartPosition(), null);
            boolean checkaftermoved = isInCheck(originalStart.getTeamColor());
            board.addPiece(move.getStartPosition(), originalStart);
            board.addPiece(move.getEndPosition(), originalEnd);
            return checkaftermoved;
        });
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPiece piece = board.getPiece(move.getStartPosition());
        if (piece == null) {
            throw new InvalidMoveException("No piece at the starting position");
        }
        if (piece.getTeamColor() != currentTeam) {
            throw new InvalidMoveException("That's an enemy");
        }
        Set<ChessMove> validMoves = new HashSet<>(validMoves(move.getStartPosition()));
        if (validMoves.contains(move)) {
            ChessPiece target = board.getPiece(move.getEndPosition());
            if (target != null && target.getTeamColor() != piece.getTeamColor()) {
                board.addPiece(move.getEndPosition(), null);
            }
            ChessPiece.PieceType promotionPiece = move.getPromotionPiece();
            if (promotionPiece == null) {
                board.addPiece(move.getEndPosition(), piece);
            } else {
                board.addPiece(move.getEndPosition(), new ChessPiece(currentTeam, promotionPiece));
            }
            board.addPiece(move.getStartPosition(), null);
            currentTeam = currentTeam == TeamColor.WHITE ? TeamColor.BLACK : TeamColor.WHITE;
        } else {
            throw new InvalidMoveException("Bad Move");
        }
    }


    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingPosition = locateKing(teamColor);
        return isBeingAttack(kingPosition, (teamColor == TeamColor.WHITE) ? TeamColor.BLACK : TeamColor.WHITE);
    }

    private ChessPosition locateKing(TeamColor teamColor) {
        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {
                ChessPosition position = new ChessPosition(row, col);
                ChessPiece piece = board.getPiece(position);
                if (piece != null && piece.getPieceType() == ChessPiece.PieceType.KING && piece.getTeamColor() == teamColor) {
                    return position;
                }
            }
        }
        return null; //this will break the game if no kings are there(aka bugged)
    }

    private boolean isBeingAttack(ChessPosition kingPosition, TeamColor attacker) {
        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {
                ChessPosition pos = new ChessPosition(row, col);
                ChessPiece piece = board.getPiece(pos);
                if (isBeingAttackedBoardLoop(piece, kingPosition, attacker, pos)){
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isBeingAttackedBoardLoop(ChessPiece piece, ChessPosition kingPosition, TeamColor attacker, ChessPosition pos) {
        if (piece != null && piece.getTeamColor() == attacker) {
            Collection<ChessMove> moves = piece.pieceMoves(board, pos);
            for (ChessMove move : moves) {
                if (move.getEndPosition().equals(kingPosition)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        if (!isInCheck(teamColor)) {
            System.out.println("Not in check.");
            return false;
        }
        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {
                ChessPosition position = new ChessPosition(row, col);
                Collection<ChessMove> moves = validMoves(position);
                ChessPiece piece = board.getPiece(position);
                if (moves != null && !moves.isEmpty() && piece != null && piece.getTeamColor() == teamColor) {
                    for (ChessMove move : moves) {
                        board.addPiece(move.getEndPosition(), piece);
                        board.addPiece(position, null);
                        if (!isInCheck(teamColor)) {
                            board.addPiece(position, piece);
                            board.addPiece(move.getEndPosition(), null);
                            return false;
                        }
                        board.addPiece(position, piece);
                        board.addPiece(move.getEndPosition(), null);
                    }
                }
            }
        }
        return true;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */

    public boolean isInStalemate(TeamColor teamColor) {
        if(isInCheck(teamColor)) {
            return false;
        }
        for(int row = 1; row <= 8; row++) {
            for(int col = 1; col <= 8; col++) {
                ChessPosition currentSpot = new ChessPosition(row, col);
                if(board.getPiece(currentSpot) != null && board.getPiece(currentSpot).getTeamColor() == teamColor) {
                    HashSet<ChessMove> currentMoves = new HashSet<>(validMoves(currentSpot));
                    if(!currentMoves.isEmpty()) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }
}

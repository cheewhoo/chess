package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

public class ChessBoardRenderer {
    public static void drawBoard(ChessBoard board, boolean whiteAtBottom) {
        String dark = " ## ";
        String light = " || ";
        int startRow = whiteAtBottom ? 1 : 8;
        int endRow = whiteAtBottom ? 8 : 1;
        int rowIncrement = whiteAtBottom ? 1 : -1;

        for (int row = startRow; whiteAtBottom ? row <= endRow : row >= endRow; row += rowIncrement) {
            for (int col = 1; col <= 8; col++) {
                ChessPosition position = new ChessPosition(row, col);
                ChessPiece piece = board.getPiece(position);

                if (piece == null) {
                    System.out.print((row + col) % 2 == 0 ? dark : light);
                } else {
                    String pieceSymbol = getPieceSymbol(piece);
                    System.out.print(pieceSymbol + ((row + col) % 2 == 0 ? dark : light));
                }
            }
            System.out.println();
        }
    }

    private static String getPieceSymbol(ChessPiece piece) {
        switch (piece.getPieceType()) {
            case KING:
                return piece.getTeamColor() == ChessGame.TeamColor.WHITE ? "♔" : "♚";
            case QUEEN:
                return piece.getTeamColor() == ChessGame.TeamColor.WHITE ? "♕" : "♛";
            case BISHOP:
                return piece.getTeamColor() == ChessGame.TeamColor.WHITE ? "♗" : "♝";
            case KNIGHT:
                return piece.getTeamColor() == ChessGame.TeamColor.WHITE ? "♘" : "♞";
            case ROOK:
                return piece.getTeamColor() == ChessGame.TeamColor.WHITE ? "♖" : "♜";
            case PAWN:
                return piece.getTeamColor() == ChessGame.TeamColor.WHITE ? "♙" : "♟";
            default:
                return "";
        }
    }
}

package ui;
import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import static ui.EscapeSequences.*;

public class ChessBoardRenderer {
    public static void drawBoard(ChessBoard board, boolean whiteAtBottom) {
        int startRow = whiteAtBottom ? 1 : 8;
        int endRow = whiteAtBottom ? 8 : 1;
        int rowIncrement = whiteAtBottom ? 1 : -1;
        System.out.println(ERASE_SCREEN);
        System.out.print(" ");
        for (char col = 'a'; col <= 'h'; col++) {
            System.out.print("  " + col + " ");
        }
        System.out.println();

        for (int row = startRow; whiteAtBottom ? row <= endRow : row >= endRow; row += rowIncrement) {
            System.out.print((whiteAtBottom ? 8 - (row - 1) : row) + " ");
            for (int col = 1; col <= 8; col++) {
                ChessPosition position = new ChessPosition(row, col);
                ChessPiece piece = board.getPiece(position);
                String squareColor = (row + col) % 2 == 0
                        ? (whiteAtBottom ? SET_BG_COLOR_LIGHT_GREY : SET_BG_COLOR_DARK_GREY)
                        : (whiteAtBottom ? SET_BG_COLOR_DARK_GREY : SET_BG_COLOR_LIGHT_GREY);
                String pieceSymbol = piece == null ? EMPTY : getPieceSymbol(piece);
                System.out.print(squareColor + pieceSymbol + RESET_BG_COLOR + " ");
            }

            System.out.println(" " + (whiteAtBottom ? 8 - (row - 1) : row));
        }

        System.out.print(" ");
        for (char col = 'a'; col <= 'h'; col++) {
            System.out.print("  " + col + " ");
        }
        System.out.println();
        System.out.print(RESET_BG_COLOR + RESET_TEXT_COLOR);
    }




    private static String getPieceSymbol(ChessPiece piece) {
        return switch (piece.getPieceType()) {
            case KING -> piece.getTeamColor() == ChessGame.TeamColor.WHITE ? WHITE_KING : BLACK_KING;
            case QUEEN -> piece.getTeamColor() == ChessGame.TeamColor.WHITE ? WHITE_QUEEN : BLACK_QUEEN;
            case BISHOP -> piece.getTeamColor() == ChessGame.TeamColor.WHITE ? WHITE_BISHOP : BLACK_BISHOP;
            case KNIGHT -> piece.getTeamColor() == ChessGame.TeamColor.WHITE ? WHITE_KNIGHT : BLACK_KNIGHT;
            case ROOK -> piece.getTeamColor() == ChessGame.TeamColor.WHITE ? WHITE_ROOK : BLACK_ROOK;
            case PAWN -> piece.getTeamColor() == ChessGame.TeamColor.WHITE ? WHITE_PAWN : BLACK_PAWN;
        };
    }
}


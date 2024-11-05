package ui;

public class ChessBoardRenderer {
    public static void drawBoard() {
        String[] pieces = { "♜", "♞", "♝", "♛", "♚", "♝", "♞", "♜" };
        String[] pawns = { "♟", "♙" };
        String dark = "##";
        String light = "  ";

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (row == 1) System.out.print(pawns[0] + (col % 2 == 0 ? dark : light));
                else if (row == 6) System.out.print(pawns[1] + (col % 2 == 0 ? dark : light));
                else if (row == 0 || row == 7) System.out.print(pieces[col] + (col % 2 == 0 ? dark : light));
                else System.out.print((col % 2 == 0 ? dark : light));
            }
            System.out.println();
        }
    }
}

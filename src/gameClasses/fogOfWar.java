package gameClasses;

import gameClasses.pieces.piece;

// Works out which squares a player is allowed to see in Fog of War mode.
// A player can see:
//     The squares their own pieces are on.
//     Every square one of their pieces could move to (from each piece's getVisibleTiles).
// Every other square is hidden from them, including any enemy pieces on it.
public class fogOfWar {

    // Returns a grid the same size as the board where true means the square is visible.
    // The grid uses 0-based indexes like the board array, so position {row, col} is at [row - 1][col - 1].
    public static boolean[][] getVisibleSquares(board gameBoard, boolean forWhite) {
        piece[][] boardState = gameBoard.getBoardState();
        int size = boardState.length;
        boolean[][] visibleSquares = new boolean[size][size];

        // Looking at the board itself rather than the player's list of pieces,
        // since captured pieces are not always removed from that list.
        for (int rowIndex = 0; rowIndex < size; rowIndex++) {
            for (int colIndex = 0; colIndex < size; colIndex++) {
                piece pieceChecking = boardState[rowIndex][colIndex];

                // Skipping empty squares and the opponent's pieces.
                if (pieceChecking == null || pieceChecking.pieceIsWhite() != forWhite) {
                    continue;
                }

                // A player can always see the squares their own pieces are on.
                visibleSquares[rowIndex][colIndex] = true;

                // A player can see every square their piece could move to.
                for (int[] tile : pieceChecking.getVisibleTiles(gameBoard)) {
                    visibleSquares[tile[0] - 1][tile[1] - 1] = true;
                }
            }
        }

        return visibleSquares;
    }
}

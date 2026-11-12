package ui;

import java.util.ArrayList;
import java.awt.GridLayout;

import javax.swing.JPanel;

import gameClasses.pieces.piece;
import gameClasses.board;
import gameClasses.player;

/**
 * The 8x8 grid of squares. Receives clicks and updates the display.
 * <p>
 * This class does NOT decide whether moves are legal; that belongs to the
 * game logic.
 */
public class BoardPanel extends JPanel {

    /** Number of rows and columns on the board. */
    public static final int SIZE = 8;

    private final SquareButton[][] squares = new SquareButton[SIZE][SIZE];
    private final StatusPanel statusPanel;
    private SquareButton selected; // null when nothing is selected
    private board connectedBoard;
    private ArrayList<int[]> moveableTiles = new ArrayList<int[]>(); // Tiles the player can click to move the selected piece to.
    private player attackingPlayer;
    private player defendingPlayer;

    /**
     * Builds the grid of squares.
     *
     * @param statusPanel the status bar to send messages to when squares are clicked
     */
    public BoardPanel(StatusPanel statusPanel) {
        this.statusPanel = statusPanel;
        setLayout(new GridLayout(SIZE, SIZE));

        // Positions are 1-based to match the game classes: row 1 is White's
        // back rank. GridLayout fills top-to-bottom, so add row 8 first.
        for (int row = SIZE; row >= 1; row--) {
            for (int col = 1; col <= SIZE; col++) {
                SquareButton square = new SquareButton(row, col);
                square.addActionListener(e -> handleSquareClick(square));
                squares[row - 1][col - 1] = square; // convert position to index
                add(square);
            }
        }
    }

    /**
     * Places the passed board state on the board.
     * <p>
     */
    public void setUpStartingPieces(piece[][] boardState) {
        
        int numRows = boardState.length;
        int numCols = boardState[0].length;

        for (int row=0; row<numRows; row++) {
            for (int col=0; col<numCols; col++) {
                piece currentPiece = boardState[row][col];
                SquareButton currentTile = squares[row][col];

                if (currentPiece == null) {
                    continue;
                }

                int cellSize = Math.max(1, Math.min(currentTile.getWidth(), currentTile.getHeight()));
                
                currentTile.setIcon(currentPiece.getPieceIcon(cellSize));
            }
        }
    }

    /**
     * Handles a click on a square.
     * <p>
     * For now: click a square to select it, click it again to deselect, or
     * click a different square to move the selection there.
     * Later: a second click on a different square should attempt a move.
     *
     * @param square the square that was clicked
     */
    private void handleSquareClick(SquareButton square) {
        // Clicking the already-selected square clears the selection.
        if (selected == square) {
            selected.setSelected(false);
            selected = null;
            statusPanel.showMessage("Selection cleared");
            moveableTiles.clear();
            return;
        }

        // This chunk here determines if the player is moving a piece.
        // We use this instead of movableTiles.contains() as it will fail due to the arrays having different address positions.
        int[] squarePosition = square.getPosition();
        boolean movingPiece = false;

        for (int[] position : moveableTiles) {
            if (position[0] == squarePosition[0] && position[1] == squarePosition[1]) {
                movingPiece = true;
            }
        }

        // If a piece has been selected AND the square selected is one it can move to
        if (movingPiece) {
            piece pieceMoving = connectedBoard.getPiece(selected.getPosition());
            int[] newPosition = square.getPosition();

            connectedBoard.movePiece(defendingPlayer, pieceMoving, newPosition);
            moveableTiles.clear();

            square.setIcon(selected.getIcon());
            selected.setIcon(null);

            selected.setSelected(false);
            selected = null;
            statusPanel.showMessage("Moved to " + square.getSquareName());

            return;
        }
        
        // If the user selected an empty square
        if (connectedBoard.getPiece(square.getPosition()) == null) {
            if (selected == null) {return;}

            moveableTiles.clear();

            selected.setSelected(false);
            selected = null;
            statusPanel.showMessage("Selected an empty square");
            return;
        }

        // If the player selected a piece they control
        if (connectedBoard.getPiece(square.getPosition()).pieceIsWhite() == attackingPlayer.controlsWhite()) {

            moveableTiles.clear();

            if (selected != null) {
                selected.setSelected(false);
            }

            selected = square;
            square.setSelected(true);

            piece selectedPiece = connectedBoard.getPiece(selected.getPosition());
            moveableTiles = selectedPiece.getVisibleTiles(getConnectedGameState());

            for (int[] position : moveableTiles) {
                System.out.println(position[0] + ", " + position[1]);
            }

            return;
        }

        selected = square;
        selected.setSelected(true);
        statusPanel.showMessage("Selected " + square.getSquareName());
    }

    /**
     * Returns the square at the given position.
     * Uses the same 1-based positions as the game classes.
     *
     * @param row the row, 1 to 8, where 1 is White's back rank
     * @param col the column, 1 to 8, where 1 is the left side (the a-file)
     * @return the square at that row and column
     */
    public SquareButton getSquare(int row, int col) {
        return squares[row - 1][col - 1];
    }

    /**
     * Overloads the function above to accept an array, e.g. from piece.getPosition().
     *
     * @param position the position as {row, col}
     * @return the square at that position
     */
    public SquareButton getSquare(int[] position) {
        return getSquare(position[0], position[1]);
    }

    public void connectBoard(board gameBoard) {
        connectedBoard = gameBoard;
    }

    public piece[][] getConnectedGameState() {
        return connectedBoard.getBoardState();
    }

    public void setPlayers(player newAttackingPlayer, player newDefendingPlayer) {
        attackingPlayer = newAttackingPlayer;
        defendingPlayer = newDefendingPlayer;
    }
}

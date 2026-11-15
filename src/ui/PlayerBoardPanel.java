package ui;

import java.util.ArrayList;
import java.awt.GridLayout;

import javax.swing.JPanel;

import gameClasses.game;
import gameClasses.player;
import gameClasses.pieces.piece;

// A board drawn from one player's point of view, so their pieces are at the bottom of the screen.
// Works like BoardPanel, but:
//     It only lets its own player move, and only on their turn.
//     It can be flipped for the black player.
//     It moves pieces through the shared game object, so every window stays in sync.
// Flipping only changes the order the squares are drawn in, the board array is never changed.
public class PlayerBoardPanel extends JPanel {
    private SquareButton[][] squares;
    private StatusPanel statusPanel;
    private game currentGame;
    private player owner; // The player using this window.
    private SquareButton selected; // null when nothing is selected
    private ArrayList<int[]> moveableTiles = new ArrayList<int[]>(); // Tiles the selected piece can move to.

    // flipped should be true for the black player, so black's pieces show at the bottom.
    public PlayerBoardPanel(StatusPanel statusPanel, game currentGame, player owner, boolean flipped) {
        this.statusPanel = statusPanel;
        this.currentGame = currentGame;
        this.owner = owner;

        int size = currentGame.getBoard().getBoardState().length;
        squares = new SquareButton[size][size];
        setLayout(new GridLayout(size, size));

        // GridLayout fills top-left to bottom-right, so the order squares are added decides which way the board faces.
        //     Not flipped (white): row 8 at the top, column 1 (a-file) on the left.
        //     Flipped (black): row 1 at the top, column 8 (h-file) on the left.
        // Each square still stores its real row and column, so the rest of the code does not care which way it faces.
        for (int screenRow = 0; screenRow < size; screenRow++) {
            for (int screenCol = 0; screenCol < size; screenCol++) {

                int row;
                int col;

                if (flipped) {
                    row = screenRow + 1;
                    col = size - screenCol;
                }
                else {
                    row = size - screenRow;
                    col = screenCol + 1;
                }

                SquareButton square = new SquareButton(row, col);
                square.addActionListener(e -> handleSquareClick(square));
                squares[row - 1][col - 1] = square; // Converting the position to an index.
                add(square);
            }
        }

        // Every time the game changes, redraw this board.
        currentGame.addChangeListener(this::refreshBoard);
    }

    // Redraws every square from the board and updates the status bar.
    // Empty squares are cleared so pieces that moved or were captured disappear.
    public void refreshBoard() {
        clearSelection();

        piece[][] boardState = currentGame.getBoard().getBoardState();

        for (int rowIndex = 0; rowIndex < boardState.length; rowIndex++) {
            for (int colIndex = 0; colIndex < boardState[0].length; colIndex++) {
                piece currentPiece = boardState[rowIndex][colIndex];
                SquareButton currentTile = squares[rowIndex][colIndex];

                if (currentPiece == null) {
                    currentTile.setIcon(null);
                    continue;
                }

                // If the window is not on screen yet the tile has no size, so fall back on the default size.
                int cellSize = Math.min(currentTile.getWidth(), currentTile.getHeight());
                if (cellSize <= 0) {
                    cellSize = currentTile.getPreferredSize().width;
                }

                currentTile.setIcon(currentPiece.getPieceIcon(cellSize));
            }
        }

        statusPanel.setTurn(currentGame.getTurnName());
        statusPanel.showMessage(currentGame.getStatusMessage());
    }

    // Handles a click on a square.
    //     Click one of your pieces to select it.
    //     Click a tile it can move to in order to move it.
    //     Click it again (or an empty tile) to clear the selection.
    private void handleSquareClick(SquareButton square) {

        if (currentGame.isOver()) {
            statusPanel.showMessage(currentGame.getStatusMessage());
            return;
        }

        // Blocking clicks when it is not this window's player's turn.
        if (currentGame.isPlayersTurn(owner) == false) {
            statusPanel.showMessage("Wait for " + currentGame.getTurnName() + " to move");
            return;
        }

        // Clicking the already selected square clears the selection.
        if (selected == square) {
            clearSelection();
            statusPanel.showMessage("Selection cleared");
            return;
        }

        // If a piece is selected and the clicked square is one it can move to, make the move.
        // The game moves the piece and then tells every window to redraw.
        if (selected != null && isMoveableTile(square.getPosition())) {
            piece pieceMoving = currentGame.getBoard().getPiece(selected.getPosition());
            clearSelection();
            currentGame.makeMove(pieceMoving, square.getPosition());
            return;
        }

        piece clickedPiece = currentGame.getBoard().getPiece(square.getPosition());

        // If the player clicked one of their own pieces, select it.
        if (clickedPiece != null && clickedPiece.pieceIsWhite() == owner.controlsWhite()) {
            clearSelection();

            selected = square;
            square.setSelected(true);
            moveableTiles = clickedPiece.getVisibleTiles(currentGame.getBoard().getBoardState());

            statusPanel.showMessage("Selected " + square.getSquareName());
            return;
        }

        // Otherwise the player clicked an empty tile or an opponent's piece they cannot move to.
        clearSelection();
    }

    // Returns true if the selected piece can move to the passed position.
    // Comparing by element since contains() compares the arrays' addresses.
    private boolean isMoveableTile(int[] position) {
        for (int[] tile : moveableTiles) {
            if (tile[0] == position[0] && tile[1] == position[1]) {
                return true;
            }
        }

        return false;
    }

    // Unhighlights the selected square and forgets its moves.
    private void clearSelection() {
        if (selected != null) {
            selected.setSelected(false);
            selected = null;
        }

        moveableTiles.clear();
    }
}

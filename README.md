# CS 67 Final Project

A game built in Java by Christian, Ben, and Elliott.

## Team

- Christian
- Ben 
- Elliott

## Potential Changes

- Fog
- Mana / gold ability system
- Custom board size
- Random effects every few turns

## Tools used 

- javax.swing

## TODO for next class

1. Handle Pawn and Castling mechanisms (Christian)
2. Implement Turns System (Ben)
3. Look Into Varying of modes and building out from base game (Elliott)

# Things that need implemented (Christian notes)

1. Pawns need to be actually implemented
2. The king and rook need to be modified to accurately account for castling.

# Ben's change notes

## Turns System

After clicking "Play Classic" a small box asks for **Two Player** or **vs Computer**.

- Two Player: two windows open side by side. Each person sees their own pieces at the bottom and can only move on their turn.
- vs Computer: one window for White. Black is moved automatically.

The board array is never flipped or rearranged. The black window just draws the squares in the opposite order.

### Files added

- `src/gameClasses/game.java` - Keeps track of whose turn it is. Holds the one shared board and both players, moves pieces, switches turns, shows check / checkmate / king captured messages, and tells every window to redraw after a move.
- `src/gameClasses/computerOpponent.java` - The automated opponent. Captures a piece if it can, otherwise makes a random move.
- `src/ui/PlayerBoardPanel.java` - The board one player sees. Can be flipped for Black, only lets its own player move on their turn, and redraws itself after every move.
- `src/ui/GameLauncher.java` - Asks which mode to play and opens the right windows (two side by side, or one plus the computer).

### Files changed

- `src/Main.java` - Now calls `GameLauncher.startGame(...)` instead of opening a `ChessFrame`. The board and piece setup is the same.

### Not changed

- `BoardPanel.java` and `ChessFrame.java` were left alone. Main no longer opens them, but they still work if needed.
- `board.java`, `player.java`, the pieces, and the start menu were not touched.

## Bens Change Notes
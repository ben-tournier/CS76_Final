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

1. Setup the game start board state (Thurs?) Start on stronger computer versus gamemode. very general setup is already made in computerOpponent. java (Thurs) (Christian)

The current computer mode is very basic and essentially: 
    1. Figures out all moves and seperates them into capture moves and non capture moves
    2. Chooses a capture move at random, if none exsist it chooses a non capture move at random

2. clean up UI, potentially setup start to effects section (death square, board rotation, etc.), look into and maybe add options to select playing as black / white / random  (Ben)

3. Get a start on fog of war gamemode (Elliott)

# Things that need implemented (Christian notes)

1. Pawns need to be actually implemented
2. The king and rook need to be modified to accurately account for castling.

# Potential Special effects options

1. Death Square - one square is deemed the death square at random from the start, any piece to land on it is captured instantly.(Cannot be a spot that is a startint position for white/black)
2. Dice Chess - roll 'dice at start of turn, result determines what piece you are allowed to move
3. Wind gust - every couple of turns randomly push all pawns foreward, backward, right, or left if the square is open. 
4. explosive pieces - when a piece (specific or general) is captured it takes all pieces in an x radius around it as well
5. Ghost piece - choose, random, or all pieces are now able to move through all other pieces
6. pacifict - choose or random piece that can no longer capture or be captured, purely used as a moveable blocker
7. vampire - whenever random or chosen piece takes another piece it becomes that piece. 
8. necromancer - when the chosen or random piece captures another piece it creates a pawn in the square it just left
9. berserker - after chosen or random piece captures enemy it must continue valid moves so long as they immediately capture, multiple moves per turn.
10. Sniper - If chosen/random piece could capture an opposing piece they may use the turn to 'snipe' and capture that piece without moving. 
11. Armored - piece requires two hits to destroy, first capture sends it back to starting position or somewhere close if occupied
12. bodyguard - a piece adjacent to the chosen bodyguard  cannot be captured until the bodyguard has been captured. 
13. slippery - when the chosen/random piece is attmepted to be captured it instead moves one square horizontally or vertically if it is open
14. martyr - if the king is in check you can sacrifice the martyr instead to move the king to its position.
15. knight blood - in addition to normal moves the piece can move like a knight
16. portal pair - two determined pieces can swap places with each other instead of taking turn. 
17. magnet - end of turn all pieces are brought one square closer to this piece if the board is open. 
18. mimic - on turn it moves like the last piece you moved as well as its own moves 
19. time anchor - mark the anchors starting square. you may replace a turn to move the piece back to that square
20. Drafting? players take turns selecting from 6 random mutations and choose what pieces to put them on. the other player does not know about what mutations each piece has but does know what the mutations the opponent has are. 
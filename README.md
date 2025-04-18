# Chess Game

A fully-featured chess game with a clean MVC architecture, comprehensive test coverage,
and proper encapsulation.

## Overview

This project is a refactoring of a legacy chess codebase. 
The original monolithic implementation has been restructured following proper
Model-View-Controller (MVC) architecture with added unit tests for all chess
piece movements and special rules.

## Features

- Game Modes: Player vs Player, Player vs Computer
- Time Controls: Configurable chess clock with hours, minutes, and seconds
- Complete Chess Rules:
    - Standard piece movements
    - Castling (kingside and queenside)
    - En passant captures
    - Pawn promotion
    - Check, checkmate, and stalemate detection
- Visual Interface: Clear indicators for selected pieces and legal moves
- Game Management: New game, surrender, and game over states

## How to Play

1. Run the application by executing the main class.
2. From the start menu, select your preferred game mode and time settings.
3. Click "Start Game" to begin.

To move a piece:
1. Click on the piece you want to move. The square will be highlighted and legal
destination squares will display a green shadow.
2. Click on a highlighted square to move the piece to that position.

## Technical Implementation

### MVC Architecture

The refactoring established a clear separation of concerns following the MVC pattern:

- Model: Core chess logic and game state
    - Encapsulated all chess rules and board management
    - Created proper class hierarchy for pieces with inheritance
    - Implemented Position class instead of raw x/y coordinates
    - Used enums for piece colors and game states

- View: User interface components
    - Separated board rendering from game logic
    - Created dedicated UI classes for different screens
    - Implemented consistent UI design patterns

- Controller: Game coordination and user input handling
    - Established mediator between Model and View
    - Implemented Observer pattern for state changes
    - Centralized move validation and execution

### Key Refactoring Improvements

1. Decoupling and Encapsulation:
    - Extracted piece movement logic into dedicated piece classes
    - Separated board representation from rendering
    - Created Position class to encapsulate board coordinates
    - Used PieceColor enum instead of string literals or integers
    - Implemented proper access modifiers to enforce encapsulation

2. Code Readability:
    - Split large methods into smaller, focused methods with clear purposes
    - Added appropriate documentation and comments
    - Used consistent naming conventions
    - Removed code duplication

3. Observer Pattern Implementation:
    - Controller observes changes in board state
    - UI components update in response to model changes
    - Clear separation between state management and rendering

4. Test-Driven Development:
    - Comprehensive unit tests for all piece movement rules
    - Test cases for special moves (castling, en passant, promotion)
    - Tests for check, checkmate, and stalemate conditions
    - Testing edge cases and boundary conditions

### Code Structure

The codebase is organized into packages following the MVC pattern:

```
src/
├── Controller/
│   ├── ChessController.java
│   └── CheckmateDetector.java
├── Model/
│   ├── piece/
│   │   ├── Bishop.java
│   │   ├── King.java
│   │   ├── Knight.java
│   │   ├── Pawn.java
│   │   ├── Queen.java
│   │   └── Rook.java
│   ├── Board.java
│   ├── GameState.java
│   ├── Move.java
│   ├── Piece.java
│   ├── PieceColor.java
│   ├── Clock.java
│   └── Position.java
└── View/
    ├── ChessBoardUI.java
    ├── GameWindow.java
    ├── SquareView.java
    └── StartMenu.java
```

### Unit Testing

A comprehensive test suite covers:
- All possible valid moves for each piece type
- Special moves like castling and en passant
- Edge cases in movement rules
- Game state transitions
- Check and checkmate detection

## Running the Application

The game can be started by running the main class. Movement of chess pieces
is done by clicking on a piece and then clicking on a destination square that
has a green shadow indicating a legal move.
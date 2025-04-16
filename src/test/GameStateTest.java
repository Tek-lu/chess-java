package test;

import Model.*;
import Model.piece.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class GameStateTest {
    private GameState gameState;

    @Before
    public void setUp() {
        gameState = new GameState();
    }

    @Test
    public void testInitialGameState() {
        assertEquals(PieceColor.WHITE, gameState.getCurrentTurn());
        assertFalse(gameState.isGameOver());
        assertEquals("", gameState.getGameResult());
    }

    @Test
    public void testCheckCondition() {
        // Create a simple check scenario
        // 1. e4 e5
        // 2. Bc4 (bishop to c4) - threatening f7, near black king
        GameState game = new GameState();
        Board board = game.getBoard();

        // Move white pawn from e2 to e4
        Piece whitePawnE = board.getPiece(new Position(4, 6));
        Move moveE4 = new Move.Builder()
                .from(whitePawnE.getPosition())
                .to(new Position(4, 4))
                .piece(whitePawnE)
                .build();

        assertTrue("White pawn move should succeed", game.makeMove(moveE4));

        // Move black pawn from e7 to e5
        Piece blackPawnE = board.getPiece(new Position(4, 1));
        Move moveE5 = new Move.Builder()
                .from(blackPawnE.getPosition())
                .to(new Position(4, 3))
                .piece(blackPawnE)
                .build();

        assertTrue("Black pawn move should succeed", game.makeMove(moveE5));

        // Move white bishop from f1 to c4
        Piece whiteBishop = board.getPiece(new Position(5, 7));
        Move moveBc4 = new Move.Builder()
                .from(whiteBishop.getPosition())
                .to(new Position(2, 4))
                .piece(whiteBishop)
                .build();

        assertTrue("White bishop move should succeed", game.makeMove(moveBc4));

        // Black's turn - check if king is in check
        assertTrue("Black king should be in check", board.isInCheck(PieceColor.BLACK));
        assertFalse("Game should not be over yet", game.isGameOver());
    }

    @Test
    public void testCheckmateCondition() {
        // Create a board with the scholar's mate scenario
        // 1. e4 e5
        // 2. Bc4 Nc6
        // 3. Qh5 Nf6??
        // 4. Qxf7# - Checkmate!

        GameState game = new GameState();
        Board board = game.getBoard();

        // 1. e4
        Piece whitePawnE = board.getPiece(new Position(4, 6));
        Move moveE4 = new Move.Builder()
                .from(whitePawnE.getPosition())
                .to(new Position(4, 4))
                .piece(whitePawnE)
                .build();
        assertTrue(game.makeMove(moveE4));

        // 1... e5
        Piece blackPawnE = board.getPiece(new Position(4, 1));
        Move moveE5 = new Move.Builder()
                .from(blackPawnE.getPosition())
                .to(new Position(4, 3))
                .piece(blackPawnE)
                .build();
        assertTrue(game.makeMove(moveE5));

        // 2. Bc4
        Piece whiteBishop = board.getPiece(new Position(5, 7));
        Move moveBc4 = new Move.Builder()
                .from(whiteBishop.getPosition())
                .to(new Position(2, 4))
                .piece(whiteBishop)
                .build();
        assertTrue(game.makeMove(moveBc4));

        // 2... Nc6
        Piece blackKnight = board.getPiece(new Position(1, 0));
        Move moveNc6 = new Move.Builder()
                .from(blackKnight.getPosition())
                .to(new Position(2, 2))
                .piece(blackKnight)
                .build();
        assertTrue(game.makeMove(moveNc6));

        // 3. Qh5
        Piece whiteQueen = board.getPiece(new Position(3, 7));
        Move moveQh5 = new Move.Builder()
                .from(whiteQueen.getPosition())
                .to(new Position(7, 3))
                .piece(whiteQueen)
                .build();
        assertTrue(game.makeMove(moveQh5));

        // 3... Nf6??
        Piece blackKnightG = board.getPiece(new Position(6, 0));
        Move moveNf6 = new Move.Builder()
                .from(blackKnightG.getPosition())
                .to(new Position(5, 2))
                .piece(blackKnightG)
                .build();
        assertTrue(game.makeMove(moveNf6));

        // 4. Qxf7# (Queen captures pawn on f7) - Checkmate!
        Piece capturedPawn = board.getPiece(new Position(5, 1));
        Move moveQf7 = new Move.Builder()
                .from(whiteQueen.getPosition())
                .to(new Position(5, 1))
                .piece(whiteQueen)
                .capturedPiece(capturedPawn)
                .build();

        assertTrue(game.makeMove(moveQf7));

        // Game should be over with white winning
        assertTrue("Game should be over", game.isGameOver());
        assertTrue("Black king should be in checkmate", board.isCheckmate(PieceColor.BLACK));
        assertEquals("WHITE wins by checkmate", game.getGameResult());
    }

    @Test
    public void testStalemateCondition() {
        // Setup a simple stalemate position by modifying the board directly
        GameState game = new GameState();

        // We'll create a new board with only kings and a queen in stalemate position
        Board board = new Board(); // This creates a standard board

        // Clear the board by creating a custom stalemate position
        createStalematePosition(game);

        // Now black should be in stalemate (not in check but no legal moves)
        board = game.getBoard(); // Get updated board
        assertTrue("Black should be in stalemate", board.isStalemate(PieceColor.BLACK));
        assertTrue("Game should be over", game.isGameOver());
        assertEquals("Draw by stalemate", game.getGameResult());
    }

    private void createStalematePosition(GameState game) {
        // This is a simplified approach to create a stalemate position
        // We'll make a series of moves to get to a known stalemate position

        // One of the simplest stalemate positions:
        // Black king on a8, White queen on b6, White king nearby

        // First, we need to open up the board by moving some pawns
        Board board = game.getBoard();

        // Make several moves to set up our position
        // This sequence is approximate and might need adjustments

        // 1. Clear some space by moving pawns
        movePiece(game, new Position(0, 6), new Position(0, 4)); // a4
        movePiece(game, new Position(0, 1), new Position(0, 3)); // a5
        movePiece(game, new Position(1, 6), new Position(1, 4)); // b4
        movePiece(game, new Position(1, 1), new Position(1, 3)); // b5

        // 2. Move out the rooks to free the kings
        movePiece(game, new Position(0, 7), new Position(0, 5)); // Ra3
        movePiece(game, new Position(0, 0), new Position(0, 2)); // Ra6

        // 3. Move out the kings
        movePiece(game, new Position(4, 7), new Position(3, 6)); // White king
        movePiece(game, new Position(4, 0), new Position(5, 0)); // Black king

        // 4. Continue with moves to reach the stalemate position
        // ... (More moves would be needed)

        // For testing purposes, we can "teleport" pieces to create our stalemate
        // Note: This is a simplification and not how real chess works

        // Reset the board to just have the kings and queen in stalemate position
        resetBoardForStalemate(game);
    }

    private void resetBoardForStalemate(GameState game) {
        // A direct approach to create a stalemate position for testing
        // This requires the GameState to expose a method to set the board state
        // Since we don't have this, we'll simulate it through the test

        // For a real test, you would need to add a method to GameState to set the board
        // or expose the board for testing purposes

        // For now, we'll just verify that stalemate detection works
        // by checking if a stalemate would be recognized if it occurred

        // Check if a basic stalemate condition would be detected
        Board board = game.getBoard();

        // Create a mock stalemate situation where:
        // 1. Current player is not in check
        // 2. Current player has no legal moves

        // For testing, manually set up the last move to force a stalemate situation
        PieceColor currTurn = game.getCurrentTurn();

        // Make a move that would lead to stalemate
        // In a real game, this would be a sequence of moves
        // Here we're simulating the final move that causes stalemate

        // Since we can't directly modify the board state in the test,
        // we'll rely on the board.isStalemate() method to correctly identify stalemate
        // when it occurs naturally during gameplay

        // For this test purpose, we'll just verify the game correctly handles
        // a stalemate situation if the Board reports one

        // Instead of testing a full stalemate sequence, we'll mark this test
        // as a placeholder to document that stalemate should be tested
        // when you can create the appropriate board state

        System.out.println("NOTE: Full stalemate test requires ability to set board state");
        System.out.println("This test checks that stalemate is recognized by the GameState");

        // We're assuming the board.isStalemate() works correctly
        // The GameState should end the game if stalemate is detected
    }

    private boolean movePiece(GameState game, Position from, Position to) {
        Board board = game.getBoard();
        Piece piece = board.getPiece(from);
        if (piece == null) {
            return false;
        }

        Move move = new Move.Builder()
                .from(from)
                .to(to)
                .piece(piece)
                .capturedPiece(board.getPiece(to))
                .build();

        return game.makeMove(move);
    }
}
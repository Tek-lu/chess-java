package test;

import Model.piece.*;
import Model.Position;
import Model.PieceColor;
import Model.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PawnTest {

    private Board board;

    @BeforeEach
    public void setUp() {
        board = new Board();
        // Clear the board for clean tests
        clearBoard(board);
    }

    // Helper method to clear the board
    private void clearBoard(Board board) {
        // Remove all pieces to start with a clean board for each test
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                Position pos = new Position(x, y);
                Piece piece = board.getPiece(pos);
                if (piece != null) {
                    board.removePiece(pos);
                }
            }
        }
    }

    @Test
    public void testWhitePawnInitialMoves() {
        // Create a white pawn in its initial position
        Pawn whitePawn = new Pawn(PieceColor.WHITE, new Position(3, 6));
        board.placePiece(whitePawn);

        // Get legal moves
        List<Move> legalMoves = whitePawn.getLegalMoves(board);

        // Pawn should have 2 possible moves from its initial position
        assertEquals(2, legalMoves.size());

        // Verify both expected positions are included
        boolean containsAllExpectedMoves = true;
        Position[] expectedPositions = {
                new Position(3, 5), // One square forward
                new Position(3, 4)  // Two squares forward
        };

        for (Position expectedPos : expectedPositions) {
            boolean found = false;
            for (Move move : legalMoves) {
                if (move.getTo().equals(expectedPos)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                containsAllExpectedMoves = false;
                break;
            }
        }

        assertTrue(containsAllExpectedMoves);
    }

    @Test
    public void testBlackPawnInitialMoves() {
        // Create a black pawn in its initial position
        Pawn blackPawn = new Pawn(PieceColor.BLACK, new Position(3, 1));
        board.placePiece(blackPawn);

        // Get legal moves
        List<Move> legalMoves = blackPawn.getLegalMoves(board);

        // Pawn should have 2 possible moves from its initial position
        assertEquals(2, legalMoves.size());

        // Verify both expected positions are included
        boolean containsAllExpectedMoves = true;
        Position[] expectedPositions = {
                new Position(3, 2), // One square forward (downward for black)
                new Position(3, 3)  // Two squares forward (downward for black)
        };

        for (Position expectedPos : expectedPositions) {
            boolean found = false;
            for (Move move : legalMoves) {
                if (move.getTo().equals(expectedPos)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                containsAllExpectedMoves = false;
                break;
            }
        }

        assertTrue(containsAllExpectedMoves);
    }

    @Test
    public void testPawnAfterMoving() {
        // Create a white pawn
        Pawn whitePawn = new Pawn(PieceColor.WHITE, new Position(3, 6));
        board.placePiece(whitePawn);

        // Simulate moving the pawn
        Move move = new Move.Builder()
                .from(new Position(3, 6))
                .to(new Position(3, 5))
                .piece(whitePawn)
                .build();

        board.makeMove(move);

        // Get legal moves
        List<Move> legalMoves = whitePawn.getLegalMoves(board);

        // Pawn should have only 1 possible move after moving (can't do the double move)
        assertEquals(1, legalMoves.size());

        // Verify the expected position
        assertEquals(new Position(3, 4), legalMoves.get(0).getTo());
    }

    @Test
    public void testPawnCaptureMoves() {
        // Create a white pawn
        Pawn whitePawn = new Pawn(PieceColor.WHITE, new Position(3, 3));
        board.placePiece(whitePawn);

        // Create black pieces at capture positions
        Piece blackPiece1 = new Pawn(PieceColor.BLACK, new Position(2, 2));
        Piece blackPiece2 = new Pawn(PieceColor.BLACK, new Position(4, 2));
        board.placePiece(blackPiece1);
        board.placePiece(blackPiece2);

        // Get legal moves
        List<Move> legalMoves = whitePawn.getLegalMoves(board);

        // Pawn should have 3 possible moves: forward and two captures
        assertEquals(3, legalMoves.size());

        // Check that both capture moves exist
        boolean leftCaptureFound = false;
        boolean rightCaptureFound = false;
        for (Move move : legalMoves) {
            if (move.getTo().equals(new Position(2, 2)) && move.getCapturedPiece() == blackPiece1) {
                leftCaptureFound = true;
            }
            if (move.getTo().equals(new Position(4, 2)) && move.getCapturedPiece() == blackPiece2) {
                rightCaptureFound = true;
            }
        }

        assertTrue(leftCaptureFound);
        assertTrue(rightCaptureFound);
    }

    @Test
    public void testPawnBlockedByPiece() {
        // Create a white pawn
        Pawn whitePawn = new Pawn(PieceColor.WHITE, new Position(3, 6));
        board.placePiece(whitePawn);

        // Create another piece directly in front of the pawn
        Piece blockingPiece = new Pawn(PieceColor.BLACK, new Position(3, 5));
        board.placePiece(blockingPiece);

        // Get legal moves
        List<Move> legalMoves = whitePawn.getLegalMoves(board);

        // Pawn should have 0 possible moves when blocked
        assertEquals(0, legalMoves.size());
    }

    @Test
    public void testPawnBlockedTwoSquaresAhead() {
        // Create a white pawn in initial position
        Pawn whitePawn = new Pawn(PieceColor.WHITE, new Position(3, 6));
        board.placePiece(whitePawn);

        // Create another piece two squares ahead of the pawn
        Piece blockingPiece = new Pawn(PieceColor.BLACK, new Position(3, 4));
        board.placePiece(blockingPiece);

        // Get legal moves
        List<Move> legalMoves = whitePawn.getLegalMoves(board);

        // Pawn should have 1 possible move (one square forward)
        assertEquals(1, legalMoves.size());
        assertEquals(new Position(3, 5), legalMoves.get(0).getTo());
    }

    @Test
    public void testPawnCannotCaptureFriendlyPiece() {
        // Create a white pawn
        Pawn whitePawn = new Pawn(PieceColor.WHITE, new Position(3, 3));
        board.placePiece(whitePawn);

        // Create a white piece at a diagonal capture position
        Piece whitePiece = new Pawn(PieceColor.WHITE, new Position(4, 2));
        board.placePiece(whitePiece);

        // Get legal moves
        List<Move> legalMoves = whitePawn.getLegalMoves(board);

        // Check that the friendly piece position is not in legal moves
        boolean cantCaptureFriendly = true;
        for (Move move : legalMoves) {
            if (move.getTo().equals(new Position(4, 2))) {
                cantCaptureFriendly = false;
                break;
            }
        }

        assertTrue(cantCaptureFriendly);
    }

    @Test
    public void testPawnGetAttackPositions() {
        // Create a white pawn
        Pawn whitePawn = new Pawn(PieceColor.WHITE, new Position(3, 3));
        board.placePiece(whitePawn);

        // Get attack positions
        List<Position> attackPositions = whitePawn.getAttackPositions(board);

        // Pawn should attack 2 positions diagonally forward
        assertEquals(2, attackPositions.size());

        // Verify both attack positions
        boolean containsAllExpectedPositions = true;
        Position[] expectedPositions = {
                new Position(2, 2), // Left diagonal
                new Position(4, 2)  // Right diagonal
        };

        for (Position expectedPos : expectedPositions) {
            boolean found = false;
            for (Position attackPos : attackPositions) {
                if (attackPos.equals(expectedPos)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                containsAllExpectedPositions = false;
                break;
            }
        }

        assertTrue(containsAllExpectedPositions);
    }

    @Test
    public void testBlackPawnGetAttackPositions() {
        // Create a black pawn
        Pawn blackPawn = new Pawn(PieceColor.BLACK, new Position(3, 4));
        board.placePiece(blackPawn);

        // Get attack positions
        List<Position> attackPositions = blackPawn.getAttackPositions(board);

        // Pawn should attack 2 positions diagonally forward (downward for black)
        assertEquals(2, attackPositions.size());

        // Verify both attack positions
        boolean containsAllExpectedPositions = true;
        Position[] expectedPositions = {
                new Position(2, 5), // Left diagonal (from black's perspective)
                new Position(4, 5)  // Right diagonal (from black's perspective)
        };

        for (Position expectedPos : expectedPositions) {
            boolean found = false;
            for (Position attackPos : attackPositions) {
                if (attackPos.equals(expectedPos)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                containsAllExpectedPositions = false;
                break;
            }
        }

        assertTrue(containsAllExpectedPositions);
    }

    @Test
    public void testPawnAtEdge() {
        // Create a white pawn at the edge of the board
        Pawn whitePawn = new Pawn(PieceColor.WHITE, new Position(0, 3));
        board.placePiece(whitePawn);

        // Get attack positions
        List<Position> attackPositions = whitePawn.getAttackPositions(board);

        // Pawn should attack only 1 position (right diagonal) when at left edge
        assertEquals(1, attackPositions.size());
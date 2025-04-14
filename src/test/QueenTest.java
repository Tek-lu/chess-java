package test;

import Model.piece.*;
import Model.Position;
import Model.PieceColor;
import Model.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class QueenTest {

    private Board board;

    @BeforeEach
    public void setUp() {
        board = new Board();
        // Clear the board for clean tests
        // Implementation would depend on Board class implementation
    }

    @Test
    public void testQueenCenterMoves() {
        // Create a queen at the center of the board
        Queen queen = new Queen(PieceColor.WHITE, new Position(3, 3));
        // Place the queen on the board
        // Implementation would depend on Board class

        // Get legal moves
        List<Move> legalMoves = queen.getLegalMoves(board);

        // Queen in center should have 27 possible moves (8 directions with varying distances)
        assertEquals(27, legalMoves.size());

        // Check that moves in all 8 directions are included
        // Sample a few key positions to verify
        Position[] expectedPositions = {
                // Horizontal moves
                new Position(0, 3), new Position(1, 3), new Position(2, 3),
                new Position(4, 3), new Position(5, 3), new Position(6, 3), new Position(7, 3),
                // Vertical moves
                new Position(3, 0), new Position(3, 1), new Position(3, 2),
                new Position(3, 4), new Position(3, 5), new Position(3, 6), new Position(3, 7),
                // Diagonal moves (top-right)
                new Position(4, 4), new Position(5, 5), new Position(6, 6), new Position(7, 7),
                // Diagonal moves (top-left)
                new Position(2, 4), new Position(1, 5), new Position(0, 6),
                // Diagonal moves (bottom-right)
                new Position(4, 2), new Position(5, 1), new Position(6, 0),
                // Diagonal moves (bottom-left)
                new Position(2, 2), new Position(1, 1), new Position(0, 0)
        };

        for (Position expectedPos : expectedPositions) {
            boolean found = false;
            for (Move move : legalMoves) {
                if (move.getTo().equals(expectedPos)) {
                    found = true;
                    break;
                }
            }
            assertTrue(found, "Expected position " + expectedPos + " not found in legal moves");
        }
    }

    @Test
    public void testQueenCapture() {
        // Create a white queen
        Queen whiteQueen = new Queen(PieceColor.WHITE, new Position(3, 3));

        // Create black pieces at positions the queen can attack
        Piece blackPiece1 = new Pawn(PieceColor.BLACK, new Position(3, 6)); // Vertical
        Piece blackPiece2 = new Pawn(PieceColor.BLACK, new Position(6, 3)); // Horizontal
        Piece blackPiece3 = new Pawn(PieceColor.BLACK, new Position(5, 5)); // Diagonal

        // Place pieces on board
        // Implementation would depend on Board class

        // Get legal moves
        List<Move> legalMoves = whiteQueen.getLegalMoves(board);

        // Check that all capture moves exist
        boolean verticalCaptureFound = false;
        boolean horizontalCaptureFound = false;
        boolean diagonalCaptureFound = false;

        for (Move move : legalMoves) {
            if (move.getTo().equals(new Position(3, 6)) && move.getCapturedPiece() == blackPiece1) {
                verticalCaptureFound = true;
            }
            if (move.getTo().equals(new Position(6, 3)) && move.getCapturedPiece() == blackPiece2) {
                horizontalCaptureFound = true;
            }
            if (move.getTo().equals(new Position(5, 5)) && move.getCapturedPiece() == blackPiece3) {
                diagonalCaptureFound = true;
            }
        }

        assertTrue(verticalCaptureFound, "Vertical capture move not found");
        assertTrue(horizontalCaptureFound, "Horizontal capture move not found");
        assertTrue(diagonalCaptureFound, "Diagonal capture move not found");
    }

    @Test
    public void testQueenBlockedByAlly() {
        // Create a white queen
        Queen whiteQueen = new Queen(PieceColor.WHITE, new Position(3, 3));

        // Create white pieces at positions that block the queen
        Piece whitePiece1 = new Pawn(PieceColor.WHITE, new Position(3, 5)); // Vertical
        Piece whitePiece2 = new Pawn(PieceColor.WHITE, new Position(5, 3)); // Horizontal
        Piece whitePiece3 = new Pawn(PieceColor.WHITE, new Position(5, 5)); // Diagonal

        // Place pieces on board
        // Implementation would depend on Board class

        // Get legal moves
        List<Move> legalMoves = whiteQueen.getLegalMoves(board);

        // Check that blocked positions are not in legal moves
        for (Move move : legalMoves) {
            Position to = move.getTo();

            // The queen shouldn't be able to move to or past allied pieces
            assertFalse(to.equals(new Position(3, 5)), "Queen shouldn't move to allied piece position");
            assertFalse(to.equals(new Position(3, 6)), "Queen shouldn't move past allied piece");
            assertFalse(to.equals(new Position(3, 7)), "Queen shouldn't move past allied piece");

            assertFalse(to.equals(new Position(5, 3)), "Queen shouldn't move to allied piece position");
            assertFalse(to.equals(new Position(6, 3)), "Queen shouldn't move past allied piece");
            assertFalse(to.equals(new Position(7, 3)), "Queen shouldn't move past allied piece");

            assertFalse(to.equals(new Position(5, 5)), "Queen shouldn't move to allied piece position");
            assertFalse(to.equals(new Position(6, 6)), "Queen shouldn't move past allied piece");
            assertFalse(to.equals(new Position(7, 7)), "Queen shouldn't move past allied piece");
        }
    }

    @Test
    public void testQueenBlockedByOpponent() {
        // Create a white queen
        Queen whiteQueen = new Queen(PieceColor.WHITE, new Position(3, 3));

        // Create black pieces at positions that block the queen
        Piece blackPiece = new Pawn(PieceColor.BLACK, new Position(3, 5)); // Vertical

        // Place pieces on board
        // Implementation would depend on Board class

        // Get legal moves
        List<Move> legalMoves = whiteQueen.getLegalMoves(board);

        // Queen should be able to capture the blocking piece but not move past it
        boolean canCaptureBlockingPiece = false;
        boolean cannotMovePastPiece = true;

        for (Move move : legalMoves) {
            Position to = move.getTo();

            if (to.equals(new Position(3, 5)) && move.getCapturedPiece() == blackPiece) {
                canCaptureBlockingPiece = true;
            }

            if (to.equals(new Position(3, 6)) || to.equals(new Position(3, 7))) {
                cannotMovePastPiece = false;
            }
        }

        assertTrue(canCaptureBlockingPiece, "Queen should be able to capture blocking opponent piece");
        assertTrue(cannotMovePastPiece, "Queen shouldn't be able to move past blocking piece");
    }

    @Test
    public void testQueenCornerMoves() {
        // Create a queen at the corner of the board
        Queen queen = new Queen(PieceColor.WHITE, new Position(0, 0));

        // Place the queen on the board
        // Implementation would depend on Board class

        // Get legal moves
        List<Move> legalMoves = queen.getLegalMoves(board);

        // Queen in corner should have 21 possible moves (3 directions with varying distances)
        assertEquals(21, legalMoves.size());

        // Check that moves in the available 3 directions are included
        // Sample a few key positions to verify
        Position[] expectedPositions = {
                // Horizontal moves
                new Position(1, 0), new Position(2, 0), new Position(7, 0),
                // Vertical moves
                new Position(0, 1), new Position(0, 2), new Position(0, 7),
                // Diagonal moves
                new Position(1, 1), new Position(2, 2), new Position(7, 7)
        };

        for (Position expectedPos : expectedPositions) {
            boolean found = false;
            for (Move move : legalMoves) {
                if (move.getTo().equals(expectedPos)) {
                    found = true;
                    break;
                }
            }
            assertTrue(found, "Expected position " + expectedPos + " not found in legal moves");
        }
    }

    @Test
    public void testQueenGetAttackPositions() {
        // Create a queen at a specific position
        Queen queen = new Queen(PieceColor.WHITE, new Position(3, 3));

        // Place the queen on an empty board
        // Implementation would depend on Board class

        // Get attack positions
        List<Position> attackPositions = queen.getAttackPositions(board);

        // Queen should attack 27 positions on an empty board from center
        assertEquals(27, attackPositions.size());

        // Verify some expected attack positions
        Position[] expectedPositions = {
                // Sample positions in different directions
                new Position(3, 0), // Vertical down
                new Position(3, 7), // Vertical up
                new Position(0, 3), // Horizontal left
                new Position(7, 3), // Horizontal right
                new Position(0, 0), // Diagonal bottom-left
                new Position(7, 7), // Diagonal top-right
                new Position(0, 6), // Diagonal top-left
                new Position(6, 0)  // Diagonal bottom-right
        };

        for (Position expectedPos : expectedPositions) {
            boolean found = false;
            for (Position attackPos : attackPositions) {
                if (attackPos.equals(expectedPos)) {
                    found = true;
                    break;
                }
            }
            assertTrue(found, "Expected attack position " + expectedPos + " not found");
        }
    }

    @Test
    public void testQueenAttackPositionsWithBlockingPiece() {
        // Create a queen
        Queen queen = new Queen(PieceColor.WHITE, new Position(3, 3));

        // Create a blocking piece
        Piece blockingPiece = new Pawn(PieceColor.BLACK, new Position(3, 5));

        // Place pieces on board
        // Implementation would depend on Board class

        // Get attack positions
        List<Position> attackPositions = queen.getAttackPositions(board);

        // Queen should attack the blocking piece position
        boolean attacksBlockingPiece = false;
        for (Position attackPos : attackPositions) {
            if (attackPos.equals(new Position(3, 5))) {
                attacksBlockingPiece = true;
                break;
            }
        }
        assertTrue(attacksBlockingPiece, "Queen should attack the blocking piece position");

        // Queen should not attack positions beyond the blocking piece in that direction
        boolean doesNotAttackBeyondBlockingPiece = true;
        for (Position attackPos : attackPositions) {
            if (attackPos.equals(new Position(3, 6)) || attackPos.equals(new Position(3, 7))) {
                doesNotAttackBeyondBlockingPiece = false;
                break;
            }
        }
        assertTrue(doesNotAttackBeyondBlockingPiece, "Queen should not attack beyond blocking piece");
    }

    @Test
    public void testQueenType() {
        // Create a queen
        Queen queen = new Queen(PieceColor.WHITE, new Position(0, 0));

        // Check the type
        assertEquals("Queen", queen.getType());
    }

    @Test
    public void testQueenCopy() {
        // Create original queen
        Queen originalQueen = new Queen(PieceColor.WHITE, new Position(3, 3));

        // Create a copy
        Piece copiedQueen = originalQueen.copy();

        // Check it's a Queen
        assertTrue(copiedQueen instanceof Queen);

        // Check color and position are the same
        assertEquals(PieceColor.WHITE, copiedQueen.getColor());
        assertEquals(new Position(3, 3), copiedQueen.getPosition());
    }
}
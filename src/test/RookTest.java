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

public class RookTest {

    private Board board;

    @BeforeEach
    public void setUp() {
        board = new Board();
        // Clear the board for clean tests
        // Implementation would depend on Board class implementation
    }

    @Test
    public void testRookCenterMoves() {
        // Create a rook at the center of the board
        Rook rook = new Rook(PieceColor.WHITE, new Position(3, 3));
        // Place the rook on the board
        // Implementation would depend on Board class

        // Get legal moves
        List<Move> legalMoves = rook.getLegalMoves(board);

        // Rook in center should have 14 possible moves (horizontal and vertical)
        assertEquals(14, legalMoves.size());

        // Check that moves in all 4 directions are included
        // Sample key positions to verify
        Position[] expectedPositions = {
                // Horizontal moves
                new Position(0, 3), new Position(1, 3), new Position(2, 3),
                new Position(4, 3), new Position(5, 3), new Position(6, 3), new Position(7, 3),
                // Vertical moves
                new Position(3, 0), new Position(3, 1), new Position(3, 2),
                new Position(3, 4), new Position(3, 5), new Position(3, 6), new Position(3, 7)
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
    public void testRookCapture() {
        // Create a white rook
        Rook whiteRook = new Rook(PieceColor.WHITE, new Position(3, 3));

        // Create black pieces at positions the rook can attack
        Piece blackPiece1 = new Pawn(PieceColor.BLACK, new Position(3, 6)); // Vertical
        Piece blackPiece2 = new Pawn(PieceColor.BLACK, new Position(6, 3)); // Horizontal

        // Place pieces on board
        // Implementation would depend on Board class

        // Get legal moves
        List<Move> legalMoves = whiteRook.getLegalMoves(board);

        // Check that capture moves exist
        boolean verticalCaptureFound = false;
        boolean horizontalCaptureFound = false;

        for (Move move : legalMoves) {
            if (move.getTo().equals(new Position(3, 6)) && move.getCapturedPiece() == blackPiece1) {
                verticalCaptureFound = true;
            }
            if (move.getTo().equals(new Position(6, 3)) && move.getCapturedPiece() == blackPiece2) {
                horizontalCaptureFound = true;
            }
        }

        assertTrue(verticalCaptureFound, "Vertical capture move not found");
        assertTrue(horizontalCaptureFound, "Horizontal capture move not found");
    }

    @Test
    public void testRookBlockedByAlly() {
        // Create a white rook
        Rook whiteRook = new Rook(PieceColor.WHITE, new Position(3, 3));

        // Create white pieces at positions that block the rook
        Piece whitePiece1 = new Pawn(PieceColor.WHITE, new Position(3, 5)); // Vertical
        Piece whitePiece2 = new Pawn(PieceColor.WHITE, new Position(5, 3)); // Horizontal

        // Place pieces on board
        // Implementation would depend on Board class

        // Get legal moves
        List<Move> legalMoves = whiteRook.getLegalMoves(board);

        // Check that blocked positions are not in legal moves
        for (Move move : legalMoves) {
            Position to = move.getTo();

            // The rook shouldn't be able to move to or past allied pieces
            assertFalse(to.equals(new Position(3, 5)), "Rook shouldn't move to allied piece position");
            assertFalse(to.equals(new Position(3, 6)), "Rook shouldn't move past allied piece");
            assertFalse(to.equals(new Position(3, 7)), "Rook shouldn't move past allied piece");

            assertFalse(to.equals(new Position(5, 3)), "Rook shouldn't move to allied piece position");
            assertFalse(to.equals(new Position(6, 3)), "Rook shouldn't move past allied piece");
            assertFalse(to.equals(new Position(7, 3)), "Rook shouldn't move past allied piece");
        }
    }

    @Test
    public void testRookBlockedByOpponent() {
        // Create a white rook
        Rook whiteRook = new Rook(PieceColor.WHITE, new Position(3, 3));

        // Create a black piece at a position that blocks the rook
        Piece blackPiece = new Pawn(PieceColor.BLACK, new Position(3, 5)); // Vertical

        // Place pieces on board
        // Implementation would depend on Board class

        // Get legal moves
        List<Move> legalMoves = whiteRook.getLegalMoves(board);

        // Rook should be able to capture the blocking piece but not move past it
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

        assertTrue(canCaptureBlockingPiece, "Rook should be able to capture blocking opponent piece");
        assertTrue(cannotMovePastPiece, "Rook shouldn't be able to move past blocking piece");
    }

    @Test
    public void testRookCornerMoves() {
        // Create a rook at the corner of the board
        Rook rook = new Rook(PieceColor.WHITE, new Position(0, 0));

        // Place the rook on the board
        // Implementation would depend on Board class

        // Get legal moves
        List<Move> legalMoves = rook.getLegalMoves(board);

        // Rook in corner should have 14 possible moves (7 horizontal + 7 vertical)
        assertEquals(14, legalMoves.size());

        // Check that moves in the available 2 directions are included
        // Sample positions to verify
        Position[] expectedPositions = {
                // Horizontal moves
                new Position(1, 0), new Position(2, 0), new Position(7, 0),
                // Vertical moves
                new Position(0, 1), new Position(0, 2), new Position(0, 7)
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
    public void testRookGetAttackPositions() {
        // Create a rook at a specific position
        Rook rook = new Rook(PieceColor.WHITE, new Position(3, 3));

        // Place the rook on an empty board
        // Implementation would depend on Board class

        // Get attack positions
        List<Position> attackPositions = rook.getAttackPositions(board);

        // Rook should attack 14 positions on an empty board from center
        assertEquals(14, attackPositions.size());

        // Verify some expected attack positions
        Position[] expectedPositions = {
                // Sample positions in different directions
                new Position(3, 0), // Vertical down
                new Position(3, 7), // Vertical up
                new Position(0, 3), // Horizontal left
                new Position(7, 3)  // Horizontal right
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
    public void testRookAttackPositionsWithBlockingPiece() {
        // Create a rook
        Rook rook = new Rook(PieceColor.WHITE, new Position(3, 3));

        // Create a blocking piece
        Piece blockingPiece = new Pawn(PieceColor.BLACK, new Position(3, 5));

        // Place pieces on board
        // Implementation would depend on Board class

        // Get attack positions
        List<Position> attackPositions = rook.getAttackPositions(board);

        // Rook should attack the blocking piece position
        boolean attacksBlockingPiece = false;
        for (Position attackPos : attackPositions) {
            if (attackPos.equals(new Position(3, 5))) {
                attacksBlockingPiece = true;
                break;
            }
        }
        assertTrue(attacksBlockingPiece, "Rook should attack the blocking piece position");

        // Rook should not attack positions beyond the blocking piece in that direction
        boolean doesNotAttackBeyondBlockingPiece = true;
        for (Position attackPos : attackPositions) {
            if (attackPos.equals(new Position(3, 6)) || attackPos.equals(new Position(3, 7))) {
                doesNotAttackBeyondBlockingPiece = false;
                break;
            }
        }
        assertTrue(doesNotAttackBeyondBlockingPiece, "Rook should not attack beyond blocking piece");
    }

    @Test
    public void testRookType() {
        // Create a rook
        Rook rook = new Rook(PieceColor.WHITE, new Position(0, 0));

        // Check the type
        assertEquals("Rook", rook.getType());
    }

    @Test
    public void testRookCopy() {
        // Create original rook
        Rook originalRook = new Rook(PieceColor.WHITE, new Position(3, 3));

        // Create a copy
        Piece copiedRook = originalRook.copy();

        // Check it's a Rook
        assertTrue(copiedRook instanceof Rook);

        // Check color and position are the same
        assertEquals(PieceColor.WHITE, copiedRook.getColor());
        assertEquals(new Position(3, 3), copiedRook.getPosition());
    }
}
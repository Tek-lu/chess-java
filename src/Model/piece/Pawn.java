package Model.piece;

import java.util.ArrayList;
import java.util.List;

import Model.*;

public class Pawn extends Piece {
    private boolean wasMoved;

    public Pawn(PieceColor color, Position position) {
        super(color, position);
    }

    @Override
    protected String determineImageFile() {
        return getColor() == PieceColor.WHITE ? "images/wp.png" : "images/bp.png";
    }

    @Override
    public List<Move> getLegalMoves(Board board) {
        List<Move> legalMoves = new ArrayList<>();
        Position position = getPosition();
        int x = position.getX();
        int y = position.getY();
        int direction = getColor() == PieceColor.WHITE ? 1 : -1;

        // One square forward
        Position oneForward = new Position(x, y + direction);
        if (board.isValidPosition(oneForward) && board.getPiece(oneForward) == null) {
            addMoveIfNotInCheck(board, position, oneForward, legalMoves);

            // Two squares forward (only if the pawn hasn't moved yet)
            if (!wasMoved) {
                Position twoForward = new Position(x, y + 2 * direction);
                if (board.isValidPosition(twoForward) && board.getPiece(twoForward) == null) {
                    addMoveIfNotInCheck(board, position, twoForward, legalMoves);
                }
            }
        }

        // Diagonal captures
        Position leftCapture = new Position(x - 1, y + direction);
        if (board.isValidPosition(leftCapture)) {
            Piece targetPiece = board.getPiece(leftCapture);
            if (targetPiece != null && targetPiece.getColor() != getColor()) {
                addMoveIfNotInCheck(board, position, leftCapture, legalMoves);
            }
        }

        Position rightCapture = new Position(x + 1, y + direction);
        if (board.isValidPosition(rightCapture)) {
            Piece targetPiece = board.getPiece(rightCapture);
            if (targetPiece != null && targetPiece.getColor() != getColor()) {
                addMoveIfNotInCheck(board, position, rightCapture, legalMoves);
            }
        }

        // TODO: En passant move would be implemented here

        return legalMoves;
    }

    private void addMoveIfNotInCheck(Board board, Position from, Position to, List<Move> moves) {
        Piece capturedPiece = board.getPiece(to);
        Move move = new Move.Builder()
                .from(from)
                .to(to)
                .piece(this)
                .capturedPiece(capturedPiece)
                .build();

        if (!wouldLeaveKingInCheck(board, move)) {
            moves.add(move);
        }
    }

    @Override
    public List<Position> getAttackPositions(Board board) {
        List<Position> attackPositions = new ArrayList<>();
        Position position = getPosition();
        int x = position.getX();
        int y = position.getY();
        int direction = getColor() == PieceColor.WHITE ? 1 : -1;

        // Pawns attack diagonally
        Position leftAttack = new Position(x - 1, y + direction);
        if (board.isValidPosition(leftAttack)) {
            attackPositions.add(leftAttack);
        }

        Position rightAttack = new Position(x + 1, y + direction);
        if (board.isValidPosition(rightAttack)) {
            attackPositions.add(rightAttack);
        }

        return attackPositions;
    }

    @Override
    public boolean moveTo(Position position) {
        boolean moved = super.moveTo(position);
        if (moved) {
            wasMoved = true;
        }
        return moved;
    }

    @Override
    public Piece copy() {
        return new Pawn(getColor(), getPosition());

    }

    @Override
    public String getType() {
        return "Pawn";
    }
}
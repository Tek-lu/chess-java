package Model.piece;

import java.util.ArrayList;
import java.util.List;

import Model.*;

public class King extends Piece {

    public King(PieceColor color, Position position) {
        super(color, position);
    }

    @Override
    protected String determineImageFile() {
        return getColor() == PieceColor.WHITE ? "wk.png" : "bk.png";
    }

    @Override
    public List<Move> getLegalMoves(Board board) {
        List<Move> legalMoves = new ArrayList<>();
        Position position = getPosition();
        int x = position.getX();
        int y = position.getY();

        // Check all 8 squares around the king
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                // Skip the current position
                if (dx == 0 && dy == 0) {
                    continue;
                }

                Position targetPos = new Position(x + dx, y + dy);

                if (board.isValidPosition(targetPos)) {
                    Piece targetPiece = board.getPiece(targetPos);

                    // Empty square or opponent's piece
                    if (targetPiece == null || targetPiece.getColor() != getColor()) {
                        // For the king, we need to check if the new position is under attack
                        if (!isPositionUnderAttack(board, targetPos)) {
                            Move move = new Move.Builder()
                                    .from(position)
                                    .to(targetPos)
                                    .piece(this)
                                    .capturedPiece(targetPiece)
                                    .build();

                            legalMoves.add(move);
                        }
                    }
                }
            }
        }

        // TODO: Add castling logic here

        return legalMoves;
    }

    /**
     * Check if a position is under attack by any of the opponent's pieces
     */
    private boolean isPositionUnderAttack(Board board, Position position) {
        PieceColor opponentColor = getColor() == PieceColor.WHITE ?
                PieceColor.BLACK : PieceColor.WHITE;

        List<Piece> opponentPieces = board.getPieces(opponentColor);

        for (Piece piece : opponentPieces) {
            List<Position> attackPositions = piece.getAttackPositions(board);

            for (Position attackPos : attackPositions) {
                if (attackPos.equals(position)) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public List<Position> getAttackPositions(Board board) {
        List<Position> attackPositions = new ArrayList<>();
        Position position = getPosition();
        int x = position.getX();
        int y = position.getY();

        // Check all 8 squares around the king
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                // Skip the current position
                if (dx == 0 && dy == 0) {
                    continue;
                }

                Position targetPos = new Position(x + dx, y + dy);

                if (board.isValidPosition(targetPos)) {
                    attackPositions.add(targetPos);
                }
            }
        }

        return attackPositions;
    }

    @Override
    public String getType() {
        return "King";
    }

    @Override
    public Piece copy() {
        return new King(getColor(), getPosition());
    }
}
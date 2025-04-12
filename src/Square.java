package view;

import model.Board;
import model.Piece;

import javax.swing.*;
import java.awt.*;

public class Square extends JComponent {

    private final Board board;
    private final int color;
    private final int xNum;
    private final int yNum;

    private Piece occupyingPiece;
    private boolean displayPiece;

    public Square(Board board, int color, int xNum, int yNum) {
        this.board = board;
        this.color = color;
        this.displayPiece = true;
        this.xNum = xNum;
        this.yNum = yNum;

        setBorder(BorderFactory.createEmptyBorder());
    }

    public int getColorCode() {
        return color;
    }

    public Piece getOccupyingPiece() {
        return occupyingPiece;
    }

    public boolean isOccupied() {
        return occupyingPiece != null;
    }

    public int getXNum() {
        return xNum;
    }

    public int getYNum() {
        return yNum;
    }

    public void setDisplayPiece(boolean displayPiece) {
        this.displayPiece = displayPiece;
    }

    public void put(Piece piece) {
        this.occupyingPiece = piece;
        piece.setPosition(this);
    }

    public Piece removePiece() {
        Piece removed = occupyingPiece;
        occupyingPiece = null;
        return removed;
    }

    public void capture(Piece piece) {
        Piece existing = getOccupyingPiece();
        if (existing != null) {
            if (existing.getColor() == 0) {
                board.getBlackPieces().remove(existing);
            } else if (existing.getColor() == 1) {
                board.getWhitePieces().remove(existing);
            }
        }
        this.occupyingPiece = piece;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Color tileColor = (color == 1) ? new Color(221, 192, 127) : new Color(101, 67, 33);
        g.setColor(tileColor);
        g.fillRect(0, 0, getWidth(), getHeight());

        if (occupyingPiece != null && displayPiece) {
            occupyingPiece.draw(g);
        }
    }

    @Override
    public int hashCode() {
        return 31 * xNum + yNum;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Square square)) return false;
        return this.xNum == square.xNum && this.yNum == square.yNum;
    }
}

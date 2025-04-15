//package View;
//
//import Model.Board;
//import Model.Piece;
//
//import java.awt.Color;
//import java.awt.Graphics;
//
//import javax.swing.*;
//
//@SuppressWarnings("serial")
//public class SquareView extends JComponent {
//    private Board b;
//
//    private final int color;
//    private Piece occupyingPiece;
//    private boolean dispPiece;
//
//    private int xNum;
//    private int yNum;
//
//    public SquareView(Board b, int c, int xNum, int yNum) {
//
//        this.b = b;
//        this.color = c;
//        this.dispPiece = true;
//        this.xNum = xNum;
//        this.yNum = yNum;
//
//
//        this.setBorder(BorderFactory.createEmptyBorder());
//    }
//
//    public int getColor() {
//        return this.color;
//    }
//
//    public Piece getOccupyingPiece() {
//        return occupyingPiece;
//    }
//
//    public boolean isOccupied() {
//        return (this.occupyingPiece != null);
//    }
//
//    public int getXNum() {
//        return this.xNum;
//    }
//
//    public int getYNum() {
//        return this.yNum;
//    }
//
//    public void setDisplay(boolean v) {
//        this.dispPiece = v;
//    }
//
//    public void put(Piece p) {
//        this.occupyingPiece = p;
//        p.setPosition(this);
//    }
//
//    public Piece removePiece() {
//        Piece p = this.occupyingPiece;
//        this.occupyingPiece = null;
//        return p;
//    }
//
//    public void capture(Piece p) {
//        Piece k = getOccupyingPiece();
//        if (k.getColor() == 0) b.Bpieces.remove(k);
//        if (k.getColor() == 1) b.Wpieces.remove(k);
//        this.occupyingPiece = p;
//    }
//
//    public void paintComponent(Graphics g) {
//        super.paintComponent(g);
//
//        if (this.color == 1) {
//            g.setColor(new Color(221,192,127));
//        } else {
//            g.setColor(new Color(101,67,33));
//        }
//
//        g.fillRect(this.getX(), this.getY(), this.getWidth(), this.getHeight());
//
//        if(occupyingPiece != null && dispPiece) {
//            occupyingPiece.draw(g);
//        }
//    }
//
//    @Override
//    public int hashCode() {
//        int prime = 31;
//        int result = 1;
//        result = prime * result + xNum;
//        result = prime * result + yNum;
//        return result;
//    }
//
//}
package View;

import Model.Piece;
import Model.Position;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;
import javax.imageio.ImageIO;

/**
 * Represents a single square on the chess board UI.
 */
public class SquareView extends JPanel {
    private static final Color LIGHT_SQUARE_COLOR = new Color(240, 217, 181);
    private static final Color DARK_SQUARE_COLOR = new Color(181, 136, 99);
    private static final Color SELECTED_SQUARE_COLOR = new Color(130, 151, 105);
    private static final Color LEGAL_MOVE_COLOR = new Color(130, 151, 105, 128);

    private final Position position;
    private final boolean isLightSquare;
    private Piece occupyingPiece;
    private boolean isSelected;
    private boolean isLegalMove;

    /**
     * Constructs a new SquareView at the specified position.
     *
     * @param position The board position this square represents
     * @param isLightSquare Whether this is a light-colored square
     */
    public SquareView(Position position, boolean isLightSquare) {
        this.position = position;
        this.isLightSquare = isLightSquare;
        this.occupyingPiece = null;
        this.isSelected = false;
        this.isLegalMove = false;

        setPreferredSize(new Dimension(75, 75));
        setBackground(isLightSquare ? LIGHT_SQUARE_COLOR : DARK_SQUARE_COLOR);
        setBorder(BorderFactory.createEmptyBorder());
    }

    /**
     * Sets the piece occupying this square.
     *
     * @param piece The piece to place on this square, or null to remove
     */
    public void setOccupyingPiece(Piece piece) {
        this.occupyingPiece = piece;
        repaint();
    }

    /**
     * Gets the piece occupying this square.
     *
     * @return The occupying piece, or null if empty
     */
    public Piece getOccupyingPiece() {
        return occupyingPiece;
    }

    /**
     * Checks if this square is occupied by a piece.
     *
     * @return True if occupied, false otherwise
     */
    public boolean isOccupied() {
        return occupyingPiece != null;
    }

    /**
     * Sets whether this square is selected.
     *
     * @param selected True if selected, false otherwise
     */
    public void setSelected(boolean selected) {
        this.isSelected = selected;
        repaint();
    }

    /**
     * Sets whether this square represents a legal move.
     *
     * @param legalMove True if a legal move, false otherwise
     */
    public void setLegalMove(boolean legalMove) {
        this.isLegalMove = legalMove;
        repaint();
    }

    /**
     * Gets the position this square represents.
     *
     * @return The board position
     */
    public Position getPosition() {
        return position;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw the square with the appropriate color
        if (isSelected) {
            setBackground(SELECTED_SQUARE_COLOR);
        } else if (isLegalMove) {
            setBackground(isLightSquare ? LIGHT_SQUARE_COLOR : DARK_SQUARE_COLOR);
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setColor(LEGAL_MOVE_COLOR);
            g2d.fillRect(0, 0, getWidth(), getHeight());
            g2d.dispose();
        } else {
            setBackground(isLightSquare ? LIGHT_SQUARE_COLOR : DARK_SQUARE_COLOR);
        }

        // Draw the piece if present
        if (occupyingPiece != null) {
            drawPiece(g);
        }

        // Draw coordinates in small text in the corner (optional)
        g.setColor(isLightSquare ? DARK_SQUARE_COLOR : LIGHT_SQUARE_COLOR);
        g.setFont(new Font("Arial", Font.PLAIN, 10));
        g.drawString(position.toString(), 2, 12);
    }

    /**
     * Draws the chess piece on this square.
     *
     * @param g The graphics context
     */
    private void drawPiece(Graphics g) {
        if (occupyingPiece == null) return;

        try {
            // Load the image for the piece
            String imagePath = "/images/" + occupyingPiece.getImageFile();
            Image pieceImage = loadPieceImage(imagePath);

            if (pieceImage != null) {
                // Center the piece image on the square
                int x = (getWidth() - pieceImage.getWidth(null)) / 2;
                int y = (getHeight() - pieceImage.getHeight(null)) / 2;
                g.drawImage(pieceImage, x, y, null);
            }
        } catch (Exception e) {
            // If image loading fails, draw a simple representation of the piece
            g.setColor(occupyingPiece.getColor().toString().equals("WHITE") ? Color.WHITE : Color.BLACK);
            g.fillOval(15, 15, getWidth() - 30, getHeight() - 30);
            g.setColor(occupyingPiece.getColor().toString().equals("WHITE") ? Color.BLACK : Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawString(occupyingPiece.getType().substring(0, 1), getWidth() / 2 - 8, getHeight() / 2 + 8);
        }
    }

    /**
     * Helper method to load piece images.
     *
     * @param imagePath The path to the image resource
     * @return The loaded image, or null if loading failed
     */
    private Image loadPieceImage(String imagePath) {
        try {
            return ImageIO.read(Objects.requireNonNull(getClass().getResource(imagePath)));
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("Failed to load piece image: " + imagePath);
            return null;
        }
    }
}
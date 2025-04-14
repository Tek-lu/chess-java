package View;

import Model.Board;
import Model.PieceColor;

public interface BoardView {
    void updateBoard(Board board);
    void setCurrentPlayer(PieceColor color);
    void showGameOverMessage(String message);
}

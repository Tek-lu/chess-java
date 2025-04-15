import Controller.ChessController;
import View.GameWindow;
import View.StartMenu;

import javax.swing.*;

public class Game implements Runnable {
    public void run() {
        ChessController controller = new ChessController();
        GameWindow gameWindow = new GameWindow(controller);
        SwingUtilities.invokeLater(new StartMenu(gameWindow, controller));
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Game());
    }
}

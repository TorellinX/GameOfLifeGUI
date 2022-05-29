package org.sosylab;

import javax.swing.SwingUtilities;
import org.sosylab.model.Game;
import org.sosylab.model.Model;
import org.sosylab.view.Controller;
import org.sosylab.view.GameOfLifeController;
import org.sosylab.view.GameOfLifeView;
import org.sosylab.view.View;

/**
 * Main class of the Game of Life project. It starts the application for a user to play the game.
 */
public class GameOfLifeMain {

  /**
   * Invokes the actual starting method {@link GameOfLifeMain#showGameOfLife()} on the <code> AWT
   * event dispatching thread</code>. This causes the method to be executed asynchronously after all
   * pending AWT events have been processed.
   *
   * @param args The command line arguments.
   */
  public static void main(String[] args) {
    SwingUtilities.invokeLater(GameOfLifeMain::showGameOfLife);
  }

  /**
   * Initializes the main {@link Model}, {@link View}, and {@link Controller} classes of this game,
   * and sets the appropriate relations between each other accordingly.
   */
  private static void showGameOfLife() {
    Model model = new Game();
    Controller controller = new GameOfLifeController(model);

    View view = new GameOfLifeView(model, controller);
    controller.setView(view);
    model.addPropertyChangeListener(view);
    controller.start();

  }

}

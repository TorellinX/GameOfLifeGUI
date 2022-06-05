package org.sosylab.view;

import org.sosylab.model.Shape;


/**
 * The main controller interface for Game Of Life. It takes the actions from the user and handles
 * them accordingly. For this the controller either invokes the necessary model-methods, or by
 * directly telling the view to change its graphical user-interface.
 */
public interface Controller {

  /**
   * Set the view that the controller will use afterwards.
   *
   * @param view The {@link View}.
   */
  void setView(View view);

  /**
   * Initializes and starts the user interface.
   */
  void start();

  /**
   * Clear a game-board such that the game has no living cells available afterwards.
   */
  void clearBoard();

  /**
   * Change the state of a cell on the board for the given parameters.
   *
   * @param column The column of the cell.
   * @param row    The row of the cell.
   * @param alive  The new state of the cell. <code>true</code> for alive, <code>false</code>
   *               otherwise
   */
  void setCellAlive(int column, int row, boolean alive);

  /**
   * Validate the input and - in case of success - ask the model to execute a step on the Game Of
   * Life board.
   *
   * @return <code>true</code> if validating the input was successful, <code>false</code> otherwise.
   */
  boolean step();

  /**
   * Set the speed for the execution of simulation steps when invoking the method
   * {@link #stepIndefinitely()}.
   *
   * @param value The speed in milliseconds.
   */
  void setStepSpeed(int value);

  /**
   * Start the simulation until {@link #stopStepping()} gets called.
   */
  void stepIndefinitely();

  /**
   * Stop the execution of simulation steps.
   */
  void stopStepping();

  /**
   * Validate a user request for setting up a new shape. It must fit in the dimensions of the
   * current game board.
   *
   * @param shape The new shape to display.
   */
  void setShape(Shape shape); // TODO: change parameter to your own implementation

  /**
   * Change the size of the board to the given height and width.
   *
   * @param width  The new width of the board
   * @param height The new height of the board
   */
  void resize(int width, int height);

  /**
   * Dispose of any remaining resources.
   */
  void dispose();
}

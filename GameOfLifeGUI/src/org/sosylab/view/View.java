package org.sosylab.view;

import java.beans.PropertyChangeListener;

/**
 * The main interface of the view. The state it displays is directly taken from the
 * {@link org.sosylab.model.Model}.
 */
public interface View extends PropertyChangeListener {

  /**
   * Show the graphical user interface of Game of Life.
   */
  void showGame();

  /**
   * Displays an error message to the user.
   *
   * @param message The message to be displayed
   */
  void showErrorMessage(String message);

  /**
   * Start creating new generations.
   */
  void startStepping();

  /**
   * Stop creating new generations.
   */
  void stopStepping();
}

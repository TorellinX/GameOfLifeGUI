package org.sosylab.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * The main interface of Game of Life model for the graphical user-interface. It provides all
 * necessary methods for accessing and manipulating the data such that a game can be played
 * successfully.
 *
 * <p>When something changes in the model, it notifies all of its observers by firing a {@link
 * PropertyChangeEvent change-event}.
 */
public interface Model extends Grid {

  String STATE_CHANGED = "State changed";

  /**
   * Add a {@link PropertyChangeListener} to the model that gets notified about any changes made to
   * the Game of Life model.
   *
   * @param pcl the view that implements the listener.
   */
  void addPropertyChangeListener(PropertyChangeListener pcl);

  /**
   * Remove a listener from the model. It will then no longer get notified about any events
   * happening in the model.
   *
   * @param pcl the view that is to be unsubscribed from the model.
   */
  void removePropertyChangeListener(PropertyChangeListener pcl);

}

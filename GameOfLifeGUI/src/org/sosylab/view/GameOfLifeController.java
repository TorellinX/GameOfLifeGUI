package org.sosylab.view;

import static java.util.Objects.requireNonNull;

import javax.swing.SwingWorker;
import org.sosylab.model.Cell;
import org.sosylab.model.Model;
import org.sosylab.model.Shape;

/**
 * Implements the main controller for Game Of Life. It takes the actions from the user and handles
 * them accordingly. For this the controller either invokes the necessary model-methods, or by
 * directly telling the view to change its graphical user-interface.
 */

public class GameOfLifeController implements Controller {

  private final Model model;
  private View view;
  private int currentSpeed;
  boolean stepping;
  public static final int MIN_SPEED = 1;
  public static final int MAX_SPEED = 30;

  /**
   * Constructs a new controller of the game.
   *
   * @param gameOfLife a model instance of the game.
   */
  public GameOfLifeController(Model gameOfLife) {
    model = requireNonNull(gameOfLife);
    this.currentSpeed = 1;
    this.stepping = false;
  }

  @Override
  public void setView(View view) {
    this.view = requireNonNull(view);
  }

  @Override
  public void start() {
    view.showGame();
  }

  @Override
  public void clearBoard() {
    model.clear();
  }

  @Override
  public void setCellAlive(int column, int row, boolean alive) {
    if (alive) {
      model.setCellAlive(column, row);
    } else {
      model.setCellDead(column, row);
    }
  }

  @Override
  public boolean step() {
    if (model == null) {
      return false;
    }
    SwingWorker<Void, Void> worker = new SwingWorker<>() {
      @Override
      protected Void doInBackground() {
        model.next();
        return null;
      }
    };
    worker.execute();
    return true;
  }

  @Override
  public void stepIndefinitely() {
    this.stepping = true;
    SwingWorker<Void, Void> swingWorker = new SwingWorker<>() {
      @Override
      protected Void doInBackground() throws Exception {
        view.startStepping();
        while (stepping) {
          Thread.sleep(1000 / currentSpeed);
          step();
        }
        return null;
      }
    };
    swingWorker.execute();
  }

  @Override
  public void setStepSpeed(int value) {
    if (value > MAX_SPEED || value < MIN_SPEED) {
      throw new IllegalArgumentException("Incorrect speed.");
    }
    currentSpeed = value;
  }

  @Override
  public void stopStepping() {
    view.stopStepping();
    this.stepping = false;
  }

  @Override
  public void setShape(Shape shape) {
    if (shape == null) {
      throw new NullPointerException("The Shape is not recognized");
    }
    if (shape.getColumns() > model.getColumns() || shape.getRows() > model.getRows()) {
      view.showErrorMessage(
          "The size of the shape may not exceed the size of the game field. \nThe shape \""
              + shape.getName().substring(0, 1).toUpperCase() + shape.getName().substring(1)
              + "\" requires min. "
              + shape.getColumns() + "x" + shape.getRows() + " field.");
      return;
    }
    model.clear();
    placeShape(shape);
  }

  @Override
  public void resize(int cols, int rows) {
    model.resize(cols, rows);
  }

  @Override
  public void dispose() {
    this.stopStepping();
    model.removePropertyChangeListener(view);
  }

  /**
   * The selected shape will be placed in the middle of the game field.
   *
   * @param shape a shape to be placed
   */
  public void placeShape(Shape shape) {
    int shapeColumns = shape.getColumns();
    int shapeRows = shape.getRows();
    int fieldColumns = model.getColumns();
    int fieldRows = model.getRows();
    int offsetRows = Math.floorDiv(fieldRows - shapeRows, 2);
    int offsetColumns = Math.floorDiv(fieldColumns - shapeColumns, 2);

    for (Cell cell : shape.getShapePopulation()) {
      model.setCellAlive(cell.getColumn() + offsetColumns, cell.getRow() + offsetRows);
    }
  }
}

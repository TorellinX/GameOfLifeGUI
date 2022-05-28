package org.sosylab.view;

import static java.util.Objects.requireNonNull;
import static org.sosylab.model.Shapes.getAvailableShapes;
import static org.sosylab.model.Shapes.getShapeByName;

import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import org.sosylab.model.Cell;
import org.sosylab.model.Game;
import org.sosylab.model.Grid;
import org.sosylab.model.Model;
import org.sosylab.model.Shape;


// TODO: add documentation
public class GameOfLifeController implements Controller {

  private final Model model;
  private View view;

  // TODO insert code here

  // TODO add documentation
  public GameOfLifeController(Model gameOfLife) {
    model = requireNonNull(gameOfLife);
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
    if(alive){
      return;
    }
    model.setCellAlive(column, row);
  }

  @Override
  public boolean step() {
    // TODO insert code here
    // step() soll ein next() mit SwingWorker aufrufen, so dass next() von einem anderen Thread bearbeitet wird.

    SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
      @Override
      protected Boolean doInBackground() throws Exception {
        /*
        SwingUtilities.invokeLater(new Runnable() {
          @Override
          public void run() {

          }
        });
        */
        model.next();
        return true;
      }
    };
    System.out.println("before: " + model.getGenerations());
    worker.execute();
    System.out.println("after???: " + model.getGenerations());
    return false;

  }

  @Override
  public void stepIndefinitely() {
    // TODO insert code here
  }

  @Override
  public void setStepSpeed(int value) {
    // TODO insert code here
  }

  @Override
  public void stopStepping() {
    // TODO insert code here
  }

  @Override
  public void setShape(Shape shape) {
    // проверка, что Shape влезает в поле ????????????????????????????
    model.clear();
    if(shape == null) {
      throw new NullPointerException("The Shape is not recognised");
    }
    placeShape(shape);
  }

  @Override
  public void resize(int width, int height) {
    // TODO insert code here
  }

  @Override
  public void dispose() {
    model.removePropertyChangeListener(view);
  }
  /*
  Die Methode dispose in GameOfLifeView überschreibt die Methode aus JFrame und wird automatisch
  aufgerufen, wenn die DefaultCloseOperation auf DisposeOnClose gesetzt ist und das entsprechende
  JFrame geschlossen wird.
  Man muss halt dran denken, eventuelle laufende SwingWorker zu beenden, wenn die Application
  geschlossen wird.
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

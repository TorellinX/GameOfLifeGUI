package org.sosylab.view;

import static java.util.Objects.requireNonNull;
import static org.sosylab.model.Shapes.getAvailableShapes;
import static org.sosylab.model.Shapes.getShapeByName;

import javax.swing.JOptionPane;
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
  private int currentSpeed;
  boolean stepping;

  // TODO insert code here

  // TODO add documentation
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
    if(alive){
      return;
    }
    model.setCellAlive(column, row);
  }

  @Override
  public boolean step() {
    // step() soll ein next() mit SwingWorker aufrufen, so dass next() von einem anderen Thread bearbeitet wird.
    SwingWorker<Boolean, Void> worker = new SwingWorker<>() {
      @Override
      protected Boolean doInBackground() throws Exception {
       model.next();
        System.out.println("Next: " + true );
       return true;
      }
    };
    worker.execute();
    System.out.println("Next: " + false );
    return false;
  }

  @Override
  public void stepIndefinitely(){
    // ?? Eingabe validieren
    this.stepping = true;
    System.out.println(stepping);
    SwingWorker<Void,Void> swingWorker = new SwingWorker<Void, Void>() {
      @Override
      protected Void doInBackground() throws Exception {
        view.startStepping(); // einige GUI-Elemente deaktivieren (kann auch in View realisiert werden)
        Thread.sleep(1000 / currentSpeed);
        while (stepping) {
          step();
          Thread.sleep(1000 / currentSpeed);
        }
        return null;
      }
    };
    swingWorker.execute();
  }

  @Override
  public void setStepSpeed(int value) {
    // Validierung?
    currentSpeed = value;
  }

  @Override
  public void stopStepping() {
    view.stopStepping();
    this.stepping = false;
    System.out.println(stepping);
  }

  @Override
  public void setShape(Shape shape) {
    System.out.println(shape.getColumns() + ">" + model.getColumns() + "||" + shape.getRows() + ">" + model.getRows());
     if (shape.getColumns() > model.getColumns() || shape.getRows() > model.getRows()) {
       GameOfLifeView.displayError("The size of the shape may not exceed the size of the game field. \nThe shape \""
           + shape.getName().substring(0, 1).toUpperCase() + shape.getName().substring(1) + "\" requires min. "
           + shape.getColumns() + "x" + shape.getRows() + " field.");
       return;
     }
    model.clear();
    if(shape == null) {
      throw new NullPointerException("The Shape is not recognized");
    }
    placeShape(shape);
  }

  @Override
  public void resize(int cols, int rows) {
    model.resize(cols, rows);
    System.out.println("model.resize(cols, rows) done. Model.cols: " + model.getColumns() + "rows: " + model.getRows());
  }

  @Override
  public void dispose() {
    this.stopStepping();
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

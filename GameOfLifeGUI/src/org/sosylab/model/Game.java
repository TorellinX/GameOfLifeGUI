package org.sosylab.model;

import static java.util.Objects.requireNonNull;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Contains the grid, the current shape and the Game Of Life algorithm that changes it.
 *
 * <p>The universe of the Game of Life is an infinite two-dimensional orthogonal
 * grid of square cells, each of which is in one of two possible states, alive or dead. Every cell
 * interacts with its eight neighbors, which are the cells that are directly horizontally,
 * vertically, or diagonally adjacent. At each step in time, the following transitions (= genetic
 * rules) occur:
 *
 * <ol>
 * <li>Deaths. Every cell with none or one alive neighboring cells dies (is
 * removed) due to isolation for the next generation. Every cell with four or
 * more alive neighboring cells dies due to overcrowding for the next
 * generation.</li>
 * <li>Survivals. Every cell with two or three alive neighboring cells survives
 * (stays) for the next generation.</li>
 * <li>Births. Each dead cell adjacent to exactly three alive neighboring cells
 * - no more, no fewer - is born (added) for the next generation.</li>
 * </ol>
 *
 * <p>The initial pattern constitutes the 'seed' of the system. The first
 * generation is created by applying the above rules simultaneously to every
 * cell in the seed in which births and deaths happen simultaneously, and the
 * discrete moment at which this happens is sometimes called a tick. (In other
 * words, each generation is a pure function of the one before.) The rules
 * continue to be applied repeatedly to create further generations.
 */
public class Game implements Model {

  /**
   * Initial number of columns when the game is shown.
   */
  private static final int INITIAL_COLUMNS = 50;

  /**
   * Initial number of rows when the game is shown.
   */
  private static final int INITIAL_ROWS = 30;


  // Staying alive in this range
  private static final int STAY_ALIVE_MIN_NEIGHBORS = 2;
  private static final int STAY_ALIVE_MAX_NEIGHBORS = 3;

  // Condition for getting newly born
  private static final int NEWBORN_NEIGHBORS = 3;

  private final PropertyChangeSupport support;

  private int generation = 0;

  /*
   * Every cell on the grid is a Cell object. The first dimension are the rows, the second the
   * columns.
   *
   * <p>This object provides a convenient way of accessing cells in a quick manner. Note that it
   * does not store the state of a cell, i.e. whether it is dead or alive (this is done in the
   * population-collection).
   */
  private Cell[][] field;
  private final Map<Integer, List<Cell>> allNeighbors;
  private final Set<Cell> population;

  /**
   * Constructs a new game with a default size of {@link Game#INITIAL_COLUMNS} and  of
   * {@link Game#INITIAL_ROWS} consisting solely of dead cells.
   */
  public Game() {
    this(INITIAL_COLUMNS, INITIAL_ROWS);
  }

  /**
   * Constructs a new game consisting solely of dead cells.
   *
   * @param columns Number of columns.
   * @param rows    Number of rows.
   */
  public Game(int columns, int rows) {
    if (columns <= 0 || rows <= 0) {
      throw new IllegalArgumentException("Number of columns and rows must be positive");
    }

    support = new PropertyChangeSupport(this);

    this.field = new Cell[rows][columns];
    this.allNeighbors = new HashMap<>();
    this.population = new HashSet<>();
    initializeFields();
  }

  /**
   * Fills the empty instance-fields with a default values.
   */
  private synchronized void initializeFields() {
    for (int row = 0; row < field.length; row++) {
      for (int col = 0; col < field[0].length; col++) {
        field[row][col] = new Cell(col, row);  // each cell has unique coordinates
        allNeighbors.put(field[row][col].hashCode(), getNeighbors(field[row][col]));
      }
    }
  }

  /**
   * Get the cells that may change in the next generation.
   *
   * @return cells to recalculate
   */
  private Set<Cell> getCellsToRecalculate() {
    Set<Cell> cellsToRecalculate = new HashSet<>();
    for (Cell cell : population) {
      cellsToRecalculate.addAll(getNeighbors(cell));
    }
    cellsToRecalculate.addAll(population);
    return cellsToRecalculate;
  }

  /**
   * Calculates game state for the next generation.
   *
   * @param cellsToRecalculate cells that may change in the next generation
   */
  private void recalculateNext(Set<Cell> cellsToRecalculate) {
    Map<Integer, Integer> allAliveNeighbors = new HashMap<>();
    for (Cell cell : cellsToRecalculate) {
      int aliveNeighbors = countAliveNeighbors(cell);
      allAliveNeighbors.put(cell.hashCode(), aliveNeighbors);
    }
    for (Cell cell : cellsToRecalculate) {
      int aliveNeighbors = allAliveNeighbors.get(cell.hashCode());
      if (isCellAlive(cell.getColumn(), cell.getRow()) && (aliveNeighbors < STAY_ALIVE_MIN_NEIGHBORS
          || aliveNeighbors > STAY_ALIVE_MAX_NEIGHBORS)) {
        setCellDead(cell.getColumn(), cell.getRow());
      }
      if (!isCellAlive(cell.getColumn(), cell.getRow()) && aliveNeighbors == NEWBORN_NEIGHBORS) {
        setCellAlive(cell.getColumn(), cell.getRow());
      }
    }
  }

  /**
   * Count alive neighbors of the specified cell.
   *
   * @param cell the cell whose neighbors need to be counted
   * @throws IllegalArgumentException if number of columns and rows is negative or greater than the
   *                                  field size
   */
  private int countAliveNeighbors(Cell cell) throws IllegalArgumentException {
    if (cell.getColumn() >= this.getColumns() || cell.getRow() >= this.getRows()) {
      throw new IllegalArgumentException(
          "Parameters for column and row may not exceed the maximum number of columns and rows");
    }
    if (cell.getColumn() < 0 || cell.getRow() < 0) {
      throw new IllegalArgumentException("Number of column and row may not be negative");
    }
    List<Cell> neighbors = allNeighbors.get(cell.hashCode());
    int aliveNeighborsCounter = 0;
    if (neighbors == null) {
      return aliveNeighborsCounter;
    }
    for (Cell neighbor : neighbors) {
      if (isCellAlive(neighbor.getColumn(), neighbor.getRow())) {
        aliveNeighborsCounter++;
      }
    }
    return aliveNeighborsCounter;
  }

  /**
   * Get neighbors of the specified cell.
   *
   * @param cell the cell whose neighbors need to be determined
   * @return neighbors of the specified cell
   * @throws IllegalArgumentException if number of columns and rows greater than the field size
   */
  private ArrayList<Cell> getNeighbors(Cell cell) throws IllegalArgumentException {
    int row = cell.getRow();
    int column = cell.getColumn();
    if (column >= this.getColumns() || row >= this.getRows()) {
      throw new IllegalArgumentException(
          "Parameters for column and row may not exceed the maximum number of columns and rows");
    }
    ArrayList<Cell> neighbors = new ArrayList<>();
    if (column != 0) {
      neighbors.add(new Cell(column - 1, row));
      if (row != 0) {
        neighbors.add(new Cell(column - 1, row - 1));
        //neighbors.add(new Cell(column, row - 1));
      }
      if (row != this.getRows() - 1) {
        neighbors.add(new Cell(column - 1, row + 1));
      }
    }
    if (column != this.getColumns() - 1) {
      neighbors.add(new Cell(column + 1, row));
      if (row != 0) {
        neighbors.add(new Cell(column + 1, row - 1));
      }
      if (row != this.getRows() - 1) {
        neighbors.add(new Cell(column + 1, row + 1));
      }
    }
    if (row != 0) {
      neighbors.add(new Cell(column, row - 1));
    }
    if (row != this.getRows() - 1) {
      neighbors.add(new Cell(column, row + 1));
    }
    return neighbors;
  }

  @Override
  public synchronized boolean isCellAlive(int col, int row) throws IllegalArgumentException {
    if (col >= this.getColumns() || row >= this.getRows()) {
      throw new IllegalArgumentException(
          "Parameters for column and row may not exceed the maximum number of columns and rows");
    }
    if (col < 0 || row < 0) {
      throw new IllegalArgumentException("Number of column and row may not be negative");
    }
    return population.contains(field[row][col]);
  }

  @Override
  public void setCellAlive(int col, int row) throws IllegalArgumentException {
    setCellWithoutNotification(col, row, true);
    notifyListeners();
  }

  @Override
  public void setCellDead(int col, int row) throws IllegalArgumentException {
    setCellWithoutNotification(col, row, false);
    notifyListeners();
  }

  /**
   * Put a cell into either a living or dead state.
   *
   * <p>This method does not make a call to {@link #notifyListeners()}, use the methods
   * {@linkplain Game#setCellAlive(int, int) setCellAlive(int, int)} or
   * {@linkplain Game#setCellDead(int, int) setCellDead(int, int)}instead</p>
   *
   * @param col   The column of the cell
   * @param row   The row of the cell
   * @param alive if <code>true</code>, the cell is set alive; otherwise it is put into its dead
   *              state
   */
  private synchronized void setCellWithoutNotification(int col, int row, boolean alive) {
    if (col < 0 || row < 0) {
      throw new IllegalArgumentException("Number of column and row may not be negative");
    } else if (col >= this.getColumns() || row >= this.getRows()) {
      throw new IllegalArgumentException("Parameters for column and row may not exceed "
          + "the maximum number of columns and rows");
    }
    Cell cell = new Cell(col, row);
    if (alive) {
      population.add(cell); // no addition if already present
    } else {
      population.remove(cell); // no removal if cell not present
    }
  }

  @Override
  public synchronized void resize(int newCols, int newRows) {
    int oldCols = getColumns();
    int oldRows = getRows();

    if (oldCols == newCols && oldRows == newRows) {
      return; // nothing to do
    }

    allNeighbors.clear();
    if (newRows < oldRows) {
      for (int row = newRows; row < oldRows; row++) {
        for (int col = 0; col < oldCols; col++) {
          population.remove(field[row][col]);
        }
      }
    }
    if (newCols < oldCols) {
      for (int row = 0; row < oldRows; row++) {
        for (int col = newCols; col < oldCols; col++) {
          population.remove(field[row][col]);
        }
      }
    }

    field = new Cell[newRows][newCols];
    for (int row = 0; row < newRows; row++) {
      for (int col = 0; col < newCols; col++) {
        field[row][col] = new Cell(col, row);
        allNeighbors.put(field[row][col].hashCode(), getNeighbors(field[row][col]));
      }
    }
    notifyListeners();
  }

  @Override
  public synchronized int getRows() {
    return field.length;
  }

  @Override
  public synchronized int getColumns() {
    return field[0].length;
  }

  @Override
  public synchronized Collection<Cell> getPopulation() {
    return new HashSet<>(population);
  }

  @Override
  public synchronized void clear() {
    this.generation = 0;
    population.clear();

    notifyListeners();
  }

  @Override
  public synchronized void next() {
    Set<Cell> cellsToRecalculate = getCellsToRecalculate();
    recalculateNext(cellsToRecalculate);
    generation++;
    notifyListeners();
  }

  @Override
  public synchronized int getGenerations() {
    return generation;
  }

  @Override
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    for (int row = 0; row < getRows(); row++) {
      for (int col = 0; col < getColumns(); col++) {
        if (isCellAlive(col, row)) {
          stringBuilder.append("X");
        } else {
          stringBuilder.append(".");
        }
      }
      stringBuilder.append("\n");
    }
    stringBuilder.deleteCharAt(stringBuilder.length() - 1);
    return stringBuilder.toString();
  }

  @Override
  public void addPropertyChangeListener(PropertyChangeListener pcl) {
    requireNonNull(pcl);
    support.addPropertyChangeListener(pcl);
  }

  @Override
  public void removePropertyChangeListener(PropertyChangeListener pcl) {
    requireNonNull(pcl);
    support.removePropertyChangeListener(pcl);
  }

  /**
   * Invokes the model to fire a new event, such that any attached observer (i.e.,
   * {@link PropertyChangeListener}) gets notified about a change in this model.
   */
  private void notifyListeners() {
    support.firePropertyChange(STATE_CHANGED, null, this);
  }
}


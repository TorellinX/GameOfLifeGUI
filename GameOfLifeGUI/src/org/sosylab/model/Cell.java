package org.sosylab.model;


/**
 * A two-dimensional coordinate within a cell grid.
 */
public class Cell {

  /**
   * The x-coordinate of the cel.
   */
  private final int column;

  /**
   * The y-coordinate of the cell.
   */
  private final int row;

  /**
   * Constructs a new cell.
   *
   * @param column The x-coordinate (column of cell).
   * @param row    The y-coordinate (row of cell).
   */
  public Cell(int column, int row) {
    if (column < 0 || row < 0) {
      throw new IllegalArgumentException("Cell must not have negative coordinates");
    }

    this.column = column;
    this.row = row;
  }

  /**
   * Get the x-coordinate of a cell.
   *
   * @return The x-coordinate.
   */
  public int getColumn() {
    return column;
  }

  /**
   * Get the y-coordinate of a cell.
   *
   * @return The y-coordinate.
   */
  public int getRow() {
    return row;
  }

  @Override
  public int hashCode() {
    int hashfactor = 5000;
    return hashfactor * row + column;
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }
    if (other == null || getClass() != other.getClass()) {
      return false;
    }
    Cell cell = (Cell) other;
    return column == cell.column && row == cell.row;
  }

  @Override
  public String toString() {
    return String.format("<%s, %s>", column, row);
  }

}


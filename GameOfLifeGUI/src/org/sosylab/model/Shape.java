package org.sosylab.model;

import java.util.HashSet;
import java.util.Set;

/**
 * Contains data of a predefined shape.
 */
public class Shape {

  private final String name;
  private final Set<Cell> shapePopulation;
  private final int columns;
  private final int rows;

  /**
   * Create a shape.
   *
   * @param name  the name of the shape
   * @param shape layout for the shape
   */
  public Shape(String name, int[][] shape) {
    this.name = name;
    this.columns = shape[0].length;
    this.rows = shape.length;
    this.shapePopulation = createShapePopulation(shape);
  }

  /**
   * Create a set of all living cells of the shape from the initial layout.
   *
   * @param shape initial layout of the shape
   * @return Set of all cells which are alive.
   */
  private static Set<Cell> createShapePopulation(int[][] shape) {
    Set<Cell> shapePopulation = new HashSet<>();
    for (int row = 0; row < shape.length; row++) {
      for (int column = 0; column < shape[0].length; column++) {
        if (shape[row][column] == 1) {
          shapePopulation.add(new Cell(column, row));
        }
      }
    }
    return shapePopulation;
  }

  /**
   * Gets the dimension of the shape in x direction.
   *
   * @return number of columns.
   */
  public int getColumns() {
    return columns;
  }

  /**
   * Gets the dimension of the shape in y direction.
   *
   * @return number of rows.
   */
  public int getRows() {
    return rows;
  }

  /**
   * Get the name of the shape.
   *
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * Get the set of all living cells of the shape.
   *
   * @return the alive cells of the shape
   */
  public Set<Cell> getShapePopulation() {
    return new HashSet<>(shapePopulation);
  }
}

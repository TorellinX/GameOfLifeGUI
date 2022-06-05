package org.sosylab.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.Serial;
import javax.swing.JPanel;
import org.sosylab.model.Model;

/**
 * The draw board displays the grid of cells of the game in current state. The state of cells is
 * indicated by color. The color {@link #dead} means a dead cell. The color {@link #alive} means a
 * living cell.
 */
public class DrawBoard extends JPanel {

  @Serial
  private static final long serialVersionUID = 1L;

  private static final int BIG_SIZE = 30;
  private static final int MEDIUM_SIZE = 20;
  private static final int SMALL_SIZE = 10;
  static final int BORDER_SIZE = 1;
  private static final Color background = Color.LIGHT_GRAY;
  private static final Color dead = Color.GRAY;
  private static final Color alive = Color.CYAN;

  private int cellSize;
  private int columns;
  private int rows;
  private final Model model;
  private final Controller controller;
  boolean isToSetAlive;

  /**
   * Constructs a new draw board on the window.
   *
   * @param gameView a window for the draw board.
   */
  public DrawBoard(GameOfLifeView gameView) {
    this.model = gameView.getModel();
    this.controller = gameView.getController();
    cellSize = BIG_SIZE;  // default
    this.columns = model.getColumns();
    this.rows = model.getRows();
    createDrawEventListeners();
    adjustPreferredSize();
  }


  /**
   * Adjusts preferred size of the draw board by the current cell size and current numbers of
   * columns and rows.
   */
  void adjustPreferredSize() {
    this.setPreferredSize(new Dimension(BORDER_SIZE + (cellSize + BORDER_SIZE) * columns,
        BORDER_SIZE + (cellSize + BORDER_SIZE) * rows));
  }

  @Override
  protected void paintComponent(Graphics g) {
    g.setColor(background);
    g.fillRect(0, 0, getWidth(), getHeight());

    // don't know, how to fix that repaint() is automatically called by thread before the state of
    // the draw board is updated by handleChangeEvent(PropertyChangeEvent event), what causes
    // unupdated numbers of columns and rows at repaint(), so I wrote this additional forced update
    this.columns = model.getColumns();
    this.rows = model.getRows();

    int positionX;
    int positionY = BORDER_SIZE;
    for (int row = 0; row < this.rows; row++) {
      positionX = BORDER_SIZE;
      for (int col = 0; col < this.columns; col++) {
        if (this.model.isCellAlive(col, row)) {
          g.setColor(alive);
        } else {
          g.setColor(dead);
        }
        g.fillRect(positionX, positionY, cellSize, cellSize);
        positionX = positionX + cellSize + BORDER_SIZE;
      }
      positionY = positionY + (cellSize + BORDER_SIZE);
    }
  }

  /**
   * Gets the current size of cells on the draw board.
   *
   * @return current size of cells
   */
  int getCellSize() {
    return this.cellSize;
  }


  /**
   * Sets the size of cells on the draw board.
   *
   * @param size size of cells
   * @throws IllegalArgumentException if the name of cell size is not recognized
   */
  void setCellSize(String size) {
    switch (size) {
      case "big" -> this.cellSize = BIG_SIZE;
      case "medium" -> this.cellSize = MEDIUM_SIZE;
      case "small" -> this.cellSize = SMALL_SIZE;
      default -> throw new IllegalArgumentException("The cell size is not recognized");
    }
  }

  /**
   * Sets the number of columns of the draw board.
   *
   * @param columns numbers of columns
   */
  void setColumns(int columns) {
    if (columns <= 0) {
      throw new IllegalArgumentException("Number of columns must be positive");
    }
    this.columns = columns;
  }

  /**
   * Sets the number of rows of the draw board.
   *
   * @param rows numbers of rows
   */
  void setRows(int rows) {
    if (columns <= 0) {
      throw new IllegalArgumentException("Number of rows must be positive");
    }
    this.rows = rows;
  }

  /**
   * Creates EventListeners for the draw panel.
   */
  private void createDrawEventListeners() {
    addMouseListener(new MouseAdapter() {
      @Override
      public void mousePressed(MouseEvent e) {
        Point position = e.getPoint();
        int col = calculateColsByX(position.x);
        int row = calculateRowsByY(position.y);
        if (col >= model.getColumns() || row >= model.getRows()) {
          return;
        }
        if (col < 0 || row < 0) {
          throw new IllegalArgumentException("Number of column and row may not be negative");
        }
        isToSetAlive = !model.isCellAlive(col, row);
        controller.setCellAlive(col, row, isToSetAlive);
      }
    });

    addMouseMotionListener(new MouseMotionAdapter() {
      @Override
      public void mouseDragged(MouseEvent e) {
        Point position = e.getPoint();
        int col = calculateColsByX(position.x);
        int row = calculateRowsByY(position.y);
        if (col >= model.getColumns() || row >= model.getRows()) {
          return;
        }
        if (col < 0 || row < 0) {
          throw new IllegalArgumentException("Number of column and row may not be negative");
        }
        controller.setCellAlive(col, row, isToSetAlive);
      }
    });
  }

  /**
   * Calculates the number of the column, which has the specified x-coordinate on the screen.
   *
   * @param x x-position on the screen
   * @return Number of the column
   */
  int calculateColsByX(int x) {
    return (int) Math.ceil(x - DrawBoard.BORDER_SIZE + 0.0) / (getCellSize()
        + DrawBoard.BORDER_SIZE);
  }

  /**
   * Calculates the number of the row, which has the specified y-coordinate on the screen.
   *
   * @param y y-position on the screen
   * @return Number of the row
   */
  int calculateRowsByY(int y) {
    return (int) Math.ceil(y - DrawBoard.BORDER_SIZE - getInsets().top + 0.0) / (getCellSize()
        + DrawBoard.BORDER_SIZE);
  }
}

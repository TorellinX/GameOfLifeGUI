package org.sosylab.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.Serial;
import javax.swing.JButton;
import javax.swing.JPanel;
import org.sosylab.model.Model;

// TODO: add documentation
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

  private Point position;
  private int columns;
  private int rows;
  private final Model model;


  // TODO insert code here

  // TODO: add documentation
  public DrawBoard(Model model) {
    this.model = model;
    cellSize = BIG_SIZE;  // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    position = new Point(1, 1);
    this.columns = model.getColumns();
    this.rows = model.getRows();
    adjustPrefferedSize();
  }


  void adjustPrefferedSize(){
    this.setPreferredSize(new Dimension(BORDER_SIZE + (cellSize + BORDER_SIZE)*columns,BORDER_SIZE + (cellSize + BORDER_SIZE)*rows));

  }
  void adjustPrefferedSize(int columns, int rows){
    this.setPreferredSize(new Dimension(BORDER_SIZE + (cellSize + BORDER_SIZE)*columns,BORDER_SIZE + (cellSize + BORDER_SIZE)*rows));
  }


  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    Graphics2D g2D = (Graphics2D) g;

    g.setColor(background);
    g.fillRect(0, 0, getWidth(), getHeight());

    //int xPos = position.x;
    //int yPos = position.y;

//    if (this.columns > model.getColumns() || this.rows > model.getRows()) {
//      this.columns = model.getColumns();
//      this.rows = model.getRows();
//      System.out.println("DrawBoard cols and rows numbers was unupdated. Cols: " + this.columns +
//           " > " + model.getColumns() + ", rows: " + this.rows + " > " + model.getRows());
//    }
    int dCols = this.columns;
    int dRows = this.rows;
    int mCols = model.getColumns();
    int mRows = model.getRows();
    if (dCols > mCols || dRows > mRows) {
      this.columns = model.getColumns();
      this.rows = model.getRows();
      /*System.out.println("DrawBoard cols and rows numbers was unupdated. Cols: " + dCols +
          " > " + mCols + ", rows: " + dRows + " > " + mRows);
      System.out.println("It would seem that Cols: " + this.columns +
           " > " + model.getColumns() + ", rows: " + this.rows + " > " + model.getRows());*/
    }

    int yPos;
    int xPos = BORDER_SIZE;
    //System.out.println("Repaint! Drawboard Cols: " + this.columns + ", rows: " + this.rows);
    for (int row = 0; row < this.rows; row++) {
      yPos = BORDER_SIZE;
      for (int col = 0; col < this.columns; col++) {
        if(this.model.isCellAlive(col, row)){
          g.setColor(alive);
        } else {
          g.setColor(dead);
        }
        g.fillRect(yPos, xPos, cellSize, cellSize);
        yPos = yPos + cellSize + BORDER_SIZE;


      }
      xPos = xPos + (cellSize + BORDER_SIZE);
    }

//    addComponentListener(new ComponentAdapter() {
//      @Override
//      public void componentResized(ComponentEvent e) {
//        System.out.println("Resized!");
//      }
//    });

  }

  int getCellSize(){
    return this.cellSize;
  }


  void setCellSize(String size){

    switch (size) {
      case "big":
        this.cellSize = BIG_SIZE;
        break;
      case "medium":
        this.cellSize = MEDIUM_SIZE;
        break;
      case "small":
        this.cellSize = SMALL_SIZE;
        break;
      default:
        throw new IllegalArgumentException("The cell size is not recognized");
    }

  }

  void setColumns(int columns){
    if(columns <= 0 ) {
      throw new IllegalArgumentException("Number of columns must be positive");
    }
    this.columns = columns;
  }

  void setRows(int rows){
    if(columns <= 0 ) {
      throw new IllegalArgumentException("Number of rows must be positive");
    }
    this.rows = rows;
  }


}

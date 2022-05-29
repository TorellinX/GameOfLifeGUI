package org.sosylab.view;

import static java.util.Objects.requireNonNull;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Point;
import java.awt.event.MouseMotionAdapter;
import java.beans.PropertyChangeEvent;
import java.io.Serial;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.sosylab.model.Model;
import org.sosylab.model.Shapes;

// TODO: add documentation
public class GameOfLifeView extends JFrame implements View {

  @Serial
  private static final long serialVersionUID = 1L;

  private final Model model;
  private final Controller controller;

  private final DrawBoard drawBoard;
  private final JPanel controlBoard;
  private final JComboBox<String> shapes;
  private final JButton nextButton;

  private final JButton startButton;
  private final JButton clearButton;
  private final JSlider speed;
  private final JComboBox size;
  private final JLabel generation;
  private Dimension screenSize;









  // TODO insert code here

  // TODO add documentation
  public GameOfLifeView(Model model, Controller controller) {
    super("Game of Life");

    this.model = requireNonNull(model);
    this.controller = requireNonNull(controller);

    drawBoard = new DrawBoard(model);
    controlBoard = new JPanel();
    shapes = new JComboBox<String>(Shapes.getAvailableShapes());
    nextButton = new JButton("Next");
    startButton = new JButton("Start");
    clearButton = new JButton("Clear");
    speed = new JSlider(JSlider.HORIZONTAL, 1, 30, 1);
    size = new JComboBox<String>(new String[]{"big", "medium", "small"});
    generation = new JLabel("Generation: " + model.getGenerations());
    createContent();
    createControlBoardContent();
    createEventListeners();
    this.pack();
    Toolkit toolkit = Toolkit.getDefaultToolkit();
    screenSize = toolkit.getScreenSize();
    this.setLocation((screenSize.width - this.getWidth())/2,(screenSize.height - this.getHeight())/2);

  }

  private void createContent(){
    this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    this.setLayout(new BorderLayout());
    Container c = this.getContentPane();
    c.add(drawBoard, BorderLayout.CENTER);
    c.add(controlBoard, BorderLayout.SOUTH);
    controlBoard.setLayout(new FlowLayout());
  }

  private void createControlBoardContent(){
    controlBoard.add(shapes);
    controlBoard.add(nextButton);
    controlBoard.add(startButton);  // auch Stop !!!!!!!!!!!!!!!!!!!!!!!!
    controlBoard.add(clearButton);
    controlBoard.add(new JLabel("Speed:"));
    controlBoard.add(speed);
    speed.setPreferredSize(new Dimension(100, 25));
    controlBoard.add(size);
    controlBoard.add(generation);

  }

  private void createEventListeners() {
    shapes.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        JComboBox box = (JComboBox) e.getSource();
        controller.setShape(Shapes.getShapeByName(box.getSelectedItem().toString().toLowerCase()));
      }
    });

    nextButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        controller.step();
      }
    });

    startButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if(startButton.getText().equals("Start")) {
          controller.stepIndefinitely();
        }
        if(startButton.getText().equals("Stop")) {
          controller.stopStepping();
        }
      }
    });

    clearButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        controller.stopStepping();
        controller.clearBoard();
      }
    });

    speed.addChangeListener(new ChangeListener() {
      @Override
      public void stateChanged(ChangeEvent e) {
        controller.setStepSpeed(speed.getValue());
      }
    });

    size.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        JComboBox box = (JComboBox) e.getSource();
        String size = box.getSelectedItem().toString().toLowerCase();
        drawBoard.setCellSize(size);
        if(getHeight() > screenSize.height || getWidth() > screenSize.width) {
          int cols = (screenSize.height - DrawBoard.BORDER_SIZE)/(drawBoard.getCellSize() + DrawBoard.BORDER_SIZE);
          int rows = (screenSize.width - controlBoard.getHeight() - getInsets().top - DrawBoard.BORDER_SIZE)/(drawBoard.getCellSize() + DrawBoard.BORDER_SIZE);
          controller.resize(cols, rows);
        }
        repack();
      }
    });

    addComponentListener(new ComponentAdapter() {
      @Override
      public void componentResized(ComponentEvent e) {
        int cols = (getWidth() - DrawBoard.BORDER_SIZE)/(drawBoard.getCellSize() + DrawBoard.BORDER_SIZE);
        int rows = (getHeight() - controlBoard.getHeight() - getInsets().top - DrawBoard.BORDER_SIZE)/(drawBoard.getCellSize() + DrawBoard.BORDER_SIZE);
        if(cols == model.getColumns() && rows == model.getRows()){
          return;
        }
        if(cols <= 0 || rows <= 0) {
          return;
        }
        controller.resize(cols, rows);
        System.out.println("Controller.resize done. Cols: " + cols + ", rows: " + rows );
        System.out.println();
      }
    });

    final Boolean drawAlive;

    addMouseListener(new MouseAdapter() {
/*      @Override
      public void mouseClicked(MouseEvent e) {
        Point position = e.getPoint();
        System.out.println(position);
        int col = calculateColsByX(position.x);
        int row = calculateRowsByY(position.y);
        if (model.isCellAlive(col, row)) {
          model.setCellDead(col, row);
        } else {
          model.setCellAlive(col, row);
        }
      }*/

      @Override
      public void mousePressed(MouseEvent e) {
        Point position = e.getPoint();
        System.out.println("Press " + position);
        int col = calculateColsByX(position.x);
        int row = calculateRowsByY(position.y);
        if (model.isCellAlive(col, row)) {
          //drawAlive = false;
          model.setCellDead(col, row);
        } else {
          //drawAlive = true;
          model.setCellAlive(col, row);
        }
      }

      @Override
      public void mouseReleased(MouseEvent e) {
        super.mouseReleased(e);
      }
    });

    addMouseMotionListener(new MouseMotionAdapter() {
      @Override
      public void mouseDragged(MouseEvent e) {
        Point position = e.getPoint();
        System.out.println(position);
      }
    });

  }

  int calculateColsByX(int x) {
    return (int) Math.ceil(x - DrawBoard.BORDER_SIZE + 0.0) / (drawBoard.getCellSize() + DrawBoard.BORDER_SIZE);
  }

  int calculateRowsByY(int y) {
    return (int) Math.ceil(y - DrawBoard.BORDER_SIZE - getInsets().top + 0.0) / (drawBoard.getCellSize() + DrawBoard.BORDER_SIZE);
  }


  void repaintWindow(){
    drawBoard.repaint();
    generation.setText("Generation: " + model.getGenerations());
  }

  private void repack(){
    drawBoard.adjustPrefferedSize();
    this.pack();
    repaintWindow(); //?????????????????????????
  }

  private void repack(int cols, int rows){
    drawBoard.adjustPrefferedSize(cols, rows);
    this.pack();
    repaintWindow(); //?????????????????????????
  }

  @Override
  public void showGame() {
    // TODO insert code here
    //drawBoard.repaint();
    this.setVisible(true);
  }

  @Override
  public void showErrorMessage(String message) {
    // TODO insert code here
  }

  @Override
  public void startStepping() {
    startButton.setText("Stop");
    shapes.setEnabled(false);
    nextButton.setEnabled(false);
  }

  @Override
  public void stopStepping() {
    startButton.setText("Start");
    shapes.setEnabled(true);
    nextButton.setEnabled(true);

  }

  @Override
  public void dispose() {
    //drawBoard.dispose();   // < zuert in DrawBoard
    controller.dispose();
    super.dispose();
  }

  @Override
  public void propertyChange(PropertyChangeEvent event) {
    SwingUtilities.invokeLater(() -> handleChangeEvent(event));
  }

  /**
   * The observable (= model) has just published that its state has changed. The GUI needs to be
   * updated accordingly here.
   *
   * @param event The event that was fired by the model.
   */
  private void handleChangeEvent(PropertyChangeEvent event) {

    // the next lines are for demonstration purposes
    if (event.getPropertyName().equals(Model.STATE_CHANGED)) {
      System.out.println("Model has changed its state.");
      drawBoard.setColumns(model.getColumns());
      drawBoard.setRows(model.getRows());
      repaintWindow(); // ??????????????
    }

    // TODO insert code here
  }

  public static void displayError(String message){
    JOptionPane.showMessageDialog(null, message, "Uh-oh!", JOptionPane.INFORMATION_MESSAGE);
  }


}

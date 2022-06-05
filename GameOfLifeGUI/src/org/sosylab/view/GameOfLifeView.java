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

/**
 * Implements the main of the view for Game Of Life. The state it displays is directly taken from
 * the {@link org.sosylab.model.Model}.
 */
public class GameOfLifeView extends JFrame implements View {

  @Serial
  private static final long serialVersionUID = 1L;

  private final Model model;
  private final Controller controller;

  private final DrawBoard drawBoard;
  private final JPanel controlBoard;

  // The elements of the control board:
  private final JComboBox<String> shapes;
  private final JButton nextButton;
  private final JButton startButton;
  private final JButton clearButton;
  private final JSlider speed;
  private final JComboBox<String> size;
  private final JLabel generation;

  private static Dimension screenSize;
  private static final int MIN_SPEED = 1;
  private static final int MAX_SPEED = 30;

  /**
   * Constructs a new view of game.
   *
   * @param model      a model instance of the game.
   * @param controller a controller instance of the game.
   */
  public GameOfLifeView(Model model, Controller controller) {
    super("Game of Life");

    this.model = requireNonNull(model);
    this.controller = requireNonNull(controller);

    drawBoard = new DrawBoard(this);
    controlBoard = new JPanel();

    shapes = new JComboBox<>(Shapes.getAvailableShapes());
    nextButton = new JButton("Next");
    startButton = new JButton("Start");
    clearButton = new JButton("Clear");
    speed = new JSlider(JSlider.HORIZONTAL, GameOfLifeController.MIN_SPEED,
        GameOfLifeController.MAX_SPEED, 1);
    size = new JComboBox<>(new String[]{"big", "medium", "small"});
    generation = new JLabel("Generation: " + model.getGenerations());
    this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    createContent();
    createControlBoardContent();
    createControlEventListeners();
    this.pack();
    setFrameLocationToScreenCenter();
  }

  /**
   * Creates a draw panel and a control panel on the main window.
   */
  private void createContent() {
    this.setLayout(new BorderLayout());
    Container c = this.getContentPane();
    c.add(drawBoard, BorderLayout.CENTER);
    c.add(controlBoard, BorderLayout.SOUTH);
  }

  /**
   * Creates game controls on the control panel.
   */
  private void createControlBoardContent() {
    controlBoard.setLayout(new FlowLayout());
    controlBoard.add(shapes);
    controlBoard.add(nextButton);
    controlBoard.add(startButton);
    controlBoard.add(clearButton);
    controlBoard.add(new JLabel("Speed:"));
    controlBoard.add(speed);
    speed.setPreferredSize(new Dimension(100, 25));
    controlBoard.add(size);
    controlBoard.add(generation);
  }

  /**
   * Creates EventListeners for elements on the control panel.
   */
  private void createControlEventListeners() {
    shapes.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        JComboBox shapesBox = (JComboBox) e.getSource();
        controller.setShape(Shapes.getShapeByName(
            requireNonNull(shapesBox.getSelectedItem()).toString().toLowerCase()));
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
        if (startButton.getText().equals("Start")) {
          controller.stepIndefinitely();
        }
        if (startButton.getText().equals("Stop")) {
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
        JComboBox sizeBox = (JComboBox) e.getSource();
        String size = requireNonNull(sizeBox.getSelectedItem()).toString().toLowerCase();
        drawBoard.setCellSize(size);
        if (getHeight() > screenSize.height || getWidth() > screenSize.width) {
          int cols = (screenSize.height - DrawBoard.BORDER_SIZE) / (drawBoard.getCellSize()
              + DrawBoard.BORDER_SIZE);
          int rows = (screenSize.width - controlBoard.getHeight() - getInsets().top
              - DrawBoard.BORDER_SIZE) / (drawBoard.getCellSize() + DrawBoard.BORDER_SIZE);
          controller.resize(cols, rows);
        }
        repackWindow();
      }
    });

    addComponentListener(new ComponentAdapter() {
      @Override
      public void componentResized(ComponentEvent e) {
        int cols = (getWidth() - DrawBoard.BORDER_SIZE) / (drawBoard.getCellSize()
            + DrawBoard.BORDER_SIZE);
        int rows =
            (getHeight() - controlBoard.getHeight() - getInsets().top - DrawBoard.BORDER_SIZE) / (
                drawBoard.getCellSize() + DrawBoard.BORDER_SIZE);
        if (cols == model.getColumns() && rows == model.getRows()) {
          return;
        }
        if (cols <= 0 || rows <= 0) {
          return;
        }
        controller.resize(cols, rows);
      }
    });
  }

  /**
   * Places the window in the middle of the screen.
   */
  private void setFrameLocationToScreenCenter() {
    Toolkit toolkit = Toolkit.getDefaultToolkit();
    screenSize = toolkit.getScreenSize();
    this.setLocation((screenSize.width - this.getWidth()) / 2,
        (screenSize.height - this.getHeight()) / 2);
  }

  /**
   * Packs Frame around adjusted DrawBoard.
   */
  private void repackWindow() {
    drawBoard.adjustPreferredSize();
    this.pack();
  }

  @Override
  public void showGame() {
    this.setVisible(true);
  }

  @Override
  public void showErrorMessage(String message) {
    JOptionPane.showMessageDialog(null, message, "Uh-oh!", JOptionPane.INFORMATION_MESSAGE);
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
    if (event.getPropertyName().equals(Model.STATE_CHANGED)) {
      drawBoard.setColumns(model.getColumns());
      drawBoard.setRows(model.getRows());
      repaintWindow();
    }
  }

  /**
   * Updates the content of the window.
   */
  private void repaintWindow() {
    drawBoard.repaint();
    generation.setText("Generation: " + model.getGenerations());
  }

  /**
   * Gets the controller instance of the game.
   *
   * @return controller
   */
  protected Controller getController() {
    return this.controller;
  }

  /**
   * Gets the model instance of the game.
   *
   * @return model
   */
  protected Model getModel() {
    return this.model;
  }
}

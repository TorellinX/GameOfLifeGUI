package org.sosylab.view;

import static java.util.Objects.requireNonNull;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.io.Serial;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;
import org.sosylab.model.Game;
import org.sosylab.model.Model;
import org.sosylab.model.Shapes;
import org.w3c.dom.events.Event;

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






  // TODO insert code here

  // TODO add documentation
  public GameOfLifeView(Model model, Controller controller) {
    super("Game of Life");

    this.model = requireNonNull(model);
    this.controller = requireNonNull(controller);

    drawBoard = new DrawBoard(model);
    controlBoard = new JPanel();
    nextButton = new JButton("Next");
    shapes = new JComboBox<String>(Shapes.getAvailableShapes());
    startButton = new JButton("Start");
    clearButton = new JButton("Clear");
    speed = new JSlider(JSlider.HORIZONTAL, 1, 100, 1);
    size = new JComboBox<String>(new String[]{"big", "medium", "small"});
    generation = new JLabel("Generation: " + model.getGenerations());

    createContent();

    this.pack();

    Toolkit toolkit = Toolkit.getDefaultToolkit();
    Dimension dimension = toolkit.getScreenSize();
    this.setLocation((dimension.width - this.getWidth())/2,(dimension.height - this.getHeight())/2);
    // TODO insert code here
  }

  // TODO insert code here
  private void createContent(){
    this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    this.setLayout(new BorderLayout());


    this.add(drawBoard, BorderLayout.CENTER);
    this.add(controlBoard, BorderLayout.SOUTH);
    controlBoard.setLayout(new FlowLayout());
    createControlBoardContent();
  }

  private void createControlBoardContent(){
    controlBoard.add(shapes);
    controlBoard.add(nextButton);
    controlBoard.add(startButton);  // auch Stop !!!!!!!!!!!!!!!!!!!!!!!!
    controlBoard.add(clearButton);
    controlBoard.add(new JLabel("Speed:"));
    controlBoard.add(speed);
    controlBoard.add(size);
    controlBoard.add(generation);

    shapes.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        JComboBox box = (JComboBox) e.getSource();
        controller.setShape(Shapes.getShapeByName(box.getSelectedItem().toString().toLowerCase()));
        repaintWindow();
      }
    });

    nextButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        controller.step();
          repaintWindow();

        // ^^^ ???????????????????????????????   propertyChanged?????
      }
    });




    clearButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        controller.clearBoard();
        repaintWindow();
      }
    });
  }

  void repaintWindow(){
    drawBoard.repaint();
    generation.setText("Generation: " + model.getGenerations());
  }



  @Override
  public void showGame() {
    // TODO insert code here
    drawBoard.repaint();
    this.setVisible(true);
  }

  @Override
  public void showErrorMessage(String message) {
    // TODO insert code here
  }

  @Override
  public void startStepping() {
    // TODO insert code here
  }

  @Override
  public void stopStepping() {
    // TODO insert code here
  }

  @Override
  public void dispose() {
    // drawBoard.dispose();   // < zuert in DrawBoard
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
    }

    // TODO insert code here
    //repaintWindow(); // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!????????------
  }


}

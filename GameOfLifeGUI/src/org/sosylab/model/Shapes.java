package org.sosylab.model;

/**
 * Some Game Of Life shapes.
 */

public class Shapes {

  private static final Shape BLOCK;
  private static final Shape BOAT;
  private static final Shape BLINKER;
  private static final Shape TOAD;
  private static final Shape GLIDER;
  private static final Shape SPACESHIP;
  private static final Shape PULSAR;

  private static final Shape[] SHAPES;

  private static final String[] availableShapes;

  static {
    BLOCK = new Shape("block", new int[][]{{1, 1}, {1, 1}});
    BOAT = new Shape("boat", new int[][]{{1, 1, 0}, {1, 0, 1}, {0, 1, 0}});
    BLINKER = new Shape("blinker", new int[][]{{1, 1, 1}});
    TOAD = new Shape("toad", new int[][]{{0, 1, 1, 1}, {1, 1, 1, 0}});
    GLIDER = new Shape("glider", new int[][]{{1, 1, 1}, {1, 0, 0}, {0, 1, 0}});
    SPACESHIP = new Shape("spaceship",
        new int[][]{{0, 1, 0, 0, 1}, {1, 0, 0, 0, 0}, {1, 0, 0, 0, 1}, {1, 1, 1, 1, 0}});
    PULSAR = new Shape("pulsar", new int[][]{{0, 0, 1, 1, 0, 0, 0, 0, 0, 1, 1, 0, 0},
        {0, 0, 0, 1, 1, 0, 0, 0, 1, 1, 0, 0, 0},
        {1, 0, 0, 1, 0, 1, 0, 1, 0, 1, 0, 0, 1},
        {1, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 1},
        {0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0},
        {0, 0, 1, 1, 1, 0, 0, 0, 1, 1, 1, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 1, 1, 1, 0, 0, 0, 1, 1, 1, 0, 0},
        {0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0},
        {1, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 1},
        {1, 0, 0, 1, 0, 1, 0, 1, 0, 1, 0, 0, 1},
        {0, 0, 0, 1, 1, 0, 0, 0, 1, 1, 0, 0, 0},
        {0, 0, 1, 1, 0, 0, 0, 0, 0, 1, 1, 0, 0}});

    SHAPES = new Shape[]{BLOCK, BOAT, BLINKER, TOAD, GLIDER, SPACESHIP, PULSAR};

    availableShapes = checkAvailableShapes();
  }

  /**
   * Get a shape by its name.
   *
   * @param name the name of the shape
   * @return selected shape
   */
  public static Shape getShapeByName(String name) {
    for (Shape shape : SHAPES) {
      if (shape.getName().equals(name)) {
        return shape;
      }
    }
    return null;
  }

  private static String[] checkAvailableShapes(){
    String[] availableShapes = new String[SHAPES.length];
    for (int i = 0; i < SHAPES.length; i++) {
      availableShapes[i] = SHAPES[i].getName().substring(0,1).toUpperCase() + SHAPES[i].getName().substring(1);
    }
    return availableShapes;
  }

  /**
   * Get a list of available shapes to shown when an incorrect argument was entered for the command
   * SHAPE.
   *
   * @return a string with a list of available shapes
   */
  public static String[] getAvailableShapes() {
    return availableShapes;
  }
}

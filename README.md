# Game of Life GUI

The task is to realize a graphical user interface for the Game of Life model using Java Swing framework. 
The implementation of the GUI must follow the Model-View-Controller concept.

## The GUI and User Interaction
The only requirement for it is to have all the functionalities implemented that are written below.

The GUI must consist of a game field and several interaction components. By mouse-clicking into the game field, a user shall be able to to change the state of a cell. If the user keeps on holding down the mouse, the current mode is retained, such that when the mouse gets moved, all cells that are moved over are respectively set either alive or dead (depending on the retaining mode). 

Additionally, the following components must be available on the GUI:

- 2 Buttons: NEXT and START/STOP.
- A selection option for new shapes.
- A selection option for at least two worker thread speeds.
- A display showing the label 'generation' including the respective counter.
- A selection for at least two different cell sizes. You can use various elements such as sliders.
- A CLEAR option, resetting the generation counter back to 0 and removing all of the living cells from the board.

Both the execution of a single generation step and running the generations endlessly should be done in a separate worker thread. This can be done using a `SwingWorker` object. The worker thread is started when a user clicks the START button, and stops when the STOP button is clicked. While the thread is running, it should not be possible for a user to load new shapes or to click the NEXT button. It should however still be possible during that time to turn cells either dead or alive when the users clicks or draws on the board.

 

## Further Notes on the Implementation
There are two templates for this task, one that includes an implemented model, and one in which the model only consists of the files that were handed out for the second task. The latter can be used to reuse your own implementation of the model.

The implementation must be done using the Model-View-Controller (MVC) structure and Observer pattern.

The model of the game has already been implemented in the previous task. The Grid interface is still binding for this task.

The number of columns and rows in the playfield should depend on the window and cell size. Changing the window size should invoke the RESIZE method of the gameboard.

The speed of the game thread is the amount of time between calls to the GENERATE method.

The CLEAR functionality can be done by e.g. providing a corresponding shape or using a separate button.

The complete application should terminate gracefully when a user closes the window (i.e. clicks on the closing button).

 

## General Requirements
You can modify the behavior of any method of the template, and add public methods if necessary.

The Google Java Style Guide must be fulfilled.

The asymptotic run-time performance of the implementation is part of the exercise. Consecutive generate steps in particular should not suffer from (huge) timing delays, i.e., the response time for a single generate step should be small.

©2022, SoSy-Lab

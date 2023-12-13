
# Ball Simulator

Ball Simulator is a simple Java desktop app that lets it's user simulate a bouncy ball's movement in a uniform gravitational field.

Originally, I made this project to help my friend with his project assignment for a course at university. It was written in Java, using Java Swing library. As a free time exercise i have decided to rewrite it using JavaFX to get familiar with this framework and extend the simulator's functionalities. 

## Implementation features
- I implemented a SingleShotChangeListener class, that extends ChangeListener, which removes itself from a property after firing. I used it to set sliders ranges and ball position when the animation canvas is initialized and it's width and height are set by the JavaFX mechanism.
- The updates are handled using JavaFX Timeline, which allows me to fire an animation update with a specified rate.

## Features

- Sliders that set the ball's initial position and velocity and air resistance coefficient.
- Buttons that start the simulation, stop it and reset it to a base state.
- Radio button that toggles visibility of the ball's current speed vector. 

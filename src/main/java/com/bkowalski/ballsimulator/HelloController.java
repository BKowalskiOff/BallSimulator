package com.bkowalski.ballsimulator;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import static java.lang.Math.floor;

public class HelloController {

    @FXML
    private Slider xSlider;
    @FXML
    private Slider ySlider;
    @FXML
    private Slider xSpeedSlider;
    @FXML
    private Slider ySpeedSlider;
    @FXML
    private Slider airResistanceCoefficientSlider;
    @FXML
    private Canvas canvas;
    @FXML
    private Pane canvasPane;

    // initialization of the controller - what is needed to be done at the very start of the app
    @FXML
    private void initialize(){
        // initiallly, width and height of the canvas are set to 0, because we don't explicitly specify their values
        // we add listeners to width and height properties of the canvas pane to calculate
        // and set appropriate size of the canvas
        // also we set the ball position sliders' max values to match the canvas size
        // after we set the size of the canvas it's content is cleared
        this.canvasPane.widthProperty().addListener((observable, oldValue, newValue) -> {
            this.canvas.setWidth(newValue.doubleValue() - 15.0);
            this.xSlider.setMax(floor(newValue.doubleValue()) - 10.0);
            this.clearCanvas();
        });

        this.canvasPane.heightProperty().addListener((observable, oldValue, newValue) -> {
            this.canvas.setHeight(newValue.doubleValue() - 20.0);
            this.ySlider.setMax(floor(newValue.doubleValue()) - 10.0);
            this.clearCanvas();
        });

    }
    // we're using a 2D graphics context of the canvas to clear it's content with white color (for now)
    public void clearCanvas(){
        GraphicsContext gc = this.canvas.getGraphicsContext2D();
        gc.setFill(Color.WHITE); // TODO: maybe a "nicer" background color
        gc.fillRect(0,0, this.canvas.getWidth(), this.canvas.getHeight());
    }

}
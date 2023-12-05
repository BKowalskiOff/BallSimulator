package com.bkowalski.ballsimulator;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

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
    @FXML
    private RadioButton showSpeedArrowRadio;
    private Ball ball;
    // the simulation area is big - if every pixel represents one meter, we are looking at a 600m by 600m area,
    // which makes a realistically modelled gravity barely visible, so I have decided to scale this value
    private final float gravity = 100;
    private boolean showSpeedArrow = true;
    private final Color arrowColor = Color.GREEN;
    private Timeline simulationTimeline;
    private final double FRAME_RATE = 150.0;

    // initialization of the controller - what is needed to be done at the very start of the app
    @FXML
    private void initialize(){

        // initializing the ball object
        // initially the ball should be in the middle of the canvas
        int ballRadius = 15;
        double bouncinessCoefficient = 0.7;

        this.ball = new Ball(this.canvas.getWidth()/2 - ballRadius,this.canvas.getHeight()/2 - ballRadius,
                0,0, ballRadius, bouncinessCoefficient);

        // initially, width and height of the canvas are set to 0, because we don't explicitly specify their values
        // we add listeners to width and height properties of the canvas pane to calculate
        // and set appropriate size of the canvas
        // also we set the ball position sliders' max values to match the canvas size
        // after we set the size of the canvas it's content is cleared
        this.canvasPane.widthProperty().addListener(new SingleShotChangeListener<>(
                (observable, oldValue, newValue) -> {
                    System.out.println(newValue);
                    this.canvas.setWidth(newValue.doubleValue() - 15.0);
                    this.xSlider.setMin(ballRadius);
                    this.xSlider.setMax(this.canvas.getWidth() - ballRadius);
                    this.xSlider.setValue(this.canvas.getWidth()/2);
                    this.ball.setX(this.canvas.getWidth()/2);
                    this.clearCanvas();
                    this.drawBall();
                }));

        this.canvasPane.heightProperty().addListener(new SingleShotChangeListener<>(
                (observable, oldValue, newValue) -> {
                    this.canvas.setHeight(newValue.doubleValue() - 20.0);
                    this.ySlider.setMin(ballRadius);
                    this.ySlider.setMax(this.canvas.getHeight() - ballRadius);
                    this.ySlider.setValue(this.canvas.getHeight()/2);
                    this.ball.setY(this.canvas.getHeight()/2);
                    this.clearCanvas();
                    this.drawBall();
                }));

        this.xSlider.valueProperty().addListener(((observable, oldValue, newValue) -> {
            this.ball.setX(newValue.doubleValue());
            this.clearCanvas();
            this.drawBall();
            if(this.showSpeedArrow){
                this.drawSpeedArrow();
            }
        }));

        this.ySlider.valueProperty().addListener(((observable, oldValue, newValue) -> {
            this.ball.setY(newValue.doubleValue());
            this.clearCanvas();
            this.drawBall();
            if(this.showSpeedArrow){
                this.drawSpeedArrow();
            }
        }));

        this.xSpeedSlider.valueProperty().addListener((observable) -> {
            this.ball.setVx(this.xSpeedSlider.getValue());
            if(!this.showSpeedArrow) return;
            this.clearCanvas();
            this.drawBall();
            this.drawSpeedArrow();
        });
        this.ySpeedSlider.valueProperty().addListener((observable) -> {
            this.ball.setVy(this.ySpeedSlider.getValue());
            if(!this.showSpeedArrow) return;
            this.clearCanvas();
            this.drawBall();
            this.drawSpeedArrow();
        });

        this.simulationTimeline = new Timeline(new KeyFrame(Duration.millis(1000/this.FRAME_RATE), (event) -> {
            this.ball.update(1.0/this.FRAME_RATE, this.canvas.getWidth(), this.canvas.getHeight(),
                    this.airResistanceCoefficientSlider.getValue()/1000.0, this.gravity);
            this.clearCanvas();
            this.drawBall();
            this.drawSpeedArrow();
        }));
        this.simulationTimeline.setCycleCount(Timeline.INDEFINITE);

    }
    // we're using a 2D graphics context of the canvas to clear it's content with white color (for now)
    private void clearCanvas(){
        GraphicsContext gc = this.canvas.getGraphicsContext2D();
        gc.setFill(Color.WHITE); // TODO: maybe a "nicer" background color
        gc.fillRect(0,0, this.canvas.getWidth(), this.canvas.getHeight());
        gc.setFill(Color.RED);
    }

    private void drawBall(){
        GraphicsContext gc = this.canvas.getGraphicsContext2D();
        gc.setFill(Color.BLACK);
        double r = this.ball.getR();
        gc.fillOval(this.ball.getX() - r, this.canvas.getHeight() - (this.ball.getY() + r), 2*r, 2*r);
    }

    private void drawSpeedArrow(){
        GraphicsContext gc = this.canvas.getGraphicsContext2D();
        gc.setStroke(this.arrowColor);
        gc.setLineWidth(3);
        double lMult = 0.5; // we multiply the length so that the arrow isn't too small or too large
        double x1 = this.ball.getX();
        double y1 = this.canvas.getHeight() - this.ball.getY();
        double vx = this.ball.getVx();
        double vy = -this.ball.getVy(); // we negate this value due to y-axis downwards direction
        double x2 = x1 + lMult*vx;
        double y2 = y1 + lMult*vy;
        // the speed vector can be represented as: vx = l * cos(a), vy = l * sin(a), where a is its angle with respect
        // to x-axis and can be calculated using the following formula: vy/vx = tan(a) -> a = atan(vy, vx)
        // the "head lines" are just this vector rotated by 135 and 225 degrees
        // the main line is drawn using the ball's position as a starting point
        // and the other two using the end of the first line
        double angle = Math.atan2(vy, vx);
        // main line
        gc.strokeLine(x1,y1,x2,y2);
        // head line 1
        double a = 5*Math.cos(angle + Math.toRadians(135));
        double b = 5*Math.sin(angle + Math.toRadians(135));
        gc.strokeLine(x2,y2,x2 + a, y2 + b);
        // head line 2
        a = 5*Math.cos(angle + Math.toRadians(225));
        b = 5*Math.sin(angle + Math.toRadians(225));
        gc.strokeLine(x2,y2,x2 + a, y2 + b);

    }
    // changing the state of the show speed vector radio button negates current flag and redraws the canvas
    // in order to remove/draw the speed vector
    public void setShowSpeedArrow() {
        this.showSpeedArrow = !this.showSpeedArrow;
        this.clearCanvas();
        this.drawBall();
        if(this.showSpeedArrow){
            this.drawSpeedArrow();
        }
    }

    @FXML
    public void onStartButtonClicked() {
        if(this.simulationTimeline.getStatus() != Animation.Status.RUNNING) {
            this.simulationTimeline.play();
        }
    }
    @FXML
    public void onStopButtonClicked() {
        if(this.simulationTimeline.getStatus() == Animation.Status.RUNNING) {
            this.simulationTimeline.pause();
        }
    }
}
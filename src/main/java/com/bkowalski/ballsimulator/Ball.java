package com.bkowalski.ballsimulator;

import static java.lang.Math.abs;
import static java.lang.Math.sqrt;

public class Ball {

    private double x, y;
    private double vx, vy;
    private int r;

    double bouncinessCoef;

    public Ball(double x, double y, double vx, double vy, int r, double bouncinessCoef) {
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.r = r;
        this.bouncinessCoef = bouncinessCoef;
    }


    void update(double dt, double canvasWidth, double canvasHeight, double resistanceCoef, double gravity){
        // first we update position using the previous velocity * 0.5 and then using the updated velocity to get
        // a more accurate approximation, because of the velocity changing between discrete points in time
        // this approach is still not 100% accurate, because of the acceleration not being constant, but it's a fair
        // approximation
        this.x += vx * dt;
        this.y += vy * dt;
        this.handleBounce(canvasWidth, canvasHeight);
        // simple formula for air resistance: Fx = -resistanceCoefficient * v * vx, Fy = -resistanceCoefficient * v * vy
        // for now, we don't consider mass, so acceleration = force (ignoring units)
        double v = sqrt(this.vx*this.vx  + this.vy*this.vy);
        double resistanceX = -1 * resistanceCoef * this.vx * v;
        double resistanceY = -1 * resistanceCoef * this.vy * v;
        double ay = resistanceY - gravity;
        this.vx += resistanceX * dt;
        this.vy += ay *dt;
    }

    private void handleBounce(double canvasWidth, double canvasHeight){
        if (this.x < this.r){
            this.x = this.r;
            this.vx = -this.vx * this.bouncinessCoef;
        }
        if (this.x > canvasWidth - this.r){
            this.x = canvasWidth - this.r;
            this.vx = -this.vx * this.bouncinessCoef;
        }
        if (this.y < this.r){
            this.y = this.r;
            this.vy = -this.vy * this.bouncinessCoef;
        }
        if (this.y > canvasHeight - this.r){
            this.y = canvasHeight - this.r;
            this.vy = -this.vy * this.bouncinessCoef;
        }
    }
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getVx() {
        return vx;
    }

    public double getVy() {
        return vy;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setVx(double vx) {
        this.vx = vx;
    }

    public void setVy(double vy) {
        this.vy = vy;
    }

    public int getR() {
        return r;
    }
}

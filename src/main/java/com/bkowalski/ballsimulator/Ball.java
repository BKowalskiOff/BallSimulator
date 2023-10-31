package com.bkowalski.ballsimulator;

import static java.lang.Math.abs;

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


    void update(double dt, double resistanceCoef, double gravity){
        // first we update position using the previous velocity * 0.5 and then using the updated velocity to get
        // a more accurate approximation, because of the velocity changing between discrete points in time
        // this approach is still not 100% accurate, because of the acceleration not being constant, but it's a fair
        // approximation
        this.x += vx * dt * 0.5;
        this.y += vy * dt * 0.5;
        // simple formula for air resistance: F = resistanceCoefficient * v^2
        // for now, we don't consider mass, so acceleration = force (ignoring units)
        // we use -1 * velocity * abs(velocity) to make the force's direction opposite to the velocity
        double resistanceX = -1 * resistanceCoef * this.vx * abs(this.vx);
        double resistanceY = -1 * resistanceCoef * this.vy * abs(this.vy);
        double ay = resistanceY - gravity;
        this.vx += resistanceX * dt;
        this.vy += ay *dt;
        this.x += vx * dt * 0.5;
        this.y += vy * dt * 0.5;
    }

}

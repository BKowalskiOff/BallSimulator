module com.bkowalski.ballsimulator {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.bkowalski.ballsimulator to javafx.fxml;
    exports com.bkowalski.ballsimulator;
}
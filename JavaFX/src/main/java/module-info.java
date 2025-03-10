module com.example.javafx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires org.xerial.sqlitejdbc;
    requires org.junit.jupiter.api;
    requires org.junit.jupiter.engine;
    requires org.junit.platform.commons;
    requires org.junit.platform.engine;
    requires javafaker;

    opens com.example.javafx to javafx.fxml;
    exports com.example.javafx;
}

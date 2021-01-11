/**
 *
 */
open module whatwebgui {
    requires java.desktop;
    requires java.sql;
    requires javafx.base;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires de.jensd.fx.fontawesomefx.fontawesome;
    requires javafx.media;
    requires sqlite.jdbc;
    requires poi;
    requires commons.csv;
    requires org.apache.commons.io;
    exports ehu.isad;
}

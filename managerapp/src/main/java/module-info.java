module org.yuqoi.managerapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires javafx.graphics;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires mysql.connector.j;
    requires java.sql;

    // icons
    requires de.jensd.fx.glyphs.commons;
    requires de.jensd.fx.glyphs.fontawesome;
    requires de.jensd.fx.glyphs.materialdesignicons;
    requires layout;
    requires kernel;
    requires io;


    opens org.yuqoi.managerapp to javafx.fxml;
    exports org.yuqoi.managerapp;
    exports org.yuqoi.managerapp.controllers.windows;
    exports org.yuqoi.managerapp.utils;
    opens org.yuqoi.managerapp.utils to javafx.fxml;
    exports org.yuqoi.managerapp.controllers.panels;
    exports org.yuqoi.managerapp.models.Watch;
    exports org.yuqoi.managerapp.utils.scenemanager;
    opens org.yuqoi.managerapp.utils.scenemanager to javafx.fxml;
    exports org.yuqoi.managerapp.models.Invoice;
}
module com.wayne.wayneen.enterpriseswyne {
    // JavaFX
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.web;

    // JDK
    requires java.sql;
    requires java.desktop;

    // Bibliotecas externas
    requires itextpdf;
    requires org.apache.poi.ooxml;
    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires jakarta.mail;
    requires jbcrypt;

    // JDBC via ServiceLoader (driver no classpath)
    uses java.sql.Driver;

    // Exports (somente se outras libs/módulos precisarem acessar public classes)
    exports com.wayne.wayneen.enterpriseswyne;
    exports com.wayne.wayneen.enterpriseswyne.controller;
    exports com.wayne.wayneen.enterpriseswyne.model;
    exports com.wayne.wayneen.enterpriseswyne.DAO;
    exports com.wayne.wayneen.enterpriseswyne.util;




    // IMPORTANTES: abrir pacotes para reflexão do FXML e do TableView
    // seus controllers "soltos"
    opens com.wayne.wayneen.enterpriseswyne.controller to javafx.fxml;// controllers no subpackage
    opens com.wayne.wayneen.enterpriseswyne.model to javafx.base, javafx.fxml;
    opens com.wayne.wayneen.enterpriseswyne to javafx.base, javafx.fxml;
    opens com.wayne.wayneen.enterpriseswyne.DAO to javafx.base, javafx.fxml; // TableView/PropertyValueFactory e FXML
}

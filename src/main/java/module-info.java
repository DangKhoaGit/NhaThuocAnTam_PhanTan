module com.antam.app {

    requires javafx.fxml;
    requires javafx.graphics;
    requires java.desktop;
    requires java.sql;
    requires javafx.controls;
    requires fontawesomefx;
    requires jbcrypt;
    requires itextpdf;
    requires static lombok;
    requires jakarta.persistence;

    // cho FXML
    opens com.antam.app.controller.hoadon to javafx.fxml;
    opens com.antam.app.controller.phieudat to javafx.fxml;
    opens com.antam.app.controller.thuoc to javafx.fxml;
    opens com.antam.app.controller.kethuoc to javafx.fxml;
    opens com.antam.app.controller.dangdieuche to javafx.fxml;
    opens com.antam.app.controller.donvitinh to javafx.fxml;
    opens com.antam.app.controller.khuyenmai to javafx.fxml;
    opens com.antam.app.controller.phieunhap to javafx.fxml;
    opens com.antam.app.controller.trangchinh to javafx.fxml;
    opens com.antam.app.controller.dangnhap to javafx.fxml;
    opens com.antam.app.controller.khungchinh to javafx.fxml;
    opens com.antam.app.controller.khachhang to javafx.fxml;
    opens com.antam.app.gui to javafx.fxml;
    opens com.antam.app.controller.nhanvien to javafx.fxml;
    opens com.antam.app.controller.caidattaikhoan to javafx.fxml;

    // cho JavaFX TableView/PropertyValueFactory đọc getter của entity
    opens com.antam.app.entity to javafx.base, org.hibernate.orm.core;

    // exports nếu muốn dùng entity ở package khác
    exports com.antam.app.entity;
    exports com.antam.app.controller.hoadon;
    exports com.antam.app.controller.phieudat;
    exports com.antam.app.controller.thuoc;
    exports com.antam.app.controller.kethuoc;
    exports com.antam.app.controller.dangdieuche;
    exports com.antam.app.controller.donvitinh;
    exports com.antam.app.controller.khuyenmai;
    exports com.antam.app.controller.phieunhap;
    exports com.antam.app.controller.dangnhap;
    exports com.antam.app.controller.trangchinh;
    exports com.antam.app.controller.khungchinh;
    exports com.antam.app.gui;

    // Hibernate cần truy cập entity để ánh xạ, nên phải mở module cho nó
    requires org.hibernate.orm.core;

}

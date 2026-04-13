//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.antam.app.controller.thuoc;

import com.antam.app.connect.ConnectDB;
import com.antam.app.dao.impl.DangDieuChe_DAO;
import com.antam.app.dao.impl.DonViTinh_DAO;
import com.antam.app.dao.impl.Ke_DAO;
import com.antam.app.dao.impl.Thuoc_DAO;
import com.antam.app.entity.DangDieuChe;
import com.antam.app.entity.DonViTinh;
import com.antam.app.entity.Ke;
import com.antam.app.entity.Thuoc;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.text.Text;
import javafx.util.StringConverter;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

public class ThemThuocFormController extends DialogPane{
    private DangDieuChe_DAO ddc_dao;
    private DonViTinh_DAO dvt_dao;
    private Thuoc_DAO thuoc_dao;
    private Ke_DAO ke_dao;

    private TextField txtAddMaThuoc, txtAddTenThuoc, txtAddHamLuong;

    private Text notification_addThuoc;

    private Spinner<Double> spAddGiaGoc, spAddGiaBan, spAddThue;

    private ComboBox<DonViTinh> cbAddDVCS;

    private ComboBox<DangDieuChe> cbAddDangDieuChe;

    private ComboBox<Ke> cbAddKe;


    public ThemThuocFormController() {
        this.setPrefSize(800, 600);

        FlowPane header = new FlowPane();
        header.setAlignment(Pos.CENTER);
        header.setStyle("-fx-background-color: #1e3a8a;");
        Text title = new Text("Thêm thuốc mới");
        title.setFill(javafx.scene.paint.Color.WHITE);
        title.setFont(Font.font("System Bold", 15));
        FlowPane.setMargin(title, new Insets(10,0,10,0));
        header.getChildren().add(title);
        this.setHeader(header);

        // ===== Content =====
        AnchorPane root = new AnchorPane();
        VBox container = new VBox(10);
        container.setPadding(new Insets(10));
        AnchorPane.setLeftAnchor(container, 0.0);
        AnchorPane.setRightAnchor(container, 0.0);

        // ===== GridPane chính =====
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setHgrow(Priority.SOMETIMES);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setHgrow(Priority.SOMETIMES);
        grid.getColumnConstraints().addAll(col1, col2);

        // Labels và Controls
        Text labelMa = new Text("Mã thuốc:");
        txtAddMaThuoc = new TextField();
        txtAddMaThuoc.setPrefHeight(40);

        Text labelTen = new Text("Tên thuốc:");
        txtAddTenThuoc = new TextField();
        txtAddTenThuoc.setPrefHeight(40);

        Text labelGiaGoc = new Text("Giá gốc:");
        spAddGiaGoc = new Spinner<>();
        spAddGiaGoc.setMaxWidth(Double.MAX_VALUE);
        spAddGiaGoc.setEditable(true);
        spAddGiaGoc.setPrefHeight(40);

        Text labelGiaBan = new Text("Giá bán:");
        spAddGiaBan = new Spinner<>();
        spAddGiaBan.setMaxWidth(Double.MAX_VALUE);
        spAddGiaBan.setEditable(true);
        spAddGiaBan.setPrefHeight(40);

        Text labelThue = new Text("Thuế (%):");
        spAddThue = new Spinner<>();
        spAddThue.setMaxWidth(Double.MAX_VALUE);
        spAddThue.setEditable(true);
        spAddThue.setPrefHeight(40);

        Text labelDangDieuChe = new Text("Dạng điều chế:");
        cbAddDangDieuChe = new ComboBox<>();
        cbAddDangDieuChe.setMaxWidth(Double.MAX_VALUE);
        cbAddDangDieuChe.setPrefHeight(40);

        Text labelKe = new Text("Kệ thuốc:");
        cbAddKe = new ComboBox<>();
        cbAddKe.setMaxWidth(Double.MAX_VALUE);
        cbAddKe.setPrefHeight(40);

        Text labelHamLuong = new Text("Hàm lượng:");
        txtAddHamLuong = new TextField();
        txtAddHamLuong.setPrefHeight(40);

        Text labelDonViTinh = new Text("Đơn vị tính:");
        cbAddDVCS = new ComboBox<>();
        cbAddDVCS.setMaxWidth(Double.MAX_VALUE);
        cbAddDVCS.setPrefHeight(40);

        // ===== Gán vào GridPane =====
        grid.add(labelMa, 0, 0);
        grid.add(txtAddMaThuoc, 0, 1);

        grid.add(labelTen, 1, 0);
        grid.add(txtAddTenThuoc, 1, 1);

        grid.add(labelGiaGoc, 0, 2);
        grid.add(spAddGiaGoc, 0, 3);

        grid.add(labelGiaBan, 1, 2);
        grid.add(spAddGiaBan, 1, 3);

        grid.add(labelThue, 0, 4);
        grid.add(spAddThue, 0, 5);

        grid.add(labelDangDieuChe, 1, 4);
        grid.add(cbAddDangDieuChe, 1, 5);

        grid.add(labelKe, 0, 6);
        grid.add(cbAddKe, 0, 7);

        grid.add(labelHamLuong, 1, 6);
        grid.add(txtAddHamLuong, 1, 7);

        grid.add(labelDonViTinh, 0, 8);
        grid.add(cbAddDVCS, 0, 9);

        // ===== Notification =====
        notification_addThuoc = new Text();
        notification_addThuoc.setFill(javafx.scene.paint.Color.RED);

        // ===== Add tất cả vào container =====
        container.getChildren().addAll(grid, notification_addThuoc);
        root.getChildren().add(container);
        this.setContent(root);

        // ===== Stylesheet =====
        this.getStylesheets().add(getClass().getResource("/com/antam/app/styles/dashboard_style.css").toExternalForm());
        /** Sự kiện **/
        // them button vao dialog
        ButtonType cancelButton = new ButtonType("Huỷ", ButtonData.CANCEL_CLOSE);
        ButtonType applyButton = new ButtonType("Lưu", ButtonData.APPLY);
        this.getButtonTypes().add(cancelButton);
        this.getButtonTypes().add(applyButton);

        // ket noi database
        try { Connection con = ConnectDB.getInstance().connect(); }
        catch (SQLException e) { throw new RuntimeException(e); }

        // su kien button luu
        Button applyBtn = (Button) this.lookupButton(applyButton);

        applyBtn.addEventFilter(ActionEvent.ACTION, event -> {
            boolean isValid = validate();

            if (!isValid) {
                event.consume();
            }else {
                String maThuoc = txtAddMaThuoc.getText();
                String tenThuoc = txtAddTenThuoc.getText();
                DonViTinh donViCoSo = cbAddDVCS.getValue();
                DangDieuChe dangDieuChe =  cbAddDangDieuChe.getValue();
                String hamLuong = txtAddHamLuong.getText();
                Double giaGoc = spAddGiaGoc.getValue();
                Double giaBan = spAddGiaBan.getValue();
                Double thue = spAddThue.getValue();
                Ke ke = cbAddKe.getValue();

                Thuoc thuoc = new Thuoc(maThuoc, tenThuoc, hamLuong, giaBan, giaGoc, thue.floatValue(), false,
                        dangDieuChe, donViCoSo, ke);
                thuoc_dao = new Thuoc_DAO();
                try {
                    ConnectDB.getInstance().connect();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                boolean check = thuoc_dao.themThuoc(thuoc);
                if (!check) {
                    notification_addThuoc.setText("Thêm thuốc thất bại!");
                    event.consume();
                }
            }

        });

        // su kien up down spinner gia goc
        SpinnerValueFactory.DoubleSpinnerValueFactory valueFactoryGiaGoc =
                new SpinnerValueFactory.DoubleSpinnerValueFactory(
                        0,
                        5000000,
                        0,
                        5000
                );
        NumberFormat currencyFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
        valueFactoryGiaGoc.setConverter(new StringConverter<Double>() {
            @Override
            public String toString(Double value) {
                if (value == null) return "";
                return currencyFormat.format(value);
            }

            @Override
            public Double fromString(String text) {
                try {
                    Number parsed = currencyFormat.parse(text);
                    return parsed.doubleValue();
                } catch (Exception e) {
                    return 0.0;
                }
            }
        });
        spAddGiaGoc.setValueFactory(valueFactoryGiaGoc);
        spAddGiaGoc.setEditable(true);
        // su kien up down spinner gia ban
        SpinnerValueFactory.DoubleSpinnerValueFactory valueFactoryGiaBan =
                new SpinnerValueFactory.DoubleSpinnerValueFactory(
                        0,
                        5000000,
                        0,
                        5000
                );
        valueFactoryGiaBan.setConverter(new StringConverter<Double>() {
            @Override
            public String toString(Double value) {
                if (value == null) return "";
                return currencyFormat.format(value);
            }

            @Override
            public Double fromString(String text) {
                try {
                    Number parsed = currencyFormat.parse(text);
                    return parsed.doubleValue();
                } catch (Exception e) {
                    return 0.0;
                }
            }
        });
        spAddGiaBan.setValueFactory(valueFactoryGiaBan);
        spAddGiaBan.setEditable(true);
        // su kien up down spinner thue
        SpinnerValueFactory.DoubleSpinnerValueFactory valueFactoryThue =
                new SpinnerValueFactory.DoubleSpinnerValueFactory(
                        0,
                        1,
                        0.05,
                        0.01
                );
        spAddThue.setValueFactory(valueFactoryThue);
        spAddThue.setEditable(true);
        //them combo box ke
        addComBoBoxKe();
        // them combo box don vi co so
        addComBoBoxDVCS();
        //them combo box dang dieu che
        addComBoBoxDDC();
    }

    // ham validate kiem tra du lieu truyen vao dung hay ko
    public boolean validate(){
        String maThuoc = txtAddMaThuoc.getText();
        String tenThuoc = txtAddTenThuoc.getText();
        String donViCoSo = cbAddDVCS.getValue().getTenDVT();
        String dangDieuChe = cbAddDangDieuChe.getValue().getTenDDC();
        String hamLuong = txtAddHamLuong.getText();
        Double giaGoc = 0.0;
        Double giaBan = 0.0;
        Double thue = 0.0;
        String ke = cbAddKe.getValue().getTenKe();

        if(maThuoc.isEmpty()){
            notification_addThuoc.setText("Vui lòng điền mã thuốc!");
            return false;
        }else{
            if (maThuoc.matches("^VN-\\d{5}-\\d{2}$")) {
                try {
                    ConnectDB.getInstance().connect();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                thuoc_dao = new Thuoc_DAO();
                if (thuoc_dao.getThuocTheoMa(maThuoc) != null){
                    notification_addThuoc.setText("Mã thuốc đã tồn tại!");
                    return false;
                }
                notification_addThuoc.setText("");
            } else {
                notification_addThuoc.setText("Mã thuốc không hợp lệ! (VD: VN-12345-01)");
                return false;
            }
        }
        if (tenThuoc.isEmpty()){
            notification_addThuoc.setText("Vui lòng điền đầy đủ tên thuốc!");
            return false;
        }
        if (donViCoSo == null){
            notification_addThuoc.setText("Vui lòng điền đầy đủ đơn vị cơ sở!");
            return false;
        }
        if (dangDieuChe == null){
            notification_addThuoc.setText("Vui lòng điền dạng điều chế!");
            return false;
        }
        if (hamLuong.isEmpty()){
            notification_addThuoc.setText("Vui lòng điền đầy đủ hàm lượng!");
            return false;
        }
        try {
            giaGoc = spAddGiaGoc.getValue();
            if (giaGoc <= 0) {
                notification_addThuoc.setText("Giá gốc phải lớn hơn 0!");
                return false;
            }
        } catch (Exception e) {
            notification_addThuoc.setText("Giá gốc không hợp lệ!");
            return false;
        }
        try {
            giaBan = spAddGiaBan.getValue();
            if (giaBan <= 0) {
                notification_addThuoc.setText("Giá bán phải lớn hơn 0!");
                return false;
            }
        } catch (Exception e) {
            notification_addThuoc.setText("Giá bán không hợp lệ!");
            return false;
        }
        try {
            thue = spAddThue.getValue();
            if (thue < 0) {
                notification_addThuoc.setText("Thuế không được âm!");
                return false;
            }
        } catch (Exception e) {
            notification_addThuoc.setText("Thuế không hợp lệ!");
            return false;
        }
        if (ke == null){
            notification_addThuoc.setText("Vui lòng điền đầy đủ thông tin!");
            return false;
        }
        return true;
    }

    // them value vao combobox ke
    public void addComBoBoxKe() {
        ke_dao = new Ke_DAO();
        ArrayList<Ke> arrayKe = ke_dao.getTatCaKeHoatDong();
        cbAddKe.getItems().clear();
        for (Ke ke : arrayKe) {
            cbAddKe.getItems().add(ke);
        }
        cbAddKe.getSelectionModel().selectFirst();
    }

    // Them value vao combobox dang dieu che
    public void addComBoBoxDDC() {
        ddc_dao = new DangDieuChe_DAO();
        ArrayList<DangDieuChe> arrayDDC = ddc_dao.getDangDieuCheHoatDong();
        cbAddDangDieuChe.getItems().clear();
        for (DangDieuChe ddc : arrayDDC){
            cbAddDangDieuChe.getItems().add(ddc);
        }
        cbAddDangDieuChe.getSelectionModel().selectFirst();
    }

    // Them value vao combobox don vi tinh
    public void addComBoBoxDVCS() {
        dvt_dao = new DonViTinh_DAO();
        ArrayList<DonViTinh> arrayDVT = dvt_dao.getAllDonViTinh();
        cbAddDVCS.getItems().clear();
        arrayDVT.forEach(dvt -> cbAddDVCS.getItems().add(dvt));
        cbAddDVCS.getSelectionModel().selectFirst();
    }

    // Cat chuoi lay don vi
    public static String extractUnit(String input) {
        if (input == null) return null;

        String pattern = "\\d+\\s*(\\p{L}+).*";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(input);

        if (m.find()) {
            return m.group(1);
        } else {
            return null;
        }
    }

}

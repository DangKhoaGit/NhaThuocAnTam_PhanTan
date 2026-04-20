//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.antam.app.controller.donvitinh;

import com.antam.app.service.I_DonViTinh_Service;
import com.antam.app.service.impl.DonViTinh_Service;
import com.antam.app.dto.DonViTinhDTO;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcons;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.*;
import java.util.ArrayList;
import java.util.Optional;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class CapNhatDonViTinhController extends ScrollPane {
    private TextField txtMa,txtTen;
    private TableView<DonViTinhDTO> tableThuoc;
    private TableColumn<DonViTinhDTO, String> colMaThuoc, colTenThuoc,colTrangThai;
    private Button btnXoa,btnCapNhat, btnKhoiPhuc;

    DonViTinh_Service donViTinh_dao = new DonViTinh_Service();
    ArrayList<DonViTinhDTO> listDVT ;

    public CapNhatDonViTinhController() {
        /** Giao diện **/
        this.setFitToHeight(true);
        this.setFitToWidth(true);
        this.setPrefSize(900, 730);
        AnchorPane.setTopAnchor(this, 0.0);
        AnchorPane.setBottomAnchor(this, 0.0);
        AnchorPane.setLeftAnchor(this, 0.0);
        AnchorPane.setRightAnchor(this, 0.0);

        VBox root = new VBox(30);
        root.setStyle("-fx-background-color: #f8fafc;");
        root.setPadding(new Insets(20));

        HBox titleBox = new HBox();
        titleBox.setAlignment(Pos.CENTER_LEFT);

        Text title = new Text("Cập nhật đơn vị tính");
        title.setFont(Font.font("System Bold", 30));
        title.setFill(Color.web("#1e3a8a"));

        Pane spacer = new Pane();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        titleBox.getChildren().addAll(title, spacer);

        // ========================= INPUT AREA =========================
        FlowPane flow = new FlowPane();
        flow.setHgap(5);
        flow.setVgap(5);
        flow.getStyleClass().add("box-pane");
        flow.setPadding(new Insets(10));
        flow.setEffect(new DropShadow(19.5, 3, 2, Color.rgb(211, 211, 211)));

        // --- Mã đơn vị tính ---
        VBox boxMa = new VBox(5);
        Text lbMa = new Text("Mã đơn vị tính");
        lbMa.setFill(Color.web("#374151"));
        lbMa.setFont(Font.font(13));

        txtMa = new TextField();
        txtMa.setPromptText("Nhập mã đơn vị tính");
        txtMa.setPrefSize(200, 40);

        boxMa.getChildren().addAll(lbMa, txtMa);

        // --- Tên đơn vị tính ---
        VBox boxTen = new VBox(5);
        Text lbTen = new Text("Tên đơn vị tính");
        lbTen.setFill(Color.web("#374151"));
        lbTen.setFont(Font.font(13));

        txtTen = new TextField();
        txtTen.setPromptText("Nhập tên đơn vị tính");
        txtTen.setPrefSize(200, 40);

        boxTen.getChildren().addAll(lbTen, txtTen);

        // --- Button Cập nhật ---
        VBox boxUpdate = new VBox(5);
        boxUpdate.setAlignment(Pos.CENTER);

        btnCapNhat = new Button("Cập nhật");
        btnCapNhat.setPrefSize(102, 40);
        btnCapNhat.setStyle("-fx-background-color: #2563eb; -fx-background-radius: 5px;");
        btnCapNhat.setTextFill(Color.WHITE);

        FontAwesomeIcon iconRefresh = new FontAwesomeIcon();
        iconRefresh.setIcon(FontAwesomeIcons.REFRESH);
        iconRefresh.setFill(Color.WHITE);
        btnCapNhat.setGraphic(iconRefresh);

        boxUpdate.getChildren().addAll(new Text(), btnCapNhat);

        // --- Button Xóa ---
        VBox boxDelete = new VBox(5);
        boxDelete.setAlignment(Pos.CENTER);

        btnXoa = new Button("Xoá");
        btnXoa.setPrefSize(102, 40);
        btnXoa.setStyle("-fx-background-color: red; -fx-background-radius: 5px;");
        btnXoa.setTextFill(Color.WHITE);

        FontAwesomeIcon iconTrash = new FontAwesomeIcon();
        iconTrash.setGlyphName("TRASH");
        iconTrash.setFill(Color.WHITE);
        btnXoa.setGraphic(iconTrash);

        boxDelete.getChildren().addAll(new Text(), btnXoa);

        VBox boxRestore = new VBox(5);
        boxRestore.setAlignment(Pos.CENTER);

        btnKhoiPhuc = new Button("Khôi phục");
        btnKhoiPhuc.getStyleClass().add("btn-khoiphuc-mini");
        btnKhoiPhuc.setTextFill(Color.WHITE);

        FontAwesomeIcon iconRestore = new FontAwesomeIcon();
        iconRestore.setIcon(FontAwesomeIcons.BACKWARD);
        iconRestore.setFill(Color.WHITE);
        btnKhoiPhuc.setGraphic(iconRestore);

        boxRestore.getChildren().addAll(new Text(), btnKhoiPhuc);

        // Add all to FlowPane
        flow.getChildren().addAll(boxMa, boxTen, boxUpdate, boxDelete, boxRestore);

        // ========================= TABLE =========================
        tableThuoc = new TableView<>();
        tableThuoc.setPrefHeight(800);

        colMaThuoc = new TableColumn<>("Mã đơn vị tính");
        colTenThuoc = new TableColumn<>("Tên đơn vị tính");
        colTrangThai = new TableColumn<>("Trạng thái");

        tableThuoc.getColumns().addAll(colMaThuoc, colTenThuoc,colTrangThai);
        tableThuoc.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // ========================= Add Everything =========================
        root.getChildren().addAll(titleBox, flow, tableThuoc);

        this.getStylesheets().add(getClass().getResource("/com/antam/app/styles/dashboard_style.css").toExternalForm());
        this.setContent(root);
        /** Sự kiện **/
        btnCapNhat.setOnAction( e-> {
            try {
                String ma = txtMa.getText();
                int maInt = Integer.parseInt(ma);
                String ten = txtTen.getText();
                DonViTinhDTO donViTinhDTO = new DonViTinhDTO(maInt, ten, false);
                donViTinh_dao.updateDonViTinh(donViTinhDTO);
                loadTable();
            }catch (Exception ex) {
                // Hiển thị thông báo lỗi nếu có
            }
        });

        btnXoa.setOnAction( e-> {
            try {
                String ma = txtMa.getText();
                int maInt = Integer.parseInt(ma);
                String ten = txtTen.getText();
                DonViTinhDTO donViTinhDTO = new DonViTinhDTO(maInt, ten,false);
                donViTinh_dao.xoaDonViTinh(donViTinhDTO);
                loadTable();
            }catch (Exception ex) {
                // Hiển thị thông báo lỗi nếu có
            }
        });
        txtMa.setEditable(false);
        try {
            DonViTinh_Service donViTinhService = new DonViTinh_Service();
            int maxMa = Integer.parseInt(donViTinhService.getHashDVT());
            txtMa.setText(String.valueOf(++maxMa));
        } catch (Exception e) {
            e.printStackTrace();
        }

        loadTable();
        setupTable();
        tableThuoc.setOnMouseClicked( e-> {
            DonViTinhDTO selected = tableThuoc.getSelectionModel().getSelectedItem();
            if (selected != null) {
                txtMa.setText(String.valueOf(selected.getMaDVT()));
                txtTen.setText(selected.getTenDVT());
            }
        });

        btnKhoiPhuc.setOnAction(e->{
            DonViTinhDTO selected = tableThuoc.getSelectionModel().getSelectedItem();
            if (selected == null) return;

            if (selected.isDelete()){
                if (showConfirm()){
                    String ma = txtMa.getText();
                    int maInt = Integer.parseInt(ma);
                    String ten = txtTen.getText();
                    DonViTinhDTO donViTinhDTO = new DonViTinhDTO(maInt, ten,false);
                    donViTinh_dao.khoiPhucDonViTinh(donViTinhDTO);
                    loadTable();
                }
            }else{
                showMess("Thông báo","Đơn vị tính đang hoạt động");
            }
        });
    }

    private void showMess(String thôngBáo, String s) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(thôngBáo);
        alert.setContentText(s);
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    private boolean showConfirm() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Xác nhận");
        alert.setHeaderText(null);
        alert.setContentText("Bạn có chắc chắn muốn thực hiện thao tác này?");
        Optional<ButtonType> result = alert.showAndWait();

        return result.isPresent() && result.get() == ButtonType.OK;
    }


    private void setupTable() {
        colMaThuoc.setCellValueFactory( e -> new SimpleStringProperty(String.valueOf(e.getValue().getMaDVT())));
        colTenThuoc.setCellValueFactory( e -> new SimpleStringProperty(e.getValue().getTenDVT()));
        colTrangThai.setCellValueFactory(e-> new SimpleStringProperty(e.getValue().isDelete()? "Đã xóa":"Đang hoạt động"));
    }

    private void loadTable() {
        listDVT = donViTinh_dao.getTatCaDonViTinh();

        // Cập nhật dữ liệu cho TableView
        tableThuoc.getItems().setAll(listDVT);

    }
}

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
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.ArrayList;

public class ThemDonViTinhController extends ScrollPane {

    private TextField txtMa,txtTen;
    private TableView<DonViTinhDTO> tableThuoc;
    private TableColumn<DonViTinhDTO, String> colMaThuoc, colTenThuoc,colTrangThai;
    private Button btnThem;

    DonViTinh_Service donViTinh_dao = new DonViTinh_Service();
    ArrayList<DonViTinhDTO> listDVT ;

    public ThemDonViTinhController() {
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

        // ========================= TITLE =========================
        HBox titleBox = new HBox();
        titleBox.setAlignment(Pos.CENTER_LEFT);

        Text title = new Text("Thêm đơn vị tính");
        title.setFont(Font.font("System Bold", 30));
        title.setFill(Color.web("#1e3a8a"));

        Pane spacer = new Pane();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        titleBox.getChildren().addAll(title, spacer);

        // ========================= INPUT PANE =========================
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

        // --- Button Thêm ---
        VBox boxBtn = new VBox(5);
        boxBtn.setAlignment(Pos.CENTER);

        btnThem = new Button("Thêm");
        btnThem.setPrefSize(76, 40);
        btnThem.setStyle("-fx-background-color: #2563eb; -fx-background-radius: 5px;");
        btnThem.setTextFill(Color.WHITE);

        FontAwesomeIcon iconPlus = new FontAwesomeIcon();
        iconPlus.setIcon(FontAwesomeIcons.PLUS);
        iconPlus.setFill(Color.WHITE);
        btnThem.setGraphic(iconPlus);

        boxBtn.getChildren().addAll(new Text(), btnThem);

        flow.getChildren().addAll(boxMa, boxTen, boxBtn);

        // ========================= TABLE =========================
        tableThuoc = new TableView<>();
        tableThuoc.setPrefHeight(800);

        colMaThuoc = new TableColumn<>("Mã đơn vị tính");
        colTenThuoc = new TableColumn<>("Tên đơn vị tính");
        colTrangThai = new TableColumn<>("Trạng thái");

        tableThuoc.getColumns().addAll(colMaThuoc, colTenThuoc,colTrangThai);

        tableThuoc.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // ========================= ADD ALL =========================
        root.getChildren().addAll(titleBox, flow, tableThuoc);

        this.getStylesheets().add(getClass().getResource("/com/antam/app/styles/dashboard_style.css").toExternalForm());
        this.setContent(root);

        /** Sự kiện **/

        btnThem.setOnAction( e-> {
            try {
                String ma = txtMa.getText();
                int maInt = Integer.parseInt(ma);
                String ten = txtTen.getText();
                DonViTinhDTO donViTinhDTO = new DonViTinhDTO(maInt, ten, false);
                donViTinh_dao.themDonViTinh(donViTinhDTO);
                loadTable();
            }catch (Exception ex) {
                // Hiển thị thông báo lỗi nếu có
            }
        });

        txtMa.setEditable(false);
        try {
            int maxMa = Integer.parseInt(I_DonViTinh_Service.getHashDVT());
            txtMa.setText(String.valueOf(++maxMa));
        } catch (Exception e) {
            e.printStackTrace();
        }

        loadTable();
        setupTable();
    }

    private void setupTable() {
        colMaThuoc.setCellValueFactory( e -> new SimpleStringProperty(String.valueOf(e.getValue().getMaDVT())));
        colTenThuoc.setCellValueFactory( e -> new SimpleStringProperty(e.getValue().getTenDVT()));
        colTrangThai.setCellValueFactory(e-> new SimpleStringProperty(e.getValue().isDelete()? "Đã xóa":"Đang hoạt động"));
    }

    private void loadTable() {
            listDVT = donViTinh_dao.getTatCaDonViTinh();

            // Loại bỏ các đơn vị đã xóa
            listDVT.removeIf(DonViTinhDTO::isDelete);

            // Cập nhật dữ liệu cho TableView
            tableThuoc.getItems().setAll(listDVT);

    }
}

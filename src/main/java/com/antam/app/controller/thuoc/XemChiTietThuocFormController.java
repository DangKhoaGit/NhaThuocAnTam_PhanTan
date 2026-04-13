package com.antam.app.controller.thuoc;

import com.antam.app.connect.ConnectDB;
import com.antam.app.dao.impl.LoThuoc_DAO;
import com.antam.app.entity.LoThuoc;
import com.antam.app.entity.Thuoc;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class XemChiTietThuocFormController extends DialogPane {

    private Text txtMaThuoc_CTT, txtTenThuoc_CTT;
    private TableView<LoThuoc> tableChiTietThuoc;

    private TableColumn<LoThuoc, Integer> colSTT_CTT = new TableColumn<>("STT");
    private TableColumn<LoThuoc, String> colMaPN_CTT = new TableColumn<>("Mã phiếu nhập");
    private TableColumn<LoThuoc, String> colSoLuong_CTT = new TableColumn<>("Số lượng");
    private TableColumn<LoThuoc, String> colNSX_CTT = new TableColumn<>("Ngày sản xuất");
    private TableColumn<LoThuoc, String> colNHH_CTT = new TableColumn<>("Ngày hết hạn");

    private ObservableList<LoThuoc> listChiTietThuoc = FXCollections.observableArrayList();

    private Thuoc thuoc;
    private LoThuoc_DAO chiTietThuoc_dao = new LoThuoc_DAO();

    public void setThuoc(Thuoc thuoc) {
        this.thuoc = thuoc;
    }

    public Thuoc getThuoc() {
        return thuoc;
    }

    /** Load dữ liệu */
    public void showData() {
        txtMaThuoc_CTT.setText("Mã Thuốc: " + thuoc.getMaThuoc());
        txtTenThuoc_CTT.setText("Tên Thuốc: " + thuoc.getTenThuoc());

        ArrayList<LoThuoc> list = chiTietThuoc_dao.getAllCHiTietThuocTheoMaThuoc(thuoc.getMaThuoc());
        listChiTietThuoc.clear();
        listChiTietThuoc.addAll(list);
        tableChiTietThuoc.setItems(listChiTietThuoc);
    }

    /** Constructor UI thuần Java */
    public XemChiTietThuocFormController() {
        this.setPrefSize(800,600);

        /* ================= HEADER ================ */
        FlowPane header = new FlowPane();
        header.setAlignment(javafx.geometry.Pos.CENTER);
        header.setStyle("-fx-background-color: #1e3a8a;");

        Text title = new Text("Chi tiết Thuốc");
        title.setFill(Color.WHITE);
        title.setFont(Font.font("System Bold", 15));
        FlowPane.setMargin(title, new Insets(10, 0, 10, 0));

        header.getChildren().add(title);
        this.setHeader(header);

        /* ================ CONTENT ================ */

        AnchorPane root = new AnchorPane();
        VBox container = new VBox(10);
        container.setPadding(new Insets(10));

        AnchorPane.setLeftAnchor(container, 0.0);
        AnchorPane.setRightAnchor(container, 0.0);

        /* ========== THÔNG TIN THUỐC ========== */

        VBox infoBox = new VBox();
        infoBox.setStyle("-fx-background-color: #2563eb; -fx-background-radius: 5px;");
        infoBox.setPadding(new Insets(10, 100, 10, 10));

        txtMaThuoc_CTT = new Text("Mã Thuốc:");
        txtMaThuoc_CTT.setFill(Color.WHITE);
        txtMaThuoc_CTT.setFont(Font.font("System Bold", 20));

        txtTenThuoc_CTT = new Text("Tên Thuốc:");
        txtTenThuoc_CTT.setFill(Color.WHITE);
        txtTenThuoc_CTT.setFont(Font.font(19));

        infoBox.getChildren().addAll(txtMaThuoc_CTT, txtTenThuoc_CTT);

        /* ========== LABEL DANH SÁCH ========== */

        Text label = new Text("Danh sách chi tiết thuốc:");

        /* ========== TABLEVIEW ========== */

        tableChiTietThuoc = new TableView<>();
        tableChiTietThuoc.setPrefWidth(200);

        // Gộp cột
        tableChiTietThuoc.getColumns().addAll(
                colSTT_CTT, colMaPN_CTT, colSoLuong_CTT, colNSX_CTT, colNHH_CTT
        );

        tableChiTietThuoc.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        /* ========== ADD ALL ========== */
        container.getChildren().addAll(infoBox, label, tableChiTietThuoc);
        root.getChildren().add(container);
        this.setContent(root);

        // Load CSS
        this.getStylesheets().add(getClass().getResource("/com/antam/app/styles/dashboard_style.css").toExternalForm());

        // Button Hủy (đóng dialog)
        ButtonType cancelButton = new ButtonType("Huỷ", ButtonData.CANCEL_CLOSE);
        this.getButtonTypes().add(cancelButton);

        /* ========== KẾT NỐI DB ========== */
        try {
            Connection con = ConnectDB.getInstance().connect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        /* ========== BIND CỘT ========== */

        colSTT_CTT.setCellValueFactory(
                c -> new ReadOnlyObjectWrapper<>(tableChiTietThuoc.getItems().indexOf(c.getValue()) + 1)
        );

        colMaPN_CTT.setCellValueFactory(
                c -> new SimpleStringProperty(c.getValue().getMaPN().getMaPhieuNhap())
        );

        colSoLuong_CTT.setCellValueFactory(new PropertyValueFactory<>("soLuong"));
        colNSX_CTT.setCellValueFactory(new PropertyValueFactory<>("ngaySanXuat"));
        colNHH_CTT.setCellValueFactory(new PropertyValueFactory<>("hanSuDung"));
    }
}

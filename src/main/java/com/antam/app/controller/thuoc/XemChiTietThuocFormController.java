package com.antam.app.controller.thuoc;

import com.antam.app.service.I_LoThuoc_Service;
import com.antam.app.service.impl.LoThuoc_Service;
import com.antam.app.dto.LoThuocDTO;
import com.antam.app.dto.ThuocDTO;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.ArrayList;

public class XemChiTietThuocFormController extends DialogPane {

    private Text txtMaThuoc_CTT, txtTenThuoc_CTT;
    private TableView<LoThuocDTO> tableChiTietThuoc;

    private TableColumn<LoThuocDTO, Integer> colSTT_CTT = new TableColumn<>("STT");
    private TableColumn<LoThuocDTO, String> colMaPN_CTT = new TableColumn<>("Mã phiếu nhập");
    private TableColumn<LoThuocDTO, String> colSoLuong_CTT = new TableColumn<>("Số lượng");
    private TableColumn<LoThuocDTO, String> colNSX_CTT = new TableColumn<>("Ngày sản xuất");
    private TableColumn<LoThuocDTO, String> colNHH_CTT = new TableColumn<>("Ngày hết hạn");

    private ObservableList<LoThuocDTO> listChiTietThuoc = FXCollections.observableArrayList();

    private ThuocDTO thuocDTO;
    private final I_LoThuoc_Service loThuocService = new LoThuoc_Service();

    public void setThuoc(ThuocDTO thuocDTO) {
        this.thuocDTO = thuocDTO;
    }

    public ThuocDTO getThuoc() {
        return thuocDTO;
    }

    /** Load dữ liệu */
    public void showData() {
        txtMaThuoc_CTT.setText("Mã Thuốc: " + thuocDTO.getMaThuoc());
        txtTenThuoc_CTT.setText("Tên Thuốc: " + thuocDTO.getTenThuoc());

        ArrayList<LoThuocDTO> list = loThuocService.getAllCHiTietThuocTheoMaThuoc(thuocDTO.getMaThuoc());
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

        /* ========== BIND CỘT ========== */

        colSTT_CTT.setCellValueFactory(
                c -> new ReadOnlyObjectWrapper<>(tableChiTietThuoc.getItems().indexOf(c.getValue()) + 1)
        );

        colMaPN_CTT.setCellValueFactory(
                c -> new SimpleStringProperty(formatDanhSachMaPN(c.getValue()))
        );

        colSoLuong_CTT.setCellValueFactory(c -> new SimpleStringProperty(String.valueOf(c.getValue().getSoLuong())));
        colNSX_CTT.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getNgaySanXuat() == null ? "" : c.getValue().getNgaySanXuat().toString()));
        colNHH_CTT.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getHanSuDung() == null ? "" : c.getValue().getHanSuDung().toString()));
    }

    private String formatDanhSachMaPN(LoThuocDTO loThuocDTO) {
        if (loThuocDTO == null || loThuocDTO.getMaCTPN() == null || loThuocDTO.getMaCTPN().isEmpty()) {
            return "";
        }
        return loThuocDTO.getMaCTPN().stream()
                .map(ctpn -> ctpn == null || ctpn.getMaPN() == null ? null : ctpn.getMaPN().getMaPhieuNhap())
                .filter(value -> value != null && !value.isBlank())
                .distinct()
                .reduce((a, b) -> a + ", " + b)
                .orElse("");
    }
}

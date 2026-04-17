/*
 * @ (#) SuaKhachHangController.java   1.0 28/10/25
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */
package com.antam.app.controller.khachhang;

import com.antam.app.service.impl.KhachHang_Service;
import com.antam.app.dto.KhachHangDTO;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/*
 * @description Controller for updating customer information
 * @author: Tran Tuan Hung
 * @date: 28/10/25
 * @version: 1.0
 */
public class SuaKhachHangFormController extends DialogPane {

    private TextField txtMaKhachHang;
    private TextField txtTenKhachHang;
    private TextField txtSoDienThoai;
    private Button btnLuu;
    private Button btnHuy;

    private KhachHangDTO khachHangDTO;
    private KhachHang_Service khachHangDAO;

    // Callback interface để thông báo khi lưu thành công
    public interface OnSaveListener {
        void onSaveSuccess();
    }

    private OnSaveListener onSaveListener;

    public SuaKhachHangFormController(){
        this.setPrefSize(800, 600);
        FlowPane header = new FlowPane();
        header.setAlignment(javafx.geometry.Pos.CENTER);
        header.setStyle("-fx-background-color: #1e3a8a;");
        Text headerText = new Text("Cập Nhật Khách Hàng");
        headerText.setFont(Font.font("System Bold", 15));
        headerText.setFill(javafx.scene.paint.Color.WHITE);
        header.setPadding(new Insets(10,0,10,0));
        header.getChildren().add(headerText);
        this.setHeader(header);

        // Content container
        VBox root = new VBox(20);
        root.setStyle("-fx-background-color: white;");
        root.setPadding(new Insets(20));

        // Mã khách hàng (không cho sửa)
        VBox vbMaKH = new VBox(5);
        Label lblMaKH = new Label("Mã khách hàng:");
        lblMaKH.setFont(Font.font("System Bold", 13));
        txtMaKhachHang = new TextField();
        txtMaKhachHang.setPromptText("Mã khách hàng");
        txtMaKhachHang.setDisable(true);
        txtMaKhachHang.setStyle("-fx-background-color: #f3f4f6; -fx-border-color: #e5e7eb; -fx-border-radius: 5px; -fx-background-radius: 5px;");
        txtMaKhachHang.setFont(Font.font(13));
        vbMaKH.getChildren().addAll(lblMaKH, txtMaKhachHang);

        // Tên khách hàng
        VBox vbTenKH = new VBox(5);
        Label lblTenKH = new Label("Tên khách hàng: *");
        lblTenKH.setFont(Font.font("System Bold", 13));
        txtTenKhachHang = new TextField();
        txtTenKhachHang.setPromptText("Nhập tên khách hàng");
        txtTenKhachHang.setStyle("-fx-border-color: #e5e7eb; -fx-border-radius: 5px; -fx-background-radius: 5px;");
        txtTenKhachHang.setFont(Font.font(13));
        vbTenKH.getChildren().addAll(lblTenKH, txtTenKhachHang);

        // Số điện thoại
        VBox vbSDT = new VBox(5);
        Label lblSDT = new Label("Số điện thoại: *");
        lblSDT.setFont(Font.font("System Bold", 13));
        txtSoDienThoai = new TextField();
        txtSoDienThoai.setPromptText("Nhập số điện thoại");
        txtSoDienThoai.setStyle("-fx-border-color: #e5e7eb; -fx-border-radius: 5px; -fx-background-radius: 5px;");
        txtSoDienThoai.setFont(Font.font(13));
        vbSDT.getChildren().addAll(lblSDT, txtSoDienThoai);

        // Buttons
        FlowPane btnBox = new FlowPane();
        btnBox.setAlignment(javafx.geometry.Pos.CENTER);
        btnBox.setHgap(10);
        btnLuu = new Button("Lưu");
        btnLuu.setPrefSize(100,35);
        btnLuu.setStyle("-fx-background-color: #2563eb; -fx-background-radius: 5px; -fx-text-fill: white;");
        btnLuu.setFont(Font.font("System Bold", 13));

        btnHuy = new Button("Hủy");
        btnHuy.setPrefSize(100,35);
        btnHuy.setStyle("-fx-background-color: #64748b; -fx-background-radius: 5px; -fx-text-fill: white;");
        btnHuy.setFont(Font.font("System Bold", 13));

        btnBox.getChildren().addAll(btnLuu, btnHuy);

        root.getChildren().addAll(vbMaKH, vbTenKH, vbSDT, btnBox);
        this.setContent(root);

        // Xử lý sự kiện đóng cửa sổ (nút X)
        this.sceneProperty().addListener((obsScene, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.windowProperty().addListener((obsWindow, oldWindow, window) -> {
                    if (window != null) {
                        window.setOnCloseRequest(ev -> {
                            window.hide();
                        });
                    }
                });
            }
        });

        // Sự kiện
        // Khởi tạo DAO
        khachHangDAO = new KhachHang_Service();

        // Thiết lập sự kiện cho nút Lưu
        btnLuu.setOnAction(event -> handleLuu());

        // Thiết lập sự kiện cho nút Hủy
        btnHuy.setOnAction(event -> handleHuy());
    }

    /**
     * Thiết lập callback khi lưu thành công
     */
    public void setOnSaveListener(OnSaveListener listener) {
        this.onSaveListener = listener;
    }

    /**
     * Thiết lập dữ liệu khách hàng để hiển thị trong form
     */
    public void setKhachHang(KhachHangDTO kh) {
        this.khachHangDTO = kh;
        if (khachHangDTO != null) {
            txtMaKhachHang.setText(khachHangDTO.getMaKH());
            txtTenKhachHang.setText(khachHangDTO.getTenKH());
            txtSoDienThoai.setText(khachHangDTO.getSoDienThoai());
        }
    }

    /**
     * Xử lý sự kiện click nút Lưu
     */
    private void handleLuu() {
        // Kiểm tra dữ liệu đầu vào
        if (txtTenKhachHang.getText().trim().isEmpty()) {
            showError("Lỗi", "Tên khách hàng không được để trống");
            return;
        }

        if (txtSoDienThoai.getText().trim().isEmpty()) {
            showError("Lỗi", "Số điện thoại không được để trống");
            return;
        }

        // Kiểm tra định dạng số điện thoại (đơn giản: chỉ chứa số)
        if (!txtSoDienThoai.getText().matches("\\d+")) {
            showError("Lỗi", "Số điện thoại phải chỉ chứa các chữ số");
            return;
        }

        try {
            // Cập nhật thông tin khách hàng
            khachHangDTO.setTenKH(txtTenKhachHang.getText().trim());
            khachHangDTO.setSoDienThoai(txtSoDienThoai.getText().trim());

            // Lưu vào database
            boolean success = khachHangDAO.updateKhachHang(khachHangDTO);

            if (success) {
                showInfo("Thành công", "Cập nhật thông tin khách hàng thành công");

                // Gọi callback khi lưu thành công
                if (onSaveListener != null) {
                    onSaveListener.onSaveSuccess();
                }

                // Đóng dialog
                btnHuy.getScene().getWindow().hide();
            } else {
                showError("Lỗi", "Không thể cập nhật thông tin khách hàng");
            }

        } catch (Exception e) {
            showError("Lỗi", "Lỗi khi cập nhật: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Xử lý sự kiện click nút Hủy
     */
    private void handleHuy() {
        // Đóng dialog mà không lưu
        btnHuy.getScene().getWindow().hide();
    }

    /**
     * Hiển thị dialog lỗi
     */
    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Hiển thị dialog thông tin
     */
    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

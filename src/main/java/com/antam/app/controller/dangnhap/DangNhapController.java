//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.antam.app.controller.dangnhap;

import com.antam.app.controller.khungchinh.KhungChinhController;
import com.antam.app.dto.NhanVienDTO;
import com.antam.app.dto.PhienNguoiDungDTO;
import com.antam.app.network.ClientManager;
import com.antam.app.network.message.Response;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;

public class DangNhapController extends StackPane {
    private final Button btnLogin = new Button("Đăng nhập");
    private final TextField txtname_login = new TextField();
    private final PasswordField txtpass_login = new PasswordField();
    private final Text notification_login = new Text();

    public DangNhapController() {
        initUI();
        initEvents();
    }

    private void initUI() {
        this.setAlignment(Pos.CENTER);

        VBox formBox = new VBox(10);
        formBox.setAlignment(Pos.TOP_CENTER);
        formBox.setMaxSize(400, 520);
        formBox.setPadding(new Insets(20, 30, 20, 30));
        formBox.setStyle("-fx-background-color: white; -fx-background-radius: 15; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 5);");

        // Logo
        try {
            ImageView logo = new ImageView(new Image(getClass().getResourceAsStream("/com/antam/app/images/logo.png")));
            logo.setFitWidth(100);
            logo.setFitHeight(120);
            logo.setPreserveRatio(true);
            formBox.getChildren().add(logo);
        } catch (Exception e) {
            System.err.println("Không tìm thấy ảnh logo");
        }

        Text title = new Text("Hệ thống Quản lý Nhà thuốc");
        title.setFill(Color.web("#1e3a8a"));
        title.setFont(Font.font("System", FontWeight.BOLD, 22));

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setAlignment(Pos.TOP_LEFT);

        ColumnConstraints col = new ColumnConstraints();
        col.setHgrow(Priority.ALWAYS);
        grid.getColumnConstraints().add(col);

        // Username field
        Text lblUser = new Text("Tên tài khoản:");
        lblUser.setFont(Font.font("System", FontWeight.BOLD, 14));
        txtname_login.setPromptText("Nhập tài khoản");
        txtname_login.setPrefHeight(40);
        txtname_login.getStyleClass().add("input");

        // Password field
        Text lblPass = new Text("Mật khẩu:");
        lblPass.setFont(Font.font("System", FontWeight.BOLD, 14));
        txtpass_login.setPromptText("Nhập mật khẩu");
        txtpass_login.setPrefHeight(40);
        txtpass_login.getStyleClass().add("input");

        grid.add(lblUser, 0, 0);
        grid.add(txtname_login, 0, 1);
        grid.add(lblPass, 0, 2);
        grid.add(txtpass_login, 0, 3);

        notification_login.setFont(Font.font("System", 13));
        notification_login.setWrappingWidth(340);

        btnLogin.setPrefWidth(400);
        btnLogin.setPrefHeight(45);
        btnLogin.setCursor(javafx.scene.Cursor.HAND);
        btnLogin.getStyleClass().add("btn-login");
        btnLogin.setStyle("-fx-background-color: #1e3a8a; -fx-text-fill: white; -fx-font-weight: bold;");

        formBox.getChildren().addAll(title, grid, notification_login, btnLogin);
        this.getChildren().add(formBox);

        // Load CSS
        this.getStylesheets().addAll(getClass().getResource("/com/antam/app/styles/authentication_style.css").toExternalForm());

        this.getStyleClass().add("page");
    }

    private void initEvents() {
        // Nút đăng nhập
        btnLogin.setOnAction(e -> onLoginButtonClick());

        // Nhấn Enter để đăng nhập
        this.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                onLoginButtonClick();
            }
        });

        // Xóa thông báo lỗi khi người dùng bắt đầu nhập lại
        txtname_login.textProperty().addListener((obs, old, newVal) -> clearNotification());
        txtpass_login.textProperty().addListener((obs, old, newVal) -> clearNotification());
    }

    protected void onLoginButtonClick() {
        if (!checkValidation()) return;

        notification_login.setText("Đang xác thực thông tin...");
        notification_login.setFill(Color.BLUE);
        btnLogin.setDisable(true);

        Task<Response> loginTask = new Task<>() {
            @Override
            protected Response call() throws Exception {
                // Gửi yêu cầu đăng nhập lên Server (Server sẽ dùng BCrypt để check)
                return ClientManager.getInstance().login(
                        txtname_login.getText().trim(),
                        txtpass_login.getText()
                );
            }
        };

        loginTask.setOnSucceeded(e -> {
            Response response = loginTask.getValue();
            if (response != null && response.isSuccess()) {
                handleLoginSuccess(response);
            } else {
                String msg = (response != null) ? response.getMessage() : "Sai tài khoản hoặc mật khẩu";
                showError(msg);
            }
        });

        loginTask.setOnFailed(e -> {
            showError("Không thể kết nối đến máy chủ. Vui lòng kiểm tra mạng.");
            e.getSource().getException().printStackTrace();
        });

        Thread thread = new Thread(loginTask);
        thread.setDaemon(true);
        thread.start();
    }

    private void handleLoginSuccess(Response response) {
        try {
            // Lấy dữ liệu nhân viên từ Response data
            Object data = response.getData();
            if (data instanceof NhanVienDTO) {
                NhanVienDTO nv = (NhanVienDTO) data;
                PhienNguoiDungDTO.setMaNV(nv);
                System.out.println("Đăng nhập thành công: " + nv);

                // Chuyển sang màn hình chính
                Platform.runLater(() -> {
                    try {
                        KhungChinhController mainController = new KhungChinhController();
                        Scene scene = new Scene(mainController);
                        Stage stage = (Stage) this.getScene().getWindow();
                        stage.setScene(scene);
                        stage.setMaximized(true);
                        stage.show();
                    } catch (Exception ex) {
                        showError("Lỗi khi tải giao diện chính: " + ex.getMessage());
                    }
                });
            } else {
                showError("Dữ liệu phản hồi không hợp lệ.");
            }
        } catch (Exception ex) {
            showError("Có lỗi xảy ra sau khi đăng nhập.");
        }
    }

    private boolean checkValidation() {
        if (txtname_login.getText().trim().isEmpty()) {
            showError("Vui lòng nhập tên tài khoản");
            txtname_login.requestFocus();
            return false;
        }
        if (txtpass_login.getText().isEmpty()) {
            showError("Vui lòng nhập mật khẩu");
            txtpass_login.requestFocus();
            return false;
        }
        return true;
    }

    private void showError(String message) {
        notification_login.setText(message);
        notification_login.setFill(Color.RED);
        btnLogin.setDisable(false);
    }

    private void clearNotification() {
        if (!notification_login.getText().isEmpty()) {
            notification_login.setText("");
        }
    }
}
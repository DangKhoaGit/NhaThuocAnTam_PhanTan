//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.antam.app.controller.dangnhap;

import com.antam.app.connect.ConnectDB;
import com.antam.app.controller.khungchinh.KhungChinhController;
import com.antam.app.dao.impl.NhanVien_DAO;
import com.antam.app.entity.PhienNguoiDung;
import com.antam.app.helper.MaKhoaMatKhau;
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

import java.sql.Connection;
import java.sql.SQLException;

public class DangNhapController extends StackPane {
    private NhanVien_DAO nv_dao = new NhanVien_DAO();
    private Button btnLogin = new Button("Đăng nhập");
    private TextField txtname_login = new TextField();
    private PasswordField txtpass_login = new PasswordField();
    private Text notification_login = new Text();

    public DangNhapController() {
        /** Giao diện **/
        this.setAlignment(Pos.CENTER);

        VBox formBox = new VBox(10);
        formBox.setAlignment(Pos.TOP_CENTER);
        formBox.setMaxSize(400, 500);
        formBox.setPadding(new Insets(10, 20, 10, 20));
        formBox.getStyleClass().add("form-box");
        formBox.setStyle("-fx-background-color: white; -fx-background-radius: 15; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 5);");

        ImageView logo = new ImageView(new Image(getClass().getResourceAsStream("/com/antam/app/images/logo.png")));
        logo.setFitWidth(100);
        logo.setFitHeight(150);
        logo.setPreserveRatio(true);

        Text title = new Text("Hệ thống Quản lý Nhà thuốc");
        title.setFill(Color.web("#1e3a8a"));
        title.setFont(Font.font("System", FontWeight.BOLD, 25));

        ColumnConstraints col0 = new ColumnConstraints();
        col0.setHgrow(Priority.SOMETIMES);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(15);
        grid.setAlignment(Pos.TOP_LEFT);
        grid.getColumnConstraints().add(col0);


        Text lblUser = new Text("Tên tài khoản:");
        lblUser.setFill(Color.web("#374151"));
        lblUser.setFont(Font.font("System", FontWeight.BOLD, 15));
        grid.add(lblUser, 0, 0);

        txtname_login.setPromptText("Nhập tài khoản của bạn");
        txtname_login.setFocusTraversable(false);
        txtname_login.getStyleClass().add("input");
        txtname_login.setPrefHeight(45);
        txtname_login.getStyleClass().add("input");
        grid.add(txtname_login, 0, 1);

        Text lblPass = new Text("Mật khẩu:");
        lblPass.setFill(Color.web("#374151"));
        lblPass.setFont(Font.font("System", FontWeight.BOLD, 15));
        grid.add(lblPass, 0, 2);

        txtpass_login.setPromptText("Nhập mật khẩu");
        txtpass_login.setFocusTraversable(false);
        txtpass_login.getStyleClass().add("input");
        txtpass_login.setPrefHeight(45);
        txtpass_login.getStyleClass().add("input");
        grid.add(txtpass_login, 0, 3);

        notification_login.setFill(Color.RED);
        notification_login.setFont(Font.font(10));

        btnLogin.setPrefWidth(400);
        btnLogin.setPrefHeight(45);
        btnLogin.getStyleClass().add("btn-login");
        btnLogin.setFont(Font.font("System", FontWeight.BOLD, 15));
        btnLogin.getStyleClass().add("btn-login");

        formBox.getChildren().addAll(
                logo,
                title,
                grid,
                notification_login,
                btnLogin
        );

        this.getChildren().add(formBox);

        this.getStylesheets().addAll(getClass().getResource("/com/antam/app/styles/authentication_style.css").toExternalForm());
        this.getStyleClass().add("page");

        /** Sự kiện **/

        btnLogin.setOnAction(e -> {
            onLoginButtonClick();
        });
        txtname_login.setOnKeyPressed(e -> {
            checklogin();
        });
        txtpass_login.setOnKeyPressed(e -> {
            checklogin();
        });
    }

    protected void onLoginButtonClick() {
        if (checklogin()) {
            if (checktnhanvien()) {
                PhienNguoiDung.setMaNV(nv_dao.getNhanVienTaiKhoan(txtname_login.getText()));
                // rào cho trường hợp đăng xuất xong đăng nhập lại
                if (PhienNguoiDung.getMaNV() == null){
                    notification_login.setText("Tài khoản không hợp lệ");
                    return;
                }
                Scene scene = new Scene(new KhungChinhController());
                Stage stage = (Stage) this.getScene().getWindow();
                stage.setScene(scene);
                stage.setMaximized(true);
                stage.show();
            }
        }
        //Dùng để tắt chức năng đăng nhập
//        try {
//            FXMLLoader fxmlLoader = new FXMLLoader(GiaoDienChinh.class.getResource("/com/antam/app/views/dashboard.fxml"));
//            Parent root = (Parent) fxmlLoader.load();
//            Stage newStage = new Stage();
//            newStage.setTitle("");
//            newStage.setScene(new Scene(root));
//            newStage.setMaximized(true);
//            newStage.initStyle(StageStyle.DECORATED);
//            newStage.show();
//            Stage oldStage = (Stage) this.btnLogin.getScene().getWindow();
//            oldStage.close();
//            PhienNguoiDung.setMaNV(nv_dao.getNhanVienTaiKhoan("Admin001"));
//        } catch (Exception var5) {
//            Exception e = var5;
//            e.printStackTrace();
//        }
    }
    private boolean checklogin(){
        // Kiểm tra tên đăng nhập
        if (txtname_login.getText().isEmpty()){
            notification_login.setText("Vui lòng nhập tên đăng nhập");
            notification_login.setStyle("-fx-text-fill: red;");
            return false;
        } else {
//            if (!txtname_login.getText().matches("^(?=.*[A-Z])(?=.*\\d)[^\\s]{8,}$")){
//                notification_login.setText("Tên đăng nhập phải chứa ít nhất 1 chữ cái viết hoa, 1 số và 8 ký tự");
//                if (!txtname_login.isFocused()) {
//                    txtname_login.requestFocus();
//                }
//                return false;
//            }
        }

        // Kiểm tra mật khẩu
        if (txtpass_login.getText().isEmpty()) {
            notification_login.setText("Vui lòng nhập mật khẩu");
            return false;
        } else {
//            if (!txtpass_login.getText().matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^\\w])[^\\s]{8,}$")){
//                notification_login.setText("Mật khẩu phải có 8 ký tự, 1 chữ hoa, 1 chữ thường, 1 số và 1 ký tự đặc biệt.");
//                if (!txtpass_login.isFocused()) {
//                    txtpass_login.requestFocus();
//                }
//                return false;
//            }
        }

        notification_login.setText("");
        return true;
    }

    private boolean checktnhanvien(){
        try {
            Connection con = ConnectDB.getInstance().connect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        nv_dao = new NhanVien_DAO();
        String id = txtname_login.getText();
        String pass = txtpass_login.getText();
        if (nv_dao.getNhanVienTaiKhoan(id) == null){
            notification_login.setText("Tên đăng nhập không tồn tại");
            return false;
        }else if (!MaKhoaMatKhau.verifyPassword(pass,nv_dao.getNhanVienTaiKhoan(id).getMatKhau())) {
            System.out.println("false");
            notification_login.setText("Mật khẩu không đúng");
            return false;
        }
        return true;
    }

}
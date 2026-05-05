package com.antam.app.controller.caidattaikhoan;

import com.antam.app.dto.PhienNguoiDungDTO;
import com.antam.app.helper.MaKhoaMatKhau;
import com.antam.app.network.ClientManager;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcons;
import javafx.concurrent.Task;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class CaiDatTaiKhoanController extends ScrollPane{

    private TextField txtMKnow, txtMKnew;
    private Button btnDoiMK;
    private Text txtTK, txtVaiTro;

    private ClientManager clientManager = ClientManager.getInstance();

    public CaiDatTaiKhoanController() {
        /** Giao diện **/
        this.setFitToHeight(true);
        this.setFitToWidth(true);
        this.setPrefSize(900, 730);
        AnchorPane.setTopAnchor(this, 0.0);
        AnchorPane.setBottomAnchor(this, 0.0);
        AnchorPane.setLeftAnchor(this, 0.0);
        AnchorPane.setRightAnchor(this, 0.0);

        VBox rootVBox = new VBox(30);
        rootVBox.setStyle("-fx-background-color: #f8fafc;");
        rootVBox.setPadding(new Insets(20));

        // Header
        HBox header = new HBox(5);
        header.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        Text title = new Text("Cài đặt tài khoản");
        title.setFont(Font.font("System Bold", 30));
        title.setFill(Color.web("#1e3a8a"));
        header.getChildren().addAll(title, new HBox()); // spacer
        HBox.setHgrow(header.getChildren().get(1), Priority.ALWAYS);
        rootVBox.getChildren().add(header);

        FlowPane flowPane = new FlowPane();
        flowPane.setHgap(20);
        flowPane.setVgap(20);
        flowPane.setAlignment(javafx.geometry.Pos.TOP_CENTER);

        // VBox đổi mật khẩu
        VBox vboxDoiMK = new VBox(10);
        vboxDoiMK.setPrefWidth(500);
        vboxDoiMK.setStyle("-fx-background-color: white; -fx-background-radius: 10px;");
        DropShadow ds = new DropShadow();
        ds.setOffsetX(3);
        ds.setOffsetY(2);
        ds.setRadius(12.93);
        ds.setColor(Color.rgb(218, 218, 218));
        vboxDoiMK.setEffect(ds);
        vboxDoiMK.setPadding(new Insets(10));

        Text lblDoiMK = new Text("Đổi mật khẩu");
        lblDoiMK.setFont(Font.font("System Bold", 20));
        lblDoiMK.setFill(Color.web("#1e3a8a"));

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setHgrow(Priority.SOMETIMES);
        GridPane gridDoiMK = new GridPane();
        gridDoiMK.setHgap(5);
        gridDoiMK.setVgap(5);
        gridDoiMK.getColumnConstraints().add(col1);

        // Mật khẩu hiện tại
        Text txtLblCurrent = new Text("Mật khẩu hiện tại:");
        txtMKnow = new TextField();
        txtMKnow.setPrefHeight(40);

        // Xác nhận mật khẩu mới
        Text txtLblNew = new Text("Xác nhận mật khẩu mới:");
        txtMKnew = new TextField();
        txtMKnew.setPrefHeight(40);

        // Button đổi mật khẩu
        FontAwesomeIcon iconDoiMK = new FontAwesomeIcon();
        iconDoiMK.setIcon(FontAwesomeIcons.EXCHANGE);
        iconDoiMK.setFill(Color.WHITE);
        btnDoiMK = new Button("Đổi mật khẩu");
        btnDoiMK.getStyleClass().add("btn-primary");
        btnDoiMK.setPrefHeight(40);
        btnDoiMK.setGraphic(iconDoiMK);

        gridDoiMK.add(txtLblCurrent, 0, 0);
        gridDoiMK.add(txtMKnow, 0, 1);
        gridDoiMK.add(txtLblNew, 0, 2);
        gridDoiMK.add(txtMKnew, 0, 3);
        gridDoiMK.add(btnDoiMK, 0, 4);

        vboxDoiMK.getChildren().addAll(lblDoiMK, gridDoiMK);

        // VBox thông tin tài khoản
        VBox vboxInfo = new VBox(10);
        vboxInfo.setPrefWidth(500);
        vboxInfo.setStyle("-fx-background-color: white; -fx-background-radius: 10px;");
        vboxInfo.setEffect(ds);
        vboxInfo.setPadding(new Insets(10));

        Text lblInfo = new Text("Thông tin tài khoản");
        lblInfo.setFont(Font.font("System Bold", 20));
        lblInfo.setFill(Color.web("#1e3a8a"));

        VBox infoBox = new VBox(10);
        infoBox.setPadding(new Insets(10));

        // Tên đăng nhập
        HBox hBoxTK = new HBox(5);
        hBoxTK.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        hBoxTK.setStyle("-fx-background-color: #f8fafc; -fx-background-radius: 6px;");
        hBoxTK.setPrefHeight(40);
        Text lblTK = new Text("Tên đăng nhập:");
        lblTK.setFont(Font.font("System", FontWeight.BOLD, 13));
        txtTK = new Text("Admin");
        txtTK.setFont(Font.font(13));
        hBoxTK.getChildren().addAll(lblTK, txtTK);

        // Vai trò
        HBox hBoxVaiTro = new HBox(5);
        hBoxVaiTro.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        hBoxVaiTro.setStyle("-fx-background-color: #f8fafc; -fx-background-radius: 6px;");
        hBoxVaiTro.setPrefHeight(40);
        Text lblRole = new Text("Vai trò: ");
        lblRole.setFont(Font.font("System", FontWeight.BOLD, 13));
        txtVaiTro = new Text("Quản trị viên");
        txtVaiTro.setFont(Font.font(13));
        hBoxVaiTro.getChildren().addAll(lblRole, txtVaiTro);

        infoBox.getChildren().addAll(hBoxTK, hBoxVaiTro);
        vboxInfo.getChildren().addAll(lblInfo, infoBox);

        flowPane.getChildren().addAll(vboxDoiMK, vboxInfo);
        rootVBox.getChildren().add(flowPane);
        this.setContent(rootVBox);
        /** Sự kiện **/
        loadThongTin();

        // Sự kiện kiểm tra mật khẩu cũ ngay khi nhập
        txtMKnow.textProperty().addListener((obs, oldText, newText) -> {
            checkPassword();
        });

        // Sự kiện đổi mật khẩu
        btnDoiMK.setOnAction(e -> {
            doiMKButtonClick();
        });
    }


    private void checkPassword() {
        if (PhienNguoiDungDTO.getMaNV() == null) return;

        String mkNow = txtMKnow.getText();
        boolean isCorrect = MaKhoaMatKhau.verifyPassword(
                mkNow,
                PhienNguoiDungDTO.getMaNV().getMatKhau()
        );

        if (isCorrect) {
            txtMKnow.setStyle("-fx-border-color: green; -fx-border-width: 2px;");
        } else {
            txtMKnow.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
        }
    }

    private void doiMKButtonClick() {
        if (PhienNguoiDungDTO.getMaNV() == null) {
            showAlert("Lỗi", "Không xác định được người dùng hiện tại!");
            return;
        }

        String mkNow = txtMKnow.getText();
        String mkNew = txtMKnew.getText();

        // Kiểm tra mật khẩu cũ
        if (!MaKhoaMatKhau.verifyPassword(mkNow, PhienNguoiDungDTO.getMaNV().getMatKhau())) {
            showAlert("Lỗi", "Mật khẩu hiện tại không đúng!");
            return;
        }

        if (mkNew.isEmpty() || mkNew.length() < 6) {
            showAlert("Cảnh báo", "Mật khẩu mới phải có ít nhất 6 ký tự!");
            return;
        }

        // Mã hóa và cập nhật
        String hashCode = MaKhoaMatKhau.hashPassword(mkNew, 10);
        PhienNguoiDungDTO.getMaNV().setMatKhau(hashCode);

        Task<Boolean> task = new Task<>() {
            @Override
            protected Boolean call() throws Exception {
                return ClientManager.getInstance().updateNhanVien(PhienNguoiDungDTO.getMaNV());
            }
        };
        task.setOnSucceeded(evt -> {
            boolean result = task.getValue();
            if (result) {
                showAlert("Thành công", "Đổi mật khẩu thành công!");
                txtMKnow.clear();
                txtMKnew.clear();
            } else {
                showAlert("Thất bại", "Đổi mật khẩu thất bại!");
            }
        });
        task.setOnFailed(evt -> {
            showAlert("Thất bại", "Đổi mật khẩu thất bại!");
        });
        new Thread(task).start();
    }

    private void loadThongTin() {
        if (PhienNguoiDungDTO.getMaNV() == null) return;

        String tk = PhienNguoiDungDTO.getMaNV().getTaiKhoan();
        String cv = PhienNguoiDungDTO.getMaNV().isQuanLi() ? "Nhân viên quản lí" : "Nhân viên";
        txtTK.setText(tk);
        txtVaiTro.setText(cv);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
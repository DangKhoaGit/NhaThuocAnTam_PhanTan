
package com.antam.app.controller.nhanvien;
/*
 * @description
 * @author: Pham Dang Khoa
 * @date: 29/10/2025
 * @version: 1.0
 */
import com.antam.app.dao.I_NhanVien_DAO;
import com.antam.app.dao.impl.NhanVien_DAO;
import com.antam.app.entity.NhanVien;
import com.antam.app.helper.MaKhoaMatKhau;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.ButtonBar.ButtonData;

import java.text.DecimalFormat;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class ThemNhanVienFormController extends DialogPane{

    private TextField txtMaNV, txtHoTen, txtSDT, txtEmail,txtDiaChi, txtTaiKhoan;
    private PasswordField txtPass;
    private ComboBox<String> cbChucVu;
    private Spinner<Double> luong;


    public ThemNhanVienFormController() {
        this.setPrefSize(800, 600);
        Text headerText = new Text("Thêm nhân viên mới");
        headerText.setFont(Font.font("System Bold", 15));
        headerText.setFill(javafx.scene.paint.Color.WHITE);
        FlowPane header = new FlowPane(headerText);
        header.setAlignment(javafx.geometry.Pos.CENTER);
        header.setStyle("-fx-background-color: #1e3a8a;");
        this.setHeader(header);

        // Content
        AnchorPane anchorPane = new AnchorPane();
        VBox rootVBox = new VBox(10);
        rootVBox.setPadding(new Insets(0));
        AnchorPane.setLeftAnchor(rootVBox, 0.0);
        AnchorPane.setRightAnchor(rootVBox, 0.0);

        GridPane grid = new GridPane();
        grid.setHgap(5);

        // Column constraints
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setHgrow(javafx.scene.layout.Priority.SOMETIMES);
        col1.setMinWidth(10);
        col1.setPrefWidth(100);

        ColumnConstraints col2 = new ColumnConstraints();
        col2.setHgrow(javafx.scene.layout.Priority.SOMETIMES);
        col2.setMinWidth(10);
        col2.setPrefWidth(100);

        grid.getColumnConstraints().addAll(col1, col2);

        // Row constraints
        for (int i = 0; i < 10; i++) {
            RowConstraints row = new RowConstraints();
            row.setMinHeight(10);
            row.setPrefHeight(i % 2 == 0 ? 30 : 40);
            row.setVgrow(javafx.scene.layout.Priority.SOMETIMES);
            grid.getRowConstraints().add(row);
        }

        // Controls
        int row = 1;

        // Mã NV
        grid.add(new Text("Mã nhân viên:"), 0, 0);
        txtMaNV = new TextField();
        txtMaNV.setPrefHeight(40);
        grid.add(txtMaNV, 0, row);

        // Họ tên
        grid.add(new Text("Họ tên:"), 1, 0);
        txtHoTen = new TextField();
        txtHoTen.setPrefHeight(40);
        grid.add(txtHoTen, 1, row++);

        // Số điện thoại
        grid.add(new Text("Số điện thoại:"), 0, row++);
        txtSDT = new TextField();
        txtSDT.setPrefHeight(40);
        grid.add(txtSDT, 0, row);

        // Email
        grid.add(new Text("Email:"), 1, row - 1);
        txtEmail = new TextField();
        txtEmail.setPrefHeight(40);
        grid.add(txtEmail, 1, row++);

        // Địa chỉ
        grid.add(new Text("Địa chỉ:"), 0, row++);
        txtDiaChi = new TextField();
        txtDiaChi.setPrefHeight(40);
        grid.add(txtDiaChi, 0, row);

        // Chức vụ
        grid.add(new Text("Chức vụ:"), 1, row - 1);
        cbChucVu = new ComboBox<>();
        cbChucVu.setPrefHeight(40);
        grid.add(cbChucVu, 1, row++);

        // Lương cơ bản
        grid.add(new Text("Lương cơ bản:"), 0, row++);
        luong = new Spinner<>(0.0, Double.MAX_VALUE, 0.0, 100.0);
        luong.setEditable(true);
        luong.setPrefHeight(40);
        grid.add(luong, 0, row);

        // Tài khoản
        grid.add(new Text("Tài khoản"), 1, row - 1);
        txtTaiKhoan = new TextField();
        txtTaiKhoan.setPrefHeight(40);
        grid.add(txtTaiKhoan, 1, row++);

        // Mật khẩu
        grid.add(new Text("Mật khẩu"), 0, row++);
        txtPass = new PasswordField();
        txtPass.setPrefHeight(40);
        grid.add(txtPass, 0, row);

        rootVBox.getChildren().add(grid);
        anchorPane.getChildren().add(rootVBox);
        this.setContent(anchorPane);
        /** Sự kiện **/
        ButtonType cancelButton = new ButtonType("Huỷ", ButtonData.CANCEL_CLOSE);
        ButtonType applyButton = new ButtonType("Lưu", ButtonData.APPLY);
        this.getButtonTypes().add(cancelButton);
        this.getButtonTypes().add(applyButton);

        Button btnThem = (Button) this.lookupButton(applyButton);
        Button btnHuy = (Button) this.lookupButton(applyButton);

        // Gán ValueFactory mặc định nếu chưa có
        if (luong.getValueFactory() == null) {
            SpinnerValueFactory<Double> valueFactory =
                    new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 100_000_000.0, 5_000_000.0, 500_000.0);
            luong.setValueFactory(valueFactory);
        }

        // tắt trạng thái của mã nhân viên
        txtMaNV.setText(getHashMaNV());
        txtMaNV.setEditable(false);
        //focus vào tên nhân viên
        txtHoTen.requestFocus();

        //setup cho comboxBox quản lí
        cbChucVu.getItems().add("Nhân viên");
        cbChucVu.getItems().add("Nhân viên quản lí");
        cbChucVu.getSelectionModel().selectFirst();
        //sự kiện thêm nhân viên
        btnThem.setOnAction(e -> {
            if (setupThemNhanVien() != null){
                I_NhanVien_DAO.themNhanVien(setupThemNhanVien());
                showAlert("Thêm nhân viên thành công!");
            }else{

            }
        });
        //sự kiện khi người dùng nhập sai thông tin
        txtEmail.textProperty().addListener((obs, oldText, newText) -> {
            if (newText.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")
                    || newText.isBlank()) {
                // Hợp lệ -> bỏ viền đỏ
                txtEmail.setStyle("");
            } else {
                // Sai -> tô viền đỏ
                txtEmail.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            }
        });

        //Sự kiện khi người dùng nhập sai trên số điện thoại
        txtSDT.textProperty().addListener((obs, oldText, newText) -> {
            if (newText.matches("^0\\d{9}$")
                    || newText.isBlank()) {
                // Hợp lệ -> bỏ viền đỏ
                txtSDT.setStyle("");
            } else {
                // Sai -> tô viền đỏ
                txtSDT.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            }
        });
    }

    /**
     * Chuẩn bị nhân viên trước khi đưa vào database
     * @return NhanVien
     */
    private NhanVien setupThemNhanVien() {
        // Kiểm tra nếu ValueFactory chưa được gán
        if (luong.getValueFactory() == null) {
            System.out.println("ValueFactory chưa được gán");
            return null;
        }

        Double value = luong.getValueFactory().getValue();
        double l = (value != null) ? value.doubleValue() : 0.0;

        //kiểm tra rỗng trên các textField và kiểm tra định dạng
        if (txtHoTen.getText().isBlank() ||
                txtMaNV.getText().isBlank() ||
                txtPass.getText().isBlank() ||
                txtEmail.getText().isBlank() ||
                txtSDT.getText().isBlank() ||
                txtDiaChi.getText().isBlank() ||
                txtTaiKhoan.getText().isBlank()) {
            System.out.println("Các field rỗng");
            return null;
        } else if (
                !txtSDT.getText().matches("^0\\d{9}$") ||
                        !txtEmail.getText().matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")
        ) {
            System.out.println("Không đúng định dạng");
            return null;
        }

        // độ khó là 10 phù hợp với các dự án nhỏ và làm tăng hiệu suất
        String pass = MaKhoaMatKhau.hashPassword(txtPass.getText(), 10);

        return new NhanVien(
                txtMaNV.getText(),
                txtHoTen.getText(),
                txtSDT.getText(),
                txtEmail.getText(),
                txtDiaChi.getText(),
                l,
                txtTaiKhoan.getText(),
                pass,
                isNVQuanLy()
        );
    }

    public boolean isNVQuanLy(){
        return cbChucVu.getSelectionModel()
                .getSelectedItem()
                .equals("Nhân viên quản lí") ? true : false;
    }

    /**
     * Tạo mã nhân viên tự động và tăng 1 giá trị mỗi khi thêm nhân viên.
     * @return String(Chuỗi mã nhân viên được tạo tự động)
     */
    public String getHashMaNV(){
        String hash = I_NhanVien_DAO.getMaxHashNhanVien();
        int maxHash = Integer.parseInt(hash);
        DecimalFormat deFomat = new DecimalFormat("00000");
        return String.format("NV%s",deFomat.format(++maxHash));
    }

    /**
     * Hiện thông báo cho một sự kiện với lời nhắn cho trước
     * @param message(Tin nhắn cần thông báo)
     */
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Thông báo");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
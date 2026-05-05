package com.antam.app.controller.nhanvien;/*
 * @description
 * @author: Pham Dang Khoa
 * @date: 29/10/2025
 * @version: 1.0
 */

import com.antam.app.dto.NhanVienDTO;
import com.antam.app.network.ClientManager;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.layout.FlowPane;

import static com.antam.app.controller.nhanvien.CapNhatNhanVienController.nhanVienDTOSelected;

public class CapNhatNhanVienFormController extends DialogPane{

    private TextField txtMaNV, txtHoTen, txtSDT, txtEmail,txtDiaChi, txtTaiKhoan;
    private ComboBox<String> cbChucVu;
    private Spinner<Double> luong;


    NhanVienDTO select = nhanVienDTOSelected;
    public CapNhatNhanVienFormController() {
        this.setPrefSize(800, 600);
        Text headerText = new Text("Cập nhật nhân viên");
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

        int rowIndex = 1;

        // Mã NV
        grid.add(new Text("Mã nhân viên:"), 0, 0);
        txtMaNV = new TextField();
        txtMaNV.setPrefHeight(40);
        grid.add(txtMaNV, 0, rowIndex);

        // Họ tên
        grid.add(new Text("Họ tên:"), 1, 0);
        txtHoTen = new TextField();
        txtHoTen.setPrefHeight(40);
        grid.add(txtHoTen, 1, rowIndex++);

        // Số điện thoại
        grid.add(new Text("Số điện thoại:"), 0, rowIndex++);
        txtSDT = new TextField();
        txtSDT.setPrefHeight(40);
        grid.add(txtSDT, 0, rowIndex);

        // Email
        grid.add(new Text("Email:"), 1, rowIndex - 1);
        txtEmail = new TextField();
        txtEmail.setPrefHeight(40);
        grid.add(txtEmail, 1, rowIndex++);

        // Địa chỉ
        grid.add(new Text("Địa chỉ:"), 0, rowIndex++);
        txtDiaChi = new TextField();
        txtDiaChi.setPrefHeight(40);
        grid.add(txtDiaChi, 0, rowIndex);

        // Chức vụ
        grid.add(new Text("Chức vụ:"), 1, rowIndex - 1);
        cbChucVu = new ComboBox<>();
        cbChucVu.setPrefHeight(40);
        grid.add(cbChucVu, 1, rowIndex++);

        // Lương cơ bản
        grid.add(new Text("Lương cơ bản:"), 0, rowIndex++);
        luong = new Spinner<>(0.0, Double.MAX_VALUE, 0.0, 100.0);
        luong.setEditable(true);
        luong.setPrefHeight(40);
        grid.add(luong, 0, rowIndex);

        // Tài khoản
        grid.add(new Text("Tài khoản"), 1, rowIndex - 1);
        txtTaiKhoan = new TextField();
        txtTaiKhoan.setPrefHeight(40);
        grid.add(txtTaiKhoan, 1, rowIndex++);

        rootVBox.getChildren().add(grid);
        anchorPane.getChildren().add(rootVBox);
        this.setContent(anchorPane);
        /** Sự kiện **/
        ButtonType cancelButton = new ButtonType("Huỷ", ButtonBar.ButtonData.CANCEL_CLOSE);
        ButtonType updateButton = new ButtonType("Cập nhật", ButtonBar.ButtonData.APPLY);
        ButtonType deleteButton = new ButtonType("Xóa", ButtonBar.ButtonData.NO);

        this.getButtonTypes().addAll(cancelButton, updateButton, deleteButton);

        Button btnSua = (Button) this.lookupButton(updateButton);
        Button btnHuy = (Button) this.lookupButton(cancelButton);
        Button btnXoa = (Button) this.lookupButton(deleteButton);

        txtMaNV.setEditable(false);
        luong.setEditable(true);
        loadData();

        // Cho phép spinner nhập trực tiếp
        luong.getEditor().textProperty().addListener((obs, oldVal, newVal) -> {
            try {
                double val = Double.parseDouble(newVal);
                luong.getValueFactory().setValue(val);
            } catch (NumberFormatException ignored) {}
        });


        // Cập nhật nhân viên
        btnSua.addEventFilter(javafx.event.ActionEvent.ACTION, e -> {
            e.consume(); // prevent auto-close; close manually on success
            if (txtHoTen.getText().isBlank() || txtSDT.getText().isBlank() || txtEmail.getText().isBlank()) {
                showMess("Vui lòng nhập đầy đủ thông tin!");
                return;
            }

            String hoTen = txtHoTen.getText();
            String soDienThoai = txtSDT.getText();
            String email = txtEmail.getText();
            String diaChi = txtDiaChi.getText();
            String taiKhoan = txtTaiKhoan.getText();
            boolean quanLy = "Nhân viên quản lí".equals(cbChucVu.getValue());
            double luongCoBan = luong.getValue();

            NhanVienDTO nvUpdate = new NhanVienDTO(
                    select.getMaNV(), hoTen, soDienThoai, email,
                    diaChi, luongCoBan, taiKhoan, select.getMatKhau(), quanLy
            );
            Task<Boolean> task = new Task<>() {
                @Override
                protected Boolean call() throws Exception {
                    return ClientManager.getInstance().updateNhanVien(nvUpdate);
                }
            };
            task.setOnSucceeded(evt -> {
                boolean result = task.getValue();
                if (result) {
                    showMess("Cập nhật nhân viên thành công!");
                    javafx.stage.Window window = btnSua.getScene() != null ? btnSua.getScene().getWindow() : null;
                    if (window != null) window.hide();
                } else {
                    showMess("Cập nhật nhân viên thất bại!");
                }
            });
            task.setOnFailed(evt -> {
                showMess("Lỗi: " + task.getException().getMessage());
            });
            new Thread(task).start();
        });

        // Xóa nhân viên
        btnXoa.setOnAction(e -> {
            try {
                Task<Boolean> task = new Task<>() {
                    @Override
                    protected Boolean call() throws Exception {
                        return ClientManager.getInstance().deleteNhanVien(select.getMaNV());
                    }
                };
                task.setOnSucceeded(evt -> {
                    boolean result = task.getValue();
                    showMess(result ? "Xóa nhân viên thành công!" : "Xóa nhân viên thất bại!");
                });
                task.setOnFailed(evt -> {
                    showMess("Lỗi: " + task.getException().getMessage());
                });
                new Thread(task).start();
            } catch (Exception ex) {
                showMess("Lỗi: " + ex.getMessage());
            }
        });

        // Validation email
        txtEmail.textProperty().addListener((obs, oldText, newText) -> {
            if (newText.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$") || newText.isBlank())
                txtEmail.setStyle("");
            else
                txtEmail.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
        });

        // Validation số điện thoại
        txtSDT.textProperty().addListener((obs, oldText, newText) -> {
            if (newText.matches("^0\\d{9}$") || newText.isBlank())
                txtSDT.setStyle("");
            else
                txtSDT.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
        });
    }


    private void loadData() {
        txtMaNV.setText(select.getMaNV());
        txtHoTen.setText(select.getHoTen());
        txtSDT.setText(select.getSoDienThoai());
        txtEmail.setText(select.getEmail());
        txtDiaChi.setText(select.getDiaChi());
        txtTaiKhoan.setText(select.getTaiKhoan());

        if (cbChucVu.getItems().isEmpty()) {
            cbChucVu.getItems().addAll("Nhân viên", "Nhân viên quản lí");
        }
        cbChucVu.getSelectionModel().select(select.isQuanLi() ? 1 : 0);

        SpinnerValueFactory<Double> valueFactory =
                new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 100_000_000.0, select.getLuongCoBan(), 500_000.0);
        luong.setValueFactory(valueFactory);
    }
    private void showMess(String mess) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Thông báo");
        alert.setHeaderText(null);
        alert.setContentText(mess);
        alert.showAndWait();
    }
}

package com.antam.app.controller.thuoc;

import com.antam.app.dto.*;
import com.antam.app.network.ClientManager;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CapNhatThuocFormController extends DialogPane{
    private ThuocDTO thuocDTO;
    private TextField txtDUMaThuoc, txtDUTenThuoc, txtDUHamLuong;
    private Text txtDUGiaByDV = new Text();
    private Text notification_DUThuoc;
    private Spinner<Double> spDUGiaGoc, spDUGiaBan, spDUThue;
    private ComboBox<DonViTinhDTO> cbDUDVCS;
    private ComboBox<DangDieuCheDTO> cbDUDangDieuChe;
    private ComboBox<KeDTO> cbDUKe;
    private final ClientManager clientManager = ClientManager.getInstance();

    public void setThuoc(ThuocDTO thuocDTO) {
        this.thuocDTO = thuocDTO;
    }

    public ThuocDTO getThuoc(){
        return thuocDTO;
    }
    // Hiển thị dữ liệu lên dialog
    public void showData(ThuocDTO t) {
        txtDUMaThuoc.setText(t.getMaThuoc());
        txtDUMaThuoc.setEditable(false);
        txtDUTenThuoc.setText(t.getTenThuoc());
        cbDUDangDieuChe.setValue(t.getDangDieuCheDTO());
        spDUGiaGoc.getValueFactory().setValue(t.getGiaGoc());
        spDUGiaBan.getValueFactory().setValue(t.getGiaBan());
        spDUThue.getValueFactory().setValue((double) t.getThue());
        txtDUHamLuong.setText(t.getHamLuong());
        cbDUKe.setValue(t.getMaKeDTO());
        cbDUDVCS.setValue(t.getMaDVTCoSo());
        txtDUGiaByDV.setText(t.getMaDVTCoSo() == null ? "" : t.getMaDVTCoSo().getTenDVT());
    }

    public CapNhatThuocFormController(){
        this.setPrefSize(800, 600);

        FlowPane header = new FlowPane();
        header.setAlignment(Pos.CENTER);
        header.setStyle("-fx-background-color: #1e3a8a;");
        Text title = new Text("Cập nhật thuốc");
        title.setFill(javafx.scene.paint.Color.WHITE);
        title.setFont(Font.font("System Bold", 15));
        FlowPane.setMargin(title, new Insets(10,0,10,0));
        header.getChildren().add(title);
        this.setHeader(header);

        // ===== Content =====
        AnchorPane root = new AnchorPane();
        VBox container = new VBox(10);
        container.setPadding(new Insets(10));
        AnchorPane.setLeftAnchor(container, 0.0);
        AnchorPane.setRightAnchor(container, 0.0);

        // ===== GridPane chính =====
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setHgrow(Priority.SOMETIMES);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setHgrow(Priority.SOMETIMES);
        grid.getColumnConstraints().addAll(col1, col2);

        // Labels và Controls
        Text labelMa = new Text("Mã thuốc:");
        txtDUMaThuoc = new TextField();
        txtDUMaThuoc.setPrefHeight(40);

        Text labelTen = new Text("Tên thuốc:");
        txtDUTenThuoc = new TextField();
        txtDUTenThuoc.setPrefHeight(40);

        Text labelGiaGoc = new Text("Giá gốc:");
        spDUGiaGoc = new Spinner<>();
        spDUGiaGoc.setMaxWidth(Double.MAX_VALUE);
        spDUGiaGoc.setEditable(true);
        spDUGiaGoc.setPrefHeight(40);

        Text labelGiaBan = new Text("Giá bán:");
        spDUGiaBan = new Spinner<>();
        spDUGiaBan.setMaxWidth(Double.MAX_VALUE);
        spDUGiaBan.setEditable(true);
        spDUGiaBan.setPrefHeight(40);

        Text labelThue = new Text("Thuế (%):");
        spDUThue = new Spinner<>();
        spDUThue.setMaxWidth(Double.MAX_VALUE);
        spDUThue.setEditable(true);
        spDUThue.setPrefHeight(40);

        Text labelDangDieuChe = new Text("Dạng điều chế:");
        cbDUDangDieuChe = new ComboBox<>();
        cbDUDangDieuChe.setMaxWidth(Double.MAX_VALUE);
        cbDUDangDieuChe.setPrefHeight(40);

        Text labelKe = new Text("Kệ thuốc:");
        cbDUKe = new ComboBox<>();
        cbDUKe.setMaxWidth(Double.MAX_VALUE);
        cbDUKe.setPrefHeight(40);

        Text labelHamLuong = new Text("Hàm lượng:");
        txtDUHamLuong = new TextField();
        txtDUHamLuong.setPrefHeight(40);

        Text labelDonViTinh = new Text("Đơn vị tính:");
        cbDUDVCS = new ComboBox<>();
        cbDUDVCS.setMaxWidth(Double.MAX_VALUE);
        cbDUDVCS.setPrefHeight(40);

        // ===== Gán vào GridPane =====
        grid.add(labelMa, 0, 0);
        grid.add(txtDUMaThuoc, 0, 1);

        grid.add(labelTen, 1, 0);
        grid.add(txtDUTenThuoc, 1, 1);

        grid.add(labelGiaGoc, 0, 2);
        grid.add(spDUGiaGoc, 0, 3);

        grid.add(labelGiaBan, 1, 2);
        grid.add(spDUGiaBan, 1, 3);

        grid.add(labelThue, 0, 4);
        grid.add(spDUThue, 0, 5);

        grid.add(labelDangDieuChe, 1, 4);
        grid.add(cbDUDangDieuChe, 1, 5);

        grid.add(labelKe, 0, 6);
        grid.add(cbDUKe, 0, 7);

        grid.add(labelHamLuong, 1, 6);
        grid.add(txtDUHamLuong, 1, 7);

        grid.add(labelDonViTinh, 0, 8);
        grid.add(cbDUDVCS, 0, 9);

        // ===== Notification =====
        notification_DUThuoc = new Text();
        notification_DUThuoc.setFill(javafx.scene.paint.Color.RED);

        // ===== Add tất cả vào container =====
        container.getChildren().addAll(grid, notification_DUThuoc);
        root.getChildren().add(container);
        this.setContent(root);

        // ===== Stylesheet =====
        URL stylesheet = getClass().getResource("/com/antam/app/styles/dashboard_style.css");
        if (stylesheet != null) {
            this.getStylesheets().add(stylesheet.toExternalForm());
        }
        // Sự kiện
        // them button vao dialog
        ButtonType closeButton = new ButtonType("Đóng", ButtonBar.ButtonData.CANCEL_CLOSE);
        ButtonType applyButtonUpdate = new ButtonType("Sửa", ButtonBar.ButtonData.APPLY);
        ButtonType applyButtonDelete = new ButtonType("Xóa", ButtonBar.ButtonData.APPLY);
        // them button vao dialog
        this.getButtonTypes().addAll(applyButtonUpdate, applyButtonDelete, closeButton);
        // su kien button sua
        Button applyBtnUpdate = (Button) this.lookupButton(applyButtonUpdate);
        Button applyBtnDelete = (Button) this.lookupButton(applyButtonDelete);

        applyBtnUpdate.addEventFilter(ActionEvent.ACTION, event -> {
            event.consume();
            if (!validate()) {
                return;
            }
            ThuocDTO updatedThuoc = buildThuocDTO();
            handleUpdateThuocAsync(updatedThuoc, applyBtnUpdate, applyBtnDelete);
        });

        // su kien button xoa
        applyBtnDelete.addEventFilter(ActionEvent.ACTION, event -> {
            event.consume();
            ThuocDTO currentThuoc = getThuoc();
            if (currentThuoc == null) {
                notification_DUThuoc.setText("Không tìm thấy thông tin thuốc để xóa!");
                return;
            }
            if (!showConfirmDeleteDialog(currentThuoc.getTenThuoc())) {
                return;
            }
            handleDeleteThuocAsync(currentThuoc.getMaThuoc(), applyBtnUpdate, applyBtnDelete);
        });


        // su kien up down spinner gia goc, gia ban, thue
        SpinnerValueFactory.DoubleSpinnerValueFactory valueFactoryGiaGoc =
                new SpinnerValueFactory.DoubleSpinnerValueFactory(
                        0,
                        1_000_000,
                        0,
                        1000
                );

        spDUGiaGoc.setValueFactory(valueFactoryGiaGoc);
        spDUGiaGoc.setEditable(true);

        SpinnerValueFactory.DoubleSpinnerValueFactory valueFactoryGiaBan =
                new SpinnerValueFactory.DoubleSpinnerValueFactory(
                        0,
                        1_000_000,
                        0,
                        1000
                );
        spDUGiaBan.setValueFactory(valueFactoryGiaBan);
        spDUGiaBan.setEditable(true);

        SpinnerValueFactory.DoubleSpinnerValueFactory valueFactoryThue =
                new SpinnerValueFactory.DoubleSpinnerValueFactory(
                        0,
                        1,
                        0,
                        0.01
                );
        spDUThue.setValueFactory(valueFactoryThue);
        spDUThue.setEditable(true);
        //them combo box ke
        configureComboBoxDisplay();
        addComBoBoxKe();
        // them combo box don vi co so
        addComBoBoxDVCS();
        //them combo box dang dieu che
        addComBoBoxDDC();
        cbDUDVCS.setOnAction(e -> {
            txtDUGiaByDV.setText(cbDUDVCS.getValue() == null ? "" : cbDUDVCS.getValue().getTenDVT());
        });

    }

    private void configureComboBoxDisplay() {
        cbDUKe.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(KeDTO item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : formatKe(item));
            }
        });
        cbDUKe.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(KeDTO item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : formatKe(item));
            }
        });

        cbDUDangDieuChe.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(DangDieuCheDTO item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : formatDDC(item));
            }
        });
        cbDUDangDieuChe.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(DangDieuCheDTO item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : formatDDC(item));
            }
        });

        cbDUDVCS.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(DonViTinhDTO item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : formatDVT(item));
            }
        });
        cbDUDVCS.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(DonViTinhDTO item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : formatDVT(item));
            }
        });
    }

    private ThuocDTO buildThuocDTO() {
        return new ThuocDTO(
                txtDUMaThuoc.getText().trim(),
                txtDUTenThuoc.getText().trim(),
                txtDUHamLuong.getText().trim(),
                spDUGiaBan.getValue(),
                spDUGiaGoc.getValue(),
                spDUThue.getValue().floatValue(),
                false,
                cbDUDangDieuChe.getValue(),
                cbDUDVCS.getValue(),
                cbDUKe.getValue()
        );
    }

    private void handleUpdateThuocAsync(ThuocDTO updatedThuoc, Button applyBtnUpdate, Button applyBtnDelete) {
        Task<Boolean> updateTask = new Task<>() {
            @Override
            protected Boolean call() {
                return clientManager.updateThuoc(updatedThuoc);
            }
        };

        updateTask.setOnRunning(e -> {
            setFormDisabled(true, applyBtnUpdate, applyBtnDelete);
            notification_DUThuoc.setText("Đang cập nhật dữ liệu...");
        });

        updateTask.setOnSucceeded(e -> {
            setFormDisabled(false, applyBtnUpdate, applyBtnDelete);
            if (Boolean.TRUE.equals(updateTask.getValue())) {
                notification_DUThuoc.setText("");
                if (getScene() != null && getScene().getWindow() != null) {
                    getScene().getWindow().hide();
                }
            } else {
                notification_DUThuoc.setText("Cập nhật thuốc thất bại!");
            }
        });

        updateTask.setOnFailed(e -> {
            setFormDisabled(false, applyBtnUpdate, applyBtnDelete);
            Throwable ex = updateTask.getException();
            notification_DUThuoc.setText(ex == null ? "Lỗi kết nối tới server!" : "Lỗi kết nối: " + ex.getMessage());
        });

        Thread updateThread = new Thread(updateTask, "cap-nhat-thuoc-task");
        updateThread.setDaemon(true);
        updateThread.start();
    }

    private void handleDeleteThuocAsync(String maThuoc, Button applyBtnUpdate, Button applyBtnDelete) {
        Task<Boolean> deleteTask = new Task<>() {
            @Override
            protected Boolean call() {
                return clientManager.deleteThuoc(maThuoc);
            }
        };

        deleteTask.setOnRunning(e -> {
            setFormDisabled(true, applyBtnUpdate, applyBtnDelete);
            notification_DUThuoc.setText("Đang xóa thuốc...");
        });

        deleteTask.setOnSucceeded(e -> {
            setFormDisabled(false, applyBtnUpdate, applyBtnDelete);
            if (Boolean.TRUE.equals(deleteTask.getValue())) {
                notification_DUThuoc.setText("");
                if (getScene() != null && getScene().getWindow() != null) {
                    getScene().getWindow().hide();
                }
            } else {
                notification_DUThuoc.setText("Xóa thuốc thất bại!");
            }
        });

        deleteTask.setOnFailed(e -> {
            setFormDisabled(false, applyBtnUpdate, applyBtnDelete);
            Throwable ex = deleteTask.getException();
            notification_DUThuoc.setText(ex == null ? "Lỗi kết nối tới server!" : "Lỗi kết nối: " + ex.getMessage());
        });

        Thread deleteThread = new Thread(deleteTask, "xoa-thuoc-task");
        deleteThread.setDaemon(true);
        deleteThread.start();
    }

    private void setFormDisabled(boolean disabled, Button applyBtnUpdate, Button applyBtnDelete) {
        txtDUMaThuoc.setDisable(disabled);
        txtDUTenThuoc.setDisable(disabled);
        txtDUHamLuong.setDisable(disabled);
        spDUGiaGoc.setDisable(disabled);
        spDUGiaBan.setDisable(disabled);
        spDUThue.setDisable(disabled);
        cbDUDVCS.setDisable(disabled);
        cbDUDangDieuChe.setDisable(disabled);
        cbDUKe.setDisable(disabled);
        applyBtnUpdate.setDisable(disabled);
        applyBtnDelete.setDisable(disabled);
    }

    private String formatKe(KeDTO keDTO) {
        return keDTO == null ? "" : keDTO.getMaKe() + " - " + keDTO.getTenKe();
    }

    private String formatDDC(DangDieuCheDTO ddc) {
        return ddc == null ? "" : ddc.getDisplayText();
    }

    private String formatDVT(DonViTinhDTO dvt) {
        return dvt == null ? "" : dvt.getMaDVT() + " - " + dvt.getTenDVT();
    }

    // hien thi hop thoai xac nhan xoa
    public boolean showConfirmDeleteDialog(String tenThuoc) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Xác nhận xóa");
        alert.setHeaderText("Bạn có chắc muốn xóa thuốc: " + tenThuoc + "?");
        alert.setContentText("Hành động này không thể hoàn tác.");

        Optional<ButtonType> result = alert.showAndWait();

        return result.isPresent() && result.get() == ButtonType.OK;
    }
    // ham kiem tra du lieu hop le
    public boolean validate(){
        String maThuoc = txtDUMaThuoc.getText() == null ? "" : txtDUMaThuoc.getText().trim();
        String tenThuoc = txtDUTenThuoc.getText() == null ? "" : txtDUTenThuoc.getText().trim();
        DonViTinhDTO donViCoSo = cbDUDVCS.getValue();
        DangDieuCheDTO dangDieuChe = cbDUDangDieuChe.getValue();
        String hamLuong = txtDUHamLuong.getText() == null ? "" : txtDUHamLuong.getText().trim();
        KeDTO ke = cbDUKe.getValue();

        if(maThuoc.isEmpty()){
            notification_DUThuoc.setText("Vui lòng điền mã thuốc!");
            return false;
        }
        if (!maThuoc.matches("^VN-\\d{5}-\\d{2}$")) {
            notification_DUThuoc.setText("Mã thuốc không hợp lệ! (VD: VN-12345-01)");
            return false;
        }
        if (tenThuoc.isEmpty()){
            notification_DUThuoc.setText("Vui lòng điền đầy đủ tên thuốc!");
            return false;
        }
        if (donViCoSo == null){
            notification_DUThuoc.setText("Vui lòng điền đầy đủ đơn vị cơ sở!");
            return false;
        }
        if (dangDieuChe == null){
            notification_DUThuoc.setText("Vui lòng điền dạng điều chế!");
            return false;
        }
        if (hamLuong.isEmpty()){
            notification_DUThuoc.setText("Vui lòng điền đầy đủ hàm lượng!");
            return false;
        }
        try {
            Double giaGoc = spDUGiaGoc.getValue();
            if (giaGoc == null || giaGoc <= 0) {
                notification_DUThuoc.setText("Giá gốc phải lớn hơn 0!");
                return false;
            }
        } catch (Exception e) {
            notification_DUThuoc.setText("Giá gốc không hợp lệ!");
            return false;
        }
        try {
            Double giaBan = spDUGiaBan.getValue();
            if (giaBan == null || giaBan <= 0) {
                notification_DUThuoc.setText("Giá bán phải lớn hơn 0!");
                return false;
            }
        } catch (Exception e) {
            notification_DUThuoc.setText("Giá bán không hợp lệ!");
            return false;
        }
        if (spDUGiaGoc.getValue() != null && spDUGiaBan.getValue() != null
                && spDUGiaGoc.getValue() > spDUGiaBan.getValue()) {
            notification_DUThuoc.setText("Giá bán phải lớn hơn hoặc bằng giá gốc!");
            return false;
        }
        try {
            Double thue = spDUThue.getValue();
            if (thue == null || thue < 0) {
                notification_DUThuoc.setText("Thuế không được âm!");
                return false;
            }
        } catch (Exception e) {
            notification_DUThuoc.setText("Thuế không hợp lệ!");
            return false;
        }
        if (ke == null){
            notification_DUThuoc.setText("Vui lòng điền đầy đủ thông tin!");
            return false;
        }

        notification_DUThuoc.setText("");
        return true;
    }

    // them value vao combobox ke
    public void addComBoBoxKe() {
        ArrayList<KeDTO> arrayKe = toKeList(clientManager.getActiveKeList());
        cbDUKe.getItems().clear();
        for (KeDTO keDTO : arrayKe) {
            cbDUKe.getItems().add(keDTO);
        }
        cbDUKe.getSelectionModel().selectFirst();
    }

    // Them value vao combobox dang dieu che
    public void addComBoBoxDDC() {
        ArrayList<DangDieuCheDTO> arrayDDC = toDangDieuCheList(clientManager.getActiveDangDieuCheList());
        cbDUDangDieuChe.getItems().clear();
        for (DangDieuCheDTO ddc : arrayDDC){
            cbDUDangDieuChe.getItems().add(ddc);
        }
        cbDUDangDieuChe.getSelectionModel().selectFirst();
    }

    // Them value vao combobox don vi tinh
    public void addComBoBoxDVCS() {
        ArrayList<DonViTinhDTO> arrayDVT = toDonViTinhList(clientManager.getDonViTinhList());
        cbDUDVCS.getItems().clear();
        arrayDVT.forEach(dvt -> cbDUDVCS.getItems().add(dvt));
        cbDUDVCS.getSelectionModel().selectFirst();
        txtDUGiaByDV.setText(cbDUDVCS.getValue() == null ? "" : cbDUDVCS.getValue().toString());
    }

    private ArrayList<KeDTO> toKeList(Collection<?> source) {
        ArrayList<KeDTO> result = new ArrayList<>();
        if (source == null) {
            return result;
        }
        for (Object item : source) {
            if (item instanceof KeDTO keDTO) {
                result.add(keDTO);
            }
        }
        return result;
    }

    private ArrayList<DangDieuCheDTO> toDangDieuCheList(Collection<?> source) {
        ArrayList<DangDieuCheDTO> result = new ArrayList<>();
        if (source == null) {
            return result;
        }
        for (Object item : source) {
            if (item instanceof DangDieuCheDTO dangDieuCheDTO) {
                result.add(dangDieuCheDTO);
            }
        }
        return result;
    }

    private ArrayList<DonViTinhDTO> toDonViTinhList(Collection<?> source) {
        ArrayList<DonViTinhDTO> result = new ArrayList<>();
        if (source == null) {
            return result;
        }
        for (Object item : source) {
            if (item instanceof DonViTinhDTO donViTinhDTO) {
                result.add(donViTinhDTO);
            }
        }
        return result;
    }

    // Cat chuoi
    public static String extractUnit(String input) {
        if (input == null) return null;

        String pattern = "\\d+\\s*(\\p{L}+).*";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(input);

        if (m.find()) {
            return m.group(1);
        } else {
            return null;
        }
    }

}

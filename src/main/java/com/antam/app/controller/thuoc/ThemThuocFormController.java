//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.antam.app.controller.thuoc;

import com.antam.app.dto.*;
import com.antam.app.network.ClientManager;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.text.Text;
import javafx.util.StringConverter;

import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

public class ThemThuocFormController extends DialogPane{
    private final ClientManager clientManager;

    private TextField txtAddMaThuoc, txtAddTenThuoc, txtAddHamLuong;

    private Text notification_addThuoc;

    private Spinner<Double> spAddGiaGoc, spAddGiaBan, spAddThue;

    private ComboBox<DonViTinhDTO> cbAddDVCS;

    private ComboBox<DangDieuCheDTO> cbAddDangDieuChe;

    private ComboBox<KeDTO> cbAddKe;


    public ThemThuocFormController() {
        this.clientManager = ClientManager.getInstance();

        this.setPrefSize(800, 600);

        FlowPane header = new FlowPane();
        header.setAlignment(Pos.CENTER);
        header.setStyle("-fx-background-color: #1e3a8a;");
        Text title = new Text("Thêm thuốc mới");
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
        txtAddMaThuoc = new TextField();
        txtAddMaThuoc.setPrefHeight(40);

        Text labelTen = new Text("Tên thuốc:");
        txtAddTenThuoc = new TextField();
        txtAddTenThuoc.setPrefHeight(40);

        Text labelGiaGoc = new Text("Giá gốc:");
        spAddGiaGoc = new Spinner<>();
        spAddGiaGoc.setMaxWidth(Double.MAX_VALUE);
        spAddGiaGoc.setEditable(true);
        spAddGiaGoc.setPrefHeight(40);

        Text labelGiaBan = new Text("Giá bán:");
        spAddGiaBan = new Spinner<>();
        spAddGiaBan.setMaxWidth(Double.MAX_VALUE);
        spAddGiaBan.setEditable(true);
        spAddGiaBan.setPrefHeight(40);

        Text labelThue = new Text("Thuế (%):");
        spAddThue = new Spinner<>();
        spAddThue.setMaxWidth(Double.MAX_VALUE);
        spAddThue.setEditable(true);
        spAddThue.setPrefHeight(40);

        Text labelDangDieuChe = new Text("Dạng điều chế:");
        cbAddDangDieuChe = new ComboBox<>();
        cbAddDangDieuChe.setMaxWidth(Double.MAX_VALUE);
        cbAddDangDieuChe.setPrefHeight(40);

        Text labelKe = new Text("Kệ thuốc:");
        cbAddKe = new ComboBox<>();
        cbAddKe.setMaxWidth(Double.MAX_VALUE);
        cbAddKe.setPrefHeight(40);

        Text labelHamLuong = new Text("Hàm lượng:");
        txtAddHamLuong = new TextField();
        txtAddHamLuong.setPrefHeight(40);

        Text labelDonViTinh = new Text("Đơn vị tính:");
        cbAddDVCS = new ComboBox<>();
        cbAddDVCS.setMaxWidth(Double.MAX_VALUE);
        cbAddDVCS.setPrefHeight(40);

        // ===== Gán vào GridPane =====
        grid.add(labelMa, 0, 0);
        grid.add(txtAddMaThuoc, 0, 1);

        grid.add(labelTen, 1, 0);
        grid.add(txtAddTenThuoc, 1, 1);

        grid.add(labelGiaGoc, 0, 2);
        grid.add(spAddGiaGoc, 0, 3);

        grid.add(labelGiaBan, 1, 2);
        grid.add(spAddGiaBan, 1, 3);

        grid.add(labelThue, 0, 4);
        grid.add(spAddThue, 0, 5);

        grid.add(labelDangDieuChe, 1, 4);
        grid.add(cbAddDangDieuChe, 1, 5);

        grid.add(labelKe, 0, 6);
        grid.add(cbAddKe, 0, 7);

        grid.add(labelHamLuong, 1, 6);
        grid.add(txtAddHamLuong, 1, 7);

        grid.add(labelDonViTinh, 0, 8);
        grid.add(cbAddDVCS, 0, 9);

        // ===== Notification =====
        notification_addThuoc = new Text();
        notification_addThuoc.setFill(javafx.scene.paint.Color.RED);

        // ===== Add tất cả vào container =====
        container.getChildren().addAll(grid, notification_addThuoc);
        root.getChildren().add(container);
        this.setContent(root);

        // ===== Stylesheet =====
        URL stylesheet = getClass().getResource("/com/antam/app/styles/dashboard_style.css");
        if (stylesheet != null) {
            this.getStylesheets().add(stylesheet.toExternalForm());
        }
        // Sự kiện
        // them button vao dialog
        ButtonType cancelButton = new ButtonType("Huỷ", ButtonData.CANCEL_CLOSE);
        ButtonType applyButton = new ButtonType("Lưu", ButtonData.APPLY);
        this.getButtonTypes().add(cancelButton);
        this.getButtonTypes().add(applyButton);

        // su kien button luu
        Button applyBtn = (Button) this.lookupButton(applyButton);

        applyBtn.addEventFilter(ActionEvent.ACTION, event -> {
            // Keep dialog open until async save finishes.
            event.consume();
            if (!validate()) {
                return;
            }

            ThuocDTO thuocDTO = buildThuocDTO();
            handleSaveThuocAsync(thuocDTO, applyBtn);
        });

        // su kien up down spinner gia goc
        SpinnerValueFactory.DoubleSpinnerValueFactory valueFactoryGiaGoc =
                new SpinnerValueFactory.DoubleSpinnerValueFactory(
                        0,
                        5000000,
                        0,
                        5000
                );
        NumberFormat currencyFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
        valueFactoryGiaGoc.setConverter(new StringConverter<Double>() {
            @Override
            public String toString(Double value) {
                if (value == null) return "";
                return currencyFormat.format(value);
            }

            @Override
            public Double fromString(String text) {
                try {
                    Number parsed = currencyFormat.parse(text);
                    return parsed.doubleValue();
                } catch (Exception e) {
                    return 0.0;
                }
            }
        });
        spAddGiaGoc.setValueFactory(valueFactoryGiaGoc);
        spAddGiaGoc.setEditable(true);
        // su kien up down spinner gia ban
        SpinnerValueFactory.DoubleSpinnerValueFactory valueFactoryGiaBan =
                new SpinnerValueFactory.DoubleSpinnerValueFactory(
                        0,
                        5000000,
                        0,
                        5000
                );
        valueFactoryGiaBan.setConverter(new StringConverter<Double>() {
            @Override
            public String toString(Double value) {
                if (value == null) return "";
                return currencyFormat.format(value);
            }

            @Override
            public Double fromString(String text) {
                try {
                    Number parsed = currencyFormat.parse(text);
                    return parsed.doubleValue();
                } catch (Exception e) {
                    return 0.0;
                }
            }
        });
        spAddGiaBan.setValueFactory(valueFactoryGiaBan);
        spAddGiaBan.setEditable(true);
        // su kien up down spinner thue
        SpinnerValueFactory.DoubleSpinnerValueFactory valueFactoryThue =
                new SpinnerValueFactory.DoubleSpinnerValueFactory(
                        0,
                        1,
                        0.05,
                        0.01
                );
        spAddThue.setValueFactory(valueFactoryThue);
        spAddThue.setEditable(true);
        //them combo box ke
        configureComboBoxDisplay();
        addComBoBoxKe();
        // them combo box don vi co so
        addComBoBoxDVCS();
        //them combo box dang dieu che
        addComBoBoxDDC();
    }

    private void configureComboBoxDisplay() {
        cbAddKe.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(KeDTO item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : formatKe(item));
            }
        });
        cbAddKe.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(KeDTO item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : formatKe(item));
            }
        });
        cbAddKe.setConverter(new StringConverter<>() {
            @Override
            public String toString(KeDTO keDTO) {
                return formatKe(keDTO);
            }

            @Override
            public KeDTO fromString(String string) {
                return null;
            }
        });

        cbAddDangDieuChe.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(DangDieuCheDTO item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : formatDDC(item));
            }
        });
        cbAddDangDieuChe.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(DangDieuCheDTO item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : formatDDC(item));
            }
        });
        cbAddDangDieuChe.setConverter(new StringConverter<>() {
            @Override
            public String toString(DangDieuCheDTO ddc) {
                return formatDDC(ddc);
            }

            @Override
            public DangDieuCheDTO fromString(String string) {
                return null;
            }
        });

        cbAddDVCS.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(DonViTinhDTO item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : formatDVT(item));
            }
        });
        cbAddDVCS.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(DonViTinhDTO item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : formatDVT(item));
            }
        });
        cbAddDVCS.setConverter(new StringConverter<>() {
            @Override
            public String toString(DonViTinhDTO dvt) {
                return formatDVT(dvt);
            }

            @Override
            public DonViTinhDTO fromString(String string) {
                return null;
            }
        });
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

    private ThuocDTO buildThuocDTO() {
        return new ThuocDTO(
                txtAddMaThuoc.getText().trim(),
                txtAddTenThuoc.getText().trim(),
                txtAddHamLuong.getText().trim(),
                spAddGiaBan.getValue(),
                spAddGiaGoc.getValue(),
                spAddThue.getValue().floatValue(),
                false,
                cbAddDangDieuChe.getValue(),
                cbAddDVCS.getValue(),
                cbAddKe.getValue()
        );
    }

    private void handleSaveThuocAsync(ThuocDTO thuocDTO, Button applyBtn) {
        Task<String> saveTask = new Task<>() {
            @Override
            protected String call() {
                if (clientManager.getThuocById(thuocDTO.getMaThuoc()) != null) {
                    return "Mã thuốc đã tồn tại!";
                }
                return clientManager.createThuoc(thuocDTO) ? null : "Thêm thuốc thất bại!";
            }
        };

        saveTask.setOnRunning(e -> {
            setFormDisabled(true, applyBtn);
            notification_addThuoc.setText("Đang lưu dữ liệu...");
        });

        saveTask.setOnSucceeded(e -> {
            setFormDisabled(false, applyBtn);
            String errorMessage = saveTask.getValue();
            if (errorMessage == null) {
                notification_addThuoc.setText("");
                if (getScene() != null && getScene().getWindow() != null) {
                    getScene().getWindow().hide();
                }
            } else {
                notification_addThuoc.setText(errorMessage);
            }
        });

        saveTask.setOnFailed(e -> {
            setFormDisabled(false, applyBtn);
            Throwable ex = saveTask.getException();
            notification_addThuoc.setText(ex == null ? "Lỗi kết nối tới server!" : "Lỗi kết nối: " + ex.getMessage());
        });

        Thread saveThread = new Thread(saveTask, "them-thuoc-save-task");
        saveThread.setDaemon(true);
        saveThread.start();
    }

    private void setFormDisabled(boolean disabled, Button applyBtn) {
        txtAddMaThuoc.setDisable(disabled);
        txtAddTenThuoc.setDisable(disabled);
        txtAddHamLuong.setDisable(disabled);
        spAddGiaGoc.setDisable(disabled);
        spAddGiaBan.setDisable(disabled);
        spAddThue.setDisable(disabled);
        cbAddDangDieuChe.setDisable(disabled);
        cbAddKe.setDisable(disabled);
        cbAddDVCS.setDisable(disabled);
        applyBtn.setDisable(disabled);
    }

    // ham validate kiem tra du lieu truyen vao dung hay ko
    public boolean validate(){
        String maThuoc = txtAddMaThuoc.getText() == null ? "" : txtAddMaThuoc.getText().trim();
        String tenThuoc = txtAddTenThuoc.getText() == null ? "" : txtAddTenThuoc.getText().trim();
        DonViTinhDTO donViCoSo = cbAddDVCS.getValue();
        DangDieuCheDTO dangDieuChe = cbAddDangDieuChe.getValue();
        String hamLuong = txtAddHamLuong.getText() == null ? "" : txtAddHamLuong.getText().trim();
        KeDTO ke = cbAddKe.getValue();

        if (maThuoc.isEmpty()) {
            notification_addThuoc.setText("Vui lòng điền mã thuốc!");
            return false;
        }
        if (!maThuoc.matches("^VN-\\d{5}-\\d{2}$")) {
            notification_addThuoc.setText("Mã thuốc không hợp lệ! (VD: VN-12345-01)");
            return false;
        }
        if (tenThuoc.isEmpty()){
            notification_addThuoc.setText("Vui lòng điền đầy đủ tên thuốc!");
            return false;
        }
        if (donViCoSo == null){
            notification_addThuoc.setText("Vui lòng điền đầy đủ đơn vị cơ sở!");
            return false;
        }
        if (dangDieuChe == null){
            notification_addThuoc.setText("Vui lòng điền dạng điều chế!");
            return false;
        }
        if (hamLuong.isEmpty()){
            notification_addThuoc.setText("Vui lòng điền đầy đủ hàm lượng!");
            return false;
        }

        try {
            Double giaGoc = spAddGiaGoc.getValue();
            if (giaGoc == null || giaGoc <= 0) {
                notification_addThuoc.setText("Giá gốc phải lớn hơn 0!");
                return false;
            }
        } catch (Exception e) {
            notification_addThuoc.setText("Giá gốc không hợp lệ!");
            return false;
        }
        try {
            Double giaBan = spAddGiaBan.getValue();
            if (giaBan == null || giaBan <= 0) {
                notification_addThuoc.setText("Giá bán phải lớn hơn 0!");
                return false;
            }
        } catch (Exception e) {
            notification_addThuoc.setText("Giá bán không hợp lệ!");
            return false;
        }
        if (spAddGiaGoc.getValue() != null && spAddGiaBan.getValue() != null
                && spAddGiaGoc.getValue() > spAddGiaBan.getValue()) {
            notification_addThuoc.setText("Giá bán phải lớn hơn hoặc bằng giá gốc!");
            return false;
        }
        try {
            Double thue = spAddThue.getValue();
            if (thue == null || thue < 0) {
                notification_addThuoc.setText("Thuế không được âm!");
                return false;
            }
        } catch (Exception e) {
            notification_addThuoc.setText("Thuế không hợp lệ!");
            return false;
        }
        if (ke == null){
            notification_addThuoc.setText("Vui lòng điền đầy đủ thông tin!");
            return false;
        }

        notification_addThuoc.setText("");
        return true;
    }

    // them value vao combobox ke
    public void addComBoBoxKe() {
        ArrayList<KeDTO> arrayKe = toKeList(clientManager.getActiveKeList());
        cbAddKe.getItems().clear();
        for (KeDTO keDTO : arrayKe) {
            cbAddKe.getItems().add(keDTO);
        }
        cbAddKe.getSelectionModel().selectFirst();
    }

    // Them value vao combobox dang dieu che
    public void addComBoBoxDDC() {
        ArrayList<DangDieuCheDTO> arrayDDC = toDangDieuCheList(clientManager.getActiveDangDieuCheList());
        cbAddDangDieuChe.getItems().clear();
        for (DangDieuCheDTO ddc : arrayDDC){
            cbAddDangDieuChe.getItems().add(ddc);
        }
        cbAddDangDieuChe.getSelectionModel().selectFirst();
    }

    // Them value vao combobox don vi tinh
    public void addComBoBoxDVCS() {
        ArrayList<DonViTinhDTO> arrayDVT = toDonViTinhList(clientManager.getDonViTinhList());
        cbAddDVCS.getItems().clear();
        arrayDVT.forEach(dvt -> cbAddDVCS.getItems().add(dvt));
        cbAddDVCS.getSelectionModel().selectFirst();
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

    // Cat chuoi lay don vi
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

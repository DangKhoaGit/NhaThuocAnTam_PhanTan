//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.antam.app.controller.khuyenmai;

import com.antam.app.network.ClientManager;
import com.antam.app.dto.KhuyenMaiDTO;
import com.antam.app.dto.LoaiKhuyenMaiDTO;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.text.Text;

import java.util.ArrayList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

public class ThemKhuyenMaiFormController extends DialogPane{

    private TextField txtMaKhuyenMai, txtTenKhuyenMai;
    private ComboBox<LoaiKhuyenMaiDTO> cbLoaiKhuyenMai;
    private Spinner<Integer> spSo, spSoLuongToiDa;
    private DatePicker dpNgayBacDau, dpNgayKetThuc;
    
    private Text txtThongBao;
    private final ClientManager clientManager;

    public ThemKhuyenMaiFormController() {
        this.clientManager = ClientManager.getInstance();
        this.setPrefSize(800, 600);
        FlowPane header = new FlowPane();
        header.setAlignment(Pos.CENTER);
        header.setStyle("-fx-background-color: #1e3a8a;");

        Text title = new Text("Thêm khuyến mãi mới");
        title.setFill(javafx.scene.paint.Color.WHITE);
        title.setFont(Font.font("System Bold", 15));
        FlowPane.setMargin(title, new Insets(10, 0, 10, 0));

        header.getChildren().add(title);
        this.setHeader(header);

        // ======================= CONTENT ===========================
        AnchorPane anchor = new AnchorPane();
        VBox main = new VBox(10);

        AnchorPane.setLeftAnchor(main, 0.0);
        AnchorPane.setRightAnchor(main, 0.0);

        // ======================= GRIDPANE ===========================
        GridPane grid = new GridPane();
        grid.setHgap(5);

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setHgrow(Priority.SOMETIMES);

        ColumnConstraints col2 = new ColumnConstraints();
        col2.setHgrow(Priority.SOMETIMES);

        grid.getColumnConstraints().addAll(col1, col2);

        for (int i = 0; i < 8; i++) {
            RowConstraints row = new RowConstraints(40);
            row.setVgrow(Priority.SOMETIMES);
            grid.getRowConstraints().add(row);
        }

        // ======================= COMPONENTS ===========================
        Text lblMa = new Text("Mã khuyến mãi:");
        lblMa.setFill(javafx.scene.paint.Color.web("#374151"));

        txtMaKhuyenMai = new TextField();
        txtMaKhuyenMai.getStyleClass().add("text-field");
        txtMaKhuyenMai.setPrefHeight(40);

        Text lblTen = new Text("Tên khuyến mãi:");
        lblTen.setFill(javafx.scene.paint.Color.web("#374151"));

        txtTenKhuyenMai = new TextField();
        txtTenKhuyenMai.setPrefHeight(40);
        txtTenKhuyenMai.getStyleClass().add("text-field");

        Text lblLoai = new Text("Loại khuyến mãi:");
        lblLoai.setFill(javafx.scene.paint.Color.web("#374151"));

        cbLoaiKhuyenMai = new ComboBox<>();
        cbLoaiKhuyenMai.setPrefHeight(40);

        Text lblSo = new Text("Số (Giá trị):");
        lblSo.setFill(javafx.scene.paint.Color.web("#374151"));

        spSo = new Spinner<>();
        spSo.setEditable(true);
        spSo.setPrefHeight(40);

        Text lblNgayBD = new Text("Ngày bắt đầu:");
        lblNgayBD.setFill(javafx.scene.paint.Color.web("#374151"));

        dpNgayBacDau = new DatePicker();
        dpNgayBacDau.setPrefHeight(40);

        Text lblNgayKT = new Text("Ngày kết thúc:");
        lblNgayKT.setFill(javafx.scene.paint.Color.web("#374151"));

        dpNgayKetThuc = new DatePicker();
        dpNgayKetThuc.setPrefHeight(40);

        Text lblSoLuong = new Text("Số lượng tối đa:");
        lblSoLuong.setFill(javafx.scene.paint.Color.web("#374151"));

        spSoLuongToiDa = new Spinner<>();
        spSoLuongToiDa.setEditable(true);
        spSoLuongToiDa.setPrefHeight(40);

        txtThongBao = new Text();
        txtThongBao.setFill(javafx.scene.paint.Color.RED);

        // ======================= ADD TO GRID ===========================
        grid.add(lblMa, 0, 0);
        grid.add(txtMaKhuyenMai, 0, 1);

        grid.add(lblTen, 1, 0);
        grid.add(txtTenKhuyenMai, 1, 1);

        grid.add(lblLoai, 0, 2);
        grid.add(cbLoaiKhuyenMai, 0, 3);

        grid.add(lblSo, 1, 2);
        grid.add(spSo, 1, 3);

        grid.add(lblNgayBD, 0, 4);
        grid.add(dpNgayBacDau, 0, 5);

        grid.add(lblNgayKT, 1, 4);
        grid.add(dpNgayKetThuc, 1, 5);

        grid.add(lblSoLuong, 0, 6);
        grid.add(spSoLuongToiDa, 0, 7);

        grid.add(txtThongBao, 1, 7);

        main.getChildren().add(grid);
        anchor.getChildren().add(main);

        this.getStylesheets().add(getClass().getResource("/com/antam/app/styles/dashboard_style.css").toExternalForm());
        this.setContent(anchor);
        // Sự kiện
        ButtonType cancelButton = new ButtonType("Huỷ", ButtonData.CANCEL_CLOSE);
        ButtonType applyButton = new ButtonType("Lưu", ButtonData.APPLY);
        this.getButtonTypes().add(cancelButton);
        this.getButtonTypes().add(applyButton);
        Button applyBtn = (Button) this.lookupButton(applyButton);

        applyBtn.addEventFilter(ActionEvent.ACTION, e -> {
            e.consume();
            if (!validate()) {
                return;
            }

            KhuyenMaiDTO khuyenMaiDTO = buildKhuyenMaiDTO();
            handleSaveKhuyenMaiAsync(khuyenMaiDTO, applyBtn);
        });
        // set ma khuyen mai
        txtMaKhuyenMai.setPromptText("Đang tạo mã...");
        txtMaKhuyenMai.setEditable(false);
        loadMaKhuyenMaiAsync();
        // load loai khuyen mai
        loadLoaiKhuyenMai();
        // set spinner
        SpinnerValueFactory<Integer> valueFactorySo = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 10, 1);
        spSo.setValueFactory(valueFactorySo);
        SpinnerValueFactory<Integer> valueFactorySoLuongToiDa = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 1000, 10, 1);
        spSoLuongToiDa.setValueFactory(valueFactorySoLuongToiDa);
        // set su kien loai khuyen mai
        cbLoaiKhuyenMai.setOnAction(e -> {
            LoaiKhuyenMaiDTO selectedLoai = cbLoaiKhuyenMai.getSelectionModel().getSelectedItem();
            applySpinnerRangeForLoai(selectedLoai);
        });
        applySpinnerRangeForLoai(cbLoaiKhuyenMai.getSelectionModel().getSelectedItem());
    }

    private KhuyenMaiDTO buildKhuyenMaiDTO() {
        return new KhuyenMaiDTO(
                txtMaKhuyenMai.getText().trim(),
                txtTenKhuyenMai.getText().trim(),
                dpNgayBacDau.getValue(),
                dpNgayKetThuc.getValue(),
                cbLoaiKhuyenMai.getValue(),
                spSo.getValue(),
                spSoLuongToiDa.getValue(),
                false
        );
    }

    private void handleSaveKhuyenMaiAsync(KhuyenMaiDTO khuyenMaiDTO, Button applyBtn) {
        Task<Boolean> saveTask = new Task<>() {
            @Override
            protected Boolean call() {
                return clientManager.createKhuyenMai(khuyenMaiDTO);
            }
        };

        saveTask.setOnRunning(e -> {
            setFormDisabled(true, applyBtn);
            txtThongBao.setFill(javafx.scene.paint.Color.web("#2563eb"));
            txtThongBao.setText("Đang lưu dữ liệu...");
        });

        saveTask.setOnSucceeded(e -> {
            setFormDisabled(false, applyBtn);
            if (Boolean.TRUE.equals(saveTask.getValue())) {
                txtThongBao.setFill(javafx.scene.paint.Color.GREEN);
                txtThongBao.setText("Thêm khuyến mãi thành công");
                if (getScene() != null && getScene().getWindow() != null) {
                    getScene().getWindow().hide();
                }
            } else {
                txtThongBao.setFill(javafx.scene.paint.Color.RED);
                txtThongBao.setText("Thêm khuyến mãi thất bại");
            }
        });

        saveTask.setOnFailed(e -> {
            setFormDisabled(false, applyBtn);
            Throwable ex = saveTask.getException();
            txtThongBao.setFill(javafx.scene.paint.Color.RED);
            txtThongBao.setText(ex == null ? "Lỗi kết nối tới server!" : "Lỗi kết nối: " + ex.getMessage());
        });

        Thread saveThread = new Thread(saveTask, "them-khuyenmai-save-task");
        saveThread.setDaemon(true);
        saveThread.start();
    }

    private void setFormDisabled(boolean disabled, Button applyBtn) {
        txtMaKhuyenMai.setDisable(disabled);
        txtTenKhuyenMai.setDisable(disabled);
        cbLoaiKhuyenMai.setDisable(disabled);
        spSo.setDisable(disabled);
        spSoLuongToiDa.setDisable(disabled);
        dpNgayBacDau.setDisable(disabled);
        dpNgayKetThuc.setDisable(disabled);
        applyBtn.setDisable(disabled);
    }

    public void loadLoaiKhuyenMai(){
        Task<ArrayList<LoaiKhuyenMaiDTO>> loadTask = new Task<>() {
            @Override
            protected ArrayList<LoaiKhuyenMaiDTO> call() {
                return new ArrayList<>(clientManager.getLoaiKhuyenMaiList());
            }
        };

        loadTask.setOnRunning(e -> txtThongBao.setText("Đang tải loại khuyến mãi..."));
        loadTask.setOnSucceeded(e -> {
            cbLoaiKhuyenMai.getItems().setAll(loadTask.getValue());
            cbLoaiKhuyenMai.getSelectionModel().selectFirst();
            applySpinnerRangeForLoai(cbLoaiKhuyenMai.getSelectionModel().getSelectedItem());
            txtThongBao.setText("");
        });
        loadTask.setOnFailed(e -> {
            Throwable ex = loadTask.getException();
            txtThongBao.setText(ex == null ? "Không tải được loại khuyến mãi" : "Không tải được loại khuyến mãi: " + ex.getMessage());
        });

        Thread loadThread = new Thread(loadTask, "load-loai-khuyenmai-task");
        loadThread.setDaemon(true);
        loadThread.start();
    }

    private void loadMaKhuyenMaiAsync() {
        Task<String> loadTask = new Task<>() {
            @Override
            protected String call() {
                return taoMaKhuyenMai();
            }
        };

        loadTask.setOnRunning(e -> txtThongBao.setText("Đang tạo mã khuyến mãi..."));
        loadTask.setOnSucceeded(e -> {
            txtMaKhuyenMai.setText(loadTask.getValue());
            txtThongBao.setText("");
        });
        loadTask.setOnFailed(e -> {
            Throwable ex = loadTask.getException();
            txtThongBao.setText(ex == null ? "Không tạo được mã khuyến mãi" : "Không tạo được mã khuyến mãi: " + ex.getMessage());
        });

        Thread loadThread = new Thread(loadTask, "load-ma-khuyenmai-task");
        loadThread.setDaemon(true);
        loadThread.start();
    }

    private void applySpinnerRangeForLoai(LoaiKhuyenMaiDTO selectedLoai) {
        if (selectedLoai == null) {
            spSo.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, Integer.MAX_VALUE, 10, 1));
            return;
        }

        String tenLoai = selectedLoai.getTenLKM() == null ? "" : selectedLoai.getTenLKM().toLowerCase();
        if (tenLoai.contains("phần trăm") || tenLoai.contains("%")) {
            spSo.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 10, 1));
        } else if (tenLoai.contains("tiền")) {
            spSo.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1000, 1000000, 10000, 1000));
        } else {
            spSo.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, Integer.MAX_VALUE, 10, 1));
        }
    }

    public boolean validate(){
        String tenKM = txtTenKhuyenMai.getText().trim();
        if (tenKM.isEmpty()) {
            txtThongBao.setText("Tên khuyến mãi không được để trống");
            return false;
        }
        if (cbLoaiKhuyenMai.getValue() == null) {
            txtThongBao.setText("Vui lòng chọn loại khuyến mãi");
            return false;
        }
        if (dpNgayBacDau.getValue() == null) {
            txtThongBao.setText("Vui lòng chọn ngày bắt đầu");
            return false;
        }
        if (dpNgayKetThuc.getValue() == null) {
            txtThongBao.setText("Vui lòng chọn ngày kết thúc");
            return false;
        }
        if (dpNgayBacDau.getValue().isAfter(dpNgayKetThuc.getValue())) {
            txtThongBao.setText("Ngày bắt đầu phải trước ngày kết thúc");
            return false;
        }
        if (spSo.getValue() == null) {
            txtThongBao.setText("Vui lòng nhập số khuyến mãi");
            return false;
        }else{
            if (cbLoaiKhuyenMai.getValue().getMaLKM() == 1) { // Phần trăm
                if (spSo.getValue() <= 0 || spSo.getValue() > 100) {
                    txtThongBao.setText("Số phần trăm khuyến mãi phải từ 1 đến 100");
                    return false;
                }
            } else if (cbLoaiKhuyenMai.getValue().getMaLKM() == 2) { // Tiền mặt
                if (spSo.getValue() <= 0) {
                    txtThongBao.setText("Số tiền khuyến mãi phải là số và lớn hơn 0");
                    return false;
                }
            }
        }
        if (spSoLuongToiDa.getValue() == null) {
            txtThongBao.setText("Vui lòng nhập số lượng tối đa");
            return false;
        }else{
            if (spSoLuongToiDa.getValue() <= 0) {
                txtThongBao.setText("Số lượng tối đa phải là số và lớn hơn 0");
                return false;
            }
        }
        return true;
    }

    public String taoMaKhuyenMai(){
        ArrayList<String> listMaKM = new ArrayList<>();
        clientManager.getKhuyenMaiList().forEach(khuyenMai -> listMaKM.add(khuyenMai.getMaKM()));
        clientManager.getKhuyenMaiDeletedList().forEach(khuyenMai -> listMaKM.add(khuyenMai.getMaKM()));
        int max = 0;
        for (String ma : listMaKM) {
            if (ma != null && ma.matches("KM\\d+")) {
                int num = Integer.parseInt(ma.substring(2));
                if (num > max) max = num;
            }
        }
        return String.format("KM%03d", max + 1);
    }
}

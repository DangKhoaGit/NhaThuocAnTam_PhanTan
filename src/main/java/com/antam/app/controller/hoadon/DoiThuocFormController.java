//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.antam.app.controller.hoadon;

import com.antam.app.connect.ConnectDB;
import com.antam.app.network.ClientManager;
import com.antam.app.dto.*;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.util.StringConverter;

import static javafx.collections.FXCollections.observableArrayList;

public class DoiThuocFormController extends DialogPane{

    private VBox vhDSCTHD, vhDSCTHDM;
    private TextField txtMaHoaDonDoi, txtKhachHangDoi;
    private Text txtTongTienDoi, txtTongTienTra, txtTongTienMua, txtThongBaoDoi;
    private Button btnThemMoiThuoc;
    private ComboBox<String> cbLyDoDoi;

    private final ClientManager clientManager;
    private HoaDonDTO hoaDonDTO;
    private ArrayList<ChiTietHoaDonDTO> selectedItems = new ArrayList<>();
    private ArrayList<ChiTietHoaDonDTO> chiTietHoaDons;
    private int soLuongThuoc = 0;
    private int soLuongThuocDoi = 0;

    private static final class ThuocMoiSelection {
        private final ThuocDTO thuoc;
        private final DonViTinhDTO donViTinh;
        private final int soLuong;

        private ThuocMoiSelection(ThuocDTO thuoc, DonViTinhDTO donViTinh, int soLuong) {
            this.thuoc = thuoc;
            this.donViTinh = donViTinh;
            this.soLuong = soLuong;
        }
    }

    public void setHoaDon(HoaDonDTO hoaDonDTO) {
        this.hoaDonDTO = hoaDonDTO;
    }

    public HoaDonDTO getHoaDon() {
        return hoaDonDTO;
    }

    public void showData(HoaDonDTO hoaDonDTO) {
        HoaDonDTO hd = (HoaDonDTO) clientManager.getHoaDonById(hoaDonDTO.getMaHD());
        chiTietHoaDons = new ArrayList<ChiTietHoaDonDTO>((Collection<? extends ChiTietHoaDonDTO>) clientManager.getChiTietHoaDon(hoaDonDTO.getMaHD()));
        txtMaHoaDonDoi.setText(hd == null ? hoaDonDTO.getMaHD() : hd.getMaHD());
        vhDSCTHD.getChildren().clear();
        selectedItems.clear();
        soLuongThuoc = 0;
        soLuongThuocDoi = 0;
        if (hd != null && hd.getMaKH() != null) {
            KhachHangDTO khachHangDTO = clientManager.getKhachHangById(hd.getMaKH().getMaKH());
            txtKhachHangDoi.setText(khachHangDTO == null ? "" : khachHangDTO.getTenKH());
        } else {
            txtKhachHangDoi.setText("");
        }

        for (ChiTietHoaDonDTO ct : chiTietHoaDons) {
            HBox hBox = renderChiTietHoaDon(ct);
            vhDSCTHD.getChildren().add(hBox);
            soLuongThuoc += 1;
        }

        txtTongTienTra.setText("0 đ");
        txtTongTienMua.setText("0 đ");
        txtTongTienDoi.setText("0 đ");
    }

    public DoiThuocFormController(){
        this.clientManager = ClientManager.getInstance();
        FlowPane header = new FlowPane();
        header.setAlignment(Pos.CENTER);
        header.setStyle("-fx-background-color: #1e3a8a;");

        Text title = new Text("Đổi thuốc");
        title.setFill(javafx.scene.paint.Color.WHITE);
        title.setFont(Font.font("System Bold", 15));
        FlowPane.setMargin(title, new Insets(10, 0, 10, 0));

        header.getChildren().add(title);
        this.setHeader(header);

        // ============================
        // CONTENT ROOT
        // ============================
        AnchorPane root = new AnchorPane();
        root.setPrefSize(800, 600);

        VBox mainVBox = new VBox(10);
        AnchorPane.setLeftAnchor(mainVBox, 0.0);
        AnchorPane.setRightAnchor(mainVBox, 0.0);

        // ============================
        // SCROLL CONTENT
        // ============================
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(480);

        VBox contentVBox = new VBox(10);

        // ============================
        // GRID 1
        // ============================
        GridPane grid1 = new GridPane();
        grid1.setHgap(5);

        ColumnConstraints c1 = new ColumnConstraints();
        c1.setHgrow(Priority.SOMETIMES);
        ColumnConstraints c2 = new ColumnConstraints();
        c2.setHgrow(Priority.SOMETIMES);
        grid1.getColumnConstraints().addAll(c1, c2);

        RowConstraints r1 = new RowConstraints();
        r1.setPrefHeight(30);
        RowConstraints r2 = new RowConstraints();
        r2.setPrefHeight(40);
        RowConstraints r3 = new RowConstraints();
        r3.setPrefHeight(30);
        RowConstraints r4 = new RowConstraints();
        r4.setPrefHeight(30);
        grid1.getRowConstraints().addAll(r1, r2, r3, r4);

        Text lblMaHD = new Text("Mã hóa đơn gốc:");
        lblMaHD.setFill(javafx.scene.paint.Color.web("#374151"));

        txtMaHoaDonDoi = new TextField();
        txtMaHoaDonDoi.setPrefHeight(40);
        txtMaHoaDonDoi.getStyleClass().add("text-field");
        txtMaHoaDonDoi.getStylesheets().add(getClass().getResource("/com/antam/app/styles/dashboard_style.css").toExternalForm());
        GridPane.setRowIndex(txtMaHoaDonDoi, 1);

        txtKhachHangDoi = new TextField();
        txtKhachHangDoi.setPrefHeight(40);
        txtKhachHangDoi.getStyleClass().add("text-field");
        txtKhachHangDoi.getStylesheets().add(getClass().getResource("/com/antam/app/styles/dashboard_style.css").toExternalForm());
        GridPane.setRowIndex(txtKhachHangDoi, 1);
        GridPane.setColumnIndex(txtKhachHangDoi, 1);

        Text lblKH = new Text("Khách hàng:");
        lblKH.setFill(javafx.scene.paint.Color.web("#374151"));
        GridPane.setColumnIndex(lblKH, 1);

        Text lblLyDo = new Text("Lý do đổi:");
        lblLyDo.setFill(javafx.scene.paint.Color.web("#374151"));
        GridPane.setRowIndex(lblLyDo, 2);

        cbLyDoDoi = new ComboBox<>();
        cbLyDoDoi.setPrefWidth(150);
        cbLyDoDoi.getStylesheets().add(getClass().getResource("/com/antam/app/styles/dashboard_style.css").toExternalForm());
        GridPane.setRowIndex(cbLyDoDoi, 3);

        grid1.getChildren().addAll(lblMaHD, txtMaHoaDonDoi, txtKhachHangDoi, lblKH, lblLyDo, cbLyDoDoi);

        // ============================
        // LIST CTHD GỐC
        // ============================
        vhDSCTHD = new VBox(5);
        vhDSCTHD.setStyle("-fx-border-color: #e5e7eb; -fx-border-radius: 6px; -fx-border-width: 2px;");
        vhDSCTHD.setPadding(new Insets(10));

        // ============================
        // GRID TỔNG TRẢ
        // ============================
        GridPane gridTongTra = new GridPane();
        gridTongTra.setHgap(5);
        gridTongTra.setStyle("-fx-background-color: #f8fafc; -fx-background-radius: 8px;");
        gridTongTra.setPadding(new Insets(10));

        ColumnConstraints t1 = new ColumnConstraints();
        t1.setHgrow(Priority.SOMETIMES);
        ColumnConstraints t2 = new ColumnConstraints();
        t2.setHgrow(Priority.SOMETIMES);
        gridTongTra.getColumnConstraints().addAll(t1, t2);

        txtTongTienTra = new Text("500.000đ");
        txtTongTienTra.setFill(javafx.scene.paint.Color.web("#374151"));
        txtTongTienTra.setFont(Font.font("System Bold", 13));
        GridPane.setColumnIndex(txtTongTienTra, 1);

        Text lblTongTra = new Text("Tổng tiền trả:");
        lblTongTra.setFill(javafx.scene.paint.Color.web("#374151"));
        lblTongTra.setFont(Font.font("System Bold", 13));

        gridTongTra.getChildren().addAll(lblTongTra, txtTongTienTra);

        // ============================
        // LABEL “Thuốc mới”
        // ============================
        Text lblThuocMoi = new Text("Thuốc mới:");

        // ============================
        // LIST CTHDM MỚI
        // ============================
        vhDSCTHDM = new VBox(5);
        vhDSCTHDM.setStyle("-fx-border-color: #e5e7eb; -fx-border-radius: 6px; -fx-border-width: 2px;");
        vhDSCTHDM.setPadding(new Insets(10));

        // ============================
        // BUTTON THÊM THUỐC MỚI
        // ============================
        btnThemMoiThuoc = new Button("Thêm thuốc mới");
        btnThemMoiThuoc.setStyle("-fx-background-color: #6b7280; -fx-font-size: 14px; -fx-font-weight: BOLD;");
        btnThemMoiThuoc.setTextFill(javafx.scene.paint.Color.WHITE);
        btnThemMoiThuoc.setPadding(new Insets(5, 10, 5, 10));

        // ============================
        // ADD to contentVBox
        // ============================
        contentVBox.getChildren().addAll(
                grid1,
                vhDSCTHD,
                gridTongTra,
                lblThuocMoi,
                vhDSCTHDM,
                btnThemMoiThuoc
        );

        scrollPane.setContent(contentVBox);

        // ============================
        // GRID TỔNG MUA (BOTTOM)
        // ============================
        GridPane gridTongMua = new GridPane();
        gridTongMua.setHgap(5);
        gridTongMua.setStyle("-fx-background-color: #f8fafc; -fx-background-radius: 8px;");
        gridTongMua.setPadding(new Insets(10));

        ColumnConstraints m1 = new ColumnConstraints();
        m1.setHgrow(Priority.SOMETIMES);
        ColumnConstraints m2 = new ColumnConstraints();
        m2.setHgrow(Priority.SOMETIMES);
        gridTongMua.getColumnConstraints().addAll(m1, m2);

        Text lblTongMua = new Text("Tổng tiền mua:");
        lblTongMua.setFill(javafx.scene.paint.Color.web("#374151"));
        lblTongMua.setFont(Font.font("System Bold", 13));

        txtTongTienMua = new Text("1.000.000đ");
        txtTongTienMua.setFill(javafx.scene.paint.Color.web("#374151"));
        txtTongTienMua.setFont(Font.font("System Bold", 13));
        GridPane.setColumnIndex(txtTongTienMua, 1);

        gridTongMua.getChildren().addAll(lblTongMua, txtTongTienMua);

        // ============================
        // KẾT QUẢ CUỐI
        // ============================
        VBox summaryBox = new VBox();
        summaryBox.setAlignment(Pos.CENTER);
        summaryBox.setStyle("-fx-background-color: #f8fafc; -fx-border-color: #2563eb; -fx-border-radius: 6px; -fx-border-width: 2px;");
        summaryBox.setPadding(new Insets(10));

        txtTongTienDoi = new Text("Tổng kết: 0 ₫");
        txtTongTienDoi.setFill(javafx.scene.paint.Color.web("#1e3a8a"));
        txtTongTienDoi.setFont(Font.font("System Bold", 22));

        txtThongBaoDoi = new Text("Không phát sinh thêm tiền");
        txtThongBaoDoi.setFill(javafx.scene.paint.Color.web("#6b6b6b"));
        txtThongBaoDoi.setFont(Font.font("System Italic", 13));

        summaryBox.getChildren().addAll(txtTongTienDoi, txtThongBaoDoi);

        // ============================
        // ADD ALL INTO mainVBox
        // ============================
        mainVBox.getChildren().addAll(
                scrollPane,
                gridTongMua,
                summaryBox
        );

        root.getChildren().add(mainVBox);
        this.setContent(root);
        /** Sự kiện **/
        ButtonType cancelButton = new ButtonType("Huỷ", ButtonData.CANCEL_CLOSE);
        ButtonType applyButton = new ButtonType("Xác nhận đổi thuốc", ButtonData.APPLY);
        this.getButtonTypes().add(cancelButton);
        this.getButtonTypes().add(applyButton);

        Button btnApply = (Button) this.lookupButton(applyButton);
        btnApply.addEventFilter(ActionEvent.ACTION, event -> {
            String lyDo = cbLyDoDoi.getValue();
            if (lyDo == null || lyDo.trim().isEmpty()) {
                showWarning("Cảnh báo", "Chưa chọn lý do trả thuốc", "Vui lòng chọn lý do trả trước khi xác nhận.");
                event.consume();
                return;
            }
            if (selectedItems.isEmpty()) {
                showWarning("Cảnh báo", "Chưa chọn thuốc để đổi", "Vui lòng chọn ít nhất một thuốc để đổi.");
                event.consume();
                return;
            }
            if (vhDSCTHDM.getChildren().isEmpty()) {
                showWarning("Cảnh báo", "Chưa thêm thuốc mới để đổi", "Vui lòng thêm ít nhất một thuốc mới để đổi.");
                event.consume();
                return;
            }
            if (!checkThongTinThuoc()) {
                showWarning("Cảnh báo", "Thông tin thuốc mới không hợp lệ", "Vui lòng kiểm tra lại thông tin thuốc mới.");
                event.consume();
                return;
            }

            ArrayList<ChiTietHoaDonDTO> selectedSnapshot = new ArrayList<>(selectedItems);
            ArrayList<ThuocMoiSelection> thuocMoiSelections = collectThuocMoiSelections();

            Task<Boolean> applyTask = new Task<>() {
                @Override
                protected Boolean call() {
                    return processDoiThuoc(lyDo, selectedSnapshot, thuocMoiSelections);
                }
            };

            applyTask.setOnRunning(e -> setFormDisabled(true, btnApply));
            applyTask.setOnSucceeded(e -> {
                setFormDisabled(false, btnApply);
                if (Boolean.TRUE.equals(applyTask.getValue())) {
                    if (getScene() != null && getScene().getWindow() != null) {
                        getScene().getWindow().hide();
                    }
                } else {
                    showWarning("Cảnh báo", "Không thể thực hiện giao dịch", "Server không xử lý được yêu cầu đổi thuốc.");
                }
            });
            applyTask.setOnFailed(e -> {
                setFormDisabled(false, btnApply);
                Throwable ex = applyTask.getException();
                showWarning("Lỗi", "Đổi thuốc thất bại", ex == null ? "Lỗi kết nối tới server!" : ex.getMessage());
            });

            Thread thread = new Thread(applyTask, "doi-thuoc-apply-task");
            thread.setDaemon(true);
            thread.start();
            event.consume();
        });
        // Them gia tri cho combobox ly do
        addValueCombobox();
        // Nút thêm mới thuốc
        btnThemMoiThuoc.setOnAction(e -> {
            renderChiTietHoaDonDoi(vhDSCTHDM);
        });
    }

    public void addValueCombobox(){
        ObservableList<String> lyDoList = observableArrayList(
                "Hết hạn sử dụng",
                "Bao bì bị hư hỏng",
                "Khách hàng đổi ý",
                "Thuốc lỗi / hư hỏng",
                "Nhập nhầm lô / dư",
                "Thuốc bị thu hồi",
                "Sai thông tin đơn / bảo hiểm"
        );
        cbLyDoDoi.setItems(lyDoList);
    }

    public HBox renderChiTietHoaDon(ChiTietHoaDonDTO chiTietHoaDonDTO) {
        if (chiTietHoaDonDTO == null) {
            HBox h = new HBox();
            h.setVisible(false);
            h.setManaged(false);
            return h;
        }
        if (isDaTra(chiTietHoaDonDTO)) {
            HBox h = new HBox();
            h.setVisible(false);
            h.setManaged(false);
            return h;
        }
        HBox hBox = new HBox();
        hBox.setSpacing(20);
        hBox.setStyle(
                "-fx-background-color: #f8fafc;" +
                "-fx-padding: 10;"
        );

        CheckBox checkBox = new CheckBox();
        checkBox.setOnAction(event -> {
            if (isBan(chiTietHoaDonDTO)) {
                soLuongThuocDoi += checkBox.isSelected() ? 1 : -1;
            }
            if (checkBox.isSelected()) {
                if (!selectedItems.contains(chiTietHoaDonDTO)) {
                    selectedItems.add(chiTietHoaDonDTO);
                }
            } else {
                selectedItems.remove(chiTietHoaDonDTO);
            }
            tinhTongTien();
        });

        LoThuocDTO ctt = clientManager.getLoThuocByLoThuocId(chiTietHoaDonDTO.getMaLoThuocDTO().getMaLoThuoc());
        ThuocDTO t = ctt == null || ctt.getMaThuocDTO() == null ? null : clientManager.getThuocById(ctt.getMaThuocDTO().getMaThuoc());
        Text txtMaThuoc = new Text(t == null ? "" : t.getTenThuoc());
        txtMaThuoc.setStyle("-fx-font-size: 15px;");
        Text txtSoLuong = new Text("SL " + chiTietHoaDonDTO.getSoLuong());
        txtSoLuong.setStyle("-fx-font-size: 15px;");
        DecimalFormat df = new DecimalFormat("#,### đ");
        Text txtDonGia = new Text(df.format(chiTietHoaDonDTO.getThanhTien()));
        txtDonGia.setStyle("-fx-font-size: 15px;");
        String valueBtn = "Bình thường";
        if (isThuocMoiKhiDoi(chiTietHoaDonDTO)) {
            valueBtn = "Thuốc đổi";
        }
        Button btn = new Button(valueBtn);
        btn.setStyle(
                "-fx-background-color: #e0f2fe;" +
                "-fx-text-fill: #0369a1;" +
                "-fx-background-radius: 10;" +
                "-fx-font-size: 12px;"
        );

        hBox.getChildren().addAll(checkBox, txtMaThuoc, txtSoLuong, txtDonGia, btn);
        return hBox;
    }

    public void renderChiTietHoaDonDoi(VBox vbox) {
        HBox hBox = new HBox(20);
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.setStyle("-fx-background-color: #f8fafc; -fx-padding: 10;");

        VBox vbThuoc = new VBox(4);
        VBox vbDVT = new VBox(4);
        VBox vbSoLuong = new VBox(4);

        ComboBox<ThuocDTO> comboBoxThuoc = new ComboBox<>();
        configureThuocComboBox(comboBoxThuoc);
        comboBoxThuoc.getStylesheets().add(getClass().getResource("/com/antam/app/styles/dashboard_style.css").toExternalForm());
        comboBoxThuoc.setPromptText("Chọn thuốc");
        for (Object obj : clientManager.getThuocList()) {
            if (obj instanceof ThuocDTO thuocDTO) {
                comboBoxThuoc.getItems().add(thuocDTO);
            }
        }

        ComboBox<DonViTinhDTO> comboBoxDVT = new ComboBox<>();
        configureDonViTinhComboBox(comboBoxDVT);
        comboBoxDVT.getStylesheets().add(getClass().getResource("/com/antam/app/styles/dashboard_style.css").toExternalForm());
        comboBoxDVT.setPromptText("Chọn đơn vị tính");

        comboBoxThuoc.setOnAction(event -> {
            comboBoxDVT.getItems().clear();
            ThuocDTO selectedThuoc = comboBoxThuoc.getValue();
            if (selectedThuoc != null && selectedThuoc.getMaDVTCoSo() != null) {
                DonViTinhDTO dvt = clientManager.getDonViTinhById(selectedThuoc.getMaDVTCoSo().getMaDVT());
                if (dvt != null) {
                    comboBoxDVT.getItems().add(dvt);
                    comboBoxDVT.getSelectionModel().selectFirst();
                }
            }
            tinhTongTien();
        });
        comboBoxDVT.setOnAction(event -> tinhTongTien());

        Spinner<Integer> spinnerSoLuong = new Spinner<>();
        spinnerSoLuong.getStylesheets().add(getClass().getResource("/com/antam/app/styles/dashboard_style.css").toExternalForm());
        spinnerSoLuong.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 1000, 1, 1));
        spinnerSoLuong.valueProperty().addListener((obs, oldValue, newValue) -> tinhTongTien());

        vbThuoc.getChildren().addAll(new Text("Thuốc đổi:"), comboBoxThuoc);
        vbDVT.getChildren().addAll(new Text("Đơn vị tính:"), comboBoxDVT);
        vbSoLuong.getChildren().addAll(new Text("Số lượng:"), spinnerSoLuong);

        Button btnRemove = new Button("X");
        btnRemove.setStyle("-fx-padding: 10 15 10 15; -fx-background-color: #ef4444; -fx-background-radius: 50%; -fx-text-fill: white; -fx-font-weight: BOLD;");
        btnRemove.setOnAction(event -> {
            vbox.getChildren().remove(hBox);
            tinhTongTien();
        });

        hBox.getChildren().addAll(vbThuoc, vbDVT, vbSoLuong, btnRemove);
        vbox.getChildren().add(hBox);
        tinhTongTien();
    }

    public void tinhTongTien() {
        double tongTienTraCoKM = 0;
        double tongTienKhiTra = 0;
        for (ChiTietHoaDonDTO ct : selectedItems) {
            if (ct == null || ct.getMaLoThuocDTO() == null || ct.getMaLoThuocDTO().getMaThuocDTO() == null) {
                continue;
            }
            LoThuocDTO ctt = ct.getMaLoThuocDTO();
            ThuocDTO t = clientManager.getThuocById(ctt.getMaThuocDTO().getMaThuoc());
            if (t == null) {
                continue;
            }
            if (isThuocMoiKhiDoi(ct)) {
                tongTienKhiTra += ct.getThanhTien() * (1 + t.getThue());
            } else {
                tongTienTraCoKM += ct.getThanhTien() * (1 + t.getThue());
            }
        }

        double tongTienMua = 0;
        for (Node node : vhDSCTHDM.getChildren()) {
            if (node instanceof HBox hBox) {
                VBox vbThuoc = (VBox) hBox.getChildren().get(0);
                VBox vbDVT = (VBox) hBox.getChildren().get(1);
                VBox vbSoLuong = (VBox) hBox.getChildren().get(2);
                ComboBox<ThuocDTO> comboThuoc = (ComboBox<ThuocDTO>) vbThuoc.getChildren().get(1);
                ComboBox<DonViTinhDTO> comboDonVi = (ComboBox<DonViTinhDTO>) vbDVT.getChildren().get(1);
                Spinner<Integer> spinnerSoLuong = (Spinner<Integer>) vbSoLuong.getChildren().get(1);
                if (comboThuoc.getValue() != null && comboDonVi.getValue() != null && spinnerSoLuong.getValue() != null) {
                    ThuocDTO t = comboThuoc.getValue();
                    tongTienMua += t.getGiaBan() * spinnerSoLuong.getValue() * (1 + t.getThue());
                }
            }
        }

        DecimalFormat df = new DecimalFormat("#,### đ");
        KhuyenMaiDTO km = null;
        if (hoaDonDTO != null && hoaDonDTO.getMaKM() != null) {
            km = clientManager.getKhuyenMaiById(hoaDonDTO.getMaKM().getMaKM());
        }

        if (km != null && tongTienTraCoKM > 0) {
            tongTienTraCoKM = TinhTienKhuyenMai(tongTienTraCoKM, km.getSo());
            txtTongTienTra.setText(df.format(tongTienTraCoKM + tongTienKhiTra) + " (KM chỉ áp dụng cho thuốc mua)");
        } else {
            txtTongTienTra.setText(df.format(tongTienTraCoKM + tongTienKhiTra));
        }

        txtTongTienMua.setText(df.format(tongTienMua));
        double tienDoi = tongTienMua - (tongTienTraCoKM + tongTienKhiTra);
        if (tienDoi >= 0) {
            txtTongTienDoi.setText("Tổng kết: " + df.format(tienDoi));
            txtThongBaoDoi.setText("Khách hàng cần trả thêm");
        } else {
            txtTongTienDoi.setText("Tổng kết: " + df.format(-tienDoi));
            txtThongBaoDoi.setText("Tiền thừa trả khách");
        }
    }

    public boolean checkThongTinThuoc() {
        for (Node node : vhDSCTHDM.getChildren()) {
            if (node instanceof HBox hBox) {
                VBox vbThuoc = (VBox) hBox.getChildren().get(0);
                VBox vbDVT = (VBox) hBox.getChildren().get(1);
                VBox vbSoLuong = (VBox) hBox.getChildren().get(2);

                ComboBox<ThuocDTO> comboThuoc = (ComboBox<ThuocDTO>) vbThuoc.getChildren().get(1);
                ComboBox<DonViTinhDTO> comboDonVi = (ComboBox<DonViTinhDTO>) vbDVT.getChildren().get(1);
                Spinner<Integer> spinnerSoLuong = (Spinner<Integer>) vbSoLuong.getChildren().get(1);
                if (comboThuoc.getValue() == null || comboDonVi.getValue() == null || spinnerSoLuong.getValue() == null || spinnerSoLuong.getValue() <= 0) {
                    return false;
                }
            }
        }
        return true;
    }

    private void configureThuocComboBox(ComboBox<ThuocDTO> comboBox) {
        comboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(ThuocDTO thuocDTO) {
                return thuocDTO == null || thuocDTO.getTenThuoc() == null ? "" : thuocDTO.getTenThuoc();
            }

            @Override
            public ThuocDTO fromString(String string) {
                return null;
            }
        });
        comboBox.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(ThuocDTO item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null || item.getTenThuoc() == null ? null : item.getTenThuoc());
            }
        });
    }

    private void configureDonViTinhComboBox(ComboBox<DonViTinhDTO> comboBox) {
        comboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(DonViTinhDTO donViTinhDTO) {
                return donViTinhDTO == null || donViTinhDTO.getTenDVT() == null ? "" : donViTinhDTO.getTenDVT();
            }

            @Override
            public DonViTinhDTO fromString(String string) {
                return null;
            }
        });
        comboBox.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(DonViTinhDTO item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null || item.getTenDVT() == null ? null : item.getTenDVT());
            }
        });
    }

    private String getDisplayTenThuoc(ThuocDTO thuocDTO, LoThuocDTO loThuocDTO) {
        if (thuocDTO != null && thuocDTO.getTenThuoc() != null) {
            return thuocDTO.getTenThuoc();
        }
        if (loThuocDTO != null && loThuocDTO.getMaThuocDTO() != null && loThuocDTO.getMaThuocDTO().getTenThuoc() != null) {
            return loThuocDTO.getMaThuocDTO().getTenThuoc();
        }
        return "";
    }

    public double TinhTienKhuyenMai(double tongTien, double giaSo){
        if (giaSo >= 1000 && soLuongThuoc <= 0) {
            return tongTien;
        }
        double giam = 0;
        if (giaSo < 100) {
            giam = tongTien * giaSo / 100.0;
        } else if (giaSo >= 1000) {
            giam = giaSo * ((double) soLuongThuocDoi / soLuongThuoc);
        }
        if (giam > tongTien) giam = tongTien;
        return tongTien - giam;
    }
    private void setFormDisabled(boolean disabled, Button btnApply) {
        txtMaHoaDonDoi.setDisable(disabled);
        txtKhachHangDoi.setDisable(disabled);
        cbLyDoDoi.setDisable(disabled);
        btnThemMoiThuoc.setDisable(disabled);
        btnApply.setDisable(disabled);
        vhDSCTHD.setDisable(disabled);
        vhDSCTHDM.setDisable(disabled);
    }

    private void showWarning(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private boolean processDoiThuoc(String lyDo, ArrayList<ChiTietHoaDonDTO> selectedSnapshot, ArrayList<ThuocMoiSelection> thuocMoiSelections) {
        try {
            if (hoaDonDTO == null) {
                return false;
            }

            if (!validateThuocMoiSelections(thuocMoiSelections, selectedSnapshot)) {
                return false;
            }

            double tongTienCu = hoaDonDTO.getTongTien();
            double tongTienTra = 0;
            double tongTienCoKM = 0;
            double tongTienMua = 0;

            // 1) Tạo chi tiết thuốc mới trước để tránh trạng thái hóa đơn bị "rỗng" khi có lỗi giữa chừng.
            for (ThuocMoiSelection selection : thuocMoiSelections) {
                ThuocDTO t = selection.thuoc;
                if (t == null || t.getMaThuoc() == null) {
                    return false;
                }
                ArrayList<LoThuocDTO> listCTT = new ArrayList<>(clientManager.getLoThuocFefoByThuocId(t.getMaThuoc()));
                ArrayList<LoThuocDTO> danhSachLoHopLe = new ArrayList<>();
                for (LoThuocDTO cts : listCTT) {
                    if (cts != null && !clientManager.tonTaiChiTietHoaDon(hoaDonDTO.getMaHD(), cts.getMaLoThuoc())) {
                        danhSachLoHopLe.add(cts);
                    }
                }

                int soLuong = selection.soLuong;
                for (LoThuocDTO ctt : danhSachLoHopLe) {
                    if (ctt == null) {
                        continue;
                    }
                    int soLuongDung = Math.min(soLuong, ctt.getSoLuong());
                    if (soLuongDung <= 0) {
                        continue;
                    }
                    ChiTietHoaDonDTO newCTHD = new ChiTietHoaDonDTO(
                            hoaDonDTO,
                            ctt,
                            soLuongDung,
                            selection.donViTinh,
                            "Thuốc Mới Khi Đổi",
                            t.getGiaBan() * soLuongDung
                    );
                    if (!clientManager.createChiTietHoaDon(newCTHD)) {
                        return false;
                    }
                    if (!clientManager.updateLoThuocQuantity(ctt.getMaLoThuoc(), -soLuongDung)) {
                        return false;
                    }
                    tongTienMua += Math.round(t.getGiaBan() * soLuongDung * (1 + t.getThue()) * 100.0) / 100.0;
                    soLuong -= soLuongDung;
                    if (soLuong <= 0) {
                        break;
                    }
                }
            }

            // 2) Tính tiền phần thuốc cũ trước khi đổi trạng thái.
            for (ChiTietHoaDonDTO ct : selectedSnapshot) {
                LoThuocDTO ctt = ct == null ? null : ct.getMaLoThuocDTO();
                ThuocDTO thuocDTO = ctt == null || ctt.getMaThuocDTO() == null ? null : clientManager.getThuocById(ctt.getMaThuocDTO().getMaThuoc());
                if (thuocDTO == null || ct == null || ct.getTinhTrang() == null) {
                    continue;
                }
                if (!isThuocMoiKhiDoi(ct)) {
                    tongTienCoKM += ct.getThanhTien() * (1 + thuocDTO.getThue());
                } else {
                    tongTienTra += ct.getThanhTien() * (1 + thuocDTO.getThue());
                }
            }

            // 3) Sau khi thêm thuốc mới thành công mới soft-delete thuốc cũ.
            for (ChiTietHoaDonDTO ct : selectedSnapshot) {
                if (ct == null || ct.getMaHD() == null || ct.getMaLoThuocDTO() == null) {
                    return false;
                }
                if (!clientManager.softDeleteChiTietHoaDon(ct.getMaHD().getMaHD(), ct.getMaLoThuocDTO().getMaLoThuoc(), "Trả Khi Đổi")) {
                    return false;
                }
                if (canCongLaiKho(lyDo)) {
                    if (!clientManager.updateLoThuocQuantity(ct.getMaLoThuocDTO().getMaLoThuoc(), ct.getSoLuong())) {
                        return false;
                    }
                }
            }

            if (hoaDonDTO.getMaKM() != null) {
                KhuyenMaiDTO km = clientManager.getKhuyenMaiById(hoaDonDTO.getMaKM().getMaKM());
                if (km != null) {
                    tongTienCoKM = TinhTienKhuyenMai(tongTienCoKM, km.getSo());
                }
            }

            return clientManager.updateHoaDonTongTien(hoaDonDTO.getMaHD(), tongTienCu - tongTienCoKM - tongTienTra + tongTienMua);
        } catch (Exception e) {
            return false;
        }
    }

    private boolean validateThuocMoiSelections(ArrayList<ThuocMoiSelection> thuocMoiSelections, ArrayList<ChiTietHoaDonDTO> selectedSnapshot) {
        if (thuocMoiSelections == null || thuocMoiSelections.isEmpty()) {
            return false;
        }
        for (ThuocMoiSelection selection : thuocMoiSelections) {
            if (selection == null || selection.thuoc == null || selection.donViTinh == null || selection.soLuong <= 0) {
                return false;
            }
            ArrayList<LoThuocDTO> listCTT = new ArrayList<>(clientManager.getLoThuocFefoByThuocId(selection.thuoc.getMaThuoc()));
            ArrayList<LoThuocDTO> danhSachLoHopLe = new ArrayList<>();
            int tongSoLuong = 0;
            for (LoThuocDTO cts : listCTT) {
                if (cts == null) {
                    continue;
                }
                boolean daTonTaiTrongHoaDon = clientManager.tonTaiChiTietHoaDon(hoaDonDTO.getMaHD(), cts.getMaLoThuoc());
                if (!daTonTaiTrongHoaDon || isSelectedLotForReplace(cts.getMaLoThuoc(), selectedSnapshot)) {
                    danhSachLoHopLe.add(cts);
                    tongSoLuong += cts.getSoLuong();
                }
            }
            if (tongSoLuong < selection.soLuong) {
                return false;
            }
            for (LoThuocDTO ctt : danhSachLoHopLe) {
                long countThuocMoiKhiDoi = selectedSnapshot.stream()
                        .filter(ct -> ct != null && ct.getMaLoThuocDTO() != null && ct.getMaLoThuocDTO().getMaLoThuoc() == ctt.getMaLoThuoc() && isThuocMoiKhiDoi(ct))
                        .count();
                if (countThuocMoiKhiDoi >= 2) {
                    return false;
                }
            }
        }
        return true;
    }

    private ArrayList<ThuocMoiSelection> collectThuocMoiSelections() {
        ArrayList<ThuocMoiSelection> results = new ArrayList<>();
        for (Node node : vhDSCTHDM.getChildren()) {
            if (node instanceof HBox hBox) {
                VBox vbThuoc = (VBox) hBox.getChildren().get(0);
                VBox vbDVT = (VBox) hBox.getChildren().get(1);
                VBox vbSoLuong = (VBox) hBox.getChildren().get(2);

                ComboBox<ThuocDTO> comboThuoc = (ComboBox<ThuocDTO>) vbThuoc.getChildren().get(1);
                ComboBox<DonViTinhDTO> comboDonVi = (ComboBox<DonViTinhDTO>) vbDVT.getChildren().get(1);
                Spinner<Integer> spinnerSoLuong = (Spinner<Integer>) vbSoLuong.getChildren().get(1);
                ThuocDTO thuoc = comboThuoc.getValue();
                DonViTinhDTO dvt = comboDonVi.getValue();
                Integer soLuong = spinnerSoLuong.getValue();
                if (thuoc != null && dvt != null && soLuong != null && soLuong > 0) {
                    results.add(new ThuocMoiSelection(thuoc, dvt, soLuong));
                }
            }
        }
        return results;
    }

    private boolean isSelectedLotForReplace(int maLoThuoc, ArrayList<ChiTietHoaDonDTO> selectedSnapshot) {
        if (selectedSnapshot == null) {
            return false;
        }
        for (ChiTietHoaDonDTO ct : selectedSnapshot) {
            if (ct != null && ct.getMaLoThuocDTO() != null && ct.getMaLoThuocDTO().getMaLoThuoc() == maLoThuoc) {
                return true;
            }
        }
        return false;
    }

    private boolean isTrangThai(ChiTietHoaDonDTO chiTietHoaDonDTO, String trangThai) {
        return chiTietHoaDonDTO != null && trangThai != null && trangThai.equals(chiTietHoaDonDTO.getTinhTrang());
    }

    private boolean isDaTra(ChiTietHoaDonDTO chiTietHoaDonDTO) {
        return isTrangThai(chiTietHoaDonDTO, "Trả") || isTrangThai(chiTietHoaDonDTO, "Trả Khi Đổi");
    }

    private boolean isThuocMoiKhiDoi(ChiTietHoaDonDTO chiTietHoaDonDTO) {
        return isTrangThai(chiTietHoaDonDTO, "Thuốc Mới Khi Đổi");
    }

    private boolean isBan(ChiTietHoaDonDTO chiTietHoaDonDTO) {
        return isTrangThai(chiTietHoaDonDTO, "Bán");
    }

    private boolean canCongLaiKho(String lyDo) {
        if (lyDo == null) {
            return false;
        }
        switch (lyDo) {
            case "Khách hàng đổi ý":
            case "Nhập nhầm lô / dư":
            case "Sai thông tin đơn / bảo hiểm":
                return true;
            default:
                return false;
        }
    }
}

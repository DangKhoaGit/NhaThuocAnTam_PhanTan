//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.antam.app.controller.hoadon;

import com.antam.app.network.ClientManager;
import com.antam.app.service.impl.*;
import com.antam.app.dto.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.text.Font;


public class TraThuocFormController extends DialogPane{
    private final VBox vbListChiTietHoaDon;
    private final TextField txtMaHoaDonTra, txtKhachHangTra;
    private final Text txtTongTienTra;
    private final ComboBox<String> cbLyDoTra;


    private final ClientManager clientManager;
    private HoaDonDTO hoaDonDTO;
    private final ArrayList<ChiTietHoaDonDTO> selectedItems = new ArrayList<>();

    public void setHoaDon(HoaDonDTO hoaDonDTO) {
        this.hoaDonDTO = hoaDonDTO;
    }

    public HoaDonDTO getHoaDOn() {
        return hoaDonDTO;
    }

    // Hiển thị thông tin hóa đơn và chi tiết hóa đơn
    public void showData(HoaDonDTO hoaDonDTO) {
        HoaDonDTO hd = (HoaDonDTO) clientManager.getHoaDonById(hoaDonDTO.getMaHD());
        ArrayList<ChiTietHoaDonDTO> chiTietHoaDons = new ArrayList<>();
        for (Object item : clientManager.getChiTietHoaDon(hoaDonDTO.getMaHD())) {
            if (item instanceof ChiTietHoaDonDTO dto) {
                chiTietHoaDons.add(dto);
            }
        }
        txtMaHoaDonTra.setText(hoaDonDTO.getMaHD());
        vbListChiTietHoaDon.getChildren().clear();
        selectedItems.clear();
        if (hd != null && hd.getMaKH() != null) {
            KhachHangDTO khachHangDTO = clientManager.getKhachHangById(hd.getMaKH().getMaKH());
            txtKhachHangTra.setText(khachHangDTO == null ? "" : khachHangDTO.getTenKH());
        } else {
            txtKhachHangTra.setText("");
        }
        for (ChiTietHoaDonDTO ct : chiTietHoaDons) {
            HBox hBox = renderChiTietHoaDon(ct);
            vbListChiTietHoaDon.getChildren().add(hBox);
        }
        txtTongTienTra.setText("0.0 đ");
    }

    public TraThuocFormController(){
        this.clientManager = ClientManager.getInstance();
        this.setPrefWidth(800);

        FlowPane header = new FlowPane();
        header.setAlignment(Pos.CENTER);
        header.setStyle("-fx-background-color: #1e3a8a;");

        Text title = new Text("Trả thuốc");
        title.setFill(Color.WHITE);
        title.setFont(Font.font("System", FontWeight.BOLD, 15));
        FlowPane.setMargin(title, new Insets(10, 0, 10, 0));
        header.getChildren().add(title);

        this.setHeader(header);

        // ================= CONTENT ====================
        AnchorPane contentRoot = new AnchorPane();
        VBox mainVBox = new VBox(10);
        AnchorPane.setLeftAnchor(mainVBox, 0.0);
        AnchorPane.setRightAnchor(mainVBox, 0.0);

        // ---------------- ScrollPane ------------------
        ScrollPane scroll = new ScrollPane();
        scroll.setFitToWidth(true);
        scroll.setPrefHeight(500);

        VBox scrollContent = new VBox(10);

        // ================= GRIDPANE ===================
        GridPane grid = new GridPane();
        grid.setHgap(5);

        // Column constraints
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setHgrow(Priority.SOMETIMES);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setHgrow(Priority.SOMETIMES);
        grid.getColumnConstraints().addAll(col1, col2);

        // Row constraints
        for (int i = 0; i < 4; i++) {
            RowConstraints rc = new RowConstraints();
            rc.setPrefHeight(new double[]{30, 40, 30, 30}[i]);
            rc.setVgrow(Priority.SOMETIMES);
            grid.getRowConstraints().add(rc);
        }

        // ==== Text + TextField + ComboBox =====
        Text lblMaHD = new Text("Mã hóa đơn gốc:");
        lblMaHD.setFill(Color.valueOf("#374151"));

        txtMaHoaDonTra = new TextField();
        txtMaHoaDonTra.setPrefHeight(40);
        txtMaHoaDonTra.getStyleClass().add("text-field");

        Text lblKH = new Text("Khách hàng:");
        lblKH.setFill(Color.valueOf("#374151"));

        txtKhachHangTra = new TextField();
        txtKhachHangTra.setPrefHeight(40);
        txtKhachHangTra.getStyleClass().add("text-field");

        Text lblLyDo = new Text("Lý do trả:");
        lblLyDo.setFill(Color.valueOf("#374151"));

        cbLyDoTra = new ComboBox<>();
        cbLyDoTra.setPrefWidth(150);

        // Add to grid
        grid.add(lblMaHD, 0, 0);
        grid.add(txtMaHoaDonTra, 0, 1);

        grid.add(lblKH, 1, 0);
        grid.add(txtKhachHangTra, 1, 1);

        grid.add(lblLyDo, 0, 2);
        grid.add(cbLyDoTra, 0, 3);

        // ========= Danh sách thuốc (VBox) ===========
        Text lblThuocTra = new Text("Thuốc trả:");
        lblThuocTra.setFill(Color.BLACK);

        vbListChiTietHoaDon = new VBox(5);
        vbListChiTietHoaDon.setStyle("-fx-border-color: #e5e7eb; -fx-border-radius: 6px; -fx-border-width: 2px;");
        vbListChiTietHoaDon.setPadding(new Insets(10));

        // Add nodes inside scroll content
        scrollContent.getChildren().addAll(grid, lblThuocTra, vbListChiTietHoaDon);
        scroll.setContent(scrollContent);

        // =========== Tổng tiền trả grid cuối =============
        GridPane bottomGrid = new GridPane();
        bottomGrid.setHgap(5);
        bottomGrid.setStyle("-fx-background-color: #f8fafc; -fx-background-radius: 8px;");
        bottomGrid.setPadding(new Insets(10));

        ColumnConstraints bc1 = new ColumnConstraints();
        bc1.setHgrow(Priority.SOMETIMES);
        ColumnConstraints bc2 = new ColumnConstraints();
        bc2.setHgrow(Priority.SOMETIMES);
        bottomGrid.getColumnConstraints().addAll(bc1, bc2);

        Text lblTongTien = new Text("Tổng tiền trả:");
        lblTongTien.setFont(Font.font("System", FontWeight.BOLD, 18));
        lblTongTien.setFill(Color.valueOf("#374151"));

        txtTongTienTra = new Text("500.000đ");
        txtTongTienTra.setFont(Font.font("System", FontWeight.BOLD, 18));
        txtTongTienTra.setFill(Color.valueOf("#374151"));

        bottomGrid.add(lblTongTien, 0, 0);
        bottomGrid.add(txtTongTienTra, 1, 0);

        // Compose all into main vbox
        mainVBox.getChildren().addAll(scroll, bottomGrid);
        contentRoot.getChildren().add(mainVBox);

        this.setContent(contentRoot);
        var css = getClass().getResource("/com/antam/app/styles/dashboard_style.css");
        if (css != null) {
            this.getStylesheets().add(css.toExternalForm());
        }

        // Sự kiện
        ButtonType cancelButton = new ButtonType("Huỷ", ButtonData.CANCEL_CLOSE);
        ButtonType applyButton = new ButtonType("Xác nhận trả thuốc", ButtonData.APPLY);
        this.getButtonTypes().add(cancelButton);
        this.getButtonTypes().add(applyButton);

        // Xử lý sự kiện khi nhấn nút Xác nhận trả thuốc
        Button applyBtn = (Button) this.lookupButton(applyButton);
        applyBtn.addEventFilter(ActionEvent.ACTION, event -> {
            if (selectedItems.isEmpty()) {
                showWarning("Cảnh báo", "Chưa chọn thuốc để trả", "Vui lòng chọn ít nhất một thuốc để trả.");
                event.consume();
                return;
            }

            String lyDoTra = cbLyDoTra.getValue();
            if (lyDoTra == null || lyDoTra.trim().isEmpty()) {
                showWarning("Cảnh báo", "Chưa chọn lý do trả thuốc", "Vui lòng chọn lý do trả trước khi xác nhận.");
                event.consume();
                return;
            }

            ArrayList<ChiTietHoaDonDTO> selectedSnapshot = new ArrayList<>(selectedItems);
            Task<Boolean> applyTask = new Task<>() {
                @Override
                protected Boolean call() {
                    return processTraThuoc(lyDoTra, selectedSnapshot);
                }
            };

            applyTask.setOnRunning(e -> setFormDisabled(true, applyBtn));
            applyTask.setOnSucceeded(e -> {
                setFormDisabled(false, applyBtn);
                if (Boolean.TRUE.equals(applyTask.getValue())) {
                    if (getScene() != null && getScene().getWindow() != null) {
                        getScene().getWindow().hide();
                    }
                } else {
                    showWarning("Cảnh báo", "Không thể thực hiện giao dịch", "Server không xử lý được yêu cầu trả thuốc.");
                }
            });
            applyTask.setOnFailed(e -> {
                setFormDisabled(false, applyBtn);
                Throwable ex = applyTask.getException();
                showWarning("Lỗi", "Trả thuốc thất bại", ex == null ? "Lỗi kết nối tới server!" : ex.getMessage());
            });

            Thread thread = new Thread(applyTask, "tra-thuoc-apply-task");
            thread.setDaemon(true);
            thread.start();
            event.consume();
        });

        // Thêm giá trị vào combobox lý do trả
        addValueCombobox();
    }
    // Tính tổng tiền trả
    public void tinhTongTienTra(){
        double tongTien = 0;
        double tongTienKhiTra = 0;
        for (ChiTietHoaDonDTO ct : selectedItems){
            if (ct == null || ct.getMaLoThuocDTO() == null || ct.getMaLoThuocDTO().getMaThuocDTO() == null) {
                continue;
            }
            LoThuocDTO ctt = ct.getMaLoThuocDTO();
            ThuocDTO t = clientManager.getThuocById(ctt.getMaThuocDTO().getMaThuoc());
            if (t == null) {
                continue;
            }
            if (isThuocMoiKhiDoi(ct)){
                tongTienKhiTra += ct.getThanhTien() * (1 + t.getThue());
            } else {
                tongTien += ct.getThanhTien() * (1 + t.getThue());
            }
        }
        DecimalFormat df = new DecimalFormat("#,### đ");
        if (hoaDonDTO != null && hoaDonDTO.getMaKM() != null) {
            String maKM = hoaDonDTO.getMaKM().getMaKM();
            KhuyenMaiDTO km = clientManager.getKhuyenMaiById(maKM);

            if (km != null) {
                tongTien = TinhTienKhuyenMai(tongTien, km.getSo());
                txtTongTienTra.setText(df.format(tongTien + tongTienKhiTra) + " (Có áp dụng KM)");
            } else {
                txtTongTienTra.setText(df.format(tongTien + tongTienKhiTra));
            }
        } else {
            txtTongTienTra.setText(df.format(tongTien + tongTienKhiTra));
        }
    }

    public void addValueCombobox(){
        ObservableList<String> lyDoList = FXCollections.observableArrayList(
                "Hết hạn sử dụng",
                "Bao bì bị hư hỏng",
                "Khách hàng đổi ý",
                "Thuốc lỗi / hư hỏng",
                "Nhập nhầm lô / dư",
                "Thuốc bị thu hồi",
                "Sai thông tin đơn / bảo hiểm"
        );
        cbLyDoTra.setItems(lyDoList);
    }

    public HBox renderChiTietHoaDon(ChiTietHoaDonDTO chiTietHoaDonDTO) {
        if (chiTietHoaDonDTO == null) {
            HBox h = new HBox();
            h.setVisible(false);
            h.setManaged(false);
            return h;
        }
        if (chiTietHoaDonDTO.getMaLoThuocDTO() == null || chiTietHoaDonDTO.getMaLoThuocDTO().getMaThuocDTO() == null) {
            HBox h = new HBox();
            h.setVisible(false);
            h.setManaged(false);
            return h;
        }

        boolean daTra = isDaTra(chiTietHoaDonDTO);

        HBox hBox = new HBox();
        hBox.setSpacing(20);
        hBox.setStyle(
                "-fx-background-color: #f8fafc;" +
                "-fx-padding: 10;"
        );

        CheckBox checkBox = new CheckBox();
        checkBox.setDisable(daTra);
        checkBox.setOnAction(event -> {
            if (checkBox.isSelected()) {
                if (!selectedItems.contains(chiTietHoaDonDTO)) {
                    selectedItems.add(chiTietHoaDonDTO);
                }
            } else {
                selectedItems.remove(chiTietHoaDonDTO);
            }
            tinhTongTienTra();
        });

        LoThuocDTO ctt = clientManager.getLoThuocByLoThuocId(chiTietHoaDonDTO.getMaLoThuocDTO().getMaLoThuoc());
        ThuocDTO t = ctt == null || ctt.getMaThuocDTO() == null ? null : clientManager.getThuocById(ctt.getMaThuocDTO().getMaThuoc());
        Text txtMaThuoc = new Text(getDisplayTenThuoc(t, ctt));
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
        if (daTra) {
            valueBtn = "Đã trả";
        }

        Button btn = new Button(valueBtn);
        btn.setDisable(daTra);
        btn.setStyle(
                "-fx-background-color: #e0f2fe;" +
                "-fx-text-fill: #0369a1;" +
                "-fx-background-radius: 10;" +
                "-fx-font-size: 12px;"
        );

        hBox.getChildren().addAll(checkBox, txtMaThuoc, txtSoLuong, txtDonGia, btn);
        return hBox;
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
        double giam = 0;
        if (giaSo < 100) {
            giam = tongTien * giaSo / 100.0;
        } else if (giaSo >= 1000) {
            giam = giaSo;
        }
        if (giam > tongTien) giam = tongTien;
        return tongTien - giam;
    }

    private void setFormDisabled(boolean disabled, Button applyBtn) {
        txtMaHoaDonTra.setDisable(disabled);
        txtKhachHangTra.setDisable(disabled);
        cbLyDoTra.setDisable(disabled);
        applyBtn.setDisable(disabled);
        vbListChiTietHoaDon.setDisable(disabled);
    }

    private void showWarning(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private boolean processTraThuoc(String lyDoTra, ArrayList<ChiTietHoaDonDTO> selectedSnapshot) {
        try {
            if (hoaDonDTO == null || selectedSnapshot == null || selectedSnapshot.isEmpty()) {
                return false;
            }

            for (ChiTietHoaDonDTO ct : selectedSnapshot) {
                if (ct == null || ct.getMaHD() == null || ct.getMaLoThuocDTO() == null) {
                    return false;
                }
                if (!clientManager.softDeleteChiTietHoaDon(ct.getMaHD().getMaHD(), ct.getMaLoThuocDTO().getMaLoThuoc(), "Trả")) {
                    return false;
                }

                if (canCongLaiKho(lyDoTra)) {
                    if (!clientManager.updateLoThuocQuantity(ct.getMaLoThuocDTO().getMaLoThuoc(), ct.getSoLuong())) {
                        return false;
                    }
                }
            }

            if (clientManager.getChiTietHoaDonConBanByHoaDonId(hoaDonDTO.getMaHD()).isEmpty()) {
                // Keep invoice for history; only set total to 0.
                hoaDonDTO.setTongTien(0);
                return clientManager.updateHoaDonTongTien(hoaDonDTO.getMaHD(), 0);
            }

            double tongTienCu = hoaDonDTO.getTongTien();
            double tongTienTra = 0;
            double tongTienCoKM = 0;
            for (ChiTietHoaDonDTO ct : selectedSnapshot) {
                LoThuocDTO ctt = ct == null ? null : ct.getMaLoThuocDTO();
                ThuocDTO t = ctt == null || ctt.getMaThuocDTO() == null ? null : clientManager.getThuocById(ctt.getMaThuocDTO().getMaThuoc());
                if (t == null) {
                    continue;
                }
                if (isThuocMoiKhiDoi(ct)) {
                    tongTienTra += ct.getThanhTien() * (1 + t.getThue());
                } else {
                    tongTienCoKM += ct.getThanhTien() * (1 + t.getThue());
                }
            }
            KhuyenMaiDTO km = null;

            if (hoaDonDTO.getMaKM() != null) {
                km = clientManager.getKhuyenMaiById(hoaDonDTO.getMaKM().getMaKM());
            }

            if (km != null) {
                tongTienCoKM = TinhTienKhuyenMai(tongTienCoKM, km.getSo());
            }

            double tongMoi = tongTienCu - tongTienTra - tongTienCoKM;
            hoaDonDTO.setTongTien(tongMoi);
            return clientManager.updateHoaDonTongTien(hoaDonDTO.getMaHD(), tongMoi);
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isTrangThai(ChiTietHoaDonDTO chiTietHoaDonDTO, String tinhTrang) {
        return chiTietHoaDonDTO != null && tinhTrang != null && tinhTrang.equals(chiTietHoaDonDTO.getTinhTrang());
    }

    private boolean isThuocMoiKhiDoi(ChiTietHoaDonDTO chiTietHoaDonDTO) {
        return isTrangThai(chiTietHoaDonDTO, "Thuốc Mới Khi Đổi");
    }

    private boolean isDaTra(ChiTietHoaDonDTO chiTietHoaDonDTO) {
        return isTrangThai(chiTietHoaDonDTO, "Trả") || isTrangThai(chiTietHoaDonDTO, "Trả Khi Đổi");
    }

    private boolean canCongLaiKho(String lyDoTra) {
        if (lyDoTra == null) {
            return false;
        }
        return switch (lyDoTra) {
            case "Khách hàng đổi ý", "Nhập nhầm lô / dư", "Sai thông tin đơn / bảo hiểm" -> true;
            default -> false;
        };
    }
}

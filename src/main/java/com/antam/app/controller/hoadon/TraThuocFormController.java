//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.antam.app.controller.hoadon;

import com.antam.app.connect.ConnectDB;
import com.antam.app.dao.*;
import com.antam.app.dao.impl.*;
import com.antam.app.entity.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.text.Font;


public class TraThuocFormController extends DialogPane{
    private VBox vbListChiTietHoaDon;
    private TextField txtMaHoaDonTra, txtKhachHangTra;
    private Text txtTongTienTra;
    private ComboBox<String> cbLyDoTra;


    private Thuoc_DAO thuoc_dao = new Thuoc_DAO();
    private HoaDon_DAO hoaDon_dao = new HoaDon_DAO();
    private KhachHang_DAO khachHang_dao = new KhachHang_DAO();
    private ChiTietHoaDon_DAO chiTietHoaDon_dao = new ChiTietHoaDon_DAO();
    private LoThuoc_DAO chiTietThuoc_dao = new LoThuoc_DAO();
    private KhuyenMai_DAO khuyenMai_dao = new KhuyenMai_DAO();
    private HoaDon hoaDon;
    private ArrayList<ChiTietHoaDon> selectedItems = new ArrayList<>();

    public void setHoaDon(HoaDon hoaDon) {
        this.hoaDon = hoaDon;
    }

    public HoaDon getHoaDOn() {
        return hoaDon;
    }

    // Hiển thị thông tin hóa đơn và chi tiết hóa đơn
    public void showData(HoaDon hoaDon) {
        HoaDon hd = hoaDon_dao.getHoaDonTheoMa(hoaDon.getMaHD());
        ArrayList<ChiTietHoaDon> chiTietHoaDons = chiTietHoaDon_dao.getAllChiTietHoaDonTheoMaHD(hoaDon.getMaHD());
        txtMaHoaDonTra.setText(hoaDon.getMaHD());
        txtKhachHangTra.setText(khachHang_dao.getKhachHangTheoMa(hd.getMaKH().getMaKH()).getTenKH());
        for (ChiTietHoaDon ct : chiTietHoaDons) {
            HBox hBox = renderChiTietHoaDon(ct);
            vbListChiTietHoaDon.getChildren().add(hBox);
        }
        txtTongTienTra.setText("0.0 đ");
    }

    public TraThuocFormController(){
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
        this.getStylesheets().add(getClass().getResource("/com/antam/app/styles/dashboard_style.css").toExternalForm());

        /** Sự kiện **/
        ButtonType cancelButton = new ButtonType("Huỷ", ButtonData.CANCEL_CLOSE);
        ButtonType applyButton = new ButtonType("Xác nhận trả thuốc", ButtonData.APPLY);
        this.getButtonTypes().add(cancelButton);
        this.getButtonTypes().add(applyButton);

        // Kết nối DB
        try { Connection con = ConnectDB.getInstance().connect(); }
        catch (SQLException e) { throw new RuntimeException(e); }
        // Xử lý sự kiện khi nhấn nút Xác nhận trả thuốc
        Button applyBtn = (Button) this.lookupButton(applyButton);
        applyBtn.addEventFilter(ActionEvent.ACTION, event -> {
            if (selectedItems.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Cảnh báo");
                alert.setHeaderText("Chưa chọn thuốc để trả");
                alert.setContentText("Vui lòng chọn ít nhất một thuốc để trả.");
                alert.showAndWait();
                event.consume();
            } else {
                try {
                    Connection con = ConnectDB.getInstance().connect();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                String lyDoTra = cbLyDoTra.getValue();
                if (lyDoTra == null || lyDoTra.trim().isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Cảnh báo");
                    alert.setHeaderText("Chưa chọn lý do trả thuốc");
                    alert.setContentText("Vui lòng chọn lý do trả trước khi xác nhận.");
                    alert.showAndWait();
                    event.consume();
                    return;
                }

                for (ChiTietHoaDon ct : selectedItems) {
                    chiTietHoaDon_dao.xoaMemChiTietHoaDon(ct.getMaHD().getMaHD(), ct.getMaLoThuoc().getMaLoThuoc(), "Trả");

                    switch (lyDoTra) {
                        // Các lý do KHÔNG cộng lại vào kho
                        case "Hết hạn sử dụng":
                        case "Bao bì bị hư hỏng":
                        case "Thuốc lỗi / hư hỏng":
                        case "Thuốc bị thu hồi":
                            // Không làm gì cả
                            break;

                        // Các lý do CÓ cộng lại vào kho
                        case "Khách hàng đổi ý":
                        case "Nhập nhầm lô / dư":
                        case "Sai thông tin đơn / bảo hiểm":
                            Thuoc t = thuoc_dao.getThuocTheoMa(
                                    chiTietThuoc_dao
                                            .getChiTietThuoc(ct.getMaLoThuoc().getMaLoThuoc())
                                            .getMaThuoc()
                                            .getMaThuoc()
                            );
                            chiTietThuoc_dao.CapNhatSoLuongChiTietThuoc(
                                    ct.getMaLoThuoc().getMaLoThuoc(),
                                    ct.getSoLuong()
                            );
                            break;

                        default:
                            // Nếu có giá trị không nằm trong danh sách trên
                            System.out.println("Lý do trả không hợp lệ: " + lyDoTra);
                            break;
                    }

                }
                if (chiTietHoaDon_dao.getAllChiTietHoaDonTheoMaHDConBan(hoaDon.getMaHD()).isEmpty()) {
                    hoaDon_dao.xoaMemHoaDon(hoaDon.getMaHD());
                    hoaDon_dao.CapNhatTongTienHoaDon(hoaDon.getMaHD(), 0);
                } else {
                    double tongTienCu = hoaDon.getTongTien();
                    double tongTienTra = 0;
                    double tongTienCoKM = 0;
                    for (ChiTietHoaDon ct : selectedItems) {
                        LoThuoc ctt = ct.getMaLoThuoc();
                        Thuoc t = thuoc_dao.getThuocTheoMa(ctt.getMaThuoc().getMaThuoc());
                        if (ct.getTinhTrang().equals("Thuốc Mới Khi Đổi")){
                            tongTienTra += ct.getThanhTien() * (1 + t.getThue());
                        } else {
                            tongTienCoKM += ct.getThanhTien() * (1 + t.getThue());
                        }
                    }
                    KhuyenMai km = null;

                    if (hoaDon.getMaKM() != null) {
                        km = khuyenMai_dao.getKhuyenMaiTheoMa(hoaDon.getMaKM().getMaKM());
                    }

                    if (km != null) {
                        tongTienCoKM = TinhTienKhuyenMai(tongTienCoKM, km.getSo());
                    }

                    double tongMoi = tongTienCu - tongTienTra - tongTienCoKM;
                    hoaDon.setTongTien(tongMoi);
                    hoaDon_dao.CapNhatTongTienHoaDon(hoaDon.getMaHD(), tongMoi);

                }
            }
        });

        // Thêm giá trị vào combobox lý do trả
        addValueCombobox();
    }
    // Tính tổng tiền trả
    public void tinhTongTienTra(){
        double tongTien = 0;
        double tongTienKhiTra = 0;
        for (ChiTietHoaDon ct : selectedItems){
            LoThuoc ctt = ct.getMaLoThuoc();
            Thuoc t = thuoc_dao.getThuocTheoMa(ctt.getMaThuoc().getMaThuoc());
            if (ct.getTinhTrang().equals("Thuốc Mới Khi Đổi")){
                tongTienKhiTra += ct.getThanhTien() * (1 + t.getThue());
            } else {
                tongTien += ct.getThanhTien() * (1 + t.getThue());
            }
        }
        DecimalFormat df = new DecimalFormat("#,### đ");
        if (hoaDon.getMaKM() != null) {

            String maKM = hoaDon.getMaKM().getMaKM();
            KhuyenMai km = khuyenMai_dao.getKhuyenMaiTheoMa(maKM);

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
    // Thêm giá trị vào combobox lý do trả
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
    // Render chi tiết hóa đơn
    public HBox renderChiTietHoaDon(ChiTietHoaDon chiTietHoaDon) {
        if (chiTietHoaDon.getTinhTrang() == null) {
            HBox h = new HBox();
            h.setVisible(false);
            h.setManaged(false);
            return h;
        }
        if (chiTietHoaDon.getTinhTrang().equals("Trả") || chiTietHoaDon.getTinhTrang().equals("Trả Khi Đổi")) {
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
            if (checkBox.isSelected()) {
                if (!selectedItems.contains(chiTietHoaDon)) {
                    selectedItems.add(chiTietHoaDon);
                }
            } else {
                selectedItems.remove(chiTietHoaDon);
            }
            tinhTongTienTra();
        });
        try {
            Connection con = ConnectDB.getInstance().connect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        LoThuoc ctt = chiTietThuoc_dao.getChiTietThuoc(chiTietHoaDon.getMaLoThuoc().getMaLoThuoc());
        Thuoc t = thuoc_dao.getThuocTheoMa(ctt.getMaThuoc().getMaThuoc());
        Text txtMaThuoc = new Text(t.getTenThuoc());
        txtMaThuoc.setStyle("-fx-font-size: 15px;");
        Text txtSoLuong = new Text("SL " + chiTietHoaDon.getSoLuong());
        txtSoLuong.setStyle("-fx-font-size: 15px;");
        DecimalFormat df = new DecimalFormat("#,### đ");
        Text txtDonGia = new Text(df.format(chiTietHoaDon.getThanhTien()));
        txtDonGia.setStyle("-fx-font-size: 15px;");
        String valueBtn = "Bình thường";
        if (chiTietHoaDon.getTinhTrang().equals("Thuốc Mới Khi Đổi")){
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

    public double TinhTienKhuyenMai(double tongTien, double giaSo){
        double giam = 0;
        if (giaSo < 100) {
            giam = tongTien * giaSo / 100.0;
        } else if (giaSo >= 1000) {
            giam = giaSo;
        }
        if (giam > tongTien) giam = tongTien;
        tongTien -= giam;
        return tongTien;
    }

}

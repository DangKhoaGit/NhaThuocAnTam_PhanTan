//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.antam.app.controller.hoadon;

import com.antam.app.connect.ConnectDB;
import com.antam.app.service.impl.*;
import com.antam.app.dto.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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


    private Thuoc_Service thuoc_service = new Thuoc_Service();
    private HoaDon_Service hoaDon_service = new HoaDon_Service();
    private KhachHang_Service khachHang_service = new KhachHang_Service();
    private ChiTietHoaDon_Service chiTietHoaDon_service = new ChiTietHoaDon_Service();
    private LoThuoc_Service loThuoc_service = new LoThuoc_Service();
    private KhuyenMai_Service khuyenMai_service = new KhuyenMai_Service();
    private HoaDonDTO hoaDonDTO;
    private ArrayList<ChiTietHoaDonDTO> selectedItems = new ArrayList<>();

    public void setHoaDon(HoaDonDTO hoaDonDTO) {
        this.hoaDonDTO = hoaDonDTO;
    }

    public HoaDonDTO getHoaDOn() {
        return hoaDonDTO;
    }

    // Hiển thị thông tin hóa đơn và chi tiết hóa đơn
    public void showData(HoaDonDTO hoaDonDTO) {
        HoaDonDTO hd = hoaDon_service.getHoaDonTheoMa(hoaDonDTO.getMaHD());
        ArrayList<ChiTietHoaDonDTO> chiTietHoaDons = chiTietHoaDon_service.getAllChiTietHoaDonTheoMaHD(hoaDonDTO.getMaHD());
        txtMaHoaDonTra.setText(hoaDonDTO.getMaHD());
        txtKhachHangTra.setText(khachHang_service.getKhachHangTheoMa(hd.getMaKH().getMaKH()).getTenKH());
        for (ChiTietHoaDonDTO ct : chiTietHoaDons) {
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

                for (ChiTietHoaDonDTO ct : selectedItems) {
                    chiTietHoaDon_service.xoaMemChiTietHoaDon(ct.getMaHD().getMaHD(), ct.getMaLoThuocDTO().getMaLoThuoc(), "Trả");

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
                            ThuocDTO t = thuoc_service.getThuocTheoMa(
                                    loThuoc_service
                                            .getChiTietThuoc(ct.getMaLoThuocDTO().getMaLoThuoc())
                                            .getMaThuocDTO()
                                            .getMaThuoc()
                            );
                            loThuoc_service.CapNhatSoLuongChiTietThuoc(
                                    ct.getMaLoThuocDTO().getMaLoThuoc(),
                                    ct.getSoLuong()
                            );
                            break;

                        default:
                            // Nếu có giá trị không nằm trong danh sách trên
                            System.out.println("Lý do trả không hợp lệ: " + lyDoTra);
                            break;
                    }

                }
                if (chiTietHoaDon_service.getAllChiTietHoaDonTheoMaHDConBan(hoaDonDTO.getMaHD()).isEmpty()) {
                    hoaDon_service.xoaMemHoaDon(hoaDonDTO.getMaHD());
                    hoaDon_service.CapNhatTongTienHoaDon(hoaDonDTO.getMaHD(), 0);
                } else {
                    double tongTienCu = hoaDonDTO.getTongTien();
                    double tongTienTra = 0;
                    double tongTienCoKM = 0;
                    for (ChiTietHoaDonDTO ct : selectedItems) {
                        LoThuocDTO ctt = ct.getMaLoThuocDTO();
                        ThuocDTO t = thuoc_service.getThuocTheoMa(ctt.getMaThuocDTO().getMaThuoc());
                        if (ct.getTinhTrang().equals("Thuốc Mới Khi Đổi")){
                            tongTienTra += ct.getThanhTien() * (1 + t.getThue());
                        } else {
                            tongTienCoKM += ct.getThanhTien() * (1 + t.getThue());
                        }
                    }
                    KhuyenMaiDTO km = null;

                    if (hoaDonDTO.getMaKM() != null) {
                        km = khuyenMai_service.getKhuyenMaiTheoMa(hoaDonDTO.getMaKM().getMaKM());
                    }

                    if (km != null) {
                        tongTienCoKM = TinhTienKhuyenMai(tongTienCoKM, km.getSo());
                    }

                    double tongMoi = tongTienCu - tongTienTra - tongTienCoKM;
                    hoaDonDTO.setTongTien(tongMoi);
                    hoaDon_service.CapNhatTongTienHoaDon(hoaDonDTO.getMaHD(), tongMoi);

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
        for (ChiTietHoaDonDTO ct : selectedItems){
            LoThuocDTO ctt = ct.getMaLoThuocDTO();
            ThuocDTO t = thuoc_service.getThuocTheoMa(ctt.getMaThuocDTO().getMaThuoc());
            if (ct.getTinhTrang().equals("Thuốc Mới Khi Đổi")){
                tongTienKhiTra += ct.getThanhTien() * (1 + t.getThue());
            } else {
                tongTien += ct.getThanhTien() * (1 + t.getThue());
            }
        }
        DecimalFormat df = new DecimalFormat("#,### đ");
        if (hoaDonDTO.getMaKM() != null) {

            String maKM = hoaDonDTO.getMaKM().getMaKM();
            KhuyenMaiDTO km = khuyenMai_service.getKhuyenMaiTheoMa(maKM);

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
    public HBox renderChiTietHoaDon(ChiTietHoaDonDTO chiTietHoaDonDTO) {
        if (chiTietHoaDonDTO.getTinhTrang() == null) {
            HBox h = new HBox();
            h.setVisible(false);
            h.setManaged(false);
            return h;
        }
        if (chiTietHoaDonDTO.getTinhTrang().equals("Trả") || chiTietHoaDonDTO.getTinhTrang().equals("Trả Khi Đổi")) {
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
                if (!selectedItems.contains(chiTietHoaDonDTO)) {
                    selectedItems.add(chiTietHoaDonDTO);
                }
            } else {
                selectedItems.remove(chiTietHoaDonDTO);
            }
            tinhTongTienTra();
        });
        try {
            Connection con = ConnectDB.getInstance().connect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        LoThuocDTO ctt = loThuoc_service.getChiTietThuoc(chiTietHoaDonDTO.getMaLoThuocDTO().getMaLoThuoc());
        ThuocDTO t = thuoc_service.getThuocTheoMa(ctt.getMaThuocDTO().getMaThuoc());
        Text txtMaThuoc = new Text(t.getTenThuoc());
        txtMaThuoc.setStyle("-fx-font-size: 15px;");
        Text txtSoLuong = new Text("SL " + chiTietHoaDonDTO.getSoLuong());
        txtSoLuong.setStyle("-fx-font-size: 15px;");
        DecimalFormat df = new DecimalFormat("#,### đ");
        Text txtDonGia = new Text(df.format(chiTietHoaDonDTO.getThanhTien()));
        txtDonGia.setStyle("-fx-font-size: 15px;");
        String valueBtn = "Bình thường";
        if (chiTietHoaDonDTO.getTinhTrang().equals("Thuốc Mới Khi Đổi")){
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

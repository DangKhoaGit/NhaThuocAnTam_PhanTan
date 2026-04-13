/*
 * @ (#) XemKhachHangController.java   1.0 28/10/25
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */
package com.antam.app.controller.khachhang;

import com.antam.app.dao.impl.ChiTietHoaDon_DAO;
import com.antam.app.dao.impl.HoaDon_DAO;
import com.antam.app.entity.ChiTietHoaDon;
import com.antam.app.entity.LoThuoc;
import com.antam.app.entity.DangDieuChe;
import com.antam.app.entity.DonViTinh;
import com.antam.app.entity.HoaDon;
import com.antam.app.entity.KhachHang;
import com.antam.app.entity.Thuoc;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/*
 * @description Controller for viewing customer details
 * @author: Tran Tuan Hung
 * @date: 28/10/25
 * @version: 1.0
 */
public class XemChiTietKhachHangController extends DialogPane {

    
    private Text txtMaKhachHang;
    private Text txtTenKhachHang;
    private Text txtSoDienThoai;
    private Text txtTongDonHang;
    private Text txtTongChiTieu;
    private Text txtDonHangGanNhat;

    
    private TableView<HoaDon> tableLichSuMuaHang;
    private TableColumn<HoaDon, String> colMaHoaDon = new TableColumn<>("Mã HĐ");
    private TableColumn<HoaDon, String> colNgayMua = new TableColumn<>("Ngày mua");
    private TableColumn<HoaDon, Integer> colSoThuoc = new TableColumn<>("Số thuốc");
    private TableColumn<HoaDon, Double> colTongTien = new TableColumn<>("Tổng tiền");
    private TableColumn<HoaDon, String> colNhanVien = new TableColumn<>("Nhân viên");

    private Button btnDong;

    private javafx.scene.layout.FlowPane flowPaneThuocDaMua;

    private KhachHang khachHang;
    private HoaDon_DAO hoaDonDAO;
    private ChiTietHoaDon_DAO chiTietHoaDonDAO;
    private DateTimeFormatter formatter;
    private DateTimeFormatter dateFormatter;

    public XemChiTietKhachHangController(){
        this.setPrefSize(800,600);
        FlowPane header = new FlowPane();
        header.setAlignment(javafx.geometry.Pos.CENTER);
        header.setStyle("-fx-background-color: #1e3a8a;");
        Text headerText = new Text("Chi tiết khách hàng");
        headerText.setFill(Color.WHITE);
        headerText.setFont(Font.font("System Bold", 15));
        header.setPadding(new Insets(10, 0, 10, 0));
        header.getChildren().add(headerText);
        this.setHeader(header);

        // Content container
        AnchorPane anchorPane = new AnchorPane();
        VBox root = new VBox(10);
        AnchorPane.setLeftAnchor(root, 0.0);
        AnchorPane.setRightAnchor(root, 0.0);
        root.setPadding(new Insets(10));

        // Info box
        HBox infoBox = new HBox();
        infoBox.setStyle("-fx-background-color: #059669; -fx-background-radius: 5px;");
        infoBox.setPadding(new Insets(10));
        infoBox.setSpacing(10);

        VBox leftInfo = new VBox();
        leftInfo.setPrefWidth(100);
        leftInfo.setFillWidth(true);
        txtMaKhachHang = new Text("Mã KH: KH001");
        txtMaKhachHang.setFill(Color.WHITE);
        txtMaKhachHang.setFont(Font.font("System Bold", 20));

        txtTenKhachHang = new Text("Nguyễn Văn A");
        txtTenKhachHang.setFill(Color.WHITE);
        txtTenKhachHang.setFont(Font.font(16));

        txtSoDienThoai = new Text("SĐT: 0123456789");
        txtSoDienThoai.setFill(Color.WHITE);
        txtSoDienThoai.setFont(Font.font(13));

        leftInfo.getChildren().addAll(txtMaKhachHang, txtTenKhachHang, txtSoDienThoai);

        VBox rightInfo = new VBox();
        rightInfo.setPrefWidth(100);
        txtTongDonHang = new Text("Tổng đơn hàng: 1");
        txtTongDonHang.setFill(Color.WHITE);
        txtTongDonHang.setFont(Font.font("System Bold", 20));

        txtTongChiTieu = new Text("Tổng chi tiêu: 125.000 ₫");
        txtTongChiTieu.setFill(Color.WHITE);
        txtTongChiTieu.setFont(Font.font(13));

        txtDonHangGanNhat = new Text("Đơn hàng gần nhất: 9/12/2024");
        txtDonHangGanNhat.setFill(Color.WHITE);
        txtDonHangGanNhat.setFont(Font.font(13));

        rightInfo.getChildren().addAll(txtTongDonHang, txtTongChiTieu, txtDonHangGanNhat);

        infoBox.getChildren().addAll(leftInfo, rightInfo);

        // Lịch sử mua hàng
        Text lblLichSu = new Text("Lịch sử mua hàng:");
        lblLichSu.setFont(Font.font("System Bold", 14));

        tableLichSuMuaHang = new TableView<>();
        tableLichSuMuaHang.setPrefWidth(200);

        tableLichSuMuaHang.getColumns().addAll(colMaHoaDon, colNgayMua, colSoThuoc, colTongTien, colNhanVien);
        tableLichSuMuaHang.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Thuốc đã mua
        Text lblThuocDaMua = new Text("Thuốc đã mua:");
        lblThuocDaMua.setFont(Font.font("System Bold", 14));

        flowPaneThuocDaMua = new FlowPane();
        flowPaneThuocDaMua.setHgap(10);
        flowPaneThuocDaMua.setVgap(10);
        flowPaneThuocDaMua.setPrefWidth(200);
        flowPaneThuocDaMua.setStyle("-fx-background-color: #f8fafc;");
        flowPaneThuocDaMua.setPadding(new Insets(10));

        // Button Đóng
        HBox btnBox = new HBox();
        btnBox.setSpacing(10);
        btnBox.setAlignment(javafx.geometry.Pos.TOP_RIGHT);
        btnBox.setPadding(new Insets(10,0,0,0));
        btnDong = new Button("Đóng");
        btnDong.setStyle("-fx-padding: 8px 20px; -fx-font-size: 14; -fx-background-color: #1e3a8a; -fx-text-fill: white; -fx-cursor: hand;");
        btnBox.getChildren().add(btnDong);

        // Assemble root
        root.getChildren().addAll(infoBox, lblLichSu, tableLichSuMuaHang, lblThuocDaMua, flowPaneThuocDaMua, btnBox);

        anchorPane.getChildren().add(root);
        this.setContent(anchorPane);
        this.getStylesheets().add(getClass().getResource("/com/antam/app/styles/dashboard_style.css").toExternalForm());
        /** Sự kiện **/
        hoaDonDAO = new HoaDon_DAO();
        chiTietHoaDonDAO = new ChiTietHoaDon_DAO();
        formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        // Thiết lập các cột bảng
        setupTableColumns();
        btnDong.setOnAction(event -> onBtnDongClick());
    }

    /**
     * Thiết lập dữ liệu cho controller (gọi từ TimKhachHangController)
     */
    public void setKhachHang(KhachHang kh) {
        this.khachHang = kh;
        loadKhachHangInfo();
        loadLichSuMuaHang();
        loadThuocDaMua();
    }

    /**
     * Thiết lập các cột của bảng
     */
    private void setupTableColumns() {
        colMaHoaDon.setCellValueFactory(new PropertyValueFactory<>("maHD"));
        colNgayMua.setCellValueFactory(cellData -> {
            HoaDon hd = cellData.getValue();
            String dateStr = hd.getNgayTao() != null
                ? hd.getNgayTao().format(formatter)
                : "N/A";
            return new javafx.beans.property.SimpleStringProperty(dateStr);
        });
        colSoThuoc.setCellValueFactory(cellData -> {
            HoaDon hd = cellData.getValue();
            // Lấy số lượng chi tiết hóa đơn
            ArrayList<ChiTietHoaDon> chiTietList = chiTietHoaDonDAO.getAllChiTietHoaDonTheoMaHD(hd.getMaHD());
            return new javafx.beans.property.SimpleObjectProperty<>(chiTietList.size());
        });
        colTongTien.setCellValueFactory(new PropertyValueFactory<>("tongTien"));
        colTongTien.setCellFactory(column -> new javafx.scene.control.TableCell<HoaDon, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("%,.0f VNĐ", item));
                }
            }
        });
        colNhanVien.setCellValueFactory(cellData -> {
            HoaDon hd = cellData.getValue();
            String tenNV = hd.getMaNV().getHoTen() != null ? hd.getMaNV().getHoTen() : "N/A";
            return new javafx.beans.property.SimpleStringProperty(tenNV);
        });
    }

    /**
     * Tải thông tin khách hàng
     */
    private void loadKhachHangInfo() {
        if (khachHang == null) return;

        txtMaKhachHang.setText("Mã KH: " + khachHang.getMaKH());
        txtTenKhachHang.setText(khachHang.getTenKH());
        txtSoDienThoai.setText("SĐT: " + khachHang.getSoDienThoai());
        txtTongDonHang.setText("Tổng đơn hàng: " + khachHang.getSoDonHang());
        txtTongChiTieu.setText("Tổng chi tiêu: " + String.format("%,.0f VNĐ", khachHang.getTongChiTieu()));

        String ngayGanNhat = khachHang.getNgayMuaGanNhat() != null
            ? khachHang.getNgayMuaGanNhat().format(formatter)
            : "N/A";
        txtDonHangGanNhat.setText("Đơn hàng gần nhất: " + ngayGanNhat);
    }

    /**
     * Tải lịch sử mua hàng của khách hàng
     */
    private void loadLichSuMuaHang() {
        if (khachHang == null) return;

        try {
            // Lấy tất cả hóa đơn từ database
            ArrayList<HoaDon> allHoaDon = hoaDonDAO.getAllHoaDon();

            // Lọc các hóa đơn của khách hàng này
            List<HoaDon> hoaDonCuaKH = allHoaDon.stream()
                .filter(hd -> hd.getMaKH().getMaKH().equals(khachHang.getMaKH()))
                .collect(Collectors.toList());

            // Chuyển đổi sang ObservableList
            ObservableList<HoaDon> dsHoaDon = FXCollections.observableArrayList(hoaDonCuaKH);

            // Gán dữ liệu cho bảng
            tableLichSuMuaHang.setItems(dsHoaDon);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Xử lý sự kiện click nút Đóng
     */
    
    private void onBtnDongClick() {
        // Đóng dialog bằng cách tìm window và đóng nó
        btnDong.getScene().getWindow().hide();
    }

    /**
     * Tải và hiển thị các card thuốc đã mua
     */
    private void loadThuocDaMua() {
        if (khachHang == null || flowPaneThuocDaMua == null) return;

        try {
            // Xóa các card cũ
            flowPaneThuocDaMua.getChildren().clear();

            // Lấy tất cả hóa đơn của khách hàng
            ArrayList<HoaDon> allHoaDon = hoaDonDAO.getAllHoaDon();
            List<HoaDon> hoaDonCuaKH = allHoaDon.stream()
                .filter(hd -> hd.getMaKH().getMaKH().equals(khachHang.getMaKH()))
                .collect(Collectors.toList());

            // Duyệt qua từng hóa đơn và lấy chi tiết thuốc
            for (HoaDon hoaDon : hoaDonCuaKH) {
                ArrayList<ChiTietHoaDon> chiTietList = chiTietHoaDonDAO.getAllChiTietHoaDonTheoMaHD(hoaDon.getMaHD());

                // Tạo card cho mỗi chi tiết thuốc
                for (ChiTietHoaDon chiTiet : chiTietList) {
                    VBox thuocCard = createThuocCard(chiTiet, hoaDon);
                    flowPaneThuocDaMua.getChildren().add(thuocCard);
                }
            }

        } catch (Exception e) {
            System.err.println("Lỗi khi tải thuốc đã mua: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Tạo card hiển thị thông tin thuốc đã mua
     */
    private VBox createThuocCard(ChiTietHoaDon chiTiet, HoaDon hoaDon) {
        VBox vbox = new VBox(5);
        vbox.setPrefWidth(280);
        vbox.setStyle("-fx-background-color: white; -fx-border-radius: 6px; -fx-border-color: #10b981; -fx-border-width: 0px 0px 0px 4px; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 5, 0, 0, 2);");
        vbox.setPadding(new Insets(12));

        // Khởi tạo thông tin mặc định
        String tenThuoc = "Thuốc không xác định";
        String dangDieuChe = "Không xác định";
        String donViTinh = "Không xác định";
        String hamLuong = "";

        try {
            // Flow: ChiTietHoaDon -> ChiTietThuoc -> Thuoc (để lấy tên thuốc và dạng điều chế)
            LoThuoc chiTietThuoc = chiTiet.getMaCTT();
            System.out.println(chiTietThuoc);
            if (chiTietThuoc != null) {
                Thuoc thuoc = chiTietThuoc.getMaThuoc();
                if (thuoc != null) {
                    // Lấy tên thuốc
                    String ten = thuoc.getTenThuoc();
                    if (ten != null && !ten.trim().isEmpty()) {
                        tenThuoc = ten;
                    }

                    // Lấy hàm lượng
                    String hl = thuoc.getHamLuong();
                    if (hl != null && !hl.trim().isEmpty()) {
                        hamLuong = " (" + hl + ")";
                    }

                    // Lấy dạng điều chế
                    if (thuoc.getDangDieuChe() != null) {
                        String ddc = thuoc.getDangDieuChe().getTenDDC();
                        if (ddc != null && !ddc.trim().isEmpty()) {
                            dangDieuChe = ddc;
                        }
                    }
                }
            }

            // Flow: ChiTietHoaDon -> DonViTinh (để lấy tên đơn vị tính)
            DonViTinh dvt = chiTiet.getMaDVT();
            if (dvt != null) {
                String tenDVT = dvt.getTenDVT();
                if (tenDVT != null && !tenDVT.trim().isEmpty()) {
                    donViTinh = tenDVT;
                }
            }

        } catch (Exception e) {
            System.err.println("Lỗi khi lấy thông tin thuốc: " + e.getMessage());
            e.printStackTrace();
        }

        // Tạo các Text elements với thông tin đầy đủ
        Text txtTenThuoc = new Text(tenThuoc + hamLuong);
        txtTenThuoc.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-fill: black;");

        Text txtLoaiThuoc = new Text("Dạng: " + dangDieuChe);
        txtLoaiThuoc.setStyle("-fx-fill: #6b6b6b; -fx-font-size: 12px;");

        Text txtThongTinMua = new Text("SL: " + chiTiet.getSoLuong() + " " + donViTinh + " | Thành tiền: " + String.format("%,.0f", chiTiet.getThanhTien()) + " ₫");
        txtThongTinMua.setStyle("-fx-fill: #059669; -fx-font-weight: bold; -fx-font-size: 12px;");

        String ngayMua = hoaDon.getNgayTao() != null ? hoaDon.getNgayTao().format(dateFormatter) : "Không xác định";
        Text txtThongTinHoaDon = new Text("HĐ: " + hoaDon.getMaHD() + " | Ngày: " + ngayMua);
        txtThongTinHoaDon.setStyle("-fx-fill: #6b7280; -fx-font-size: 11px;");

        vbox.getChildren().addAll(txtTenThuoc, txtLoaiThuoc, txtThongTinMua, txtThongTinHoaDon);
        return vbox;
    }
}

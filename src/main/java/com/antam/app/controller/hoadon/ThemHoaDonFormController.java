//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.antam.app.controller.hoadon;

import com.antam.app.dao.*;
import com.antam.app.dao.impl.*;
import com.antam.app.entity.*;
import com.antam.app.helper.XuatHoaDonPDF;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.File;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.antam.app.controller.phieudat.CapNhatPhieuDatController.selectedPDT;

public class ThemHoaDonFormController extends DialogPane {
    private TextField txtMaHoaDon;
    private TextField txtSoDienThoai;
    private TextField txtTenKhachHang;
    private ComboBox<Thuoc> cbMedicine;
    private ComboBox<DonViTinh> cb_unit;
    private TextField txt_price;
    private TextField txtQuantity;
    private Text txtTamTinh;
    private Text txtThue;
    private Text txtTongTien;
    private VBox medicineRowsVBox;
    private Button btnThemThuoc;
    private ComboBox<KhuyenMai> cb_promotion;
    private Text txtThongTinKhuyenMai;
    private Text txtWarning;

    // Thêm biến lưu tổng tiền thực tế để tránh phải parse từ text
    private double tongTienThucTe = 0;
    private double tamTinhThucTe = 0;
    private double thueThucTe = 0;

    // Định dạng tiền tệ kiểu Việt Nam: 1.000đ, 10.000đ
    private static final DecimalFormat VND_FORMAT;

    static {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.');
        symbols.setDecimalSeparator(',');
        VND_FORMAT = new DecimalFormat("#,##0", symbols);
    }

    public ThemHoaDonFormController() {
        FlowPane header = new FlowPane();
        header.setAlignment(Pos.CENTER);
        header.setStyle("-fx-background-color: #1e3a8a;");

        Text title = new Text("Tạo hóa đơn mới");
        title.setFill(Color.WHITE);
        title.setFont(Font.font("System", FontWeight.BOLD, 15));

        FlowPane.setMargin(title, new Insets(10, 0, 10, 0));
        header.getChildren().add(title);

        this.setHeader(header);

        /* ---------------- CONTENT ROOT ---------------- */
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        VBox root = new VBox(10);
        root.setPadding(new Insets(10));

        /* ---------------- GRID 1: Mã + Tên KH + SĐT ---------------- */
        GridPane grid1 = new GridPane();
        grid1.setHgap(5);
        grid1.setVgap(5);

        ColumnConstraints colA = new ColumnConstraints();
        colA.setHgrow(Priority.SOMETIMES);
        ColumnConstraints colB = new ColumnConstraints();
        colB.setHgrow(Priority.SOMETIMES);
        grid1.getColumnConstraints().addAll(colA, colB);

        Text lblMa = new Text("Mã hóa đơn:");
        lblMa.setFill(Color.web("#374151"));

        txtMaHoaDon = new TextField();
        txtMaHoaDon.setPrefHeight(40);

        Text lblSoDienThoai = new Text("Số điện thoại:");
        lblSoDienThoai.setFill(Color.web("#374151"));
        GridPane.setColumnIndex(lblSoDienThoai, 1);

        txtSoDienThoai = new TextField();
        txtSoDienThoai.setPrefHeight(40);
        txtSoDienThoai.setPromptText("Nhập số điện thoại khách hàng");
        GridPane.setColumnIndex(txtSoDienThoai, 1);
        GridPane.setRowIndex(txtSoDienThoai, 1);

        Text lblTen = new Text("Tên khách hàng:");
        lblTen.setFill(Color.web("#374151"));
        GridPane.setRowIndex(lblTen, 2);

        txtTenKhachHang = new TextField();
        txtTenKhachHang.setPrefHeight(40);
        txtTenKhachHang.setPromptText("Tên sẽ tự động điền theo SĐT");
        GridPane.setColumnSpan(txtTenKhachHang, 2);
        GridPane.setRowIndex(txtTenKhachHang, 3);

        GridPane.setRowIndex(txtMaHoaDon, 1);

        grid1.getChildren().addAll(lblMa, txtMaHoaDon, lblSoDienThoai, txtSoDienThoai, lblTen, txtTenKhachHang);

        /* ---------------- HEADER ROW FOR MEDICINES ---------------- */
        HBox headerRow = new HBox(10);
        headerRow.setStyle("-fx-background-color: #f1f5f9; -fx-border-color: #e5e7eb; -fx-border-radius: 6 6 0 0; -fx-border-width: 2 2 0 2;");
        headerRow.setPadding(new Insets(5, 10, 5, 10));

        headerRow.getChildren().addAll(
                createHeaderText("Thuốc:", 120),
                createHeaderText("Đơn vị:", 120),
                createHeaderText("Số lượng:", 120),
                createHeaderText("Đơn giá:", 100),
                createHeaderText("Thao tác:", 0)
        );

        /* ---------------- MEDICINE ROWS CONTAINER ---------------- */
        medicineRowsVBox = new VBox();
        medicineRowsVBox.setStyle("-fx-border-color: #e5e7eb; -fx-border-radius: 0 0 6 6; -fx-border-width: 0 2 2 2;");
        medicineRowsVBox.setPadding(new Insets(10));

        medicineRowsVBox.getChildren().add(buildMedicineRow());

        btnThemThuoc = new Button("Thêm thuốc");
        btnThemThuoc.getStyleClass().add("btn-gray");

        /* ---------------- KHUYEN MAI ---------------- */
        ColumnConstraints colC = new ColumnConstraints();
        colC.setHgrow(Priority.SOMETIMES);
        GridPane grid2 = new GridPane();
        grid2.getColumnConstraints().addAll(colC);
        grid2.setHgap(5);

        Text lblKM = new Text("Áp dụng khuyến mãi:");
        lblKM.setFill(Color.web("#374151"));

        cb_promotion = new ComboBox<>();
        cb_promotion.setPrefHeight(40);
        GridPane.setRowIndex(cb_promotion, 1);

        grid2.getChildren().addAll(lblKM, cb_promotion);

        /* ---------------- WARNINGS ---------------- */
        txtWarning = new Text("");
        txtWarning.setFill(Color.RED);
        txtWarning.setVisible(false);
        txtWarning.setStyle("-fx-font-size: 12px;");

        /* ---------------- TỔNG TIỀN ---------------- */
        GridPane grid3 = new GridPane();
        grid3.setHgap(5);
        grid3.setStyle("-fx-background-color: #f8fafc; -fx-background-radius: 8;");
        grid3.setPadding(new Insets(10));

        Text lblTamTinh = new Text("Tạm tính:");
        Text lblThue = new Text("Thuế:");
        Text lblTong = new Text("Tổng tiền:");
        lblTong.setFont(Font.font("System", FontWeight.BOLD, 18));

        txtTamTinh = new Text();
        txtThue = new Text();

        txtTongTien = new Text();
        txtTongTien.setFont(Font.font("System", FontWeight.BOLD, 18));

        txtThongTinKhuyenMai = new Text();
        txtThongTinKhuyenMai.setFill(Color.web("#1e40af"));

        grid3.add(lblTamTinh, 0, 0);
        grid3.add(txtTamTinh, 1, 0);
        grid3.add(lblThue, 0, 1);
        grid3.add(txtThue, 1, 1);
        grid3.add(lblTong, 0, 2);
        grid3.add(txtTongTien, 1, 2);
        grid3.add(txtThongTinKhuyenMai, 1, 3);

        /* ---------------- ADD ALL TO ROOT ---------------- */
        root.getChildren().addAll(
                grid1,
                headerRow,
                medicineRowsVBox,
                btnThemThuoc,
                grid2,
                txtWarning,
                grid3
        );

        scrollPane.setContent(root);
        String css = getClass().getResource("/com/antam/app/styles/dashboard_style.css").toExternalForm();
        this.getStylesheets().addAll(css);
        this.setContent(scrollPane);
        /** Sự kiện **/
        ButtonType cancelButton = new ButtonType("Huỷ", ButtonData.CANCEL_CLOSE);
        ButtonType applyButton = new ButtonType("Tạo hoá đơn", ButtonData.APPLY);
        this.getButtonTypes().add(cancelButton);
        this.getButtonTypes().add(applyButton);

        // Tự động sinh mã hoá đơn mới
        txtMaHoaDon.setText(generateNewMaHoaDon());
        txtMaHoaDon.setEditable(false); // Khóa không cho sửa mã hoá đơn

        // Auto-fill tên khách hàng khi nhập số điện thoại
        txtSoDienThoai.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && !newVal.trim().isEmpty()) {
                // Chỉ tìm khi đủ 10 số
                if (newVal.trim().matches("\\d{10}")) {
                    KhachHang_DAO khachHangDAO = new KhachHang_DAO();
                    KhachHang kh = khachHangDAO.getKhachHangTheoSoDienThoai(newVal.trim());
                    if (kh != null) {
                        // Tìm thấy khách hàng, tự động điền tên
                        txtTenKhachHang.setText(kh.getTenKH());
                        txtTenKhachHang.setEditable(false); // Khóa không cho sửa tên
                        txtTenKhachHang.setStyle("-fx-background-color: #e0f2fe;");
                    } else {
                        // Không tìm thấy, cho phép nhập tên mới
                        txtTenKhachHang.clear();
                        txtTenKhachHang.setEditable(true);
                        txtTenKhachHang.setStyle("");
                        txtTenKhachHang.setPromptText("Nhập tên khách hàng mới");
                    }
                } else {
                    // Chưa đủ 10 số, reset
                    txtTenKhachHang.clear();
                    txtTenKhachHang.setEditable(true);
                    txtTenKhachHang.setStyle("");
                    txtTenKhachHang.setPromptText("Tên sẽ tự động điền theo SĐT");
                }
            } else {
                // Số điện thoại rỗng, reset
                txtTenKhachHang.clear();
                txtTenKhachHang.setEditable(true);
                txtTenKhachHang.setStyle("");
                txtTenKhachHang.setPromptText("Tên sẽ tự động điền theo SĐT");
            }
        });

        // Load thuốc vào cbMedicine
        Thuoc_DAO thuocDAO = new Thuoc_DAO();
        var thuocList = FXCollections.observableArrayList(thuocDAO.getAllThuoc());
        cbMedicine.setItems(thuocList);
        cbMedicine.setConverter(new javafx.util.StringConverter<>() {
            @Override
            public String toString(Thuoc thuoc) {
                return thuoc == null ? "" : thuoc.getTenThuoc();
            }

            @Override
            public Thuoc fromString(String s) {
                return thuocList.stream().filter(t -> t.getTenThuoc().equals(s)).findFirst().orElse(null);
            }
        });

//        cbMedicine.valueProperty().addListener((obs, oldVal, newVal) -> {
//            if (newVal != null) {
//                // Lấy danh sách đơn vị tính quy đổi cho thuốc
//                QuyDoi_DAO quyDoiDAO = new QuyDoi_DAO();
//                DonViTinh_DAO donViTinhDAO = new DonViTinh_DAO();
//                var allQuyDoi = quyDoiDAO.getAllQuyDoi();
//                var quyDoiList = allQuyDoi.getOrDefault(newVal.getMaThuoc(), List.of());
//                HashSet<Integer> dvtIdSet = new HashSet<>();
//                // Thêm đơn vị cơ sở
//                dvtIdSet.add(newVal.getMaDVTCoSo().getMaDVT());
//                // Thêm các đơn vị quy đổi
//                for (QuyDoi qd : quyDoiList) {
//                    dvtIdSet.add(qd.getMaDVTCha().getMaDVT());
//                    dvtIdSet.add(qd.getMaDVTCon().getMaDVT());
//                }
//                HashSet<DonViTinh> dvtSet = new HashSet<>();
//                for (Integer maDVT : dvtIdSet) {
//                    DonViTinh dvt = donViTinhDAO.getDVTTheoMa(maDVT);
//                    if (dvt != null) dvtSet.add(dvt);
//                }
//                cb_unit.setItems(FXCollections.observableArrayList(dvtSet));
//                cb_unit.getSelectionModel().selectFirst();
//                // Hiển thị giá bán
//                txt_price.setText(VND_FORMAT.format(newVal.getGiaBan()) + "đ");
//                updateSummary();
//            } else {
//                cb_unit.getItems().clear();
//                txt_price.clear();
//                txtTamTinh.setText("");
//                txtThue.setText("");
//                txtTongTien.setText("");
//            }
//        });
        txtQuantity.textProperty().addListener((obs, oldVal, newVal) -> updateSummary());
        txt_price.textProperty().addListener((obs, oldVal, newVal) -> updateSummary());
        cb_unit.valueProperty().addListener((obs, oldVal, newVal) -> updateSummary());
        btnThemThuoc.setOnAction(e -> addMedicineRow());
        if (!medicineRowsVBox.getChildren().isEmpty() && medicineRowsVBox.getChildren().get(0) instanceof HBox hbox) {
            setupMedicineRow(hbox);
        }

        // Load khuyến mãi vào cb_promotion
        KhuyenMai_DAO khuyenMaiDAO = new KhuyenMai_DAO();
        var dsKhuyenMai = I_KhuyenMai_DAO.getAllKhuyenMaiConHieuLuc();
        cb_promotion.setItems(FXCollections.observableArrayList(dsKhuyenMai));
        cb_promotion.setConverter(new javafx.util.StringConverter<>() {
            @Override
            public String toString(KhuyenMai km) {
                return km == null ? "" : km.getTenKM();
            }

            @Override
            public KhuyenMai fromString(String s) {
                return dsKhuyenMai.stream().filter(km -> km.getTenKM().equals(s)).findFirst().orElse(null);
            }
        });

        // Hiển thị thông tin khuyến mãi khi chọn
        cb_promotion.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                txtThongTinKhuyenMai.setText(
                        String.format("%s (Từ %s đến %s)\nLoại: %s, Giá trị: %s%s",
                                newVal.getTenKM(),
                                newVal.getNgayBatDau(),
                                newVal.getNgayKetThuc(),
                                newVal.getLoaiKhuyenMai().getTenLKM(),
                                newVal.getSo(),
                                newVal.getLoaiKhuyenMai().getTenLKM().toLowerCase().contains("%") ? "%" : ""
                        )
                );
            } else {
                txtThongTinKhuyenMai.setText("");
            }
            updateSummary();
        });

        // Xử lý khi nhấn nút tạo hoá đơn
        Button btnTaoHoaDon = (Button) this.lookupButton(applyButton);
        // Validate dữ liệu trước khi tạo hoá đơn
        btnTaoHoaDon.addEventFilter(javafx.event.ActionEvent.ACTION, e -> {
            // Reset warning
            txtWarning.setVisible(false);
            txtWarning.setText("");

            // Validate số điện thoại
            String soDienThoai = txtSoDienThoai.getText().trim();
            if (soDienThoai.isEmpty()) {
                txtWarning.setText("Vui lòng nhập số điện thoại khách hàng!");
                txtWarning.setVisible(true);
                e.consume();
                return;
            }
            if (!soDienThoai.matches("\\d{10}")) {
                txtWarning.setText("Số điện thoại phải là 10 chữ số!");
                txtWarning.setVisible(true);
                e.consume();
                return;
            }

            // Validate tên khách hàng
            String tenKH = txtTenKhachHang.getText().trim();
            if (tenKH.isEmpty()) {
                txtWarning.setText("Vui lòng nhập tên khách hàng!");
                txtWarning.setVisible(true);
                e.consume();
                return;
            }

            boolean hasMedicine = false;
            for (var node : medicineRowsVBox.getChildren()) {
                if (node instanceof HBox hbox) {
                    ComboBox<Thuoc> cb = (ComboBox<Thuoc>) hbox.getChildren().get(0);
                    if (cb.getValue() != null) {
                        hasMedicine = true;
                        break;
                    }
                }
            }
            if (!hasMedicine) {
                txtWarning.setText("Vui lòng chọn ít nhất một thuốc!");
                txtWarning.setVisible(true);
                e.consume();
                return;
            }

            // Kiểm tra từng dòng: nếu đã chọn thuốc thì phải nhập số lượng hợp lệ
            for (var node : medicineRowsVBox.getChildren()) {
                if (node instanceof HBox hbox) {
                    ComboBox<Thuoc> cb = (ComboBox<Thuoc>) hbox.getChildren().get(0);
                    TextField txtQuantity = (TextField) hbox.getChildren().get(2);
                    Thuoc thuoc = cb.getValue();
                    String qtyStr = txtQuantity.getText().trim();

                    // Nếu đã chọn thuốc nhưng không nhập số lượng
                    if (thuoc != null && qtyStr.isEmpty()) {
                        txtWarning.setText("Vui lòng nhập số lượng cho thuốc: " + thuoc.getTenThuoc());
                        txtWarning.setVisible(true);
                        e.consume();
                        return;
                    }

                    // Nếu đã chọn thuốc và nhập số lượng, kiểm tra tính hợp lệ
                    if (thuoc != null && !qtyStr.isEmpty()) {
                        try {
                            int qty = Integer.parseInt(qtyStr);
                            if (qty <= 0) {
                                txtWarning.setText("Số lượng của " + thuoc.getTenThuoc() + " phải là số nguyên dương!");
                                txtWarning.setVisible(true);
                                e.consume();
                                return;
                            }
                        } catch (NumberFormatException ex) {
                            txtWarning.setText("Số lượng của " + thuoc.getTenThuoc() + " phải là số nguyên hợp lệ!");
                            txtWarning.setVisible(true);
                            e.consume();
                            return;
                        }
                    }
                }
            }

            // Kiểm tra tồn kho cho từng thuốc
            LoThuoc_DAO chiTietThuocDAO = new LoThuoc_DAO();
            boolean enoughStock = true;
            String outOfStockMedicine = null;
            for (var node : medicineRowsVBox.getChildren()) {
                if (node instanceof HBox hbox) {
                    ComboBox<Thuoc> cb = (ComboBox<Thuoc>) hbox.getChildren().get(0);
                    TextField txtQuantity = (TextField) hbox.getChildren().get(2);
                    Thuoc thuoc = cb.getValue();
                    int soLuong = 0;
                    try {
                        soLuong = Integer.parseInt(txtQuantity.getText().trim());
                    } catch (Exception ex) {
                    }
                    if (thuoc != null && soLuong > 0) {
                        int tongTonKho = chiTietThuocDAO.getTongTonKhoTheoMaThuoc(thuoc.getMaThuoc());
                        if (tongTonKho < soLuong) {
                            enoughStock = false;
                            outOfStockMedicine = thuoc.getTenThuoc();
                            break;
                        }
                    }
                }
            }
            if (!enoughStock) {
                txtWarning.setText("Số lượng tồn không đủ cho thuốc: " + outOfStockMedicine);
                txtWarning.setVisible(true);
                e.consume();
                return;
            }
            // Tiến hành tạo hoá đơn
            txtWarning.setVisible(false);
            txtWarning.setText("");

            // 1. Lấy mã khách hàng (tự động thêm nếu chưa có)
            String maKH = getOrCreateMaKhachHang();
            KhachHang_DAO khachHangDAO = new KhachHang_DAO();
            KhachHang kh = khachHangDAO.getKhachHangTheoMa(maKH);

            // 2. Lấy nhân viên (nếu có ComboBox chọn nhân viên, ví dụ cbEmployee)
            NhanVien_DAO nhanVienDAO = new NhanVien_DAO();
            NhanVien nhanVien = PhienNguoiDung.getMaNV(); // Hardcode mã nhân viên
            if (nhanVien == null || nhanVien.getMaNV() == null) {
                txtWarning.setText("Không tìm thấy nhân viên hợp lệ! Không thể tạo hóa đơn.");
                txtWarning.setVisible(true);
                e.consume();
                return;
            }

            // 3. Lấy khuyến mãi (đã có đối tượng km)
            KhuyenMai km = cb_promotion.getValue();
            // 4. Lấy mã hoá đơn
            String maHD = txtMaHoaDon.getText().trim();
            // 5. Sử dụng biến tongTienThucTe đã tính toán thay vì parse từ text
            double tongTien = tongTienThucTe;

            // 6. Tạo hoá đơn đúng kiểu constructor
            HoaDon_DAO hoaDonDAO = new HoaDon_DAO();
            HoaDon hoaDon = new HoaDon(maHD, java.time.LocalDate.now(), nhanVien, kh, km, tongTien, false);
            boolean hoaDonInserted = hoaDonDAO.insertHoaDon(hoaDon);
            if (!hoaDonInserted) {
                txtWarning.setText("Tạo hóa đơn thất bại! Vui lòng kiểm tra lại thông tin.");
                txtWarning.setVisible(true);
                e.consume();
                return;
            }
            // 5. Thêm chi tiết hoá đơn cho từng thuốc và trừ kho
            ChiTietHoaDon_DAO chiTietHoaDonDAO = new ChiTietHoaDon_DAO();
            ArrayList<ChiTietHoaDon> listCTHD = new ArrayList<>(); // Danh sách chi tiết hóa đơn để truyền vào hàm xuất

            // Gộp các dòng cùng loại thuốc lại trước khi xử lý
            java.util.Map<String, Integer> thuocSoLuongMap = new java.util.HashMap<>();
            java.util.Map<String, Double> thuocDonGiaMap = new java.util.HashMap<>();
            java.util.Map<String, DonViTinh> thuocDVTMap = new java.util.HashMap<>();
            java.util.Map<String, Thuoc> thuocObjMap = new java.util.HashMap<>(); // Thêm map để lưu đối tượng Thuoc

            for (var node : medicineRowsVBox.getChildren()) {
                if (node instanceof HBox hbox) {
                    ComboBox<Thuoc> cb = (ComboBox<Thuoc>) hbox.getChildren().get(0);
                    ComboBox<DonViTinh> cbUnit = (ComboBox<DonViTinh>) hbox.getChildren().get(1);
                    TextField txtQuantity = (TextField) hbox.getChildren().get(2);
                    TextField txtPrice = (TextField) hbox.getChildren().get(3);
                    Thuoc thuoc = cb.getValue();
                    DonViTinh dvt = cbUnit.getValue();
                    int soLuong = 0;
                    double donGia = 0;
                    try {
                        soLuong = Integer.parseInt(txtQuantity.getText().trim());
                    } catch (Exception ex) {
                    }
                    try {
                        donGia = parseCurrency(txtPrice.getText().trim());
                    } catch (Exception ex) {
                    }

                    if (thuoc != null && dvt != null && soLuong > 0) {
                        String maThuoc = thuoc.getMaThuoc();
                        // Gộp số lượng nếu trùng thuốc
                        thuocSoLuongMap.put(maThuoc, thuocSoLuongMap.getOrDefault(maThuoc, 0) + soLuong);
                        thuocDonGiaMap.put(maThuoc, donGia);
                        thuocDVTMap.put(maThuoc, dvt);
                        thuocObjMap.put(maThuoc, thuoc); // Lưu đối tượng Thuoc đầy đủ
                    }
                }
            }

            // Xử lý từng loại thuốc đã gộp
            for (String maThuoc : thuocSoLuongMap.keySet()) {
                int tongSoLuong = thuocSoLuongMap.get(maThuoc);
                double donGia = thuocDonGiaMap.get(maThuoc);
                DonViTinh dvt = thuocDVTMap.get(maThuoc);
                Thuoc thuocDayDu = thuocObjMap.get(maThuoc); // Lấy đối tượng Thuoc đầy đủ

                // Lấy danh sách các lô ChiTietThuoc còn tồn kho cho thuốc này
                List<LoThuoc> listCTT = chiTietThuocDAO.getAllChiTietThuoc().stream()
                        .filter(ctt -> ctt.getMaThuoc().getMaThuoc().equals(maThuoc) && ctt.getSoLuong() > 0)
                        .sorted(java.util.Comparator.comparing(LoThuoc::getHanSuDung)) // Ưu tiên lô hết hạn trước
                        .collect(Collectors.toList());

                int soLuongConLai = tongSoLuong;
                for (LoThuoc ctt : listCTT) {
                    if (soLuongConLai <= 0) break;
                    int soLuongXuat = Math.min(ctt.getSoLuong(), soLuongConLai);
                    if (soLuongXuat <= 0) continue;

                    // Đảm bảo ChiTietThuoc có đầy đủ thông tin Thuoc
                    ctt.setMaThuoc(thuocDayDu);

                    ChiTietHoaDon cthd = new ChiTietHoaDon(new HoaDon(maHD), ctt, soLuongXuat, dvt, "Bán", soLuongXuat * donGia);
                    chiTietHoaDonDAO.themChiTietHoaDon(cthd);
                    listCTHD.add(cthd); // Thêm vào danh sách để xuất PDF
                    // Trừ kho lô này
                    chiTietThuocDAO.CapNhatSoLuongChiTietThuoc(ctt.getMaCTT(), -soLuongXuat);
                    soLuongConLai -= soLuongXuat;
                }
            }

            // Đóng dialog và hiển thị thông báo + xuất hóa đơn với đầy đủ tham số
            this.getScene().getWindow().hide();
            thongBaoVaXuatHoaDon(hoaDon, listCTHD, tongTienThucTe, thueThucTe);
        });

    }

    private Text createHeaderText(String text, double rightMargin) {
        Text t = new Text(text);
        t.setFill(Color.web("#374151"));
        t.setFont(Font.font("System", FontWeight.BOLD, 12));
        HBox.setMargin(t, new Insets(0, rightMargin, 0, 0));
        return t;
    }

    private HBox buildMedicineRow() {
        HBox row = new HBox(10);
        row.setPadding(new Insets(10));
        row.setStyle("-fx-background-color: #f8fafc;");

        cbMedicine = new ComboBox<>();
        cbMedicine.setPrefWidth(150);

        cb_unit = new ComboBox<>();
        cb_unit.setPrefWidth(150);

        txtQuantity = new TextField();
        txtQuantity.setPromptText("Số lượng");

        txt_price = new TextField();
        txt_price.setPromptText("Đơn giá");

        Button btnRemove = new Button();
        btnRemove.setStyle("-fx-background-color: #ef4444; -fx-background-radius: 50%;");
        btnRemove.setGraphic(new Text("X"));

        row.getChildren().addAll(cbMedicine, cb_unit, txtQuantity, txt_price, btnRemove);
        return row;
    }


    // Thêm một hàng thuốc mới vào VBox chứa các hàng thuốc
    private void addMedicineRow() {
        HBox hbox = new HBox(10);
        hbox.setStyle("-fx-background-color: #f8fafc;");
        hbox.setPadding(new javafx.geometry.Insets(10));
        ComboBox<Thuoc> cbMedicine = new ComboBox<>();
        cbMedicine.setPrefWidth(150);
        cbMedicine.setPromptText("Chọn thuốc");
        cbMedicine.getStylesheets().add(getClass().getResource("/com/antam/app/styles/dashboard_style.css").toExternalForm());
        ComboBox<DonViTinh> cb_unit = new ComboBox<>();
        cb_unit.setPrefWidth(150);
        cb_unit.setPromptText("Đơn vị");
        cb_unit.getStylesheets().add(getClass().getResource("/com/antam/app/styles/dashboard_style.css").toExternalForm());
        TextField txtQuantity = new TextField();
        txtQuantity.setPromptText("Số lượng");
        txtQuantity.getStyleClass().add("text-field");
        txtQuantity.getStylesheets().add(getClass().getResource("/com/antam/app/styles/dashboard_style.css").toExternalForm());
        TextField txt_price = new TextField();
        txt_price.setPromptText("Đơn giá");
        txt_price.getStyleClass().add("text-field");
        txt_price.getStylesheets().add(getClass().getResource("/com/antam/app/styles/dashboard_style.css").toExternalForm());
        Button btnRemove = new Button("X");
        btnRemove.setStyle(
                "-fx-padding: 5 8 5 8;" +
                        "-fx-background-color: #ef4444;" +
                        "-fx-background-radius: 50%;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;"
        );
        btnRemove.setOnAction(e -> medicineRowsVBox.getChildren().remove(hbox));
        hbox.getChildren().addAll(cbMedicine, cb_unit, txtQuantity, txt_price, btnRemove);
        medicineRowsVBox.getChildren().add(hbox);
        setupMedicineRow(hbox);
    }

    // Thiết lập sự kiện và logic cho một hàng thuốc mới thêm vào
    private void setupMedicineRow(HBox hbox) {
        ComboBox<Thuoc> cbMedicine = (ComboBox<Thuoc>) hbox.getChildren().get(0);
        ComboBox<DonViTinh> cb_unit = (ComboBox<DonViTinh>) hbox.getChildren().get(1);
        TextField txtQuantity = (TextField) hbox.getChildren().get(2);
        TextField txt_price = (TextField) hbox.getChildren().get(3);
        txt_price.setEditable(false); // Khóa không cho sửa giá
        Thuoc_DAO thuocDAO = new Thuoc_DAO();
        var thuocList = FXCollections.observableArrayList(thuocDAO.getAllThuoc());
        cbMedicine.setItems(thuocList);
        cbMedicine.setConverter(new javafx.util.StringConverter<>() {
            @Override
            public String toString(Thuoc thuoc) {
                return thuoc == null ? "" : thuoc.getTenThuoc();
            }

            @Override
            public Thuoc fromString(String s) {
                return thuocList.stream().filter(t -> t.getTenThuoc().equals(s)).findFirst().orElse(null);
            }
        });

        // Khi chọn thuốc, tự động set đơn vị cơ sở và không cho chọn đơn vị khác
        cbMedicine.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                DonViTinh_DAO donViTinhDAO = new DonViTinh_DAO();
                // Chỉ lấy đơn vị cơ sở của thuốc
                DonViTinh dvtCoSo = newVal.getMaDVTCoSo();
                if (dvtCoSo != null) {
                    // Lấy thông tin đầy đủ của đơn vị tính từ database
                    DonViTinh dvtFull = donViTinhDAO.getDVTTheoMa(dvtCoSo.getMaDVT());
                    if (dvtFull != null) {
                        cb_unit.setItems(FXCollections.observableArrayList(dvtFull));
                        cb_unit.getSelectionModel().selectFirst();
                        cb_unit.setDisable(true); // Không cho chọn đơn vị khác
                    }
                }
                // Hiển thị giá bán
                txt_price.setText(VND_FORMAT.format(newVal.getGiaBan()) + "đ");
            } else {
                cb_unit.getItems().clear();
                cb_unit.setDisable(false);
                txt_price.clear();
            }
            updateSummary();
        });

        txtQuantity.textProperty().addListener((obs, oldVal, newVal) -> updateSummary());
        txt_price.textProperty().addListener((obs, oldVal, newVal) -> updateSummary());
        cb_unit.valueProperty().addListener((obs, oldVal, newVal) -> updateSummary());
    }

    //Hàm tính tổng tiền, tạm tính Cập nhật lại phần tóm tắt hoá đơn (tạm tính, thuế, tổng tiền)
    private void updateSummary() {
        double tongTamTinh = 0;
        double tongThue = 0;
        double tongTien = 0;
        for (var node : medicineRowsVBox.getChildren()) {
            if (node instanceof HBox hbox) {
                ComboBox<Thuoc> cbMedicine = (ComboBox<Thuoc>) hbox.getChildren().get(0);
                TextField txtQuantity = (TextField) hbox.getChildren().get(2);
                TextField txt_price = (TextField) hbox.getChildren().get(3);
                txt_price.setEditable(false); // Khóa không cho sửa giá
                int quantity = 0;
                try {
                    quantity = Integer.parseInt(txtQuantity.getText().trim());
                } catch (Exception ignored) {
                }
                double price = 0;
                try {
                    price = parseCurrency(txt_price.getText().trim());
                } catch (Exception ignored) {
                }
                double taxPercent = 0;
                Thuoc selectedThuoc = cbMedicine.getValue();
                if (selectedThuoc != null) {
                    taxPercent = selectedThuoc.getThue();
                }
                double tamTinh = quantity * price;
                // Tính thuế VAT: giá chưa bao gồm thuế, thuế = thành tiền × tỷ lệ thuế
                double thue = tamTinh * taxPercent;
                tongTamTinh += tamTinh;
                tongThue += thue;
            }
        }
        tongTien = tongTamTinh + tongThue;
        // Áp dụng khuyến mãi nếu có
        KhuyenMai km = cb_promotion.getValue();
        double giam = 0;
        if (km != null) {
            double so = km.getSo();
            if (so < 100) {
                giam = tongTien * so / 100.0;
            } else if (so >= 1000) {
                giam = so;
            }
            if (giam > tongTien) giam = tongTien;
            tongTien -= giam;
        }

        // Lưu giá trị thực tế vào biến để dùng khi lưu DB
        tamTinhThucTe = tongTamTinh;
        thueThucTe = tongThue;
        tongTienThucTe = tongTien;

        txtTamTinh.setText(VND_FORMAT.format(tongTamTinh) + "đ");
        txtThue.setText(VND_FORMAT.format(tongThue) + "đ");
        if (km != null && giam > 0) {
            txtTongTien.setText(VND_FORMAT.format(tongTien) + "đ (đã giảm " + VND_FORMAT.format(giam) + "đ)");
        } else {
            txtTongTien.setText(VND_FORMAT.format(tongTien) + "đ");
        }
    }

    /**
     * Sinh mã hoá đơn mới chưa tồn tại trong CSDL dạng HDxxx
     */
    private String generateNewMaHoaDon() {
        HoaDon_DAO hoaDonDAO = new HoaDon_DAO();
        List<String> allMaHD = hoaDonDAO.getAllHoaDon().stream().map(hd -> hd.getMaHD()).collect(Collectors.toList());
        int max = 0;
        for (String ma : allMaHD) {
            if (ma != null && ma.matches("HD\\d+")) {
                int num = Integer.parseInt(ma.substring(2));
                if (num > max) max = num;
            }
        }
        return String.format("HD%03d", max + 1);
    }

    // Xử lý khi nhấn nút tạo hóa đơn
    private String getOrCreateMaKhachHang() {
        String tenKH = txtTenKhachHang.getText().trim();
        String soDienThoai = txtSoDienThoai.getText().trim();

        if (tenKH.isEmpty() || soDienThoai.isEmpty()) return null;

        KhachHang_DAO khachHangDAO = new KhachHang_DAO();

        // Tìm khách hàng theo số điện thoại trước
        KhachHang existingKH = khachHangDAO.getKhachHangTheoSoDienThoai(soDienThoai);
        if (existingKH != null) {
            return existingKH.getMaKH();
        }

        // Nếu không tìm thấy, tạo khách hàng mới với số điện thoại đã nhập
        List<KhachHang> allKH = khachHangDAO.getAllKhachHang();
        String newMaKH = generateNewMaKhachHang(allKH);
        KhachHang newKH = new KhachHang(newMaKH, tenKH, soDienThoai, false);
        khachHangDAO.insertKhachHang(newKH);
        return newMaKH;
    }

    // Sinh mã khách hàng mới chưa tồn tại trong CSDL dạng KHxxx hoặc KH000000001
    private String generateNewMaKhachHang(List<KhachHang> allKH) {
        int max = 0;
        int digitCount = 3; // Mặc định nếu chưa có mã nào
        for (KhachHang kh : allKH) {
            String ma = kh.getMaKH();
            if (ma != null && ma.matches("KH\\d+")) {
                String numberPart = ma.substring(2);
                int num = Integer.parseInt(numberPart);
                if (num > max) max = num;
                if (numberPart.length() > digitCount) digitCount = numberPart.length();
            }
        }
        return String.format("KH%0" + digitCount + "d", max + 1);
    }

    // Chuyển chuỗi định dạng tiền tệ về số double
    private double parseCurrency(String currencyString) throws Exception {
        if (currencyString == null || currencyString.trim().isEmpty()) {
            return 0;
        }
        // Loại bỏ ký tự không phải số và không phải dấu chấm thập phân
        String numericString = currencyString.replaceAll("[^0-9]", "");
        return Double.parseDouble(numericString);
    }

    /**
     * dùng để xuất hóa đơn. chỉ cần gọi hàm và truyền tham số hóa đơn, ArrayList trong phương thức , thuế và tiền trong code là ok.
     * dùng thì cứ gọi sao khi thanh toán thành công là được.
     */
    private void thongBaoVaXuatHoaDon(HoaDon hoaDon, ArrayList<ChiTietHoaDon> listCTHD, double tongTien, double thue) {
        Platform.runLater(() -> {
            // Hiển thị thông báo thành công
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Tạo hóa đơn thành công");
            successAlert.setHeaderText("Hóa đơn " + hoaDon.getMaHD() + " đã được tạo thành công!");
            successAlert.setContentText("Tổng tiền: " + VND_FORMAT.format(hoaDon.getTongTien()) + "đ\n" +
                    "Khách hàng: " + hoaDon.getMaKH().getTenKH() + "\n" +
                    "Ngày tạo: " + hoaDon.getNgayTao().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

            // Thêm nút xuất hóa đơn
            ButtonType xuatHoaDonButton = new ButtonType("Xuất hóa đơn PDF", ButtonBar.ButtonData.YES);
            ButtonType dongButton = new ButtonType("Đóng", ButtonBar.ButtonData.NO);
            successAlert.getButtonTypes().setAll(xuatHoaDonButton, dongButton);

            // Try to set owner for the alert
            Window owner = (this.getScene() != null) ? this.getScene().getWindow() : null;
            if (owner == null) {
                for (Window w : Window.getWindows()) {
                    if (w.isShowing()) {
                        owner = w;
                        break;
                    }
                }
            }
            if (owner != null) successAlert.initOwner(owner);

            Optional<ButtonType> result = successAlert.showAndWait();

            if (result.isPresent() && result.get() == xuatHoaDonButton) {
                try {
                    FileChooser chooser = new FileChooser();
                    chooser.setTitle("Lưu hóa đơn PDF");

                    // Gợi ý tên file mặc định
                    String fileName = "HoaDon_"
                            + hoaDon.getMaHD() + "_"
                            + LocalDate.now() + ".pdf";
                    chooser.setInitialFileName(fileName);

                    chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
                    File file;
                    if (owner instanceof Stage) {
                        file = chooser.showSaveDialog((Stage) owner);
                    } else {
                        // fallback
                        file = chooser.showSaveDialog(null);
                    }
                    if (file == null) return;

                    XuatHoaDonPDF.xuatFilePDF(
                            file,
                            hoaDon,
                            listCTHD,
                            thue,
                            tongTien
                    );

                    System.out.println("===== PREVIEW HÓA ĐƠN =====");
                    System.out.println("Mã HD: " + hoaDon.getMaHD());
                    System.out.println("Khách hàng: " + hoaDon.getMaKH().getTenKH());
                    System.out.println("Ngày tạo: " + hoaDon.getNgayTao());
                    System.out.println("Thuế: " + VND_FORMAT.format(thue) + "đ");
                    System.out.println("Tổng tiền: " + VND_FORMAT.format(tongTien) + "đ");
                    System.out.println("---------------------------");

                    System.out.println("CHI TIẾT HÓA ĐƠN:");
                    for (ChiTietHoaDon ct : listCTHD) {
                        String tenThuoc = (ct.getMaCTT() != null && ct.getMaCTT().getMaThuoc() != null)
                            ? ct.getMaCTT().getMaThuoc().getTenThuoc()
                            : "N/A";
                        String donVi = (ct.getMaDVT() != null)
                            ? ct.getMaDVT().getTenDVT()
                            : "N/A";
                        System.out.println(
                                String.format("- %s | Số lượng: %d | Đơn vị: %s | Thành tiền: %s",
                                        tenThuoc,
                                        ct.getSoLuong(),
                                        donVi,
                                        VND_FORMAT.format(ct.getThanhTien()) + "đ"
                                )
                        );
                    }
                    System.out.println("===== END PREVIEW =====");


                    showMess("Thành công", "Xuất hóa đơn PDF thành công!");

                } catch (Exception ex) {
                    ex.printStackTrace();
                    showMess("Lỗi", "Không thể xuất hóa đơn PDF");
                }
            }
        });
    }

    private void showMess(String tieuDe, String noiDung) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(tieuDe);
        alert.setHeaderText(null);
        alert.setContentText(noiDung);
        alert.showAndWait();
    }

}

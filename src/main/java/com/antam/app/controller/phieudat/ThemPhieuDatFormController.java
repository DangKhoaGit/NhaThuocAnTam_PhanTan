//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.antam.app.controller.phieudat;

import com.antam.app.connect.ConnectDB;
import com.antam.app.service.*;
import com.antam.app.service.impl.*;
import com.antam.app.dto.*;
import com.antam.app.helper.TuDongGoiY;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javafx.geometry.Insets;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.TextFlow;

public class ThemPhieuDatFormController extends DialogPane{
    private TextField txtMa;
    private TextField txtTenKhach;
    private TextField txtSoDienThoai;
    private VBox vbThuoc;
    private ComboBox<ThuocDTO> cbTenThuoc;
    private ComboBox<DonViTinhDTO> cbDonVi;
    private Spinner<Integer> spSoLuong;
    private TextField txtDonGia;
    private Button btnThem;
    private ComboBox<KhuyenMaiDTO> cbKhuyenMai;
    private TableView<ChiTietPhieuDatThuocDTO> tbChonThuoc;
    private TableColumn<ChiTietPhieuDatThuocDTO,String> colTenThuoc;
    private TableColumn<ChiTietPhieuDatThuocDTO,String> colDonVi;
    private TableColumn<ChiTietPhieuDatThuocDTO,String> colSoLuong;
    private TableColumn<ChiTietPhieuDatThuocDTO,String> colDonGia;
    private TableColumn<ChiTietPhieuDatThuocDTO,String> colThanhTien;
    private Text txtTotal;
    private Text txtCanhBaoSDT = new Text();
    private Text txtCanhBaoKM = new Text();
    private Text txtThue = new Text();

    private Thuoc_Service thuoc_dao = new Thuoc_Service();
    private DonViTinh_Service donViTinh_dao = new DonViTinh_Service();
    private ArrayList<ThuocDTO> dsThuoc;
    private ArrayList<DonViTinhDTO> dsDonViTinh;
    private DecimalFormat decimalFormat = new DecimalFormat("#,### đ");
    private ArrayList<ChiTietPhieuDatThuocDTO> list = new ArrayList<>();
    private ObservableList<ChiTietPhieuDatThuocDTO> obsThuoc = FXCollections.observableArrayList();


    private KhachHang_Service khachHangDAO = new KhachHang_Service();
    private ArrayList<KhachHangDTO> dsKhach = khachHangDAO.getAllKhachHang();
    private KhuyenMai_Service KhuyenMai_DAO = new KhuyenMai_Service();
    private ArrayList<KhuyenMaiDTO> dsKhuyenMai = new ArrayList<>();
    private LoThuoc_Service chiTietThuoc_dao = new LoThuoc_Service();
    private HoaDon_Service hoaDon_DAO = new HoaDon_Service();
    private ObservableList<KhachHangDTO> autoKhach = FXCollections.observableArrayList(dsKhach);

    public ThemPhieuDatFormController() {
        FlowPane header = new FlowPane();
        header.setAlignment(Pos.CENTER);
        header.setPrefSize(800, 35);
        header.setStyle("-fx-background-color: #1e3a8a;");

        Text headerTitle = new Text("Tạo phiếu đặt mới");
        headerTitle.setFill(Color.WHITE);
        headerTitle.setFont(Font.font("System Bold", 15));
        FlowPane.setMargin(headerTitle, new Insets(10, 0, 10, 0));
        header.getChildren().add(headerTitle);

        AnchorPane content = new AnchorPane();
        content.setPrefSize(800, 557);

        VBox root = new VBox(10);
        AnchorPane.setLeftAnchor(root, 10.0);
        AnchorPane.setTopAnchor(root, 10.0);

        GridPane gridTop = new GridPane();
        gridTop.setHgap(5);
        gridTop.setPrefSize(744, 69);

        ColumnConstraints cc1 = new ColumnConstraints();
        cc1.setHgrow(Priority.SOMETIMES);
        ColumnConstraints cc2 = new ColumnConstraints();
        cc2.setHgrow(Priority.SOMETIMES);
        gridTop.getColumnConstraints().addAll(cc1, cc2);

        gridTop.getRowConstraints().addAll(
                new RowConstraints(30),
                new RowConstraints(40),
                new RowConstraints(30)
        );

        Text lblMa = new Text("Mã phiếu đặt:");
        lblMa.setFill(Color.web("#374151"));

        txtMa = new TextField();
        txtMa.getStyleClass().add("text-field");

        GridPane.setRowIndex(txtMa, 1);

        Text lblTenKhach = new Text("Tên khách hàng:");
        lblTenKhach.setFill(Color.web("#374151"));
        GridPane.setColumnIndex(lblTenKhach, 1);

        txtTenKhach = new TextField();
        txtTenKhach.getStyleClass().add("text-field");
        GridPane.setColumnIndex(txtTenKhach, 1);
        GridPane.setRowIndex(txtTenKhach, 1);

        HBox hbSDT = new HBox();
        Text lblSoDienThoai = new Text("Số điện thoại:");
        lblSoDienThoai.setFill(Color.web("#374151"));
        // Cảnh báo
        txtCanhBaoSDT.setFill(Color.RED);

        // ngăn chặn nhảy lệch
        TextFlow warnFlow = new TextFlow(txtCanhBaoSDT);
        warnFlow.setMinWidth(150);
        warnFlow.setMaxWidth(150);

        // Tạo nhóm label + cảnh báo
        hbSDT.getChildren().add(lblSoDienThoai);
        hbSDT.getChildren().add(warnFlow);

        // Đặt vào GridPane
        GridPane.setColumnIndex(hbSDT, 1);
        GridPane.setRowIndex(hbSDT, 2);
        // TextField nhập SDT
        txtSoDienThoai = new TextField();
        txtSoDienThoai.getStyleClass().add("text-field");
        // Đặt TextField vào GridPane
        GridPane.setColumnIndex(txtSoDienThoai, 1);
        GridPane.setRowIndex(txtSoDienThoai, 3);


        Text lblThuocDat = new Text("Thuốc đặt:");
        lblThuocDat.setFill(Color.web("#374151"));
        GridPane.setRowIndex(lblThuocDat, 4);

        gridTop.getChildren().addAll(lblMa, txtMa, lblTenKhach, txtTenKhach, hbSDT, txtSoDienThoai, lblThuocDat);

        vbThuoc = new VBox(5);
        vbThuoc.setMaxWidth(Double.MAX_VALUE);
        vbThuoc.setStyle("-fx-border-color: #e5e7eb; -fx-border-radius: 6px; -fx-border-width: 2px;");
        vbThuoc.setPadding(new Insets(10));

        HBox hbFirst = new HBox(20);
        hbFirst.setPrefSize(720, 29);
        hbFirst.setAlignment(Pos.CENTER_LEFT);

        VBox boxTen = new VBox();
        Text lblTenThuoc = new Text("Tên thuốc:");
        cbTenThuoc = new ComboBox();
        cbTenThuoc.setPrefSize(154, 26);
        boxTen.getChildren().addAll(lblTenThuoc, cbTenThuoc);

        VBox boxDonVi = new VBox();
        Text lblDV = new Text("Đơn vị:");
        cbDonVi = new ComboBox();
        cbDonVi.setPrefSize(161, 26);
        boxDonVi.getChildren().addAll(lblDV, cbDonVi);

        VBox boxSL = new VBox();
        Text lblSL = new Text("Số lượng:");
        spSoLuong = new Spinner();
        spSoLuong.setPrefSize(150, 26);
        boxSL.getChildren().addAll(lblSL, spSoLuong);

        VBox boxGia = new VBox();
        Text lblGia = new Text("Đơn giá:");
        txtDonGia = new TextField();
        boxGia.getChildren().addAll(lblGia, txtDonGia);

        hbFirst.getChildren().addAll(boxTen, boxDonVi, boxSL, boxGia);
        vbThuoc.getChildren().add(hbFirst);

        btnThem = new Button("Thêm thuốc");
        btnThem.getStyleClass().add("btn-gray");
        btnThem.getStylesheets().add(getClass().getResource("/com/antam/app/styles/dashboard_style.css").toExternalForm());

        HBox hbCanhBaoKM = new HBox();
        txtCanhBaoKM.setFill(Color.RED);
        Text lblKM = new Text("Áp dụng khuyến mãi:");

        hbCanhBaoKM.getChildren().addAll(lblKM, txtCanhBaoKM);
        cbKhuyenMai = new ComboBox();
        cbKhuyenMai.setPrefSize(778, 44);
        cbKhuyenMai.setPromptText("Chọn khuyến mãi");

        tbChonThuoc = new TableView();
        tbChonThuoc.setPrefSize(785, 200);

        colTenThuoc = new TableColumn("Tên thuốc");
        colTenThuoc.setPrefWidth(192.8);

        colDonVi = new TableColumn("Đơn vị");
        colDonVi.setPrefWidth(168.8);

        colSoLuong = new TableColumn("Số lượng");
        colSoLuong.setPrefWidth(110.4);

        colDonGia = new TableColumn("Đơn giá");
        colDonGia.setPrefWidth(166.4);

        colThanhTien = new TableColumn("Thành tiền");
        colThanhTien.setPrefWidth(143.2);

        tbChonThuoc.getColumns().addAll(colTenThuoc, colDonVi, colSoLuong, colDonGia, colThanhTien);
        GridPane gridTotal = new GridPane();
        gridTotal.setHgap(10);
        gridTotal.setVgap(5);
        gridTotal.setPadding(new Insets(10));
        gridTotal.setStyle(
                "-fx-background-color: #f8fafc;" +
                        "-fx-background-radius: 8px;" +
                        "-fx-border-color: #e5e7eb;" +
                        "-fx-border-radius: 8px;"
        );

        /* Column trái - label */
        ColumnConstraints colLeft = new ColumnConstraints();
        colLeft.setHgrow(Priority.ALWAYS);

        /* Column phải - tiền */
        ColumnConstraints colRight = new ColumnConstraints();
        colRight.setHgrow(Priority.ALWAYS);
        colRight.setHalignment(HPos.RIGHT);

        gridTotal.getColumnConstraints().addAll(colLeft, colRight);

        /* Label Tổng tiền */
        Text lblTong = new Text("");
        lblTong.setFill(Color.web("#374151"));
        lblTong.setFont(Font.font("System", FontWeight.BOLD, 16));

        /* Tổng tiền */
        txtTotal = new Text("0 đ");
        txtTotal.setFill(Color.web("#111827"));
        txtTotal.setFont(Font.font("System", FontWeight.BOLD, 18));

        /* Thuế */
        txtThue = new Text("Thuế: 0 đ");
        txtThue.setFill(Color.web("#6b7280"));
        txtThue.setFont(Font.font(14));

        /* Add vào grid */
        gridTotal.add(lblTong, 0, 0);
        gridTotal.add(txtTotal, 1, 0);
        gridTotal.add(txtThue, 1, 1);

        root.getChildren().addAll(
                gridTop,
                vbThuoc,
                btnThem,
                hbCanhBaoKM,
                cbKhuyenMai,
                tbChonThuoc,
                gridTotal
        );

        content.getChildren().add(root);

        this.getStylesheets().add(getClass().getResource("/com/antam/app/styles/dashboard_style.css").toExternalForm());
        this.setHeader(header);
        this.setContent(content);
        /** Sự kiện **/

        ButtonType cancelButton = new ButtonType("Huỷ", ButtonData.CANCEL_CLOSE);
        ButtonType applyButton = new ButtonType("Lưu", ButtonData.APPLY);
        this.getButtonTypes().add(cancelButton);
        this.getButtonTypes().add(applyButton);

        //tạo cổng kết nối
        try {
            Connection con = ConnectDB.getInstance().connect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        //setup phụ
        txtDonGia.setEditable(false);
        cbDonVi.setDisable(true);

        //load ds đơn vị tính.
        dsDonViTinh = donViTinh_dao.getTatCaDonViTinh();
        dsThuoc = thuoc_dao.getAllThuoc();
        cbDonVi.getItems().addAll(FXCollections.observableArrayList(dsDonViTinh));
        cbDonVi.getSelectionModel().selectFirst();
        // load comboBox thuốc
        cbTenThuoc.getItems().addAll(FXCollections.observableArrayList(dsThuoc));

        // setup mã phiếu
        txtMa.setText(getHashPD());
        txtMa.setEditable(false);


        // Load khuyen mai after services are ready.
        dsKhuyenMai = new ArrayList<>(KhuyenMai_DAO.getAllKhuyenMaiConHieuLuc());

        // load ComboBox Khuyến mãi.
        KhuyenMaiDTO nothing = new KhuyenMaiDTO("None","Không áp dụng");
        cbKhuyenMai.getItems().clear();
        cbKhuyenMai.getItems().add(nothing);
        cbKhuyenMai.getItems().addAll(FXCollections.observableArrayList(dsKhuyenMai));
        cbKhuyenMai.setConverter(new javafx.util.StringConverter<>() {
            @Override
            public String toString(KhuyenMaiDTO km) {
                return km == null ? "" : km.getTenKM();
            }

            @Override
            public KhuyenMaiDTO fromString(String text) {
                if (text == null) return null;
                return cbKhuyenMai.getItems().stream()
                        .filter(km -> text.equals(km.getTenKM()))
                        .findFirst()
                        .orElse(null);
            }
        });
        cbKhuyenMai.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(KhuyenMaiDTO item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getTenKM());
            }
        });

        //gọi tính tổng tiền khi chạy giao diện
        loadTongTien();

        //setup table
        setupTable();
        tbChonThuoc.setItems(obsThuoc);
        loadTable();

        //
        Button btnApply = (Button)this.lookupButton(applyButton);
        Button btnCancel = (Button)this.lookupButton(cancelButton);

        //sự kiện thêm phiếu đặt
        btnApply.addEventFilter(ActionEvent.ACTION, event -> {
            if (checkDuLieu()) {
                themPhieuDat();
            } else {
                event.consume();
            }
        });


        //thêm thuốc vào table
        btnThem.setOnAction(  e ->   {
            if (approveThuoc()){
                addThuocVaoTable();
                loadTongTien();
            }
        });

        //sự kiện khi thay đổi comboBox khuyến mãi
        cbKhuyenMai.setOnAction(e -> {
            KhuyenMaiDTO km = cbKhuyenMai.getSelectionModel().getSelectedItem();
            // Không áp dụng hoặc chọn null
            if (km == null || km.getTenKM().equals("Không áp dụng")) {
                txtCanhBaoKM.setText("");
                loadTongTien();
                return;
            }
            LocalDate today = LocalDate.now();
            int soDaSuDung = hoaDon_DAO.soHoaDonDaCoKhuyenMaiVoiMa(km.getMaKM());
            // Chưa đến ngày bắt đầu
            if (km.getNgayBatDau().isAfter(today)) {
                txtCanhBaoKM.setText("Khuyến mãi chưa bắt đầu");
            }
            // Đã hết hạn
            else if (km.getNgayKetThuc().isBefore(today)) {
                txtCanhBaoKM.setText("Khuyến mãi đã hết hạn");
            }
            else if (soDaSuDung >= km.getSoLuongToiDa()) {
                    txtCanhBaoKM.setText("Khuyến mãi đã đạt số lượng tối đa");
            }
            // Hợp lệ
            else {
                txtCanhBaoKM.setText("");
            }
            loadTongTien();
        });

        //setup spinner số lượng
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 1);
        spSoLuong.setValueFactory(valueFactory);
        spSoLuong.setEditable(true);

        //sự kiện khi thay đổi comboBox thuốc
        cbTenThuoc.setOnAction(e -> {

            if (cbTenThuoc.getSelectionModel().getSelectedItem() == null) {
                return;
            }

            for (DonViTinhDTO dvt : cbDonVi.getItems()) {
                if (dvt.getMaDVT() == cbTenThuoc.getSelectionModel().getSelectedItem().getMaDVTCoSo().getMaDVT() ) {
                    cbDonVi.getSelectionModel().select(dvt);
                    break;
                }
            }

            txtDonGia.setText(dinhDangTien(cbTenThuoc.getSelectionModel().getSelectedItem().getGiaBan()));
        });

        //sự kiện thay đổi số điện thoại
        txtSoDienThoai.textProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue.isBlank()) {
                txtCanhBaoSDT.setText("");
                return;
            }

            if (!newValue.matches("^0\\d{9}$")) {
                txtCanhBaoSDT.setText("Số điện thoại không hợp lệ");
            } else {
                txtCanhBaoSDT.setText("");
            }
        });


        //sự kiện autocomplete khách hàng
        TuDongGoiY.goiYKhach(txtTenKhach, txtSoDienThoai, autoKhach);

        //sự kiện xóa thuốc khỏi bảng
        tbChonThuoc.setRowFactory( tv -> {
            TableRow<ChiTietPhieuDatThuocDTO> row = new TableRow<>();
            ContextMenu contextMenu = new ContextMenu();
            MenuItem deleteItem = new MenuItem("Xóa thuốc khỏi bảng");
            deleteItem.setOnAction( event -> {
                ChiTietPhieuDatThuocDTO selectedItem = row.getItem();
                list.remove(selectedItem);
                loadTable();
                loadTongTien();
            });
            contextMenu.getItems().add(deleteItem);
            // Chỉ hiển thị menu khi có dữ liệu
            row.contextMenuProperty().bind(
                    javafx.beans.binding.Bindings.when(row.emptyProperty())
                            .then((ContextMenu)null)
                            .otherwise(contextMenu)
            );
            return row;
        });
    }


    /**
     * Duyệt thông tin thuốc trước khi thêm vào bảng
     * @return true - hợp lệ, false - không hợp lệ
     */
    private boolean approveThuoc() {

        ThuocDTO selectedThuocDTO = cbTenThuoc.getSelectionModel().getSelectedItem();
        //gọi integer để có thể sử dụng null trong điều kiện
        Integer soLuongNhap = spSoLuong.getValue();

        // Nếu người dùng chưa chọn thuốc hoặc chọn rỗng
        if (selectedThuocDTO == null) {
            showMess("Thiếu thông tin", "Vui lòng chọn thuốc.");
            return false;
        }
        // Số lượng không hợp lệ
        if (soLuongNhap == null || soLuongNhap <= 0) {
            showMess("Số lượng sai", "Số lượng thuốc phải lớn hơn 0.");
            return false;
        }

        // Lấy chi tiết thuốc trong kho
        ArrayList<LoThuocDTO> dsChiTiet =
                chiTietThuoc_dao.getAllCHiTietThuocTheoMaThuoc(selectedThuocDTO.getMaThuoc());
        if (dsChiTiet.isEmpty()) {
            showMess("Hết hàng", "Thuốc \"" + selectedThuocDTO.getTenThuoc() + "\" hiện không có trong kho.");
            return false;
        }

        int tongSoLuongTrongKho = dsChiTiet.stream()
                .filter(ctt -> ctt.getHanSuDung().isAfter(LocalDate.now()))
                .mapToInt(LoThuocDTO::getSoLuong)
                .sum();
        // So sánh tồn kho với số lượng nhập

        int soLuongdaChon = 0;
        for (ChiTietPhieuDatThuocDTO ct : tbChonThuoc.getItems()) {
            if (ct.getMaThuoc().getMaThuocDTO().equals(selectedThuocDTO.getMaThuoc())) {
                soLuongdaChon += ct.getSoLuong();
            }
        }

        if ( (soLuongNhap + soLuongdaChon) > tongSoLuongTrongKho) {
            showMess("Không đủ số lượng",
                    "đơn vị của thuốc " + selectedThuocDTO.getTenThuoc() +
                            " Kho chỉ còn \"" + (tongSoLuongTrongKho )+ "\".");
            return false;
        }
        return true;
    }

    /**
     * Load lại bảng thuốc
     */
    private void loadTable() {
        obsThuoc.setAll(list);
    }

    /**
     * Thêm thuốc vào bảng nếu đã tồn tại thì cộng dồn số lượng, nếu chi tiết thiếu thì tạo thêm 1 dòng mới
     */
    private void addThuocVaoTable() {

        ThuocDTO thuocDTO = cbTenThuoc.getSelectionModel().getSelectedItem();
        int soLuongCan = spSoLuong.getValue();
        DonViTinhDTO dvt = cbDonVi.getSelectionModel().getSelectedItem();

        if (thuocDTO == null || soLuongCan <= 0) return;

        // 1. Lấy các lô còn hạn, sắp theo hạn tăng dần
        ArrayList<LoThuocDTO> dsLo = chiTietThuoc_dao
                .getAllChiTietThuocVoiMaChoCTPD(thuocDTO.getMaThuoc())
                .stream()
                .filter(ct -> ct.getHanSuDung().isAfter(LocalDate.now()))
                .sorted(Comparator.comparing(LoThuocDTO::getHanSuDung))
                .collect(Collectors.toCollection(ArrayList::new));

        int tongTon = dsLo.stream().mapToInt(LoThuocDTO::getSoLuong).sum();
        if (tongTon < soLuongCan) {
            showMess("Không đủ tồn", "Kho chỉ còn " + tongTon);
            return;
        }

        // 2. Chia số lượng cho từng lô
        for (LoThuocDTO lo : dsLo) {
            if (soLuongCan <= 0) break;

            int lay = Math.min(lo.getSoLuong(), soLuongCan);
            soLuongCan -= lay;

            ChiTietPhieuDatThuocDTO ct = new ChiTietPhieuDatThuocDTO(lo, lay, dvt);

            // 3. Nếu đã tồn tại cùng lô → cộng dồn
            Optional<ChiTietPhieuDatThuocDTO> existing = list.stream()
                    .filter(x -> x.getMaThuoc().getMaLoThuoc() == lo.getMaLoThuoc())
                    .findFirst();

            if (existing.isPresent()) {
                ChiTietPhieuDatThuocDTO old = existing.get();
                old.setSoLuong(old.getSoLuong() + lay);
            } else {
                list.add(ct);
            }
        }

        // 4. Update UI
        loadTable();
        loadTongTien();

        txtDonGia.setText(dinhDangTien(thuocDTO.getGiaBan()));
    }

    private void themPhieuDat() {

        // ===== 1. NHÂN VIÊN =====
        NhanVienDTO nguoiDat = PhienNguoiDungDTO.getMaNV();
        if (nguoiDat == null) {
            showMess("Lỗi", "Không xác định được nhân viên.");
            return;
        }

        // ===== 2. KHÁCH HÀNG =====
        String ten = txtTenKhach.getText().trim();
        String sdt = txtSoDienThoai.getText().trim();

        if (ten.isEmpty() || sdt.isEmpty()) {
            showMess("Thiếu thông tin", "Vui lòng nhập tên và SĐT khách hàng.");
            return;
        }

        KhachHangDTO khach;
        if (isKhachHangMoi()) {
            khach = new KhachHangDTO(getMaKhachMoi(), ten, sdt, false);
            khachHangDAO.insertKhachHang(khach);
        } else {
            khach = dsKhach.stream()
                    .filter(k -> k.getSoDienThoai().equals(sdt))
                    .findFirst()
                    .orElse(null);

            if (khach == null) {
                showMess("Lỗi", "Không tìm thấy khách hàng.");
                return;
            }
        }

        // ===== 3. KIỂM TRA THUỐC =====
        if (tbChonThuoc.getItems().isEmpty()) {
            showMess("Lỗi", "Chưa chọn thuốc.");
            return;
        }

        // ===== 4. KHUYẾN MÃI =====
        KhuyenMaiDTO km = cbKhuyenMai.getSelectionModel().getSelectedItem();
        if (km != null && "Không áp dụng".equals(km.getTenKM())) {
            km = null;
        }

        // ===== 5. TẠO PHIẾU =====
        PhieuDatThuocDTO phieu = new PhieuDatThuocDTO(
                getHashPD(),
                LocalDate.now(),
                false,
                nguoiDat,
                khach,
                km,
                tinhTongTien()
        );

        // Lưu vào dbs
        try {
            ConnectDB.getInstance().connect();
            Connection con = ConnectDB.getConnection();
            con.setAutoCommit(false); // TRANSACTION

            // 6.1 Thêm phiếu
            I_PhieuDat_Service.themPhieuDatThuocVaoDBS(phieu);

            // 6.2 Thêm chi tiết + trừ kho
            for (ChiTietPhieuDatThuocDTO ct : tbChonThuoc.getItems()) {

                LoThuocDTO lo = ct.getMaThuoc();
                int soLuongDat = ct.getSoLuong();

                if (lo.getSoLuong() < soLuongDat) {
                    throw new RuntimeException("Không đủ tồn kho cho lô " + lo.getMaLoThuoc());
                }

                // Thêm chi tiết phiếu
                I_ChiTietPhieuDat_Service.themChiTietPhieuDatVaoDBS(
                        new ChiTietPhieuDatThuocDTO(
                                phieu,
                                lo,
                                soLuongDat,
                                ct.getDonViTinhDTO()
                        )
                );

                // Trừ kho đúng lô
                chiTietThuoc_dao.CapNhatSoLuongChiTietThuoc(
                        lo.getMaLoThuoc(),
                        lo.getSoLuong() - soLuongDat
                );
            }

            con.commit(); // OK
            showMess("Thành công", "Tạo phiếu đặt thuốc thành công.");

        } catch (Exception e) {
            e.printStackTrace();
            try {
                ConnectDB.getConnection().rollback();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            showMess("Lỗi", "Không thể tạo phiếu đặt. Dữ liệu đã được hoàn tác.");
        }
    }


    /**
     * Tạo mã khách hàng mới với đinh dạng KHxxxxxxxxx (x là số bất kì, có 9 số)
     * @return
     */
    private String getMaKhachMoi() {
        int newNum = khachHangDAO. getMaxHash() +1;
        return String.format("KH%09d", newNum);
    }


    /**
     * Kiểm tra khách hàng đã tồn tại chưa
     * @return true - khách mới. false - khách đã tồn tại.
     */
    private boolean isKhachHangMoi() {
        String sdt = txtSoDienThoai.getText().trim();
        if (sdt == null || sdt.isEmpty()) return true;

        for (KhachHangDTO kh : dsKhach) {
            if (kh.getSoDienThoai().equals(sdt)) {
                return false;
            }
        }
        return true;
    }


    /**
     * Tạo mã phiếu đặt mới chưa tồn tại trong dbs
     * @return String - mã phiếu đặt mới
     */
    private String getHashPD() {
        String hash = I_PhieuDat_Service.getMaxHash();
        if (hash == null){
            return "";
        }else{
            int soThuTu = Integer.parseInt(hash) + 1;
            return String.format("PDT%03d", soThuTu);
        }
    }

    /**
     * Tính tổng tiền của phiếu đặt áp dụng khuyến mãi (nếu có)
     * @return double - tổng tiền sau khi áp dụng khuyến mãi (nếu có)
     */
    public double tinhTongTien(){
        double tongTien = 0.0;
        for (ChiTietPhieuDatThuocDTO e : tbChonThuoc.getItems()){
            tongTien += e.getSoLuong()
                    * e.getMaThuoc().getMaThuocDTO().getGiaBan()
                    * (1 - e.getMaThuoc().getMaThuocDTO().getThue());
        }
        // Áp dụng khuyến mãi nếu có
        if (cbKhuyenMai.getSelectionModel().getSelectedItem() != null &&
                !cbKhuyenMai.getSelectionModel().getSelectedItem().getTenKM().equals("Không áp dụng")) {
            if (!txtCanhBaoKM.getText().isEmpty()) {
                return tongTien;
            }
            KhuyenMaiDTO khuyenMaiDTO = cbKhuyenMai.getSelectionModel().getSelectedItem();
            LoaiKhuyenMaiDTO loaiKM = khuyenMaiDTO.getLoaiKhuyenMaiDTO();
            int soDaSuDung = hoaDon_DAO.soHoaDonDaCoKhuyenMaiVoiMa(khuyenMaiDTO.getMaKM());
            if (soDaSuDung >= khuyenMaiDTO.getSoLuongToiDa()) {
                return tongTien > 0? tongTien : 0;
            }else{
                if(loaiKM.getMaLKM() == 1){
                    tongTien = tongTien * (1 - khuyenMaiDTO.getSo()/100);
                }else {
                    tongTien = tongTien - khuyenMaiDTO.getSo();
                }
            }
        }

        return tongTien > 0 ? tongTien : 0;
    }

    public void showMess(String tieuDe, String vanBan){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(tieuDe);
        alert.setContentText(vanBan);
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    /**
     * Cập nhật tổng tiền hiển thị trên giao diện
     */
    private void loadTongTien(){
        double tongTien = tinhTongTien();
        txtTotal.setText("Tổng tiền: " + dinhDangTien(tongTien));
        txtThue.setText("Thuế: " + dinhDangTien(tinhThue()));
    }

    private double tinhThue() {
        double thue = 0.0;
        if (tbChonThuoc.getItems().isEmpty()) {
            return thue;
        }
        for (ChiTietPhieuDatThuocDTO e : tbChonThuoc.getItems()){
            thue += e.getSoLuong() * e.getMaThuoc().getMaThuocDTO().getGiaBan()* e.getMaThuoc().getMaThuocDTO().getThue();
        }
        return thue;
    }

    private boolean checkDuLieu() {
        if (txtTenKhach.getText().trim().isEmpty()) {
            showMess("Thiếu thông tin", "Vui lòng nhập tên khách hàng.");
            txtTenKhach.requestFocus();
            return false;
        }
        if (txtSoDienThoai.getText().trim().isEmpty()){
            showMess("Thiếu thông tin số điện thọai khách hàng", "Vui lòng nhập số điện thoại khách hàng.");
            txtSoDienThoai.requestFocus();;
            return false;
        }
        if (!txtSoDienThoai.getText().matches("^0\\d{9}$")){
            showMess("Thông tin số điện thọai khách hàng không hợp lệ", "Số điện thoại khách hàng phải có 10 số và bắt đầu với 03, 05, 06, 07, 09.");
            txtSoDienThoai.requestFocus();;
            return false;
        }
        if (tbChonThuoc.getItems().isEmpty()) {
            showMess("Thiếu thông tin", "Vui lòng thêm thuốc vào phiếu đặt.");
            cbTenThuoc.requestFocus();
            return false;
        }
        if (txtTotal.getText().equals("Tổng tiền: 0 đ")) {
            showMess("Tổng tiền bằng 0", "Giá trị tổng tiền không hợp lệ.");
            return false;
        }
        return true;
    }

    /**
     * hỗ trợ định dạng tiền
     * @param tien
     * @return String - tiền đã được định dạng
     */
    public String dinhDangTien(double tien){
        DecimalFormat df = new DecimalFormat("#,###đ");
        return df.format(tien);
    }

    /**
     * Cài đặt các cột trong bảng
     */
    private void setupTable(){
        colTenThuoc.setCellValueFactory( cellData -> new SimpleStringProperty(cellData.getValue().getMaThuoc().getMaThuocDTO().getTenThuoc()));
        colDonVi.setCellValueFactory( cellData -> new SimpleStringProperty(cellData.getValue().getMaThuoc().getMaThuocDTO().getMaDVTCoSo().getTenDVT()));
        colSoLuong.setCellValueFactory( cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getSoLuong())));
        colDonGia.setCellValueFactory( cellData -> new SimpleStringProperty(dinhDangTien(cellData.getValue().getMaThuoc().getMaThuocDTO().getGiaBan())));
        colThanhTien.setCellValueFactory( cellData -> {
            double thanhTien = cellData.getValue().getSoLuong() * cellData.getValue().getMaThuoc().getMaThuocDTO().getGiaBan();
            return new SimpleStringProperty(dinhDangTien(thanhTien));
        } );
    }
}

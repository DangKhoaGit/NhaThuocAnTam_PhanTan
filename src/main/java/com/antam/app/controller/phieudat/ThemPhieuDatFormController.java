//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.antam.app.controller.phieudat;

import com.antam.app.network.ClientManager;
import com.antam.app.dto.*;
import com.antam.app.helper.TuDongGoiY;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import javafx.geometry.Insets;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.TextFlow;

public class ThemPhieuDatFormController extends DialogPane {
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
    private TableColumn<ChiTietPhieuDatThuocDTO, String> colTenThuoc;
    private TableColumn<ChiTietPhieuDatThuocDTO, String> colDonVi;
    private TableColumn<ChiTietPhieuDatThuocDTO, String> colSoLuong;
    private TableColumn<ChiTietPhieuDatThuocDTO, String> colDonGia;
    private TableColumn<ChiTietPhieuDatThuocDTO, String> colThanhTien;
    private Text txtTotal = new Text();
    private final Text txtCanhBaoSDT = new Text();
    private final Text txtCanhBaoKM = new Text();
    private Text txtThue = new Text();

    private ArrayList<ThuocDTO> dsThuoc;
    private ArrayList<DonViTinhDTO> dsDonViTinh;
//    private ArrayList<ChiTietPhieuDatThuocDTO> list = new ArrayList<>();
    private ObservableList<ChiTietPhieuDatThuocDTO> obsThuoc = FXCollections.observableArrayList();

    private ArrayList<KhachHangDTO> dsKhach = new ArrayList<>();
    private ArrayList<KhuyenMaiDTO> dsKhuyenMai;
    private ObservableList<KhachHangDTO> autoKhach = FXCollections.observableArrayList();

    private ClientManager clientManager = ClientManager.getInstance();

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
                new RowConstraints(30));

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
        cbTenThuoc = new ComboBox<>();
        cbTenThuoc.setPrefSize(154, 26);
        boxTen.getChildren().addAll(lblTenThuoc, cbTenThuoc);

        VBox boxDonVi = new VBox();
        Text lblDV = new Text("Đơn vị:");
        cbDonVi = new ComboBox<>();
        cbDonVi.setPrefSize(161, 26);
        cbDonVi.setConverter(new javafx.util.StringConverter<>() {
            @Override public String toString(DonViTinhDTO dvt) { return dvt == null ? "" : dvt.getTenDVT(); }
            @Override public DonViTinhDTO fromString(String s) { return null; }
        });
        cbDonVi.setCellFactory(lv -> new ListCell<>() {
            @Override protected void updateItem(DonViTinhDTO item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getTenDVT());
            }
        });
        boxDonVi.getChildren().addAll(lblDV, cbDonVi);

        VBox boxSL = new VBox();
        Text lblSL = new Text("Số lượng:");
        spSoLuong = new Spinner<>();
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
        var cssResource = getClass().getResource("/com/antam/app/styles/dashboard_style.css");
        if (cssResource != null) {
            btnThem.getStylesheets().add(cssResource.toExternalForm());
        }

        HBox hbCanhBaoKM = new HBox();
        txtCanhBaoKM.setFill(Color.RED);
        Text lblKM = new Text("Áp dụng khuyến mãi:");

        hbCanhBaoKM.getChildren().addAll(lblKM, txtCanhBaoKM);
        cbKhuyenMai = new ComboBox<>();
        cbKhuyenMai.setPrefSize(778, 44);
        cbKhuyenMai.setPromptText("Chọn khuyến mãi");

        tbChonThuoc = new TableView<>();
        tbChonThuoc.setPrefSize(785, 200);

        colTenThuoc = new TableColumn<>("Tên thuốc");
        colTenThuoc.setPrefWidth(192.8);

        colDonVi = new TableColumn<>("Đơn vị");
        colDonVi.setPrefWidth(168.8);

        colSoLuong = new TableColumn<>("Số lượng");
        colSoLuong.setPrefWidth(110.4);

        colDonGia = new TableColumn<>("Đơn giá");
        colDonGia.setPrefWidth(166.4);

        colThanhTien = new TableColumn<>("Thành tiền");
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
                        "-fx-border-radius: 8px;");

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
                gridTotal);

        content.getChildren().add(root);

        var cssResource2 = getClass().getResource("/com/antam/app/styles/dashboard_style.css");
        if (cssResource2 != null) {
            this.getStylesheets().add(cssResource2.toExternalForm());
        }
        this.setHeader(header);
        this.setContent(content);

        Task<ArrayList<KhachHangDTO>> loadKhachTask = new Task<>() {
            @Override
            protected ArrayList<KhachHangDTO> call() {
                return clientManager.getKhachHangList();
            }
        };
        loadKhachTask.setOnSucceeded(event -> {
            dsKhach = loadKhachTask.getValue();
            autoKhach.addAll(dsKhach);
        });
        Thread loadKhachThread = new Thread(loadKhachTask);
        loadKhachThread.start();

        ButtonType cancelButton = new ButtonType("Huỷ", ButtonData.CANCEL_CLOSE);
        ButtonType applyButton = new ButtonType("Lưu", ButtonData.APPLY);
        this.getButtonTypes().add(cancelButton);
        this.getButtonTypes().add(applyButton);


        // setup phụ
        txtDonGia.setEditable(false);
        cbDonVi.setDisable(true);

        // load ds đơn vị tính.
        Task<ArrayList<DonViTinhDTO>> loadDVTTask = new Task<>() {
            @Override
            protected ArrayList<DonViTinhDTO> call() {
                return clientManager.getDonViTinhList();
            }
        };

        loadDVTTask.setOnSucceeded(event -> {
            dsDonViTinh = loadDVTTask.getValue();
            cbDonVi.getItems().addAll(FXCollections.observableArrayList(dsDonViTinh));
            cbDonVi.getSelectionModel().selectFirst();
        });
        Thread loadDVTThread = new Thread(loadDVTTask);
        loadDVTThread.start();

        Task<ArrayList<ThuocDTO>> loadThuocTask = new Task<>() {
            @Override
            protected ArrayList<ThuocDTO> call() {
                return clientManager.getThuocList();
            }
        };
        loadThuocTask.setOnSucceeded(event -> {
            dsThuoc = loadThuocTask.getValue();
            cbTenThuoc.getItems().addAll(FXCollections.observableArrayList(dsThuoc));
        });

        Thread loadThuocThread = new Thread(loadThuocTask);
        loadThuocThread.start();

        // Removed: cbDonVi.getItems().addAll(FXCollections.observableArrayList(dsDonViTinh));
        // Removed: cbDonVi.getSelectionModel().selectFirst();
        // Removed: cbTenThuoc.getItems().addAll(FXCollections.observableArrayList(dsThuoc));
        cbTenThuoc.setConverter(new javafx.util.StringConverter<>() {
            @Override
            public String toString(ThuocDTO thuoc) {
                return thuoc == null ? "" : thuoc.getTenThuoc();
            }

            @Override
            public ThuocDTO fromString(String text) {
                if (text == null)
                    return null;
                return cbTenThuoc.getItems().stream()
                        .filter(t -> text.equals(t.getTenThuoc()))
                        .findFirst()
                        .orElse(null);
            }
        });
        cbTenThuoc.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(ThuocDTO item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getTenThuoc());
            }
        });

        // setup mã phiếu
        txtMa.setEditable(false);
        Task<String> loadMaTask = new Task<>() {
            @Override protected String call() { return getHashPD(); }
        };
        loadMaTask.setOnSucceeded(e -> txtMa.setText(loadMaTask.getValue()));
        new Thread(loadMaTask).start();

        // Load khuyen mai after services are ready.
        Task<ArrayList<KhuyenMaiDTO>> loadKMTask = new Task<>() {
            @Override
            protected ArrayList<KhuyenMaiDTO> call() {
                return clientManager.getKhuyenMaiConHieuLuc();
            }
        };

        dsKhuyenMai = new ArrayList<>();
        KhuyenMaiDTO nothing = new KhuyenMaiDTO("None", "Không áp dụng");
        cbKhuyenMai.getItems().add(nothing);
        loadKMTask.setOnSucceeded(event -> {
            dsKhuyenMai = loadKMTask.getValue();
            cbKhuyenMai.getItems().addAll(FXCollections.observableArrayList(dsKhuyenMai));
        });
        Thread loadKMThread = new Thread(loadKMTask);
        loadKMThread.start();
        cbKhuyenMai.setConverter(new javafx.util.StringConverter<>() {
            @Override
            public String toString(KhuyenMaiDTO km) {
                return km == null ? "" : km.getTenKM();
            }

            @Override
            public KhuyenMaiDTO fromString(String text) {
                if (text == null)
                    return null;
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

        // gọi tính tổng tiền khi chạy giao diện
        loadTongTien();

        // setup table
        setupTable();
        tbChonThuoc.setItems(obsThuoc);
        loadTable();

        //
        Button btnApply = (Button) this.lookupButton(applyButton);

        // sự kiện thêm phiếu đặt
        btnApply.addEventFilter(ActionEvent.ACTION, event -> {
            event.consume(); // always prevent auto-close; dialog closes manually on success
            if (checkDuLieu()) {
                themPhieuDat();
            }
        });

        // thêm thuốc vào table
        btnThem.setOnAction(e -> {
            // approveThuoc dùng để kiểm tra dữ liệu nhập nhanh (ko check DB)
            if (approveThuoc()) {
                // Gọi hàm thêm, việc Refresh UI sẽ nằm TRONG hàm này
                addThuocVaoTable();
            }
        });

        // sự kiện khi thay đổi comboBox khuyến mãi
        cbKhuyenMai.setOnAction(e -> {
            KhuyenMaiDTO km = cbKhuyenMai.getSelectionModel().getSelectedItem();
            if (km == null || km.getTenKM().equals("Không áp dụng")) {
                txtCanhBaoKM.setText("");
                loadTongTien();
                return;
            }
            LocalDate today = LocalDate.now();
            if (km.getNgayBatDau().isAfter(today)) {
                txtCanhBaoKM.setText("Khuyến mãi chưa bắt đầu");
                loadTongTien();
                return;
            }
            if (km.getNgayKetThuc().isBefore(today)) {
                txtCanhBaoKM.setText("Khuyến mãi đã hết hạn");
                loadTongTien();
                return;
            }
            // Kiểm tra số lần đã sử dụng (async)
            Task<Integer> task = new Task<>() {
                @Override
                protected Integer call() {
                    return clientManager.countHoaDonByKhuyenMai(km.getMaKM());
                }
            };
            task.setOnSucceeded(event -> {
                int soDaSuDung = task.getValue();
                if (soDaSuDung >= km.getSoLuongToiDa()) {
                    txtCanhBaoKM.setText("Khuyến mãi đã đạt số lượng tối đa");
                } else {
                    txtCanhBaoKM.setText("");
                }
                loadTongTien();
            });
            task.setOnFailed(event -> {
                txtCanhBaoKM.setText("");
                loadTongTien();
            });
            new Thread(task).start();
        });

        // setup spinner số lượng
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,
                Integer.MAX_VALUE, 1);
        spSoLuong.setValueFactory(valueFactory);
        spSoLuong.setEditable(true);

        // sự kiện khi thay đổi comboBox thuốc
        cbTenThuoc.setOnAction(e -> {

            if (cbTenThuoc.getSelectionModel().getSelectedItem() == null) {
                return;
            }

            for (DonViTinhDTO dvt : cbDonVi.getItems()) {
                if (dvt.getMaDVT() == cbTenThuoc.getSelectionModel().getSelectedItem().getMaDVTCoSo().getMaDVT()) {
                    cbDonVi.getSelectionModel().select(dvt);
                    break;
                }
            }

            txtDonGia.setText(dinhDangTien(cbTenThuoc.getSelectionModel().getSelectedItem().getGiaBan()));
        });

        // sự kiện thay đổi số điện thoại
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

        // sự kiện autocomplete khách hàng
        TuDongGoiY.goiYKhach(txtTenKhach, txtSoDienThoai, autoKhach);

        // sự kiện xóa thuốc khỏi bảng
        tbChonThuoc.setRowFactory(tv -> {
            TableRow<ChiTietPhieuDatThuocDTO> row = new TableRow<>();
            ContextMenu contextMenu = new ContextMenu();
            MenuItem deleteItem = new MenuItem("Xóa thuốc khỏi bảng");
            deleteItem.setOnAction(event -> {
                ChiTietPhieuDatThuocDTO selectedItem = row.getItem();
                obsThuoc.remove(selectedItem); // Xóa trực tiếp từ ObservableList
                loadTongTien();
            });
            contextMenu.getItems().add(deleteItem);
            // Chỉ hiển thị menu khi có dữ liệu
            row.contextMenuProperty().bind(
                    javafx.beans.binding.Bindings.when(row.emptyProperty())
                            .then((ContextMenu) null)
                            .otherwise(contextMenu));
            return row;
        });
    }

    /**
     * Duyệt thông tin thuốc trước khi thêm vào bảng
     * 
     * @return true - hợp lệ, false - không hợp lệ
     */
    private boolean approveThuoc() {
        ThuocDTO selected = cbTenThuoc.getSelectionModel().getSelectedItem();
        Integer nhap = spSoLuong.getValue();

        if (selected == null || nhap == null) return false;

        // 1. Tính tổng tồn kho - Đảm bảo biến này là biến CỤC BỘ (local)
        final int tongTonKhoThucTe = clientManager.getLoThuocList().stream()
                .filter(lot -> lot.getMaThuocDTO() != null &&
                        selected.getMaThuoc().equals(lot.getMaThuocDTO().getMaThuoc()))
                .filter(lot -> lot.getHanSuDung() != null &&
                        lot.getHanSuDung().isAfter(LocalDate.now()))
                .mapToInt(LoThuocDTO::getSoLuong)
                .sum();

        // 2. Tính số lượng đã có trong bảng
        final int daThemTrongBang = tbChonThuoc.getItems().stream()
                .filter(item -> item.getMaThuoc() != null &&
                        item.getMaThuoc().getMaThuocDTO() != null &&
                        selected.getMaThuoc().equals(item.getMaThuoc().getMaThuocDTO().getMaThuoc()))
                .mapToInt(ChiTietPhieuDatThuocDTO::getSoLuong)
                .sum();

        // DEBUG kiểm tra giá trị ngay trước khi IF
        System.out.println("DEBUG CHECK: Kho=" + tongTonKhoThucTe + ", DaThem=" + daThemTrongBang + ", Nhap=" + nhap);

        // 3. Logic so sánh - Sử dụng đúng tên biến cục bộ ở trên
        if (tongTonKhoThucTe <= 0) {
            showMess("Hết hàng", "Thuốc hiện không còn lô nào khả dụng trong kho.");
            return false;
        }

        if ((nhap + daThemTrongBang) > tongTonKhoThucTe) {
            int conLai = tongTonKhoThucTe - daThemTrongBang;
            showMess("Không đủ số lượng", "Kho chỉ còn " + conLai + " sản phẩm.");
            return false;
        }

        System.out.println("DEBUG CHECK: Thuốc hợp lệ để thêm vào bảng.");
        return true;
    }

    /**
     * Load lại bảng thuốc
     */
    private void loadTable() {
        // Không cần làm gì nếu đã dùng chung obsThuoc
    }

    /**
     * Thêm thuốc vào bảng nếu đã tồn tại thì cộng dồn số lượng, nếu chi tiết thiếu
     * thì tạo thêm 1 dòng mới
     */
    private void addThuocVaoTable() {
        ThuocDTO thuocDTO = cbTenThuoc.getSelectionModel().getSelectedItem();
        int soLuongYeuCau = spSoLuong.getValue();
        DonViTinhDTO dvt = cbDonVi.getSelectionModel().getSelectedItem();

        Task<List<LoThuocDTO>> task = new Task<>() {
            @Override
            protected List<LoThuocDTO> call() {
                return clientManager.getLoThuocFefoByThuocId(thuocDTO.getMaThuoc()).stream()
                        .filter(lot -> lot.getHanSuDung().isAfter(LocalDate.now()) && lot.getSoLuong() > 0)
                        .sorted(Comparator.comparing(LoThuocDTO::getHanSuDung))
                        .collect(Collectors.toList());
            }
        };

        task.setOnSucceeded(event -> {
            List<LoThuocDTO> dsLo = task.getValue();
            int conLai = soLuongYeuCau;

            for (LoThuocDTO lo : dsLo) {
                if (conLai <= 0) break;
                int lay = Math.min(lo.getSoLuong(), conLai);
                conLai -= lay;

                // Thay 'list' bằng 'obsThuoc'
                Optional<ChiTietPhieuDatThuocDTO> trungLo = obsThuoc.stream()
                        .filter(x -> x.getMaThuoc().getMaLoThuoc() == lo.getMaLoThuoc())
                        .findFirst();

                if (trungLo.isPresent()) {
                    trungLo.get().setSoLuong(trungLo.get().getSoLuong() + lay);
                } else {
                    // Thêm trực tiếp vào ObservableList
                    obsThuoc.add(new ChiTietPhieuDatThuocDTO(lo, lay, dvt));
                }
            }

            // Cập nhật UI
            tbChonThuoc.refresh();
            loadTongTien();
        });

        new Thread(task).start();
    }

    private void themPhieuDat() {
        NhanVienDTO nguoiDat = PhienNguoiDungDTO.getMaNV();
        if (nguoiDat == null) {
            showMess("Lỗi", "Không xác định được nhân viên.");
            return;
        }

        final String ten = txtTenKhach.getText().trim();
        final String sdt = txtSoDienThoai.getText().trim();
        final boolean isKhachMoi = isKhachHangMoi();

        // For existing customer, resolve now on FX thread
        final KhachHangDTO khachCu = isKhachMoi ? null : dsKhach.stream()
                .filter(k -> k.getSoDienThoai().equals(sdt))
                .findFirst().orElse(null);

        if (!isKhachMoi && khachCu == null) {
            showMess("Lỗi", "Không tìm thấy khách hàng.");
            return;
        }

        KhuyenMaiDTO km = cbKhuyenMai.getSelectionModel().getSelectedItem();
        if (km != null && "Không áp dụng".equals(km.getTenKM())) km = null;

        final KhuyenMaiDTO kmFinal = km;
        final String maPhieu = txtMa.getText();
        final List<ChiTietPhieuDatThuocDTO> chiTietList = new ArrayList<>(tbChonThuoc.getItems());
        final double tongTien = tinhTongTien();

        // Use AtomicReference to pass newly created KhachHangDTO from Task to setOnSucceeded
        final java.util.concurrent.atomic.AtomicReference<KhachHangDTO> khachRef = new java.util.concurrent.atomic.AtomicReference<>(khachCu);

        Task<Boolean> saveTask = new Task<>() {
            @Override
            protected Boolean call() throws Exception {
                KhachHangDTO khach;
                if (isKhachMoi) {
                    Integer maxHash = clientManager.getMaxHashKhachHang();
                    String maKhach = String.format("KH%09d", (maxHash != null ? maxHash : 0) + 1);
                    khach = new KhachHangDTO(maKhach, ten, sdt, false);
                    if (!clientManager.insertKhachHang(khach)) {
                        throw new RuntimeException("Không thể thêm khách hàng mới.");
                    }
                    khachRef.set(khach);
                } else {
                    khach = khachCu;
                }
                // Tạo phiếu
                PhieuDatThuocDTO phieu = new PhieuDatThuocDTO(
                        maPhieu, LocalDate.now(), false, nguoiDat, khach, kmFinal, tongTien);
                if (!clientManager.createPhieuDat(phieu)) {
                    throw new RuntimeException("Không thể tạo phiếu đặt.");
                }
                // Thêm chi tiết + trừ kho
                for (ChiTietPhieuDatThuocDTO ct : chiTietList) {
                    LoThuocDTO lo = ct.getMaThuoc();
                    int soLuongDat = ct.getSoLuong();
                    ChiTietPhieuDatThuocDTO ctNew = new ChiTietPhieuDatThuocDTO(
                            phieu, lo, soLuongDat, ct.getDonViTinhDTO());
                    if (!clientManager.createChiTietPhieuDat(ctNew)) {
                        throw new RuntimeException("Không thể thêm chi tiết phiếu.");
                    }
                    clientManager.updateSoLuongLoThuoc(lo.getMaLoThuoc(), -soLuongDat);
                }
                return true;
            }
        };

        saveTask.setOnSucceeded(e -> {
            if (isKhachMoi) {
                KhachHangDTO newKhach = khachRef.get();
                if (newKhach != null) {
                    dsKhach.add(newKhach);
                    autoKhach.add(newKhach);
                }
            }
            showMess("Thành công", "Tạo phiếu đặt thuốc thành công.");
            javafx.stage.Window window = this.getScene() != null ? this.getScene().getWindow() : null;
            if (window != null) window.hide();
        });

        saveTask.setOnFailed(e -> {
            Throwable ex = saveTask.getException();
            showMess("Lỗi", ex != null ? ex.getMessage() : "Không thể tạo phiếu đặt.");
        });

        new Thread(saveTask).start();
    }


    /**
     * Kiểm tra khách hàng đã tồn tại chưa
     * 
     * @return true - khách mới. false - khách đã tồn tại.
     */
    private boolean isKhachHangMoi() {
        String sdt = txtSoDienThoai.getText().trim();
        if (sdt.isEmpty())
            return true;

        for (KhachHangDTO kh : dsKhach) {
            if (kh.getSoDienThoai().equals(sdt)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Tạo mã phiếu đặt mới chưa tồn tại trong dbs
     *
     * @return String - mã phiếu đặt mới
     */
    private String getHashPD() {
        String hash = clientManager.getMaxHashPhieuDat();
        if (hash == null) {
            return "PDT001";
        } else {
            String numberPart = hash.replaceAll("[^0-9]", "");
            int soThuTu = Integer.parseInt(numberPart) + 1;
            return String.format("PDT%03d", soThuTu);
        }
    }

    /**
     * Tính tổng tiền của phiếu đặt áp dụng khuyến mãi (nếu có)
     * 
     * @return double - tổng tiền sau khi áp dụng khuyến mãi (nếu có)
     */
    public double tinhTongTien() {
        double tongTien = 0.0;
        for (ChiTietPhieuDatThuocDTO e : tbChonThuoc.getItems()) {
            tongTien += e.getSoLuong()
                    * e.getMaThuoc().getMaThuocDTO().getGiaBan()
                    * (1 - e.getMaThuoc().getMaThuocDTO().getThue());
        }
        // Áp dụng khuyến mãi nếu hợp lệ (txtCanhBaoKM rỗng = không có lỗi)
        KhuyenMaiDTO km = cbKhuyenMai.getSelectionModel().getSelectedItem();
        if (km != null && !km.getTenKM().equals("Không áp dụng") && txtCanhBaoKM.getText().isEmpty()) {
            LoaiKhuyenMaiDTO loaiKM = km.getLoaiKhuyenMaiDTO();
            if (loaiKM.getMaLKM() == 1) {
                tongTien = tongTien * (1 - km.getSo() / 100);
            } else {
                tongTien = tongTien - km.getSo();
            }
        }
        return tongTien > 0 ? tongTien : 0;
    }

    public void showMess(String tieuDe, String vanBan) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(tieuDe);
        alert.setContentText(vanBan);
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    /**
     * Cập nhật tổng tiền hiển thị trên giao diện
     */
    private void loadTongTien() {
        double tongTien = tinhTongTien();
        txtTotal.setText("Tổng tiền: " + dinhDangTien(tongTien));
        txtThue.setText("Thuế: " + dinhDangTien(tinhThue()));
    }

    private double tinhThue() {
        double thue = 0.0;
        if (tbChonThuoc.getItems().isEmpty()) {
            return thue;
        }
        for (ChiTietPhieuDatThuocDTO e : tbChonThuoc.getItems()) {
            thue += e.getSoLuong() * e.getMaThuoc().getMaThuocDTO().getGiaBan()
                    * e.getMaThuoc().getMaThuocDTO().getThue();
        }
        return thue;
    }

    private boolean checkDuLieu() {
        if (txtTenKhach.getText().trim().isEmpty()) {
            showMess("Thiếu thông tin", "Vui lòng nhập tên khách hàng.");
            txtTenKhach.requestFocus();
            return false;
        }
        if (txtSoDienThoai.getText().trim().isEmpty()) {
            showMess("Thiếu thông tin số điện thọai khách hàng", "Vui lòng nhập số điện thoại khách hàng.");
            txtSoDienThoai.requestFocus();
            return false;
        }
        if (!txtSoDienThoai.getText().matches("^0\\d{9}$")) {
            showMess("Thông tin số điện thọai khách hàng không hợp lệ",
                    "Số điện thoại khách hàng phải có 10 số và bắt đầu với 03, 05, 06, 07, 09.");
            txtSoDienThoai.requestFocus();
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
     * 
     * @param tien số tiền cần định dạng
     * @return String - tiền đã được định dạng
     */
    public String dinhDangTien(double tien) {
        DecimalFormat df = new DecimalFormat("#,###đ");
        return df.format(tien);
    }

    /**
     * Cài đặt các cột trong bảng
     */
    private void setupTable() {
        colTenThuoc.setCellValueFactory(
                cellData -> new SimpleStringProperty(cellData.getValue().getMaThuoc().getMaThuocDTO().getTenThuoc()));
        colDonVi.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().getMaThuoc().getMaThuocDTO().getMaDVTCoSo().getTenDVT()));
        colSoLuong.setCellValueFactory(
                cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getSoLuong())));
        colDonGia.setCellValueFactory(cellData -> new SimpleStringProperty(
                dinhDangTien(cellData.getValue().getMaThuoc().getMaThuocDTO().getGiaBan())));
        colThanhTien.setCellValueFactory(cellData -> {
            double thanhTien = cellData.getValue().getSoLuong()
                    * cellData.getValue().getMaThuoc().getMaThuocDTO().getGiaBan();
            return new SimpleStringProperty(dinhDangTien(thanhTien));
        });
    }
}

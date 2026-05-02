//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.antam.app.controller.phieudat;

import com.antam.app.connect.ConnectDB;
import com.antam.app.network.ClientManager;
import com.antam.app.dto.ChiTietPhieuDatThuocDTO;
import com.antam.app.dto.LoThuocDTO;
import com.antam.app.dto.NhanVienDTO;
import com.antam.app.dto.PhieuDatThuocDTO;
import com.antam.app.service.impl.ChiTietPhieuDat_Service;
import com.antam.app.service.impl.LoThuoc_Service;
import com.antam.app.service.impl.PhieuDat_Service;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcons;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.util.StringConverter;

import java.sql.Connection;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class KhoiPhucPhieuDatController extends ScrollPane{

    private Button btnKhoiPhuc;

    private ComboBox<NhanVienDTO> cbNhanVien = new ComboBox<>();

    private ComboBox<String> cbTrangThai = new ComboBox<>();
    private ComboBox<String> cbGia = new ComboBox<>();

    private DatePicker dpstart = new DatePicker();
    private DatePicker dpend = new DatePicker();

    private TextField txtFind = new TextField();

    private Button btnFind = new Button();
    private Button btnXoaRong = new Button();

    private TableView<PhieuDatThuocDTO> tvPhieuDat = new TableView<>();

    private TableColumn<PhieuDatThuocDTO,String> colMaPhieu = new TableColumn<>("Mã phiếu");
    private TableColumn<PhieuDatThuocDTO,String> colNgay = new TableColumn<>("Ngày tạo");
    private TableColumn<PhieuDatThuocDTO,String> colKhach = new TableColumn<>("Khách hàng");
    private TableColumn<PhieuDatThuocDTO,String> colSDT = new TableColumn<>("SĐT");
    private TableColumn<PhieuDatThuocDTO,String> colNhanVien = new TableColumn<>("Nhân viên");
    private TableColumn<PhieuDatThuocDTO,String> colStatus = new TableColumn<>("Trạng thái");
    private TableColumn<PhieuDatThuocDTO,String> colTotal = new TableColumn<>("Tổng tiền");

    public static PhieuDatThuocDTO selectedPDT;

//    private I_PhieuDat_Service I_PhieuDat_Service = new PhieuDat_Service();
//    private ChiTietPhieuDat_Service I_ChiTietPhieuDat_Service = new ChiTietPhieuDat_Service();
    List<PhieuDatThuocDTO> listPDT;
    public LoThuoc_Service ctThuoc_dao = new LoThuoc_Service();
//    private NhanVien_Service nhanVien_service = new NhanVien_Service();
    List<NhanVienDTO> listNV;
    ObservableList<PhieuDatThuocDTO> origin;
    ObservableList<PhieuDatThuocDTO> filter= FXCollections.observableArrayList();

    private ClientManager clientManager = ClientManager.getInstance();

    public KhoiPhucPhieuDatController() {
        this.setFitToHeight(true);
        this.setFitToWidth(true);
        this.setPrefSize(900, 730);

        AnchorPane.setTopAnchor(this, 0.0);
        AnchorPane.setBottomAnchor(this, 0.0);
        AnchorPane.setLeftAnchor(this, 0.0);
        AnchorPane.setRightAnchor(this, 0.0);

        VBox mainVBox = new VBox(30);
        mainVBox.setStyle("-fx-background-color: #f8fafc;");
        mainVBox.setPadding(new Insets(20));

        // Header
        HBox header = new HBox(5);
        header.setAlignment(Pos.CENTER_LEFT);

        Text title = new Text("Khôi phục phiếu đặt");
        title.setFill(Color.web("#1e3a8a"));
        title.setFont(Font.font("System",30));

        Pane spacer = new Pane();

        FontAwesomeIcon iconAdd = new FontAwesomeIcon();
        iconAdd.setFill(Color.WHITE);
        iconAdd.setIcon(FontAwesomeIcons.BACKWARD);
        btnKhoiPhuc = new Button("Khôi phục");
        btnKhoiPhuc.setGraphic(iconAdd);
        btnKhoiPhuc.getStyleClass().add("btn-khoiphuc");

        HBox.setHgrow(spacer, Priority.ALWAYS);
        header.getChildren().addAll(title, spacer, btnKhoiPhuc);

        FlowPane filters = new FlowPane(5, 5);
        filters.setStyle("-fx-background-color: white;");
        filters.getStyleClass().add("box-pane");
        filters.setPadding(new Insets(10));

        DropShadow ds = new DropShadow();
        ds.setBlurType(BlurType.GAUSSIAN);
        ds.setHeight(44.45);
        ds.setWidth(35.66);
        ds.setOffsetX(3);
        ds.setOffsetY(2);
        ds.setRadius(19.53);
        ds.setColor(Color.rgb(211, 211, 211));
        filters.setEffect(ds);

        filters.getChildren().add(createFilterVBox("Nhân viên:", cbNhanVien));
        filters.getChildren().add(createFilterVBox("Trạng thái:", cbTrangThai));
        filters.getChildren().add(createFilterVBox("Từ ngày:", dpstart));
        filters.getChildren().add(createFilterVBox("Đến ngày:", dpend));
        filters.getChildren().add(createFilterVBox("Khoảng giá:", cbGia));

        VBox vbReset = new VBox(5);
        Text emptyText = new Text("");
        emptyText.setFont(Font.font(13));

        btnXoaRong.setText("Xoá rỗng");
        btnXoaRong.setPrefSize(93, 40);
        btnXoaRong.setTextFill(Color.WHITE);
        btnXoaRong.getStyleClass().add("btn-xoarong");
        FontAwesomeIcon refreshIcon = new FontAwesomeIcon();
        refreshIcon.setIcon(FontAwesomeIcons.REFRESH);
        refreshIcon.setFill(Color.WHITE);
        btnXoaRong.setGraphic(refreshIcon);

        vbReset.getChildren().addAll(emptyText, btnXoaRong);
        filters.getChildren().add(vbReset);

        // --- Search box ---
        HBox searchBox = new HBox(10);
        searchBox.setAlignment(Pos.CENTER_LEFT);

        txtFind.setPromptText("Tìm kiếm mã phiếu đặt");
        txtFind.setPrefSize(300, 40);
        txtFind.setStyle("-fx-background-color: white; -fx-border-color: #e5e7eb; -fx-border-radius: 8px;");

        btnFind.setPrefSize(50, 40);
        btnFind.setStyle("-fx-background-color: #2563eb; -fx-background-radius: 5px;");
        btnFind.setTextFill(Color.WHITE);
        FontAwesomeIcon searchIcon = new FontAwesomeIcon(); searchIcon.setGlyphName("SEARCH");
        searchIcon.setFill(Color.WHITE);
        btnFind.setGraphic(searchIcon);

        searchBox.getChildren().addAll(txtFind, btnFind);

        // --- Table ---
        VBox tableWrapper = new VBox(5);
        tvPhieuDat.setPrefSize(200, 500);

        tvPhieuDat.getColumns().addAll(
                colMaPhieu, colNgay, colKhach, colSDT,
                colNhanVien, colStatus, colTotal
        );

        tvPhieuDat.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        Button hint = new Button("Nhấn 2 lần chuột trái vào bảng để xem chi tiết");
        hint.setMaxWidth(Double.MAX_VALUE);
        hint.getStyleClass().add("pane-huongdan");
        hint.setTextFill(Color.web("#2563eb"));
        hint.setPadding(new Insets(10));
        FontAwesomeIcon infoIcon = new FontAwesomeIcon(); infoIcon.setGlyphName("INFO");
        infoIcon.setFill(Color.web("#2563eb"));
        hint.setGraphic(infoIcon);

        tableWrapper.getChildren().addAll(tvPhieuDat, hint);

        // Add tất cả vào giao diện
        mainVBox.getChildren().addAll(header, filters, searchBox, tableWrapper);

        this.getStylesheets().add(getClass().getResource("/com/antam/app/styles/dashboard_style.css").toExternalForm());
        this.setContent(mainVBox);

        tvPhieuDat.setPlaceholder(new Label("Chưa có phiếu đặt thuốc nào bị hủy"));
        /** Sự kiện **/

//        List<PhieuDatThuocDTO> listPDT = I_PhieuDat_Service.getAllPhieuDatThuocFromDBS();
        Task<List<PhieuDatThuocDTO>> loadDataTask = new Task<>() {
            @Override
            protected List<PhieuDatThuocDTO> call() throws Exception {
                return clientManager.getPhieuiDatThuocDaXoa();
            }
        };

        loadDataTask.setOnSucceeded(e -> {
            listPDT = loadDataTask.getValue();
        });
        loadDataTask.setOnFailed(e -> {

        });
        Thread thread = new Thread(loadDataTask);
        thread.start();

//        List<NhanVienDTO> listNV = nhanVien_service.getAllNhanVien();
        Task<List<NhanVienDTO>> loadDataTask2 = new Task<>() {
            @Override
            protected List<NhanVienDTO> call() throws Exception {
                return clientManager.getNhanVienList();
            }
        };
        loadDataTask2.setOnSucceeded(e -> {
            listNV = loadDataTask2.getValue();
            loadDataComboBox();
        });
        loadDataTask2.setOnFailed(e -> {});
        Thread thread2 = new Thread(loadDataTask2);
        thread2.start();


        setupBang();
        loadDataVaoBang();
        setupListenerComboBox();
        setupListenerFind();
        btnXoaRong.setOnAction( e->{
            cbGia.getSelectionModel().selectFirst();
            cbNhanVien.getSelectionModel().selectFirst();
            cbTrangThai.getSelectionModel().selectFirst();
            dpstart.setValue(null);
            dpend.setValue(null);
            txtFind.setText("");
            loadDataVaoBang();
        });
        btnKhoiPhuc.setOnAction(e -> {

            PhieuDatThuocDTO selected =
                    tvPhieuDat.getSelectionModel().getSelectedItem();

            if (selected == null) {
                showMess("Chưa chọn phiếu đặt",
                        "Vui lòng chọn phiếu đặt để khôi phục");
                return;
            }

            if (!canhBao(
                    "Xác nhận khôi phục",
                    "Bạn có chắc muốn khôi phục phiếu đặt "
                            + selected.getMaPhieu() + " không?"
            )) return;

            try {
                ConnectDB.getInstance().connect();
                Connection con = ConnectDB.getConnection();
                con.setAutoCommit(false); // TRANSACTION

                PhieuDat_Service phieuDatService = new PhieuDat_Service();
                // 1. Khôi phục phiếu
                boolean kqPhieu =
                        phieuDatService.khoiPhucPhieuDat(selected.getMaPhieu());

                if (!kqPhieu) {
                    throw new RuntimeException("Không thể khôi phục phiếu đặt");
                }

                ChiTietPhieuDat_Service chiTietPhieuDatService = new ChiTietPhieuDat_Service();
                // 2. Lấy chi tiết phiếu
                List<ChiTietPhieuDatThuocDTO> chiTietList =
                        chiTietPhieuDatService.getChiTietTheoPhieu(selected.getMaPhieu());

                // 3. Trừ kho lại & khôi phục chi tiết
                for (ChiTietPhieuDatThuocDTO ct : chiTietList) {

                    LoThuocDTO ctt =
                            ctThuoc_dao.getChiTietThuoc(
                                    ct.getMaThuoc().getMaLoThuoc()
                            );

                    if (ctt.getSoLuong() < ct.getSoLuong()) {
                        throw new RuntimeException(
                                "Không đủ tồn kho để khôi phục thuốc "
                                        + ctt.getMaThuocDTO().getTenThuoc()
                        );
                    }

                    int soMoi = ctt.getSoLuong() - ct.getSoLuong();
                    ctThuoc_dao.CapNhatSoLuongChiTietThuoc(
                            ctt.getMaLoThuoc(), soMoi
                    );
                }

                // 4. Khôi phục trạng thái chi tiết
//                I_ChiTietPhieuDat_Service.khoiPhucChiTietPhieu(
//                        selected.getMaPhieu();
//                );
                Task<Boolean> khoiPhucChiTietTask = new Task<>() {
                    @Override
                    protected Boolean call() throws Exception {
                        return clientManager.khoiPhucChiTietPhieu(selected.getMaPhieu());
                    }
                };

                khoiPhucChiTietTask.setOnSucceeded(ev -> {
                    boolean kqChiTiet = khoiPhucChiTietTask.getValue();
                    if (!kqChiTiet) {
                        showMess("Lỗi",
                                "Khôi phục chi tiết phiếu đặt thất bại. Dữ liệu đã được hoàn tác.");
                        return;
                    }
                });
                khoiPhucChiTietTask.setOnFailed(ev -> {
                    showMess("Lỗi",
                            "Khôi phục chi tiết phiếu đặt thất bại. Dữ liệu đã được hoàn tác.");
                });
                Thread khoiPhucThread = new Thread(khoiPhucChiTietTask);
                khoiPhucThread.start();

                con.commit();

                showMess("Khôi phục thành công",
                        "Phiếu đặt đã được khôi phục thành công");
                loadDataVaoBang();

            } catch (Exception ex) {
                ex.printStackTrace();
                try {
                    ConnectDB.getConnection().rollback();
                } catch (Exception ignore) {}
                showMess("Lỗi",
                        "Khôi phục phiếu đặt thất bại. Dữ liệu đã được hoàn tác.");
            }
        });

    }

    private VBox createFilterVBox(String label, Control control) {
        VBox vb = new VBox(5);

        Text txt = new Text(label);
        txt.setFill(Color.web("#374151"));
        txt.setFont(Font.font(13));

        control.setPrefSize(200, 40);
        control.getStyleClass().add("combo-box");

        vb.getChildren().addAll(txt, control);
        return vb;
    }

    /**
     * Hiển thị cảnh báo xác nhận
     * @param tieuDe
     * @param noidung
     * @return
     */
    private boolean canhBao(String tieuDe, String noidung){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(tieuDe);
        alert.setHeaderText(null);
        alert.setContentText(noidung);

        ButtonType buttonTypeYes = new ButtonType("Có", ButtonBar.ButtonData.YES);
        ButtonType buttonTypeNo = new ButtonType("Không", ButtonBar.ButtonData.NO);

        alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

        return alert.showAndWait().orElse(buttonTypeNo) == buttonTypeYes;
    }

    private void showMess(String tieude, String noidung) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(tieude);
        alert.setHeaderText(null);
        alert.setContentText(noidung);
        alert.showAndWait();
    }


    private void setupListenerComboBox() {
        String gia = cbGia.getSelectionModel().getSelectedItem();
        String trangThai = cbTrangThai.getSelectionModel().getSelectedItem();
        NhanVienDTO nv = cbNhanVien.getSelectionModel().getSelectedItem();
        LocalDate start = dpstart.getValue();
        LocalDate end = dpend.getValue();

        // Nếu tất cả đều là "Tất cả" hoặc chưa chọn gì => hiển thị gốc
        if ((gia == null || gia.equals("Tất cả")) &&
                (trangThai == null || trangThai.equals("Tất cả")) &&
                (nv == null || nv.getHoTen().equals("Tất cả")) &&
                (start == null && end == null)) {
            loadDataVaoBang();
            return;
        }

        // Xác định khoảng giá
        double min, max;
        switch (gia) {
            case "Dưới 500.000đ": min = 0; max = 500000; break;
            case "Từ 500.000đ đến 1.000.000đ": min = 500000; max = 1000000; break;
            case "Từ 1.000.000đ đến 2.000.000đ": min = 1000000; max = 2000000; break;
            case "Trên 2.000.000đ": min = 2000000; max = Double.MAX_VALUE; break;
            default: min = 0; max = Double.MAX_VALUE;
        }

        // Trạng thái
        Boolean filStatus = null;
        if ("Đã thanh toán".equals(trangThai)) filStatus = true;
        else if ("Chưa thanh toán".equals(trangThai)) filStatus = false;

        // Bắt đầu lọc
        ObservableList<PhieuDatThuocDTO> filter = FXCollections.observableArrayList();

        for (PhieuDatThuocDTO e : origin) {
            boolean match = true;

            // Giá
            if (!(e.getTongTien() >= min && e.getTongTien() <= max))
                match = false;

            // Nhân viên
            if (nv != null && nv.getHoTen() != null &&
                    !nv.getHoTen().equals("Tất cả") &&
                    !e.getNhanVienDTO().getMaNV().equals(nv.getMaNV()))
                match = false;

            // Trạng thái
            if (filStatus != null && e.isThanhToan() != filStatus)
                match = false;

            // Ngày
            if (start != null && e.getNgayTao().isBefore(start))
                match = false;
            if (end != null && e.getNgayTao().isAfter(end))
                match = false;

            if (match) filter.add(e);
        }

        tvPhieuDat.setItems(filter);
    }


    private void setupListenerFind() {
        txtFind.textProperty().addListener((obs, oldT, newT) -> {

            tvPhieuDat.getSelectionModel().clearSelection();

            String key = newT.trim().toLowerCase();

            if (key.isEmpty()) {
                tvPhieuDat.setItems(origin);
                return;
            }

            ObservableList<PhieuDatThuocDTO> filtered =
                    origin.filtered(p ->
                            p.getMaPhieu().toLowerCase().contains(key)
                                    || p.getKhachHang().getTenKH().toLowerCase().contains(key)
                    );

            tvPhieuDat.setItems(filtered);
        });
    }


    private void setupBang() {
        colMaPhieu.setCellValueFactory(t -> new SimpleStringProperty(t.getValue().getMaPhieu()));
        colNgay.setCellValueFactory(t -> new SimpleStringProperty(t.getValue().getNgayTao().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));
        colKhach.setCellValueFactory(t -> new SimpleStringProperty(t.getValue().getKhachHang().getTenKH()));
        colSDT.setCellValueFactory(t -> new SimpleStringProperty(t.getValue().getKhachHang().getSoDienThoai()));
        colNhanVien.setCellValueFactory(t -> new SimpleStringProperty(t.getValue().getNhanVienDTO().getHoTen()));
        colStatus.setCellValueFactory(t -> new SimpleStringProperty("Đã xóa"));
        colTotal.setCellValueFactory(t -> new SimpleStringProperty( dinhDangTien(t.getValue().getTongTien()) ));
    }
    private String dinhDangTien(double tien){
        DecimalFormat df = new DecimalFormat("#,### đ");
        return df.format(tien);
    }

    private void loadDataVaoBang() {
        Task<List<PhieuDatThuocDTO>> loadDataTask = new Task<>() {
            @Override
            protected List<PhieuDatThuocDTO> call() throws Exception {
                return clientManager.getPhieuiDatThuocDaXoa();
            }
        };

        loadDataTask.setOnSucceeded(e -> {
            listPDT = loadDataTask.getValue();
            origin = FXCollections.observableArrayList(listPDT);
            filter = FXCollections.observableArrayList(origin);
            tvPhieuDat.setItems(filter);
            tvPhieuDat.refresh();
        });
        loadDataTask.setOnFailed(e -> {

        });
        Thread thread = new Thread(loadDataTask);
        thread.start();
    }


    public void loadDataComboBox(){
        NhanVienDTO all = new NhanVienDTO("Tất cả",false);
        cbNhanVien.getItems().add(all);
        for (NhanVienDTO e : listNV){
            cbNhanVien.getItems().add(e);
        }
        cbTrangThai.getItems().add("Tất cả");
        cbTrangThai.getItems().add("Chưa thanh toán");
        cbTrangThai.getItems().add("Đã thanh toán");
        cbGia.getItems().add("Tất cả");
        cbGia.getItems().add("Dưới 500.000đ");
        cbGia.getItems().add("Từ 500.000đ đến 1.000.000đ");
        cbGia.getItems().add("Từ 1.000.000đ đến 2.000.000đ");
        cbGia.getItems().add("Trên 2.000.000đ");
        cbGia.getSelectionModel().selectFirst();
        cbNhanVien.getSelectionModel().selectFirst();
        cbTrangThai.getSelectionModel().selectFirst();

        // Set converter to display only the name
        cbNhanVien.setConverter(new StringConverter<NhanVienDTO>() {
            @Override
            public String toString(NhanVienDTO nv) {
                return nv == null ? "" : nv.getHoTen();
            }

            @Override
            public NhanVienDTO fromString(String string) {
                return null; // Not needed
            }
        });
    }
}

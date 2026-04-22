//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.antam.app.controller.khuyenmai;

import com.antam.app.connect.ConnectDB;
import com.antam.app.network.ClientManager;
import com.antam.app.dto.KhuyenMaiDTO;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcons;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.ArrayList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class TimKhuyenMaiController extends ScrollPane{
    
    private ComboBox<String> cbLoaiKhuyenMai, cbTrangThai;
    private DatePicker dpTuNgay, dpDenNgay;
    private TextField txtTiemKiemKhuyenMai;
    private Button btnTimKiem, btnclear;
    private TableView<KhuyenMaiDTO> tableKhuyenMai;
    
    private TableColumn<KhuyenMaiDTO, String> colMaKhuyenMai, colTenKhuyenMai, colLoaiKhuyenMai, colSo, colSoLuongToiDa, colTinhTrang;
    private final ClientManager clientManager;
    private ObservableList<KhuyenMaiDTO> khuyenMaiList = FXCollections.observableArrayList();
    private ArrayList<KhuyenMaiDTO> arrayKhuyenMai = new ArrayList<>();
    public TimKhuyenMaiController() {
        this.clientManager = ClientManager.getInstance();
        /** Giao diện **/
        this.setFitToHeight(true);
        this.setFitToWidth(true);
        this.setPrefSize(900, 730);
        AnchorPane.setTopAnchor(this, 0.0);
        AnchorPane.setBottomAnchor(this, 0.0);
        AnchorPane.setLeftAnchor(this, 0.0);
        AnchorPane.setRightAnchor(this, 0.0);

        VBox root = new VBox(30);
        root.setStyle("-fx-background-color: #f8fafc;");
        root.setPadding(new Insets(20));

        // ================= Tiêu đề ===================
        HBox titleBox = new HBox(5);
        titleBox.setAlignment(Pos.CENTER_LEFT);

        Text title = new Text("Tìm khuyến mãi");
        title.setFont(Font.font("System", 30));
        title.setFill(Color.web("#1e3a8a"));

        Pane spacer = new Pane();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        titleBox.getChildren().addAll(title, spacer);

        // ================= Form điều kiện ===================
        FlowPane flow = new FlowPane(5, 5);
        flow.getStyleClass().add("box-pane");
        flow.setPadding(new Insets(10));
        flow.setEffect(new DropShadow(19, 3, 2, Color.rgb(211, 211, 211)));

        // ------------ Loại khuyến mãi ------------
        cbLoaiKhuyenMai = new ComboBox<>();
        cbLoaiKhuyenMai.setPrefSize(200, 40);
        VBox boxLoai = createLabeledField("Loại khuyến mãi:", cbLoaiKhuyenMai);

        // ------------ Trạng thái ------------
        cbTrangThai = new ComboBox<>();
        cbTrangThai.setPrefSize(200, 40);
        VBox boxTrangThai = createLabeledField("Trạng thái:", cbTrangThai);

        // ------------ Từ ngày ------------
        dpTuNgay = new DatePicker();
        dpTuNgay.setPrefSize(200, 40);
        VBox boxTuNgay = createLabeledField("Từ ngày:", dpTuNgay);

        // ------------ Đến ngày ------------
        dpDenNgay = new DatePicker();
        dpDenNgay.setPrefSize(200, 40);
        VBox boxDenNgay = createLabeledField("Đến ngày:", dpDenNgay);

        // --- Button Xóa rỗng ---
        btnclear = new Button("Xoá rỗng");
        btnclear.setPrefSize(93, 40);
        btnclear.getStyleClass().add("btn-xoarong");
        btnclear.setTextFill(Color.WHITE);

        FontAwesomeIcon ref = new FontAwesomeIcon();
        ref.setGlyphName("REFRESH");
        ref.setFill(Color.WHITE);
        btnclear.setGraphic(ref);

        VBox boxXoa = new VBox();
        boxXoa.setAlignment(Pos.BOTTOM_CENTER);
        boxXoa.getChildren().add(btnclear);

        flow.getChildren().addAll(boxLoai, boxTrangThai, boxTuNgay, boxDenNgay, boxXoa);

        // ================= Thanh tìm kiếm ===================
        HBox searchBox = new HBox(10);
        searchBox.setAlignment(Pos.CENTER_LEFT);

        txtTiemKiemKhuyenMai = new TextField();
        txtTiemKiemKhuyenMai.setPrefSize(300, 40);
        txtTiemKiemKhuyenMai.setPromptText("Tìm kiếm khuyến mãi...");
        txtTiemKiemKhuyenMai.setStyle("-fx-background-color: white; -fx-border-color: #e5e7eb; -fx-border-radius: 8px;");

        btnTimKiem = new Button();
        btnTimKiem.setPrefSize(50, 40);
        btnTimKiem.setStyle("-fx-background-color: #2563eb; -fx-background-radius: 5px;");
        btnTimKiem.setTextFill(Color.WHITE);

        FontAwesomeIcon searchIcon = new FontAwesomeIcon();
        searchIcon.setIcon(FontAwesomeIcons.SEARCH);
        searchIcon.setFill(Color.WHITE);
        btnTimKiem.setGraphic(searchIcon);

        searchBox.getChildren().addAll(txtTiemKiemKhuyenMai, btnTimKiem);

        // ================= Bảng dữ liệu ===================
        tableKhuyenMai = new TableView<>();
        tableKhuyenMai.setPrefHeight(800);

        colMaKhuyenMai = new TableColumn<>("Mã khuyến mãi");
        colTenKhuyenMai = new TableColumn<>("Tên khuyến mãi");
        colLoaiKhuyenMai = new TableColumn<>("Loại");
        colSo = new TableColumn<>("Số (Giá trị)");
        colSoLuongToiDa = new TableColumn<>("Số lượng tối đa");
        colTinhTrang = new TableColumn<>("Trạng thái");

        tableKhuyenMai.getColumns().addAll(
                colMaKhuyenMai, colTenKhuyenMai, colLoaiKhuyenMai,
                colSo, colSoLuongToiDa, colTinhTrang
        );

        tableKhuyenMai.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableKhuyenMai.setPlaceholder(new Label("Không có khuyến mãi"));

        // ================= Add vào root ===================
        root.getChildren().addAll(titleBox, flow, searchBox, tableKhuyenMai);

        this.setContent(root);
        /** Sự kiện **/
        // cau hinh table
        colMaKhuyenMai.setCellValueFactory(c -> new SimpleStringProperty(safeText(c.getValue().getMaKM())));
        colTenKhuyenMai.setCellValueFactory(c -> new SimpleStringProperty(safeText(c.getValue().getTenKM())));
        colLoaiKhuyenMai.setCellValueFactory(celldata -> {
            KhuyenMaiDTO km = celldata.getValue();
            if (km.getLoaiKhuyenMaiDTO() != null) {
                return new SimpleStringProperty(km.getLoaiKhuyenMaiDTO().getTenLKM());
            } else {
                return new SimpleStringProperty("Không xác định");
            }
        });
        colSo.setCellValueFactory(c -> new SimpleStringProperty(String.valueOf(c.getValue().getSo())));
        colSoLuongToiDa.setCellValueFactory(c -> new SimpleStringProperty(String.valueOf(c.getValue().getSoLuongToiDa())));
        colTinhTrang.setCellValueFactory(cellData -> {
            KhuyenMaiDTO km = cellData.getValue();
            if (LocalDate.now().isBefore(km.getNgayBatDau())) {
                return new SimpleStringProperty("Chưa bắt đầu");
            } else if (LocalDate.now().isAfter(km.getNgayKetThuc())) {
                return new SimpleStringProperty("Đã kết thúc");
            } else {
                return new SimpleStringProperty("Đang diễn ra");
            }
        });
        // load du lieu
        arrayKhuyenMai = new ArrayList<>(clientManager.getKhuyenMaiList());
        khuyenMaiList.setAll(arrayKhuyenMai);
        tableKhuyenMai.setItems(khuyenMaiList);
        // them combobox
        addCombobox();
        // su kien loc va tim kiem
        btnTimKiem.setOnAction(e -> fiterAndSearch());
        cbLoaiKhuyenMai.setOnAction(e -> fiterAndSearch());
        cbTrangThai.setOnAction(e -> fiterAndSearch());
        dpTuNgay.setOnAction(e -> fiterAndSearch());
        dpDenNgay.setOnAction(e -> fiterAndSearch());
        txtTiemKiemKhuyenMai.setOnKeyReleased(e -> fiterAndSearch());
        btnclear.setOnAction(e -> clearFilters());
    }

    private VBox createLabeledField(String label, Control field) {
        VBox box = new VBox(5);

        Text lbl = new Text(label);
        lbl.setFill(Color.web("#374151"));
        lbl.setFont(Font.font(13));

        box.getChildren().addAll(lbl, field);
        return box;
    }

    public void addCombobox() {
        cbLoaiKhuyenMai.getItems().addAll("Tất cả", "Giảm theo phần trăm", "Giảm theo số tiền");
        cbLoaiKhuyenMai.getSelectionModel().selectFirst();

        cbTrangThai.getItems().addAll("Tất cả", "Chưa bắt đầu", "Đang diễn ra", "Đã kết thúc");
        cbTrangThai.getSelectionModel().selectFirst();
    }

    public void fiterAndSearch() {
        String loaiKhuyenMai = cbLoaiKhuyenMai.getValue();
        String trangThai = cbTrangThai.getValue();
        LocalDate tuNgay = dpTuNgay.getValue();
        LocalDate denNgay = dpDenNgay.getValue();
        String tuKhoa = txtTiemKiemKhuyenMai.getText().trim().toLowerCase();

        if (tuNgay != null && denNgay != null && denNgay.isBefore(tuNgay)) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Lỗi nhập ngày");
            alert.setHeaderText(null);
            alert.setContentText("Ngày kết thúc không được nhỏ hơn ngày bắt đầu!");
            alert.showAndWait();

            khuyenMaiList.clear();
            tableKhuyenMai.setItems(khuyenMaiList);
            return;
        }

        ObservableList<KhuyenMaiDTO> filteredList = FXCollections.observableArrayList();

        for (KhuyenMaiDTO km : arrayKhuyenMai) {
            boolean matchesLoai =
                    loaiKhuyenMai.equals("Tất cả") ||
                            (loaiKhuyenMai.equals("Giảm theo phần trăm") && safeTenLoai(km).equals("Giảm theo phần trăm")) ||
                            (loaiKhuyenMai.equals("Giảm theo số tiền") && safeTenLoai(km).equals("Giảm theo số tiền"));

            LocalDate today = LocalDate.now();
            boolean matchesTrangThai =
                    trangThai.equals("Tất cả") ||
                            (trangThai.equals("Chưa bắt đầu") && today.isBefore(km.getNgayBatDau())) ||
                            (trangThai.equals("Đang diễn ra") && !today.isBefore(km.getNgayBatDau()) && !today.isAfter(km.getNgayKetThuc())) ||
                            (trangThai.equals("Đã kết thúc") && today.isAfter(km.getNgayKetThuc()));

            boolean matchesNgay = true;
            if (tuNgay != null && denNgay != null) {
                matchesNgay = !(km.getNgayKetThuc().isBefore(tuNgay) || km.getNgayBatDau().isAfter(denNgay));
            } else if (tuNgay != null) {
                matchesNgay = !km.getNgayKetThuc().isBefore(tuNgay);
            } else if (denNgay != null) {
                matchesNgay = !km.getNgayBatDau().isAfter(denNgay);
            }

            boolean matchesTuKhoa =
                    tuKhoa.isEmpty() ||
                            km.getMaKM().toLowerCase().contains(tuKhoa) ||
                            km.getTenKM().toLowerCase().contains(tuKhoa);

            if (matchesLoai && matchesTrangThai && matchesNgay && matchesTuKhoa) {
                filteredList.add(km);
            }
        }

        khuyenMaiList.setAll(filteredList);
        tableKhuyenMai.setItems(khuyenMaiList);
    }

    public void clearFilters() {
        cbLoaiKhuyenMai.getSelectionModel().selectFirst();
        cbTrangThai.getSelectionModel().selectFirst();
        dpTuNgay.setValue(null);
        dpDenNgay.setValue(null);
        txtTiemKiemKhuyenMai.clear();
        updateTableKhuyenMai();
    }

    public void updateTableKhuyenMai(){
        khuyenMaiList.clear();
        tableKhuyenMai.refresh();
        arrayKhuyenMai = new ArrayList<>(clientManager.getKhuyenMaiList());
        khuyenMaiList.addAll(arrayKhuyenMai);
        tableKhuyenMai.setItems(khuyenMaiList);
    }

    private String safeText(String value) { return value == null ? "" : value; }

    private String safeTenLoai(KhuyenMaiDTO km) { return km.getLoaiKhuyenMaiDTO() == null || km.getLoaiKhuyenMaiDTO().getTenLKM() == null ? "" : km.getLoaiKhuyenMaiDTO().getTenLKM(); }
}

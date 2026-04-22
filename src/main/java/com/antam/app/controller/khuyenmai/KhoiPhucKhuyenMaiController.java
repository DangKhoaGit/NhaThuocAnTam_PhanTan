//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.antam.app.controller.khuyenmai;

import com.antam.app.connect.ConnectDB;
import com.antam.app.network.ClientManager;
import com.antam.app.dto.LoaiKhuyenMaiDTO;
import com.antam.app.service.impl.KhuyenMai_Service;
import com.antam.app.dto.KhuyenMaiDTO;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcons;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;

import java.time.LocalDate;
import java.util.ArrayList;

public class KhoiPhucKhuyenMaiController extends ScrollPane{

    private Button btnKhoiPhuc, btnclear;
    private ComboBox<String> cbLoaiKhuyenMai, cbTrangThai;
    private DatePicker dpTuNgay, dpDenNgay;
    private TextField txtTiemKiemKhuyenMai;
    private Button btnTimKiem;
    private TableView<KhuyenMaiDTO> tableKhuyenMai;

    private TableColumn<KhuyenMaiDTO, String> colMaKhuyenMai, colTenKhuyenMai, colLoaiKhuyenMai, colSo, colSoLuongToiDa, colTinhTrang;
    private final ClientManager clientManager;
    private ObservableList<KhuyenMaiDTO> khuyenMaiList = FXCollections.observableArrayList();
    private ArrayList<KhuyenMaiDTO> arrayKhuyenMai = new ArrayList<>();
    private KhuyenMai_Service khuyenMai_dao = new KhuyenMai_Service();
    public KhoiPhucKhuyenMaiController() {
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

        // ========================= TIÊU ĐỀ =========================
        HBox titleBox = new HBox(5);
        titleBox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

        Text title = new Text("Khôi phục khuyến mãi");
        title.setFont(Font.font("System Bold", 30));
        title.setFill(Color.web("#1e3a8a"));

        Pane space = new Pane();
        HBox.setHgrow(space, Priority.ALWAYS);

        btnKhoiPhuc = new Button("Khôi phục");
        btnKhoiPhuc.getStyleClass().add("btn-khoiphuc");
        btnKhoiPhuc.setTextFill(Color.WHITE);
        FontAwesomeIcon iconRestore = new FontAwesomeIcon();
        iconRestore.setIcon(FontAwesomeIcons.BACKWARD);
        iconRestore.setFill(Color.WHITE);
        btnKhoiPhuc.setGraphic(iconRestore);

        titleBox.getChildren().addAll(title, space, btnKhoiPhuc);

        FlowPane filterPane = new FlowPane(5, 5);
        filterPane.getStyleClass().add("box-pane");
        filterPane.setPadding(new Insets(10));
        filterPane.setStyle("-fx-background-color: white;");

        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.web("#d3d3d3"));
        shadow.setOffsetX(3);
        shadow.setOffsetY(2);
        filterPane.setEffect(shadow);

        cbLoaiKhuyenMai = new ComboBox<>();
        cbTrangThai = new ComboBox<>();
        dpTuNgay = new DatePicker();
        dpDenNgay = new DatePicker();

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
        boxXoa.setAlignment(javafx.geometry.Pos.BOTTOM_CENTER);
        boxXoa.getChildren().add(btnclear);

        filterPane.getChildren().addAll(
                createBox("Loại khuyến mãi:", cbLoaiKhuyenMai),
                createBox("Trạng thái:", cbTrangThai),
                createBox("Từ ngày:", dpTuNgay),
                createBox("Đến ngày:", dpDenNgay),
                boxXoa
        );

        // ========================= TÌM KIẾM =========================
        HBox searchBox = new HBox(10);
        searchBox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

        txtTiemKiemKhuyenMai = new TextField();
        txtTiemKiemKhuyenMai.setPromptText("Tìm kiếm khuyến mãi...");
        txtTiemKiemKhuyenMai.setPrefSize(300, 40);
        txtTiemKiemKhuyenMai.setStyle("-fx-background-color: white; -fx-border-color: #e5e7eb; -fx-border-radius: 8px;");

        btnTimKiem = new Button();
        btnTimKiem.setPrefSize(50, 40);
        btnTimKiem.setStyle("-fx-background-color: #2563eb; -fx-background-radius: 5px;");
        btnTimKiem.setTextFill(Color.WHITE);

        FontAwesomeIcon iconSearch = new FontAwesomeIcon();
        iconSearch.setIcon(FontAwesomeIcons.SEARCH);
        iconSearch.setFill(Color.WHITE);
        btnTimKiem.setGraphic(iconSearch);

        searchBox.getChildren().addAll(txtTiemKiemKhuyenMai, btnTimKiem);

        // ========================= TABLE =========================
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
        tableKhuyenMai.setPlaceholder(new Label("Không có khuyến mãi đã xoá"));

        // ========================= ADD TO ROOT =========================
        root.getChildren().addAll(titleBox, filterPane, searchBox, tableKhuyenMai);

        this.setContent(root);
        /** Sự kiện **/
        try {
            ConnectDB.getInstance().connect();
        }catch (Exception e) {
            throw new RuntimeException(e);
        }

        // nut them khuyen mai
        this.btnKhoiPhuc.setOnAction((e) -> {
            KhuyenMaiDTO selectedKM = tableKhuyenMai.getSelectionModel().getSelectedItem();
            if (selectedKM != null) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Xác nhận khôi phục");
                alert.setHeaderText(null);
                alert.setContentText("Bạn có chắc chắn muốn khôi phục khuyến mãi " + selectedKM.getMaKM() + " không?");
                alert.initModality(Modality.APPLICATION_MODAL);

                alert.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        boolean success = clientManager.restoreKhuyenMai(selectedKM.getMaKM());
                        if (success) {
                            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                            successAlert.setTitle("Thành công");
                            successAlert.setHeaderText(null);
                            successAlert.setContentText("Khôi phục khuyến mãi thành công!");
                            successAlert.showAndWait();
                            updateTableKhuyenMai();
                        } else {
                            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                            errorAlert.setTitle("Lỗi");
                            errorAlert.setHeaderText(null);
                            errorAlert.setContentText("Khôi phục khuyến mãi thất bại!");
                            errorAlert.showAndWait();
                        }
                    }
                });
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Chưa chọn khuyến mãi");
                alert.setHeaderText(null);
                alert.setContentText("Vui lòng chọn khuyến mãi cần khôi phục!");
                alert.showAndWait();
            }
        });

        // cau hinh table
        colMaKhuyenMai.setCellValueFactory(c -> new SimpleStringProperty(safeText(c.getValue().getMaKM())));
        colTenKhuyenMai.setCellValueFactory(c -> new SimpleStringProperty(safeText(c.getValue().getTenKM())));
        colLoaiKhuyenMai.setCellValueFactory(celldata -> {
            KhuyenMaiDTO km = celldata.getValue();
            return new SimpleStringProperty(safeTenLoai(km).isEmpty() ? "Không xác định" : safeTenLoai(km));
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
        arrayKhuyenMai = new ArrayList<>(clientManager.getKhuyenMaiDeletedList());
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
        // su kien xoa rong
        btnclear.setOnAction(e -> clearFilters());

    }

    private VBox createBox(String title, Control field) {
        VBox box = new VBox(5);

        Text label = new Text(title);
        label.setFill(Color.web("#374151"));
        label.setFont(Font.font(13));

        field.setPrefSize(200, 40);

        box.getChildren().addAll(label, field);
        return box;
    }
    public void addCombobox() {
        cbLoaiKhuyenMai.getItems().clear();
        cbLoaiKhuyenMai.getItems().add("Tất cả");

        for (LoaiKhuyenMaiDTO loaiKhuyenMai : clientManager.getLoaiKhuyenMaiList()) {
            String tenLoai = safeText(loaiKhuyenMai.getTenLKM());
            if (!tenLoai.isBlank() && !cbLoaiKhuyenMai.getItems().contains(tenLoai)) {
                cbLoaiKhuyenMai.getItems().add(tenLoai);
            }
        }
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
            boolean matchesLoai = loaiKhuyenMai.equals("Tất cả") || safeTenLoai(km).equals(loaiKhuyenMai);

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
        arrayKhuyenMai = new ArrayList<>(clientManager.getKhuyenMaiDeletedList());
        khuyenMaiList.addAll(arrayKhuyenMai);
        tableKhuyenMai.setItems(khuyenMaiList);
    }

    private String safeText(String value) { return value == null ? "" : value; }

    private String safeTenLoai(KhuyenMaiDTO km) { return km.getLoaiKhuyenMaiDTO() == null || km.getLoaiKhuyenMaiDTO().getTenLKM() == null ? "" : km.getLoaiKhuyenMaiDTO().getTenLKM(); }
 }

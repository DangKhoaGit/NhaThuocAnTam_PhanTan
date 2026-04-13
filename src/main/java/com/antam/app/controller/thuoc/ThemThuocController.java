package com.antam.app.controller.thuoc;

import com.antam.app.connect.ConnectDB;
import com.antam.app.dao.impl.LoThuoc_DAO;
import com.antam.app.dao.impl.DangDieuChe_DAO;
import com.antam.app.dao.impl.Ke_DAO;
import com.antam.app.dao.impl.Thuoc_DAO;
import com.antam.app.entity.LoThuoc;
import com.antam.app.entity.DangDieuChe;
import com.antam.app.entity.Ke;
import com.antam.app.entity.Thuoc;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcons;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class ThemThuocController extends ScrollPane{

    private Ke_DAO ke_dao;
    private DangDieuChe_DAO ddc_dao;
    private Thuoc_DAO thuoc_dao;
    private LoThuoc_DAO chiTietThuoc_dao;
    private HashMap<String, Integer> mapTonKho = new HashMap<>();

    private Button btnAddMedicine;
    private ComboBox<Ke> cbKe;
    private ComboBox<DangDieuChe> cbDangDieuChe;
    private ComboBox<String>cbTonKho;
    private TextField searchNameThuoc;
    private Button btnSearchThuoc;
    private TableView<Thuoc> tableThuoc;
    private TableColumn<Thuoc, String> colMaThuoc, colTenThuoc, colHamLuong, colDangDieuChe, colGiaBan, colKe;
    private TableColumn<Thuoc, String> colTonKho;
    private Button btnSearchInvoice1;

    private ObservableList<Thuoc> thuocList = FXCollections.observableArrayList();
    private ArrayList<Thuoc> arrayThuoc = new ArrayList<>();

    public ThemThuocController(){
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

        // ===================== TIÊU ĐỀ =====================
        HBox titleBox = new HBox();
        titleBox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

        Text title = new Text("Thêm thuốc");
        title.setFont(Font.font("System Bold", 30));
        title.setFill(Color.web("#1e3a8a"));

        Pane spacer = new Pane();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        FontAwesomeIcon iconAdd = new FontAwesomeIcon();
        iconAdd.setFill(Color.WHITE);
        iconAdd.setIcon(FontAwesomeIcons.valueOf("PLUS"));
        btnAddMedicine = new Button("Thêm thuốc");
        btnAddMedicine.setGraphic(iconAdd);
        btnAddMedicine.getStyleClass().add("btn-them");

        titleBox.getChildren().addAll(title, spacer, btnAddMedicine);

        // ===================== BỘ LỌC =====================
        FlowPane filterPane = new FlowPane(5, 5);
        filterPane.getStyleClass().add("box-pane");

        filterPane.setPadding(new Insets(10));

        DropShadow ds = new DropShadow();
        ds.setBlurType(javafx.scene.effect.BlurType.GAUSSIAN);
        ds.setOffsetX(3);
        ds.setOffsetY(2);
        ds.setRadius(19.5);
        ds.setColor(Color.rgb(211, 211, 211));
        filterPane.setEffect(ds);

        // --- Kệ thuốc ---
        cbKe = new ComboBox<>();
        cbKe.setPrefSize(200, 40);
        cbKe.setPromptText("Chọn kệ thuốc");
        cbKe.getStyleClass().add("combo-box");

        VBox boxKe = createFilterBox("Kệ thuốc:", cbKe);

        // --- Dạng điều chế ---
        cbDangDieuChe = new ComboBox<>();
        cbDangDieuChe.setPrefSize(200, 40);
        cbDangDieuChe.setPromptText("Chọn dạng điều chế:");
        cbDangDieuChe.getStyleClass().add("combo-box");

        VBox boxDang = createFilterBox("Dạng điều chế:", cbDangDieuChe);

        // --- Tồn kho ---
        cbTonKho = new ComboBox<>();
        cbTonKho.setPrefSize(200, 40);
        cbTonKho.setPromptText("Chọn tình trạng tồn kho:");
        cbTonKho.getStyleClass().add("combo-box");

        VBox boxTonKho = createFilterBox("Tình trạng tồn kho::", cbTonKho);

        // --- Button Xóa rỗng ---
        btnSearchInvoice1 = new Button("Xoá rỗng");
        btnSearchInvoice1.setPrefSize(93, 40);
        btnSearchInvoice1.getStyleClass().add("btn-xoarong");
        btnSearchInvoice1.setTextFill(Color.WHITE);

        FontAwesomeIcon ref = new FontAwesomeIcon();
        ref.setGlyphName("REFRESH");
        ref.setFill(Color.WHITE);
        btnSearchInvoice1.setGraphic(ref);

        VBox boxXoa = new VBox(5);
        boxXoa.getChildren().addAll(new Text(""), btnSearchInvoice1);

        filterPane.getChildren().addAll(boxKe, boxDang, boxTonKho, boxXoa);

        // ===================== Ô TÌM KIẾM =====================
        HBox searchBox = new HBox(10);
        searchBox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

        searchNameThuoc = new TextField();
        searchNameThuoc.setPrefSize(300, 40);
        searchNameThuoc.setPromptText("Tìm kiếm thuốc...");
        searchNameThuoc.setStyle("-fx-background-color: white; -fx-border-color: #e5e7eb; -fx-border-radius: 8px;");

        btnSearchThuoc = new Button();
        btnSearchThuoc.setPrefSize(50, 40);
        btnSearchThuoc.setStyle("-fx-background-color: #2563eb; -fx-background-radius: 5px;");
        btnSearchThuoc.setTextFill(Color.WHITE);

        FontAwesomeIcon iconSearch = new FontAwesomeIcon();
        iconSearch.setGlyphName("SEARCH");
        iconSearch.setFill(Color.WHITE);
        btnSearchThuoc.setGraphic(iconSearch);

        searchBox.getChildren().addAll(searchNameThuoc, btnSearchThuoc);

        // ===================== TABLE =====================
        tableThuoc = new TableView<>();
        tableThuoc.setPrefHeight(800);

        colMaThuoc = new TableColumn<>("Mã Thuốc");
        colTenThuoc = new TableColumn<>("Tên thuốc");
        colHamLuong = new TableColumn<>("Hàm lượng");
        colDangDieuChe = new TableColumn<>("Dạng điều chế");
        colGiaBan = new TableColumn<>("Giá bán");
        colTonKho = new TableColumn<>("Tồn kho");
        colKe = new TableColumn<>("Kệ thuốc");

        tableThuoc.getColumns().addAll(
                colMaThuoc, colTenThuoc, colHamLuong, colDangDieuChe, colGiaBan, colTonKho, colKe
        );
        tableThuoc.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableThuoc.setPlaceholder(new Label("Không có thuốc nào trong kho"));

        Button guide = new Button("Nhấn 2 lần chuột trái vào bảng để xem chi tiết");
        guide.getStyleClass().add("pane-huongdan");
        guide.setMaxWidth(Double.MAX_VALUE);
        guide.setPadding(new Insets(10));
        FontAwesomeIcon infoIcon = new FontAwesomeIcon(); infoIcon.setGlyphName("INFO"); infoIcon.setFill(Color.web("#2563eb"));
        guide.setGraphic(infoIcon);

        // ===================== ADD CONTAINER =====================
        root.getChildren().addAll(titleBox, filterPane, searchBox, tableThuoc, guide);

        this.getStylesheets().add(getClass().getResource("/com/antam/app/styles/dashboard_style.css").toExternalForm());
        this.setContent(root);
        /** Sự kiện **/
        // Nút thêm thuốc
        btnAddMedicine.setOnAction(e -> {
            ThemThuocFormController themDialog = new ThemThuocFormController();

            Dialog<Void> dialog = new Dialog<>();
            dialog.setDialogPane(themDialog);
            dialog.setTitle("Thêm thuốc");
            dialog.initModality(Modality.APPLICATION_MODAL);

            dialog.showAndWait();
            updateTableThuoc();
            loadTonKho();
        });

        // Load tồn kho
        loadTonKho();

        // Kết nối DB
        try { Connection con = ConnectDB.getInstance().connect(); }
        catch (SQLException e) { throw new RuntimeException(e); }

        // Cấu hình Table
        colMaThuoc.setCellValueFactory(new PropertyValueFactory<>("maThuoc"));
        colTenThuoc.setCellValueFactory(new PropertyValueFactory<>("tenThuoc"));
        colHamLuong.setCellValueFactory(new PropertyValueFactory<>("hamLuong"));
        colGiaBan.setCellValueFactory(new PropertyValueFactory<>("giaBan"));
        colTonKho.setCellValueFactory(data -> {
            int tonKho = TinhTonKho(data.getValue());
            return new SimpleStringProperty(String.valueOf(tonKho));
        });

        colDangDieuChe.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDangDieuChe().getTenDDC()));
        colKe.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getMaKe().getTenKe()));

        // Load comboBox
        addComBoBoxKe();
        addComBoBoxDDC();
        addComboboxTonKho();

        // Load dữ liệu
        thuoc_dao = new Thuoc_DAO();
        arrayThuoc = thuoc_dao.getAllThuoc();
        thuocList.addAll(arrayThuoc);
        tableThuoc.setItems(thuocList);

        // Event lọc
        cbKe.setOnAction(e -> filterAndSearchThuoc());
        cbDangDieuChe.setOnAction(e -> filterAndSearchThuoc());
        cbTonKho.setOnAction(e -> filterAndSearchThuoc());

        // Event tìm kiếm
        btnSearchThuoc.setOnAction(e -> filterAndSearchThuoc());
        searchNameThuoc.setOnKeyReleased(e -> filterAndSearchThuoc());
        // su kien table view
        tableThuoc.setRowFactory(tv -> {
            TableRow<Thuoc> row = new TableRow<>();

            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    Thuoc selectedThuoc = row.getItem();

                    XemChiTietThuocFormController xemDialog = new XemChiTietThuocFormController();
                    xemDialog.setThuoc(selectedThuoc);
                    xemDialog.showData();

                    Dialog<Void> dialog = new Dialog<>();
                    dialog.setDialogPane(xemDialog);
                    dialog.setTitle("Chi tiết thuốc");
                    dialog.initModality(Modality.APPLICATION_MODAL);
                    dialog.showAndWait();
                }
            });

            return row;
        });


        btnSearchInvoice1.setOnAction(e -> clearSearchAndFilter());
    }

    private VBox createFilterBox(String label, ComboBox<?> cb) {
        Text t = new Text(label);
        t.setFont(Font.font(13));
        t.setFill(Color.web("#374151"));

        VBox box = new VBox(5);
        box.getChildren().addAll(t, cb);
        return box;
    }

    // them value vao combobox ke
    public void addComBoBoxKe() {
        ke_dao = new Ke_DAO();
        ArrayList<Ke> arrayKe = ke_dao.getTatCaKeHoatDong();
        cbKe.getItems().clear();
        Ke tatCa = new Ke("KE0001", "Tất cả", "Tất cả", false);
        cbKe.getItems().add(tatCa);
        for (Ke ke : arrayKe) {
            cbKe.getItems().add(ke);
        }
        cbKe.getSelectionModel().selectFirst();
    }

    // them value vao combobox dang dieu che
    public void addComBoBoxDDC() {
        ddc_dao = new DangDieuChe_DAO();
        ArrayList<DangDieuChe> arrayDDC = ddc_dao.getDangDieuCheHoatDong();
        cbDangDieuChe.getItems().clear();
        DangDieuChe Tatca = new DangDieuChe(-1, "Tất cả");
        cbDangDieuChe.getItems().add(Tatca);
        for (DangDieuChe ddc : arrayDDC){
            cbDangDieuChe.getItems().add(ddc);
        }
        cbDangDieuChe.getSelectionModel().selectFirst();
    }

    // them value vao combobox ton kho
    public void addComboboxTonKho() {
        cbTonKho.getItems().clear();
        cbTonKho.getItems().addAll("Tất cả","Tồn kho thấp (< 50)","Bình thường (50-200)","Dồi dào (> 200)");
        cbTonKho.getSelectionModel().selectFirst();
    }

    // ham update table
    public void updateTableThuoc(){
        thuocList.clear();
        tableThuoc.refresh();
        thuoc_dao = new Thuoc_DAO();
        arrayThuoc = thuoc_dao.getAllThuoc();
        thuocList.addAll(arrayThuoc);
        tableThuoc.setItems(thuocList);
    }

    // ham loc va tim kiem thuoc
    public void filterAndSearchThuoc() {
        String selectedKe = cbKe.getValue().getTenKe();
        String selectedDDC = cbDangDieuChe.getValue().getTenDDC();
        String selectedTonKho =  cbTonKho.getValue();
        String searchText = searchNameThuoc.getText().trim().toLowerCase();

        ArrayList<Thuoc> filteredList = new ArrayList<>();

        for (Thuoc p : arrayThuoc) { // luôn thao tác trên danh sách gốc
            boolean match = true;

            // Filter Ke
            if (!selectedKe.equals("Tất cả") && !p.getMaKe().getTenKe().equals(selectedKe)) match = false;

            // Filter DDC
            if (!selectedDDC.equals("Tất cả") && !p.getDangDieuChe().getTenDDC().equals(selectedDDC)) match = false;

            // Filter TonKho
            if (!selectedTonKho.equals("Tất cả")) {
                if (selectedTonKho.equals("Tồn kho thấp (< 50)") && TinhTonKho(p) >= 50) match = false;
                else if (selectedTonKho.equals("Bình thường (50-200)") && TinhTonKho(p) < 50 || TinhTonKho(p) > 200) match = false;
                else if (selectedTonKho.equals("Dồi dào (> 200)") && TinhTonKho(p) <= 200) match = false;
            }


            // Search theo tên
            if (!searchText.isEmpty() && !p.getTenThuoc().toLowerCase().contains(searchText)) match = false;

            if (match) filteredList.add(p);
        }

        thuocList.setAll(filteredList);
        tableThuoc.setItems(thuocList);
    }

    // ham load ton kho
    public void loadTonKho() {
        try {
            Connection con = ConnectDB.getInstance().connect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        chiTietThuoc_dao = new LoThuoc_DAO();
        ArrayList<LoThuoc> list = chiTietThuoc_dao.getAllChiTietThuoc();
        mapTonKho.clear();

        for (LoThuoc ct : list) {
            String maThuoc = ct.getMaThuoc().getMaThuoc();
            mapTonKho.put(maThuoc, mapTonKho.getOrDefault(maThuoc, 0) + ct.getSoLuong());
        }
    }

    // ham xoa trang thai tim kiem
    public void clearSearchAndFilter() {
        cbKe.getSelectionModel().selectFirst();
        cbDangDieuChe.getSelectionModel().selectFirst();
        cbTonKho.getSelectionModel().selectFirst();
        searchNameThuoc.clear();
        updateTableThuoc();
    }

    // ham tinh ton kho
    public int TinhTonKho(Thuoc thuoc) {
        return mapTonKho.getOrDefault(thuoc.getMaThuoc(), 0);
    }


}

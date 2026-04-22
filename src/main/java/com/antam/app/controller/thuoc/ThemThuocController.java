package com.antam.app.controller.thuoc;

import com.antam.app.dto.*;
import com.antam.app.network.ClientManager;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class ThemThuocController extends ScrollPane{

    private final ClientManager clientManager;
    private HashMap<String, Integer> mapTonKho = new HashMap<>();

    private Button btnAddMedicine;
    private ComboBox<KeDTO> cbKe;
    private ComboBox<DangDieuCheDTO> cbDangDieuChe;
    private ComboBox<String>cbTonKho;
    private TextField searchNameThuoc;
    private Button btnSearchThuoc;
    private TableView<ThuocDTO> tableThuoc;
    private TableColumn<ThuocDTO, String> colMaThuoc, colTenThuoc, colHamLuong, colDangDieuChe, colGiaBan, colKe;
    private TableColumn<ThuocDTO, String> colTonKho;
    private Button btnSearchInvoice1;

    private ObservableList<ThuocDTO> thuocList = FXCollections.observableArrayList();
    private ArrayList<ThuocDTO> arrayThuoc = new ArrayList<>();

    public ThemThuocController(){
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

        // Cấu hình Table
        colMaThuoc.setCellValueFactory(data -> new SimpleStringProperty(safeText(data.getValue().getMaThuoc())));
        colTenThuoc.setCellValueFactory(data -> new SimpleStringProperty(safeText(data.getValue().getTenThuoc())));
        colHamLuong.setCellValueFactory(data -> new SimpleStringProperty(safeText(data.getValue().getHamLuong())));
        colGiaBan.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getGiaBan())));
        colTonKho.setCellValueFactory(data -> {
            int tonKho = TinhTonKho(data.getValue());
            return new SimpleStringProperty(String.valueOf(tonKho));
        });

        colDangDieuChe.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getDangDieuCheDTO() == null ? "" : safeText(data.getValue().getDangDieuCheDTO().getTenDDC())));
        colKe.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getMaKeDTO() == null ? "" : safeText(data.getValue().getMaKeDTO().getTenKe())));

        // Load comboBox
        addComBoBoxKe();
        addComBoBoxDDC();
        addComboboxTonKho();

        // Load dữ liệu
        arrayThuoc = new ArrayList<>(clientManager.getThuocList());
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
            TableRow<ThuocDTO> row = new TableRow<>();

            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    ThuocDTO selectedThuocDTO = row.getItem();

                    XemChiTietThuocFormController xemDialog = new XemChiTietThuocFormController();
                    xemDialog.setThuoc(selectedThuocDTO);
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
        ArrayList<KeDTO> arrayKe = new ArrayList<>(clientManager.getActiveKeList());
        cbKe.getItems().clear();
        KeDTO tatCa = new KeDTO("KE0000", "Tất cả", "Tất cả", false);
        cbKe.getItems().add(tatCa);
        for (KeDTO keDTO : arrayKe) {
            cbKe.getItems().add(keDTO);
        }
        cbKe.getSelectionModel().selectFirst();
    }

    // them value vao combobox dang dieu che
    public void addComBoBoxDDC() {
        ArrayList<DangDieuCheDTO> arrayDDC = new ArrayList<>(clientManager.getActiveDangDieuCheList());
        cbDangDieuChe.getItems().clear();
        DangDieuCheDTO Tatca = new DangDieuCheDTO(-1, "Tất cả");
        cbDangDieuChe.getItems().add(Tatca);
        for (DangDieuCheDTO ddc : arrayDDC){
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
        arrayThuoc = new ArrayList<>(clientManager.getThuocList());
        thuocList.addAll(arrayThuoc);
        tableThuoc.setItems(thuocList);
    }

    // ham loc va tim kiem thuoc
    public void filterAndSearchThuoc() {
        String selectedKe = cbKe.getValue() == null ? "Tất cả" : cbKe.getValue().getTenKe();
        String selectedDDC = cbDangDieuChe.getValue() == null ? "Tất cả" : cbDangDieuChe.getValue().getTenDDC();
        String selectedTonKho = cbTonKho.getValue() == null ? "Tất cả" : cbTonKho.getValue();
        String searchText = searchNameThuoc.getText().trim().toLowerCase(Locale.ROOT);

        ArrayList<ThuocDTO> filteredList = new ArrayList<>();

        for (ThuocDTO p : arrayThuoc) { // luôn thao tác trên danh sách gốc
            boolean match = true;
            int tonKho = TinhTonKho(p);

            // Filter Ke
            if (!selectedKe.equals("Tất cả") && !p.getMaKeDTO().getTenKe().equals(selectedKe)) match = false;

            // Filter DDC
            if (!selectedDDC.equals("Tất cả") && !p.getDangDieuCheDTO().getTenDDC().equals(selectedDDC)) match = false;

            // Filter TonKho
            if (!selectedTonKho.equals("Tất cả")) {
                if (selectedTonKho.equals("Tồn kho thấp (< 50)") && tonKho >= 50) match = false;
                else if (selectedTonKho.equals("Bình thường (50-200)") && (tonKho < 50 || tonKho > 200)) match = false;
                else if (selectedTonKho.equals("Dồi dào (> 200)") && tonKho <= 200) match = false;
            }


            // Search theo tên
            if (!searchText.isEmpty() && !p.getTenThuoc().toLowerCase(Locale.ROOT).contains(searchText)) match = false;

            if (match) filteredList.add(p);
        }

        thuocList.setAll(filteredList);
        tableThuoc.setItems(thuocList);
    }

    // ham load ton kho
    public void loadTonKho() {
        ArrayList<LoThuocDTO> list = new ArrayList<>(clientManager.getLoThuocList());
        mapTonKho.clear();

        for (LoThuocDTO ct : list) {
            String maThuoc = ct.getMaThuocDTO().getMaThuoc();
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
    public int TinhTonKho(ThuocDTO thuocDTO) {
        return mapTonKho.getOrDefault(thuocDTO.getMaThuoc(), 0);
    }

    private void configureComboBoxDisplay() {
        if (cbKe == null || cbDangDieuChe == null) {
            return;
        }
        cbKe.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(KeDTO item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : formatKe(item));
            }
        });
        cbKe.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(KeDTO item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : formatKe(item));
            }
        });

        cbDangDieuChe.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(DangDieuCheDTO item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : formatDDC(item));
            }
        });
        cbDangDieuChe.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(DangDieuCheDTO item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : formatDDC(item));
            }
        });
    }

    private String formatKe(KeDTO keDTO) {
        return keDTO.getMaKe() + " - " + keDTO.getTenKe();
    }

    private String formatDDC(DangDieuCheDTO ddc) {
        return ddc == null ? "" : ddc.getDisplayText();
    }

    private String safeText(String value) {
        return value == null ? "" : value;
    }


}

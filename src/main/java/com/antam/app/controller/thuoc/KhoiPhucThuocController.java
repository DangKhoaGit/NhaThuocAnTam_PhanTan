package com.antam.app.controller.thuoc;

import com.antam.app.dto.*;
import com.antam.app.network.ClientManager;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcons;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;

public class KhoiPhucThuocController extends ScrollPane{

    private final ClientManager clientManager;
    private HashMap<String, Integer> mapTonKho = new HashMap<>();

    private ComboBox<KeDTO> cbKe;
    private ComboBox<DangDieuCheDTO> cbDangDieuChe;
    private ComboBox<String>cbTonKho;
    private TextField searchNameThuoc;
    private Button btnSearchThuoc, btnKhoiPhuc;
    private TableView<ThuocDTO> tableThuoc;
    private TableColumn<ThuocDTO, String> colMaThuoc, colTenThuoc, colHamLuong, colDangDieuChe, colGiaBan, colKe;
    private TableColumn<ThuocDTO, String> colTonKho;
    private Button btnSearchInvoice1;

    private ObservableList<ThuocDTO> thuocList = FXCollections.observableArrayList();
    private ArrayList<ThuocDTO> arrayThuoc = new ArrayList<>();

    public KhoiPhucThuocController(){
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

        HBox titleBox = new HBox();
        titleBox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

        Text title = new Text("Khôi phục thuốc");
        title.setFont(Font.font("System Bold", 30));
        title.setFill(Color.web("#1e3a8a"));

        Pane spacer = new Pane();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        FontAwesomeIcon iconAdd = new FontAwesomeIcon();
        iconAdd.setFill(Color.WHITE);
        iconAdd.setIcon(FontAwesomeIcons.BACKWARD);
        btnKhoiPhuc = new Button("Khôi phục");
        btnKhoiPhuc.setGraphic(iconAdd);
        btnKhoiPhuc.getStyleClass().add("btn-khoiphuc");

        titleBox.getChildren().addAll(title, spacer, btnKhoiPhuc);

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
        tableThuoc.setPlaceholder(new Label("Không có thuốc nào trong kho đã bị xóa."));

        Button guide = new Button("Nhấn 2 lần chuột trái vào bảng để xem chi tiết");
        guide.getStyleClass().add("pane-huongdan");
        guide.setMaxWidth(Double.MAX_VALUE);
        guide.setPadding(new Insets(10));
        FontAwesomeIcon infoIcon = new FontAwesomeIcon(); infoIcon.setGlyphName("INFO"); infoIcon.setFill(Color.web("#2563eb"));
        guide.setGraphic(infoIcon);

        // ===================== ADD CONTAINER =====================
        root.getChildren().addAll(titleBox, filterPane, searchBox, tableThuoc, guide);

        URL stylesheet = getClass().getResource("/com/antam/app/styles/dashboard_style.css");
        if (stylesheet != null) {
            this.getStylesheets().add(stylesheet.toExternalForm());
        }
        this.setContent(root);
        /** Sự kiện **/

        // Nút thêm thuốc
        btnKhoiPhuc.setOnAction(e -> {
            ThuocDTO selectedThuocDTO = tableThuoc.getSelectionModel().getSelectedItem();
            if (selectedThuocDTO == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Chưa chọn thuốc");
                alert.setHeaderText(null);
                alert.setContentText("Vui lòng chọn thuốc cần khôi phục!");
                alert.showAndWait();
                return;
            }

            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Xác nhận khôi phục");
            confirmAlert.setHeaderText(null);
            confirmAlert.setContentText("Bạn có chắc chắn muốn khôi phục thuốc " + selectedThuocDTO.getTenThuoc() + "?");
            confirmAlert.initModality(Modality.APPLICATION_MODAL);
            confirmAlert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    handleRestoreThuocAsync(selectedThuocDTO);
                }
            });
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
        arrayThuoc = toThuocList(clientManager.getDeletedThuocList());
        thuocList.addAll(arrayThuoc);
        tableThuoc.setItems(thuocList);

        // Event lọc
        cbKe.setOnAction(e -> filterAndSearchThuoc());
        cbDangDieuChe.setOnAction(e -> filterAndSearchThuoc());
        cbTonKho.setOnAction(e -> filterAndSearchThuoc());

        // Event tìm kiếm
        btnSearchThuoc.setOnAction(e -> filterAndSearchThuoc());
        searchNameThuoc.setOnKeyReleased(e -> filterAndSearchThuoc());
        btnSearchInvoice1.setOnAction(e -> clearSearchAndFilter());

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

        configureComboBoxDisplay();
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
        ArrayList<KeDTO> arrayKe = toKeList(clientManager.getActiveKeList());
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
        ArrayList<DangDieuCheDTO> arrayDDC = toDangDieuCheList(clientManager.getActiveDangDieuCheList());
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
        arrayThuoc = toThuocList(clientManager.getDeletedThuocList());
        thuocList.addAll(arrayThuoc);
        tableThuoc.setItems(thuocList);
    }

    private void handleRestoreThuocAsync(ThuocDTO selectedThuocDTO) {
        Task<Boolean> restoreTask = new Task<>() {
            @Override
            protected Boolean call() {
                return clientManager.restoreThuoc(selectedThuocDTO.getMaThuoc());
            }
        };

        restoreTask.setOnRunning(e -> btnKhoiPhuc.setDisable(true));

        restoreTask.setOnSucceeded(e -> {
            btnKhoiPhuc.setDisable(false);
            if (Boolean.TRUE.equals(restoreTask.getValue())) {
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Khôi phục thành công");
                successAlert.setHeaderText(null);
                successAlert.setContentText("Khôi phục thuốc thành công!");
                successAlert.showAndWait();
                updateTableThuoc();
                loadTonKho();
            } else {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Khôi phục thất bại");
                errorAlert.setHeaderText(null);
                errorAlert.setContentText("Khôi phục thuốc thất bại. Vui lòng thử lại!");
                errorAlert.showAndWait();
            }
        });

        restoreTask.setOnFailed(e -> {
            btnKhoiPhuc.setDisable(false);
            Throwable ex = restoreTask.getException();
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Khôi phục thất bại");
            errorAlert.setHeaderText(null);
            errorAlert.setContentText(ex == null ? "Lỗi kết nối tới server!" : "Lỗi kết nối: " + ex.getMessage());
            errorAlert.showAndWait();
        });

        Thread restoreThread = new Thread(restoreTask, "khoi-phuc-thuoc-task");
        restoreThread.setDaemon(true);
        restoreThread.start();
    }

    // ham loc va tim kiem thuoc
    public void filterAndSearchThuoc() {
        String selectedKe = cbKe.getValue() == null ? "Tất cả" : cbKe.getValue().getTenKe();
        String selectedDDC = cbDangDieuChe.getValue() == null ? "Tất cả" : cbDangDieuChe.getValue().getTenDDC();
        String selectedTonKho = cbTonKho.getValue() == null ? "Tất cả" : cbTonKho.getValue();
        String searchText = searchNameThuoc.getText() == null ? "" : searchNameThuoc.getText().trim().toLowerCase(Locale.ROOT);

        ArrayList<ThuocDTO> filteredList = new ArrayList<>();

        for (ThuocDTO p : arrayThuoc) {
            if (p == null) {
                continue;
            }
            boolean match = true;
            int tonKho = TinhTonKho(p);
            String tenKe = p.getMaKeDTO() == null ? "" : safeText(p.getMaKeDTO().getTenKe());
            String tenDDC = p.getDangDieuCheDTO() == null ? "" : safeText(p.getDangDieuCheDTO().getTenDDC());
            String tenThuoc = safeText(p.getTenThuoc()).toLowerCase(Locale.ROOT);

            if (!selectedKe.equals("Tất cả") && !tenKe.equals(selectedKe)) match = false;
            if (!selectedDDC.equals("Tất cả") && !tenDDC.equals(selectedDDC)) match = false;

            if (!selectedTonKho.equals("Tất cả")) {
                if (selectedTonKho.equals("Tồn kho thấp (< 50)") && tonKho >= 50) match = false;
                else if (selectedTonKho.equals("Bình thường (50-200)") && (tonKho < 50 || tonKho > 200)) match = false;
                else if (selectedTonKho.equals("Dồi dào (> 200)") && tonKho <= 200) match = false;
            }

            if (!searchText.isEmpty() && !tenThuoc.contains(searchText)) match = false;

            if (match) filteredList.add(p);
        }

        thuocList.setAll(filteredList);
        tableThuoc.setItems(thuocList);
    }

    // ham load ton kho
    public void loadTonKho() {
        ArrayList<LoThuocDTO> list = toLoThuocList(clientManager.getLoThuocList());
        mapTonKho.clear();

        for (LoThuocDTO ct : list) {
            if (ct == null || ct.getMaThuocDTO() == null || ct.getMaThuocDTO().getMaThuoc() == null) {
                continue;
            }
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

    private ArrayList<ThuocDTO> toThuocList(Collection<?> source) {
        ArrayList<ThuocDTO> result = new ArrayList<>();
        if (source == null) {
            return result;
        }
        for (Object item : source) {
            if (item instanceof ThuocDTO thuocDTO) {
                result.add(thuocDTO);
            }
        }
        return result;
    }

    private ArrayList<KeDTO> toKeList(Collection<?> source) {
        ArrayList<KeDTO> result = new ArrayList<>();
        if (source == null) {
            return result;
        }
        for (Object item : source) {
            if (item instanceof KeDTO keDTO) {
                result.add(keDTO);
            }
        }
        return result;
    }

    private ArrayList<DangDieuCheDTO> toDangDieuCheList(Collection<?> source) {
        ArrayList<DangDieuCheDTO> result = new ArrayList<>();
        if (source == null) {
            return result;
        }
        for (Object item : source) {
            if (item instanceof DangDieuCheDTO dangDieuCheDTO) {
                result.add(dangDieuCheDTO);
            }
        }
        return result;
    }

    private ArrayList<LoThuocDTO> toLoThuocList(Collection<?> source) {
        ArrayList<LoThuocDTO> result = new ArrayList<>();
        if (source == null) {
            return result;
        }
        for (Object item : source) {
            if (item instanceof LoThuocDTO loThuocDTO) {
                result.add(loThuocDTO);
            }
        }
        return result;
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

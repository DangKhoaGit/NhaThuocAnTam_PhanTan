//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.antam.app.controller.hoadon;

import com.antam.app.network.ClientManager;
import com.antam.app.dto.HoaDonDTO;
import com.antam.app.dto.NhanVienDTO;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcons;
import javafx.beans.property.SimpleDoubleProperty;
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

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javafx.concurrent.Task;

/**
 * Controller cho giao diện quản lý hóa đơn (invoice_view.fxml).
 * - Hiển thị danh sách hóa đơn ra TableView.
 * - Cho phép tạo, đổi, trả hóa đơn qua các nút chức năng.
 * - Khi double-click vào 1 dòng hóa đơn sẽ mở dialog chi tiết hóa đơn.
 */
public class ThemHoaDonController extends ScrollPane{
    private Button btnAddInvoice;

    private TableView<HoaDonDTO> table_invoice;
    private TableColumn<HoaDonDTO, String> colMaHD;
    private TableColumn<HoaDonDTO, String> colNgayTao;
    private TableColumn<HoaDonDTO, String> colKhachHang;
    private TableColumn<HoaDonDTO, String> colNhanVien;
    private TableColumn<HoaDonDTO, String> colKhuyenMai;
    private TableColumn<HoaDonDTO, Double> colTongTien;
    private TableColumn<HoaDonDTO, String> colTrangThai;
    private TextField txtSearchInvoice;
    private Button btnSearchInvoice;
    private Button btnResetInvoice;
    private ComboBox<NhanVienDTO> cbEmployee;
    private ComboBox<String> cbStatus;
    private ComboBox<String> cbPrice;
    private DatePicker cbFirstDate;
    private DatePicker cbEndDate;

    private ClientManager clientManager = ClientManager.getInstance();
    private List<HoaDonDTO> cachedHoaDonList = new ArrayList<>();

    // Định dạng tiền tệ kiểu Việt Nam: 1.000đ, 10.000đ
    private static final DecimalFormat VND_FORMAT;
    static {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.');
        symbols.setDecimalSeparator(',');
        VND_FORMAT = new DecimalFormat("#,##0", symbols);
    }

    public ThemHoaDonController() {
        /** Giao diện **/
        this.setFitToHeight(true);
        this.setFitToWidth(true);
        this.setPrefSize(900, 730);
        AnchorPane.setTopAnchor(this, 0.0);
        AnchorPane.setBottomAnchor(this, 0.0);
        AnchorPane.setLeftAnchor(this, 0.0);
        AnchorPane.setRightAnchor(this, 0.0);

        VBox rootPane = new VBox(30);
        rootPane.setStyle("-fx-background-color: #f8fafc;");
        rootPane.setPadding(new Insets(20));

        FontAwesomeIcon iconAdd = new FontAwesomeIcon();
        iconAdd.setFill(Color.WHITE);
        iconAdd.setIcon(FontAwesomeIcons.valueOf("PLUS"));
        btnAddInvoice = new Button("Thêm hoá đơn");
        btnAddInvoice.setGraphic(iconAdd);
        btnAddInvoice.getStyleClass().add("btn-them");

        HBox titleBox = new HBox(5);
        Text title = new Text("Thêm hoá đơn");
        title.setFill(Color.web("#1e3a8a"));
        title.setFont(Font.font("System", 30));
        Pane spacer = new Pane();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        titleBox.getChildren().addAll(title, spacer, btnAddInvoice);

        FlowPane filters = new FlowPane(5, 5);
        filters.getStyleClass().add("box-pane");
        filters.setPadding(new Insets(10));
        filters.setEffect(new DropShadow(19.5, 3, 2, Color.rgb(211, 211, 211)));

        cbEmployee = new ComboBox<>();
        cbStatus = new ComboBox<>();
        cbFirstDate = new DatePicker();
        cbEndDate = new DatePicker();
        cbPrice = new ComboBox<>();

        btnResetInvoice = new Button("Xoá rỗng");
        btnResetInvoice.getStyleClass().add("btn-xoarong");
        btnResetInvoice.setPrefSize(93, 40);
        FontAwesomeIcon refreshIcon = new FontAwesomeIcon();
        refreshIcon.setIcon(FontAwesomeIcons.REFRESH);
        refreshIcon.setFill(Color.WHITE);
        btnResetInvoice.setGraphic(refreshIcon);

        filters.getChildren().addAll(
                createVBox("Nhân viên:", cbEmployee),
                createVBox("Trạng thái:", cbStatus),
                createVBox("Từ ngày:", cbFirstDate),
                createVBox("Đến ngày:", cbEndDate),
                createVBox("Khoảng giá:", cbPrice),
                createVBox("", btnResetInvoice)
        );

        HBox searchBox = new HBox(10);
        txtSearchInvoice = new TextField();
        txtSearchInvoice.setPromptText("Tìm kiếm mã hoá đơn");
        txtSearchInvoice.setPrefSize(300, 40);
        txtSearchInvoice.setStyle("-fx-background-color: white; -fx-border-color: #e5e7eb; -fx-border-radius: 8px;");

        btnSearchInvoice = new Button();
        btnSearchInvoice.setPrefSize(50, 40);
        btnSearchInvoice.setStyle("-fx-background-color: #2563eb; -fx-background-radius: 5px;");
        FontAwesomeIcon searchIcon = new FontAwesomeIcon(); searchIcon.setGlyphName("SEARCH"); searchIcon.setFill(Color.WHITE);
        btnSearchInvoice.setGraphic(searchIcon);

        searchBox.getChildren().addAll(txtSearchInvoice, btnSearchInvoice);

        VBox tableBox = new VBox(5);
        table_invoice = new TableView<>();

        colMaHD = new TableColumn<>("Mã hoá đơn");
        colNgayTao = new TableColumn<>("Ngày tạo");
        colKhachHang = new TableColumn<>("Khách hàng");
        colNhanVien = new TableColumn<>("Nhân viên");
        colKhuyenMai = new TableColumn<>("Khuyến mãi");
        colTongTien = new TableColumn<>("Tổng tiền");
        colTrangThai = new TableColumn<>("Trạng thái");

        table_invoice.getColumns().addAll(colMaHD, colNgayTao, colKhachHang, colNhanVien, colKhuyenMai, colTongTien, colTrangThai);
        table_invoice.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table_invoice.setPrefHeight(500);

        Button guide = new Button("Nhấn 2 lần chuột trái vào bảng để xem chi tiết");
        guide.getStyleClass().add("pane-huongdan");
        guide.setMaxWidth(Double.MAX_VALUE);
        guide.setPadding(new Insets(10));
        FontAwesomeIcon infoIcon = new FontAwesomeIcon(); infoIcon.setGlyphName("INFO");
        infoIcon.setFill(Color.web("#2563eb"));
        guide.setGraphic(infoIcon);

        tableBox.getChildren().addAll(table_invoice, guide);

        rootPane.getChildren().addAll(titleBox, filters, searchBox, tableBox);
        this.getStylesheets().addAll(getClass().getResource("/com/antam/app/styles/dashboard_style.css").toExternalForm());
        this.setContent(rootPane);

        /** Khởi tạo bảng và dữ liệu **/
        setupTableColumns();
        loadDataVaoBang();
        loadNhanVienComboBox();
        setupComboBoxStatus();
        setupComboBoxPrice();
        setupEventListeners();
    }

    /**
     * Setup các cột bảng
     */
    private void setupTableColumns() {
        colMaHD.setCellValueFactory(new PropertyValueFactory<>("MaHD"));
        colNgayTao.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNgayTao().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))));
        colKhachHang.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMaKH().getTenKH()));
        colNhanVien.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMaNV().getHoTen()));
        colKhuyenMai.setCellValueFactory(cellData -> {
            HoaDonDTO hoaDonDTO = cellData.getValue();
            String tenKM = (hoaDonDTO.getMaKM() != null) ? hoaDonDTO.getMaKM().getTenKM() : "Không có khuyến mãi";
            return new SimpleStringProperty(tenKM);
        });
        colTongTien.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getTongTien()).asObject());
        colTongTien.setCellFactory(column -> new TableCell<HoaDonDTO, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(VND_FORMAT.format(item) + "đ");
                }
            }
        });
        colTrangThai.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().isDeleteAt() ? "Đã huỷ" : "Hoạt động"));
    }

    /**
     * Load dữ liệu hóa đơn từ server
     */
    private void loadDataVaoBang() {
        Task<List<HoaDonDTO>> loadTask = new Task<>() {
            @Override
            protected List<HoaDonDTO> call() throws Exception {
                List<?> result = clientManager.getHoaDonList();
                return (result instanceof List) ? (List<HoaDonDTO>) result : new ArrayList<>();
            }
        };

        loadTask.setOnSucceeded(e -> {
            List<HoaDonDTO> hoaDonList = loadTask.getValue();
            if (hoaDonList != null) {
                cachedHoaDonList = new ArrayList<>(hoaDonList);
                table_invoice.setItems(FXCollections.observableArrayList(hoaDonList));
            }
        });

        loadTask.setOnFailed(e -> {
            Throwable ex = loadTask.getException();
            if (ex != null) System.err.println("Failed to load HoaDon list: " + ex.getMessage());
        });

        Thread loadThread = new Thread(loadTask);
        loadThread.setDaemon(true);
        loadThread.start();
    }

    /**
     * Load dữ liệu nhân viên vào ComboBox
     */
    private void loadNhanVienComboBox() {
        Task<List<NhanVienDTO>> loadNVTask = new Task<>() {
            @Override
            protected List<NhanVienDTO> call() throws Exception {
                List<?> result = clientManager.getNhanVienList();
                return (result instanceof List) ? (List<NhanVienDTO>) result : new ArrayList<>();
            }
        };

        loadNVTask.setOnSucceeded(e -> {
            List<NhanVienDTO> dsNhanVienList = loadNVTask.getValue();
            if (dsNhanVienList != null) {
                ObservableList<NhanVienDTO> dsNhanVien = FXCollections.observableArrayList(dsNhanVienList);
                // Thêm lựa chọn "Tất cả" vào đầu danh sách
                NhanVienDTO tatCaNV = new NhanVienDTO("Tất cả");
                dsNhanVien.add(0, tatCaNV);
                cbEmployee.setItems(dsNhanVien);
                cbEmployee.setPromptText("Chọn nhân viên");
                // Hiển thị tên nhân viên trong ComboBox thay vì mã
                cbEmployee.setCellFactory(lv -> new ListCell<NhanVienDTO>() {
                    @Override
                    protected void updateItem(NhanVienDTO item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText(null);
                        } else {
                            setText("Tất cả".equals(item.getMaNV()) ? "Tất cả" : item.getHoTen());
                        }
                    }
                });
                cbEmployee.setButtonCell(new ListCell<NhanVienDTO>() {
                    @Override
                    protected void updateItem(NhanVienDTO item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText(null);
                        } else {
                            setText("Tất cả".equals(item.getMaNV()) ? "Tất cả" : item.getHoTen());
                        }
                    }
                });
            }
        });

        loadNVTask.setOnFailed(e -> {
            Throwable ex = loadNVTask.getException();
            if (ex != null) System.err.println("Failed to load NhanVien list: " + ex.getMessage());
        });

        Thread loadNVThread = new Thread(loadNVTask);
        loadNVThread.setDaemon(true);
        loadNVThread.start();
    }

    /**
     * Setup ComboBox trạng thái
     */
    private void setupComboBoxStatus() {
        ObservableList<String> dsTrangThai = FXCollections.observableArrayList("Tất cả", "Hoạt động", "Đã huỷ");
        cbStatus.setItems(dsTrangThai);
        cbStatus.setValue("Tất cả");
    }

    /**
     * Setup ComboBox khoảng giá
     */
    private void setupComboBoxPrice() {
        ObservableList<String> dsKhoangGia = FXCollections.observableArrayList(
                "Tất cả",
                "Dưới 500.000",
                "500.000 - 1.000.000",
                "1.000.000 - 2.000.000",
                "Trên 2.000.000"
        );
        cbPrice.setItems(dsKhoangGia);
        cbPrice.setValue("Tất cả");
    }

    /**
     * Setup các sự kiện (nút bấm, lắng nghe thay đổi)
     */
    private void setupEventListeners() {
        // Sự kiện nút thêm hóa đơn
        this.btnAddInvoice.setOnAction((e) -> {
            ThemHoaDonFormController themDialog = new ThemHoaDonFormController();
            themDialog.setOnHoaDonCreated(this::loadDataVaoBang);
            Dialog<Void> dialog = new Dialog<>();
            dialog.setDialogPane(themDialog);
            dialog.setTitle("Thêm hóa đơn");
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.showAndWait();
        });

        // Sự kiện lọc hóa đơn
        cbEmployee.valueProperty().addListener((obs, oldNV, newNV) -> performFilter());
        cbStatus.valueProperty().addListener((obs, oldSt, newSt) -> performFilter());
        cbPrice.valueProperty().addListener((obs, oldPr, newPr) -> performFilter());
        if (cbFirstDate != null) cbFirstDate.valueProperty().addListener((obs, oldDate, newDate) -> performFilter());
        if (cbEndDate != null) cbEndDate.valueProperty().addListener((obs, oldDate, newDate) -> performFilter());

        // Sự kiện tìm kiếm theo mã hóa đơn
        txtSearchInvoice.textProperty().addListener((observable, oldValue, newValue) -> performSearch(newValue));
        btnSearchInvoice.setOnAction(e -> performSearch(txtSearchInvoice.getText()));

        // Sự kiện đặt lại bộ lọc
        btnResetInvoice.setOnAction(e -> {
            cbEmployee.setValue(null);
            cbStatus.setValue("Tất cả");
            cbPrice.setValue("Tất cả");
            if (cbFirstDate != null) cbFirstDate.setValue(null);
            if (cbEndDate != null) cbEndDate.setValue(null);
            txtSearchInvoice.clear();
            table_invoice.setItems(FXCollections.observableArrayList(cachedHoaDonList));
        });

        // Row factory để tô màu dòng được chọn
        this.table_invoice.setRowFactory(tv -> new TableRow<>() {
            @Override
            protected void updateItem(HoaDonDTO item, boolean empty) {
                super.updateItem(item, empty);
                if (!empty && item != null && isSelected()) {
                    setStyle("-fx-background-color: #d1fae5;");
                } else {
                    setStyle("");
                }
            }
        });

        // Double-click để xem chi tiết hóa đơn
        this.table_invoice.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && table_invoice.getSelectionModel().getSelectedItem() != null) {
                HoaDonDTO selectedHoaDonDTO = table_invoice.getSelectionModel().getSelectedItem();
                XemChiTietHoaDonFormController xemDialog = new XemChiTietHoaDonFormController();
                xemDialog.setInvoice(selectedHoaDonDTO);
                Dialog<Void> dialog = new Dialog<>();
                dialog.setDialogPane(xemDialog);
                dialog.setTitle("Chi tiết hóa đơn");
                dialog.initModality(Modality.APPLICATION_MODAL);
                dialog.showAndWait();
            }
        });
    }

    /**
     * Lọc hóa đơn theo các tiêu chí
     */
    private void performFilter() {
        if (cachedHoaDonList.isEmpty()) return;
        List<HoaDonDTO> allHoaDon = new ArrayList<>(cachedHoaDonList);
        NhanVienDTO selectedNV = cbEmployee.getValue();
        String selectedStatus = cbStatus.getValue();
        String selectedPrice = cbPrice.getValue();
        LocalDate fromDate = cbFirstDate != null ? cbFirstDate.getValue() : null;
        LocalDate toDate = cbEndDate != null ? cbEndDate.getValue() : null;
        boolean allNV = (selectedNV == null || "Tất cả".equals(selectedNV.getMaNV()));
        boolean allStatus = (selectedStatus == null || "Tất cả".equals(selectedStatus));
        boolean allPrice = (selectedPrice == null || "Tất cả".equals(selectedPrice));
        
        // Lọc theo nhân viên và trạng thái trước
        List<HoaDonDTO> baseList;
        if (allNV && allStatus) {
            baseList = new ArrayList<>(allHoaDon);
        } else if (!allNV && allStatus) {
            baseList = allHoaDon.stream()
                    .filter(hd -> hd.getMaNV() != null && selectedNV.getMaNV().equals(hd.getMaNV().getMaNV()))
                    .collect(Collectors.toList());
        } else if (allNV && !allStatus) {
            baseList = allHoaDon.stream()
                    .filter(hd -> "Hoạt động".equals(selectedStatus) ? !hd.isDeleteAt() : hd.isDeleteAt())
                    .collect(Collectors.toList());
        } else {
            baseList = allHoaDon.stream()
                    .filter(hd -> ("Hoạt động".equals(selectedStatus) ? !hd.isDeleteAt() : hd.isDeleteAt()) &&
                            hd.getMaNV() != null && selectedNV.getMaNV().equals(hd.getMaNV().getMaNV()))
                    .collect(Collectors.toList());
        }
        
        // Lọc tiếp theo khoảng giá
        if (!allPrice) {
            double min = 0, max = Double.MAX_VALUE;
            switch (selectedPrice) {
                case "Dưới 500.000":
                    max = 500000;
                    break;
                case "500.000 - 1.000.000":
                    min = 500000;
                    max = 1000000;
                    break;
                case "1.000.000 - 2.000.000":
                    min = 1000000;
                    max = 2000000;
                    break;
                case "Trên 2.000.000":
                    min = 2000000;
                    max = Double.MAX_VALUE;
                    break;
            }
            double finalMin = min;
            double finalMax = max;
            baseList = baseList.stream()
                    .filter(hd -> hd.getTongTien() >= finalMin && hd.getTongTien() < finalMax)
                    .collect(Collectors.toList());
        }
        
        // Lọc tiếp theo ngày
        if (fromDate != null || toDate != null) {
            baseList = baseList.stream()
                    .filter(hd -> {
                        LocalDate ngayTao = hd.getNgayTao();
                        boolean afterFrom = (fromDate == null) || !ngayTao.isBefore(fromDate);
                        boolean beforeTo = (toDate == null) || !ngayTao.isAfter(toDate);
                        return afterFrom && beforeTo;
                    })
                    .collect(Collectors.toList());
        }
        
        ObservableList<HoaDonDTO> filtered = FXCollections.observableArrayList(baseList);
        table_invoice.setItems(filtered);
    }

    /**
     * Tìm kiếm hóa đơn theo mã
     */
    private void performSearch(String keyword) {
        if (cachedHoaDonList.isEmpty()) return;
        if (keyword == null || keyword.trim().isEmpty()) {
            table_invoice.setItems(FXCollections.observableArrayList(cachedHoaDonList));
        } else {
            List<HoaDonDTO> searchResult = cachedHoaDonList.stream()
                    .filter(hd -> hd.getMaHD().toLowerCase().contains(keyword.toLowerCase()))
                    .collect(Collectors.toList());
            table_invoice.setItems(FXCollections.observableArrayList(searchResult));
        }
    }

    private VBox createVBox(String label, Control field) {
        VBox v = new VBox(5);
        if (label != null) {
            Text t = new Text(label);
            t.setFill(Color.web("#374151"));
            t.setFont(Font.font(13));
            v.getChildren().add(t);
        }
        field.setPrefSize(200, 40);
        v.getChildren().add(field);
        return v;
    }
}

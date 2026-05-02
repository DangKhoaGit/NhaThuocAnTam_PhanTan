/*
 * @ (#) KhoiPhucKhachHangController.java   1.0 28/10/25
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */
package com.antam.app.controller.khachhang;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

import com.antam.app.dto.KhachHangDTO;
import com.antam.app.network.ClientManager;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcons;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/*
 * @description Controller for recovering (restoring) deleted customers
 * @author: Tran Tuan Hung
 * @date: 28/10/25
 * @version: 2.0 (refactored according to n-layer architecture)
 * 
 * Architecture Compliance:
 * - Controller calls Service methods only (no DAO access)
 * - Service handles business logic and DTO↔Entity conversion
 */
public class KhoiPhucKhachHangController extends ScrollPane {


    private Button btnKhoiPhuc;
    private TextField txtSearchEmployee;
    private Button btnSearchEmployee;
    private ComboBox<String> cbSoDonHang; // Số đơn hàng filter
    private ComboBox<String> cbTrangThai; // Loại khách hàng filter
    private ComboBox<String> cbTongChiTieu; // Tổng chi tiêu filter

    private TableView<KhachHangDTO> tableViewKhachHang;
    private TableColumn<KhachHangDTO, String> colMaKH = new TableColumn<>("Mã khách hàng");
    private TableColumn<KhachHangDTO, String> colTenKH = new TableColumn<>("Tên khách hàng");
    private TableColumn<KhachHangDTO, String> colSoDienThoai = new TableColumn<>("Số điện thoại");
    private TableColumn<KhachHangDTO, Integer> colSoDonHang = new TableColumn<>("Số đơn hàng");
    private TableColumn<KhachHangDTO, Double> colTongChiTieu = new TableColumn<>("Tổng chi tiêu");
    private TableColumn<KhachHangDTO, String> colDonHangGanNhat = new TableColumn<>("Đơn hàng gần nhất");
    private TableColumn<KhachHangDTO, String> colLoaiKhachHang = new TableColumn<>("Loại khách hàng");

    private ObservableList<KhachHangDTO> dsKhachHang;
    private ObservableList<KhachHangDTO> dsKhachHangGoc; // Để lưu danh sách gốc cho việc lọc

    // Service instance - manages business logic and DAO interactions
    private ClientManager clientManager;
    private DateTimeFormatter formatter;

    // Định dạng tiền tệ kiểu Việt Nam: 1.000đ, 10.000đ
    private static final DecimalFormat VND_FORMAT;
    static {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.');
        symbols.setDecimalSeparator(',');
        VND_FORMAT = new DecimalFormat("#,##0", symbols);
    }

    public KhoiPhucKhachHangController(){
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

        HBox header = new HBox();
        header.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        Text title = new Text("Khôi phục khách hàng");
        title.setFill(Color.web("#1e3a8a"));
        title.setFont(Font.font("System Bold", 30));
        Pane spacer = new Pane();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        FontAwesomeIcon iconRestore = new FontAwesomeIcon();
        iconRestore.setIcon(FontAwesomeIcons.BACKWARD);
        iconRestore.setFill(Color.WHITE);
        btnKhoiPhuc = new Button("Khôi phục");
        btnKhoiPhuc.getStyleClass().add("btn-khoiphuc");
        btnKhoiPhuc.setGraphic(iconRestore);
        header.getChildren().addAll(title, spacer, btnKhoiPhuc);

        // Filter pane
        FlowPane filterPane = new FlowPane(5,5);
        filterPane.getStyleClass().add("box-pane");
        filterPane.setPadding(new Insets(10));
        DropShadow ds = new DropShadow();
        ds.setBlurType(BlurType.GAUSSIAN);
        ds.setOffsetX(3);
        ds.setOffsetY(2);
        ds.setRadius(19.5275);
        ds.setColor(Color.rgb(211,211,211));
        filterPane.setEffect(ds);

        // ComboBoxes
        VBox vbSoDonHang = new VBox(5);
        Text lblSoDonHang = new Text("Số đơn hàng");
        lblSoDonHang.setFont(Font.font(13));
        lblSoDonHang.setFill(Color.web("#374151"));
        cbSoDonHang = new ComboBox<>();
        cbSoDonHang.setPrefSize(200, 40);
        cbSoDonHang.setPromptText("Chọn số đơn hàng");
        cbSoDonHang.getStyleClass().add("combo-box");
        vbSoDonHang.getChildren().addAll(lblSoDonHang, cbSoDonHang);

        VBox vbTrangThai = new VBox(5);
        Text lblTrangThai = new Text("Loại khách hàng");
        lblTrangThai.setFont(Font.font(13));
        lblTrangThai.setFill(Color.web("#374151"));
        cbTrangThai = new ComboBox<>();
        cbTrangThai.setPrefSize(200, 40);
        cbTrangThai.setPromptText("Chọn trạng thái");
        cbTrangThai.getStyleClass().add("combo-box");
        vbTrangThai.getChildren().addAll(lblTrangThai, cbTrangThai);

        VBox vbTongChiTieu = new VBox(5);
        Text lblTongChiTieu = new Text("Tổng chi tiêu");
        lblTongChiTieu.setFont(Font.font(13));
        lblTongChiTieu.setFill(Color.web("#374151"));
        cbTongChiTieu = new ComboBox<>();
        cbTongChiTieu.setPrefSize(200, 40);
        cbTongChiTieu.setPromptText("Chọn tổng chi tiêu");
        cbTongChiTieu.getStyleClass().add("combo-box");
        vbTongChiTieu.getChildren().addAll(lblTongChiTieu, cbTongChiTieu);

        filterPane.getChildren().addAll(vbSoDonHang, vbTrangThai, vbTongChiTieu);

        // Search bar
        HBox searchBox = new HBox(10);
        searchBox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        txtSearchEmployee = new TextField();
        txtSearchEmployee.setPrefSize(300,40);
        txtSearchEmployee.setPromptText("Tìm kiếm khách hàng...");
        txtSearchEmployee.setStyle("-fx-background-color: white; -fx-border-color: #e5e7eb; -fx-border-radius: 8px;");

        btnSearchEmployee = new Button();
        btnSearchEmployee.setPrefSize(50,40);
        btnSearchEmployee.setStyle("-fx-background-color: #2563eb; -fx-background-radius: 5px;");
        btnSearchEmployee.setTextFill(Color.WHITE);
        FontAwesomeIcon searchIcon = new FontAwesomeIcon();
        searchIcon.setIcon(FontAwesomeIcons.SEARCH);
        searchIcon.setFill(Color.WHITE);
        btnSearchEmployee.setGraphic(searchIcon);

        searchBox.getChildren().addAll(txtSearchEmployee, btnSearchEmployee);

        // TableView
        tableViewKhachHang = new TableView<>();
        tableViewKhachHang.setPrefSize(867, 400);

        tableViewKhachHang.getColumns().addAll(colMaKH, colTenKH, colSoDienThoai, colSoDonHang, colTongChiTieu, colDonHangGanNhat, colLoaiKhachHang);
        tableViewKhachHang.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Hint button
        Button hintBtn = new Button("Nhấn 2 lần chuột trái vào bảng để xem chi tiết");
        hintBtn.setMaxWidth(Double.MAX_VALUE);
        hintBtn.getStyleClass().add("pane-huongdan");
        hintBtn.setTextFill(Color.web("#2563eb"));
        hintBtn.setPadding(new Insets(10));
        FontAwesomeIcon infoIcon = new FontAwesomeIcon();
        infoIcon.setGlyphName("INFO");
        infoIcon.setFill(Color.web("#2563eb"));
        hintBtn.setGraphic(infoIcon);

        VBox tableBox = new VBox(tableViewKhachHang, hintBtn);

        // Assemble root
        root.getChildren().addAll(header, filterPane, searchBox, tableBox);

        this.getStylesheets().add(getClass().getResource("/com/antam/app/styles/dashboard_style.css").toExternalForm());
        this.setContent(root);
        
        /** Sự kiện **/
        // Service instance to manage business logic and data access
        clientManager = ClientManager.getInstance();
        formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        // Setup table columns with property binding
        setupTableColumns();

        // Load data from Service (already includes statistics)
        loadDataFromService();

        // Setup event handlers for user interactions
        setupEventHandlers();

        // Setup ComboBox filter options
        setupComboBoxes();
    }

    /**
     * Thiết lập các cột của bảng với property binding
     */
    private void setupTableColumns() {
        colMaKH.setCellValueFactory(new PropertyValueFactory<>("maKH"));
        colTenKH.setCellValueFactory(new PropertyValueFactory<>("tenKH"));
        colSoDienThoai.setCellValueFactory(new PropertyValueFactory<>("soDienThoai"));
        colSoDonHang.setCellValueFactory(new PropertyValueFactory<>("soDonHang"));

        // Format Tổng chi tiêu column as VND currency
        colTongChiTieu.setCellValueFactory(new PropertyValueFactory<>("tongChiTieu"));
        colTongChiTieu.setCellFactory(column -> new TableCell<KhachHangDTO, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("%,.0f VNĐ", item));
                }
            }
        });

        // Format Ngày mua gần nhất column
        colDonHangGanNhat.setCellValueFactory(cellData -> {
            KhachHangDTO khachHang = cellData.getValue();
            String dateStr = khachHang.getNgayMuaGanNhat() != null
                ? khachHang.getNgayMuaGanNhat().format(formatter)
                : "N/A";
            return new javafx.beans.property.SimpleStringProperty(dateStr);
        });
        colLoaiKhachHang.setCellValueFactory(new PropertyValueFactory<>("loaiKhachHang"));
    }

    /**
     * Tải dữ liệu khách hàng từ Service
     * Service.loadKhachHangWithStats() returns DTOs with:
     * - soDonHang: calculated from HoaDon table
     * - tongChiTieu: sum of all invoice amounts
     * - ngayMuaGanNhat: latest purchase date
     */
    private void loadDataFromService() {
        try {
            // Service handles all data access and statistics calculation
            java.util.List<KhachHangDTO> listKhachHang = clientManager.loadKhachHangWithStats();

            // Convert to ObservableList for TableView binding
            dsKhachHangGoc = javafx.collections.FXCollections.observableArrayList(listKhachHang);
            dsKhachHang = javafx.collections.FXCollections.observableArrayList(listKhachHang);

            // Display data in table
            tableViewKhachHang.setItems(dsKhachHang);

        } catch (Exception exception) {
            showError("Lỗi tải dữ liệu", "Không thể tải dữ liệu khách hàng từ database: " + exception.getMessage());
        }
    }

    /**
     * Thiết lập các sự kiện cho UI
     */
    private void setupEventHandlers() {
        // Real-time search as user types
        txtSearchEmployee.textProperty().addListener((observable, oldValue, newValue) -> handleSearch());

        // Filter when ComboBox selection changes
        cbSoDonHang.setOnAction(event -> applyFilters());
        cbTrangThai.setOnAction(event -> applyFilters());
        cbTongChiTieu.setOnAction(event -> applyFilters());

        // Double-click on row to view customer details or restore
        tableViewKhachHang.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                KhachHangDTO selected = tableViewKhachHang.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    openChiTietKhachHangDialog(selected);
                }
            }
        });

        // Restore button action
        btnKhoiPhuc.setOnAction(event -> {
            KhachHangDTO selected = tableViewKhachHang.getSelectionModel().getSelectedItem();
            if (selected != null) {
                handleKhoiPhuc(selected);
            } else {
                showInfo("Thông báo", "Vui lòng chọn khách hàng để khôi phục");
            }
        });
    }

    /**
     * Thiết lập các giá trị cho ComboBox
     */
    private void setupComboBoxes() {
        // ComboBox Số đơn hàng
        ObservableList<String> soDonHangOptions = javafx.collections.FXCollections.observableArrayList(
            "Chọn số đơn hàng",
            "1-5 đơn hàng",
            "6-10 đơn hàng",
            "11-20 đơn hàng",
            "Trên 20 đơn hàng"
        );
        cbSoDonHang.setItems(soDonHangOptions);
        cbSoDonHang.setValue("Chọn số đơn hàng");

        // ComboBox Loại khách hàng
        ObservableList<String> trangThaiOptions = javafx.collections.FXCollections.observableArrayList(
            "Chọn loại khách hàng",
            "VIP",
            "Thường"
        );
        cbTrangThai.setItems(trangThaiOptions);
        cbTrangThai.setValue("Chọn loại khách hàng");

        // ComboBox Tổng chi tiêu
        ObservableList<String> tongChiTieuOptions = javafx.collections.FXCollections.observableArrayList(
            "Chọn tổng chi tiêu",
            "Dưới 500,000 VNĐ",
            "500,000 - 1,000,000 VNĐ",
            "1,000,000 - 5,000,000 VNĐ",
            "Trên 5,000,000 VNĐ"
        );
        cbTongChiTieu.setItems(tongChiTieuOptions);
        cbTongChiTieu.setValue("Chọn tổng chi tiêu");
    }

    /**
     * Xử lý sự kiện tìm kiếm theo tên, mã, số điện thoại
     */
    private void handleSearch() {
        String searchText = txtSearchEmployee.getText().toLowerCase().trim();

        if (searchText.isEmpty()) {
            dsKhachHang.setAll(dsKhachHangGoc);
        } else {
            java.util.List<KhachHangDTO> searchResult = dsKhachHangGoc.stream()
                .filter(khachHang -> khachHang.getMaKH().toLowerCase().contains(searchText)
                    || khachHang.getTenKH().toLowerCase().contains(searchText)
                    || khachHang.getSoDienThoai().contains(searchText))
                .collect(Collectors.toList());

            dsKhachHang.setAll(searchResult);
        }
    }

    /**
     * Áp dụng các bộ lọc từ ComboBox
     */
    private void applyFilters() {
        java.util.List<KhachHangDTO> filteredList = new java.util.ArrayList<>(dsKhachHangGoc);

        // Filter by số đơn hàng
        String selectedSoDonHang = cbSoDonHang.getValue();
        if (selectedSoDonHang != null && !selectedSoDonHang.equals("Chọn số đơn hàng")) {
            filteredList = filteredList.stream()
                .filter(khachHang -> filterBySoDonHang(khachHang, selectedSoDonHang))
                .collect(Collectors.toList());
        }

        // Filter by loại khách hàng (VIP/Thường)
        String selectedTrangThai = cbTrangThai.getValue();
        if (selectedTrangThai != null && !selectedTrangThai.equals("Chọn loại khách hàng")) {
            filteredList = filteredList.stream()
                .filter(khachHang -> khachHang.getLoaiKhachHang().equals(selectedTrangThai))
                .collect(Collectors.toList());
        }

        // Filter by tổng chi tiêu
        String selectedTongChiTieu = cbTongChiTieu.getValue();
        if (selectedTongChiTieu != null && !selectedTongChiTieu.equals("Chọn tổng chi tiêu")) {
            filteredList = filteredList.stream()
                .filter(khachHang -> filterByTongChiTieu(khachHang, selectedTongChiTieu))
                .collect(Collectors.toList());
        }

        dsKhachHang.setAll(filteredList);
    }

    /**
     * Lọc khách hàng theo số đơn hàng
     */
    private boolean filterBySoDonHang(KhachHangDTO khachHang, String range) {
        int soDonHang = khachHang.getSoDonHang();
        switch (range) {
            case "1-5 đơn hàng":
                return soDonHang >= 1 && soDonHang <= 5;
            case "6-10 đơn hàng":
                return soDonHang >= 6 && soDonHang <= 10;
            case "11-20 đơn hàng":
                return soDonHang >= 11 && soDonHang <= 20;
            case "Trên 20 đơn hàng":
                return soDonHang > 20;
            default:
                return true;
        }
    }

    /**
     * Lọc khách hàng theo tổng chi tiêu
     */
    private boolean filterByTongChiTieu(KhachHangDTO khachHang, String range) {
        double tongChiTieu = khachHang.getTongChiTieu();
        switch (range) {
            case "Dưới 500,000 VNĐ":
                return tongChiTieu < 500000;
            case "500,000 - 1,000,000 VNĐ":
                return tongChiTieu >= 500000 && tongChiTieu <= 1000000;
            case "1,000,000 - 5,000,000 VNĐ":
                return tongChiTieu > 1000000 && tongChiTieu <= 5000000;
            case "Trên 5,000,000 VNĐ":
                return tongChiTieu > 5000000;
            default:
                return true;
        }
    }

    /**
     * Xử lý khôi phục khách hàng (chuyển DeleteAt = 1 sang DeleteAt = 0)
     */
    private void handleKhoiPhuc(KhachHangDTO khachHangDTO) {
        // TODO: Implement restore logic - call Service to restore (set DeleteAt = 0)
        showInfo("Khôi phục", "Khôi phục khách hàng: " + khachHangDTO.getTenKH());
    }

    /**
     * Mở dialog để xem chi tiết khách hàng
     * @param khachHangDTO KhachHangDTO to display
     */
    private void openChiTietKhachHangDialog(KhachHangDTO khachHangDTO) {
        try {
            XemChiTietKhachHangController detailController = new XemChiTietKhachHangController();
            detailController.setKhachHang(khachHangDTO);

            // Create and show dialog
            Dialog<Void> dialog = new Dialog<>();
            dialog.setDialogPane(detailController);
            dialog.setTitle("Chi tiết khách hàng");
            dialog.showAndWait();

        } catch (Exception exception) {
            showError("Lỗi", "Không thể mở chi tiết khách hàng: " + exception.getMessage());
        }
    }

    /**
     * Display error message dialog
     * @param title Dialog title
     * @param message Error message content
     */
    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Display information message dialog
     * @param title Dialog title
     * @param message Information message content
     */
    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

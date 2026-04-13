/*
 * @ (#) TimKhachHangController.java   1.0 28/10/25
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */
package com.antam.app.controller.khachhang;

import com.antam.app.dao.I_KhachHang_DAO;
import com.antam.app.dao.impl.HoaDon_DAO;
import com.antam.app.dao.impl.KhachHang_DAO;
import com.antam.app.entity.HoaDon;
import com.antam.app.entity.KhachHang;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcons;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.net.URL;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/*
 * @description Controller for customer search functionality
 * @author: Tran Tuan Hung
 * @date: 28/10/25
 * @version: 1.0
 */
public class CapNhatKhachHangController extends ScrollPane {

    
    private Button btnEditEmployee;
    private TextField txtSearchEmployee;
    private Button btnSearchEmployee;
    private ComboBox<String> cbSoDonHang; // Số đơn hàng filter
    private ComboBox<String> cbTrangThai; // Loại khách hàng filter
    private ComboBox<String> cbTongChiTieu; // Tổng chi tiêu filter

    private TableView<KhachHang> tableViewKhachHang;
    private TableColumn<KhachHang, String> colMaKH = new TableColumn<>("Mã khách hàng");
    private TableColumn<KhachHang, String> colTenKH = new TableColumn<>("Tên khách hàng");
    private TableColumn<KhachHang, String> colSoDienThoai = new TableColumn<>("Số điện thoại");
    private TableColumn<KhachHang, Integer> colSoDonHang = new TableColumn<>("Số đơn hàng");
    private TableColumn<KhachHang, Double> colTongChiTieu = new TableColumn<>("Tổng chi tiêu");
    private TableColumn<KhachHang, String> colDonHangGanNhat = new TableColumn<>("Đơn hàng gần nhất");
    private TableColumn<KhachHang, String> colLoaiKhachHang = new TableColumn<>("Loại khách hàng");

    private ObservableList<KhachHang> dsKhachHang;
    private ObservableList<KhachHang> dsKhachHangGoc; // Để lưu danh sách gốc cho việc lọc

    private KhachHang_DAO khachHangDAO;
    private HoaDon_DAO hoaDonDAO;
    private DateTimeFormatter formatter;

    // Định dạng tiền tệ kiểu Việt Nam: 1.000đ, 10.000đ
    private static final DecimalFormat VND_FORMAT;
    static {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.');
        symbols.setDecimalSeparator(',');
        VND_FORMAT = new DecimalFormat("#,##0", symbols);
    }

    public CapNhatKhachHangController(){
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
        Text title = new Text("Cập nhật khách hàng");
        title.setFill(Color.web("#1e3a8a"));
        title.setFont(Font.font("System Bold", 30));
        Pane spacer = new Pane();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        btnEditEmployee = new Button("Cập nhật");
        btnEditEmployee.getStyleClass().add("btn-them");
        header.getChildren().addAll(title, spacer, btnEditEmployee);

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
        // Khởi tạo các đối tượng DAO
        khachHangDAO = new KhachHang_DAO();
        hoaDonDAO = new HoaDon_DAO();
        formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        // Thiết lập các cột của bảng
        setupTableColumns();

        // Tải dữ liệu từ database
        loadDataFromDB();

        // Thiết lập các sự kiện
        setupEventHandlers();

        // Thiết lập các giá trị cho ComboBox
        setupComboBoxes();
    }

    /**
     * Thiết lập các cột của bảng
     */
    private void setupTableColumns() {
        colMaKH.setCellValueFactory(new PropertyValueFactory<>("maKH"));
        colTenKH.setCellValueFactory(new PropertyValueFactory<>("tenKH"));
        colSoDienThoai.setCellValueFactory(new PropertyValueFactory<>("soDienThoai"));
        colSoDonHang.setCellValueFactory(new PropertyValueFactory<>("soDonHang"));

        // Định dạng cột Tổng chi tiêu theo VND
        colTongChiTieu.setCellValueFactory(new PropertyValueFactory<>("tongChiTieu"));
        colTongChiTieu.setCellFactory(column -> new TableCell<KhachHang, Double>() {
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

        colDonHangGanNhat.setCellValueFactory(cellData -> {
            KhachHang kh = cellData.getValue();
            String dateStr = kh.getNgayMuaGanNhat() != null
                    ? kh.getNgayMuaGanNhat().format(formatter)
                    : "N/A";
            return new javafx.beans.property.SimpleStringProperty(dateStr);
        });
        colLoaiKhachHang.setCellValueFactory(new PropertyValueFactory<>("loaiKhachHang"));
    }

    /**
     * Tải dữ liệu khách hàng từ database
     */
    private void loadDataFromDB() {
        try {
            // Lấy danh sách khách hàng từ database
            ArrayList<KhachHang> listKhachHang = I_KhachHang_DAO.loadBanFromDB();

            // Lấy danh sách hóa đơn để tính toán thống kê
            ArrayList<HoaDon> listHoaDon = hoaDonDAO.getAllHoaDon();

            // Tính toán thống kê cho mỗi khách hàng
            for (KhachHang kh : listKhachHang) {
                // Lọc các hóa đơn của khách hàng này
                List<HoaDon> hoaDonCuaKH = listHoaDon.stream()
                        .filter(hd -> hd.getMaKH().getMaKH().equals(kh.getMaKH()))
                        .collect(Collectors.toList());

                // Tính số đơn hàng
                kh.setSoDonHang(hoaDonCuaKH.size());

                // Tính tổng chi tiêu
                double tongChiTieu = hoaDonCuaKH.stream()
                        .mapToDouble(HoaDon::getTongTien)
                        .sum();
                kh.setTongChiTieu(tongChiTieu);

                // Lấy ngày mua gần nhất
                if (!hoaDonCuaKH.isEmpty()) {
                    java.time.LocalDate ngayGanNhat = hoaDonCuaKH.stream()
                            .map(HoaDon::getNgayTao)
                            .max(Comparator.naturalOrder())
                            .orElse(null);
                    kh.setNgayMuaGanNhat(ngayGanNhat);
                }
            }

            // Chuyển đổi sang ObservableList
            dsKhachHangGoc = FXCollections.observableArrayList(listKhachHang);
            dsKhachHang = FXCollections.observableArrayList(listKhachHang);

            // Gán dữ liệu cho TableView
            tableViewKhachHang.setItems(dsKhachHang);

        } catch (Exception e) {
            showError("Lỗi tải dữ liệu", "Không thể tải dữ liệu khách hàng từ database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Thiết lập các sự kiện cho UI
     */
    private void setupEventHandlers() {
        // Sự kiện tìm kiếm tự động khi gõ vào TextField
        txtSearchEmployee.textProperty().addListener((observable, oldValue, newValue) -> handleSearch());

        // Sự kiện khi thay đổi ComboBox
        cbSoDonHang.setOnAction(event -> applyFilters());
        cbTrangThai.setOnAction(event -> applyFilters());
        cbTongChiTieu.setOnAction(event -> applyFilters());

        // Sự kiện click nút Edit
        btnEditEmployee.setOnAction(event -> handleEditEmployee());

        // Sự kiện double-click trên hàng trong bảng để chuyển sang chi tiết
        tableViewKhachHang.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                KhachHang selected = tableViewKhachHang.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    openChiTietKhachHangDialog(selected);
                }
            }
        });
    }

    /**
     * Thiết lập các giá trị cho ComboBox
     */
    private void setupComboBoxes() {
        // ComboBox Số đơn hàng
        ObservableList<String> soDonHangOptions = FXCollections.observableArrayList(
                "Chọn số đơn hàng",
                "1-5 đơn hàng",
                "6-10 đơn hàng",
                "11-20 đơn hàng",
                "Trên 20 đơn hàng"
        );
        cbSoDonHang.setItems(soDonHangOptions);
        cbSoDonHang.setValue("Chọn số đơn hàng");

        // ComboBox Loại khách hàng
        ObservableList<String> trangThaiOptions = FXCollections.observableArrayList(
                "Chọn loại khách hàng",
                "VIP",
                "Thường"
        );
        cbTrangThai.setItems(trangThaiOptions);
        cbTrangThai.setValue("Chọn loại khách hàng");

        // ComboBox Tổng chi tiêu
        ObservableList<String> tongChiTieuOptions = FXCollections.observableArrayList(
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
     * Xử lý sự kiện tìm kiếm
     */
    private void handleSearch() {
        String searchText = txtSearchEmployee.getText().toLowerCase().trim();

        if (searchText.isEmpty()) {
            dsKhachHang.setAll(dsKhachHangGoc);
        } else {
            List<KhachHang> searchResult = dsKhachHangGoc.stream()
                    .filter(kh -> kh.getMaKH().toLowerCase().contains(searchText)
                            || kh.getTenKH().toLowerCase().contains(searchText)
                            || kh.getSoDienThoai().contains(searchText))
                    .collect(Collectors.toList());

            dsKhachHang.setAll(searchResult);
        }
    }

    /**
     * Áp dụng các bộ lọc
     */
    private void applyFilters() {
        List<KhachHang> filteredList = new ArrayList<>(dsKhachHangGoc);

        // Lọc theo số đơn hàng
        String selectedSoDonHang = cbSoDonHang.getValue();
        if (selectedSoDonHang != null && !selectedSoDonHang.equals("Chọn số đơn hàng")) {
            filteredList = filteredList.stream()
                    .filter(kh -> filterBySoDonHang(kh, selectedSoDonHang))
                    .collect(Collectors.toList());
        }

        // Lọc theo loại khách hàng
        String selectedTrangThai = cbTrangThai.getValue();
        if (selectedTrangThai != null && !selectedTrangThai.equals("Chọn loại khách hàng")) {
            filteredList = filteredList.stream()
                    .filter(kh -> kh.getLoaiKhachHang().equals(selectedTrangThai))
                    .collect(Collectors.toList());
        }

        // Lọc theo tổng chi tiêu
        String selectedTongChiTieu = cbTongChiTieu.getValue();
        if (selectedTongChiTieu != null && !selectedTongChiTieu.equals("Chọn tổng chi tiêu")) {
            filteredList = filteredList.stream()
                    .filter(kh -> filterByTongChiTieu(kh, selectedTongChiTieu))
                    .collect(Collectors.toList());
        }

        dsKhachHang.setAll(filteredList);
    }

    /**
     * Lọc khách hàng theo số đơn hàng
     */
    private boolean filterBySoDonHang(KhachHang kh, String range) {
        int soDonHang = kh.getSoDonHang();
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
    private boolean filterByTongChiTieu(KhachHang kh, String range) {
        double tongChiTieu = kh.getTongChiTieu();
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
     * Xử lý sự kiện double-click trên hàng
     */
    private void handleRowDoubleClick() {
        KhachHang selected = tableViewKhachHang.getSelectionModel().getSelectedItem();
        if (selected != null) {
            openChiTietKhachHangDialog(selected);
        }
    }

    /**
     * Mở dialog xem chi tiết khách hàng
     */
    private void openChiTietKhachHangDialog(KhachHang khachHang) {
        try {
            XemChiTietKhachHangController xemDialog = new XemChiTietKhachHangController();
            xemDialog.setKhachHang(khachHang);

            // Tạo Dialog
            javafx.scene.control.Dialog<Void> dialog = new javafx.scene.control.Dialog<>();
            dialog.setDialogPane(xemDialog);
            dialog.setTitle("Chi tiết khách hàng");
            dialog.showAndWait();

        } catch (Exception e) {
            showError("Lỗi", "Không thể mở chi tiết khách hàng: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Hiển thị lỗi
     */
    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }



    /**
     * Hiển thị thông tin
     */
    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Xử lý sự kiện click nút Edit Employee
     */
    private void handleEditEmployee() {
        KhachHang selected = tableViewKhachHang.getSelectionModel().getSelectedItem();

        // Kiểm tra xem có chọn khách hàng trong bảng không
        if (selected == null) {
            showInfo("Thông báo", "Vui lòng chọn khách hàng để sửa");
            return;
        }

        // Mở dialog cập nhật khách hàng
        openCapNhatKhachHangDialog(selected);
    }

    /**
     * Mở dialog cập nhật thông tin khách hàng
     */
    private void openCapNhatKhachHangDialog(KhachHang khachHang) {
        try {
            SuaKhachHangFormController suaDialog = new SuaKhachHangFormController();
            suaDialog.setKhachHang(khachHang);
            suaDialog.setOnSaveListener(() -> {
                loadDataFromDB();
            });

            // Tạo Dialog
            javafx.scene.control.Dialog<Void> dialog = new javafx.scene.control.Dialog<>();
            dialog.setDialogPane(suaDialog);
            dialog.setTitle("Cập nhật khách hàng");


            dialog.showAndWait();

        } catch (Exception e) {
            showError("Lỗi", "Không thể mở dialog cập nhật khách hàng: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

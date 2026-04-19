package com.antam.app.controller.hoadon;


import com.antam.app.service.impl.ThongKeDoanhThu_Service;
import com.antam.app.service.impl.ThongKeTrangChinh_Service;
import com.antam.app.dto.NhanVienDTO;
import com.antam.app.dto.ThongKeDoanhThuDTO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Map;
import java.util.ResourceBundle;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.geometry.Pos;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.geometry.Insets;
import javafx.scene.paint.Color;


public class ThongKeDoanhThuController extends ScrollPane {

    private Button btnXuatBaoCao;
    private ComboBox<String> cmbThoiGian;
    private ComboBox<String> cmbNhanVien;

    private Text txtDoanhThuKy;
    private Text txtSoDonHang;
    private Text txtDonHangTB;
    private Text txtSoKhachHang;

    private Button btnDoanhThuChange;
    private Button btnDonHangChange;
    private Button btnDonHangTBChange;
    private Button btnKhachHangChange;

    private LineChart<String, Number> chartDoanhThu;
    private BarChart<String, Number> chartTopSanPham;

    private TableView<ThongKeDoanhThuDTO> tableChiTiet;
    private TableColumn<ThongKeDoanhThuDTO, LocalDate> colNgay;
    private TableColumn<ThongKeDoanhThuDTO, Integer> colSoDonHang;
    private TableColumn<ThongKeDoanhThuDTO, Double> colDoanhThu;
    private TableColumn<ThongKeDoanhThuDTO, Double> colDonHangTB;
    private TableColumn<ThongKeDoanhThuDTO, Integer> colKhachHangMoi;
    private TableColumn<ThongKeDoanhThuDTO, String> colNhanVienBan;

    private Button btnRefresh;

    private ThongKeDoanhThu_Service thongKe_service;
    private ThongKeTrangChinh_Service thongKeTrangChinh_service;
    private LocalDate tuNgay;
    private LocalDate denNgay;
    private String selectedNhanVien = null;
    private DecimalFormat formatter = new DecimalFormat("#,###");
    private DecimalFormat percentFormatter = new DecimalFormat("#0.0");

    public ThongKeDoanhThuController(){
        this.setFitToHeight(true);
        this.setFitToWidth(true);
        this.setPrefSize(900, 730);
        AnchorPane.setTopAnchor(this, 0.0);
        AnchorPane.setBottomAnchor(this, 0.0);
        AnchorPane.setLeftAnchor(this, 0.0);
        AnchorPane.setRightAnchor(this, 0.0);

        VBox root = new VBox(30);
        root.setStyle("-fx-background-color: #f8fafc;");
        setContent(root);

        // ---------------------- HEADER ----------------------
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);

        Text title = new Text("Thống kê doanh thu");
        title.setFill(Color.web("#1e3a8a"));
        title.setFont(Font.font("System Bold", 30));

        Region space = new Region();
        HBox.setHgrow(space, Priority.ALWAYS);

        btnXuatBaoCao = new Button("Xuất báo cáo");
        btnXuatBaoCao.setPrefSize(120, 50);
        btnXuatBaoCao.getStyleClass().add("btn-gray");
        btnXuatBaoCao.setTextFill(Color.WHITE);

        FontAwesomeIcon iconDownload = new FontAwesomeIcon();
        iconDownload.setGlyphName("CLOUD_DOWNLOAD");
        iconDownload.setFill(Color.WHITE);
        btnXuatBaoCao.setGraphic(iconDownload);

        header.getChildren().addAll(title, space, btnXuatBaoCao);

        // ---------------------- FILTER PANE ----------------------
        FlowPane filterPane = new FlowPane(5, 5);
        filterPane.setPadding(new Insets(10));
        filterPane.getStyleClass().add("box-pane");
        filterPane.setEffect(new DropShadow(20, Color.rgb(211, 211, 211)));

        // TIME COMBOBOX
        VBox vbTime = new VBox(5);
        Text txtTime = new Text("Thời gian:");
        txtTime.setFill(Color.web("#374151"));
        cmbThoiGian = new ComboBox<>();
        cmbThoiGian.setPrefSize(200, 40);
        cmbThoiGian.getStyleClass().add("combo-box");

        vbTime.getChildren().addAll(txtTime, cmbThoiGian);

        // EMPLOYEE COMBOBOX
        VBox vbNV = new VBox(5);
        Text txtNV = new Text("Nhân viên:");
        txtNV.setFill(Color.web("#374151"));
        cmbNhanVien = new ComboBox<>();
        cmbNhanVien.setPrefSize(200, 40);
        cmbNhanVien.getStyleClass().add("combo-box");

        vbNV.getChildren().addAll(txtNV, cmbNhanVien);

        filterPane.getChildren().addAll(vbTime, vbNV);

        // ---------------------- STAT CARDS ----------------------
        FlowPane cards = new FlowPane(30, 20);
        cards.setAlignment(Pos.CENTER);

        // Card 1 – DOANH THU KY
        StackPane card1 = createCard(
                "#059669",
                "MONEY",
                txtDoanhThuKy = new Text("0"),
                new Text("Doanh thu kỳ"),
                btnDoanhThuChange = new Button("0%")
        );
        // Card 2 – DON HANG
        StackPane card2 = createCard(
                "#2563eb",
                "FILE",
                txtSoDonHang = new Text("0"),
                new Text("Đơn hàng"),
                btnDonHangChange = new Button("0%")
        );
        // Card 3 – DON HANG TB
        StackPane card3 = createCard(
                "#8b5cf6",
                "CALCULATOR",
                txtDonHangTB = new Text("0"),
                new Text("Đơn hàng TB"),
                btnDonHangTBChange = new Button("0%")
        );
        // Card 4 – KHÁCH HÀNG
        StackPane card4 = createCard(
                "#f59e0b",
                "USERS",
                txtSoKhachHang = new Text("0"),
                new Text("Khách hàng"),
                btnKhachHangChange = new Button("0%")
        );

        cards.getChildren().addAll(card1, card2, card3, card4);

        // ---------------------- CHARTS ----------------------
        FlowPane charts = new FlowPane(20, 20);
        charts.setAlignment(Pos.TOP_CENTER);

        // LINE CHART
        CategoryAxis x1 = new CategoryAxis();
        NumberAxis y1 = new NumberAxis();
        chartDoanhThu = new LineChart<>(x1, y1);

        VBox vbChart1 = wrapChart("Doanh thu theo thời gian", chartDoanhThu);

        // BAR CHART
        CategoryAxis x2 = new CategoryAxis();
        NumberAxis y2 = new NumberAxis();
        chartTopSanPham = new BarChart<>(x2, y2);

        VBox vbChart2 = wrapChart("Top sản phẩm bán chạy", chartTopSanPham);
        charts.getChildren().addAll(vbChart1, vbChart2);

        // ---------------------- TABLE ----------------------
        AnchorPane tablePane = new AnchorPane();
        tablePane.getStyleClass().add("box-pane");

        VBox vbTable = new VBox(5);
        vbTable.setPadding(new Insets(5));

        HBox tbHeader = new HBox();
        Text tbTitle = new Text("Chi tiết doanh thu");
        tbTitle.setFont(Font.font("System Bold", 20));
        tbTitle.setFill(Color.web("#1e3a8a"));

        Region tbSpace = new Region();
        HBox.setHgrow(tbSpace, Priority.ALWAYS);

        btnRefresh = new Button();
        btnRefresh.setPrefSize(50, 40);
        btnRefresh.setStyle("-fx-background-color: #6b7280; -fx-border-radius: 8px;");

        FontAwesomeIcon refreshIcon = new FontAwesomeIcon();
        refreshIcon.setGlyphName("REFRESH");
        refreshIcon.setFill(Color.WHITE);
        btnRefresh.setGraphic(refreshIcon);

        tbHeader.getChildren().addAll(tbTitle, tbSpace, btnRefresh);

        // TableView
        tableChiTiet = new TableView<>();
        tableChiTiet.setPrefHeight(500);

        colNgay = new TableColumn<>("Ngày");
        colSoDonHang = new TableColumn<>("Số đơn hàng");
        colDoanhThu = new TableColumn<>("Doanh thu");
        colDonHangTB = new TableColumn<>("Đơn hàng TB");
        colKhachHangMoi = new TableColumn<>("Khách hàng mới");
        colNhanVienBan = new TableColumn<>("Nhân viên bán");

        tableChiTiet.getColumns().addAll(
                colNgay, colSoDonHang, colDoanhThu,
                colDonHangTB, colKhachHangMoi, colNhanVienBan
        );

        vbTable.getChildren().addAll(tbHeader, tableChiTiet);

        AnchorPane.setLeftAnchor(vbTable, 0.0);
        AnchorPane.setRightAnchor(vbTable, 0.0);
        tablePane.getChildren().add(vbTable);

        // ---------------- ADD ALL ----------------
        root.setPadding(new Insets(20));
        root.getChildren().addAll(header, filterPane, cards, charts, tablePane);
        this.getStylesheets().add(getClass().getResource("/com/antam/app/styles/dashboard_style.css").toExternalForm());
        this.getChildren().add(root);

        /** Sự kiện **/
        thongKe_service = new ThongKeDoanhThu_Service();
        thongKeTrangChinh_service = new ThongKeTrangChinh_Service();

        // Khởi tạo ComboBox thời gian
        cmbThoiGian.setItems(FXCollections.observableArrayList(
                "Hôm nay", "7 ngày qua", "30 ngày qua", "Tháng này", "Tháng trước", "Năm nay"
        ));
        cmbThoiGian.setValue("7 ngày qua");

        // Load danh sách nhân viên
        loadDanhSachNhanVien();

        // Khởi tạo bảng
        setupTableView();

        // Tính thời gian mặc định
        calculateDefaultTimeRange();

        // Load dữ liệu
        loadData();

        // Thêm event handlers
        cmbThoiGian.setOnAction(e -> locTheoThoiGian(e));
        cmbNhanVien.setOnAction(e -> locTheoNhanVien(e));
        btnRefresh.setOnAction(e->refreshData());
        btnXuatBaoCao.setOnAction(e->xuatBaoCao());

    }

    //
    private StackPane createCard(String color, String icon, Text bigText, Text label, Button changeBtn) {

        StackPane card = new StackPane();
        card.setPrefSize(250, 121);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 20px;");
        card.setEffect(new DropShadow(10, Color.rgb(212, 212, 212)));

        HBox box = new HBox(10);
        box.setAlignment(Pos.CENTER_LEFT);
        box.setPadding(new Insets(0, 20, 0, 20));

        Button iconBtn = new Button();
        iconBtn.setPrefSize(60, 60);
        iconBtn.setStyle("-fx-background-color: " + color + "; -fx-background-radius: 15px;");

        FontAwesomeIcon iconNode = new FontAwesomeIcon();
        iconNode.setGlyphName(icon);
        iconNode.setFill(Color.WHITE);
        iconNode.setSize("2em");
        iconBtn.setGraphic(iconNode);

        VBox texts = new VBox();
        texts.setPrefHeight(60);
        texts.setAlignment(Pos.CENTER_LEFT);
        bigText.setFont(Font.font("System Bold", 32));
        bigText.setFill(Color.web("#1e3a8a"));

        label.setFill(Color.web("#64748b"));
        changeBtn.setStyle("-fx-background-radius: 50px;");

        texts.getChildren().addAll(bigText, label, changeBtn);

        box.getChildren().addAll(iconBtn, texts);
        card.getChildren().add(box);

        return card;
    }

    // ---------------------------------------------------------
    //  HELPER: tạo chart wrapper box-pane
    // ---------------------------------------------------------
    private VBox wrapChart(String titleStr, Node chart) {
        VBox box = new VBox(10);
        box.setPrefWidth(500);
        box.setPadding(new Insets(10));
        box.getStyleClass().add("box-pane");
        box.setEffect(new DropShadow(12, Color.rgb(218, 218, 218)));

        Text title = new Text(titleStr);
        title.setFont(Font.font("System Bold", 20));
        title.setFill(Color.web("#1e3a8a"));

        box.getChildren().addAll(title, chart);
        return box;
    }

    private void setupTableView() {
        // Thêm placeholder cho bảng khi không có dữ liệu
        Label placeholderLabel = new Label("Không có dữ liệu");
        placeholderLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #64748b;");
        tableChiTiet.setPlaceholder(placeholderLabel);

        colNgay.setCellValueFactory(new PropertyValueFactory<>("ngay"));
        colNgay.setCellFactory(column -> new TableCell<ThongKeDoanhThuDTO, LocalDate>() {
            @Override
            protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                }
            }
        });

        colSoDonHang.setCellValueFactory(new PropertyValueFactory<>("soDonHang"));

        colDoanhThu.setCellValueFactory(new PropertyValueFactory<>("doanhThu"));
        colDoanhThu.setCellFactory(column -> new TableCell<ThongKeDoanhThuDTO, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(formatter.format(item) + " đ");
                }
            }
        });

        colDonHangTB.setCellValueFactory(new PropertyValueFactory<>("donHangTB"));
        colDonHangTB.setCellFactory(column -> new TableCell<ThongKeDoanhThuDTO, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(formatter.format(item) + " đ");
                }
            }
        });

        colKhachHangMoi.setCellValueFactory(new PropertyValueFactory<>("khachHangMoi"));
        colNhanVienBan.setCellValueFactory(new PropertyValueFactory<>("nhanVienBan"));
    }

    private void loadDanhSachNhanVien() {
        ArrayList<NhanVienDTO> dsNhanVien = thongKe_service.getDanhSachNhanVien();
        ObservableList<String> items = FXCollections.observableArrayList("Tất cả");
        for (NhanVienDTO nv : dsNhanVien) {
            items.add(nv.getMaNV() + " - " + nv.getHoTen());
        }
        cmbNhanVien.setItems(items);
        cmbNhanVien.setValue("Tất cả");
    }

    private void calculateDefaultTimeRange() {
        String thoiGian = cmbThoiGian.getValue();
        denNgay = LocalDate.now();

        switch (thoiGian) {
            case "Hôm nay":
                tuNgay = LocalDate.now();
                break;
            case "7 ngày qua":
                tuNgay = LocalDate.now().minusDays(6);
                break;
            case "30 ngày qua":
                tuNgay = LocalDate.now().minusDays(29);
                break;
            case "Tháng này":
                tuNgay = LocalDate.now().withDayOfMonth(1);
                break;
            case "Tháng trước":
                tuNgay = LocalDate.now().minusMonths(1).withDayOfMonth(1);
                denNgay = LocalDate.now().withDayOfMonth(1).minusDays(1);
                break;
            case "Năm nay":
                tuNgay = LocalDate.now().withDayOfYear(1);
                break;
            default:
                tuNgay = LocalDate.now().minusDays(6);
        }
    }


    private void locTheoThoiGian(ActionEvent event) {
        calculateDefaultTimeRange();
        loadData();
    }


    private void locTheoNhanVien(ActionEvent event) {
        String selected = cmbNhanVien.getValue();
        if (selected != null && !selected.equals("Tất cả")) {
            selectedNhanVien = selected.split(" - ")[0];
        } else {
            selectedNhanVien = null;
        }
        loadData();
    }


    private void refreshData() {
        loadData();
    }

    private void loadData() {
        // Load dữ liệu tổng quan
        loadTongQuan();

        // Load biểu đồ
        loadChartDoanhThu();
        loadTopProductsChart();

        // Load bảng chi tiết
        loadTableData();
    }

    private void loadTongQuan() {
        double tongDoanhThu = thongKe_service.getTongDoanhThu(tuNgay, denNgay, selectedNhanVien);
        int tongDonHang = thongKe_service.getTongDonHang(tuNgay, denNgay, selectedNhanVien);
        int soKhachHang = thongKe_service.getSoKhachHangMoi(tuNgay, denNgay, selectedNhanVien);
        double donHangTB = tongDonHang > 0 ? tongDoanhThu / tongDonHang : 0;

        // Cập nhật text
        txtDoanhThuKy.setText(formatCurrency(tongDoanhThu));
        txtSoDonHang.setText(String.valueOf(tongDonHang));
        txtDonHangTB.setText(formatCurrency(donHangTB));
        txtSoKhachHang.setText(String.valueOf(soKhachHang));

        // Tính % thay đổi so với kỳ trước (đơn giản hóa - có thể cải thiện)
        calculateChange();
    }

    /*
     * Tính phần trăm thay đổi so với kỳ trước (thời gian) và cập nhật nút tương ứng
     * ((Giá trị hiện tại - Giá trị trước) / Giá trị trước) × 100
     * */
    private void calculateChange() {
        // Tính kỳ trước
        long days = java.time.temporal.ChronoUnit.DAYS.between(tuNgay, denNgay) + 1;
        LocalDate tuNgayTruoc = tuNgay.minusDays(days);
        LocalDate denNgayTruoc = denNgay.minusDays(days);

        double doanhThuHienTai = thongKe_service.getTongDoanhThu(tuNgay, denNgay, selectedNhanVien);
        double doanhThuTruoc = thongKe_service.getTongDoanhThu(tuNgayTruoc, denNgayTruoc, selectedNhanVien);

        int donHangHienTai = thongKe_service.getTongDonHang(tuNgay, denNgay, selectedNhanVien);
        int donHangTruoc = thongKe_service.getTongDonHang(tuNgayTruoc, denNgayTruoc, selectedNhanVien);

        int khachHangHienTai = thongKe_service.getSoKhachHangMoi(tuNgay, denNgay, selectedNhanVien);
        int khachHangTruoc = thongKe_service.getSoKhachHangMoi(tuNgayTruoc, denNgayTruoc, selectedNhanVien);

        updateChangeButton(btnDoanhThuChange, doanhThuHienTai, doanhThuTruoc);
        updateChangeButton(btnDonHangChange, donHangHienTai, donHangTruoc);
        updateChangeButton(btnDonHangTBChange,
                donHangHienTai > 0 ? doanhThuHienTai / donHangHienTai : 0,
                donHangTruoc > 0 ? doanhThuTruoc / donHangTruoc : 0);
        updateChangeButton(btnKhachHangChange, khachHangHienTai, khachHangTruoc);
    }

    /*
     *  Cập nhật nút thay đổi với phần trăm và màu sắc tương ứng
     * */
    private void updateChangeButton(Button btn, double current, double previous) {
        if (previous == 0) {
            btn.setText("N/A");
            return;
        }

        double change = ((current - previous) / previous) * 100;
        String text = (change >= 0 ? "+" : "") + percentFormatter.format(change) + "%";
        btn.setText(text);

        if (change >= 0) {
            btn.setStyle("-fx-background-color: #d1fae5; -fx-text-fill: #059669; -fx-background-radius: 50px;");
        } else {
            btn.setStyle("-fx-background-color: #fee2e2; -fx-text-fill: #dc2626; -fx-background-radius: 50px;");
        }
    }

    private void loadChartDoanhThu() {
        chartDoanhThu.getData().clear();

        // Kiểm tra xem có phải hiển thị theo tháng không (cho khoảng thời gian dài như năm)
        String thoiGian = cmbThoiGian.getValue();
        boolean hienThiTheoThang = "Năm nay".equals(thoiGian);

        ArrayList<ThongKeDoanhThuDTO> dsThongKe;
        if (hienThiTheoThang) {
            // Lấy dữ liệu theo tháng
            dsThongKe = thongKe_service.getDoanhThuTheoThang(tuNgay, denNgay, selectedNhanVien);
        } else {
            // Lấy dữ liệu theo ngày
            dsThongKe = thongKe_service.getDoanhThuTheoThoiGian(tuNgay, denNgay, selectedNhanVien);
        }

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Doanh thu");

        DateTimeFormatter dateFormatter = hienThiTheoThang
                ? DateTimeFormatter.ofPattern("MM/yyyy")
                : DateTimeFormatter.ofPattern("dd/MM");
        DateTimeFormatter fullDateFormatter = hienThiTheoThang
                ? DateTimeFormatter.ofPattern("'Tháng' MM/yyyy")
                : DateTimeFormatter.ofPattern("dd/MM/yyyy");

        if (hienThiTheoThang) {
            // Hiển thị theo tháng - tạo đầy đủ các tháng trong khoảng thời gian
            Map<String, ThongKeDoanhThuDTO> dataMap = new java.util.HashMap<>();
            for (ThongKeDoanhThuDTO tk : dsThongKe) {
                String monthKey = tk.getNgay().format(dateFormatter);
                dataMap.put(monthKey, tk);
            }

            Map<String, ThongKeDoanhThuDTO> displayMap = new java.util.LinkedHashMap<>();

            // Tạo đầy đủ các tháng từ tuNgay đến denNgay
            LocalDate currentMonth = tuNgay.withDayOfMonth(1);
            LocalDate endMonth = denNgay.withDayOfMonth(1);

            while (!currentMonth.isAfter(endMonth)) {
                String monthKey = currentMonth.format(dateFormatter);
                ThongKeDoanhThuDTO tk = dataMap.get(monthKey);

                if (tk == null) {
                    // Tạo dữ liệu rỗng cho tháng không có data
                    tk = new ThongKeDoanhThuDTO(currentMonth, 0, 0.0, 0.0, 0, "");
                }

                displayMap.put(monthKey, tk);
                series.getData().add(new XYChart.Data<>(monthKey, tk.getDoanhThu()));

                currentMonth = currentMonth.plusMonths(1);
            }

            chartDoanhThu.getData().add(series);

            // Thêm tooltip cho từng điểm dữ liệu
            for (XYChart.Data<String, Number> data : series.getData()) {
                ThongKeDoanhThuDTO tk = displayMap.get(data.getXValue());
                if (tk != null) {
                    Tooltip tooltip = new Tooltip(
                            tk.getNgay().format(fullDateFormatter) + "\n" +
                                    "Doanh thu: " + formatter.format(data.getYValue().doubleValue()) + " đ\n" +
                                    "Số đơn hàng: " + tk.getSoDonHang() + "\n" +
                                    "Đơn hàng TB: " + formatter.format(tk.getDonHangTB()) + " đ\n" +
                                    "Khách hàng: " + tk.getKhachHangMoi()
                    );

                    tooltip.setShowDelay(javafx.util.Duration.millis(100));
                    tooltip.setStyle(
                            "-fx-font-size: 12px; " +
                                    "-fx-background-color: rgba(30, 58, 138, 0.95); " +
                                    "-fx-text-fill: white; " +
                                    "-fx-padding: 8px; " +
                                    "-fx-background-radius: 6px;"
                    );

                    Node node = data.getNode();
                    if (node != null) {
                        Tooltip.install(node, tooltip);

                        node.setOnMouseEntered(e -> {
                            node.setStyle(
                                    "-fx-cursor: hand; " +
                                            "-fx-background-color: #059669; " +
                                            "-fx-background-radius: 8px;"
                            );
                            node.setScaleX(1.5);
                            node.setScaleY(1.5);
                        });
                        node.setOnMouseExited(e -> {
                            node.setStyle("");
                            node.setScaleX(1.0);
                            node.setScaleY(1.0);
                        });
                    }
                }
            }
        } else {
            // Hiển thị theo ngày - tạo đầy đủ các ngày
            Map<LocalDate, ThongKeDoanhThuDTO> dataMap = new java.util.HashMap<>();
            for (ThongKeDoanhThuDTO tk : dsThongKe) {
                dataMap.put(tk.getNgay(), tk);
            }

            Map<String, ThongKeDoanhThuDTO> displayMap = new java.util.LinkedHashMap<>();

            LocalDate currentDate = tuNgay;
            while (!currentDate.isAfter(denNgay)) {
                ThongKeDoanhThuDTO tk = dataMap.get(currentDate);

                if (tk == null) {
                    tk = new ThongKeDoanhThuDTO(currentDate, 0, 0.0, 0.0, 0, "");
                }

                String shortDate = currentDate.format(dateFormatter);
                displayMap.put(shortDate, tk);
                series.getData().add(new XYChart.Data<>(shortDate, tk.getDoanhThu()));

                currentDate = currentDate.plusDays(1);
            }

            chartDoanhThu.getData().add(series);

            // Thêm tooltip cho từng điểm dữ liệu
            for (XYChart.Data<String, Number> data : series.getData()) {
                ThongKeDoanhThuDTO tk = displayMap.get(data.getXValue());
                if (tk != null) {
                    Tooltip tooltip = new Tooltip(
                            "Ngày: " + tk.getNgay().format(fullDateFormatter) + "\n" +
                                    "Doanh thu: " + formatter.format(data.getYValue().doubleValue()) + " đ\n" +
                                    "Số đơn hàng: " + tk.getSoDonHang() + "\n" +
                                    "Đơn hàng TB: " + formatter.format(tk.getDonHangTB()) + " đ\n" +
                                    "Khách hàng: " + tk.getKhachHangMoi()
                    );

                    tooltip.setShowDelay(javafx.util.Duration.millis(100));
                    tooltip.setStyle(
                            "-fx-font-size: 12px; " +
                                    "-fx-background-color: rgba(30, 58, 138, 0.95); " +
                                    "-fx-text-fill: white; " +
                                    "-fx-padding: 8px; " +
                                    "-fx-background-radius: 6px;"
                    );

                    Node node = data.getNode();
                    if (node != null) {
                        Tooltip.install(node, tooltip);

                        node.setOnMouseEntered(e -> {
                            node.setStyle(
                                    "-fx-cursor: hand; " +
                                            "-fx-background-color: #059669; " +
                                            "-fx-background-radius: 8px;"
                            );
                            node.setScaleX(1.5);
                            node.setScaleY(1.5);
                        });
                        node.setOnMouseExited(e -> {
                            node.setStyle("");
                            node.setScaleX(1.0);
                            node.setScaleY(1.0);
                        });
                    }
                }
            }
        }

        // Tùy chỉnh style cho LineChart
        chartDoanhThu.setCreateSymbols(true);
        chartDoanhThu.setLegendVisible(true);
    }

    /**
     * ==================== BIỂU ĐỒ TOP SẢN PHẨM ====================
     * Load biểu đồ top sản phẩm bán chạy
     */
    private void loadTopProductsChart() {
        if (chartTopSanPham == null) return;

        try {
            // *** CHỈNH SỐ SẢN PHẨM: Thay đổi số 5 để hiển thị nhiều/ít sản phẩm hơn ***
            Map<String, Integer> topProducts = thongKeTrangChinh_service.getTopSanPhamBanChay(5);

            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Số lượng bán");

            // Map để lưu tên đầy đủ của thuốc
            Map<String, String> fullNamesMap = new java.util.HashMap<>();

            for (Map.Entry<String, Integer> entry : topProducts.entrySet()) {
                String tenThuocDayDu = entry.getKey();
                String tenThuoc = tenThuocDayDu;

                // *** CHỈNH ĐỘ DÀI TÊN: Thay đổi số 15 (độ dài tối đa) và 12 (độ dài cắt) ***
                // Rút ngắn tên thuốc nếu quá dài
                if (tenThuoc.length() > 15) {
                    tenThuoc = tenThuoc.substring(0, 12) + "...";
                }

                // Lưu tên đầy đủ
                fullNamesMap.put(tenThuoc, tenThuocDayDu);

                XYChart.Data<String, Number> data = new XYChart.Data<>(tenThuoc, entry.getValue());
                series.getData().add(data);
            }

            chartTopSanPham.getData().clear();
            chartTopSanPham.getData().add(series);

            // Thêm tooltip cho từng thanh trong biểu đồ
            for (XYChart.Data<String, Number> data : series.getData()) {
                String tenThuocHienThi = data.getXValue();
                String tenThuocDayDu = fullNamesMap.getOrDefault(tenThuocHienThi, tenThuocHienThi);
                int soLuong = data.getYValue().intValue();

                Tooltip tooltip = new Tooltip(
                        "Thuốc: " + tenThuocDayDu + "\n" +
                                "Số lượng bán: " + formatter.format(soLuong)
                );

                // *** CHỈNH MÀU TOOLTIP: Thay đổi màu nền và màu chữ ***
                tooltip.setShowDelay(javafx.util.Duration.millis(100));
                tooltip.setStyle(
                        "-fx-font-size: 12px; " +
                                "-fx-background-color: #1e3a8a; " +    // Màu nền tooltip (xanh lá)
                                "-fx-text-fill: white;"                 // Màu chữ tooltip (trắng)
                );

                // Gắn tooltip vào node của data point (thanh)
                Node node = data.getNode();
                if (node != null) {
                    Tooltip.install(node, tooltip);

                    // *** CHỈNH HIỆU ỨNG HOVER: Thay đổi độ mờ khi rê chuột ***
                    node.setOnMouseEntered(e -> {
                        node.setStyle(
                                "-fx-cursor: hand; " +
                                        "-fx-opacity: 0.8;"             // Độ mờ khi hover (0.0 - 1.0)
                        );
                    });
                    node.setOnMouseExited(e -> {
                        node.setStyle("-fx-opacity: 1.0;");
                    });
                }
            }

        } catch (Exception e) {
            System.err.println("Error loading top products chart: " + e.getMessage());
        }
    }

    private void loadTableData() {
        ArrayList<ThongKeDoanhThuDTO> dsThongKe = thongKe_service.getDoanhThuTheoThoiGian(tuNgay, denNgay, selectedNhanVien);
        ObservableList<ThongKeDoanhThuDTO> data = FXCollections.observableArrayList(dsThongKe);
        tableChiTiet.setItems(data);
    }


    //    Xuất báo cáo thống kê doanh thu ra file CSV
    private void xuatBaoCao() {
        try {
            // Tạo FileChooser để chọn nơi lưu file
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Xuất báo cáo thống kê doanh thu");
            fileChooser.setInitialFileName("BaoCaoDoanhThu_" + LocalDate.now().format(DateTimeFormatter.ofPattern("ddMMyyyy")) + ".csv");

            // Thiết lập filter cho file CSV
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");
            fileChooser.getExtensionFilters().add(extFilter);

            // Hiển thị dialog để chọn nơi lưu
            File file = fileChooser.showSaveDialog(btnXuatBaoCao.getScene().getWindow());

            if (file != null) {
                // Xuất dữ liệu ra file CSV
                exportToCSV(file);

                // Hiển thị thông báo thành công
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Xuất báo cáo thành công");
                alert.setHeaderText(null);
                alert.setContentText("Báo cáo đã được xuất thành công!\nĐường dẫn: " + file.getAbsolutePath());
                alert.showAndWait();
            }
        } catch (Exception e) {
            // Hiển thị thông báo lỗi
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi xuất báo cáo");
            alert.setHeaderText(null);
            alert.setContentText("Có lỗi xảy ra khi xuất báo cáo: " + e.getMessage());
            alert.showAndWait();
            e.printStackTrace();
        }
    }

    //    Xuất dữ liệu thống kê doanh thu ra file CSV
    private void exportToCSV(File file) throws IOException {
        // Kiểm tra xem có phải hiển thị theo tháng không
        String thoiGian = cmbThoiGian.getValue();
        boolean hienThiTheoThang = "Năm nay".equals(thoiGian);

        // Lấy dữ liệu
        ArrayList<ThongKeDoanhThuDTO> dsThongKe;
        if (hienThiTheoThang) {
            dsThongKe = thongKe_service.getDoanhThuTheoThang(tuNgay, denNgay, selectedNhanVien);
        } else {
            dsThongKe = thongKe_service.getDoanhThuTheoThoiGian(tuNgay, denNgay, selectedNhanVien);
        }

        // Tạo FileWriter với UTF-8 BOM để Excel hiển thị đúng tiếng Việt
        try (FileWriter writer = new FileWriter(file)) {
            // Thêm UTF-8 BOM
            writer.write('\ufeff');

            // Ghi header thông tin báo cáo
            writer.append("BAO CAO THONG KE DOANH THU\n");
            writer.append("Thoi gian: ").append(thoiGian).append("\n");
            writer.append("Tu ngay: ").append(tuNgay.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))).append("\n");
            writer.append("Den ngay: ").append(denNgay.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))).append("\n");

            String nhanVienText = selectedNhanVien != null ? selectedNhanVien : "Tat ca";
            writer.append("Nhan vien: ").append(nhanVienText).append("\n");
            writer.append("\n");

            // Ghi thông tin tổng quan
            writer.append("THONG TIN TONG QUAN\n");
            double tongDoanhThu = thongKe_service.getTongDoanhThu(tuNgay, denNgay, selectedNhanVien);
            int tongDonHang = thongKe_service.getTongDonHang(tuNgay, denNgay, selectedNhanVien);
            int soKhachHang = thongKe_service.getSoKhachHangMoi(tuNgay, denNgay, selectedNhanVien);
            double donHangTB = tongDonHang > 0 ? tongDoanhThu / tongDonHang : 0;

            writer.append("Tong doanh thu,").append(String.format("%.2f", tongDoanhThu)).append(" VND\n");
            writer.append("Tong don hang,").append(String.valueOf(tongDonHang)).append("\n");
            writer.append("Don hang trung binh,").append(String.format("%.2f", donHangTB)).append(" VND\n");
            writer.append("So khach hang,").append(String.valueOf(soKhachHang)).append("\n");
            writer.append("\n");

            // Ghi header cho bảng chi tiết
            writer.append("CHI TIET DOANH THU\n");
            if (hienThiTheoThang) {
                writer.append("Thang,So don hang,Doanh thu (VND),Don hang TB (VND),Khach hang moi,Nhan vien ban\n");
            } else {
                writer.append("Ngay,So don hang,Doanh thu (VND),Don hang TB (VND),Khach hang moi,Nhan vien ban\n");
            }

            // Ghi dữ liệu chi tiết
            DateTimeFormatter dateFormatter = hienThiTheoThang
                    ? DateTimeFormatter.ofPattern("MM/yyyy")
                    : DateTimeFormatter.ofPattern("dd/MM/yyyy");

            for (ThongKeDoanhThuDTO tk : dsThongKe) {
                writer.append(tk.getNgay().format(dateFormatter)).append(",");
                writer.append(String.valueOf(tk.getSoDonHang())).append(",");
                writer.append(String.format("%.2f", tk.getDoanhThu())).append(",");
                writer.append(String.format("%.2f", tk.getDonHangTB())).append(",");
                writer.append(String.valueOf(tk.getKhachHangMoi())).append(",");
                writer.append(tk.getNhanVienBan() != null ? tk.getNhanVienBan() : "").append("\n");
            }

            writer.append("\n");

            // Ghi top sản phẩm bán chạy
            writer.append("TOP SAN PHAM BAN CHAY\n");
            writer.append("Ten thuoc,So luong ban\n");

            Map<String, Integer> topSanPham = thongKe_service.getTopSanPhamBanChay(tuNgay, denNgay, 5);
            for (Map.Entry<String, Integer> entry : topSanPham.entrySet()) {
                writer.append(entry.getKey()).append(",");
                writer.append(String.valueOf(entry.getValue())).append("\n");
            }

            writer.flush();
        }
    }

    //    Định dạng số tiền với đơn vị K, M, B
    private String formatCurrency(double amount) {
        if (amount >= 1_000_000_000) {
            return percentFormatter.format(amount / 1_000_000_000) + "B";
        } else if (amount >= 1_000_000) {
            return percentFormatter.format(amount / 1_000_000) + "M";
        } else if (amount >= 1_000) {
            return percentFormatter.format(amount / 1_000) + "K";
        }
        return formatter.format(amount);
    }
}


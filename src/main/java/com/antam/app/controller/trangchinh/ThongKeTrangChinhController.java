/*
 * @ (#) DashboardController.java   1.0 14/10/25
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */
package com.antam.app.controller.trangchinh;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcons;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.geometry.Insets;
import javafx.scene.paint.Color;

import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import com.antam.app.network.ClientManager;
import javafx.concurrent.Task;

/*
 * @description Controller for dashboard view
 * @author: Tran Tuan Hung
 * @date: 14/10/25
 * @version: 1.0
 */
public class ThongKeTrangChinhController extends ScrollPane{

    // ==================== CÁC THÀNH PHẦN GIAO DIỆN ====================
    private Text txtTongSoThuoc = new Text();
    private Text txtSoNhanVien = new Text();
    private Text txtSoHoaDonHomNay = new Text();
    private Text txtSoKhuyenMai = new Text();
    private LineChart<String, Number> chartDoanhThu;        // Biểu đồ đường doanh thu
    private BarChart<String, Number> chartTopSanPham;       // Biểu đồ cột sản phẩm
    private VBox vboxThuocSapHetHan = new VBox();           // Danh sách thuốc sắp hết hạn
    private VBox vboxThuocTonKhoThap = new VBox();          // Danh sách thuốc tồn kho thấp

    // ==================== CÁC BIẾN CẤU HÌNH ====================
    private final DecimalFormat formatter = new DecimalFormat("#,###");


    public ThongKeTrangChinhController() {
        /** Giao diện **/
        this.setFitToHeight(true);
        this.setFitToWidth(true);
        // Make the ScrollPane flexible so it can resize to the parent container
        this.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        this.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        AnchorPane.setTopAnchor(this, 0.0);
        AnchorPane.setBottomAnchor(this, 0.0);
        AnchorPane.setLeftAnchor(this, 0.0);
        AnchorPane.setRightAnchor(this, 0.0);

        VBox root = new VBox(30);
        root.setStyle("-fx-background-color: #f8fafc;");
        root.setPadding(new Insets(20));

        Text title = new Text("Tổng quan hệ thống");
        title.setFill(Color.web("#1e3a8a"));
        title.setFont(Font.font("System Bold", 30));

        FlowPane paneTopCards = new FlowPane(30, 20);
        paneTopCards.setAlignment(Pos.CENTER);

        paneTopCards.getChildren().addAll(
                createCard(FontAwesomeIcons.MEDKIT, txtTongSoThuoc = new Text("0"), "Tổng số thuốc"),
                createCard(FontAwesomeIcons.USERS, txtSoNhanVien = new Text("0"), "Nhân viên"),
                createCard(FontAwesomeIcons.FILE, txtSoHoaDonHomNay = new Text("0"), "Hoá đơn hôm nay"),
                createCard(FontAwesomeIcons.GIFT, txtSoKhuyenMai = new Text("0"), "Khuyến mãi áp dụng")
        );

        FlowPane chartsPane = new FlowPane(20, 20);
        chartsPane.setAlignment(Pos.CENTER);
        chartsPane.setRowValignment(VPos.TOP);

        chartsPane.getChildren().addAll(
                createLineChartBox(),
                createBarChartBox()
        );

        FlowPane extraPane = new FlowPane(20, 20);
        extraPane.setAlignment(Pos.CENTER);
        extraPane.setRowValignment(VPos.TOP);

        vboxThuocSapHetHan = createDataBox("Thuốc sắp hết hạn");
        vboxThuocTonKhoThap = createDataBox("Thuốc còn tồn kho thấp");

        extraPane.getChildren().addAll(vboxThuocSapHetHan, vboxThuocTonKhoThap);

        root.getChildren().addAll(title, paneTopCards, chartsPane, extraPane);
        this.setContent(root);

        /** Sự kiện **/
        loadDashboardData();

    }


    private StackPane createCard(FontAwesomeIcons icon, Text valueText, String label) {
        StackPane card = new StackPane();
        // allow cards to size naturally but remain constrained
        card.setPrefSize(250, 121);
        card.setMaxSize(250, 121);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 20px;");

        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.rgb(212, 212, 212));
        shadow.setOffsetX(2);
        shadow.setOffsetY(3);
        shadow.setRadius(8);
        card.setEffect(shadow);

        HBox box = new HBox(10);
        box.setAlignment(Pos.CENTER_LEFT);
        box.setPadding(new Insets(0, 20, 0, 20));

        Button btn = new Button();
        btn.setPrefSize(60, 60);
        btn.setStyle("-fx-background-color: #2563eb; -fx-background-radius: 15px;");

        FontAwesomeIcon glyph = new FontAwesomeIcon();
        glyph.setIcon(icon);
        glyph.setFill(Color.WHITE);
        glyph.setSize("2em");
        btn.setGraphic(glyph);

        VBox v = new VBox();
        v.setPrefHeight(60);
        v.setAlignment(Pos.CENTER_LEFT);
        valueText.setFill(Color.web("#1e3a8a"));
        valueText.setFont(Font.font("System Bold", 32));

        Text lbl = new Text(label);
        lbl.setFill(Color.web("#64748b"));

        v.getChildren().addAll(valueText, lbl);

        box.getChildren().addAll(btn, v);
        card.getChildren().add(box);
        return card;
    }

    private VBox createLineChartBox() {
        VBox box = createChartBox("Doanh thu 7 ngày gần nhất");

        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();

        chartDoanhThu = new LineChart<>(xAxis, yAxis);
        chartDoanhThu.setPrefHeight(300);

        box.getChildren().add(chartDoanhThu);
        return box;
    }

    private VBox createBarChartBox() {
        VBox box = createChartBox("Top sản phẩm bán chạy");

        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();

        chartTopSanPham = new BarChart<>(xAxis, yAxis);
        chartTopSanPham.setPrefHeight(300);

        box.getChildren().add(chartTopSanPham);
        return box;
    }

    private VBox createChartBox(String titleText) {
        VBox box = new VBox(10);
        // prefer flexible width so charts can grow/shrink with the layout
        box.setPrefWidth(Region.USE_COMPUTED_SIZE);
        box.setMaxWidth(Double.MAX_VALUE);
        box.setStyle("-fx-background-color: white; -fx-background-radius: 10px;");
        box.setPadding(new Insets(10));

        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.rgb(218, 218, 218));
        shadow.setRadius(12);
        shadow.setOffsetX(3);
        shadow.setOffsetY(2);
        box.setEffect(shadow);

        Text title = new Text(titleText);
        title.setFill(Color.web("#1e3a8a"));
        title.setFont(Font.font("System Bold", 20));

        box.getChildren().add(title);
        return box;
    }

    private VBox createDataBox(String titleText) {
        VBox box = new VBox(10);
        box.setPrefWidth(Region.USE_COMPUTED_SIZE);
        box.setMaxWidth(Double.MAX_VALUE);
        box.setStyle("-fx-background-color: white; -fx-background-radius: 10px;");
        box.setPadding(new Insets(10));

        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.rgb(218, 218, 218));
        shadow.setRadius(12);
        shadow.setOffsetX(3);
        shadow.setOffsetY(2);
        box.setEffect(shadow);

        Text title = new Text(titleText);
        title.setFill(Color.web("#1e3a8a"));
        title.setFont(Font.font("System Bold", 20));

        box.getChildren().add(title);
        return box;
    }

    /**
     * Load tất cả dữ liệu cho dashboard
     */
    public void loadDashboardData() {
        loadBasicStatistics();
        loadRevenueChart();
        loadTopProductsChart();
        loadExpiredMedicines();
        loadLowStockMedicines();
    }

    /**
     * Load các thống kê cơ bản
     */
    private void loadBasicStatistics() {
        // Load tổng số thuốc
        Task<Integer> tongSoThuocTask = new Task<Integer>() {
            @Override
            protected Integer call() {
                return ClientManager.getInstance().getTongSoThuoc();
            }
        };
        tongSoThuocTask.setOnSucceeded(e -> {
            int tongSoThuoc = tongSoThuocTask.getValue();
            if (txtTongSoThuoc != null) {
                txtTongSoThuoc.setText(formatter.format(tongSoThuoc));
            }
        });
        tongSoThuocTask.setOnFailed(e -> {
            System.err.println("Error loading TongSoThuoc: " + tongSoThuocTask.getException());
        });
        new Thread(tongSoThuocTask).start();

        // Load số nhân viên
        Task<Integer> soNhanVienTask = new Task<Integer>() {
            @Override
            protected Integer call() {
                return ClientManager.getInstance().getTongSoNhanVien();
            }
        };
        soNhanVienTask.setOnSucceeded(e -> {
            int soNhanVien = soNhanVienTask.getValue();
            if (txtSoNhanVien != null) {
                txtSoNhanVien.setText(formatter.format(soNhanVien));
            }
        });
        soNhanVienTask.setOnFailed(e -> {
            System.err.println("Error loading TongSoNhanVien: " + soNhanVienTask.getException());
        });
        new Thread(soNhanVienTask).start();

        // Load số hóa đơn hôm nay
        Task<Integer> soHoaDonHomNayTask = new Task<Integer>() {
            @Override
            protected Integer call() {
                return ClientManager.getInstance().getSoHoaDonHomNay();
            }
        };
        soHoaDonHomNayTask.setOnSucceeded(e -> {
            int soHoaDonHomNay = soHoaDonHomNayTask.getValue();
            if (txtSoHoaDonHomNay != null) {
                txtSoHoaDonHomNay.setText(formatter.format(soHoaDonHomNay));
            }
        });
        soHoaDonHomNayTask.setOnFailed(e -> {
            System.err.println("Error loading SoHoaDonHomNay: " + soHoaDonHomNayTask.getException());
        });
        new Thread(soHoaDonHomNayTask).start();

        // Load số khuyến mãi áp dụng
        Task<Integer> soKhuyenMaiTask = new Task<Integer>() {
            @Override
            protected Integer call() {
                return ClientManager.getInstance().getSoKhuyenMaiApDung();
            }
        };
        soKhuyenMaiTask.setOnSucceeded(e -> {
            int soKhuyenMai = soKhuyenMaiTask.getValue();
            if (txtSoKhuyenMai != null) {
                txtSoKhuyenMai.setText(formatter.format(soKhuyenMai));
            }
        });
        soKhuyenMaiTask.setOnFailed(e -> {
            System.err.println("Error loading SoKhuyenMaiApDung: " + soKhuyenMaiTask.getException());
        });
        new Thread(soKhuyenMaiTask).start();
    }

    /**
     * ==================== BIỂU ĐỒ DOANH THU ====================
     * Load biểu đồ doanh thu 7 ngày gần nhất
     */
    private void loadRevenueChart() {
        if (chartDoanhThu == null) return;

        Task<Map<String, Double>> revenueTask = new Task<Map<String, Double>>() {
            @Override
            protected Map<String, Double> call() {
                return ClientManager.getInstance().getDoanhThu7NgayGanNhat();
            }
        };
        revenueTask.setOnSucceeded(e -> {
            Map<String, Double> doanhThuData = revenueTask.getValue();

            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Doanh thu (VNĐ)");

            // Tạo dữ liệu cho 7 ngày gần nhất
            // *** CHỈNH SỬA SỐ NGÀY: Thay đổi số 6 thành (số_ngày - 1) ***
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM");
            for (int i = 6; i >= 0; i--) {
                LocalDate date = LocalDate.now().minusDays(i);
                String dateKey = date.toString();
                String displayDate = date.format(dateFormatter);

                double revenue = doanhThuData.getOrDefault(dateKey, 0.0);
                XYChart.Data<String, Number> data = new XYChart.Data<>(displayDate, revenue);
                series.getData().add(data);
            }

            chartDoanhThu.getData().clear();
            chartDoanhThu.getData().add(series);

            // Thêm tooltip cho từng điểm dữ liệu
            for (XYChart.Data<String, Number> data : series.getData()) {
                javafx.scene.control.Tooltip tooltip = new javafx.scene.control.Tooltip(
                    "Ngày: " + data.getXValue() + "\n" +
                    "Doanh thu: " + formatter.format(data.getYValue().doubleValue()) + " VNĐ"
                );

                // *** CHỈNH MÀU TOOLTIP: Thay đổi màu nền và màu chữ ***
                tooltip.setShowDelay(javafx.util.Duration.millis(100));
                tooltip.setStyle(
                    "-fx-font-size: 12px; " +
                    "-fx-background-color: #1e3a8a; " +    // Màu nền tooltip (xanh dương đậm)
                    "-fx-text-fill: white;"                 // Màu chữ tooltip (trắng)
                );

                // Gắn tooltip vào node của data point
                javafx.scene.Node node = data.getNode();
                if (node != null) {
                    javafx.scene.control.Tooltip.install(node, tooltip);

                    // *** CHỈNH HIỆU ỨNG HOVER: Thay đổi kích thước khi rê chuột ***
                    node.setOnMouseEntered(event -> {
                        node.setStyle(
                            "-fx-cursor: hand; " +
                            "-fx-scale-x: 1.5; " +          // Phóng to 1.5 lần theo chiều ngang
                            "-fx-scale-y: 1.5;"             // Phóng to 1.5 lần theo chiều dọc
                        );
                    });
                    node.setOnMouseExited(event -> {
                        node.setStyle("-fx-scale-x: 1.0; -fx-scale-y: 1.0;");
                    });
                }
            }
        });
        revenueTask.setOnFailed(e -> {
            System.err.println("Error loading revenue chart: " + revenueTask.getException());
        });
        new Thread(revenueTask).start();
    }

    /**
     * ==================== BIỂU ĐỒ TOP SẢN PHẨM ====================
     * Load biểu đồ top sản phẩm bán chạy
     */
    private void loadTopProductsChart() {
        if (chartTopSanPham == null) return;

        Task<Map<String, Integer>> topProductsTask = new Task<Map<String, Integer>>() {
            @Override
            protected Map<String, Integer> call() {
                return ClientManager.getInstance().getTopSanPhamBanChay(5);
            }
        };
        topProductsTask.setOnSucceeded(e -> {
            Map<String, Integer> topProducts = topProductsTask.getValue();

            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Số lượng bán");

            // Map để lưu tên đầy đủ của thuốc
            Map<String, String> fullNamesMap = new HashMap<>();

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

                javafx.scene.control.Tooltip tooltip = new javafx.scene.control.Tooltip(
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
                javafx.scene.Node node = data.getNode();
                if (node != null) {
                    javafx.scene.control.Tooltip.install(node, tooltip);

                    // *** CHỈNH HIỆU ỨNG HOVER: Thay đổi độ mờ khi rê chuột ***
                    node.setOnMouseEntered(event -> {
                        node.setStyle(
                            "-fx-cursor: hand; " +
                            "-fx-opacity: 0.8;"             // Độ mờ khi hover (0.0 - 1.0)
                        );
                    });
                    node.setOnMouseExited(event -> {
                        node.setStyle("-fx-opacity: 1.0;");
                    });
                }
            }
        });
        topProductsTask.setOnFailed(e -> {
            System.err.println("Error loading top products chart: " + topProductsTask.getException());
        });
        new Thread(topProductsTask).start();
    }

    /**
     * ==================== DANH SÁCH THUỐC SẮP HẾT HẠN ====================
     * Load danh sách thuốc sắp hết hạn
     */
    private void loadExpiredMedicines() {
        if (vboxThuocSapHetHan == null) return;

        Task<List<Map<String, Object>>> expiredMedicinesTask = new Task<List<Map<String, Object>>>() {
            @Override
            protected List<Map<String, Object>> call() {
                return ClientManager.getInstance().getThuocSapHetHan();
            }
        };
        expiredMedicinesTask.setOnSucceeded(e -> {
            List<Map<String, Object>> thuocSapHetHan = expiredMedicinesTask.getValue();

            // Xóa các item cũ (giữ lại header)
            if (vboxThuocSapHetHan.getChildren().size() > 1) {
                vboxThuocSapHetHan.getChildren().removeIf(node ->
                    !(node instanceof Text && ((Text) node).getStyleClass().contains("header")));
            }

            if (thuocSapHetHan.isEmpty()) {
                // *** CHỈNH MÀU CHỮ: Thay đổi màu khi không có dữ liệu ***
                Label noDataLabel = new Label("Không có thuốc nào sắp hết hạn");
                noDataLabel.setStyle(
                    "-fx-text-fill: #64748b; " +            // Màu chữ (xám)
                    "-fx-font-style: italic;"               // Kiểu chữ nghiêng
                );
                vboxThuocSapHetHan.getChildren().add(noDataLabel);
            } else {
                // *** CHỈNH SỐ THUỐC: Thay đổi số 5 để hiển thị nhiều/ít thuốc hơn ***
                int count = Math.min(thuocSapHetHan.size(), 5);
                for (int i = 0; i < count; i++) {
                    Map<String, Object> thuoc = thuocSapHetHan.get(i);
                    createExpiredMedicineItem(thuoc);
                }
            }
        });
        expiredMedicinesTask.setOnFailed(e -> {
            System.err.println("Error loading expired medicines: " + expiredMedicinesTask.getException());
        });
        new Thread(expiredMedicinesTask).start();
    }

    /**
     * ==================== DANH SÁCH THUỐC TỒN KHO THẤP ====================
     * Load danh sách thuốc tồn kho thấp
     */
    private void loadLowStockMedicines() {
        if (vboxThuocTonKhoThap == null) return;

        Task<List<Map<String, Object>>> lowStockMedicinesTask = new Task<List<Map<String, Object>>>() {
            @Override
            protected List<Map<String, Object>> call() {
                return ClientManager.getInstance().getThuocTonKhoThap();
            }
        };
        lowStockMedicinesTask.setOnSucceeded(e -> {
            List<Map<String, Object>> thuocTonKhoThap = lowStockMedicinesTask.getValue();

            // Xóa các item cũ (giữ lại header)
            if (vboxThuocTonKhoThap.getChildren().size() > 1) {
                vboxThuocTonKhoThap.getChildren().removeIf(node ->
                    !(node instanceof Text && ((Text) node).getStyleClass().contains("header")));
            }

            if (thuocTonKhoThap.isEmpty()) {
                // *** CHỈNH MÀU CHỮ: Thay đổi màu khi không có dữ liệu ***
                Label noDataLabel = new Label("Tất cả thuốc đều có tồn kho đủ");
                noDataLabel.setStyle(
                    "-fx-text-fill: #64748b; " +            // Màu chữ (xám)
                    "-fx-font-style: italic;"               // Kiểu chữ nghiêng
                );
                vboxThuocTonKhoThap.getChildren().add(noDataLabel);
            } else {
                // *** CHỈNH SỐ THUỐC: Thay đổi số 5 để hiển thị nhiều/ít thuốc hơn ***
                int count = Math.min(thuocTonKhoThap.size(), 5);
                for (int i = 0; i < count; i++) {
                    Map<String, Object> thuoc = thuocTonKhoThap.get(i);
                    createLowStockMedicineItem(thuoc);
                }
            }
        });
        lowStockMedicinesTask.setOnFailed(e -> {
            System.err.println("Error loading low stock medicines: " + lowStockMedicinesTask.getException());
        });
        new Thread(lowStockMedicinesTask).start();
    }

    /**
     * ==================== TẠO ITEM THUỐC SẮP HẾT HẠN ====================
     * Tạo item hiển thị thuốc sắp hết hạn
     */
    private void createExpiredMedicineItem(Map<String, Object> thuoc) {
        try {
            HBox container = new HBox();
            // *** CHỈNH KHOẢNG CÁCH: Thay đổi số trong setSpacing() ***
            container.setSpacing(10);                       // Khoảng cách giữa các phần tử (pixels)
            container.setPadding(new Insets(5, 0, 5, 0));  // Padding: top, right, bottom, left

            // *** CHỈNH MÀU VIỀN: Thay đổi màu và độ dày viền ***
            container.setStyle(
                "-fx-border-color: #e2e8f0; " +             // Màu viền (xám nhạt)
                "-fx-border-width: 0 0 1 0;"                // Độ dày viền: top right bottom left
            );

            VBox leftContent = new VBox();
            leftContent.setSpacing(2);

            // *** CHỈNH MÀU VÀ CỠ CHỮ TÊN THUỐC ***
            Text tenThuoc = new Text((String) thuoc.get("tenThuoc"));
            tenThuoc.setFont(Font.font("System", 14));      // Cỡ chữ: 14px
            tenThuoc.setFill(Color.web("#1e3a8a"));        // Màu chữ: xanh dương đậm

            // *** CHỈNH MÀU VÀ CỠ CHỮ NGÀY HẾT HẠN ***
            Date hanSuDung = (Date) thuoc.get("hanSuDung");
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Text ngayHetHan = new Text("Hết hạn: " + sdf.format(hanSuDung));
            ngayHetHan.setFont(Font.font("System", 12));    // Cỡ chữ: 12px
            ngayHetHan.setFill(Color.web("#dc2626"));      // Màu chữ: đỏ (cảnh báo)

            leftContent.getChildren().addAll(tenThuoc, ngayHetHan);

            // *** CHỈNH MÀU VÀ CỠ CHỮ SỐ LƯỢNG ***
            Text soLuong = new Text(formatter.format((Integer) thuoc.get("soLuongTon")) + " đơn vị");
            soLuong.setFont(Font.font("System", 12));       // Cỡ chữ: 12px
            soLuong.setFill(Color.web("#64748b"));         // Màu chữ: xám

            container.getChildren().addAll(leftContent, soLuong);
            vboxThuocSapHetHan.getChildren().add(container);

        } catch (Exception e) {
            System.err.println("Error creating expired medicine item: " + e.getMessage());
        }
    }

    /**
     * ==================== TẠO ITEM THUỐC TỒN KHO THẤP ====================
     * Tạo item hiển thị thuốc tồn kho thấp
     */
    private void createLowStockMedicineItem(Map<String, Object> thuoc) {
        try {
            HBox container = new HBox();
            // *** CHỈNH KHOẢNG CÁCH: Thay đổi số trong setSpacing() ***
            container.setSpacing(10);                       // Khoảng cách giữa các phần tử (pixels)
            container.setPadding(new Insets(5, 0, 5, 0));  // Padding: top, right, bottom, left

            // *** CHỈNH MÀU VIỀN: Thay đổi màu và độ dày viền ***
            container.setStyle(
                "-fx-border-color: #e2e8f0; " +             // Màu viền (xám nhạt)
                "-fx-border-width: 0 0 1 0;"                // Độ dày viền: top right bottom left
            );

            VBox leftContent = new VBox();
            leftContent.setSpacing(2);

            // *** CHỈNH MÀU VÀ CỠ CHỮ TÊN THUỐC ***
            Text tenThuoc = new Text((String) thuoc.get("tenThuoc"));
            tenThuoc.setFont(Font.font("System", 14));      // Cỡ chữ: 14px
            tenThuoc.setFill(Color.web("#1e3a8a"));        // Màu chữ: xanh dương đậm

            // *** CHỈNH MÀU VÀ CỠ CHỮ MÃ THUỐC ***
            Text maThuoc = new Text("Mã: " + (String) thuoc.get("maThuoc"));
            maThuoc.setFont(Font.font("System", 12));       // Cỡ chữ: 12px
            maThuoc.setFill(Color.web("#64748b"));         // Màu chữ: xám

            leftContent.getChildren().addAll(tenThuoc, maThuoc);

            // *** CHỈNH MÀU VÀ CỠ CHỮ SỐ LƯỢNG (MÀU ĐỎ CẢNH BÁO) ***
            Text soLuong = new Text(formatter.format((Integer) thuoc.get("soLuongTon")) + " đơn vị");
            soLuong.setFont(Font.font("System", 12));       // Cỡ chữ: 12px
            soLuong.setFill(Color.web("#dc2626"));         // Màu chữ: đỏ (cảnh báo tồn kho thấp)

            container.getChildren().addAll(leftContent, soLuong);
            vboxThuocTonKhoThap.getChildren().add(container);

        } catch (Exception e) {
            System.err.println("Error creating low stock medicine item: " + e.getMessage());
        }
    }

    /**
     * Refresh dữ liệu dashboard
     */
    public void refreshData() {
        loadDashboardData();
    }
}

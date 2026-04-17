//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.antam.app.controller.hoadon;

import com.antam.app.service.I_NhanVien_Service;
import com.antam.app.service.impl.HoaDon_Service;
import com.antam.app.service.impl.NhanVien_Service;
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

/**
 * Controller cho giao diện quản lý hóa đơn (invoice_view.fxml).
 * - Hiển thị danh sách hóa đơn ra TableView.
 * - Cho phép tạo, đổi, trả hóa đơn qua các nút chức năng.
 * - Khi double-click vào 1 dòng hóa đơn sẽ mở dialog chi tiết hóa đơn.
 */
public class CapNhatHoaDonController extends ScrollPane{
    private Button btnReturnMedicine;
    private Button btnExchangeMedicine;

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

    // Định dạng tiền tệ kiểu Việt Nam: 1.000đ, 10.000đ
    private static final DecimalFormat VND_FORMAT;
    static {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.');
        symbols.setDecimalSeparator(',');
        VND_FORMAT = new DecimalFormat("#,##0", symbols);
    }

    public CapNhatHoaDonController() {
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

        FontAwesomeIcon iconChange = new FontAwesomeIcon();
        iconChange.setFill(Color.WHITE);
        iconChange.setIcon(FontAwesomeIcons.EXCHANGE);

        FontAwesomeIcon iconReturn = new FontAwesomeIcon();
        iconReturn.setFill(Color.WHITE);
        iconReturn.setIcon(FontAwesomeIcons.REFRESH);

        btnExchangeMedicine = new Button("Đổi thuốc");
        btnExchangeMedicine.setGraphic(iconChange);
        btnExchangeMedicine.getStyleClass().add("btn-them");

        btnReturnMedicine = new Button("Trả thuốc");
        btnReturnMedicine.setGraphic(iconReturn);
        btnReturnMedicine.getStyleClass().add("btn-them");

        HBox titleBox = new HBox(5);
        Text title = new Text("Cập nhật hoá đơn");
        title.setFill(Color.web("#1e3a8a"));
        title.setFont(Font.font("System", 30));
        Pane spacer = new Pane();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        titleBox.getChildren().addAll(title, spacer, btnExchangeMedicine, btnReturnMedicine);

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
        FontAwesomeIcon infoIcon = new FontAwesomeIcon(); infoIcon.setGlyphName("INFO"); infoIcon.setFill(Color.web("#2563eb"));
        guide.setGraphic(infoIcon);

        tableBox.getChildren().addAll(table_invoice, guide);

        this.getStylesheets().addAll(getClass().getResource("/com/antam/app/styles/dashboard_style.css").toExternalForm());
        rootPane.getChildren().addAll(titleBox, filters, searchBox, tableBox);
        setContent(rootPane);
        /** Sự kiện **/
        this.btnExchangeMedicine.setOnAction((e) -> {
            HoaDonDTO selectedHoaDonDTO = table_invoice.getSelectionModel().getSelectedItem();
            if (selectedHoaDonDTO == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Cảnh báo");
                alert.setHeaderText("Chưa chọn hóa đơn");
                alert.setContentText("Vui lòng chọn ít nhất một hóa đơn.");
                alert.showAndWait();
                return;
            }else{
                DoiThuocFormController doiDialog = new DoiThuocFormController();
                doiDialog.setHoaDon(selectedHoaDonDTO);
                doiDialog.showData(selectedHoaDonDTO);

                Dialog<Void> dialog = new Dialog<>();
                dialog.setDialogPane(doiDialog);
                dialog.setTitle("Đổi thuốc");
                dialog.initModality(Modality.APPLICATION_MODAL);
                dialog.showAndWait();

                HoaDon_Service hoaDonDAO = new HoaDon_Service();
                ObservableList<HoaDonDTO> hoaDonList = FXCollections.observableArrayList(hoaDonDAO.getAllHoaDon());
                table_invoice.refresh();
                table_invoice.setItems(hoaDonList);
            }
        });
        // duong
        this.btnReturnMedicine.setOnAction((e) -> {
            HoaDonDTO selectedHoaDonDTO = table_invoice.getSelectionModel().getSelectedItem();
            if (selectedHoaDonDTO == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Cảnh báo");
                alert.setHeaderText("Chưa chọn hóa đơn");
                alert.setContentText("Vui lòng chọn ít nhất một hóa đơn.");
                alert.showAndWait();
                return;
            }else{
                TraThuocFormController traDialog = new TraThuocFormController();
                traDialog.setHoaDon(selectedHoaDonDTO);
                traDialog.showData(selectedHoaDonDTO);
                Dialog<Void> dialog = new Dialog<>();
                dialog.setDialogPane(traDialog);
                dialog.setTitle("Trả thuốc");
                dialog.initModality(Modality.APPLICATION_MODAL);
                dialog.showAndWait();

                HoaDon_Service hoaDonDAO = new HoaDon_Service();
                ObservableList<HoaDonDTO> hoaDonList = FXCollections.observableArrayList(hoaDonDAO.getAllHoaDon());
                table_invoice.refresh();
                table_invoice.setItems(hoaDonList);
            }
        });

        // Thiết lập cách lấy dữ liệu cho từng cột TableView

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

        // Load dữ liệu hóa đơn từ DB lên bảng
        HoaDon_Service hoaDonDAO = new HoaDon_Service();
        ObservableList<HoaDonDTO> hoaDonList = FXCollections.observableArrayList(hoaDonDAO.getAllHoaDon());
        table_invoice.setItems(hoaDonList);

        // --- Khởi tạo ComboBox nhân viên ---
        NhanVien_Service nhanVienDAO = new NhanVien_Service();
        ObservableList<NhanVienDTO> dsNhanVien = FXCollections.observableArrayList(I_NhanVien_Service.getDsNhanVienformDBS());
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
                    // Hiển thị "Tất cả" cho lựa chọn đặc biệt, hoặc tên nhân viên
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
                    // Hiển thị "Tất cả" cho lựa chọn đặc biệt, hoặc tên nhân viên
                    setText("Tất cả".equals(item.getMaNV()) ? "Tất cả" : item.getHoTen());
                }
            }
        });
        // --- Khởi tạo ComboBox trạng thái ---
        ObservableList<String> dsTrangThai = FXCollections.observableArrayList("Tất cả", "Hoạt động", "Đã huỷ");
        cbStatus.setItems(dsTrangThai);
        cbStatus.setValue("Tất cả");
        // --- Khởi tạo ComboBox khoảng giá ---
        ObservableList<String> dsKhoangGia = FXCollections.observableArrayList(
                "Tất cả",
                "Dưới 500.000",
                "500.000 - 1.000.000",
                "1.000.000 - 2.000.000",
                "Trên 2.000.000"
        );
        cbPrice.setItems(dsKhoangGia);
        cbPrice.setValue("Tất cả");

        // --- Hàm lọc hóa đơn theo nhân viên, trạng thái, khoảng giá, ngày ---
        Runnable filterInvoices = () -> {
            HoaDon_Service hoaDonDAO1 = new HoaDon_Service();
            NhanVienDTO selectedNV = cbEmployee.getValue();
            String selectedStatus = cbStatus.getValue();
            String selectedPrice = cbPrice.getValue();
            LocalDate fromDate = cbFirstDate != null ? cbFirstDate.getValue() : null;
            LocalDate toDate = cbEndDate != null ? cbEndDate.getValue() : null;
            boolean allNV = (selectedNV == null || "Tất cả".equals(selectedNV.getMaNV()));
            boolean allStatus = (selectedStatus == null || "Tất cả".equals(selectedStatus));
            boolean allPrice = (selectedPrice == null || "Tất cả".equals(selectedPrice));
            ObservableList<HoaDonDTO> filtered;
            // Lọc theo nhân viên và trạng thái trước
            ArrayList<HoaDonDTO> baseList;
            if (allNV && allStatus) {
                baseList = new ArrayList<>(hoaDonDAO1.getAllHoaDon());
            } else if (!allNV && allStatus) {
                baseList = new ArrayList<>(hoaDonDAO1.searchHoaDonByMaNV(selectedNV.getMaNV()));
            } else if (allNV && !allStatus) {
                baseList = new ArrayList<>(hoaDonDAO1.searchHoaDonByStatus(selectedStatus));
            } else {
                ArrayList<HoaDonDTO> byStatus = hoaDonDAO1.searchHoaDonByStatus(selectedStatus);
                baseList = new ArrayList<>();
                for (HoaDonDTO hd : byStatus) {
                    if (hd.getMaNV() != null && selectedNV.getMaNV().equals(hd.getMaNV().getMaNV())) {
                        baseList.add(hd);
                    }
                }
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
                ArrayList<HoaDonDTO> priceFiltered = new ArrayList<>();
                for (HoaDonDTO hd : baseList) {
                    double tongTien = hd.getTongTien();
                    if (tongTien >= min && tongTien < max) {
                        priceFiltered.add(hd);
                    }
                }
                baseList = priceFiltered;
            }
            // Lọc tiếp theo ngày (lấy hóa đơn có ngày tạo nằm trong khoảng [fromDate, toDate])
            if (fromDate != null || toDate != null) {
                ArrayList<HoaDonDTO> dateFiltered = new ArrayList<>();
                for (HoaDonDTO hd : baseList) {
                    LocalDate ngayTao = hd.getNgayTao();
                    boolean afterFrom = (fromDate == null) || !ngayTao.isBefore(fromDate);
                    boolean beforeTo = (toDate == null) || !ngayTao.isAfter(toDate);
                    if (afterFrom && beforeTo) {
                        dateFiltered.add(hd);
                    }
                }
                baseList = dateFiltered;
            }
            filtered = FXCollections.observableArrayList(baseList);
            table_invoice.setItems(filtered);
        };
        // Lắng nghe thay đổi trên cbEmployee, cbStatus, cbPrice, cbFirstDate, cbEndDate
        cbEmployee.valueProperty().addListener((obs, oldNV, newNV) -> filterInvoices.run());
        cbStatus.valueProperty().addListener((obs, oldSt, newSt) -> filterInvoices.run());
        cbPrice.valueProperty().addListener((obs, oldPr, newPr) -> filterInvoices.run());
        if (cbFirstDate != null) cbFirstDate.valueProperty().addListener((obs, oldDate, newDate) -> filterInvoices.run());
        if (cbEndDate != null) cbEndDate.valueProperty().addListener((obs, oldDate, newDate) -> filterInvoices.run());

        // Lắng nghe thay đổi nội dung ô tìm kiếm để search realtime
        txtSearchInvoice.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.trim().isEmpty()) {
                // Nếu ô tìm kiếm rỗng, load lại toàn bộ hóa đơn
                ObservableList<HoaDonDTO> allHoaDon = FXCollections.observableArrayList(hoaDonDAO.getAllHoaDon());
                table_invoice.setItems(allHoaDon);
            } else {
                // Nếu có nội dung, search theo mã
                ObservableList<HoaDonDTO> searchResult = FXCollections.observableArrayList(hoaDonDAO.searchHoaDonByMaHd(newValue));
                table_invoice.setItems(searchResult);
            }
        });
        // Sự kiện cho nút tìm kiếm vẫn giữ lại để người dùng có thể bấm nút
        btnSearchInvoice.setOnAction(e -> {
            String maHd = txtSearchInvoice.getText();
            if (maHd == null || maHd.trim().isEmpty()) {
                ObservableList<HoaDonDTO> allHoaDon = FXCollections.observableArrayList(hoaDonDAO.getAllHoaDon());
                table_invoice.setItems(allHoaDon);
            } else {
                ObservableList<HoaDonDTO> searchResult = FXCollections.observableArrayList(hoaDonDAO.searchHoaDonByMaHd(maHd));
                table_invoice.setItems(searchResult);
            }
        });
        // Đặt row factory để tô màu dòng được chọn
        this.table_invoice.setRowFactory(tv -> new TableRow<>() {
            @Override
            protected void updateItem(HoaDonDTO item, boolean empty) {
                super.updateItem(item, empty);
                if (!empty && item != null && isSelected()) {
                    setStyle("-fx-background-color: #d1fae5;"); // Màu xanh nhạt
                } else {
                    setStyle("");
                }
            }
        });
        // Chỉ gán duy nhất một handler cho sự kiện click
        this.table_invoice.setOnMouseClicked(event -> {
            // Nếu double click và có dòng được chọn thì mở dialog chi tiết hóa đơn
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

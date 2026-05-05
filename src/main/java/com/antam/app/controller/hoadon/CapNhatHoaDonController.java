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
import javafx.concurrent.Task;
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

public class CapNhatHoaDonController extends ScrollPane {
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

    private final ClientManager clientManager = ClientManager.getInstance();

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
        FontAwesomeIcon searchIcon = new FontAwesomeIcon();
        searchIcon.setGlyphName("SEARCH");
        searchIcon.setFill(Color.WHITE);
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
        FontAwesomeIcon infoIcon = new FontAwesomeIcon();
        infoIcon.setGlyphName("INFO");
        infoIcon.setFill(Color.web("#2563eb"));
        guide.setGraphic(infoIcon);

        tableBox.getChildren().addAll(table_invoice, guide);

        this.getStylesheets().addAll(getClass().getResource("/com/antam/app/styles/dashboard_style.css").toExternalForm());
        rootPane.getChildren().addAll(titleBox, filters, searchBox, tableBox);
        setContent(rootPane);

        /** Sự kiện **/

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

        // Load danh sách hóa đơn
        loadHoaDonList();

        // Load danh sách nhân viên cho ComboBox
        loadNhanVienComboBox();

        // Khởi tạo ComboBox trạng thái
        ObservableList<String> dsTrangThai = FXCollections.observableArrayList("Tất cả", "Hoạt động", "Đã huỷ");
        cbStatus.setItems(dsTrangThai);
        cbStatus.setValue("Tất cả");

        // Khởi tạo ComboBox khoảng giá
        ObservableList<String> dsKhoangGia = FXCollections.observableArrayList(
                "Tất cả",
                "Dưới 500.000",
                "500.000 - 1.000.000",
                "1.000.000 - 2.000.000",
                "Trên 2.000.000"
        );
        cbPrice.setItems(dsKhoangGia);
        cbPrice.setValue("Tất cả");

        // Lắng nghe thay đổi để lọc hóa đơn
        cbEmployee.valueProperty().addListener((obs, oldNV, newNV) -> filterHoaDon());
        cbStatus.valueProperty().addListener((obs, oldSt, newSt) -> filterHoaDon());
        cbPrice.valueProperty().addListener((obs, oldPr, newPr) -> filterHoaDon());
        cbFirstDate.valueProperty().addListener((obs, oldDate, newDate) -> filterHoaDon());
        cbEndDate.valueProperty().addListener((obs, oldDate, newDate) -> filterHoaDon());

        // Tìm kiếm realtime theo mã hóa đơn
        txtSearchInvoice.textProperty().addListener((observable, oldValue, newValue) -> searchHoaDon(newValue));
        btnSearchInvoice.setOnAction(e -> searchHoaDon(txtSearchInvoice.getText()));

        btnResetInvoice.setOnAction(e -> {
            cbEmployee.setValue(null);
            cbStatus.setValue("Tất cả");
            cbPrice.setValue("Tất cả");
            cbFirstDate.setValue(null);
            cbEndDate.setValue(null);
            txtSearchInvoice.clear();
            loadHoaDonList();
        });

        // Đổi thuốc
        this.btnExchangeMedicine.setOnAction((e) -> {
            HoaDonDTO selectedHoaDonDTO = table_invoice.getSelectionModel().getSelectedItem();
            if (selectedHoaDonDTO == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Cảnh báo");
                alert.setHeaderText("Chưa chọn hóa đơn");
                alert.setContentText("Vui lòng chọn ít nhất một hóa đơn.");
                alert.showAndWait();
                return;
            }
            DoiThuocFormController doiDialog = new DoiThuocFormController();
            doiDialog.setHoaDon(selectedHoaDonDTO);
            doiDialog.showData(selectedHoaDonDTO);

            Dialog<Void> dialog = new Dialog<>();
            dialog.setDialogPane(doiDialog);
            dialog.setTitle("Đổi thuốc");
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.showAndWait();

            loadHoaDonList();
        });

        // Trả thuốc
        this.btnReturnMedicine.setOnAction((e) -> {
            HoaDonDTO selectedHoaDonDTO = table_invoice.getSelectionModel().getSelectedItem();
            if (selectedHoaDonDTO == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Cảnh báo");
                alert.setHeaderText("Chưa chọn hóa đơn");
                alert.setContentText("Vui lòng chọn ít nhất một hóa đơn.");
                alert.showAndWait();
                return;
            }
            TraThuocFormController traDialog = new TraThuocFormController();
            traDialog.setHoaDon(selectedHoaDonDTO);
            traDialog.showData(selectedHoaDonDTO);

            Dialog<Void> dialog = new Dialog<>();
            dialog.setDialogPane(traDialog);
            dialog.setTitle("Trả thuốc");
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.showAndWait();

            loadHoaDonList();
        });

        // Tô màu dòng được chọn
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

        // Double click xem chi tiết hóa đơn
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

    private void loadHoaDonList() {
        Task<List<HoaDonDTO>> task = new Task<>() {
            @Override
            protected List<HoaDonDTO> call() {
                return clientManager.getAllHoaDon();
            }
        };
        task.setOnSucceeded(e -> table_invoice.setItems(FXCollections.observableArrayList(task.getValue())));
        new Thread(task).start();
    }

    private void loadNhanVienComboBox() {
        Task<List<NhanVienDTO>> task = new Task<>() {
            @Override
            protected List<NhanVienDTO> call() {
                return clientManager.getNhanVienList();
            }
        };
        task.setOnSucceeded(e -> {
            ObservableList<NhanVienDTO> dsNhanVien = FXCollections.observableArrayList(task.getValue());
            NhanVienDTO tatCaNV = new NhanVienDTO("Tất cả");
            dsNhanVien.add(0, tatCaNV);
            cbEmployee.setItems(dsNhanVien);
            cbEmployee.setPromptText("Chọn nhân viên");
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
        });
        new Thread(task).start();
    }

    private void filterHoaDon() {
        NhanVienDTO selectedNV = cbEmployee.getValue();
        String selectedStatus = cbStatus.getValue();
        String selectedPrice = cbPrice.getValue();
        LocalDate fromDate = cbFirstDate.getValue();
        LocalDate toDate = cbEndDate.getValue();
        boolean allNV = (selectedNV == null || "Tất cả".equals(selectedNV.getMaNV()));
        boolean allStatus = (selectedStatus == null || "Tất cả".equals(selectedStatus));
        boolean allPrice = (selectedPrice == null || "Tất cả".equals(selectedPrice));

        Task<List<HoaDonDTO>> task = new Task<>() {
            @Override
            protected List<HoaDonDTO> call() {
                List<HoaDonDTO> baseList;
                if (allNV && allStatus) {
                    baseList = new ArrayList<>(clientManager.getAllHoaDon());
                } else if (!allNV && allStatus) {
                    baseList = new ArrayList<>(clientManager.searchHoaDonByMaNV(selectedNV.getMaNV()));
                } else if (allNV && !allStatus) {
                    baseList = new ArrayList<>(clientManager.searchHoaDonByStatus(selectedStatus));
                } else {
                    List<HoaDonDTO> byStatus = clientManager.searchHoaDonByStatus(selectedStatus);
                    baseList = new ArrayList<>();
                    for (HoaDonDTO hd : byStatus) {
                        if (hd.getMaNV() != null && selectedNV.getMaNV().equals(hd.getMaNV().getMaNV())) {
                            baseList.add(hd);
                        }
                    }
                }
                if (!allPrice) {
                    double min = 0, max = Double.MAX_VALUE;
                    switch (selectedPrice) {
                        case "Dưới 500.000": max = 500000; break;
                        case "500.000 - 1.000.000": min = 500000; max = 1000000; break;
                        case "1.000.000 - 2.000.000": min = 1000000; max = 2000000; break;
                        case "Trên 2.000.000": min = 2000000; break;
                    }
                    ArrayList<HoaDonDTO> priceFiltered = new ArrayList<>();
                    for (HoaDonDTO hd : baseList) {
                        double t = hd.getTongTien();
                        if (t >= min && t < max) priceFiltered.add(hd);
                    }
                    baseList = priceFiltered;
                }
                if (fromDate != null || toDate != null) {
                    ArrayList<HoaDonDTO> dateFiltered = new ArrayList<>();
                    for (HoaDonDTO hd : baseList) {
                        LocalDate ngayTao = hd.getNgayTao();
                        boolean afterFrom = (fromDate == null) || !ngayTao.isBefore(fromDate);
                        boolean beforeTo = (toDate == null) || !ngayTao.isAfter(toDate);
                        if (afterFrom && beforeTo) dateFiltered.add(hd);
                    }
                    baseList = dateFiltered;
                }
                return baseList;
            }
        };
        task.setOnSucceeded(e -> table_invoice.setItems(FXCollections.observableArrayList(task.getValue())));
        new Thread(task).start();
    }

    private void searchHoaDon(String maHd) {
        Task<List<HoaDonDTO>> task = new Task<>() {
            @Override
            protected List<HoaDonDTO> call() {
                if (maHd == null || maHd.trim().isEmpty()) {
                    return clientManager.getAllHoaDon();
                } else {
                    return clientManager.searchHoaDonByMaHd(maHd);
                }
            }
        };
        task.setOnSucceeded(e -> table_invoice.setItems(FXCollections.observableArrayList(task.getValue())));
        new Thread(task).start();
    }

    private VBox createVBox(String label, Control field) {
        VBox v = new VBox(5);
        if (label != null && !label.isEmpty()) {
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

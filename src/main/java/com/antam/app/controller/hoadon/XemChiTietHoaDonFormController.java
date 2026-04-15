package com.antam.app.controller.hoadon;


import com.antam.app.dao.impl.ChiTietHoaDon_DAO;
import com.antam.app.dao.impl.DonViTinh_DAO;
import com.antam.app.dao.impl.LoThuoc_DAO;
import com.antam.app.dao.impl.Thuoc_DAO;
import com.antam.app.entity.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class XemChiTietHoaDonFormController extends DialogPane{

    private Text txtInvoiceId;
    private Text txtDate;
    private Text txtCustomer;
    private Text txtEmployee;
    private Text txtSubTotal;
    private Text txtVAT;
    private Text txtTotal;
    private Text txtPromotion;
    private Text txtReturnTotal;
    private TableView<ChiTietHoaDon> tableListsThuoc;
    private TableColumn<ChiTietHoaDon, Integer> sttCol = new TableColumn<>("STT");
    private TableColumn<ChiTietHoaDon, String> tenThuocCol = new TableColumn<>("Tên thuốc");
    private TableColumn<ChiTietHoaDon, Integer> soLuongCol = new TableColumn<>("Số lượng");
    private TableColumn<ChiTietHoaDon, Double> donGiaCol = new TableColumn<>("Đơn giá");
    private TableColumn<ChiTietHoaDon, Double> thanhTienCol = new TableColumn<>("Thành tiền");
    private TableColumn<ChiTietHoaDon, String> trangThaiCol = new TableColumn<>("Trạng thái");
    private TableColumn<ChiTietHoaDon, String> dvtCol = new TableColumn<>("Đơn vị tính");
    private TableColumn<ChiTietHoaDon, Double> thueCol = new TableColumn<>("Thuế");

    // Định dạng tiền tệ kiểu Việt Nam: 1.000đ, 10.000đ
    private static final DecimalFormat VND_FORMAT;
    static {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.');
        symbols.setDecimalSeparator(',');
        VND_FORMAT = new DecimalFormat("#,##0", symbols);
    }

    public XemChiTietHoaDonFormController(){
        /** Giao diện **/
        // Thêm nút Huỷ và Xác nhận trả thuốc
        ButtonType cancelButton = new ButtonType("Đóng", ButtonBar.ButtonData.CANCEL_CLOSE);
        this.getButtonTypes().add(cancelButton);

        FlowPane header = new FlowPane();
        header.setAlignment(javafx.geometry.Pos.CENTER);
        header.setStyle("-fx-background-color: #1e3a8a;");

        Text headerText = new Text("Chi tiết hóa đơn");
        headerText.setFill(javafx.scene.paint.Color.WHITE);
        headerText.setFont(Font.font("System Bold", 15));

        FlowPane.setMargin(headerText, new Insets(10, 0, 10, 0));
        header.getChildren().add(headerText);

        this.setHeader(header);

        AnchorPane root = new AnchorPane();
        VBox mainBox = new VBox(10);
        AnchorPane.setLeftAnchor(mainBox, 0.0);
        AnchorPane.setRightAnchor(mainBox, 0.0);

        VBox infoBox = new VBox();
        infoBox.setStyle("-fx-background-color: #2563eb; -fx-background-radius: 5px;");
        infoBox.setPadding(new Insets(10, 100, 10, 10));

        txtInvoiceId = createText("Hóa đơn: HD001", 20, true);
        txtDate = createText("Ngày: 9/12/2024", 13, false);
        txtCustomer = createText("Khách hàng: Nguyễn Văn A", 13, false);
        txtEmployee = createText("Nhân viên: Nguyễn Văn An", 13, false);

        infoBox.getChildren().addAll(txtInvoiceId, txtDate, txtCustomer, txtEmployee);

        mainBox.getChildren().add(infoBox);

        Text listLabel = new Text("Danh sách thuốc:");
        mainBox.getChildren().add(listLabel);

        tableListsThuoc = new TableView<>();
        tableListsThuoc.setPrefWidth(200);
        tableListsThuoc.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        tableListsThuoc.getColumns().addAll(
                sttCol, tenThuocCol, soLuongCol, donGiaCol, thanhTienCol, dvtCol, thueCol
        );

        mainBox.getChildren().add(tableListsThuoc);

        GridPane grid = new GridPane();
        grid.setHgap(5);
        grid.setStyle("-fx-background-color: #f8fafc; -fx-background-radius: 8px;");
        grid.setPadding(new Insets(10));

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setHgrow(Priority.SOMETIMES);

        ColumnConstraints col2 = new ColumnConstraints();
        col2.setHgrow(Priority.SOMETIMES);

        grid.getColumnConstraints().addAll(col1, col2);

        for (int i = 0; i < 5; i++) {
            grid.getRowConstraints().add(new RowConstraints());
        }

        addRow(grid, 0, "Tạm tính:", txtSubTotal = createText("200.000đ"));
        addRow(grid, 1, "Thuế GTGT (theo từng loại thuốc):", txtVAT = createText("12.500 ₫"));
        addRow(grid, 2, "Tiền hàng trả:", txtReturnTotal = createText("0đ"));
        addRowBold(grid, 3, "Tổng tiền:", txtTotal = createText("500.000đ", 18, true));
        addRow(grid, 4, "", txtPromotion = createText("KM001"));

        mainBox.getChildren().add(grid);

        mainBox.setPadding(new Insets(10));

        root.getChildren().add(mainBox);
        String css = getClass().getResource("/com/antam/app/styles/dashboard_style.css").toExternalForm();
        this.getStylesheets().addAll(css);
        this.setContent(root);
        this.setStyle("-fx-background-color: white;");
        this.setPrefWidth(800);
        /** Sự kiện **/
        tenThuocCol.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().getMaLoThuoc().getMaThuoc().getTenThuoc()
                )
        );
        soLuongCol.setCellValueFactory(new PropertyValueFactory<>("soLuong"));
        donGiaCol.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleDoubleProperty(
                        cellData.getValue().getMaLoThuoc().getMaThuoc().getGiaBan()
                ).asObject()
        );
        donGiaCol.setCellFactory(col -> new javafx.scene.control.TableCell<ChiTietHoaDon, Double>() {
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
        thanhTienCol.setCellValueFactory(cellData -> {
            double donGia = 0;
            int soLuong = cellData.getValue().getSoLuong();
            if (cellData.getValue().getMaLoThuoc() != null && cellData.getValue().getMaLoThuoc().getMaThuoc() != null) {
                donGia = cellData.getValue().getMaLoThuoc().getMaThuoc().getGiaBan();
            }
            return new javafx.beans.property.SimpleDoubleProperty(donGia * soLuong).asObject();
        });
        thanhTienCol.setCellFactory(col -> new javafx.scene.control.TableCell<ChiTietHoaDon, Double>() {
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
        trangThaiCol.setCellValueFactory(new PropertyValueFactory<>("tinhTrang"));
        dvtCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(
                cellData.getValue().getMaDVT().getTenDVT()));
        // Thuế GTGT
        thueCol.setCellValueFactory(cellData -> {
            double thue = 0;
            if (cellData.getValue().getMaLoThuoc() != null && cellData.getValue().getMaLoThuoc().getMaThuoc() != null) {
                thue = cellData.getValue().getMaLoThuoc().getMaThuoc().getThue();
            }
            return new javafx.beans.property.SimpleDoubleProperty(thue).asObject();
        });
        thueCol.setCellFactory(col -> new javafx.scene.control.TableCell<ChiTietHoaDon, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("%.0f%%", item * 100));
                }
            }
        });
        // STT custom
        sttCol.setCellFactory(col -> new javafx.scene.control.TableCell<>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(String.valueOf(getIndex() + 1));
                }
            }
        });
    }

    private Text createText(String text) {
        return createText(text, 13, false);
    }

    private Text createText(String text, int size, boolean bold) {
        Text t = new Text(text);
        t.setFill(javafx.scene.paint.Color.WHITE);
        t.setFont(bold ? Font.font("System Bold", size) : Font.font(size));
        return t;
    }

    private void addRow(GridPane grid, int row, String label, Text value) {
        Text lbl = new Text(label);
        lbl.setFill(javafx.scene.paint.Color.web("#374151"));
        lbl.setFont(Font.font(13));

        value.setFill(javafx.scene.paint.Color.web("#374151"));

        grid.add(lbl, 0, row);
        grid.add(value, 1, row);
    }

    private void addRowBold(GridPane grid, int row, String label, Text value) {
        Text lbl = new Text(label);
        lbl.setFill(javafx.scene.paint.Color.web("#374151"));
        lbl.setFont(Font.font("System Bold", 18));

        value.setFill(javafx.scene.paint.Color.web("#374151"));

        grid.add(lbl, 0, row);
        grid.add(value, 1, row);
    }

    public void setInvoice(HoaDon hoaDon) {
        if (hoaDon == null) return;
        txtInvoiceId.setText("Hóa đơn: " + hoaDon.getMaHD());
        txtDate.setText("Ngày: " + hoaDon.getNgayTao().toString());
        txtCustomer.setText("Khách hàng: " + hoaDon.getMaKH().getTenKH());
        txtEmployee.setText("Nhân viên: " + hoaDon.getMaNV().getHoTen());
        // Lấy danh sách chi tiết hóa đơn từ DAO
        ChiTietHoaDon_DAO cthdDAO = new ChiTietHoaDon_DAO();
        LoThuoc_DAO chiTietThuoc_dao = new LoThuoc_DAO();
        Thuoc_DAO thuoc_dao = new Thuoc_DAO();
        DonViTinh_DAO donViTinh_dao = new DonViTinh_DAO();
        java.util.List<ChiTietHoaDon> list = cthdDAO.getAllChiTietHoaDonTheoMaHD(hoaDon.getMaHD());
        double subTotal = 0;
        double returnTotal = 0;
        double soldItemsTotal = 0; // Tổng tiền của hàng bán
        double vatTotal = 0; // Tổng thuế VAT của hàng bán
        for (ChiTietHoaDon chiTietHoaDon : list) {
            LoThuoc ctt = chiTietThuoc_dao.getChiTietThuoc(chiTietHoaDon.getMaLoThuoc().getMaLoThuoc());
            if (ctt != null) {
                Thuoc t = thuoc_dao.getThuocTheoMa(ctt.getMaThuoc().getMaThuoc());
                if (t != null) {
                    ctt.setMaThuoc(t);
                }
                chiTietHoaDon.setMaLoThuoc(ctt);
            }
            // Lấy lại thông tin đơn vị tính
            DonViTinh dvt = donViTinh_dao.getDVTTheoMa(chiTietHoaDon.getMaDVT().getMaDVT());
            if (dvt != null) {
                chiTietHoaDon.setMaDVT(dvt);
            }
            subTotal += chiTietHoaDon.getThanhTien();

            // Tính tiền hàng trả
            if ("Trả".equalsIgnoreCase(chiTietHoaDon.getTinhTrang()) || "Đổi".equalsIgnoreCase(chiTietHoaDon.getTinhTrang())) {
                returnTotal += chiTietHoaDon.getThanhTien();
            } else {
                // Chỉ tính thuế VAT cho hàng bán (không phải trả hoặc đổi)
                soldItemsTotal += chiTietHoaDon.getThanhTien();
                if (chiTietHoaDon.getMaLoThuoc() != null && chiTietHoaDon.getMaLoThuoc().getMaThuoc() != null) {
                    double thue = chiTietHoaDon.getMaLoThuoc().getMaThuoc().getThue(); // 0.1 = 10%
                    double thanhTien = chiTietHoaDon.getThanhTien();
                    // Tính thuế VAT: nếu giá chưa bao gồm thuế thì nhân trực tiếp
                    vatTotal += thanhTien * thue;
                }
            }
        }
        ObservableList<ChiTietHoaDon> data = FXCollections.observableArrayList(list);
        tableListsThuoc.setItems(data);
        // Tính toán các giá trị tiền
        double tongTien = hoaDon.getTongTien();
        txtTotal.setText(VND_FORMAT.format(tongTien) + "đ");
        txtVAT.setText(VND_FORMAT.format(vatTotal) + "đ");
        txtSubTotal.setText(VND_FORMAT.format(subTotal) + "đ");
        txtReturnTotal.setText(VND_FORMAT.format(returnTotal) + "đ");
        txtPromotion.setText(hoaDon.getMaKM() != null ? hoaDon.getMaKM().getTenKM() : "Không có khuyến mãi");
    }
}

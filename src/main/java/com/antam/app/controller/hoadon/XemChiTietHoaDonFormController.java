package com.antam.app.controller.hoadon;


import com.antam.app.dto.*;
import com.antam.app.network.ClientManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class XemChiTietHoaDonFormController extends DialogPane{
    private final ClientManager clientManager = ClientManager.getInstance();

    private Text txtInvoiceId;
    private Text txtDate;
    private Text txtCustomer;
    private Text txtEmployee;
    private Text txtSubTotal;
    private Text txtVAT;
    private Text txtTotal;
    private Text txtPromotion;
    private Text txtReturnTotal;
    private TableView<ChiTietHoaDonDTO> tableListsThuoc;
    private TableColumn<ChiTietHoaDonDTO, Integer> sttCol = new TableColumn<>("STT");
    private TableColumn<ChiTietHoaDonDTO, String> tenThuocCol = new TableColumn<>("Tên thuốc");
    private TableColumn<ChiTietHoaDonDTO, Integer> soLuongCol = new TableColumn<>("Số lượng");
    private TableColumn<ChiTietHoaDonDTO, Double> donGiaCol = new TableColumn<>("Đơn giá");
    private TableColumn<ChiTietHoaDonDTO, Double> thanhTienCol = new TableColumn<>("Thành tiền");
    private TableColumn<ChiTietHoaDonDTO, String> trangThaiCol = new TableColumn<>("Trạng thái");
    private TableColumn<ChiTietHoaDonDTO, String> dvtCol = new TableColumn<>("Đơn vị tính");
    private TableColumn<ChiTietHoaDonDTO, Double> thueCol = new TableColumn<>("Thuế");

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
                sttCol, tenThuocCol, soLuongCol, donGiaCol, thanhTienCol, dvtCol, trangThaiCol, thueCol
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
                        getTenThuoc(cellData.getValue())
                )
        );
        soLuongCol.setCellValueFactory(new PropertyValueFactory<>("soLuong"));
        donGiaCol.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleDoubleProperty(
                        getDonGia(cellData.getValue())
                ).asObject()
        );
        donGiaCol.setCellFactory(col -> new TableCell<ChiTietHoaDonDTO, Double>() {
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
            ChiTietHoaDonDTO chiTiet = cellData.getValue();
            double thanhTien = chiTiet != null ? chiTiet.getThanhTien() : 0;
            return new javafx.beans.property.SimpleDoubleProperty(thanhTien).asObject();
        });
        thanhTienCol.setCellFactory(col -> new TableCell<ChiTietHoaDonDTO, Double>() {
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
                getTenDonViTinh(cellData.getValue())));
        // Thuế GTGT
        thueCol.setCellValueFactory(cellData -> {
            return new javafx.beans.property.SimpleDoubleProperty(getThue(cellData.getValue())).asObject();
        });
        thueCol.setCellFactory(col -> new TableCell<ChiTietHoaDonDTO, Double>() {
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
        sttCol.setCellFactory(col -> new TableCell<>() {
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

    public void setInvoice(HoaDonDTO hoaDonDTO) {
        if (hoaDonDTO == null) return;
        HoaDonDTO invoice = getHoaDonDayDu(hoaDonDTO);
        txtInvoiceId.setText("Hóa đơn: " + safeText(invoice.getMaHD()));
        txtDate.setText("Ngày: " + (invoice.getNgayTao() != null ? invoice.getNgayTao().toString() : "N/A"));
        txtCustomer.setText("Khách hàng: " + getTenKhachHang(invoice));
        txtEmployee.setText("Nhân viên: " + getTenNhanVien(invoice));
        java.util.List<ChiTietHoaDonDTO> list = new java.util.ArrayList<>(clientManager.getChiTietHoaDonByHoaDonId(invoice.getMaHD()));
        double subTotal = 0;
        double returnTotal = 0;
        double soldItemsTotal = 0; // Tổng tiền của hàng bán
        double vatTotal = 0; // Tổng thuế VAT của hàng bán
        for (ChiTietHoaDonDTO chiTietHoaDonDTO : list) {
            enrichChiTietHoaDon(chiTietHoaDonDTO);
            subTotal += chiTietHoaDonDTO.getThanhTien();

            // Tính tiền hàng trả
            if ("Trả".equalsIgnoreCase(chiTietHoaDonDTO.getTinhTrang()) || "Đổi".equalsIgnoreCase(chiTietHoaDonDTO.getTinhTrang())) {
                returnTotal += chiTietHoaDonDTO.getThanhTien();
            } else {
                // Chỉ tính thuế VAT cho hàng bán (không phải trả hoặc đổi)
                soldItemsTotal += chiTietHoaDonDTO.getThanhTien();
                double thue = getThue(chiTietHoaDonDTO); // 0.1 = 10%
                double thanhTien = chiTietHoaDonDTO.getThanhTien();
                // Tính thuế VAT: nếu giá chưa bao gồm thuế thì nhân trực tiếp
                vatTotal += thanhTien * thue;
            }
        }
        ObservableList<ChiTietHoaDonDTO> data = FXCollections.observableArrayList(list);
        tableListsThuoc.setItems(data);
        // Tính toán các giá trị tiền
        double tongTien = invoice.getTongTien();
        txtTotal.setText(VND_FORMAT.format(tongTien) + "đ");
        txtVAT.setText(VND_FORMAT.format(vatTotal) + "đ");
        txtSubTotal.setText(VND_FORMAT.format(subTotal) + "đ");
        txtReturnTotal.setText(VND_FORMAT.format(returnTotal) + "đ");
        txtPromotion.setText(invoice.getMaKM() != null && invoice.getMaKM().getTenKM() != null ? invoice.getMaKM().getTenKM() : "Không có khuyến mãi");
    }

    private HoaDonDTO getHoaDonDayDu(HoaDonDTO hoaDonDTO) {
        try {
            if (hoaDonDTO.getMaHD() != null) {
                Object result = clientManager.getHoaDonById(hoaDonDTO.getMaHD());
                if (result instanceof HoaDonDTO dto) {
                    return dto;
                }
            }
        } catch (Exception e) {
            System.err.println("Không thể tải hóa đơn từ server: " + e.getMessage());
        }
        return hoaDonDTO;
    }

    private void enrichChiTietHoaDon(ChiTietHoaDonDTO chiTiet) {
        if (chiTiet == null) return;
        LoThuocDTO loThuoc = chiTiet.getMaLoThuocDTO();
        if (loThuoc != null && loThuoc.getMaLoThuoc() > 0) {
            LoThuocDTO loThuocDayDu = clientManager.getLoThuocByLoThuocId(loThuoc.getMaLoThuoc());
            if (loThuocDayDu != null) {
                ThuocDTO thuocDTO = loThuocDayDu.getMaThuocDTO();
                if (thuocDTO != null && thuocDTO.getMaThuoc() != null) {
                    ThuocDTO thuocDayDu = clientManager.getThuocById(thuocDTO.getMaThuoc());
                    if (thuocDayDu != null) {
                        loThuocDayDu.setMaThuocDTO(thuocDayDu);
                    }
                }
                chiTiet.setMaLoThuocDTO(loThuocDayDu);
            } else if (loThuoc.getMaThuocDTO() != null && loThuoc.getMaThuocDTO().getMaThuoc() != null) {
                ThuocDTO thuocDayDu = clientManager.getThuocById(loThuoc.getMaThuocDTO().getMaThuoc());
                if (thuocDayDu != null) {
                    loThuoc.setMaThuocDTO(thuocDayDu);
                }
            }
        }
        if (chiTiet.getMaDVT() != null && chiTiet.getMaDVT().getMaDVT() > 0) {
            DonViTinhDTO dvt = clientManager.getDonViTinhById(chiTiet.getMaDVT().getMaDVT());
            if (dvt != null) {
                chiTiet.setMaDVT(dvt);
            }
        }
    }

    private String getTenThuoc(ChiTietHoaDonDTO chiTiet) {
        if (chiTiet != null && chiTiet.getMaLoThuocDTO() != null && chiTiet.getMaLoThuocDTO().getMaThuocDTO() != null) {
            String tenThuoc = chiTiet.getMaLoThuocDTO().getMaThuocDTO().getTenThuoc();
            if (tenThuoc != null && !tenThuoc.trim().isEmpty()) {
                return tenThuoc;
            }
        }
        return "N/A";
    }

    private double getDonGia(ChiTietHoaDonDTO chiTiet) {
        if (chiTiet != null && chiTiet.getMaLoThuocDTO() != null && chiTiet.getMaLoThuocDTO().getMaThuocDTO() != null) {
            return chiTiet.getMaLoThuocDTO().getMaThuocDTO().getGiaBan();
        }
        return chiTiet != null && chiTiet.getSoLuong() > 0 ? chiTiet.getThanhTien() / chiTiet.getSoLuong() : 0;
    }

    private double getThue(ChiTietHoaDonDTO chiTiet) {
        if (chiTiet != null && chiTiet.getMaLoThuocDTO() != null && chiTiet.getMaLoThuocDTO().getMaThuocDTO() != null) {
            return chiTiet.getMaLoThuocDTO().getMaThuocDTO().getThue();
        }
        return 0;
    }

    private String getTenDonViTinh(ChiTietHoaDonDTO chiTiet) {
        if (chiTiet != null && chiTiet.getMaDVT() != null && chiTiet.getMaDVT().getTenDVT() != null) {
            return chiTiet.getMaDVT().getTenDVT();
        }
        return "N/A";
    }

    private String getTenKhachHang(HoaDonDTO hoaDonDTO) {
        return hoaDonDTO.getMaKH() != null && hoaDonDTO.getMaKH().getTenKH() != null ? hoaDonDTO.getMaKH().getTenKH() : "N/A";
    }

    private String getTenNhanVien(HoaDonDTO hoaDonDTO) {
        return hoaDonDTO.getMaNV() != null && hoaDonDTO.getMaNV().getHoTen() != null ? hoaDonDTO.getMaNV().getHoTen() : "N/A";
    }

    private String safeText(String text) {
        return text != null ? text : "N/A";
    }
}

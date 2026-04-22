//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.antam.app.controller.hoadon;

import com.antam.app.connect.ConnectDB;
import com.antam.app.network.ClientManager;
import com.antam.app.dto.*;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.util.StringConverter;

import static javafx.collections.FXCollections.observableArrayList;

public class DoiThuocFormController extends DialogPane{

    private VBox vhDSCTHD, vhDSCTHDM;
    private TextField txtMaHoaDonDoi, txtKhachHangDoi;
    private Text txtTongTienDoi, txtTongTienTra, txtTongTienMua, txtThongBaoDoi;
    private Button btnThemMoiThuoc;
    private ComboBox<String> cbLyDoDoi;

    private final ClientManager clientManager;
    private HoaDonDTO hoaDonDTO;
    private ArrayList<ChiTietHoaDonDTO> selectedItems = new ArrayList<>();
    private ArrayList<ChiTietHoaDonDTO> chiTietHoaDons;
    private int soLuongThuoc = 0;
    private int soLuongThuocDoi = 0;

    public void setHoaDon(HoaDonDTO hoaDonDTO) {
        this.hoaDonDTO = hoaDonDTO;
    }

    public HoaDonDTO getHoaDon() {
        return hoaDonDTO;
    }

    public void showData(HoaDonDTO hoaDonDTO) {
        HoaDonDTO hd = clientManager.getHoaDonById(hoaDonDTO.getMaHD());
        chiTietHoaDons = new ArrayList<>(clientManager.getChiTietHoaDonByHoaDonId(hoaDonDTO.getMaHD()));
        txtMaHoaDonDoi.setText(hd == null ? hoaDonDTO.getMaHD() : hd.getMaHD());
        vhDSCTHD.getChildren().clear();
        selectedItems.clear();
        soLuongThuoc = 0;
        soLuongThuocDoi = 0;
        if (hd != null && hd.getMaKH() != null) {
            KhachHangDTO khachHangDTO = clientManager.getKhachHangById(hd.getMaKH().getMaKH());
            txtKhachHangDoi.setText(khachHangDTO == null ? "" : khachHangDTO.getTenKH());
        } else {
            txtKhachHangDoi.setText("");
        }

        for (ChiTietHoaDonDTO ct : chiTietHoaDons) {
            HBox hBox = renderChiTietHoaDon(ct);
            vhDSCTHD.getChildren().add(hBox);
            soLuongThuoc += 1;
        }

        txtTongTienTra.setText("0 đ");
        txtTongTienMua.setText("0 đ");
        txtTongTienDoi.setText("0 đ");
    }

    public DoiThuocFormController(){
        this.clientManager = ClientManager.getInstance();
        FlowPane header = new FlowPane();
        header.setAlignment(Pos.CENTER);
        header.setStyle("-fx-background-color: #1e3a8a;");

        Text title = new Text("Đổi thuốc");
        title.setFill(javafx.scene.paint.Color.WHITE);
        title.setFont(Font.font("System Bold", 15));
        FlowPane.setMargin(title, new Insets(10, 0, 10, 0));

        header.getChildren().add(title);
        this.setHeader(header);

        // ============================
        // CONTENT ROOT
        // ============================
        AnchorPane root = new AnchorPane();
        root.setPrefSize(800, 600);

        VBox mainVBox = new VBox(10);
        AnchorPane.setLeftAnchor(mainVBox, 0.0);
        AnchorPane.setRightAnchor(mainVBox, 0.0);

        // ============================
        // SCROLL CONTENT
        // ============================
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(480);

        VBox contentVBox = new VBox(10);

        // ============================
        // GRID 1
        // ============================
        GridPane grid1 = new GridPane();
        grid1.setHgap(5);

        ColumnConstraints c1 = new ColumnConstraints();
        c1.setHgrow(Priority.SOMETIMES);
        ColumnConstraints c2 = new ColumnConstraints();
        c2.setHgrow(Priority.SOMETIMES);
        grid1.getColumnConstraints().addAll(c1, c2);

        RowConstraints r1 = new RowConstraints();
        r1.setPrefHeight(30);
        RowConstraints r2 = new RowConstraints();
        r2.setPrefHeight(40);
        RowConstraints r3 = new RowConstraints();
        r3.setPrefHeight(30);
        RowConstraints r4 = new RowConstraints();
        r4.setPrefHeight(30);
        grid1.getRowConstraints().addAll(r1, r2, r3, r4);

        Text lblMaHD = new Text("Mã hóa đơn gốc:");
        lblMaHD.setFill(javafx.scene.paint.Color.web("#374151"));

        txtMaHoaDonDoi = new TextField();
        txtMaHoaDonDoi.setPrefHeight(40);
        txtMaHoaDonDoi.getStyleClass().add("text-field");
        txtMaHoaDonDoi.getStylesheets().add(getClass().getResource("/com/antam/app/styles/dashboard_style.css").toExternalForm());
        GridPane.setRowIndex(txtMaHoaDonDoi, 1);

        txtKhachHangDoi = new TextField();
        txtKhachHangDoi.setPrefHeight(40);
        txtKhachHangDoi.getStyleClass().add("text-field");
        txtKhachHangDoi.getStylesheets().add(getClass().getResource("/com/antam/app/styles/dashboard_style.css").toExternalForm());
        GridPane.setRowIndex(txtKhachHangDoi, 1);
        GridPane.setColumnIndex(txtKhachHangDoi, 1);

        Text lblKH = new Text("Khách hàng:");
        lblKH.setFill(javafx.scene.paint.Color.web("#374151"));
        GridPane.setColumnIndex(lblKH, 1);

        Text lblLyDo = new Text("Lý do đổi:");
        lblLyDo.setFill(javafx.scene.paint.Color.web("#374151"));
        GridPane.setRowIndex(lblLyDo, 2);

        cbLyDoDoi = new ComboBox<>();
        cbLyDoDoi.setPrefWidth(150);
        cbLyDoDoi.getStylesheets().add(getClass().getResource("/com/antam/app/styles/dashboard_style.css").toExternalForm());
        GridPane.setRowIndex(cbLyDoDoi, 3);

        grid1.getChildren().addAll(lblMaHD, txtMaHoaDonDoi, txtKhachHangDoi, lblKH, lblLyDo, cbLyDoDoi);

        // ============================
        // LIST CTHD GỐC
        // ============================
        vhDSCTHD = new VBox(5);
        vhDSCTHD.setStyle("-fx-border-color: #e5e7eb; -fx-border-radius: 6px; -fx-border-width: 2px;");
        vhDSCTHD.setPadding(new Insets(10));

        // ============================
        // GRID TỔNG TRẢ
        // ============================
        GridPane gridTongTra = new GridPane();
        gridTongTra.setHgap(5);
        gridTongTra.setStyle("-fx-background-color: #f8fafc; -fx-background-radius: 8px;");
        gridTongTra.setPadding(new Insets(10));

        ColumnConstraints t1 = new ColumnConstraints();
        t1.setHgrow(Priority.SOMETIMES);
        ColumnConstraints t2 = new ColumnConstraints();
        t2.setHgrow(Priority.SOMETIMES);
        gridTongTra.getColumnConstraints().addAll(t1, t2);

        txtTongTienTra = new Text("500.000đ");
        txtTongTienTra.setFill(javafx.scene.paint.Color.web("#374151"));
        txtTongTienTra.setFont(Font.font("System Bold", 13));
        GridPane.setColumnIndex(txtTongTienTra, 1);

        Text lblTongTra = new Text("Tổng tiền trả:");
        lblTongTra.setFill(javafx.scene.paint.Color.web("#374151"));
        lblTongTra.setFont(Font.font("System Bold", 13));

        gridTongTra.getChildren().addAll(lblTongTra, txtTongTienTra);

        // ============================
        // LABEL “Thuốc mới”
        // ============================
        Text lblThuocMoi = new Text("Thuốc mới:");

        // ============================
        // LIST CTHDM MỚI
        // ============================
        vhDSCTHDM = new VBox(5);
        vhDSCTHDM.setStyle("-fx-border-color: #e5e7eb; -fx-border-radius: 6px; -fx-border-width: 2px;");
        vhDSCTHDM.setPadding(new Insets(10));

        // ============================
        // BUTTON THÊM THUỐC MỚI
        // ============================
        btnThemMoiThuoc = new Button("Thêm thuốc mới");
        btnThemMoiThuoc.setStyle("-fx-background-color: #6b7280; -fx-font-size: 14px; -fx-font-weight: BOLD;");
        btnThemMoiThuoc.setTextFill(javafx.scene.paint.Color.WHITE);
        btnThemMoiThuoc.setPadding(new Insets(5, 10, 5, 10));

        // ============================
        // ADD to contentVBox
        // ============================
        contentVBox.getChildren().addAll(
                grid1,
                vhDSCTHD,
                gridTongTra,
                lblThuocMoi,
                vhDSCTHDM,
                btnThemMoiThuoc
        );

        scrollPane.setContent(contentVBox);

        // ============================
        // GRID TỔNG MUA (BOTTOM)
        // ============================
        GridPane gridTongMua = new GridPane();
        gridTongMua.setHgap(5);
        gridTongMua.setStyle("-fx-background-color: #f8fafc; -fx-background-radius: 8px;");
        gridTongMua.setPadding(new Insets(10));

        ColumnConstraints m1 = new ColumnConstraints();
        m1.setHgrow(Priority.SOMETIMES);
        ColumnConstraints m2 = new ColumnConstraints();
        m2.setHgrow(Priority.SOMETIMES);
        gridTongMua.getColumnConstraints().addAll(m1, m2);

        Text lblTongMua = new Text("Tổng tiền mua:");
        lblTongMua.setFill(javafx.scene.paint.Color.web("#374151"));
        lblTongMua.setFont(Font.font("System Bold", 13));

        txtTongTienMua = new Text("1.000.000đ");
        txtTongTienMua.setFill(javafx.scene.paint.Color.web("#374151"));
        txtTongTienMua.setFont(Font.font("System Bold", 13));
        GridPane.setColumnIndex(txtTongTienMua, 1);

        gridTongMua.getChildren().addAll(lblTongMua, txtTongTienMua);

        // ============================
        // KẾT QUẢ CUỐI
        // ============================
        VBox summaryBox = new VBox();
        summaryBox.setAlignment(Pos.CENTER);
        summaryBox.setStyle("-fx-background-color: #f8fafc; -fx-border-color: #2563eb; -fx-border-radius: 6px; -fx-border-width: 2px;");
        summaryBox.setPadding(new Insets(10));

        txtTongTienDoi = new Text("Tổng kết: 0 ₫");
        txtTongTienDoi.setFill(javafx.scene.paint.Color.web("#1e3a8a"));
        txtTongTienDoi.setFont(Font.font("System Bold", 22));

        txtThongBaoDoi = new Text("Không phát sinh thêm tiền");
        txtThongBaoDoi.setFill(javafx.scene.paint.Color.web("#6b6b6b"));
        txtThongBaoDoi.setFont(Font.font("System Italic", 13));

        summaryBox.getChildren().addAll(txtTongTienDoi, txtThongBaoDoi);

        // ============================
        // ADD ALL INTO mainVBox
        // ============================
        mainVBox.getChildren().addAll(
                scrollPane,
                gridTongMua,
                summaryBox
        );

        root.getChildren().add(mainVBox);
        this.setContent(root);
        /** Sự kiện **/
        ButtonType cancelButton = new ButtonType("Huỷ", ButtonData.CANCEL_CLOSE);
        ButtonType applyButton = new ButtonType("Xác nhận đổi thuốc", ButtonData.APPLY);
        this.getButtonTypes().add(cancelButton);
        this.getButtonTypes().add(applyButton);

        Button btnApply = (Button) this.lookupButton(applyButton);
        btnApply.addEventFilter(ActionEvent.ACTION, event -> {
            String lyDo = cbLyDoDoi.getValue();
            if (lyDo == null || lyDo.trim().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Cảnh báo");
                alert.setHeaderText("Chưa chọn lý do trả thuốc");
                alert.setContentText("Vui lòng chọn lý do trả trước khi xác nhận.");
                alert.showAndWait();
                event.consume();
                return;
            }
            if (selectedItems.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Cảnh báo");
                alert.setHeaderText("Chưa chọn thuốc để đổi");
                alert.setContentText("Vui lòng chọn ít nhất một thuốc để đổi.");
                alert.showAndWait();
                event.consume();
            }
            if (vhDSCTHDM.getChildren().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Cảnh báo");
                alert.setHeaderText("Chưa thêm thuốc mới để đổi");
                alert.setContentText("Vui lòng thêm ít nhất một thuốc mới để đổi.");
                alert.showAndWait();
                event.consume();
            }
            if (!checkThongTinThuoc()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Cảnh báo");
                alert.setHeaderText("Thông tin thuốc mới không hợp lệ");
                alert.setContentText("Vui lòng kiểm tra lại thông tin thuốc mới.");
                alert.showAndWait();
                event.consume();
            }
            else {
                // BƯỚC 1: Validate tất cả dữ liệu TRƯỚC khi xóa/sửa gì cả
                boolean validationPassed = true;
                String validationMessage = "";

                for (Node node : vhDSCTHDM.getChildren()) {
                    if (node instanceof HBox hBox) {
                        VBox vbThuoc = (VBox) hBox.getChildren().get(0);
                        VBox vbDVT = (VBox) hBox.getChildren().get(1);
                        VBox vbSoLuong = (VBox) hBox.getChildren().get(2);

                        ComboBox<ThuocDTO> comboThuoc = (ComboBox<ThuocDTO>) vbThuoc.getChildren().get(1);
                        ComboBox<DonViTinhDTO> comboDonVi = (ComboBox<DonViTinhDTO>) vbDVT.getChildren().get(1);
                        Spinner<Integer> spinnerSoLuong = (Spinner<Integer>) vbSoLuong.getChildren().get(1);

                        if (comboThuoc.getValue() != null && comboDonVi.getValue() != null && spinnerSoLuong.getValue() != null) {
                            ThuocDTO t = comboThuoc.getValue();
                            int soLuong = spinnerSoLuong.getValue();
                            ArrayList<LoThuocDTO> listCTT = new ArrayList<>(clientManager.getLoThuocFefoByThuocId(t.getMaThuoc()));
                            ArrayList<LoThuocDTO> danhSachLoHopLe = new ArrayList<>();
                            int tongSoLuong = 0;

                            for (LoThuocDTO cts : listCTT) {
                                boolean daTonTaiTrongHoaDon = clientManager.tonTaiChiTietHoaDon(
                                        hoaDonDTO.getMaHD(),
                                        cts.getMaLoThuoc()
                                );
                                if (!daTonTaiTrongHoaDon) {
                                    danhSachLoHopLe.add(cts);
                                    tongSoLuong += cts.getSoLuong();
                                }
                            }

                            if (tongSoLuong < soLuong) {
                                validationPassed = false;
                                validationMessage = "Số lượng thuốc trong kho không đủ hoặc bị trùng lô";
                                break;
                            }

                            // Check xem đã có 2 dòng "Thuốc Mới Khi Đổi" cho lô nào không
                            for (LoThuocDTO ctt : danhSachLoHopLe) {
                                int countThuocMoiKhiDoi = chiTietHoaDons.stream()
                                    .filter(ct -> ct.getMaLoThuocDTO().getMaLoThuoc() == ctt.getMaLoThuoc()
                                               && "Thuốc Mới Khi Đổi".equals(ct.getTinhTrang()))
                                    .toList()
                                    .size();

                                if (countThuocMoiKhiDoi >= 2) {
                                    validationPassed = false;
                                    validationMessage = "Lô thuốc này đã được mua 2 lần trong hóa đơn này. Không thể mua thêm nữa.";
                                    break;
                                }
                            }

                            if (!validationPassed) {
                                break;
                            }
                        }
                    }
                }

                // Nếu validation không pass, hiển thị cảnh báo và RETURN (không làm gì cả)
                if (!validationPassed) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Cảnh báo");
                    alert.setHeaderText("Không thể thực hiện giao dịch");
                    alert.setContentText(validationMessage);
                    alert.showAndWait();
                    event.consume();
                    return;
                }

                // BƯỚC 2: Validation passed, giờ mới bắt đầu xóa/sửa
                for (ChiTietHoaDonDTO ct : selectedItems) {
                    clientManager.softDeleteChiTietHoaDon(ct.getMaHD().getMaHD(), ct.getMaLoThuocDTO().getMaLoThuoc(), "Trả Khi Đổi");
                    switch (lyDo) {
                        // Các lý do KHÔNG cộng lại vào kho
                        case "Hết hạn sử dụng":
                        case "Bao bì bị hư hỏng":
                        case "Thuốc lỗi / hư hỏng":
                        case "Thuốc bị thu hồi":
                            // Không làm gì cả
                            break;

                        // Các lý do CÓ cộng lại vào kho
                        case "Khách hàng đổi ý":
                        case "Nhập nhầm lô / dư":
                        case "Sai thông tin đơn / bảo hiểm":
                            clientManager.updateLoThuocQuantity(
                                    ct.getMaLoThuocDTO().getMaLoThuoc(),
                                    ct.getSoLuong()
                            );
                            break;

                        default:
                            System.out.println("Lý do trả không hợp lệ: " + lyDo);
                            break;
                    }
                }

                // BƯỚC 3: Insert các chi tiết hóa đơn mới
                for (Node node : vhDSCTHDM.getChildren()) {
                    if (node instanceof HBox hBox) {
                        VBox vbThuoc = (VBox) hBox.getChildren().get(0);
                        VBox vbDVT = (VBox) hBox.getChildren().get(1);
                        VBox vbSoLuong = (VBox) hBox.getChildren().get(2);

                        ComboBox<ThuocDTO> comboThuoc = (ComboBox<ThuocDTO>) vbThuoc.getChildren().get(1);
                        ComboBox<DonViTinhDTO> comboDonVi = (ComboBox<DonViTinhDTO>) vbDVT.getChildren().get(1);
                        Spinner<Integer> spinnerSoLuong = (Spinner<Integer>) vbSoLuong.getChildren().get(1);

                        if (comboThuoc.getValue() != null && comboDonVi.getValue() != null && spinnerSoLuong.getValue() != null) {
                            ThuocDTO t = comboThuoc.getValue();
                            int soLuong = spinnerSoLuong.getValue();
                            ArrayList<LoThuocDTO> listCTT = new ArrayList<>(clientManager.getLoThuocFefoByThuocId(t.getMaThuoc()));
                            ArrayList<LoThuocDTO> danhSachLoHopLe = new ArrayList<>();
                            double tongTienMua = 0;

                            for (LoThuocDTO cts : listCTT) {
                                boolean daTonTaiTrongHoaDon = clientManager.tonTaiChiTietHoaDon(
                                        hoaDonDTO.getMaHD(),
                                        cts.getMaLoThuoc()
                                );
                                if (!daTonTaiTrongHoaDon) {
                                    danhSachLoHopLe.add(cts);
                                }
                            }

                            for (LoThuocDTO ctt : danhSachLoHopLe) {
                                if (ctt.getSoLuong() >= soLuong) {
                                    ChiTietHoaDonDTO newCTHD = new ChiTietHoaDonDTO(
                                            hoaDonDTO,
                                            ctt,
                                            soLuong,
                                            comboDonVi.getValue(),
                                            "Thuốc Mới Khi Đổi",
                                            t.getGiaBan() * soLuong
                                    );
                                    clientManager.createChiTietHoaDon(newCTHD);
                                    clientManager.updateLoThuocQuantity(ctt.getMaLoThuoc(), -soLuong);
                                    tongTienMua += Math.round(t.getGiaBan() * soLuong * (1 + t.getThue()) * 100.0) / 100.0;
                                    break;
                                } else {
                                    soLuong -= ctt.getSoLuong();
                                    ChiTietHoaDonDTO newCTHD = new ChiTietHoaDonDTO(
                                            hoaDonDTO,
                                            ctt,
                                            ctt.getSoLuong(),
                                            comboDonVi.getValue(),
                                            "Thuốc Mới Khi Đổi",
                                            t.getGiaBan() * ctt.getSoLuong()
                                    );
                                    clientManager.createChiTietHoaDon(newCTHD);
                                    clientManager.updateLoThuocQuantity(ctt.getMaLoThuoc(), -ctt.getSoLuong());
                                    tongTienMua += Math.round(t.getGiaBan() * ctt.getSoLuong() * (1 + t.getThue()) * 100.0) / 100.0;
                                }
                            }

                            double tongTienCu = hoaDonDTO.getTongTien();
                            double tongTienTra = 0;
                            double tongTienCoKM = 0;
                            for (ChiTietHoaDonDTO ct : selectedItems) {
                                LoThuocDTO ctt = ct.getMaLoThuocDTO();
                                ThuocDTO thuocDTO = clientManager.getThuocById(ctt.getMaThuocDTO().getMaThuoc());
                                if (thuocDTO == null) {
                                    continue;
                                }
                                if (!ct.getTinhTrang().equals("Thuốc Mới Khi Đổi")){
                                    tongTienCoKM += ct.getThanhTien() * (1 + thuocDTO.getThue());
                                }else{
                                    tongTienTra += ct.getThanhTien() * (1 + thuocDTO.getThue());
                                }
                            }
                            if (hoaDonDTO.getMaKM() != null) {
                                String maKM = hoaDonDTO.getMaKM().getMaKM();
                                KhuyenMaiDTO km = clientManager.getKhuyenMaiById(maKM);

                                if (km != null) {
                                    tongTienCoKM = TinhTienKhuyenMai(tongTienCoKM, km.getSo());
                                }
                            }

                            clientManager.updateHoaDonTongTien(hoaDonDTO.getMaHD(), tongTienCu - tongTienCoKM - tongTienTra + tongTienMua);
                        }

                    }
                }

            }
        });
        // Them gia tri cho combobox ly do
        addValueCombobox();
        // Nút thêm mới thuốc
        btnThemMoiThuoc.setOnAction(e -> {
            renderChiTietHoaDonDoi(vhDSCTHDM);
        });
    }

    public void addValueCombobox(){
        ObservableList<String> lyDoList = observableArrayList(
                "Hết hạn sử dụng",
                "Bao bì bị hư hỏng",
                "Khách hàng đổi ý",
                "Thuốc lỗi / hư hỏng",
                "Nhập nhầm lô / dư",
                "Thuốc bị thu hồi",
                "Sai thông tin đơn / bảo hiểm"
        );
        cbLyDoDoi.setItems(lyDoList);
    }

    public HBox renderChiTietHoaDon(ChiTietHoaDonDTO chiTietHoaDonDTO) {
        if (chiTietHoaDonDTO == null) {
            HBox h = new HBox();
            h.setVisible(false);
            h.setManaged(false);
            return h;
        }
        if (chiTietHoaDonDTO.getTinhTrang().equals("Trả") || chiTietHoaDonDTO.getTinhTrang().equals("Trả Khi Đổi")) {
            HBox h = new HBox();
            h.setVisible(false);
            h.setManaged(false);
            return h;
        }
        HBox hBox = new HBox();
        hBox.setSpacing(20);
        hBox.setStyle(
                "-fx-background-color: #f8fafc;" +
                "-fx-padding: 10;"
        );

        CheckBox checkBox = new CheckBox();
        checkBox.setOnAction(event -> {
            if (chiTietHoaDonDTO.getTinhTrang().equals("Bán")) {
                soLuongThuocDoi += checkBox.isSelected() ? 1 : -1;
            }
            if (checkBox.isSelected()) {
                selectedItems.add(chiTietHoaDonDTO);
            } else {
                selectedItems.remove(chiTietHoaDonDTO);
            }
            tinhTongTien();
        });

        LoThuocDTO ctt = clientManager.getLoThuocById(chiTietHoaDonDTO.getMaLoThuocDTO().getMaLoThuoc());
        ThuocDTO t = ctt == null || ctt.getMaThuocDTO() == null ? null : clientManager.getThuocById(ctt.getMaThuocDTO().getMaThuoc());
        Text txtMaThuoc = new Text(t == null ? "" : t.getTenThuoc());
        txtMaThuoc.setStyle("-fx-font-size: 15px;");
        Text txtSoLuong = new Text("SL " + chiTietHoaDonDTO.getSoLuong());
        txtSoLuong.setStyle("-fx-font-size: 15px;");
        DecimalFormat df = new DecimalFormat("#,### đ");
        Text txtDonGia = new Text(df.format(chiTietHoaDonDTO.getThanhTien()));
        txtDonGia.setStyle("-fx-font-size: 15px;");
        String valueBtn = "Bình thường";
        if (chiTietHoaDonDTO.getTinhTrang().equals("Trả")) {
            valueBtn = "Đã trả";
        } else if (chiTietHoaDonDTO.getTinhTrang().equals("Trả Khi Đổi")) {
            valueBtn = "Đã đổi";
        } else if (chiTietHoaDonDTO.getTinhTrang().equals("Thuốc Mới Khi Đổi")){
            valueBtn = "Thuốc đổi";
        }
        Button btn = new Button(valueBtn);
        btn.setStyle(
                "-fx-background-color: #e0f2fe;" +
                "-fx-text-fill: #0369a1;" +
                "-fx-background-radius: 10;" +
                "-fx-font-size: 12px;"
        );

        hBox.getChildren().addAll(checkBox, txtMaThuoc, txtSoLuong, txtDonGia, btn);
        return hBox;
    }


    public void renderChiTietHoaDonDoi(VBox vbox) {
        HBox hBox = new HBox();
        hBox.setSpacing(20);
        hBox.setStyle(
                "-fx-background-color: #f8fafc;" +
                "-fx-padding: 10;"
        );
        hBox.setAlignment(Pos.CENTER);
        VBox vbThuoc = new VBox();
        ComboBox<ThuocDTO> comboBoxThuoc = new ComboBox<>();
        configureThuocComboBox(comboBoxThuoc);
        comboBoxThuoc.getStylesheets().add(
                getClass().getResource("/com/antam/app/styles/dashboard_style.css").toExternalForm()
        );
        comboBoxThuoc.setPromptText("Chọn thuốc");

        ArrayList<ThuocDTO> thuocs = new ArrayList<>(clientManager.getThuocList());
        for (ThuocDTO t : thuocs) {
            comboBoxThuoc.getItems().add(t);
        }
        VBox vbDVT = new VBox();
        ComboBox<DonViTinhDTO> comboBoxDVT = new ComboBox<>();
        configureDonViTinhComboBox(comboBoxDVT);
        comboBoxDVT.getStylesheets().add(
                getClass().getResource("/com/antam/app/styles/dashboard_style.css").toExternalForm()
        );
        comboBoxThuoc.setOnAction(event -> {
            comboBoxDVT.getItems().clear();
            ThuocDTO selectedThuoc = comboBoxThuoc.getValue();
            if (selectedThuoc != null && selectedThuoc.getMaDVTCoSo() != null) {
                DonViTinhDTO dvt = clientManager.getDonViTinhById(selectedThuoc.getMaDVTCoSo().getMaDVT());
                if (dvt != null) {
                    comboBoxDVT.getItems().add(dvt);
                    comboBoxDVT.getSelectionModel().selectFirst();
                }
            }
            tinhTongTien();
        });
        comboBoxDVT.setOnAction(event -> {
            tinhTongTien();
        });
        comboBoxDVT.setPromptText("Chọn đơn vị tính");
        VBox vbSoLuong = new VBox();
        Spinner<Integer> spinnerSoLuong = new Spinner<>();
        spinnerSoLuong.getStylesheets().add(
                getClass().getResource("/com/antam/app/styles/dashboard_style.css").toExternalForm()
        );
        spinnerSoLuong.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 1000, 0, 1));

        spinnerSoLuong.valueProperty().addListener((obs, oldValue, newValue) -> {
            tinhTongTien();
        });
        spinnerSoLuong.setPromptText("Nhập số lượng");
        Button btn = new Button("X");
        btn.setStyle(
                "-fx-padding: 10 15 10 15;" +
                "-fx-background-color: #ef4444;" +
                "-fx-background-radius: 50%;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 12px;" +
                "-fx-font-weight: BOLD;"
        );

        btn.setOnAction(event -> {
            vbox.getChildren().remove(hBox);
            tinhTongTien();
        });
        vbThuoc.getChildren().addAll(new Text("Thuốc Bán: "),comboBoxThuoc);
        vbDVT.getChildren().addAll(new Text("Đơn Vị Tính: "),comboBoxDVT);
        vbSoLuong.getChildren().addAll(new Text("Số Lượng: "),spinnerSoLuong);
        hBox.getChildren().addAll(vbThuoc, vbDVT, vbSoLuong , btn);
        vbox.getChildren().add(hBox);
    }

    private void configureThuocComboBox(ComboBox<ThuocDTO> comboBox) {
        comboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(ThuocDTO thuocDTO) {
                return thuocDTO == null || thuocDTO.getTenThuoc() == null ? "" : thuocDTO.getTenThuoc();
            }

            @Override
            public ThuocDTO fromString(String string) {
                return null;
            }
        });
        comboBox.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(ThuocDTO item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null || item.getTenThuoc() == null ? null : item.getTenThuoc());
            }
        });
    }

    private void configureDonViTinhComboBox(ComboBox<DonViTinhDTO> comboBox) {
        comboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(DonViTinhDTO donViTinhDTO) {
                return donViTinhDTO == null || donViTinhDTO.getTenDVT() == null ? "" : donViTinhDTO.getTenDVT();
            }

            @Override
            public DonViTinhDTO fromString(String string) {
                return null;
            }
        });
        comboBox.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(DonViTinhDTO item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null || item.getTenDVT() == null ? null : item.getTenDVT());
            }
        });
    }

    public void tinhTongTien() {
        double tongTienTraCoKM = 0;
        double tongTienKhiTra = 0;
        for (ChiTietHoaDonDTO ct : selectedItems) {
            LoThuocDTO ctt = ct.getMaLoThuocDTO();
            ThuocDTO t = clientManager.getThuocById(ctt.getMaThuocDTO().getMaThuoc());
            if (t == null) {
                continue;
            }
            if (ct.getTinhTrang().equals("Thuốc Mới Khi Đổi")){
                tongTienKhiTra += ct.getThanhTien() * (1 + t.getThue());
            } else {
                tongTienTraCoKM += ct.getThanhTien() * (1 + t.getThue());
            }
        }

        double tongTienMua = 0;
        for (Node node : vhDSCTHDM.getChildren()) {
            if (node instanceof HBox hBox) {
                VBox vbThuoc = (VBox) hBox.getChildren().get(0);
                VBox vbDVT = (VBox) hBox.getChildren().get(1);
                VBox vbSoLuong = (VBox) hBox.getChildren().get(2);

                ComboBox<ThuocDTO> comboThuoc = (ComboBox<ThuocDTO>) vbThuoc.getChildren().get(1);
                ComboBox<DonViTinhDTO> comboDonVi = (ComboBox<DonViTinhDTO>) vbDVT.getChildren().get(1);
                Spinner<Integer> spinnerSoLuong = (Spinner<Integer>) vbSoLuong.getChildren().get(1);
                if (comboThuoc.getValue() != null && comboDonVi.getValue() != null && spinnerSoLuong.getValue() != null) {
                    ThuocDTO t = comboThuoc.getValue();
                    int soLuong = spinnerSoLuong.getValue();
                    tongTienMua += t.getGiaBan() * soLuong * (1 + t.getThue());
                }
            }
        }

        DecimalFormat df = new DecimalFormat("#,### đ");
        KhuyenMaiDTO km = null;

        if (hoaDonDTO.getMaKM() != null) {
            km = clientManager.getKhuyenMaiById(hoaDonDTO.getMaKM().getMaKM());
        }

        if (km != null && tongTienTraCoKM > 0) {
            tongTienTraCoKM = TinhTienKhuyenMai(tongTienTraCoKM, km.getSo());
            txtTongTienTra.setText(df.format(tongTienTraCoKM + tongTienKhiTra) + " (KM chỉ áp dụng cho thuốc mua)");
        } else {
            txtTongTienTra.setText(df.format(tongTienTraCoKM + tongTienKhiTra));
        }

        txtTongTienMua.setText(df.format(tongTienMua));

        double tienDoi = tongTienMua - (tongTienTraCoKM + tongTienKhiTra);
        if (tienDoi >= 0) {
            txtTongTienDoi.setText("Tổng kết: " + df.format(tienDoi));
            txtThongBaoDoi.setText("Khách hàng cần trả thêm");
        } else {
            txtTongTienDoi.setText("Tổng kết: " + df.format(-tienDoi));
            txtThongBaoDoi.setText("Tiền thừa trả khách");
        }
    }
    public double TinhTienKhuyenMai(double tongTien, double giaSo){
        double giam = 0;
        if (giaSo < 100) {
            giam = tongTien * giaSo / 100.0;
        } else if (giaSo >= 1000) {
            giam = giaSo * (soLuongThuocDoi / soLuongThuoc);
        }
        if (giam > tongTien) giam = tongTien;
        tongTien -= giam;
        return tongTien;
    }

    public boolean checkThongTinThuoc() {
        for (Node node : vhDSCTHDM.getChildren()) {
            if (node instanceof HBox hBox) {
                VBox vbThuoc = (VBox) hBox.getChildren().get(0);
                VBox vbDVT = (VBox) hBox.getChildren().get(1);
                VBox vbSoLuong = (VBox) hBox.getChildren().get(2);

                ComboBox<ThuocDTO> comboThuoc = (ComboBox<ThuocDTO>) vbThuoc.getChildren().get(1);
                ComboBox<DonViTinhDTO> comboDonVi = (ComboBox<DonViTinhDTO>) vbDVT.getChildren().get(1);
                Spinner<Integer> spinnerSoLuong = (Spinner<Integer>) vbSoLuong.getChildren().get(1);
                if (comboThuoc.getValue() == null || comboDonVi.getValue() == null || spinnerSoLuong.getValue() == null || spinnerSoLuong.getValue() <= 0) {
                    return false;
                }
            }
        }
        return true;
    }
}

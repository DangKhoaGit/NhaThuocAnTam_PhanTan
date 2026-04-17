//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.antam.app.controller.phieunhap;

import com.antam.app.connect.ConnectDB;
import com.antam.app.service.impl.*;
import com.antam.app.service.impl.Thuoc_Service;
import com.antam.app.dto.*;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.StringConverter;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Locale;
import javafx.geometry.Insets;
import javafx.scene.layout.*;
import javafx.scene.text.Font;


public class ThemPhieuNhapFormController extends DialogPane{

    private TextField tfMaPhieuNhap, tfNhaCungCap, tfDiaChi, tfLyDo;
    private Button btnThemThuoc;
    private VBox vbDanhSachThuocNhap;
    private Text txtTongTien;

    private Thuoc_Service thuoc_DAO = new Thuoc_Service();
    private DonViTinh_Service donViTinh_DAO = new DonViTinh_Service();
    private PhieuNhap_Service phieuNhap_DAO = new PhieuNhap_Service();
    private ChiTietPhieuNhap_Service chiTietPhieuNhap_DAO = new ChiTietPhieuNhap_Service();
    private LoThuoc_Service chiTietThuoc_DAO = new LoThuoc_Service();

    private ArrayList<ThuocDTO> dsThuoc;
    private ArrayList<DonViTinhDTO> dsDonViTinh;

    public ThemPhieuNhapFormController() {
        this.setPrefSize(800, 600);
        FlowPane header = new FlowPane();
        header.setAlignment(Pos.CENTER);
        header.setStyle("-fx-background-color: #1e3a8a;");

        Text title = new Text("Nhập kho");
        title.setFill(javafx.scene.paint.Color.WHITE);
        title.setFont(Font.font("System Bold", 15));
        FlowPane.setMargin(title, new Insets(10, 0, 10, 0));

        header.getChildren().add(title);
        this.setHeader(header);

        // =========================
        // 2. CONTENT ROOT
        // =========================
        AnchorPane root = new AnchorPane();
        this.setContent(root);

        VBox container = new VBox(10);
        AnchorPane.setLeftAnchor(container, 0.0);
        AnchorPane.setRightAnchor(container, 0.0);

        root.getChildren().add(container);

        // =========================
        // 3. SCROLLPANE
        // =========================
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(400);
        scrollPane.setPadding(new Insets(5));
        container.getChildren().add(scrollPane);

        VBox scrollContent = new VBox(10);
        scrollPane.setContent(scrollContent);

        // =========================
        // 4. GRIDPANE NHẬP THÔNG TIN
        // =========================
        GridPane grid = new GridPane();
        grid.setHgap(5);

        ColumnConstraints c1 = new ColumnConstraints();
        c1.setHgrow(Priority.SOMETIMES);
        ColumnConstraints c2 = new ColumnConstraints();
        c2.setHgrow(Priority.SOMETIMES);

        grid.getColumnConstraints().addAll(c1, c2);

        // ==== ROW 0 ====
        Text lblMa = new Text("Mã phiếu nhập:");
        lblMa.setFill(javafx.scene.paint.Color.web("#374151"));

        Text lblNCC = new Text("Nhà cung cấp:");
        lblNCC.setFill(javafx.scene.paint.Color.web("#374151"));
        GridPane.setColumnIndex(lblNCC, 1);

        grid.add(lblMa, 0, 0);
        grid.add(lblNCC, 1, 0);

        // ==== ROW 1 ====
        tfMaPhieuNhap = new TextField();
        tfMaPhieuNhap.setPrefHeight(40);
        tfMaPhieuNhap.setPromptText("Nhập mã phiếu nhập");
        tfMaPhieuNhap.getStyleClass().add("text-field");

        tfNhaCungCap = new TextField();
        tfNhaCungCap.setPrefHeight(40);
        tfNhaCungCap.setPromptText("Nhập nhà cung cấp");
        tfNhaCungCap.getStyleClass().add("text-field");

        GridPane.setRowIndex(tfMaPhieuNhap, 1);
        GridPane.setColumnIndex(tfNhaCungCap, 1);
        GridPane.setRowIndex(tfNhaCungCap, 1);

        grid.getChildren().addAll(tfMaPhieuNhap, tfNhaCungCap);

        // ==== ROW 2 ====
        Text lblDiaChi = new Text("Địa chỉ:");
        lblDiaChi.setFill(javafx.scene.paint.Color.web("#374151"));
        GridPane.setRowIndex(lblDiaChi, 2);

        Text lblLyDo = new Text("Lý do nhập");
        lblLyDo.setFill(javafx.scene.paint.Color.web("#374151"));
        GridPane.setRowIndex(lblLyDo, 2);
        GridPane.setColumnIndex(lblLyDo, 1);

        grid.getChildren().addAll(lblDiaChi, lblLyDo);

        // ==== ROW 3 ====
        tfDiaChi = new TextField();
        tfDiaChi.setPrefHeight(40);
        tfDiaChi.setPromptText("Nhập địa chỉ");
        tfDiaChi.getStyleClass().add("text-field");
        GridPane.setRowIndex(tfDiaChi, 3);

        tfLyDo = new TextField();
        tfLyDo.setPrefHeight(40);
        tfLyDo.setPromptText("Nhập lý do");
        tfLyDo.getStyleClass().add("text-field");
        GridPane.setColumnIndex(tfLyDo, 1);
        GridPane.setRowIndex(tfLyDo, 3);

        grid.getChildren().addAll(tfDiaChi, tfLyDo);

        // ==== ROW 4 ====
        Text lblThuocNhap = new Text("Thuốc nhập:");
        lblThuocNhap.setFill(javafx.scene.paint.Color.web("#374151"));
        GridPane.setRowIndex(lblThuocNhap, 4);

        grid.getChildren().add(lblThuocNhap);

        scrollContent.getChildren().add(grid);

        // =========================
        // 5. Danh sách thuốc nhập
        // =========================
        vbDanhSachThuocNhap = new VBox(10);
        vbDanhSachThuocNhap.setMaxWidth(Double.MAX_VALUE);
        vbDanhSachThuocNhap.setStyle("-fx-border-color: #e5e7eb; -fx-border-radius: 6px; -fx-border-width: 2px;");
        vbDanhSachThuocNhap.setPadding(new Insets(10));

        scrollContent.getChildren().add(vbDanhSachThuocNhap);

        // =========================
        // 6. Button Thêm Thuốc
        // =========================
        btnThemThuoc = new Button("Thêm thuốc");
        btnThemThuoc.getStyleClass().add("btn-gray");

        scrollContent.getChildren().add(btnThemThuoc);

        // =========================
        // 7. TỔNG TIỀN
        // =========================
        VBox boxTongTien = new VBox();
        boxTongTien.setAlignment(Pos.CENTER);
        boxTongTien.setPrefWidth(100);
        boxTongTien.setStyle("-fx-background-color: #f8fafc; -fx-border-color: #2563eb; -fx-border-radius: 6px; -fx-border-width: 2px;");
        boxTongTien.setPadding(new Insets(10));

        txtTongTien = new Text("Tổng tiền: 0 ₫");
        txtTongTien.setFill(javafx.scene.paint.Color.web("#1e3a8a"));
        txtTongTien.setFont(Font.font("System Bold", 22));

        boxTongTien.getChildren().add(txtTongTien);
        container.getChildren().add(boxTongTien);

        this.getStylesheets().add(getClass().getResource("/com/antam/app/styles/dashboard_style.css").toExternalForm());
        /** Su kiện **/
        //Kết nối
        try {
            Connection con = ConnectDB.getInstance().connect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        dsThuoc = thuoc_DAO.getAllThuoc();
        dsDonViTinh = donViTinh_DAO.getAllDonViTinh();

        //Tạo mã phiếu nhập tự động
        tfMaPhieuNhap.setText(phieuNhap_DAO.taoMaPhieuNhapTuDong());
        tfMaPhieuNhap.setEditable(false);

        ButtonType cancelButton = new ButtonType("Huỷ", ButtonData.CANCEL_CLOSE);
        ButtonType applyButton = new ButtonType("Lưu", ButtonData.APPLY);
        this.getButtonTypes().add(cancelButton);
        this.getButtonTypes().add(applyButton);

        Button btnAppy = (Button) this.lookupButton(applyButton);
        btnAppy.addEventFilter(ActionEvent.ACTION, event -> {
            if (!checkTruongDuLieu() || !checkChiTietPhieuNhap()){
                event.consume();
            }else{
                boolean kiemTraThanhCong = true;
                PhieuNhapDTO pn = new PhieuNhapDTO(tfMaPhieuNhap.getText(), tfNhaCungCap.getText(), LocalDate.now(), tfDiaChi.getText(), tfLyDo.getText(), PhienNguoiDungDTO.getMaNV(), tinhTongTien(), false);
                if (phieuNhap_DAO.tonTaiMaPhieuNhap(tfMaPhieuNhap.getText())) {
                    showCanhBao("Trùng mã phiếu nhập", "Mã phiếu nhập này đã tồn tại. Vui lòng nhập mã khác!");
                    event.consume();
                    kiemTraThanhCong = false;
                }else{
                    phieuNhap_DAO.themPhieuNhap(pn);
                    kiemTraThanhCong = true;
                }
                for (Node node : vbDanhSachThuocNhap.getChildren()) {
                    if (node instanceof HBox hBox) {

                        // Lấy từng VBox con trong HBox
                        VBox vboxThuoc = (VBox) hBox.getChildren().get(0);
                        VBox vboxDonVi = (VBox) hBox.getChildren().get(1);
                        VBox vboxNgaySX = (VBox) hBox.getChildren().get(2);
                        VBox vboxHanSD = (VBox) hBox.getChildren().get(3);
                        VBox vboxSoLuong = (VBox) hBox.getChildren().get(4);
                        VBox vboxGiaNhap = (VBox) hBox.getChildren().get(5);

                        // Lấy control trong từng VBox
                        ComboBox<ThuocDTO> cbDanhSachThuocNhap = (ComboBox<ThuocDTO>) vboxThuoc.getChildren().get(1);
                        ComboBox<DonViTinhDTO> cbDonViTinh = (ComboBox<DonViTinhDTO>) vboxDonVi.getChildren().get(1);
                        DatePicker dpNgaySanXuat = (DatePicker) vboxNgaySX.getChildren().get(1);
                        DatePicker dpHanSuDung = (DatePicker) vboxHanSD.getChildren().get(1);
                        Spinner<Integer> spSoLuong = (Spinner<Integer>) vboxSoLuong.getChildren().get(1);
                        Spinner<Double> spGiaNhap = (Spinner<Double>) vboxGiaNhap.getChildren().get(1);

                        // Nếu tất cả các trường đều hợp lệ
                        ChiTietPhieuNhapDTO ctpt = new ChiTietPhieuNhapDTO(
//                                new PhieuNhap(tfMaPhieuNhap.getText()), cbDanhSachThuocNhap.getValue(), cbDonViTinh.getValue(), spSoLuong.getValue(), spGiaNhap.getValue()
                        );
                        LoThuocDTO ctt = new LoThuocDTO(
                                -1, pn, cbDanhSachThuocNhap.getValue(), spSoLuong.getValue(), dpHanSuDung.getValue(), dpNgaySanXuat.getValue()
                        );
                        if(kiemTraThanhCong){
                            chiTietPhieuNhap_DAO.themChiTietPhieuNhap(ctpt);
                            chiTietThuoc_DAO.themChiTietThuoc(ctt);
                        }
                    }
                }
                if(kiemTraThanhCong){
                    showCanhBao("Thành công", "Lưu phiếu nhập thành công!");
                }
            }
        });

        btnThemThuoc.setOnAction(e -> loadVBoxDanhachThuocNhap(vbDanhSachThuocNhap));
    }

    public void loadVBoxDanhachThuocNhap(VBox vbDanhSachThuocNhap){
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.setSpacing(20);
        hBox.setStyle(
                "-fx-background-color: #f8fafc;" +
                        "-fx-padding: 10;"
        );

        //Danh sách thuốc
        ComboBox<ThuocDTO> cbDanhSachThuocNhap = new ComboBox<>();
        cbDanhSachThuocNhap.setPrefWidth(150);
        cbDanhSachThuocNhap.setPromptText("Chọn thuốc");
        for (ThuocDTO thuocDTO : dsThuoc){
            cbDanhSachThuocNhap.getItems().add(thuocDTO);
        }
        VBox vbChonThuoc = new VBox();
        vbChonThuoc.getChildren().addAll(new Text("Thuốc nhập:"), cbDanhSachThuocNhap);

        //Đơn vị tính
        ComboBox<DonViTinhDTO> cbDonViTinh = new ComboBox<>();
        cbDonViTinh.setPrefWidth(150);
        cbDonViTinh.setPromptText("Đơn vị tính");
        cbDonViTinh.setDisable(true);

        VBox vbDonViTinh = new VBox();
        vbDonViTinh.getChildren().addAll(new Text("Đơn vị tính:"), cbDonViTinh);

        //Sự kiện chọn thuốc để load đơn vị tính tương ứng
        cbDanhSachThuocNhap.setOnAction(e -> {
            cbDonViTinh.getItems().clear();
            cbDonViTinh.getItems().add(donViTinh_DAO.getDVTTheoMaDVT(cbDanhSachThuocNhap.getValue().getMaDVTCoSo().getMaDVT()));
            cbDonViTinh.getSelectionModel().selectFirst();
        });

        //Ngày sản xuất
        DatePicker dpNgaySanXuat = new DatePicker();
        dpNgaySanXuat.setPrefWidth(150);
        dpNgaySanXuat.setPromptText("Chọn ngày sản xuất");
        VBox vbNgaySanXuat = new VBox();
        vbNgaySanXuat.getChildren().addAll(new Text("Ngày sản xuất:"), dpNgaySanXuat);

        //Hạn sử dụng
        DatePicker dpHanSuDung = new DatePicker();
        dpHanSuDung.setPrefWidth(150);
        dpHanSuDung.setPromptText("Chọn hạn sử dụng");
        VBox vbHanSuDung = new VBox();
        vbHanSuDung.getChildren().addAll(new Text("Hạn sử dụng:"), dpHanSuDung);

        //Số lượng
        Spinner<Integer> spSoLuong = new Spinner<>();
        /* Số lượng ít nhất là 1 và nhiều nhất là 50.000 */
        spSoLuong.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 50000, 1, 1));
        spSoLuong.setPrefWidth(100);
        spSoLuong.setEditable(Boolean.TRUE);
        spSoLuong.setPromptText("Số lượng");
        spSoLuong.valueProperty().addListener(e ->{
            txtTongTien.setText("Tổng tiền: " + NumberFormat.getInstance(new Locale("vi", "VN")).format(tinhTongTien()) + " VNĐ");
        });
        VBox vbSoLuong = new VBox();
        vbSoLuong.getChildren().addAll(new Text("Số lượng:"), spSoLuong);

        //Giá nhập
        Spinner<Double> spGiaNhap = new Spinner<>();
        /* Định dạng tiền Việt Nam (Ít nhất 1.000đ nhiều nhất 999.999.999đ, hiển thị trước 10.000đ và bước nhảy 5.000đ) */
        SpinnerValueFactory.DoubleSpinnerValueFactory valueFactory =
                new SpinnerValueFactory.DoubleSpinnerValueFactory(1_000, 999_999_999, 10_000, 5_000);
        NumberFormat currencyFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
        valueFactory.setConverter(new StringConverter<Double>() {
            @Override
            public String toString(Double value) {
                if (value == null) return "";
                return currencyFormat.format(value);
            }

            @Override
            public Double fromString(String text) {
                try {
                    Number parsed = currencyFormat.parse(text);
                    return parsed.doubleValue();
                } catch (Exception e) {
                    return 0.0;
                }
            }
        });
        spGiaNhap.setValueFactory(valueFactory);
        spGiaNhap.setEditable(true);
        spGiaNhap.setPrefWidth(120);
        spGiaNhap.setPromptText("Nhập giá nhập");
        spGiaNhap.valueProperty().addListener(e ->{
            txtTongTien.setText("Tổng tiền: " + NumberFormat.getInstance(new Locale("vi", "VN")).format(tinhTongTien()) + " VNĐ");
        });
        VBox vbGiaNhap = new VBox();
        vbGiaNhap.getChildren().addAll(new Text("Giá nhập (VNĐ):"), spGiaNhap);

        //Nút xoá
        Button btnXoa = new Button("X");
        btnXoa.setStyle(
                "-fx-padding: 0 5 0 5;" +
                        "-fx-background-color: #ef4444;" +
                        "-fx-background-radius: 50%;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 12px;" +
                        "-fx-font-weight: BOLD;"
        );

        btnXoa.setOnAction(e ->
                {
                    vbDanhSachThuocNhap.getChildren().remove(hBox);
                    txtTongTien.setText("Tổng tiền: " + NumberFormat.getInstance(new Locale("vi", "VN")).format(tinhTongTien()) + " VNĐ");
                }
        );

        hBox.getChildren().addAll(vbChonThuoc, vbDonViTinh, vbNgaySanXuat, vbHanSuDung, vbSoLuong, vbGiaNhap, btnXoa);
        vbDanhSachThuocNhap.getChildren().add(hBox);
        txtTongTien.setText("Tổng tiền: " + NumberFormat.getInstance(new Locale("vi", "VN")).format(tinhTongTien()) + " VNĐ");
    }

    /**
     * Kiểm tra các trường dữ liệu
     * @return
     */
    public boolean checkTruongDuLieu(){
        if(!tfMaPhieuNhap.getText().isEmpty()){
            if (!tfMaPhieuNhap.getText().matches("^PN\\d{6}")) {
                showCanhBao("Lỗi định dạng", "Mã phiếu nhập bắt đầu là PN và theo sau là 6 ký tự số");
                tfMaPhieuNhap.requestFocus();
                return false;
            }
        }else {
            showCanhBao("Không được rỗng", "Mã phiếu nhập không được rỗng");
            return false;
        }

        if (tfNhaCungCap.getText().isEmpty()){
            showCanhBao("Không được rỗng", "Nhà cung cấp không được rỗng");
            tfNhaCungCap.requestFocus();
            return false;
        }

        if (tfDiaChi.getText().isEmpty()){
            showCanhBao("Không được rỗng", "Địa chỉ không được rỗng");
            tfDiaChi.requestFocus();
            return false;
        }

        if (tfLyDo.getText().isEmpty()){
            showCanhBao("Không được rỗng", "Lý do nhập không được rỗng");
            tfLyDo.requestFocus();
            return false;
        }

        if (vbDanhSachThuocNhap.getChildren().isEmpty()){
            showCanhBao("Chưa thêm thuốc nhập", "Cần thêm thuốc nhập vào phiếu");
            return false;
        }
        return true;
    }

    /**
     * Hiển thị thông báo cảnh báo
     * @param tieuDe
     * @param vanBan
     */
    public void showCanhBao(String tieuDe, String vanBan){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(tieuDe);
        alert.setContentText(vanBan);
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    public boolean checkChiTietPhieuNhap() {
        for (Node node : vbDanhSachThuocNhap.getChildren()) {
            if (node instanceof HBox hBox) {

                // Lấy từng VBox con trong HBox
                VBox vboxThuoc = (VBox) hBox.getChildren().get(0);
                VBox vboxDonVi = (VBox) hBox.getChildren().get(1);
                VBox vboxNgaySX = (VBox) hBox.getChildren().get(2);
                VBox vboxHanSD = (VBox) hBox.getChildren().get(3);
                VBox vboxSoLuong = (VBox) hBox.getChildren().get(4);
                VBox vboxGiaNhap = (VBox) hBox.getChildren().get(5);

                // Lấy control trong từng VBox
                ComboBox<ThuocDTO> cbDanhSachThuocNhap = (ComboBox<ThuocDTO>) vboxThuoc.getChildren().get(1);
                ComboBox<DonViTinhDTO> cbDonViTinh = (ComboBox<DonViTinhDTO>) vboxDonVi.getChildren().get(1);
                DatePicker dpNgaySanXuat = (DatePicker) vboxNgaySX.getChildren().get(1);
                DatePicker dpHanSuDung = (DatePicker) vboxHanSD.getChildren().get(1);
                Spinner<Integer> spSoLuong = (Spinner<Integer>) vboxSoLuong.getChildren().get(1);
                Spinner<Double> spGiaNhap = (Spinner<Double>) vboxGiaNhap.getChildren().get(1);

                // Kiểm tra dữ liệu
                if (cbDanhSachThuocNhap.getSelectionModel().getSelectedItem() != null && cbDonViTinh.getSelectionModel().getSelectedItem() != null && dpNgaySanXuat.getValue() != null && dpHanSuDung.getValue() != null && spGiaNhap.getValue() != null && spSoLuong.getValue() != null) {
                    if (LocalDate.now().isBefore(dpNgaySanXuat.getValue())) {
                        showCanhBao("Ngày sản xuất không hợp lệ", "Ngày sản xuất không được sau ngày hiện tại");
                        return false;
                    } else if (dpNgaySanXuat.getValue().isAfter(dpHanSuDung.getValue()) || dpNgaySanXuat.getValue().equals(dpHanSuDung.getValue())) {
                        showCanhBao("Ngày sản xuất không hợp lệ", "Ngày sản xuất không được sau hạn sử dụng");
                        return false;
                    }

                    if (spSoLuong.getValue().equals(0)) {
                        showCanhBao("Số lượng không hợp lệ", "Số lượng phải lớn hơn 0");
                        return false;
                    }
                } else {
                    showCanhBao("Thông tin thuốc không đầy đủ", "Hãy điền đầy đủ vào tất cả các thông tin của thuốc");
                    return false;
                }
            }
        }
        return true;
    }

    public double tinhTongTien(){
        double tongTien = 0.0;
        for (Node node : vbDanhSachThuocNhap.getChildren()) {
            if (node instanceof HBox hBox) {
                VBox vboxGiaNhap = (VBox) hBox.getChildren().get(5);
                VBox vboxSoLuong = (VBox) hBox.getChildren().get(4);

                Spinner<Integer> spSoLuong = (Spinner<Integer>) vboxSoLuong.getChildren().get(1);
                Spinner<Double> spGiaNhap = (Spinner<Double>) vboxGiaNhap.getChildren().get(1);
                double thanhTien = spSoLuong.getValue() * spGiaNhap.getValue();
                tongTien += thanhTien;
            }
        }
        return tongTien;
    }
}
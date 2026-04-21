//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.antam.app.controller.phieunhap;

import com.antam.app.connect.ConnectDB;
import com.antam.app.service.I_NhanVien_Service;
import com.antam.app.service.impl.NhanVien_Service;
import com.antam.app.service.impl.PhieuNhap_Service;
import com.antam.app.dto.NhanVienDTO;
import com.antam.app.dto.PhieuNhapDTO;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcons;
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

import java.sql.Connection;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ThemPhieuNhapController extends ScrollPane{
   
    private Button btnThem, btnXoaRong;
    private TableView<PhieuNhapDTO> tbPhieuNhap;
    private ComboBox<NhanVienDTO> cbNhanVienNhap;
    private DatePicker dpTuNgay, dpDenNgay;
    private ComboBox<String> cbKhoangGia;

    private TextField tfTimPhieuNhap;

    private PhieuNhap_Service phieuNhap_DAO = new PhieuNhap_Service();
    private NhanVien_Service nhanVien_DAO = new NhanVien_Service();

    /* Lấy dữ liệu từ DAO */
    private ArrayList<PhieuNhapDTO> dsPhieuNhap = new ArrayList<>();
    private ObservableList<PhieuNhapDTO> data = FXCollections.observableArrayList();

    private PhieuNhapDTO phieuNhapDTODuocChon;

    public ThemPhieuNhapController() {
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

        HBox titleBox = new HBox();
        titleBox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

        Text title = new Text("Thêm phiếu nhập");
        title.setFill(Color.web("#1e3a8a"));
        title.setFont(Font.font("System Bold", 30));

        Pane spacer = new Pane();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        FontAwesomeIcon iconAdd = new FontAwesomeIcon();
        iconAdd.setIcon(FontAwesomeIcons.PLUS);
        iconAdd.setFill(Color.WHITE);
        btnThem = new Button("Thêm phiếu nhập");
        btnThem.getStyleClass().add("btn-them");
        btnThem.setGraphic(iconAdd);

        titleBox.getChildren().addAll(title, spacer, btnThem);

        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabPane.setPrefWidth(200);


        Tab tabPhieuNhap = new Tab("Phiếu nhập");

        VBox tabContent = new VBox(10);
        tabContent.setPadding(new Insets(10));

        FlowPane filterPane = new FlowPane();
        filterPane.setHgap(5);
        filterPane.setVgap(5);
        filterPane.getStyleClass().add("box-pane");

        filterPane.setPadding(new Insets(10));

        DropShadow ds = new DropShadow();
        ds.setBlurType(javafx.scene.effect.BlurType.GAUSSIAN);
        ds.setOffsetX(3);
        ds.setOffsetY(2);
        ds.setRadius(19);
        ds.setColor(Color.rgb(211, 211, 211));

        filterPane.setEffect(ds);

        cbNhanVienNhap = new ComboBox<>();
        cbNhanVienNhap.setPrefSize(200, 40);
        cbNhanVienNhap.setPromptText("Chọn nhân viên nhập");
        cbNhanVienNhap.getStyleClass().add("combo-box");

        VBox v1 = createLabeledBox("Nhân viên nhập:", cbNhanVienNhap);

        dpTuNgay = new DatePicker();
        dpTuNgay.setPrefSize(200, 40);
        dpTuNgay.getStyleClass().add("combo-box");

        VBox v2 = createLabeledBox("Từ ngày:", dpTuNgay);

        dpDenNgay = new DatePicker();
        dpDenNgay.setPrefSize(200, 40);
        dpDenNgay.getStyleClass().add("combo-box");

        VBox v3 = createLabeledBox("Đến ngày:", dpDenNgay);

        cbKhoangGia = new ComboBox<>();
        cbKhoangGia.setPrefSize(200, 40);
        cbKhoangGia.setPromptText("Chọn khoảng giá");
        cbKhoangGia.getStyleClass().add("combo-box");

        VBox v4 = createLabeledBox("Khoảng giá:", cbKhoangGia);

        btnXoaRong = new Button("Xoá rỗng");
        btnXoaRong.setPrefSize(93, 40);
        btnXoaRong.getStyleClass().add("btn-xoarong");

        FontAwesomeIcon refreshIcon = new FontAwesomeIcon();
        refreshIcon.setIcon(FontAwesomeIcons.REFRESH);
        refreshIcon.setFill(Color.WHITE);
        btnXoaRong.setGraphic(refreshIcon);

        VBox v5 = new VBox(5);
        v5.getChildren().addAll(new Text(""), btnXoaRong);

        filterPane.getChildren().addAll(v1, v2, v3, v4, v5);

        HBox searchBox = new HBox(10);

        tfTimPhieuNhap = new TextField();
        tfTimPhieuNhap.setPromptText("Tìm kiếm theo mã phiếu hoặc nhà cung cấp");
        tfTimPhieuNhap.setPrefSize(300, 100);
        tfTimPhieuNhap.setStyle("-fx-background-color: white; -fx-border-color: #e5e7eb; -fx-border-radius: 8px;");
        tfTimPhieuNhap.setPadding(new Insets(10));

        Button btnSearch = new Button();
        btnSearch.setPrefSize(50, 40);
        btnSearch.setStyle("-fx-background-color: #2563eb; -fx-background-radius: 5px;");

        FontAwesomeIcon searchIcon = new FontAwesomeIcon();
        searchIcon.setGlyphName("SEARCH");
        searchIcon.setFill(Color.WHITE);

        btnSearch.setGraphic(searchIcon);

        searchBox.getChildren().addAll(tfTimPhieuNhap, btnSearch);

        tbPhieuNhap = new TableView();
        tbPhieuNhap.setPrefHeight(800);

        tbPhieuNhap.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        Button btnHuongDan = new Button("Nhấn 2 lần chuột trái vào bảng để xem chi tiết");
        btnHuongDan.setMaxWidth(Double.MAX_VALUE);
        btnHuongDan.getStyleClass().add("pane-huongdan");

        FontAwesomeIcon infoIcon = new FontAwesomeIcon();
        infoIcon.setGlyphName("INFO");
        infoIcon.setFill(Color.web("#2563eb"));
        btnHuongDan.setGraphic(infoIcon);
        btnHuongDan.setPadding(new Insets(10));

        tabContent.getChildren().addAll(filterPane, searchBox, tbPhieuNhap, btnHuongDan);

        tabPhieuNhap.setContent(tabContent);
        tabPane.getTabs().add(tabPhieuNhap);

        root.getChildren().addAll(titleBox, tabPane);

        this.getStylesheets().add(getClass().getResource("/com/antam/app/styles/dashboard_style.css").toExternalForm());
        this.setContent(root);
        /** End Giao diện **/
        try {
            Connection con = ConnectDB.getInstance().connect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        this.btnThem.setOnAction((e) -> {
            ThemPhieuNhapFormController themDialog = new ThemPhieuNhapFormController();
            Dialog<Void> dialog = new Dialog<>();
            dialog.setDialogPane(themDialog);
            dialog.setTitle("Thêm phiếu nhập");
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.showAndWait();

            tbPhieuNhap.refresh();
            ObservableList<PhieuNhapDTO> phieuNhapList = FXCollections.observableArrayList(phieuNhap_DAO.getDanhSachPhieuNhap());
            tbPhieuNhap.setItems(phieuNhapList);
        });

        loadDanhSachPhieuNhap();
        loadDanhSachNhanVien();
        loadKhoangGia();

        dsPhieuNhap = phieuNhap_DAO.getDanhSachPhieuNhap();
        data.setAll(dsPhieuNhap);
        tbPhieuNhap.setItems(data);

        cbNhanVienNhap.setOnAction(e -> filterAndSearch());
        dpTuNgay.setOnAction(e -> filterAndSearch());
        dpDenNgay.setOnAction(e -> filterAndSearch());
        cbKhoangGia.setOnAction(e -> filterAndSearch());
        tfTimPhieuNhap.setOnKeyReleased(e -> filterAndSearch());

        //Tuỳ chỉnh field
        dpTuNgay.setPromptText("Chọn ngày");
        dpDenNgay.setPromptText("Đến ngày");

        //Nút xoá rỗng
        btnXoaRong.setOnAction(e -> {
            cbNhanVienNhap.getSelectionModel().clearSelection();
            dpTuNgay.setValue(null);
            dpDenNgay.setValue(null);
            cbKhoangGia.getSelectionModel().clearSelection();
            tfTimPhieuNhap.clear();
            data.setAll(dsPhieuNhap);
            tbPhieuNhap.setItems(data);
        });

        //Khi click double vào 1 phiếu nhập sẽ hiện chi tiết phiếu nhập
        //Sự kiện khi click table
        tbPhieuNhap.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        tbPhieuNhap.setOnMousePressed(e -> {
            PhieuNhapDTO selected = tbPhieuNhap.getSelectionModel().getSelectedItem();
            if (e.getClickCount() == 2){
                if (selected != null) {
                    phieuNhapDTODuocChon = new PhieuNhapDTO(
                            selected.getMaPhieuNhap(),
                            selected.getNhaCungCap(),
                            selected.getNgayNhap(),
                            selected.getDiaChi(),
                            selected.getLyDo(),
                            selected.getMaNV(),
                            selected.getTongTien(),
                            selected.isDeleteAt()
                    );
                    XemChiTietPhieuNhapFormController xemDialog = new XemChiTietPhieuNhapFormController();
                    xemDialog.showChiTietPhieuNhap(phieuNhapDTODuocChon);
                    Dialog<Void> dialog = new Dialog<>();
                    dialog.setDialogPane(xemDialog);
                    dialog.setTitle("Chi tiết phiếu nhập");
                    dialog.showAndWait();
                }else{
                    showMess("Cảnh báo","Hãy chọn một phiếu nhập để xem chi tiết");
                }
            }
        });
    }

    private void showMess(String canhBao, String s) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(canhBao);
        alert.setHeaderText(null);
        alert.setContentText(s);
        alert.showAndWait();
    }

    private VBox createLabeledBox(String label, Control control) {
        Text t = new Text(label);
        t.setFill(Color.web("#374151"));
        t.setFont(Font.font(13));

        VBox v = new VBox(5);
        v.getChildren().addAll(t, control);
        return v;
    }

    public void loadDanhSachNhanVien(){
        List<NhanVienDTO> dsNhanVienRaw = nhanVien_DAO.getAllNhanVien();

        ObservableList<NhanVienDTO> dsNhanVien = FXCollections.observableArrayList(dsNhanVienRaw);

        cbNhanVienNhap.setItems(dsNhanVien);
        cbNhanVienNhap.setPromptText("Chọn nhân viên");

        cbNhanVienNhap.setCellFactory(lv -> new ListCell<NhanVienDTO>() {
            @Override
            protected void updateItem(NhanVienDTO item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    // Hiển thị "Tất cả" nếu mã là "Tất cả", ngược lại hiện Họ tên
                    setText("Tất cả".equals(item.getMaNV()) ? "Tất cả" : item.getHoTen());
                }
            }
        });

        cbNhanVienNhap.setButtonCell(new ListCell<NhanVienDTO>() {
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
    }

    public void loadKhoangGia(){
        cbKhoangGia.getItems().addAll("0đ-500.000đ","500.000đ-1.000.000đ","1.000.000đ trở lên");
    }


    public void loadDanhSachPhieuNhap(){

        /* Tên cột */
        TableColumn<PhieuNhapDTO, String> colMaPhieuNhap = new TableColumn<>("Mã Phiếu Nhập");
        colMaPhieuNhap.setCellValueFactory(new PropertyValueFactory<>("maPhieuNhap"));

        TableColumn<PhieuNhapDTO, String> colNhaCungCap = new TableColumn<>("Nhà Cung Cấp");
        colNhaCungCap.setCellValueFactory(new PropertyValueFactory<>("nhaCungCap"));

        TableColumn<PhieuNhapDTO, Date> colNgayNhap = new TableColumn<>("Ngày Nhập");
        colNgayNhap.setCellValueFactory(new PropertyValueFactory<>("ngayNhap"));

        TableColumn<PhieuNhapDTO, String> colDiaChi = new TableColumn<>("Địa Chỉ");
        colDiaChi.setCellValueFactory(new PropertyValueFactory<>("diaChi"));

        TableColumn<PhieuNhapDTO, String> colLyDo = new TableColumn<>("Lý Do");
        colLyDo.setCellValueFactory(new PropertyValueFactory<>("lyDo"));

        TableColumn<PhieuNhapDTO, String> colHoTenNhanVien = new TableColumn<>("Nhân Viên");
        colHoTenNhanVien.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMaNV().getHoTen()));

        TableColumn<PhieuNhapDTO, Double> colTongTien = new TableColumn<>("Tổng tiền");
        colTongTien.setCellValueFactory(new PropertyValueFactory<>("tongTien"));
        colTongTien.setCellFactory(column -> new TableCell<PhieuNhapDTO, Double>() {
            private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
            @Override
            protected void updateItem(Double value, boolean empty) {
                super.updateItem(value, empty);
                if (empty || value == null) {
                    setText(null);
                } else {
                    setText(currencyFormat.format(value));
                }
            }
        });

        TableColumn<PhieuNhapDTO, Boolean> colTrangThai = new TableColumn<>("Trạng Thái");
        colTrangThai.setCellValueFactory(new PropertyValueFactory<>("deleteAt"));
        colTrangThai.setCellFactory(column -> new TableCell<PhieuNhapDTO, Boolean>() {
            @Override
            protected void updateItem(Boolean isDeleted, boolean empty) {
                super.updateItem(isDeleted, empty);
                if (empty || isDeleted == null) {
                    setText(null);
                } else {
                    setText(isDeleted ? "Đã huỷ" : "Hoạt động");
                }
            }
        });

        tbPhieuNhap.getColumns().addAll(colMaPhieuNhap, colNhaCungCap, colNgayNhap, colDiaChi, colLyDo, colHoTenNhanVien, colTongTien, colTrangThai);

    }

    public void filterAndSearch(){
        NhanVienDTO selectedNV = cbNhanVienNhap.getValue();
        String maNV = selectedNV != null ? selectedNV.getMaNV() : null;

        LocalDate tuNgay = dpTuNgay.getValue();
        LocalDate denNgay = dpDenNgay.getValue();
        String khoangGia = cbKhoangGia.getValue();
        String maPhieuNhap = tfTimPhieuNhap.getText().trim();

        ArrayList<PhieuNhapDTO> phieuNhap = new ArrayList<>();
        for (PhieuNhapDTO ds : dsPhieuNhap) {
            boolean check = true;

            // lọc theo nhân viên
            if (maNV != null && !maNV.equals(ds.getMaNV().getMaNV())) {
                check = false;
            }

            // lọc theo ngày
            if (tuNgay != null && ds.getNgayNhap().isBefore(tuNgay)) {
                check = false;
            }
            if (denNgay != null && ds.getNgayNhap().isAfter(denNgay)) {
                check = false;
            }

            // lọc theo khoảng giá
            if (khoangGia != null) {
                double tongTien = ds.getTongTien();
                if (khoangGia.equals("0đ-500.000đ") && tongTien > 500000) {
                    check = false;
                } else if (khoangGia.equals("500.000đ-1.000.000đ") && (tongTien < 500000 || tongTien > 1000000)) {
                    check = false;
                } else if (khoangGia.equals("1.000.000đ trở lên") && tongTien < 1000000) {
                    check = false;
                }
            }

            // lọc theo mã phiếu nhập
            if (maPhieuNhap != null && !maPhieuNhap.isEmpty() && !ds.getMaPhieuNhap().contains(maPhieuNhap)) {
                check = false;
            }

            if (check) {
                phieuNhap.add(ds);
            }
        }

        data.setAll(phieuNhap);
        tbPhieuNhap.setItems(data);
    }
}

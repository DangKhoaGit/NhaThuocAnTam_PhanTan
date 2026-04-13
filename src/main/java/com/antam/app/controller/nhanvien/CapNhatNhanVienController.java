package com.antam.app.controller.nhanvien;

import com.antam.app.dao.I_NhanVien_DAO;
import com.antam.app.dao.impl.NhanVien_DAO;
import com.antam.app.entity.NhanVien;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcons;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class CapNhatNhanVienController extends ScrollPane{
    
    private TableView<NhanVien> tbNhanVien;
    private Button btnFindNV,btnXoaTrang,btnXoaSua;
    private TextField txtFindNV;
    private TableColumn<NhanVien, String> colMaNV, colHoTen, colChucVu, colSDT, colDiaChi, colEmail;
    private TableColumn<NhanVien, String> colLuong;
    private ComboBox<String> cbChucVu, cbLuongCB;
    ArrayList<NhanVien> listNV = I_NhanVien_DAO.getDsNhanVienformDBS();

    private ObservableList<NhanVien> TVNhanVien;
    private final ObservableList<NhanVien> filteredList = FXCollections.observableArrayList();
    public  static NhanVien nhanVienSelected;

    public CapNhatNhanVienController(){
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

        // Title HBox
        HBox titleBox = new HBox(5);
        titleBox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        Text title = new Text("Cập nhật nhân viên");
        title.setFont(Font.font("System Bold", 30));
        title.setFill(Color.web("#1e3a8a"));
        Pane spacer = new Pane();
        HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);
        btnXoaSua = new Button("Sửa nhân viên");
        btnXoaSua.getStyleClass().add("btn-them");
        titleBox.getChildren().addAll(title, spacer, btnXoaSua);

        // Filters FlowPane
        FlowPane filterPane = new FlowPane(5, 5);
        filterPane.setPadding(new Insets(10));
        filterPane.setStyle("-fx-background-color: white; -fx-background-radius: 8px; -fx-border-radius: 5px;");
        DropShadow ds = new DropShadow();
        ds.setColor(Color.rgb(211,211,211));
        ds.setOffsetX(3);
        ds.setOffsetY(2);
        ds.setRadius(19.5);
        filterPane.setEffect(ds);

        // Chức vụ
        VBox vbChucVu = new VBox(5);
        Text txtChucVu = new Text("Chức vụ");
        txtChucVu.setFont(Font.font(13));
        txtChucVu.setFill(Color.web("#374151"));
        cbChucVu = new ComboBox<>();
        cbChucVu.setPrefSize(200, 40);
        cbChucVu.setPromptText("Chọn chức vụ");
        vbChucVu.getChildren().addAll(txtChucVu, cbChucVu);

        // Lương cơ bản
        VBox vbLuong = new VBox(5);
        Text txtLuong = new Text("Lương cơ bản");
        txtLuong.setFont(Font.font(13));
        txtLuong.setFill(Color.web("#374151"));
        cbLuongCB = new ComboBox<>();
        cbLuongCB.setPrefSize(200, 40);
        cbLuongCB.setPromptText("Chọn lương cơ bản");
        vbLuong.getChildren().addAll(txtLuong, cbLuongCB);

        // Xóa trắng
        VBox vbXoaTrang = new VBox(5);
        Text txtEmpty = new Text("");
        btnXoaTrang = new Button("Xóa trắng");
        btnXoaTrang.setPrefSize(70, 34);
        btnXoaTrang.getStyleClass().add("btn-xoarong");
        vbXoaTrang.getChildren().addAll(txtEmpty, btnXoaTrang);

        filterPane.getChildren().addAll(vbChucVu, vbLuong, vbXoaTrang);

        // Search box HBox
        HBox searchBox = new HBox(10);
        searchBox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        txtFindNV = new TextField();
        txtFindNV.setPromptText("Tìm kiếm nhân viên...");
        txtFindNV.setPrefSize(300, 40);
        txtFindNV.setStyle("-fx-background-color: white; -fx-border-color: #e5e7eb; -fx-border-radius: 8px;");

        btnFindNV = new Button();
        btnFindNV.setPrefSize(50, 40);
        btnFindNV.setStyle("-fx-background-color: #2563eb; -fx-background-radius: 5px;");
        btnFindNV.setTextFill(Color.WHITE);
        FontAwesomeIcon searchIcon = new FontAwesomeIcon();
        searchIcon.setIcon(FontAwesomeIcons.SEARCH);
        searchIcon.setFill(Color.WHITE);
        btnFindNV.setGraphic(searchIcon);

        searchBox.getChildren().addAll(txtFindNV, btnFindNV);

        // TableView
        tbNhanVien = new TableView<>();
        tbNhanVien.setPrefSize(858, 480);

        colMaNV = new TableColumn<>("Mã nhân viên");
        colHoTen = new TableColumn<>("Họ tên");
        colChucVu = new TableColumn<>("Chức vụ");
        colSDT = new TableColumn<>("Số điện thoại");
        colDiaChi = new TableColumn<>("Địa chỉ");
        colEmail = new TableColumn<>("Email");
        colLuong = new TableColumn<>("Lương cơ bản");

        tbNhanVien.getColumns().addAll(colMaNV, colHoTen, colChucVu, colSDT, colDiaChi, colEmail, colLuong);
        tbNhanVien.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        VBox tableBox = new VBox(5);
        Button guide = new Button("Nhấn 2 lần chuột trái vào bảng để cập nhật nhân viên nhanh");
        guide.getStyleClass().add("pane-huongdan");
        guide.setMaxWidth(Double.MAX_VALUE);
        guide.setPadding(new Insets(10));
        FontAwesomeIcon infoIcon = new FontAwesomeIcon(); infoIcon.setGlyphName("INFO"); infoIcon.setFill(Color.web("#2563eb"));
        guide.setGraphic(infoIcon);

        tableBox.getChildren().addAll(tbNhanVien, guide);

        root.getChildren().addAll(titleBox, filterPane, searchBox, tableBox);

        this.setContent(root);
        /** Sự kiện **/
        this.btnXoaSua.setOnAction((e) -> {
            nhanVienSelected = tbNhanVien.getSelectionModel().getSelectedItem();
            if (nhanVienSelected == null) {
                showMess("Vui lòng chọn nhân viên cần sửa!");
                return;
            }
            CapNhatNhanVienFormController capNhatDialog = new CapNhatNhanVienFormController();
            Dialog<Void> dialog = new javafx.scene.control.Dialog<>();
            dialog.setDialogPane(capNhatDialog);
            dialog.setTitle("Cập nhật nhân viên");
            dialog.showAndWait();
            loadNhanVien();
        });
        tbNhanVien.setOnMouseClicked(e -> {
            if (e.getClickCount()==2){
                nhanVienSelected = tbNhanVien.getSelectionModel().getSelectedItem();
                CapNhatNhanVienFormController capNhatDialog = new CapNhatNhanVienFormController();
                Dialog<Void> dialog = new javafx.scene.control.Dialog<>();
                dialog.setDialogPane(capNhatDialog);
                dialog.setTitle("Cập nhật nhân viên");
                dialog.showAndWait();
                loadNhanVien();
            }
        });
        setupTableNhanVien();
        loadNhanVien();
        loadComboBox();
        setupListener();

        btnFindNV.setOnAction(e -> timNhanVien());
        setupListener();

        tbNhanVien.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        btnXoaTrang.setOnAction(e -> {
            txtFindNV.clear();
            cbChucVu.getSelectionModel().selectFirst();
            cbLuongCB.getSelectionModel().selectFirst();
            tbNhanVien.setItems(TVNhanVien);
        });
    }

    private void showMess(String noidung) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Thông báo");
        alert.setHeaderText(null);
        alert.setContentText(noidung);
        alert.showAndWait();
    }

    private void loadNhanVien() {
        listNV = I_NhanVien_DAO.getDsNhanVienformDBS();
        TVNhanVien = FXCollections.observableArrayList(
                listNV.stream()
                        .filter(nv -> !nv.isDeleteAt())
                        .toList()
        );
        tbNhanVien.setItems(TVNhanVien);
    }


    private void setupTableNhanVien() {
        colMaNV.setCellValueFactory(t -> new SimpleStringProperty(t.getValue().getMaNV()));
        colHoTen.setCellValueFactory(t -> new SimpleStringProperty(t.getValue().getHoTen()));
        colChucVu.setCellValueFactory(t -> new SimpleStringProperty(
                t.getValue().isQuanLy() ? "Nhân viên quản lý" : "Nhân viên"));
        colSDT.setCellValueFactory(t -> new SimpleStringProperty(t.getValue().getSoDienThoai()));
        colDiaChi.setCellValueFactory(t -> new SimpleStringProperty(t.getValue().getDiaChi()));
        colEmail.setCellValueFactory(t -> new SimpleStringProperty(t.getValue().getEmail()));
        colLuong.setCellValueFactory(t -> new SimpleStringProperty(dinhDangTien(t.getValue().getLuongCoBan())));
    }

    private void loadComboBox() {
        cbChucVu.setItems(FXCollections.observableArrayList(
                "Tất cả", "Nhân viên", "Nhân viên quản lý"));
        cbChucVu.getSelectionModel().selectFirst();
        cbLuongCB.setItems(FXCollections.observableArrayList(
                "Tất cả", "Dưới 5 triệu", "Từ 5 triệu đến 10 triệu", "Trên 10 triệu"));
        cbLuongCB.getSelectionModel().selectFirst();
    }

    private void setupListener() {
        // Khi người dùng gõ tìm kiếm
        txtFindNV.textProperty().addListener((ob, oldT, newT) -> filterNhanVien());

        // Khi chọn chức vụ
        cbChucVu.setOnAction(e -> filterNhanVien());

        // Khi chọn mức lương
        cbLuongCB.setOnAction(e -> filterNhanVien());
    }

    /**
     * Lọc nhân viên theo nhiều điều kiện: tìm kiếm, chức vụ và mức lương
     */
    private void filterNhanVien() {
        filteredList.clear();

        String keyword = txtFindNV.getText() == null ? "" : txtFindNV.getText().toLowerCase();
        String chucVu = cbChucVu.getSelectionModel().getSelectedItem();
        String luongCB = cbLuongCB.getSelectionModel().getSelectedItem();

        for (NhanVien nv : TVNhanVien) {
            boolean matchKeyword = keyword.isBlank() ||
                    nv.getMaNV().toLowerCase().contains(keyword) ||
                    nv.getHoTen().toLowerCase().contains(keyword);

            boolean matchChucVu =
                    chucVu.equals("Tất cả") ||
                            (chucVu.equals("Nhân viên") && !nv.isQuanLy()) ||
                            (chucVu.equals("Nhân viên quản lý") && nv.isQuanLy());

            double l = nv.getLuongCoBan();
            boolean matchLuong =
                    luongCB.equals("Tất cả") ||
                            (luongCB.equals("Dưới 5 triệu") && l < 5_000_000) ||
                            (luongCB.equals("Từ 5 triệu đến 10 triệu") && l >= 5_000_000 && l <= 10_000_000) ||
                            (luongCB.equals("Trên 10 triệu") && l > 10_000_000);

            if (matchKeyword && matchChucVu && matchLuong) {
                filteredList.add(nv);
            }
        }

        tbNhanVien.setItems(filteredList.isEmpty() && !keyword.isBlank() ? FXCollections.observableArrayList() : filteredList);
        if (filteredList.isEmpty() && keyword.isBlank() && chucVu.equals("Tất cả") && luongCB.equals("Tất cả")) {
            tbNhanVien.setItems(TVNhanVien);
        }
    }


    private void timNhanVien() {
        String x = txtFindNV.getText().trim().toLowerCase();
        if (x.isEmpty()) return;

        for (NhanVien a : tbNhanVien.getItems()) {
            if (a.getMaNV().toLowerCase().contains(x) ||
                    a.getHoTen().toLowerCase().contains(x)) {
                tbNhanVien.getSelectionModel().select(a);
                tbNhanVien.scrollTo(a);
            }
        }
    }

    private String dinhDangTien(double tien) {
        DecimalFormat df = new DecimalFormat("#,### đ");
        return df.format(tien);
    }
}

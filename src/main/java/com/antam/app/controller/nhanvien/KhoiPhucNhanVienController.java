package com.antam.app.controller.nhanvien;

import com.antam.app.dto.NhanVienDTO;
import com.antam.app.network.ClientManager;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcons;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.text.DecimalFormat;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class KhoiPhucNhanVienController extends ScrollPane{
    private static final Logger LOGGER = Logger.getLogger(ThemNhanVienController.class.getName());

    private final TableView<NhanVienDTO> tbNhanVien;
    private final Button btnFindNV;
    private final Button btnXoaTrang;
    private final Button btnKhoiPhuc;
    private final TextField txtFindNV;
    private final TableColumn<NhanVienDTO, String> colMaNV;
    private final TableColumn<NhanVienDTO, String> colHoTen;
    private final TableColumn<NhanVienDTO, String> colChucVu;
    private final TableColumn<NhanVienDTO, String> colSDT;
    private final TableColumn<NhanVienDTO, String> colDiaChi;
    private final TableColumn<NhanVienDTO, String> colEmail;
    private final TableColumn<NhanVienDTO, String> colLuong;
    private final ComboBox<String> cbChucVu;
    private final ComboBox<String> cbLuongCB;

    private ObservableList<NhanVienDTO> TVNhanVien;
    private final ObservableList<NhanVienDTO> filteredList = FXCollections.observableArrayList();
    public  static NhanVienDTO nhanVienDTOSelected;
    public ClientManager clientManager = ClientManager.getInstance();

    private List<NhanVienDTO> listNV;


    public KhoiPhucNhanVienController(){
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

        HBox titleBox = new HBox(5);
        titleBox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        Text title = new Text("Khôi phục nhân viên");
        title.setFont(Font.font("System Bold", 30));
        title.setFill(Color.web("#1e3a8a"));
        Pane spacer = new Pane();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        FontAwesomeIcon iconRestore = new FontAwesomeIcon();
        iconRestore.setIcon(FontAwesomeIcons.BACKWARD);
        iconRestore.setFill(Color.WHITE);
        btnKhoiPhuc = new Button("Khôi phục");
        btnKhoiPhuc.getStyleClass().add("btn-khoiphuc");
        btnKhoiPhuc.setGraphic(iconRestore);
        titleBox.getChildren().addAll(title, spacer, btnKhoiPhuc);

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
        Button guide = new Button("Nhấn 2 lần chuột trái vào bảng để khôi phục nhanh");
        guide.getStyleClass().add("pane-huongdan");
        guide.setMaxWidth(Double.MAX_VALUE);
        guide.setPadding(new Insets(10));
        FontAwesomeIcon infoIcon = new FontAwesomeIcon(); infoIcon.setGlyphName("INFO"); infoIcon.setFill(Color.web("#2563eb"));
        guide.setGraphic(infoIcon);

        tableBox.getChildren().addAll(tbNhanVien, guide);

        root.getChildren().addAll(titleBox, filterPane, searchBox, tableBox);

        this.setContent(root);
        /** Sự kiện **/

        // setup bảng nhân viên
        loadNhanVienAsync();
        setupTableNhanVien();
        loadComboBox();
        setupListener();

        //sự kiện tìm kiến
        btnFindNV.setOnAction(e -> timNhanVien());

        // sự kiện xóa các điều kiện
        btnXoaTrang.setOnAction(e -> {
            txtFindNV.clear();
            cbChucVu.getSelectionModel().selectFirst();
            cbLuongCB.getSelectionModel().selectFirst();
            tbNhanVien.setItems(TVNhanVien);
        });

        //sự kiện khôi phục
        btnKhoiPhuc.setOnAction(e -> {
            nhanVienDTOSelected = tbNhanVien.getSelectionModel().getSelectedItem();
            if (nhanVienDTOSelected == null) {
                showError("Chưa chọn nhân viên", "Vui lòng chọn nhân viên cần khôi phục.");
            }else{
                boolean success = clientManager.khoiPhucNhanVien(nhanVienDTOSelected.getMaNV());
                if (success) {
                    showSuccess("Khôi phục thành công", "Nhân viên đã được khôi phục thành công.");
                    loadNhanVienAsync();
                    filterNhanVien();
                } else {
                    showError("Khôi phục thất bại", "Đã có lỗi xảy ra trong quá trình khôi phục nhân viên.");
                }
            }
        } );

        // Sự kiện double click để khôi phục nhanh
        tbNhanVien.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                nhanVienDTOSelected = tbNhanVien.getSelectionModel().getSelectedItem();

                if (nhanVienDTOSelected == null) return;

                if (canhBao("Xác nhận khôi phục", "Bạn có chắc muốn khôi phục nhân viên " + nhanVienDTOSelected.getHoTen() + " không?")) {
                    if (nhanVienDTOSelected != null) {
                        boolean success = clientManager.khoiPhucNhanVien(nhanVienDTOSelected.getMaNV());
                        if (success) {
                            showSuccess("Khôi phục thành công", "Nhân viên đã được khôi phục thành công.");
                            loadNhanVienAsync();
                            filterNhanVien();
                        } else {
                            showError("Khôi phục thất bại", "Đã có lỗi xảy ra trong quá trình khôi phục nhân viên.");
                        }
                    }
                }
            }
        });
    }

    /**
     * Hiển thị cảnh báo xác nhận
     * @param tieuDe
     * @param noidung
     * @return
     */
    private boolean canhBao(String tieuDe, String noidung){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(tieuDe);
        alert.setHeaderText(null);
        alert.setContentText(noidung);

        ButtonType buttonTypeYes = new ButtonType("Có", ButtonBar.ButtonData.YES);
        ButtonType buttonTypeNo = new ButtonType("Không", ButtonBar.ButtonData.NO);

        alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

        return alert.showAndWait().orElse(buttonTypeNo) == buttonTypeYes;
    }

    /**
     * Hiển thị hộp thoại thông báo
     * @param tieuDe
     * @param noidung
     */
    private void showSuccess(String tieuDe, String noidung) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(tieuDe);
        alert.setHeaderText(null);
        alert.setContentText(noidung);
        alert.showAndWait();
    }

    /**
     * Hiển thị hộp thoại lỗi
     * @param tieuDe
     * @param noidung
     */
    private void showError(String tieuDe, String noidung) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(tieuDe);
        alert.setHeaderText(null);
        alert.setContentText(noidung);
        alert.showAndWait();
    }

    private void loadNhanVienAsync() {
        Task<List<NhanVienDTO>> task = new Task<List<NhanVienDTO>>() {
            @Override
            protected List<NhanVienDTO> call() throws Exception {
                try {
                    return (List<NhanVienDTO>) clientManager.getNhanVienList();
                } catch (Exception e) {
                    LOGGER.log(Level.SEVERE, "Error loading NhanVien from server", e);
                    throw new Exception("Lỗi kết nối server: " + e.getMessage());
                }
            }
        };

        task.setOnSucceeded(e -> {
            try {
                List<NhanVienDTO> list = task.getValue();
                if (list != null && !list.isEmpty()) {
                    TVNhanVien = FXCollections.observableArrayList(
                            list.stream()
                                    .filter(nv -> !nv.isDeleteAt())
                                    .toList()
                    );
                    tbNhanVien.setItems(TVNhanVien);
                    LOGGER.info("Loaded " + TVNhanVien.size() + " nhân viên records");
                } else {
                    showAlert("Thông báo", "Không có dữ liệu nhân viên");
                    TVNhanVien = FXCollections.observableArrayList();
                    tbNhanVien.setItems(TVNhanVien);
                }
            } catch (Exception ex) {
                LOGGER.log(Level.SEVERE, "Error processing NhanVien data", ex);
                showAlert("Lỗi", "Không thể xử lý dữ liệu: " + ex.getMessage());
            }
        });

        task.setOnFailed(e -> {
            Throwable exception = task.getException();
            LOGGER.log(Level.SEVERE, "NhanVien loading failed", exception);
            showAlert("Lỗi", "Không thể tải dữ liệu: " + exception.getMessage());
        });

        new Thread(task).start();
    }
    /**
     * Hiện thông báo cho người dùng
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void setupTableNhanVien() {
        colMaNV.setCellValueFactory(t -> new SimpleStringProperty(t.getValue().getMaNV()));
        colHoTen.setCellValueFactory(t -> new SimpleStringProperty(t.getValue().getHoTen()));
        colChucVu.setCellValueFactory(t -> new SimpleStringProperty(
                t.getValue().isQuanLi() ? "Nhân viên quản lý" : "Nhân viên"));
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

        for (NhanVienDTO nv : TVNhanVien) {
            boolean matchKeyword = keyword.isBlank() ||
                    nv.getMaNV().toLowerCase().contains(keyword) ||
                    nv.getHoTen().toLowerCase().contains(keyword);

            boolean matchChucVu =
                    chucVu.equals("Tất cả") ||
                            (chucVu.equals("Nhân viên") && !nv.isQuanLi()) ||
                            (chucVu.equals("Nhân viên quản lý") && nv.isQuanLi());

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

        for (NhanVienDTO a : tbNhanVien.getItems()) {
            if (a.getMaNV().toLowerCase().contains(x) ||
                    a.getHoTen().toLowerCase().contains(x)) {
                tbNhanVien.getSelectionModel().select(a);
                tbNhanVien.scrollTo(a);
                break;
            }
        }
    }

    private String dinhDangTien(double tien) {
        DecimalFormat df = new DecimalFormat("#,### đ");
        return df.format(tien);
    }
}

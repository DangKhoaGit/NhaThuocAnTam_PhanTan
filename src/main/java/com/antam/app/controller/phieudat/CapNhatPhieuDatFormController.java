//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.antam.app.controller.phieudat;

import com.antam.app.network.ClientManager;
import com.antam.app.dto.*;
import com.antam.app.helper.XuatHoaDonPDF;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.control.*;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.text.Text;

import java.io.File;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.application.Platform;
import javafx.stage.Window;

import static com.antam.app.controller.phieudat.CapNhatPhieuDatController.selectedPDT;

public class CapNhatPhieuDatFormController extends DialogPane{

    private final Text txtMa;
    private final Text txtNgay;
    private final Text txtSDT;
    private final Text txtStatus;
    private final Text txtTongTien;
    private final Text txtKM;

    private final TableColumn<ChiTietPhieuDatThuocDTO,Integer> colSTT;
    private final TableColumn<ChiTietPhieuDatThuocDTO,String> colTenThuoc;
    private final TableColumn<ChiTietPhieuDatThuocDTO,String> colThanhTien;
    private final TableColumn<ChiTietPhieuDatThuocDTO,String> colDonGia;
    private final TableColumn<ChiTietPhieuDatThuocDTO,Integer> colSoLuong;

    private final TableView<ChiTietPhieuDatThuocDTO> tbThuoc;

    private final PhieuDatThuocDTO select;
    private List<ChiTietPhieuDatThuocDTO> listChiTiet;
    private final ClientManager clientManager;

    public CapNhatPhieuDatFormController() {
        // Initialize final fields
        this.txtMa = new Text();
        this.txtNgay = new Text();
        this.txtSDT = new Text();
        this.txtStatus = new Text();
        this.txtTongTien = new Text();
        this.txtKM = new Text();

        this.colSTT = new TableColumn<>("STT");
        this.colTenThuoc = new TableColumn<>("Tên thuốc");
        this.colSoLuong = new TableColumn<>("Số lượng");
        this.colDonGia = new TableColumn<>("Đơn giá");
        this.colThanhTien = new TableColumn<>("Thành tiền");

        this.tbThuoc = new TableView<>();

        this.select = selectedPDT;
        this.clientManager = ClientManager.getInstance();

        FlowPane header = new FlowPane();
        header.setAlignment(Pos.CENTER);
        header.setStyle("-fx-background-color: #1e3a8a;");

        Text title = new Text("Cập nhật phiếu đặt");
        title.setFill(javafx.scene.paint.Color.WHITE);
        title.setFont(Font.font("System Bold", 15));

        FlowPane.setMargin(title, new Insets(10, 0, 10, 0));
        header.getChildren().add(title);
        this.setHeader(header);

        // ============================
        // CONTENT ROOT
        AnchorPane anchor = new AnchorPane();
        VBox rootVBox = new VBox(10);
        rootVBox.setPadding(new Insets(10));

        // ============================
        // BOX THÔNG TIN ĐẦU
        VBox infoBox = new VBox(5);
        infoBox.setStyle("-fx-background-color: #2563eb; -fx-background-radius: 5px;");
        infoBox.setPadding(new Insets(10, 100, 10, 10));

        txtMa.setFill(javafx.scene.paint.Color.WHITE);
        txtMa.setFont(Font.font("System Bold", 20));

        txtNgay.setFill(javafx.scene.paint.Color.WHITE);
        txtNgay.setFont(Font.font(13));

        txtSDT.setFill(javafx.scene.paint.Color.WHITE);
        txtSDT.setFont(Font.font(13));

        txtStatus.setFill(javafx.scene.paint.Color.WHITE);
        txtStatus.setFont(Font.font(13));

        infoBox.getChildren().addAll(txtMa, txtNgay, txtSDT, txtStatus);

        // LABEL DANH SÁCH THUỐC
        Text labelThuoc = new Text("Danh sách thuốc đặt:");

        // ============================
        // TABLEVIEW
        tbThuoc.setPrefSize(758, 282);


        tbThuoc.getColumns().add(colSTT);
        tbThuoc.getColumns().add(colTenThuoc);
        tbThuoc.getColumns().add(colSoLuong);
        tbThuoc.getColumns().add(colDonGia);
        tbThuoc.getColumns().add(colThanhTien);

        // ============================
        // GRID TỔNG TIỀN
        GridPane grid = new GridPane();
        grid.setHgap(5);
        grid.setPadding(new Insets(10));
        grid.setStyle("-fx-background-color: #f8fafc; -fx-background-radius: 8px;");

        ColumnConstraints col1 = new ColumnConstraints();
        ColumnConstraints col2 = new ColumnConstraints();
        col1.setHgrow(Priority.SOMETIMES);
        col2.setHgrow(Priority.SOMETIMES);

        grid.getColumnConstraints().addAll(col1, col2);

        // Khuyến mãi
        Text kmLabel = new Text("Khuyến mãi:");
        kmLabel.setFill(javafx.scene.paint.Color.web("#374151"));
        kmLabel.setFont(Font.font(13));

        txtKM.setFill(javafx.scene.paint.Color.web("#374151"));
        txtKM.setFont(Font.font(13));
        GridPane.setColumnIndex(txtKM, 1);

        // Tổng cộng
        Text totalLabel = new Text("Tổng cộng:");
        totalLabel.setFill(javafx.scene.paint.Color.web("#374151"));
        totalLabel.setFont(Font.font("System Bold", 18));
        GridPane.setRowIndex(totalLabel, 2);

        txtTongTien.setFill(javafx.scene.paint.Color.web("#374151"));
        txtTongTien.setFont(Font.font("System Bold", 18));
        GridPane.setColumnIndex(txtTongTien, 1);
        GridPane.setRowIndex(txtTongTien, 2);

        grid.getChildren().addAll(kmLabel, txtKM, totalLabel, txtTongTien);

        // ============================
        // GÁN TẤT CẢ VÀO VBOX ROOT
        // ============================
        rootVBox.getChildren().addAll(
                infoBox,
                labelThuoc,
                tbThuoc,
                grid
        );

        anchor.getChildren().add(rootVBox);
        AnchorPane.setLeftAnchor(rootVBox, 0.0);
        AnchorPane.setRightAnchor(rootVBox, 0.0);

        var stylesheetUrl = getClass().getResource("/com/antam/app/styles/dashboard_style.css");
        if (stylesheetUrl != null) {
            this.getStylesheets().add(stylesheetUrl.toExternalForm());
        }
        this.setContent(anchor);

        // Sự kiện

        if (select.getKhuyenMaiDTO()==null || select.getKhuyenMaiDTO().getTenKM().isEmpty() ){
            txtKM.setText("Phiếu đặt thuốc không áp dụng khuyến mãi");
        }else{
            txtKM.setText("Áp dụng khuyến mãi: "+select.getKhuyenMaiDTO().getTenKM());
        }

        Task<List<ChiTietPhieuDatThuocDTO>> loadChiTietTask = new Task<>() {
            @Override
            protected List<ChiTietPhieuDatThuocDTO> call() {
                return clientManager.getChiTietPDT(select.getMaPhieu());
            }
        };
        loadChiTietTask.setOnSucceeded(e -> listChiTiet = loadChiTietTask.getValue());

        Thread thread = new Thread(loadChiTietTask);
        thread.start();

        ButtonType cancelButton = new ButtonType("Huỷ", ButtonData.CANCEL_CLOSE);
        ButtonType applyButton = new ButtonType("Thanh Toán", ButtonData.APPLY);
        this.getButtonTypes().add(cancelButton);
        this.getButtonTypes().add(applyButton);
        loadContent();
        setupTable();
        loadBangChiTiet();
        loadTien();

        Button btnThanhToan = (Button) this.lookupButton(applyButton);
        btnThanhToan.setOnAction(event -> {
            if (select.isThanhToan()) {
                showMess("Cảnh báo","Phiếu đặt thuốc đã được thanh toán");
            }else{
                thanhToanPhieuDat();
            }
        });

        Button btnHuy =  (Button) this.lookupButton(cancelButton);

        btnHuy.setOnAction(event -> {

        });

    }

    private void loadTien() {
        double tongTien = select.getTongTien();
        txtTongTien.setText(dinhDangTien(tongTien));
    }

    private void thanhToanPhieuDat() {
        // Cập nhật trạng thái thanh toán
        select.setThanhToan(true);
            HoaDonDTO hoaDonDTO = new HoaDonDTO(getMaxHashHoaDon(),
                    LocalDate.now()
                    , PhienNguoiDungDTO.getMaNV()
                    , select.getKhachHang()
                    , select.getKhuyenMaiDTO()
                    ,select.getTongTien()
                    , false);
            Task<Boolean> insertHoaDonTask = new Task<>() {
                @Override
                protected Boolean call() {
                    return clientManager.createHoaDon(hoaDonDTO);
                }
            };

            insertHoaDonTask.setOnSucceeded(event -> {
                boolean success = insertHoaDonTask.getValue();
                if (success) {
                    ArrayList<ChiTietHoaDonDTO> list = new ArrayList<>();
                    for (ChiTietPhieuDatThuocDTO item : tbThuoc.getItems()){

                        Task<Boolean> taskThemCTTHD = new Task<>() {
                            @Override
                            protected Boolean call() {
                                return clientManager.createChiTietHoaDon(item);
                            }
                        };

                        taskThemCTTHD.setOnSucceeded(ev -> {
                            boolean ctSuccess = taskThemCTTHD.getValue();
                            if (!ctSuccess) {
                                showMess("Lỗi", "Không thể thêm chi tiết hóa đơn cho thuốc: " + item.getMaThuoc().getMaThuocDTO().getTenThuoc());
                            }
                        });

                        ChiTietHoaDonDTO i = new ChiTietHoaDonDTO();
                        i.setMaHD(hoaDonDTO);
                        i.setMaLoThuocDTO(item.getMaThuoc());
                        i.setThanhTien(item.getThanhTien());
                        i.setMaDVT(item.getDonViTinhDTO());
                        i.setSoLuong(item.getSoLuong());
                        i.setTinhTrang("Bán");
                        list.add(i);

                        Thread thread = new Thread(taskThemCTTHD);
                        thread.start();
                    }
                    //xuất pdf phiếu dặt
                    thongBaoVaXuatHoaDon(hoaDonDTO,list);
                }
            });

            insertHoaDonTask.setOnFailed(ex -> showMess("Lỗi", "Không thể tạo hóa đơn"));

            Thread thread = new Thread(insertHoaDonTask);
            thread.start();
    }

    private String getMaxHashHoaDon(){
        Task<Integer> getMaxHashTask = new Task<>() {
            @Override
            protected Integer call() {
                return clientManager.getMaxHashHoaDon();
            }
        };
        AtomicInteger max = new AtomicInteger();
        getMaxHashTask.setOnSucceeded(event -> max.set(getMaxHashTask.getValue()));

        Thread thread = new Thread(getMaxHashTask);
        thread.start();

        try {
            thread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return String.format("HD%03d", max.get() + 1);
    }

    public void showMess(String tieuDe, String vanBan) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            // Try to set an owner so dialogs don't appear behind other windows.
            Window owner = (this.getScene() != null) ? this.getScene().getWindow() : null;
            if (owner == null) {
                // fallback: find any showing window
                for (Window w : Window.getWindows()) {
                    if (w.isShowing()) { owner = w; break; }
                }
            }
            if (owner != null) {
                alert.initOwner(owner);
            }
            alert.setTitle(tieuDe);
            alert.setHeaderText(null);
            alert.setContentText(vanBan);
            alert.showAndWait();
        });
    }


    private void loadContent() {
        txtMa.setText("Mã phiếu đặt: " + select.getMaPhieu());
        txtNgay.setText("Ngày đặt: "+ select.getNgayTao().toString());
        txtSDT.setText("Tên khách hàng: "+select.getKhachHang().getTenKH());
        String trangThai;
        if (select.isThanhToan()) {
            trangThai = "Đã thanh toán";
        } else  {
            trangThai = "Chưa thanh toán";
        }
        txtStatus.setText("Trạng thái: "+ trangThai);
        if (select.getKhuyenMaiDTO() != null) {
            txtKM.setText(select.getKhuyenMaiDTO().getTenKM());
        } else {
            txtKM.setText("Không áp dụng");
        }
        txtTongTien.setText(dinhDangTien(select.getTongTien()));
    }

    private void setupTable() {
        colSTT.setCellValueFactory(cellData -> new SimpleIntegerProperty(listChiTiet.indexOf(cellData.getValue()) + 1).asObject());
        colTenThuoc.setCellValueFactory(cellData ->new SimpleStringProperty(cellData.getValue().getMaThuoc().getMaThuocDTO().getTenThuoc()));
        colSoLuong.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getSoLuong()).asObject());
        colDonGia.setCellValueFactory(cellData -> new SimpleStringProperty(dinhDangTien(cellData.getValue().getMaThuoc().getMaThuocDTO().getGiaBan())));
        colThanhTien.setCellValueFactory(cellData -> new SimpleStringProperty(dinhDangTien(cellData.getValue().getThanhTien())));
    }

    private void loadBangChiTiet() {
        ObservableList<ChiTietPhieuDatThuocDTO> load = FXCollections.observableArrayList(listChiTiet);
        tbThuoc.setItems(load);
    }

    private String dinhDangTien(double tien){
        DecimalFormat df = new DecimalFormat("#,### đ");
        return df.format(tien);
    }

    private void thongBaoVaXuatHoaDon(HoaDonDTO hoaDonDTO, ArrayList<ChiTietHoaDonDTO> listCTHD) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            // Try to set owner for the alert
            Window owner = (this.getScene() != null) ? this.getScene().getWindow() : null;
            if (owner == null) {
                for (Window w : Window.getWindows()) { if (w.isShowing()) { owner = w; break; } }
            }
            if (owner != null) alert.initOwner(owner);

            alert.setTitle("Thành công");
            alert.setHeaderText(null);
            alert.setContentText("Thanh toán phiếu đặt thuốc thành công.\nBạn có muốn xuất hóa đơn?");

            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    FileChooser chooser = new FileChooser();
                    chooser.setTitle("Lưu hóa đơn PDF");
                    // Gợi ý tên file mặc định
                    String fileName = "HoaDon_"
                            + hoaDonDTO.getMaHD() + "_"
                            + LocalDate.now() + ".pdf";
                    chooser.setInitialFileName(fileName);

                    chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));

                    File file;
                    if (owner instanceof Stage stage) {
                        file = chooser.showSaveDialog(stage);
                    } else {
                        // fallback
                        file = chooser.showSaveDialog(null);
                    }
                    if (file == null) return;

                    XuatHoaDonPDF.xuatFilePDF(
                            file,
                            hoaDonDTO,
                            listCTHD,
                            tinhThue(),
                            selectedPDT.getTongTien()
                    );

                    showMess("Thành công", "Xuất hóa đơn PDF thành công!");

                } catch (Exception ex) {
                    System.err.println("Error exporting PDF: " + ex.getMessage());
                    showMess("Lỗi", "Không thể xuất hóa đơn PDF");
                }
            }
        });
    }

    private double tinhThue() {
        double thue = 0.0;
        if (listChiTiet==null || listChiTiet.isEmpty()) {
            return thue;
        }
        for (ChiTietPhieuDatThuocDTO e : listChiTiet){
            thue += e.getSoLuong() * e.getMaThuoc().getMaThuocDTO().getGiaBan()* e.getMaThuoc().getMaThuocDTO().getThue();
        }
        return thue;
    }}

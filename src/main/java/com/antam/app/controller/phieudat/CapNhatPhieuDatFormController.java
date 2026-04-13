//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.antam.app.controller.phieudat;

import com.antam.app.dao.I_ChiTietPhieuDat_DAO;
import com.antam.app.dao.I_PhieuDat_DAO;
import com.antam.app.dao.impl.ChiTietHoaDon_DAO;
import com.antam.app.dao.impl.ChiTietPhieuDat_DAO;
import com.antam.app.dao.impl.HoaDon_DAO;
import com.antam.app.dao.impl.PhieuDat_DAO;
import com.antam.app.entity.*;
import com.antam.app.helper.XuatHoaDonPDF;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.text.Text;

import java.io.File;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

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

    private Text txtMa,txtNgay,txtSDT,txtStatus,txtTongTien,txtKM;
    
    private TableColumn<ChiTietPhieuDatThuoc,Integer> colSTT;
    
    private TableColumn<ChiTietPhieuDatThuoc,String> colTenThuoc,colThanhTien,colDonGia;
    
    private TableColumn<ChiTietPhieuDatThuoc,Integer> colSoLuong;
    
    private TableView<ChiTietPhieuDatThuoc> tbThuoc;

    private PhieuDatThuoc select = selectedPDT;
    private ArrayList<ChiTietPhieuDatThuoc> listChiTiet = I_ChiTietPhieuDat_DAO.getChiTietTheoPhieu(select.getMaPhieu());
    private HoaDon_DAO hoaDon_dao = new HoaDon_DAO();

    public CapNhatPhieuDatFormController() {
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

        txtMa = new Text("Mã phiếu đặt: PD001");
        txtMa.setFill(javafx.scene.paint.Color.WHITE);
        txtMa.setFont(Font.font("System Bold", 20));

        txtNgay = new Text("Ngày đặt: 15/9/2025");
        txtNgay.setFill(javafx.scene.paint.Color.WHITE);
        txtNgay.setFont(Font.font(13));

        txtSDT = new Text("Tên khách hàng: A");
        txtSDT.setFill(javafx.scene.paint.Color.WHITE);
        txtSDT.setFont(Font.font(13));

        txtStatus = new Text("Trạng thái: Chờ xử lý");
        txtStatus.setFill(javafx.scene.paint.Color.WHITE);
        txtStatus.setFont(Font.font(13));

        infoBox.getChildren().addAll(txtMa, txtNgay, txtSDT, txtStatus);

        // LABEL DANH SÁCH THUỐC
        Text labelThuoc = new Text("Danh sách thuốc đặt:");

        // ============================
        // TABLEVIEW
        tbThuoc = new TableView();
        tbThuoc.setPrefSize(758, 282);

        colSTT = new TableColumn("STT");
        colTenThuoc = new TableColumn("Tên thuốc");
        colSoLuong = new TableColumn("Số lượng");
        colDonGia = new TableColumn("Đơn giá");
        colThanhTien = new TableColumn("Thành tiền");

        tbThuoc.getColumns().addAll(
                colSTT, colTenThuoc, colSoLuong, colDonGia, colThanhTien
        );

        tbThuoc.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

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

        txtKM = new Text("");
        txtKM.setFill(javafx.scene.paint.Color.web("#374151"));
        txtKM.setFont(Font.font(13));
        GridPane.setColumnIndex(txtKM, 1);

        // Tổng cộng
        Text totalLabel = new Text("Tổng cộng:");
        totalLabel.setFill(javafx.scene.paint.Color.web("#374151"));
        totalLabel.setFont(Font.font("System Bold", 18));
        GridPane.setRowIndex(totalLabel, 2);

        txtTongTien = new Text("");
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

        this.getStylesheets().add(getClass().getResource("/com/antam/app/styles/dashboard_style.css").toExternalForm());
        this.setContent(anchor);
        /** Sự kiện **/

        if (select.getKhuyenMai()==null || select.getKhuyenMai().getTenKM().equals("") ){
            txtKM.setText("Phiếu đặt thuốc không áp dụng khuyến mãi");
        }else{
            txtKM.setText("Áp dụng khuyến mãi: "+select.getKhuyenMai().getTenKM());
        }



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
        boolean capNhatOK = I_PhieuDat_DAO.capNhatThanhToanPhieuDat(select.getMaPhieu());
        if (!capNhatOK) {
            showMess("Lỗi", "Không thể cập nhật trạng thái phiếu đặt!");
            return;
        }

        // Đồng bộ lại object trong RAM
        select.setThanhToan(true);
        HoaDon hoaDon = new HoaDon(getMaxHashHoaDon(),
                LocalDate.now()
                , PhienNguoiDung.getMaNV()
                , select.getKhachHang()
                , select.getKhuyenMai()
                ,select.getTongTien()
                , false);
        if (hoaDon_dao.insertHoaDon(hoaDon)) {
            ChiTietHoaDon_DAO chiTietHoaDonDao = new ChiTietHoaDon_DAO();
            ArrayList<ChiTietHoaDon> list = new ArrayList<>();
            for (ChiTietPhieuDatThuoc e : tbThuoc.getItems()){
                ChiTietHoaDon i = new ChiTietHoaDon();
                i.setMaHD(hoaDon);
                i.setMaCTT(e.getChiTietThuoc());
                i.setThanhTien(e.getThanhTien());
                i.setMaDVT(e.getDonViTinh());
                i.setSoLuong(e.getSoLuong());
                i.setTinhTrang("Bán");
                chiTietHoaDonDao.themChiTietHoaDon(i);
                list.add(i);
            }
            //xuất pdf phiếu dặt
            thongBaoVaXuatHoaDon(hoaDon,list);
        }else {
            showMess("Lỗi","Thanh toán phiếu đặt thuốc thất bại");
        }
    }

    private String getMaxHashHoaDon(){
        int max = Integer.parseInt(hoaDon_dao.getMaxHash());
        String hash;
        hash = String.format("HD%03d", max + 1);
        return hash;
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
        if (select.getKhuyenMai() != null) {
            txtKM.setText(select.getKhuyenMai().getTenKM());
        } else {
            txtKM.setText("Không áp dụng");
        }
        txtTongTien.setText(dinhDangTien(select.getTongTien()));
    }

    private void setupTable() {
        colSTT.setCellValueFactory(cellData -> new SimpleIntegerProperty(listChiTiet.indexOf(cellData.getValue()) + 1).asObject());
        colTenThuoc.setCellValueFactory(cellData ->new SimpleStringProperty(cellData.getValue().getChiTietThuoc().getMaThuoc().getTenThuoc()));
        colSoLuong.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getSoLuong()).asObject());
        colDonGia.setCellValueFactory(cellData -> new SimpleStringProperty(dinhDangTien(cellData.getValue().getChiTietThuoc().getMaThuoc().getGiaBan())));
        colThanhTien.setCellValueFactory(cellData -> new SimpleStringProperty(dinhDangTien(cellData.getValue().getThanhTien())));
    }

    private void loadBangChiTiet() {
        ObservableList<ChiTietPhieuDatThuoc> load = FXCollections.observableArrayList(listChiTiet);
        tbThuoc.setItems(load);
    }

    private String dinhDangTien(double tien){
        DecimalFormat df = new DecimalFormat("#,### đ");
        return df.format(tien);
    }

    private void thongBaoVaXuatHoaDon(HoaDon hoaDon,ArrayList<ChiTietHoaDon> listCTHD) {
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
                            + hoaDon.getMaHD() + "_"
                            + LocalDate.now() + ".pdf";
                    chooser.setInitialFileName(fileName);

                    chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));

                    File file;
                    if (owner instanceof Stage) {
                        file = chooser.showSaveDialog((Stage) owner);
                    } else {
                        // fallback
                        file = chooser.showSaveDialog(null);
                    }
                    if (file == null) return;

                    XuatHoaDonPDF.xuatFilePDF(
                            file,
                            hoaDon,
                            listCTHD,
                            tinhThue(),
                            selectedPDT.getTongTien()
                    );

                    showMess("Thành công", "Xuất hóa đơn PDF thành công!");

                } catch (Exception ex) {
                    ex.printStackTrace();
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
        for (ChiTietPhieuDatThuoc e : listChiTiet){
            thue += e.getSoLuong() * e.getChiTietThuoc().getMaThuoc().getGiaBan()* e.getChiTietThuoc().getMaThuoc().getThue();
        }
        return thue;
    }}

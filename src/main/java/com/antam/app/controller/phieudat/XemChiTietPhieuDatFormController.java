//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.antam.app.controller.phieudat;

import com.antam.app.network.ClientManager;
import com.antam.app.dto.ChiTietPhieuDatThuocDTO;
import com.antam.app.dto.PhieuDatThuocDTO;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;

import java.text.DecimalFormat;
import java.util.List;

import static com.antam.app.controller.phieudat.TimPhieuDatController.selectedPhieuDatThuocDTO;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class XemChiTietPhieuDatFormController extends DialogPane{
    private Text txtMa,txtNgay,txtSDT,txtStatus,txtTongTien,txtKM;
    
    private TableColumn<ChiTietPhieuDatThuocDTO,Integer> colSTT;
    
    private TableColumn<ChiTietPhieuDatThuocDTO,String> colTenThuoc,colThanhTien,colDonGia;
    
    private TableColumn<ChiTietPhieuDatThuocDTO,Integer> colSoLuong;
    
    private TableView<ChiTietPhieuDatThuocDTO> tbThuoc;

    private PhieuDatThuocDTO select = selectedPhieuDatThuocDTO;
//    private I_ChiTietPhieuDat_Service I_ChiTietPhieuDat_Service = new ChiTietPhieuDat_Service();
    private List<ChiTietPhieuDatThuocDTO> listChiTiet ;
    private ClientManager clientManager = ClientManager.getInstance();

    public XemChiTietPhieuDatFormController() {
        this.setPrefSize(800, 662);
        FlowPane headerPane = new FlowPane();
        headerPane.setAlignment(Pos.CENTER);
        headerPane.setStyle("-fx-background-color: #1e3a8a;");

        Text headerText = new Text("Chi tiết phiếu đặt");
        headerText.setFill(Color.WHITE);
        headerText.setFont(Font.font("System", FontWeight.BOLD, 15));
        FlowPane.setMargin(headerText, new Insets(10, 0, 10, 0));

        headerPane.getChildren().add(headerText);
        this.setHeader(headerPane);


        /* ================ MAIN CONTENT ================ */
        VBox mainVBox = new VBox(10);
        mainVBox.setPadding(new Insets(10));


        /* ============= BLUE INFO BOX ============= */
        VBox infoBox = new VBox(5);
        infoBox.setStyle("-fx-background-color: #2563eb; -fx-background-radius: 5px;");
        infoBox.setPadding(new Insets(10, 100, 10, 10));

        txtMa = createWhiteText("Mã phiếu đặt: --", 20, true);
        txtNgay = createWhiteText("Ngày đặt: --", 13, false);
        txtSDT = createWhiteText("Tên khách hàng: --", 13, false);
        txtStatus = createWhiteText("Trạng thái: --", 13, false);

        infoBox.getChildren().addAll(txtMa, txtNgay, txtSDT, txtStatus);


        /* ============= LABEL ============= */
        Text labelThuoc = new Text("Danh sách thuốc đặt:");
        labelThuoc.setFont(Font.font(14));


        /* ============= TABLE ============= */
        tbThuoc = new TableView<>();
        tbThuoc.setPrefHeight(350);

        VBox.setVgrow(tbThuoc, Priority.ALWAYS); // fix table co lại

        colSTT = new TableColumn<>("STT");
        colSTT.setPrefWidth(70);

        colTenThuoc = new TableColumn<>("Tên thuốc");
        colTenThuoc.setPrefWidth(260);

        colSoLuong = new TableColumn<>("Số lượng");
        colSoLuong.setPrefWidth(140);

        colDonGia = new TableColumn<>("Đơn giá");
        colDonGia.setPrefWidth(130);

        colThanhTien = new TableColumn<>("Thành tiền");
        colThanhTien.setPrefWidth(140);

        tbThuoc.getColumns().addAll(colSTT, colTenThuoc, colSoLuong, colDonGia, colThanhTien);


        /* ============= TỔNG TIỀN GRID ============= */
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setStyle("-fx-background-color: #f8fafc; -fx-background-radius: 8px;");
        grid.setHgap(5);
        grid.setVgap(8);

        ColumnConstraints c1 = new ColumnConstraints();
        c1.setHgrow(Priority.ALWAYS);
        ColumnConstraints c2 = new ColumnConstraints();
        c2.setHgrow(Priority.ALWAYS);
        grid.getColumnConstraints().addAll(c1, c2);

        // Row 1 - KM
        Text kmLabel = createGrayText("Khuyến mãi:", 13, false);
        GridPane.setRowIndex(kmLabel, 1);

        txtKM = createGrayText("", 13, false);
        GridPane.setColumnIndex(txtKM, 1);
        GridPane.setRowIndex(txtKM, 1);

        // Row 2 - Tổng tiền
        Text sumLabel = createGrayText("Tổng cộng:", 18, true);
        GridPane.setRowIndex(sumLabel, 2);

        txtTongTien = createGrayText("", 18, true);
        GridPane.setColumnIndex(txtTongTien, 1);
        GridPane.setRowIndex(txtTongTien, 2);

        grid.getChildren().addAll(kmLabel, txtKM, sumLabel, txtTongTien);


        /* ============ ADD TO MAIN UI ============ */
        mainVBox.getChildren().addAll(infoBox, labelThuoc, tbThuoc, grid);

        // Quan trọng: dùng setContent thay vì add children
        this.setContent(mainVBox);

        // Style
        this.getStylesheets().add(
                getClass().getResource("/com/antam/app/styles/dashboard_style.css").toExternalForm()
        );
        /** Sự kiện **/
        ButtonType cancelButton = new ButtonType("Huỷ", ButtonData.CANCEL_CLOSE);
        this.getButtonTypes().add(cancelButton);

        //gọi các phương thức xử lí

        Task< List<ChiTietPhieuDatThuocDTO>> task = new Task<List<ChiTietPhieuDatThuocDTO>>() {
            @Override
            protected List<ChiTietPhieuDatThuocDTO> call() throws Exception {
                return clientManager.getChiTietPDT(select.getMaPhieu());
            }
        };

        task.setOnSucceeded(event -> {
            listChiTiet = task.getValue();
            loadBangChiTiet();
        });
        Thread thread = new Thread(task);
        thread.start();

        setupTable();
        loadContent();
    }

    private Text createWhiteText(String msg, int size, boolean bold) {
        Text t = new Text(msg);
        t.setFill(Color.WHITE);
        t.setFont(Font.font("System", bold ? FontWeight.BOLD : FontWeight.NORMAL, size));
        return t;
    }

    private Text createGrayText(String msg, int size, boolean bold) {
        Text t = new Text(msg);
        t.setFill(Color.web("#374151"));
        t.setFont(Font.font("System", bold ? FontWeight.BOLD : FontWeight.NORMAL, size));
        return t;
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
}

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.antam.app.controller.phieunhap;

import com.antam.app.connect.ConnectDB;
import com.antam.app.dao.impl.ChiTietPhieuNhap_DAO;
import com.antam.app.dao.impl.PhieuNhap_DAO;
import com.antam.app.entity.ChiTietPhieuNhap;
import com.antam.app.entity.PhieuNhap;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Font;
import javafx.scene.paint.Color;


public class XemChiTietPhieuNhapFormController extends DialogPane{

    private Text txtMaPhieuNhap, txtDiaChi, txtLyDo, txtNgayNhap, txtTongTien, txtNhanVien, txtNhaCungCap, txtTrangThai;
    private TableView<ChiTietPhieuNhap> tbChiTietPhieuNhap;

    private PhieuNhap_DAO phieuNhap_DAO = new PhieuNhap_DAO();
    private ChiTietPhieuNhap_DAO chiTietPhieuNhap_DAO = new ChiTietPhieuNhap_DAO();

    public XemChiTietPhieuNhapFormController() {
        this.setPrefSize(800, 600);
        VBox mainVBox = new VBox(10);
        AnchorPane.setTopAnchor(mainVBox, 0.0);
        AnchorPane.setBottomAnchor(mainVBox, 0.0);
        AnchorPane.setLeftAnchor(mainVBox, 0.0);
        AnchorPane.setRightAnchor(mainVBox, 0.0);

        FlowPane header = new FlowPane();
        header.setStyle("-fx-background-color: #1e3a8a;");
        header.setPadding(new Insets(10, 0, 10, 0));
        Text headerText = new Text("Chi tiết phiếu nhập");
        headerText.setFont(Font.font("System Bold", 15));
        headerText.setFill(Color.WHITE);
        header.setAlignment(Pos.CENTER);
        header.getChildren().add(headerText);

        VBox infoBox = new VBox(10);
        infoBox.setStyle("-fx-background-color: #2563eb; -fx-background-radius: 5px;");
        infoBox.setPadding(new Insets(10));

        txtMaPhieuNhap = new Text("Phiếu nhập: HD001");
        txtMaPhieuNhap.setFont(Font.font("System Bold", 20));
        txtMaPhieuNhap.setFill(Color.WHITE);

        GridPane gridInfo = new GridPane();
        ColumnConstraints col1 = new ColumnConstraints();
        ColumnConstraints col2 = new ColumnConstraints();
        gridInfo.getColumnConstraints().addAll(col1, col2);

        RowConstraints row0 = new RowConstraints(30);
        RowConstraints row1 = new RowConstraints(30);
        RowConstraints row2 = new RowConstraints(30);
        gridInfo.getRowConstraints().addAll(row0, row1, row2);

        txtLyDo = new Text("Nhân viên: Nguyễn Văn An");
        txtLyDo.setFont(Font.font(13));
        txtLyDo.setFill(Color.WHITE);

        txtNgayNhap = new Text("Khách hàng: Nguyễn Văn A");
        txtNgayNhap.setFont(Font.font(13));
        txtNgayNhap.setFill(Color.WHITE);
        GridPane.setRowIndex(txtNgayNhap, 1);

        txtNhaCungCap = new Text("Ngày: 9/12/2024");
        txtNhaCungCap.setFont(Font.font(13));
        txtNhaCungCap.setFill(Color.WHITE);
        GridPane.setRowIndex(txtNhaCungCap, 2);

        txtDiaChi = new Text("Địa chỉ: ...");
        txtDiaChi.setFont(Font.font(13));
        txtDiaChi.setFill(Color.WHITE);
        GridPane.setColumnIndex(txtDiaChi, 1);

        txtNhanVien = new Text("Nhân viên: Nguyễn Văn An");
        txtNhanVien.setFont(Font.font(13));
        txtNhanVien.setFill(Color.WHITE);
        GridPane.setColumnIndex(txtNhanVien, 1);
        GridPane.setRowIndex(txtNhanVien, 1);

        txtTrangThai = new Text("Trạng thái : 1");
        txtTrangThai.setFont(Font.font(13));
        txtTrangThai.setFill(Color.WHITE);
        GridPane.setColumnIndex(txtTrangThai, 1);
        GridPane.setRowIndex(txtTrangThai, 2);

        gridInfo.getChildren().addAll(txtLyDo, txtNgayNhap, txtNhaCungCap, txtDiaChi, txtNhanVien, txtTrangThai);

        infoBox.getChildren().addAll(txtMaPhieuNhap, gridInfo);

        Text tableLabel = new Text("Danh chi tiết:");
        tbChiTietPhieuNhap = new TableView<>();
        tbChiTietPhieuNhap.getStyleClass().add("table-view");
        tbChiTietPhieuNhap.setPrefWidth(200);

        GridPane totalGrid = new GridPane();
        totalGrid.setStyle("-fx-background-color: #f8fafc; -fx-background-radius: 8px;");
        totalGrid.setHgap(5);
        totalGrid.setPadding(new Insets(10));

        ColumnConstraints totalCol1 = new ColumnConstraints();
        ColumnConstraints totalCol2 = new ColumnConstraints();
        totalGrid.getColumnConstraints().addAll(totalCol1, totalCol2);
        RowConstraints totalRow = new RowConstraints(30);
        totalGrid.getRowConstraints().add(totalRow);

        Text totalLabel = new Text("Tổng tiền:");
        totalLabel.setFont(Font.font("System Bold", 18));
        totalLabel.setFill(Color.web("#374151"));

        txtTongTien = new Text("500.000đ");
        txtTongTien.setFont(Font.font("System Bold", 18));
        txtTongTien.setFill(Color.web("#374151"));
        GridPane.setColumnIndex(txtTongTien, 1);

        totalGrid.getChildren().addAll(totalLabel, txtTongTien);

        // Thêm vào main VBox
        mainVBox.getChildren().addAll(header, infoBox, tableLabel, tbChiTietPhieuNhap, totalGrid);
        this.getStylesheets().add(getClass().getResource("/com/antam/app/styles/dashboard_style.css").toExternalForm());
        this.setContent(mainVBox);
        /** Sự kiện **/
        //Kết nối
        try {
            Connection con = ConnectDB.getInstance().connect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        ButtonType applyButton = new ButtonType("Đóng", ButtonData.CANCEL_CLOSE);
        this.getButtonTypes().add(applyButton);
    }


    public void showChiTietPhieuNhap(PhieuNhap phieuNhap) {
        txtMaPhieuNhap.setText("Mã phiếu nhập: "+ phieuNhap.getMaPhieuNhap());
        txtDiaChi.setText("Địa chỉ: " + phieuNhap.getDiaChi());
        txtLyDo.setText("Lý do nhập: " + phieuNhap.getLyDo());
        txtNgayNhap.setText("Ngày nhập: " + String.valueOf(phieuNhap.getNgayNhap()));
        DecimalFormat decimal = new DecimalFormat("#,### đ");
        txtTongTien.setText(decimal.format(phieuNhap.getTongTien()));
        txtNhanVien.setText("Nhân viên nhập: " + phieuNhap.getMaNV().getHoTen());
        txtNhaCungCap.setText("Nhà cung cấp: " + phieuNhap.getNhaCungCap());
        txtTrangThai.setText("Trạng thái: " + (phieuNhap.isDeleteAt() ? "Đã hủy" : "Hoạt động"));

        //Load chi tiết phiếu nhập
        ArrayList<ChiTietPhieuNhap> dsChiTietPhieuNhap = chiTietPhieuNhap_DAO.getDanhSachChiTietPhieuNhapTheoMaPN(phieuNhap.getMaPhieuNhap());
        ObservableList<ChiTietPhieuNhap> data = FXCollections.observableArrayList();
        data.setAll(dsChiTietPhieuNhap);
        tbChiTietPhieuNhap.setItems(data);
        TableColumn<ChiTietPhieuNhap, Number> colSoThuTu = new TableColumn<>("STT");
        colSoThuTu.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(tbChiTietPhieuNhap.getItems().indexOf(cellData.getValue()) + 1)
        );

        TableColumn<ChiTietPhieuNhap, String> colMaThuoc = new TableColumn<>("Mã thuốc");
        colMaThuoc.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLoThuoc().getMaThuoc().getMaThuoc()));

        TableColumn<ChiTietPhieuNhap, String> colDonViTinh = new TableColumn<>("Đơn Vị Tính");
        colDonViTinh.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMaDVT().getTenDVT()));

        TableColumn<ChiTietPhieuNhap, Number> colSoLuongNhap = new TableColumn<>("Số Lượng Nhập");
        colSoLuongNhap.setCellValueFactory(new PropertyValueFactory<>("soLuong"));

        TableColumn<ChiTietPhieuNhap, Double> colGiaNhap = new TableColumn<>("Giá Nhập");
        colGiaNhap.setCellValueFactory(new PropertyValueFactory<>("giaNhap"));
        colGiaNhap.setCellFactory(tc -> new TableCell<ChiTietPhieuNhap, Double>() {
            @Override
            protected void updateItem(Double giaNhap, boolean empty) {
                super.updateItem(giaNhap, empty);
                if (empty) {
                    setText(null);
                } else {
                    DecimalFormat df = new DecimalFormat("#,###");
                    setText(df.format(giaNhap) + " đ");
                }
            }
        });
        tbChiTietPhieuNhap.getColumns().addAll(colSoThuTu, colMaThuoc, colDonViTinh, colSoLuongNhap, colGiaNhap);
    }

}
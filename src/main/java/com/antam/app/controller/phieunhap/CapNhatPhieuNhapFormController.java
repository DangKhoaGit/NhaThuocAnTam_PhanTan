//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.antam.app.controller.phieunhap;

import com.antam.app.connect.ConnectDB;
import com.antam.app.dao.*;
import com.antam.app.dao.impl.PhieuNhap_DAO;
import com.antam.app.entity.*;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;

public class CapNhatPhieuNhapFormController extends DialogPane{

    private TextField tfMaPhieuNhap, tfNhaCungCap, tfDiaChi, tfLyDo;

    private PhieuNhap_DAO phieuNhap_DAO = new PhieuNhap_DAO();

    public CapNhatPhieuNhapFormController() {
        this.setPrefSize(800, 600);
        FlowPane header = new FlowPane();
        header.setAlignment(Pos.CENTER);
        header.setStyle("-fx-background-color: #1e3a8a;");

        Text title = new Text("Cập nhật phiếu nhập");
        title.setFill(Color.WHITE);
        title.setFont(Font.font("System Bold", 15));
        FlowPane.setMargin(title, new Insets(10, 0, 10, 0));

        header.getChildren().add(title);
        this.setHeader(header);

        /* ================= CONTENT ================= */
        VBox contentWrapper = new VBox(10);

        ScrollPane scroll = new ScrollPane();
        scroll.setFitToWidth(true);

        VBox scrollContent = new VBox(10);

        /* ========= GRIDPANE ========= */
        GridPane grid = new GridPane();
        grid.setHgap(5);

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setHgrow(Priority.SOMETIMES);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setHgrow(Priority.SOMETIMES);

        grid.getColumnConstraints().addAll(col1, col2);

        // Rows
        grid.getRowConstraints().add(new RowConstraints(30));
        grid.getRowConstraints().add(new RowConstraints(40));
        grid.getRowConstraints().add(new RowConstraints(40));
        grid.getRowConstraints().add(new RowConstraints(40));

        /* ====== Controls ====== */
        Text lblMa = new Text("Mã phiếu nhập:");
        lblMa.setFill(Color.web("#374151"));

        tfMaPhieuNhap = new TextField();
        tfMaPhieuNhap.setPrefHeight(40);
        tfMaPhieuNhap.setPromptText("Nhập mã phiếu nhập");
        tfMaPhieuNhap.getStyleClass().add("text-field");

        Text lblNCC = new Text("Nhà cung cấp:");
        lblNCC.setFill(Color.web("#374151"));

        tfNhaCungCap = new TextField();
        tfNhaCungCap.setPrefHeight(40);
        tfNhaCungCap.setPromptText("Nhập nhà cung cấp");
        tfNhaCungCap.getStyleClass().add("text-field");

        Text lblDiaChi = new Text("Địa chỉ:");
        lblDiaChi.setFill(Color.web("#374151"));

        tfDiaChi = new TextField();
        tfDiaChi.setPrefHeight(40);
        tfDiaChi.setPromptText("Nhập địa chỉ");
        tfDiaChi.getStyleClass().add("text-field");

        Text lblLyDo = new Text("Lý do nhập");
        lblLyDo.setFill(Color.web("#374151"));

        tfLyDo = new TextField();
        tfLyDo.setPrefHeight(40);
        tfLyDo.setPromptText("Nhập lý do");
        tfLyDo.getStyleClass().add("text-field");

        /* ====== Add to Grid ====== */
        grid.add(lblMa, 0, 0);
        grid.add(lblNCC, 1, 0);

        grid.add(tfMaPhieuNhap, 0, 1);
        grid.add(tfNhaCungCap, 1, 1);

        grid.add(lblDiaChi, 0, 2);
        grid.add(lblLyDo, 1, 2);

        grid.add(tfDiaChi, 0, 3);
        grid.add(tfLyDo, 1, 3);

        /* ====== Final Layout ====== */
        scrollContent.getChildren().add(grid);
        scroll.setContent(scrollContent);
        contentWrapper.getChildren().add(scroll);

        AnchorPane root = new AnchorPane(contentWrapper);
        AnchorPane.setLeftAnchor(contentWrapper, 0.0);
        AnchorPane.setRightAnchor(contentWrapper, 0.0);

        this.setContent(root);
        /** Sự kiện **/
        this.getStylesheets().add(getClass().getResource("/com/antam/app/styles/dashboard_style.css").toExternalForm());
        //Kết nối
        try {
            Connection con = ConnectDB.getInstance().connect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        ButtonType cancelButton = new ButtonType("Huỷ", ButtonData.CANCEL_CLOSE);
        ButtonType deleteButton = new ButtonType("Huỷ Phiếu Nhập", ButtonData.YES);
        ButtonType applyButton = new ButtonType("Lưu", ButtonData.APPLY);
        this.getButtonTypes().add(cancelButton);
        this.getButtonTypes().add(applyButton);
        this.getButtonTypes().add(deleteButton);

        Button btnAppy = (Button) this.lookupButton(applyButton);
        Button btnDelete = (Button) this.lookupButton(deleteButton);
        btnDelete.addEventFilter(ActionEvent.ACTION, e->{
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Xác nhận huỷ phiếu nhập");
            alert.setHeaderText(null);
            alert.setContentText("Bạn có chắc chắn muốn huỷ phiếu nhập này không?");
            ButtonType btnYes = new ButtonType("Có", ButtonData.YES);
            ButtonType btnNo = new ButtonType("Không", ButtonData.CANCEL_CLOSE);
            alert.getButtonTypes().setAll(btnYes, btnNo);
            alert.showAndWait().ifPresent(type -> {
                if (type == btnYes) {
                    if (phieuNhap_DAO.huyPhieuNhap(tfMaPhieuNhap.getText())){
                        showCanhBao("Thành công", "Huỷ phiếu nhập thành công");
                    }else{
                        showCanhBao("Thất bại", "Huỷ phiếu nhập thất bại");
                        e.consume();
                    }
                } else {
                    e.consume();
                }
            });
        });

        btnAppy.addEventFilter(ActionEvent.ACTION, event -> {
            if (!checkTruongDuLieu()){
                event.consume();
            }else{
                if (phieuNhap_DAO.suaPhieuNhap(new PhieuNhap(tfMaPhieuNhap.getText(), tfNhaCungCap.getText(), LocalDate.now(), tfDiaChi.getText(), tfLyDo.getText(), null, 0.0, false))){
                    showCanhBao("Thành công", "Cập nhật phiếu nhập thành công");
                }else{
                    showCanhBao("Thất bại", "Cập nhật phiếu nhập thất bại");
                    event.consume();
                }
            }
        });
    }

    // Hiển thị thông tin phiếu nhập lên form
    public void showPhieuNhap(PhieuNhap phieuNhap){
        tfMaPhieuNhap.setText(phieuNhap.getMaPhieuNhap());
        tfMaPhieuNhap.setEditable(false);
        tfNhaCungCap.setText(phieuNhap.getNhaCungCap());
        tfDiaChi.setText(phieuNhap.getDiaChi());
        tfLyDo.setText(phieuNhap.getLyDo());
        System.out.println(phieuNhap.getMaPhieuNhap());
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
}
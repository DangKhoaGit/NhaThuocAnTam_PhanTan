/*
 * @ (#) TuyChinhKhuyenMaiController.java   1.0 10/15/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package com.antam.app.controller.khuyenmai;

import com.antam.app.connect.ConnectDB;
import com.antam.app.network.ClientManager;
import com.antam.app.service.impl.KhuyenMai_Service;
import com.antam.app.service.impl.LoaiKhuyenMai_Service;
import com.antam.app.dto.KhuyenMaiDTO;
import com.antam.app.dto.LoaiKhuyenMaiDTO;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.text.Text;

import java.sql.Connection;
import java.util.ArrayList;
import javafx.geometry.Insets;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/*
 * @description
 * @author: Duong Nguyen
 * @date: 10/15/2025
 * version: 1.0
 */
public class CapNhatKhuyenMaiFormController extends DialogPane{

    private TextField txtMaKhuyenMai, txtTenKhuyenMai;
    private ComboBox<LoaiKhuyenMaiDTO> cbLoaiKhuyenMai;
    private Spinner<Integer> spSo, spSoLuongToiDa;
    private DatePicker dpNgayBacDau, dpNgayKetThuc;
    
    private Text txtThongBao;
    private KhuyenMai_Service khuyenMai_dao = new KhuyenMai_Service();
    private LoaiKhuyenMai_Service loaiKhuyenMai_dao = new LoaiKhuyenMai_Service();
    private KhuyenMaiDTO khuyenMaiDTO;
    private final ClientManager clientManager;

    public void setKhuyenMai(KhuyenMaiDTO khuyenMaiDTO) {
        this.khuyenMaiDTO = khuyenMaiDTO;
    }
    public KhuyenMaiDTO getKhuyenMai() {
        return khuyenMaiDTO;
    }
    public void showdata(KhuyenMaiDTO khuyenMaiDTO) {
        if (khuyenMaiDTO != null) {
            txtMaKhuyenMai.setText(khuyenMaiDTO.getMaKM());
            txtTenKhuyenMai.setText(khuyenMaiDTO.getTenKM());
            cbLoaiKhuyenMai.setValue(khuyenMaiDTO.getLoaiKhuyenMaiDTO());
            if (khuyenMaiDTO.getLoaiKhuyenMaiDTO().getMaLKM() == 1) { // Phần trăm
                SpinnerValueFactory<Integer> valueFactorySo = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 10, 1);
                spSo.setValueFactory(valueFactorySo);
            } else if (khuyenMaiDTO.getLoaiKhuyenMaiDTO().getMaLKM() == 2) { // Tiền mặt
                SpinnerValueFactory<Integer> valueFactorySo = new SpinnerValueFactory.IntegerSpinnerValueFactory(1000, 1000000, 10000, 1000);
                spSo.setValueFactory(valueFactorySo);
            }
            SpinnerValueFactory<Integer> valueFactorySoLuongToiDa = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 1000, 10, 1);
            spSoLuongToiDa.setValueFactory(valueFactorySoLuongToiDa);
            spSo.getValueFactory().setValue((int) khuyenMaiDTO.getSo());
            spSoLuongToiDa.getValueFactory().setValue(khuyenMaiDTO.getSoLuongToiDa());
            dpNgayBacDau.setValue(khuyenMaiDTO.getNgayBatDau());
            dpNgayKetThuc.setValue(khuyenMaiDTO.getNgayKetThuc());
        }
    }
    public CapNhatKhuyenMaiFormController() {
        this.clientManager = ClientManager.getInstance();
        this.setPrefSize(800, 600);
        FlowPane header = new FlowPane();
        header.setAlignment(javafx.geometry.Pos.CENTER);
        header.setStyle("-fx-background-color: #1e3a8a;");

        Text txtHeader = new Text("Cập Nhật Khuyến Mãi");
        txtHeader.setFill(Color.WHITE);
        txtHeader.setFont(Font.font("System Bold", 15));
        FlowPane.setMargin(txtHeader, new Insets(10, 0, 10, 0));

        header.getChildren().add(txtHeader);
        this.setHeader(header);

        // ====================== CONTENT ROOT ======================
        AnchorPane root = new AnchorPane();
        VBox vbox = new VBox(10);

        AnchorPane.setLeftAnchor(vbox, 0.0);
        AnchorPane.setRightAnchor(vbox, 0.0);

        // ====================== GRIDPANE FORM ======================
        GridPane grid = new GridPane();
        grid.setHgap(5);

        // Column cấu hình
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPrefWidth(100);
        col1.setHgrow(Priority.SOMETIMES);

        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPrefWidth(100);
        col2.setHgrow(Priority.SOMETIMES);

        grid.getColumnConstraints().addAll(col1, col2);

        // Row spacing theo FXML
        for (int i = 0; i < 8; i++) {
            RowConstraints row = new RowConstraints();
            row.setPrefHeight((i % 2 == 0) ? 30 : 40);
            row.setVgrow(Priority.SOMETIMES);
            grid.getRowConstraints().add(row);
        }

        // ====================== TẠO COMPONENT ======================
        Text lblMa = label("Mã khuyến mãi:");
        txtMaKhuyenMai = createTextField();

        Text lblTen = label("Tên khuyến mãi:");
        txtTenKhuyenMai = createTextField();

        Text lblLoai = label("Loại khuyến mãi:");
        cbLoaiKhuyenMai = new ComboBox<>();
        cbLoaiKhuyenMai.setPrefHeight(40);
        cbLoaiKhuyenMai.setMaxWidth(Double.MAX_VALUE);

        Text lblSo = label("Số (Giá trị):");
        spSo = new Spinner<>(0, Integer.MAX_VALUE, 0);
        spSo.setEditable(true);
        spSo.setPrefHeight(40);
        spSo.setMaxWidth(Double.MAX_VALUE);

        Text lblNgayBD = label("Ngày bắt đầu:");
        dpNgayBacDau = new DatePicker();
        dpNgayBacDau.setPrefHeight(40);

        Text lblNgayKT = label("Ngày kết thúc:");
        dpNgayKetThuc = new DatePicker();
        dpNgayKetThuc.setPrefHeight(40);

        Text lblSLToiDa = label("Số lượng tối đa:");
        spSoLuongToiDa = new Spinner<>(0, Integer.MAX_VALUE, 0);
        spSoLuongToiDa.setEditable(true);
        spSoLuongToiDa.setPrefHeight(40);
        spSoLuongToiDa.setMaxWidth(Double.MAX_VALUE);

        txtThongBao = new Text();
        txtThongBao.setFill(Color.RED);
        txtThongBao.setWrappingWidth(386);

        // ====================== ADD vào GRID ======================

        // Hàng 0
        grid.add(lblMa, 0, 0);
        grid.add(lblTen, 1, 0);

        // Hàng 1
        grid.add(txtMaKhuyenMai, 0, 1);
        grid.add(txtTenKhuyenMai, 1, 1);

        // Hàng 2
        grid.add(lblLoai, 0, 2);
        grid.add(lblSo, 1, 2);

        // Hàng 3
        grid.add(cbLoaiKhuyenMai, 0, 3);
        grid.add(spSo, 1, 3);

        // Hàng 4
        grid.add(lblNgayBD, 0, 4);
        grid.add(lblNgayKT, 1, 4);

        // Hàng 5
        grid.add(dpNgayBacDau, 0, 5);
        grid.add(dpNgayKetThuc, 1, 5);

        // Hàng 6
        grid.add(lblSLToiDa, 0, 6);

        // Hàng 7
        grid.add(spSoLuongToiDa, 0, 7);
        grid.add(txtThongBao, 1, 7);

        vbox.getChildren().add(grid);
        root.getChildren().add(vbox);

        this.setContent(root);
        /** Sự kiện **/
        // them button vao dialog
        ButtonType closeButton = new ButtonType("Đóng", ButtonBar.ButtonData.CANCEL_CLOSE);
        ButtonType applyButtonUpdate = new ButtonType("Sửa", ButtonBar.ButtonData.APPLY);
        ButtonType applyButtonDelete = new ButtonType("Xóa", ButtonBar.ButtonData.APPLY);
        // them button vao dialog
        this.getButtonTypes().addAll(applyButtonUpdate, applyButtonDelete, closeButton);

        try {
            ConnectDB.getInstance().getConnection();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // su kien button
        Button applyBtnUpdate = (Button) this.lookupButton(applyButtonUpdate);
        Button applyBtnDelete = (Button) this.lookupButton(applyButtonDelete);

        applyBtnUpdate.addEventFilter(ActionEvent.ACTION, e -> {
            if (!validate()) {
                e.consume();
            } else {
                String maKM = txtMaKhuyenMai.getText().trim();
                String tenKM = txtTenKhuyenMai.getText().trim();
                LoaiKhuyenMaiDTO loaiKM = cbLoaiKhuyenMai.getSelectionModel().getSelectedItem();
                int so = spSo.getValue();
                int soLuongToiDa = spSoLuongToiDa.getValue();
                java.time.LocalDate ngayBatDau = dpNgayBacDau.getValue();
                java.time.LocalDate ngayKetThuc = dpNgayKetThuc.getValue();

                KhuyenMaiDTO updatedKhuyenMaiDTO = new KhuyenMaiDTO(maKM, tenKM, ngayBatDau, ngayKetThuc, loaiKM, so, soLuongToiDa, false);
                boolean success = clientManager.updateKhuyenMai(updatedKhuyenMaiDTO);
                if (success) {
                    txtThongBao.setText("Cập nhật khuyến mãi thành công");
                    khuyenMaiDTO = updatedKhuyenMaiDTO;
                } else {
                    txtThongBao.setText("Cập nhật khuyến mãi thất bại");
                    e.consume();
                }
            }
        });

        applyBtnDelete.addEventFilter(ActionEvent.ACTION, e -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Xác nhận xóa");
            alert.setHeaderText(null);
            alert.setContentText("Bạn có chắc chắn muốn xóa khuyến mãi này?");
            ButtonType result = alert.showAndWait().orElse(ButtonType.CANCEL);
            if (result != ButtonType.OK) {
                e.consume();
            } else {
                String maKM = txtMaKhuyenMai.getText().trim();
                boolean success = clientManager.deleteKhuyenMai(maKM);
                if (success) {
                    txtThongBao.setText("Xóa khuyến mãi thành công");
                } else {
                    txtThongBao.setText("Xóa khuyến mãi thất bại");
                    e.consume();
                }
            }
        });
        // set ma khuyen mai
        txtMaKhuyenMai.setEditable(false);
        // load loai khuyen mai
        loadLoaiKhuyenMai();
        // set su kien loai khuyen mai
        cbLoaiKhuyenMai.setOnAction(e -> {
            LoaiKhuyenMaiDTO selectedLoai = cbLoaiKhuyenMai.getSelectionModel().getSelectedItem();
            if (selectedLoai != null) {
                if (selectedLoai.getMaLKM() == 1) { // Phần trăm
                    SpinnerValueFactory<Integer> value = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 10, 1);
                    spSo.setValueFactory(value);
                } else if (selectedLoai.getMaLKM() == 2) { // Tiền mặt
                    SpinnerValueFactory<Integer> value = new SpinnerValueFactory.IntegerSpinnerValueFactory(1000, 1000000, 10000, 1000);
                    spSo.setValueFactory(value);
                }
            }
        });
    }

    private Text label(String text) {
        Text lbl = new Text(text);
        lbl.setFill(Color.web("#374151"));
        return lbl;
    }

    private TextField createTextField() {
        TextField tf = new TextField();
        tf.setPrefHeight(40);
        tf.setMaxHeight(Double.MAX_VALUE);
        return tf;
    }

    public void loadLoaiKhuyenMai(){
        ArrayList<LoaiKhuyenMaiDTO> listLoaiKhuyenMai = new ArrayList<>(clientManager.getLoaiKhuyenMaiList());
        cbLoaiKhuyenMai.getItems().clear();
        cbLoaiKhuyenMai.getItems().addAll(listLoaiKhuyenMai);
        cbLoaiKhuyenMai.getSelectionModel().selectFirst();
    }

    public boolean validate(){
        String tenKM = txtTenKhuyenMai.getText().trim();
        if (tenKM.isEmpty()) {
            txtThongBao.setText("Tên khuyến mãi không được để trống");
            return false;
        }
        if (cbLoaiKhuyenMai.getValue() == null) {
            txtThongBao.setText("Vui lòng chọn loại khuyến mãi");
            return false;
        }
        if (dpNgayBacDau.getValue() == null) {
            txtThongBao.setText("Vui lòng chọn ngày bắt đầu");
            return false;
        }
        if (dpNgayKetThuc.getValue() == null) {
            txtThongBao.setText("Vui lòng chọn ngày kết thúc");
            return false;
        }
        if (dpNgayBacDau.getValue().isAfter(dpNgayKetThuc.getValue())) {
            txtThongBao.setText("Ngày bắt đầu phải trước ngày kết thúc");
            return false;
        }
        if (spSo.getValue() == null || spSo.getValue() <= 0) {
            txtThongBao.setText("Giá trị khuyến mãi phải lớn hơn 0");
            return false;
        }
        if (spSoLuongToiDa.getValue() == null || spSoLuongToiDa.getValue() <= 0) {
            txtThongBao.setText("Số lượng tối đa phải lớn hơn 0");
            return false;
        }
        return true;
    }
}

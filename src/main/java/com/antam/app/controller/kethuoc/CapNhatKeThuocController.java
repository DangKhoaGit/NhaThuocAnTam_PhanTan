//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.antam.app.controller.kethuoc;

import com.antam.app.network.ClientManager;
import com.antam.app.dto.KeDTO;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcons;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import java.util.ArrayList;
import java.util.Date;

public class CapNhatKeThuocController extends ScrollPane{

    private TableView<KeDTO> tbKeThuoc;
    private TextField tfMaKe, tfTenKe, tfLoaiKe;
    private Button btnSuaKe, btnXoaKe, btnKhoiPhucKe;
    private ClientManager ke_DAO = ClientManager.getInstance();

    /* Lấy dữ liệu từ DAO */
    private ArrayList<KeDTO> dsKeThuoc = new ArrayList<>();
    private ObservableList<KeDTO> data = FXCollections.observableArrayList();

    public CapNhatKeThuocController() {
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
        titleBox.setAlignment(Pos.CENTER_LEFT);

        Text title = new Text("Cập nhật kệ thuốc");
        title.setFill(Color.web("#1e3a8a"));
        title.setFont(Font.font("System Bold", 30));

        Pane spacer = new Pane();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        titleBox.getChildren().addAll(title, spacer);


        // ============================
        //        INPUT FORM
        // ============================
        FlowPane formPane = new FlowPane();
        formPane.setHgap(5);
        formPane.setVgap(5);
        formPane.getStyleClass().add("box-pane");
        formPane.setPadding(new Insets(10));

        DropShadow shadow = new DropShadow();
        shadow.setRadius(19.5);
        shadow.setOffsetX(3);
        shadow.setOffsetY(2);
        shadow.setColor(Color.rgb(211, 211, 211));
        formPane.setEffect(shadow);

        // ---- Mã kệ ----
        VBox vbMaKe = new VBox(5);
        Text lbMaKe = new Text("Mã kệ:");
        lbMaKe.setFill(Color.web("#374151"));
        lbMaKe.setFont(Font.font(13));

        tfMaKe = new TextField();
        tfMaKe.setPrefSize(200, 40);
        tfMaKe.setPromptText("Nhập mã kệ");

        vbMaKe.getChildren().addAll(lbMaKe, tfMaKe);

        // ---- Tên kệ ----
        VBox vbTenKe = new VBox(5);
        Text lbTenKe = new Text("Tên kệ:");
        lbTenKe.setFill(Color.web("#374151"));
        lbTenKe.setFont(Font.font(13));

        tfTenKe = new TextField();
        tfTenKe.setPrefSize(200, 40);
        tfTenKe.setPromptText("Nhập tên kệ");

        vbTenKe.getChildren().addAll(lbTenKe, tfTenKe);

        // ---- Loại kệ ----
        VBox vbLoaiKe = new VBox(5);
        Text lbLoaiKe = new Text("Loại kệ:");
        lbLoaiKe.setFill(Color.web("#374151"));
        lbLoaiKe.setFont(Font.font(13));

        tfLoaiKe = new TextField();
        tfLoaiKe.setPrefSize(200, 40);
        tfLoaiKe.setPromptText("Nhập loại kệ");

        vbLoaiKe.getChildren().addAll(lbLoaiKe, tfLoaiKe);

        // ---- Button Thêm ----
        VBox vbEdit = new VBox(5);
        Text lbEmpty = new Text("");
        btnSuaKe = new Button("Sửa kệ");
        btnSuaKe.setPrefSize(93, 40);
        btnSuaKe.getStyleClass().add("btn-sua");
        btnSuaKe.setTextFill(Color.WHITE);

        FontAwesomeIcon iconEdit = new FontAwesomeIcon();
        iconEdit.setIcon(FontAwesomeIcons.EDIT);
        iconEdit.setFill(Color.WHITE);
        btnSuaKe.setGraphic(iconEdit);

        vbEdit.getChildren().addAll(lbEmpty,btnSuaKe);

        // ---- Button Xoá ----
        VBox vbRemove = new VBox(5);
        Text lbEmpty1 = new Text("");
        btnXoaKe = new Button("Xoá kệ");
        btnXoaKe.setPrefSize(93, 40);
        btnXoaKe.getStyleClass().add("btn-xoa");
        btnXoaKe.setTextFill(Color.WHITE);

        vbRemove.getChildren().addAll(lbEmpty1,btnXoaKe);

        // ---- Button Xoá ----
        VBox vbRestore = new VBox(5);
        Text lbEmpty2 = new Text("");
        btnKhoiPhucKe = new Button("Khôi phục");
        btnKhoiPhucKe.setPrefSize(93, 40);
        btnKhoiPhucKe.getStyleClass().add("btn-khoiphuc-mini");
        btnKhoiPhucKe.setTextFill(Color.WHITE);

        FontAwesomeIcon iconRemove = new FontAwesomeIcon();
        iconRemove.setIcon(FontAwesomeIcons.BACKWARD);
        iconRemove.setFill(Color.WHITE);
        btnKhoiPhucKe.setGraphic(iconRemove);

        vbRestore.getChildren().addAll(lbEmpty2,btnKhoiPhucKe);

        formPane.getChildren().addAll(vbMaKe, vbTenKe, vbLoaiKe, vbEdit, vbRemove, vbRestore);


        // ============================
        //       TABLE VIEW
        // ============================
        tbKeThuoc = new TableView<>();
        tbKeThuoc.setPrefHeight(800);
        tbKeThuoc.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);


        // ============================
        //       ADD TO ROOT
        // ============================

        root.getChildren().addAll(titleBox, formPane, tbKeThuoc);

        this.getStylesheets().add(getClass().getResource("/com/antam/app/styles/dashboard_style.css").toExternalForm());
        this.setContent(root);
        /** Sự kiện **/

        dsKeThuoc =  ke_DAO.getTatCaKeThuoc();
        data.setAll(dsKeThuoc);
        tbKeThuoc.setItems(data);

        loadDanhSachKeThuoc();

        tfMaKe.setEditable(false);

        tbKeThuoc.setOnMouseClicked(e -> {
            KeDTO keDTO = tbKeThuoc.getSelectionModel().getSelectedItem();
            if (keDTO == null) {
                return;
            }
            tfMaKe.setText(keDTO.getMaKe());
            tfTenKe.setText(keDTO.getTenKe());
            tfLoaiKe.setText(keDTO.getLoaiKe());
        });

        btnSuaKe.setOnAction(e -> {
            if (kiemTraHopLe()) {
                ke_DAO.suaKe(new KeDTO(tfMaKe.getText(), tfTenKe.getText(), tfLoaiKe.getText(), false));
                showCanhBao("Thông báo", "Cập nhật kệ thành công!");
                //Cập nhật lại bảng
                dsKeThuoc = ke_DAO.getTatCaKeThuoc();
                data.setAll(dsKeThuoc);
                tbKeThuoc.setItems(data);
                //Xoá trắng các trường nhập liệu
                tfMaKe.clear();
                tfTenKe.clear();
                tfLoaiKe.clear();
            }
        });

        btnXoaKe.setOnAction(e -> {
            if(!tfMaKe.getText().isEmpty()){
                KeDTO selected = tbKeThuoc.getSelectionModel().getSelectedItem();
                if (selected == null) return;
                if (!selected.isDeleteAt()) {
                    ke_DAO.xoaKe(tfMaKe.getText());
                    showCanhBao("Thông báo", "Xoá kệ thành công!");
                    //Cập nhật lại bảng
                    dsKeThuoc = ke_DAO.getTatCaKeThuoc();
                    data.setAll(dsKeThuoc);
                    tbKeThuoc.setItems(data);
                    //Xoá trắng các trường nhập liệu
                    tfMaKe.clear();
                    tfTenKe.clear();
                    tfLoaiKe.clear();
                }else{
                    showCanhBao("Thông báo","Kệ đang bị xoá!");
                }
            }else{
                showCanhBao("Lỗi xoá kệ","Vui lòng chọn kệ cần xoá!");
            }
        });

        btnKhoiPhucKe.setOnAction(e -> {
            if(!tfMaKe.getText().isEmpty()){
                KeDTO selected = tbKeThuoc.getSelectionModel().getSelectedItem();
                if (selected == null) return;
                if (selected.isDeleteAt()){
                    ke_DAO.khoiPhucKe(tfMaKe.getText());
                    showCanhBao("Thông báo","Khôi phục kệ thành công!");
                    //Cập nhật lại bảng
                    dsKeThuoc =  ke_DAO.getTatCaKeThuoc();
                    data.setAll(dsKeThuoc);
                    tbKeThuoc.setItems(data);
                    //Xoá trắng các trường nhập liệu
                    tfMaKe.clear();
                    tfTenKe.clear();
                    tfLoaiKe.clear();
                }else {
                    showCanhBao("Thông báo","Kệ đang hoạt động!");
                }
            }else{
                showCanhBao("Lỗi khôi phục kệ","Vui lòng chọn kệ cần khôi phục!");
            }
        });
    }

    public void loadDanhSachKeThuoc(){

        /* Tên cột */
        TableColumn<KeDTO, String> colMaKe = new TableColumn<>("Mã Kệ");
        colMaKe.setCellValueFactory(new PropertyValueFactory<>("MaKe"));

        TableColumn<KeDTO, String> colTenKe = new TableColumn<>("Tên Kệ");
        colTenKe.setCellValueFactory(new PropertyValueFactory<>("tenKe"));

        TableColumn<KeDTO, Date> colLoaiKe = new TableColumn<>("Loại Kệ");
        colLoaiKe.setCellValueFactory(new PropertyValueFactory<>("LoaiKe"));

        TableColumn<KeDTO, String> colTrangThai = new TableColumn<>("Trạng Thái");
        colTrangThai.setCellValueFactory(cellData -> {
            KeDTO keDTO = cellData.getValue();
            return new SimpleStringProperty(keDTO.isDeleteAt() ? "Đã xoá" : "Hoạt động");
        });

        tbKeThuoc.getColumns().addAll(colMaKe, colTenKe, colLoaiKe, colTrangThai);
    }

    public boolean kiemTraHopLe(){
        String maKe = tfMaKe.getText();
        String tenKe = tfTenKe.getText();
        String loaiKe = tfLoaiKe.getText();

        if (tenKe.isEmpty()){
            showCanhBao("Lỗi nhập liệu","Vui lòng nhập tên kệ!");
            tfTenKe.requestFocus();
            return false;
        }

        if (loaiKe.isEmpty()){
            showCanhBao("Lỗi nhập liệu","Vui lòng nhập loại kệ!");
            tfLoaiKe.requestFocus();
            return false;
        }

        if (ke_DAO.isTenKeTrung(tenKe, maKe)){
            showCanhBao("Lỗi nhập liệu","Tên kệ đã tồn tại, vui lòng nhập tên khác!");
            tfTenKe.requestFocus();
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

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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.util.ArrayList;
import java.util.Date;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class ThemKeThuocController extends ScrollPane{

    private TableView<KeDTO> tbKeThuoc;
    private TextField tfMaKe, tfTenKe, tfLoaiKe;
    private Button btnThemKe;
    private ClientManager ke_DAO = ClientManager.getInstance();

    /* Lấy dữ liệu từ DAO */
    private ArrayList<KeDTO> dsKeThuoc = new ArrayList<>();
    private ObservableList<KeDTO> data = FXCollections.observableArrayList();

    public ThemKeThuocController() {
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

        // ============================
        //        TITLE
        // ============================
        HBox titleBox = new HBox();
        titleBox.setAlignment(Pos.CENTER_LEFT);

        Text title = new Text("Thêm kệ thuốc");
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
        VBox vbAdd = new VBox(5);
        Text lbEmpty = new Text("");
        btnThemKe = new Button("Thêm kệ");
        btnThemKe.setPrefSize(93, 40);
        btnThemKe.getStyleClass().add("btn-xoarong");
        btnThemKe.setTextFill(Color.WHITE);

        FontAwesomeIcon icon = new FontAwesomeIcon();
        icon.setIcon(FontAwesomeIcons.PLUS);
        icon.setFill(Color.WHITE);
        btnThemKe.setGraphic(icon);

        vbAdd.getChildren().addAll(lbEmpty,btnThemKe);

        formPane.getChildren().addAll(vbMaKe, vbTenKe, vbLoaiKe, vbAdd);


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

        //Tạo mã kệ tự động khi mở form
        tfMaKe.setText(ke_DAO.taoMaKeTuDong());
        tfMaKe.setEditable(false);

        //Sự kiện click thêm
        btnThemKe.setOnAction(e ->{
            if (kiemTraHopLe()){
                ke_DAO.themKe(new KeDTO(tfMaKe.getText(), tfTenKe.getText(), tfLoaiKe.getText(), false));
                showCanhBao("Thêm kệ thành công","Bạn đã thêm kệ thuốc thành công!");
                //Cập nhật lại bảng
                dsKeThuoc =  ke_DAO.getTatCaKeThuoc();
                data.setAll(dsKeThuoc);
                tbKeThuoc.setItems(data);
                //Tạo mã kệ tự động mới
                tfMaKe.setText(ke_DAO.taoMaKeTuDong());
                //Xoá trắng các trường nhập liệu
                tfTenKe.clear();
                tfLoaiKe.clear();
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

        if (ke_DAO.getKeTheoName(tenKe) != null){
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

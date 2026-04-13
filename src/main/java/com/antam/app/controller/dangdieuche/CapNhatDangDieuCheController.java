//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.antam.app.controller.dangdieuche;

import com.antam.app.connect.ConnectDB;

import com.antam.app.dao.impl.DangDieuChe_DAO;
import com.antam.app.entity.DangDieuChe;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcons;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;


public class CapNhatDangDieuCheController extends ScrollPane{

    private TableView<DangDieuChe> tbDangDieuChe = new TableView<>();
    private TextField tfMaDangDieuChe, tfTenDangDieuChe ;
    private Button btnCapNhat, btnXoa, btnKhoiPhuc;
    private DangDieuChe_DAO DangDieuChe_DAO = new DangDieuChe_DAO();

    /* Lấy dữ liệu từ DAO */
    private ArrayList<DangDieuChe> dsDangDieuCheThuoc;
    private ObservableList<DangDieuChe> data = FXCollections.observableArrayList();

    public CapNhatDangDieuCheController() {
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

        Text title = new Text("Cập nhật dạng điều chế");
        title.setFont(Font.font("System Bold", 30));
        title.setFill(Color.web("#1e3a8a"));

        Pane spacer = new Pane();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        titleBox.getChildren().addAll(title, spacer);

        // ============= FORM PANE =============
        FlowPane formPane = new FlowPane();
        formPane.setHgap(5);
        formPane.setVgap(5);

        formPane.getStyleClass().add("box-pane");

        formPane.setPadding(new Insets(10));

        DropShadow shadow = new DropShadow();
        shadow.setBlurType(javafx.scene.effect.BlurType.GAUSSIAN);
        shadow.setRadius(19.5);
        shadow.setOffsetX(3);
        shadow.setOffsetY(2);
        shadow.setColor(Color.rgb(211, 211, 211));

        formPane.setEffect(shadow);

        // --- Mã dạng điều chế ---
        VBox colMa = new VBox(5);

        Text lblMa = new Text("Mã dạng điều chế");
        lblMa.setFont(Font.font(13));
        lblMa.setFill(Color.web("#374151"));

        tfMaDangDieuChe = new TextField();
        tfMaDangDieuChe.setPrefSize(200, 40);
        tfMaDangDieuChe.setPromptText("Nhập mã dạng điều chế");

        colMa.getChildren().addAll(lblMa, tfMaDangDieuChe);


        // --- Tên dạng điều chế ---
        VBox colTen = new VBox(5);

        Text lblTen = new Text("Tên dạng điều chế");
        lblTen.setFont(Font.font(13));
        lblTen.setFill(Color.web("#374151"));

        tfTenDangDieuChe = new TextField();
        tfTenDangDieuChe.setPrefSize(200, 40);
        tfTenDangDieuChe.setPromptText("Nhập tên dạng điều chế");

        colTen.getChildren().addAll(lblTen, tfTenDangDieuChe);


        // --- Button Cập nhật ---
        VBox colCapNhat = new VBox(5);
        colCapNhat.setAlignment(Pos.CENTER);

        btnCapNhat = new Button("Cập nhật");
        btnCapNhat.setPrefSize(102, 40);
        btnCapNhat.setTextFill(Color.WHITE);
        btnCapNhat.setStyle("-fx-background-color: #2563eb; -fx-background-radius: 5px;");

        FontAwesomeIcon iconRefresh = new FontAwesomeIcon();
        iconRefresh.setIcon(FontAwesomeIcons.REFRESH);
        iconRefresh.setFill(Color.WHITE);
        btnCapNhat.setGraphic(iconRefresh);

        colCapNhat.getChildren().addAll(new Text(), btnCapNhat);


        // --- Button Xoá ---
        VBox colXoa = new VBox(5);
        colXoa.setAlignment(Pos.CENTER);

        btnXoa = new Button("Xoá");
        btnXoa.setPrefSize(102, 40);
        btnXoa.setTextFill(Color.WHITE);
        btnXoa.getStyleClass().add("btn-xoa");

        FontAwesomeIcon iconTrash = new FontAwesomeIcon();
        iconTrash.setGlyphName("TRASH");
        iconTrash.setFill(Color.WHITE);
        btnXoa.setGraphic(iconTrash);

        colXoa.getChildren().addAll(new Text(), btnXoa);

        VBox colKhoiPhuc = new VBox(5);
        colXoa.setAlignment(Pos.CENTER);

        btnKhoiPhuc = new Button("Khôi phục");
        btnKhoiPhuc.setPrefSize(102, 40);
        btnKhoiPhuc.setTextFill(Color.WHITE);
        btnKhoiPhuc.getStyleClass().add("btn-khoiphuc-mini");

        FontAwesomeIcon iconRestore = new FontAwesomeIcon();
        iconRestore.setIcon(FontAwesomeIcons.BACKWARD);
        iconRestore.setFill(Color.WHITE);
        btnKhoiPhuc.setGraphic(iconRestore);

        colKhoiPhuc.getChildren().addAll(new Text(), btnKhoiPhuc);


        // Add all fields to formPane
        formPane.getChildren().addAll(colMa, colTen, colCapNhat, colXoa, colKhoiPhuc);

        // ============= TABLE =============
        tbDangDieuChe.setPrefHeight(800);
        tbDangDieuChe.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // ============= ADD TO ROOT =============
        root.getChildren().addAll(titleBox, formPane, tbDangDieuChe);

        this.getStylesheets().add(getClass().getResource("/com/antam/app/styles/dashboard_style.css").toExternalForm());
        this.setContent(root);
        /** Sự kiện **/
        try {
            Connection con = ConnectDB.getInstance().connect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        dsDangDieuCheThuoc =  DangDieuChe_DAO.getTatCaDangDieuChe();
        data.setAll(dsDangDieuCheThuoc);
        tbDangDieuChe.setItems(data);

        loadDanhSachDangDieuCheThuoc();

        tfMaDangDieuChe.setEditable(false);

        //Sự kiện khi chọn 1 hàng trong bảng
        tbDangDieuChe.setOnMouseClicked(e -> {
            DangDieuChe DangDieuChe = tbDangDieuChe.getSelectionModel().getSelectedItem();
            if (DangDieuChe == null) {
                return;
            }
            tfMaDangDieuChe.setText(String.valueOf(DangDieuChe.getMaDDC()));
            tfTenDangDieuChe.setText(DangDieuChe.getTenDDC());
        });

        //Sự kiện cho nút sửa dạng điều chế
        btnCapNhat.setOnAction(e -> {
            if (kiemTraHopLe()){
                DangDieuChe_DAO.suaDangDieuChe(new DangDieuChe(Integer.parseInt(tfMaDangDieuChe.getText()), tfTenDangDieuChe.getText()));
                showCanhBao("Thông báo","Cập nhật dạng điều chế thành công!");
                //Cập nhật lại bảng
                dsDangDieuCheThuoc =  DangDieuChe_DAO.getTatCaDangDieuChe();
                data.setAll(dsDangDieuCheThuoc);
                tbDangDieuChe.setItems(data);
                //Xoá trắng các trường nhập liệu
                tfMaDangDieuChe.clear();
                tfTenDangDieuChe.clear();
            }
        });

        btnXoa.setOnAction(e -> {
            if(!tfMaDangDieuChe.getText().isEmpty()){
                DangDieuChe selected = tbDangDieuChe.getSelectionModel().getSelectedItem();
                if (selected == null) return;
                if (!selected.isDeleteAt()) {
                    DangDieuChe_DAO.xoaDangDieuChe(Integer.parseInt(tfMaDangDieuChe.getText()));
                    showCanhBao("Thông báo","Xoá dạng điều chế thành công!");
                    //Cập nhật lại bảng
                    dsDangDieuCheThuoc =  DangDieuChe_DAO.getTatCaDangDieuChe();
                    data.setAll(dsDangDieuCheThuoc);
                    tbDangDieuChe.setItems(data);
                    //Xoá trắng các trường nhập liệu
                    tfMaDangDieuChe.clear();
                    tfTenDangDieuChe.clear();
                }else{
                    showCanhBao("Thông báo","Dạng điều chế đang bị xoá!");
                }
            }else{
                showCanhBao("Lỗi","Vui lòng chọn dạng điều chế cần xoá!");
            }
        });

        btnKhoiPhuc.setOnAction(e -> {
            if(!tfMaDangDieuChe.getText().isEmpty()){
                DangDieuChe selected = tbDangDieuChe.getSelectionModel().getSelectedItem();
                if (selected == null) return;
                if (selected.isDeleteAt()){
                    DangDieuChe_DAO.khoiPhucDangDieuChe(Integer.parseInt(tfMaDangDieuChe.getText()));
                    showCanhBao("Thông báo","Khôi phục dạng điều chế thành công!");
                    //Cập nhật lại bảng
                    dsDangDieuCheThuoc =  DangDieuChe_DAO.getTatCaDangDieuChe();
                    data.setAll(dsDangDieuCheThuoc);
                    tbDangDieuChe.setItems(data);
                    //Xoá trắng các trường nhập liệu
                    tfMaDangDieuChe.clear();
                    tfTenDangDieuChe.clear();
                }else{
                    showCanhBao("Thông báo","Dạng điều chế đang hoạt động!");
                }
            }else{
                showCanhBao("Lỗi","Vui lòng chọn dạng điều chế cần khôi phục!");
            }
        });
    }

    public void loadDanhSachDangDieuCheThuoc(){

        /* Tên cột */
        TableColumn<DangDieuChe, String> colMaDangDieuChe = new TableColumn<>("Mã Dạng Điều Chế");
        colMaDangDieuChe.setCellValueFactory(new PropertyValueFactory<>("MaDDC"));

        TableColumn<DangDieuChe, String> colTenDangDieuChe = new TableColumn<>("Tên Dạng Điều Chế");
        colTenDangDieuChe.setCellValueFactory(new PropertyValueFactory<>("TenDDC"));

        TableColumn<DangDieuChe, Boolean> colTrangThai = new TableColumn<>("Trạng Thái");
        colTrangThai.setCellValueFactory(new PropertyValueFactory<>("deleteAt"));
        colTrangThai.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    if (!item) {
                        setText("Hoạt Động");
                    } else {
                        setText("Đã Xoá");
                    }
                }
            }
        });

        tbDangDieuChe.getColumns().addAll(colMaDangDieuChe, colTenDangDieuChe, colTrangThai);
    }

    public boolean kiemTraHopLe(){
        String maDangDieuChe = tfMaDangDieuChe.getText();
        String tenDangDieuChe = tfTenDangDieuChe.getText();

        if (maDangDieuChe.isEmpty()){
            showCanhBao("Lỗi nhập liệu","Vui lòng nhập mã dạng điều chế");
            tfMaDangDieuChe.requestFocus();
            return false;
        }

        if (tenDangDieuChe.isEmpty()){
            showCanhBao("Lỗi nhập liệu","Vui lòng nhập tên dạng điều chế!");
            tfTenDangDieuChe.requestFocus();
            return false;
        }

        if (DangDieuChe_DAO.getDDCTheoName(tenDangDieuChe) != null){
            showCanhBao("Lỗi nhập liệu","Tên dạng điều chế đã tồn tại!");
            tfTenDangDieuChe.requestFocus();
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

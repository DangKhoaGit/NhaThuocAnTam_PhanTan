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
import javafx.concurrent.Task;
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

public class TimKeThuocController extends ScrollPane{

    private TableView<KeDTO> tbKeThuoc;
    private ComboBox<String> cbLuaChon;
    private Button btnTimKiem, btnXoaRong;
    private TextField tfTimKiem;
    private ClientManager clientManager = ClientManager.getInstance();

    /* Lấy dữ liệu từ DAO */
    private ArrayList<KeDTO> dsKeThuoc;
    private ObservableList<KeDTO> data = FXCollections.observableArrayList();

    public TimKeThuocController() {
        /** Giao diện **/
        this.setFitToHeight(true);
        this.setFitToWidth(true);
        this.setPrefSize(900, 730);
        AnchorPane.setTopAnchor(this, 0.0);
        AnchorPane.setBottomAnchor(this, 0.0);
        AnchorPane.setLeftAnchor(this, 0.0);
        AnchorPane.setRightAnchor(this, 0.0);

        VBox root = new VBox(30);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #f8fafc;");

        // ============================
        // HEADER
        // ============================
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);

        Text title = new Text("Tìm kệ thuốc");
        title.setFont(Font.font("System", 30));
        title.setFill(Color.web("#1e3a8a"));

        Pane spacer = new Pane();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        header.getChildren().addAll(title, spacer);

        // ============================
        // FLOWPANE TÌM KIẾM
        // ============================

        FlowPane searchPane = new FlowPane();
        searchPane.setHgap(5);
        searchPane.setVgap(5);
        searchPane.getStyleClass().add("box-pane");

        searchPane.setPadding(new Insets(10));

        DropShadow shadow = new DropShadow();
        shadow.setBlurType(javafx.scene.effect.BlurType.GAUSSIAN);
        shadow.setHeight(44.45);
        shadow.setWidth(35.66);
        shadow.setRadius(19.5275);
        shadow.setOffsetX(3);
        shadow.setOffsetY(2);
        shadow.setColor(Color.rgb(211, 211, 211));
        searchPane.setEffect(shadow);

        // --- TÌM KIẾM TEXTFIELD ---
        VBox boxTimKiem = new VBox(5);
        Text labelTimKiem = new Text("Tìm kiếm:");
        labelTimKiem.setFill(Color.web("#374151"));
        labelTimKiem.setFont(Font.font(13));

        tfTimKiem = new TextField();
        tfTimKiem.setPrefSize(300, 40);
        tfTimKiem.setPromptText("Tìm kiếm kệ thuốc...");
        tfTimKiem.setStyle("-fx-background-color: white; -fx-border-color: #e5e7eb; -fx-border-radius: 8px;");

        boxTimKiem.getChildren().addAll(labelTimKiem, tfTimKiem);

        // --- COMBO LỰA CHỌN ---
        VBox boxLuaChon = new VBox(5);
        Text labelLuaChon = new Text("Tìm theo:");
        labelLuaChon.setFill(Color.web("#374151"));
        labelLuaChon.setFont(Font.font(13));

        cbLuaChon = new ComboBox<>();
        cbLuaChon.setPrefSize(200, 40);
        cbLuaChon.setPromptText("Chọn trạng thái");
        cbLuaChon.getStyleClass().add("combo-box");

        boxLuaChon.getChildren().addAll(labelLuaChon, cbLuaChon);

        // --- BUTTON TÌM KIẾM ---
        VBox boxBtnTim = new VBox(5);
        boxBtnTim.getChildren().add(new Text("")); // giống FXML để lấy khoảng trống

        btnTimKiem = new Button();
        btnTimKiem.setPrefSize(50, 40);
        btnTimKiem.setStyle("-fx-background-color: #2563eb; -fx-background-radius: 5px;");

        FontAwesomeIcon iconSearch = new FontAwesomeIcon();
        iconSearch.setIcon(FontAwesomeIcons.SEARCH);
        iconSearch.setFill(Color.WHITE);
        btnTimKiem.setGraphic(iconSearch);

        boxBtnTim.getChildren().add(btnTimKiem);

        // --- BUTTON XÓA RỖNG ---
        VBox boxBtnXoa = new VBox(5);
        boxBtnXoa.getChildren().add(new Text(""));

        btnXoaRong = new Button("Xoá rỗng");
        btnXoaRong.setPrefSize(93, 40);
        btnXoaRong.getStyleClass().add("btn-xoarong");
        btnXoaRong.setTextFill(Color.WHITE);

        FontAwesomeIcon iconRefresh = new FontAwesomeIcon();
        iconRefresh.setGlyphName("REFRESH");
        iconRefresh.setFill(Color.WHITE);
        btnXoaRong.setGraphic(iconRefresh);

        boxBtnXoa.getChildren().add(btnXoaRong);

        // Add tất cả vào flowpane
        searchPane.getChildren().addAll(boxTimKiem, boxLuaChon, boxBtnTim, boxBtnXoa);

        // ============================
        // TABLEVIEW
        // ============================

        tbKeThuoc = new TableView<>();
        tbKeThuoc.setPrefHeight(800);
        tbKeThuoc.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // ============================

        root.getChildren().addAll(header, searchPane, tbKeThuoc);

        this.getStylesheets().add(getClass().getResource("/com/antam/app/styles/dashboard_style.css").toExternalForm());
        this.setContent(root);
        /** Sự kiện **/
        Task<ArrayList<KeDTO>> loadData =  new Task<ArrayList<KeDTO>>() {
            @Override
            protected ArrayList<KeDTO> call() throws Exception {
                return clientManager.getKeList();
            }
        };

        loadData.setOnSucceeded(e -> {
            dsKeThuoc =  loadData.getValue();
            data.setAll(dsKeThuoc);
            tbKeThuoc.setItems(data);
        });

        loadData.setOnFailed(e -> {

        });

        Thread thread = new Thread(loadData);
        thread.start();

        loadDanhSachKeThuoc();
        //ComboBox lựa chọn
        cbLuaChon.setPromptText("Lựa chọn");
        cbLuaChon.getItems().addAll("Tất cả", "Mã kệ", "Tên kệ", "Loại kệ");

        //Sự kiện nút tìm kiếm
        tfTimKiem.setOnKeyReleased(e -> {
            filterAndSearch();
        });
        btnTimKiem.setOnAction(e ->{
            filterAndSearch();
        });
        //Nút xoá rỗng
        btnXoaRong.setOnAction(e ->{
            tfTimKiem.clear();
            cbLuaChon.getSelectionModel().clearSelection();
            cbLuaChon.setPromptText("Lựa chọn");
            filterAndSearch();
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

    public void filterAndSearch() {
        String selectedOption = cbLuaChon.getValue();
        String searchText = tfTimKiem.getText().trim().toLowerCase();

        ObservableList<KeDTO> filteredData = FXCollections.observableArrayList();

        for (KeDTO keDTO : dsKeThuoc) {
            boolean match = false;

            if (selectedOption == null || selectedOption.equals("Tất cả")) {
                match = keDTO.getMaKe().toLowerCase().contains(searchText) ||
                        keDTO.getTenKe().toLowerCase().contains(searchText) ||
                        keDTO.getLoaiKe().toLowerCase().contains(searchText);
            } else {
                switch (selectedOption) {
                    case "Mã kệ":
                        match = keDTO.getMaKe().toLowerCase().contains(searchText);
                        break;
                    case "Tên kệ":
                        match = keDTO.getTenKe().toLowerCase().contains(searchText);
                        break;
                    case "Loại kệ":
                        match = keDTO.getLoaiKe().toLowerCase().contains(searchText);
                        break;
                }
            }

            if (match) {
                filteredData.add(keDTO);
            }
        }

        data.setAll(filteredData);
        tbKeThuoc.setItems(data);
    }


}

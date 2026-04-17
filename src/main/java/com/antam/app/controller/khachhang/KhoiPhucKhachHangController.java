/*
 * @ (#) TimKhachHangController.java   1.0 28/10/25
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */
package com.antam.app.controller.khachhang;

import com.antam.app.service.impl.HoaDon_Service;
import com.antam.app.service.impl.KhachHang_Service;
import com.antam.app.dto.KhachHangDTO;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcons;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.format.DateTimeFormatter;

/*
 * @description Controller for customer search functionality
 * @author: Tran Tuan Hung
 * @date: 28/10/25
 * @version: 1.0
 */
public class KhoiPhucKhachHangController extends ScrollPane {


    private Button btnKhoiPhuc;
    private TextField txtSearchEmployee;
    private Button btnSearchEmployee;
    private ComboBox<String> cbSoDonHang; // Số đơn hàng filter
    private ComboBox<String> cbTrangThai; // Loại khách hàng filter
    private ComboBox<String> cbTongChiTieu; // Tổng chi tiêu filter

    private TableView<KhachHangDTO> tableViewKhachHang;
    private TableColumn<KhachHangDTO, String> colMaKH = new TableColumn<>("Mã khách hàng");
    private TableColumn<KhachHangDTO, String> colTenKH = new TableColumn<>("Tên khách hàng");
    private TableColumn<KhachHangDTO, String> colSoDienThoai = new TableColumn<>("Số điện thoại");
    private TableColumn<KhachHangDTO, Integer> colSoDonHang = new TableColumn<>("Số đơn hàng");
    private TableColumn<KhachHangDTO, Double> colTongChiTieu = new TableColumn<>("Tổng chi tiêu");
    private TableColumn<KhachHangDTO, String> colDonHangGanNhat = new TableColumn<>("Đơn hàng gần nhất");
    private TableColumn<KhachHangDTO, String> colLoaiKhachHang = new TableColumn<>("Loại khách hàng");

    private ObservableList<KhachHangDTO> dsKhachHang;
    private ObservableList<KhachHangDTO> dsKhachHangGoc; // Để lưu danh sách gốc cho việc lọc

    private KhachHang_Service khachHangDAO;
    private HoaDon_Service hoaDonDAO;
    private DateTimeFormatter formatter;

    // Định dạng tiền tệ kiểu Việt Nam: 1.000đ, 10.000đ
    private static final DecimalFormat VND_FORMAT;
    static {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.');
        symbols.setDecimalSeparator(',');
        VND_FORMAT = new DecimalFormat("#,##0", symbols);
    }

    public KhoiPhucKhachHangController(){
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

        HBox header = new HBox();
        header.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        Text title = new Text("Khôi phục khách hàng");
        title.setFill(Color.web("#1e3a8a"));
        title.setFont(Font.font("System Bold", 30));
        Pane spacer = new Pane();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        FontAwesomeIcon iconRestore = new FontAwesomeIcon();
        iconRestore.setIcon(FontAwesomeIcons.BACKWARD);
        iconRestore.setFill(Color.WHITE);
        btnKhoiPhuc = new Button("Khôi phục");
        btnKhoiPhuc.getStyleClass().add("btn-khoiphuc");
        btnKhoiPhuc.setGraphic(iconRestore);
        header.getChildren().addAll(title, spacer, btnKhoiPhuc);

        // Filter pane
        FlowPane filterPane = new FlowPane(5,5);
        filterPane.getStyleClass().add("box-pane");
        filterPane.setPadding(new Insets(10));
        DropShadow ds = new DropShadow();
        ds.setBlurType(BlurType.GAUSSIAN);
        ds.setOffsetX(3);
        ds.setOffsetY(2);
        ds.setRadius(19.5275);
        ds.setColor(Color.rgb(211,211,211));
        filterPane.setEffect(ds);

        // ComboBoxes
        VBox vbSoDonHang = new VBox(5);
        Text lblSoDonHang = new Text("Số đơn hàng");
        lblSoDonHang.setFont(Font.font(13));
        lblSoDonHang.setFill(Color.web("#374151"));
        cbSoDonHang = new ComboBox<>();
        cbSoDonHang.setPrefSize(200, 40);
        cbSoDonHang.setPromptText("Chọn số đơn hàng");
        cbSoDonHang.getStyleClass().add("combo-box");
        vbSoDonHang.getChildren().addAll(lblSoDonHang, cbSoDonHang);

        VBox vbTrangThai = new VBox(5);
        Text lblTrangThai = new Text("Loại khách hàng");
        lblTrangThai.setFont(Font.font(13));
        lblTrangThai.setFill(Color.web("#374151"));
        cbTrangThai = new ComboBox<>();
        cbTrangThai.setPrefSize(200, 40);
        cbTrangThai.setPromptText("Chọn trạng thái");
        cbTrangThai.getStyleClass().add("combo-box");
        vbTrangThai.getChildren().addAll(lblTrangThai, cbTrangThai);

        VBox vbTongChiTieu = new VBox(5);
        Text lblTongChiTieu = new Text("Tổng chi tiêu");
        lblTongChiTieu.setFont(Font.font(13));
        lblTongChiTieu.setFill(Color.web("#374151"));
        cbTongChiTieu = new ComboBox<>();
        cbTongChiTieu.setPrefSize(200, 40);
        cbTongChiTieu.setPromptText("Chọn tổng chi tiêu");
        cbTongChiTieu.getStyleClass().add("combo-box");
        vbTongChiTieu.getChildren().addAll(lblTongChiTieu, cbTongChiTieu);

        filterPane.getChildren().addAll(vbSoDonHang, vbTrangThai, vbTongChiTieu);

        // Search bar
        HBox searchBox = new HBox(10);
        searchBox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        txtSearchEmployee = new TextField();
        txtSearchEmployee.setPrefSize(300,40);
        txtSearchEmployee.setPromptText("Tìm kiếm khách hàng...");
        txtSearchEmployee.setStyle("-fx-background-color: white; -fx-border-color: #e5e7eb; -fx-border-radius: 8px;");

        btnSearchEmployee = new Button();
        btnSearchEmployee.setPrefSize(50,40);
        btnSearchEmployee.setStyle("-fx-background-color: #2563eb; -fx-background-radius: 5px;");
        btnSearchEmployee.setTextFill(Color.WHITE);
        FontAwesomeIcon searchIcon = new FontAwesomeIcon();
        searchIcon.setIcon(FontAwesomeIcons.SEARCH);
        searchIcon.setFill(Color.WHITE);
        btnSearchEmployee.setGraphic(searchIcon);

        searchBox.getChildren().addAll(txtSearchEmployee, btnSearchEmployee);

        // TableView
        tableViewKhachHang = new TableView<>();
        tableViewKhachHang.setPrefSize(867, 400);

        tableViewKhachHang.getColumns().addAll(colMaKH, colTenKH, colSoDienThoai, colSoDonHang, colTongChiTieu, colDonHangGanNhat, colLoaiKhachHang);
        tableViewKhachHang.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Hint button
        Button hintBtn = new Button("Nhấn 2 lần chuột trái vào bảng để xem chi tiết");
        hintBtn.setMaxWidth(Double.MAX_VALUE);
        hintBtn.getStyleClass().add("pane-huongdan");
        hintBtn.setTextFill(Color.web("#2563eb"));
        hintBtn.setPadding(new Insets(10));
        FontAwesomeIcon infoIcon = new FontAwesomeIcon();
        infoIcon.setGlyphName("INFO");
        infoIcon.setFill(Color.web("#2563eb"));
        hintBtn.setGraphic(infoIcon);

        VBox tableBox = new VBox(tableViewKhachHang, hintBtn);

        // Assemble root
        root.getChildren().addAll(header, filterPane, searchBox, tableBox);

        this.getStylesheets().add(getClass().getResource("/com/antam/app/styles/dashboard_style.css").toExternalForm());
        this.setContent(root);
        /** Sự kiện **/
    }
}

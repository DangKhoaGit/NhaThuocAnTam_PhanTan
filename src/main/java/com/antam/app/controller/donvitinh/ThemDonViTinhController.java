//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.antam.app.controller.donvitinh;

import com.antam.app.network.ClientManager;
import com.antam.app.dto.DonViTinhDTO;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcons;
import javafx.beans.property.SimpleStringProperty;
import javafx.concurrent.Task;
import javafx.scene.control.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;

public class ThemDonViTinhController extends ScrollPane {

    private TextField txtMa,txtTen;
    private TableView<DonViTinhDTO> tableThuoc;
    private TableColumn<DonViTinhDTO, String> colMaThuoc, colTenThuoc,colTrangThai;
    private Button btnThem;

    ArrayList<DonViTinhDTO> listDVT ;
    ClientManager clientManager = ClientManager.getInstance();

    public ThemDonViTinhController() {
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

        // ========================= TITLE =========================
        HBox titleBox = new HBox();
        titleBox.setAlignment(Pos.CENTER_LEFT);

        Text title = new Text("Thêm đơn vị tính");
        title.setFont(Font.font("System Bold", 30));
        title.setFill(Color.web("#1e3a8a"));

        Pane spacer = new Pane();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        titleBox.getChildren().addAll(title, spacer);

        // ========================= INPUT PANE =========================
        FlowPane flow = new FlowPane();
        flow.setHgap(5);
        flow.setVgap(5);
        flow.getStyleClass().add("box-pane");
        flow.setPadding(new Insets(10));
        flow.setEffect(new DropShadow(19.5, 3, 2, Color.rgb(211, 211, 211)));

        // --- Mã đơn vị tính ---
        VBox boxMa = new VBox(5);
        Text lbMa = new Text("Mã đơn vị tính");
        lbMa.setFill(Color.web("#374151"));
        lbMa.setFont(Font.font(13));

        txtMa = new TextField();
        txtMa.setPromptText("Nhập mã đơn vị tính");
        txtMa.setPrefSize(200, 40);

        boxMa.getChildren().addAll(lbMa, txtMa);

        // --- Tên đơn vị tính ---
        VBox boxTen = new VBox(5);
        Text lbTen = new Text("Tên đơn vị tính");
        lbTen.setFill(Color.web("#374151"));
        lbTen.setFont(Font.font(13));

        txtTen = new TextField();
        txtTen.setPromptText("Nhập tên đơn vị tính");
        txtTen.setPrefSize(200, 40);

        boxTen.getChildren().addAll(lbTen, txtTen);

        // --- Button Thêm ---
        VBox boxBtn = new VBox(5);
        boxBtn.setAlignment(Pos.CENTER);

        btnThem = new Button("Thêm");
        btnThem.setPrefSize(76, 40);
        btnThem.setStyle("-fx-background-color: #2563eb; -fx-background-radius: 5px;");
        btnThem.setTextFill(Color.WHITE);

        FontAwesomeIcon iconPlus = new FontAwesomeIcon();
        iconPlus.setIcon(FontAwesomeIcons.PLUS);
        iconPlus.setFill(Color.WHITE);
        btnThem.setGraphic(iconPlus);

        boxBtn.getChildren().addAll(new Text(), btnThem);

        flow.getChildren().addAll(boxMa, boxTen, boxBtn);

        // ========================= TABLE =========================
        tableThuoc = new TableView<>();
        tableThuoc.setPrefHeight(800);

        colMaThuoc = new TableColumn<>("Mã đơn vị tính");
        colTenThuoc = new TableColumn<>("Tên đơn vị tính");
        colTrangThai = new TableColumn<>("Trạng thái");

        tableThuoc.getColumns().addAll(colMaThuoc, colTenThuoc,colTrangThai);

        tableThuoc.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // ========================= ADD ALL =========================
        root.getChildren().addAll(titleBox, flow, tableThuoc);

        this.getStylesheets().add(getClass().getResource("/com/antam/app/styles/dashboard_style.css").toExternalForm());
        this.setContent(root);

        /** Sự kiện **/

        btnThem.setOnAction(e -> {
            try {
                String ma = txtMa.getText();
                int maInt = Integer.parseInt(ma);
                String ten = txtTen.getText().trim();
                if (ten.isEmpty()) {
                    showAlert("Lỗi", "Vui lòng nhập tên đơn vị tính!");
                    return;
                }
                DonViTinhDTO donViTinhDTO = new DonViTinhDTO(maInt, ten, false);

                Task<Boolean> task = new Task<>() {
                    @Override
                    protected Boolean call() throws Exception {
                        return clientManager.createDonViTinh(donViTinhDTO);
                    }
                };

                task.setOnSucceeded(event -> {
                    boolean result = task.getValue();
                    if (result) {
                        showAlert("Thành công", "Đã thêm đơn vị tính thành công!");
                        loadNextMa();
                    } else {
                        showAlert("Thất bại", "Thêm đơn vị tính thất bại!");
                    }
                    loadTable();
                });

                task.setOnFailed(event -> showAlert("Lỗi", "Không thể thêm đơn vị tính!"));

                new Thread(task).start();
            } catch (Exception ex) {
                showAlert("Lỗi", "Dữ liệu không hợp lệ!");
            }
        });

        txtMa.setEditable(false);
        loadNextMa();

        loadTable();
        setupTable();
    }

    private void loadNextMa() {
        Task<Integer> task = new Task<>() {
            @Override
            protected Integer call() throws Exception {
                return clientManager.getMaxHashDonViTinh();
            }
        };
        task.setOnSucceeded(e -> {
            Integer max = task.getValue();
            int next = (max != null ? max : 0) + 1;
            txtMa.setText(String.valueOf(next));
        });
        new Thread(task).start();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void setupTable() {
        colMaThuoc.setCellValueFactory( e -> new SimpleStringProperty(String.valueOf(e.getValue().getMaDVT())));
        colTenThuoc.setCellValueFactory( e -> new SimpleStringProperty(e.getValue().getTenDVT()));
        colTrangThai.setCellValueFactory(e-> new SimpleStringProperty(e.getValue().isDelete()? "Đã xóa":"Đang hoạt động"));
    }

    private void loadTable() {
//            listDVT = donViTinh_dao.getTatCaDonViTinh();

            Task<List<DonViTinhDTO>> task = new Task<List<DonViTinhDTO>>() {
                @Override
                protected List<DonViTinhDTO> call() throws Exception {
                    return clientManager.getDonViTinhList();
                }
            };

            task.setOnSucceeded(event -> {
                List<DonViTinhDTO> result = task.getValue();
                listDVT = new ArrayList<>(result);
                // Loại bỏ các đơn vị đã xóa
                listDVT.removeIf(DonViTinhDTO::isDelete);

                // Cập nhật dữ liệu cho TableView
                tableThuoc.getItems().setAll(listDVT);
            });

           task.setOnFailed(event -> {});
           Thread thread = new Thread(task);
           thread.start();

    }
}

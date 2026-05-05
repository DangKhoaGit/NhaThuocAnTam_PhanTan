//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.antam.app.controller.dangdieuche;

import com.antam.app.network.ClientManager;
import com.antam.app.dto.DangDieuCheDTO;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcons;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class ThemDangDieuCheController extends ScrollPane{

    private TableView<DangDieuCheDTO> tbDangDieuChe;
    private TextField tfMaDangDieuChe, tfTenDangDieuChe;
    private Button btnThem;
    private ClientManager clientManager = ClientManager.getInstance();
    /* Lấy dữ liệu từ DAO */
    private ArrayList<DangDieuCheDTO> dsDangDieuChe;
    private ObservableList<DangDieuCheDTO> data = FXCollections.observableArrayList();

    public ThemDangDieuCheController() {
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

        // ============= TITLE =============
        HBox titleBox = new HBox();
        titleBox.setAlignment(Pos.CENTER_LEFT);

        Text title = new Text("Thêm dạng điều chế");
        title.setFill(Color.web("#1e3a8a"));
        title.setFont(Font.font("System Bold", 30));

        Pane spacer = new Pane();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        titleBox.getChildren().addAll(title, spacer);

        // ============= INPUT PANEL =============
        FlowPane formPane = new FlowPane();
        formPane.setHgap(5);
        formPane.setVgap(5);
        formPane.getStyleClass().add("box-pane");
        formPane.setPadding(new Insets(10));

        DropShadow shadow = new DropShadow();
        shadow.setBlurType(javafx.scene.effect.BlurType.GAUSSIAN);
        shadow.setOffsetX(3);
        shadow.setOffsetY(2);
        shadow.setRadius(19.5);
        shadow.setColor(Color.rgb(211, 211, 211));

        formPane.setEffect(shadow);

        // --- Mã dạng điều chế ---
        VBox colMa = new VBox(5);
        Text lblMa = new Text("Mã dạng điều chế:");
        lblMa.setFill(Color.web("#374151"));
        lblMa.setFont(Font.font(13));

        tfMaDangDieuChe = new TextField();
        tfMaDangDieuChe.setPrefSize(200, 40);
        tfMaDangDieuChe.setPromptText("Nhập mã dạng điều chế");

        colMa.getChildren().addAll(lblMa, tfMaDangDieuChe);


        // --- Tên dạng điều chế ---
        VBox colTen = new VBox(5);
        Text lblTen = new Text("Tên dạng điều chế:");
        lblTen.setFill(Color.web("#374151"));
        lblTen.setFont(Font.font(13));

        tfTenDangDieuChe = new TextField();
        tfTenDangDieuChe.setPrefSize(200, 40);
        tfTenDangDieuChe.setPromptText("Nhập tên dạng điều chế");

        colTen.getChildren().addAll(lblTen, tfTenDangDieuChe);

        // --- Button thêm ---
        VBox colBtn = new VBox(5);
        colBtn.setAlignment(Pos.CENTER);

        btnThem = new Button("Thêm");
        btnThem.setPrefSize(76, 40);
        btnThem.setStyle("-fx-background-color: #2563eb; -fx-background-radius: 5px;");
        btnThem.setTextFill(Color.WHITE);

        FontAwesomeIcon iconPlus = new FontAwesomeIcon();
        iconPlus.setIcon(FontAwesomeIcons.PLUS);
        iconPlus.setFill(Color.WHITE);
        btnThem.setGraphic(iconPlus);

        colBtn.getChildren().addAll(new Text(), btnThem);

        // Add to formPane
        formPane.getChildren().addAll(colMa, colTen, colBtn);

        // ============= TABLE =============
        tbDangDieuChe = new TableView<>();
        tbDangDieuChe.setPrefHeight(800);

        tbDangDieuChe.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // ============= ADD TO ROOT =============
        root.getChildren().addAll(titleBox, formPane, tbDangDieuChe);

        this.getStylesheets().add(getClass().getResource("/com/antam/app/styles/dashboard_style.css").toExternalForm());
        this.setContent(root);
        /** Sự kiện **/

        Task<List<DangDieuCheDTO>> loadDataTask = new Task<List<DangDieuCheDTO>>() {
            @Override
            protected List<DangDieuCheDTO> call() throws Exception {
                return clientManager.getDangDieuCheList();
            }
        };

        loadDataTask.setOnSucceeded(e -> {

            dsDangDieuChe =  new ArrayList<>(loadDataTask.getValue());
            data.setAll(dsDangDieuChe);
            tbDangDieuChe.setItems(data);
        });

//        loadDataTask.setOnFailed(e -> {
//            showCanhBao("Lỗi kết nối", "Không thể tải dữ liệu dạng điều chế từ server.");
//        });

        Thread thread = new Thread(loadDataTask);
        thread.setDaemon(true);
        thread.start();

        loadDanhSachDangDieuChe();

        //Tạo mã kệ tự động khi mở form
        Task<String> taskTaoMa = new Task<>() {
            @Override
            protected String call() throws Exception {
                return clientManager.taoMaDCCTuDong();
            }
        };
        taskTaoMa.setOnSucceeded(e -> {

            tfMaDangDieuChe.setText(taskTaoMa.getValue());
        });

        taskTaoMa.setOnFailed(e -> {
            showCanhBao("Lỗi kết nối", "Không thể tạo mã dạng điều chế tự động.");
        });
        Thread threadTaoMa = new Thread(taskTaoMa);
        threadTaoMa.setDaemon(true);
        threadTaoMa.start();

        tfMaDangDieuChe.setEditable(false);

        //Sự kiện click thêm
        btnThem.setOnAction(e ->{
            if (kiemTraHopLe()){
               Task<Boolean> taskThem = new Task<>() {
                   @Override
                   public Boolean call() throws Exception {
                       return clientManager.createDangDieuChe(new DangDieuCheDTO(Integer.parseInt(tfMaDangDieuChe.getText()), tfTenDangDieuChe.getText(), false));
                   }
               };
               taskThem.setOnSucceeded(evt -> {
                   boolean result = taskThem.getValue();
                   if (result) {
                       showCanhBao("Thêm dạng điều chế", "Thêm dạng điều chế thành công!");
                       //Cập nhật lại bảng
                       Task<List<DangDieuCheDTO>> reloadTask = new Task<List<DangDieuCheDTO>>() {
                           @Override
                           protected List<DangDieuCheDTO> call() throws Exception {
                               return clientManager.getDangDieuCheList();
                           }
                       };
                       reloadTask.setOnSucceeded(reloadEvt -> {
                           dsDangDieuChe = new ArrayList<>(reloadTask.getValue());
                           data.setAll(dsDangDieuChe);
                           tbDangDieuChe.setItems(data);
                           //Tạo mã kệ tự động mới
                           Task<String> newTaskTaoMa = new Task<>() {
                               @Override
                               protected String call() throws Exception {
                                   return clientManager.taoMaDCCTuDong();
                               }
                           };
                           newTaskTaoMa.setOnSucceeded(newEvt -> {
                               tfMaDangDieuChe.setText(newTaskTaoMa.getValue());
                           });
                           newTaskTaoMa.setOnFailed(newEvt -> {
                               showCanhBao("Lỗi kết nối", "Không thể tạo mã dạng điều chế tự động.");
                           });
                           Thread newThreadTaoMa = new Thread(newTaskTaoMa);
                           newThreadTaoMa.setDaemon(true);
                           newThreadTaoMa.start();
                           //Xoá trắng các trường nhập liệu
                           tfTenDangDieuChe.clear();
                       });
                       Thread reloadThread = new Thread(reloadTask);
                       reloadThread.setDaemon(true);
                       reloadThread.start();
                   } else {
                       showCanhBao("Lỗi", "Thêm dạng điều chế thất bại. Vui lòng thử lại.");
                   };
               });
               taskThem.setOnFailed(evt -> {
                   showCanhBao("Lỗi kết nối", "Không thể thêm dạng điều chế mới. Vui lòng thử lại.");
               });

               Thread threadThem = new Thread(taskThem);
               threadThem.setDaemon(true);
               threadThem.start();
            }
        });
    }


    public void loadDanhSachDangDieuChe(){

        /* Tên cột */
        TableColumn<DangDieuCheDTO, String> colMaDangDieuChe = new TableColumn<>("Mã Dạng Điều Chế");
        colMaDangDieuChe.setCellValueFactory(new PropertyValueFactory<>("MaDDC"));

        TableColumn<DangDieuCheDTO, String> colTenDangDieuChe = new TableColumn<>("Tên Dạng Điều Chế");
        colTenDangDieuChe.setCellValueFactory(new PropertyValueFactory<>("TenDDC"));

        TableColumn<DangDieuCheDTO, Boolean> colTrangThai = new TableColumn<>("Trạng Thái");
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
        String tenDDC = tfTenDangDieuChe.getText();

        if (tenDDC == null || tenDDC.isBlank()){
            showCanhBao("Lỗi nhập liệu","Vui lòng nhập tên dạng điều chế!");
            tfTenDangDieuChe.requestFocus();
            return false;
        }

        DangDieuCheDTO existing = clientManager.getDDCTheoName(tenDDC);
        if (existing != null){
            showCanhBao("Lỗi nhập liệu","Dạng điều chế đã tồn tại trong hệ thống!");
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

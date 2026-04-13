package com.antam.app.controller.khungchinh;

import com.antam.app.controller.caidattaikhoan.CaiDatTaiKhoanController;
import com.antam.app.controller.dangdieuche.CapNhatDangDieuCheController;
import com.antam.app.controller.dangdieuche.ThemDangDieuCheController;
import com.antam.app.controller.dangnhap.DangNhapController;
import com.antam.app.controller.donvitinh.CapNhatDonViTinhController;
import com.antam.app.controller.donvitinh.ThemDonViTinhController;
import com.antam.app.controller.hoadon.CapNhatHoaDonController;
import com.antam.app.controller.hoadon.ThemHoaDonController;
import com.antam.app.controller.hoadon.ThongKeDoanhThuController;
import com.antam.app.controller.hoadon.TimHoaDonController;
import com.antam.app.controller.kethuoc.CapNhatKeThuocController;
import com.antam.app.controller.kethuoc.ThemKeThuocController;
import com.antam.app.controller.kethuoc.TimKeThuocController;
import com.antam.app.controller.khachhang.CapNhatKhachHangController;
import com.antam.app.controller.khachhang.KhoiPhucKhachHangController;
import com.antam.app.controller.khachhang.TimKhachHangController;
import com.antam.app.controller.khuyenmai.CapNhatKhuyenMaiController;
import com.antam.app.controller.khuyenmai.KhoiPhucKhuyenMaiController;
import com.antam.app.controller.khuyenmai.ThemKhuyenMaiController;
import com.antam.app.controller.khuyenmai.TimKhuyenMaiController;
import com.antam.app.controller.nhanvien.CapNhatNhanVienController;
import com.antam.app.controller.nhanvien.KhoiPhucNhanVienController;
import com.antam.app.controller.nhanvien.ThemNhanVienController;
import com.antam.app.controller.nhanvien.TimNhanVienController;
import com.antam.app.controller.phieudat.CapNhatPhieuDatController;
import com.antam.app.controller.phieudat.KhoiPhucPhieuDatController;
import com.antam.app.controller.phieudat.ThemPhieuDatController;
import com.antam.app.controller.phieudat.TimPhieuDatController;
import com.antam.app.controller.phieunhap.CapNhatPhieuNhapController;
import com.antam.app.controller.phieunhap.KhoiPhucPhieuNhapController;
import com.antam.app.controller.phieunhap.ThemPhieuNhapController;
import com.antam.app.controller.phieunhap.TimPhieuNhapController;
import com.antam.app.controller.thuoc.CapNhatThuocController;
import com.antam.app.controller.thuoc.KhoiPhucThuocController;
import com.antam.app.controller.thuoc.ThemThuocController;
import com.antam.app.controller.thuoc.TimThuocController;
import com.antam.app.controller.trangchinh.ThongKeTrangChinhController;
import com.antam.app.entity.NhanVien;
import com.antam.app.entity.PhienNguoiDung;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcons;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class KhungChinhController extends BorderPane {

    private Button btnTrangChinh, btnHoaDon, btnPhieuDat, btnThuoc, btnKeThuoc, btnDangDieuChe,
            btnDonViTinh, btnKhuyenMai, btnPhieuNhap, btnKhachHang, btnNhanVien, btnCaiDat, btnDangXuat;

    private VBox subMenuHoaDon, subMenuPhieuDat, subMenuThuoc, subMenuKeThuoc, subMenuDangDieuChe,
            subMenuDonViTinh, subMenuKhuyenMai, subMenuPhieuNhap, subMenuKhachHang, subMenuNhanVien, subMenuCaiDat;

    private StackPane paneContent = new StackPane();

    public KhungChinhController() {
        /** Giao diện **/
        HBox topMenu = new HBox();
        topMenu.setAlignment(Pos.CENTER);
        topMenu.setMinHeight(70);;
        topMenu.setSpacing(5);
        topMenu.getStyleClass().add("top-page");

        ImageView logo = new ImageView(
                new Image(getClass().getResource("/com/antam/app/images/logo.png").toExternalForm())
        );
        logo.setFitWidth(55);
        logo.setPreserveRatio(true);

        VBox titleBox = new VBox();
        titleBox.setAlignment(Pos.TOP_CENTER);
        titleBox.setPrefWidth(200);

        Text txt1 = new Text("NHÀ THUỐC AN TÂM");
        txt1.setFill(Color.web("#1e3a8a"));
        txt1.setFont(Font.font("System", FontWeight.BOLD, 20));

        Text txt2 = new Text("HỆ THỐNG QUẢN LÝ NHÀ THUỐC");
        txt2.setFill(Color.web("#1e3a8a"));
        txt2.setFont(Font.font(15));

        titleBox.getChildren().addAll(txt1, txt2);

        Region region = new Region();
        HBox.setHgrow(region, Priority.ALWAYS);

        FontAwesomeIcon iconLogout = new FontAwesomeIcon();
        iconLogout.setIcon(FontAwesomeIcons.SIGN_OUT);
        iconLogout.setFill(Color.WHITE);
        Button btnDangXuat = new Button("Đăng xuất");
        btnDangXuat.setGraphic(iconLogout);
        btnDangXuat.getStyleClass().add("btn-logout");

        topMenu.setPadding(new Insets(10, 10, 10, 10));

        DropShadow shadow = new DropShadow();
        shadow.setBlurType(BlurType.TWO_PASS_BOX);
        shadow.setRadius(12.93);
        shadow.setHeight(32.72);
        topMenu.setEffect(shadow);

        topMenu.getChildren().addAll(
                logo,
                titleBox,
                region,
                btnDangXuat
        );


        VBox leftMenu = new VBox();
        leftMenu.setSpacing(5);
        leftMenu.setPrefWidth(260);

        ScrollPane scroll = new ScrollPane(leftMenu);
        scroll.setFitToWidth(true);
        // ensure left menu scroll does not force center content size and allows proper layout
        scroll.setFitToHeight(true);


        btnTrangChinh = createMainButton(FontAwesomeIcons.valueOf("HOME"), "Trang chính");
        btnHoaDon = createMainButton(FontAwesomeIcons.valueOf("FILE"), "Hoá đơn");
        btnPhieuDat = createMainButton(FontAwesomeIcons.valueOf("FILE"), "Phiếu đặt");
        btnThuoc = createMainButton(FontAwesomeIcons.valueOf("MEDKIT"),"Thuốc");
        btnKeThuoc = createMainButton(FontAwesomeIcons.valueOf("ARCHIVE"),"Kệ thuốc");
        btnDangDieuChe = createMainButton(FontAwesomeIcons.valueOf("TINT"),"Dạng điều chế");
        btnDonViTinh = createMainButton(FontAwesomeIcons.valueOf("ASTERISK"),"Đơn vị tính");
        btnKhuyenMai = createMainButton(FontAwesomeIcons.valueOf("GIFT"),"Khuyến mãi");
        btnPhieuNhap = createMainButton(FontAwesomeIcons.valueOf("CLIPBOARD"),"Phiếu nhập");
        btnKhachHang = createMainButton(FontAwesomeIcons.valueOf("USERS"),"Khách hàng");
        btnNhanVien = createMainButton(FontAwesomeIcons.valueOf("USER"),"Nhân viên");
        btnCaiDat = createMainButton(FontAwesomeIcons.valueOf("COG"),"Cài đặt");

        subMenuHoaDon = createSubMenu("Tìm hoá đơn", "Thêm hoá đơn", "Cập nhật hoá đơn", "Thống kê doanh thu");
        subMenuPhieuDat = createSubMenu("Tìm phiếu đặt", "Thêm phiếu đặt", "Cập nhật phiếu đặt", "Khôi phục phiếu đặt");
        subMenuThuoc = createSubMenu("Tìm thuốc", "Thêm thuốc", "Cập nhật thuốc", "Khôi phục thuốc");
        subMenuKeThuoc = createSubMenu("Tìm kệ thuốc", "Thêm kệ thuốc", "Cập nhật kệ thuốc");
        subMenuDangDieuChe = createSubMenu("Thêm dạng điều chế", "Cập nhật dạng điều chế");
        subMenuDonViTinh = createSubMenu("Thêm đơn vị tính", "Cập nhật đơn vị tính");
        subMenuKhuyenMai = createSubMenu("Tìm khuyến mãi", "Thêm khuyến mãi", "Cập nhật khuyến mãi", "Khôi phục khuyến mãi");
        subMenuPhieuNhap = createSubMenu("Tìm phiếu nhập", "Thêm phiếu nhập", "Cập nhật phiếu nhập", "Khôi phục phiếu nhập");
        subMenuKhachHang = createSubMenu("Tìm khách hàng", "Cập nhật khách hàng");
        subMenuNhanVien = createSubMenu("Tìm nhân viên", "Thêm nhân viên", "Cập nhật nhân viên", "Khôi phục nhân viên");
        subMenuCaiDat = createSubMenu("Cài đặt tài khoản");

        hideAllSubMenus();

        btnTrangChinh.setOnMouseClicked(e -> loadView(btnTrangChinh.getText()));
        btnHoaDon.setOnMouseClicked(e -> toggleMenu(subMenuHoaDon));
        btnPhieuDat.setOnMouseClicked(e -> toggleMenu(subMenuPhieuDat));
        btnThuoc.setOnMouseClicked(e -> toggleMenu(subMenuThuoc));
        btnKeThuoc.setOnMouseClicked(e -> toggleMenu(subMenuKeThuoc));
        btnDangDieuChe.setOnMouseClicked(e -> toggleMenu(subMenuDangDieuChe));
        btnDonViTinh.setOnMouseClicked(e -> toggleMenu(subMenuDonViTinh));
        btnKhuyenMai.setOnMouseClicked(e -> toggleMenu(subMenuKhuyenMai));
        btnPhieuNhap.setOnMouseClicked(e -> toggleMenu(subMenuPhieuNhap));
        btnKhachHang.setOnMouseClicked(e -> toggleMenu(subMenuKhachHang));
        btnNhanVien.setOnMouseClicked(e -> toggleMenu(subMenuNhanVien));
        btnCaiDat.setOnMouseClicked(e -> toggleMenu(subMenuCaiDat));

        leftMenu.getChildren().addAll(
                btnTrangChinh,

                btnHoaDon, subMenuHoaDon,
                btnPhieuDat, subMenuPhieuDat,
                btnThuoc, subMenuThuoc,
                btnKeThuoc, subMenuKeThuoc,
                btnDangDieuChe, subMenuDangDieuChe,
                btnDonViTinh, subMenuDonViTinh,
                btnKhuyenMai, subMenuKhuyenMai,
                btnPhieuNhap, subMenuPhieuNhap,
                btnKhachHang, subMenuKhachHang,
                btnNhanVien, subMenuNhanVien,
                btnCaiDat, subMenuCaiDat
        );

        this.getStylesheets().addAll(getClass().getResource("/com/antam/app/styles/dashboard_style.css").toExternalForm());
        this.setTop(topMenu);
        this.setLeft(scroll);
        this.setCenter(paneContent);
        // allow center content to resize with the window
        paneContent.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        paneContent.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        this.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        this.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        loadView("Trang chính");

        // sự kiện đăng xuất
        btnDangXuat.setOnAction(e -> {
            // Xử lý đăng xuất. Nhảy ra màn hình đăng nhập
            if (canhBaoDangXuat()){
                PhienNguoiDung.setMaNV(null);

                Scene scene = new Scene(new DangNhapController());
                Stage stage = (Stage) this.getScene().getWindow();

                stage.setScene(scene);
                stage.setMaximized(true);
                stage.setWidth(Double.MAX_VALUE);
                stage.setHeight(Double.MAX_VALUE);
                stage.show();

//                System.out.println(PhienNguoiDung.getMaNV());
            }
        });

        // Phân quyền người dùng
        boolean isAdmin = PhienNguoiDung.getMaNV().isQuanLy();
        System.out.println(isAdmin);
        if (isAdmin){
            batFullQuyenNguoiDung();
        } else {
            btnNhanVien.setVisible(false);
            btnKeThuoc.setVisible(false);
            btnDangDieuChe.setVisible(false);
            btnDonViTinh.setVisible(false);

            // tắt chiếm chỗ UI
            btnNhanVien.setManaged(false);
            btnKeThuoc.setManaged(false);
            btnDangDieuChe .setManaged(false);
            btnDonViTinh.setManaged(false);
        }
    }

    private void batFullQuyenNguoiDung(){
        btnCaiDat.setVisible(true);
        btnTrangChinh.setVisible(true);
        btnHoaDon.setVisible(true);
        btnPhieuDat.setVisible(true);
        btnThuoc.setVisible(true);
        btnKeThuoc.setVisible(true);
        btnDangDieuChe.setVisible(true);
        btnDonViTinh.setVisible(true);
        btnKhuyenMai.setVisible(true);
        btnPhieuNhap.setVisible(true);
        btnKhachHang.setVisible(true);
        btnNhanVien.setVisible(true);
    }

    private boolean canhBaoDangXuat() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Xác nhận đăng xuất");
        alert.setHeaderText(null);
        alert.setContentText("Mọi tiến trình đang thực hiện sẽ bị huỷ bỏ.");

        ButtonType btnCo = new ButtonType("Có", ButtonBar.ButtonData.OK_DONE);
        ButtonType btnKhong = new ButtonType("Không", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(btnCo, btnKhong);

        return alert.showAndWait().orElse(btnKhong) == btnCo;
    }


    private Button createMainButton(FontAwesomeIcons iconName, String text) {
        FontAwesomeIcon icon = new FontAwesomeIcon();
        icon.setIcon(iconName);
        Button btn = new Button(text);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setAlignment(Pos.CENTER_LEFT);
        btn.setPadding(new Insets(10));
        btn.setGraphic(icon);
        btn.getStyleClass().add("sidebar-item");
        return btn;
    }

    private VBox createSubMenu(String... items) {
        VBox box = new VBox();
        box.setSpacing(2);
        box.setPadding(new Insets(0, 0, 0, 20));


        for (String name : items) {
            FontAwesomeIcon icon = new FontAwesomeIcon();
            icon.setIcon(FontAwesomeIcons.valueOf("BARS"));

            Button item = new Button(name);
            item.setGraphic(icon);
            item.setMaxWidth(Double.MAX_VALUE);
            item.getStyleClass().add("sidebar-sub-item");

            item.setOnAction(e -> loadView(name));
            box.getChildren().add(item);
        }
        return box;
    }

    private void hideAllSubMenus() {
        if (subMenuHoaDon != null) {
            subMenuHoaDon.setVisible(false);
            subMenuHoaDon.setManaged(false);
        }

        if (subMenuPhieuDat != null) {
            subMenuPhieuDat.setVisible(false);
            subMenuPhieuDat.setManaged(false);
        }

        if (subMenuThuoc != null) {
            subMenuThuoc.setVisible(false);
            subMenuThuoc.setManaged(false);
        }

        if (subMenuKeThuoc != null) {
            subMenuKeThuoc.setVisible(false);
            subMenuKeThuoc.setManaged(false);
        }

        if (subMenuDangDieuChe != null) {
            subMenuDangDieuChe.setVisible(false);
            subMenuDangDieuChe.setManaged(false);
        }

        if (subMenuDonViTinh != null) {
            subMenuDonViTinh.setVisible(false);
            subMenuDonViTinh.setManaged(false);
        }

        if (subMenuKhuyenMai != null) {
            subMenuKhuyenMai.setVisible(false);
            subMenuKhuyenMai.setManaged(false);
        }

        if (subMenuPhieuNhap != null) {
            subMenuPhieuNhap.setVisible(false);
            subMenuPhieuNhap.setManaged(false);
        }

        if (subMenuKhachHang != null) {
            subMenuKhachHang.setVisible(false);
            subMenuKhachHang.setManaged(false);
        }

        if (subMenuNhanVien != null) {
            subMenuNhanVien.setVisible(false);
            subMenuNhanVien.setManaged(false);
        }

        if (subMenuCaiDat != null) {
            subMenuCaiDat.setVisible(false);
            subMenuCaiDat.setManaged(false);
        }
    }

    private void toggleMenu(VBox menu) {
        boolean status = !menu.isVisible();
        hideAllSubMenus();
        menu.setVisible(status);
        menu.setManaged(status);
    }

    private void loadView(String name) {
        Node nodePageLoad = null;
        switch (name){
            case "Trang chính":
                nodePageLoad = new ThongKeTrangChinhController();
                break;
            case "Tìm hoá đơn":
                nodePageLoad = new TimHoaDonController();
                break;
            case "Thêm hoá đơn":
                nodePageLoad = new ThemHoaDonController();
                break;
            case "Cập nhật hoá đơn":
                nodePageLoad = new CapNhatHoaDonController();
                break;
            case "Thống kê doanh thu":
                nodePageLoad = new ThongKeDoanhThuController();
                break;
            case "Tìm phiếu đặt":
                nodePageLoad = new TimPhieuDatController();
                break;
            case "Thêm phiếu đặt":
                nodePageLoad = new ThemPhieuDatController();
                break;
            case "Cập nhật phiếu đặt":
                nodePageLoad = new CapNhatPhieuDatController();
                break;
            case "Khôi phục phiếu đặt":
                nodePageLoad = new KhoiPhucPhieuDatController();
                break;
            case "Tìm thuốc":
                nodePageLoad = new TimThuocController();
                break;
            case "Thêm thuốc":
                nodePageLoad = new ThemThuocController();
                break;
            case "Cập nhật thuốc":
                nodePageLoad = new CapNhatThuocController();
                break;
            case "Khôi phục thuốc":
                nodePageLoad = new KhoiPhucThuocController();
                break;
            case "Tìm kệ thuốc":
                nodePageLoad = new TimKeThuocController();
                break;
            case "Thêm kệ thuốc":
                nodePageLoad = new ThemKeThuocController();
                break;
            case "Cập nhật kệ thuốc":
                nodePageLoad = new CapNhatKeThuocController();
                break;
            case "Thêm dạng điều chế":
                nodePageLoad = new ThemDangDieuCheController();
                break;
            case "Cập nhật dạng điều chế":
                nodePageLoad = new CapNhatDangDieuCheController();
                break;
            case "Thêm đơn vị tính":
                nodePageLoad = new ThemDonViTinhController();
                break;
            case "Cập nhật đơn vị tính":
                nodePageLoad = new CapNhatDonViTinhController();
                break;
            case "Tìm khuyến mãi":
                nodePageLoad = new TimKhuyenMaiController();
                break;
            case "Thêm khuyến mãi":
                nodePageLoad = new ThemKhuyenMaiController();
                break;
            case "Cập nhật khuyến mãi":
                nodePageLoad = new CapNhatKhuyenMaiController();
                break;
            case "Khôi phục khuyến mãi":
                nodePageLoad = new KhoiPhucKhuyenMaiController();
                break;
            case "Tìm phiếu nhập":
                nodePageLoad = new TimPhieuNhapController();
                break;
            case "Thêm phiếu nhập":
                nodePageLoad = new ThemPhieuNhapController();
                break;
            case "Cập nhật phiếu nhập":
                nodePageLoad = new CapNhatPhieuNhapController();
                break;
            case "Khôi phục phiếu nhập":
                nodePageLoad = new KhoiPhucPhieuNhapController();
                break;
            case "Tìm khách hàng":
                nodePageLoad = new TimKhachHangController();
                break;
            case "Cập nhật khách hàng":
                nodePageLoad = new CapNhatKhachHangController();
                break;
            case "Khôi phục khách hàng":
                nodePageLoad = new KhoiPhucKhachHangController();
                break;
            case "Tìm nhân viên":
                nodePageLoad = new TimNhanVienController();
                break;
            case "Thêm nhân viên":
                nodePageLoad = new ThemNhanVienController();
                break;
            case "Cập nhật nhân viên":
                nodePageLoad = new CapNhatNhanVienController();
                break;
            case "Khôi phục nhân viên":
                nodePageLoad = new KhoiPhucNhanVienController();
                break;
            case "Cài đặt tài khoản":
                nodePageLoad = new CaiDatTaiKhoanController();
                break;
        }

        if (nodePageLoad != null) {
            paneContent.getChildren().clear();
            // If the loaded node is a Region, allow it to resize with paneContent
            if (nodePageLoad instanceof Region) {
                Region r = (Region) nodePageLoad;
                r.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                try { // unbind any previous bindings
                    r.prefWidthProperty().unbind();
                    r.prefHeightProperty().unbind();
                } catch (Exception ignored) {}
                r.prefWidthProperty().bind(paneContent.widthProperty());
                r.prefHeightProperty().bind(paneContent.heightProperty());
            }

            // ensure loaded node is aligned to top-left inside the StackPane
            StackPane.setAlignment(nodePageLoad, Pos.TOP_LEFT);

            paneContent.getChildren().setAll(nodePageLoad);
        }
    }
}

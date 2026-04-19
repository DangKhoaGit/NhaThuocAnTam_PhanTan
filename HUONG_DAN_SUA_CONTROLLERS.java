/*
 * @ (#) HoaDon_Controller_Example.java - HƯỚNG DẪN
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

/**
 * HƯỚNG DẪN: Cách sửa Controllers theo chuẩn luồng dữ liệu
 * 
 * Luồng dữ liệu chuẩn:
 * GHI: UI → DTO → Service → Entity → DAO → DB
 * ĐỌC: DB → DAO → Entity → Service → DTO → UI
 * 
 * VÍ DỤ TRƯỚC (KHÔNG ĐÚNG):
 * ========================================
 * public class ThemHoaDonController {
 *     private HoaDon_Service hoaDonService = new HoaDon_Service(); // ❌ Sai - gọi Service cũ
 *     
 *     private void themHoaDon() {
 *         // Lấy dữ liệu từ UI
 *         String maHD = txtMaHD.getText();
 *         double tongTien = Double.parseDouble(txtTongTien.getText());
 *         
 *         // ❌ Sai - tạo DTO nhưng không chuyển đổi đúng
 *         HoaDonDTO hdDTO = new HoaDonDTO(maHD);
 *         hdDTO.setTongTien(tongTien);
 *         
 *         // ❌ Sai - Service cũ gọi trực tiếp SQL
 *         hoaDonService.insertHoaDon(hdDTO);
 *     }
 * }
 * 
 * 
 * VÍ DỤ ĐÚNG (THEO CHUẨN MỚI):
 * ========================================
 * public class ThemHoaDonController extends ScrollPane {
 * 
 *     // ✅ Đúng - Dependency Injection của Service mới
 *     private final I_HoaDon_Service hoaDonService = new HoaDon_Service();
 *     private final I_ChiTietHoaDon_Service chiTietHoaDonService = new ChiTietHoaDon_Service();
 * 
 *     // UI Components
 *     private TextField txtMaHD, txtTongTien;
 *     private ComboBox<NhanVienDTO> cbNhanVien;
 *     private ComboBox<KhachHangDTO> cbKhachHang;
 *     private ComboBox<KhuyenMaiDTO> cbKhuyenMai;
 *     private Button btnThem;
 *     private TableView<ChiTietHoaDonDTO> tableChiTiet;
 *     
 *     public ThemHoaDonController() {
 *         // ... Xây dựng UI ...
 *         setupUI();
 *         setupEventHandlers();
 *     }
 *     
 *     private void setupUI() {
 *         // ... Khởi tạo các UI components ...
 *     }
 *     
 *     private void setupEventHandlers() {
 *         btnThem.setOnAction(e -> themHoaDon());
 *     }
 *     
 *     // ✅ ĐÚNG - Luồng GHI: UI → DTO → Service → Entity → DAO → DB
 *     private void themHoaDon() {
 *         try {
 *             // Step 1: Lấy dữ liệu từ UI
 *             String maHD = txtMaHD.getText().trim();
 *             double tongTien = Double.parseDouble(txtTongTien.getText());
 *             NhanVienDTO nhanVien = cbNhanVien.getValue();
 *             KhachHangDTO khachHang = cbKhachHang.getValue();
 *             KhuyenMaiDTO khuyenMai = cbKhuyenMai.getValue();
 *             
 *             // Step 2: Validation
 *             if (maHD.isEmpty() || tongTien < 0) {
 *                 showAlert("Lỗi", "Vui lòng nhập dữ liệu hợp lệ");
 *                 return;
 *             }
 *             
 *             // Step 3: Tạo DTO (Data Transfer Object)
 *             HoaDonDTO hoaDonDTO = HoaDonDTO.builder()
 *                     .MaHD(maHD)
 *                     .ngayTao(LocalDate.now())
 *                     .maNV(nhanVien)
 *                     .maKH(khachHang)
 *                     .maKM(khuyenMai)
 *                     .tongTien(tongTien)
 *                     .deleteAt(false)
 *                     .build();
 *             
 *             // Step 4: Gọi Service (Service sẽ xử lý logic business)
 *             // Service → sẽ convert DTO → Entity → gọi DAO
 *             boolean success = hoaDonService.insertHoaDon(hoaDonDTO);
 *             
 *             if (success) {
 *                 showAlert("Thành công", "Thêm hóa đơn thành công");
 *                 clearForm();
 *                 loadHoaDonList();
 *             } else {
 *                 showAlert("Lỗi", "Thêm hóa đơn thất bại");
 *             }
 *         } catch (NumberFormatException e) {
 *             showAlert("Lỗi", "Tổng tiền phải là số");
 *         } catch (Exception e) {
 *             showAlert("Lỗi", "Lỗi: " + e.getMessage());
 *             e.printStackTrace();
 *         }
 *     }
 *     
 *     // ✅ ĐÚNG - Luồng ĐỌC: DB → DAO → Entity → Service → DTO → UI
 *     private void loadHoaDonList() {
 *         try {
 *             // Step 1: Gọi Service để lấy dữ liệu
 *             // Service sẽ gọi DAO → DB lấy Entity → convert thành DTO
 *             ArrayList<HoaDonDTO> hoaDonList = hoaDonService.getAllHoaDon();
 *             
 *             // Step 2: Cập nhật UI (TableView)
 *             ObservableList<HoaDonDTO> observableList = FXCollections.observableArrayList(hoaDonList);
 *             tableHoaDon.setItems(observableList);
 *         } catch (Exception e) {
 *             showAlert("Lỗi", "Lỗi khi tải danh sách hóa đơn: " + e.getMessage());
 *             e.printStackTrace();
 *         }
 *     }
 *     
 *     // ✅ ĐÚNG - Cập nhật hóa đơn
 *     private void capNhatHoaDon(String maHD, double tongTienMoi) {
 *         try {
 *             // Gọi Service
 *             boolean success = hoaDonService.CapNhatTongTienHoaDon(maHD, tongTienMoi);
 *             if (success) {
 *                 showAlert("Thành công", "Cập nhật thành công");
 *                 loadHoaDonList();
 *             }
 *         } catch (Exception e) {
 *             showAlert("Lỗi", "Lỗi: " + e.getMessage());
 *         }
 *     }
 *     
 *     // ✅ ĐÚNG - Tìm kiếm hóa đơn
 *     private void timKiemHoaDon(String maHD) {
 *         try {
 *             // Luồng ĐỌC: Service → DAO → DB → Entity → Service → DTO → UI
 *             ArrayList<HoaDonDTO> result = hoaDonService.searchHoaDonByMaHd(maHD);
 *             ObservableList<HoaDonDTO> observableList = FXCollections.observableArrayList(result);
 *             tableHoaDon.setItems(observableList);
 *         } catch (Exception e) {
 *             showAlert("Lỗi", "Lỗi: " + e.getMessage());
 *         }
 *     }
 *     
 *     // ✅ ĐÚNG - Thêm chi tiết hóa đơn
 *     private void themChiTietHoaDon(String maHD, int maLoThuoc, int soLuong, double thanhTien) {
 *         try {
 *             HoaDonDTO hoaDonDTO = new HoaDonDTO(maHD);
 *             LoThuocDTO loThuocDTO = new LoThuocDTO();
 *             loThuocDTO.setMaLoThuoc(maLoThuoc);
 *             DonViTinhDTO dvt = new DonViTinhDTO("1"); // Mã DVT
 *             
 *             ChiTietHoaDonDTO cthdDTO = ChiTietHoaDonDTO.builder()
 *                     .MaHD(hoaDonDTO)
 *                     .maLoThuocDTO(loThuocDTO)
 *                     .soLuong(soLuong)
 *                     .maDVT(dvt)
 *                     .tinhTrang("Bán")
 *                     .thanhTien(thanhTien)
 *                     .build();
 *             
 *             boolean success = chiTietHoaDonService.themChiTietHoaDon(cthdDTO);
 *             if (success) {
 *                 showAlert("Thành công", "Thêm chi tiết hóa đơn thành công");
 *                 loadChiTietHoaDon(maHD);
 *             }
 *         } catch (Exception e) {
 *             showAlert("Lỗi", "Lỗi: " + e.getMessage());
 *         }
 *     }
 *     
 *     // ✅ ĐÚNG - Tải chi tiết hóa đơn
 *     private void loadChiTietHoaDon(String maHD) {
 *         try {
 *             List<ChiTietHoaDonDTO> list = chiTietHoaDonService.getChiTietByMaHD(maHD);
 *             ObservableList<ChiTietHoaDonDTO> observableList = FXCollections.observableArrayList(list);
 *             tableChiTiet.setItems(observableList);
 *         } catch (Exception e) {
 *             showAlert("Lỗi", "Lỗi: " + e.getMessage());
 *         }
 *     }
 *     
 *     private void clearForm() {
 *         txtMaHD.clear();
 *         txtTongTien.clear();
 *         cbNhanVien.setValue(null);
 *         cbKhachHang.setValue(null);
 *         cbKhuyenMai.setValue(null);
 *     }
 *     
 *     private void showAlert(String title, String message) {
 *         Alert alert = new Alert(Alert.AlertType.INFORMATION);
 *         alert.setTitle(title);
 *         alert.setContentText(message);
 *         alert.showAndWait();
 *     }
 * }
 * 
 * 
 * CHÚC ÍU TÓM TẮT:
 * ========================================
 * 1. Controllers chỉ gọi Service, không gọi DAO trực tiếp
 * 2. Controllers làm việc với DTO, không làm việc với Entity
 * 3. Service xử lý logic business và chuyển đổi DTO ↔ Entity
 * 4. DAO chỉ xử lý database operations và Entity
 * 5. Luôn catch Exception và hiển thị error message
 * 
 * LỚP PHÂN TÁN CỦA LUỒNG DỮ LIỆU:
 * ========================================
 * Controllers (UI) ← → Service (Business Logic) ← → DAO (Database Access)
 *     DTO                Entity                       Entity
 * 
 */

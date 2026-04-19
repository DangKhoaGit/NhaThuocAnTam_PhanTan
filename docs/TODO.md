# ❌ Những Task Cần Hoàn Thành

## Phase 2: Sửa Controllers ⚠️ (TODO)

### Tổng Quan
7 controllers trong `/controller/hoadon/` cần được refactor để:
1. Gọi Service thay vì DAO trực tiếp
2. Làm việc với DTO thay vì Entity
3. Áp dụng error handling (try-catch)
4. Sử dụng session pattern từ `PhienNguoiDungDTO`

---

## 📋 Danh Sách Controllers Cần Sửa

### 1. ❌ ThemHoaDonController.java
**Mục đích**: Tạo hóa đơn mới

**Vị trí**: `src/main/java/com/antam/app/controller/hoadon/ThemHoaDonController.java`

**Các thay đổi cần làm**:
- [ ] Đổi dependency: `HoaDon_DAO` → `HoaDon_Service`
- [ ] Đổi dependency: `ChiTietHoaDon_DAO` → `ChiTietHoaDon_Service`
- [ ] Đổi Entity input → DTO input (`HoaDon` → `HoaDonDTO`)
- [ ] Cập nhật method calls: từ DAO methods → Service methods
- [ ] Thêm try-catch error handling
- [ ] Áp dụng session: `PhienNguoiDungDTO.getMaNV()` cho maNV
- [ ] Cập nhật TableView/ListView để hiển thị `List<HoaDonDTO>`

**Key Methods to Update**:
- `themHoaDon()` - Gọi `hoaDonService.insertHoaDon(hoaDonDTO)`
- `loadHoaDonList()` - Gọi `hoaDonService.getAllHoaDon()`
- `thêmChiTiết...()` - Gọi `chiTietHoaDonService.themChiTietHoaDon(cthdDTO)`

---

### 2. ❌ CapNhatHoaDonController.java
**Mục đích**: Cập nhật hóa đơn

**Vị trí**: `src/main/java/com/antam/app/controller/hoadon/CapNhatHoaDonController.java`

**Các thay đổi cần làm**:
- [ ] Đổi dependency: `HoaDon_DAO` → `HoaDon_Service`
- [ ] Đổi Entity → DTO
- [ ] Thêm `hoaDonService.updateHoaDon(hoaDonDTO)`
- [ ] Thêm try-catch
- [ ] Cập nhật form binding để nhận DTO

**Key Methods to Update**:
- `capNhatHoaDon()` - Gọi `hoaDonService.updateHoaDon(hoaDonDTO)`
- `loadHoaDonToForm()` - Gọi `hoaDonService.getHoaDonTheoMa(maHD)` → DTO

---

### 3. ❌ TimHoaDonController.java
**Mục đích**: Tìm kiếm hóa đơn

**Vị trí**: `src/main/java/com/antam/app/controller/hoadon/TimHoaDonController.java`

**Các thay đổi cần làm**:
- [ ] Đổi dependency: `HoaDon_DAO` → `HoaDon_Service`
- [ ] Cập nhật `search()` methods gọi Service
- [ ] Cập nhật return type: Entity array → DTO ArrayList
- [ ] Thêm try-catch
- [ ] Cập nhật TableView binding

**Key Methods to Update**:
- `timKiemTheoMaHD()` - Gọi `hoaDonService.searchHoaDonByMaHd(maHD)`
- `timKiemTheoStatus()` - Gọi `hoaDonService.searchHoaDonByStatus(status)`
- `timKiemTheoMaNV()` - Gọi `hoaDonService.searchHoaDonByMaNV(maNV)`
- `timKiemTheoMaKH()` - Gọi `hoaDonService.getHoaDonByMaKH(maKH)`

---

### 4. ❌ ThongKeDoanhThuController.java
**Mục đích**: Thống kê doanh thu

**Vị trí**: `src/main/java/com/antam/app/controller/hoadon/ThongKeDoanhThuController.java`

**Các thay đổi cần làm**:
- [ ] Đổi dependency: `HoaDon_DAO` → `HoaDon_Service`
- [ ] Cập nhật `loadHoaDon()` gọi Service
- [ ] Đổi Entity → DTO trong Chart/TableView
- [ ] Thêm try-catch

**Key Methods to Update**:
- `loadHoaDonByStatus()` - Gọi `hoaDonService.searchHoaDonByStatus()`
- `loadHoaDonByMaNV()` - Gọi `hoaDonService.searchHoaDonByMaNV()`
- `calculateTongDoanh()` - Calculate từ `HoaDonDTO.tongTien`

---

### 5. ❌ TraThuocFormController.java
**Mục đích**: Form trả thuốc

**Vị trí**: `src/main/java/com/antam/app/controller/hoadon/TraThuocFormController.java`

**Các thay đổi cần làm**:
- [ ] Đổi dependency: `ChiTietHoaDon_DAO` → `ChiTietHoaDon_Service`
- [ ] Cập nhật `loadChiTiet()` - Gọi `chiTietHoaDonService.getAllChiTietHoaDonTheoMaHD()`
- [ ] Cập nhật `traThuoc()` - Gọi `chiTietHoaDonService.updateChiTietHoaDon()`
- [ ] Đổi Entity → DTO trong TableView
- [ ] Thêm try-catch

**Key Methods to Update**:
- `loadChiTietHoaDon(maHD)` - Gọi Service lấy ArrayList<ChiTietHoaDonDTO>
- `traThuoc(maHD, maLoThuoc)` - Update status via Service

---

### 6. ❌ DoiThuocFormController.java
**Mục đích**: Form đổi thuốc

**Vị trí**: `src/main/java/com/antam/app/controller/hoadon/DoiThuocFormController.java`

**Các thay đổi cần làm**:
- [ ] Đổi dependency: `ChiTietHoaDon_DAO` → `ChiTietHoaDon_Service`
- [ ] Cập nhật `loadChiTiet()` - Gọi Service
- [ ] Cập nhật `doiThuoc()` - Gọi `chiTietHoaDonService.themChiTietHoaDonTraKhiDoi()`
- [ ] Đổi Entity → DTO
- [ ] Thêm try-catch

**Key Methods to Update**:
- `loadChiTietHoaDon(maHD)` - Service call
- `doiThuoc(chiTietDTO)` - Gọi Service với DTO

---

### 7. ❌ XemChiTietHoaDonFormController.java
**Mục đích**: Xem chi tiết hóa đơn

**Vị trí**: `src/main/java/com/antam/app/controller/hoadon/XemChiTietHoaDonFormController.java`

**Các thay đổi cần làm**:
- [ ] Đổi dependency: `HoaDon_DAO` → `HoaDon_Service`
- [ ] Đổi dependency: `ChiTietHoaDon_DAO` → `ChiTietHoaDon_Service`
- [ ] Cập nhật `loadHoaDon()` - Gọi `hoaDonService.getHoaDonTheoMa()`
- [ ] Cập nhật `loadChiTiet()` - Gọi `chiTietHoaDonService.getAllChiTietHoaDonTheoMaHD()`
- [ ] Đổi Entity → DTO
- [ ] Thêm try-catch

---

## 🔧 Template Sửa Chung

### Thay Đổi Dependency
```java
// ❌ Cũ
private HoaDon_DAO hoaDonDAO = new HoaDon_DAO();
private ChiTietHoaDon_DAO chiTietHoaDonDAO = new ChiTietHoaDon_DAO();

// ✅ Mới
private final I_HoaDon_Service hoaDonService = new HoaDon_Service();
private final I_ChiTietHoaDon_Service chiTietHoaDonService = new ChiTietHoaDon_Service();
```

### Gọi Methods
```java
// ❌ Cũ - DAO trực tiếp
ArrayList<HoaDon> list = hoaDonDAO.getAllHoaDon();

// ✅ Mới - Service + convert DTO
ArrayList<HoaDonDTO> list = hoaDonService.getAllHoaDon();
```

### Error Handling
```java
// ✅ Thêm try-catch
try {
    ArrayList<HoaDonDTO> list = hoaDonService.getAllHoaDon();
    // ... xử lý ...
} catch (Exception e) {
    showAlert("Lỗi", "Lỗi: " + e.getMessage());
    e.printStackTrace();
}
```

### UI Binding
```java
// ❌ Cũ - Entity
ObservableList<HoaDon> observableList = FXCollections.observableArrayList(hoaDonList);
tableHoaDon.setItems(observableList);

// ✅ Mới - DTO
ObservableList<HoaDonDTO> observableList = FXCollections.observableArrayList(hoaDonDTOList);
tableHoaDon.setItems(observableList);
```

### Session Pattern
```java
// ✅ Lấy current staff từ session
NhanVienDTO currentStaff = PhienNguoiDungDTO.getNhanVienDangNhap();
String maNV = currentStaff.getMaNV();

// Sử dụng khi tạo hóa đơn
HoaDonDTO hdDTO = HoaDonDTO.builder()
        .MaHD(maHD)
        .maNV(currentStaff)  // Từ session
        .build();
```

---

## Phase 3: Testing & Compilation ⚠️ (TODO)

### Compilation
- [ ] Compile tất cả files: `mvn clean compile`
- [ ] Kiểm tra unresolved references
- [ ] Kiểm tra missing imports
- [ ] Verify DTO class structures

### Runtime Testing
- [ ] Test create HoaDon
- [ ] Test read HoaDon
- [ ] Test update HoaDon
- [ ] Test delete HoaDon
- [ ] Test create ChiTietHoaDon
- [ ] Test update ChiTietHoaDon
- [ ] Test "Trả Khi Đổi" validation
- [ ] Test relationships loading (NhanVien, KhachHang, KhuyenMai)

### Integration Testing
- [ ] Test login session integration
- [ ] Test Controllers calling Service
- [ ] Test UI updates after CRUD operations
- [ ] Test error handling (display error messages)

---

## 📊 Tiến Độ Hiện Tại

| Task | Status | Progress |
|------|--------|----------|
| DAO Layer | ✅ Complete | 100% |
| Service Layer | ✅ Complete | 100% |
| Documentation | ✅ Complete | 100% |
| **Controllers** | ❌ TODO | 0% |
| **Testing** | ❌ TODO | 0% |
| **Overall** | 🔄 In Progress | 60% |

---

## 🚀 Priority Order

1. **HIGH**: Controllers refactoring (7 files)
   - Impact: Controllers are visible to users
   - Dependency: Service/DAO already ready
   - Effort: Medium (each ~30-50 min)

2. **HIGH**: Compilation & Runtime Testing
   - Impact: Verify everything works
   - Dependency: All code done
   - Effort: Medium (testing each feature)

3. **MEDIUM**: Edge Case Testing
   - Impact: Ensure robustness
   - Effort: High (various scenarios)

---

## 📝 Ghi Chú

- Tất cả Services & DAOs đã hoàn tất, ready for use
- Controllers là "entry point", cần sửa để activate new architecture
- Session pattern (PhienNguoiDungDTO) sẽ giúp track who did what
- Error handling cần consistent across all Controllers

---

**Last Updated**: 19/04/2026  
**Estimated Time**: ~10-12 hours for all Controllers + testing

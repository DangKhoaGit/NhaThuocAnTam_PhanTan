# ❌ Những Task Cần Hoàn Thành

## 🚨 Phase 0: Critical Bug Fixes (BLOCKING) ⚡

**Status**: 🔴 Must fix before anything else  
**Timeline**: 1-2 days  
**Impact**: App cannot run without these fixes

### Critical Issue #1: SQL Server Driver → MariaDB
**Severity**: 🔴 CRITICAL  
**Effort**: 5 minutes  
**File**: `src/main/java/com/antam/app/connect/ConnectDB.java`

**Task**:
- [ ] Update line 29: `com.microsoft.sqlserver.jdbc.SQLServerDriver` → `org.mariadb.jdbc.Driver`
- [ ] Update line 30: `jdbc:sqlserver://localhost:1433` → `jdbc:mariadb://localhost:3306`
- [ ] Update line 31-32: credentials for MariaDB (root/123456)
- [ ] Test connection with Tester.java

**Details**: Application currently tries to connect to SQL Server but database is MariaDB. This causes immediate connection failure.

---

### Critical Issue #2: Connection Null Pointer (Auto-Fix)
**Severity**: 🔴 CRITICAL  
**Effort**: Auto-fix after Issue #1  
**Verification**: All DAO operations should work

**Task**:
- [ ] After fixing Issue #1, run any DAO operation
- [ ] Verify no "Connection is null" errors
- [ ] If still failing, debug ConnectDB reconnection logic

---

### Critical Issue #3: JDBC vs JPA Strategy Decision
**Severity**: 🔴 CRITICAL  
**Effort**: Decision + planning (30 min)  
**Impact**: Long-term architecture

**Decision Options**:

**Option A: Keep JDBC + Improve**
- Remove static singleton connection
- Add HikariCP connection pool
- Make thread-safe
- Estimated work: 4-6 hours
- Recommendation: Quick fix

**Option B: Migrate to Full JPA**
- Remove raw JDBC entirely
- Use JPA_Util + AbstractGenericDao
- Automatic relationship loading
- Transaction management
- Estimated work: 16-20 hours
- Recommendation: Better long-term

**Task**:
- [ ] Review both options in [AUDIT_REPORT.md](../AUDIT_REPORT.md)
- [ ] Decide based on timeline & team capabilities
- [ ] Plan next sprint accordingly
- [ ] Recommendation: Do Option A now, plan Option B for sprint 2

---

## Phase 1: High Priority Issues (1 Week)

After critical bugs fixed, handle these:

### Issue #4: Add Connection Pooling
**Effort**: 2-3 hours  
**File**: `ConnectDB.java`, `pom.xml`
- [ ] Add HikariCP dependency
- [ ] Refactor ConnectDB to use HikariDataSource
- [ ] Test with 10+ concurrent connections

### Issue #6: Add Dependency Injection
**Effort**: 3-4 hours  
**Files**: 18 Service implementations
- [ ] Change from `new HoaDon_DAO()` to constructor injection
- [ ] Update all Services (18 files)
- [ ] Enable unit testing with mocks

### Issue #7: Refactor I_KhachHang_Service
**Effort**: 1-2 hours  
**File**: `service/I_KhachHang_Service.java`
- [ ] Remove direct JDBC access
- [ ] Use DAO layer instead
- [ ] Fix layer abstraction violation

### Issue #8: Remove NhanVien_DAO Static Cache
**Effort**: 30 minutes  
**File**: `dao/impl/NhanVien_DAO.java`
- [ ] Remove static cache field
- [ ] Fetch fresh data each query
- [ ] Prevent memory leak

---

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
| **Critical Bugs** | 🔴 TODO | 0% |
| **High Priority Issues** | 🟠 TODO | 0% |
| **Controllers** | ❌ TODO | 0% |
| **Testing** | ❌ TODO | 0% |
| **Overall** | 🔄 In Progress | ~50% |

### Blocking Status
🔴 **CANNOT PROCEED** until Phase 0 (Critical Bugs) fixed
- Issue #1: Database connection broken
- Issue #2: All DAO operations fail
- Issue #3: Persistence strategy unclear

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

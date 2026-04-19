# 🏗️ Kiến Trúc Luồng Dữ Liệu HoaDon Module

## 📐 Tổng Quan Kiến Trúc

```
┌─────────────────────────────────────────────────────────────────────┐
│                        USER INTERFACE LAYER                         │
│                       (JavaFX Controllers)                          │
│                                                                     │
│  ThemHoaDonController, CapNhatHoaDonController, TimHoaDonController │
│                                                                     │
│                      Works with DTOs                               │
│                                                                     │
└────────────────┬────────────────────────────────────────────────────┘
                 │ DTO (HoaDonDTO, ChiTietHoaDonDTO)
                 ↓
┌─────────────────────────────────────────────────────────────────────┐
│                      BUSINESS LOGIC LAYER                           │
│                     (Service Implementations)                       │
│                                                                     │
│  HoaDon_Service (11 methods)                                        │
│  ChiTietHoaDon_Service (8 methods)                                  │
│                                                                     │
│  - Delegation to DAO                                               │
│  - DTO ↔ Entity Conversion                                         │
│  - Error Handling (try-catch)                                      │
│  - Business Logic (calculations, validations)                      │
│                                                                     │
└────────────────┬────────────────────────────────────────────────────┘
                 │ Entity (HoaDon, ChiTietHoaDon)
                 ↓
┌─────────────────────────────────────────────────────────────────────┐
│                    DATA ACCESS LAYER (DAO)                          │
│              (Database Operations & Persistence)                    │
│                                                                     │
│  HoaDon_DAO (12 methods)                                            │
│  ChiTietHoaDon_DAO (8 methods)                                      │
│                                                                     │
│  - Direct SQL/JDBC                                                 │
│  - ResultSet ↔ Entity Mapping                                      │
│  - Relationship Loading                                            │
│  - Connection Management                                           │
│                                                                     │
└────────────────┬────────────────────────────────────────────────────┘
                 │ SQL / JDBC
                 ↓
┌─────────────────────────────────────────────────────────────────────┐
│                         DATABASE LAYER                              │
│                          (MariaDB)                                  │
│                                                                     │
│  Tables: HoaDon, ChiTietHoaDon, NhanVien, KhachHang, etc.          │
│                                                                     │
└─────────────────────────────────────────────────────────────────────┘
```

---

## 📤 Luồng GHI: Tạo/Cập Nhật Dữ Liệu

### Step-by-Step Flow

```
1️⃣  USER INPUT (Controller)
    ↓
    Lấy dữ liệu từ TextField, ComboBox, DatePicker, ...
    
2️⃣  BUILD DTO (Controller → Service)
    ↓
    HoaDonDTO.builder()
        .MaHD(txtMaHD.getText())
        .ngayTao(dpNgayTao.getValue())
        .maNV(cbNhanVien.getValue())           // NhanVienDTO
        .maKH(cbKhachHang.getValue())          // KhachHangDTO
        .maKM(cbKhuyenMai.getValue())          // KhuyenMaiDTO
        .tongTien(Double.parseDouble(...))
        .build()
    
3️⃣  CALL SERVICE (Controller → Service)
    ↓
    hoaDonService.insertHoaDon(hoaDonDTO)
    
4️⃣  CONVERT DTO → ENTITY (Service)
    ↓
    Service.mapDTOToEntity(hoaDonDTO)
    {
        - Extract DTO fields
        - Load full Entity objects via DAO calls
        - Create Entity with relationships
    }
    
5️⃣  CALL DAO (Service → DAO)
    ↓
    hoaDonDAO.insertHoaDon(hoaDonEntity)
    
6️⃣  EXECUTE SQL (DAO → Database)
    ↓
    PreparedStatement: INSERT INTO HoaDon (...) VALUES (...)
    
7️⃣  RESULT (Database → User)
    ↓
    Success: Show alert "Thêm thành công"
    Failure: Show alert with error message
```

### Code Example: Tạo Hóa Đơn

```java
// Controller Layer
private void themHoaDon() {
    try {
        // 1️⃣ Get input
        String maHD = txtMaHD.getText();
        NhanVienDTO nhanVien = cbNhanVien.getValue();
        KhachHangDTO khachHang = cbKhachHang.getValue();
        double tongTien = Double.parseDouble(txtTongTien.getText());
        
        // 2️⃣ Build DTO
        HoaDonDTO hdDTO = HoaDonDTO.builder()
                .MaHD(maHD)
                .ngayTao(LocalDate.now())
                .maNV(nhanVien)
                .maKH(khachHang)
                .tongTien(tongTien)
                .build();
        
        // 3️⃣ Call Service
        boolean success = hoaDonService.insertHoaDon(hdDTO);
        
        // 7️⃣ Handle result
        if (success) {
            showAlert("Thành công", "Thêm hóa đơn thành công");
            loadHoaDonList();
        }
    } catch (Exception e) {
        showAlert("Lỗi", "Lỗi: " + e.getMessage());
    }
}

// Service Layer
public boolean insertHoaDon(HoaDonDTO hdDTO) {
    try {
        // 4️⃣ Convert DTO → Entity
        HoaDon hdEntity = mapDTOToEntity(hdDTO);
        
        // 5️⃣ Call DAO
        hoaDonDAO.insertHoaDon(hdEntity);
        return true;
    } catch (Exception e) {
        throw new RuntimeException("Lỗi thêm hóa đơn", e);
    }
}

private HoaDon mapDTOToEntity(HoaDonDTO dto) {
    if (dto == null) return null;
    
    HoaDon entity = new HoaDon();
    entity.setMaHD(dto.getMaHD());
    entity.setNgayTao(dto.getNgayTao());
    
    // Load related entities
    if (dto.getMaNV() != null) {
        NhanVien nv = nhanVienDAO.findNhanVienVoiMa(dto.getMaNV().getMaNV());
        entity.setMaNV(nv);
    }
    if (dto.getMaKH() != null) {
        KhachHang kh = khachHangDAO.getKhachHangTheoMa(dto.getMaKH().getMaKH());
        entity.setMaKH(kh);
    }
    
    return entity;
}

// DAO Layer
public void insertHoaDon(HoaDon hd) {
    try {
        Connection con = ConnectDB.getConnection();
        String sql = "INSERT INTO HoaDon (MaHD, NgayTao, MaNV, MaKH, TongTien) " +
                     "VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, hd.getMaHD());
            pstmt.setDate(2, java.sql.Date.valueOf(hd.getNgayTao()));
            pstmt.setString(3, hd.getMaNV().getMaNV());
            pstmt.setString(4, hd.getMaKH().getMaKH());
            pstmt.setDouble(5, hd.getTongTien());
            pstmt.executeUpdate();
        }
    } catch (Exception e) {
        throw new RuntimeException("Lỗi thêm hóa đơn vào DB", e);
    }
}
```

---

## 📥 Luồng ĐỌC: Lấy Dữ Liệu

### Step-by-Step Flow

```
1️⃣  DATABASE QUERY (DAO)
    ↓
    PreparedStatement: SELECT * FROM HoaDon
    
2️⃣  RESULT SET MAPPING (DAO)
    ↓
    ResultSet → Entity mapping
    mapResultSetToEntity(rs)
    {
        - Read MaHD, NgayTao, TongTien, ...
        - Call other DAOs to load relationships
        - Create Entity with full data
    }
    
3️⃣  RETURN TO SERVICE (DAO → Service)
    ↓
    ArrayList<HoaDon> entities
    
4️⃣  CONVERT ENTITY → DTO (Service)
    ↓
    Service.mapEntityToDTO(entity)
    {
        - Extract Entity fields
        - Create new DTOs for relationships
        - Return DTO with full data
    }
    
5️⃣  RETURN TO CONTROLLER (Service → Controller)
    ↓
    ArrayList<HoaDonDTO> dtos
    
6️⃣  UPDATE UI (Controller)
    ↓
    ObservableList<HoaDonDTO> observableList = FXCollections.observableArrayList(dtos)
    tableHoaDon.setItems(observableList)
    
7️⃣  DISPLAY TO USER (UI)
    ↓
    User thấy dữ liệu trong TableView
```

### Code Example: Lấy Danh Sách Hóa Đơn

```java
// Controller Layer
private void loadHoaDonList() {
    try {
        // 5️⃣ Call Service (which queries DB and converts)
        ArrayList<HoaDonDTO> hoaDonList = hoaDonService.getAllHoaDon();
        
        // 6️⃣ Update UI
        ObservableList<HoaDonDTO> observableList = 
            FXCollections.observableArrayList(hoaDonList);
        tableHoaDon.setItems(observableList);
        
        // 7️⃣ Display to user
        // TableView renders automatically
    } catch (Exception e) {
        showAlert("Lỗi", "Lỗi khi tải danh sách: " + e.getMessage());
    }
}

// Service Layer
public ArrayList<HoaDonDTO> getAllHoaDon() {
    ArrayList<HoaDonDTO> result = new ArrayList<>();
    try {
        // 3️⃣ Call DAO to get entities
        ArrayList<HoaDon> hoaDonList = hoaDonDAO.getAllHoaDon();
        
        // 4️⃣ Convert each Entity to DTO
        for (HoaDon hd : hoaDonList) {
            result.add(mapEntityToDTO(hd));
        }
    } catch (Exception e) {
        throw new RuntimeException("Lỗi lấy danh sách hóa đơn", e);
    }
    return result;
}

private HoaDonDTO mapEntityToDTO(HoaDon entity) {
    if (entity == null) return null;
    
    // Extract relationship DTOs
    NhanVienDTO nvDTO = new NhanVienDTO(entity.getMaNV().getMaNV());
    KhachHangDTO khDTO = new KhachHangDTO(entity.getMaKH().getMaKH());
    KhuyenMaiDTO kmDTO = (entity.getMaKM() != null) 
        ? new KhuyenMaiDTO(entity.getMaKM().getMaKM()) 
        : null;
    
    // Create DTO
    return HoaDonDTO.builder()
            .MaHD(entity.getMaHD())
            .ngayTao(entity.getNgayTao())
            .maNV(nvDTO)
            .maKH(khDTO)
            .maKM(kmDTO)
            .tongTien(entity.getTongTien())
            .deleteAt(entity.isDeleteAt())
            .build();
}

// DAO Layer
public ArrayList<HoaDon> getAllHoaDon() {
    ArrayList<HoaDon> dsHoaDon = new ArrayList<>();
    try {
        Connection con = ConnectDB.getConnection();
        if (con == null || con.isClosed()) {
            ConnectDB.getInstance().connect();
            con = ConnectDB.getConnection();
        }
        
        String sql = "SELECT * FROM HoaDon WHERE DeleteAt = 0";
        try (Statement state = con.createStatement();
             ResultSet rs = state.executeQuery(sql)) {
            
            // 1️⃣ Query database
            // 2️⃣ Mapping ResultSet to Entity
            while (rs.next()) {
                HoaDon hd = mapResultSetToEntity(rs);
                dsHoaDon.add(hd);
            }
        }
    } catch (Exception e) {
        throw new RuntimeException("Lỗi lấy danh sách hóa đơn", e);
    }
    return dsHoaDon;
}

private HoaDon mapResultSetToEntity(ResultSet rs) throws SQLException {
    HoaDon hd = new HoaDon();
    
    // Read basic fields
    hd.setMaHD(rs.getString("MaHD"));
    hd.setNgayTao(rs.getDate("NgayTao").toLocalDate());
    hd.setTongTien(rs.getDouble("TongTien"));
    
    // Load relationships (DAO chain pattern)
    try {
        String maNV = rs.getString("MaNV");
        NhanVien nv = nhanVienDAO.findNhanVienVoiMa(maNV);
        hd.setMaNV(nv);
        
        String maKH = rs.getString("MaKH");
        KhachHang kh = khachHangDAO.getKhachHangTheoMa(maKH);
        hd.setMaKH(kh);
        
        String maKM = rs.getString("MaKM");
        if (maKM != null) {
            KhuyenMai km = khuyenMaiDAO.getKhuyenMaiTheoMa(maKM);
            hd.setMaKM(km);
        }
    } catch (Exception e) {
        // Handle missing relationships
        System.err.println("Warning: Could not load relationships for HoaDon: " + e.getMessage());
    }
    
    return hd;
}
```

---

## 🔄 Relationship Loading Pattern

### DAO Chain Pattern (Relationship Loading)

```java
// When loading HoaDon, we need related entities
// DAO Pattern: Load primary entity, then load related entities

ResultSet rs = ...;  // From database

// Read basic HoaDon fields
String maNV = rs.getString("MaNV");

// Call NhanVien_DAO to get full NhanVien object
NhanVien nhanVien = nhanVienDAO.findNhanVienVoiMa(maNV);

// Set relationship
hoaDon.setMaNV(nhanVien);

// Same for other relationships
KhachHang khachHang = khachHangDAO.getKhachHangTheoMa(rs.getString("MaKH"));
hoaDon.setMaKH(khachHang);
```

### Benefits
1. ✅ **Lazy but Safe**: Relationships loaded when needed
2. ✅ **Reusable**: Each DAO handles its entity
3. ✅ **Consistent**: Same pattern in all DAOs
4. ✅ **Flexible**: Can handle missing data gracefully

---

## 🛡️ Error Handling Strategy

### Three-Layer Error Handling

```
Controller Layer
    ↓ (Display error)
    try-catch
    ↓ showAlert()
    
Service Layer
    ↓ (Log & wrap)
    try-catch
    ↓ throw RuntimeException
    
DAO Layer
    ↓ (Log & propagate)
    try-catch
    ↓ throw RuntimeException
```

### Pattern

```java
// DAO Layer: Catch and throw
public void insertHoaDon(HoaDon hd) {
    try {
        // SQL logic
    } catch (SQLException e) {
        throw new RuntimeException("Lỗi thêm hóa đơn vào DB", e);
    }
}

// Service Layer: Catch and wrap
public boolean insertHoaDon(HoaDonDTO hdDTO) {
    try {
        HoaDon entity = mapDTOToEntity(hdDTO);
        hoaDonDAO.insertHoaDon(entity);
        return true;
    } catch (Exception e) {
        throw new RuntimeException("Lỗi thêm hóa đơn", e);
    }
}

// Controller Layer: Catch and display
private void themHoaDon() {
    try {
        HoaDonDTO hdDTO = buildDTO();
        hoaDonService.insertHoaDon(hdDTO);
        showAlert("Thành công", "Thêm hóa đơn thành công");
    } catch (RuntimeException e) {
        showAlert("Lỗi", "Lỗi: " + e.getMessage());
        e.printStackTrace();
    }
}
```

---

## 🔐 Connection Management

### Pattern

```java
// Singleton pattern via ConnectDB
Connection con = ConnectDB.getConnection();

// Safety check
if (con == null || con.isClosed()) {
    ConnectDB.getInstance().connect();
    con = ConnectDB.getConnection();
}

// Use try-with-resources for auto cleanup
try (Statement stmt = con.createStatement();
     ResultSet rs = stmt.executeQuery(sql)) {
    // Process results
}  // Auto close stmt and rs
```

### Benefits
1. ✅ **Singleton**: Single connection pool
2. ✅ **Safety**: Check before use
3. ✅ **Resource Management**: Auto cleanup with try-with-resources
4. ✅ **Exception Handling**: Proper SQLException handling

---

## 📊 Entity Relationships

### HoaDon Relationships
```
HoaDon (1)
  ├─ MaNV → NhanVien (Many-to-One)
  ├─ MaKH → KhachHang (Many-to-One)
  ├─ MaKM → KhuyenMai (Many-to-One)
  └─ MaHD ← ChiTietHoaDon (One-to-Many)
```

### ChiTietHoaDon Relationships
```
ChiTietHoaDon (Composite Key: MaHD + MaLoThuoc)
  ├─ MaHD → HoaDon (Many-to-One)
  ├─ MaLoThuoc → LoThuoc (Many-to-One)
  └─ MaDVT → DonViTinh (Many-to-One)
```

### DTO Mirroring
```
HoaDon Entity
  ↓ (mapEntityToDTO)
HoaDonDTO
  ├─ maNV: NhanVienDTO
  ├─ maKH: KhachHangDTO
  └─ maKM: KhuyenMaiDTO

ChiTietHoaDon Entity
  ↓ (mapEntityToDTO)
ChiTietHoaDonDTO
  ├─ MaHD: HoaDonDTO
  ├─ maLoThuocDTO: LoThuocDTO
  └─ maDVT: DonViTinhDTO
```

---

## 🎯 Key Design Patterns

| Pattern | Usage | Benefit |
|---------|-------|---------|
| **DAO Pattern** | Data Access Layer | Abstraction from DB implementation |
| **Service Pattern** | Business Logic Layer | Decoupling logic from UI |
| **DTO Pattern** | Data Transfer | Type-safe, UI-independent |
| **Builder Pattern** | Object Construction | Readable, flexible object creation |
| **Singleton Pattern** | Connection Management | Single instance, resource efficient |
| **Try-With-Resources** | Resource Management | Auto cleanup, exception safe |

---

## 🚀 Performance Considerations

### Lazy Loading Strategy
- ✅ **Relationships loaded on-demand** in DAO via other DAOs
- ⚠️ **Not true lazy-loading** (no proxy objects)
- ✅ **Acceptable for this app size**

### Connection Management
- ✅ **Single connection pool** via ConnectDB singleton
- ✅ **Connection reuse** across DAOs
- ✅ **Safe null/closed checks** before use

### Query Optimization
- ✅ **PreparedStatement** prevents SQL injection
- ✅ **Proper SQL** with WHERE clauses
- ⚠️ **Room for optimization** with join queries

---

## 📚 References

### For Controllers
- See: `HUONG_DAN_SUA_CONTROLLERS.java`
- Pattern: UI → DTO → Service → DAO → DB

### For Testing
- Test each layer independently
- Mock DAO in Service tests
- Mock Service in Controller tests

### For Modifications
- Add new methods to interface first
- Then implement in DAO/Service
- Update Controller to call Service

---

**Last Updated**: 19/04/2026  
**Architecture Version**: 1.0 (n-layer with Service)

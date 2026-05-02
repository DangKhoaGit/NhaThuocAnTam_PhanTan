# 📋 HƯỚNG DẪN GỌI CLIENT - NHÀ THUỐC AN TÂM

## 1️⃣ KIẾN TRÚC TỔNG QUÁT

```
┌─────────────────────────────────────────────────────────┐
│                    JAVAFX UI LAYER                      │
│            (Controllers - CapNhatPhieuDatController)     │
└──────────────────────┬──────────────────────────────────┘
                       │ Gọi
                       ↓
┌─────────────────────────────────────────────────────────┐
│                  CLIENT MANAGER (SINGLETON)              │
│    - getInstance()                                       │
│    - send(Command) / sendForSuccess(Command)            │
│    - Các method GET/POST/PUT/DELETE                     │
└──────────────────────┬──────────────────────────────────┘
                       │ Sử dụng Task (Async)
                       ↓
┌─────────────────────────────────────────────────────────┐
│                   NETWORK LAYER                          │
│              Client - sendCommand()                      │
│                (Socket Communication)                    │
└──────────────────────┬──────────────────────────────────┘
                       │ Network Call
                       ↓
┌─────────────────────────────────────────────────────────┐
│                   SERVER SIDE                            │
│             (REST API / Socket Handler)                  │
└─────────────────────────────────────────────────────────┘
```

---

## 2️⃣ CÁCH SỬ DỤNG CLIENT MANAGER

### A) KHỞI TẠO CLIENT MANAGER

```java
// Lấy singleton instance
private ClientManager clientManager = ClientManager.getInstance();

// Hoặc
ClientManager clientManager = ClientManager.getInstance();
```

**Đặc điểm:**
- ✅ **Singleton pattern** - chỉ có 1 instance duy nhất
- ✅ **Synchronized** - an toàn với multi-threading
- ✅ Kết nối tái sử dụng

---

### B) KẾT NỐI TỚI SERVER

```java
// Kết nối với host và port cụ thể
boolean connected = clientManager.connectToServer("localhost", 8080);

// Hoặc kết nối với cấu hình mặc định
boolean connected = clientManager.connectToServer();

// Kiểm tra kết nối
if (clientManager.isConnected()) {
    System.out.println("✅ Đã kết nối tới server");
}
```

---

### C) CÁCH GỌI CLIENT TRONG CONTROLLERS

#### **Pattern 1: Gọi đơn giản (không async)**

```java
// ❌ KHÔNG NÊN gọi trực tiếp - sẽ BLOCK UI thread
List<PhieuDatThuocDTO> list = clientManager.getPhieuDatList();
```

#### **Pattern 2: Gọi với JavaFX Task (ĐÚNG - ASYNC)**

```java
// ✅ GỌI ĐÚNG CÁT - Chạy trên background thread
Task<List<PhieuDatThuocDTO>> loadDataTask = new Task<>() {
    @Override
    protected List<PhieuDatThuocDTO> call() throws Exception {
        // Gọi client trên background thread
        return clientManager.getPhieuDatList();
    }
};

// Xử lý khi thành công
loadDataTask.setOnSucceeded(event -> {
    List<PhieuDatThuocDTO> data = loadDataTask.getValue();
    // Cập nhật UI trên JavaFX thread
    tvPhieuDat.setItems(FXCollections.observableArrayList(data));
});

// Xử lý khi thất bại
loadDataTask.setOnFailed(event -> {
    System.err.println("❌ Lỗi load data: " + loadDataTask.getException());
});

// Chạy task trên thread mới
new Thread(loadDataTask).start();
```

---

## 3️⃣ LUỒNG GỌITHỰC TẾ

### **Luồng 1: Load dữ liệu hóa đơn**

```
┌─────────────────────────────────────────────────────────┐
│  1. UI Khởi tạo - Constructor ThemHoaDonController      │
└────────────────┬────────────────────────────────────────┘
                 │
                 ↓
┌─────────────────────────────────────────────────────────┐
│  2. Tạo Task<List<HoaDonDTO>>                           │
└────────────────┬────────────────────────────────────────┘
                 │
                 ↓
┌─────────────────────────────────────────────────────────┐
│  3. Task.call() trên Background Thread                  │
│     → clientManager.getHoaDonList()                      │
│     → Gửi Command tới Server                            │
│     → Chờ Response                                      │
└────────────────┬────────────────────────────────────────┘
                 │
                 ↓ (Kết quả)
┌─────────────────────────────────────────────────────────┐
│  4. setOnSucceeded() - Cập nhật UI                      │
│     → table_invoice.setItems(data)                      │
│     → Refresh TableView                                 │
└─────────────────────────────────────────────────────────┘
```

**Code thực tế:**

```java
Task<List<HoaDonDTO>> loadTask = new Task<List<HoaDonDTO>>() {
    @Override
    protected List<HoaDonDTO> call() throws Exception {
        // Chạy trên background thread
        return clientManager.getHoaDonList();  // ⬅️ Gọi client
    }
};

loadTask.setOnSucceeded(e -> {
    // Chạy trên JavaFX thread
    List<HoaDonDTO> hoaDons = loadTask.getValue();
    table_invoice.setItems(FXCollections.observableArrayList(hoaDons));
});

new Thread(loadTask).start();  // ⬅️ Chạy trên thread mới
```

---

### **Luồng 2: Thêm/Sửa/Xóa dữ liệu (Write Operations)**

```
┌─────────────────────────────────────────────────────────┐
│  1. User Click "Thêm" hoặc "Sửa" button                │
└────────────────┬────────────────────────────────────────┘
                 │
                 ↓
┌─────────────────────────────────────────────────────────┐
│  2. Mở Dialog Form                                       │
│     (ThemHoaDonFormController)                          │
└────────────────┬────────────────────────────────────────┘
                 │
                 ↓
┌─────────────────────────────────────────────────────────┐
│  3. User nhập dữ liệu và Click "Lưu"                   │
└────────────────┬────────────────────────────────────────┘
                 │
                 ↓
┌─────────────────────────────────────────────────────────┐
│  4. Tạo Task<Boolean> hoặc gọi trực tiếp              │
│     clientManager.createHoaDon(dto)                     │
│     clientManager.updateHoaDon(dto)                     │
│     clientManager.deleteHoaDon(maHD)                    │
└────────────────┬────────────────────────────────────────┘
                 │
                 ↓
┌─────────────────────────────────────────────────────────┐
│  5. Nhận Response từ Server                             │
│     isSuccess = true/false                              │
└────────────────┬────────────────────────────────────────┘
                 │
                 ↓
┌─────────────────────────────────────────────────────────┐
│  6. Hiển thị notification + Reload dữ liệu             │
│     showMess("Thành công", "Lưu thành công!")           │
│     Gọi lại loadDataVaoBang()                           │
└─────────────────────────────────────────────────────────┘
```

---

## 4️⃣ CÁC PHƯƠNG THỨC CLIENT THƯỜNG DÙNG

### **Authentication**
```java
// Đăng nhập
boolean isLogin = clientManager.login("user123", "password123");
if (isLogin) {
    sessionId = clientManager.getSessionId();
}

// Đăng xuất
clientManager.logout();
```

### **Get List (Read)**
```java
// Lấy danh sách
List<HoaDonDTO> hoaDons = clientManager.getHoaDonList();
List<NhanVienDTO> nhanViens = clientManager.getNhanVienList();
List<KhachHangDTO> khachHangs = clientManager.getKhachHangList();
List<ThuocDTO> thuocs = clientManager.getThuocList();
List<PhieuDatThuocDTO> phieuDats = clientManager.getPhieuDatList();

// Lấy chi tiết
HoaDonDTO hd = clientManager.getHoaDonById("HD001");
KhachHangDTO kh = clientManager.getKhachHangById("KH001");
ThuocDTO t = clientManager.getThuocById("T001");
```

### **Create (Write)**
```java
// Tạo mới
boolean success = clientManager.createHoaDon(hoaDonDTO);
boolean success = clientManager.createNhanVien(nhanVienDTO);
boolean success = clientManager.createKhachHang(khachHangDTO);
boolean success = clientManager.createThuoc(thuocDTO);
```

### **Update (Write)**
```java
// Cập nhật
boolean success = clientManager.updateHoaDon(hoaDonDTO);
boolean success = clientManager.updateNhanVien(nhanVienDTO);
boolean success = clientManager.updateThuoc(thuocDTO);
```

### **Delete (Write)**
```java
// Xóa
boolean success = clientManager.deleteHoaDon("HD001");
boolean success = clientManager.deleteNhanVien("NV001");
boolean success = clientManager.deleteThuoc("T001");
```

### **Statistics**
```java
// Thống kê
int tongSoThuoc = clientManager.getTongSoThuoc();
int tongSoNhanVien = clientManager.getTongSoNhanVien();
int soHoaDonHom = clientManager.getSoHoaDonHomNay();
Map<String, Double> doanhThu7Ngay = clientManager.getDoanhThu7NgayGanNhat();
List<Map<String, Object>> thuocSapHetHan = clientManager.getThuocSapHetHan();
```

---

## 5️⃣ CÁCH XÓNG CLIENT (CORE SEND METHODS)

### **Private send() method - Trả về data kiểu T**

```java
@SuppressWarnings("unchecked")
private <T> T send(Command command) {
    try {
        // 1. Inject sessionId vào command (nếu đã login)
        if (sessionId != null) {
            command.setSessionId(sessionId);
        }

        // 2. Gửi command tới server với auto-reconnect
        Response response = sendCommandWithAutoConnect(command);

        // 3. Kiểm tra response
        if (!response.isSuccess()) {
            LOGGER.warning("Command failed: " + response.getMessage());
            return null;  // ❌ Thất bại
        }

        // 4. Trả về dữ liệu
        return (T) response.getData();  // ✅ Thành công

    } catch (Exception e) {
        LOGGER.log(Level.SEVERE, "Client error", e);
        return null;
    }
}
```

**Ưu điểm:**
- ✅ Generic - có thể trả về bất cứ type nào
- ✅ Tự động inject sessionId
- ✅ Xử lý auto-reconnect
- ✅ Catch exception

### **Private sendForSuccess() method - Trả về boolean**

```java
private boolean sendForSuccess(Command command) {
    try {
        // 1. Inject sessionId
        if (sessionId != null) {
            command.setSessionId(sessionId);
        }

        // 2. Gửi command
        Response response = sendCommandWithAutoConnect(command);

        // 3. Kiểm tra null
        if (response == null) {
            LOGGER.warning("⚠ Response = null (kết nối hoặc serialization error)");
            return false;
        }

        // 4. Kiểm tra success
        if (!response.isSuccess()) {
            LOGGER.warning("Command failed: " + response.getMessage());
            return false;
        }

        return true;  // ✅ Thành công

    } catch (Exception e) {
        LOGGER.log(Level.SEVERE, "Client error", e);
        return false;
    }
}
```

**Ưu điểm:**
- ✅ Đơn giản - chỉ cần true/false
- ✅ Tốt cho CREATE, UPDATE, DELETE

---

## 6️⃣ VÍ DỤ THỰC TẾ TỪ PROJECT

### **Ví dụ 1: Load Phiếu Đặt (CapNhatPhieuDatController)**

```java
// Khởi tạo
private ClientManager clientManager = ClientManager.getInstance();

// Constructor
public CapNhatPhieuDatController() {
    // ... UI setup code ...

    // Load dữ liệu
    Task<List<PhieuDatThuocDTO>> loadDataTask = new Task<>() {
        @Override
        protected List<PhieuDatThuocDTO> call() throws Exception {
            // ⬅️ Gọi client - chạy trên background
            return clientManager.getPhieuDatList();
        }
    };

    // Xử lý kết quả
    loadDataTask.setOnSucceeded(event -> {
        listPDT = loadDataTask.getValue();
        origin = FXCollections.observableArrayList(listPDT);
        filter = FXCollections.observableArrayList(origin);
        tvPhieuDat.setItems(filter);  // ⬅️ Cập nhật UI
    });

    loadDataTask.setOnFailed(event -> {
        System.err.println("Lỗi: " + event.getSource().getException());
    });

    // Chạy trên thread mới
    new Thread(loadDataTask).start();
}
```

### **Ví dụ 2: Xử lý Sự Kiện Click**

```java
btnThanhToan.setOnAction((e) -> {
    // Lấy phiếu được chọn
    if (tvPhieuDat.getSelectionModel().getSelectedItem() == null) {
        showMess("Cảnh báo", "Hãy chọn một phiếu đặt thuốc");
        return;
    }

    PhieuDatThuocDTO selectedPDT = tvPhieuDat.getSelectionModel().getSelectedItem();

    // Mở form chi tiết (xử lý update ở form)
    CapNhatPhieuDatFormController formController = new CapNhatPhieuDatFormController();
    Dialog<DialogPane> dialog = new Dialog<>();
    dialog.setDialogPane(formController);
    dialog.setTitle("Chi tiết phiếu đặt");
    dialog.initModality(Modality.APPLICATION_MODAL);
    dialog.showAndWait();

    // Reload dữ liệu sau khi form đóng
    loadDataVaoBang();
});
```

---

## 7️⃣ CÓ NHỮNG GÌ CẦN CHUYÊN TÂM?

### **⚠️ LỖI THƯỜNG GẶP**

| Vấn đề | Nguyên nhân | Cách khắc phục |
|--------|-----------|----------------|
| ❌ UI bị freeze | Gọi client trực tiếp trên JavaFX thread | Dùng `Task<>` chạy background |
| ❌ Null Exception | Response = null | Kiểm tra `isConnected()` trước, thêm try-catch |
| ❌ Dữ liệu cũ | Forget reload sau write | Gọi `loadDataVaoBang()` sau mỗi update |
| ❌ Multi-thread issue | Cập nhật UI từ background thread | Dùng `Platform.runLater()` hoặc `setOnSucceeded()` |
| ❌ SessionId lost | Disconnect or timeout | Gọi reconnect hoặc re-login |

---

## 8️⃣ CHECKLIST GỌI CLIENT

```
✅ Bước 1: Khởi tạo ClientManager
   ClientManager clientManager = ClientManager.getInstance();

✅ Bước 2: Kiểm tra kết nối
   if (!clientManager.isConnected()) {
       clientManager.connectToServer();
   }

✅ Bước 3: Gọi phương thức trong Task
   Task<List<?>> task = new Task<>() {
       protected List<?> call() throws Exception {
           return clientManager.getList();  // ⬅️ Gọi client
       }
   };

✅ Bước 4: Xử lý kết quả trong setOnSucceeded()
   task.setOnSucceeded(event -> {
       updateUI(task.getValue());
   });

✅ Bước 5: Chạy task trên thread mới
   new Thread(task).start();

✅ Bước 6: Xử lý exception
   task.setOnFailed(event -> {
       handleError(task.getException());
   });
```

---

## 9️⃣ DIAGRAM LUỒNG ĐẦY ĐỦ

```
USER ACTION
    ↓
┌─ Controller Event Handler ─┐
│  (Button Click)             │
└──────────────┬──────────────┘
               ↓
    ┌─ Lấy dữ liệu từ UI form ─┐
    │ (Validation)              │
    └──────────────┬─────────────┘
                   ↓
        ┌─ Tạo Task<Boolean> ─┐
        │ (Background work)   │
        └──────┬──────────────┘
               ↓
    ┌─ Call ClientManager Method ─┐
    │ (e.g., createHoaDon(dto))   │
    └──────────┬──────────────────┘
               ↓
        ┌─ Build Command ─┐
        │ (With SessionId)│
        └────────┬────────┘
                 ↓
    ┌─ sendCommandWithAutoConnect() ─┐
    │ (Auto-reconnect if needed)     │
    └──────────┬─────────────────────┘
               ↓
        ┌─ Socket Send ─┐
        │ (Network I/O) │
        └────────┬───────┘
                 ↓
            [SERVER]
                 ↓
        ┌─ Socket Receive ─┐
        │ (Response obj)   │
        └────────┬─────────┘
                 ↓
    ┌─ Check Response.isSuccess() ─┐
    │ (true = OK, false = Error)   │
    └──────────┬───────────────────┘
               ↓
    ┌─ Return Data (or null) ─┐
    │ (to send() method)      │
    └──────────┬──────────────┘
               ↓
        ┌─ task.getValue() ─┐
        │ (Get result)      │
        └────────┬──────────┘
                 ↓
    ┌─ setOnSucceeded() ─┐
    │ (Update UI)        │
    │ (On JavaFX thread) │
    └────────────────────┘
```

---

## 🔟 TÓM TẮT

### **Quick Reference**

```java
// 1. Singleton
ClientManager cm = ClientManager.getInstance();

// 2. Connect
cm.connectToServer();

// 3. Read (Async with Task)
Task<List<?>> task = new Task<>() {
    protected List<?> call() throws Exception {
        return cm.getList();
    }
};
task.setOnSucceeded(e -> updateUI(task.getValue()));
new Thread(task).start();

// 4. Write
boolean success = cm.createData(dto);
if (success) showMess("OK", "Thành công");
else showMess("Error", "Thất bại");

// 5. Reload
loadDataVaoBang();  // Call after write
```

---

**Chúc bạn code vui vẻ! 🚀**

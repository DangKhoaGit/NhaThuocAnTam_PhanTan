# 📘 Hướng Dẫn Triển Khai Luồng Client-Server (Task-Based)

Kiến trúc này đảm bảo ứng dụng không bị treo (Non-blocking UI) và xử lý logic tập trung tại Server.

---

## 🔄 Quy trình 5 bước thực hiện

### Bước 1: Khai báo Giao thức (CommandType)

Mọi hành động giữa Client và Server phải được định danh. Bạn cần thêm một giá trị vào Enum `CommandType`.

**File:** `com.antam.app.network.command.CommandType`

```java
public enum CommandType {
    // ... các loại cũ
    CREATE_PHIEUDAT_FULL, // Thêm phiếu đặt kèm chi tiết và trừ kho
    // ...
}
```

### Bước 2: Xây dựng API tại ClientManager

`ClientManager` đóng vai trò là "người đưa thư". Bạn tạo một phương thức để đóng gói dữ liệu vào `Command`.

**File:** `com.antam.app.network.ClientManager`

```java
public boolean insertPhieuDatFull(PhieuDatThuocDTO phieu, List<ChiTietPhieuDatThuocDTO> details) {
    try {
        Map<String, Object> payload = new HashMap<>();
        payload.put("phieu", phieu);
        payload.put("details", details);

        Command command = Command.builder()
                .type(CommandType.CREATE_PHIEUDAT_FULL)
                .payload(payload)
                .timestamp(System.currentTimeMillis())
                .build();

        Response response = sendCommandWithAutoConnect(command);
        return response.isSuccess();
    } catch (Exception e) {
        LOGGER.log(Level.SEVERE, "Lỗi gửi lệnh tạo phiếu đặt", e);
        return false;
    }
}
```

### Bước 3: Định tuyến xử lý tại Server (CommandRouter)

Server nhận được `Command`, `CommandRouter` sẽ quyết định gọi Service nào. **Quan trọng:** Phải xử lý Transaction tại đây.

**File:** `com.antam.app.network.handler.CommandRouter`

```java
private Response handleCreatePhieuDatFull(Command command) {
    try {
        Map<String, Object> payload = command.getPayload();
        PhieuDatThuocDTO phieu = (PhieuDatThuocDTO) payload.get("phieu");
        List<ChiTietPhieuDatThuocDTO> details = (List<ChiTietPhieuDatThuocDTO>) payload.get("details");

        // Gọi Service xử lý logic (Lưu database, trừ kho, v.v.)
        boolean success = serviceLocator.getPhieuDatService().createFullTransaction(phieu, details);

        return Response.builder()
                .success(success)
                .message(success ? "Tạo phiếu thành công" : "Lỗi xử lý tại Server")
                .build();
    } catch (Exception e) {
        return Response.builder().success(false).message(e.getMessage()).build();
    }
}
```

### Bước 4: Tạo Task Async tại Controller

Tại UI (Controller), chúng ta không gọi `ClientManager` trực tiếp. Chúng ta bọc nó trong `javafx.concurrent.Task` để chạy ngầm.

**Ví dụ trong `ThemPhieuDatFormController`:**

```java
private void handleSavePhieuDat() {
    // 1. Tạo Task
    Task<Boolean> saveTask = new Task<>() {
        @Override
        protected Boolean call() throws Exception {
            return ClientManager.getInstance().insertPhieuDatFull(phieuDTO, listChiTiet);
        }
    };

    // 2. Gắn sự kiện (Bước 5)
    saveTask.setOnSucceeded(e -> {
        if (saveTask.getValue()) {
            showMess("Thành công", "Dữ liệu đã được lưu qua Server.");
        }
    });

    // 3. Chạy Thread
    new Thread(saveTask).start();
}
```

### Bước 5: Áp dụng xử lý sự kiện (Event Handling)

Sử dụng các Hook của Task để tương tác với người dùng:

- `setOnRunning`: Hiển thị Loading indicator (vòng xoay).
- `setOnSucceeded`: Cập nhật TableView, xóa trắng Form, thông báo thành công.
- `setOnFailed`: Thông báo lỗi kết nối hoặc lỗi logic từ Server.

---

## 💡 Quy tắc vàng cho Chuyên gia

1. **Serialization**: Đảm bảo tất cả DTO đều `implements Serializable`.
2. **Single Point of Truth**: Client không bao giờ tự ý truy cập Database (SQL). Chỉ có Server mới được dùng DAO.
3. **Thread Safety**: Mọi cập nhật UI (như `alert.show()`) bên trong Task phải bọc trong `Platform.runLater()`.
4. **Payload naming**: Đặt tên key trong Map payload đồng nhất giữa ClientManager và CommandRouter.

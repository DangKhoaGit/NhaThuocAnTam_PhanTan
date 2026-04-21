# 🏗️ Kiến Trúc Client-Server - Hệ Thống Quản Lý Nhà Thuốc

## 📋 Tổng Quan

Project đã được triển khai kiến trúc **Client-Server** sử dụng **Socket Programming** với các thành phần chính:

```
┌──────────────────────────────────────────────────────────────┐
│                    CLIENT APPLICATIONS                       │
│                 (JavaFX Controllers)                         │
│                                                              │
│  Uses: ClientManager → Network Commands → Server            │
└────────────────────┬─────────────────────────────────────────┘
                     │ TCP Socket (Port 9999)
                     ↓
┌──────────────────────────────────────────────────────────────┐
│                    SERVER APPLICATION                        │
│                  (Server.java)                              │
│                                                              │
│  ├─ Accept Connections                                      │
│  ├─ ClientHandler (per client thread)                       │
│  ├─ CommandRouter (dispatch commands)                       │
│  └─ ServiceLocator (access business logic)                  │
└──────────────────────────────────────────────────────────────┘
```

---

## 📁 Cấu Trúc Thư Mục

```
src/main/java/com/antam/app/network/
├── Client.java                          # Socket client connection
├── Sever.java                           # Server socket listener
├── ClientManager.java                   # Facade for client operations
├── ClientExample.java                   # Usage example
├── ServerMain.java                      # Server entry point
├── RequestBuilder.java                  # Build commands
│
├── message/
│   ├── Command.java                     # Command message wrapper
│   └── Response.java                    # Response message wrapper
│
├── command/
│   └── CommandType.java                 # Enum of all commands
│
├── handler/
│   ├── ClientHandler.java               # Handles individual client
│   ├── CommandRouter.java               # Routes commands to handlers
│   └── ServiceLocator.java              # Service dependency injection
│
├── config/
│   └── NetworkConfig.java               # Network configuration
│
└── util/
    └── SerializationUtil.java           # JSON serialization
```

---

## 🚀 Quick Start

### 1. Khởi động Server

```bash
# Cách 1: Run ServerMain class
java com.antam.app.network.ServerMain

# Cách 2: Chỉ định port khác
java com.antam.app.network.ServerMain 8888
```

Server sẽ:
- Lắng nghe trên port 9999 (mặc định)
- Chấp nhận client connections
- Xử lý commands từ clients
- Hiển thị interactive console

### 2. Kết nối từ Client

```java
// Lấy ClientManager singleton
ClientManager cm = ClientManager.getInstance();

// Kết nối tới server
boolean connected = cm.connectToServer("localhost", 9999);

if (connected) {
    // Gửi commands
    List<HoaDonDTO> hoaDons = cm.getHoaDonList();
    
    // Disconnect khi xong
    cm.disconnectFromServer();
}
```

---

## 📡 Message Protocol

### Command Structure
```java
Command {
    CommandType type;           // Loại command (GET_HOADON_LIST, etc.)
    Map<String, Object> payload; // Dữ liệu (JSON-compatible)
    String sessionId;            // Session ID (nếu cần)
    long timestamp;              // Timestamp của request
}
```

### Response Structure
```java
Response {
    boolean success;            // Thành công hay thất bại
    String message;             // Thông báo cho user
    Object data;                // Dữ liệu trả về
    String errorCode;           // Error code (nếu failed)
}
```

---

## 🔄 Luồng Request-Response

### Ví dụ: Lấy Danh Sách Hóa Đơn

```
1. CLIENT:
   ClientManager.getHoaDonList()
   
2. BUILD COMMAND:
   Command {
     type: GET_HOADON_LIST,
     payload: {},
     timestamp: 1234567890
   }

3. SEND OVER NETWORK:
   Socket → ObjectOutputStream → Server

4. SERVER RECEIVES:
   ClientHandler receives Command
   
5. ROUTE COMMAND:
   CommandRouter.route(command)
   → case GET_HOADON_LIST: handleGetHoaDonList()

6. CALL SERVICE:
   ServiceLocator.getHoaDonService().getAllHoaDon()
   
7. DATABASE:
   HoaDon_DAO queries database
   
8. BUILD RESPONSE:
   Response {
     success: true,
     message: "HoaDon list retrieved successfully",
     data: [HoaDonDTO, HoaDonDTO, ...],
     errorCode: null
   }

9. SEND RESPONSE:
   ObjectOutputStream → Client

10. CLIENT RECEIVES:
    Response deserialized
    
11. PARSE DATA:
    data instanceof ArrayList → cast to List<HoaDonDTO>
    
12. UPDATE UI:
    TableView.setItems(observableList)
```

---

## 💻 API Reference

### ClientManager Methods

#### Connection Management
```java
// Kết nối tới server
boolean connectToServer(String host, int port);
boolean connectToServer(); // localhost:9999

// Kiểm tra kết nối
boolean isConnected();

// Ngắt kết nối
void disconnectFromServer();
```

#### HoaDon Operations
```java
// Lấy danh sách
List<HoaDonDTO> getHoaDonList();

// CRUD operations
boolean insertHoaDon(HoaDonDTO dto);
boolean updateHoaDon(HoaDonDTO dto);
boolean deleteHoaDon(String maHD);
```

#### NhanVien Operations
```java
List<NhanVienDTO> getNhanVienList();
```

#### KhachHang Operations
```java
List<KhachHangDTO> getKhachHangList();
```

#### Authentication
```java
boolean login(String username, String password);
boolean logout();
```

#### System
```java
boolean checkServerStatus();
```

---

## 🔧 Cấu Hình

### NetworkConfig (network.properties)

```properties
# Server configuration
server.host=localhost
server.port=9999

# Thread pool
thread.pool.size=10

# Timeouts (milliseconds)
connection.timeout=5000
command.timeout=30000
```

---

## 📚 CommandType - Các Loại Commands

### Authentication
- `LOGIN` - Đăng nhập
- `LOGOUT` - Đăng xuất
- `REGISTER` - Đăng ký

### HoaDon Operations
- `GET_HOADON_LIST` - Lấy danh sách hóa đơn
- `CREATE_HOADON` - Tạo hóa đơn
- `UPDATE_HOADON` - Cập nhật hóa đơn
- `DELETE_HOADON` - Xóa hóa đơn
- `GET_HOADON_BY_ID` - Lấy hóa đơn theo ID
- `SEARCH_HOADON` - Tìm kiếm hóa đơn

### NhanVien Operations
- `GET_NHANVIEN_LIST`
- `CREATE_NHANVIEN`
- `UPDATE_NHANVIEN`
- `DELETE_NHANVIEN`

### KhachHang Operations
- `GET_KHACHHANG_LIST`
- `CREATE_KHACHHANG`
- `UPDATE_KHACHHANG`
- `DELETE_KHACHHANG`

### System Commands
- `SERVER_STATUS` - Kiểm tra trạng thái server
- `PING` - Ping server
- `USER_LIST` - Lấy danh sách users

---

## 🔐 Security Considerations

### Current Implementation
- Basic Command/Response protocol
- Session ID support (for future auth)
- Timestamp validation (optional)

### Future Enhancements
- [ ] SSL/TLS encryption
- [ ] Token-based authentication
- [ ] Command signature verification
- [ ] Rate limiting
- [ ] IP whitelisting

---

## 📊 Performance Characteristics

| Metric | Value |
|--------|-------|
| Network Protocol | TCP Socket |
| Serialization | Java ObjectStream |
| Thread Model | Thread Pool (10 threads) |
| Connection Timeout | 5 seconds |
| Command Timeout | 30 seconds |
| Max Concurrent Clients | 10 (configurable) |

---

## 🧪 Testing

### Unit Test Example

```java
@Test
public void testClientConnection() {
    ClientManager cm = ClientManager.getInstance();
    assertTrue(cm.connectToServer());
    assertTrue(cm.isConnected());
    cm.disconnectFromServer();
    assertFalse(cm.isConnected());
}

@Test
public void testGetHoaDonList() {
    ClientManager cm = ClientManager.getInstance();
    cm.connectToServer();
    
    List<HoaDonDTO> list = cm.getHoaDonList();
    assertNotNull(list);
    
    cm.disconnectFromServer();
}
```

---

## 🐛 Troubleshooting

### Issue: "Connection refused"
**Solution:** Đảm bảo server đang chạy
```bash
java com.antam.app.network.ServerMain
```

### Issue: "Port already in use"
**Solution:** Chỉ định port khác
```bash
java com.antam.app.network.ServerMain 8888
```

### Issue: "EOFException on client"
**Solution:** Server đã disconnected, check server logs

---

## 📝 Integration with Controllers

### Before (Direct Service Call)
```java
@FXML
private void loadHoaDonList() {
    List<HoaDonDTO> list = hoaDonService.getAllHoaDon();
    tableHoaDon.setItems(FXCollections.observableArrayList(list));
}
```

### After (Network Call)
```java
@FXML
private void loadHoaDonList() {
    Task<List<HoaDonDTO>> task = new Task<List<HoaDonDTO>>() {
        @Override
        protected List<HoaDonDTO> call() {
            return ClientManager.getInstance().getHoaDonList();
        }
    };
    
    task.setOnSucceeded(e -> {
        List<HoaDonDTO> list = task.getValue();
        tableHoaDon.setItems(FXCollections.observableArrayList(list));
    });
    
    new Thread(task).start();
}
```

---

## 🔄 Migration Path

### Phase 1: Server-side only
- Implement Server, CommandRouter, ClientHandler
- Test with ServerMain

### Phase 2: Client-side
- Implement Client, ClientManager
- Test with ClientExample

### Phase 3: Controller integration
- Update existing controllers to use ClientManager
- Use JavaFX Task for async operations
- Add error handling

### Phase 4: Production
- Add security (SSL/TLS, authentication)
- Configure connection pooling
- Add monitoring/logging

---

## 📖 References

- [Java Socket Programming](https://docs.oracle.com/javase/tutorial/networking/sockets/)
- [ObjectStream Serialization](https://docs.oracle.com/javase/8/docs/api/java/io/ObjectOutputStream.html)
- [Jackson JSON Library](https://github.com/FasterXML/jackson)
- [Thread Pools & ExecutorService](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/ExecutorService.html)

---

**Last Updated:** 21/04/2026  
**Architecture Version:** 1.0 (Client-Server with Socket)  
**Author:** Pham Dang Khoa


package com.antam.app.network;

import com.antam.app.dto.HoaDonDTO;
import com.antam.app.dto.NhanVienDTO;
import com.antam.app.dto.KhachHangDTO;
import com.antam.app.network.message.Command;
import com.antam.app.network.message.Response;
import com.antam.app.network.command.CommandType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * @description: Facade pattern để quản lý network communication với server
 * Cung cấp high-level API cho controllers
 * @author: Pham Dang Khoa
 * @date: 21/04/2026
 * @version: 1.0
 */
public class ClientManager {
    private static final Logger LOGGER = Logger.getLogger(ClientManager.class.getName());
    private static ClientManager instance;
    private Client client;
    private String sessionId;

    private ClientManager() {
        this.client = new Client();
        this.sessionId = null;
    }

    /**
     * Lấy singleton instance
     */
    public static synchronized ClientManager getInstance() {
        if (instance == null) {
            instance = new ClientManager();
        }
        return instance;
    }

    /**
     * Kết nối tới server
     */
    public boolean connectToServer(String host, int port) {
        try {
            client = new Client(host, port);
            client.connect();
            LOGGER.info("Successfully connected to server: " + host + ":" + port);
            return true;
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to connect to server", e);
            return false;
        }
    }

    /**
     * Kết nối tới server mặc định
     */
    public boolean connectToServer() {
        return connectToServer("localhost", 9999);
    }

    /**
     * Ngắt kết nối khỏi server
     */
    public void disconnectFromServer() {
        if (client != null) {
            client.disconnect();
            sessionId = null;
            LOGGER.info("Disconnected from server");
        }
    }

    /**
     * Kiểm tra xem đã kết nối chưa
     */
    public boolean isConnected() {
        return client != null && client.isConnected();
    }

    /**
     * === HoaDon Operations ===
     */

    public List<HoaDonDTO> getHoaDonList() {
        try {
            Command command = Command.builder()
                    .type(CommandType.GET_HOADON_LIST)
                    .sessionId(sessionId)
                    .timestamp(System.currentTimeMillis())
                    .build();

            Response response = sendCommand(command);

            if (response.isSuccess() && response.getData() instanceof ArrayList) {
                return (ArrayList<HoaDonDTO>) response.getData();
            }

            LOGGER.warning("Failed to get HoaDon list: " + response.getMessage());
            return new ArrayList<>();

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting HoaDon list", e);
            return new ArrayList<>();
        }
    }

    public boolean insertHoaDon(HoaDonDTO dto) {
        try {
            Map<String, Object> payload = new HashMap<>();
            payload.put("hoaDon", dto);

            Command command = Command.builder()
                    .type(CommandType.CREATE_HOADON)
                    .payload(payload)
                    .sessionId(sessionId)
                    .timestamp(System.currentTimeMillis())
                    .build();

            Response response = sendCommand(command);

            if (!response.isSuccess()) {
                LOGGER.warning("Failed to insert HoaDon: " + response.getMessage());
            }

            return response.isSuccess();

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error inserting HoaDon", e);
            return false;
        }
    }

    public boolean updateHoaDon(HoaDonDTO dto) {
        try {
            Map<String, Object> payload = new HashMap<>();
            payload.put("hoaDon", dto);

            Command command = Command.builder()
                    .type(CommandType.UPDATE_HOADON)
                    .payload(payload)
                    .sessionId(sessionId)
                    .timestamp(System.currentTimeMillis())
                    .build();

            Response response = sendCommand(command);

            if (!response.isSuccess()) {
                LOGGER.warning("Failed to update HoaDon: " + response.getMessage());
            }

            return response.isSuccess();

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error updating HoaDon", e);
            return false;
        }
    }

    public boolean deleteHoaDon(String maHD) {
        try {
            Map<String, Object> payload = new HashMap<>();
            payload.put("maHD", maHD);

            Command command = Command.builder()
                    .type(CommandType.DELETE_HOADON)
                    .payload(payload)
                    .sessionId(sessionId)
                    .timestamp(System.currentTimeMillis())
                    .build();

            Response response = sendCommand(command);

            if (!response.isSuccess()) {
                LOGGER.warning("Failed to delete HoaDon: " + response.getMessage());
            }

            return response.isSuccess();

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error deleting HoaDon", e);
            return false;
        }
    }

    /**
     * === NhanVien Operations ===
     */

    public List<NhanVienDTO> getNhanVienList() {
        try {
            Command command = Command.builder()
                    .type(CommandType.GET_NHANVIEN_LIST)
                    .sessionId(sessionId)
                    .timestamp(System.currentTimeMillis())
                    .build();

            Response response = sendCommand(command);

            if (response.isSuccess() && response.getData() instanceof ArrayList) {
                return (ArrayList<NhanVienDTO>) response.getData();
            }

            LOGGER.warning("Failed to get NhanVien list: " + response.getMessage());
            return new ArrayList<>();

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting NhanVien list", e);
            return new ArrayList<>();
        }
    }

    /**
     * === KhachHang Operations ===
     */

    public List<KhachHangDTO> getKhachHangList() {
        try {
            Command command = Command.builder()
                    .type(CommandType.GET_KHACHHANG_LIST)
                    .sessionId(sessionId)
                    .timestamp(System.currentTimeMillis())
                    .build();

            Response response = sendCommand(command);

            if (response.isSuccess() && response.getData() instanceof ArrayList) {
                return (ArrayList<KhachHangDTO>) response.getData();
            }

            LOGGER.warning("Failed to get KhachHang list: " + response.getMessage());
            return new ArrayList<>();

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting KhachHang list", e);
            return new ArrayList<>();
        }
    }

    /**
     * === Authentication Operations ===
     */

    public boolean login(String username, String password) {
        try {
            Map<String, Object> payload = new HashMap<>();
            payload.put("username", username);
            payload.put("password", password);

            Command command = Command.builder()
                    .type(CommandType.LOGIN)
                    .payload(payload)
                    .timestamp(System.currentTimeMillis())
                    .build();

            Response response = sendCommand(command);

            if (response.isSuccess()) {
                // Store session ID if provided
                this.sessionId = (String) response.getData();
                LOGGER.info("Login successful");
            } else {
                LOGGER.warning("Login failed: " + response.getMessage());
            }

            return response.isSuccess();

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error during login", e);
            return false;
        }
    }

    public boolean logout() {
        try {
            Command command = Command.builder()
                    .type(CommandType.LOGOUT)
                    .sessionId(sessionId)
                    .timestamp(System.currentTimeMillis())
                    .build();

            Response response = sendCommand(command);

            if (response.isSuccess()) {
                this.sessionId = null;
                LOGGER.info("Logout successful");
            } else {
                LOGGER.warning("Logout failed: " + response.getMessage());
            }

            return response.isSuccess();

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error during logout", e);
            return false;
        }
    }

    /**
     * === System Operations ===
     */

    public boolean checkServerStatus() {
        try {
            Command command = Command.builder()
                    .type(CommandType.SERVER_STATUS)
                    .sessionId(sessionId)
                    .timestamp(System.currentTimeMillis())
                    .build();

            Response response = sendCommand(command);
            return response.isSuccess();

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error checking server status", e);
            return false;
        }
    }

    /**
     * === Internal Helper Methods ===
     */

    /**
     * Gửi command và nhận response
     */
    private Response sendCommand(Command command) throws IOException {
        if (!isConnected()) {
            throw new IOException("Not connected to server");
        }
        return client.sendCommand(command);
    }

    /**
     * Lấy session ID hiện tại
     */
    public String getSessionId() {
        return sessionId;
    }

    /**
     * Đặt session ID
     */
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}

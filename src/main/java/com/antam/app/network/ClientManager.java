package com.antam.app.network;

import com.antam.app.dto.*;
import com.antam.app.network.message.Command;
import com.antam.app.network.message.Response;
import com.antam.app.network.command.CommandType;
import com.antam.app.network.config.NetworkConfig;

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
        NetworkConfig config = NetworkConfig.getInstance();
        return connectToServer(config.getServerHost(), config.getServerPort());
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
     * Kiểm tra trạng thái server
     */
    public boolean checkServerStatus() {
        try {
            Command command = RequestBuilder.buildServerStatusCommand();

            Response response = sendCommandWithAutoConnect(command);
            if (response.isSuccess()) {
                return true;
            }

            LOGGER.warning("Failed to check server status: " + response.getMessage());
            return false;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error checking server status", e);
            return false;
        }
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

            Response response = sendCommandWithAutoConnect(command);

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

            Response response = sendCommandWithAutoConnect(command);

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

            Response response = sendCommandWithAutoConnect(command);

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

            Response response = sendCommandWithAutoConnect(command);

            if (!response.isSuccess()) {
                LOGGER.warning("Failed to delete HoaDon: " + response.getMessage());
            }

            return response.isSuccess();

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error deleting HoaDon", e);
            return false;
        }
    }

    public HoaDonDTO getHoaDonById(String maHD) {
        try {
            Map<String, Object> payload = new HashMap<>();
            payload.put("maHD", maHD);

            Command command = Command.builder()
                    .type(CommandType.GET_HOADON_BY_ID)
                    .payload(payload)
                    .sessionId(sessionId)
                    .timestamp(System.currentTimeMillis())
                    .build();

            Response response = sendCommandWithAutoConnect(command);
            if (response.isSuccess() && response.getData() instanceof HoaDonDTO) {
                return (HoaDonDTO) response.getData();
            }

            LOGGER.warning("Failed to get HoaDon by ID: " + response.getMessage());
            return null;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting HoaDon by ID", e);
            return null;
        }
    }

    public boolean updateHoaDonTongTien(String maHD, double tongTien) {
        try {
            Map<String, Object> payload = new HashMap<>();
            payload.put("maHD", maHD);
            payload.put("tongTien", tongTien);

            Command command = Command.builder()
                    .type(CommandType.UPDATE_HOADON_TOTAL)
                    .payload(payload)
                    .sessionId(sessionId)
                    .timestamp(System.currentTimeMillis())
                    .build();

            Response response = sendCommandWithAutoConnect(command);
            if (!response.isSuccess()) {
                LOGGER.warning("Failed to update HoaDon total: " + response.getMessage());
            }
            return response.isSuccess();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error updating HoaDon total", e);
            return false;
        }
    }

    public boolean softDeleteHoaDon(String maHD) {
        return deleteHoaDon(maHD);
    }

    /**
     * === Thuoc Operations ===
     */

    public List<ThuocDTO> getThuocList() {
        return fetchList(CommandType.GET_THUOC_LIST, "Failed to get Thuoc list");
    }

    public List<ThuocDTO> getDeletedThuocList() {
        return fetchList(CommandType.GET_THUOC_DELETED_LIST, "Failed to get deleted Thuoc list");
    }

    public ThuocDTO getThuocById(String maThuoc) {
        try {
            Map<String, Object> payload = new HashMap<>();
            payload.put("maThuoc", maThuoc);

            Command command = Command.builder()
                    .type(CommandType.GET_THUOC_BY_ID)
                    .payload(payload)
                    .sessionId(sessionId)
                    .timestamp(System.currentTimeMillis())
                    .build();

            Response response = sendCommandWithAutoConnect(command);
            if (response.isSuccess() && response.getData() instanceof ThuocDTO) {
                return (ThuocDTO) response.getData();
            }

            LOGGER.warning("Failed to get Thuoc by ID: " + response.getMessage());
            return null;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting Thuoc by ID", e);
            return null;
        }
    }

    public boolean createThuoc(ThuocDTO dto) {
        return executeThuocWrite(CommandType.CREATE_THUOC, dto, "Error creating Thuoc");
    }

    public boolean updateThuoc(ThuocDTO dto) {
        return executeThuocWrite(CommandType.UPDATE_THUOC, dto, "Error updating Thuoc");
    }

    public boolean deleteThuoc(String maThuoc) {
        return executeThuocIdWrite(CommandType.DELETE_THUOC, maThuoc, "Error deleting Thuoc");
    }

    public boolean restoreThuoc(String maThuoc) {
        return executeThuocIdWrite(CommandType.RESTORE_THUOC, maThuoc, "Error restoring Thuoc");
    }

    /**
     * === Lookup Operations ===
     */

    public List<KeDTO> getActiveKeList() {
        return fetchList(CommandType.GET_KE_ACTIVE_LIST, "Failed to get active Ke list");
    }

    public List<DangDieuCheDTO> getActiveDangDieuCheList() {
        return fetchList(CommandType.GET_DANGDIEUCHE_ACTIVE_LIST, "Failed to get active DangDieuChe list");
    }

    public List<DonViTinhDTO> getDonViTinhList() {
        return fetchList(CommandType.GET_DONVITINH_LIST, "Failed to get DonViTinh list");
    }

    public List<LoThuocDTO> getLoThuocList() {
        return fetchList(CommandType.GET_LOTHUOC_LIST, "Failed to get LoThuoc list");
    }

    public List<LoThuocDTO> getLoThuocByThuocId(String maThuoc) {
        try {
            Map<String, Object> payload = new HashMap<>();
            payload.put("maThuoc", maThuoc);

            Command command = Command.builder()
                    .type(CommandType.GET_LOTHUOC_BY_THUOC_ID)
                    .payload(payload)
                    .sessionId(sessionId)
                    .timestamp(System.currentTimeMillis())
                    .build();

            Response response = sendCommandWithAutoConnect(command);
            if (response.isSuccess() && response.getData() instanceof ArrayList) {
                return (ArrayList<LoThuocDTO>) response.getData();
            }

            LOGGER.warning("Failed to get LoThuoc by Thuoc ID: " + response.getMessage());
            return new ArrayList<>();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting LoThuoc by Thuoc ID", e);
            return new ArrayList<>();
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

            Response response = sendCommandWithAutoConnect(command);

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

            Response response = sendCommandWithAutoConnect(command);

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

    public KhachHangDTO getKhachHangById(String maKH) {
        try {
            Map<String, Object> payload = new HashMap<>();
            payload.put("maKH", maKH);

            Command command = Command.builder()
                    .type(CommandType.GET_KHACHHANG_BY_ID)
                    .payload(payload)
                    .sessionId(sessionId)
                    .timestamp(System.currentTimeMillis())
                    .build();

            Response response = sendCommandWithAutoConnect(command);
            if (response.isSuccess() && response.getData() instanceof KhachHangDTO) {
                return (KhachHangDTO) response.getData();
            }

            LOGGER.warning("Failed to get KhachHang by ID: " + response.getMessage());
            return null;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting KhachHang by ID", e);
            return null;
        }
    }

    /**
     * === ChiTietHoaDon Operations ===
     */

    public List<ChiTietHoaDonDTO> getChiTietHoaDonByHoaDonId(String maHD) {
        return fetchChiTietHoaDonList(CommandType.GET_CHITIETHOADON_BY_HOADON_ID, maHD, "Failed to get ChiTietHoaDon list by HoaDon ID");
    }

    public List<ChiTietHoaDonDTO> getChiTietHoaDonConBanByHoaDonId(String maHD) {
        return fetchChiTietHoaDonList(CommandType.GET_CHITIETHOADON_ACTIVE_BY_HOADON_ID, maHD, "Failed to get active ChiTietHoaDon list by HoaDon ID");
    }

    public boolean softDeleteChiTietHoaDon(String maHD, int maLoThuoc, String tinhTrang) {
        try {
            Map<String, Object> payload = new HashMap<>();
            payload.put("maHD", maHD);
            payload.put("maLoThuoc", maLoThuoc);
            payload.put("tinhTrang", tinhTrang);

            Command command = Command.builder()
                    .type(CommandType.SOFT_DELETE_CHITIETHOADON)
                    .payload(payload)
                    .sessionId(sessionId)
                    .timestamp(System.currentTimeMillis())
                    .build();

            Response response = sendCommandWithAutoConnect(command);
            if (!response.isSuccess()) {
                LOGGER.warning("Failed to soft delete ChiTietHoaDon: " + response.getMessage());
            }
            return response.isSuccess();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error soft deleting ChiTietHoaDon", e);
            return false;
        }
    }

    public boolean createChiTietHoaDon(ChiTietHoaDonDTO dto) {
        try {
            Map<String, Object> payload = new HashMap<>();
            payload.put("chiTietHoaDon", dto);

            Command command = Command.builder()
                    .type(CommandType.CREATE_CHITIETHOADON)
                    .payload(payload)
                    .sessionId(sessionId)
                    .timestamp(System.currentTimeMillis())
                    .build();

            Response response = sendCommandWithAutoConnect(command);
            if (!response.isSuccess()) {
                LOGGER.warning("Failed to create ChiTietHoaDon: " + response.getMessage());
            }
            return response.isSuccess();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error creating ChiTietHoaDon", e);
            return false;
        }
    }

    public boolean tonTaiChiTietHoaDon(String maHD, int maLoThuoc) {
        try {
            Map<String, Object> payload = new HashMap<>();
            payload.put("maHD", maHD);
            payload.put("maLoThuoc", maLoThuoc);

            Command command = Command.builder()
                    .type(CommandType.CHECK_CHITIETHOADON_EXISTS)
                    .payload(payload)
                    .sessionId(sessionId)
                    .timestamp(System.currentTimeMillis())
                    .build();

            Response response = sendCommandWithAutoConnect(command);
            if (response.isSuccess() && response.getData() instanceof Boolean) {
                return (Boolean) response.getData();
            }

            LOGGER.warning("Failed to check ChiTietHoaDon exists: " + response.getMessage());
            return false;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error checking ChiTietHoaDon exists", e);
            return false;
        }
    }

    /**
     * === LoThuoc Operations ===
     */

    public LoThuocDTO getLoThuocById(int maLoThuoc) {
        try {
            Map<String, Object> payload = new HashMap<>();
            payload.put("maLoThuoc", maLoThuoc);

            Command command = Command.builder()
                    .type(CommandType.GET_LOTHUOC_BY_ID)
                    .payload(payload)
                    .sessionId(sessionId)
                    .timestamp(System.currentTimeMillis())
                    .build();

            Response response = sendCommandWithAutoConnect(command);
            if (response.isSuccess() && response.getData() instanceof LoThuocDTO) {
                return (LoThuocDTO) response.getData();
            }

            LOGGER.warning("Failed to get LoThuoc by ID: " + response.getMessage());
            return null;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting LoThuoc by ID", e);
            return null;
        }
    }

    public List<LoThuocDTO> getLoThuocFefoByThuocId(String maThuoc) {
        try {
            Map<String, Object> payload = new HashMap<>();
            payload.put("maThuoc", maThuoc);

            Command command = Command.builder()
                    .type(CommandType.GET_LOTHUOC_FEFO_BY_THUOC_ID)
                    .payload(payload)
                    .sessionId(sessionId)
                    .timestamp(System.currentTimeMillis())
                    .build();

            Response response = sendCommandWithAutoConnect(command);
            if (response.isSuccess() && response.getData() instanceof ArrayList) {
                return (ArrayList<LoThuocDTO>) response.getData();
            }

            LOGGER.warning("Failed to get LoThuoc FEFO by Thuoc ID: " + response.getMessage());
            return new ArrayList<>();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting LoThuoc FEFO by Thuoc ID", e);
            return new ArrayList<>();
        }
    }

    public boolean updateLoThuocQuantity(int maLoThuoc, int deltaSoLuong) {
        try {
            Map<String, Object> payload = new HashMap<>();
            payload.put("maLoThuoc", maLoThuoc);
            payload.put("deltaSoLuong", deltaSoLuong);

            Command command = Command.builder()
                    .type(CommandType.UPDATE_LOTHUOC_QUANTITY)
                    .payload(payload)
                    .sessionId(sessionId)
                    .timestamp(System.currentTimeMillis())
                    .build();

            Response response = sendCommandWithAutoConnect(command);
            if (!response.isSuccess()) {
                LOGGER.warning("Failed to update LoThuoc quantity: " + response.getMessage());
            }
            return response.isSuccess();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error updating LoThuoc quantity", e);
            return false;
        }
    }

    /**
     * === DonViTinh Operations ===
     */

    public DonViTinhDTO getDonViTinhById(int maDVT) {
        try {
            Map<String, Object> payload = new HashMap<>();
            payload.put("maDVT", maDVT);

            Command command = Command.builder()
                    .type(CommandType.GET_DONVITINH_BY_ID)
                    .payload(payload)
                    .sessionId(sessionId)
                    .timestamp(System.currentTimeMillis())
                    .build();

            Response response = sendCommandWithAutoConnect(command);
            if (response.isSuccess() && response.getData() instanceof DonViTinhDTO) {
                return (DonViTinhDTO) response.getData();
            }

            LOGGER.warning("Failed to get DonViTinh by ID: " + response.getMessage());
            return null;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting DonViTinh by ID", e);
            return null;
        }
    }

    /**
     * === KhuyenMai Operations ===
     */

    public List<KhuyenMaiDTO> getKhuyenMaiList() {
        return fetchList(CommandType.GET_KHUYENMAI_LIST, "Failed to get KhuyenMai list");
    }

    public List<KhuyenMaiDTO> getKhuyenMaiDeletedList() {
        return fetchList(CommandType.GET_KHUYENMAI_DELETED_LIST, "Failed to get deleted KhuyenMai list");
    }

    public List<KhuyenMaiDTO> getKhuyenMaiActiveList() {
        return fetchList(CommandType.GET_KHUYENMAI_ACTIVE_LIST, "Failed to get active KhuyenMai list");
    }

    public KhuyenMaiDTO getKhuyenMaiById(String maKM) {
        try {
            Map<String, Object> payload = new HashMap<>();
            payload.put("maKM", maKM);

            Command command = Command.builder()
                    .type(CommandType.GET_KHUYENMAI_BY_ID)
                    .payload(payload)
                    .sessionId(sessionId)
                    .timestamp(System.currentTimeMillis())
                    .build();

            Response response = sendCommandWithAutoConnect(command);
            if (response.isSuccess() && response.getData() instanceof KhuyenMaiDTO) {
                return (KhuyenMaiDTO) response.getData();
            }

            LOGGER.warning("Failed to get KhuyenMai by ID: " + response.getMessage());
            return null;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting KhuyenMai by ID", e);
            return null;
        }
    }

    public boolean createKhuyenMai(KhuyenMaiDTO dto) {
        return executeKhuyenMaiWrite(CommandType.CREATE_KHUYENMAI, dto, "Error creating KhuyenMai");
    }

    public boolean updateKhuyenMai(KhuyenMaiDTO dto) {
        return executeKhuyenMaiWrite(CommandType.UPDATE_KHUYENMAI, dto, "Error updating KhuyenMai");
    }

    public boolean deleteKhuyenMai(String maKM) {
        return executeKhuyenMaiIdWrite(CommandType.DELETE_KHUYENMAI, maKM, "Error deleting KhuyenMai");
    }

    public boolean restoreKhuyenMai(String maKM) {
        return executeKhuyenMaiIdWrite(CommandType.RESTORE_KHUYENMAI, maKM, "Error restoring KhuyenMai");
    }

    public List<LoaiKhuyenMaiDTO> getLoaiKhuyenMaiList() {
        return fetchList(CommandType.GET_LOAIKHUYENMAI_LIST, "Failed to get LoaiKhuyenMai list");
    }

    /**
     * === Internal Helpers ===
     */

    private <T> List<T> fetchList(CommandType commandType, String logMessage) {
        try {
            Command command = Command.builder()
                    .type(commandType)
                    .sessionId(sessionId)
                    .timestamp(System.currentTimeMillis())
                    .build();

            Response response = sendCommandWithAutoConnect(command);
            if (response.isSuccess() && response.getData() instanceof ArrayList) {
                return (ArrayList<T>) response.getData();
            }

            LOGGER.warning(logMessage + ": " + response.getMessage());
            return new ArrayList<>();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, logMessage, e);
            return new ArrayList<>();
        }
    }

    private boolean executeThuocWrite(CommandType commandType, ThuocDTO dto, String errorLog) {
        try {
            Map<String, Object> payload = new HashMap<>();
            payload.put("thuoc", dto);

            Command command = Command.builder()
                    .type(commandType)
                    .payload(payload)
                    .sessionId(sessionId)
                    .timestamp(System.currentTimeMillis())
                    .build();

            Response response = sendCommandWithAutoConnect(command);
            if (!response.isSuccess()) {
                LOGGER.warning(errorLog + ": " + response.getMessage());
            }
            return response.isSuccess();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, errorLog, e);
            return false;
        }
    }

    private boolean executeThuocIdWrite(CommandType commandType, String maThuoc, String errorLog) {
        try {
            Map<String, Object> payload = new HashMap<>();
            payload.put("maThuoc", maThuoc);

            Command command = Command.builder()
                    .type(commandType)
                    .payload(payload)
                    .sessionId(sessionId)
                    .timestamp(System.currentTimeMillis())
                    .build();

            Response response = sendCommandWithAutoConnect(command);
            if (!response.isSuccess()) {
                LOGGER.warning(errorLog + ": " + response.getMessage());
            }
            return response.isSuccess();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, errorLog, e);
            return false;
        }
    }

    private boolean executeKhuyenMaiWrite(CommandType commandType, KhuyenMaiDTO dto, String errorLog) {
        try {
            Map<String, Object> payload = new HashMap<>();
            payload.put("khuyenMai", dto);

            Command command = Command.builder()
                    .type(commandType)
                    .payload(payload)
                    .sessionId(sessionId)
                    .timestamp(System.currentTimeMillis())
                    .build();

            Response response = sendCommandWithAutoConnect(command);
            if (!response.isSuccess()) {
                LOGGER.warning(errorLog + ": " + response.getMessage());
            }
            return response.isSuccess();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, errorLog, e);
            return false;
        }
    }

    private boolean executeKhuyenMaiIdWrite(CommandType commandType, String maKM, String errorLog) {
        try {
            Map<String, Object> payload = new HashMap<>();
            payload.put("maKM", maKM);

            Command command = Command.builder()
                    .type(commandType)
                    .payload(payload)
                    .sessionId(sessionId)
                    .timestamp(System.currentTimeMillis())
                    .build();

            Response response = sendCommandWithAutoConnect(command);
            if (!response.isSuccess()) {
                LOGGER.warning(errorLog + ": " + response.getMessage());
            }
            return response.isSuccess();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, errorLog, e);
            return false;
        }
    }

    private <T> List<T> fetchChiTietHoaDonList(CommandType commandType, String maHD, String logMessage) {
        try {
            Map<String, Object> payload = new HashMap<>();
            payload.put("maHD", maHD);

            Command command = Command.builder()
                    .type(commandType)
                    .payload(payload)
                    .sessionId(sessionId)
                    .timestamp(System.currentTimeMillis())
                    .build();

            Response response = sendCommandWithAutoConnect(command);
            if (response.isSuccess() && response.getData() instanceof ArrayList) {
                return (ArrayList<T>) response.getData();
            }

            LOGGER.warning(logMessage + ": " + response.getMessage());
            return new ArrayList<>();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, logMessage, e);
            return new ArrayList<>();
        }
    }

    private Response sendCommandWithAutoConnect(Command command) throws IOException {
        if (!isConnected() && !connectToServer()) {
            throw new IOException("Cannot connect to server");
        }
        return sendCommand(command);
    }

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

package com.antam.app.network;

import com.antam.app.dto.*;
import com.antam.app.network.command.CommandType;
import com.antam.app.network.config.NetworkConfig;
import com.antam.app.network.message.Command;
import com.antam.app.network.message.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientManager {

    private static final Logger LOGGER = Logger.getLogger(ClientManager.class.getName());
    private static ClientManager instance;
    private Client client;
    private String sessionId;


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
        } catch ( IOException e) {
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


    // =========================================================
    // 🔥 CORE SEND METHOD (QUAN TRỌNG NHẤT)
    // =========================================================
    @SuppressWarnings("unchecked")
    private <T> T send(Command command) {
        try {
            // inject session nếu có
            if (sessionId != null) {
                command.setSessionId(sessionId);
            }

            Response response = sendCommandWithAutoConnect(command);

            if (!response.isSuccess()) {
                LOGGER.warning("Command failed: " + response.getMessage());
                return null;
            }

            return (T) response.getData();

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Client error", e);
            return null;
        }
    }

    private boolean sendForSuccess(Command command) {
        try {
            // inject session nếu có
            if (sessionId != null) {
                command.setSessionId(sessionId);
            }

            Response response = sendCommandWithAutoConnect(command);

            if (response == null) {
                LOGGER.warning("⚠Response = null (lỗi kết nối / serialization)");
                return false;
            }

            if (!response.isSuccess()) {
                LOGGER.warning("Command failed: " + response.getMessage());
                return false;
            }

            return true;

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Client error", e);
            return false;
        }
    }

    // =========================================================
    // 🔐 AUTH
    // =========================================================
    public boolean login(String username, String password) {
        try {
            Response response = sendCommandWithAutoConnect(
                    RequestBuilder.login(username, password)
            );

            if (response.isSuccess()) {
                this.sessionId = (String) response.getData();
                return true;
            }

            return false;

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Login error", e);
            return false;
        }
    }

    public void logout() {
        send(RequestBuilder.logout(sessionId));
        sessionId = null;
    }

    // =========================================================
    // 🧾 HOADON
    // =========================================================
    public List<?> getHoaDonList() {
        List<?> result = send(RequestBuilder.getHoaDonList());
        return result != null ? result : new ArrayList<>();
    }

    public Object getHoaDonById(String maHD) {
        return send(RequestBuilder.getHoaDonById(maHD));
    }

    public boolean createHoaDon(Object dto) {
        Boolean rs = send(RequestBuilder.createHoaDon((HoaDonDTO) dto));
        return rs != null && rs;
    }

    public boolean updateHoaDon(Object dto) {
        Boolean rs = send(RequestBuilder.updateHoaDon((HoaDonDTO) dto));
        return rs != null && rs;
    }

    public boolean deleteHoaDon(String maHD) {
        Boolean rs = send(RequestBuilder.deleteHoaDon(maHD));
        return rs != null && rs;
    }

    // =========================================================
    // 👥 NHANVIEN
    // =========================================================
    public List<?> getNhanVienList() {
        List<?> rs = send(RequestBuilder.getNhanVienList());
        return rs != null ? rs : new ArrayList<>();
    }

    public boolean createNhanVien(Object dto) {
        Boolean rs = send(RequestBuilder.createNhanVien((NhanVienDTO) dto));
        return rs != null && rs;
    }

    public boolean updateNhanVien(Object dto) {
        Boolean rs = send(RequestBuilder.updateNhanVien((NhanVienDTO) dto));
        return rs != null && rs;
    }

    public boolean deleteNhanVien(String id) {
        Boolean rs = send(RequestBuilder.deleteNhanVien(id));
        return rs != null && rs;
    }

    // =========================================================
    // 👤 KHACHHANG
    // =========================================================
    public List<?> getKhachHangList() {
        List<?> rs = send(RequestBuilder.getKhachHangList());
        return rs != null ? rs : new ArrayList<>();
    }

    public Object getKhachHangByPhone(String phone) {
        return send(RequestBuilder.getKhachHangByPhone(phone));
    }

    public List<?> searchKhachHangByName(String name) {
        List<?> rs = send(RequestBuilder.searchKhachHangByName(name));
        return rs != null ? rs : new ArrayList<>();
    }


    public KhachHangDTO getKhachHangById(String maKH) {
        return send(RequestBuilder.getKhachHangById(maKH));
    }

    // =========================================================
    // 💊 THUOC
    // =========================================================
    public List<?> getThuocList() {
        List<?> rs = send(RequestBuilder.getThuocList());
        return rs != null ? rs : new ArrayList<>();
    }

    public boolean createThuoc(Object dto) {
        Boolean rs = sendForSuccess(RequestBuilder.createThuoc((ThuocDTO) dto));
        return rs != null && rs;
    }

    // =========================================================
    // 📦 PHIEUNHAP
    // =========================================================
    public List<?> getPhieuNhapList() {
        List<?> rs = send(RequestBuilder.getPhieuNhapList());
        return rs != null ? rs : new ArrayList<>();
    }

    public boolean createPhieuNhap(Object dto) {
        Boolean rs = send(RequestBuilder.createPhieuNhap((PhieuNhapDTO) dto));
        return rs != null && rs;
    }

    public boolean cancelPhieuNhap(String maPN) {
        Boolean rs = send(RequestBuilder.cancelPhieuNhap(maPN));
        return rs != null && rs;
    }

    // =========================================================
    // 📦 PHIEUDAT
    // =========================================================
    public List<?> getPhieuDatList() {
        List<?> rs = send(RequestBuilder.getPhieuDatList());
        return rs != null ? rs : new ArrayList<>();
    }

    public boolean createPhieuDat(Object dto) {
        Boolean rs = send(RequestBuilder.createPhieuDat((PhieuDatThuocDTO) dto));
        return rs != null && rs;
    }

    // =========================================================
    // 🧾 CHITIET HOADON
    // =========================================================
    public List<?> getChiTietHoaDon(String maHD) {
        List<?> rs = send(RequestBuilder.getChiTietHoaDon(maHD));
        return rs != null ? rs : new ArrayList<>();
    }

    public boolean createChiTietHoaDon(Object dto) {
        Boolean rs = send(RequestBuilder.createChiTietHoaDon((ChiTietHoaDonDTO) dto));
        return rs != null && rs;
    }

    // =========================================================
    // 📊 THONGKE
    // =========================================================
    public Object thongKeTrangChinh() {
        return send(RequestBuilder.thongKeTrangChinh());
    }

    public List<?> doanhThuTheoThoiGian(Object payload) {
        List<?> rs = send(RequestBuilder.doanhThuTheoThoiGian((java.util.Map<String, Object>) payload));
        return rs != null ? rs : new ArrayList<>();
    }

    // =========================================================
    // ⚙️ SYSTEM
    // =========================================================
    public void ping() {
        send(RequestBuilder.ping());
    }

    public Object serverStatus() {
        return send(RequestBuilder.serverStatus());
    }

    // =========================================================
    // 🔌 NETWORK (GIỮ NGUYÊN CỦA BẠN)
    // =========================================================
    private Response sendCommand(Command command) throws IOException {
        if (!isConnected()) {
            throw new IOException("Not connected to server");
        }
        return client.sendCommand(command);
    }

    private Response sendCommandWithAutoConnect(Command command) throws IOException {
        if (!isConnected()) {
            // đảm bảo clean connection cũ trước khi reconnect
            if (client != null) {
                client.disconnect();
            }

            if (!connectToServer()) {
                throw new IOException("Cannot connect to server");
            }
        }
        return sendCommand(command);
    }

    public boolean createKhuyenMai(KhuyenMaiDTO khuyenMaiDTO) {
        Boolean rs = send(RequestBuilder.createKhuyenMai(khuyenMaiDTO));
        return rs != null && rs;
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
            if (response.isSuccess() && response.getData() instanceof List<?>) {
                return toTypedList((List<?>) response.getData(), LoThuocDTO.class);
            }

            LOGGER.warning("Failed to get LoThuoc FEFO by Thuoc ID: " + response.getMessage());
            return new ArrayList<>();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting LoThuoc FEFO by Thuoc ID", e);
            return new ArrayList<>();
        }
    }

    public boolean tonTaiChiTietHoaDon(String maHD, int maLoThuoc) {
        Boolean rs = send(RequestBuilder.tonTaiCTHD(maHD , maLoThuoc));
        return rs != null && rs;
    }

    public DonViTinhDTO getDonViTinhById(int maDVT) {
        DonViTinhDTO  donViTinhDTO = send(RequestBuilder.getDonViTinhById(maDVT));
        return donViTinhDTO;
    }

    public List<KeDTO> getActiveKeList() {
        List<?> keDTOList = send(RequestBuilder.getActiveKeList());
        return toTypedList(keDTOList, KeDTO.class);
    }

    public ThuocDTO getThuocById(String maThuoc) {
        ThuocDTO thuoc = send(RequestBuilder.getThuocById(maThuoc));
        return thuoc;
    }

    public List<DangDieuCheDTO> getActiveDangDieuCheList() {
        List<?> list = send(RequestBuilder.getActivceDDCList());
        return toTypedList(list, DangDieuCheDTO.class);
    }

    public List<LoThuocDTO> getLoThuocList() {
        List<?> list = send(RequestBuilder.getLoThuocList());
        return toTypedList(list, LoThuocDTO.class);
    }

    public KhuyenMaiDTO getKhuyenMaiById(String maKM) {
        KhuyenMaiDTO a = send(RequestBuilder.getKhuyenMaibyId());
        return a;
    }

    public boolean updateThuoc(ThuocDTO thuocDTO) {
        Boolean send = sendForSuccess(RequestBuilder.updateThuoc(thuocDTO));
        System.out.println(send);
        return send != null && send;
    }

    public boolean deleteThuoc(String maThuoc) {
        Boolean send = sendForSuccess(RequestBuilder.deleteThuoc(maThuoc));
        return send != null && send;
    }

    public List<DonViTinhDTO> getDonViTinhList() {
        List<?> list = send(RequestBuilder.getDonViTinhList());
        return toTypedList(list, DonViTinhDTO.class);
    }

    public LoThuocDTO getLoThuocByLoThuocId(int maLoThuoc) {
        LoThuocDTO loThuocDTO = send(RequestBuilder.getLoThuocByLoThuocId(maLoThuoc));
        return loThuocDTO;
    }

    public void softDeleteChiTietHoaDon(String maHD, int maLoThuoc, String tinhTrang) {
        send(RequestBuilder.softDeleteChiTietHoaDon(maHD, maLoThuoc, tinhTrang));
    }

    public void updateLoThuocQuantity(int maLoThuoc, int soLuong) {
        send(RequestBuilder.updateLoThuocQuantity(maLoThuoc,soLuong));
    }

    public void updateHoaDonTongTien(String maHD, double v) {
        send(RequestBuilder.updateHoaDonTongTien(maHD,v));
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

    private <T> List<T> toTypedList(List<?> source, Class<T> targetClass) {
        List<T> result = new ArrayList<>();
        if (source == null) {
            return result;
        }
        for (Object item : source) {
            if (targetClass.isInstance(item)) {
                result.add(targetClass.cast(item));
            }
        }
        return result;
    }

    public List<ChiTietHoaDonDTO> getChiTietHoaDonConBanByHoaDonId(String maHD) {
        return fetchChiTietHoaDonList(CommandType.GET_CHITIETHOADON_ACTIVE_BY_HOADON_ID, maHD, "Failed to get active ChiTietHoaDon list by HoaDon ID");
    }

    public List<ChiTietHoaDonDTO> getChiTietHoaDonByHoaDonId(String maHD) {
        return fetchChiTietHoaDonList(CommandType.GET_CHITIETHOADON_BY_HOADON_ID, maHD, "Failed to get ChiTietHoaDon list by HoaDon ID");
    }

    public List<LoaiKhuyenMaiDTO> getLoaiKhuyenMaiList() {
        List<LoaiKhuyenMaiDTO> list = send(RequestBuilder.getLoaiKhuyenMaiList());
        return  list;
    }
    private <T> List<T> fetchList(CommandType commandType, String logMessage) {
        try {
            Command command = Command.builder()
                    .type(commandType)
                    .sessionId(sessionId)
                    .timestamp(System.currentTimeMillis())
                    .build();

            Response response = sendCommandWithAutoConnect(command);

            if (response.isSuccess() && response.getData() instanceof List<?>) {
                return (List<T>) response.getData();
            }

            LOGGER.warning(logMessage + ": " + response.getMessage());
            return new ArrayList<>();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, logMessage, e);
            return new ArrayList<>();
        }
    }

    public List<KhuyenMaiDTO> getKhuyenMaiList() {
        return fetchList(CommandType.GET_KHUYENMAI_LIST, "Failed to get KhuyenMai list");
    }

    public List<KhuyenMaiDTO> getKhuyenMaiDeletedList() {
        return fetchList(CommandType.GET_KHUYENMAI_DELETED_LIST, "Failed to get deleted KhuyenMai list");
    }

    public List<KhuyenMaiDTO> getKhuyenMaiActiveList() {
        return fetchList(CommandType.GET_KHUYENMAI_ACTIVE_LIST, "Failed to get active KhuyenMai list");
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


    public boolean restoreKhuyenMai(String maKM) {
        return executeKhuyenMaiIdWrite(CommandType.RESTORE_KHUYENMAI, maKM, "Error restoring KhuyenMai");
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

    public boolean updateKhuyenMai(KhuyenMaiDTO dto) {
        return executeKhuyenMaiWrite(CommandType.UPDATE_KHUYENMAI, dto, "Error updating KhuyenMai");
    }

    public boolean deleteKhuyenMai(String maKM) {
        return executeKhuyenMaiIdWrite(CommandType.DELETE_KHUYENMAI, maKM, "Error deleting KhuyenMai");
    }


    public int getTongSoThuoc() {
        try {
            Command command = Command.builder()
                    .type(CommandType.GET_THONGKE_TRANGCHINH)
                    .payload(Map.of("type", "TONG_SO_THUOC"))
                    .sessionId(sessionId)
                    .timestamp(System.currentTimeMillis())
                    .build();

            Response response = sendCommandWithAutoConnect(command);
            if (response.isSuccess() && response.getData() instanceof Integer) {
                return (Integer) response.getData();
            }

            LOGGER.warning("Failed to get TongSoThuoc: " + response.getMessage());
            return 0;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting TongSoThuoc", e);
            return 0;
        }
    }

    public int getTongSoNhanVien() {
        try {
            Command command = Command.builder()
                    .type(CommandType.GET_THONGKE_TRANGCHINH)
                    .payload(Map.of("type", "TONG_SO_NHANVIEN"))
                    .sessionId(sessionId)
                    .timestamp(System.currentTimeMillis())
                    .build();

            Response response = sendCommandWithAutoConnect(command);
            if (response.isSuccess() && response.getData() instanceof Integer) {
                return (Integer) response.getData();
            }

            LOGGER.warning("Failed to get TongSoNhanVien: " + response.getMessage());
            return 0;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting TongSoNhanVien", e);
            return 0;
        }
    }

    public int getSoHoaDonHomNay() {
        try {
            Command command = Command.builder()
                    .type(CommandType.GET_THONGKE_TRANGCHINH)
                    .payload(Map.of("type", "SO_HOADON_HOMNAY"))
                    .sessionId(sessionId)
                    .timestamp(System.currentTimeMillis())
                    .build();

            Response response = sendCommandWithAutoConnect(command);
            if (response.isSuccess() && response.getData() instanceof Integer) {
                return (Integer) response.getData();
            }

            LOGGER.warning("Failed to get SoHoaDonHomNay: " + response.getMessage());
            return 0;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting SoHoaDonHomNay", e);
            return 0;
        }
    }

    public int getSoKhuyenMaiApDung() {
        try {
            Command command = Command.builder()
                    .type(CommandType.GET_THONGKE_TRANGCHINH)
                    .payload(Map.of("type", "SO_KHUYENMAI_APDUNG"))
                    .sessionId(sessionId)
                    .timestamp(System.currentTimeMillis())
                    .build();

            Response response = sendCommandWithAutoConnect(command);
            if (response.isSuccess() && response.getData() instanceof Integer) {
                return (Integer) response.getData();
            }

            LOGGER.warning("Failed to get SoKhuyenMaiApDung: " + response.getMessage());
            return 0;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting SoKhuyenMaiApDung", e);
            return 0;
        }
    }

    public Map<String, Double> getDoanhThu7NgayGanNhat() {
        try {
            Command command = Command.builder()
                    .type(CommandType.GET_THONGKE_TRANGCHINH)
                    .payload(Map.of("type", "DOANHTHU_7NGAY"))
                    .sessionId(sessionId)
                    .timestamp(System.currentTimeMillis())
                    .build();

            Response response = sendCommandWithAutoConnect(command);
            if (response.isSuccess() && response.getData() instanceof Map) {
                return (Map<String, Double>) response.getData();
            }

            LOGGER.warning("Failed to get DoanhThu7NgayGanNhat: " + response.getMessage());
            return new HashMap<>();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting DoanhThu7NgayGanNhat", e);
            return new HashMap<>();
        }
    }

    public Map<String, Integer> getTopSanPhamBanChay(int limit) {
        try {
            Command command = Command.builder()
                    .type(CommandType.GET_THONGKE_TRANGCHINH)
                    .payload(Map.of("type", "TOP_SANPHAM", "limit", limit))
                    .sessionId(sessionId)
                    .timestamp(System.currentTimeMillis())
                    .build();

            Response response = sendCommandWithAutoConnect(command);
            if (response.isSuccess() && response.getData() instanceof Map) {
                return (Map<String, Integer>) response.getData();
            }

            LOGGER.warning("Failed to get TopSanPhamBanChay: " + response.getMessage());
            return new HashMap<>();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting TopSanPhamBanChay", e);
            return new HashMap<>();
        }
    }

    public List<Map<String, Object>> getThuocSapHetHan() {
        try {
            Command command = Command.builder()
                    .type(CommandType.GET_THONGKE_TRANGCHINH)
                    .payload(Map.of("type", "THUOC_SAP_HETHAN"))
                    .sessionId(sessionId)
                    .timestamp(System.currentTimeMillis())
                    .build();

            Response response = sendCommandWithAutoConnect(command);
            if (response.isSuccess() && response.getData() instanceof List) {
                return (List<Map<String, Object>>) response.getData();
            }

            LOGGER.warning("Failed to get ThuocSapHetHan: " + response.getMessage());
            return new ArrayList<>();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting ThuocSapHetHan", e);
            return new ArrayList<>();
        }
    }

    public List<Map<String, Object>> getThuocTonKhoThap() {
        try {
            Command command = Command.builder()
                    .type(CommandType.GET_THONGKE_TRANGCHINH)
                    .payload(Map.of("type", "THUOC_TONKHO_THAP"))
                    .sessionId(sessionId)
                    .timestamp(System.currentTimeMillis())
                    .build();

            Response response = sendCommandWithAutoConnect(command);
            if (response.isSuccess() && response.getData() instanceof List) {
                return (List<Map<String, Object>>) response.getData();
            }

            LOGGER.warning("Failed to get ThuocTonKhoThap: " + response.getMessage());
            return new ArrayList<>();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting ThuocTonKhoThap", e);
            return new ArrayList<>();
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

    public boolean restoreThuoc(String maThuoc) {
        return executeThuocIdWrite(CommandType.RESTORE_THUOC, maThuoc, "Error restoring Thuoc");
    }

    public List<ThuocDTO> getDeletedThuocList() {
        return fetchList(CommandType.GET_THUOC_DELETED_LIST, "Failed to get deleted Thuoc list");
    }


    public NhanVienDTO getNhanVienByTaiKhoan(String text) {
        NhanVienDTO nhanVienDTO = send(RequestBuilder.getNhanVienByTaiKhoan(text));
        return nhanVienDTO;

    }
}


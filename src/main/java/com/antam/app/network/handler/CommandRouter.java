package com.antam.app.network.handler;

import com.antam.app.network.message.Command;
import com.antam.app.network.message.Response;
import com.antam.app.network.command.CommandType;
import com.antam.app.dto.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * @description: Router để dispatch commands tới các service handlers
 * Sử dụng ServiceLocator pattern để quản lý service instances
 * @author: Pham Dang Khoa
 * @date: 21/04/2026
 * @version: 1.0
 */
public class CommandRouter {
    private static final Logger LOGGER = Logger.getLogger(CommandRouter.class.getName());
    private final ServiceLocator serviceLocator;

    public CommandRouter() {
        this.serviceLocator = ServiceLocator.getInstance();
    }

    public Response route(Command command, String clientId) {
        if (command == null) {
            return Response.builder()
                    .success(false)
                    .message("Command cannot be null")
                    .errorCode("INVALID_COMMAND")
                    .build();
        }

        try {
            CommandType commandType = command.getType();

            switch(commandType) {
                // HoaDon Operations
                case GET_HOADON_LIST:
                    return handleGetHoaDonList(command);
                case GET_HOADON_BY_ID:
                    return handleGetHoaDonById(command);
                case CREATE_HOADON:
                    return handleCreateHoaDon(command);
                case UPDATE_HOADON:
                    return handleUpdateHoaDon(command);
                case UPDATE_HOADON_TOTAL:
                    return handleUpdateHoaDonTotal(command);
                case DELETE_HOADON:
                    return handleDeleteHoaDon(command);

                // ChiTietHoaDon Operations
                case GET_CHITIETHOADON_BY_HOADON_ID:
                    return handleGetChiTietHoaDonByHoaDonId(command);
                case GET_CHITIETHOADON_ACTIVE_BY_HOADON_ID:
                    return handleGetChiTietHoaDonActiveByHoaDonId(command);
                case CHECK_CHITIETHOADON_EXISTS:
                    return handleCheckChiTietHoaDonExists(command);
                case SOFT_DELETE_CHITIETHOADON:
                    return handleSoftDeleteChiTietHoaDon(command);
                case CREATE_CHITIETHOADON:
                    return handleCreateChiTietHoaDon(command);

                // NhanVien Operations
                case GET_NHANVIEN_LIST:
                    return handleGetNhanVienList(command);
                case GET_NHANVIEN_TAIKHOAN:
                     return handleGetNhanVienByTaiKhoan(command);

                // KhachHang Operations
                case GET_KHACHHANG_LIST:
                    return handleGetKhachHangList(command);
                case GET_KHACHHANG_BY_ID:
                    return handleGetKhachHangById(command);

                // Authentication
                case LOGIN:
                    return handleLogin(command);
                case LOGOUT:
                    return handleLogout(command);

                // System
                case SERVER_STATUS:
                    return handleServerStatus(command);

                // Thuoc Operations
                case GET_THUOC_LIST:
                    return handleGetThuocList();
                case GET_THUOC_DELETED_LIST:
                    return handleGetThuocDeletedList();
                case GET_THUOC_BY_ID:
                    return handleGetThuocById(command);
                case CREATE_THUOC:
                    return handleCreateThuoc(command);
                case UPDATE_THUOC:
                    return handleUpdateThuoc(command);
                case DELETE_THUOC:
                    return handleDeleteThuoc(command);
                case RESTORE_THUOC:
                    return handleRestoreThuoc(command);

                // LoThuoc Operations
                case GET_LOTHUOC_LIST:
                    return handleGetLoThuocList();
                case GET_LOTHUOC_BY_ID:
                    return handleGetLoThuocById(command);
                case GET_LOTHUOC_BY_THUOC_ID:
                    return handleGetLoThuocByThuocId(command);
                case GET_LOTHUOC_FEFO_BY_THUOC_ID:
                    return handleGetLoThuocFefoByThuocId(command);
                case UPDATE_LOTHUOC_QUANTITY:
                    return handleUpdateLoThuocQuantity(command);

                // Lookup Operations
                case GET_KE_ACTIVE_LIST:
                    return handleGetKeActiveList();
                case GET_DANGDIEUCHE_ACTIVE_LIST:
                    return handleGetDangDieuCheActiveList();
                case GET_DONVITINH_LIST:
                    return handleGetDonViTinhList();
                case GET_DONVITINH_BY_ID:
                    return handleGetDonViTinhById(command);

                // KhuyenMai Operations
                case GET_KHUYENMAI_LIST:
                    return handleGetKhuyenMaiList();
                case GET_KHUYENMAI_BY_ID:
                    return handleGetKhuyenMaiById(command);
                case GET_KHUYENMAI_ACTIVE_LIST:
                    return handleGetKhuyenMaiActiveList();
                case GET_KHUYENMAI_DELETED_LIST:
                    return handleGetKhuyenMaiDeletedList();
                case CREATE_KHUYENMAI:
                    return handleCreateKhuyenMai(command);
                case UPDATE_KHUYENMAI:
                    return handleUpdateKhuyenMai(command);
                case DELETE_KHUYENMAI:
                    return handleDeleteKhuyenMai(command);
                case RESTORE_KHUYENMAI:
                    return handleRestoreKhuyenMai(command);

                // LoaiKhuyenMai Operations
                case GET_LOAIKHUYENMAI_LIST:
                    return handleGetLoaiKhuyenMaiList();

                // ThongKe Operations
                case GET_THONGKE_TRANGCHINH:
                    return handleGetThongKeTrangChinh(command);

                default:
                    return Response.builder()
                            .success(false)
                            .message("Unknown command type: " + commandType)
                            .errorCode("UNKNOWN_COMMAND")
                            .build();
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error routing command", e);
            return Response.builder()
                    .success(false)
                    .message("Error: " + e.getMessage())
                    .errorCode("SERVER_ERROR")
                    .build();
        }
    }


    // === HoaDon Handlers ===

    private Response handleGetHoaDonList(Command command) {
        try {
            ArrayList<HoaDonDTO> hoaDonList = serviceLocator.getHoaDonService().getAllHoaDon();
            return Response.builder()
                    .success(true)
                    .message("HoaDon list retrieved successfully")
                    .data(hoaDonList)
                    .build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting HoaDon list", e);
            return Response.builder()
                    .success(false)
                    .message("Error retrieving HoaDon list: " + e.getMessage())
                    .errorCode("GET_HOADON_ERROR")
                    .build();
        }
    }

    private Response handleGetHoaDonById(Command command) {
        try {
            if (command.getPayload() == null || !command.getPayload().containsKey("maHD")) {
                return Response.builder().success(false).message("Invalid payload for get HoaDon by ID").errorCode("INVALID_PAYLOAD").build();
            }
            String maHD = (String) command.getPayload().get("maHD");
            HoaDonDTO dto = serviceLocator.getHoaDonService().getHoaDonTheoMa(maHD);
            return Response.builder().success(true).message("HoaDon retrieved successfully").data(dto).build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting HoaDon by ID", e);
            return Response.builder().success(false).message("Error retrieving HoaDon: " + e.getMessage()).errorCode("GET_HOADON_BY_ID_ERROR").build();
        }
    }

    private Response handleCreateHoaDon(Command command) {
        try {
            if (command.getPayload() == null || !command.getPayload().containsKey("hoaDon")) {
                return Response.builder()
                        .success(false)
                        .message("Invalid payload for create HoaDon")
                        .errorCode("INVALID_PAYLOAD")
                        .build();
            }

            // Convert payload to DTO
            // In production, use Jackson ObjectMapper for this
            // HoaDonDTO dto = objectMapper.convertValue(command.getPayload().get("hoaDon"), HoaDonDTO.class);

            LOGGER.info("HoaDon creation command received");

            return Response.builder()
                    .success(true)
                    .message("HoaDon created successfully")
                    .build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error creating HoaDon", e);
            return Response.builder()
                    .success(false)
                    .message("Error creating HoaDon: " + e.getMessage())
                    .errorCode("CREATE_HOADON_ERROR")
                    .build();
        }
    }

    private Response handleUpdateHoaDon(Command command) {
        try {
            if (command.getPayload() == null || !command.getPayload().containsKey("hoaDon")) {
                return Response.builder().success(false).message("Invalid payload for update HoaDon").errorCode("INVALID_PAYLOAD").build();
            }
            HoaDonDTO dto = (HoaDonDTO) command.getPayload().get("hoaDon");
            boolean success = serviceLocator.getHoaDonService().updateHoaDon(dto);
            return Response.builder().success(success).message(success ? "HoaDon updated successfully" : "Failed to update HoaDon").errorCode(success ? null : "UPDATE_HOADON_FAILED").build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error updating HoaDon", e);
            return Response.builder()
                    .success(false)
                    .message("Error updating HoaDon: " + e.getMessage())
                    .errorCode("UPDATE_HOADON_ERROR")
                    .build();
        }
    }

    private Response handleUpdateHoaDonTotal(Command command) {
        try {
            if (command.getPayload() == null || !command.getPayload().containsKey("maHD") || !command.getPayload().containsKey("tongTien")) {
                return Response.builder().success(false).message("Invalid payload for update HoaDon total").errorCode("INVALID_PAYLOAD").build();
            }
            String maHD = (String) command.getPayload().get("maHD");
            double tongTien = (Double) command.getPayload().get("tongTien");
            boolean success = serviceLocator.getHoaDonService().CapNhatTongTienHoaDon(maHD, tongTien);
            return Response.builder().success(success).message(success ? "HoaDon total updated successfully" : "Failed to update HoaDon total").errorCode(success ? null : "UPDATE_HOADON_TOTAL_FAILED").build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error updating HoaDon total", e);
            return Response.builder()
                    .success(false)
                    .message("Error updating HoaDon total: " + e.getMessage())
                    .errorCode("UPDATE_HOADON_TOTAL_ERROR")
                    .build();
        }
    }

    private Response handleDeleteHoaDon(Command command) {
        try {
            if (command.getPayload() == null || !command.getPayload().containsKey("maHD")) {
                return Response.builder()
                        .success(false)
                        .message("Invalid payload for delete HoaDon")
                        .errorCode("INVALID_PAYLOAD")
                        .build();
            }

            String maHD = (String) command.getPayload().get("maHD");
            serviceLocator.getHoaDonService().xoaMemHoaDon(maHD);

            return Response.builder()
                    .success(true)
                    .message("HoaDon deleted successfully")
                    .build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error deleting HoaDon", e);
            return Response.builder()
                    .success(false)
                    .message("Error deleting HoaDon: " + e.getMessage())
                    .errorCode("DELETE_HOADON_ERROR")
                    .build();
        }
    }

    // === NhanVien Handlers ===

    private Response handleGetNhanVienList(Command command) {
        try {
            List<NhanVienDTO> nhanVienList = serviceLocator.getNhanVienService().getAllNhanVien();
            return Response.builder()
                    .success(true)
                    .message("NhanVien list retrieved successfully")
                    .data(nhanVienList)
                    .build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting NhanVien list", e);
            return Response.builder()
                    .success(false)
                    .message("Error retrieving NhanVien list: " + e.getMessage())
                    .errorCode("GET_NHANVIEN_ERROR")
                    .build();
        }
    }


    private Response handleGetNhanVienByTaiKhoan(Command command) {
        try {
            if (command.getPayload() == null || !command.getPayload().containsKey("taiKhoan")) {
                return Response.builder().success(false).message("Invalid payload for get NhanVien by TaiKhoan").errorCode("INVALID_PAYLOAD").build();
            }
            String taiKhoan = (String) command.getPayload().get("taiKhoan");
            NhanVienDTO nhanVien = serviceLocator.getNhanVienService().getNhanVienTaiKhoan(taiKhoan);
            return Response.builder()
                    .success(true)
                    .message("NhanVien retrieved successfully")
                    .data(nhanVien)
                    .build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting NhanVien by TaiKhoan", e);
            return Response.builder()
                    .success(false)
                    .message("Error retrieving NhanVien: " + e.getMessage())
                    .errorCode("GET_NHANVIEN_BY_TAIKHOAN_ERROR")
                    .build();
        }
    }


    // === KhachHang Handlers ===

    private Response handleGetKhachHangList(Command command) {
        try {
            ArrayList<KhachHangDTO> khachHangList = serviceLocator.getKhachHangService().getAllKhachHang();
            return Response.builder()
                    .success(true)
                    .message("KhachHang list retrieved successfully")
                    .data(khachHangList)
                    .build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting KhachHang list", e);
            return Response.builder()
                    .success(false)
                    .message("Error retrieving KhachHang list: " + e.getMessage())
                    .errorCode("GET_KHACHHANG_ERROR")
                    .build();
        }
    }

    private Response handleGetKhachHangById(Command command) {
        try {
            if (command.getPayload() == null || !command.getPayload().containsKey("maKH")) {
                return Response.builder().success(false).message("Invalid payload for get KhachHang by ID").errorCode("INVALID_PAYLOAD").build();
            }
            String maKH = (String) command.getPayload().get("maKH");
            KhachHangDTO dto = serviceLocator.getKhachHangService().getKhachHangTheoMa(maKH);
            return Response.builder().success(true).message("KhachHang retrieved successfully").data(dto).build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting KhachHang by ID", e);
            return Response.builder().success(false).message("Error retrieving KhachHang: " + e.getMessage()).errorCode("GET_KHACHHANG_BY_ID_ERROR").build();
        }
    }

    // === ChiTietHoaDon Handlers ===

    private Response handleGetChiTietHoaDonByHoaDonId(Command command) {
        try {
            if (command.getPayload() == null || !command.getPayload().containsKey("maHD")) {
                return Response.builder().success(false).message("Invalid payload for get ChiTietHoaDon by HoaDon ID").errorCode("INVALID_PAYLOAD").build();
            }
            String maHD = (String) command.getPayload().get("maHD");
            ArrayList<ChiTietHoaDonDTO> list = serviceLocator.getChiTietHoaDonService().getAllChiTietHoaDonTheoMaHD(maHD);
            return Response.builder().success(true).message("ChiTietHoaDon list retrieved successfully").data(list).build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting ChiTietHoaDon by HoaDon ID", e);
            return Response.builder().success(false).message("Error retrieving ChiTietHoaDon list: " + e.getMessage()).errorCode("GET_CHITIETHOADON_BY_HOADON_ID_ERROR").build();
        }
    }

    private Response handleGetChiTietHoaDonActiveByHoaDonId(Command command) {
        try {
            if (command.getPayload() == null || !command.getPayload().containsKey("maHD")) {
                return Response.builder().success(false).message("Invalid payload for get active ChiTietHoaDon by HoaDon ID").errorCode("INVALID_PAYLOAD").build();
            }
            String maHD = (String) command.getPayload().get("maHD");
            ArrayList<ChiTietHoaDonDTO> list = serviceLocator.getChiTietHoaDonService().getAllChiTietHoaDonTheoMaHDConBan(maHD);
            return Response.builder().success(true).message("Active ChiTietHoaDon list retrieved successfully").data(list).build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting active ChiTietHoaDon by HoaDon ID", e);
            return Response.builder().success(false).message("Error retrieving active ChiTietHoaDon list: " + e.getMessage()).errorCode("GET_CHITIETHOADON_ACTIVE_BY_HOADON_ID_ERROR").build();
        }
    }

    private Response handleCheckChiTietHoaDonExists(Command command) {
        try {
            if (command.getPayload() == null || !command.getPayload().containsKey("maHD") || !command.getPayload().containsKey("maLoThuoc")) {
                return Response.builder().success(false).message("Invalid payload for check ChiTietHoaDon exists").errorCode("INVALID_PAYLOAD").build();
            }
            String maHD = (String) command.getPayload().get("maHD");
            int maLoThuoc = (Integer) command.getPayload().get("maLoThuoc");
            boolean exists = serviceLocator.getChiTietHoaDonService().tonTaiChiTietHoaDon(maHD, maLoThuoc);
            return Response.builder().success(true).message("ChiTietHoaDon existence checked").data(exists).build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error checking ChiTietHoaDon exists", e);
            return Response.builder().success(false).message("Error checking ChiTietHoaDon existence: " + e.getMessage()).errorCode("CHECK_CHITIETHOADON_EXISTS_ERROR").build();
        }
    }

    private Response handleSoftDeleteChiTietHoaDon(Command command) {
        try {
            if (command.getPayload() == null || !command.getPayload().containsKey("maHD") || !command.getPayload().containsKey("maLoThuoc") || !command.getPayload().containsKey("tinhTrang")) {
                return Response.builder().success(false).message("Invalid payload for soft delete ChiTietHoaDon").errorCode("INVALID_PAYLOAD").build();
            }
            String maHD = (String) command.getPayload().get("maHD");
            int maLoThuoc = (Integer) command.getPayload().get("maLoThuoc");
            String tinhTrang = (String) command.getPayload().get("tinhTrang");
            boolean success = serviceLocator.getChiTietHoaDonService().xoaMemChiTietHoaDon(maHD, maLoThuoc, tinhTrang);
            return Response.builder().success(success).message(success ? "ChiTietHoaDon soft deleted successfully" : "Failed to soft delete ChiTietHoaDon").errorCode(success ? null : "SOFT_DELETE_CHITIETHOADON_FAILED").build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error soft deleting ChiTietHoaDon", e);
            return Response.builder().success(false).message("Error soft deleting ChiTietHoaDon: " + e.getMessage()).errorCode("SOFT_DELETE_CHITIETHOADON_ERROR").build();
        }
    }

    private Response handleCreateChiTietHoaDon(Command command) {
        try {
            if (command.getPayload() == null || !command.getPayload().containsKey("chiTietHoaDon")) {
                return Response.builder().success(false).message("Invalid payload for create ChiTietHoaDon").errorCode("INVALID_PAYLOAD").build();
            }
            ChiTietHoaDonDTO dto = (ChiTietHoaDonDTO) command.getPayload().get("chiTietHoaDon");
            boolean success = serviceLocator.getChiTietHoaDonService().themChiTietHoaDon(dto);
            return Response.builder().success(success).message(success ? "ChiTietHoaDon created successfully" : "Failed to create ChiTietHoaDon").errorCode(success ? null : "CREATE_CHITIETHOADON_FAILED").build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error creating ChiTietHoaDon", e);
            return Response.builder().success(false).message("Error creating ChiTietHoaDon: " + e.getMessage()).errorCode("CREATE_CHITIETHOADON_ERROR").build();
        }
    }

    // === Thuoc Handlers ===

    private Response handleGetThuocList() {
        try {
            ArrayList<ThuocDTO> thuocList = serviceLocator.getThuocService().getAllThuoc();
            return Response.builder().success(true).message("Thuoc list retrieved successfully").data(thuocList).build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting Thuoc list", e);
            return Response.builder().success(false).message("Error retrieving Thuoc list: " + e.getMessage()).errorCode("GET_THUOC_ERROR").build();
        }
    }

    private Response handleGetThuocDeletedList() {
        try {
            ArrayList<ThuocDTO> thuocList = serviceLocator.getThuocService().getAllThuocDaXoa();
            return Response.builder().success(true).message("Deleted Thuoc list retrieved successfully").data(thuocList).build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting deleted Thuoc list", e);
            return Response.builder().success(false).message("Error retrieving deleted Thuoc list: " + e.getMessage()).errorCode("GET_THUOC_DELETED_ERROR").build();
        }
    }

    private Response handleGetThuocById(Command command) {
        try {
            if (command.getPayload() == null || !command.getPayload().containsKey("maThuoc")) {
                return Response.builder().success(false).message("Invalid payload for get Thuoc by ID").errorCode("INVALID_PAYLOAD").build();
            }
            String maThuoc = (String) command.getPayload().get("maThuoc");
            ThuocDTO thuocDTO = serviceLocator.getThuocService().getThuocTheoMa(maThuoc);
            return Response.builder().success(true).message("Thuoc retrieved successfully").data(thuocDTO).build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting Thuoc by ID", e);
            return Response.builder().success(false).message("Error retrieving Thuoc: " + e.getMessage()).errorCode("GET_THUOC_BY_ID_ERROR").build();
        }
    }

    private Response handleCreateThuoc(Command command) {
        try {
            if (command.getPayload() == null || !command.getPayload().containsKey("thuoc")) {
                return Response.builder().success(false).message("Invalid payload for create Thuoc").errorCode("INVALID_PAYLOAD").build();
            }
            ThuocDTO thuocDTO = (ThuocDTO) command.getPayload().get("thuoc");
            boolean success = serviceLocator.getThuocService().themThuoc(thuocDTO);
            return Response.builder().success(success).message(success ? "Thuoc created successfully" : "Failed to create Thuoc").errorCode(success ? null : "CREATE_THUOC_FAILED").build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error creating Thuoc", e);
            return Response.builder().success(false).message("Error creating Thuoc: " + e.getMessage()).errorCode("CREATE_THUOC_ERROR").build();
        }
    }

    private Response handleUpdateThuoc(Command command) {
        try {
            if (command.getPayload() == null || !command.getPayload().containsKey("thuoc")) {
                return Response.builder().success(false).message("Invalid payload for update Thuoc").errorCode("INVALID_PAYLOAD").build();
            }
            ThuocDTO thuocDTO = (ThuocDTO) command.getPayload().get("thuoc");
            boolean success = serviceLocator.getThuocService().capNhatThuoc(thuocDTO);
            return Response.builder().success(success).message(success ? "Thuoc updated successfully" : "Failed to update Thuoc").errorCode(success ? null : "UPDATE_THUOC_FAILED").build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error updating Thuoc", e);
            return Response.builder().success(false).message("Error updating Thuoc: " + e.getMessage()).errorCode("UPDATE_THUOC_ERROR").build();
        }
    }

    private Response handleDeleteThuoc(Command command) {
        try {
            if (command.getPayload() == null || !command.getPayload().containsKey("maThuoc")) {
                return Response.builder().success(false).message("Invalid payload for delete Thuoc").errorCode("INVALID_PAYLOAD").build();
            }
            String maThuoc = (String) command.getPayload().get("maThuoc");
            boolean success = serviceLocator.getThuocService().xoaThuocTheoMa(maThuoc);
            return Response.builder().success(success).message(success ? "Thuoc deleted successfully" : "Failed to delete Thuoc").errorCode(success ? null : "DELETE_THUOC_FAILED").build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error deleting Thuoc", e);
            return Response.builder().success(false).message("Error deleting Thuoc: " + e.getMessage()).errorCode("DELETE_THUOC_ERROR").build();
        }
    }

    private Response handleRestoreThuoc(Command command) {
        try {
            if (command.getPayload() == null || !command.getPayload().containsKey("maThuoc")) {
                return Response.builder().success(false).message("Invalid payload for restore Thuoc").errorCode("INVALID_PAYLOAD").build();
            }
            String maThuoc = (String) command.getPayload().get("maThuoc");
            boolean success = serviceLocator.getThuocService().khoiPhucThuocTheoMa(maThuoc);
            return Response.builder().success(success).message(success ? "Thuoc restored successfully" : "Failed to restore Thuoc").errorCode(success ? null : "RESTORE_THUOC_FAILED").build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error restoring Thuoc", e);
            return Response.builder().success(false).message("Error restoring Thuoc: " + e.getMessage()).errorCode("RESTORE_THUOC_ERROR").build();
        }
    }

    // === LoThuoc Handlers ===

    private Response handleGetLoThuocList() {
        try {
            ArrayList<LoThuocDTO> loThuocList = serviceLocator.getLoThuocService().getAllChiTietThuoc();
            return Response.builder().success(true).message("LoThuoc list retrieved successfully").data(loThuocList).build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting LoThuoc list", e);
            return Response.builder().success(false).message("Error retrieving LoThuoc list: " + e.getMessage()).errorCode("GET_LOTHUOC_ERROR").build();
        }
    }

    private Response handleGetLoThuocById(Command command) {
        try {
            if (command.getPayload() == null || !command.getPayload().containsKey("maLoThuoc")) {
                return Response.builder().success(false).message("Invalid payload for get LoThuoc by ID").errorCode("INVALID_PAYLOAD").build();
            }
            int maLoThuoc = (Integer) command.getPayload().get("maLoThuoc");
            LoThuocDTO dto = serviceLocator.getLoThuocService().getChiTietThuoc(maLoThuoc);
            return Response.builder().success(true).message("LoThuoc retrieved successfully").data(dto).build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting LoThuoc by ID", e);
            return Response.builder().success(false).message("Error retrieving LoThuoc: " + e.getMessage()).errorCode("GET_LOTHUOC_BY_ID_ERROR").build();
        }
    }

    private Response handleGetLoThuocByThuocId(Command command) {
        try {
            if (command.getPayload() == null || !command.getPayload().containsKey("maThuoc")) {
                return Response.builder().success(false).message("Invalid payload for get LoThuoc by Thuoc ID").errorCode("INVALID_PAYLOAD").build();
            }
            String maThuoc = (String) command.getPayload().get("maThuoc");
            ArrayList<LoThuocDTO> loThuocList = serviceLocator.getLoThuocService().getAllCHiTietThuocTheoMaThuoc(maThuoc);
            return Response.builder().success(true).message("LoThuoc by Thuoc ID retrieved successfully").data(loThuocList).build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting LoThuoc by Thuoc ID", e);
            return Response.builder().success(false).message("Error retrieving LoThuoc by Thuoc ID: " + e.getMessage()).errorCode("GET_LOTHUOC_BY_THUOC_ID_ERROR").build();
        }
    }

    private Response handleGetLoThuocFefoByThuocId(Command command) {
        try {
            if (command.getPayload() == null || !command.getPayload().containsKey("maThuoc")) {
                return Response.builder().success(false).message("Invalid payload for get LoThuoc FEFO by Thuoc ID").errorCode("INVALID_PAYLOAD").build();
            }
            String maThuoc = (String) command.getPayload().get("maThuoc");
            ArrayList<LoThuocDTO> list = serviceLocator.getLoThuocService().getChiTietThuocHanSuDungGiamDan(maThuoc);
            return Response.builder().success(true).message("LoThuoc FEFO list retrieved successfully").data(list).build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting LoThuoc FEFO by Thuoc ID", e);
            return Response.builder().success(false).message("Error retrieving LoThuoc FEFO list: " + e.getMessage()).errorCode("GET_LOTHUOC_FEFO_BY_THUOC_ID_ERROR").build();
        }
    }

    private Response handleUpdateLoThuocQuantity(Command command) {
        try {
            if (command.getPayload() == null || !command.getPayload().containsKey("maLoThuoc") || !command.getPayload().containsKey("deltaSoLuong")) {
                return Response.builder().success(false).message("Invalid payload for update LoThuoc quantity").errorCode("INVALID_PAYLOAD").build();
            }
            int maLoThuoc = (Integer) command.getPayload().get("maLoThuoc");
            int deltaSoLuong = (Integer) command.getPayload().get("deltaSoLuong");
            boolean success = serviceLocator.getLoThuocService().CapNhatSoLuongChiTietThuoc(maLoThuoc, deltaSoLuong);
            return Response.builder().success(success).message(success ? "LoThuoc quantity updated successfully" : "Failed to update LoThuoc quantity").errorCode(success ? null : "UPDATE_LOTHUOC_QUANTITY_FAILED").build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error updating LoThuoc quantity", e);
            return Response.builder().success(false).message("Error updating LoThuoc quantity: " + e.getMessage()).errorCode("UPDATE_LOTHUOC_QUANTITY_ERROR").build();
        }
    }

    // === Lookup Handlers ===

    private Response handleGetKeActiveList() {
        try {
            ArrayList<KeDTO> keList = serviceLocator.getKeService().getTatCaKeHoatDong();
            return Response.builder().success(true).message("Active Ke list retrieved successfully").data(keList).build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting active Ke list", e);
            return Response.builder().success(false).message("Error retrieving active Ke list: " + e.getMessage()).errorCode("GET_KE_ACTIVE_ERROR").build();
        }
    }

    private Response handleGetDangDieuCheActiveList() {
        try {
            ArrayList<DangDieuCheDTO> ddcList = serviceLocator.getDangDieuCheService().getDangDieuCheHoatDong();
            return Response.builder().success(true).message("Active DangDieuChe list retrieved successfully").data(ddcList).build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting active DangDieuChe list", e);
            return Response.builder().success(false).message("Error retrieving active DangDieuChe list: " + e.getMessage()).errorCode("GET_DANGDIEUCHE_ACTIVE_ERROR").build();
        }
    }

    private Response handleGetDonViTinhList() {
        try {
            ArrayList<DonViTinhDTO> dvtList = serviceLocator.getDonViTinhService().getAllDonViTinh();
            return Response.builder().success(true).message("DonViTinh list retrieved successfully").data(dvtList).build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting DonViTinh list", e);
            return Response.builder().success(false).message("Error retrieving DonViTinh list: " + e.getMessage()).errorCode("GET_DONVITINH_ERROR").build();
        }
    }

    private Response handleGetDonViTinhById(Command command) {
        try {
            if (command.getPayload() == null || !command.getPayload().containsKey("maDVT")) {
                return Response.builder().success(false).message("Invalid payload for get DonViTinh by ID").errorCode("INVALID_PAYLOAD").build();
            }
            int maDVT = (Integer) command.getPayload().get("maDVT");
            DonViTinhDTO dvt = serviceLocator.getDonViTinhService().getDVTTheoMaDVT(maDVT);
            return Response.builder().success(true).message("DonViTinh retrieved successfully").data(dvt).build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting DonViTinh by ID", e);
            return Response.builder().success(false).message("Error retrieving DonViTinh: " + e.getMessage()).errorCode("GET_DONVITINH_BY_ID_ERROR").build();
        }
    }

    // === KhuyenMai Handlers ===

    private Response handleGetKhuyenMaiList() {
        try {
            ArrayList<KhuyenMaiDTO> khuyenMaiList = serviceLocator.getKhuyenMaiService().getAllKhuyenMaiChuaXoa();
            return Response.builder().success(true).message("KhuyenMai list retrieved successfully").data(khuyenMaiList).build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting KhuyenMai list", e);
            return Response.builder().success(false).message("Error retrieving KhuyenMai list: " + e.getMessage()).errorCode("GET_KHUYENMAI_ERROR").build();
        }
    }

    private Response handleGetKhuyenMaiById(Command command) {
        try {
            if (command.getPayload() == null || !command.getPayload().containsKey("maKM")) {
                return Response.builder().success(false).message("Invalid payload for get KhuyenMai by ID").errorCode("INVALID_PAYLOAD").build();
            }
            String maKM = (String) command.getPayload().get("maKM");
            KhuyenMaiDTO khuyenMaiDTO = serviceLocator.getKhuyenMaiService().getKhuyenMaiTheoMa(maKM);
            return Response.builder().success(true).message("KhuyenMai retrieved successfully").data(khuyenMaiDTO).build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting KhuyenMai by ID", e);
            return Response.builder().success(false).message("Error retrieving KhuyenMai: " + e.getMessage()).errorCode("GET_KHUYENMAI_BY_ID_ERROR").build();
        }
    }

    private Response handleGetKhuyenMaiActiveList() {
        try {
            ArrayList<KhuyenMaiDTO> khuyenMaiList = new ArrayList<>(serviceLocator.getKhuyenMaiService().getAllKhuyenMaiConHieuLuc());
            return Response.builder().success(true).message("Active KhuyenMai list retrieved successfully").data(khuyenMaiList).build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting active KhuyenMai list", e);
            return Response.builder().success(false).message("Error retrieving active KhuyenMai list: " + e.getMessage()).errorCode("GET_KHUYENMAI_ACTIVE_ERROR").build();
        }
    }

    private Response handleGetKhuyenMaiDeletedList() {
        try {
            ArrayList<KhuyenMaiDTO> khuyenMaiList = serviceLocator.getKhuyenMaiService().getAllKhuyenMaiDaXoa();
            return Response.builder().success(true).message("Deleted KhuyenMai list retrieved successfully").data(khuyenMaiList).build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting deleted KhuyenMai list", e);
            return Response.builder().success(false).message("Error retrieving deleted KhuyenMai list: " + e.getMessage()).errorCode("GET_KHUYENMAI_DELETED_ERROR").build();
        }
    }

    private Response handleCreateKhuyenMai(Command command) {
        try {
            if (command.getPayload() == null || !command.getPayload().containsKey("khuyenMai")) {
                return Response.builder().success(false).message("Invalid payload for create KhuyenMai").errorCode("INVALID_PAYLOAD").build();
            }
            KhuyenMaiDTO khuyenMaiDTO = (KhuyenMaiDTO) command.getPayload().get("khuyenMai");
            boolean success = serviceLocator.getKhuyenMaiService().themKhuyenMai(
                    khuyenMaiDTO.getMaKM(),
                    khuyenMaiDTO.getTenKM(),
                    khuyenMaiDTO.getLoaiKhuyenMaiDTO(),
                    khuyenMaiDTO.getSo(),
                    khuyenMaiDTO.getNgayBatDau(),
                    khuyenMaiDTO.getNgayKetThuc(),
                    khuyenMaiDTO.getSoLuongToiDa());
            return Response.builder().success(success).message(success ? "KhuyenMai created successfully" : "Failed to create KhuyenMai").errorCode(success ? null : "CREATE_KHUYENMAI_FAILED").build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error creating KhuyenMai", e);
            return Response.builder().success(false).message("Error creating KhuyenMai: " + e.getMessage()).errorCode("CREATE_KHUYENMAI_ERROR").build();
        }
    }

    private Response handleUpdateKhuyenMai(Command command) {
        try {
            if (command.getPayload() == null || !command.getPayload().containsKey("khuyenMai")) {
                return Response.builder().success(false).message("Invalid payload for update KhuyenMai").errorCode("INVALID_PAYLOAD").build();
            }
            KhuyenMaiDTO khuyenMaiDTO = (KhuyenMaiDTO) command.getPayload().get("khuyenMai");
            boolean success = serviceLocator.getKhuyenMaiService().capNhatKhuyenMai(khuyenMaiDTO);
            return Response.builder().success(success).message(success ? "KhuyenMai updated successfully" : "Failed to update KhuyenMai").errorCode(success ? null : "UPDATE_KHUYENMAI_FAILED").build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error updating KhuyenMai", e);
            return Response.builder().success(false).message("Error updating KhuyenMai: " + e.getMessage()).errorCode("UPDATE_KHUYENMAI_ERROR").build();
        }
    }

    private Response handleDeleteKhuyenMai(Command command) {
        try {
            if (command.getPayload() == null || !command.getPayload().containsKey("maKM")) {
                return Response.builder().success(false).message("Invalid payload for delete KhuyenMai").errorCode("INVALID_PAYLOAD").build();
            }
            String maKM = (String) command.getPayload().get("maKM");
            boolean success = serviceLocator.getKhuyenMaiService().xoaKhuyenMai(maKM);
            return Response.builder().success(success).message(success ? "KhuyenMai deleted successfully" : "Failed to delete KhuyenMai").errorCode(success ? null : "DELETE_KHUYENMAI_FAILED").build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error deleting KhuyenMai", e);
            return Response.builder().success(false).message("Error deleting KhuyenMai: " + e.getMessage()).errorCode("DELETE_KHUYENMAI_ERROR").build();
        }
    }

    private Response handleRestoreKhuyenMai(Command command) {
        try {
            if (command.getPayload() == null || !command.getPayload().containsKey("maKM")) {
                return Response.builder().success(false).message("Invalid payload for restore KhuyenMai").errorCode("INVALID_PAYLOAD").build();
            }
            String maKM = (String) command.getPayload().get("maKM");
            boolean success = serviceLocator.getKhuyenMaiService().khoiPhucKhuyenMai(maKM);
            return Response.builder().success(success).message(success ? "KhuyenMai restored successfully" : "Failed to restore KhuyenMai").errorCode(success ? null : "RESTORE_KHUYENMAI_FAILED").build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error restoring KhuyenMai", e);
            return Response.builder().success(false).message("Error restoring KhuyenMai: " + e.getMessage()).errorCode("RESTORE_KHUYENMAI_ERROR").build();
        }
    }

    private Response handleGetLoaiKhuyenMaiList() {
        try {
            ArrayList<LoaiKhuyenMaiDTO> loaiKhuyenMaiList = serviceLocator.getLoaiKhuyenMaiService().getAllLoaiKhuyenMai();
            return Response.builder().success(true).message("LoaiKhuyenMai list retrieved successfully").data(loaiKhuyenMaiList).build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting LoaiKhuyenMai list", e);
            return Response.builder().success(false).message("Error retrieving LoaiKhuyenMai list: " + e.getMessage()).errorCode("GET_LOAIKHUYENMAI_ERROR").build();
        }
    }

    private Response handleLogin(Command command) {
        try {
            if (command.getPayload() == null ||
                    !command.getPayload().containsKey("username") ||
                    !command.getPayload().containsKey("password")) {
                return Response.builder()
                        .success(false)
                        .message("Invalid login credentials")
                        .errorCode("INVALID_CREDENTIALS")
                        .build();
            }

            return Response.builder()
                    .success(true)
                    .message("Login successful")
                    .build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error during login", e);
            return Response.builder()
                    .success(false)
                    .message("Login failed: " + e.getMessage())
                    .errorCode("LOGIN_ERROR")
                    .build();
        }
    }

    private Response handleLogout(Command command) {
        try {
            return Response.builder()
                    .success(true)
                    .message("Logout successful")
                    .build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error during logout", e);
            return Response.builder()
                    .success(false)
                    .message("Logout failed: " + e.getMessage())
                    .errorCode("LOGOUT_ERROR")
                    .build();
        }
    }

    private Response handleServerStatus(Command command) {
        try {
            return Response.builder()
                    .success(true)
                    .message("Server is running")
                    .data("OK")
                    .build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error checking server status", e);
            return Response.builder()
                    .success(false)
                    .message("Error checking server status: " + e.getMessage())
                    .errorCode("STATUS_ERROR")
                    .build();
        }
    }

    // === ThongKe Handlers ===

    private Response handleGetThongKeTrangChinh(Command command) {
        try {
            if (command.getPayload() == null || !command.getPayload().containsKey("type")) {
                return Response.builder().success(false).message("Invalid payload for get ThongKeTrangChinh").errorCode("INVALID_PAYLOAD").build();
            }

            String type = (String) command.getPayload().get("type");

            switch (type) {
                case "TONG_SO_THUOC":
                    int tongSoThuoc = serviceLocator.getThongKeTrangChinhService().getTongSoThuoc();
                    return Response.builder().success(true).message("TongSoThuoc retrieved successfully").data(tongSoThuoc).build();

                case "TONG_SO_NHANVIEN":
                    int tongSoNhanVien = serviceLocator.getThongKeTrangChinhService().getTongSoNhanVien();
                    return Response.builder().success(true).message("TongSoNhanVien retrieved successfully").data(tongSoNhanVien).build();

                case "SO_HOADON_HOMNAY":
                    int soHoaDonHomNay = serviceLocator.getThongKeTrangChinhService().getSoHoaDonHomNay();
                    return Response.builder().success(true).message("SoHoaDonHomNay retrieved successfully").data(soHoaDonHomNay).build();

                case "SO_KHUYENMAI_APDUNG":
                    int soKhuyenMaiApDung = serviceLocator.getThongKeTrangChinhService().getSoKhuyenMaiApDung();
                    return Response.builder().success(true).message("SoKhuyenMaiApDung retrieved successfully").data(soKhuyenMaiApDung).build();

                case "DOANHTHU_7NGAY":
                    Map<String, Double> doanhThu7Ngay = serviceLocator.getThongKeTrangChinhService().getDoanhThu7NgayGanNhat();
                    return Response.builder().success(true).message("DoanhThu7NgayGanNhat retrieved successfully").data(doanhThu7Ngay).build();

                case "TOP_SANPHAM":
                    int limit = command.getPayload().containsKey("limit") ? (Integer) command.getPayload().get("limit") : 10;
                    Map<String, Integer> topSanPham = serviceLocator.getThongKeTrangChinhService().getTopSanPhamBanChay(limit);
                    return Response.builder().success(true).message("TopSanPhamBanChay retrieved successfully").data(topSanPham).build();

                case "THUOC_SAP_HETHAN":
                    List<Map<String, Object>> thuocSapHetHan = serviceLocator.getThongKeTrangChinhService().getThuocSapHetHan();
                    return Response.builder().success(true).message("ThuocSapHetHan retrieved successfully").data(thuocSapHetHan).build();

                case "THUOC_TONKHO_THAP":
                    List<Map<String, Object>> thuocTonKhoThap = serviceLocator.getThongKeTrangChinhService().getThuocTonKhoThap();
                    return Response.builder().success(true).message("ThuocTonKhoThap retrieved successfully").data(thuocTonKhoThap).build();

                default:
                    return Response.builder().success(false).message("Unknown ThongKeTrangChinh type: " + type).errorCode("UNKNOWN_THONGKE_TYPE").build();
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting ThongKeTrangChinh", e);
            return Response.builder().success(false).message("Error retrieving ThongKeTrangChinh: " + e.getMessage()).errorCode("GET_THONGKE_TRANGCHINH_ERROR").build();
        }
    }
}

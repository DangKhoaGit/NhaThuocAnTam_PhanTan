package com.antam.app.network.handler;

import com.antam.app.entity.PhieuDatThuoc;
import com.antam.app.helper.MaKhoaMatKhau;
import com.antam.app.network.message.Command;
import com.antam.app.network.message.Response;
import com.antam.app.network.command.CommandType;
import com.antam.app.dto.*;
import com.antam.app.service.impl.NhanVien_Service;

import java.time.LocalDate;
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
                case CREATE_HOADON_WITH_DETAILS:
                    return handleCreateHoaDonWithDetails(command);
                case UPDATE_HOADON:
                    return handleUpdateHoaDon(command);
                case UPDATE_HOADON_TOTAL:
                    return handleUpdateHoaDonTotal(command);
                case DELETE_HOADON:
                    return handleDeleteHoaDon(command);
                case GET_HOADON_BY_KHACHHANG_ID:
                    return handleGetHoaDonByKhachHangId(command);
                case SEARCH_HOADON_BY_MA:
                    return handleSearchHoaDonByMa(command);
                case SEARCH_HOADON_BY_STATUS:
                    return handleSearchHoaDonByStatus(command);
                case SEARCH_HOADON_BY_NHANVIEN:
                    return handleSearchHoaDonByNhanVien(command);
                case COUNT_HOADON_BY_KHUYENMAI:
                    return handleCountHoaDonByKhuyenMai(command);
                case GET_MAX_HASH_HOADON:
                    return handleGetMaxHashHoaDon(command);

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
                case CREATE_CHITIETHOADON_TRA_KHI_DOI:
                    return handleCreateChiTietHoaDon(command); // Có thể cần handler riêng nếu logic khác biệt
                case UPDATE_CHITIETHOADON:
                    return handleUpdateChiTietHoaDon(command); // Cần implement thêm handler này nếu có logic cập nhật khác biệt
                case UPSERT_CHITIETHOADON:
                    return handleCreateChiTietHoaDon(command); // Tạm thời dùng chung handler, cần implement thêm nếu logic khác biệt
                case CHECK_CHITIETHOADON_EXISTS_WITH_STATUS:
                    return handleCheckChiTietHoaDonExists(command); // Cần implement thêm handler nếu cần kiểm tra với status khác biệt

                // NhanVien Operations
                case GET_NHANVIEN_LIST:
                    return handleGetNhanVienList(command);
                case GET_NHANVIEN_TAIKHOAN:
                     return handleGetNhanVienByTaiKhoan(command);
                case GET_NHANVIEN_DELETED_LIST:
                    return handleGetNhanVienDeleteList(command); // Cần implement thêm handler nếu logic khác biệt
                case GET_NHANVIEN_BY_ID:
                    return handleGetNhanVienById(command); // Cần implement thêm handler nếu logic khác biệt
                case CREATE_NHANVIEN:
                    return handleCreateNhanVien(command);
                case UPDATE_NHANVIEN:
                    return handleUpdateNhanVien(command);
                case DELETE_NHANVIEN:
                    return handleDeleteNhanVien(command);
                case KHOI_PHUC_NHAN_VIEN:
                    return handleRestoreNhanVien(command);
                case GET_MAX_HASH_NHANVIEN:
                    return handleGetMaxHashNhanVien(command);



                // KhachHang Operations
                case GET_KHACHHANG_LIST:
                    return handleGetKhachHangList(command);
                case GET_KHACHHANG_BY_ID:
                    return handleGetKhachHangById(command);
                case GET_KHACHHANG_WITH_STATS:
                    return handleGetKhachHangWithStats(command);
                case GET_KHACHHANG_BY_PHONE:
                    return handleGetKhachHangByPhone(command);
                case SEARCH_KHACHHANG_BY_NAME:
                    return handleSearchKhachHangByName(command);
                case CREATE_KHACHHANG:
                    return handleCreateKhachHang(command);
                case UPDATE_KHACHHANG:
                    return handleUpdateKhachHang(command);
                case GET_TONG_KHACHHANG:
                        return handleGetTongKhachHang(command);
                case GET_TONG_KHACHHANG_VIP:
                    return handleGetTongKhachHangVip(command);
                case GET_TONG_DONHANG_KHACHHANG:
                    return handleGetTongDonHangKhachHang(command);
                case GET_MAX_HASH_KHACHHANG:
                    return handleGetMaxHashKhachHang(command);

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
                case CREATE_LOTHUOC:
                    return handleCreateLoThuoc(command);
                case GET_TONG_TONKHO_BY_THUOC_ID:
                    return handleGetTongTonKhoByThuocId(command);
                case GET_LOTHUOC_FOR_CHITIETPHIEUDAT:
                    return handleGetLoThuocForChiTietPhieuDat(command);


                // Lookup Operations
                case GET_KE_ACTIVE_LIST:
                    return handleGetKeActiveList();
                case GET_KE_BY_NAME:
                    return handleGetKeByName(command);
                case GET_KE_BY_ID:
                    return handleGetKeById(command);
                case CREATE_KE:
                     return handleCreateKe(command);
                case UPDATE_KE:
                    return handleUpdateKe(command);
                case DELETE_KE:
                    return handleDeleteKe(command);
                case RESTORE_KE:
                    return handleRestoreKe(command);
                case GENERATE_KE_CODE:
                    return handleGenerateKeCode(command);
                case GET_KE_LIST:
                    return handleGetKeList();
                case CHECK_PHIEUNHAP_EXISTS:
                    return handleCheckKeExists(command);

                    //Dạng điều chế
                case GET_DANGDIEUCHE_ACTIVE_LIST:
                    return handleGetDangDieuCheActiveList();
                case GET_DANGDIEUCHE_BY_NAME:
                    return handleGetDangDieuCheByName(command);
                case CREATE_DANGDIEUCHE:
                    return handleCreateDangDieuChe(command);
                case UPDATE_DANGDIEUCHE:
                    return handleUpdateDangDieuChe(command);
                case DELETE_DANGDIEUCHE:
                    return handleDeleteDangDieuChe(command);
                case RESTORE_DANGDIEUCHE:
                    return handleRestoreDangDieuChe(command);
                case GENERATE_DANGDIEUCHE_CODE:
                    return handleGenerateDangDieuCheCode(command);
                case GET_DANGDIEUCHE_LIST:
                    return handleGetDangDieuCheList();

                    //Đơn vị tính
                case GET_DONVITINH_LIST:
                    return handleGetDonViTinhList();
                case GET_DONVITINH_BY_NAME:
                    return handleGetDonViTinhByName(command);
                case CREATE_DONVITINH:
                    return handleCreateDonViTinh(command);
                case UPDATE_DONVITINH:
                    return handleUpdateDonViTinh(command);
                case DELETE_DONVITINH:
                    return handleDeleteDonViTinh(command);
                case RESTORE_DONVITINH:
                    return handleRestoreDonViTinh(command);
                case GET_MAX_HASH_DONVITINH:
                    return handleGetMaxHashDonViTinh(command);
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
                case GET_KHUYENMAI_NOT_DELETED_LIST:
                    return handleGetKhuyenMaiNotDeletedList();


                // LoaiKhuyenMai Operations
                case GET_LOAIKHUYENMAI_LIST:
                    return handleGetLoaiKhuyenMaiList();

                // ThongKe Operations
                case GET_THONGKE_TRANGCHINH:
                    return handleGetThongKeTrangChinh(command);

                //Phiếu nhập
                case GET_PHIEUNHAP_LIST:
                    return       handleGetPhieuNhapList();
                case GET_PHIEUNHAP_BY_STATUS:
                    return handleGetPhieuNhapByStatus(command);
                case CREATE_PHIEUNHAP:
                    return handleCreatePhieuNhap(command);
                case UPDATE_PHIEUNHAP:
                    return handleUpdatePhieuNhap(command);
                case DELETE_PHIEUNHAP:
                    return handleDeletePhieuNhap(command);
                case CANCEL_PHIEUNHAP:
                    return handleCancelPhieuNhap(command);
                case GENERATE_PHIEUNHAP_CODE:
                    return handleGeneratePhieuNhapCode(command);
                case CHECK_TEN_KE_EXISTS:
                    return handleCheckPhieuNhapExists(command);

                 // Chi tiết phiếu nhập
                case GET_CHITIETPHIEUNHAP_BY_PHIEUNHAP_ID:
                    return handleGetChiTietPhieuNhapByPhieuNhapId(command);
                case CREATE_CHITIETPHIEUNHAP:
                    return handleCreateChiTietPhieuNhap(command);

                    // Phiếu đặt
                case GET_PHIEUDAT_LIST:
                    return handleGetPhieuDatList();
                case GET_PHIEUDAT_BY_ID:
                    return handleGetPhieuDatById(command);
                case GET_PHIEUDAT_DELETED_LIST:
                    return handleGetPhieuDatDeletedList(command);
                case CREATE_PHIEUDAT:
                    return handleCreatePhieuDat(command);
                case UPDATE_PHIEUDAT:
                    return handleUpdatePhieuDat(command);
                case DELETE_PHIEUDAT:
                    return handleDeletePhieuDat(command);
                case RESTORE_PHIEUDAT:
                    return handleRestorePhieuDat(command);
                case UPDATE_PHIEUNHAP_STATUS:
                    return handleUpdatePhieuNhapStatus(command);
                case UPDATE_PHIEUDAT_PAYMENT_STATUS:
                    return handleUpdatePhieuDatPaymentStatus(command);
                case GET_MAX_HASH_PHIEUDAT:
                    return handleGetMaxHashPhieuDat(command);

                //Chi tiết phiếu đặt
                case GET_CHITIETPHIEUDAT_BY_PHIEUDAT_ID:
                    return handleGetChiTietPhieuDatByPhieuDatId(command);
                case CREATE_CHITIETPHIEUDAT:
                    return handleCreateChiTietPhieuDat(command);
                case UPDATE_CHITIETPHIEUDAT_PAYMENT_STATUS:
                    return handleUpdateChiTietPhieuDatPaymentStatus(command);
                case CANCEL_CHITIETPHIEUDAT:
                    return handleCancelChiTietPhieuDat(command);
                case RESTORE_CHITIETPHIEUDAT:
                    return handleRestoreChiTietPhieuDat(command);

                    //Thống kê
                case GET_TONG_SO_THUOC:
                    return handleGetTongSoThuoc(command);
                case GET_TONG_SO_NHANVIEN:
                    return handleGetTongSoNhanVien(command);
                case GET_SO_HOADON_HOMNAY:
                    return handleGetSoHoaDonHomNay(command);
                case GET_SO_KHUYENMAI_AP_DUNG:
                    return handleGetSoKhuyenMaiApDung(command);
                case GET_DOANHTHU_7_NGAY:
                    return handleGetDoanhThu7Ngay(command);
                case GET_TOP_SANPHAM_BANCHAY:
                    return handleGetTopSanPhamBanChay(command);
                case GET_THUOC_SAP_HETHAN:
                    return handleGetThuocSapHetHan(command);
                case GET_THUOC_TONKHO_THAP:
                    return handleGetThuocTonKhoThap(command);
//                case GET_THONGKE_DOANHTHU:
//                    return handleGetThongKeDoanhThu(command);
                case  GET_DOANHTHU_THEO_THANG:
                    return handleGetDoanhThuTheoThang(command);
                case GET_DOANHTHU_THEO_THOIGIAN:
                    return handleGetDoanhThuTheoThoiGian(command);
                case GET_TONG_DOANHTHU:
                    return handleGetTongDoanhThu(command);
                case GET_TONG_DONHANG:
                    return handleGetTongDonHang(command);
                case GET_SO_KHACHHANG_MOI:
                    return handleGetSoKhachHangMoi(command);
                case GET_TOP_SANPHAM_BANCHAY_THEO_THOIGIAN:
                    return handleGetTopSanPhamBanChayTheoThoiGian(command);
                case GET_NHANVIEN_LIST_FOR_FILTER:
                    return handleGetNhanVienListForFilter(command);

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

    private Response handleGetKeById(Command command) {
        try {
            if (command.getPayload() == null || !command.getPayload().containsKey("maKe")) {
                return Response.builder().success(false).message("Invalid payload for get TongTonKho by Thuoc ID").errorCode("INVALID_PAYLOAD").build();
            }
            String maThuoc = (String) command.getPayload().get("maThuoc");
            KeDTO  keDTO = serviceLocator.getKeService().getKeTheoMa(maThuoc);
            return Response.builder().success(true).message("TongTonKho retrieved successfully").data(keDTO).build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting TongTonKho by Thuoc ID", e);
            return Response.builder().success(false).message("Error retrieving TongTonKho: " + e.getMessage()).errorCode("GET_TONGTONKHO_ERROR").build();
        }
    }

    private Response handleGetTongTonKhoByThuocId(Command command) {
        try {
            if (command.getPayload() == null || !command.getPayload().containsKey("maThuoc")) {
                return Response.builder().success(false).message("Invalid payload for get TongTonKho by Thuoc ID").errorCode("INVALID_PAYLOAD").build();
            }
            String maThuoc = (String) command.getPayload().get("maThuoc");
            int tongTonKho = serviceLocator.getLoThuocService().getTongTonKhoTheoMaThuoc(maThuoc);
            return Response.builder().success(true).message("TongTonKho retrieved successfully").data(tongTonKho).build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting TongTonKho by Thuoc ID", e);
            return Response.builder().success(false).message("Error retrieving TongTonKho: " + e.getMessage()).errorCode("GET_TONGTONKHO_ERROR").build();
        }
    }

    private Response handleGetTongDonHangKhachHang(Command command) {
        try {
            if (command.getPayload() == null || !command.getPayload().containsKey("maKH")) {
                return Response.builder().success(false).message("Invalid payload for get TongDonHang KhachHang").errorCode("INVALID_PAYLOAD").build();
            }
            String maKH = (String) command.getPayload().get("maKH");
            int tongDonHang = serviceLocator.getHoaDonService().getAllHoaDon().indexOf(maKH);
            return Response.builder().success(true).message("TongDonHang KhachHang retrieved successfully").data(tongDonHang).build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting TongDonHang KhachHang", e);
            return Response.builder().success(false).message("Error retrieving TongDonHang KhachHang: " + e.getMessage()).errorCode("GET_TONG_DONHANG_KHACHHANG_ERROR").build();
        }
    }

    private Response handleCreateLoThuoc(Command command) {
        try {
            if (command.getPayload() == null || !command.getPayload().containsKey("loThuoc")) {
                return Response.builder().success(false).message("Invalid payload for create LoThuoc").errorCode("INVALID_PAYLOAD").build();
            }
            LoThuocDTO dto = (LoThuocDTO) command.getPayload().get("loThuoc");
            boolean success = serviceLocator.getLoThuocService().themChiTietThuoc(dto);
            return Response.builder().success(success).message(success ? "LoThuoc created successfully" : "Failed to create LoThuoc").errorCode(success ? null : "CREATE_LOTHUOC_FAILED").build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error creating LoThuoc", e);
            return Response.builder().success(false).message("Error creating LoThuoc: " + e.getMessage()).errorCode("CREATE_LOTHUOC_ERROR").build();
        }
    }

    private Response handleGetMaxHashDonViTinh(Command command) {
        try {
            String maxHash = serviceLocator.getDonViTinhService().getHashDVT();
            return Response.builder().success(true).message("MaxHashDonViTinh retrieved successfully").data(maxHash).build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting MaxHashDonViTinh", e);
            return Response.builder().success(false).message("Error retrieving MaxHashDonViTinh: " + e.getMessage()).errorCode("GET_MAX_HASH_DONVITINH_ERROR").build();
        }
    }

    private Response handleGetMaxHashKhachHang(Command command) {
        try {
            int maxHash = serviceLocator.getKhachHangService().getMaxHash();
            return Response.builder().success(true).message("MaxHashKhachHang retrieved successfully").data(maxHash).build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting MaxHashKhachHang", e);
            return Response.builder().success(false).message("Error retrieving MaxHashKhachHang: " + e.getMessage()).errorCode("GET_MAX_HASH_KHACHHANG_ERROR").build();
        }
    }

    private Response handleGetTongKhachHangVip(Command command) {
        try {
            int tongKhachHangVip = serviceLocator.getKhachHangService().getTongKhachHangVIP();
            return Response.builder().success(true).message("TongKhachHangVip retrieved successfully").data(tongKhachHangVip).build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting TongKhachHangVip", e);
            return Response.builder().success(false).message("Error retrieving TongKhachHangVip: " + e.getMessage()).errorCode("GET_TONG_KHACHHANG_VIP_ERROR").build();
        }
    }

    private Response handleGetTongKhachHang(Command command) {
        try {
            int tongKhachHang = serviceLocator.getKhachHangService().getTongKhachHang();
            return Response.builder().success(true).message("TongKhachHang retrieved successfully").data(tongKhachHang).build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting TongKhachHang", e);
            return Response.builder().success(false).message("Error retrieving TongKhachHang: " + e.getMessage()).errorCode("GET_TONG_KHACHHANG_ERROR").build();
        }
    }

    private Response handleUpdateKhachHang(Command command) {
        try {
            if (command.getPayload() == null || !command.getPayload().containsKey("khachHang")) {
                return Response.builder().success(false).message("Invalid payload for update KhachHang").errorCode("INVALID_PAYLOAD").build();
            }
            KhachHangDTO dto = (KhachHangDTO) command.getPayload().get("khachHang");
            boolean success = serviceLocator.getKhachHangService().updateKhachHang(dto);
            return Response.builder().success(success).message(success ? "KhachHang updated successfully" : "Failed to update KhachHang").errorCode(success ? null : "UPDATE_KHACHHANG_FAILED").build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error updating KhachHang", e);
            return Response.builder().success(false).message("Error updating KhachHang: " + e.getMessage()).errorCode("UPDATE_KHACHHANG_ERROR").build();
        }
    }

    private Response handleCreateKhachHang(Command command) {
        try {
            if (command.getPayload() == null || !command.getPayload().containsKey("khachHang")) {
                return Response.builder().success(false).message("Invalid payload for create KhachHang").errorCode("INVALID_PAYLOAD").build();
            }
            KhachHangDTO dto = (KhachHangDTO) command.getPayload().get("khachHang");
            boolean success = serviceLocator.getKhachHangService().insertKhachHang(dto);
            return Response.builder().success(success).message(success ? "KhachHang created successfully" : "Failed to create KhachHang").errorCode(success ? null : "CREATE_KHACHHANG_FAILED").build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error creating KhachHang", e);
            return Response.builder().success(false).message("Error creating KhachHang: " + e.getMessage()).errorCode("CREATE_KHACHHANG_ERROR").build();
        }
    }

    private Response handleGetKhachHangByPhone(Command command) {
        try {
            if (command.getPayload() == null || !command.getPayload().containsKey("phone")) {
                return Response.builder().success(false).message("Invalid payload for get KhachHang by Phone").errorCode("INVALID_PAYLOAD").build();
            }
            String phone = (String) command.getPayload().get("phone");
            KhachHangDTO dto = serviceLocator.getKhachHangService().getKhachHangTheoSoDienThoai(phone);
            return Response.builder().success(dto != null).message(dto != null ? "KhachHang retrieved successfully" : "KhachHang not found").data(dto).build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting KhachHang by Phone", e);
            return Response.builder().success(false).message("Error retrieving KhachHang: " + e.getMessage()).errorCode("GET_KHACHHANG_BY_PHONE_ERROR").build();
        }
    }

    private Response handleGetKhachHangWithStats(Command command) {
        try {
            if (command.getPayload() == null || !command.getPayload().containsKey("maKH")) {
                List<KhachHangDTO> khachHangList = serviceLocator.getKhachHangService().loadKhachHangWithStats();
                return Response.builder().success(true).message("KhachHang with stats list retrieved successfully").data(khachHangList).build();
            }
            String maKH = (String) command.getPayload().get("maKH");
            KhachHangDTO dto = serviceLocator.getKhachHangService().getKhachHangTheoMa(maKH);
            return Response.builder().success(dto != null).message(dto != null ? "KhachHang with stats retrieved successfully" : "KhachHang not found").data(dto).build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting KhachHang with stats", e);
            return Response.builder().success(false).message("Error retrieving KhachHang: " + e.getMessage()).errorCode("GET_KHACHHANG_WITH_STATS_ERROR").build();
        }
    }

    private Response handleSearchKhachHangByName(Command command) {
        try {
            if (command.getPayload() == null || !command.getPayload().containsKey("tenKH")) {
                return Response.builder().success(false).message("Invalid payload for search KhachHang by name").errorCode("INVALID_PAYLOAD").build();
            }
            String tenKH = (String) command.getPayload().get("tenKH");
            List<KhachHangDTO> khachHangList = serviceLocator.getKhachHangService().searchKhachHangByName(tenKH);
            return Response.builder().success(true).message("KhachHang search result retrieved successfully").data(khachHangList).build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error searching KhachHang by name", e);
            return Response.builder().success(false).message("Error searching KhachHang: " + e.getMessage()).errorCode("SEARCH_KHACHHANG_BY_NAME_ERROR").build();
        }
    }

    private Response handleGetMaxHashNhanVien(Command command) {
        try {
            String maxHash = serviceLocator.getNhanVienService().getMaxHashNhanVien();
            return Response.builder().success(true).message("MaxHashNhanVien retrieved successfully").data(maxHash).build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting MaxHashNhanVien", e);
            return Response.builder().success(false).message("Error retrieving MaxHashNhanVien: " + e.getMessage()).errorCode("GET_MAX_HASH_NHANVIEN_ERROR").build();
        }
    }

    private Response handleRestoreNhanVien(Command command) {
        try {
            if (command.getPayload() == null || !command.getPayload().containsKey("maNV")) {
                return Response.builder().success(false).message("Invalid payload for restore NhanVien").errorCode("INVALID_PAYLOAD").build();
            }
            String maNV = (String) command.getPayload().get("maNV");
            boolean success = serviceLocator.getNhanVienService().khoiPhucNhanVien(maNV);
            return Response.builder().success(success).message(success ? "NhanVien restored successfully" : "Failed to restore NhanVien").errorCode(success ? null : "RESTORE_NHANVIEN_FAILED").build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error restoring NhanVien", e);
            return Response.builder().success(false).message("Error restoring NhanVien: " + e.getMessage()).errorCode("RESTORE_NHANVIEN_ERROR").build();
        }
    }

    private Response handleDeleteNhanVien(Command command) {
        try {
            if (command.getPayload() == null || !command.getPayload().containsKey("maNV")) {
                return Response.builder().success(false).message("Invalid payload for delete NhanVien").errorCode("INVALID_PAYLOAD").build();
            }
            String maNV = (String) command.getPayload().get("maNV");
            boolean success = serviceLocator.getNhanVienService().xoaNhanVienTrongDBS(maNV);
            return Response.builder().success(success).message(success ? "NhanVien deleted successfully" : "Failed to delete NhanVien").errorCode(success ? null : "DELETE_NHANVIEN_FAILED").build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error deleting NhanVien", e);
            return Response.builder().success(false).message("Error deleting NhanVien: " + e.getMessage()).errorCode("DELETE_NHANVIEN_ERROR").build();
        }
    }

    private Response handleUpdateNhanVien(Command command) {
        try {
            if (command.getPayload() == null || !command.getPayload().containsKey("nhanVien")) {
                return Response.builder().success(false).message("Invalid payload for update NhanVien").errorCode("INVALID_PAYLOAD").build();
            }
            NhanVienDTO dto = (NhanVienDTO) command.getPayload().get("nhanVien");
            boolean success = serviceLocator.getNhanVienService().updateNhanVienTrongDBS(dto);
            return Response.builder().success(success).message(success ? "NhanVien updated successfully" : "Failed to update NhanVien").errorCode(success ? null : "UPDATE_NHANVIEN_FAILED").build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error updating NhanVien", e);
            return Response.builder().success(false).message("Error updating NhanVien: " + e.getMessage()).errorCode("UPDATE_NHANVIEN_ERROR").build();
        }
    }

    private Response handleCreateNhanVien(Command command) {
        try {
            if (command.getPayload() == null || !command.getPayload().containsKey("nhanVien")) {
                return Response.builder().success(false).message("Invalid payload for create NhanVien").errorCode("INVALID_PAYLOAD").build();
            }
            NhanVienDTO dto = (NhanVienDTO) command.getPayload().get("nhanVien");
            boolean success = serviceLocator.getNhanVienService().themNhanVien(dto);
            return Response.builder().success(success).message(success ? "NhanVien created successfully" : "Failed to create NhanVien").errorCode(success ? null : "CREATE_NHANVIEN_FAILED").build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error creating NhanVien", e);
            return Response.builder().success(false).message("Error creating NhanVien: " + e.getMessage()).errorCode("CREATE_NHANVIEN_ERROR").build();
        }
    }

    private Response handleGetNhanVienById(Command command) {
        try {
            if (command.getPayload() == null || !command.getPayload().containsKey("maNV")) {
                return Response.builder().success(false).message("Invalid payload for get NhanVien by ID").errorCode("INVALID_PAYLOAD").build();
            }
            String maNV = (String) command.getPayload().get("maNV");
            NhanVienDTO dto = serviceLocator.getNhanVienService().getNhanVien(maNV);
            return Response.builder().success(true).message("NhanVien retrieved successfully").data(dto).build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting NhanVien by ID", e);
            return Response.builder().success(false).message("Error retrieving NhanVien: " + e.getMessage()).errorCode("GET_NHANVIEN_BY_ID_ERROR").build();
        }
    }

    private Response handleGetNhanVienDeleteList(Command command) {
        try {
            List<NhanVienDTO> nhanVienList = serviceLocator.getNhanVienService().getAllNhanVien().stream()
                    .filter( e -> e.isDeleteAt() == true).toList();
            return Response.builder().success(true).message("Deleted NhanVien list retrieved successfully").data(nhanVienList).build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting deleted NhanVien list", e);
            return Response.builder().success(false).message("Error retrieving deleted NhanVien list: " + e.getMessage()).errorCode("GET_NHANVIEN_DELETED_ERROR").build();
        }
    }


    // === HoaDon Handlers ===


    private Response handleGetMaxHashHoaDon(Command command) {
        try {
            String maxHash = serviceLocator.getHoaDonService().getMaxHash();
            return Response.builder().success(true).message("MaxHashHoaDon retrieved successfully").data(maxHash).build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting MaxHashHoaDon", e);
            return Response.builder().success(false).message("Error retrieving MaxHashHoaDon: " + e.getMessage()).errorCode("GET_MAX_HASH_HOADON_ERROR").build();
        }
    }

    private Response handleCountHoaDonByKhuyenMai(Command command) {
        try {
            if (command.getPayload() == null || !command.getPayload().containsKey("maKM")) {
                return Response.builder().success(false).message("Invalid payload for count HoaDon by KhuyenMai").errorCode("INVALID_PAYLOAD").build();
            }
            String maKM = (String) command.getPayload().get("maKM");
            int count = serviceLocator.getHoaDonService().soHoaDonDaCoKhuyenMaiVoiMa(maKM);
            return Response.builder().success(true).message("HoaDon count retrieved successfully").data(count).build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error counting HoaDon by KhuyenMai", e);
            return Response.builder().success(false).message("Error counting HoaDon: " + e.getMessage()).errorCode("COUNT_HOADON_BY_KHUYENMAI_ERROR").build();
        }
    }

    private Response handleSearchHoaDonByNhanVien(Command command) {
        try {
            if (command.getPayload() == null || !command.getPayload().containsKey("maNV")) {
                return Response.builder().success(false).message("Invalid payload for search HoaDon by NhanVien").errorCode("INVALID_PAYLOAD").build();
            }
            String maNV = (String) command.getPayload().get("maNV");
            ArrayList<HoaDonDTO> hoaDonList = serviceLocator.getHoaDonService().searchHoaDonByMaNV(maNV);
            return Response.builder().success(true).message("HoaDon list retrieved successfully").data(hoaDonList).build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error searching HoaDon by NhanVien", e);
            return Response.builder().success(false).message("Error searching HoaDon: " + e.getMessage()).errorCode("SEARCH_HOADON_BY_NHANVIEN_ERROR").build();
        }
    }

    private Response handleSearchHoaDonByStatus(Command command) {
        try {
            if (command.getPayload() == null || !command.getPayload().containsKey("status")) {
                return Response.builder().success(false).message("Invalid payload for search HoaDon by Status").errorCode("INVALID_PAYLOAD").build();
            }
            String status = (String) command.getPayload().get("status");
            ArrayList<HoaDonDTO> hoaDonList = serviceLocator.getHoaDonService().searchHoaDonByStatus(status);
            return Response.builder().success(true).message("HoaDon list retrieved successfully").data(hoaDonList).build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error searching HoaDon by Status", e);
            return Response.builder().success(false).message("Error searching HoaDon: " + e.getMessage()).errorCode("SEARCH_HOADON_BY_STATUS_ERROR").build();
        }
    }

    private Response handleSearchHoaDonByMa(Command command) {
        try {
            if (command.getPayload() == null || !command.getPayload().containsKey("maHD")) {
                return Response.builder().success(false).message("Invalid payload for search HoaDon by Ma").errorCode("INVALID_PAYLOAD").build();
            }
            String maHD = (String) command.getPayload().get("maHD");
            ArrayList<HoaDonDTO> hoaDonList = serviceLocator.getHoaDonService().searchHoaDonByMaHd(maHD);
            return Response.builder().success(true).message("HoaDon list retrieved successfully").data(hoaDonList).build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error searching HoaDon by Ma", e);
            return Response.builder().success(false).message("Error searching HoaDon: " + e.getMessage()).errorCode("SEARCH_HOADON_BY_MA_ERROR").build();
        }
    }

    private Response handleGetHoaDonByKhachHangId(Command command) {
        try {
            if (command.getPayload() == null || !command.getPayload().containsKey("maKH")) {
                return Response.builder().success(false).message("Invalid payload for get HoaDon by KhachHang ID").errorCode("INVALID_PAYLOAD").build();
            }
            String maKH = (String) command.getPayload().get("maKH");
            ArrayList<HoaDonDTO> hoaDonList = serviceLocator.getHoaDonService().getHoaDonByMaKH(maKH);
            return Response.builder().success(true).message("HoaDon list retrieved successfully").data(hoaDonList).build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting HoaDon by KhachHang ID", e);
            return Response.builder().success(false).message("Error retrieving HoaDon list: " + e.getMessage()).errorCode("GET_HOADON_BY_KHACHHANG_ID_ERROR").build();
        }
    }

    private Response handleUpdateChiTietHoaDon(Command command) {
        try {
            if (command.getPayload() == null || !command.getPayload().containsKey("chiTietHoaDon")) {
                return Response.builder().success(false).message("Invalid payload for update ChiTietHoaDon").errorCode("INVALID_PAYLOAD").build();
            }
            ChiTietHoaDonDTO dto = (ChiTietHoaDonDTO) command.getPayload().get("chiTietHoaDon");
            boolean success = serviceLocator.getChiTietHoaDonService().updateChiTietHoaDon(dto);
            return Response.builder().success(success).message(success ? "ChiTietHoaDon updated successfully" : "Failed to update ChiTietHoaDon").errorCode(success ? null : "UPDATE_CHITIETHOADON_FAILED").build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error updating ChiTietHoaDon", e);
            return Response.builder().success(false).message("Error updating ChiTietHoaDon: " + e.getMessage()).errorCode("UPDATE_CHITIETHOADON_ERROR").build();
        }
    }


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

            HoaDonDTO dto = (HoaDonDTO) command.getPayload().get("hoaDon");
            boolean success = serviceLocator.getHoaDonService().insertHoaDon(dto);
            return Response.builder()
                    .success(success)
                    .message(success ? "HoaDon created successfully" : "Failed to create HoaDon")
                    .errorCode(success ? null : "CREATE_HOADON_FAILED")
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

    @SuppressWarnings("unchecked")
    private Response handleCreateHoaDonWithDetails(Command command) {
        try {
            if (command.getPayload() == null
                    || !command.getPayload().containsKey("hoaDon")
                    || !command.getPayload().containsKey("chiTietHoaDonList")) {
                return Response.builder()
                        .success(false)
                        .message("Invalid payload for create HoaDon with details")
                        .errorCode("INVALID_PAYLOAD")
                        .build();
            }

            HoaDonDTO hoaDonDTO = (HoaDonDTO) command.getPayload().get("hoaDon");
            List<ChiTietHoaDonDTO> chiTietHoaDonList = (List<ChiTietHoaDonDTO>) command.getPayload().get("chiTietHoaDonList");
            boolean success = serviceLocator.getHoaDonService().insertHoaDonVaChiTiet(hoaDonDTO, chiTietHoaDonList);
            return Response.builder()
                    .success(success)
                    .message(success ? "HoaDon with details created successfully" : "Failed to create HoaDon with details")
                    .errorCode(success ? null : "CREATE_HOADON_WITH_DETAILS_FAILED")
                    .build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error creating HoaDon with details", e);
            return Response.builder()
                    .success(false)
                    .message("Error creating HoaDon with details: " + e.getMessage())
                    .errorCode("CREATE_HOADON_WITH_DETAILS_ERROR")
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
            boolean success = serviceLocator.getChiTietHoaDonService().themHoacCapNhatChiTietHoaDon(dto);
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
                return Response.builder().success(false).data(false).message("Invalid payload for create Thuoc").errorCode("INVALID_PAYLOAD").build();
            }
            ThuocDTO thuocDTO = (ThuocDTO) command.getPayload().get("thuoc");
            boolean success = serviceLocator.getThuocService().themThuoc(thuocDTO);
            return Response.builder().success(success).data(success).message(success ? "Thuoc created successfully" : "Failed to create Thuoc").errorCode(success ? null : "CREATE_THUOC_FAILED").build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error creating Thuoc", e);
            return Response.builder().success(false).data(false).message("Error creating Thuoc: " + e.getMessage()).errorCode("CREATE_THUOC_ERROR").build();
        }
    }

    private Response handleUpdateThuoc(Command command) {
        try {
            if (command.getPayload() == null || !command.getPayload().containsKey("thuoc")) {
                return Response.builder().success(false).data(false).message("Invalid payload for update Thuoc").errorCode("INVALID_PAYLOAD").build();
            }
            ThuocDTO thuocDTO = (ThuocDTO) command.getPayload().get("thuoc");
            boolean success = serviceLocator.getThuocService().capNhatThuoc(thuocDTO);
            return Response.builder().success(success).data(success).message(success ? "Thuoc updated successfully" : "Failed to update Thuoc").errorCode(success ? null : "UPDATE_THUOC_FAILED").build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error updating Thuoc", e);
            return Response.builder().success(false).data(false).message("Error updating Thuoc: " + e.getMessage()).errorCode("UPDATE_THUOC_ERROR").build();
        }
    }

    private Response handleDeleteThuoc(Command command) {
        try {
            if (command.getPayload() == null || !command.getPayload().containsKey("maThuoc")) {
                return Response.builder().success(false).data(false).message("Invalid payload for delete Thuoc").errorCode("INVALID_PAYLOAD").build();
            }
            String maThuoc = (String) command.getPayload().get("maThuoc");
            boolean success = serviceLocator.getThuocService().xoaThuocTheoMa(maThuoc);
            return Response.builder().success(success).data(success).message(success ? "Thuoc deleted successfully" : "Failed to delete Thuoc").errorCode(success ? null : "DELETE_THUOC_FAILED").build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error deleting Thuoc", e);
            return Response.builder().success(false).data(false).message("Error deleting Thuoc: " + e.getMessage()).errorCode("DELETE_THUOC_ERROR").build();
        }
    }

    private Response handleRestoreThuoc(Command command) {
        try {
            if (command.getPayload() == null || !command.getPayload().containsKey("maThuoc")) {
                return Response.builder().success(false).data(false).message("Invalid payload for restore Thuoc").errorCode("INVALID_PAYLOAD").build();
            }
            String maThuoc = (String) command.getPayload().get("maThuoc");
            boolean success = serviceLocator.getThuocService().khoiPhucThuocTheoMa(maThuoc);
            return Response.builder().success(success).data(success).message(success ? "Thuoc restored successfully" : "Failed to restore Thuoc").errorCode(success ? null : "RESTORE_THUOC_FAILED").build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error restoring Thuoc", e);
            return Response.builder().success(false).data(false).message("Error restoring Thuoc: " + e.getMessage()).errorCode("RESTORE_THUOC_ERROR").build();
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
            Map<String, Object> payload = command.getPayload();
            String username = (String) payload.get("username");
            String password = (String) payload.get("password");

            // 1. Tìm nhân viên trong Database theo username
            // Giả sử bạn có một lớp DAO hoặc Service để truy vấn
            NhanVienDTO nv = serviceLocator.getNhanVienService().getNhanVienTaiKhoan(username);

            // 2. Kiểm tra tồn tại và so khớp mật khẩu bằng BCrypt
            if (nv != null && MaKhoaMatKhau.verifyPassword(password, nv.getMatKhau())) {

                // (Tùy chọn) Tạo Session ID hoặc trả về thông tin nhân viên
                return Response.builder()
                        .success(true)
                        .message("Login successful")
                        .data(nv) // Trả về DTO để Client sử dụng luôn
                        .build();
            } else {
                return Response.builder()
                        .success(false)
                        .message("Tên đăng nhập hoặc mật khẩu không chính xác")
                        .errorCode("AUTH_FAILED")
                        .build();
            }

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error during login", e);
            return Response.builder()
                    .success(false)
                    .message("Lỗi hệ thống: " + e.getMessage())
                    .errorCode("SERVER_ERROR")
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

    // Ke Handlers
    private Response handleGetKeByName(Command command) {
        try {
            if (command.getPayload() == null || !command.getPayload().containsKey("tenKe")) {
                return Response.builder().success(false).message("Invalid payload for get Ke by name").errorCode("INVALID_PAYLOAD").build();
            }
            String tenKe = (String) command.getPayload().get("tenKe");
            KeDTO ke = serviceLocator.getKeService().getKeTheoName(tenKe);
            return Response.builder().success(ke != null).message(ke != null ? "Ke retrieved successfully" : "Ke not found").data(ke).build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting Ke by name", e);
            return Response.builder().success(false).message("Error retrieving Ke: " + e.getMessage()).errorCode("GET_KE_BY_NAME_ERROR").build();
        }
    }

    private Response handleCreateKe(Command command) {
        try {
            if (command.getPayload() == null || !command.getPayload().containsKey("ke")) {
                return Response.builder().success(false).message("Invalid payload for create Ke").errorCode("INVALID_PAYLOAD").build();
            }
            KeDTO dto = (KeDTO) command.getPayload().get("ke");
            boolean success = serviceLocator.getKeService().themKe(dto);
            return Response.builder().success(success).message(success ? "Ke created successfully" : "Failed to create Ke").errorCode(success ? null : "CREATE_KE_FAILED").build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error creating Ke", e);
            return Response.builder().success(false).message("Error creating Ke: " + e.getMessage()).errorCode("CREATE_KE_ERROR").build();
        }
    }

    private Response handleUpdateKe(Command command) {
        try {
            if (command.getPayload() == null || !command.getPayload().containsKey("ke")) {
                return Response.builder().success(false).message("Invalid payload for update Ke").errorCode("INVALID_PAYLOAD").build();
            }
            KeDTO dto = (KeDTO) command.getPayload().get("ke");
            boolean success = serviceLocator.getKeService().suaKe(dto);
            return Response.builder().success(success).message(success ? "Ke updated successfully" : "Failed to update Ke").errorCode(success ? null : "UPDATE_KE_FAILED").build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error updating Ke", e);
            return Response.builder().success(false).message("Error updating Ke: " + e.getMessage()).errorCode("UPDATE_KE_ERROR").build();
        }
    }

    private Response handleDeleteKe(Command command) {
        try {
            if (command.getPayload() == null || !command.getPayload().containsKey("maKe")) {
                return Response.builder().success(false).message("Invalid payload for delete Ke").errorCode("INVALID_PAYLOAD").build();
            }
            String maKe = (String) command.getPayload().get("maKe");
            boolean success = serviceLocator.getKeService().xoaKe(maKe);
            return Response.builder().success(success).message(success ? "Ke deleted successfully" : "Failed to delete Ke").errorCode(success ? null : "DELETE_KE_FAILED").build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error deleting Ke", e);
            return Response.builder().success(false).message("Error deleting Ke: " + e.getMessage()).errorCode("DELETE_KE_ERROR").build();
        }
    }

    private Response handleRestoreKe(Command command) {
        try {
            if (command.getPayload() == null || !command.getPayload().containsKey("maKe")) {
                return Response.builder().success(false).message("Invalid payload for restore Ke").errorCode("INVALID_PAYLOAD").build();
            }
            String maKe = (String) command.getPayload().get("maKe");
            boolean success = serviceLocator.getKeService().khoiPhucKe(maKe);
            return Response.builder().success(success).message(success ? "Ke restored successfully" : "Failed to restore Ke").errorCode(success ? null : "RESTORE_KE_FAILED").build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error restoring Ke", e);
            return Response.builder().success(false).message("Error restoring Ke: " + e.getMessage()).errorCode("RESTORE_KE_ERROR").build();
        }
    }

    private Response handleGenerateKeCode(Command command) {
        try {
            String keCode = serviceLocator.getKeService().taoMaKeTuDong();
            return Response.builder().success(true).message("Ke code generated successfully").data(keCode).build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error generating Ke code", e);
            return Response.builder().success(false).message("Error generating Ke code: " + e.getMessage()).errorCode("GENERATE_KE_CODE_ERROR").build();
        }
    }

    private Response handleGetKeList() {
        try {
            ArrayList<KeDTO> keList = serviceLocator.getKeService().getTatCaKeThuoc();
            return Response.builder().success(true).message("Ke list retrieved successfully").data(keList).build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting Ke list", e);
            return Response.builder().success(false).message("Error retrieving Ke list: " + e.getMessage()).errorCode("GET_KE_LIST_ERROR").build();
        }
    }

    private Response handleCheckKeExists(Command command) {
        try {
            if (command.getPayload() == null || !command.getPayload().containsKey("maKe")) {
                return Response.builder().success(false).message("Invalid payload for check Ke exists").errorCode("INVALID_PAYLOAD").build();
            }
            String maKe = (String) command.getPayload().get("maKe");
            boolean exists = serviceLocator.getKeService().getKeTheoMa(maKe) == null ? false : true;
            return Response.builder().success(true).message("Ke existence checked").data(exists).build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error checking Ke exists", e);
            return Response.builder().success(false).message("Error checking Ke existence: " + e.getMessage()).errorCode("CHECK_KE_EXISTS_ERROR").build();
        }
    }

    // DangDieuChe Handlers
    private Response handleGetDangDieuCheByName(Command command) {
        try {
            if (command.getPayload() == null || !command.getPayload().containsKey("tenDDC")) {
                return Response.builder().success(false).message("Invalid payload for get DangDieuChe by name").errorCode("INVALID_PAYLOAD").build();
            }
            String tenDDC = (String) command.getPayload().get("tenDDC");
            DangDieuCheDTO ddc = serviceLocator.getDangDieuCheService().getDDCTheoName(tenDDC);
            return Response.builder().success(ddc != null).message(ddc != null ? "DangDieuChe retrieved successfully" : "DangDieuChe not found").data(ddc).build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting DangDieuChe by name", e);
            return Response.builder().success(false).message("Error retrieving DangDieuChe: " + e.getMessage()).errorCode("GET_DANGDIEUCHE_BY_NAME_ERROR").build();
        }
    }

    private Response handleCreateDangDieuChe(Command command) {
        try {
            if (command.getPayload() == null || !command.getPayload().containsKey("dangDieuChe")) {
                return Response.builder().success(false).message("Invalid payload for create DangDieuChe").errorCode("INVALID_PAYLOAD").build();
            }
            DangDieuCheDTO dto = (DangDieuCheDTO) command.getPayload().get("dangDieuChe");
            boolean success = serviceLocator.getDangDieuCheService().themDDC(dto);
            return Response.builder().success(success).message(success ? "DangDieuChe created successfully" : "Failed to create DangDieuChe").errorCode(success ? null : "CREATE_DANGDIEUCHE_FAILED").build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error creating DangDieuChe", e);
            return Response.builder().success(false).message("Error creating DangDieuChe: " + e.getMessage()).errorCode("CREATE_DANGDIEUCHE_ERROR").build();
        }
    }

    private Response handleUpdateDangDieuChe(Command command) {
        try {
            if (command.getPayload() == null || !command.getPayload().containsKey("dangDieuChe")) {
                return Response.builder().success(false).message("Invalid payload for update DangDieuChe").errorCode("INVALID_PAYLOAD").build();
            }
            DangDieuCheDTO dto = (DangDieuCheDTO) command.getPayload().get("dangDieuChe");
            boolean success = serviceLocator.getDangDieuCheService().suaDangDieuChe(dto);
            return Response.builder().success(success).message(success ? "DangDieuChe updated successfully" : "Failed to update DangDieuChe").errorCode(success ? null : "UPDATE_DANGDIEUCHE_FAILED").build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error updating DangDieuChe", e);
            return Response.builder().success(false).message("Error updating DangDieuChe: " + e.getMessage()).errorCode("UPDATE_DANGDIEUCHE_ERROR").build();
        }
    }

    private Response handleDeleteDangDieuChe(Command command) {
        try {
            if (command.getPayload() == null || !command.getPayload().containsKey("maDDC")) {
                return Response.builder().success(false).message("Invalid payload for delete DangDieuChe").errorCode("INVALID_PAYLOAD").build();
            }
            int maDDC = (Integer) command.getPayload().get("maDDC");
            boolean success = serviceLocator.getDangDieuCheService().xoaDangDieuChe(maDDC);
            return Response.builder().success(success).message(success ? "DangDieuChe deleted successfully" : "Failed to delete DangDieuChe").errorCode(success ? null : "DELETE_DANGDIEUCHE_FAILED").build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error deleting DangDieuChe", e);
            return Response.builder().success(false).message("Error deleting DangDieuChe: " + e.getMessage()).errorCode("DELETE_DANGDIEUCHE_ERROR").build();
        }
    }

    private Response handleRestoreDangDieuChe(Command command) {
        try {
            if (command.getPayload() == null || !command.getPayload().containsKey("maDDC")) {
                return Response.builder().success(false).message("Invalid payload for restore DangDieuChe").errorCode("INVALID_PAYLOAD").build();
            }
            int maDDC = (Integer) command.getPayload().get("maDDC");
            boolean success = serviceLocator.getDangDieuCheService().khoiPhucDangDieuChe(maDDC);
            return Response.builder().success(success).message(success ? "DangDieuChe restored successfully" : "Failed to restore DangDieuChe").errorCode(success ? null : "RESTORE_DANGDIEUCHE_FAILED").build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error restoring DangDieuChe", e);
            return Response.builder().success(false).message("Error restoring DangDieuChe: " + e.getMessage()).errorCode("RESTORE_DANGDIEUCHE_ERROR").build();
        }
    }

    private Response handleGenerateDangDieuCheCode(Command command) {
        try {
            String code = serviceLocator.getDangDieuCheService().taoMaDDCTuDong();
            return Response.builder().success(true).message("DangDieuChe code generated successfully").data(code).build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error generating DangDieuChe code", e);
            return Response.builder().success(false).message("Error generating DangDieuChe code: " + e.getMessage()).errorCode("GENERATE_DANGDIEUCHE_CODE_ERROR").build();
        }
    }

    private Response handleGetDangDieuCheList() {
        try {
            ArrayList<DangDieuCheDTO> ddcList = serviceLocator.getDangDieuCheService().getTatCaDangDieuChe();
            return Response.builder().success(true).message("DangDieuChe list retrieved successfully").data(ddcList).build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting DangDieuChe list", e);
            return Response.builder().success(false).message("Error retrieving DangDieuChe list: " + e.getMessage()).errorCode("GET_DANGDIEUCHE_LIST_ERROR").build();
        }
    }

    // DonViTinh Handlers
    private Response handleGetDonViTinhByName(Command command) {
        try {
            if (command.getPayload() == null || !command.getPayload().containsKey("tenDVT")) {
                return Response.builder().success(false).message("Invalid payload for get DonViTinh by name").errorCode("INVALID_PAYLOAD").build();
            }
            String tenDVT = (String) command.getPayload().get("tenDVT");
            DonViTinhDTO dvt = serviceLocator.getDonViTinhService().getDVTTheoTen(tenDVT);
            return Response.builder().success(dvt != null).message(dvt != null ? "DonViTinh retrieved successfully" : "DonViTinh not found").data(dvt).build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting DonViTinh by name", e);
            return Response.builder().success(false).message("Error retrieving DonViTinh: " + e.getMessage()).errorCode("GET_DONVITINH_BY_NAME_ERROR").build();
        }
    }

    private Response handleCreateDonViTinh(Command command) {
        try {
            if (command.getPayload() == null || !command.getPayload().containsKey("donViTinh")) {
                return Response.builder().success(false).message("Invalid payload for create DonViTinh").errorCode("INVALID_PAYLOAD").build();
            }
            DonViTinhDTO dto = (DonViTinhDTO) command.getPayload().get("donViTinh");
            boolean success = serviceLocator.getDonViTinhService().themDonViTinh(dto) > 0;
            return Response.builder().success(success).message(success ? "DonViTinh created successfully" : "Failed to create DonViTinh").errorCode(success ? null : "CREATE_DONVITINH_FAILED").build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error creating DonViTinh", e);
            return Response.builder().success(false).message("Error creating DonViTinh: " + e.getMessage()).errorCode("CREATE_DONVITINH_ERROR").build();
        }
    }

    private Response handleUpdateDonViTinh(Command command) {
        try {
            if (command.getPayload() == null || !command.getPayload().containsKey("donViTinh")) {
                return Response.builder().success(false).message("Invalid payload for update DonViTinh").errorCode("INVALID_PAYLOAD").build();
            }
            DonViTinhDTO dto = (DonViTinhDTO) command.getPayload().get("donViTinh");
            boolean success = serviceLocator.getDonViTinhService().updateDonViTinh(dto) > 0;
            return Response.builder().success(success).message(success ? "DonViTinh updated successfully" : "Failed to update DonViTinh").errorCode(success ? null : "UPDATE_DONVITINH_FAILED").build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error updating DonViTinh", e);
            return Response.builder().success(false).message("Error updating DonViTinh: " + e.getMessage()).errorCode("UPDATE_DONVITINH_ERROR").build();
        }
    }

    private Response handleDeleteDonViTinh(Command command) {
        try {
            if (command.getPayload() == null || !command.getPayload().containsKey("maDVT")) {
                return Response.builder().success(false).message("Invalid payload for delete DonViTinh").errorCode("INVALID_PAYLOAD").build();
            }
            int maDVT = (int) command.getPayload().get("maDVT");
            DonViTinhDTO dvt = serviceLocator.getDonViTinhService().getDVTTheoMaDVT((maDVT));
            if (dvt == null) {
                return Response.builder().success(false).message("DonViTinh not found").errorCode("DONVITINH_NOT_FOUND").build();
            }
            boolean success = serviceLocator.getDonViTinhService().xoaDonViTinh(dvt) > 0;
            return Response.builder().success(success).message(success ? "DonViTinh deleted successfully" : "Failed to delete DonViTinh").errorCode(success ? null : "DELETE_DONVITINH_FAILED").build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error deleting DonViTinh", e);
            return Response.builder().success(false).message("Error deleting DonViTinh: " + e.getMessage()).errorCode("DELETE_DONVITINH_ERROR").build();
        }
    }

    private Response handleRestoreDonViTinh(Command command) {
        try {
            if (command.getPayload() == null || !command.getPayload().containsKey("maDVT")) {
                return Response.builder().success(false).message("Invalid payload for restore DonViTinh").errorCode("INVALID_PAYLOAD").build();
            }
            int maDVT = (int) command.getPayload().get("maDVT");
            DonViTinhDTO donViTinhDTO = serviceLocator.getDonViTinhService().getDVTTheoMaDVT(maDVT);
            if (donViTinhDTO == null) {
                return Response.builder().success(false).message("DonViTinh not found").errorCode("DONVITINH_NOT_FOUND").build();
            }
            boolean success = serviceLocator.getDonViTinhService().khoiPhucDonViTinh(donViTinhDTO) > 0;
            return Response.builder().success(success).message(success ? "DonViTinh restored successfully" : "Failed to restore DonViTinh").errorCode(success ? null : "RESTORE_DONVITINH_FAILED").build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error restoring DonViTinh", e);
            return Response.builder().success(false).message("Error restoring DonViTinh: " + e.getMessage()).errorCode("RESTORE_DONVITINH_ERROR").build();
        }
    }

    // KhuyenMai Handlers
    private Response handleGetKhuyenMaiNotDeletedList() {
        try {
            ArrayList<KhuyenMaiDTO> khuyenMaiList = serviceLocator.getKhuyenMaiService().getAllKhuyenMaiChuaXoa();
            return Response.builder().success(true).message("Non-deleted KhuyenMai list retrieved successfully").data(khuyenMaiList).build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting non-deleted KhuyenMai list", e);
            return Response.builder().success(false).message("Error retrieving non-deleted KhuyenMai list: " + e.getMessage()).errorCode("GET_KHUYENMAI_NOT_DELETED_ERROR").build();
        }
    }

    // PhieuNhap Handlers
    private Response handleGetPhieuNhapList() {
        try {
            ArrayList<PhieuNhapDTO> phieuNhapList = serviceLocator.getPhieuNhapService().getDanhSachPhieuNhap();
            return Response.builder().success(true).message("PhieuNhap list retrieved successfully").data(phieuNhapList).build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting PhieuNhap list", e);
            return Response.builder().success(false).message("Error retrieving PhieuNhap list: " + e.getMessage()).errorCode("GET_PHIEUNHAP_ERROR").build();
        }
    }

    private Response handleGetPhieuNhapByStatus(Command command) {
        try {
            if (command.getPayload() == null || !command.getPayload().containsKey("status")) {
                return Response.builder().success(false).message("Invalid payload for get PhieuNhap by status").errorCode("INVALID_PAYLOAD").build();
            }
            String status = (String) command.getPayload().get("status");
            Boolean a = "true".equals(status);
            ArrayList<PhieuNhapDTO> phieuNhapList = serviceLocator.getPhieuNhapService().getDanhSachPhieuNhapTheoTrangThai(a);
            return Response.builder().success(true).message("PhieuNhap list retrieved successfully").data(phieuNhapList).build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting PhieuNhap by status", e);
            return Response.builder().success(false).message("Error retrieving PhieuNhap list: " + e.getMessage()).errorCode("GET_PHIEUNHAP_BY_STATUS_ERROR").build();
        }
    }

    private Response handleCreatePhieuNhap(Command command) {
        try {
            if (command.getPayload() == null || !command.getPayload().containsKey("phieuNhap")) {
                return Response.builder().success(false).message("Invalid payload for create PhieuNhap").errorCode("INVALID_PAYLOAD").build();
            }
            PhieuNhapDTO dto = (PhieuNhapDTO) command.getPayload().get("phieuNhap");
            boolean success = serviceLocator.getPhieuNhapService().themPhieuNhap(dto);
            return Response.builder().success(success).message(success ? "PhieuNhap created successfully" : "Failed to create PhieuNhap").errorCode(success ? null : "CREATE_PHIEUNHAP_FAILED").build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error creating PhieuNhap", e);
            return Response.builder().success(false).message("Error creating PhieuNhap: " + e.getMessage()).errorCode("CREATE_PHIEUNHAP_ERROR").build();
        }
    }

    private Response handleUpdatePhieuNhap(Command command) {
        try {
            if (command.getPayload() == null || !command.getPayload().containsKey("phieuNhap")) {
                return Response.builder().success(false).message("Invalid payload for update PhieuNhap").errorCode("INVALID_PAYLOAD").build();
            }
            PhieuNhapDTO dto = (PhieuNhapDTO) command.getPayload().get("phieuNhap");
            boolean success = serviceLocator.getPhieuNhapService().suaPhieuNhap(dto);
            return Response.builder().success(success).message(success ? "PhieuNhap updated successfully" : "Failed to update PhieuNhap").errorCode(success ? null : "UPDATE_PHIEUNHAP_FAILED").build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error updating PhieuNhap", e);
            return Response.builder().success(false).message("Error updating PhieuNhap: " + e.getMessage()).errorCode("UPDATE_PHIEUNHAP_ERROR").build();
        }
    }

    private Response handleDeletePhieuNhap(Command command) {
        try {
            if (command.getPayload() == null || !command.getPayload().containsKey("maPN")) {
                return Response.builder().success(false).message("Invalid payload for delete PhieuNhap").errorCode("INVALID_PAYLOAD").build();
            }
            String maPN = (String) command.getPayload().get("maPN");
            boolean success = serviceLocator.getPhieuNhapService().huyPhieuNhap(maPN);
            return Response.builder().success(success).message(success ? "PhieuNhap deleted successfully" : "Failed to delete PhieuNhap").errorCode(success ? null : "DELETE_PHIEUNHAP_FAILED").build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error deleting PhieuNhap", e);
            return Response.builder().success(false).message("Error deleting PhieuNhap: " + e.getMessage()).errorCode("DELETE_PHIEUNHAP_ERROR").build();
        }
    }

    private Response handleCancelPhieuNhap(Command command) {
        try {
            if (command.getPayload() == null || !command.getPayload().containsKey("maPN")) {
                return Response.builder().success(false).message("Invalid payload for cancel PhieuNhap").errorCode("INVALID_PAYLOAD").build();
            }
            String maPN = (String) command.getPayload().get("maPN");
            boolean success = serviceLocator.getPhieuNhapService().suaTrangThaiPhieuNhap(maPN);
            return Response.builder().success(success).message(success ? "PhieuNhap cancelled successfully" : "Failed to cancel PhieuNhap").errorCode(success ? null : "CANCEL_PHIEUNHAP_FAILED").build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error cancelling PhieuNhap", e);
            return Response.builder().success(false).message("Error cancelling PhieuNhap: " + e.getMessage()).errorCode("CANCEL_PHIEUNHAP_ERROR").build();
        }
    }

    private Response handleGeneratePhieuNhapCode(Command command) {
        try {
            String code = serviceLocator.getPhieuNhapService().taoMaPhieuNhapTuDong();
            return Response.builder().success(true).message("PhieuNhap code generated successfully").data(code).build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error generating PhieuNhap code", e);
            return Response.builder().success(false).message("Error generating PhieuNhap code: " + e.getMessage()).errorCode("GENERATE_PHIEUNHAP_CODE_ERROR").build();
        }
    }

    private Response handleCheckPhieuNhapExists(Command command) {
        try {
            if (command.getPayload() == null || !command.getPayload().containsKey("maPN")) {
                return Response.builder().success(false).message("Invalid payload for check PhieuNhap exists").errorCode("INVALID_PAYLOAD").build();
            }
            String maPN = (String) command.getPayload().get("maPN");
            boolean exists = serviceLocator.getPhieuNhapService().tonTaiMaPhieuNhap(maPN);
            return Response.builder().success(true).message("PhieuNhap existence checked").data(exists).build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error checking PhieuNhap exists", e);
            return Response.builder().success(false).message("Error checking PhieuNhap existence: " + e.getMessage()).errorCode("CHECK_PHIEUNHAP_EXISTS_ERROR").build();
        }
    }

    // ChiTietPhieuNhap Handlers
    private Response handleGetChiTietPhieuNhapByPhieuNhapId(Command command) {
        try {
            if (command.getPayload() == null || !command.getPayload().containsKey("maPN")) {
                return Response.builder().success(false).message("Invalid payload for get ChiTietPhieuNhap by PhieuNhap ID").errorCode("INVALID_PAYLOAD").build();
            }
            String maPN = (String) command.getPayload().get("maPN");
            ArrayList<ChiTietPhieuNhapDTO> list = serviceLocator.getChiTietPhieuNhapService().getDanhSachChiTietPhieuNhapTheoMaPN(maPN);
            return Response.builder().success(true).message("ChiTietPhieuNhap list retrieved successfully").data(list).build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting ChiTietPhieuNhap by PhieuNhap ID", e);
            return Response.builder().success(false).message("Error retrieving ChiTietPhieuNhap list: " + e.getMessage()).errorCode("GET_CHITIETPHIEUNHAP_BY_PHIEUNHAP_ID_ERROR").build();
        }
    }

    private Response handleCreateChiTietPhieuNhap(Command command) {
        try {
            if (command.getPayload() == null || !command.getPayload().containsKey("chiTietPhieuNhap")) {
                return Response.builder().success(false).message("Invalid payload for create ChiTietPhieuNhap").errorCode("INVALID_PAYLOAD").build();
            }
            ChiTietPhieuNhapDTO dto = (ChiTietPhieuNhapDTO) command.getPayload().get("chiTietPhieuNhap");
            boolean success = serviceLocator.getChiTietPhieuNhapService().themChiTietPhieuNhap(dto);
            return Response.builder().success(success).message(success ? "ChiTietPhieuNhap created successfully" : "Failed to create ChiTietPhieuNhap").errorCode(success ? null : "CREATE_CHITIETPHIEUNHAP_FAILED").build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error creating ChiTietPhieuNhap", e);
            return Response.builder().success(false).message("Error creating ChiTietPhieuNhap: " + e.getMessage()).errorCode("CREATE_CHITIETPHIEUNHAP_ERROR").build();
        }
    }

    // PhieuDat Handlers
    private Response handleGetPhieuDatList() {
        try {
            ArrayList<PhieuDatThuocDTO> phieuDatList = (ArrayList<PhieuDatThuocDTO>) serviceLocator.getPhieuDatService().getAllPhieuDatThuocFromDBS();
            return Response.builder().success(true).message("PhieuDat list retrieved successfully").data(phieuDatList).build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting PhieuDat list", e);
            return Response.builder().success(false).message("Error retrieving PhieuDat list: " + e.getMessage()).errorCode("GET_PHIEUDAT_ERROR").build();
        }
    }

    private Response handleGetPhieuDatById(Command command) {
        try {
            if (command.getPayload() == null || !command.getPayload().containsKey("maPD")) {
                return Response.builder().success(false).message("Invalid payload for get PhieuDat by ID").errorCode("INVALID_PAYLOAD").build();
            }
            String maPD = (String) command.getPayload().get("maPD");
            PhieuDatThuocDTO dto = serviceLocator.getPhieuDatService().getPhieuDatByMaFromDBS(maPD);
            return Response.builder().success(dto != null).message(dto != null ? "PhieuDat retrieved successfully" : "PhieuDat not found").data(dto).build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting PhieuDat by ID", e);
            return Response.builder().success(false).message("Error retrieving PhieuDat: " + e.getMessage()).errorCode("GET_PHIEUDAT_BY_ID_ERROR").build();
        }
    }

    private Response handleGetPhieuDatDeletedList(Command command) {
        try {
            ArrayList<PhieuDatThuocDTO> phieuDatList = serviceLocator.getPhieuDatService().getAllPhieuDatThuocDaXoa();
            return Response.builder().success(true).message("Deleted PhieuDat list retrieved successfully").data(phieuDatList).build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting deleted PhieuDat list", e);
            return Response.builder().success(false).message("Error retrieving deleted PhieuDat list: " + e.getMessage()).errorCode("GET_PHIEUDAT_DELETED_ERROR").build();
        }
    }

    private Response handleCreatePhieuDat(Command command) {
        try {
            if (command.getPayload() == null || !command.getPayload().containsKey("phieuDat")) {
                return Response.builder().success(false).message("Invalid payload for create PhieuDat").errorCode("INVALID_PAYLOAD").build();
            }
            PhieuDatThuocDTO dto = (PhieuDatThuocDTO) command.getPayload().get("phieuDat");
            boolean success = serviceLocator.getPhieuDatService().themPhieuDatThuocVaoDBS(dto);
            return Response.builder().success(success).message(success ? "PhieuDat created successfully" : "Failed to create PhieuDat").errorCode(success ? null : "CREATE_PHIEUDAT_FAILED").build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error creating PhieuDat", e);
            return Response.builder().success(false).message("Error creating PhieuDat: " + e.getMessage()).errorCode("CREATE_PHIEUDAT_ERROR").build();
        }
    }

    private Response handleUpdatePhieuDat(Command command) {
        try {
            if (command.getPayload() == null || !command.getPayload().containsKey("phieuDat")) {
                return Response.builder().success(false).message("Invalid payload for update PhieuDat").errorCode("INVALID_PAYLOAD").build();
            }
            String dto = (String) command.getPayload().get("maPhieu");
            boolean success = serviceLocator.getPhieuDatService().capNhatThanhToanPhieuDat(dto);
            return Response.builder().success(success).message(success ? "PhieuDat updated successfully" : "Failed to update PhieuDat").errorCode(success ? null : "UPDATE_PHIEUDAT_FAILED").build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error updating PhieuDat", e);
            return Response.builder().success(false).message("Error updating PhieuDat: " + e.getMessage()).errorCode("UPDATE_PHIEUDAT_ERROR").build();
        }
    }

    private Response handleDeletePhieuDat(Command command) {
        try {
            if (command.getPayload() == null || !command.getPayload().containsKey("maPD")) {
                return Response.builder().success(false).message("Invalid payload for delete PhieuDat").errorCode("INVALID_PAYLOAD").build();
            }
            String maPD = (String) command.getPayload().get("maPD");
            boolean success = serviceLocator.getPhieuDatService().xoaPhieuDatThuocTrongDBS(maPD);
            return Response.builder().success(success).message(success ? "PhieuDat deleted successfully" : "Failed to delete PhieuDat").errorCode(success ? null : "DELETE_PHIEUDAT_FAILED").build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error deleting PhieuDat", e);
            return Response.builder().success(false).message("Error deleting PhieuDat: " + e.getMessage()).errorCode("DELETE_PHIEUDAT_ERROR").build();
        }
    }

    private Response handleRestorePhieuDat(Command command) {
        try {
            if (command.getPayload() == null || !command.getPayload().containsKey("maPD")) {
                return Response.builder().success(false).message("Invalid payload for restore PhieuDat").errorCode("INVALID_PAYLOAD").build();
            }
            String maPD = (String) command.getPayload().get("maPD");
            boolean success = serviceLocator.getPhieuDatService().khoiPhucPhieuDat(maPD);
            return Response.builder().success(success).message(success ? "PhieuDat restored successfully" : "Failed to restore PhieuDat").errorCode(success ? null : "RESTORE_PHIEUDAT_FAILED").build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error restoring PhieuDat", e);
            return Response.builder().success(false).message("Error restoring PhieuDat: " + e.getMessage()).errorCode("RESTORE_PHIEUDAT_ERROR").build();
        }
    }

    private Response handleUpdatePhieuNhapStatus(Command command) {
        try {
            if (command.getPayload() == null || !command.getPayload().containsKey("maPD") || !command.getPayload().containsKey("status")) {
                return Response.builder().success(false).message("Invalid payload for update PhieuNhap status").errorCode("INVALID_PAYLOAD").build();
            }
            String maPD = (String) command.getPayload().get("maPD");
            boolean success = serviceLocator.getPhieuNhapService().suaTrangThaiPhieuNhap(maPD);
            return Response.builder().success(success).message(success ? "PhieuNhap status updated successfully" : "Failed to update PhieuNhap status").errorCode(success ? null : "UPDATE_PHIEUNHAP_STATUS_FAILED").build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error updating PhieuNhap status", e);
            return Response.builder().success(false).message("Error updating PhieuNhap status: " + e.getMessage()).errorCode("UPDATE_PHIEUNHAP_STATUS_ERROR").build();
        }
    }

    private Response handleUpdatePhieuDatPaymentStatus(Command command) {
        try {
            if (command.getPayload() == null || !command.getPayload().containsKey("maPD")) {
                return Response.builder().success(false).message("Invalid payload for update PhieuDat payment status").errorCode("INVALID_PAYLOAD").build();
            }
            String maPD = (String) command.getPayload().get("maPD");
            boolean success = serviceLocator.getPhieuDatService().capNhatThanhToanPhieuDat(maPD);
            return Response.builder().success(success).message(success ? "PhieuDat payment status updated successfully" : "Failed to update PhieuDat payment status").errorCode(success ? null : "UPDATE_PHIEUDAT_PAYMENT_STATUS_FAILED").build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error updating PhieuDat payment status", e);
            return Response.builder().success(false).message("Error updating PhieuDat payment status: " + e.getMessage()).errorCode("UPDATE_PHIEUDAT_PAYMENT_STATUS_ERROR").build();
        }
    }

    private Response handleGetMaxHashPhieuDat(Command command) {
        try {
            String maxHash = serviceLocator.getPhieuDatService().getMaxHash();
            return Response.builder().success(true).message("MaxHashPhieuDat retrieved successfully").data(maxHash).build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting MaxHashPhieuDat", e);
            return Response.builder().success(false).message("Error retrieving MaxHashPhieuDat: " + e.getMessage()).errorCode("GET_MAX_HASH_PHIEUDAT_ERROR").build();
        }
    }

    // ChiTietPhieuDat Handlers
    private Response handleGetChiTietPhieuDatByPhieuDatId(Command command) {
        try {
            if (command.getPayload() == null || !command.getPayload().containsKey("maPD")) {
                return Response.builder().success(false).message("Invalid payload for get ChiTietPhieuDat by PhieuDat ID").errorCode("INVALID_PAYLOAD").build();
            }
            String maPD = (String) command.getPayload().get("maPD");
            ArrayList<ChiTietPhieuDatThuocDTO> list = (ArrayList<ChiTietPhieuDatThuocDTO>) serviceLocator.getChiTietPhieuDatService().getChiTietTheoPhieu(maPD);
            return Response.builder().success(true).message("ChiTietPhieuDat list retrieved successfully").data(list).build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting ChiTietPhieuDat by PhieuDat ID", e);
            return Response.builder().success(false).message("Error retrieving ChiTietPhieuDat list: " + e.getMessage()).errorCode("GET_CHITIETPHIEUDAT_BY_PHIEUDAT_ID_ERROR").build();
        }
    }

    private Response handleCreateChiTietPhieuDat(Command command) {
        try {
            if (command.getPayload() == null || !command.getPayload().containsKey("chiTietPhieuDat")) {
                return Response.builder().success(false).message("Invalid payload for create ChiTietPhieuDat").errorCode("INVALID_PAYLOAD").build();
            }
            ChiTietPhieuDatThuocDTO dto = (ChiTietPhieuDatThuocDTO) command.getPayload().get("chiTietPhieuDat");
            boolean success = serviceLocator.getChiTietPhieuDatService().themChiTietPhieuDatVaoDBS(dto);
            return Response.builder().success(success).message(success ? "ChiTietPhieuDat created successfully" : "Failed to create ChiTietPhieuDat").errorCode(success ? null : "CREATE_CHITIETPHIEUDAT_FAILED").build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error creating ChiTietPhieuDat", e);
            return Response.builder().success(false).message("Error creating ChiTietPhieuDat: " + e.getMessage()).errorCode("CREATE_CHITIETPHIEUDAT_ERROR").build();
        }
    }

    private Response handleUpdateChiTietPhieuDatPaymentStatus(Command command) {
        try {
            if (command.getPayload() == null || !command.getPayload().containsKey("maCTPD") || !command.getPayload().containsKey("paymentStatus")) {
                return Response.builder().success(false).message("Invalid payload for update ChiTietPhieuDat payment status").errorCode("INVALID_PAYLOAD").build();
            }
            String maCTPD = (String) command.getPayload().get("maCTPD");
            boolean success = serviceLocator.getChiTietPhieuDatService().thanhToanChiTietVoiMa(maCTPD);
            return Response.builder().success(success).message(success ? "ChiTietPhieuDat payment status updated successfully" : "Failed to update payment status").errorCode(success ? null : "UPDATE_CHITIETPHIEUDAT_PAYMENT_FAILED").build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error updating ChiTietPhieuDat payment status", e);
            return Response.builder().success(false).message("Error updating payment status: " + e.getMessage()).errorCode("UPDATE_CHITIETPHIEUDAT_PAYMENT_ERROR").build();
        }
    }

    private Response handleCancelChiTietPhieuDat(Command command) {
        try {
            if (command.getPayload() == null || !command.getPayload().containsKey("maCTPD")) {
                return Response.builder().success(false).message("Invalid payload for cancel ChiTietPhieuDat").errorCode("INVALID_PAYLOAD").build();
            }
            String maCTPD = (String) command.getPayload().get("maCTPD");
            boolean success = serviceLocator.getChiTietPhieuDatService().huyChiTietPhieu(maCTPD);
            return Response.builder().success(success).message(success ? "ChiTietPhieuDat cancelled successfully" : "Failed to cancel ChiTietPhieuDat").errorCode(success ? null : "CANCEL_CHITIETPHIEUDAT_FAILED").build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error cancelling ChiTietPhieuDat", e);
            return Response.builder().success(false).message("Error cancelling ChiTietPhieuDat: " + e.getMessage()).errorCode("CANCEL_CHITIETPHIEUDAT_ERROR").build();
        }
    }

    private Response handleRestoreChiTietPhieuDat(Command command) {
        try {
            if (command.getPayload() == null || !command.getPayload().containsKey("maCTPD")) {
                return Response.builder().success(false).message("Invalid payload for restore ChiTietPhieuDat").errorCode("INVALID_PAYLOAD").build();
            }
            String maCTPD = (String) command.getPayload().get("maCTPD");
            boolean success = serviceLocator.getChiTietPhieuDatService().khoiPhucChiTietPhieu(maCTPD);
            return Response.builder().success(success).message(success ? "ChiTietPhieuDat restored successfully" : "Failed to restore ChiTietPhieuDat").errorCode(success ? null : "RESTORE_CHITIETPHIEUDAT_FAILED").build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error restoring ChiTietPhieuDat", e);
            return Response.builder().success(false).message("Error restoring ChiTietPhieuDat: " + e.getMessage()).errorCode("RESTORE_CHITIETPHIEUDAT_ERROR").build();
        }
    }

    // Additional ThongKe Handlers
    private Response handleGetTongSoThuoc(Command command) {
        try {
            int tongSoThuoc = serviceLocator.getThongKeTrangChinhService().getTongSoThuoc();
            return Response.builder().success(true).message("TongSoThuoc retrieved successfully").data(tongSoThuoc).build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting TongSoThuoc", e);
            return Response.builder().success(false).message("Error retrieving TongSoThuoc: " + e.getMessage()).errorCode("GET_TONG_SO_THUOC_ERROR").build();
        }
    }

    private Response handleGetTongSoNhanVien(Command command) {
        try {
            int tongSoNhanVien = serviceLocator.getThongKeTrangChinhService().getTongSoNhanVien();
            return Response.builder().success(true).message("TongSoNhanVien retrieved successfully").data(tongSoNhanVien).build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting TongSoNhanVien", e);
            return Response.builder().success(false).message("Error retrieving TongSoNhanVien: " + e.getMessage()).errorCode("GET_TONG_SO_NHANVIEN_ERROR").build();
        }
    }

    private Response handleGetSoHoaDonHomNay(Command command) {
        try {
            int soHoaDonHomNay = serviceLocator.getThongKeTrangChinhService().getSoHoaDonHomNay();
            return Response.builder().success(true).message("SoHoaDonHomNay retrieved successfully").data(soHoaDonHomNay).build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting SoHoaDonHomNay", e);
            return Response.builder().success(false).message("Error retrieving SoHoaDonHomNay: " + e.getMessage()).errorCode("GET_SO_HOADON_HOMNAY_ERROR").build();
        }
    }

    private Response handleGetSoKhuyenMaiApDung(Command command) {
        try {
            int soKhuyenMaiApDung = serviceLocator.getThongKeTrangChinhService().getSoKhuyenMaiApDung();
            return Response.builder().success(true).message("SoKhuyenMaiApDung retrieved successfully").data(soKhuyenMaiApDung).build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting SoKhuyenMaiApDung", e);
            return Response.builder().success(false).message("Error retrieving SoKhuyenMaiApDung: " + e.getMessage()).errorCode("GET_SO_KHUYENMAI_APDUNG_ERROR").build();
        }
    }

    private Response handleGetDoanhThu7Ngay(Command command) {
        try {
            Map<String, Double> doanhThu7Ngay = serviceLocator.getThongKeTrangChinhService().getDoanhThu7NgayGanNhat();
            return Response.builder().success(true).message("DoanhThu7NgayGanNhat retrieved successfully").data(doanhThu7Ngay).build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting DoanhThu7Ngay", e);
            return Response.builder().success(false).message("Error retrieving DoanhThu7Ngay: " + e.getMessage()).errorCode("GET_DOANHTHU_7_NGAY_ERROR").build();
        }
    }

    private Response handleGetTopSanPhamBanChay(Command command) {
        try {
            int limit = command.getPayload() != null && command.getPayload().containsKey("limit") ? (Integer) command.getPayload().get("limit") : 10;
            Map<String, Integer> topSanPham = serviceLocator.getThongKeDoanhThuService().getTopSanPhamBanChay(LocalDate.now().minusDays(1),LocalDate.now(), limit);
            return Response.builder().success(true).message("TopSanPhamBanChay retrieved successfully").data(topSanPham).build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting TopSanPhamBanChay", e);
            return Response.builder().success(false).message("Error retrieving TopSanPhamBanChay: " + e.getMessage()).errorCode("GET_TOP_SANPHAM_BANCHAY_ERROR").build();
        }
    }

    private Response handleGetThuocSapHetHan(Command command) {
        try {
            List<Map<String, Object>> thuocSapHetHan = serviceLocator.getThongKeTrangChinhService().getThuocSapHetHan();
            return Response.builder().success(true).message("ThuocSapHetHan retrieved successfully").data(thuocSapHetHan).build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting ThuocSapHetHan", e);
            return Response.builder().success(false).message("Error retrieving ThuocSapHetHan: " + e.getMessage()).errorCode("GET_THUOC_SAP_HETHAN_ERROR").build();
        }
    }

    private Response handleGetThuocTonKhoThap(Command command) {
        try {
            List<Map<String, Object>> thuocTonKhoThap = serviceLocator.getThongKeTrangChinhService().getThuocTonKhoThap();
            return Response.builder().success(true).message("ThuocTonKhoThap retrieved successfully").data(thuocTonKhoThap).build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting ThuocTonKhoThap", e);
            return Response.builder().success(false).message("Error retrieving ThuocTonKhoThap: " + e.getMessage()).errorCode("GET_THUOC_TONKHO_THAP_ERROR").build();
        }
    }

//    private Response handleGetThongKeDoanhThu(Command command) {
//        try {
//            Map<String, Object> thongKeDoanhThu = serviceLocator.getThongKeTrangChinhService().
//            return Response.builder().success(true).message("ThongKeDoanhThu retrieved successfully").data(thongKeDoanhThu).build();
//        } catch (Exception e) {
//            LOGGER.log(Level.SEVERE, "Error getting ThongKeDoanhThu", e);
//            return Response.builder().success(false).message("Error retrieving ThongKeDoanhThu: " + e.getMessage()).errorCode("GET_THONGKE_DOANHTHU_ERROR").build();
//        }
//    }

    private Response handleGetDoanhThuTheoThang(Command command) {
        try {
            if (command.getPayload() == null || !command.getPayload().containsKey("startDate") || !command.getPayload().containsKey("endDate")) {
                return Response.builder().success(false).message("Invalid payload for get DoanhThu theo thang").errorCode("INVALID_PAYLOAD").build();
            }
            LocalDate tu = LocalDate.parse((String) command.getPayload().get("startDate"));
            LocalDate den = LocalDate.parse((String) command.getPayload().get("endDate"));
            String nv = (String) command.getPayload().get("nhanVien");
            ArrayList<ThongKeDoanhThuDTO> doanhThuTheoThang = serviceLocator.getThongKeDoanhThuService().getDoanhThuTheoThang(tu, den, nv);
            return Response.builder().success(true).message("DoanhThuTheoThang retrieved successfully").data(doanhThuTheoThang).build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting DoanhThuTheoThang", e);
            return Response.builder().success(false).message("Error retrieving DoanhThuTheoThang: " + e.getMessage()).errorCode("GET_DOANHTHU_THEO_THANG_ERROR").build();
        }
    }

    private Response handleGetDoanhThuTheoThoiGian(Command command) {
        try {
            if (command.getPayload() == null || !command.getPayload().containsKey("startDate") || !command.getPayload().containsKey("endDate")) {
                return Response.builder().success(false).message("Invalid payload for get DoanhThu theo thoi gian").errorCode("INVALID_PAYLOAD").build();
            }
            LocalDate startDate =LocalDate.parse( (String) command.getPayload().get("startDate"));
            LocalDate endDate = LocalDate.parse((String) command.getPayload().get("endDate"));
            String nv = (String) command.getPayload().get("nhanVien");
            ArrayList<ThongKeDoanhThuDTO> doanhThuTheoThoiGian = serviceLocator.getThongKeDoanhThuService().getDoanhThuTheoThoiGian(startDate, endDate, nv);
            return Response.builder().success(true).message("DoanhThuTheoThoiGian retrieved successfully").data(doanhThuTheoThoiGian).build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting DoanhThuTheoThoiGian", e);
            return Response.builder().success(false).message("Error retrieving DoanhThuTheoThoiGian: " + e.getMessage()).errorCode("GET_DOANHTHU_THEO_THOIGIAN_ERROR").build();
        }
    }

    private Response handleGetTongDoanhThu(Command command) {
        try {
            LocalDate startDate =LocalDate.parse( (String) command.getPayload().get("startDate"));
            LocalDate endDate = LocalDate.parse((String) command.getPayload().get("endDate"));
            String nv = (String) command.getPayload().get("nhanVien");
            double tongDoanhThu = serviceLocator.getThongKeDoanhThuService().getTongDoanhThu(startDate,endDate,nv);
            return Response.builder().success(true).message("TongDoanhThu retrieved successfully").data(tongDoanhThu).build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting TongDoanhThu", e);
            return Response.builder().success(false).message("Error retrieving TongDoanhThu: " + e.getMessage()).errorCode("GET_TONG_DOANHTHU_ERROR").build();
        }
    }

    private Response handleGetTongDonHang(Command command) {
        try {
            LocalDate startDate =LocalDate.parse( (String) command.getPayload().get("startDate"));
            LocalDate endDate = LocalDate.parse((String) command.getPayload().get("endDate"));
            String nv = (String) command.getPayload().get("nhanVien");
            int tongDonHang = serviceLocator.getThongKeDoanhThuService().getTongDonHang(startDate,endDate,nv);
            return Response.builder().success(true).message("TongDonHang retrieved successfully").data(tongDonHang).build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting TongDonHang", e);
            return Response.builder().success(false).message("Error retrieving TongDonHang: " + e.getMessage()).errorCode("GET_TONG_DONHANG_ERROR").build();
        }
    }

    private Response handleGetSoKhachHangMoi(Command command) {
        try {
            LocalDate startDate =LocalDate.parse( (String) command.getPayload().get("startDate"));
            LocalDate endDate = LocalDate.parse((String) command.getPayload().get("endDate"));
            String nv = (String) command.getPayload().get("nhanVien");
            int soKhachHangMoi = serviceLocator.getThongKeDoanhThuService().getSoKhachHangMoi(startDate,endDate,nv);
            return Response.builder().success(true).message("SoKhachHangMoi retrieved successfully").data(soKhachHangMoi).build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting SoKhachHangMoi", e);
            return Response.builder().success(false).message("Error retrieving SoKhachHangMoi: " + e.getMessage()).errorCode("GET_SO_KHACHHANG_MOI_ERROR").build();
        }
    }

    private Response handleGetTopSanPhamBanChayTheoThoiGian(Command command) {
        try {
            if (command.getPayload() == null || !command.getPayload().containsKey("startDate") || !command.getPayload().containsKey("endDate")) {
                return Response.builder().success(false).message("Invalid payload for get top sanpham ban chay theo thoi gian").errorCode("INVALID_PAYLOAD").build();
            }
            String startDate = (String) command.getPayload().get("startDate");
            String endDate = (String) command.getPayload().get("endDate");
            int limit = command.getPayload().containsKey("limit") ? (Integer) command.getPayload().get("limit") : 10;
            Map<String, Integer> topSanPham = serviceLocator.getThongKeDoanhThuService().getTopSanPhamBanChay(LocalDate.parse(startDate), LocalDate.parse(endDate), limit);
            return Response.builder().success(true).message("TopSanPhamBanChayTheoThoiGian retrieved successfully").data(topSanPham).build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting top sanpham ban chay theo thoi gian", e);
            return Response.builder().success(false).message("Error retrieving top sanpham: " + e.getMessage()).errorCode("GET_TOP_SANPHAM_BANCHAY_THEO_THOIGIAN_ERROR").build();
        }
    }

    private Response handleGetNhanVienListForFilter(Command command) {
        try {
            List<NhanVienDTO> nhanVienList = serviceLocator.getNhanVienService().getAllNhanVien();
            return Response.builder().success(true).message("NhanVien list for filter retrieved successfully").data(nhanVienList).build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting NhanVien list for filter", e);
            return Response.builder().success(false).message("Error retrieving NhanVien list for filter: " + e.getMessage()).errorCode("GET_NHANVIEN_LIST_FOR_FILTER_ERROR").build();
        }
    }

    private Response handleGetLoThuocForChiTietPhieuDat(Command command) {
        try {
            if (command.getPayload() == null || !command.getPayload().containsKey("maThuoc")) {
                return Response.builder().success(false).message("Invalid payload for get LoThuoc for ChiTietPhieuDat").errorCode("INVALID_PAYLOAD").build();
            }
            String maThuoc = (String) command.getPayload().get("maThuoc");
            ArrayList<LoThuocDTO> loThuocList = serviceLocator.getLoThuocService().getAllChiTietThuocVoiMaChoCTPD(maThuoc);
            return Response.builder().success(true).message("LoThuoc for ChiTietPhieuDat retrieved successfully").data(loThuocList).build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting LoThuoc for ChiTietPhieuDat", e);
            return Response.builder().success(false).message("Error retrieving LoThuoc: " + e.getMessage()).errorCode("GET_LOTHUOC_FOR_CHITIETPHIEUDAT_ERROR").build();
        }
    }
}

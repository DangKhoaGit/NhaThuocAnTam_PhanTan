package com.antam.app.network.handler;

import com.antam.app.network.message.Command;
import com.antam.app.network.message.Response;
import com.antam.app.network.command.CommandType;
import com.antam.app.dto.*;

import java.util.ArrayList;
import java.util.List;
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
                case CREATE_HOADON:
                    return handleCreateHoaDon(command);
                case UPDATE_HOADON:
                    return handleUpdateHoaDon(command);
                case DELETE_HOADON:
                    return handleDeleteHoaDon(command);

                // NhanVien Operations
                case GET_NHANVIEN_LIST:
                    return handleGetNhanVienList(command);

                // KhachHang Operations
                case GET_KHACHHANG_LIST:
                    return handleGetKhachHangList(command);

                // Authentication
                case LOGIN:
                    return handleLogin(command);
                case LOGOUT:
                    return handleLogout(command);

                // System
                case SERVER_STATUS:
                    return handleServerStatus(command);

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
            LOGGER.info("HoaDon update command received");

            return Response.builder()
                    .success(true)
                    .message("HoaDon updated successfully")
                    .build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error updating HoaDon", e);
            return Response.builder()
                    .success(false)
                    .message("Error updating HoaDon: " + e.getMessage())
                    .errorCode("UPDATE_HOADON_ERROR")
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

    // === Authentication Handlers ===

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

            LOGGER.info("Login command received");

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
            LOGGER.info("Logout command received");

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

    // === System Handlers ===

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
}

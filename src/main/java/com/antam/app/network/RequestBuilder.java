package com.antam.app.network;

import com.antam.app.dto.HoaDonDTO;
import com.antam.app.network.message.Command;
import com.antam.app.network.command.CommandType;

import java.util.HashMap;
import java.util.Map;

/*
 * @description: Builder class để tạo các command requests
 * Sử dụng Builder pattern để tạo commands một cách dễ dàng
 * @author: Pham Dang Khoa
 * @date: 21/04/2026
 * @version: 1.0
 */
public class RequestBuilder {

    /**
     * Tạo command để lấy danh sách hóa đơn
     */
    public static Command buildGetHoaDonListCommand(String filter) {
        Map<String, Object> payload = new HashMap<>();
        if (filter != null && !filter.isEmpty()) {
            payload.put("filter", filter);
        }

        return Command.builder()
                .type(CommandType.GET_HOADON_LIST)
                .payload(payload)
                .timestamp(System.currentTimeMillis())
                .build();
    }

    /**
     * Tạo command để tạo hóa đơn
     */
    public static Command buildCreateHoaDonCommand(HoaDonDTO dto) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("hoaDon", dto);

        return Command.builder()
                .type(CommandType.CREATE_HOADON)
                .payload(payload)
                .timestamp(System.currentTimeMillis())
                .build();
    }

    /**
     * Tạo command để cập nhật hóa đơn
     */
    public static Command buildUpdateHoaDonCommand(HoaDonDTO dto) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("hoaDon", dto);

        return Command.builder()
                .type(CommandType.UPDATE_HOADON)
                .payload(payload)
                .timestamp(System.currentTimeMillis())
                .build();
    }

    /**
     * Tạo command để xóa hóa đơn
     */
    public static Command buildDeleteHoaDonCommand(String maHD) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("maHD", maHD);

        return Command.builder()
                .type(CommandType.DELETE_HOADON)
                .payload(payload)
                .timestamp(System.currentTimeMillis())
                .build();
    }

    /**
     * Tạo command để lấy danh sách nhân viên
     */
    public static Command buildGetNhanVienListCommand(String filter) {
        Map<String, Object> payload = new HashMap<>();
        if (filter != null && !filter.isEmpty()) {
            payload.put("filter", filter);
        }

        return Command.builder()
                .type(CommandType.GET_NHANVIEN_LIST)
                .payload(payload)
                .timestamp(System.currentTimeMillis())
                .build();
    }

    /**
     * Tạo command để lấy danh sách khách hàng
     */
    public static Command buildGetKhachHangListCommand(String filter) {
        Map<String, Object> payload = new HashMap<>();
        if (filter != null && !filter.isEmpty()) {
            payload.put("filter", filter);
        }

        return Command.builder()
                .type(CommandType.GET_KHACHHANG_LIST)
                .payload(payload)
                .timestamp(System.currentTimeMillis())
                .build();
    }

    /**
     * Tạo command để login
     */
    public static Command buildLoginCommand(String username, String password) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("username", username);
        payload.put("password", password);

        return Command.builder()
                .type(CommandType.LOGIN)
                .payload(payload)
                .timestamp(System.currentTimeMillis())
                .build();
    }

    /**
     * Tạo command để logout
     */
    public static Command buildLogoutCommand(String sessionId) {
        Map<String, Object> payload = new HashMap<>();

        return Command.builder()
                .type(CommandType.LOGOUT)
                .payload(payload)
                .sessionId(sessionId)
                .timestamp(System.currentTimeMillis())
                .build();
    }

    /**
     * Tạo command để kiểm tra trạng thái server
     */
    public static Command buildServerStatusCommand() {
        return Command.builder()
                .type(CommandType.SERVER_STATUS)
                .payload(new HashMap<>())
                .timestamp(System.currentTimeMillis())
                .build();
    }
}

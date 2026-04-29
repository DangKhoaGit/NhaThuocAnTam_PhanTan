package com.antam.app.network;

import com.antam.app.dto.HoaDonDTO;
import com.antam.app.network.config.NetworkConfig;
import com.antam.app.network.message.Response;

import java.util.List;
import java.util.logging.Logger;

/*
 * @description: Example client usage
 * Demonstrates how to use ClientManager to communicate with server
 * @author: Pham Dang Khoa
 * @date: 21/04/2026
 * @version: 1.0
 */
public class ClientExample {
    private static final Logger LOGGER = Logger.getLogger(ClientExample.class.getName());

    // ANSI color codes
    private static final String RESET = "\u001B[0m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String RED = "\u001B[31m";
    private static final String BLUE = "\u001B[34m";
    private static final String CYAN = "\u001B[36m";

    public static void main(String[] args) {
        try {
            // Lấy ClientManager singleton
            ClientManager clientManager = new ClientManager();

            System.out.println(BLUE + "🔗 Connecting to server..." + RESET);
            // Kết nối tới server
            boolean connected = clientManager.connectToServer("localhost", 9999);

            if (!connected) {
                System.out.println(RED + "❌ Failed to connect to server" + RESET);
                return;
            }

            System.out.println(GREEN + "✅ Connected successfully!" + RESET);

//            // === Example 1: Get HoaDon List ===
//            System.out.println(CYAN + "\n📋 === Fetching HoaDon List ===" + RESET);
//            List<HoaDonDTO> hoaDonList = (List<HoaDonDTO>) clientManager.getHoaDonList();
//            System.out.println(GREEN + "✅ Retrieved " + hoaDonList.size() + " HoaDons" + RESET);
//            for (HoaDonDTO hd : hoaDonList) {
//                System.out.println("  📄 " + hd.getMaHD() + ": " + YELLOW + hd.getTongTien() + RESET);
//            }
//
//            // === Example 2: Check Server Status ===
//            System.out.println(CYAN + "\n🔍 === Checking Server Status ===" + RESET);
//            boolean serverOk = (boolean) clientManager.serverStatus();
//            if (serverOk) {
//                System.out.println(GREEN + "✅ Server Status: OK" + RESET);
//            } else {
//                System.out.println(RED + "❌ Server Status: FAILED" + RESET);
//            }
//
//            // === Example 3: Get NhanVien List ===
//            System.out.println(CYAN + "\n👥 === Fetching NhanVien List ===" + RESET);
//            var nhanVienList = clientManager.getNhanVienList();
//            System.out.println(GREEN + "✅ Retrieved " + nhanVienList.size() + " NhanViens" + RESET);
//
//            // === Example 4: Get KhachHang List ===
//            System.out.println(CYAN + "\n🛒 === Fetching KhachHang List ===" + RESET);
//            var khachHangList = clientManager.getKhachHangList();
//            System.out.println(GREEN + "✅ Retrieved " + khachHangList.size() + " KhachHangs" + RESET);


            var khachHangList = clientManager.getMaxHashNhanVien();
            System.out.println(GREEN + "✅ Retrieved " + khachHangList+ RESET);


            System.out.println(GREEN + "\n🎉 === All examples completed successfully! ===" + RESET);

        } catch (Exception e) {
            System.out.println(RED + "❌ Error: " + e.getMessage() + RESET);
            e.printStackTrace();
        } finally {
            // Disconnect from server
            ClientManager.getInstance().disconnectFromServer();
            System.out.println(BLUE + "🔌 Disconnected from server" + RESET);
        }
    }


}

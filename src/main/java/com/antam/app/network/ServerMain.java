package com.antam.app.network;

import com.antam.app.network.config.NetworkConfig;

import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * @description: Main class để khởi động server
 * Có thể chạy như một ứng dụng độc lập
 * @author: Pham Dang Khoa
 * @date: 21/04/2026
 * @version: 1.0
 */
public class ServerMain {
    private static final Logger LOGGER = Logger.getLogger(ServerMain.class.getName());
    
    // ANSI color codes
    private static final String RESET = "\u001B[0m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String RED = "\u001B[31m";
    private static final String BLUE = "\u001B[34m";
    private static final String CYAN = "\u001B[36m";
    private static final String MAGENTA = "\u001B[35m";

    public static void main(String[] args) {
        try {
            // Lấy cấu hình
            NetworkConfig config = NetworkConfig.getInstance();
            int port = config.getServerPort();

            // Nếu có argument, sử dụng port từ argument
            if (args.length > 0) {
                try {
                    port = Integer.parseInt(args[0]);
                } catch (NumberFormatException e) {
                    System.out.println(YELLOW + "⚠️  Invalid port argument, using default: " + port + RESET);
                }
            }

            // Khởi động server
            Sever server = new Sever(port);
            server.start();

            System.out.println(GREEN + "╔══════════════════════════════════════════════════════════════╗" + RESET);
            System.out.println(GREEN + "║                                                              ║" + RESET);
            System.out.println(GREEN + "║              🚀 SERVER STARTED SUCCESSFULLY!                ║" + RESET);
            System.out.println(GREEN + "║                                                              ║" + RESET);
            System.out.println(GREEN + "╠══════════════════════════════════════════════════════════════╣" + RESET);
            System.out.println(GREEN + "║                                                              ║" + RESET);
            System.out.println(GREEN + "║  Port: " + CYAN + port + GREEN + "                                                  ║" + RESET);
            System.out.println(GREEN + "║  Status: " + GREEN + "Running" + GREEN + "                                              ║" + RESET);
            System.out.println(GREEN + "║                                                              ║" + RESET);
            System.out.println(GREEN + "╠══════════════════════════════════════════════════════════════╣" + RESET);
            System.out.println(GREEN + "║                                                              ║" + RESET);
            System.out.println(GREEN + "║  Commands:                                                   ║" + RESET);
            System.out.println(GREEN + "║  • " + CYAN + "q, quit, exit" + GREEN + " - Shutdown server                    ║" + RESET);
            System.out.println(GREEN + "║  • " + CYAN + "status" + GREEN + "        - Show server status                 ║" + RESET);
            System.out.println(GREEN + "║  • " + CYAN + "help" + GREEN + "          - Show help message                  ║" + RESET);
            System.out.println(GREEN + "║                                                              ║" + RESET);
            System.out.println(GREEN + "╚══════════════════════════════════════════════════════════════╝" + RESET);
            System.out.println();
            System.out.println(BLUE + "⏳ Waiting for clients to connect..." + RESET);

            // Interactive console
            Scanner scanner = new Scanner(System.in);
            while (server.isRunning()) {
                try {
                    String input = scanner.nextLine().trim().toLowerCase();

                    switch (input) {
                        case "q":
                        case "quit":
                        case "exit":
                            System.out.println(YELLOW + "🛑 Shutting down server..." + RESET);
                            server.stop();
                            break;

                        case "status":
                            boolean isRunning = server.isRunning();
                            int activeClients = server.getActiveClientCount();
                            System.out.println(CYAN + "📊 Server Status:" + RESET);
                            System.out.println("  Status: " + (isRunning ? GREEN + "Running" : RED + "Stopped") + RESET);
                            System.out.println("  Active Clients: " + YELLOW + activeClients + RESET);
                            break;

                        case "help":
                            printHelp();
                            break;

                        default:
                            if (!input.isEmpty()) {
                                System.out.println(YELLOW + "⚠️  Unknown command: " + input + RESET);
                                printHelp();
                            }
                    }
                } catch (Exception e) {
                    System.out.println(RED + "❌ Error processing command: " + e.getMessage() + RESET);
                }
            }

            scanner.close();
            System.out.println(GREEN + "✅ Server shutdown complete" + RESET);

        } catch (IOException e) {
            System.out.println(RED + "❌ Failed to start server: " + e.getMessage() + RESET);
            System.exit(1);
        }
    }

    private static void printHelp() {
        System.out.println();
        System.out.println(CYAN + "📋 Available commands:" + RESET);
        System.out.println("  " + GREEN + "q, quit, exit" + RESET + " - Shutdown the server");
        System.out.println("  " + GREEN + "status" + RESET + "        - Show server status and active clients");
        System.out.println("  " + GREEN + "help" + RESET + "          - Show this help message");
        System.out.println();
    }
}

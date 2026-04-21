package com.antam.app.network;

import com.antam.app.network.handler.ClientHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * @description: Server để nhận client connections
 * Xử lý các client trên các thread riêng biệt từ thread pool
 * @author: Pham Dang Khoa
 * @date: 21/04/2026
 * @version: 1.0
 */
public class Sever {
    private static final Logger LOGGER = Logger.getLogger(Sever.class.getName());
    private static final int DEFAULT_PORT = 9999;
    private static final int DEFAULT_THREAD_POOL_SIZE = 10;

    private ServerSocket serverSocket;
    private int port;
    private ExecutorService executorService;
    private volatile boolean running = false;
    private final Set<ClientHandler> activeClients = new HashSet<>();
    private final AtomicInteger clientIdCounter = new AtomicInteger(0);
    private Thread acceptThread;

    public Sever() {
        this.port = DEFAULT_PORT;
    }

    public Sever(int port) {
        this.port = port;
    }

    /**
     * Khởi động server
     */
    public void start(int port) throws IOException {
        this.port = port;
        start();
    }

    /**
     * Khởi động server
     */
    public synchronized void start() throws IOException {
        if (running) {
            LOGGER.warning("Server is already running");
            return;
        }

        try {
            serverSocket = new ServerSocket(port);
            executorService = Executors.newFixedThreadPool(DEFAULT_THREAD_POOL_SIZE);
            running = true;

            LOGGER.info("Server started on port " + port);

            // Bắt đầu thread để chấp nhận connections
            acceptThread = new Thread(this::acceptConnections, "ServerAcceptThread");
            acceptThread.setDaemon(false);
            acceptThread.start();

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to start server", e);
            running = false;
            throw e;
        }
    }

    /**
     * Chấp nhận các client connections
     */
    public void acceptConnections() {
        try {
            while (running && !serverSocket.isClosed()) {
                try {
                    Socket clientSocket = serverSocket.accept();

                    String clientId = "Client-" + clientIdCounter.incrementAndGet();
                    ClientHandler handler = new ClientHandler(clientSocket, clientId);

                    synchronized (activeClients) {
                        activeClients.add(handler);
                    }

                    // Xử lý client trên thread pool
                    executorService.execute(handler);

                    LOGGER.info("Client accepted: " + clientId);

                } catch (IOException e) {
                    if (running) {
                        LOGGER.log(Level.WARNING, "Error accepting client connection", e);
                    }
                }
            }
        } catch (Exception e) {
            if (running) {
                LOGGER.log(Level.SEVERE, "Error in accept loop", e);
            }
        } finally {
            LOGGER.info("Accept connections loop stopped");
        }
    }

    /**
     * Dừng server
     */
    public synchronized void stop() {
        if (!running) {
            LOGGER.warning("Server is not running");
            return;
        }

        running = false;

        try {
            // Đóng tất cả active clients
            synchronized (activeClients) {
                for (ClientHandler handler : activeClients) {
                    handler.stop();
                }
                activeClients.clear();
            }

            // Shutdown executor service
            if (executorService != null && !executorService.isShutdown()) {
                executorService.shutdown();
                LOGGER.info("Executor service shutdown");
            }

            // Đóng server socket
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }

            LOGGER.info("Server stopped");

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error stopping server", e);
        }
    }

    /**
     * Kiểm tra xem server có đang chạy không
     */
    public boolean isRunning() {
        return running && serverSocket != null && !serverSocket.isClosed();
    }

    /**
     * Lấy số lượng clients đang kết nối
     */
    public int getActiveClientCount() {
        synchronized (activeClients) {
            return activeClients.size();
        }
    }

    /**
     * Lấy port của server
     */
    public int getPort() {
        return port;
    }

    /**
     * Gửi broadcast message tới tất cả clients
     * (Có thể mở rộng trong tương lai)
     */
    public void broadcast(String message) {
        synchronized (activeClients) {
            LOGGER.info("Broadcasting to " + activeClients.size() + " clients: " + message);
        }
    }
}


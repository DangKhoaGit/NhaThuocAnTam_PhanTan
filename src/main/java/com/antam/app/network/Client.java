package com.antam.app.network;

import com.antam.app.network.message.Command;
import com.antam.app.network.message.Response;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * @description: Client để kết nối tới Server
 * Sử dụng Socket và Object streams để trao đổi Command/Response
 * @author: Pham Dang Khoa
 * @date: 21/04/2026
 * @version: 1.0
 */
public class Client {
    private static final Logger LOGGER = Logger.getLogger(Client.class.getName());
    private static final long CONNECTION_TIMEOUT = 5000; // 5 giây
    private static final long COMMAND_TIMEOUT = 30000; // 30 giây
    private static final int MAX_RECONNECT_ATTEMPTS = 3;

    private String serverHost;
    private int serverPort;
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private volatile boolean connected = false;
    private final ReentrantReadWriteLock streamLock = new ReentrantReadWriteLock();

    public Client() {
        this.serverHost = "localhost";
        this.serverPort = 9999;
    }

    public Client(String serverHost, int serverPort) {
        this.serverHost = serverHost;
        this.serverPort = serverPort;
    }

    /**
     * Kết nối tới server
     */
    public boolean connect() throws IOException {
        streamLock.writeLock().lock();
        try {
            // Nếu đã có kết nối, ngắt trước
            if (socket != null && socket.isConnected()) {
                disconnect();
            }

            try {
                socket = new Socket(serverHost, serverPort);
                socket.setSoTimeout((int) COMMAND_TIMEOUT);

                // Khởi tạo streams (output trước input)
                // Output stream phải được tạo và flush trước input stream
                this.out = new ObjectOutputStream(socket.getOutputStream());
                this.out.flush();

                this.in = new ObjectInputStream(socket.getInputStream());

                connected = true;
                LOGGER.info("Connected to server: " + serverHost + ":" + serverPort);
                return true;
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "Failed to connect to server", e);
                connected = false;
                cleanupStreams();
                throw e;
            }
        } finally {
            streamLock.writeLock().unlock();
        }
    }

    /**
     * Đóng và tạo lại streams (dùng khi stream corruption)
     */
    private void cleanupStreams() {
        try {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    LOGGER.log(Level.FINE, "Error closing input stream", e);
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    LOGGER.log(Level.FINE, "Error closing output stream", e);
                }
            }
        } finally {
            in = null;
            out = null;
        }
    }

    /**
     * Kiểm tra xem đã kết nối chưa
     */
    public boolean isConnected() {
        return connected && socket != null && socket.isConnected() && !socket.isClosed();
    }

    /**
     * Gửi command tới server và nhận response
     * Tự động reconnect nếu stream bị corrupt
     */
    public Response sendCommand(Command command) throws IOException {
        if (command == null) {
            throw new IllegalArgumentException("Command cannot be null");
        }

        // Thử gửi command với retry logic
        for (int attempt = 0; attempt < MAX_RECONNECT_ATTEMPTS; attempt++) {
            try {
                return sendCommandInternal(command);
            } catch (IOException e) {
                LOGGER.log(Level.WARNING, "Send command failed (attempt " + (attempt + 1) + "/" + MAX_RECONNECT_ATTEMPTS + "): " + e.getMessage());

                // Nếu là lần cuối, throw exception
                if (attempt == MAX_RECONNECT_ATTEMPTS - 1) {
                    throw e;
                }

                // Thử reconnect
                try {
                    disconnect();
                    LOGGER.info("Attempting to reconnect...");
                    connect();
                } catch (IOException reconnectError) {
                    LOGGER.log(Level.SEVERE, "Reconnection failed", reconnectError);
                    throw new IOException("Command failed and reconnection failed", e);
                }
            }
        }

        throw new IOException("Failed to send command after " + MAX_RECONNECT_ATTEMPTS + " attempts");
    }

    /**
     * Gửi command nội bộ
     */
    private final Object requestLock = new Object();

    private Response sendCommandInternal(Command command) throws IOException {
        synchronized (requestLock) {
            if (!isConnected()) {
                throw new IOException("Not connected to server");
            }

            try {
                out.writeObject(command);
                out.flush();
                out.reset();

                Object obj = in.readObject();

                if (!(obj instanceof Response)) {
                    throw new IOException("Invalid response: " + obj);
                }

                return (Response) obj;

            } catch (ClassNotFoundException e) {
                connected = false;
                throw new IOException("Invalid response format", e);
            } catch (IOException e) {
                connected = false;
                throw e;
            }
        }
    }

    /**
     * Kết nối tới server
     */
    public void connect(String host, int port) throws IOException {
        this.serverHost = host;
        this.serverPort = port;
        connect();
    }

    /**
     * Ngắt kết nối
     */
    public void disconnect() {
        streamLock.writeLock().lock();
        try {
            connected = false;
            cleanupStreams();

            if (socket != null && !socket.isClosed()) {
                try {
                    socket.close();
                } catch (IOException e) {
                    LOGGER.log(Level.WARNING, "Error closing socket", e);
                }
            }
            LOGGER.info("Disconnected from server");
        } finally {
            streamLock.writeLock().unlock();
        }
    }

    /**
     * Lấy server host
     */
    public String getServerHost() {
        return serverHost;
    }

    /**
     * Lấy server port
     */
    public int getServerPort() {
        return serverPort;
    }
}

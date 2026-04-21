package com.antam.app.network;

import com.antam.app.network.message.Command;
import com.antam.app.network.message.Response;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
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

    private String serverHost;
    private int serverPort;
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private volatile boolean connected = false;

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
        try {
            socket = new Socket(serverHost, serverPort);
            socket.setSoTimeout((int) COMMAND_TIMEOUT);

            // Khởi tạo streams (output trước input)
            this.out = new ObjectOutputStream(socket.getOutputStream());
            this.out.flush();
            this.in = new ObjectInputStream(socket.getInputStream());

            connected = true;
            LOGGER.info("Connected to server: " + serverHost + ":" + serverPort);
            return true;
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to connect to server", e);
            connected = false;
            throw e;
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
     */
    public Response sendCommand(Command command) throws IOException {
        if (!isConnected()) {
            throw new IOException("Not connected to server");
        }

        if (command == null) {
            throw new IllegalArgumentException("Command cannot be null");
        }

        try {
            // Gửi command
            synchronized (out) {
                out.writeObject(command);
                out.flush();
                LOGGER.fine("Command sent: " + command.getType());
            }

            // Nhận response
            Object obj = in.readObject();

            if (!(obj instanceof Response)) {
                throw new IOException("Received non-Response object from server");
            }

            Response response = (Response) obj;
            LOGGER.fine("Response received: success=" + response.isSuccess());
            return response;

        } catch (ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "Class not found while deserializing response", e);
            throw new IOException("Invalid response format from server", e);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error communicating with server", e);
            connected = false;
            throw e;
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
        connected = false;
        try {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
            LOGGER.info("Disconnected from server");
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Error disconnecting from server", e);
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

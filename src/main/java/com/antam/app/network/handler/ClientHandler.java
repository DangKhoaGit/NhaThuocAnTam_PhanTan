package com.antam.app.network.handler;

import com.antam.app.network.message.Command;
import com.antam.app.network.message.Response;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * @description: Xử lý các client connections trên server
 * Mỗi ClientHandler chạy trên một thread riêng
 * @author: Pham Dang Khoa
 * @date: 21/04/2026
 * @version: 1.0
 */
public class ClientHandler implements Runnable {
    private static final Logger LOGGER = Logger.getLogger(ClientHandler.class.getName());

    private final Socket clientSocket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private String clientId;
    private CommandRouter commandRouter;
    private volatile boolean running = true;

    public ClientHandler(Socket clientSocket, String clientId) {
        this.clientSocket = clientSocket;
        this.clientId = clientId;
        this.commandRouter = new CommandRouter();
    }

    @Override
    public void run() {
        try {
            // Khởi tạo streams (output trước input để tránh deadlock)
            this.out = new ObjectOutputStream(clientSocket.getOutputStream());
            this.out.flush();
            this.in = new ObjectInputStream(clientSocket.getInputStream());

            LOGGER.info("Client " + clientId + " connected from " + clientSocket.getInetAddress());

            // Lặp để nhận commands từ client
            while (running && !clientSocket.isClosed()) {
                try {
                    Object obj = in.readObject();

                    if (!(obj instanceof Command)) {
                        LOGGER.warning("Invalid object from " + clientId);
                        continue;
                    }

                    Command command = (Command) obj;
                    Response response = commandRouter.route(command, clientId);
                    sendResponse(response);

                } catch (EOFException e) {
                    LOGGER.info("Client " + clientId + " disconnected");
                    break;
                } catch (IOException e) {
                    LOGGER.warning("Connection lost with " + clientId + ": " + e.getMessage());
                    break; // 🔥 QUAN TRỌNG
                } catch (ClassNotFoundException e) {
                    LOGGER.log(Level.WARNING, "Class not found", e);
                }
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "IO Error handling client " + clientId, e);
        } finally {
            cleanup();
        }
    }

    /**
     * Gửi response về client
     */
    private void sendResponse(Response response) {
        try {
            if (out != null && !clientSocket.isClosed()) {
                synchronized (out) {
                    out.writeObject(response);
                    out.flush();
                    out.reset(); // QUAN TRỌNG: reset stream để tránh memory leak khi gửi nhiều object
                }
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error sending response to client " + clientId, e);
        }
    }

    /**
     * Đóng kết nối và clean up resources
     */
    private void cleanup() {
        running = false;
        try {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
            if (clientSocket != null && !clientSocket.isClosed()) {
                clientSocket.close();
            }
            LOGGER.info("Client " + clientId + " resources cleaned up");
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Error cleaning up client " + clientId, e);
        }
    }

    /**
     * Stop the handler gracefully
     */
    public void stop() {
        running = false;
        try {
            if (clientSocket != null && !clientSocket.isClosed()) {
                clientSocket.close();
            }
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Error stopping client handler " + clientId, e);
        }
    }

    /**
     * Kiểm tra xem handler có đang chạy không
     */
    public boolean isRunning() {
        return running && clientSocket != null && !clientSocket.isClosed();
    }

    /**
     * Lấy ID của client
     */
    public String getClientId() {
        return clientId;
    }
}

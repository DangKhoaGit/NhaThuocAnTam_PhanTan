package com.antam.app.network.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

/*
 * @description: Cấu hình cho network operations
 * @author: Pham Dang Khoa
 * @date: 21/04/2026
 * @version: 1.0
 */
public class NetworkConfig {
    private static final Logger LOGGER = Logger.getLogger(NetworkConfig.class.getName());

    private static final String DEFAULT_SERVER_HOST = "localhost";
    private static final int DEFAULT_SERVER_PORT = 9999;
    private static final int DEFAULT_THREAD_POOL_SIZE = 10;
    private static final long DEFAULT_CONNECTION_TIMEOUT = 5000;
    private static final long DEFAULT_COMMAND_TIMEOUT = 30000;

    private String serverHost;
    private int serverPort;
    private int threadPoolSize;
    private long connectionTimeout;
    private long commandTimeout;

    private static NetworkConfig instance;

    private NetworkConfig() {
        loadConfig();
    }

    public static synchronized NetworkConfig getInstance() {
        if (instance == null) {
            instance = new NetworkConfig();
        }
        return instance;
    }

    private void loadConfig() {
        // Mặc định
        this.serverHost = DEFAULT_SERVER_HOST;
        this.serverPort = DEFAULT_SERVER_PORT;
        this.threadPoolSize = DEFAULT_THREAD_POOL_SIZE;
        this.connectionTimeout = DEFAULT_CONNECTION_TIMEOUT;
        this.commandTimeout = DEFAULT_COMMAND_TIMEOUT;

        // Cố gắng tải từ properties file
        try {
            Properties props = new Properties();
            props.load(new FileInputStream("network.properties"));

            this.serverHost = props.getProperty("server.host", DEFAULT_SERVER_HOST);
            this.serverPort = Integer.parseInt(props.getProperty("server.port", String.valueOf(DEFAULT_SERVER_PORT)));
            this.threadPoolSize = Integer.parseInt(props.getProperty("thread.pool.size", String.valueOf(DEFAULT_THREAD_POOL_SIZE)));
            this.connectionTimeout = Long.parseLong(props.getProperty("connection.timeout", String.valueOf(DEFAULT_CONNECTION_TIMEOUT)));
            this.commandTimeout = Long.parseLong(props.getProperty("command.timeout", String.valueOf(DEFAULT_COMMAND_TIMEOUT)));

            LOGGER.info("Network configuration loaded from properties file");
        } catch (IOException e) {
            LOGGER.info("Using default network configuration");
        }
    }

    // Getters
    public String getServerHost() {
        return serverHost;
    }

    public int getServerPort() {
        return serverPort;
    }

    public int getThreadPoolSize() {
        return threadPoolSize;
    }

    public long getConnectionTimeout() {
        return connectionTimeout;
    }

    public long getCommandTimeout() {
        return commandTimeout;
    }

    // Setters
    public void setServerHost(String serverHost) {
        this.serverHost = serverHost;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    public void setThreadPoolSize(int threadPoolSize) {
        this.threadPoolSize = threadPoolSize;
    }

    public void setConnectionTimeout(long connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public void setCommandTimeout(long commandTimeout) {
        this.commandTimeout = commandTimeout;
    }
}


package com.antam.app.network.message;

import com.antam.app.network.command.CommandType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

/*
 * @description:
 * @author: Pham Dang Khoa
 * @date: 21/04/2026
 * @version: 1.0
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Command implements Serializable {
    private CommandType type;
    private Map<String, Object> payload;  // JSON-compatible
    private String sessionId;
    private long timestamp;
}


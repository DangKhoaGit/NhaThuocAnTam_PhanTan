package com.antam.app.network.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

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
public class Response implements Serializable {
    private boolean success;
    private String message;
    private Object data;
    private String errorCode;
}
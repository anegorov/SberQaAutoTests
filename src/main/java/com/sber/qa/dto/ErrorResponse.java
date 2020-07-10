package com.sber.qa.dto;

import lombok.Data;

@Data
public class ErrorResponse {

    private String error;
    private String exception;
    private String message;
    private String path;
    private Long status;
    private Long timestamp;

}

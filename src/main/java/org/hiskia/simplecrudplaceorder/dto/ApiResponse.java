package org.hiskia.simplecrudplaceorder.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    private boolean success;
    private int code;
    private String status;
    private String message;
    private T data;

    public static <T> ApiResponse<T> success(int code, String status, String message, T data) {
        return new ApiResponse<>(true, code, status, message, data);
    }

    public static <T> ApiResponse<T> success(int code, String status, String message) {
        return new ApiResponse<>(true, code, status, message, null);
    }

    public static <T> ApiResponse<T> error(int code, String status, String message) {
        return new ApiResponse<>(false, code, status, message, null);
    }
}

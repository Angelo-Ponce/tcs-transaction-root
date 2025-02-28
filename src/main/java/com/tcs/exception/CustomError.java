package com.tcs.exception;

public record CustomError(int status,
                          String error,
                          String message,
                          String path) {
}

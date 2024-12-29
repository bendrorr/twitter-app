package com.example.twitterapp.Exception;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RestAssuredError extends RuntimeException {

    /**
     * -- GETTER --
     * Get the HTTP status code of the error.
     * <p>
     * <p>
     * -- SETTER --
     * Set the HTTP status code of the error.
     *
     * @return HTTP status code.
     * @param statusCode HTTP status code.
     */
    private int statusCode; // Optional: HTTP status code of the error
    /**
     * -- GETTER --
     * Get additional error details.
     * <p>
     * <p>
     * -- SETTER --
     * Set additional error details.
     *
     * @return Error details.
     * @param errorDetails Error details.
     */
    private String errorDetails; // Optional: Additional error details (e.g., from a response)

    /**
     * Constructor with just a message.
     *
     * @param message The error message.
     */
    public RestAssuredError(String message) {
        super(message);
    }

    /**
     * Constructor with message and cause.
     *
     * @param message The error message.
     * @param cause   The root cause of the error.
     */
    public RestAssuredError(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor with message, status code, and error details.
     *
     * @param message      The error message.
     * @param statusCode   HTTP status code.
     * @param errorDetails Additional error details.
     */
    public RestAssuredError(String message, int statusCode, String errorDetails) {
        super(message);
        this.statusCode = statusCode;
        this.errorDetails = errorDetails;
    }


}

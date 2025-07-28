package com.dineconnect.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.dineconnect.backend.restaurant.exception.RestaurantAlreadyExistsException;
import com.dineconnect.backend.restaurant.exception.RestaurantNotFoundException;
import com.dineconnect.backend.user.exception.UserAlreadyExistsException;
import com.dineconnect.backend.booking.exception.BookingNotFoundException;
import com.dineconnect.backend.booking.exception.BookingAlreadyExistsException;
import com.dineconnect.backend.booking.exception.BookingAlreadyCancelledException;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RestaurantNotFoundException.class)
    public ResponseEntity<Map<String,String>> handleRestaurantNotFoundException(RestaurantNotFoundException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RestaurantAlreadyExistsException.class)
    public ResponseEntity<Map<String,String>> handleRestaurantAlreadyExistsException(RestaurantAlreadyExistsException ex){
        var error = new HashMap<String, String>();
        error.put("error", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<Map<String,String>> handleUserAlreadyExistsException(UserAlreadyExistsException ex){
        var error = new HashMap<String, String>();
        error.put("error", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

      @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Map<String,String>> handleInvalidCredentialsException(){
        var error = new HashMap<String, String>();
        error.put("error", "Invalid Username or Password");
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGeneralException(Exception ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(BookingNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleBookingNotFoundException(BookingNotFoundException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BookingAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> handleBookingAlreadyExists(BookingAlreadyExistsException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(BookingAlreadyCancelledException.class)
    public ResponseEntity<Map<String, String>> handleBookingAlreadyCancelled(BookingAlreadyCancelledException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

}

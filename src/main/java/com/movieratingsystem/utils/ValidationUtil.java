package com.movieratingsystem.utils;

import java.util.regex.Pattern;

public class ValidationUtil {
    
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    private static final int MIN_PASSWORD_LENGTH = 6;
    
    public static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email).matches();
    }
    
    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= MIN_PASSWORD_LENGTH;
    }
    
    public static boolean isValidName(String name) {
        return name != null && !name.trim().isEmpty();
    }
    
    public static boolean isValidMovieTitle(String title) {
        return title != null && !title.trim().isEmpty();
    }
    
    public static boolean isValidDuration(int minutes) {
        return minutes > 0;
    }
    
    public static boolean isValidGenreName(String genreName) {
        return genreName != null && !genreName.trim().isEmpty();
    }
    
    public static boolean isValidComment(String comment) {
        return comment != null && !comment.trim().isEmpty();
    }
    
    public static boolean isValidRating(int rating) {
        return rating >= 1 && rating <= 10;
    }
}
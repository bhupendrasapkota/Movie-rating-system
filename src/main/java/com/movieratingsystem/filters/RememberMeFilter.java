package com.movieratingsystem.filters;

import com.movieratingsystem.models.UserModel;
import com.movieratingsystem.service.UserService;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Optional;

@WebFilter(filterName = "RememberMeFilter", urlPatterns = {"/*"})
public class RememberMeFilter implements Filter {
    
    private static final String REMEMBER_ME_COOKIE = "rememberMe";
    private UserService userService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        userService = new UserService();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        
        // Skip filter for login and logout paths
        String requestPath = httpRequest.getRequestURI().substring(httpRequest.getContextPath().length());
        if (requestPath.equals("/login") || requestPath.equals("/logout")) {
            chain.doFilter(request, response);
            return;
        }

        // Check if user is already logged in
        HttpSession session = httpRequest.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            chain.doFilter(request, response);
            return;
        }

        // Check for remember me cookie
        Cookie[] cookies = httpRequest.getCookies();
        if (cookies != null) {
            Optional<Cookie> rememberMeCookie = Arrays.stream(cookies)
                    .filter(cookie -> REMEMBER_ME_COOKIE.equals(cookie.getName()))
                    .findFirst();

            if (rememberMeCookie.isPresent()) {
                try {
                    // Decode the cookie value (email:password)
                    String decodedValue = new String(Base64.getDecoder().decode(rememberMeCookie.get().getValue()));
                    String[] credentials = decodedValue.split(":");
                    
                    if (credentials.length == 2) {
                        String email = credentials[0];
                        String password = credentials[1];
                        
                        // Authenticate user
                        UserModel user = userService.authenticateUser(email, password);
                        if (user != null) {
                            // Create new session and store user
                            session = httpRequest.getSession();
                            session.setAttribute("user", user);
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Error decoding remember me cookie: " + e.getMessage());
                }
            }
        }

        // Continue the filter chain
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
} 
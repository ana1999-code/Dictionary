package com.example.dictionary.application.security.utils;

import com.example.dictionary.application.security.auth.ApplicationUser;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class SecurityUtils {

    private SecurityUtils() {
    }

    public static String getUsername() {
        SecurityContext context = SecurityContextHolder.getContext();
        if (context != null && context.getAuthentication() != null) {
            Object principal = context.getAuthentication().getPrincipal();
            if (principal instanceof UserDetails) {
                ApplicationUser user = (ApplicationUser) context.getAuthentication().getPrincipal();
                return user.getUsername();
            }
        }
        return null;
    }

    public static boolean isUserLoggedIn() {
        return isLoggedIn(SecurityContextHolder.getContext().getAuthentication());
    }

    private static boolean isLoggedIn(Authentication authentication) {
        return authentication != null
                && !(authentication instanceof AnonymousAuthenticationToken);
    }

}

package com.example.dictionary.ui.security;

import com.example.dictionary.application.security.auth.ApplicationUserDetailService;
import com.example.dictionary.application.security.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Collection;

import static com.example.dictionary.application.security.role.Permission.WORD_WRITE;
import static com.example.dictionary.application.security.role.Role.ADMIN;

@Component
@Scope(value = "prototype")
public class CurrentUserPermissionService {

    private boolean hasWordWritePermission;

    private boolean isAdmin;

    private ApplicationUserDetailService userDetailService;

    @Autowired
    public void setUserDetailService(ApplicationUserDetailService userDetailService) {
        this.userDetailService = userDetailService;
    }

    public boolean hasWordWritePermission() {
        getGrantedAuthorities().stream()
                .filter(auth -> auth.getAuthority().equalsIgnoreCase(WORD_WRITE.getPermission()))
                .findAny()
                .ifPresent(auth -> hasWordWritePermission = true);

        return hasWordWritePermission;
    }

    public boolean isAdmin() {
        getGrantedAuthorities().stream()
                .filter(auth -> auth.getAuthority().equalsIgnoreCase("ROLE_" + ADMIN.name()))
                .findAny()
                .ifPresent(auth -> isAdmin = true);

        return isAdmin;
    }

    private Collection<? extends GrantedAuthority> getGrantedAuthorities() {
        String currentUserEmail = SecurityUtils.getUsername();
        UserDetails userDetails = userDetailService.loadUserByUsername(currentUserEmail);
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        return authorities;
    }
}

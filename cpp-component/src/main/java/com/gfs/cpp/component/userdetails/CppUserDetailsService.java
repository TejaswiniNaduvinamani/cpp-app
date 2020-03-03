package com.gfs.cpp.component.userdetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.gfs.util.spring.security.GfsUserDetails;

@Service
public class CppUserDetailsService {

    public String getCurrentUserId() {
        return (getAuthentication()).getUsername();
    }

    private GfsUserDetails getAuthentication() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("User Not Authenticated");
        }
        return (GfsUserDetails) authentication.getPrincipal();
    }

    public boolean isAccountManagerUser() {
        return hasRole(RoleConstants.ACCOUNT_MANAGER);
    }

    public boolean isPowerUser() {
        return hasRole(RoleConstants.POWER_USER);
    }

    public boolean hasContractEditAccess() {
        return isAccountManagerUser() || isPowerUser();
    }

    public boolean hasRole(String roleName) {
        return getAllRoles().contains("ROLE_" + roleName);
    }

    private Set<String> getAllRoles() {
        Set<String> roles = new HashSet<>();
        GfsUserDetails authentication = getAuthentication();
        Collection<GrantedAuthority> authorities = authentication.getAuthorities();
        for (GrantedAuthority grantedAuthority : authorities) {
            roles.add(grantedAuthority.getAuthority());
        }

        return roles;
    }

}

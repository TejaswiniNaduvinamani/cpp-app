package com.gfs.cpp.acceptanceTests.mocks;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.gfs.cpp.acceptanceTests.config.CukesConstants;
import com.gfs.cpp.component.userdetails.RoleConstants;
import com.gfs.util.spring.security.GfsUserDetails;

@Component
public class CppUserDetailsServiceMocker implements Resettable {

    private static final String ROLE_PREFIX = "ROLE_";
    @Autowired
    private GrantedAuthority grantedAuthority;

    public void setRoleName(String roleName) {
        doReturn(ROLE_PREFIX + roleName).when(grantedAuthority).getAuthority();
    }

    @Override
    public void reset() {

        final Authentication authentication = mock(Authentication.class);
        final SecurityContext securityContext = mock(SecurityContext.class);
        final GfsUserDetails gfsUserDetails = mock(GfsUserDetails.class);

        SecurityContextHolder.setContext(securityContext);

        doReturn(true).when(authentication).isAuthenticated();
        doReturn(authentication).when(securityContext).getAuthentication();
        doReturn(gfsUserDetails).when(authentication).getPrincipal();
        doReturn(Collections.singleton(grantedAuthority)).when(gfsUserDetails).getAuthorities();

        doReturn(CukesConstants.CURRENT_USER_ID).when(gfsUserDetails).getUsername();

        doReturn(ROLE_PREFIX + RoleConstants.POWER_USER).when(grantedAuthority).getAuthority();
    }

}

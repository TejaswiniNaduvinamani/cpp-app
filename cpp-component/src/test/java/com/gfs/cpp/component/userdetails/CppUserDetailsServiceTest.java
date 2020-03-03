package com.gfs.cpp.component.userdetails;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;
import static org.mockito.Mockito.doReturn;

import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import com.gfs.cpp.component.userdetails.CppUserDetailsService;
import com.gfs.util.spring.security.GfsUserDetails;

@RunWith(MockitoJUnitRunner.class)
public class CppUserDetailsServiceTest {

    @InjectMocks
    private CppUserDetailsService target;
    @Mock
    private Authentication authentication;
    @Mock
    private GfsUserDetails userDetails;
    @Mock
    private GrantedAuthority grantedAuthority;

    @Before
    public void setup() {
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    public void shouldReturnCurrentUser() throws Exception {

        String currentUser = "current user";

        doReturn(userDetails).when(authentication).getPrincipal();
        doReturn(currentUser).when(userDetails).getUsername();
        doReturn(true).when(authentication).isAuthenticated();

        assertThat(target.getCurrentUserId(), equalTo(currentUser));
    }

    @Test(expected = AccessDeniedException.class)
    public void shouldThrowAccessDeniedExceptionWhenNotAuthenticated() throws Exception {

        doReturn(false).when(authentication).isAuthenticated();

        target.getCurrentUserId();
    }

    @Test(expected = AccessDeniedException.class)
    public void shouldThrowAccessDeniedExceptionWhenNoAuthenticationFound() throws Exception {

        SecurityContextHolder.getContext().setAuthentication(null);
        doReturn(true).when(authentication).isAuthenticated();

        target.getCurrentUserId();
    }

    @Test
    public void shouldReturnTrueIfRolesListHasAccountManager() throws Exception {

        setAccountManagerRole();

        assertThat(target.isAccountManagerUser(), equalTo(true));

    }

    @Test
    public void shouldReturnFalseIfRolesListHasNoAccountManager() throws Exception {

        doReturn(userDetails).when(authentication).getPrincipal();
        doReturn(true).when(authentication).isAuthenticated();
        doReturn(Collections.emptyList()).when(userDetails).getAuthorities();

        assertThat(target.isAccountManagerUser(), equalTo(false));

    }

    @Test
    public void shouldReturnTrueIfRolesListHaPowerUser() throws Exception {

        setPowerUserRole();

        assertThat(target.isPowerUser(), equalTo(true));

    }

    @Test
    public void shouldReturnTrueIfRolesListNoPowerUser() throws Exception {

        setAccountManagerRole();

        assertThat(target.isPowerUser(), equalTo(false));

    }

    @Test
    public void shouldHasCreateContractAccessReturnTrueWhenPowerUser() throws Exception {

        setPowerUserRole();

        assertThat(target.hasContractEditAccess(), equalTo(true));
    }

    @Test
    public void shouldHasCreateContractAccessReturnTrueWhenAccountManagerUser() throws Exception {

        setAccountManagerRole();

        assertThat(target.hasContractEditAccess(), equalTo(true));
    }

    @Test
    public void shouldHasCreateContractAccessReturnTrueWhenViewUser() throws Exception {

        doReturn(userDetails).when(authentication).getPrincipal();
        doReturn(true).when(authentication).isAuthenticated();
        doReturn("ROLE_VIEWUSER").when(grantedAuthority).getAuthority();
        doReturn(Collections.singletonList(grantedAuthority)).when(userDetails).getAuthorities();

        assertThat(target.hasContractEditAccess(), equalTo(false));
    }

    private void setPowerUserRole() {
        doReturn(userDetails).when(authentication).getPrincipal();
        doReturn(true).when(authentication).isAuthenticated();
        doReturn("ROLE_POWERUSER").when(grantedAuthority).getAuthority();
        doReturn(Collections.singletonList(grantedAuthority)).when(userDetails).getAuthorities();
    }

    private void setAccountManagerRole() {
        doReturn(userDetails).when(authentication).getPrincipal();
        doReturn(true).when(authentication).isAuthenticated();
        doReturn("ROLE_ACCOUNTMGR").when(grantedAuthority).getAuthority();
        doReturn(Collections.singletonList(grantedAuthority)).when(userDetails).getAuthorities();
    }

}

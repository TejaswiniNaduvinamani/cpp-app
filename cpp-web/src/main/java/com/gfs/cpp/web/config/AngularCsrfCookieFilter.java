package com.gfs.cpp.web.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.filter.GenericFilterBean;

public class AngularCsrfCookieFilter extends GenericFilterBean {
    
    private static final Logger LOG = LoggerFactory.getLogger(AngularCsrfCookieFilter.class);
    
    public static final String DEFAULT_COOKIE_NAME = "XSRF-TOKEN"; //$NON-NLS-1$
    
    private boolean cookieSecure = false;
    private String cookieName = DEFAULT_COOKIE_NAME;
    private String cookiePath;
    
    @Override
    protected void initFilterBean() throws ServletException {
        if (cookiePath == null) {
            cookiePath = getServletContext().getContextPath();
        }
    }
    
    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response,
            final FilterChain chain) throws IOException, ServletException {
        
        final HttpServletRequest httpRequest = (HttpServletRequest) request;
        final HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        addCsrfCookie(httpRequest, httpResponse);
        chain.doFilter(request, httpResponse);
    }
    
    private void addCsrfCookie(final HttpServletRequest request, final HttpServletResponse response) {
        final CsrfToken token = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        
        if (token == null) {
            LOG.debug("No CSRF token available"); //$NON-NLS-1$
        } else {
            final Cookie cookie = new Cookie(cookieName, token.getToken());
            cookie.setPath(cookiePath);
            cookie.setSecure(cookieSecure);
            response.addCookie(cookie);
        }
    }
    
    public void setCsrfCookieSecure(final boolean csrfCookieSecure) {
        this.cookieSecure = csrfCookieSecure;
    }
    
    public void setCsrfCookieName(final String csrfCookieName) {
        this.cookieName = csrfCookieName;
    }
    
    public void setCsrfCookiePath(final String csrfCookiePath) {
        this.cookiePath = csrfCookiePath;
    }
}

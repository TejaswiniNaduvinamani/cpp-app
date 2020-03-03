/**
 * <p class="copyright">Copyright 2014 by Gordon Food Service, Inc.</p>
 *
 * <div class="vcard">
 *   <div class="fn org">Gordon Food Service, Inc.</div>
 *   <div class="adr">
 *     <div class="post-office-box">P.O. Box 1787</div>
 *     <div>
 *       <span class="locality">Grand Rapids</span>,
 *       <abbr class="region" title="Michigan">MI</abbr> <span class="postal-code">49501</span>
 *     </div>
 *     <div class="country-name"><abbr title="United States of America">USA</abbr></div>
 *   </div>
 * </div>
 *
 * <p class="copyrightRights">All rights reserved.</p>
 *
 * <p class="legal">
 * This software is the confidential and proprietary information of Gordon
 * Food Service, Inc. ("Confidential Information"). You shall not disclose
 * such Confidential Information and shall use it only in accordance with
 * the terms specified by Gordon Food Service.
 * </p>
 */
package com.gfs.cpp.web.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;

import com.gfs.cpp.component.userdetails.RoleConstants;
import com.gfs.util.spring.security.GfsUserDetailsContextMapper;

/**
 * LDAP Security configuration for CPP Web
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Profile({ "weblogic", "jetty" })
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String USER_DISTINGUISHED_NAME = "uid=CppAuthUser,ou=Special Users,o=GFS";
    private static final String GROUP_SEARCH_BASE = "ou=CustomerPriceProfile,ou=Applications,ou=Corporate,o=gfs";
    private static final String DEFAULT_COOKIE_NAME = "X-XSRF-TOKEN";

    private static final Logger LOG = LoggerFactory.getLogger(SecurityConfig.class);

    @Configuration
    protected static class AuthenticationConfiguration extends GlobalAuthenticationConfigurerAdapter {

        @Autowired
        private Environment environment;

        @Override
        public void init(final AuthenticationManagerBuilder auth) throws Exception {
            LOG.info("Authenticating Customer Price Profile");
            // @formatter:off
            auth.ldapAuthentication()
                .userDnPatterns("uid={0},ou=People,ou=Corporate,o=gfs")
                .groupSearchBase(GROUP_SEARCH_BASE)
                .userDetailsContextMapper(new GfsUserDetailsContextMapper())
                
                .contextSource()
                .url(environment.getProperty("ldap.url"))
                .managerDn(USER_DISTINGUISHED_NAME)
                .managerPassword(environment.getProperty("ldap.password"));
            // @formatter:on
        }
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {

        final AngularCsrfCookieFilter angularFilter = new AngularCsrfCookieFilter();
        angularFilter.setCsrfCookiePath("/cpp");
        angularFilter.setCsrfCookieSecure(true);

        final HttpSessionCsrfTokenRepository tokenRepository = new HttpSessionCsrfTokenRepository();
        tokenRepository.setHeaderName(DEFAULT_COOKIE_NAME);

        // @formatter:off
        http
        .httpBasic().realmName("GordonLine")
        .and()
        .authorizeRequests().anyRequest().authenticated()
        .and()
        .authorizeRequests().anyRequest()
        .hasAnyRole(RoleConstants.ACCOUNT_MANAGER,
                RoleConstants.POWER_USER,RoleConstants.VIEW_USER)
        .and()
        .csrf().disable();
        // @formatter:on
    }
}
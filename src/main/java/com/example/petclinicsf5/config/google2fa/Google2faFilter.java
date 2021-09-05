package com.example.petclinicsf5.config.google2fa;

import com.example.petclinicsf5.model.security.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.boot.autoconfigure.security.servlet.StaticResourceRequest;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class Google2faFilter extends GenericFilterBean {

    private final AuthenticationTrustResolver authenticationTrustResolver = new AuthenticationTrustResolverImpl();
    private final Google2faFailureHandler google2faFailureHandler = new Google2faFailureHandler();
    private final RequestMatcher verify2faUrl = new AntPathRequestMatcher("user/verify2fa");
    private final RequestMatcher resourceUrl = new AntPathRequestMatcher("/resources/**");



    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        // Add 2fa verification page and static page resources to filter:
        StaticResourceRequest.StaticResourceRequestMatcher staticResources = PathRequest.toStaticResources().atCommonLocations();
        if (verify2faUrl.matches(request) || resourceUrl.matches(request) || staticResources.matches(request)) {
            filterChain.doFilter(request,response);
            return;
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && !authenticationTrustResolver.isAnonymous(authentication)) {
            log.debug("Authenticated... processing 2FA filter...");

            if (authentication.getPrincipal() != null && authentication.getPrincipal() instanceof User) {
                User user = (User) authentication.getPrincipal();

                if (user.getIsUser2fa() && user.getIsUser2faRequired()) {
                    log.debug("2FA verification required for this user...");
                    google2faFailureHandler.onAuthenticationFailure(request,response,null);
                    return;
                }
            }
        }

        filterChain.doFilter(request,response);
    }
}

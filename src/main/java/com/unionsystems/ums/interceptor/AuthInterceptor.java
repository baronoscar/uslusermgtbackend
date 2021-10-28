package com.unionsystems.ums.interceptor;

import com.unionsystems.ums.model.User;
import com.unionsystems.ums.service.UserService;
import com.unionsystems.ums.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.List;

@Slf4j
public class AuthInterceptor implements HandlerInterceptor {
    public static final String JWT_TOKEN_HEADER_PARAM = "Authorization";
    public static final String CURRENT_USER = "CURRENT_USER";

    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        boolean matches = skipPaths().stream().anyMatch(e -> new AntPathMatcher().match(e, request.getRequestURI()));
        if (!matches) {
            String token = request.getHeader(JWT_TOKEN_HEADER_PARAM);
            if (token == null) {
                throw new AccessDeniedException("Unauthorized access");
            }
            boolean tokenValid = jwtUtils.validateJwtToken(token);
            if (!tokenValid) {
                throw new AccessDeniedException("Invalid token");
            }
            handleToken(request, token);
        }
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    private List<String> skipPaths() {
        return Collections.singletonList("/api/auth/**");
    }

    private void handleToken(HttpServletRequest request, String token) {
        String email = jwtUtils.getEmailFromJwtToken(token);
        User user = userService.findByEmail(email).orElseThrow(() -> new AccessDeniedException("Unauthorized access"));
        request.setAttribute(CURRENT_USER, user);
    }
}

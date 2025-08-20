package com.tss.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.Base64;

@WebFilter(urlPatterns = {"/login", "/register/*", "/forgot-password", "/user/transaction", "/user/transfer"})
public class CsrfFilter implements Filter {

    private static final String CSRF_TOKEN_ATTR = "csrfToken";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        HttpSession session = req.getSession(true);

        // Ensure token exists for GET/HEAD
        if (!"POST".equalsIgnoreCase(req.getMethod())) {
            ensureToken(session);
            chain.doFilter(request, response);
            return;
        }

        // Validate token for POST
        String sessionToken = (String) session.getAttribute(CSRF_TOKEN_ATTR);
        String formToken = req.getParameter("csrfToken");
        if (sessionToken == null || formToken == null || !constantTimeEquals(sessionToken, formToken)) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid CSRF token");
            return;
        }
        chain.doFilter(request, response);
    }

    private void ensureToken(HttpSession session) {
        if (session.getAttribute(CSRF_TOKEN_ATTR) == null) {
            byte[] bytes = new byte[32];
            new SecureRandom().nextBytes(bytes);
            String token = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
            session.setAttribute(CSRF_TOKEN_ATTR, token);
        }
    }

    private boolean constantTimeEquals(String a, String b) {
        if (a == null || b == null) return false;
        if (a.length() != b.length()) return false;
        int result = 0;
        for (int i = 0; i < a.length(); i++) {
            result |= a.charAt(i) ^ b.charAt(i);
        }
        return result == 0;
    }
}


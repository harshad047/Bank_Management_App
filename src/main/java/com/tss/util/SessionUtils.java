// src/main/java/com/tss/util/SessionUtils.java

package com.tss.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class SessionUtils {

    // Get current user role from session
    public static String getUserRole(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            return (String) session.getAttribute("role");
        }
        return null;
    }

    // Check if user is logged in
    public static boolean isLoggedIn(HttpServletRequest request) {
        return getUserRole(request) != null;
    }

    // Check if user is admin
    public static boolean isAdmin(HttpServletRequest request) {
        String role = getUserRole(request);
        return "admin".equals(role);
    }

    // Check if user is customer
    public static boolean isUser(HttpServletRequest request) {
        String role = getUserRole(request);
        return "user".equals(role);
    }

    // Get current user ID from session
    public static int getUserId(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("userId") != null) {
            return (int) session.getAttribute("userId");
        }
        return -1;
    }

    // Get current admin ID from session
    public static int getAdminId(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("adminId") != null) {
            return (int) session.getAttribute("adminId");
        }
        return -1;
    }

    // Invalidate session
    public static void logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }
}
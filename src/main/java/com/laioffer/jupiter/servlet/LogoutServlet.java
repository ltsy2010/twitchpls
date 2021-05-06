package com.laioffer.jupiter.servlet;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "LogoutServlet", urlPatterns = {"/logout"})
public class LogoutServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Destroy the session since the user is logged out.
        HttpSession session = request.getSession(false);//false -> not create new, return.
        if (session != null) {
            session.invalidate();
        }
        Cookie cookie = new Cookie("JSESSIONID", null);
        cookie.setPath("/");//clear localhost:8080/jupiter/
        cookie.setMaxAge(0);
        response.addCookie(cookie);//respond null cookie to frontend
    }
}

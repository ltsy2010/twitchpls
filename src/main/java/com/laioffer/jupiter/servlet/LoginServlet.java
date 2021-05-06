package com.laioffer.jupiter.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.laioffer.jupiter.db.MySQLConnection;
import com.laioffer.jupiter.db.MySQLException;
import com.laioffer.jupiter.entity.LoginRequestBody;
import com.laioffer.jupiter.entity.LoginResponseBody;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Read user data from the request body
        LoginRequestBody body = ServletUtil.readRequestBody(LoginRequestBody.class, request);

        if (body == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        String username;
        MySQLConnection connection = null;
        try {
            // Verify if the user ID and password are correct
            connection = new MySQLConnection();
            String userId = body.getUserId();
            String password = ServletUtil.encryptPassword(body.getUserId(), body.getPassword());
            username = connection.verifyLogin(userId, password);
        } catch (MySQLException e) {
            throw new ServletException(e);
        } finally {
            connection.close();
        }

        // Create a new session for the user if user ID and password are correct, otherwise return Unauthorized error.
        if (!username.isEmpty()) {
            // Create a new session, put user ID as an attribute into the session object, and set the expiration time to 6000 seconds.
            HttpSession session = request.getSession();//create session, tomcat will return uuid from request.getSession
            session.setAttribute("user_id", body.getUserId()); //key - value
            session.setMaxInactiveInterval(6000); //second
            //save seesion to DB, DB will have login info, if you have same API in different containers.

            LoginResponseBody loginResponseBody = new LoginResponseBody(body.getUserId(), username);
            response.setContentType("application/json;charset=UTF-8");
            ObjectMapper mapper = new ObjectMapper();
            response.getWriter().print(new ObjectMapper().writeValueAsString(loginResponseBody));
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);//401
        }

    }
}

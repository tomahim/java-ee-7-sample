package com.tomahim.geodaota.security;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = {"/LoginServlet"})
public class LoginServlet extends HttpServlet {
	
    @Inject
    private AccountService accountService;
 
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
 
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
 
    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String username = "tom";//request.getParameter("username");
            String password = "azerty";//request.getParameter("password");
 
            if (username != null && password != null) {
                request.login(username, password);
            }
 
            CustomPrincipal principal = (CustomPrincipal) request.getUserPrincipal();
            response.getWriter().println("principal=" + request.getUserPrincipal().getClass().getSimpleName());
            //response.getWriter().println("username=" + accountService.getPrincipalName());
 
        } catch (ServletException e) {
        	e.printStackTrace();
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
        }
    }
}
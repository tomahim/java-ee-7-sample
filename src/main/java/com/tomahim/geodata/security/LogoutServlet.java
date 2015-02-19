package com.tomahim.geodata.security;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = {"/logout"})
public class LogoutServlet extends HttpServlet {
	
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
    	response.setHeader("Cache-Control", "no-cache, no-store");
        response.setHeader("Pragma", "no-cache"); 
        response.setHeader("Expires", new java.util.Date().toString());
        //
        if(request.getSession() != null) {
        	request.getSession().invalidate();//remove session.
        }
        if(request.getSession(false) != null) {
        	request.getSession(false).invalidate();//remove session.
        }
        if(request.getSession(true) != null) {
        	request.getSession(true).invalidate();//remove session.
        }
        	
        try {
			request.logout();
			response.sendRedirect("/geodata/");
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//JAAS log out! do NOT work? (servlet specification)
    }
}
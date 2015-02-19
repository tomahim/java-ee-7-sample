package com.tomahim.geodata.security;

import java.io.BufferedReader;
import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

@WebServlet(urlPatterns = {"/LoginServlet"})
public class LoginServlet extends HttpServlet {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
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
    
    public class PostParam {
    	private String username;
    	private String password;
		public String getUsername() {
			return username;
		}
		public void setUsername(String username) {
			this.username = username;
		}
		public String getPassword() {
			return password;
		}
		public void setPassword(String password) {
			this.password = password;
		}
    	
    }
 
    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
    	BufferedReader reader = request.getReader();
    	Gson gson = new Gson();

    	PostParam params = gson.fromJson(reader, PostParam.class);
        try {
            String username = params.getUsername();//quickstartUser
            String password = params.getPassword(); //quickstartPwd1!
            
 
            if (username != null && password != null) {
                request.login(username, password);
            }
 
            Principal principal = request.getUserPrincipal();
            response.getWriter().println("principal=" + request.getUserPrincipal().getClass().getSimpleName());
 
        } catch (ServletException e) {
        	e.printStackTrace();
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
        }
    }
}
package iFile;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import models.UserBean;

@WebServlet("/Login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			request.getRequestDispatcher("LoginGROUP.jsp").forward(request, response);
			}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//Get the parameters from the login form
		String name = request.getParameter("name");
		String password = request.getParameter("password");
		
		//Create a session scope and set the current user name for the session
		HttpSession session = request.getSession();
		session.setAttribute("user", name);
		
		//Error handling with invalid login
		boolean hasError = false;
		//If the user didn't type the name
		if (name == null || name.trim().length() == 0) {
			hasError = true;
			request.setAttribute("nameError", "You must enter your name");
		}		
		//If the user didn't type the password
		if (password == null || password.trim().length() == 0) {
			hasError = true;
			request.setAttribute("passwordError", "You must enter a password");
		}
		
		if (hasError) {
			if(name == null && password == null) {
				request.setAttribute("loginError", "Sorry, you must be logged in");
				response.sendRedirect("LoginGROUP.jsp");
				return;
			}
			else {
				doGet(request, response);
			}
			
		}
		
		else {
			Connection c = null;

	        try{
	            String url = "jdbc:mysql://cs3.calstatela.edu/cs3220stu91";																																												
	            c = DriverManager.getConnection(url, "username", "password"  );
	            Statement stmt = c.createStatement();
	            ResultSet rs = stmt.executeQuery( "SELECT * from users WHERE name='" + name + "' AND password ='" + password + "'");
	          
	            String dbname = null;
	    		String dbpassword = null;
	            
	    		if(rs.next()) {
	            	dbname = rs.getString("name");
	            	dbpassword = rs.getString("password");
	            	
		            if (name.equals(dbname) && password.equals(dbpassword)){
		    			session.setAttribute("userid", rs.getInt("id") );
		    			response.sendRedirect("UploadViewGROUP.jsp");
		            	}
		            }
	    		else {
	    			//User doesn't exists
	    			request.setAttribute("userError", "User doesnt exist!");
	    			request.getRequestDispatcher("LoginGROUP.jsp").forward(request, response);
	            	}
	    		rs.close(); 
	    		}
	        catch( SQLException e ){
	            throw new ServletException( e );
	            }
	        
	        
	        finally{
	            try{
	                if( c != null ) c.close();
	                }
	            catch( SQLException e ){
	                throw new ServletException( e );
	                }	        
	            }
	        }
		}
	}
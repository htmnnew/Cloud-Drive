package iFile;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletConfig;
import models.UserBean;

@WebServlet("/Registration")
public class Registration extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public void init( ServletConfig config ) throws ServletException
    {
        super.init( config );
        try{
            Class.forName( "com.mysql.jdbc.Driver" );
        }
        catch( ClassNotFoundException e ){
            throw new ServletException( e );
        }
    }
    
    protected void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException{
            request.getRequestDispatcher( "RegisterGROUP.jsp" ).forward(request, response );
        }

    protected void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException{
    	List <UserBean> users = new ArrayList<UserBean>();
    	
    	//Get the parameters from the request
	    String name = request.getParameter( "name" );
	    String password = request.getParameter( "password" );
	    String passwordC = request.getParameter( "passwordC" );
	    
	    //Set the current user name 
	    request.setAttribute("username", name);
	    
	    boolean hasError = false;

	    //Error handling in case of incorrect input
		if (name == null || name.trim().length() == 0) {
			hasError = true;
			request.setAttribute("nameError", "You must enter your name");
			}
		if (password == null || password.trim().length() == 0) {
			hasError = true;
			request.setAttribute("passwordError", "You must enter password");
			}
		if (passwordC == null || passwordC.trim().length() == 0) {
				hasError = true;
				request.setAttribute("passwordCError", "You must confirm the password");
			}
		
		if (!password.matches(passwordC)) {
			hasError = true;
			request.setAttribute("confirmationError", "Passwords you've typed doesn't match, try again!");
			}
		
		if (hasError) {
			doGet(request, response);
			return;
		}
		
		// Insert the record into the database
		Connection c = null;		
        try {
        	String url = "jdbc:mysql://cs3.calstatela.edu/cs3220stu91";																																												
            c = DriverManager.getConnection( url, "cs3220stu91", "Xy7DRTV*"  );
            Statement stmt = c.createStatement();
            
            ResultSet rs = stmt.executeQuery( "SELECT name from users WHERE name='" + name + "'");
           
            //Error handling about existing user name
            if(rs.first()) {
	    			request.setAttribute("userExists", "User with name:" + name + " already exists. Please type another name!");
	    			request.setAttribute("username", null);
	    			request.getRequestDispatcher( "RegisterGROUP.jsp" ).forward(request, response );
	    			return;
	            	}
            
            //If the user name doesn't exists, insert to DB
            else {
		            String sql = "insert into users (name, password) values (?, ?)";
		            PreparedStatement pstmt = c.prepareStatement( sql );
		            pstmt.setString( 1, name );
		            pstmt.setString( 2, password );
		            
		            int numberOfRowsAffected = pstmt.executeUpdate();
		            if (numberOfRowsAffected >0 ) {
		                request.setAttribute("register", "Registration was successfull!" );
		              //Redirect user to login page
			            request.getRequestDispatcher( "LoginGROUP.jsp" ).forward(request, response );
		            }
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

package iFile;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.mysql.cj.Session;

@WebServlet("/SearchFiles")
public class SearchFiles extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public SearchFiles() {
		super();

	}
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//Get parameters from the current session and from the current request
		HttpSession session = request.getSession();
		String filename = request.getParameter( "search" );
		int userid = (int) session.getAttribute("userid");

		//Connection to DB
		Connection c = null;		
		
		try {
			String url = "jdbc:mysql://cs3.calstatela.edu/username";																																												
			c = DriverManager.getConnection(url, "username", "password"  );
			Statement stmt = c.createStatement();

			//Search for the file
			ResultSet rs = stmt.executeQuery( "SELECT filename, user_id, upload_date from files WHERE filename LIKE '%" + filename + "%' AND user_id='"+ userid +"'");
			
			//If statement for searching
			if(rs.first()) {
				request.setAttribute("searchSuccess", "success");
				request.setAttribute("filename", filename);
				request.setAttribute("searchMessage", "Great! We could find what you have been searched for. ");
			}
			else {
				request.setAttribute("searchSuccess", "not success");
				request.setAttribute("filename", filename);
				request.setAttribute("searchMessage", "We're sorry, we couldn't find anything with: " + filename + " :(");
			}
			request.getRequestDispatcher( "UploadViewGROUP.jsp" ).forward(request, response );
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

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	}
}
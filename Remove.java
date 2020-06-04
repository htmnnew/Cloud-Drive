package iFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/Remove")
public class Remove extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//Get parameters from the current session and from the current request
		HttpSession session = request.getSession();
		String name = (String) session.getAttribute("user");
		String fileName = request.getParameter("fileName");
		int fileId = Integer.parseInt(request.getParameter("id"));
		int id = (Integer) session.getAttribute("userid");

		//Connection to DB
		Connection c = null;
		String url = "jdbc:mysql://cs3.calstatela.edu/username";																																												

		//Get the current file which has to be removed
		try {
			c = DriverManager.getConnection( url, "username", "password" );
			Statement stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery( "SELECT * from files WHERE id='" + fileId + "' AND filename='" + fileName + "' AND user_id='" + id + "'");

			String filePath= null;

			if (rs.first()) {
				// Reads real path from an absolute path
				filePath = rs.getString("path");

				//Delete the file
				File deleteFile = new File(filePath);
				deleteFile.delete();

				//If the file has been deleted, delete it as well from DB
				int numberOfRowsAffected = stmt.executeUpdate( "DELETE from files WHERE id='"+ fileId + "'");

				if (numberOfRowsAffected >0 ) {
					request.setAttribute("fileRemoveSuccess", "File successfuly deleted!");
					request.getRequestDispatcher( "UploadViewGROUP.jsp" ).forward(request, response );
				}
			}
			else {
				//File doesn't exists
				request.setAttribute("fileRemoveError", "File doesn't exists!");
				request.getRequestDispatcher("UploadViewGROUP.jsp").forward(request, response);
			}
			rs.close(); 

		}// End of try block 
		catch (Exception e) {
			throw new IOException( e ); 	
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}

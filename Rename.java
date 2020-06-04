package iFile;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/Rename")
public class Rename extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//Get parameters from the current session and from the current request
		HttpSession session = request.getSession();
		String name = (String) session.getAttribute("user");
		int id = (Integer) session.getAttribute("userid");
		
		//Get old and new names for renaming
		String oldfileName = request.getParameter("oldname");
		String newfileName = request.getParameter("newname");

		//Connection to DB
		Connection c = null;
		String url = "jdbc:mysql://cs3.calstatela.edu/username";																																												

		//Get the current name file from the database
		try {
			c = DriverManager.getConnection( url, "username", "password" );
			Statement stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery( "SELECT path from files WHERE user_id='" + id + "' AND filename='" + oldfileName + "'");

			String filePath= null;

			if (rs.next()) {
				// Reads real path from an absolute path
				filePath = rs.getString("path"); 
			}
			else {
				//File doesn't exists
				request.setAttribute("fileRenameError", "File doesn't exists!");
				request.getRequestDispatcher("UploadViewGROUP.jsp").forward(request, response);
			}
			rs.close(); 
			
			//Create new Files for renaming
			File oldFile = new File(filePath);
			String newfilePath = getServletContext().getRealPath( "/WEB-INF/uploads/" )+ newfileName;
			File newFile = new File(newfilePath);
			System.out.print(newfilePath);

			//If path of the file would found in database, it should be renamed
			if(filePath!=null){
				oldFile.renameTo(newFile);
				String fileDir = getServletContext().getRealPath( "/WEB-INF/uploads" );
				String path = fileDir;
				String pathDB = null;
				//String tempfilename;

				//tempfilename = RenamedFile.getName();
				pathDB = path.replace("\\", "\\\\");
				pathDB = pathDB + "/" + newfileName;

				//if file is renamed then also rename the file in the table
				stmt.executeUpdate( "UPDATE files SET filename='" + newfileName + "', path='" + pathDB + "' WHERE user_id='" + id + "' AND filename='" + oldfileName + "'");
				request.setAttribute("fileRenameSuccess", "File successfuly renamed to " + newfileName + "!");
				request.setAttribute("fileName", newfileName);
				request.getRequestDispatcher( "UploadViewGROUP.jsp" ).forward(request, response );
			}  
			else {
				request.getRequestDispatcher( "UploadViewGROUP.jsp" ).forward(request, response );
			}

		}// End of try block 
		catch (Exception e) {
			//Set the error message if file couldn't be removed
			throw new IOException( e ); 	
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);	
	}
}

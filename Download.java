package iFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.jasper.tagplugins.jstl.core.Out;

@WebServlet("/Download")
public class Download extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//Get parameters from the current session and from the current request
		HttpSession session = request.getSession();
		String name = (String) session.getAttribute("user");
		String fileName = request.getParameter("fileName");

						//If no user has logged in send back to the login page
						if (name == null) {
							response.sendRedirect("LoginGROUP.jsp");
							return;
						}
						if (fileName == null) {
							request.getRequestDispatcher("UploadViewGROUP.jsp").forward(request, response);		
		}

		int id = (Integer) session.getAttribute("userid");
		Connection c = null;
		String url = "jdbc URL";																																												

		//Get the current name file with the current user from the database and download it
		try {
			c = DriverManager.getConnection( url, "username", "password"  );
			Statement stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery( "SELECT path from files WHERE user_id='" + id + "' AND filename='" + fileName + "'");

			String filePath= null;

			if (rs.next()) {
				// reads real path from an absolute path
				filePath = rs.getString("path"); 
			}
			else {
				//File doesn't exists doesn't exists
				request.setAttribute("fileDownloadError", "File doesn't exists!");
				request.getRequestDispatcher("UploadViewGROUP.jsp").forward(request, response);
			}
			rs.close(); 
			File downloadFile = new File(filePath);
			FileInputStream inStream = new FileInputStream(downloadFile);

			// obtains ServletContext
			ServletContext context = getServletContext();

			// gets MIME type of the file
			String mimeType = context.getMimeType(filePath);
			if (mimeType == null) {         
				// set to binary type if MIME mapping not found
				mimeType = "application/octet-stream";
			}

			// modifies response
			response.setContentType(mimeType);
			response.setContentLength((int) downloadFile.length());

			// Add header data for downloading
			String headerKey = "Content-Disposition";
			String headerValue = String.format("attachment; filename=\"%s\"", downloadFile.getName());
			response.setHeader(headerKey, headerValue);

			// obtains response's output stream
			OutputStream outStream = response.getOutputStream();

			byte[] buffer = new byte[4096];
			int bytesRead = -1;

			while ((bytesRead = inStream.read(buffer)) != -1) {
				outStream.write(buffer, 0, bytesRead);
			}

			inStream.close();
			outStream.close(); 
			c.close(); 					
		}// End of try block 
		catch (Exception e) {
			//Set the error message if file couldn't be download
			//request.setAttribute("NotDownloaded", "The file could not be downloaded");
			throw new IOException( e ); 
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
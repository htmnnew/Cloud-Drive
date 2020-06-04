package iFile;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

@WebServlet("/Upload")
public class Upload extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException{
		request.getRequestDispatcher( "LoginGROUP.jsp" ).forward(request, response );
	}

	protected void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException{
		//Set the user name and the user id for the session scope
		HttpSession session = request.getSession();
		String name = (String) session.getAttribute("user");

		//If no user has logged in send him back to the login page
		if (name == null) {
			response.sendRedirect("LoginGROUP.jsp");
			return;
		}

		int id = (Integer) session.getAttribute("userid");

		//Current date for database, to track the upload
		SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		Date date = new Date(System.currentTimeMillis());

		// Create a factory for disk-based file items
		DiskFileItemFactory factory = new DiskFileItemFactory();

		// Configure a repository (to ensure a secure temp location is used)
		ServletContext servletContext = this.getServletConfig().getServletContext();
		File repository = (File) servletContext.getAttribute( "javax.servlet.context.tempdir" );

		factory.setRepository( repository );

		// Create a new file upload handler
		ServletFileUpload upload = new ServletFileUpload( factory );

		// Count how many files are uploaded
		int count = 0;

		// The directory we want to save the uploaded files to.
		String fileDir = getServletContext().getRealPath( "/WEB-INF/uploads" );

		// Insert the record into the database
		Connection c = null;
		String filename = null;
		String path = fileDir;
		String pathDB = null;

		// Parse the request
		try
		{
			List<FileItem> items = upload.parseRequest( request );
			for( FileItem item : items )
			{
				// If the item is not a form field - meaning it's an uploaded file, we save it to the target dir
				// If no file was selected, redirect the user back to the Upload page with an error message 
				if (item.getSize()==0) {
					request.setAttribute("uploadError", "No file selected" );
					request.getRequestDispatcher( "UploadViewGROUP.jsp" ).forward(request, response ); 
					return;
				}
				else {
					if( !item.isFormField()) {
						// item.getName() will return the full path of the uploaded
						// file, e.g. "C:/My Documents/files/test.txt", but we only
						// want the file name part, which is why we first create a
						// File object, then use File.getName() to get the file
						// name.
						// /var/usr/some/temp/dir/some-file.jpg
						// /user/albert/3220/WEB-INF/uploads   some-file.jpg

						String fileName = (new File( item.getName() )).getName();
						File file = new File( fileDir, fileName );
						try {
							item.write( file );
						}
						catch (Exception e) {
							response.sendRedirect("UploadViewGROUP.jsp");	
							return;
						}
						++count;
						filename= file.getName();
						//Modify path with double \\ for DB, otherwise the path wouldn't include any "\" 
						pathDB = path.replace("\\", "\\\\");
						pathDB = pathDB + "/" + fileName;

						//If the upload was successful, set an error message
						request.setAttribute("fileName", file.getName());
						request.setAttribute("uploadSuccess", "<b>"+ count + " file(s) has been uploaded successfully"); //to " + fileDir
					}
				}
			}

			//Upload the filename to a database
			String url = "jdbc:mysql://cs3.calstatela.edu/username";
			c = DriverManager.getConnection( url, "username", "password" );
			String sql = "INSERT INTO files (filename, user_id, upload_date, path) VALUES('" + filename + "', '" + id + "', '" + formatter.format(date) + "', '"+ pathDB +"')";
			PreparedStatement pstmt = c.prepareStatement( sql );

			//Set the message of successful upload to DB, and redirect the user back
			int numberOfRowsAffected = pstmt.executeUpdate();
			if (numberOfRowsAffected >0 ) {
				request.setAttribute("uploadDB", "Upload of \""+ filename +"\" to database successfull!" );
				request.getRequestDispatcher( "UploadViewGROUP.jsp" ).forward(request, response );
			}
		} // The end of the try block

		catch( Exception e ){
			throw new IOException( e ); 
		}
		finally {
			try {
				if( c != null ) c.close();
			}
			catch( SQLException e ){
				throw new ServletException( e );
			}
		}        
	}
}
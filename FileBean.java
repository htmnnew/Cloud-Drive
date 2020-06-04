package models;

import java.util.Date;

public class FileBean {

    Integer id;
    String filename;
    String user_id;
    Date upload_date;
    
    public FileBean()
    {
    	//Constructor
    }

    public FileBean( Integer id, String filename, String user_id, Date upload_date)
    {
        this.id = id;
        this.filename = filename;
        this.user_id = user_id;
        this.upload_date = upload_date;
    }

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public Date getUpload_date() {
		return upload_date;
	}

	public void setUpload_date(Date upload_date) {
		this.upload_date = upload_date;
	}
    
}

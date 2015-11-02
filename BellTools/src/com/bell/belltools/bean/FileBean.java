package com.bell.belltools.bean;  
/*  
 * @info   
 * @author GuoJiang 
 * @time 2015年10月27日 下午5:31:17  
 * @version    
 */
public class FileBean {
	
	private String filePath;
	private String fileName;
	private String fileSize;
	private String fileTime;
	private boolean isFile;
	public FileBean(){}
	public FileBean(String filePath, String fileName, String fileSize, String fileTime, boolean isFile) {
		super();
		this.filePath = filePath;
		this.fileName = fileName;
		this.fileSize = fileSize;
		this.fileTime = fileTime;
		this.isFile = isFile;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFileSize() {
		return fileSize;
	}
	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}
	public String getFileTime() {
		return fileTime;
	}
	public void setFileTime(String fileTime) {
		this.fileTime = fileTime;
	}
	public boolean isFile() {
		return isFile;
	}
	public void setFile(boolean isFile) {
		this.isFile = isFile;
	}

	
}
  
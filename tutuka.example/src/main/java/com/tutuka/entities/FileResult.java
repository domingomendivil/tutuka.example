package com.tutuka.entities;

/**
 * Describes the results for the corresponding file.
 * Information : fileName, total Records found, unmatched records on the file
 * @author Domingo Mendivil
 *
 */
public class FileResult {
	
	private String fileName;
	private Long totalRecords= new Long(0);
	private Long unMatchedRecords= new Long(0);
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public Long getTotalRecords() {
		return totalRecords;
	}
	
	public Long getUnMatchedRecords() {
		return unMatchedRecords;
	}
	
	public void addTotalRecords(){
		totalRecords++;
	}
	
	public void addUnMatchedRecords(){
		unMatchedRecords++;
	}

	
	

}

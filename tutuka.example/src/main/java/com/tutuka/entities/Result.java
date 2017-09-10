package com.tutuka.entities;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Indicates the result of comparing two files.
 * Information: file information, total matching records and a list of unmatched records
 * @author Domingo Mendivil
 *
 */
public class Result {
	private Long matchingRecords =new Long(0);
	
	private FileResult fileResult1;
	private FileResult fileResult2;
	
	
	public Result(){
		fileResult1= new FileResult();
		fileResult2= new FileResult();
	}
	
	
	
	
	public FileResult getFileResult1() {
		return fileResult1;
	}

	public void setFileResult1(FileResult fileResult1) {
		this.fileResult1 = fileResult1;
	}

	public FileResult getFileResult2() {
		return fileResult2;
	}

	public void setFileResult2(FileResult fileResult2) {
		this.fileResult2 = fileResult2;
	}

	

	public Long getMatchingRecords() {
		return matchingRecords;
	}
	
	public void addMatchingRecord(){
		matchingRecords++;
	}
	
	

	private Collection<UnMatchedRecord> unMatchedRecords = new ArrayList<>();
	public Collection<UnMatchedRecord> getUnMatchedRecords() {
		return unMatchedRecords;
	}

	public void setUnMatchedRecords(Collection<UnMatchedRecord> unMatchedRecords) {
		this.unMatchedRecords = unMatchedRecords;
	}
}

package com.tutuka.facade;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.ListIterator;

import com.tutuka.entities.Result;
import com.tutuka.entities.Transaction;
import com.tutuka.entities.UnMatchedRecord;


/**
 * This is the class where the concealment between the two files is done.
 * 
 * @author Domingo Mendivil
 *
 */
public class TrxComparator {

	/*
	 * Separator for CSV file
	 */
	private static final String LINE_SEPARATOR = ",";
	
	
	/*
	 * Formatting for Dates in the CSV files
	 */
	private static final String DATE_FORMAT = "yyyy-MM-dd hh:mm:ss";
	
	/*
	 *  Helper class for date-format
	 */
	private SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);

	/*
	 * List of transactions from file 1. Is not a local variable for performance reasons
	 */
	private ArrayList<Transaction> trxFile1;
	
	/*
	 * List of transactions from file 2. Is not a local variable for performance reasons
	 */
	private ArrayList<Transaction> trxFile2;
	
	
	/*
	 * Map containing transactions from file 1, where the key is the transaction ID. Is not a local variable for performance reasons
	 */
	private HashMap<BigInteger,ArrayList<Transaction>> map;
	
	/*
	 * Hash with the transactions IDs that were concealed successfully. 
	 */
	private HashSet<BigInteger> concealed;
	
	/*
	 * Result of comparing the two files. Is not a local variable for performance reasons
	 */
	private Result result;
	
	
	
	
	/**
	 * Reads all the transactions from the first file, and put them in an list and a HashMap
	 * with the key being the transaction ID. All the transactions that have the same ID are put in the
	 * corresponding hash, in a list inside the hash.
	 * 
	 * @param fileReader1
	 * @param result
	 * @throws ParseException
	 * @throws IOException
	 */
	private void addTransactionsFile1(BufferedReader fileReader1) throws ParseException, IOException{
		String line = fileReader1.readLine();
		while ((line != null)) {
			Transaction trx = getTransactionLine(line);
			addTransaction(trx);
			line = fileReader1.readLine();
			result.getFileResult1().addTotalRecords();
		}
	}
	
	
	/**
	 * This the main method, that makes the concealment of transactions of two files.
	 * It returns a class Result
	 * @param file1
	 * @param file2
	 * @return a result, with the resulting information of the concealment
	 * @throws IOException
	 * @throws ParseException
	 */
	public Result compareFiles(BufferedReader fileReader1, BufferedReader fileReader2,String fileName1,String fileName2) throws IOException, ParseException {
			concealed = new HashSet<>(); //initialize the structures
			trxFile1 = new ArrayList<>();
			trxFile2 = new ArrayList<>();
			map = new HashMap<>();
			fileReader1.readLine(); //step over the headers
			fileReader2.readLine(); //step over the headers
			result = new Result();
			result.getFileResult1().setFileName(fileName1);
			result.getFileResult2().setFileName(fileName2);
			addTransactionsFile1(fileReader1);
			mapFile2withFile1(fileReader2);
			getRestOfUnMatchedRecords(trxFile2,result,concealed);
			return result;
	}
	
	
	
	/**
	 * After having all the unmatched transactions from file 2, this method tries to find
	 * similar transactions, considering the transaction ID.
	 * Transactions with the same ID that appear in both files, are joined together, and 
	 * a record of unmatching transactions is created. That record also indicates which field(s)
	 * are different.
	 * 
	 * To do this, it iterates over the unmatched transactions of the second file
	 * and finds the corresponding transactions from the first file (from the hash) with the same ID.
	 * It maps both transactions (file1 and file2) and creates a record. Both transactions are removed
	 * from the list of unmatched transactions.
	 * 
	 * If a transaction on the second file is not found in the first file, is because these transactions
	 * have already been mapped together, and there is a repeated transaction on file 2.
	 * 
	 * Lastly, records of unmatching transactions from the first file are created.
	 * 
	 * @param trxFile22
	 * @param result
	 * @param concealed
	 */
	private void getRestOfUnMatchedRecords(ArrayList<Transaction> trxFile22, Result result,
			HashSet<BigInteger> concealed) {
		for (Transaction trx : trxFile2){
			ArrayList<Transaction> transactions1 = findTransactionById(trx.getId());
			if (transactions1!=null){
				ListIterator<Transaction> it =transactions1.listIterator(); //ListIterator is used in order to remove concurrently
				if (it.hasNext()){
					Transaction trx1 =it.next();
					UnMatchedRecord unMatched = new UnMatchedRecord(trx1,trx);
					result.getUnMatchedRecords().add(unMatched);
					result.getFileResult1().addUnMatchedRecords();
					result.getFileResult2().addUnMatchedRecords();
					it.remove();
				    trxFile1.remove(trx1);
					while (it.hasNext()){
						trx1 =it.next();
						boolean wasConcealed = concealed.contains(trx.getId());
						unMatched= new UnMatchedRecord(wasConcealed);
						unMatched.setTrxFile1(trx1);
						result.getUnMatchedRecords().add(unMatched);
						result.getFileResult1().addUnMatchedRecords();
					    it.remove();
					    trxFile1.remove(trx1);
						
					}
				}else{
					UnMatchedRecord unMatched;
					boolean wasConcealed = concealed.contains(trx.getId());
					unMatched= new UnMatchedRecord(wasConcealed);
					unMatched.setTrxFile2(trx);
					result.getUnMatchedRecords().add(unMatched);
					result.getFileResult2().addUnMatchedRecords();;
				}
			
			}
		}
		
		getUnMatchedTransactionsFromFile1(result, concealed);
	}
	
	
	/**
	 * The last thing is creating records of the remaining unmatched transactions from file 1, 
	 * which couldn't be mapped with any transactions from the second file. That is beacause
	 * transactions ID were not found in the second file.
	 * @param result
	 * @param concealed
	 */
	private void getUnMatchedTransactionsFromFile1(Result result,HashSet<BigInteger> concealed){
		for (Transaction trx:trxFile1){
			boolean wasConcealed = concealed.contains(trx.getId());
			UnMatchedRecord unMatched= new UnMatchedRecord(wasConcealed);
			unMatched.setTrxFile1(trx);
			result.getUnMatchedRecords().add(unMatched);
			result.getFileResult1().addUnMatchedRecords();
		}
	}

	
	/**
	 * It iterates over the second file getting each of the transactions, and tries to match each transaction
	 * with the list of transactions obtained previously from the first file.
	 * To find if the transaction is already in file 1, it gets the id from the hash 
	 * If the transaction was not found in the first file, or is not completely equal it is un unmatched transaction in 2nd file
	 * If the transaction matches (is fully equal), the counter of matching transactions are incremented
	 * @param fileReader2
	 * @param result
	 * @param concealed
	 * @throws IOException
	 * @throws ParseException
	 */
	private void mapFile2withFile1(BufferedReader fileReader2) throws IOException, ParseException {
		String line2 = fileReader2.readLine();
		while (line2!=null){
			Transaction trx = getTransactionLine(line2);
			ArrayList<Transaction> transactions=findTransactionById(trx.getId());
			if (transactions==null){   //Transaction not found
				UnMatchedRecord unMatched = new UnMatchedRecord();
				unMatched.setTrxFile2(trx);
				result.getUnMatchedRecords().add(unMatched);
				result.getFileResult2().addUnMatchedRecords();;
			}else{
				if (transactions.contains(trx)){
					result.addMatchingRecord();
					removeTransactionFile1(trx);
					concealed.add(trx.getId());
				}else{
					trxFile2.add(trx);
				}
			}
			result.getFileResult2().addTotalRecords();;
			line2 = fileReader2.readLine();
		}
		
	}
	
	/**
	 * Returns all the transactions from file 1, by Transaction ID
	 * @param id
	 * @return
	 */
	private ArrayList<Transaction> findTransactionById(BigInteger id) {
		return map.get(id);

	}

	private void removeTransactionFile1(Transaction trx) {
		trxFile1.remove(trx);
		ArrayList<Transaction> transactions = map.get(trx.getId());
		transactions.remove(trx);
	}

	/**
	 * Adds a transaction to the list of transactions of file 1, and puts that transaction into the hash
	 * by Transaction ID
	 * @param trx
	 */
	private void addTransaction(Transaction trx){
		trxFile1.add(trx);
		ArrayList<Transaction> trxs =map.get(trx.getId());
		if (trxs==null){
			trxs = new ArrayList<>();
			map.put(trx.getId(),trxs);
		}
		trxs.add(trx);
	}

	
	/**
	 * 
	 * @param line
	 * @return
	 * @throws ParseException
	 */
	private Transaction getTransactionLine(String line) throws ParseException {
		if (line!=null){
			String[] trxFile1 = line.split(LINE_SEPARATOR);
			Transaction trx = new Transaction();
			trx.setProfileName(trxFile1[0]);
			try{
				trx.setDate(dateFormat.parse(trxFile1[1]));
				
			} catch(ParseException e){
				
			}
			
			try{
			trx.setAmount(new BigDecimal(trxFile1[2]));
			} catch(NumberFormatException e){
				
			}
			trx.setNarrative(trxFile1[3]);
			trx.setDescription(trxFile1[4]);
			trx.setId(new BigInteger(trxFile1[5]));
			trx.setType(Byte.parseByte(trxFile1[6]));
			if (trxFile1.length>=8)
			trx.setWalletReference(trxFile1[7]);
			return trx;
		}
		else
			return null;
	}
	


}

package com.tutuka.entities;

/**
 * This class represents all the unmatched records found
 * @author Domingo Mendivil
 *
 */
public class UnMatchedRecord {
	
	/*
	 * The transaction found on file 1, if there is one (could be null
	 */
	private Transaction trxFile1;  
	
	/*
	 * The transaction found on file 2, if there is one (could be null
	 */
	private Transaction trxFile2; 
	
	/*
	 * Indicates whether or not the transaction ID was previously found and concealed succesfully.
	 * In this case, is because the transaction record was found repeated on the file
	 * 
	 */
	private boolean transactionIDConcealed; 
	
	
	/*
	 * Indicates if the narrative field differs or not in each record on file 1 and file 2
	 */
	private boolean narrativeDiffer; 
	
	/*
	 * Indicates if the Type field differs or not in each record on file 1 and file 2
	 */
	private boolean typeDiffer;
	
	/*
	 * Indicates if the profile field differs or not in each record on file 1 and file 2
	 */
	private boolean profileNameDiffer;
	
	/*
	 * Indicates if the walletReference field differs or not in each record on file 1 and file 2
	 */
	private boolean walletReferenceDiffer;

	
	/*
	 * Indicates if the date  field differs or not in each record on file 1 and file 2
	 */
	private boolean dateDiffer;
	
	/*
	 * Indicates if the Amount field differs or not in each record on file 1 and file 2
	 */
	private boolean amountDiffer;
	
	/*
	 * Indicates if the description field differs or not in each record on file 1 and file 2
	 */
	private boolean descriptionDiffer;

	
	/**
	 * Constructs and an UnMatchedRecord, passing as argument whether or not a transaction with the same ID was concealed 
	 * successfully
	 * @param transactionIDConcealed
	 */
	public UnMatchedRecord(boolean transactionIDConcealed){
		this.transactionIDConcealed = transactionIDConcealed;
	}
	
	
	/**
	 * Indicates if there is a transaction with the same ID which was concealed
	 * @return
	 */
	public boolean isTransactionIDConcealed() {
		return transactionIDConcealed;
	}


	public void setTransactionIDConcealed(boolean transactionIDConcealed) {
		this.transactionIDConcealed = transactionIDConcealed;
	}


	public UnMatchedRecord(Transaction trxFile1,Transaction trxFile2){
		this.trxFile1=trxFile1;
		this.trxFile2=trxFile2;
		
		if (trxFile1.getDate()!=null){
			if (!trxFile1.getDate().equals(trxFile2.getDate())){
				dateDiffer=true;
			}		
		}else if (trxFile2.getDate()!=null){
			dateDiffer=true;
		}

		if (trxFile1.getAmount()!=null){
			if (!trxFile1.getAmount().equals(trxFile2.getAmount())){
				amountDiffer=true;
			}
		}else if (trxFile2.getAmount()!=null){ 
			amountDiffer=true;
		}
		
		
		if (!trxFile1.getDescription().equals(trxFile2.getDescription())){
			descriptionDiffer=true;
		}
		
		if (!trxFile1.getNarrative().equals(trxFile2.getNarrative())){
			narrativeDiffer=true;
		}
		
		if (trxFile1.getType()!=trxFile2.getType()){
			typeDiffer=true;
		}
		
		if (!trxFile1.getProfileName().equals(trxFile2.getProfileName())){
			profileNameDiffer=true;
		}
		
		if (trxFile1.getWalletReference()!=null){
			if (!trxFile1.getWalletReference().equals(trxFile2.getWalletReference())){
				walletReferenceDiffer=true;
			}
		}else if (trxFile2.getWalletReference()!=null){
			walletReferenceDiffer=true;
		}
	
	}
	
	
	
	public boolean isNarrativeDiffer() {
		return narrativeDiffer;
	}



	public boolean isTypeDiffer() {
		return typeDiffer;
	}



	public boolean isProfileNameDiffer() {
		return profileNameDiffer;
	}



	public boolean isWalletReferenceDiffer() {
		return walletReferenceDiffer;
	}



	public UnMatchedRecord(){
		
	}
	
	

	
	
	public boolean isDateDiffer() {
		return dateDiffer;
	}



	public boolean isAmountDiffer() {
		return amountDiffer;
	}


	public boolean isDescriptionDiffer() {
		return descriptionDiffer;
	}


	public Transaction getTrxFile1() {
		return trxFile1;
	}
	public void setTrxFile1(Transaction trxFile1) {
		this.trxFile1 = trxFile1;
	}
	public Transaction getTrxFile2() {
		return trxFile2;
	}
	public void setTrxFile2(Transaction trxFile2) {
		this.trxFile2 = trxFile2;
	}

}

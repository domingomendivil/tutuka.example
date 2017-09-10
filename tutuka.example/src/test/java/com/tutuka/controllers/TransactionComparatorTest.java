package com.tutuka.controllers;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.BufferedReader;
import java.io.IOException;
import java.text.ParseException;

import org.junit.Test;

import com.tutuka.entities.Result;
import com.tutuka.facade.TrxComparator;

/**
 * Unit Testing class for TransactionComparator (TDD)
 * 
 * @author Domingo Mendivil
 *
 */
public class TransactionComparatorTest {

	private BufferedReader fileReader1 = mock(BufferedReader.class);

	private BufferedReader fileReader2 = mock(BufferedReader.class);

	// When both files have no records, it must return
	// total records file 1 : 0
	// total records file 2 : 0
	// matching records : 0
	// unmatching records file 1: 0
	// unmatching records file 2: 0
	@Test
	public void test3() throws IOException, ParseException {
		when(fileReader1.readLine()).thenReturn("").thenReturn(null);
		when(fileReader2.readLine()).thenReturn("").thenReturn(null);
		Result result = new TrxComparator().compareFiles(fileReader1, fileReader2, "name1", "name2");
		assertEquals(result.getMatchingRecords(), new Long(0));
		assertEquals(result.getFileResult1().getTotalRecords(), new Long(0));
		assertEquals(result.getFileResult2().getTotalRecords(), new Long(0));
		assertEquals(result.getUnMatchedRecords().size(), 0);
	}

	// When file 1 has one record, and file 2 has no records, it must return
	// total records file 1 : 1
	// total records file 2 : 0
	// matching records : 0
	// unmatching records file 1: 1
	// unmatching records file 2: 0

	@Test
	public void test2() throws IOException, ParseException {
		String trx1 = "Card Campaign,2014-01-11 22:39:11,-10000,*MOGODITSHANE2            MOGODITHSANE  BW,DEDUCT,0584011815513406,1,P_NzI1MjA1NjZfMTM3ODczODI3Mi4wNzY5,";
		;
		when(fileReader1.readLine()).thenReturn("").thenReturn(trx1).thenReturn(null);
		when(fileReader2.readLine()).thenReturn("").thenReturn(null);
		Result result = new TrxComparator().compareFiles(fileReader1, fileReader2, "name1", "name2");
		assertEquals(result.getMatchingRecords(), new Long(0));
		assertEquals(result.getFileResult1().getTotalRecords(), new Long(1));
		assertEquals(result.getFileResult2().getTotalRecords(), new Long(0));
		assertEquals(result.getUnMatchedRecords().size(), 1);
	}

	// When the two files have one record and is the same in bothe files, it
	// must return
	// total records file 1 : 1
	// total records file 2 : 1
	// matching records : 1
	// unmatching records file 1: 0
	// unmatching records file 2: 0
	@Test
	public void test4() throws IOException, ParseException {
		String trx1 = "Card Campaign,2014-01-11 22:39:11,-10000,*MOGODITSHANE2            MOGODITHSANE  BW,DEDUCT,0584011815513406,1,P_NzI1MjA1NjZfMTM3ODczODI3Mi4wNzY5,";
		;
		when(fileReader1.readLine()).thenReturn("").thenReturn(trx1).thenReturn(null);
		when(fileReader2.readLine()).thenReturn("").thenReturn(trx1).thenReturn(null);
		;
		Result result = new TrxComparator().compareFiles(fileReader1, fileReader2, "name1", "name2");
		assertEquals(result.getMatchingRecords(), new Long(1));
		assertEquals(result.getFileResult1().getTotalRecords(), new Long(1));
		assertEquals(result.getFileResult2().getTotalRecords(), new Long(1));
		assertEquals(result.getFileResult1().getUnMatchedRecords(), new Long(0));
		assertEquals(result.getFileResult2().getUnMatchedRecords(), new Long(0));
		assertEquals(result.getUnMatchedRecords().size(), 0);
	}

	// When the two files have one record with the same transaction ID but
	// differs in amount, result must be
	// total records file 1 : 1
	// total records file 2 : 1
	// matching records : 0
	// unmatching records file 1: 1
	// unmatching records file 2: 1
	// total unmatching records: 1

	@Test
	public void test5() throws IOException, ParseException {
		String trx1 = "Card Campaign,2014-01-11 22:39:11,-10000,*MOGODITSHANE2            MOGODITHSANE  BW,DEDUCT,0584011815513406,1,P_NzI1MjA1NjZfMTM3ODczODI3Mi4wNzY5,";
		;
		String trx2 = "Card Campaign,2014-01-11 22:39:11,-10050,*MOGODITSHANE2            MOGODITHSANE  BW,DEDUCT,0584011815513406,1,P_NzI1MjA1NjZfMTM3ODczODI3Mi4wNzY5,";
		;
		when(fileReader1.readLine()).thenReturn("").thenReturn(trx1).thenReturn(null);
		when(fileReader2.readLine()).thenReturn("").thenReturn(trx2).thenReturn(null);
		;
		Result result = new TrxComparator().compareFiles(fileReader1, fileReader2, "name1", "name2");
		assertEquals(result.getMatchingRecords(), new Long(0));
		assertEquals(result.getFileResult1().getTotalRecords(), new Long(1));
		assertEquals(result.getFileResult2().getTotalRecords(), new Long(1));
		assertEquals(result.getFileResult1().getUnMatchedRecords(), new Long(1));
		assertEquals(result.getFileResult2().getUnMatchedRecords(), new Long(1));
		assertEquals(result.getUnMatchedRecords().size(), 1);
	}

	// When the two files have one record with different transaction ID, result
	// must be
	// total records file 1 : 1
	// total records file 2 : 1
	// matching records : 0
	// unmatching records file 1: 1
	// unmatching records file 2: 1
	// total unmatching records: 2

	@Test
	public void test6() throws IOException, ParseException {
		String trx1 = "Card Campaign,2014-01-11 22:39:11,-10000,*MOGODITSHANE2            MOGODITHSANE  BW,DEDUCT,0584011815513406,1,P_NzI1MjA1NjZfMTM3ODczODI3Mi4wNzY5,";
		;
		String trx2 = "Card Campaign,2014-01-11 22:39:11,-10050,*MOGODITSHANE2            MOGODITHSANE  BW,DEDUCT,0584011815513407,1,P_NzI1MjA1NjZfMTM3ODczODI3Mi4wNzY5,";
		;
		when(fileReader1.readLine()).thenReturn("").thenReturn(trx1).thenReturn(null);
		when(fileReader2.readLine()).thenReturn("").thenReturn(trx2).thenReturn(null);
		;
		Result result = new TrxComparator().compareFiles(fileReader1, fileReader2, "name1", "name2");
		assertEquals(result.getMatchingRecords(), new Long(0));
		assertEquals(result.getFileResult1().getTotalRecords(), new Long(1));
		assertEquals(result.getFileResult2().getTotalRecords(), new Long(1));
		assertEquals(result.getFileResult1().getUnMatchedRecords(), new Long(1));
		assertEquals(result.getFileResult2().getUnMatchedRecords(), new Long(1));
		assertEquals(result.getUnMatchedRecords().size(), 2);
	}

	// When file 1 has two records with the same Transaction ID, and both
	// records are exactly the same
	// and file 2 has one record, which is exactly the same record, result must
	// be
	// total records file 1 : 2
	// total records file 2 : 1
	// matching records : 1
	// unmatching records file 1: 1
	// unmatching records file 2: 0
	// total unmatching records: 1

	@Test
	public void test7() throws IOException, ParseException {
		String trx1 = "Card Campaign,2014-01-11 22:39:11,-10000,*MOGODITSHANE2            MOGODITHSANE  BW,DEDUCT,0584011815513406,1,P_NzI1MjA1NjZfMTM3ODczODI3Mi4wNzY5,";
		;
		when(fileReader1.readLine()).thenReturn("").thenReturn(trx1).thenReturn(trx1).thenReturn(null);
		when(fileReader2.readLine()).thenReturn("").thenReturn(trx1).thenReturn(null);
		;
		Result result = new TrxComparator().compareFiles(fileReader1, fileReader2, "name1", "name2");
		assertEquals(result.getMatchingRecords(), new Long(1));
		assertEquals(result.getFileResult1().getTotalRecords(), new Long(2));
		assertEquals(result.getFileResult2().getTotalRecords(), new Long(1));
		assertEquals(result.getFileResult1().getUnMatchedRecords(), new Long(1));
		assertEquals(result.getFileResult2().getUnMatchedRecords(), new Long(0));
		assertEquals(result.getUnMatchedRecords().size(), 1);
	}

	// When file 2 has two records with the same Transaction ID, and both
	// records are exactly the same
	// and file 1 has one record, which is exactly the same record, result must
	// be
	// total records file 1 : 1
	// total records file 2 : 2
	// matching records : 1
	// unmatching records file 1: 0
	// unmatching records file 2: 1
	// total unmatching records: 1

	@Test
	public void test8() throws IOException, ParseException {
		String trx1 = "Card Campaign,2014-01-11 22:39:11,-10000,*MOGODITSHANE2            MOGODITHSANE  BW,DEDUCT,0584011815513406,1,P_NzI1MjA1NjZfMTM3ODczODI3Mi4wNzY5,";
		;
		when(fileReader1.readLine()).thenReturn("").thenReturn(trx1).thenReturn(null);
		when(fileReader2.readLine()).thenReturn("").thenReturn(trx1).thenReturn(trx1).thenReturn(null);
		;
		Result result = new TrxComparator().compareFiles(fileReader1, fileReader2, "name1", "name2");
		assertEquals(result.getMatchingRecords(), new Long(1));
		assertEquals(result.getFileResult1().getTotalRecords(), new Long(1));
		assertEquals(result.getFileResult2().getTotalRecords(), new Long(2));
		assertEquals(result.getFileResult1().getUnMatchedRecords(), new Long(0));
		assertEquals(result.getFileResult2().getUnMatchedRecords(), new Long(1));
		assertEquals(result.getUnMatchedRecords().size(), 1);
	}

	// When the two files have one record with the same transaction ID but
	// differs in date, result must be
	// total records file 1 : 1
	// total records file 2 : 1
	// matching records : 0
	// unmatching records file 1: 1
	// unmatching records file 2: 1
	// total unmatching records: 1

	@Test
	public void test9() throws IOException, ParseException {
		String trx1 = "Card Campaign,2014-01-11 22:39:11,-10000,*MOGODITSHANE2            MOGODITHSANE  BW,DEDUCT,0584011815513406,1,P_NzI1MjA1NjZfMTM3ODczODI3Mi4wNzY5,";
		;
		String trx2 = "Card Campaign,2014-01-13 22:39:11,-10000,*MOGODITSHANE2            MOGODITHSANE  BW,DEDUCT,0584011815513406,1,P_NzI1MjA1NjZfMTM3ODczODI3Mi4wNzY5,";
		;
		when(fileReader1.readLine()).thenReturn("").thenReturn(trx1).thenReturn(null);
		when(fileReader2.readLine()).thenReturn("").thenReturn(trx2).thenReturn(null);
		;
		Result result = new TrxComparator().compareFiles(fileReader1, fileReader2, "name1", "name2");
		assertEquals(result.getMatchingRecords(), new Long(0));
		assertEquals(result.getFileResult1().getTotalRecords(), new Long(1));
		assertEquals(result.getFileResult2().getTotalRecords(), new Long(1));
		assertEquals(result.getFileResult1().getUnMatchedRecords(), new Long(1));
		assertEquals(result.getFileResult2().getUnMatchedRecords(), new Long(1));
		assertEquals(result.getUnMatchedRecords().size(), 1);
	}

	// When the two files have one record with the same transaction ID but
	// differs in description, result must be
	// total records file 1 : 1
	// total records file 2 : 1
	// matching records : 0
	// unmatching records file 1: 1
	// unmatching records file 2: 1
	// total unmatching records: 1

	@Test
	public void test10() throws IOException, ParseException {
		String trx1 = "Card Campaign,2014-01-11 22:39:11,-10000,*MOGODITSHANE2            MOGODITHSANE  BW,DEDUCT,0584011815513406,1,P_NzI1MjA1NjZfMTM3ODczODI3Mi4wNzY5,";
		;
		String trx2 = "Card Campaign,2014-01-13 22:39:11,-10000,*MOGODITSHANE,DEDUCT,0584011815513406,1,P_NzI1MjA1NjZfMTM3ODczODI3Mi4wNzY5,";
		;
		when(fileReader1.readLine()).thenReturn("").thenReturn(trx1).thenReturn(null);
		when(fileReader2.readLine()).thenReturn("").thenReturn(trx2).thenReturn(null);
		;
		Result result = new TrxComparator().compareFiles(fileReader1, fileReader2, "name1", "name2");
		assertEquals(result.getMatchingRecords(), new Long(0));
		assertEquals(result.getFileResult1().getTotalRecords(), new Long(1));
		assertEquals(result.getFileResult2().getTotalRecords(), new Long(1));
		assertEquals(result.getFileResult1().getUnMatchedRecords(), new Long(1));
		assertEquals(result.getFileResult2().getUnMatchedRecords(), new Long(1));
		assertEquals(result.getUnMatchedRecords().size(), 1);
	}

	// When the two files have one record with the same transaction ID but
	// differs in type, result must be
	// total records file 1 : 1
	// total records file 2 : 1
	// matching records : 0
	// unmatching records file 1: 1
	// unmatching records file 2: 1
	// total unmatching records: 1

	@Test
	public void test11() throws IOException, ParseException {
		String trx1 = "Card Campaign,2014-01-11 22:39:11,-10000,*MOGODITSHANE2            MOGODITHSANE  BW,DEDUCT,0584011815513406,1,P_NzI1MjA1NjZfMTM3ODczODI3Mi4wNzY5,";
		;
		String trx2 = "Card Campaign,2014-01-13 22:39:11,-10000,*MOGODITSHANE2            MOGODITHSANE  BW,DEDUCT,0584011815513406,2,P_NzI1MjA1NjZfMTM3ODczODI3Mi4wNzY5,";
		;
		when(fileReader1.readLine()).thenReturn("").thenReturn(trx1).thenReturn(null);
		when(fileReader2.readLine()).thenReturn("").thenReturn(trx2).thenReturn(null);
		;
		Result result = new TrxComparator().compareFiles(fileReader1, fileReader2, "name1", "name2");
		assertEquals(result.getMatchingRecords(), new Long(0));
		assertEquals(result.getFileResult1().getTotalRecords(), new Long(1));
		assertEquals(result.getFileResult2().getTotalRecords(), new Long(1));
		assertEquals(result.getFileResult1().getUnMatchedRecords(), new Long(1));
		assertEquals(result.getFileResult2().getUnMatchedRecords(), new Long(1));
		assertEquals(result.getUnMatchedRecords().size(), 1);
	}

	// When the two files have one record with the same transaction ID but
	// differs in wallet reference, result must be
	// total records file 1 : 1
	// total records file 2 : 1
	// matching records : 0
	// unmatching records file 1: 1
	// unmatching records file 2: 1
	// total unmatching records: 1

	@Test
	public void test12() throws IOException, ParseException {
		String trx1 = "Card Campaign,2014-01-11 22:39:11,-10000,*MOGODITSHANE2            MOGODITHSANE  BW,DEDUCT,0584011815513406,1,P_NzI1MjA1NjZfMTM3ODwerOFSI3MiezY5,";
		;
		String trx2 = "Card Campaign,2014-01-13 22:39:11,-10000,*MOGODITSHANE2            MOGODITHSANE  BW,DEDUCT,0584011815513406,2,P_NzI1MjA1NjZfMTM3ODczODI3Mi4wNzY5,";
		;
		when(fileReader1.readLine()).thenReturn("").thenReturn(trx1).thenReturn(null);
		when(fileReader2.readLine()).thenReturn("").thenReturn(trx2).thenReturn(null);
		;
		Result result = new TrxComparator().compareFiles(fileReader1, fileReader2, "name1", "name2");
		assertEquals(result.getMatchingRecords(), new Long(0));
		assertEquals(result.getFileResult1().getTotalRecords(), new Long(1));
		assertEquals(result.getFileResult2().getTotalRecords(), new Long(1));
		assertEquals(result.getFileResult1().getUnMatchedRecords(), new Long(1));
		assertEquals(result.getFileResult2().getUnMatchedRecords(), new Long(1));
		assertEquals(result.getUnMatchedRecords().size(), 1);
	}

	// When the two files have one record with the same transaction ID but
	// differs in date, being the date in file 1 in bad format
	// total records file 1 : 1
	// total records file 2 : 1
	// matching records : 0
	// unmatching records file 1: 1
	// unmatching records file 2: 1
	// total unmatching records: 1
	@Test
	public void test15() throws IOException, ParseException {
		String trx1 = "Card Campaign,A,-10000,*MOGODITSHANE2            MOGODITHSANE  BW,DEDUCT,0584011815513406,1,P_NzI1MjA1NjZfMTM3ODwerOFSI3MiezY5,";
		;
		String trx2 = "Card Campaign,2014-01-13 22:39:11,-10000,*MOGODITSHANE2            MOGODITHSANE  BW,DEDUCT,0584011815513406,2,P_NzI1MjA1NjZfMTM3ODczODI3Mi4wNzY5,";
		;
		when(fileReader1.readLine()).thenReturn("").thenReturn(trx1).thenReturn(null);
		when(fileReader2.readLine()).thenReturn("").thenReturn(trx2).thenReturn(null);
		;
		Result result = new TrxComparator().compareFiles(fileReader1, fileReader2, "name1", "name2");
		assertEquals(result.getMatchingRecords(), new Long(0));
		assertEquals(result.getFileResult1().getTotalRecords(), new Long(1));
		assertEquals(result.getFileResult2().getTotalRecords(), new Long(1));
		assertEquals(result.getFileResult1().getUnMatchedRecords(), new Long(1));
		assertEquals(result.getFileResult2().getUnMatchedRecords(), new Long(1));
		assertEquals(result.getUnMatchedRecords().size(), 1);
	}

	// When the two files have one record with the same transaction ID but
	// differs in date, being the date in file 2 in bad format
	// total records file 1 : 1
	// total records file 2 : 1
	// matching records : 0
	// unmatching records file 1: 1
	// unmatching records file 2: 1
	// total unmatching records: 1
	@Test
	public void test16() throws IOException, ParseException {
		String trx1 = "Card Campaign,2014-01-13 22:39:11,-10000,*MOGODITSHANE2            MOGODITHSANE  BW,DEDUCT,0584011815513406,1,P_NzI1MjA1NjZfMTM3ODwerOFSI3MiezY5,";
		;
		String trx2 = "Card Campaign,A,-10000,*MOGODITSHANE2            MOGODITHSANE  BW,DEDUCT,0584011815513406,2,P_NzI1MjA1NjZfMTM3ODczODI3Mi4wNzY5,";
		;
		when(fileReader1.readLine()).thenReturn("").thenReturn(trx1).thenReturn(null);
		when(fileReader2.readLine()).thenReturn("").thenReturn(trx2).thenReturn(null);
		;
		Result result = new TrxComparator().compareFiles(fileReader1, fileReader2, "name1", "name2");
		assertEquals(result.getMatchingRecords(), new Long(0));
		assertEquals(result.getFileResult1().getTotalRecords(), new Long(1));
		assertEquals(result.getFileResult2().getTotalRecords(), new Long(1));
		assertEquals(result.getFileResult1().getUnMatchedRecords(), new Long(1));
		assertEquals(result.getFileResult2().getUnMatchedRecords(), new Long(1));
		assertEquals(result.getUnMatchedRecords().size(), 1);
	}

	// When the two files have one record with the same transaction ID but
	// differs in amount, being the amount in file 1 in bad format
	// total records file 1 : 1
	// total records file 2 : 1
	// matching records : 0
	// unmatching records file 1: 1
	// unmatching records file 2: 1
	// total unmatching records: 1
	@Test
	public void test17() throws IOException, ParseException {
		String trx1 = "Card Campaign,2014-01-13 22:39:11,A,*MOGODITSHANE2            MOGODITHSANE  BW,DEDUCT,0584011815513406,1,P_NzI1MjA1NjZfMTM3ODwerOFSI3MiezY5,";
		;
		String trx2 = "Card Campaign,2014-01-13 22:39:11,-10000,*MOGODITSHANE2            MOGODITHSANE  BW,DEDUCT,0584011815513406,2,P_NzI1MjA1NjZfMTM3ODczODI3Mi4wNzY5,";
		;
		when(fileReader1.readLine()).thenReturn("").thenReturn(trx1).thenReturn(null);
		when(fileReader2.readLine()).thenReturn("").thenReturn(trx2).thenReturn(null);
		;
		Result result = new TrxComparator().compareFiles(fileReader1, fileReader2, "name1", "name2");
		assertEquals(result.getMatchingRecords(), new Long(0));
		assertEquals(result.getFileResult1().getTotalRecords(), new Long(1));
		assertEquals(result.getFileResult2().getTotalRecords(), new Long(1));
		assertEquals(result.getFileResult1().getUnMatchedRecords(), new Long(1));
		assertEquals(result.getFileResult2().getUnMatchedRecords(), new Long(1));
		assertEquals(result.getUnMatchedRecords().size(), 1);
	}

	// When file 1 has two records, which are also in file 2 and are exactly the
	// same, but
	// appear in different order, it must return
	// total records file 1 : 2
	// total records file 2 : 2
	// matching records : 2
	// unmatching records file 1: 0
	// unmatching records file 2: 0
	// total unmatching records: 0
	@Test
	public void test18() throws IOException, ParseException {
		String trx1 = "Card Campaign,2014-01-13 22:39:11,A,*MOGODITSHANE2            MOGODITHSANE  BW,DEDUCT,0584011815513409,1,P_NzI1MjA1NjZfMTM3ODwerOFSI3MiezY5,";
		;
		String trx2 = "Card Campaign,2014-01-20 22:39:11,-10000,*MOGODITSHANE2            MOGODITHSANE  BW,DEDUCT,0584011815513406,2,P_NzI1MjA1NjZfMTM3ODczODI3Mi4wNzY5,";
		;
		when(fileReader1.readLine()).thenReturn("").thenReturn(trx1).thenReturn(trx2).thenReturn(null);
		when(fileReader2.readLine()).thenReturn("").thenReturn(trx2).thenReturn(trx1).thenReturn(null);
		;
		Result result = new TrxComparator().compareFiles(fileReader1, fileReader2, "name1", "name2");
		assertEquals(result.getMatchingRecords(), new Long(2));
		assertEquals(result.getFileResult1().getTotalRecords(), new Long(2));
		assertEquals(result.getFileResult2().getTotalRecords(), new Long(2));
		assertEquals(result.getFileResult1().getUnMatchedRecords(), new Long(0));
		assertEquals(result.getFileResult2().getUnMatchedRecords(), new Long(0));
		assertEquals(result.getUnMatchedRecords().size(), 0);
	}

	// When file 1 has two records, which are also in file 2 and are exactly the
	// same, but
	// appear in different order, and file 1 has another record that does not
	// appear in file 2
	// must return
	// total records file 1 : 3
	// total records file 2 : 2
	// matching records : 2
	// unmatching records file 1: 1
	// unmatching records file 2: 0
	// total unmatching records: 1
	@Test
	public void test19() throws IOException, ParseException {
		String trx1 = "Card Campaign,2014-01-13 22:39:11,A,*MOGODITSHANE2            MOGODITHSANE  BW,DEDUCT,0584011815513409,1,P_NzI1MjA1NjZfMTM3ODwerOFSI3MiezY5,";
		;
		String trx2 = "Card Campaign,2014-01-20 22:39:11,-10000,*MOGODITSHANE2            MOGODITHSANE  BW,DEDUCT,0584011815513406,2,P_NzI1MjA1NjZfMTM3ODczODI3Mi4wNzY5,";
		;
		String trx3 = "Card Campaign,2014-05-17 22:39:11,-10000,*MOGODITSHANE2            MOGODITHSANE  BW,DEDUCT,0554031815513407,2,P_NzI1MjA1NjZfMTM3ODczODI3Mi4wNzY5,";
		;
		when(fileReader1.readLine()).thenReturn("").thenReturn(trx1).thenReturn(trx2).thenReturn(trx3).thenReturn(null);
		when(fileReader2.readLine()).thenReturn("").thenReturn(trx2).thenReturn(trx1).thenReturn(null);
		;
		Result result = new TrxComparator().compareFiles(fileReader1, fileReader2, "name1", "name2");
		assertEquals(result.getMatchingRecords(), new Long(2));
		assertEquals(result.getFileResult1().getTotalRecords(), new Long(3));
		assertEquals(result.getFileResult2().getTotalRecords(), new Long(2));
		assertEquals(result.getFileResult1().getUnMatchedRecords(), new Long(1));
		assertEquals(result.getFileResult2().getUnMatchedRecords(), new Long(0));
		assertEquals(result.getUnMatchedRecords().size(), 1);
	}

}

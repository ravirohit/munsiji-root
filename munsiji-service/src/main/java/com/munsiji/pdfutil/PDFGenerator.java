package com.munsiji.pdfutil;

import java.io.FileOutputStream;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.munsiji.commonUtil.DateUtil;
import com.munsiji.persistance.resource.UserAccount;

//https://www.baeldung.com/java-pdf-creation
public class PDFGenerator {
	public static void main(String[] arg){
		PDFGenerator obj = new PDFGenerator();
		obj.createPDFFile(null,null,null,null,null);
	}

   public void createPDFFile(List<Object[]> userObjectExpenseList,String fileName, String userName, String accType, String[] accNameArr ){
	   System.out.println("creating pdf file method called");
	   try{
	   Document document = new Document();
	   PdfWriter.getInstance(document, new FileOutputStream("C:/Project_Work/Other_learn/munsiji-root/munsiji-service/src/main/resources/pdf/"+fileName));
	   document.open();
	   Font font = FontFactory.getFont(FontFactory.COURIER, 10, BaseColor.BLACK);
	  /* Chunk chunk = new Chunk("Hello World\n\n", font);
	   document.add(chunk);*/
	   document.add( new Paragraph( "Hello, User!",font));
	   document.add(Chunk.NEWLINE);
	   document.add( new Paragraph( "Your user id: "+ userName,font) );
	   document.add( new Paragraph( "Please find the below report for account Type: "+accType,font) );
	   document.add( new Paragraph( "Selected account names for the above account Type: ["+String.join(",", accNameArr)+"]",font));	   
	   document.add(Chunk.NEWLINE);
	   
	   PdfPTable table = new PdfPTable(4);
	   Font tableFont = FontFactory.getFont(FontFactory.COURIER, 10, BaseColor.BLACK);
	   addTableHeader(table,font);
	   addRows(table,tableFont,userObjectExpenseList);
	   document.add(table);
	   document.close();
	   }
	   catch(Exception e){
		   System.out.println("Exception occur while creating pdf file:"+e);
	   }
   }
   private void addTableHeader(PdfPTable table,Font font) {
	    Stream.of("Amount", "Date of Expense", "Account Name","Expense Desc")
	      .forEach(columnTitle -> {
	        PdfPCell header = new PdfPCell();
	        header.setBackgroundColor(BaseColor.LIGHT_GRAY);
	        header.setBorderWidth(2);
	        header.setPhrase(new Phrase(columnTitle));
	        table.addCell(header);
	    });
	}
   private void addRows(PdfPTable table, Font font, List<Object[]> userObjectExpenseList) {
	   for(Object[] objEle:userObjectExpenseList){
		   table.addCell(new PdfPCell(new Phrase(((Float)objEle[0]).toString(),font)));
		   table.addCell(new PdfPCell(new Phrase(DateUtil.convertDBStringToViewString((Date)objEle[1]),font)));
		   table.addCell(new PdfPCell(new Phrase(((UserAccount)objEle[2]).getName(),font)));
		   table.addCell(new PdfPCell(new Phrase((String)objEle[3],font)));
	   }
	   
	   /* table.addCell("row 1, col 1");
	    table.addCell("row 1, col 2");
	    table.addCell("row 1, col 3");*/
	}

}

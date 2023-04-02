package edu.upenn.qrf.QRFLogSendTest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 */
public class QRFLogSendTest {

	/**
	 * 
	 */
	private void debug(String aMessage) {
		System.out.println(aMessage);
	}

	/**
	 * @param aPath
	 * @return
	 */
	public ArrayList<String> listFiles(String aPath) {
		File folder = new File(aPath);
		File[] listOfFiles = folder.listFiles();
		ArrayList<String> results = new ArrayList<String>();

		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				results.add(listOfFiles[i].getName());
			}
		}

		return results;
	}

	/**
	 * @param files
	 */
	public void debugFileList(ArrayList<String> files) {
		for (int i = 0; i < files.size(); i++) {
			debug("File [" + i + "]: " + files.get(i));
		}
	}

	/**
	 * @param aFile
	 * @return
	 */
	public byte[] fileToByte(String aFile) {
		File file = new File(aFile);

		FileInputStream fileInputStream = null;

		byte[] b = new byte[(int) file.length()];
		try {
			fileInputStream = new FileInputStream(file);
			fileInputStream.read(b);
			/*
			 * for (int i = 0; i < b.length; i++) { System.out.print((char) b[i]); }
			 */
		} catch (FileNotFoundException e) {
			debug("File Not Found.");
			// e.printStackTrace();
			return (null);
		} catch (IOException e1) {
			System.out.println("Error Reading The File.");
			e1.printStackTrace();
			return (null);
		}

		try {
			fileInputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
			return (null);
		}

		return b;
	}

	/**
	 * @param string
	 * @return
	 */
	public ArrayList<String> encodeFile(String aFile) {
		ArrayList<String> parts = new ArrayList<String>();

		byte[] rawData = fileToByte(aFile);

		if (rawData == null) {
			return null;
		}

		int blockSize = 1024;

		int count = 0;
		//int index=0;

		int pieces = rawData.length / blockSize;
		int partial = rawData.length % blockSize;
		
		debug("Encoding file of raw length: " + rawData.length + " into " + pieces + " pieces and " + partial + " bytes");
					
		for (int i=0;i<pieces;i++) {
			byte[] block = new byte[blockSize];
			
			System.arraycopy(rawData, count, block, 0, blockSize);
			
			String encoded = Base64.encodeToString(block, false);
			
			parts.add(encoded);

			count+=blockSize;
		}
		
	  if (partial>0) {
	  	byte[] block = new byte[partial];
	  	
	  	System.arraycopy(rawData, count, block, 0, partial);
	  	
			String encoded = Base64.encodeToString(block, false);
			
			parts.add(encoded);
	  }
		
		debug ("Created " + parts.size() + " parts for this file");

		return parts;
	}	
	
	/**
	 * @param parts
	 * @param aFile
	 * @return
	 */
	public String partsToXML (ArrayList<String> parts,String aFile) {
		StringBuffer formatted=new StringBuffer ();
		
		for (int i=0;i<parts.size();i++) {
		  formatted.append("<file part=\""+(i+1)+"\" of=\""+parts.size()+"\">");
		  formatted.append("<name>"+aFile+"</name>");
		  formatted.append("<data><![CDATA[");
		  formatted.append(parts.get(i));
		  formatted.append("]]></data>");
		  formatted.append("</file>\n");
		}
		
		return (formatted.toString());
	}	
	
	/**
	 * @param files
	 */
	public void transformToXML(ArrayList<String> files, String aBasePath) {
		for (int i = 0; i < files.size(); i++) {
			ArrayList<String> fileParts = encodeFile(aBasePath + "//" + files.get(i));
			String fileInXML=partsToXML (fileParts,files.get(i));
			
			PrintWriter out=null;
			try {
				out = new PrintWriter(aBasePath + "//out//" + files.get(i)+".xml");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			out.println(fileInXML);
			out.close();
		}
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("QRFLogSendTest ()");

		String aBasePath = "D:\\Incoming\\testdata";

		QRFLogSendTest test = new QRFLogSendTest();

		ArrayList<String> files = test.listFiles(aBasePath);

		test.debugFileList(files);

		test.transformToXML(files, aBasePath);
	}

}

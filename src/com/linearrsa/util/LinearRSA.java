package com.linearrsa.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.tomcat.util.codec.binary.Base64;

public class LinearRSA {
	final static Integer keySize=1024;
	private Integer bitlen = 1024;
	private long totalTime;
	BigInteger d,q,e,n,qe;
	static Integer blockSize=1024;
	private static BigInteger cipherTxt;
	public LinearRSA() {
		// TODO Auto-generated constructor stu
	}
	public LinearRSA(BigInteger n,BigInteger d){
		this.n=n;
		this.d=d;
	}
	public BigInteger getN() {
		return n;
	}
	public void setN(BigInteger n) {
		this.n = n;
	}
	public BigInteger getQe() {
		return qe;
	}
	public void setQe(BigInteger qe) {
		this.qe = qe;
	}
	public static BigInteger getCipherTxt() {
		return cipherTxt;
	}
	public static void setCipherTxt(BigInteger cipherTxt) {
		LinearRSA.cipherTxt = cipherTxt;
	}
	
	
	public long getTotalTime() {
		return totalTime;
	}
	public void setTotalTime(long totalTime) {
		this.totalTime = totalTime;
	}
	public LinearRSA(Integer bitlen){
		this.bitlen=bitlen;
		SecureRandom r = new SecureRandom();
		BigInteger a = new BigInteger(bitlen/2, 80, r);
		BigInteger b = new BigInteger(bitlen/2, 80, r);
		BigInteger p = new BigInteger(bitlen/8, 100, r);
		System.out.println("a: "+a+"\nb: "+b+"\np: "+p);
		BigInteger m = (a.multiply(b)).subtract(p);
		e = p.multiply(p).multiply(m).add(b);
		
		d = (p.multiply(m)).add(a);
		BigInteger edP = (e.multiply(d)).subtract(p);
		n = edP.divide(m);
		q = p.modInverse(n);
		qe = q.multiply(e);
	}
	
	public synchronized HashMap<String, BigInteger> getKeys() {
		HashMap<String, BigInteger> hashMap = new HashMap<String, BigInteger>();
		hashMap.put("pubKey", d);
		hashMap.put("privKey", qe);
		hashMap.put("nValue", n);
		return hashMap;
	}
	
	public synchronized BigInteger encrypt(BigInteger message){
		this.cipherTxt=d.multiply(message);
		long startTime = System.currentTimeMillis();
		d.multiply(message).mod(n);
		long endTime = System.currentTimeMillis()+1;
		long totalTime = endTime - startTime;
		this.totalTime=totalTime;
		return d.multiply(message).mod(n);
	}
	
	public synchronized BigInteger encrypt1(BigInteger message){
		return d.multiply(message);
	}
	public synchronized BigInteger decrypt(BigInteger message){
		return q.multiply(e).multiply(message).mod(n);
	}
	public synchronized BigInteger decrypt1(BigInteger message){
		return this.qe.multiply(message).mod(this.n);
	}
	
	public static void main(String[] args) throws NumberFormatException, IOException {
		LinearRSA linearRSA = new LinearRSA(keySize);
		blockSize = keySize / 8;
		choice(linearRSA);
		String text1 = "this is plain text";
		System.out.println("Plain Text: "+text1);
		
		BigInteger plaintext = new BigInteger(text1.getBytes());
		System.out.println("Plain Text: "+plaintext);
 	    BigInteger cipherText = linearRSA.encrypt(plaintext);
 	    System.out.println("Ciphertext: " + cipherTxt);
 	    plaintext = linearRSA.decrypt(cipherTxt);
// 	    System.out.println(q.multiply(e).multiply(d).multiply(plaintext));
 	    String text2 = new String(plaintext.toByteArray());
 	    System.out.println("Plaintext: " + text2);
	}
	
	public static void choice(LinearRSA linearRsa) throws NumberFormatException, IOException{
		System.out.println("-----------select a choice------------\n1. Text encryption\t 2. File encryption\t 3.File decryption");
		Integer choice;
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		choice = Integer.parseInt(br.readLine());
		switch (choice) {
		case 1:
			stringChoice(linearRsa);
			break;
		case 2:
			encryptFileChoice(linearRsa);
			break;
		case 3:
			decryptFileChoice(linearRsa);
			break;
		default:
			break;
		}
	}
	public static void encryptFileChoice(LinearRSA linearRsa) throws IOException{
		System.out.println("file encryption");
		Path path = Paths.get("C:/users/Anurag/desktop/sample.txt");
	    byte[] data = Files.readAllBytes(path);
	    
	    String fileData = new String(data);
	    StringBuilder cipherData = new StringBuilder();
//	    System.out.println(fileData+"\n\n");
	    byte[] cipher = null;
	    System.out.println("------------"+data.length+"=------------");
	    ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
	    for(int i=0;i<data.length;i+=blockSize){
	    	byte[] part;
	    	if(i>((data.length-1)-blockSize))
	    		part = Arrays.copyOfRange(data, i, data.length);
	    	else
	    		part = Arrays.copyOfRange(data, i, i+blockSize);
	    	
	    	String partData = new String(part);
//	    	System.out.println(partData+"\n");
	    	BigInteger plaintext = new BigInteger(part);
			
			BigInteger ciphertext = linearRsa.encrypt1(plaintext);
			System.out.println("Ciphertext: " + ciphertext);
			System.out.println(log10(ciphertext));
			byte[]   bytesEncoded = Base64.encodeBase64(ciphertext.toString().getBytes());
			System.out.println(bytesEncoded +"  \n"+bytesEncoded.length);
			cipherData.append(ciphertext.toString());
			cipherData.append("|");
			outputStream.write(part);
			plaintext = linearRsa.decrypt(ciphertext);
			String text2 = new String(plaintext.toByteArray());
			System.out.println("Plaintext: " + text2+"\n");
	    }
	    File f = new File("C:/users/Anurag/desktop/L-cipher.txt");
	    FileUtils.writeStringToFile(f, cipherData.toString());
	    choice(linearRsa);
	    //FileUtils.writeStringToFile(new File("C:/users/Anurag/desktop/cipher.txt"), cipherData, StandardCharsets.UTF_8);
	    //	    FileUtils.writeByteArrayToFile(new File("C:/users/Anurag/desktop/cipher.txt"), cipher);
	}
	public void encryptFile(String storagePath, String fileName) throws IOException{
		System.out.println("file encryption");
		Path path = Paths.get(storagePath+fileName);
		System.out.println(path.toUri());
		//Path p1 = Paths.get(path.toUri()+"/"+fileName);
	    byte[] data = Files.readAllBytes(path);
	    
	    String fileData = new String(data);
	    StringBuilder cipherData = new StringBuilder();
//	    System.out.println(fileData+"\n\n");
	    byte[] cipher = null;
	    System.out.println("------------"+data.length+"=------------");
	    ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
	    for(int i=0;i<data.length;i+=blockSize){
	    	byte[] part;
	    	if(i>((data.length-1)-blockSize))
	    		part = Arrays.copyOfRange(data, i, data.length);
	    	else
	    		part = Arrays.copyOfRange(data, i, i+blockSize);
	    	
	    	String partData = new String(part);
//	    	System.out.println(partData+"\n");
	    	BigInteger plaintext = new BigInteger(part);
			
			BigInteger ciphertext = encrypt1(plaintext);
			System.out.println("Ciphertext: " + ciphertext);
			System.out.println(log10(ciphertext));
			byte[]   bytesEncoded = Base64.encodeBase64(ciphertext.toString().getBytes());
			System.out.println(bytesEncoded +"  \n"+bytesEncoded.length);
			cipherData.append(ciphertext.toString());
			cipherData.append("|");
			outputStream.write(part);
//			plaintext = decrypt(ciphertext);
//			String text2 = new String(plaintext.toByteArray());
//			System.out.println("Plaintext: " + text2+"\n");
	    }
	    File f = new File(storagePath+"cipher-"+fileName);
	    FileUtils.writeStringToFile(f, cipherData.toString());
	    //choice(linearRsa);
	    //FileUtils.writeStringToFile(new File("C:/users/Anurag/desktop/cipher.txt"), cipherData, StandardCharsets.UTF_8);
	    //	    FileUtils.writeByteArrayToFile(new File("C:/users/Anurag/desktop/cipher.txt"), cipher);
	}
	
	public static void decryptFileChoice(LinearRSA linearRsa) throws IOException{
		String cipherData = FileUtils.readFileToString(new File("C:/users/Anurag/desktop/L-cipher.txt"));
		String[] cipherParts = cipherData.split("\\|");
		StringBuilder pText = new StringBuilder();
		for (int i = 0; i < cipherParts.length; i++) {
			//System.out.println(cipherParts[i]+"\n");
			BigInteger c = new BigInteger(cipherParts[i]);
			System.out.println(c);
			BigInteger plaintext = linearRsa.decrypt(c);
			pText.append(new String(plaintext.toByteArray()));
//			System.out.println("Plaintext: " + pText);
		}
		FileUtils.writeStringToFile(new File("C:/users/Anurag/desktop/L-plaintext.txt"), pText.toString());
		choice(linearRsa);
	}
	public static void stringChoice(LinearRSA linearRsa) throws NumberFormatException, IOException {
		List<BigInteger> cipherTxtblocks = new ArrayList<BigInteger>();
		Integer blockSize = keySize / 8;
		String text1=null;
		text1 = "hello welcome hello welcom hello welcomehello welcomehello welcomehello welcomeehello welcome"
				+ "hello welcome hello welcom hello welcomehello welcomehello welcomehello welcomeehello welcome"
				+ "hello welcome hello welcom hello welcomehello welcomehello welcomehello welcomeehello welcome";
//		System.out.println("Enter some text");
//		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
//		text1 = br.readLine();
		
		System.out.println("Plaintext: " + text1);

		String newStr = text1.replaceAll("(.{" + blockSize.toString() + "})", "$1|");
		System.out.println(newStr);
		String[] newStrings = newStr.split("\\|");
		List<BigInteger> cipherTextblocks = new ArrayList<BigInteger>();
		for (int i = 0; i < newStrings.length; i++) {
			cipherTextblocks.add(i, linearRsa.encrypt(new BigInteger(newStrings[i].getBytes())));
			cipherTxtblocks.add(i, linearRsa.encrypt1(new BigInteger(newStrings[i].getBytes())));
			System.out.println(cipherTextblocks.get(i) + "\n");
		}

		String decryptedText = "";
		System.out.println("---------------decryptedText---------------\n ");
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < cipherTextblocks.size(); i++) {
			sb.append(new String(linearRsa.decrypt(cipherTxtblocks.get(i)).toByteArray()));
		}

		System.out.println(sb + "\n" + text1);
		// BigInteger plaintext = new BigInteger(text1.getBytes());
		//
		// BigInteger ciphertext = linearRsa.encrypt(plaintext);
		// System.out.println("Ciphertext: " + ciphertext);
		// plaintext = linearRsa.decrypt(ciphertext);
		//
		// String text2 = new String(plaintext.toByteArray());
		// System.out.println("Plaintext: " + text2);
		
		choice(linearRsa);
	}
	private static int log10(BigInteger huge) {
	    int digits = 0;
	    int bits = huge.bitLength();
	    // Serious reductions.
	    while (bits > 4) {
	      // 4 > log[2](10) so we should not reduce it too far.
	      int reduce = bits / 4;
	      // Divide by 10^reduce
	      huge = huge.divide(BigInteger.TEN.pow(reduce));
	      // Removed that many decimal digits.
	      digits += reduce;
	      // Recalculate bitLength
	      bits = huge.bitLength();
	    }
	    // Now 4 bits or less - add 1 if necessary.
	    if ( huge.intValue() > 9 ) {
	      digits += 1;
	    }
	    return digits;
	  }
}

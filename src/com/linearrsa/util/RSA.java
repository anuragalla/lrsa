package com.linearrsa.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.sun.xml.internal.ws.util.ByteArrayBuffer;

public class RSA {
	private BigInteger n, d, e;
	private long totalTime;
	final static Integer keySize = 1024;
	private Integer bitlen = 1024;
	static Integer blockSize;
	public RSA(BigInteger newn, BigInteger newe) {
		n = newn;
		e = newe;
	}
	
	
	public long getTotalTime() {
		return totalTime;
	}


	public void setTotalTime(long totalTime) {
		this.totalTime = totalTime;
	}


	public BigInteger getD() {
		return d;
	}


	public void setD(BigInteger d) {
		this.d = d;
	}


	public Integer getBitlen() {
		return bitlen;
	}


	public void setBitlen(Integer bitlen) {
		this.bitlen = bitlen;
	}


	public void setN(BigInteger n) {
		this.n = n;
	}


	public void setE(BigInteger e) {
		this.e = e;
	}


	// Constructor that can both encrypt and decrypt
	public RSA(int bits) {
		bitlen = bits;
		SecureRandom r = new SecureRandom();
		BigInteger p = new BigInteger(bitlen / 2, 100, r);

		System.out.println("prime p" + p);
		BigInteger q = new BigInteger(bitlen / 2, 100, r);
		n = p.multiply(q);
		BigInteger m = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));
		// e = new BigInteger("3");
		e = BigInteger.probablePrime(bitlen / 4, r);
		while (m.gcd(e).compareTo(BigInteger.ONE) > 0 && e.compareTo(m) < 0) {
			e.add(BigInteger.ONE);
		}
		d = e.modInverse(m);
		System.out.println("PubKey e: " + e + "\n Private Key d: " + d);
		// while (m.gcd(e).intValue() > 1) {
		// e = e.add(new BigInteger("2"));
		// }
		// d = e.modInverse(m);
	}

	public RSA() {
		// TODO Auto-generated constructor stub
	}

	public synchronized HashMap<String, BigInteger> getKeys() {
		HashMap<String, BigInteger> hashMap = new HashMap<String, BigInteger>();
		hashMap.put("pubKey", e);
		hashMap.put("privKey", d);
		hashMap.put("nValue", n);
		return hashMap;
	}

	// Encrypt the given plaintext message
	public synchronized String encrypt(String message) {
		return (new BigInteger(message.getBytes())).modPow(e, n).toString();
	}

	// Encrypt the given plaintext message
	public synchronized BigInteger encrypt(BigInteger message) {
		long startTime = System.currentTimeMillis();
		message.modPow(e, n);
		long endTime = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		this.totalTime=totalTime;
		return message.modPow(e, n);
	}

	// Decrypt the given ciphertext message
	public synchronized String decrypt(String message) {
		return new String((new BigInteger(message)).modPow(d, n).toByteArray());
	}

	// Decrypt the given ciphertext message
	public synchronized BigInteger decrypt(BigInteger message) {
		return message.modPow(d, n);
	}

	// Generate a new public and private key set.
	public synchronized void generateKeys() {
		SecureRandom r = new SecureRandom();
		BigInteger p = new BigInteger(bitlen / 2, 100, r);
		BigInteger q = new BigInteger(bitlen / 2, 100, r);
		n = p.multiply(q);
		BigInteger m = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));
		e = new BigInteger("3");
		while (m.gcd(e).intValue() > 1) {
			e = e.add(new BigInteger("2"));
		}
		d = e.modInverse(m);
	}

	/** Return the modulus. */
	public synchronized BigInteger getN() {
		return n;
	}

	/** Return the public key. */
	public synchronized BigInteger getE() {
		return e;
	}

	public static void main(String[] args) throws IOException {
		RSA rsa = new RSA(keySize);
		blockSize = keySize / 8;
		choice(rsa);
		

	}
	public static void choice(RSA rsa) throws NumberFormatException, IOException{
		System.out.println("-----------select a choice------------\n1. Text encryption\t 2. File encryption\t 3.File decryption");
		Integer choice;
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		choice = Integer.parseInt(br.readLine());
		switch (choice) {
		case 1:
			stringChoice(rsa);
			break;
		case 2:
			encryptFileChoice(rsa);
			break;
		case 3:
			decryptFileChoice(rsa);
			break;
		default:
			break;
		}
	}
	public static void encryptFileChoice(RSA rsa) throws IOException{
		System.out.println("file encryption");
		Path path = Paths.get("C:/users/Anurag/desktop/test.txt");
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
			
			BigInteger ciphertext = rsa.encrypt(plaintext);
			System.out.println("Ciphertext: " + ciphertext);
			cipherData.append(ciphertext.toString());
			cipherData.append("|");
			outputStream.write(part);
			plaintext = rsa.decrypt(ciphertext);
			Integer digits = log10(ciphertext);
			String text2 = new String(plaintext.toByteArray());
			System.out.println("Plaintext: " + text2+"\n"+digits);
	    }
	    File f = new File("C:/users/Anurag/desktop/ciphertest.txt");
	    FileUtils.writeStringToFile(f, cipherData.toString());
	    choice(rsa);
	    //FileUtils.writeStringToFile(new File("C:/users/Anurag/desktop/cipher.txt"), cipherData, StandardCharsets.UTF_8);
	    //	    FileUtils.writeByteArrayToFile(new File("C:/users/Anurag/desktop/cipher.txt"), cipher);
	}
	
	public static void decryptFileChoice(RSA rsa) throws IOException{
		String cipherData = FileUtils.readFileToString(new File("C:/users/Anurag/desktop/ciphertest.txt"));
		String[] cipherParts = cipherData.split("\\|");
		StringBuilder pText = new StringBuilder();
		for (int i = 0; i < cipherParts.length; i++) {
			//System.out.println(cipherParts[i]+"\n");
			BigInteger c = new BigInteger(cipherParts[i]);
			System.out.println(c);
			BigInteger plaintext = rsa.decrypt(c);
			pText.append(new String(plaintext.toByteArray()));
//			System.out.println("Plaintext: " + text2);
		}
		FileUtils.writeStringToFile(new File("C:/users/Anurag/desktop/plaintexttest.txt"), pText.toString());
		choice(rsa);
	}
	public static void stringChoice(RSA rsa) throws NumberFormatException, IOException {
		Integer blockSize = keySize / 8;
		String text1 = "hello welcome hello welcom hello welcomehello welcomehello welcomehello welcomeehello welcome"
				+ "hello welcome hellhello welcomehello welcomehello welcomehello welcomeehello welcome"
				+ "hellowelcome hello welcom hello welcomehello welcomehello wcomehello welcomeehello welcome";
		System.out.println("Plaintext: " + text1);

		String newStr = text1.replaceAll("(.{" + blockSize.toString() + "})", "$1|");
		System.out.println(newStr);
		String[] newStrings = newStr.split("\\|");
		List<BigInteger> cipherTextblocks = new ArrayList<BigInteger>();
		for (int i = 0; i < newStrings.length; i++) {
			System.out.println(newStrings[i].getBytes().length);
			cipherTextblocks.add(i, rsa.encrypt(new BigInteger(newStrings[i].getBytes())));
			System.out.println(cipherTextblocks.get(i)+"\t:"+ log10(cipherTextblocks.get(i)) + "\n");
		}

		String decryptedText = "";
		System.out.println("---------------decryptedText---------------\n ");
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < cipherTextblocks.size(); i++) {
			sb.append(new String(rsa.decrypt(cipherTextblocks.get(i)).toByteArray()));
		}

		System.out.println(sb + "\n" + text1);
		// BigInteger plaintext = new BigInteger(text1.getBytes());
		//
		// BigInteger ciphertext = rsa.encrypt(plaintext);
		// System.out.println("Ciphertext: " + ciphertext);
		// plaintext = rsa.decrypt(ciphertext);
		//
		// String text2 = new String(plaintext.toByteArray());
		// System.out.println("Plaintext: " + text2);
		
		choice(rsa);
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
	
	static byte[] concatenateByteArrays(byte[] a, byte[] b) {
	    byte[] result = new byte[a.length + b.length]; 
	    System.arraycopy(a, 0, result, 0, a.length); 
	    System.arraycopy(b, 0, result, a.length, b.length); 
	    return result;
	} 
}
package com.linearrsa.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;

public class TestLRSA {

	public static boolean prime(BigInteger n) {
		BigInteger a = new BigInteger("2");
		BigInteger b = n.divide(a);
		for (; a.compareTo(b) <= 0; a = a.add(BigInteger.ONE)) {
			if (n.mod(a).equals(BigInteger.ZERO)) {
				return false;
			}
		}
		return true;
	}

	public static void main(String[] args) {
		try{
		BigInteger a, b, P;
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		// Entering first prime number
		while (true) {
			System.out.println("Enter the first prime number P:");
			P = new BigInteger(br.readLine());
			if (prime(P) == false) {
				System.out.println("Not a prime.");
			} else {
				break;
			}
		}
		while (true) {
			System.out.println("Enter the value of a and b:");
			a = new BigInteger(br.readLine());
			b = new BigInteger(br.readLine());
			// int t2=b.intValue();
			BigInteger t = a.multiply(b);
			int y = t.intValue();
			int t3 = P.intValue();
			// int y=t1.multiply(t2);
			if (t3 < y) {
				break;
			} else {
				System.out.println("no is not correct:");
			}
		}

		BigInteger M = a.multiply(b).subtract(P);
		System.out.println("value of M:" + M);
		BigInteger e = P.multiply(P).multiply(M).add(a);
		System.out.println("value of e:" + e);
		BigInteger d = P.multiply(M).add(b);
		System.out.println("value of d:" + d);
		// compute ed-P
		BigInteger X = e.multiply(d).subtract(P);
		System.out.println("value of X:" + X);
		// compute the value of n
		BigInteger n = X.divide(M);
		System.out.println("value of n:" + n);
		// find unique Q
		BigInteger Q = P.modInverse(n);
		System.out.println("value of Q:" + Q);
		// input massage
		System.out.println("input massage is:");
		BigInteger m = new BigInteger(br.readLine());
		// encryption
		BigInteger E = d.multiply(m).mod(n);
		System.out.println("encrypted massage is:" + E);
		// decryption
		BigInteger qedm = Q.multiply(e).multiply(d).multiply(m);
		System.out.println("qdem value is:" + qedm);
		BigInteger D = Q.multiply(e).multiply(d).multiply(m).mod(n);
		System.out.println("decrypted massage is:" + D);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
		

}

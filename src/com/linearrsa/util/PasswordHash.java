package com.linearrsa.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import com.linearrsa.entity.Users;

public class PasswordHash {

	public String hashToSHA1(String password) throws NoSuchAlgorithmException {
		// String salt = getSalt();

		password = get_SHA_384_SecurePassword(password, "churny");
		// System.out.println(securePassword);

		return password;
	}

	private String get_SHA_1_SecurePassword(String passwordToHash, String salt) {
		return null;

	}

	private String get_SHA_256_SecurePassword(String passwordToHash, String salt) {
		// Use MessageDigest md = MessageDigest.getInstance("SHA-256");
		return null;
	}

	private static String get_SHA_384_SecurePassword(String passwordToHash, String salt) {
		// Use MessageDigest md = MessageDigest.getInstance("SHA-384");
		String generatedPassword = null;
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-384");
			md.update(salt.getBytes());
			byte[] bytes = md.digest(passwordToHash.getBytes());
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < bytes.length; i++) {
				sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
			}
			generatedPassword = sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return generatedPassword;
	}

	private String get_SHA_512_SecurePassword(String passwordToHash, String salt) {
		// Use MessageDigest md = MessageDigest.getInstance("SHA-512");
		String generatedPassword = null;
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-512");
			md.update(salt.getBytes());
			byte[] bytes = md.digest(passwordToHash.getBytes());
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < bytes.length; i++) {
				sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
			}
			generatedPassword = sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return generatedPassword;
	}

	// Add salt
	private static String getSalt() throws NoSuchAlgorithmException {
		SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
		byte[] salt = new byte[16];
		sr.nextBytes(salt);
		return salt.toString();
	}
}
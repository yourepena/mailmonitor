package br.com.bluesi.mailmonitor;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class EncriptaDecriptaDES {

	public static final String encodedKey = "0EY0c8Rtodo=";

	public static void main(String[] argv) {
//		byte[] en = encryptString("1720@OpsBlue");
		byte[] en = encryptString("Cubano15@513");
		System.out.println("Senha encriptada: " + en);
		System.out.println("Senha encriptada e String: " + new String(en));
		
		byte[] re  = java.util.Base64.getEncoder().encode(en);
		
		System.out.println("Senha encriptada Base64: " + re);
		String stringNova = new String(re);
		System.out.println("String pra guardar " + stringNova);
		
		
		System.out.println("Senha encriptada sem base 64 " +Base64.getDecoder().decode(stringNova) );
		
		
		System.out.println("Senha decriptada: " + decryptString(stringNova));
		

	}

	public static byte[] encryptString(String text) {
		try {

			byte[] encodedKeyBytes = java.util.Base64.getDecoder().decode(encodedKey);

			SecretKey chaveDES = new SecretKeySpec(encodedKeyBytes, 0, encodedKeyBytes.length, "DES");

			Cipher cifraDES;

			// Cria a cifra
			cifraDES = Cipher.getInstance("DES/ECB/PKCS5Padding");

			// Inicializa a cifra para o processo de encriptação
			cifraDES.init(Cipher.ENCRYPT_MODE, chaveDES);

			// Texto puro
			byte[] textoPuro = text.getBytes();

			// Texto encriptado
			byte[] textoEncriptado = cifraDES.doFinal(textoPuro);

			return textoEncriptado;

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static String decryptString(String textEncrypt) {
		
		

		byte[] encodedKeyBytes = java.util.Base64.getDecoder().decode(encodedKey);

		SecretKey chaveDES = new SecretKeySpec(encodedKeyBytes, 0, encodedKeyBytes.length, "DES");

		Cipher cifraDES;

		// Cria a cifra
		try {
			cifraDES = Cipher.getInstance("DES/ECB/PKCS5Padding");

			// Inicializa a cifra também para o processo de decriptação
			cifraDES.init(Cipher.DECRYPT_MODE, chaveDES);

			// Decriptografa o texto
			byte[] textoDecriptografado = cifraDES.doFinal(Base64.getDecoder().decode(textEncrypt));

			return new String(textoDecriptografado);

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		
		return null;
	}

}
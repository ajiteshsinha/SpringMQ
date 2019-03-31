package com.ajitesh.jwt;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Base64;

import com.ajitesh.jwt.model.JWTClaim;
import com.google.gson.Gson;

public class JwtBearerToken {

	public static void main(String[] args) throws Exception {
		
		
		Path path_jks = Paths.get("D:\\keytool_jwt\\oauth.jks");
		Path path = Paths.get("D:\\keytool_jwt\\pkcs8_key.txt");

	
		byte[] bytes = Files.readAllBytes(path);

		KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
		keystore.load(Files.newInputStream(path_jks), "password".toCharArray());
		String alias = "alias";
		Key key = keystore.getKey(alias, "password".toCharArray());
		System.out.println(key instanceof PrivateKey);
		System.out.println(key.getFormat());
		
		
			PrivateKey privateKey = (PrivateKey)key;
			/*String outFile = ...;
			out = new FileOutputStream(outFile + ".key");
			out.write(pvt.getEncoded());
			out.close();*/
			String token = sign("hello", privateKey);
			System.out.println("sign token :-> "  +token);
		
		/*	
		
		String realPK = new String(bytes).replaceAll("-----BEGIN CERTIFICATE-----", "")
										 .replaceAll("-----END CERTIFICATE-----", "")
										 .replaceAll("-----BEGIN PRIVATE KEY-----", "")
										 .replaceAll("-----END PRIVATE KEY-----", "")
										 .replaceAll("\n", "").trim();*/
	//	System.out.println(realPK);
		  PKCS8EncodedKeySpec ks = new PKCS8EncodedKeySpec(bytes);
		  System.out.println("format :" + ks.getFormat());
		  KeyFactory kf = KeyFactory.getInstance("RSA"); 
		  PrivateKey privateKey2 =  kf.generatePrivate(ks);
		  System.out.println(privateKey2);
		  String token2 = sign("hello", privateKey);
		  System.out.println("sign token2 :-> "  +token2);
		  
		  String claimSet = getJWTClaim();
		  System.out.println("claim set :" + claimSet);
		  String claimSetEncod = Base64.getUrlEncoder().encodeToString(claimSet
				  										.getBytes(Charset.forName("UTF-8")));
		  
		  String algoHeader = "{\"alg\":\"RS256\"}";
		  String algoHeaderEncod = Base64.getUrlEncoder().encodeToString(algoHeader
														.getBytes(Charset.forName("UTF-8")));
		  
		  String claimPayload = algoHeaderEncod + "." + claimSetEncod;
		  System.out.println("claimPayload : " + claimPayload);
		  String signedPayLoad = sign(claimPayload, privateKey2);
		  String assertion = claimPayload + "." + signedPayLoad;
		  System.out.println("Assertion :-> " + assertion);
		  

	}

	private static String sign(String input, PrivateKey key) throws Exception {

		Signature privateSignature = Signature.getInstance("SHA256withRSA");
		privateSignature.initSign(key);
		privateSignature.update(input.getBytes("UTF-8"));
		byte[] s = privateSignature.sign();
		return Base64.getEncoder().encodeToString(s);

	}
	
	private static String getJWTClaim() {
		JWTClaim claim = new JWTClaim();
		ZonedDateTime zdtIat0 = ZonedDateTime.now();
		ZonedDateTime zdtIat = ZonedDateTime.now(ZoneId.of("UTC"));
		ZonedDateTime zdtExp = zdtIat.plus(Duration.ofMinutes(10));
		LocalDateTime ldt = LocalDateTime.now();
		System.out.println("ldtIat : "  + LocalDateTime.now());
		System.out.println("zdtIat : " + zdtIat0);
		System.out.println("zdtIat : " + zdtIat);
		System.out.println("zdtExp : " + zdtExp);
		System.out.println("UTC Sec : " + zdtIat0.toEpochSecond());
		System.out.println("UTC Sec : " + zdtIat.toEpochSecond());
		System.out.println("Loc Sec : " + ldt.toEpochSecond(ZoneOffset.UTC));
		
		
//		claim.setIss("e0bc4614-a056-400d-9c5a-42b2a14de74a");
		claim.setIss("9214cc58-2561-499b-8c68-f91ba587efd4");
		claim.setAud("https://******oauth/token");
		claim.setExp(String.valueOf(zdtExp.toEpochSecond()));
		claim.setIat(String.valueOf(zdtIat.toEpochSecond()));
		Gson gson = new Gson();
		return gson.toJson(claim);
		
	}

}

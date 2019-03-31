package com.ajitesh.jwt.model;

public class JWTClaim {
	
	
	private String iss;
	private String aud;
	private String exp;
	private String iat;
	public String getIss() {
		return iss;
	}
	public void setIss(String iss) {
		this.iss = iss;
	}
	public String getAud() {
		return aud;
	}
	public void setAud(String aud) {
		this.aud = aud;
	}
	public String getExp() {
		return exp;
	}
	public void setExp(String exp) {
		this.exp = exp;
	}
	public String getIat() {
		return iat;
	}
	public void setIat(String iat) {
		this.iat = iat;
	}
	

	
}

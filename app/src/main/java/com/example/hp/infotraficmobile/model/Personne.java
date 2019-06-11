package com.example.hp.infotraficmobile.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class Personne implements Serializable{

	@SerializedName("id")
	private long id;
	@SerializedName("nom")
	private String nom;
	@SerializedName("prenom")
	private String prenom;
	@SerializedName("email")
	private String email;
	@SerializedName("telephone")
	private String telephone;
	@SerializedName("login")
	private String login;
	@SerializedName("password")
	private String password;
	@SerializedName("code")
	private String code;

	@SerializedName("activation")
	private boolean activation;

	private boolean enabled;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Personne() {
		super();
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public boolean isActivation() {
		return activation;
	}

	public void setActivation(boolean activation) {
		this.activation = activation;
	}

	@Override
	public String toString() {
		return "Personne{" +
				"id=" + id +
				", nom='" + nom + '\'' +
				", prenom='" + prenom + '\'' +
				", email='" + email + '\'' +
				", telephone='" + telephone + '\'' +
				", login='" + login + '\'' +
				", password='" + password + '\'' +
				", code='" + code + '\'' +
				", enabled=" + enabled +
				'}';
	}
}

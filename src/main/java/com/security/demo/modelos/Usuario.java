package com.security.demo.modelos;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public class Usuario {
	
	private @Id @GeneratedValue(strategy = GenerationType.IDENTITY ) Long idUsuario;
	private @NotNull String usuario;
	private @NotNull(message = "É necessario a Senha") String senha;
	
	public Usuario() {
		
	}

	public Usuario(@NotNull String usuario, @NotNull String senha) {
		this.usuario = usuario;
		this.senha = senha;
	}
	
	public Long getIdUsuario() {
		return idUsuario;
	}
	public void setIdUsuario(Long idUsuario) {
		this.idUsuario = idUsuario;
	}
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
}

package com.security.demo.controladores;

import java.nio.charset.Charset;
import java.util.List;

import javax.validation.Valid;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.security.demo.modelos.Usuario;
import com.security.demo.modelos.utilitarios.UsuarioLogin;
import com.security.demo.repositorios.UsuarioRepositorio;

@RestController
@RequestMapping("/api/v1/usuario")
public class UsuarioControlador {

	private @Autowired UsuarioRepositorio repositorio;
	
	@GetMapping("/todos")
	public ResponseEntity<List<Usuario>> pegarTodos(){
		List<Usuario> listaDeUsuario = repositorio.findAll();
		if (!listaDeUsuario.isEmpty()) {
			return ResponseEntity.status(200).body(listaDeUsuario);
		} else {			
			return ResponseEntity.status(204).build();
		}
	}
	
	@PostMapping("/salvar")
	public ResponseEntity<Object> salvarUsuario(@Valid @RequestBody Usuario novoUsuario){
		return repositorio.findByUsuario(novoUsuario.getUsuario())
				.map(usuarioExistente -> {
					return ResponseEntity.status(400).build();
				})
				.orElseGet(() -> {
					BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
					String senhaCriptografada = encoder.encode(novoUsuario.getSenha());
					novoUsuario.setSenha(senhaCriptografada);
					return ResponseEntity.status(201).body(repositorio.save(novoUsuario));
				});
	}
	
	@PostMapping("/logar")
	public ResponseEntity<?> logarUsuario(@RequestBody UsuarioLogin usuarioParaAutenticar){
		return repositorio.findByUsuario(usuarioParaAutenticar.getUsuario())
				.map(usuarioExistente -> {
					BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
					
					if (encoder.matches(usuarioParaAutenticar.getSenha(), usuarioExistente.getSenha())) {
						String estruturaBasic = usuarioParaAutenticar.getUsuario() + ":" + usuarioParaAutenticar.getSenha();
						byte[] autorizacaoBase64 = Base64.encodeBase64(estruturaBasic.getBytes(Charset.forName("US-ASCII")));
						String autorizacaoHeader = "Basic " + new String(autorizacaoBase64);
						
						usuarioParaAutenticar.setToken(autorizacaoHeader);
						usuarioParaAutenticar.setUsuario(usuarioExistente.getUsuario());
						usuarioParaAutenticar.setSenha(usuarioExistente.getSenha());
						return ResponseEntity.status(200).body(usuarioParaAutenticar);
					} else {
						return ResponseEntity.status(401).build();
					}
				})
				.orElse(ResponseEntity.status(401).build());
	}
	
	@PutMapping("/alterar")
	public ResponseEntity<?> alterarUsuario(@Valid @RequestBody Usuario usuarioParaAtualizar){
		return repositorio.findByUsuario(usuarioParaAtualizar.getUsuario())
				.map(usuarioExistente -> {
					BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
					String senhaCriptografada = encoder.encode(usuarioParaAtualizar.getSenha());
					usuarioExistente.setSenha(senhaCriptografada);
					return ResponseEntity.status(201).body(repositorio.save(usuarioExistente));
				})
				.orElse(ResponseEntity.status(400).build());
	}
}

package com.security.demo.repositorio;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import com.security.demo.modelos.Usuario;
import com.security.demo.repositorios.UsuarioRepositorio;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UsuarioRepositorioTest {

	@Autowired
	private UsuarioRepositorio repositorio;
	
	@BeforeAll
	void start() {
		Usuario usuario = new Usuario("VagnerBoaz", "134652");
		if(repositorio.findByUsuario(usuario.getUsuario())!=null)
			repositorio.save(usuario);
		
		usuario = new Usuario("LucasBoaz", "134652");
		if(repositorio.findByUsuario(usuario.getUsuario())!=null)
			repositorio.save(usuario);
		
		usuario = new Usuario("MarceloBoaz", "134652");
		if(repositorio.findByUsuario(usuario.getUsuario())!=null)
			repositorio.save(usuario);
	}
	
	@Test
	public void findByUsuarioRetornaUsuario() throws Exception {

		Usuario usuario = repositorio.findByUsuario("VagnerBoaz").get();
		assertTrue(usuario.getUsuario().equals("VagnerBoaz"));
	}
	
	@Test
	public void findAllByUsuarioContainingIgnoreCaseRetornaTresContato() {

		List<Usuario> listaDeUsuarios = repositorio.findAllByUsuarioContainingIgnoreCase("Boaz");
		assertEquals(3, listaDeUsuarios.size());
	}
	
	@AfterAll
	public void end() {
		repositorio.deleteAll();
	}

}

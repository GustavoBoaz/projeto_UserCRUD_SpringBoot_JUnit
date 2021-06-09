package com.security.demo.seguranca;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.security.demo.modelos.Usuario;
import com.security.demo.repositorios.UsuarioRepositorio;

@Service
public class UserDetailServiceImplements implements UserDetailsService {

	private @Autowired UsuarioRepositorio repositorio;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		Optional<Usuario> usuario = repositorio.findByUsuario(username);
		
		if (usuario.isPresent()) {
			return new UserDatailImplements(usuario.get());
		} else {
			throw new UsernameNotFoundException(username + " not found.");
		}
	}

}

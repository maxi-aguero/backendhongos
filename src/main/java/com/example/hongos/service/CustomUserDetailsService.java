package com.example.hongos.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.hongos.model.Rol;
import com.example.hongos.repository.UsuarioRepository;


@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    	com.example.hongos.model.User usuario = usuarioRepository.findByUsername(username);
    	System.out.println(usuario);
    	
    	if (usuario == null) {
            throw new UsernameNotFoundException("Usuario no encontrado");
        }

     // Encriptar la contrasenia si no esta encriptada
        if (!usuario.getPassword().startsWith("$2a$")) { // Prefijo estandar para contrasenias encriptadas con BCrypt
            usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
            usuarioRepository.save(usuario); // Guardar la contraseniaa encriptada en la base de datos
            System.out.println("paso 1");

        }

       
        Set<GrantedAuthority> authorities = new HashSet<>();
        Rol rol = usuario.getRoles();
        authorities.add(new SimpleGrantedAuthority(rol.getName()));
      
        
       

        return org.springframework.security.core.userdetails.User.withUsername(usuario.getUsername())
                .password(usuario.getPassword())
                .authorities(authorities)

                   
                   .build();
    }
}


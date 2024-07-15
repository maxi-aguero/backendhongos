package com.example.hongos.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.hongos.model.User;

public interface UsuarioRepository extends JpaRepository<User, Long> {
	User findByUsername(String id);

}




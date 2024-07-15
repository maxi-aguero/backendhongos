package com.example.hongos.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.hongos.model.User;
public interface UsuarioRegistradoRepository extends JpaRepository<User, Long> {

}

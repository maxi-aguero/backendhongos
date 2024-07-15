package com.example.hongos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.hongos.model.Rol;

public interface RolRepository extends JpaRepository<Rol,Long> {
    Rol getById(Long id);

}

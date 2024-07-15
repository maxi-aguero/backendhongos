package com.example.hongos.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.hongos.model.Rol;
import com.example.hongos.repository.RolRepository;

@Component
public class RolData implements CommandLineRunner {

    @Autowired
    private RolRepository rolRepository;//repositorio para la entidad Rol
       

    @Override
    public void run(String... args) throws Exception {
        // Verifica si la tabla 'rol' esta vacia
        if (rolRepository.findAll().isEmpty()) {
            // Crea instancias de los roles 'USER' y 'ADMIN'
            Rol userRole = new Rol();
            userRole.setName("USER");
            Rol adminRole = new Rol();
            adminRole.setName("ADMIN");

            // Guarda las instancias en la base de datos
            rolRepository.save(userRole);
            rolRepository.save(adminRole);
            
        }
    }
}

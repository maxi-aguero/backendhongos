package com.example.hongos.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.hongos.model.User;
import com.example.hongos.repository.UsuarioRegistradoRepository;

import java.util.List;
import java.util.Optional;

@Service
public class usuarioService {

    @Autowired
    private UsuarioRegistradoRepository userRepository;   

    public void createUser(User user) {
    	  	
    	List<User> listadeusuarios = userRepository.findAll();
        boolean enc = false;
        int i = 0;
        String elem = user.getUsername();

        while (!enc && i < listadeusuarios.size()) {
            User item = listadeusuarios.get(i);
            if (item.getUsername().equals(elem)) {
                enc = true;
            }
            i++;
        }

        if (!enc) {
            userRepository.save(user);
        } else {
            throw new IllegalArgumentException("El email del usuario ya esta en uso.");
        }
    }
    
    
    public User BuscarUser(String miemail) {
    	   	

    	
    	List<User> listadeusuarios = userRepository.findAll();
        boolean enc = false;
        int i = 0;
        User toReturn=null;
        
        while (!enc && i < listadeusuarios.size()) {
        	toReturn = listadeusuarios.get(i);
            if (toReturn.getUsername().equals(miemail)) {
                enc = true;
            }
            i++;
        }

        if (!enc) {
        	toReturn=null;
        } 
        return toReturn;
    }
    
    public User buscarusario_en_bd(Long userid) {
    	Optional<User> optionaluser = userRepository.findById(userid);
    	//asumo que esta el usuario en la base de datos
	    return optionaluser.get();
    }
    
    public void guarrdar_usuario(User user) {
    	userRepository.save(user);

    }

    
    
    
   

  }
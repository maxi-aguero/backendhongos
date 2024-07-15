package com.example.hongos.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.hongos.model.Rol;
import com.example.hongos.repository.RolRepository;

@Service
public class RolService {
	@Autowired
    private RolRepository rolRepository;
	
	//Busca y devuelve el Rol cuyo nombre es pasado por parametro
    public Rol buscarRolUSER(String nombreRol) {
    	List<Rol> conjuntoderoles = rolRepository.findAll();
    	boolean enc=false;
    	int indice=0;
    	Rol elem=null;
    	while(!enc && indice<conjuntoderoles.size()) {
    		elem=conjuntoderoles.get(indice);
    		if (elem.getName().equals(nombreRol))
    			enc=true;
    		indice++;
    	}
    	if (!enc) {elem=null;}
    	return elem;
    	
    }
    
   



}

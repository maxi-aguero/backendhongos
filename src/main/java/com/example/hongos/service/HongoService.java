package com.example.hongos.service;

import com.example.hongos.model.Hongo;
import com.example.hongos.repository.HongoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HongoService {

    @Autowired
    private HongoRepository hongoRepository;

    public List<Hongo> getAllHongos() {
        return hongoRepository.findAll();
    }

    public Hongo getHongoById(Long id) {
        return hongoRepository.findById(id).orElse(null);
    }

    public Hongo createHongo(Hongo hongo) {
        
    	return hongoRepository.save(hongo);
        
    }

    public Hongo updateHongo(Long id, Hongo hongo) {
        Optional<Hongo> optionalHongo = hongoRepository.findById(id);
        if (optionalHongo.isPresent()) {
        	Hongo existingHongo = optionalHongo.get();
            existingHongo.setNombre(hongo.getNombre());                       
            existingHongo.setDescripcion(hongo.getDescripcion());
            existingHongo.setEm(hongo.getEm());                       
            existingHongo.setEscomestible(hongo.getEscomestible());                       

            return hongoRepository.save(existingHongo);
        }
        return null;
    }

    public void deleteHongo(Long id) {
    	hongoRepository.deleteById(id);
    }
}

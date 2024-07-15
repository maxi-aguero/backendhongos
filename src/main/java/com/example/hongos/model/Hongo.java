package com.example.hongos.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Hongo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String descripcion;
    private String escomestible;
    private String em;//email del usuario quien lo creo

    public Hongo() {}
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }


    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public String getEscomestible() {
        return escomestible;
    }

    public void setEscomestible(String escomestible) {
        this.escomestible = escomestible;
    }
    
    public String getEm() {
        return em;
    }

    public void setEm(String em) {
        this.em = em;
    }
    
    
}

package com.example.hongos.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.hongos.model.Hongo;

public interface HongoRepository extends JpaRepository<Hongo, Long> {
    Hongo findById(String id);

}
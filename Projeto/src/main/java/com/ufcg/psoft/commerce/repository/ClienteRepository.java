package com.ufcg.psoft.commerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ufcg.psoft.commerce.model.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    
}

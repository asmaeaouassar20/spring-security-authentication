package com.algostyle.authentication.repository;

import com.algostyle.authentication.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client,Integer> {
    public Client findByEmail(String email);
}

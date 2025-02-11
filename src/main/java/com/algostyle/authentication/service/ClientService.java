package com.algostyle.authentication.service;

import com.algostyle.authentication.entity.Client;
import com.algostyle.authentication.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class ClientService implements UserDetailsService {

    @Autowired
    private ClientRepository clientRepository; //Repository pour accéder aux données des clients

    @Autowired
    private BCryptPasswordEncoder passwordEncoder; //Encodeur de mot de passe


    /**
     * Charge un utilisateur par son email pour l'authentification
     * @param email l'email de l'utilisateur
     * @return une instance de UserDetails contenant les informations de l'utilisateur
     * @throws UsernameNotFoundException si l'utilisateur n'est pas trouvé
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Client client=clientRepository.findByEmail(email); // Recherche du client par email
        if(client==null){
            throw new UsernameNotFoundException("user not found with email "+email);
        }
        return new User(client.getEmail(),client.getMotDePasse(),Collections.EMPTY_LIST);
        // Retourne un objet User avec email, mot de passe encodé et une liste vide d'autorisations
    }


    /**
     * Enregistre un nouveau client dans la base de données après avoir encodé son mot de passe
     * @param client l'objet Client à sauvegarder
     * @return true si l'enregistrement a réussi, false sinon
     */
    public boolean saveClient(Client client){
        String encodedPassword=passwordEncoder.encode(client.getMotDePasse()); //Encode le mot de passe avant sauvegarde
        client.setMotDePasse(encodedPassword);
        Client savedClient=clientRepository.save(client); //Sauvegarde le client en base de données
        return savedClient.getId()!=null; // Vérifie si le client a été enregistré avec succès
    }
}

package com.algostyle.authentication.config;


import com.algostyle.authentication.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class AppSecurityConfig {

    // Service permettant de charger les utilisateurs
    @Autowired
    private ClientService clientService;


    /**
     * Défini un bean BCryptPasswordEncoder pour encoder les mots de passe
     * @return une instance de BCryptPasswordEncoder
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
       return new BCryptPasswordEncoder(); // Bean pour encoder les mots de passe avec BCrypt
   }



    /**
     * Définit un DaoAuthenticationProvider qui utilise ClientService  pour charger les utilisateurs
     * et BCryptPasswordEncoder pour vérifier les mots de passe
     * @return une instance de DaoAuthenticationProvider
     */
   @Bean
    public DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authenticationProvider=new DaoAuthenticationProvider();

        authenticationProvider.setUserDetailsService(clientService); // définit le service de récupération des utilisateurs
        authenticationProvider.setPasswordEncoder(passwordEncoder()); // définit l'encodeur de mot de passe

        return authenticationProvider;
   }


    /**
     * Récupère l'AuthenticationManager depuis la configuration Spring Security
     * @param config la configuration de l'authentification
     * @return une instance d'AuthenticationManager
     * @throws Exception en cas d'erreur
     */
   @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception{
        return config.getAuthenticationManager();
   }


    /**
     * Définit la configuration de la sécurité HTTP
     * Autorise l'accès aux endpoints /register  et  /login  sans authentification,
     * mais exige une authentification pour toutes les autres requêtes
     * @param http la configuration HTTP de Spring Security
     * @return une instance de SecurityFilterChain
     * @throws Exception en cas d'erreur
     */
   @Bean
    public SecurityFilterChain security(HttpSecurity http) throws Exception{
        http.authorizeHttpRequests((req)->{
            req.requestMatchers("/register","/login") // Autorise l'accès sans authentification
                    .permitAll()
                    .anyRequest()
                    .authenticated(); // Tous les autres endpoints nécessitent une authentification
        });
        return http.csrf().disable().build(); //désactive la protection CSRF (à éviter en production sans alternative)
   }
}

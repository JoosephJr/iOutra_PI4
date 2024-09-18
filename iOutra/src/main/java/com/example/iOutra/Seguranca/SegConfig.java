package com.example.iOutra.Seguranca;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SegConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/resources/**", "/css/**", "/js/**").permitAll()  // qualquer usuario pode carregar o css e etc
                        .requestMatchers("/cadastrarUsuario").permitAll()
                        .requestMatchers("/estoquista/**").hasRole("ESTOQUISTA")
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/user/**").hasRole("USER")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")    // Use custom login page
                        .loginProcessingUrl("/login")   // URL for form action (POST method)
                        .defaultSuccessUrl("/index", true)  // Redirect after successful login
                        .failureUrl("/login?error=true")   // Redirect to login page on failure
                        .permitAll()    // Allow all users to access login page
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout=true")
                        .permitAll());

        return http.build();
    }

}

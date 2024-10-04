package com.example.iOutra.infra.security;

import com.example.iOutra.services.MyUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    MyUserDetails myUserRepository;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(registry -> {

                    //Livre acesso
                    registry.requestMatchers("/resumo", "/resumo/**", "/pagamento", "/pagamento/**","/addEndereco",
                            "/setup", "/static/css/img/**", "/css/**", "/css", "/backoffice/setup", "/", "/produto", "/login",
                            "/cadastro", "/cadastro/**","/sair", "/carrinho", "/item", "/item/**", "/itemreduzir", "/remover",
                            "/pedidos", "/pedidos/**", "/comprar").permitAll();

                    //Administrador e estoquista
                    registry.requestMatchers("/backoffice/", "/backoffice/pedidos",
                            "/backoffice/pedidos/**").hasAnyRole("Administrador", "Estoquista");

                    // estoquista nao acessa backoffice-listar produtos
                    registry.requestMatchers("/backoffice/usuarios").hasRole("Administrador");

                    registry.anyRequest().authenticated();

                })
                .formLogin(httpSecurityFormLoginConfigurer -> {
                    httpSecurityFormLoginConfigurer
                            .loginPage("/backoffice")
                            .usernameParameter("email")
                            .successHandler(new SimpleUrlAuthenticationSuccessHandler("/backoffice/home"))
                            .permitAll();
                })
                .logout(logoutConfigurer -> logoutConfigurer
                        .logoutSuccessUrl("/backoffice")
                        .logoutUrl("/logout")
                        .permitAll())

                .build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return myUserRepository;
    }
    @Bean public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(myUserRepository);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

}

package com.example.iOutra.service;

import com.example.iOutra.model.Usuario;
import com.example.iOutra.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MyUserDetailService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Usuario> user = usuarioRepository.findByEmail(email); {
            if (user.isPresent() && user.get().isActive())  {
                var userObj = user.get();
                return User.builder()
                        .username(userObj.getEmail())
                        .password(userObj.getPassword())
                        .roles(userObj.getGrupo())
                        .build();
            } else {
                throw new UsernameNotFoundException(email);
            }
        }
    }
}

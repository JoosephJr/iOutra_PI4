package com.example.iOutra.servico;

import com.example.iOutra.DTO.ReqRes;
import com.example.iOutra.model.Usuario;
import com.example.iOutra.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class UserManagementService {

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private JWTUtils jwtUtils;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private PasswordEncoder passwordEncoder;



    public ReqRes registrar(ReqRes registrationRequest){
        ReqRes resp = new ReqRes();

        try{
            Usuario usuario = new Usuario();
            usuario.setEmail(registrationRequest.getEmail());
            usuario.setNome(registrationRequest.getNome());
            usuario.setCpf(registrationRequest.getCpf());
            usuario.setSenha(passwordEncoder.encode(registrationRequest.getSenha()));
            usuario.setRole(registrationRequest.getRole());
            Usuario usuarioResult = usuarioRepository.save(usuario);

        }catch (Exception e){
            resp.setStatusCOD(500);
            resp.setErro(e.getMessage());
        }
        return resp;
    }

    public ReqRes login(ReqRes loginRequest){
        ReqRes response = new ReqRes();
        try {
            authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),
                            loginRequest.getSenha()));
            var usuario = usuarioRepository.findByEmail(loginRequest.getEmail());
            var jwt = jwtUtils.gerarToken(usuario);
            var refreshToken = jwtUtils.gerarRefreshToken(new HashMap<>(), usuario);
            response.setStatusCOD(200);
            response.setToken(jwt);
            response.setRole(usuario.getRole());
            response.setRefreshToken(refreshToken);
            response.setExpirationTime("24Hrs");
            response.setMensagem("Login bem sucedido");

        }catch (Exception e){
            response.setStatusCOD(500);
            response.setMensagem(e.getMessage());
        }
        return response;
    }
    public ReqRes refreshToken(ReqRes refreshTokenReqiest){
        ReqRes response = new ReqRes();
        try{
            String ourEmail = jwtUtils.extractUsername(refreshTokenReqiest.getToken());
            Usuario usuario = usuarioRepository.findByEmail(ourEmail);
            if (jwtUtils.isTokenValid(refreshTokenReqiest.getToken(), usuario)) {
                var jwt = jwtUtils.gerarToken(usuario);
                response.setStatusCOD(200);
                response.setToken(jwt);
                response.setRefreshToken(refreshTokenReqiest.getToken());
                response.setExpirationTime("24Hr");
                response.setMensagem("Token atualizado");
            }
            response.setStatusCOD(200);
            return response;

        }catch (Exception e){
            response.setStatusCOD(500);
            response.setMensagem(e.getMessage());
            return response;
        }
    }


    public ReqRes getAllUsers() {
        ReqRes reqRes = new ReqRes();

        try {
            List<Usuario> result = usuarioRepository.findAll();
            if (!result.isEmpty()) {
                reqRes.setUsuarioList(result);
                reqRes.setStatusCOD(200);
                reqRes.setMensagem("Successful");
            } else {
                reqRes.setStatusCOD(404);
                reqRes.setMensagem("No users found");
            }
            return reqRes;
        } catch (Exception e) {
            reqRes.setStatusCOD(500);
            reqRes.setMensagem("Error occurred: " + e.getMessage());
            return reqRes;
        }
    }


    public ReqRes getUsersById(Integer id) {
        ReqRes reqRes = new ReqRes();
        try {
            Usuario usersById = usuarioRepository.findById(id).orElseThrow(() -> new RuntimeException("User Not found"));
            reqRes.setUsuario(usersById);
            reqRes.setStatusCOD(200);
            reqRes.setMensagem("Users with id '" + id + "' found successfully");
        } catch (Exception e) {
            reqRes.setStatusCOD(500);
            reqRes.setMensagem("Error occurred: " + e.getMessage());
        }
        return reqRes;
    }


    public ReqRes deleteUser(Integer userId) {
        ReqRes reqRes = new ReqRes();
        try {
            Optional<Usuario> userOptional = usuarioRepository.findById(userId);
            if (userOptional.isPresent()) {
                usuarioRepository.deleteById(userId);
                reqRes.setStatusCOD(200);
                reqRes.setMensagem("User deleted successfully");
            } else {
                reqRes.setStatusCOD(404);
                reqRes.setMensagem("User not found for deletion");
            }
        } catch (Exception e) {
            reqRes.setStatusCOD(500);
            reqRes.setMensagem("Error occurred while deleting user: " + e.getMessage());
        }
        return reqRes;
    }

    public ReqRes updateUser(Integer userId, Usuario updatedUser) {
        ReqRes reqRes = new ReqRes();
        try {
            Optional<Usuario> userOptional = usuarioRepository.findById(userId);
            if (userOptional.isPresent()) {
                Usuario userExistente = userOptional.get();
                userExistente.setEmail(updatedUser.getEmail());
                userExistente.setNome(updatedUser.getNome());
                userExistente.setCpf(updatedUser.getCpf());
                userExistente.setRole(updatedUser.getRole());


                if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {

                    userExistente.setSenha(passwordEncoder.encode(updatedUser.getPassword()));
                }

                Usuario savedUser = usuarioRepository.save(userExistente);
                reqRes.setUsuario(savedUser);
                reqRes.setStatusCOD(200);
                reqRes.setMensagem("User updated successfully");
            } else {
                reqRes.setStatusCOD(404);
                reqRes.setMensagem("User not found for update");
            }
        } catch (Exception e) {
            reqRes.setStatusCOD(500);
            reqRes.setMensagem("Error occurred while updating user: " + e.getMessage());
        }
        return reqRes;
    }

//falta o metodo de recuperar dados de um user ainda

}
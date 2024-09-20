package com.example.iOutra.controller;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.iOutra.model.Usuario;

@Controller
@RequestMapping("/backoffice")
public class BackofficeController {
    
    @GetMapping({ "", "/" })
    public String exibirListaUsuarios(Model model){
        return "backoffice/backoffice";
    }

}

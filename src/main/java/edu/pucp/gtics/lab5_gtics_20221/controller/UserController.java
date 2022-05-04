package edu.pucp.gtics.lab5_gtics_20221.controller;

import edu.pucp.gtics.lab5_gtics_20221.entity.Juegos;
import edu.pucp.gtics.lab5_gtics_20221.entity.User;
import edu.pucp.gtics.lab5_gtics_20221.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {


    @Autowired
    UserRepository usuarioRepository;

    @GetMapping("/")
    public String index() {
        return "redirect:/product";
    }

    @GetMapping("/SingIn")
    public String login() {
        return "user/signIn";
    }


    @GetMapping("/redirectByRole")
    public String redirectByRole(Authentication auth,
                                 HttpSession session) {

        String role = "";
        for (GrantedAuthority authority : auth.getAuthorities()) {
            role = authority.getAuthority();
            break;
        }
        User usuario = usuarioRepository.findByCorreo(auth.getName());
        session.setAttribute("usuario", usuario);
        if (role.equalsIgnoreCase("user")) {
            session.setAttribute("carrito", new ArrayList<Juegos>());
            session.setAttribute("ncarrito", 0);
            return "redirect:/vista";
        } else {
            return "redirect:/juegos/lista";
        }

    }

}
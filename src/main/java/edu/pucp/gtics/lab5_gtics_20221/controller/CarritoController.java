package edu.pucp.gtics.lab5_gtics_20221.controller;

import edu.pucp.gtics.lab5_gtics_20221.entity.*;
import edu.pucp.gtics.lab5_gtics_20221.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class CarritoController {

    @Autowired
    JuegosRepository juegosRepository;
    @Autowired
    JuegosxUsuarioRepository juegosxUsuarioRepository;

    @GetMapping("/carrito/lista")
    public String listaCarrito (Model model, HttpSession session ){
        return "carrito/lista";
    }

    @GetMapping("/carrito/compra")
    public String comprarCarrito(HttpSession session){
        List<Juegos> carrito = (List<Juegos>) session.getAttribute("carrito");
        User user = (User) session.getAttribute("user");
        List<JuegosxUsuario> listaComprar = new ArrayList<JuegosxUsuario>();
        for (Juegos i : carrito){
            JuegosxUsuario juegosxUsuario = new JuegosxUsuario();
            juegosxUsuario.setIdjuego(i);
            juegosxUsuario.setIdusuario(user);
            listaComprar.add(juegosxUsuario);
        }
        juegosxUsuarioRepository.saveAll(listaComprar);
        session.setAttribute("carrito", new ArrayList<Juegos>());
        return "redirect:/vista";
    }

    @GetMapping("/carrito/borrar")
    public String borrarCarrito(@RequestParam("id") String idString){
        try{
            int id = Integer.parseInt(idString);
        }catch (Exception e){
            e.printStackTrace();
            return "redirect:/vista";
        }


        return "redirect:/carrito/lista";
    }


}

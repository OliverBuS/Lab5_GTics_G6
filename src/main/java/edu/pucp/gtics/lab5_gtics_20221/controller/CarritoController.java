package edu.pucp.gtics.lab5_gtics_20221.controller;

import edu.pucp.gtics.lab5_gtics_20221.entity.*;
import edu.pucp.gtics.lab5_gtics_20221.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
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
        session.setAttribute("ncarrito", 0);
        return "redirect:/vista";
    }


    @GetMapping("/carrito/borrar")
    public String borrarCarrito(HttpSession session,@RequestParam("id") String idString){
        int id;
        try{
            id = Integer.parseInt(idString);
        }catch (Exception e){
            e.printStackTrace();
            return "redirect:/vista";
        }
        Optional<Juegos> optionalJuegos = juegosRepository.findById(id);
        if(optionalJuegos.isEmpty()){return "redirect:/vista";}
        List<Juegos> carrito = (List<Juegos>) session.getAttribute("carrito");
        int ncarrito = (int) session.getAttribute("ncarrito");
        int index=0;
        for (Juegos juego : carrito){
            if(juego.getIdjuego()==optionalJuegos.get().getIdjuego()){
                carrito.remove(index);
                ncarrito--;
            }
            index++;
        }
        session.setAttribute("ncarrito", ncarrito);
        session.setAttribute("carrito",carrito);
        return "redirect:/carrito/lista";
    }

    @GetMapping("/carrito/anadir")
    public String anadirCarrito(HttpSession session, @RequestParam("id") String idString){
        int id;
        try {
            id=Integer.parseInt(idString);
        } catch (Exception e){
            e.printStackTrace();
            return "redirect:/vista";
        }
        Optional<Juegos> optionalJuegos = juegosRepository.findById(id);
        if(optionalJuegos.isEmpty()){return "redirect:/vista";}
        List<Juegos> carrito = (List<Juegos>) session.getAttribute("carrito");
        int ncarrito = (int) session.getAttribute("ncarrito");
        carrito.add(optionalJuegos.get());
        session.setAttribute("ncarrito", ncarrito++);
        session.setAttribute("carrito",carrito);
        return "redirect:/carrito/lista";
    }


}

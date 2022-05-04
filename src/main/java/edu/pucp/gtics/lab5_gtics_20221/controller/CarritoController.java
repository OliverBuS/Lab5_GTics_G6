package edu.pucp.gtics.lab5_gtics_20221.controller;

import edu.pucp.gtics.lab5_gtics_20221.entity.*;
import edu.pucp.gtics.lab5_gtics_20221.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public String listaCarrito(Model model, HttpSession session) {
        return "carrito/lista";
    }

    @GetMapping("/carrito/comprar")
    public String comprarCarrito(HttpSession session, RedirectAttributes redirectAttributes) {
        List<Juegos> carrito = (List<Juegos>) session.getAttribute("carrito");
        User user = (User) session.getAttribute("usuario");
        List<JuegosxUsuario> listaComprar = new ArrayList<JuegosxUsuario>();
        for (Juegos i : carrito) {
            JuegosxUsuario juegosxUsuario = new JuegosxUsuario();
            juegosxUsuario.setIdjuego(i);
            juegosxUsuario.setIdusuario(user);
            listaComprar.add(juegosxUsuario);
        }
        juegosxUsuarioRepository.saveAll(listaComprar);
        session.setAttribute("carrito", new ArrayList<Juegos>());
        session.setAttribute("ncarrito", 0);
        redirectAttributes.addFlashAttribute("msg","juegos comprados exitosamente");
        return "redirect:/carrito/lista";
    }


    @GetMapping("/carrito/borrar")
    public String borrarCarrito(HttpSession session, @RequestParam("id") String idString, RedirectAttributes redirectAttributes) {
        int id;
        try {
            id = Integer.parseInt(idString);
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/vista";
        }
        Optional<Juegos> optionalJuegos = juegosRepository.findById(id);
        if (optionalJuegos.isEmpty()) {
            return "redirect:/vista";
        }
        List<Juegos> carrito = (List<Juegos>) session.getAttribute("carrito");
        int ncarrito = (int) session.getAttribute("ncarrito");
        int index = 0;
        String juegoeliminado = "";
        if (carrito.size() == 1 && (carrito.get(0).getIdjuego()==id)) {
            carrito = new ArrayList<Juegos>();
            ncarrito=ncarrito-1;
        } else {
            for (Juegos juego : carrito) {
                if (juego.getIdjuego() == optionalJuegos.get().getIdjuego()) {
                    ncarrito=ncarrito-1;
                    juegoeliminado= carrito.get(index).getNombre();
                    carrito.remove(index);

                    break;
                }
                index++;
            }
        }
        session.setAttribute("ncarrito", ncarrito);
        session.setAttribute("carrito", carrito);
        redirectAttributes.addFlashAttribute("msg","Juego borrado exitosamente "+ juegoeliminado );
        return "redirect:/carrito/lista";
    }

    @GetMapping("/carrito/anadir")
    public String anadirCarrito(HttpSession session, @RequestParam("id") String idString, RedirectAttributes redirectAttributes) {
        int id;
        try {
            id = Integer.parseInt(idString);
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/vista";
        }
        Optional<Juegos> optionalJuegos = juegosRepository.findById(id);
        if (optionalJuegos.isEmpty()) {
            return "redirect:/vista";
        }
        List<Juegos> carrito = (List<Juegos>) session.getAttribute("carrito");
        int ncarrito = (int) session.getAttribute("ncarrito");
        carrito.add(optionalJuegos.get());
        session.setAttribute("ncarrito", ncarrito+1);
        session.setAttribute("carrito", carrito);
        redirectAttributes.addFlashAttribute("msg", "se ha añadido el juego " +optionalJuegos.get().getNombre() + " al carrito");
        return "redirect:/carrito/lista";
    }


}

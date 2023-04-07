package pe.com.cibertec.web;

import java.util.Collection;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import pe.com.cibertec.domain.Cliente;
import pe.com.cibertec.servicio.ClienteService;

@Controller
@Slf4j
public class ControladorInicio {
    @Autowired
    private ClienteService clienteService;

    @GetMapping("/")
    public String inicio(Model model,@Param("palabra")String palabra, @AuthenticationPrincipal User user) {
        Collection<? extends GrantedAuthority> currentUserRoles = user.getAuthorities();
        log.info("User rol:" + currentUserRoles);
        boolean isAdmin = currentUserRoles.stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
        boolean isClient = currentUserRoles.stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_CLIENTE"));

        if (isClient && !isAdmin) {
            model.addAttribute("mostrarBoton", true);
        } else {
            model.addAttribute("mostrarBoton", false);
        }

        
        var clientes = clienteService.listarClientes(palabra);
        
        model.addAttribute("clientes", clientes);
        model.addAttribute("palabra",palabra);
        return "clientes";
    }

    @GetMapping("/agregar")
    public String agregar(Cliente cliente) {
        return "actualizar";
    }

    @PostMapping("/guardar")
    public String guardar(Cliente cliente) {
        clienteService.guardar(cliente);
        return "redirect:/";
    }

    @GetMapping("/editar/{ideCli}")
    public String editar(Cliente cliente, Model model) {
        cliente = clienteService.encontrarCliente(cliente);  
        model.addAttribute("cliente", cliente);
        return "actualizar";
    }

    @GetMapping("/eliminar/{ideCli}")
    public String eliminar(Cliente cliente) {
        clienteService.eliminar(cliente);
        return "redirect:/";
    }
}

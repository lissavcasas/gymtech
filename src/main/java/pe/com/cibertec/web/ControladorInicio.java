package pe.com.cibertec.web;

import java.util.Collection;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pe.com.cibertec.domain.Cliente;
import pe.com.cibertec.servicio.ClienteService;

@Controller
@Slf4j
public class ControladorInicio {

    @Autowired
    private ClienteService clienteService;

    @GetMapping("/")
    public String inicio(Model model, @Param("palabra") String palabra, @AuthenticationPrincipal User user) {
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
        return "home";
    }

    @GetMapping("/clientes")
    public String clientes(Model model, @Param("palabra") String palabra, @AuthenticationPrincipal User user) {
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
        model.addAttribute("palabra", palabra);
        return "clientes";
    }

    @GetMapping("/agregar")
    public String agregar(Cliente cliente) {
        return "agregar";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid Cliente cliente, BindingResult result, Model model) throws BindException {

        if (result.hasErrors()) {
            String mensaje = "Los siguientes campos presentan errores: ";
            for (FieldError error : result.getFieldErrors()) {
                mensaje += error.getField() + " ";
            }
            model.addAttribute("error", mensaje);
            return "agregar";
        } else {
            clienteService.guardar(cliente);
            String mensaje = "Cliente con código Nro. " + cliente.getIdeCli() + " guardado con éxito.";
            model.addAttribute("mensaje", mensaje);
            return "agregar";
        }
    }

    @GetMapping("/editar/{ideCli}")
    public String editar(Cliente cliente, Model model) {
        cliente = clienteService.encontrarCliente(cliente);
        model.addAttribute("cliente", cliente);
        return "actualizar";
    }

    @PostMapping("/guardar/{ideCli}")
    public String actualizar(@Valid Cliente cliente, BindingResult result, Model model) throws BindException {

        if (result.hasErrors()) {
            String mensaje = "Los siguientes campos presentan errores: ";
            for (FieldError error : result.getFieldErrors()) {
                mensaje += error.getField() + " ";
            }
            model.addAttribute("error", mensaje);
            return "actualizar";
        }
        log.info("Cliente con código:" + cliente.getIdeCli());

        if (cliente.getIdeCli() != null) { // Verifica si el cliente ya existe en la base de datos

            Cliente clienteExistente = clienteService.encontrarCliente(cliente); // Recupera el objeto Cliente existente

            if (clienteExistente != null) { // Verifica si el objeto existe
                log.info("****usuario existente");
                log.info("Cliente con código:" + clienteExistente.getIdeCli());

                // Actualiza los campos necesarios del objeto Cliente existente
                clienteExistente.setNomCli(cliente.getNomCli());
                clienteExistente.setApePat(cliente.getApePat());
                clienteExistente.setApeMat(cliente.getApeMat());
                clienteExistente.setDniCli(cliente.getDniCli());
                clienteExistente.setDirCli(cliente.getDirCli());
                clienteExistente.setIdeDis(cliente.getIdeDis());
                clienteExistente.setTelCli(cliente.getTelCli());
                clienteExistente.setEmaCli(cliente.getEmaCli());
                clienteExistente.setFechaNac(cliente.getFechaNac());
                clienteExistente.setSexo(cliente.getSexo());
                clienteExistente.setRolCli(cliente.getRolCli());

                log.info(clienteExistente.getApePat());
                // Guarda el objeto Cliente actualizado en la base de datos
                clienteService.guardar(clienteExistente);
                String mensaje = "Cliente con código Nro. " + clienteExistente.getIdeCli() + " actualizado con éxito.";
                model.addAttribute("mensaje", mensaje);
                return "actualizar";
            }
        }

        log.info("****no existe");
        // Si el cliente no existe, crea un nuevo objeto Cliente y guárdalo en la base de datos
        clienteService.guardar(cliente);
        String mensaje = "Cliente con código Nro. " + cliente.getIdeCli() + " guardado con éxito.";
        model.addAttribute("mensaje", mensaje);
        return "actualizar";

    }

    @GetMapping("/eliminar/{ideCli}")
    public String eliminar(Cliente cliente, Model model) {
        log.info("Cliente con código:" + cliente.getIdeCli());
        clienteService.eliminar(cliente);
        model.addAttribute("cliente", cliente);
        return "redirect:/clientes";
    }
}

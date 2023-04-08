package pe.com.cibertec.web;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import pe.com.cibertec.domain.Registro;
import pe.com.cibertec.domain.RegistroDTO;
import pe.com.cibertec.domain.Rutina;
import pe.com.cibertec.servicio.RegistroService;
import pe.com.cibertec.servicio.RutinaService;
import pe.com.cibertec.servicio.UsuarioService;

@Controller
@Slf4j
public class ControladorAsistencias {
    
    @Autowired
    private RegistroService registroService;
    
    @Autowired
    private RutinaService rutinaService;
    
    @Autowired
    private UsuarioService usuarioService;
   
    @GetMapping("/registros")
    public String listarRegistros(Model model) {
        List<RegistroDTO> registros = registroService.listarRegistros();
        model.addAttribute("registros", registros);
        model.addAttribute("hayRegistroEnProceso", hayRegistroEnProceso());
        model.addAttribute("user_ide_cli",ideCliByUser());
        return "registros/lista";
    }

    @GetMapping("/registros/nuevo")
    public String mostrarFormularioNuevoRegistro(Model model) {
        Registro registro = new Registro();
        List<Rutina> rutinas = rutinaService.listarRutinas();
        model.addAttribute("registro", registro);
        model.addAttribute("rutinas", rutinas);
        return "registros/nuevo";
    }

    @PostMapping("/registros/nuevo")
    public String crearRegistro(@ModelAttribute("registro") Registro registro) {
        registro.setHora_entrada(getTimeNow());
        registro.setFecha(getDateNow()); 
        registro.setIde_cli(ideCliByUser());
        registroService.guardarRegistro(registro);
        return "redirect:/registros";
    }

    @GetMapping("/registros/editar")
    public String mostrarFormularioEditarRegistro(Model model) {
        Long idRegistro = obtenerIdRegistroEnProceso();
        if (idRegistro == null) {
            return "redirect:/registros";
        }
        Registro registro = registroService.encontrarRegistro(idRegistro);
        if (registro == null) {
            return "redirect:/registros";
        }
        model.addAttribute("registro", registro);
        return "registros/editar";
    }

    @PostMapping("/registros/editar")
    public String actualizarRegistro(@ModelAttribute("registro") Registro registro) {
        Long idRegistro = obtenerIdRegistroEnProceso();
        if (idRegistro == null) {
            return "redirect:/registros";
        }
        Registro registroCompleto = registroService.encontrarRegistro(idRegistro);
        if (registroCompleto == null) {
            return "redirect:/registros";
        }
        registroCompleto.setHora_salida(getTimeNow());
        registroService.actualizarRegistro(idRegistro ,registroCompleto);
        return "redirect:/registros";
    }
    
    private boolean hayRegistroEnProceso() {
        return registroService.hayRegistroEnProceso();
    }
    
    private Long obtenerIdRegistroEnProceso() {
        return registroService.obtenerIdRegistroEnProceso();
    }

    private Integer ideCliByUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();
        Integer ide_cli = usuarioService.findUserCodeByUsername(currentUserName);
        return ide_cli;
    }
    
    private String getTimeNow() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatterHora = DateTimeFormatter.ofPattern("HH:mm:ss");
        String horaActual = now.format(formatterHora);
        return horaActual;
    }
    
    private String getDateNow() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String fechaHoraActual = now.format(formatter);
        return fechaHoraActual.substring(0, 10);
    }
    
}

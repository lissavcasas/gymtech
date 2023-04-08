package pe.com.cibertec.web;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

@Controller
@Slf4j
public class ControladorAsistencias {
    
    @Autowired
    private RegistroService registroService;
    
    @Autowired
    private RutinaService rutinaService;
    
    @GetMapping("/registros")
    public String listarRegistros(Model model) {
        List<RegistroDTO> registros = registroService.listarRegistros();
        model.addAttribute("registros", registros);
        model.addAttribute("hayRegistroEnProceso", hayRegistroEnProceso());
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
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter formatterHora = DateTimeFormatter.ofPattern("HH:mm:ss");
        String fechaHoraActual = now.format(formatter);
        String horaActual = now.format(formatterHora);
        registro.setHora_entrada(horaActual);
        registro.setFecha(fechaHoraActual.substring(0, 10)); //obtener solo la fecha sin la hora
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
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatterHora = DateTimeFormatter.ofPattern("HH:mm:ss");
        String horaActual = now.format(formatterHora);
        
        Registro registroCompleto = registroService.encontrarRegistro(idRegistro);
        if (registroCompleto == null) {
            return "redirect:/registros";
        }
        registroCompleto.setHora_salida(horaActual);
        registroService.actualizarRegistro(idRegistro ,registroCompleto);
        return "redirect:/registros";
    }
    
    public boolean hayRegistroEnProceso() {
        return registroService.hayRegistroEnProceso();
    }
    
    public Long obtenerIdRegistroEnProceso() {
        return registroService.obtenerIdRegistroEnProceso();
    }

}

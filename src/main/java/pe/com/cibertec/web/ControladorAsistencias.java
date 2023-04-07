package pe.com.cibertec.web;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
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
        registroService.guardarRegistro(registro);
        return "redirect:/registros";
    }

    @GetMapping("/registros/{id}/editar")
    public String mostrarFormularioEditarRegistro(@PathVariable("id") Long id, Model model) {
        Registro registro = registroService.encontrarRegistro(id);
        //List<RutinaDTO> rutinas = rutinaService.listarRutinas();
        model.addAttribute("registro", registro);
        //model.addAttribute("rutinas", rutinas);
        return "registros/editar";
    }

    @PostMapping("/registros/{id}/editar")
    public String actualizarRegistro(@PathVariable("id") Long id, @ModelAttribute("registro") Registro registro) {
        registroService.actualizarRegistro(id, registro);
        return "redirect:/registros";
    }

}

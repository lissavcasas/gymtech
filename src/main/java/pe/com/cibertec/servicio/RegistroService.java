/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package pe.com.cibertec.servicio;

import java.util.List;
import org.springframework.data.domain.Page;
import pe.com.cibertec.domain.Registro;
import pe.com.cibertec.domain.RegistroDTO;


public interface RegistroService {
    
    public List<RegistroDTO> listarRegistros();
    
    public void guardarRegistro(Registro registro);
    
    public void eliminar(Long id);
    
    public void actualizarRegistro(Long id, Registro registro);
    
    public Registro encontrarRegistro(Long id);
    
    public boolean hayRegistroEnProceso();
    
    public Long obtenerIdRegistroEnProceso();
    
}

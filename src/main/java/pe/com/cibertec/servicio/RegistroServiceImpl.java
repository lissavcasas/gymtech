/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pe.com.cibertec.servicio;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.com.cibertec.Dao.RegistroDao;
import pe.com.cibertec.domain.Registro;
import pe.com.cibertec.domain.RegistroDTO;

@Service
public class RegistroServiceImpl implements RegistroService {
    
    @Autowired
    private RegistroDao registroDao;

    @Transactional(readOnly = true)
    public List<RegistroDTO> listarRegistros() {
        return registroDao.listarRegistros();
    }

    @Override
    public void guardarRegistro(Registro registro) {
        registroDao.save(registro);
    }

    @Override
    public void eliminar(Long id) {
        registroDao.deleteById(id);
    }

    @Override
    public void actualizarRegistro(Long id, Registro registro) {
        Registro registroExistente = registroDao.findById(id).orElse(null);
        if (registroExistente != null) {
            registro.setIdRegistro(id);
            registroDao.save(registro);
        }
    }

    @Override
    public Registro encontrarRegistro(Long id) {
        return registroDao.findById(id).orElse(null);
    }
    
}

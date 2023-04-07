package pe.com.cibertec.Dao;

import org.springframework.data.repository.CrudRepository;
import pe.com.cibertec.domain.Cliente;

public interface ClienteDao extends CrudRepository<Cliente, Long> {
    
}
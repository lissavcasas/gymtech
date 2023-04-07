package pe.com.cibertec.domain;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Data
@Table(name = "tb_clientes")
public class Cliente implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ideCli;

    @NotEmpty
    private String nomCli;

    @NotEmpty
    private String apePat;

    @NotEmpty
    private String apeMat;

    @NotEmpty
    private String dniCli;

    @NotEmpty
    private String dirCli;

    private Integer ideDis;

    @NotEmpty
    private String telCli;

    @NotEmpty
    private String emaCli;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date fechaNac;

    @NotEmpty
    private String sexo;

    @NotEmpty
    private String rolCli;
}

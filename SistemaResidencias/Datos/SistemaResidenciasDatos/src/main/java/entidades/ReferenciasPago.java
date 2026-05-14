package entidades;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "referencias_pago")
public class ReferenciasPago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "residente_id", nullable = false)
    private Residente residente;

    @Column(length = 50, nullable = false)
    private String concepto;

    @Column(length = 30, nullable = false)
    private String referenciaBancaria;

    @Column(length = 50, nullable = false)
    private String planDePago;

    @Column(length = 40, nullable = false)
    private String cicloLectivo;

    @Column(nullable = false)
    private Double monto;

    @Column(nullable = false)
    private LocalDate fechaLimite;

    @Column(nullable = false)
    private LocalDate fechaGeneracion;

    public ReferenciasPago() {
    }

    public ReferenciasPago(Residente residente, String concepto, String referenciaBancaria,
            String planDePago, String cicloLectivo, Double monto,
            LocalDate fechaLimite, LocalDate fechaGeneracion) {
        this.residente = residente;
        this.concepto = concepto;
        this.referenciaBancaria = referenciaBancaria;
        this.planDePago = planDePago;
        this.cicloLectivo = cicloLectivo;
        this.monto = monto;
        this.fechaLimite = fechaLimite;
        this.fechaGeneracion = fechaGeneracion;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Residente getResidente() { return residente; }
    public void setResidente(Residente residente) { this.residente = residente; }

    public String getConcepto() { return concepto; }
    public void setConcepto(String concepto) { this.concepto = concepto; }

    public String getReferenciaBancaria() { return referenciaBancaria; }
    public void setReferenciaBancaria(String referenciaBancaria) { this.referenciaBancaria = referenciaBancaria; }

    public String getPlanDePago() { return planDePago; }
    public void setPlanDePago(String planDePago) { this.planDePago = planDePago; }

    public String getCicloLectivo() { return cicloLectivo; }
    public void setCicloLectivo(String cicloLectivo) { this.cicloLectivo = cicloLectivo; }

    public Double getMonto() { return monto; }
    public void setMonto(Double monto) { this.monto = monto; }

    public LocalDate getFechaLimite() { return fechaLimite; }
    public void setFechaLimite(LocalDate fechaLimite) { this.fechaLimite = fechaLimite; }

    public LocalDate getFechaGeneracion() { return fechaGeneracion; }
    public void setFechaGeneracion(LocalDate fechaGeneracion) { this.fechaGeneracion = fechaGeneracion; }
}

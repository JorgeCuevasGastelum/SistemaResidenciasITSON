package dtos;

import java.time.LocalDate;

public class ReferenciasPagoDTO {

    private Long id;
    private String idResidente;
    private String nombreResidente;
    private String correoResidente;
    private Integer numeroHabitacion;
    private String concepto;
    private String referenciaBancaria;
    private String planDePago;
    private String cicloLectivo;
    private Double monto;
    private LocalDate fechaLimite;
    private LocalDate fechaGeneracion;

    public ReferenciasPagoDTO() {
    }

    public ReferenciasPagoDTO(String idResidente, String nombreResidente, String correoResidente,
            Integer numeroHabitacion, String concepto, String referenciaBancaria,
            String planDePago, String cicloLectivo, Double monto, LocalDate fechaLimite) {
        this.idResidente = idResidente;
        this.nombreResidente = nombreResidente;
        this.correoResidente = correoResidente;
        this.numeroHabitacion = numeroHabitacion;
        this.concepto = concepto;
        this.referenciaBancaria = referenciaBancaria;
        this.planDePago = planDePago;
        this.cicloLectivo = cicloLectivo;
        this.monto = monto;
        this.fechaLimite = fechaLimite;
        this.fechaGeneracion = LocalDate.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getIdResidente() { return idResidente; }
    public void setIdResidente(String idResidente) { this.idResidente = idResidente; }

    public String getNombreResidente() { return nombreResidente; }
    public void setNombreResidente(String nombreResidente) { this.nombreResidente = nombreResidente; }

    public String getCorreoResidente() { return correoResidente; }
    public void setCorreoResidente(String correoResidente) { this.correoResidente = correoResidente; }

    public Integer getNumeroHabitacion() { return numeroHabitacion; }
    public void setNumeroHabitacion(Integer numeroHabitacion) { this.numeroHabitacion = numeroHabitacion; }

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

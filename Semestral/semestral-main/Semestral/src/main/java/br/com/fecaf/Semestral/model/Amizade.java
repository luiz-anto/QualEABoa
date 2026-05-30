package br.com.fecaf.Semestral.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "amizades", indexes = {
        @Index(name = "idx_amiz_remetente",    columnList = "remetente_id"),
        @Index(name = "idx_amiz_destinatario", columnList = "destinatario_id"),
        @Index(name = "idx_amiz_status",       columnList = "status")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"remetente", "destinatario"})
@ToString(exclude = {"remetente", "destinatario"})
public class Amizade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "remetente_id")
    @JsonIgnore
    private Usuario remetente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destinatario_id")
    @JsonIgnore
    private Usuario destinatario;

    @Enumerated(EnumType.STRING)
    private AmizadeStatus status;

    private String criadoEm;

    @JsonProperty("remetenteId")
    public Long getRemetenteId() { return remetente != null ? remetente.getId() : null; }

    @JsonProperty("remetenteNome")
    public String getRemetenteNome() { return remetente != null ? remetente.getNome() : null; }

    @JsonProperty("remetenteHandle")
    public String getRemetenteHandle() { return remetente != null ? remetente.getHandle() : null; }

    @JsonProperty("destinatarioId")
    public Long getDestinatarioId() { return destinatario != null ? destinatario.getId() : null; }

    @JsonProperty("destinatarioNome")
    public String getDestinatarioNome() { return destinatario != null ? destinatario.getNome() : null; }

    @JsonProperty("destinatarioHandle")
    public String getDestinatarioHandle() { return destinatario != null ? destinatario.getHandle() : null; }
}

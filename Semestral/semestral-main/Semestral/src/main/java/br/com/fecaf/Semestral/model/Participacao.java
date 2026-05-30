package br.com.fecaf.Semestral.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "participacoes", indexes = {
        @Index(name = "idx_partic_usuario",        columnList = "usuario_id"),
        @Index(name = "idx_partic_evento",         columnList = "evento_id"),
        @Index(name = "idx_partic_usuario_evento", columnList = "usuario_id,evento_id")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"usuario", "evento"})
@ToString(exclude = {"usuario", "evento"})
public class Participacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    @JsonIgnore
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evento_id")
    @JsonIgnore
    private Evento evento;

    @Enumerated(EnumType.STRING)
    private ParticipacaoStatus status;

    private String dataParticipacao;

    @JsonProperty("usuarioId")
    public Long getUsuarioId() { return usuario != null ? usuario.getId() : null; }

    @JsonProperty("eventoId")
    public Long getEventoId() { return evento != null ? evento.getId() : null; }

    @JsonProperty("eventoNome")
    public String getEventoNome() { return evento != null ? evento.getNome() : null; }

    @JsonProperty("eventoData")
    public String getEventoData() { return evento != null ? evento.getData() : null; }

    @JsonProperty("eventoLocal")
    public String getEventoLocal() { return evento != null ? evento.getLocal() : null; }

    @JsonProperty("eventoStatus")
    public String getEventoStatus() { return evento != null ? evento.getStatus() : null; }

    @JsonProperty("eventoPrivado")
    public Boolean getEventoPrivado() { return evento != null ? evento.getPrivado() : null; }
}

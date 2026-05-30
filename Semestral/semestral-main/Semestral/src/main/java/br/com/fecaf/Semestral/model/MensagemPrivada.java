package br.com.fecaf.Semestral.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "mensagens_privadas", indexes = {
        @Index(name = "idx_msgpriv_conversa",   columnList = "conversa_id"),
        @Index(name = "idx_msgpriv_remetente",  columnList = "remetente_id"),
        @Index(name = "idx_msgpriv_dataenvio",  columnList = "dataEnvio")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"conversa", "remetente"})
@ToString(exclude = {"conversa", "remetente"})
public class MensagemPrivada {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conversa_id")
    @JsonIgnore
    private Conversa conversa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "remetente_id")
    @JsonIgnore
    private Usuario remetente;

    @Column(columnDefinition = "TEXT")
    private String texto;

    private String dataEnvio;
    private Boolean lida = false;

    @JsonProperty("conversaId")
    public Long getConversaId() { return conversa != null ? conversa.getId() : null; }

    @JsonProperty("remetenteId")
    public Long getRemetenteId() { return remetente != null ? remetente.getId() : null; }

    @JsonProperty("remetenteNome")
    public String getRemetenteNome() { return remetente != null ? remetente.getNome() : null; }
}

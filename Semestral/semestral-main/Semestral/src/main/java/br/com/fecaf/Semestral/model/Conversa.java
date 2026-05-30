package br.com.fecaf.Semestral.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "conversas", indexes = {
        @Index(name = "idx_conv_usuario1", columnList = "usuario1_id"),
        @Index(name = "idx_conv_usuario2", columnList = "usuario2_id")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"usuario1", "usuario2"})
@ToString(exclude = {"usuario1", "usuario2"})
public class Conversa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario1_id")
    @JsonIgnore
    private Usuario usuario1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario2_id")
    @JsonIgnore
    private Usuario usuario2;

    private String ultimaMensagem;
    private String dataUltimaMensagem;

    @JsonProperty("usuario1Id")
    public Long getUsuario1Id() { return usuario1 != null ? usuario1.getId() : null; }

    @JsonProperty("usuario1Nome")
    public String getUsuario1Nome() { return usuario1 != null ? usuario1.getNome() : null; }

    @JsonProperty("usuario1Handle")
    public String getUsuario1Handle() { return usuario1 != null ? usuario1.getHandle() : null; }

    @JsonProperty("usuario2Id")
    public Long getUsuario2Id() { return usuario2 != null ? usuario2.getId() : null; }

    @JsonProperty("usuario2Nome")
    public String getUsuario2Nome() { return usuario2 != null ? usuario2.getNome() : null; }

    @JsonProperty("usuario2Handle")
    public String getUsuario2Handle() { return usuario2 != null ? usuario2.getHandle() : null; }
}

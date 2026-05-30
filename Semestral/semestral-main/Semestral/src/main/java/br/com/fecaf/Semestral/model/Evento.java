package br.com.fecaf.Semestral.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "eventos", indexes = {
        @Index(name = "idx_evento_status",    columnList = "status"),
        @Index(name = "idx_evento_privado",   columnList = "privado"),
        @Index(name = "idx_evento_usuario",   columnList = "usuario_id"),
        @Index(name = "idx_evento_criado_em", columnList = "criadoEm")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "usuario")
@ToString(exclude = "usuario")
public class Evento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    private String local;
    private String data;
    private String categoria;
    private String vagas;
    private String preco;
    private Double coordLng;
    private Double coordLat;

    @Column(columnDefinition = "TEXT")
    private String imgUrl;

    private String status;
    private String motivoCancelamento;
    private String criadoEm;
    private String organizadorNome;
    private String organizadorEmail;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private Boolean privado = false;

    @Column(nullable = false, columnDefinition = "integer default 0")
    private Integer totalParticipantes = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    @JsonIgnore
    private Usuario usuario;

    @JsonProperty("usuarioId")
    public Long getUsuarioId() {
        return usuario != null ? usuario.getId() : null;
    }
}

package br.com.fecaf.Semestral.dto;

import java.util.List;

public record CriarEventoRequest(
        String nome,
        String descricao,
        String local,
        String data,
        String categoria,
        String vagas,
        String preco,
        List<Double> coords,
        String imgUrl,
        Long usuarioId,
        Boolean privado
) {}

package br.com.fecaf.Semestral.dto;

public record UsuarioBuscaDTO(
        Long id,
        String nome,
        String handle,
        String email,
        String statusAmizade,
        Long amizadeId
) {}

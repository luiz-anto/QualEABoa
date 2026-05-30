package br.com.fecaf.Semestral.dto;

public record EnviarMensagemRequest(Long remetenteId, Long destinatarioId, String texto) {}

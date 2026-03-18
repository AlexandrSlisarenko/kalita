package ru.slisarenko.kalita.dto;

import lombok.Builder;

@Builder
public record WalletDTO(String id, String operationType, Long amount) {
}

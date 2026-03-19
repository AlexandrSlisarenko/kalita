package ru.slisarenko.kalita.dto;

import lombok.Builder;

@Builder
public record WalletDTO(String walletId, String operationType, Double amount) {
}

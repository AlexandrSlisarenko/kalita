package ru.slisarenko.kalita.dto;

import lombok.Builder;

@Builder
public record ResponseDTO(WalletDTO walletDTO, String error) {
}

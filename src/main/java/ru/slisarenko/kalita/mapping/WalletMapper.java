package ru.slisarenko.kalita.mapping;

import java.math.BigDecimal;
import java.util.UUID;
import org.springframework.stereotype.Component;
import ru.slisarenko.kalita.dto.BalanceDTO;
import ru.slisarenko.kalita.dto.WalletDTO;
import ru.slisarenko.kalita.enums.OperationType;
import ru.slisarenko.kalita.persist.model.Wallet;

@Component
public class WalletMapper {

    public WalletDTO toDTO(Wallet wallet) {
        return WalletDTO.builder()
                .walletId(wallet.getWalletId().toString())
                .operationType(wallet.getOperationType().toString())
                .amount(wallet.getAmount().doubleValue())
                .build();
    }

    public Wallet fromDTO(WalletDTO dto) {
        return Wallet.builder()
                .walletId(UUID.fromString(dto.walletId()))
                .operationType(OperationType.valueOf(dto.operationType()))
                .amount(BigDecimal.valueOf(dto.amount()))
                .build();
    }

    public BalanceDTO toBalanceDTO(Double amount) {
        return BalanceDTO.builder()
                .balance(amount)
                .build();
    }

}

package ru.slisarenko.kalita.mapping;

import java.math.BigDecimal;
import java.util.UUID;
import org.springframework.stereotype.Component;
import ru.slisarenko.kalita.dto.WalletDTO;
import ru.slisarenko.kalita.enums.OperationType;
import ru.slisarenko.kalita.persist.model.Wallet;

@Component
public class WalletMapper {

    public WalletDTO toDTO(Wallet wallet) {
        return WalletDTO.builder()
                .id(wallet.getWalletId().toString())
                .operationType(wallet.getOperationType().toString())
                .amount(wallet.getAmount().longValue())
                .build();
    }

    public Wallet fromDTO(WalletDTO dto) {
        return Wallet.builder()
                .walletId(UUID.fromString(dto.id()))
                .operationType(OperationType.valueOf(dto.operationType()))
                .amount(BigDecimal.valueOf(dto.amount()))
                .build();
    }

}

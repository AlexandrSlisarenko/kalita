package ru.slisarenko.kalita.service;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.slisarenko.kalita.dto.WalletDTO;
import ru.slisarenko.kalita.enums.OperationType;
import ru.slisarenko.kalita.mapping.WalletMapper;
import ru.slisarenko.kalita.persist.service.WalletService;

@Slf4j
@Service
@RequiredArgsConstructor
public class KalitaService {
    private final WalletService walletService;
    private final WalletMapper walletMapper;


    public WalletDTO open(WalletDTO walletDTO) {
        if(checkOperation(walletDTO.operationType(), OperationType.OPEN)){
            var wallet = this.walletMapper.fromDTO(walletDTO);
            wallet = this.walletService.open(wallet);
            return walletMapper.toDTO(wallet);
        }
        return WalletDTO.builder().id("").build();
    }


    private static boolean checkId(String id) {
        try {
            UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            return false;
        }
        return true;
    }

    private boolean checkOperation(String type, OperationType operation) {
        try {
            return OperationType.valueOf(type).equals(operation);
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            return false;
        }
    }
}

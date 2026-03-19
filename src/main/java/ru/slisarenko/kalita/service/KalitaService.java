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
            wallet = this.walletService.save(wallet);
            return walletMapper.toDTO(wallet);
        }
        return WalletDTO.builder().id("").build();
    }

    public boolean close(WalletDTO walletDTO) {
        var wallet = this.walletMapper.fromDTO(walletDTO);
        return this.walletService.delete(wallet);
    }

    public WalletDTO deposit(WalletDTO walletDTO) {
        if(checkDTO(walletDTO, OperationType.DEPOSIT)){
            var wallet = this.walletMapper.fromDTO(walletDTO);
            var walletFromDB = this.walletService.get(wallet.getWalletId());
            walletFromDB.setOperationType(wallet.getOperationType());
            walletFromDB.setAmount(walletFromDB.getAmount().add(wallet.getAmount()));
            walletFromDB = this.walletService.update(walletFromDB);
            return walletMapper.toDTO(walletFromDB);
        }
        return WalletDTO.builder().id("").build();
    }

    public WalletDTO withDraw(WalletDTO walletDTO) {
        if(checkDTO(walletDTO, OperationType.WITHDRAW)){
            var wallet = this.walletMapper.fromDTO(walletDTO);
            var walletFromDB = this.walletService.get(wallet.getWalletId());
            walletFromDB.setOperationType(wallet.getOperationType());
            walletFromDB.setAmount(walletFromDB.getAmount().subtract(wallet.getAmount()));
            walletFromDB = this.walletService.update(wallet);
            return walletMapper.toDTO(walletFromDB);
        }
        return WalletDTO.builder().id("").build();
    }


    private boolean checkDTO(WalletDTO dto, OperationType type) {
        if(checkOperation(dto.operationType(), type) && checkMoreZero(dto.amount())){
                return this.walletService.exists(UUID.fromString(dto.id()));
        }
        return false;

    }

    private boolean checkMoreZero(Double amount) {
        return amount > 0;
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

package ru.slisarenko.kalita.service;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.slisarenko.kalita.dto.BalanceDTO;
import ru.slisarenko.kalita.dto.WalletDTO;
import ru.slisarenko.kalita.enums.OperationType;
import ru.slisarenko.kalita.exception.ErrorJsonException;
import ru.slisarenko.kalita.exception.InsufficientFundsException;
import ru.slisarenko.kalita.exception.WalletNotFoundException;
import ru.slisarenko.kalita.mapping.WalletMapper;
import ru.slisarenko.kalita.persist.service.WalletService;

@Slf4j
@Service
@RequiredArgsConstructor
public class KalitaService {
    private final WalletService walletService;
    private final WalletMapper walletMapper;


    public WalletDTO open(WalletDTO walletDTO) {
        if(checkOperation(walletDTO.operationType())){
            var wallet = this.walletMapper.fromDTO(walletDTO);
            wallet = this.walletService.save(wallet);
            return walletMapper.toDTO(wallet);
        }
        return WalletDTO.builder().walletId("").build();
    }

    public boolean close(WalletDTO walletDTO) {
        var wallet = this.walletMapper.fromDTO(walletDTO);
        return this.walletService.delete(wallet);
    }

    public BalanceDTO getBalance(UUID id) {
        try {
            var walletFromDB = this.walletService.get(id);
            return this.walletMapper.toBalanceDTO(walletFromDB.getAmount().doubleValue());
        }catch (WalletNotFoundException e){
            log.error(e.getMessage());
            return this.walletMapper.toBalanceDTO(-1.00);
        }
    }

    public WalletDTO deposit(WalletDTO walletDTO) {
            var wallet = this.walletMapper.fromDTO(walletDTO);
            var walletFromDB = this.walletService.get(wallet.getWalletId());
            walletFromDB.setOperationType(wallet.getOperationType());
            walletFromDB.setAmount(walletFromDB.getAmount().add(wallet.getAmount()));
            walletFromDB = this.walletService.update(walletFromDB);
            return walletMapper.toDTO(walletFromDB);
    }

    public WalletDTO withDraw(WalletDTO walletDTO) {
            var wallet = this.walletMapper.fromDTO(walletDTO);
            var walletFromDB = this.walletService.get(wallet.getWalletId());
            var result = walletFromDB.getAmount().subtract(wallet.getAmount());
            if(result.doubleValue() < 0 ){
                throw new InsufficientFundsException(String.format("Insufficient funds : %s", result));
            }
            walletFromDB.setOperationType(wallet.getOperationType());
            walletFromDB.setAmount(result);
            walletFromDB = this.walletService.update(walletFromDB);
            return walletMapper.toDTO(walletFromDB);
    }

    public WalletDTO giveWallet(WalletDTO walletDTO) {
        if(!checkDTO(walletDTO)){
            throw new ErrorJsonException("Invalid wallet");
        }
        return switch (OperationType.valueOf(walletDTO.operationType())) {
            case WITHDRAW -> this.withDraw(walletDTO);
            case DEPOSIT -> this.deposit(walletDTO);
            default -> WalletDTO.builder().walletId("").build();
        };
    }

    private boolean checkDTO(WalletDTO dto) {
        if(checkId(dto.walletId()) && checkOperation(dto.operationType()) && checkMoreZero(dto.amount())){
                if(!this.walletService.exists(UUID.fromString(dto.walletId()))){
                    throw new WalletNotFoundException(String.format("Wallet not found : %s", dto.walletId()));
                }
                return true;
        }
        return false;
    }

    private boolean checkId(String id) {
        try {
            UUID.fromString(id);
            return true;
        } catch (IllegalArgumentException e) {
            log.error("Invalid id:{}. {}", id, e.getMessage());
            return false;
        }
    }

    private boolean checkMoreZero(Double amount) {
        return amount > 0;
    }

    private boolean checkOperation(String type) {
        try {
            OperationType.valueOf(type);
            return true;
        } catch (IllegalArgumentException e) {
            log.error("Invalid type:{}. {}", type, e.getMessage());
            return false;
        }
    }
}

package ru.slisarenko.kalita.persist.service;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.slisarenko.kalita.exception.WalletNotFoundException;
import ru.slisarenko.kalita.persist.model.Wallet;
import ru.slisarenko.kalita.persist.repository.WalletRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class WalletService {
    private final WalletRepository walletRepository;

    public Wallet get(UUID id) {
        return this.walletRepository.findById(id).orElseThrow(() -> new WalletNotFoundException("Wallet not found"));
    }

    public Wallet save(Wallet wallet) {
        wallet.setWalletId(UUID.randomUUID());
        log.info("NEW ID: " + wallet.getWalletId());
        return this.walletRepository.save(wallet);
    }

    public boolean delete(Wallet wallet) {
        this.walletRepository.delete(wallet);
        return !this.walletRepository.existsById(wallet.getWalletId());
    }

    public Wallet update(Wallet wallet) {
        var walletFromDB = get(wallet.getWalletId());
        walletFromDB.setOperationType(wallet.getOperationType());
        walletFromDB.setAmount(wallet.getAmount());
        return this.walletRepository.save(walletFromDB);
    }

    public boolean exists(UUID id) {
        return this.walletRepository.existsById(id);
    }
}

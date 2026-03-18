package ru.slisarenko.kalita.persist.service;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.slisarenko.kalita.exception.WalletNotFoundException;
import ru.slisarenko.kalita.persist.model.Wallet;
import ru.slisarenko.kalita.persist.repository.WalletRepository;

@Service
@RequiredArgsConstructor
public class WalletService {
    private final WalletRepository walletRepository;

    public Wallet open(Wallet wallet) {
        wallet.setWalletId(UUID.randomUUID());
        return this.walletRepository.save(wallet);
    }

    public boolean close(Wallet wallet) {
        this.walletRepository.delete(wallet);
        return this.walletRepository.existsById(wallet.getWalletId());
    }

    public Wallet update(Wallet wallet) {
        var walletFromDB = this.walletRepository.findById(wallet.getWalletId()).orElseThrow(() -> new WalletNotFoundException("Wallet not found"));
        walletFromDB.setOperationType(wallet.getOperationType());
        walletFromDB.setAmount(wallet.getAmount());
        return this.walletRepository.save(walletFromDB);
    }


}

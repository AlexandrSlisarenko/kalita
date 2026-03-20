package ru.slisarenko.kalita.persist.service;

import java.math.BigDecimal;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.slisarenko.kalita.exception.WalletNotFoundException;
import ru.slisarenko.kalita.persist.model.Wallet;
import ru.slisarenko.kalita.persist.repository.WalletRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class WalletService {

    @Value("${app.fast-update}")
    private boolean fastUpdate;
    private final WalletRepository walletRepository;
    private final JdbcTemplate jdbcTemplate;

    @Transactional(readOnly = true)
    public Wallet get(UUID id) {
        return this.walletRepository.findById(id).orElseThrow(() -> new WalletNotFoundException("Wallet not found"));
    }

    @Transactional
    public Wallet save(Wallet wallet) {
        wallet.setWalletId(UUID.randomUUID());
        log.info("NEW ID: " + wallet.getWalletId());
        return this.walletRepository.save(wallet);
    }

    public boolean delete(Wallet wallet) {
        this.walletRepository.delete(wallet);
        return !this.walletRepository.existsById(wallet.getWalletId());
    }

    @Transactional
    public Wallet update(Wallet wallet) {
        log.info("UPDATING ID: " + wallet.getWalletId());
        log.info("fastUpdate: " + fastUpdate);
        if (!fastUpdate) {
            var walletFromDB = get(wallet.getWalletId());
            walletFromDB.setOperationType(wallet.getOperationType());
            walletFromDB.setAmount(wallet.getAmount());
            return this.walletRepository.save(wallet);
        }   else {
            var isUpdate = updateJDBCTemplates(wallet.getWalletId(), wallet.getAmount());
            log.info("wallet id:{} is update: {} ",wallet.getWalletId(), isUpdate);
            return wallet;
        }

    }

    public boolean updateJDBCTemplates(UUID id, BigDecimal amount) {
        String sql = "UPDATE kalita.wallet SET amount = ? WHERE walletid = ?";
        int updated = jdbcTemplate.update(sql, amount, id);
        return updated > 0;
    }

    public boolean exists(UUID id) {
        return this.walletRepository.existsByWalletId(id);
    }
}

package ru.slisarenko.kalita.persist.service;

import java.math.BigDecimal;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.slisarenko.kalita.enums.OperationType;
import ru.slisarenko.kalita.persist.model.Wallet;

@SpringBootTest
class WalletServiceTest {

    private UUID uuid;
    private Wallet wallet;

    @Autowired
    private WalletService walletService;

    @BeforeEach
    void setUp() {
        uuid = UUID.randomUUID();
        wallet = Wallet.builder()
                .walletId(uuid)
                .operationType(OperationType.OPEN)
                .amount(new BigDecimal("100.00"))
                .build();
    }

    @Test
    void open() {
        var testWallet = this.walletService.open(wallet);
        Assertions.assertNotEquals(uuid, testWallet.getWalletId());
        Assertions.assertTrue(this.walletService.close(testWallet));
    }

    @Test
    void updateData(){
        wallet = Wallet.builder()
                .walletId(uuid)
                .operationType(OperationType.OPEN)
                .amount(new BigDecimal("100.00"))
                .build();
        var testWallet = this.walletService.open(wallet);
        Wallet updateWalletTest = this.walletService.update(wallet);
        Assertions.assertNotEquals(updateWalletTest.getWalletId(), testWallet.getWalletId());
        Assertions.assertTrue(this.walletService.close(testWallet));
    }
}
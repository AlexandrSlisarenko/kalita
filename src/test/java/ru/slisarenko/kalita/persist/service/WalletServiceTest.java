package ru.slisarenko.kalita.persist.service;

import java.math.BigDecimal;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.slisarenko.kalita.config.MyTestContainer;
import ru.slisarenko.kalita.enums.OperationType;
import ru.slisarenko.kalita.persist.model.Wallet;

@SpringBootTest
@Testcontainers
@Import({MyTestContainer.class})
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
        this.walletService.save(wallet);
    }

    @Test
    void open() {
        Assertions.assertNotNull(wallet.getWalletId());
        Assertions.assertTrue(this.walletService.delete(wallet));
    }

    @Test
    void updateData(){
        var testType = OperationType.DEPOSIT;
        var testAmount = new BigDecimal("200.00");
        wallet.setAmount(testAmount);
        wallet.setOperationType(testType);
        var updateWalletTest = this.walletService.update(wallet);
        Assertions.assertEquals(testType, updateWalletTest.getOperationType());
        Assertions.assertEquals( testAmount, updateWalletTest.getAmount());
        Assertions.assertTrue(this.walletService.delete(wallet));
    }
}
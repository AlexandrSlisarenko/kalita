package ru.slisarenko.kalita.service;

import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.slisarenko.kalita.dto.WalletDTO;
import ru.slisarenko.kalita.enums.OperationType;

@Slf4j
@SpringBootTest
class KalitaServiceTest {

    private UUID uuid;
    private WalletDTO wallet;
    private final double startBalance = 100.00;

    @Autowired
    private KalitaService kalitaService;
    @BeforeEach
     void createWallet() {
        uuid = UUID.randomUUID();
        wallet = WalletDTO.builder()
                .id(uuid.toString())
                .operationType(OperationType.OPEN.toString())
                .amount(startBalance)
                .build();

        wallet = this.kalitaService.open(wallet);
    }

    @Test
    void open() {
        Assertions.assertNotNull(this.wallet);
        Assertions.assertNotEquals(uuid, this.wallet.id());
        Assertions.assertTrue(this.kalitaService.close(this.wallet));
    }

    @Test
    void depositTest() {
        var type = OperationType.DEPOSIT.toString();
        var amount = 500.00;
        var walletTest = WalletDTO.builder()
                .id(wallet.id())
                .amount(amount)
                .operationType(type)
                .build();
        walletTest = this.kalitaService.deposit(walletTest);
        Assertions.assertEquals(wallet.id(), walletTest.id());
        Assertions.assertEquals(amount + startBalance, walletTest.amount());
        Assertions.assertEquals(type, walletTest.operationType());
        Assertions.assertTrue(this.kalitaService.close(walletTest));
    }

    @Test
    void withDrawTest() {
        var type = OperationType.WITHDRAW.toString();
        var amount = 50.00;
        var walletTest = WalletDTO.builder()
                .id(wallet.id())
                .amount(amount)
                .operationType(type)
                .build();
        walletTest = this.kalitaService.withDraw(walletTest);
        Assertions.assertEquals(wallet.id(), walletTest.id());
        Assertions.assertEquals(startBalance - amount, walletTest.amount());
        Assertions.assertEquals(type, walletTest.operationType());
        Assertions.assertTrue(this.kalitaService.close(walletTest));
    }


}
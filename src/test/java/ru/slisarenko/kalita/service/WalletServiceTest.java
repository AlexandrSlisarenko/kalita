package ru.slisarenko.kalita.service;

import java.math.BigDecimal;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import ru.slisarenko.kalita.persist.model.Wallet;
import ru.slisarenko.kalita.persist.repository.WalletRepository;

import static org.mockito.Mockito.doReturn;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class WalletServiceTest {
    public static final UUID UUID = java.util.UUID.randomUUID();

    @Mock
    private WalletRepository walletRepository;

    @InjectMocks
    private WalletService walletService;


    @Test
    void getBalanceTest() {
        var testWallet = Wallet.builder()
                .walletId(UUID)
                .amount(BigDecimal.valueOf(100L))
                .build();

        doReturn(testWallet).when(walletRepository).findById(UUID);

    }
}
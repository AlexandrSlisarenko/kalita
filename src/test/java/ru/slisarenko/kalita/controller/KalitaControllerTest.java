package ru.slisarenko.kalita.controller;

import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.slisarenko.kalita.config.MyTestContainer;
import ru.slisarenko.kalita.dto.BalanceDTO;
import ru.slisarenko.kalita.dto.WalletDTO;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureTestRestTemplate
@Testcontainers
@Import({MyTestContainer.class})
class KalitaControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeEach
    void setUp() {
    }

    @Test
    void getWallet() {
        UUID wallet_uuid = UUID.fromString("a31f2209-638a-4829-b3f3-94bc33a385e1");
        ResponseEntity<BalanceDTO> response = restTemplate.exchange(
                "/api/v1/wallet/{wallet_uuid}",
                HttpMethod.GET,
                null,
                BalanceDTO.class,
                wallet_uuid);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().balance()).isEqualTo(100.00);
    }

    @Test
    void giveWallet() {
        UUID wallet_uuid = UUID.fromString("a31f2209-638a-4829-b3f3-94bc33a385e1");
        var requestEntity = WalletDTO.builder()
                .operationType("DEPOSIT")
                .walletId(wallet_uuid.toString())
                .amount(432.32)
                .build();
        ResponseEntity<WalletDTO> response = restTemplate.exchange(
                "/api/v1/wallet",
                HttpMethod.POST,
                new HttpEntity<>(requestEntity),
                WalletDTO.class,
                wallet_uuid);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }
}
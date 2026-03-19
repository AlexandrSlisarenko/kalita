package ru.slisarenko.kalita.controller;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.slisarenko.kalita.dto.BalanceDTO;
import ru.slisarenko.kalita.dto.ResponseDTO;
import ru.slisarenko.kalita.dto.WalletDTO;
import ru.slisarenko.kalita.service.KalitaService;

@Slf4j
@RestController
@RequestMapping("api/v1/wallet")
@RequiredArgsConstructor
public class KalitaController {
    private final KalitaService kalitaService;

    @GetMapping("/{wallet_uuid}")
    public ResponseEntity<BalanceDTO> getWallet(@PathVariable("wallet_uuid") UUID walletUuid) {
        log.info("Uuid: {}", walletUuid);
        var result = this.kalitaService.getBalance(walletUuid);
        return result.balance().equals(-1.00) ? new ResponseEntity<>(result, HttpStatus.NOT_FOUND) :
                new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ResponseDTO> giveWallet(@RequestBody WalletDTO walletDTO) {
        log.info("WalletDTO: {}", walletDTO);
        try{
            var result = this.kalitaService.giveWallet(walletDTO);
            return new ResponseEntity<>(ResponseDTO.builder()
                    .walletDTO(result)
                    .build(), HttpStatus.OK);
        }  catch (Exception e) {
            return new ResponseEntity<>(ResponseDTO.builder()
                    .walletDTO(walletDTO)
                    .error(e.getMessage())
                    .build(), HttpStatus.BAD_REQUEST);
        }
    }
}

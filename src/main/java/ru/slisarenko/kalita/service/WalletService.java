package ru.slisarenko.kalita.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.slisarenko.kalita.persist.repository.WalletRepository;

@Service
@RequiredArgsConstructor
public class WalletService {
    private final WalletRepository walletRepository;


}

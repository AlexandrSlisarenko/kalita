package ru.slisarenko.kalita.persist.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.slisarenko.kalita.persist.model.Wallet;

public interface WalletRepository extends JpaRepository<Wallet, UUID> {
}

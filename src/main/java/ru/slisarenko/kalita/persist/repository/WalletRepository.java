package ru.slisarenko.kalita.persist.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.slisarenko.kalita.persist.model.Wallet;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, UUID> {
    boolean existsByWalletId(UUID id);
}

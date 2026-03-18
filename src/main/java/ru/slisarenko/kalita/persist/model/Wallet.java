package ru.slisarenko.kalita.persist.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;
import ru.slisarenko.kalita.enums.OperationTypes;

@Entity
@Table(schema = "kalita", name="wallet")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Wallet {

    @Id
    private UUID walletId;

    @Column(name = "operationtype", nullable = false)
    @Enumerated(EnumType.STRING)
    private OperationTypes operationType;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;
}

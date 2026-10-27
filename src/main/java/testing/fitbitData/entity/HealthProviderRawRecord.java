package testing.fitbitData.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "health_provider_raw_records",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_provider_external_id", columnNames = {"provider", "external_id"})
        })
@Getter
@Setter
public class HealthProviderRawRecord {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "patient_id", nullable = false)
    private UUID patientId;

    @Column(name = "provider", nullable = false, length = 32)
    private String provider;

    @Column(name = "data_type", nullable = false, length = 64)
    private String dataType;

    @Column(name = "external_id", nullable = false, length = 256)
    private String externalId;

    @Column(name = "recorded_at")
    private Instant recordedAt;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "payload", nullable = false, columnDefinition = "jsonb")
    private String payload;

    @Column(name = "received_at", nullable = false)
    private Instant receivedAt = Instant.now();
}

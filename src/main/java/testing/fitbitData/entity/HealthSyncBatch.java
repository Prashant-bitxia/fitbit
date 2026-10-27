package testing.fitbitData.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "health_sync_batches",
        indexes = {
                @Index(name = "health_sync_batches_patient_time_idx", columnList = "patient_id, synced_at")
        })
@Getter
@Setter
public class HealthSyncBatch {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "patient_id", nullable = false)
    private UUID patientId;

    @Column(name = "device_id")
    private UUID deviceId;

    @Column(name = "synced_at", nullable = false)
    private Instant syncedAt = Instant.now();

    @Column(name = "platform", nullable = false, length = 32)
    private String platform;

    @Column(name = "app_version", length = 32)
    private String appVersion;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "metrics_sent", nullable = false, columnDefinition = "jsonb")
    private String metricsSent = "[]";

    @Column(name = "record_count", nullable = false)
    private Integer recordCount = 0;

    @Column(name = "accepted_count", nullable = false)
    private Integer acceptedCount = 0;

    @Column(name = "duplicate_count", nullable = false)
    private Integer duplicateCount = 0;

    @Column(name = "rejected_count", nullable = false)
    private Integer rejectedCount = 0;

    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;
}

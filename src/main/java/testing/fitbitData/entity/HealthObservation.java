package testing.fitbitData.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "health_observations",
        indexes = {
                @Index(name = "health_obs_patient_metric_time_idx", columnList = "patient_id, metric_type, recorded_at"),
                @Index(name = "health_obs_received_at_idx", columnList = "patient_id, received_at")
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_health_obs_dedupe",
                        columnNames = {"patient_id", "metric_type", "recorded_at", "source_platform", "external_id"})
        })
@Getter
@Setter
public class HealthObservation {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "patient_id", nullable = false)
    private UUID patientId;

    @Column(name = "metric_type", nullable = false, length = 64)
    private String metricType;

    @Column(name = "value_numeric")
    private Double valueNumeric;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "value_json", columnDefinition = "jsonb")
    private String valueJson;

    @Column(name = "unit", nullable = false, length = 32)
    private String unit;

    @Column(name = "recorded_at", nullable = false)
    private Instant recordedAt;

    @Column(name = "received_at", nullable = false)
    private Instant receivedAt = Instant.now();

    @Column(name = "source_platform", nullable = false, length = 32)
    private String sourcePlatform;

    @Column(name = "source_app", length = 128)
    private String sourceApp;

    @Column(name = "external_id", length = 256)
    private String externalId;

    @Column(name = "device_id")
    private UUID deviceId;

    @Column(name = "sync_batch_id")
    private UUID syncBatchId;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "metadata", nullable = false, columnDefinition = "jsonb")
    private String metadata = "{}";
}

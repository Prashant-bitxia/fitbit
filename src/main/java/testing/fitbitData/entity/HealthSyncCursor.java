package testing.fitbitData.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "health_sync_cursors")
@Getter
@Setter
@IdClass(HealthSyncCursor.HealthSyncCursorId.class)
public class HealthSyncCursor {

    @Id
    @Column(name = "patient_id", nullable = false)
    private UUID patientId;

    @Id
    @Column(name = "metric_type", nullable = false, length = 64)
    private String metricType;

    @Column(name = "last_recorded_at", nullable = false)
    private Instant lastRecordedAt;

    @Column(name = "last_external_id", length = 256)
    private String lastExternalId;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt = Instant.now();

    @Getter
    @Setter
    public static class HealthSyncCursorId implements Serializable {
        private UUID patientId;
        private String metricType;

        public HealthSyncCursorId() {
        }

        public HealthSyncCursorId(UUID patientId, String metricType) {
            this.patientId = patientId;
            this.metricType = metricType;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof HealthSyncCursorId that)) return false;
            return Objects.equals(patientId, that.patientId) &&
                    Objects.equals(metricType, that.metricType);
        }

        @Override
        public int hashCode() {
            return Objects.hash(patientId, metricType);
        }
    }
}
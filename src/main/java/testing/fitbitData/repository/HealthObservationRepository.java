package testing.fitbitData.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import testing.fitbitData.entity.HealthObservation;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface HealthObservationRepository extends JpaRepository<testing.fitbitData.entity.HealthObservation, UUID> {

    boolean existsByPatientIdAndMetricTypeAndRecordedAtAndSourcePlatformAndExternalId(
            UUID patientId,
            String metricType,
            Instant recordedAt,
            String sourcePlatform,
            String externalId
    );

    List<HealthObservation> findByPatientIdAndMetricTypeOrderByRecordedAtDesc(UUID patientId, String metricType);

    Optional<HealthObservation> findTopByPatientIdAndMetricTypeOrderByRecordedAtDesc(UUID patientId, String metricType);

    List<HealthObservation> findByPatientIdOrderByRecordedAtDesc(UUID patientId);
}

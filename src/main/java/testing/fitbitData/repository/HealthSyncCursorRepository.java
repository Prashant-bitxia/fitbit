package testing.fitbitData.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import testing.fitbitData.entity.HealthSyncCursor;

import java.util.List;
import java.util.UUID;

public interface HealthSyncCursorRepository extends JpaRepository<HealthSyncCursor, HealthSyncCursor.HealthSyncCursorId> {

    List<HealthSyncCursor> findByPatientId(UUID patientId);

    boolean existsByPatientIdAndMetricType(UUID patientId, String metricType);
}

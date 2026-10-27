package testing.fitbitData.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import testing.fitbitData.entity.HealthSyncBatch;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface HealthSyncBatchRepository extends JpaRepository<testing.fitbitData.entity.HealthSyncBatch, UUID> {

    List<HealthSyncBatch> findByPatientIdOrderBySyncedAtDesc(UUID patientId);

    Optional<HealthSyncBatch> findTopByPatientIdOrderBySyncedAtDesc(UUID patientId);
}

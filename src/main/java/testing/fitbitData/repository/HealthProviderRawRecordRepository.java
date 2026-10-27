package testing.fitbitData.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import testing.fitbitData.entity.HealthProviderRawRecord;

import java.util.List;
import java.util.UUID;

public interface HealthProviderRawRecordRepository extends JpaRepository<HealthProviderRawRecord, UUID> {

    boolean existsByProviderAndExternalId(String provider, String externalId);

    List<testing.fitbitData.entity.HealthProviderRawRecord> findByPatientIdAndProvider(UUID patientId, String provider);

    List<HealthProviderRawRecord> findByPatientIdAndDataType(UUID patientId, String dataType);
}

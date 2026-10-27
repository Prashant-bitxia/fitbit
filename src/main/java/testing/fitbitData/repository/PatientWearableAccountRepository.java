package testing.fitbitData.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import testing.fitbitData.entity.PatientWearableAccount;

import java.util.Optional;
import java.util.UUID;

public interface PatientWearableAccountRepository extends JpaRepository<PatientWearableAccount, UUID> {

    Optional<testing.fitbitData.entity.PatientWearableAccount> findByPatientIdAndProvider(UUID patientId, String provider);

    boolean existsByPatientIdAndProvider(UUID patientId, String provider);

    Optional<PatientWearableAccount> findByProviderUserIdAndProvider(String providerUserId, String provider);
}

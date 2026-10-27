package testing.fitbitData.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "patient_wearable_accounts",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_patient_provider", columnNames = {"patient_id", "provider"})
        })
@Getter
@Setter
public class PatientWearableAccount {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "patient_id", nullable = false)
    private UUID patientId;

    @Column(name = "provider", nullable = false, length = 32)
    private String provider;

    @Column(name = "provider_user_id", length = 128)
    private String providerUserId;

    @Column(name = "access_token_encrypted", nullable = false, columnDefinition = "TEXT")
    private String accessTokenEncrypted;

    @Column(name = "refresh_token_encrypted", nullable = false, columnDefinition = "TEXT")
    private String refreshTokenEncrypted;

    @Column(name = "token_expires_at", nullable = false)
    private Instant tokenExpiresAt;

    @Column(name = "scope", columnDefinition = "TEXT")
    private String scope;

    @Column(name = "connected_at", nullable = false)
    private Instant connectedAt = Instant.now();

    @Column(name = "last_refreshed_at")
    private Instant lastRefreshedAt;

    @Column(name = "last_sync_at")
    private Instant lastSyncAt;

    @Column(name = "status", nullable = false, length = 32)
    private String status = "active";
}
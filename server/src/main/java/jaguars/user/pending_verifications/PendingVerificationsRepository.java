package jaguars.user.pending_verifications;


import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PendingVerificationsRepository extends CrudRepository<PendingVerifications, Integer> {
    List<PendingVerifications> findByUsername(String username);
    List<PendingVerifications> findByVerify(String verify);
}
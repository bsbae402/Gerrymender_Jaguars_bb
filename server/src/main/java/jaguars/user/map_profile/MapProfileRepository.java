package jaguars.user.map_profile;

import jaguars.sample.Account;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MapProfileRepository extends CrudRepository<MapProfile, Integer> {
    List<MapProfile> findByAccount(Account account);
}

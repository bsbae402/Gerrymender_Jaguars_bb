package jaguars.user;

import jaguars.sample.Account;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MapProfileRepository extends CrudRepository<MapProfile, Integer> {
    public List<MapProfile> findByAccount(Account account);
}

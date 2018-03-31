package jaguars.user;

import jaguars.sample.Account;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Integer> {
    public List<User> findByUsername(String username);
}

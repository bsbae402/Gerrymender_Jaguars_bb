package jaguars.user;

import jaguars.sample.Account;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, String> {
}

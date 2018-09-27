package dbtest;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface StudentRepository extends CrudRepository<Student, Integer> {
    List<Student> findById(Integer id);
}

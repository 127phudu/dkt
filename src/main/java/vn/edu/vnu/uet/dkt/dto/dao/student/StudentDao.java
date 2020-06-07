package vn.edu.vnu.uet.dkt.dto.dao.student;

import vn.edu.vnu.uet.dkt.dto.model.Student;

import java.util.List;

public interface StudentDao {
    Student getByEmail(String email);

    Student getByUsername(String username);
    Student getById(Long id);

    Student save(Student student);

    List<Student> getAll();
}

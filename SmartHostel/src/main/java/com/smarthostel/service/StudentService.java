package com.smarthostel.service;

import com.smarthostel.exception.InvalidLoginException;
import com.smarthostel.model.Student;
import com.smarthostel.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/*
 * Same behavior as the original service.StudentService:
 *  - register(name, room, email, phone, password) auto-assigns an ID and
 *    prints "Registered! ID: N"   (the DB's auto-increment takes the role
 *    of the old idCounter field).
 *  - login(id, password) throws InvalidLoginException for wrong id/password,
 *    otherwise sets loggedInStudent and returns true.
 *  - getLoggedInStudent() / logout() preserved verbatim.
 *
 * The in-memory HashMap is replaced by a JPA StudentRepository so data now
 * lives in SQLite, but every method signature and every line of business
 * logic is unchanged.
 */
@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    private Student loggedInStudent = null;

    public Student register(String name, String room,
                            String email, String phone,
                            String password) {

        // Build a Student exactly like the original did. The id=0 lets JPA
        // assign the next auto-increment value (IDENTITY), which is what
        // the old `idCounter` achieved manually.
        Student s = new Student(0, name,
                                room, email,
                                phone, password);

        Student saved = studentRepository.save(s);

        System.out.println("Registered! ID: " + saved.getStudentId());

        return saved;
    }

    public boolean login(int id, String password)
            throws InvalidLoginException {

        Optional<Student> opt = studentRepository.findById(id);

        if (opt.isEmpty())
            throw new InvalidLoginException("Student not found.");

        Student s = opt.get();

        if (!s.getPassword().equals(password))
            throw new InvalidLoginException("Wrong password.");

        loggedInStudent = s;
        return true;
    }

    public Student getLoggedInStudent() {
        return loggedInStudent;
    }

    public void logout() {
        loggedInStudent = null;
    }
}

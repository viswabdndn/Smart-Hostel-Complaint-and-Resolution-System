package com.smarthostel.controller;

import com.smarthostel.exception.InvalidLoginException;
import com.smarthostel.model.Student;
import com.smarthostel.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/*
 * REST endpoints for the Student side of the old Main.java menu:
 *
 *   "1.Student" -> "1.Register 2.Login"
 *
 * POST /api/students/register     -> studentService.register(...)
 * POST /api/students/login        -> studentService.login(id, password)
 * POST /api/students/logout       -> studentService.logout()
 * GET  /api/students/current      -> studentService.getLoggedInStudent()
 */
@RestController
@RequestMapping("/api/students")
@CrossOrigin(origins = "*")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody Map<String, String> body) {

        String name     = body.get("name");
        String room     = body.get("room");
        String email    = body.get("email");
        String phone    = body.get("phone");
        String password = body.get("password");

        Student s = studentService.register(name, room, email, phone, password);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Registered! ID: " + s.getStudentId());
        response.put("studentId", s.getStudentId());
        response.put("name", s.getName());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, Object> body)
            throws InvalidLoginException {

        int id          = Integer.parseInt(body.get("id").toString());
        String password = body.get("password").toString();

        studentService.login(id, password);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Login Success!");
        response.put("studentId", id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout() {
        studentService.logout();
        Map<String, String> response = new HashMap<>();
        response.put("message", "Logged out.");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/current")
    public ResponseEntity<Student> current() {
        return ResponseEntity.ok(studentService.getLoggedInStudent());
    }
}

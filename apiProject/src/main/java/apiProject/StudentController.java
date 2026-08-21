package apiProject;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private StudentRepository repo;

    @PostMapping
    public Student createStudent(@RequestBody Student student) {
        return repo.save(student);
    }

    @GetMapping
    public List<Student> getAllStudents() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public Student getStudent(@PathVariable Integer id) {
        return repo.findById(id).orElse(null);
    }

    @PutMapping("/{id}")
    public Student updateStudent(@PathVariable Integer id,
                                 @RequestBody Student student) {

        return repo.findById(id)
                .map(existing -> {
                    existing.setName(student.getName());
                    existing.setMarks(student.getMarks());
                    return repo.save(existing);
                })
                .orElse(null);
    }

    @DeleteMapping("/{id}")
    public String deleteStudent(@PathVariable Integer id) {

        if (repo.existsById(id)) {
            repo.deleteById(id);
            return "Student Deleted Successfully";
        }

        return "Student Not Found";
    }
}
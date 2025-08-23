package com.example.demo.Service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.example.demo.Entity.Course;

public interface CourseService {
	Course addCourse(String courseName, String description, MultipartFile syllabusFile) throws IOException;
    List<Course> getAllCourses();
    byte[] getSyllabusFile(int courseId) throws IOException;

}

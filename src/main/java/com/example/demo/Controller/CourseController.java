package com.example.demo.Controller;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.net.URLEncoder;

import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.Entity.Course;
import com.example.demo.Service.CourseService;

@RestController
@RequestMapping("/api/courses")
public class CourseController {
	@Autowired
	private CourseService courseService;

	// Add Course with syllabus PDF
	@PostMapping(consumes = {"multipart/form-data"})
	public ResponseEntity<Course> addCourse(
	        @RequestParam String courseName,
	        @RequestParam String description,
	        @RequestParam("syllabusFile") MultipartFile syllabusFile
	) throws IOException {
	    Course course = courseService.addCourse(courseName, description, syllabusFile);
	    return ResponseEntity.ok(course);
	}

	// Get all courses
	@GetMapping
	public ResponseEntity<List<Course>> getAllCourses() {
	    return ResponseEntity.ok(courseService.getAllCourses());
	}

	// Download syllabus PDF
	@GetMapping("/{id}/syllabus")
	public ResponseEntity<byte[]> downloadSyllabus(@PathVariable int id) throws IOException {
	    byte[] fileData = courseService.getSyllabusFile(id);
	    // Download filename सेट करा (syllabus.pdf किंवा course नावावरून)
	    String downloadName = URLEncoder.encode("syllabus.pdf", StandardCharsets.UTF_8).replace("+", "%20");

	    return ResponseEntity.ok()
	            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + downloadName + "\"")
	            .contentType(MediaType.APPLICATION_PDF)
	            .contentLength(fileData.length)
	            .body(fileData);
	}
}

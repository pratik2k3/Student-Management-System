package com.example.demo.Service;
import java.nio.file.Path;   // ✅ Correct import
import java.nio.file.Paths;  // ✅ Needed
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.Entity.Course;
import com.example.demo.Repository.CourseRepository;

import org.springframework.beans.factory.annotation.Value;
@Service
public class CourseServiceImpl implements CourseService {
	@Autowired
	private CourseRepository courseRepository;
	@Value("${file.upload-dir}")
	private String uploadDir;

	private Path getBaseDir() throws IOException {
	    Path base = Paths.get(uploadDir).toAbsolutePath().normalize();
	    Files.createDirectories(base);
	    return base;
	}
	@Override
	public Course addCourse(String courseName, String description, MultipartFile syllabusFile) throws IOException {
		// TODO Auto-generated method stub
		 if (syllabusFile == null || syllabusFile.isEmpty()) {
		        throw new IllegalArgumentException("Syllabus PDF is required");
		    }

		    // Basic content-type/extension check
		    String original = syllabusFile.getOriginalFilename();
		    String lower = original != null ? original.toLowerCase() : "";
		    if (!(lower.endsWith(".pdf") || "application/pdf".equalsIgnoreCase(syllabusFile.getContentType()))) {
		        throw new IllegalArgumentException("Only PDF files are allowed for syllabus");
		    }

		    Path base = getBaseDir();

		    // Generate safe unique filename
		    String storedName = UUID.randomUUID().toString() + ".pdf";
		    Path target = base.resolve(storedName).normalize();

		    // Copy file to E:/javaSyllabus/uploadedfile
		    Files.copy(syllabusFile.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

		    // Save only relative filename in DB
		    Course course = new Course();
		    course.setCourseName(courseName);
		    course.setDescription(description);
		    course.setSyllabusPath(storedName);

		    return courseRepository.save(course);
		
		
	
	}

	@Override
	public List<Course> getAllCourses() {
		// TODO Auto-generated method stub
		return courseRepository.findAll();
	}

	@Override
	public byte[] getSyllabusFile(int courseId) throws IOException {
		// TODO Auto-generated method stub
		Course course = courseRepository.findById(courseId)
	            .orElseThrow(() -> new RuntimeException("Course not found"));
	    if (course.getSyllabusPath() == null || course.getSyllabusPath().isEmpty()) {
	        throw new RuntimeException("Syllabus not uploaded for this course");
	    }
	    Path base = getBaseDir();
	    Path file = base.resolve(course.getSyllabusPath()).normalize();
	    if (!Files.exists(file)) {
	        throw new RuntimeException("Syllabus file not found on disk");
	    }
	    return Files.readAllBytes(file);
	}

}

package com.fis.framework.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fis.framework.entities.Course;
import com.fis.framework.repositories.CourseRepository;

@Service
public class CourseService {
	private final CourseRepository courseRepository;

	@Autowired
	public CourseService(CourseRepository courseRepository) {
		this.courseRepository = courseRepository;
	}

	// Get All Users
	public List<Course> getAllCourses() {
		return courseRepository.findAll();
	}

	public void saveCourse(Course course) {
		this.courseRepository.save(course);
	}

	public Course getCourseById(long id) {
		Optional<Course> existCourse = courseRepository.findById(id);
		Course course = existCourse.get();
		return course;
	}

	public void deleteCourseById(long id) {
		this.courseRepository.deleteById(id);
	}

}

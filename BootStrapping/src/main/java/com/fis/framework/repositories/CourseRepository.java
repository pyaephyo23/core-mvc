package com.fis.framework.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.fis.framework.entities.Course;

public interface CourseRepository extends JpaRepository<Course, Long> {

	@Query("select course_code from Course")
	List<String> findCourseCode();

	@Query("select c from Course c where c.course_code = ?1")
	Course findByCourseCode(String code);

}

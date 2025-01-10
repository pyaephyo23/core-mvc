package com.fis.framework.controllers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.fis.framework.entities.Course;
import com.fis.framework.repositories.CourseRepository;
import com.fis.framework.services.CourseService;

@Controller
public class FileUploadDownloadController {
	List<List<String>> uploadData = new ArrayList<>();

	@GetMapping("/upload_form")
	public String uploadForm() {
		return "upload_form";
	}

	@SuppressWarnings("deprecation")
	@PostMapping("/upload")
	public String uploadFile(@RequestParam("file") MultipartFile file, Model model) throws IOException {

		try (InputStream inputStream = file.getInputStream()) {

			Workbook workbook = WorkbookFactory.create(inputStream);
			Sheet sheet = workbook.getSheetAt(0);

			List<List<String>> excelData = new ArrayList<>();

			for (Row row : sheet) {
				List<String> rowData = new ArrayList<>();
				for (Cell cell : row) {
					cell.setCellType(CellType.STRING);
					rowData.add(cell.getStringCellValue());
				}
				excelData.add(rowData);
			}
			uploadData = excelData;
			model.addAttribute("filename", file.getOriginalFilename());
			model.addAttribute("excelData", excelData);
		} catch (IOException e) {
			// Handle exceptions
		}
		return "upload_form";
	}

	@Autowired
	private CourseRepository courseRepository;

	@PostMapping("/savefile")
	public String saveFile(Model m) {
		List<Course> courses = new ArrayList<>();
		for (List<String> rowData : uploadData) {
			if (rowData.get(1).equals("Code") == false) {
				Course course = new Course();
				course.setCourse_code(rowData.get(1));
				course.setCourse_name(rowData.get(2));
				course.setCourse_des(rowData.get(3));
				course.setFaculty(rowData.get(4));
				courses.add(course);
			}
		}
		courseRepository.saveAll(courses);

		uploadData.clear();
		return "upload_form";

	}

	private final CourseService courseService;

	@Autowired
	public FileUploadDownloadController(CourseService courseService) {
		this.courseService = courseService;
	}

	@GetMapping("/download_form")
	public String listcourses(Model model, @RequestParam(defaultValue = "0") int page) {
		List<Course> courselist = courseService.getAllCourses();
		model.addAttribute("courseData", courselist);
		return "download_form";
	}

	@GetMapping("/showNewCourseForm")
	public String showNewCourseForm(Model model) {
		// create model attribute to bind form data
		Course course = new Course();
		model.addAttribute("course", course);
		return "addNewCourse_form";
	}

	@GetMapping("/showFormForUpdate/{id}")
	public String showFormForUpdate(@PathVariable(value = "id") long id, Model model) {

		// get employee from the service
		Course course = courseService.getCourseById(id);

		// set employee as a model attribute to pre-populate the form
		model.addAttribute("course", course);
		return "update_course";
	}

	@PostMapping("/saveCourse")
	public String saveEmployee(@ModelAttribute("course") Course course) {
		// save employee to database
		courseService.saveCourse(course);
		return "redirect:/download_form";
	}

	@GetMapping("/deleteCourse/{id}")
	public String deleteCourse(@PathVariable(value = "id") long id) {
		// call delete employee method
		this.courseService.deleteCourseById(id);
		return "redirect:/download_form";
	}

	@GetMapping("/download")
	public ResponseEntity<Resource> downloadExcel() {

		List<Course> courselist = courseService.getAllCourses();

		// Generate Excel file
		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("Course");
		Row row = sheet.createRow(0);
		row.createCell(0).setCellValue("ID");
		row.createCell(1).setCellValue("Code");
		row.createCell(2).setCellValue("Name");
		row.createCell(3).setCellValue("Description");
		row.createCell(4).setCellValue("Faculty");
		int rowNum = 1;
		for (Course data : courselist) {
			row = sheet.createRow(rowNum++);
			row.createCell(0).setCellValue(data.getId());
			row.createCell(1).setCellValue(data.getCourse_code());
			row.createCell(2).setCellValue(data.getCourse_name());
			row.createCell(3).setCellValue(data.getCourse_des());
			row.createCell(4).setCellValue(data.getFaculty());
		}

		// Prepare Excel file for download
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try {
			workbook.write(outputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Return Excel file for download
		byte[] excelBytes = outputStream.toByteArray();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "attachment; filename=course.xlsx");
		return ResponseEntity.ok().headers(headers)
				.contentType(
						MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
				.body(new ByteArrayResource(excelBytes));

	}

	@ModelAttribute
	public void CourseCode(Model m) {
		List<String> code = courseRepository.findCourseCode();
		m.addAttribute("courseCode", code);
	}

	@GetMapping("/course")
	public String courseInformation(Model m) {
		Course course = new Course();
		m.addAttribute("course", course);
		return "course_form";
	}

	@PostMapping("/course")
	public String showCourse(@ModelAttribute("course") Course course, Model m) {

		Course courseDetail = courseRepository.findByCourseCode(course.getCourse_code());
		m.addAttribute("courseDetail", courseDetail);
		return "course_form";
	}

}

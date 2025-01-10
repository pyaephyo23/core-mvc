package com.fis.framework.entities;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Table(name = "candidates")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 40)
	@Size(min = 3, max = 40)
	private String name;

	@Column(nullable = false, unique = true, length = 45)
	@NotBlank
	@Email(message = "Please enter a valid e-mail address")
	private String email;

	@Column(nullable = false, length = 64)
	@NotBlank
	private String password;

	@Column(nullable = false)
	@NotBlank
	private String gender;

	@Column(nullable = true)
	@Size(max = 10, message = "note cannot exceed 10 characters")
	private String note;

	@Column(nullable = false)
	// @AssertTrue
	private boolean married;

	@Column(nullable = false)
	@DateTimeFormat(pattern = "yyyy-mm-dd")
	private Date birthday;

	@Column(nullable = false)
	@NotBlank
	private String profession;

	/*
	 * @Min(value = 20_000)
	 * 
	 * @Max(value = 200_000) private long income;
	 */

	// getters and setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public boolean isMarried() {
		return married;
	}

	public void setMarried(boolean married) {
		this.married = married;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getProfession() {
		return profession;
	}

	public void setProfession(String profession) {
		this.profession = profession;
	}

}

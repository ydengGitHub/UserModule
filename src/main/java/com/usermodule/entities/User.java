package com.usermodule.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;


/*change the table to usr instead of user, because user is MySQL keyword
 *create a index with "email", and set it as unique*/

@Entity
@Table(name="usr", indexes = {
		@Index(columnList = "email", unique=true)
})
public class User {
	public static final int EMAIL_MAX=250;
	public static final int NAME_MAX=50;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	
	@Column(nullable = false, length = EMAIL_MAX)
	private String email;
	
	@Column(nullable = false, length = NAME_MAX)
	private String name;
	
	//no length because it will be encrypted
	@Column(nullable = false)
	private String password;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}

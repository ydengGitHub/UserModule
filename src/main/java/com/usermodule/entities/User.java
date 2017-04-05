package com.usermodule.entities;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
	
	public static enum Role{
		UNVERIFIED, BLOCKED, ADMIN
	}
	
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
	
	//Hold a Randomly generated 16 characters verification code
	@Column(length=16)
	private String verificationCode;

	public String getVerificationCode() {
		return verificationCode;
	}

	public void setVerificationCode(String verificationCode) {
		this.verificationCode = verificationCode;
	}

	/*An ElementCollection can be used to define a one-to-many relationship to an Embeddable object, 
	 * or a Basic value (such as a collection of Strings).*/
	/*The ElementCollection values are always stored in a separate table.*/
	@ElementCollection(fetch=FetchType.EAGER)
	private Set<Role> roles=new HashSet<>();
	
	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

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

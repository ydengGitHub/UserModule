package com.usermodule.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.usermodule.entities.User;

/*Repository interface to let spring to create the repository to access the Database*/
public interface UserRepository extends JpaRepository<User, Long> {
	
}

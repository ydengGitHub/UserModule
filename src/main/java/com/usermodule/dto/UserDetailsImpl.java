package com.usermodule.dto;

import java.util.Collection;
import java.util.HashSet;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.usermodule.entities.User;
import com.usermodule.entities.User.Role;

public class UserDetailsImpl implements UserDetails {
	
	/**
	 * Since UserDetails is Serializable, UserDetailsImpl should also be
	 */
	private static final long serialVersionUID = 2728050777976360974L;
	private User user;

	public UserDetailsImpl(User user) {
		this.user=user;
	}

	/*Matched rows, return all the roles the user having*/
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> authorities = new HashSet<GrantedAuthority>(
				user.getRoles().size() + 1);

		for (Role role : user.getRoles())
			authorities.add(new SimpleGrantedAuthority("ROLE_" + role.name()));

		authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

		return authorities;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}

package com.buildazan.config;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.buildazan.entities.User;
import com.buildazan.enums.MemberShipLevel;
import com.buildazan.enums.SubscriptionStatus;


public class UserDetailsImpl implements UserDetails{
	private User user;
	
	public UserDetailsImpl(User user) {
		this.user = user;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		SimpleGrantedAuthority authorities = new SimpleGrantedAuthority(user.getUserRole().toString());
		return List.of(authorities);
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getUsername();
	}

	public String getEmail() {
		return user.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		 return user.isAccountNonExpired();
	}

	@Override
	public boolean isAccountNonLocked() {
		 return user.isAccountNonLocked();
	}

	@Override
	public boolean isCredentialsNonExpired() {
		 return user.isCaredentialsNonExpired();
	}

	@Override
	public boolean isEnabled() {
		 return user.isAccountEnabled();
	}

	public String getFirstName(){
		return user.getFirstName();
	}
	
	public MemberShipLevel getMemberShipLevel() {
		return user.getMemberShipLevel();
	}
	
	public SubscriptionStatus getSubscriptionStatus() {
		return user.getSubscriptionStatus();
	}
	
	public boolean isEmailVerified() {
		return user.isEmailVerified();
	}

	public String getUserId(){
		return user.getId();
	}

}

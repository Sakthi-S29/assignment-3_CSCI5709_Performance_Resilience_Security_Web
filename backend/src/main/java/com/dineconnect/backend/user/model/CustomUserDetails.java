package com.dineconnect.backend.user.model;

import org.springframework.security.core.userdetails.UserDetails;
import com.dineconnect.backend.user.model.Role;

public interface CustomUserDetails extends UserDetails {

    String getEmail();

    String getId();

    Role getRole();

}

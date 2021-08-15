package com.example.petclinicsf5.config.securityPermissions;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("hasAuthority('owner.create')")
public @interface CreateOwnerPermission {
}

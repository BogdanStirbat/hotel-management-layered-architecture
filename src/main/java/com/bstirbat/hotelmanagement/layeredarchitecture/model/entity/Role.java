package com.bstirbat.hotelmanagement.layeredarchitecture.model.entity;

import java.util.Set;

public enum Role {
  ADMIN(Set.of(Permission.ADMIN, Permission.STAFF, Permission.CLIENT)),

  STAFF(Set.of(Permission.STAFF, Permission.CLIENT)),

  CLIENT(Set.of(Permission.CLIENT));

  private final Set<Permission> permissions;

  Role(Set<Permission> permissions) {
    this.permissions = permissions;
  }

  public Set<Permission> getPermissions() {
    return permissions;
  }
}

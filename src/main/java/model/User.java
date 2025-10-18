package model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import java.util.HashSet;
import java.util.Set;


public class User {

  final String id;
  final String name;
  final String tenenantId;
  final Set<Role> roles = new HashSet<>();

  User(String id, String name,String tenenantId, Set<Role> roles ) {
    this.id= id;
    this.name = name;
    this.tenenantId = tenenantId;
    roles.addAll(roles);
  }

  public String getTenenantId() {
    return tenenantId;
  }

  public Set<Role>  getRoles() {
    return roles;
  }




}

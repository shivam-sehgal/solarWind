package model;

import java.util.Optional;

public class StepSpec {

  final String name;
  public final Role roleRequired;
  final Optional<String> condition;

  StepSpec(String name, Role roleRequired, String condition) {
    this.name = name;
    this.roleRequired = roleRequired;
    this.condition = Optional.ofNullable(condition);
  }




}

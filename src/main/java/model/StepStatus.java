package model;

public class StepStatus {

  public StepSpec stepSpec;
  User assignedTo;
  Action userAction;

  public void setAssignedTo(User user) {
    this.assignedTo=user;
  }

}

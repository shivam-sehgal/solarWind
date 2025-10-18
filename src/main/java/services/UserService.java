package services;

import java.util.List;
import model.ExpenseRequest;
import model.StepStatus;
import model.User;

public class UserService {

  static List<User> users;


  /**
   * Method that assigns request step to user based on tennat
   * condition check is missing if condition is not true i.e approval is not required step shoudl be auto approved thinking of using java expression language library
   * to parse conditions from json workflows
   * @param expenseRequest request for which step approval required
   * @param stepStatus step for which approval required
   *
   *   TO-DO: USERrequest class having map of userid->list of request needing approval as  well as other maps to show userid to request rejected or approved
   */


  public static void assignUser(ExpenseRequest expenseRequest, StepStatus stepStatus) {
    User requestor = expenseRequest.requestor;
    for(User user: users) {
      if(user!=requestor && user.getTenenantId().equals(requestor.getTenenantId())&& user.getRoles().contains(stepStatus.stepSpec.roleRequired)) {
        stepStatus.setAssignedTo(user);
        return;
      }

    }

    throw new RuntimeException("No user found to whom request can be assigned");

  }



}

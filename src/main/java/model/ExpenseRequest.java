package model;

import java.util.ArrayList;
import java.util.List;
import services.UserService;
import util.WorkFlowUtil;

public class ExpenseRequest {

  final String requestId;
  final String tennatId;
  public final User requestor;
  final double amount;
  List<StepStatus> stepStatuses;

  RequestStatus requestStatus;


  final WorkFlowTemplate workFlowTemplate;

  ExpenseRequest( String requestId, String tennatId, User requestor, double amount) {
    this.requestId = requestId;
    this.tennatId = tennatId;
    this.requestor = requestor;
    this.amount = amount;
    this. workFlowTemplate = WorkFlowUtil.loadTemplateFromTenant(tennatId);
    stepStatuses = new ArrayList<>();
  }




  public void updateStepStatus(StepStatus stepStatus, Action userAction, User user){

    if(user!=stepStatus.assignedTo)
      throw  new RuntimeException("not correct user for action");
    stepStatus.userAction = userAction;
    executeTemplate();
  }

  /**
   * main executor should be added in the Requestexecutor service but due to lack of time written here
   */

  public void executeTemplate() {

    List<StepSpec> stepSpecs = workFlowTemplate.steps;

    for(StepSpec stepSpec: stepSpecs) {
      if(shouldProcess(stepSpec)) {
        StepStatus stepStatus = getStepStatus(stepSpec);
        boolean isAssigned = assignIfRequired(stepStatus);
        if(isAssigned)
          break;
      }
    }
    // if all steps executed succesfully we will mark request status as done else pending or rejected if any step status is rejected
    processFinalStatus();


  }

  /**
   * PROCESSING THE FINAL STTAUS OF REQUEST
   */
  private void processFinalStatus() {
    List<StepSpec> stepSpecs = workFlowTemplate.steps;
    for(StepSpec stepSpec: stepSpecs) {
        StepStatus stepStatus = getStepStatus(stepSpec);
        if(stepStatus.userAction==Action.IN_REVIEW) {
          this.requestStatus = RequestStatus.PENDING;
          return;
        }
      if(stepStatus.userAction==Action.REJECT) {
        this.requestStatus = RequestStatus.REJECTED;
        return;
      }

    }
    this.requestStatus = RequestStatus.APPROVED;

  }





  private boolean assignIfRequired(StepStatus stepStatus) {
    if(stepStatus.assignedTo!=null) {
      System.out.println("step is already assigned to user");
      return false;
    }
    UserService.assignUser(this, stepStatus);
    return true;
  }

  private StepStatus getStepStatus(StepSpec stepSpec) {
    for (StepStatus stepStatus : stepStatuses) {
      if (stepStatus.stepSpec.name.equals(stepSpec.name)) {
        return stepStatus;
      }

    }

    throw new RuntimeException("this should never be haappening");

  }


  private boolean shouldProcess(StepSpec stepSpec) {

    for(StepStatus stepStatus: stepStatuses) {
      if(stepStatus.stepSpec.name.equals(stepSpec.name)) {
        if(stepStatus.userAction!=null && stepStatus.userAction!=Action.IN_REVIEW)
          return false;
        else
          return true;
      }
    }

    StepStatus stepStatus = new StepStatus();
    stepStatus.stepSpec = stepSpec;
    stepStatuses.add(stepStatus);

    return  true;
  }












}

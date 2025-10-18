package model;

import java.util.ArrayList;
import java.util.List;
import util.WorkFlowUtil;

public class ExpenseRequest {

  final String requestId;
  final String tennatId;
  final User requestor;
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

    setFinalStatus();


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

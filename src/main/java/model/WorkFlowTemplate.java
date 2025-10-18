package model;

import java.util.List;

public class WorkFlowTemplate {

  final String id;
  final String name;
  final List<StepSpec> steps;
  WorkFlowTemplate(String id, String name, List<StepSpec> stepSpecs) {
    this.id = id;
    this.name = name;
    this.steps = stepSpecs;
  }


}

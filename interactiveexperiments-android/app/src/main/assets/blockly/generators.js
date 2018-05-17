Blockly.JavaScript['set_block'] = function(block) {
  var value_set_value = Blockly.JavaScript.valueToCode(block, 'set_value', Blockly.JavaScript.ORDER_ATOMIC);
  // TODO: Assemble JavaScript into code variable.
  var code = 'SET';
  return code;
};

Blockly.JavaScript['pulley_block'] = function(block) {
  // TODO: Assemble JavaScript into code variable.
  var code = 'Pulley-Experiment';
  // TODO: Change ORDER_NONE to the correct strength.
  return code;
};

Blockly.JavaScript['inclinedplane_block'] = function(block) {
  // TODO: Assemble JavaScript into code variable.
  var code = 'Inclined-Plane-Experiment';
  // TODO: Change ORDER_NONE to the correct strength.
  return code;
};

Blockly.JavaScript['start_block'] = function(block) {
  // TODO: Assemble JavaScript into code variable.
  var code = 'START-';
  return code;
};

Blockly.JavaScript['set_experiment_block'] = function(block) {
  var value_set_experiment_value = Blockly.JavaScript.valueToCode(block, 'set_experiment_value', Blockly.JavaScript.ORDER_ATOMIC);
  // TODO: Assemble JavaScript into code variable.
  var code = 'SET-EXPERIMENT';
  return code + value_set_experiment_value;
};

Blockly.JavaScript['end_block'] = function(block) {
  // TODO: Assemble JavaScript into code variable.
  var code = 'END-';
  return code;
};
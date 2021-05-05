/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Switches to draft state of a specified ticket tag.
 * @author Patryk
 */
public class ManageCommand implements Command {

  private final ClientModel model;
  private final String[] args;
  private String error = "";
  
  public ManageCommand(ClientModel model, String[] args) {
    this.model = model;
    this.args = args;
  }
  
  @Override
  public void execute() {
    // Validate args
    if (args.length != 0) {
      // Set Drafting state
      this.model.setState(ClientModel.State.Drafting);
      // Set tag
      this.model.setDraftTag(this.args[0]);
    } else {
      this.error = this.model.getStrings().getString("tag_missing");
    }
  }

  @Override
  public String getStringResponse() {
    if (!this.error.isEmpty()) {
      return this.error;
    }
    return "";
  }
  
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Discards currently drafted lines and returns to the Main state.
 * @author Patryk
 */
public class DiscardCommand implements Command {

  private final ClientModel model;
  
  public DiscardCommand(ClientModel model) {
    this.model = model;
  }
  
  @Override
  public void execute() {
    this.model.clearDraftLines();
    this.model.setState(ClientModel.State.Main);
  }

  @Override
  public String getStringResponse() {
    return "";
  }
  
}

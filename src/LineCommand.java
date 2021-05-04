/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Patry
 */
public class LineCommand implements Command {

  public ClientModel model;
  public String[] args;
  
  public LineCommand(ClientModel model, String[] args) {
    this.model = model;
    this.args = args;
  }
  
  @Override
  public void execute() {
    String line = String.join(" ", this.args);
    this.model.addDraftLine(line);
  }

  @Override
  public String getStringResponse() {
    return "";
  }
  
}

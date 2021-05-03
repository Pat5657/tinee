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

  public Client client;
  public String[] args;
  
  public LineCommand(Client client, String[] args) {
    this.client = client;
    this.args = args;
  }
  
  @Override
  public void execute() {
    String line = String.join(" ", this.args);
    this.client.addDraftLine(line);
  }
  
}

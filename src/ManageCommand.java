/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Patry
 */
public class ManageCommand implements Command {

  private Client client;
  private String tag;
  
  public ManageCommand(Client client, String tag) {
    this.client = client;
    this.tag = tag;
  }
  
  @Override
  public void execute() {
    this.client.setState(Client.State.Drafting);
    this.client.setDraftTag(this.tag);
  }
  
}

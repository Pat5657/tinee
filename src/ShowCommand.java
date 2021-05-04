
import java.io.IOException;
import sep.tinee.net.message.ShowReply;
import sep.tinee.net.message.ShowRequest;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Patry
 */
public class ShowCommand implements Command {

  private ClientModel model;
  
  public ShowCommand(ClientModel model) {
    this.model = model;
  }
  
  @Override
  public void execute() {
    try {
      // Send Show request
      this.model.getChan().send(new ShowRequest());
      // Get show reply
      ShowReply rep = (ShowReply) this.model.getChan().receive();
      // Output
      System.out.println(CLFormatter.formatShow(rep.tags));
    } catch (IOException | ClassNotFoundException e) {
      System.out.println(e.getMessage());
    }
  }
  
}

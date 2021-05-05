
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
  private ShowReply response;
  private String error = "";
  
  public ShowCommand(ClientModel model) {
    this.model = model;
  }
  
  @Override
  public void execute() {
    try {
      // Send Show request
      this.model.getChan().send(new ShowRequest());
      // Get show reply
      this.response = (ShowReply) this.model.getChan().receive();
    } catch (IOException | ClassNotFoundException e) {
      this.error = e.getMessage();
    }
  }

  @Override
  public String getStringResponse() {
    if (this.error.isEmpty()) {
      return this.model.getClf().formatShow(this.response.tags);
    } else {
      return this.error;
    }
  }
  
}

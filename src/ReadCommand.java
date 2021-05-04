
import java.io.IOException;
import sep.tinee.net.message.ReadReply;
import sep.tinee.net.message.ReadRequest;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Patry
 */
public class ReadCommand implements Command {

  private final ClientModel model;
  private final String[] args;
  private ReadReply response;
  private String error = "";
  
  public ReadCommand(ClientModel model, String[] args) {
    this.model = model;
    this.args = args;
  }
  
  @Override
  public void execute() {
    try {
      // Validate arguments
      if (this.args.length != 0) {
        // Send Read request
        this.model.getChan().send(new ReadRequest(this.args[0]));
        // Get response from server
        this.response = (ReadReply) this.model.getChan().receive();
      } else {
        this.error = "Tag is missing.";
      }
    } catch (IOException | ClassNotFoundException e) {
      this.error = e.getMessage();
    }
  }

  @Override
  public String getStringResponse() {
    if (this.error.isEmpty()) {
      return this.model.getClf().formatRead(this.args[0], this.response.users, this.response.lines);
    } else {
      return this.error;
    }
  }
  
}

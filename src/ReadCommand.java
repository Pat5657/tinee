
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
  
  public ReadCommand(ClientModel model, String[] args) {
    this.model = model;
    this.args = args;
  }
  
  @Override
  public void execute() {
    try {
      // Validate arguments
      if (this.args.length != 0) {
        // Define tag
        String tag = this.args[0];
        // Send Read request
        this.model.getChan().send(new ReadRequest(tag));
        // Get response from server
        ReadReply rep = (ReadReply) this.model.getChan().receive();
        // Output response
        System.out.print(CLFormatter.formatRead(tag, rep.users, rep.lines));
      } else {
        System.out.println("Tag is missing.");
      }
    } catch (IOException | ClassNotFoundException e) {
      System.out.println(e.getMessage());
    }
  }
  
}


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

  private Client client;
  private String tag;
  
  public ReadCommand(Client client, String[] args) {
    this.client = client;
    this.tag = args[0];
  }
  
  @Override
  public void execute() {
    try {
      this.client.getChan().send(new ReadRequest(this.tag));
      ReadReply rep = (ReadReply) this.client.getChan().receive();
      System.out.print(CLFormatter.formatRead(this.tag, rep.users, rep.lines));
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }
  
}

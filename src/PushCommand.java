
import java.io.IOException;
import java.util.List;
import sep.tinee.net.message.Push;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Patry
 */
public class PushCommand implements Command {
  
  private Client client;
  private String user;
  private String draftTag;
  private List<String> draftLines;
  
  public PushCommand(Client client, String user, String draftTag, List<String> draftLines) {
    this.client = client;
    this.user = user;
    this.draftTag = draftTag;
    this.draftLines = draftLines;
  }

  @Override
  public void execute() {
    try {
      this.client.getChan().send(new Push(this.user, this.draftTag, this.draftLines));
      this.client.setState(Client.State.Main);
      this.client.setDraftTag(null);
    } catch (IOException ex) {
//      Logger.getLogger(PushCommand.class.getName()).log(Level.SEVERE, null, ex);
    }
  }
  
  
}

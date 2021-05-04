
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
  
  private ClientModel model;
  
  public PushCommand(ClientModel model) {
    this.model = model;
  }

  @Override
  public void execute() {
    try {
      // If user provided drafter lines. 
      if (!this.model.getDraftLines().isEmpty()) {
        // Send Push request
        this.model.getChan().send(new Push(this.model.getUser(), this.model.getDraftTag(), this.model.getDraftLines()));
        // Clear draft lines
        this.model.clearDraftLines();
      }
      // Set Main state
      this.model.setState(ClientModel.State.Main);
      // Clear draft tag
      this.model.setDraftTag(null);
    } catch (IOException ex) {
//      Logger.getLogger(PushCommand.class.getName()).log(Level.SEVERE, null, ex);
    }
  }
  
  
}

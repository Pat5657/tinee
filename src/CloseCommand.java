
import java.io.IOException;
import java.util.Arrays;
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
public class CloseCommand implements Command {

  private ClientModel model;
  
  public CloseCommand(ClientModel model) {
    this.model = model;
  }
  
  @Override
  public void execute() {
    try {
      this.model.getChan().send(
        // Send close Push request
        new Push(
          this.model.getUser(), 
          this.model.getDraftTag(), 
          Arrays.asList(Push.CLOSE_LINE)
        )
      );
      // Set Main state
      this.model.setState(ClientModel.State.Main);
      // Clear draft tag
      this.model.setDraftTag(null);
    } catch (IOException e) {
      System.err.println(e.getMessage());
    }
  }
  
}

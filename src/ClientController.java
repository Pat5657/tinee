
import java.io.IOException;
import sep.mvc.AbstractController;
import sep.mvc.AbstractModel;
import sep.mvc.AbstractView;
import sep.tinee.net.channel.ClientChannel;
import sep.tinee.net.message.Bye;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Client Controller for handling the client view and model.
 * @author Patryk
 */
public class ClientController extends AbstractController {

  public ClientController(AbstractModel model, AbstractView view) {
    super(model, view);
  }

  /**
   * Initialise the view and set its model. 
   * Start up the view.
   */
  public void run() {
    // Initialise view
    this.getView().init();
    // Define model
    this.getView().update();
    try {
      // Start the view
      this.getView().run();
    } catch (IOException ex) {
      System.err.println(ex.getMessage());
    }
  }

  @Override
  public void shutdown() {
    // Close view
    this.getView().close();
    // Get client commuication
    ClientChannel cc = ((ClientModel) this.getModel()).getChan();
    // Close client-server communication
    if (cc.isOpen()) {
      try {
        // If the channel is open, send Bye and close
        cc.send(new Bye());
        cc.close();
      } catch (IOException ex) {
        System.err.println(ex.getMessage());
      }
    }
  }
}

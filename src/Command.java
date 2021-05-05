/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Interface for creating commands that provide functionality to the client user.
 * An additional get function can be implemented to return responses of a 
 * specific type to the GUI implementation.
 * @author Patryk
 */
public interface Command {
  
  /**
   * Executes the intended functionality of the command.
   */
  public void execute();
  
  /**
   * Returns a response after executing the command in a string format.
   * String response is intended to be handled by a CLI view.
   * @return
   */
  public String getStringResponse();
}


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import sep.mvc.AbstractView;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Patry
 */
public class CommandLineView extends AbstractView {
  
  private ClientModel model;
  private BufferedReader reader = null;
  
  @Override
  public void close() {
    // Close reader
    if (this.reader != null) {
      try {
        this.reader.close();
      } catch (IOException ex) {
        System.err.println(ex.getMessage());
      }
    }
  }

  @Override
  public void run() throws IOException {
    try {
      // Init reader
      reader = new BufferedReader(new InputStreamReader(System.in));
      // Validate user and host
      if (this.model.getUser().isEmpty() || this.model.getHost().isEmpty()) {
        System.err.println(this.model.getStrings().getString("user_host_not_set"));
        System.exit(1);
      }
      // Print splash screen
      this.printSplash();
      // Begin loop
      this.loop();
    } catch (Exception e) {
      throw new IOException(e.getMessage());
    } finally {
      // Exit application
      this.getController().shutdown();
    }
  }

  @Override
  public void update() {
    this.model = (ClientModel) this.getModel();
  }

  private void printSplash() {
    // If splash screen is enabled
    if (this.model.getPrintSplash() == true) {
      System.out.print(this.model.getClf().formatSplash(this.model.getUser()));
    }
  }
  
  private void loop() throws IOException {
    // Define command
    Command command;
    
    // The loop
    for (boolean done = false; !done;) {
      // Clear command
      command = null;
      // Print user options
      this.printOptions();
      // Read a line of user input
      String raw = reader.readLine();
      if (raw == null) {
        throw new IOException(this.model.getStrings().getString("stream_closed"));
      }
      // Trim leading/trailing white space, and split words according to spaces
      List<String> split = Arrays.stream(raw.trim().split("\\ "))
          .map(x -> x.trim()).collect(Collectors.toList());
      // First word is the command keyword
      String cmd = split.remove(0);
      // Remainder, if any, are arguments
      String[] rawArgs = split.toArray(new String[split.size()]);
      
      // Process user input
      if (this.model.getStrings().getString("exit_cmd").equals(cmd)) {
        // exit command applies in either state
        done = true;
        // Shutdown application
        this.getController().shutdown();
      } else if (this.model.getState() == ClientModel.State.Main) {
        // Main state commands
        command = this.handleMainState(cmd, rawArgs);
      } else if (this.model.getState() == ClientModel.State.Drafting) {
        // Drafting state commands
        command = this.handleDraftingState(cmd, rawArgs);
      } else {
        this.printInvalidCommand();
      }
      // Execute the command if it has been set.
      if (command != null) {
        command.execute();
        if (!command.getStringResponse().isEmpty()) {
          System.out.println(command.getStringResponse());
        }
      }
    }
  }
  
  private Command handleMainState(String cmd, String[] rawArgs) {
    // Switch to "Drafting" state and start a new "draft"
    if (cmd.equals(this.model.getStrings().getString("manage_cmd"))) {
      return new ManageCommand(this.model, rawArgs);
    } // Read tines on server
    else if (cmd.equals(this.model.getStrings().getString("read_cmd"))) {
      return new ReadCommand(this.model, rawArgs);
    } // Show tags and authers on server
    else if (cmd.equals(this.model.getStrings().getString("show_cmd"))) {
      return new ShowCommand(this.model);
    } 
    // Command not valid
    this.printInvalidCommand();
    return null;
  }
  
  private Command handleDraftingState(String cmd, String[] rawArgs) {
    // Add a tine message line
    if (cmd.equals(this.model.getStrings().getString("line_cmd"))) {
      return new LineCommand(this.model, rawArgs);
    } // Send drafted tines to the server, and go back to "Main" state
    else if (cmd.equals(this.model.getStrings().getString("push_cmd"))) {
      return new PushCommand(this.model);
    } // Close ticket if user is auther
    else if (cmd.equals(this.model.getStrings().getString("close_cmd"))) {
      return new CloseCommand(this.model);
    } // Discard all drafted tines
    else if (cmd.equals(this.model.getStrings().getString("discard_cmd"))) {
      return new DiscardCommand(this.model);
    }
    // Command not valid
    this.printInvalidCommand();
    return null;
  }
  
  /**
   * Prints out "invalid command or arguments" message.
   */
  private void printInvalidCommand() {
    System.out.println(this.model.getStrings().getString("invalid_command"));
  }
  
  private void printOptions() {
    if (this.model.getState() == ClientModel.State.Main) {
      System.out.print(this.model.getClf().formatMainMenuPrompt());
    } else {  // state = "Drafting"
      System.out.print(this.model.getClf().formatDraftingMenuPrompt(this.model.getDraftTag(), this.model.getDraftLines()));
    }
  }
}

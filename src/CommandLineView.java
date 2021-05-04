
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
        System.err.println("User/host has not been set.");
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
      System.out.print(CLFormatter.formatSplash(this.model.getUser()));
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
        throw new IOException("Input stream closed while reading.");
      }
      // Trim leading/trailing white space, and split words according to spaces
      List<String> split = Arrays.stream(raw.trim().split("\\ "))
          .map(x -> x.trim()).collect(Collectors.toList());
      // First word is the command keyword
      String cmd = split.remove(0);
      // Remainder, if any, are arguments
      String[] rawArgs = split.toArray(new String[split.size()]);
      
      // Process user input
      if ("exit".equals(cmd)) {
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
        System.out.println("Could not parse command/args.");
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
    switch (cmd) {
      case "manage":
        // Switch to "Drafting" state and start a new "draft"
        return new ManageCommand(this.model, rawArgs);
      case "read":
        // Read tines on server
        return new ReadCommand(this.model, rawArgs);
      case "show":
        // Show tags and authers on server
        return new ShowCommand(this.model);
      default:
        // Command not valid
        this.printInvalidCommand();
        return null;
    }
  }
  
  private Command handleDraftingState(String cmd, String[] rawArgs) {
    switch (cmd) {
      case "line":
        // Add a tine message line
        return new LineCommand(this.model, rawArgs);
      case "push":
        // Send drafted tines to the server, and go back to "Main" state
        return new PushCommand(this.model);
      case "close":
        // Close ticket if user is auther
        return new CloseCommand(this.model);
      default:
        // Command not valid
        this.printInvalidCommand();
        return null;
    }
  }
  
  private void printInvalidCommand() {
    System.out.println("Could not parse command/args.");
  }
  
  private void printOptions() {
    if (this.model.getState() == ClientModel.State.Main) {
      System.out.print(CLFormatter.formatMainMenuPrompt());
    } else {  // state = "Drafting"
      System.out.print(CLFormatter.formatDraftingMenuPrompt(this.model.getDraftTag(), this.model.getDraftLines()));
    }
  }
}

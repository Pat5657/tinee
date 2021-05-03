
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import sep.tinee.net.channel.ClientChannel;
import sep.tinee.net.message.Bye;
import sep.tinee.net.message.Push;
import sep.tinee.net.message.ReadReply;
import sep.tinee.net.message.ReadRequest;

/**
 * This class is an initial work-in-progress prototype for a command line
 * Tinee client. It has been hastily hacked together, as often the case
 * in early exploratory coding, and is incomplete and buggy. However, it
 * does compile and run, and <i>some</i> basic functionality, such as pushing
 * and reading tines to and from an instance of
 * {@link sep.tinee.server.Server}, is working. Try it out!
 * <p>
 * The arguments required to run a client correspond to the
 * {@link #set(String, String, int)} method: a user name, and the host name
 * and port number of a Tinee server.
 * <p>
 * You can compile and run this client using <b>NetBeans</b>; e.g., right-click
 * this file in the NetBeans editor and select "Run File".  Note, to provide
 * the above arguments, you should set up a <b>run configuration</b> for this
 * class: {@literal ->} "Set Project Configuration" {@literal ->} "Customize..."
 * {@literal ->} "New...".
 * <p>
 * Assuming compilation using NetBeans (etc.), you can also run {@code Client}
 * from the command line; e.g., on Windows, run:
 * <ul>
 * <li style="list-style-type: none;">
 * {@code C:\...\tinee>  java -cp build\classes Client userid localhost 8888}
 * </ul>
 * <p>
 * You will be significantly reworking and extending this client: your aim is
 * to meet the specification, and you have mostly free rein to do so.
 * (That is as opposed to the base framework, which you are <b>not</b> permitted
 * to modify, i.e., the packages {@link sep.tinee.server},
 * {@link sep.tinee.server}, {@link sep.tinee.server} and]
 * {@link sep.tinee.server}.) The constraints on your client are:
 * <ul>
 * <li>
 * You must retain a class named {@code Client}) as the frontend class for
 * running your client, i.e., via its static {@linkplain #main(String[]) main}
 * method.
 * <li>
 * The {@linkplain Client#main(String[]) main} method must accept the <i>same
 * arguments</i> as currently, i.e., user name, host name and port number.
 * <li>
 * Your client must continue to accept user commands on the <i>standard input
 * stream</i> ({@code System.in}), and output on the <i>standard output
 * stream</i> ({@code System.out}).
 * <li>
 * Any other conditions specified by the assignment tasks.
 * </ul>
 * <p>
 * <i>Tip:</i> generate and read the <b>JavaDoc</b> API documentation for the
 * provided base framework classes (if you're not already reading this there!).
 * After importing the code project into NetBeans, right-click the project in
 * the "Projects" window and select "Generate Javadoc".
 * By default, the output is written to the {@code dist/javadoc} directory.
 * You can directly read the comments in the code for the same information, but
 * the generated JavaDoc is formatted more nicely as HTML with click-able links.  
 *
 * @see CLFormatter
 */
public class Client {

  private String user;
  private String host;
  private int port;
  private ClientChannel chan;  // Client-side channel for talking to a Tinee server
  private List<String> draftLines = new LinkedList<>();
  private State state;
  private String draftTag;
  boolean printSplash = true;

  enum State {
    Main,
    Drafting
  }
  
  public Client(String user, String host, int port) {
    this.user = user;
    this.host = host;
    this.port = port;
    this.chan = new ClientChannel(host, port);
  }

  public static void main(String[] args) throws IOException {
    String user = args[0];
    String host = args[1];
    int port = Integer.parseInt(args[2]);
    Client client = new Client(user, host, port);
    client.run();
  }
  
  public ClientChannel getChan() {
    return this.chan;
  }
  
  public void addDraftLine(String lines) {
    this.draftLines.add(lines);
  }
  
  public void setState(State s) {
    this.state = s;
  }
  
  public void setDraftTag(String tag) {
    this.draftTag = tag;
  }

  // Run the client
  @SuppressFBWarnings(
      value = "DM_DEFAULT_ENCODING",
      justification = "When reading console, ignore 'default encoding' warning")
  void run() throws IOException {
    
    BufferedReader reader = null;
    try {
      reader = new BufferedReader(new InputStreamReader(System.in));

      if (this.user.isEmpty() || this.host.isEmpty()) {
        System.err.println("User/host has not been set.");
        System.exit(1);
      }

      if (this.printSplash = true) {
        System.out.print(CLFormatter.formatSplash(this.user));
      }
      loop(reader);
    } catch (IOException | ClassNotFoundException ex) {
      throw new RuntimeException(ex);
    } finally {
      if (reader != null) {
        reader.close();
      }
      if (this.chan.isOpen()) {
        // If the channel is open, send Bye and close
        this.chan.send(new Bye());
        this.chan.close();
      }
    }
  }

// Main loop: print user options, read user input and process
  void loop(BufferedReader reader) throws IOException,
      ClassNotFoundException {

    // The app is in one of two states: "Main" or "Drafting"
    this.state = State.Main;

    // Holds the current draft data when in the "Drafting" state
    this.draftTag = null;
    
    // Define command
    Command command;
    
    // The loop
    for (boolean done = false; !done;) {
      // Clear command
      command = null;
      
      // Print user options
      if (this.state == State.Main) {
        System.out.print(CLFormatter.formatMainMenuPrompt());
      } else {  // state = "Drafting"
        System.out.print(CLFormatter.formatDraftingMenuPrompt(this.draftTag, this.draftLines));
      }

      // Read a line of user input
      String raw = reader.readLine();
      if (raw == null) {
        throw new IOException("Input stream closed while reading.");
      }
      // Trim leading/trailing white space, and split words according to spaces
      List<String> split = Arrays.stream(raw.trim().split("\\ "))
          .map(x -> x.trim()).collect(Collectors.toList());
      String cmd = split.remove(0);  // First word is the command keyword
      String[] rawArgs = split.toArray(new String[split.size()]);
      // Remainder, if any, are arguments

      // Process user input
      if ("exit".startsWith(cmd)) {
        // exit command applies in either state
        done = true;
      } // "Main" state commands
      else if (this.state == State.Main) {
        if ("manage".startsWith(cmd)) {
          // Switch to "Drafting" state and start a new "draft"
          command = new ManageCommand(this, rawArgs[0]);
        } else if ("read".startsWith(cmd)) {
          // Read tines on server
          command = new ReadCommand(this, rawArgs);
        } else {
          System.out.println("Could not parse command/args.");
        }
      } // "Drafting" state commands
      else if (this.state == State.Drafting) {
        if ("line".startsWith(cmd)) {
          // Add a tine message line
          command = new LineCommand(this, rawArgs);
        } else if ("push".startsWith(cmd)) {
          // Send drafted tines to the server, and go back to "Main" state
          command = new PushCommand(this, user, draftTag, this.draftLines);
        } else {
          System.out.println("Could not parse command/args.");
        }
      } else {
        System.out.println("Could not parse command/args.");
      }
      // Execute the command if it has been set.
      if (command != null) {
        command.execute();
      }
    }
  }
}

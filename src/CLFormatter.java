
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * A helper class for the current prototype {@link Client client}.  <i>E.g.</i>,
 * for formatting Command Line messages.
 */
public class CLFormatter {

  /* Following are the auxiliary methods for formatting the UI text */

  static String formatSplash(String user) {
    return "\nHello " + user + "!\n"
        + "Note:  Commands can be abbreviated to any prefix, "
        + "e.g., read [mytag] => re [mytag]\n";
  }

  static String formatMainMenuPrompt() {
    return "\n[Main] Enter command: "
        + "read [mytag], "
        + "manage [mytag], "
        + "show, "
        + "exit"
        + "\n> ";
  }

  static String formatDraftingMenuPrompt(String tag, List<String> lines) {
    return "\nDrafting: " + formatDrafting(tag, lines)
        + "\n[Drafting] Enter command: "
        + "line [mytext], "
        + "push, "
        + "close, "
        + "exit"
        + "\n> ";
  }

  static String formatDrafting(String tag, List<String> lines) {
    StringBuilder b = new StringBuilder("#");
    b.append(tag);
    int i = 1;
    for (String x : lines) {
      b.append("\n");
      b.append(String.format("%12d", i++));
      b.append("  ");
      b.append(x);
    };
    return b.toString();
  }

  static String formatRead(String tag, List<String> users,
      List<String> read) {
    StringBuilder b = new StringBuilder("Read: #");
    b.append(tag);
    Iterator<String> it = read.iterator();
    for (String user : users) {
      b.append("\n");
      b.append(String.format("%12s", user));
      b.append("  ");
      b.append(it.next());
    };
    b.append("\n");
    return b.toString();
  }
  
  static String formatShow(Map<String, String> show) {
    StringBuilder b = new StringBuilder("Show:");
    for (String tag : show.keySet()) {
      b.append("\n");
      b.append(String.format("%12s", tag));
      b.append("  ");
      b.append(show.get(tag));
    };
    b.append("\n");
    return b.toString();
  }
}

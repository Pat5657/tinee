
import java.text.MessageFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * A helper class for the current prototype {@link Client client}.  <i>E.g.</i>,
 * for formatting Command Line messages.
 */
public class CLFormatter {
  
  private ResourceBundle strings;
  
  public CLFormatter(ResourceBundle strings) {
    this.strings = strings;
  }

  /* Following are the auxiliary methods for formatting the UI text */

  public String formatSplash(String user) {
    return MessageFormat.format(this.strings.getString("splash"), user);
  }

  public String formatMainMenuPrompt() {
    return this.strings.getString("main_menu_options");
  }

  public String formatDraftingMenuPrompt(String tag, List<String> lines) {
    return MessageFormat.format(this.strings.getString("drafting_menu_options"), formatDrafting(tag, lines));
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

  public String formatRead(String tag, List<String> users,
      List<String> read) {
    StringBuilder b = new StringBuilder(this.strings.getString("read") + ": #");
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
  
  public String formatShow(Map<String, String> show) {
    StringBuilder b = new StringBuilder(this.strings.getString("show") + ":");
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

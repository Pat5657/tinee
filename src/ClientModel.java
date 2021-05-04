
import java.util.LinkedList;
import java.util.List;
import sep.mvc.AbstractModel;
import sep.tinee.net.channel.ClientChannel;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Patry
 */
public class ClientModel extends AbstractModel {
  private String user;
  private String host;
  private int port;
  private List<String> draftLines = new LinkedList<>();
  private State state = State.Main;
  private String draftTag = null;
  private boolean printSplash = true;
  private ClientChannel chan;  // Client-side channel for talking to a Tinee server

  
  public enum State {
    Main,
    Drafting
  }
    
  public ClientModel(String user, String host, int port) {
    this.user = user;
    this.host = host;
    this.port = port;
    this.chan = new ClientChannel(host, port);
  }
  
  public String getUser() {
    return this.user;
  }
  
  public String getHost() {
    return this.host;
  }
  
  public int getPort() {
    return this.port;
  }
  
  public boolean getPrintSplash() {
    return this.printSplash;
  }
  
  public State getState() {
    return this.state;
  }
  
  public String getDraftTag() {
    return this.draftTag;
  }
  
  public List<String> getDraftLines() {
    return this.draftLines;
  }
  
  public void setState(State s) {
    this.state = s;
  }
  
  public void setDraftTag(String tag) {
    this.draftTag = tag;
  }
  
  public ClientChannel getChan() {
    return this.chan;
  }
  
  public void addDraftLine(String line) {
    this.draftLines.add(line);
  }
  
  public void clearDraftLines() {
    this.draftLines.clear();
  }
  }

package com.danielirvine.http;

import java.io.*;
import java.util.*;
import java.security.*;

public class Authorizor {

  private final List<AuthorizationTableEntry> authorizationTable = new ArrayList<AuthorizationTableEntry>();

  public Authorizor(InputStream in) {
    try(BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
      String currentLine = null;
      while((currentLine = reader.readLine()) != null) {
        parseAuthorizationTableEntry(currentLine);
      }
    }catch(IOException ex) {
      ex.printStackTrace();
    }
  }

  private void parseAuthorizationTableEntry(String line) {
    String[] parts = line.split(":");

    authorizationTable.add(new AuthorizationTableEntry(parts[0], parts[1], parts[2]));
  }

  public boolean requiresAuthorization(String path) {
    return authorizationTable.stream().anyMatch(a->a.forPath(path));
  }

  public boolean isAuthorized(String path, String username, String password) {
    return authorizationTable.stream().anyMatch(a->a.isAuthorized(path, username, password));
  }

  private class AuthorizationTableEntry {
    private final String path;
    private final String user;
    private final String password;

    public AuthorizationTableEntry(String path, String user, String password) {
      this.path = path;
      this.user = user;
      this.password = password;
    }

    public boolean forPath(String other) {
      return path.equals(other);
    }

    public boolean isAuthorized(String otherPath, String otherUser, String otherPassword) {
      if(!path.equals(otherPath)) {
        return false;
      }
      if(!user.equals(otherUser)) {
        return false;
      }
      try {
        MessageDigest sha = MessageDigest.getInstance("SHA-256");
        byte[] otherHash = sha.digest(otherPassword.getBytes());
        return hexEncode(otherHash).equals(password);
      } catch(Exception ex) {
        return false;
      }
    }

    private String hexEncode(final byte[] hash) {
      Formatter formatter = new Formatter();
      for (byte b : hash) {
        formatter.format("%02x", b);
      }
      return formatter.toString();
    }
  }
}

package com.ullink.slack.simpleslackapi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TestUtils {

  public static String getFile(String filename) throws IOException {
    InputStream stream = TestUtils.class.getResourceAsStream(filename);
    InputStreamReader isReader = new InputStreamReader(stream);
    BufferedReader reader = new BufferedReader(isReader);
    StringBuilder strBuilder = new StringBuilder();
    String line;
    while ((line = reader.readLine()) != null) {
      strBuilder.append(line);
    }
    return strBuilder.toString();
  }


}

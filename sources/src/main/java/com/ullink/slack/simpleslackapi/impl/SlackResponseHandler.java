package com.ullink.slack.simpleslackapi.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ullink.slack.simpleslackapi.SlackPresence;
import com.ullink.slack.simpleslackapi.SlackPresenceSerDes;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

public class SlackResponseHandler<T> implements ResponseHandler<T> {

  private final Gson gson;
  private Class<T> reference;

  public SlackResponseHandler(Class<T> reference) {
    this.reference = reference;
    GsonBuilder builder = new GsonBuilder();
    builder.registerTypeAdapter(SlackPresence.class, new SlackPresenceSerDes());
    gson = builder.create();

  }

  @Override
  public T handleResponse(HttpResponse response) throws IOException {
    String json = consumeToString(response.getEntity().getContent());
    return gson.fromJson(json, reference);
  }

  private String consumeToString(InputStream content) throws IOException {
    Reader reader = new InputStreamReader(content, StandardCharsets.UTF_8);
    StringBuilder buf = new StringBuilder();
    char data[] = new char[16384];
    int numread;
    while (0 <= (numread = reader.read(data)))
      buf.append(data, 0, numread);
    return buf.toString();
  }


}

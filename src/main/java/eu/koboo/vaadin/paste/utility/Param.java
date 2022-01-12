package eu.koboo.vaadin.paste.utility;

import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeLeaveEvent;
import com.vaadin.flow.router.Location;
import com.vaadin.flow.router.QueryParameters;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Param {

  public static Param create() {
    return new Param();
  }

  public static Param with(String key, String value) {
    return new Param().and(key, value);
  }

  public static String getParam(AfterNavigationEvent event, String key) {
    return getParam(event.getLocation(), key);
  }

  public static String getParam(BeforeEnterEvent event, String key) {
    return getParam(event.getLocation(), key);
  }

  public static String getParam(BeforeLeaveEvent event, String key) {
    return getParam(event.getLocation(), key);
  }

  public static String getParam(Location location, String key) {
    List<String> queryParams = getParams(location, key);
    if(queryParams != null) {
      return queryParams.get(0);
    }
    return null;
  }

  public static List<String> getParams(Location location, String key) {
    if(!location.getQueryParameters().getParameters().containsKey(key)) {
      return null;
    }
    List<String> queryList = location.getQueryParameters().getParameters().get(key);
    if(queryList.isEmpty()) {
      return null;
    }
    return queryList;
  }

  private final Map<String, List<String>> parameters;

  private Param() {
    this.parameters = new HashMap<>();
  }

  public Param and(String key, String value) {
    List<String> paramList = parameters.computeIfAbsent(key, k -> new ArrayList<>());
    paramList.add(value);
    return this;
  }

  public Param remove(String key, String value) {
    if(parameters.containsKey(key)) {
      List<String> paramList = parameters.get(key);
      paramList.remove(value);
    }
    return this;
  }

  public Param clear(String key) {
    parameters.remove(key);
    return this;
  }

  public QueryParameters build() {
    Map<String, String[]> parameterMap = new HashMap<>();
    for (Entry<String, List<String>> entry : parameters.entrySet()) {
      List<String> valueList = entry.getValue();
      parameterMap.put(entry.getKey(), Arrays.copyOf(valueList.toArray(), valueList.size(), String[].class));
    }
    return QueryParameters.full(parameterMap);
  }

}
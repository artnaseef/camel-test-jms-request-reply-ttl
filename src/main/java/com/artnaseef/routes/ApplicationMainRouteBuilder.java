package com.artnaseef.routes;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.processor.aggregate.AggregationStrategy;

import java.util.Objects;

/**
 *
 */
public class ApplicationMainRouteBuilder extends RouteBuilder {

  private boolean serviceEnabled = true;
  private String requestJmsDestination;
  private String requestCompletedEventJmsDestination;
  private String errorNotificationJmsDestination;

//========================================
// Getters and Setters
//----------------------------------------

  public boolean isServiceEnabled() {
    return serviceEnabled;
  }

  public void setServiceEnabled(boolean serviceEnabled) {
    this.serviceEnabled = serviceEnabled;
  }

  public String getRequestJmsDestination() {
    return requestJmsDestination;
  }

  public void setRequestJmsDestination(String requestJmsDestination) {
    this.requestJmsDestination = requestJmsDestination;
  }

  public String getRequestCompletedEventJmsDestination() {
    return requestCompletedEventJmsDestination;
  }

  public void setRequestCompletedEventJmsDestination(
      String requestCompletedEventJmsDestination) {
    this.requestCompletedEventJmsDestination = requestCompletedEventJmsDestination;
  }

  public String getErrorNotificationJmsDestination() {
    return errorNotificationJmsDestination;
  }

  public void setErrorNotificationJmsDestination(String errorNotificationJmsDestination) {
    this.errorNotificationJmsDestination = errorNotificationJmsDestination;
  }

//========================================
// ROUTE CONFIGURATION
//----------------------------------------

  public void configure() {
    if (serviceEnabled) {
      // // Configure exception handling
      // onException(Exception.class)
      //     .handled(true)
      //     .log("EXCEPTION HERE")
      // ;

      this.configureExceptionSubroute();
      this.configureMainRoute();
    } else {
      this.log.info("Main Service Route is disabled");
    }
  }

//========================================
// Internal Methods
//----------------------------------------

  private void configureMainRoute() {
    // Main Orchestration Route
    from("jms-in:" + this.requestJmsDestination).routeId("service.requests")
        .to("log:" + ApplicationMainRouteBuilder.class.getName() + "?level=INFO")
        .setBody(simple("RESPONSE TO ${in.body}"))
    ;
  }

  private void configureExceptionSubroute() {
    from("direct:exception-handling-subroute").routeId("service.onException")
        .to("log:" + ApplicationMainRouteBuilder.class.getName()
            + ".xyz?level=ERROR&showCaughtException=true&showStackTrace=true")

        // Log all replies
        .convertBodyTo(String.class)
        .to("log:" + ApplicationMainRouteBuilder.class.getName() + ".xyz?level=ERROR")
        .to("jms-out:" + errorNotificationJmsDestination)
    ;
  }
}

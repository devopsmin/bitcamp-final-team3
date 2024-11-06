package project.tripMaker.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

@Component
public class CustomSessionListener implements HttpSessionListener {
  private static final Logger logger = LoggerFactory.getLogger(CustomSessionListener.class);

  @Override
  public void sessionCreated(HttpSessionEvent se) {
    logger.info("======================Session Created: " + se.getSession().getId());
  }

  @Override
  public void sessionDestroyed(HttpSessionEvent se) {
    logger.info("======================Session Destroyed: " + se.getSession().getId());
  }
}

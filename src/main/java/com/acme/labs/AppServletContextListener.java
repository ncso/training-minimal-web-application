
package com.acme.labs;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;

@WebListener
public class AppServletContextListener implements ServletContextListener {
    private static final Logger LOG = Log.getLogger(AppServletContextListener.class);
    private static final String THISKEY = "h77krfld2jo7001scr"; /* com-acme-labs-AppServletContextListener */
    public static void someUtilityMethod(ServletContext servletContext) throws Exception {
        AppServletContextListener _this = (AppServletContextListener)servletContext.getAttribute(THISKEY);
        if (LOG.isDebugEnabled()) {
        	LOG.debug("hi from " + _this.toString() + "!");
        }
    }
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        ServletContext servletContext = servletContextEvent.getServletContext();
        LOG.info("context initializing");
        // try {
        //     //
        // } catch (Exception e) {
        //     LOG.warn(e);
        //     throw new RuntimeException(e);
        // }
        LOG.info("context initialized");
        servletContext.setAttribute(THISKEY, this);
    }
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        ServletContext servletContext = servletContextEvent.getServletContext();
        servletContext.removeAttribute(THISKEY);
        LOG.info("context destroyed");
    }
}

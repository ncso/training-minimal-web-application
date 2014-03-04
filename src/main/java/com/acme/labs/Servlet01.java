
package com.acme.labs;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Map;
import java.util.LinkedHashMap;
import java.util.List;

import org.javatuples.Triplet;

import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;

@WebServlet(value="/s01/*", loadOnStartup=1)
public class Servlet01 extends HttpServlet {
    private static final Logger LOG = Log.getLogger(Servlet01.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletContext context = request.getServletContext();
        PrintWriter out = response.getWriter();

        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/plain");

        out.write("server info: " + context.getServerInfo() + "\n");
        out.write("servlet version: " + context.getMajorVersion() + "." + context.getMinorVersion() + "\n\n");

        String contextPath = request.getContextPath();
        String servletPath = request.getServletPath();
        String pathInfo = request.getPathInfo();
        String queryString = request.getQueryString();

        Map<String,String> map = new LinkedHashMap<String,String>();
        map.put("contextPath", contextPath);
        map.put("servletPath", servletPath);
        map.put("pathInfo", pathInfo);
        map.put("queryString", queryString);

        if (pathInfo == null) {
            LOG.debug("path info is null");
            out.write("path info is null\n\n");
        } else {
            Triplet<List<String>,String,String> tuple = PathInfo.getPathInfoComponents(pathInfo);
            if (tuple.getValue0().size() == 0) {
                LOG.debug("path is empty");
                response.getWriter().write("path is empty\n\n");
            } else {
                String path = tuple.getValue1();
                String pathId = new StringID(path).toString();
                LOG.debug("path is \"" + path + "\"");
                out.write("path is \"" + path + "\"\n\n");
                map.put("pathId", pathId);
                map.put("remaining", tuple.getValue2());
            }
        }

        out.write(StringUtils.join(map,"\n", ": [", "]"));
        out.write("\n");
    }
}

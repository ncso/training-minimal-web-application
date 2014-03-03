
package com.acme.labs;

import java.util.List;
import java.util.ArrayList;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

import org.javatuples.Triplet;

public class PathInfo {
    /*
     * valid path info
     *
     * rule 1: must start with '/' and end with a path component
     *
     * rule 2: path components are separated by '/'
     *
     * rule 3: path components are made of only ASCII letters, digits, and
     *     the `-' character
     *
     * rule 4: path components must begin and end only with a letter or
     *     digit
     *
     * reference: http://www.ietf.org/rfc/rfc1912.txt
     *
     */

    static private Pattern _regex1 = Pattern.compile("^/([a-zA-Z0-9\\-]+)");

    /* <components,remaining> */
    static Triplet<List<String>,String,String> getPathInfoComponents(String pathInfo) {
        Matcher m = _regex1.matcher(pathInfo);
        int pathLength = pathInfo.length();
        int lastEnd = 0;
        List<String> components = new ArrayList<String>();
        while (m.find()) {
            String component = m.group(1);
            if (component.startsWith("-") || component.endsWith("-")) {
                lastEnd = m.start();
                break;
            }
            lastEnd = m.end();
            components.add(component);
            m.region(lastEnd, pathLength);
        }
        return Triplet.with(components, pathInfo.substring(0,lastEnd), pathInfo.substring(lastEnd));
    }
}

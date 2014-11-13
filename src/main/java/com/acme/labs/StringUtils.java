
package com.acme.labs;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

public class StringUtils {
    public static String join(Collection<?> s, String delimiter) {
	if (s.isEmpty()) return "";
	Iterator<?> iter = s.iterator();
	StringBuilder buffer = new StringBuilder(String.valueOf(iter.next()));
	while (iter.hasNext()) buffer.append(delimiter).append(iter.next());
	return buffer.toString();
    }
    public static String join(Map<?,?> m, String delimiter, String afterKey, String afterValue) {
	if (m.isEmpty()) return "";
	Iterator<? extends Map.Entry<?,?>> iter = m.entrySet().iterator();
	Map.Entry<?,?> i = iter.next();
	StringBuilder buffer = new StringBuilder();
        buffer.append(i.getKey()).append(afterKey).append(i.getValue()).append(afterValue);
	while (iter.hasNext()) {
	    i = iter.next();
	    buffer.append(delimiter)
                .append(i.getKey()).append(afterKey).append(i.getValue()).append(afterValue);
	}
        return buffer.toString();
    }
}

package org.dangcat.web.filter;

import org.dangcat.commons.utils.ValueUtils;

import javax.servlet.http.HttpServletRequest;

public class RequestUtils {
    public static String getServletPath(HttpServletRequest request) {
        String servletPath = request.getServletPath();
        String requestUri = request.getRequestURI();
        // Detecting other characters that the servlet container cut off (like
        // anything after ';')
        if (requestUri != null && servletPath != null && !requestUri.endsWith(servletPath)) {
            int pos = requestUri.indexOf(servletPath);
            if (pos > -1)
                servletPath = requestUri.substring(requestUri.indexOf(servletPath));
        }

        if (null != servletPath && !"".equals(servletPath))
            return servletPath;

        int startIndex = request.getContextPath().equals("") ? 0 : request.getContextPath().length();
        int endIndex = request.getPathInfo() == null ? requestUri.length() : requestUri.lastIndexOf(request.getPathInfo());

        // this should not happen
        if (startIndex > endIndex)
            endIndex = startIndex;
        return requestUri.substring(startIndex, endIndex);
    }

    public static String getUri(HttpServletRequest request) {
        String uri = getServletPath(request);
        if (!ValueUtils.isEmpty(uri))
            return uri;

        uri = request.getRequestURI();
        return uri.substring(request.getContextPath().length());
    }
}
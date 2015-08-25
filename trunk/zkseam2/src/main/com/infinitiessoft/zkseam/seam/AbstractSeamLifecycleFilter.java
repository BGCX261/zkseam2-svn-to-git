/*
 * Copyright (C) 2010 InfinitiesSoft Corporation. 
 * http://www.infinitiessoft.com
 * 
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
*/
package com.infinitiessoft.zkseam.seam;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.jboss.seam.contexts.ServletLifecycleEx;
import org.jboss.seam.log.Log;
import org.jboss.seam.log.Logging;
import org.jboss.seam.web.AbstractFilter;

/**
 * 
 * @author Dennis Chen , cola.orange@gmail.com 
 */
public abstract class AbstractSeamLifecycleFilter extends AbstractFilter {

    private static Log log = Logging.getLog(AbstractSeamLifecycleFilter.class);

    String name;

    public void init(FilterConfig filterConfig) throws ServletException {
        super.init(filterConfig);
        name = filterConfig.getServletContext().getContextPath();
        if (name != null && name.startsWith("/")) {
            name = name.substring(1);
        }
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
            ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        if (ignoreSeamLifecycle(req)) {
            chain.doFilter(request, response);
        } else {
            beforeRequest(req);
            boolean error = false;
            try {
                chain.doFilter(request, response);
            } catch (RuntimeException x) {
                log.error(x.getMessage(), x);
                error = true;
                throw x;
            } catch (IOException x) {
                log.error(x.getMessage(), x);
                error = true;
                throw x;
            } catch (ServletException x) {
                log.error(x.getMessage(), x);
                error = true;
                throw x;
            } finally {
                afterRequest(req,error);
            }
        }
    }

    private void afterRequest(HttpServletRequest req,boolean error) {
        try{
          //zk will not throw out exception in auEngine, so we check it by execution exception
            ExecutionExceptions ee = ExecutionExceptions.instance();
            if(ee.hasException()){
                log.error(ee.getThrowable().getMessage(),ee.getThrowable());
            }
            ServletLifecycleEx.handleTransactionsAfterRequest(ee.isRollback() || error);
            if (!error) {
                // don't end when exception, ExceptionFilter will end it.
                ServletLifecycleEx.handleConversationAfterRequest(req);
                ServletLifecycleEx.endRequest(req);
            }
            log.debug("end loader #0", req.getRequestURL());
        }catch(RuntimeException x){
            log.error(x.getMessage(),x);
            throw x;
        }
    }

    private void beforeRequest(HttpServletRequest request) {
        try{
            log.debug("before filter request #0", request.getRequestURL());
            String dtid = request.getParameter("dtid");
            log.debug("desktop id #0", dtid);
            
            ServletLifecycleEx.beginRequest(name, request);
            ServletLifecycleEx.restoreConversation(dtid,request);
            ServletLifecycleEx.handleTransactionsBeforeRequest();
        }catch(RuntimeException x){
            log.error(x.getMessage(),x);
            throw x;
        }
    }

    /**
     * @param error
     */


    protected boolean ignoreSeamLifecycle(HttpServletRequest request) {
        return false;
    }

    // private Object outContext(Context context) {
    // String[] names = context.getNames();
    //        
    // StringBuilder sb = new StringBuilder();
    // sb.append("\n\tContext Type : "+context.getType()).append("\n");
    // for(String n:names){
    // sb.append("\t").append(n).append(" : ").append(context.get(n)).append("\n");
    // }
    // return sb.toString();
    // }
}

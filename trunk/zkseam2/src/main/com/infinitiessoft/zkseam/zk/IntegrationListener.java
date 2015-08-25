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
package com.infinitiessoft.zkseam.zk;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.jboss.seam.contexts.Contexts;
import org.jboss.seam.contexts.LifecycleEx;
import org.jboss.seam.core.Conversation;
import org.jboss.seam.log.Log;
import org.jboss.seam.log.Logging;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.sys.ExecutionCtrl;
import org.zkoss.zk.ui.util.DesktopCleanup;
import org.zkoss.zk.ui.util.DesktopInit;
import org.zkoss.zk.ui.util.ExecutionCleanup;
import org.zkoss.zk.ui.util.ExecutionInit;

import com.infinitiessoft.zkseam.Configuration;
import com.infinitiessoft.zkseam.DesktopConversation;
import com.infinitiessoft.zkseam.seam.ExecutionExceptions;
/**
 * 
 * @author Dennis Chen , cola.orange@gmail.com 
 */
public class IntegrationListener implements ExecutionInit, ExecutionCleanup,DesktopInit,DesktopCleanup,Serializable{

    private static final long serialVersionUID = 1L;

    private static final String VARIABLE_MARK = "SeamVariableResolver.mark";
    
    private static Log log = Logging.getLog(IntegrationListener.class);
    
    public void init(Execution exec, Execution parent) throws Exception {
        Conversation conv = Conversation.instance();
        Configuration config = Configuration.instance();
        if(config.isAutoLinkDesktopConversation() && !conv.isLongRunning()){
            log.warn("Running execution not in long running desktop #0, conversation #1, did you set the desktop timeout large then conversation timeout",exec.getDesktop().getId(),conv.getId());
        }
        if(exec instanceof ExecutionCtrl){
            Page page = ((ExecutionCtrl)exec).getCurrentPage();
            if(page!=null){
                //is there a resolver already..
                if(page.getAttribute(VARIABLE_MARK)==null){
                    SeamVariableResolver resolver = new SeamVariableResolver();
                    log.debug("add variable resolver #0 to page",resolver);
                    page.addVariableResolver(resolver);
                    page.setAttribute(VARIABLE_MARK,VARIABLE_MARK);
                }
            }
        }
        //wrapping desktop to event context
        Map<String,Object> dtwrap = new ExecutionMapWrapper(exec);
        LifecycleEx.wrapEventContext(dtwrap);
    }

    public void cleanup(Execution exec, Execution parent, List errs) throws Exception {
        LifecycleEx.unwrapEventContext();
        SeamVariableResolver.lastNullEval.set(null);//clear it for each execution;
        if(errs!=null){
            ExecutionExceptions ee = ExecutionExceptions.instance();
            for(Object o:errs){
                if(o instanceof Throwable){
                    ee.setThrowable((Throwable)o);
                }
            }
        }
    }

    
    public void init(Desktop desktop, Object request) throws Exception {
        log.debug("desktop init #0",desktop.getId());
        
        Configuration config = Configuration.instance();
        if(config.isAutoLinkDesktopConversation()){
            DesktopConversation.begin(desktop);
        }
    }


    public void cleanup(Desktop desktop) throws Exception {
        log.debug("desktop cleanup #0",desktop.getId());
        //if this is not a requested clean, then there is no application context.
        if(Contexts.isApplicationContextActive() && Contexts.isConversationContextActive()){
            Configuration config = Configuration.instance();
            if(config.isAutoLinkDesktopConversation()){
                DesktopConversation.end();
            }
        }
    }
}

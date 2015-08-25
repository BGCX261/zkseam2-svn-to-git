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
package com.infinitiessoft.zkseam;

import org.jboss.seam.core.Conversation;
import org.jboss.seam.core.Manager;
import org.jboss.seam.log.Log;
import org.jboss.seam.log.Logging;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;

/**
 * 
 * @author Dennis Chen , cola.orange@gmail.com 
 */
public class DesktopConversation {
    private static Log log = Logging.getLog(DesktopConversation.class);
    
    /**
     * begin conversation with current desktop
     */
    public static void begin(){
        Execution exec = Executions.getCurrent();
        if(exec==null){
            throw new IllegalStateException("current execution not found");
        }
        Desktop desktop = exec.getDesktop();
        begin(desktop,true);
    }
    
    /**
     * begin conversation with desktop
     * @param desktop
     */
    public static void begin(Desktop desktop){
        begin(desktop,true);
    }
    
    public static void begin(Desktop desktop,boolean join){
        Conversation conv = Conversation.instance();
        if(conv.isLongRunning()){
            if(!join){
                throw new IllegalStateException("Converstation already began "+conv.getId());
            }else if(!conv.getId().equals(desktop.getId())){
                log.debug("update conversation id from #0 to desktop id #1",conv.getId(),desktop.getId());
                Manager.instance().updateCurrentConversationId(desktop.getId());
            }else{
                return ;
            }
        }
        log.debug("begin conversation #0, desktop #1", conv.getId(),desktop.getId());
        if(!conv.getId().equals(desktop.getId())){
            Manager mgr = Manager.instance();
            log.debug("set conversation id from #0 to desktop id #1",conv.getId(),desktop.getId());
            mgr.updateCurrentConversationId(desktop.getId());
        }
        conv.begin();
    }
    
    public static void end(){
        Conversation conv = Conversation.instance();
        if(!conv.isLongRunning()){
            return;
        }
        log.debug("end conversation #0", conv.getId());
        conv.end();
    }
    
}

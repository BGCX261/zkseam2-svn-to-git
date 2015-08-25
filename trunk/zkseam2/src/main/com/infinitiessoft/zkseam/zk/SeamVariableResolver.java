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

import org.jboss.seam.Component;
import org.jboss.seam.Namespace;
import org.jboss.seam.core.Init;
import org.jboss.seam.log.Log;
import org.jboss.seam.log.Logging;
import org.zkoss.xel.VariableResolver;
import org.zkoss.xel.XelException;
/**
 * 
 * @author Dennis Chen , cola.orange@gmail.com 
 */
public class SeamVariableResolver implements VariableResolver,Serializable{

    private static final long serialVersionUID = 1L;

    private static Log log = Logging.getLog(SeamVariableResolver.class);
    
    static ThreadLocal<String> lastNullEval = new ThreadLocal<String>();//to prevent eval a non seam variable cost in zk
    
    public Object resolveVariable(String name) throws XelException {
        if(name.equals(lastNullEval.get())){
            //skip
            return null;
        }
        
        Object obj = getImplicit(name); 
        if(obj==null){
            obj = Component.getInstance(name);
        }
        if(obj==null){
            lastNullEval.set(name);
        }else{
            lastNullEval.set(null);//reset it
        }
        log.debug("get seam variable #0 = #1",name,obj==null?null:obj.getClass());
        return obj;
    }

    /**
     * @param name
     * @return
     */
    private Object getImplicit(String name) {
        String qname = null;
        //look up 
        //use seam namespaces directly
        Object obj;
        Init init = Init.instance();
        for(Namespace ns : init.getGlobalImports()){
           obj = ns.getComponentInstance(name);
           if(obj!=null){
               return obj;
           }
        }
        //or just lookup , (better performance?)
//        String[] lookup = new String[]{"org.jboss.seam.core.","org.jboss.seam.international."};
//        for(String l:lookup){
//            qname = l+name;;
//            obj = Component.getInstance(qname);
//            if(obj!=null){
//                return obj;
//            }
//        }
        return null;
    }
}

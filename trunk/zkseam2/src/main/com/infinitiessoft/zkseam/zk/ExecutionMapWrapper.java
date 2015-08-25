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

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.jboss.seam.log.Log;
import org.jboss.seam.log.Logging;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.IdSpace;
import org.zkoss.zk.ui.Page;

/**
 * @author Dennis Chen, cola.orange@gmail.com
 *
 */
public class ExecutionMapWrapper implements Map<String, Object> {

    private static Log log = Logging.getLog(ExecutionMapWrapper.class);
    
    Desktop desktop;
    Execution exec;
    Page page;
    
    
    Map<String,Object> cache = new HashMap<String,Object>();
    
    public ExecutionMapWrapper(Execution exec) {
        this.exec = exec;
        this.desktop = exec.getDesktop();
        log.debug("exec #0, desktop #1",exec,desktop);
    }

    public void clear() {
    }
    
    
    public boolean containsKey(Object key) {
        return false;
    }

    public boolean containsValue(Object value) {
        return false;
    }

    public Set<java.util.Map.Entry<String, Object>> entrySet() {
        return Collections.EMPTY_SET;
    }
    
    
    public Object get(Object key) {
//        log.debug("key #0",key);
        String path[] = key.toString().split("\\.");
        
        //what is instance is changed in an execution??
        Object obj = cache.get(key.toString());
        if(obj!=null){
            return obj;
        }
        int size = path.length;
        for(int i=0;i<size;i++){
            String p = path[i];
//            log.debug("current obj is #0, next path is '#1'",obj,p);
            if(i==0){
                obj = findImplicit(p);
                if(obj!=null) {
//                    log.debug("find implict #0 path '#1'",obj,p);
                    continue;
                }
                Page page = desktop.getPageIfAny(p);
                if(page!=null){
                    obj = page;
//                    log.debug("find page #0 path '#1'",obj,p);
                    continue;
                }
                //get from first page
                Collection col = desktop.getPages();
                if(!col.isEmpty()){
                    page = (Page)col.iterator().next();
                    obj = page.getFellowIfAny(p);
                    if(obj!=null){
                        continue;
                    }
                }
                
//                log.debug("no object found with path '#0'",p);
                break;
            }else{
                if(obj instanceof IdSpace){
                    obj = ((IdSpace)obj).getFellowIfAny(p);
                    if(obj!=null){
                        continue;
                    }
//                    log.debug("no object found in IdSpace #0 , name '#1'",obj,p);
                }else{
//                    log.debug("#0 is not a IdSapce, can not get next component name '#1'",obj,p);
                }
            }
        }
//        log.debug("result is #0",obj);
        if(obj!=null){
            cache.put(key.toString(),obj);
            log.debug("find object #0 with path '#1'",obj,key);
        }
        
        return obj;
    }

    /**
     * @param p
     * @return
     */
    private Object findImplicit(String p) {
        if("desktop".equals(p)){
            return desktop;
        }else if("page".equals(p)){
            Collection col = desktop.getPages();
            if(!col.isEmpty()){
                return col.iterator().next();
            }
        }else if("execution".equals(p)){
            return exec;
        }
        return null;
    }

    public boolean isEmpty() {
        return true;
    }

    public Set<String> keySet() {
        return Collections.EMPTY_SET;
    }

    public Object put(String key, Object value) {
        return null;
    }

    public void putAll(Map<? extends String, ? extends Object> t) {
    }

    public Object remove(Object key) {
        return null;
    }

    public int size() {
        return 0;
    }
    public Collection<Object> values() {
        return Collections.EMPTY_LIST;
    }

}

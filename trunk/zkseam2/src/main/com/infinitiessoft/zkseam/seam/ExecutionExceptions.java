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

import java.util.ArrayList;
import java.util.List;

import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.ApplicationException;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;
import org.jboss.seam.log.Logging;

/**
 * 
 * @author Dennis Chen , cola.orange@gmail.com 
 */
@Name("iss.zkseam.execExceptions")
@AutoCreate
@Scope(ScopeType.EVENT)
public class ExecutionExceptions {

    private static Log log = Logging.getLog(ExecutionExceptions.class);
    
    List<Throwable> throwables = new ArrayList<Throwable>(3);

    public Throwable getThrowable() {
        return throwables.size()==0?null:throwables.get(throwables.size()-1);
    }

    public void setThrowable(Throwable throwable) {
        boolean r1 = isRollback();
        Throwable t1 = getThrowable();
        throwables.add(throwable);
        boolean r2 = isRollback();
        
        if(t1!=null){
            log.debug("Try to push another exception, last #0, new #1",t1,throwable);
        }
        
        if(r1 && !r2){
            log.warn("Try to push another exception and it changes rollback from true to false, last #0, new #1",t1,throwable);
        }
        
    }
    
    public void clear(){
        throwables.clear();
    }

    public boolean hasException() {
        return throwables.size()>0;
    }

    public boolean isRollback() {
        if (hasException()) {
            javax.ejb.ApplicationException ae = getApplicationException(getThrowable());
            if(ae!=null && !ae.rollback()){
                return false;
            }
            ApplicationException sae = getSeamApplicationException(getThrowable());
            if(sae!=null && !sae.rollback()){
                return false;
            }
            return true;
        }
        
        return false;
    }

//    public boolean isApplicationException() {
//        if (hasException()) {
//            if(getApplicationException()!=null || getSeamApplicationException()!=null){
//                return true;
//            }
//        }
//        return false;
//    }
    
    private static ApplicationException getSeamApplicationException(Throwable throwable){
        return throwable==null?null:throwable.getClass().getAnnotation(ApplicationException.class);
    }
    
    private static javax.ejb.ApplicationException getApplicationException(Throwable throwable){
        return throwable==null?null:throwable.getClass().getAnnotation(javax.ejb.ApplicationException.class);
    }

    public static ExecutionExceptions instance() {
        return (ExecutionExceptions) Component.getInstance(ExecutionExceptions.class, ScopeType.EVENT);
    }

}

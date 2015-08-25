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

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Startup;
import org.jboss.seam.annotations.intercept.BypassInterceptors;
import org.jboss.seam.annotations.web.Filter;
import org.jboss.seam.log.Log;
import org.jboss.seam.log.Logging;

/**
 * 
 * @author Dennis Chen , cola.orange@gmail.com 
 */
@Name("iss.zkseam.loaderFilter")
@Scope(ScopeType.APPLICATION)
@AutoCreate 
@Startup
@BypassInterceptors
@Filter(within="org.jboss.seam.web.exceptionFilter")//MUST within exceptionFilter
public class LoaderFilter extends AbstractSeamLifecycleFilter{

    
    private static Log log = Logging.getLog(LoaderFilter.class);
    
    @Create
    public void create(){
        log.info("initial #0",getClass());
        if(getUrlPattern()==null && getRegexUrlPattern()==null){
            setUrlPattern("*.zul");
        }
        
    }
}

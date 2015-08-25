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

import java.util.Map;

import javax.servlet.ServletContext;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.Install;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Startup;
import org.jboss.seam.contexts.Lifecycle;
import org.jboss.seam.contexts.LifecycleEx;
import org.jboss.seam.contexts.ServletLifecycle;
import org.jboss.seam.contexts.ServletLifecycleEx;
import org.jboss.seam.log.Log;
import org.jboss.seam.log.Logging;
/**
 * 
 * @author Dennis Chen , cola.orange@gmail.com 
 */
@Name("iss.zkseam.seamLifecycleEx")
@Install(true)
@Scope(ScopeType.APPLICATION)
@Startup
@AutoCreate
public class SeamLifecycleEx {

    private static final Log log = Logging.getLog(SeamLifecycleEx.class);

    private String _name;

    @Create
    public void create() {
        // this will get the freshly servlet context since web initial is
        // sequential
        ServletContext context = ServletLifecycle.getServletContext();
        if (context == null) {
            log.warn("servlet context not found, did you initial seam out of WAR moudle?");
            Map<String, Object> currentApplication = Lifecycle.getApplication();
            log.debug("register applicatoin context with null name ");
            LifecycleEx.registerApplication(null, currentApplication);
            return;
        }
        _name = context.getContextPath();
        // in mock/test , _name is null
        if (_name != null && _name.startsWith("/")) {
            _name = _name.substring(1);
        }
        log.debug("register servlet application context with name '#0' ", _name);
        ServletLifecycleEx.registerApplication(_name, context);
    }

    @Destroy
    public void destroy() {
        ServletLifecycleEx.unregisterApplication(_name);
    }
}

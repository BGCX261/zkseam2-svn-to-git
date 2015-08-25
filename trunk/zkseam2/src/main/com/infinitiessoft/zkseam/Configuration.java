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

import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

/**
 * 
 * @author Dennis Chen , cola.orange@gmail.com 
 */
@Name("iss.zkseam.configuration")
@Scope(ScopeType.APPLICATION)
public class Configuration {

    boolean autoLinkDesktopConversation = false;
    
    
    
    
    public boolean isAutoLinkDesktopConversation() {
        return autoLinkDesktopConversation;
    }




    public static Configuration instance()
    {
       return (Configuration) Component.getInstance(Configuration.class, ScopeType.APPLICATION);
    }
}

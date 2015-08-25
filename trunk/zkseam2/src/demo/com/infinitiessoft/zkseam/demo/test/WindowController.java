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
package com.infinitiessoft.zkseam.demo.test;

import java.util.Date;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Page;
import org.zkoss.zul.api.Button;
import org.zkoss.zul.api.Textbox;
import org.zkoss.zul.api.Window;

/**
 * @author Dennis Chen, cola.orange@gmail.com
 *
 */
//@Name("iss.zkseam.winCtrl")
@Name("winCtrl")
@Scope(ScopeType.CONVERSATION)
public class WindowController {

    @In(value="desktop",required=false)
    Desktop desktop;
    
    @In(value="page",required=false)
    Page page;
    
    @In(value="execution",required=false)
    Execution exec;
    
    @In(value="win",required=false)
    Window window;
    
    @In(value="win.btn",required=false)
    Button btn;
    
    @In(value="win.tb",required=false)
    Textbox textbox;
    

    public void add(){
        window.setTitle("Title is "+new Date());
        btn.setLabel("Click me "+new Date());
        textbox.setValue("Time is "+new Date());
    }
}

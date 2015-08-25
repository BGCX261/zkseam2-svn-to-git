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
import java.util.Map;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.util.Initiator;
import org.zkoss.zk.ui.util.InitiatorExt;
/**
 * 
 * @author Dennis Chen , cola.orange@gmail.com 
 */
public class AnnotateActionBinderInit  implements Initiator, InitiatorExt,Serializable {

    private static final long serialVersionUID = 1L;

    /** The AnnotateDataBinder created in doAfterCompose() */
	//-- Initator --//
	public void doAfterCompose(Page page) {
		//will not be called since we implements InitiatorExt
	}
 	public boolean doCatch(java.lang.Throwable ex) {
 		return false; // do nothing
 	}
	public void doFinally() {
		// do nothing
	}
	public void doInit(Page page, Map args) {
		//TODO provide more option
	}
	
	public void doAfterCompose(Page page, Component[] comps) throws Exception {
		new AnnotateActionBinder(page);
	}

}

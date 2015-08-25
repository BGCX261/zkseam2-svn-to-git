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

import org.jboss.seam.core.Expressions;
import org.jboss.seam.log.Log;
import org.jboss.seam.log.Logging;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
/**
 * 
 * @author Dennis Chen , cola.orange@gmail.com 
 */
public class SeamExpressionActionListener implements EventListener,Serializable {

    private static final long serialVersionUID = 1L;
    private static Log log = Logging.getLog(SeamExpressionActionListener.class);
    String expression;
    
    public SeamExpressionActionListener(String expression) {
        this.expression = expression;
    }

    public void onEvent(Event event) throws Exception {
        log.debug("event #0, expression #1",event,expression);
        Expressions.instance().createMethodExpression(expression).invoke(new Object[]{});
    }

}

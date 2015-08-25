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
import java.util.Iterator;
import java.util.List;

import org.jboss.seam.log.Log;
import org.jboss.seam.log.Logging;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.metainfo.Annotation;
import org.zkoss.zk.ui.sys.ComponentCtrl;
/**
 * 
 * @author Dennis Chen , cola.orange@gmail.com 
 */
public class AnnotateActionBinder implements Serializable{

    private static final long serialVersionUID = 1L;
    private static Log log = Logging.getLog(AnnotateActionBinder.class);
    
    public AnnotateActionBinder(Page page) {
        init(page);
    }
    
    public AnnotateActionBinder(Component comp) {
        init(comp);
    }

    private void init(Page page) {
        for (final Iterator it = page.getRoots().iterator(); it.hasNext();) {
            loadAnnotations((Component) it.next());
        }
    }
    private void init(Component comp) {
        loadAnnotations(comp);
    }

    private void loadAnnotations(Component comp) {
        loadComponentPropertyAnnotation(comp);

        final List children = comp.getChildren();
        for (final Iterator it = children.iterator(); it.hasNext();) {
            loadAnnotations((Component) it.next()); // recursive back
        }
    }

    private void loadComponentPropertyAnnotation(Component comp) {
        loadComponentPropertyAnnotation(comp, "action");
    }

    private void loadComponentPropertyAnnotation(Component comp, String annName) {
        ComponentCtrl compCtrl = (ComponentCtrl) comp;
        final List props = compCtrl.getAnnotatedPropertiesBy(annName);
        for (final Iterator it = props.iterator(); it.hasNext();) {
            final String propName = (String) it.next();
            loadComponentPropertyAnnotation(comp, propName, annName);
        }
    }

    private void loadComponentPropertyAnnotation(Component comp, String propName, String annName) {
        
        if(!Events.isValid(propName)){
            return;
        }
        ComponentCtrl compCtrl = (ComponentCtrl) comp;
        final Annotation ann = compCtrl.getAnnotation(propName, annName);
        String val = ann.getAttribute("value");//default annoaton attribute
        if(val==null){
            val = ann.getAttribute("action");//again
        }
        if(val==null){
            return;
        }
        String expression = new StringBuilder().append("#{").append(val).append("}").toString();
        log.debug("action expression #0",expression);
        
        comp.addEventListener(propName,new SeamExpressionActionListener(expression));
    }

}

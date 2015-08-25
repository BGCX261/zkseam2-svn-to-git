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
package com.infinitiessoft.zkseam.demo.todo;

import java.util.List;

import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Listbox;

/**
 * @author Dennis Chen, cola.orange@gmail.com
 *
 */
public class ZKTodoEventController extends GenericForwardComposer {
    private static final long serialVersionUID = 1L;
    
    TodoEvent current = new TodoEvent();
    Listbox box;
    
    public TodoEvent getCurrent() {
        return current;
    }
    public void setCurrent(TodoEvent current) {
        this.current = current;
    }
    public List getAllEvents() {
        return TodoEventDAO.instance().findAll();
    }   
    public void onClick$add() {     
        // insert into database
        TodoEvent newEvt = new TodoEvent(current.getName(),current.getPriority(), current.getDate());
        TodoEventDAO.instance().insert(newEvt);
        current = newEvt;
    }   
    public void onClick$update() {      
        if (box.getSelectedItem() != null) {
            // update database
            current = (TodoEvent) box.getSelectedItem().getValue();
            current = TodoEventDAO.instance().update((TodoEvent) box.getSelectedItem().getValue());
            
        }
    }
    public void onClick$delete() {      
        if (box.getSelectedItem() != null) {
            TodoEventDAO.instance().delete((TodoEvent) box.getSelectedItem().getValue());
            current = new TodoEvent();
        }
    }
}

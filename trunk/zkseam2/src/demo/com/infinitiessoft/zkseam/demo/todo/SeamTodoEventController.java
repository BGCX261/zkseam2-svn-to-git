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

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import com.infinitiessoft.zkseam.DesktopConversation;

/**
 * @author Dennis Chen, cola.orange@gmail.com
 *
 */
//@Name("iss.zkseam.todoEventController")
@Name("todoCtrl")
@Scope(ScopeType.CONVERSATION)
public class SeamTodoEventController {

    @In("iss.zkseam.todoEventDao")
    TodoEventDAO evtDao;
    
    TodoEvent current = new TodoEvent();
    
    @Create
    public void create(){
        DesktopConversation.begin();
    }

    public TodoEvent getCurrent() {
        return current;
    }

    public void setCurrent(TodoEvent current) {
        this.current = current;
    }
    
    public List getAllEvents() {
        return evtDao.findAll();
    }
    public void addEvent() {
        TodoEvent newEvt = new TodoEvent(current.getName(),current.getPriority(),current.getDate());
        evtDao.insert(newEvt);
    }

    public void updateEvent() {
        if(current.getId()!=null){
            evtDao.update(current);
        }
    }
    public void deleteEvent() {
        if(current.getId()!=null){
            evtDao.delete(current);
            current = new TodoEvent();
        }
    }
}

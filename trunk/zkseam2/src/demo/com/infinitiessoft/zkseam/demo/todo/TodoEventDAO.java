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

import javax.persistence.EntityManager;

import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;

/**
 * @author Dennis Chen, cola.orange@gmail.com
 *
 */
@Name("iss.zkseam.todoEventDao")
@Scope(ScopeType.EVENT)
@AutoCreate
public class TodoEventDAO {
    
    @Logger
    Log log;
    
    @In("entityManager")
    EntityManager em;
    
    public List<TodoEvent> findAll(){
        return em.createQuery("SELECT t FROM TodoEvent t").getResultList();
    } 
    public void delete(TodoEvent evt){
        log.debug("remove #0",evt.getName());
        em.remove(em.merge(evt));
    } 
    public void insert(TodoEvent evt){
        log.debug("persist #0",evt.getName());
        em.persist(evt);        
    } 
    public TodoEvent update(TodoEvent evt){
        log.debug("merge #0",evt.getName());
        return (TodoEvent)em.merge(evt);
    }
    public void refresh(TodoEvent evt){
        log.debug("refresh #0",evt.getName());
        em.refresh(evt);
    }
    
    public static TodoEventDAO instance(){
        return (TodoEventDAO)Component.getInstance(TodoEventDAO.class,ScopeType.EVENT);
    }
    
}

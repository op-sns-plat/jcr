/*
 * Copyright (C) 2009 eXo Platform SAS.
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
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.exoplatform.services.jcr.ext.script.groovy;

import java.util.List;

/**
 * Should be used in configuration.xml as object parameter.
 * 
 * @author <a href="mailto:andrew00x@gmail.com">Andrey Parfonov</a>
 * @version $Id: ObservationListenerConfiguration.java 34445 2009-07-24 07:51:18Z dkatayev $
 */
public class ObservationListenerConfiguration
{
   /**
    * Repository name.
    */
   private String repository;

   /**
    * Workspace name.
    */
   private List<String> workspaces;

   /**
    * @return get repository
    */
   public String getRepository()
   {
      return repository;
   }

   /**
    * @param repository repository name
    */
   public void setRepository(String repository)
   {
      this.repository = repository;
   }

   /**
    * @return get list of workspaces
    */
   public List<String> getWorkspaces()
   {
      return workspaces;
   }

   /**
    * @param workspaces list of workspaces
    */
   public void setWorkspaces(List<String> workspaces)
   {
      this.workspaces = workspaces;
   }

}

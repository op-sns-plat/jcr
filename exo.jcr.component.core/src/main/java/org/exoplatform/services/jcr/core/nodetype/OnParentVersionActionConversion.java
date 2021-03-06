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
package org.exoplatform.services.jcr.core.nodetype;

import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;

import javax.jcr.version.OnParentVersionAction;

/**
 * Serialization/Deserialization for OnParentVersionAction beans. For JiBX binding process only.
 * 
 * @author <a href="mailto:peterit@rambler.ru">Petro Nedonosko</a>
 */
public class OnParentVersionActionConversion
{

   private static final Log LOG = ExoLogger.getLogger("exo.jcr.component.core.OnParentVersionActionConversion");

   public static String serializeType(int propertyType)
   {

      String r = OnParentVersionAction.ACTIONNAME_IGNORE; // default
      try
      {
         r = OnParentVersionAction.nameFromValue(propertyType);
      }
      catch (IllegalArgumentException e)
      {
         // r = PropertyType.TYPENAME_UNDEFINED;
         if (LOG.isTraceEnabled())
         {
            LOG.trace("An exception occurred: " + e.getMessage());
         }
      }
      catch (Exception e)
      {
         // r = PropertyType.TYPENAME_UNDEFINED;
         if (LOG.isTraceEnabled())
         {
            LOG.trace("An exception occurred: " + e.getMessage());
         }
      }
      return r;
   }

   public static int deserializeType(String propertyTypeString)
   {

      int r = OnParentVersionAction.IGNORE; // default
      try
      {
         r = OnParentVersionAction.valueFromName(propertyTypeString);
      }
      catch (IllegalArgumentException e)
      {
         // r = PropertyType.TYPENAME_UNDEFINED;
         if (LOG.isTraceEnabled())
         {
            LOG.trace("An exception occurred: " + e.getMessage());
         }
      }
      catch (Exception e)
      {
         // r = PropertyType.TYPENAME_UNDEFINED;
         if (LOG.isTraceEnabled())
         {
            LOG.trace("An exception occurred: " + e.getMessage());
         }
      }
      return r;
   }

}

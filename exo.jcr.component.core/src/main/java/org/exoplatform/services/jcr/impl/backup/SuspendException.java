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
package org.exoplatform.services.jcr.impl.backup;

/**
 * @author <a href="mailto:anatoliy.bazko@gmail.com">Anatoliy Bazko</a>
 * @version $Id: SuspendException.java 34360 2009-07-22 23:58:59Z tolusha $
 */
public class SuspendException extends Exception
{
   /**
    * Constructor SuspendException.
    * 
    * @param cause
    *          the cause
    */
   public SuspendException(Throwable cause)
   {
      super(cause);
   }

   /**
    * Constructor SuspendException.
    * 
    * @param message
    *          the message
    */
   public SuspendException(String message)
   {
      super(message);
   }

   /**
    * Constructor SuspendException.
    * 
    * @param message
    *          the message
    * @param cause
    *          the cause
    */
   public SuspendException(String message, Throwable cause)
   {
      super(message, cause);
   }
}

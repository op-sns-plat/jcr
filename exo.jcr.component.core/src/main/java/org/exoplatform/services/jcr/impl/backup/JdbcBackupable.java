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

import java.io.File;
import java.sql.Connection;

/**
 * @author <a href="mailto:anatoliy.bazko@gmail.com">Anatoliy Bazko</a>
 * @version $Id: Backupable.java 34360 2009-07-22 23:58:59Z tolusha $
 */
public interface JdbcBackupable extends Backupable
{

   /**
    * Get data restorer to support atomic restore.
    * 
    * @param storageDir
    *          the directory where backup is stored
    * @param jdbcConn
    *          the connection to database          
    * @throws RestoreException
    *          if any exception occurred
    */
   DataRestore getDataRestorer(File storageDir, Connection jdbcConn) throws BackupException;

}
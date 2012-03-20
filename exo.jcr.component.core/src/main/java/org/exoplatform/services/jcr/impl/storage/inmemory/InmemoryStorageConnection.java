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
package org.exoplatform.services.jcr.impl.storage.inmemory;

import org.exoplatform.services.jcr.datamodel.ItemData;
import org.exoplatform.services.jcr.datamodel.ItemType;
import org.exoplatform.services.jcr.datamodel.NodeData;
import org.exoplatform.services.jcr.datamodel.PropertyData;
import org.exoplatform.services.jcr.datamodel.QPath;
import org.exoplatform.services.jcr.datamodel.QPathEntry;
import org.exoplatform.services.jcr.datamodel.ValueData;
import org.exoplatform.services.jcr.impl.core.itemfilters.QPathEntryFilter;
import org.exoplatform.services.jcr.impl.dataflow.persistent.ACLHolder;
import org.exoplatform.services.jcr.storage.WorkspaceStorageConnection;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

import javax.jcr.InvalidItemStateException;
import javax.jcr.ItemExistsException;
import javax.jcr.PropertyType;
import javax.jcr.RepositoryException;

/**
 * Created by The eXo Platform SAS.
 * 
 * @author <a href="mailto:gennady.azarenkov@exoplatform.com">Gennady Azarenkov</a>
 * @version $Id: InmemoryStorageConnection.java 11907 2008-03-13 15:36:21Z ksm $
 */

public class InmemoryStorageConnection implements WorkspaceStorageConnection
{

   private static Log log = ExoLogger.getLogger("exo.jcr.component.core.InmemoryStorageConnection");

   private TreeMap items;

   private TreeMap identifiers;

   InmemoryStorageConnection(String name)
   {
      items = WorkspaceContainerRegistry.getInstance().getWorkspaceContainer(name);
      identifiers = new TreeMap();
   }

   /**
    * {@inheritDoc}
    */
   public ItemData getItemData(NodeData parentData, QPathEntry name) throws RepositoryException, IllegalStateException
   {
      return getItemData(parentData, name, ItemType.UNKNOWN);
   }

   public ItemData getItemData(NodeData parentData, QPathEntry name, ItemType itemType) throws RepositoryException,
      IllegalStateException
   {
      ItemData itemData = null;
      QPath qPath = QPath.makeChildPath(parentData.getQPath(), name);

      if (itemType == ItemType.NODE || itemType == ItemType.UNKNOWN)
      {
         itemData = (ItemData)items.get(new MapKey(qPath, ItemType.NODE));
      }
      if (itemType == ItemType.PROPERTY || itemType == ItemType.UNKNOWN && itemData == null)
      {
         itemData = (ItemData)items.get(new MapKey(qPath, ItemType.PROPERTY));
      }

      return itemData;
   }

   public ItemData getItemData(QPath qPath) throws RepositoryException, IllegalStateException
   {
      log.debug("InmemoryContainer finding " + qPath.getAsString());

      Object o = items.get(new MapKey(qPath, ItemType.NODE));
      if (o == null)
      {
         o = items.get(new MapKey(qPath, ItemType.PROPERTY));
      }

      log.debug("InmemoryContainer FOUND " + qPath.getAsString() + " " + o);
      return (ItemData)o;
   }

   public ItemData getItemData(String identifier) throws RepositoryException, IllegalStateException
   {
      Iterator itemsIterator = items.values().iterator();
      while (itemsIterator.hasNext())
      {
         ItemData data = (ItemData)itemsIterator.next();
         if (data.getIdentifier().equals(identifier))
            return data;
      }
      return null;
   }

   public List<NodeData> getChildNodesData(NodeData parent) throws RepositoryException, IllegalStateException
   {
      return null;
   }

   public List<NodeData> getChildNodesData(NodeData parent, List<QPathEntryFilter> pattern) throws RepositoryException,
      IllegalStateException
   {
      return getChildNodesData(parent);
   }

   public List<PropertyData> getChildPropertiesData(NodeData parent) throws RepositoryException, IllegalStateException
   {
      return null;
   }

   public List<PropertyData> getChildPropertiesData(NodeData parent, List<QPathEntryFilter> pattern)
      throws RepositoryException, IllegalStateException
   {
      return getChildPropertiesData(parent);
   }

   public List<PropertyData> listChildPropertiesData(NodeData parent) throws RepositoryException, IllegalStateException
   {
      return null;
   }

   public int getLastOrderNumber(NodeData nodeData) throws RepositoryException
   {
      return -1;
   }
   
   public int getChildNodesCount(NodeData nodeData) throws RepositoryException
   {
      return 0;
   }

   public int getChildPropertiesCount(NodeData nodeData) throws RepositoryException
   {
      return 0;
   }

   public List<PropertyData> getReferencesData(String identifier) throws RepositoryException, IllegalStateException
   {
      ArrayList<PropertyData> refs = new ArrayList<PropertyData>();
      Iterator it = items.values().iterator();
      while (it.hasNext())
      {
         ItemData itemData = (ItemData)it.next();
         ValueData identifierVal = ((PropertyData)itemData).getValues().get(0);
         try
         {
            if ((itemData instanceof PropertyData) && ((PropertyData)itemData).getType() == PropertyType.REFERENCE
               && new String(identifierVal.getAsByteArray()).equals(identifier))
            {

               refs.add((PropertyData)itemData);
            }
         }
         catch (IOException e)
         {
            throw new RepositoryException(e);
         }
      }
      return refs;
   }

   public void add(NodeData item) throws RepositoryException, UnsupportedOperationException, InvalidItemStateException,
      IllegalStateException
   {

      if (items.get(new MapKey(item.getQPath(), ItemType.getItemType(item))) != null)
         throw new ItemExistsException("WorkspaceContainerImpl.add(Item) item '" + item.getQPath().getAsString()
            + "' already exists!");

      items.put(new MapKey(item.getQPath(), ItemType.getItemType(item)), item);
      log.debug("InmemoryContainer added node " + item.getQPath().getAsString());
      Iterator props = getChildProperties(item).iterator();
      while (props.hasNext())
      {
         add((PropertyData)props.next());
      }
      Iterator nodes = getChildNodes(item).iterator();
      while (nodes.hasNext())
      {
         add((NodeData)nodes.next());
      }
   }

   public void add(PropertyData prop) throws RepositoryException, UnsupportedOperationException,
      InvalidItemStateException, IllegalStateException
   {
      items.put(new MapKey(prop.getQPath(), ItemType.getItemType(prop)), prop);
      log.debug("InmemoryContainer added property " + prop.getQPath().getAsString());
   }

   public void update(NodeData data) throws RepositoryException, UnsupportedOperationException,
      InvalidItemStateException, IllegalStateException
   {
      throw new UnsupportedOperationException("not implemented");
   }

   public void reindex(NodeData oldData, NodeData data) throws RepositoryException, UnsupportedOperationException,
      InvalidItemStateException, IllegalStateException
   {
      throw new UnsupportedOperationException("not implemented");
   }

   public void update(PropertyData item) throws RepositoryException, UnsupportedOperationException,
      InvalidItemStateException, IllegalStateException
   {
      items.put(new MapKey(item.getQPath(), ItemType.getItemType(item)), item);
      log.debug("InmemoryContainer updated " + item);
   }

   public void delete(NodeData data) throws RepositoryException, UnsupportedOperationException,
      InvalidItemStateException, IllegalStateException
   {
      items.remove(new MapKey(data.getQPath(), ItemType.getItemType(data)));
      log.debug("InmemoryContainer removed " + data.getQPath().getAsString());
   }

   public void delete(PropertyData data) throws RepositoryException, UnsupportedOperationException,
      InvalidItemStateException, IllegalStateException
   {
      items.remove(new MapKey(data.getQPath(), ItemType.getItemType(data)));
      log.debug("InmemoryContainer removed " + data.getQPath().getAsString());
   }

   public void commit() throws IllegalStateException, RepositoryException
   {
   }

   public void rollback() throws IllegalStateException, RepositoryException
   {
   }

   public void prepare() throws IllegalStateException, RepositoryException
   {
   }

   public void close() throws IllegalStateException, RepositoryException
   {
   }

   public boolean isOpened()
   {
      return false;
   }

   protected List getChildProperties(NodeData node)
   {
      return null;
   }

   protected List getChildNodes(NodeData node)
   {
      return null;
   }

   public String dump()
   {
      StringBuilder str = new StringBuilder("Inmemory WorkspaceContainer Data: \n");
      Iterator i = items.keySet().iterator();
      while (i.hasNext())
      {
         MapKey d = (MapKey)i.next();
         str.append(d.getQPath().getAsString()).append('\t').append(d.getItemType().toString()).append("\n");
      }
      return str.toString();
   }

   public void rename(NodeData destData) throws RepositoryException, UnsupportedOperationException,
      InvalidItemStateException, IllegalStateException
   {
      throw new UnsupportedOperationException();

   }
   
   /**
    * @see org.exoplatform.services.jcr.storage.WorkspaceStorageConnection#getACLHolders()
    */
   public List<ACLHolder> getACLHolders() throws RepositoryException, IllegalStateException,
      UnsupportedOperationException
   {
      throw new UnsupportedOperationException();
   }

   class MapKey
   {

      private final QPath path;

      private final String key;

      private final ItemType itemType;

      MapKey(QPath path, ItemType itemType)
      {
         this.path = path;
         this.itemType = itemType;
         this.key = key(this.path, this.itemType);
      }

      protected String key(final QPath path, ItemType itemType)
      {
         StringBuilder sk = new StringBuilder();
         sk.append(path.getAsString());
         sk.append(itemType.toString());

         return sk.toString();
      }

      @Override
      public boolean equals(Object obj)
      {
         if (key.hashCode() == obj.hashCode() && obj instanceof MapKey)
            return key.equals(((MapKey)obj).key);
         return false;
      }

      @Override
      public int hashCode()
      {
         return key.hashCode();
      }

      QPath getQPath()
      {
         return path;
      }

      ItemType getItemType()
      {
         return itemType;
      }
   }

   /**
    * {@inheritDoc}
    */
   public boolean getChildNodesDataByPage(NodeData parent, int fromOrderNum, int toOrderNum, List<NodeData> childs)
      throws RepositoryException
   {
      throw new UnsupportedOperationException(
         "The method getChildNodesDataLazily is supported only for JDBCStorageConnection");
   }
}

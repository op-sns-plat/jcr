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
package org.exoplatform.services.jcr.lab.cluster.test;

import org.exoplatform.services.jcr.JcrAPIBaseTest;
import org.exoplatform.services.jcr.core.CredentialsImpl;
import org.exoplatform.services.jcr.impl.core.SessionImpl;
import org.exoplatform.services.jcr.lab.cluster.prepare.TestLoadIndexerWriter;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Random;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;

/**
 * @author <a href="mailto:nikolazius@gmail.com">Nikolay Zamosenchuk</a>
 * @version $Id: TestLoadIndexerQuery.java 34360 2009-07-22 23:58:59Z nzamosenchuk $
 *
 */
public class TestLoadIndexerQueryWithModes extends JcrAPIBaseTest
{

   private Log log = ExoLogger.getLogger("exo.jcr.component.core.TestLoadIndexerQueryWithModes");

   private boolean stop = false;

   private int threadCount = 20;

   public void testQuery() throws RepositoryException
   {
      try
      {
         System.out.println("Skip (y/n) :");
         BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
         String line = reader.readLine();
         if (!line.equals("y"))
         {
            log.info("Creating threads...");
            for (int i = 0; i < threadCount; i++)
            {
               // create new thread and start it
               new Thread(new QueryTask()).start();
               log.info("Thread#" + i + " created and started.");
            }
         }
         else
         {
            log.info("Wait for data");
         }

         // wait 4 minutes

         Thread.sleep(60000 * 4000);
      }
      catch (Exception e)
      {
         log.error(e);
      }

      stop = true;
   }

   private class QueryTask implements Runnable
   {

      private Random random;

      public QueryTask() throws RepositoryException
      {
         random = new Random();
      }

      /**
       * @see java.lang.Runnable#run()
       */
      public void run()
      {

         while (!stop)
         {
            Session sessionLocal = null;
            try
            {
               // login
               CredentialsImpl credentials = new CredentialsImpl("admin", "admin".toCharArray());
               sessionLocal = (SessionImpl)repository.login(credentials, "ws");
               // prepare nodes
               Node rootLocal = sessionLocal.getRootNode();
               Node threadNode = getRandomChild(rootLocal, "Thread*");
               if (threadNode != null)
               {
                  Node statisticNode = threadNode.getNode(TestLoadIndexerWriter.STATISTIC);
                  Node contentNode = threadNode.getNode(TestLoadIndexerWriter.CONTENT);
                  Node wordNode = getRandomChild(statisticNode, "*");
                  if (wordNode != null)
                  {
                     String word = wordNode.getName();
                     Long count = wordNode.getProperty(TestLoadIndexerWriter.COUNT).getLong();

                     try
                     {
                        Thread.sleep(2000);
                     }
                     catch (InterruptedException e1)
                     {
                     }

                     QueryManager qman = sessionLocal.getWorkspace().getQueryManager();

                     Query q =
                        qman.createQuery("SELECT * FROM nt:base WHERE jcr:path LIKE '" + contentNode.getPath()
                           + "/%' and fn:name() = '" + word + "'", Query.SQL);
                     long time = System.currentTimeMillis();
                     QueryResult res = q.execute();
                     long sqlsize = res.getNodes().getSize();
                     log.info("Exp: " + count + "\t found:" + sqlsize + " time=" + (System.currentTimeMillis() - time));
                  }
               }
            }
            catch (Exception e)
            {
               log.error(e);
            }
            finally
            {
               if (sessionLocal != null)
               {
                  sessionLocal.logout();
                  sessionLocal = null;
               }
            }
         }
      }

      private Node getRandomChild(Node parent, String pattern) throws RepositoryException
      {
         NodeIterator iterator = parent.getNodes(pattern);
         if (iterator.getSize() < 1)
         {
            return null;
         }
         int i = random.nextInt((int)iterator.getSize());
         iterator.skip(i);
         return iterator.nextNode();
      }
   }
}

/**
 *
 * Copyright (C) 2011 Cloud Conscious, LLC. <info@cloudconscious.com>
 *
 * ====================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ====================================================================
 */
package org.jclouds.gogrid.compute.strategy;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.jclouds.compute.domain.ComputeMetadata;
import org.jclouds.compute.domain.NodeMetadata;
import org.jclouds.compute.predicates.NodePredicates;
import org.jclouds.compute.strategy.ListNodesStrategy;
import org.jclouds.gogrid.GoGridClient;
import org.jclouds.gogrid.domain.Server;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

/**
 * 
 * @author Adrian Cole
 */
@Singleton
public class GoGridListNodesStrategy implements ListNodesStrategy {
   private final GoGridClient client;
   private final Function<Server, NodeMetadata> serverToNodeMetadata;

   @Inject
   protected GoGridListNodesStrategy(GoGridClient client, Function<Server, NodeMetadata> serverToNodeMetadata) {
      this.client = client;
      this.serverToNodeMetadata = serverToNodeMetadata;
   }

   @Override
   public Iterable<? extends ComputeMetadata> listNodes() {
      return listDetailsOnNodesMatching(NodePredicates.all());
   }

   @Override
   public Iterable<? extends NodeMetadata> listDetailsOnNodesMatching(Predicate<ComputeMetadata> filter) {
      return Iterables.filter(Iterables.transform(client.getServerServices().getServerList(), serverToNodeMetadata),
               filter);
   }
}
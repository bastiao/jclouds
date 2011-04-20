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

/**
 * 
 */
package org.jclouds.datacolumnar;

import java.util.Map;

import javax.annotation.Nullable;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import org.jclouds.datacolumnar.attr.TableID;
import org.jclouds.location.functions.RegionToEndpointOrProviderIfNull;
import org.jclouds.rest.annotations.EndpointParam;
import org.jclouds.rest.annotations.FormParams;
import org.jclouds.rest.annotations.XMLResponseParser;

import com.google.common.collect.Table;
import com.google.common.util.concurrent.ListenableFuture;

/**
 * @author Luís A. Bastião Silva <bastiao@ua.pt>
 *
 */
public interface AsyncDataColumnar {


	   
	   public ListenableFuture<String> putAttributes(TableID table, Table blob);

	   public ListenableFuture<Table> select(TableID table, String conditionalExpression); 
	
	   
	   
	   

}

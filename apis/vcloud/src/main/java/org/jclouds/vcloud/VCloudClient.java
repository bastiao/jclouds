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
package org.jclouds.vcloud;

import java.io.InputStream;
import java.net.URI;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nullable;

import org.jclouds.concurrent.Timeout;
import org.jclouds.ovf.Envelope;
import org.jclouds.vcloud.domain.CatalogItem;
import org.jclouds.vcloud.domain.GuestCustomizationSection;
import org.jclouds.vcloud.domain.NetworkConnectionSection;
import org.jclouds.vcloud.domain.ReferenceType;
import org.jclouds.vcloud.domain.Task;
import org.jclouds.vcloud.domain.VApp;
import org.jclouds.vcloud.domain.VAppTemplate;
import org.jclouds.vcloud.domain.Vm;
import org.jclouds.vcloud.options.CaptureVAppOptions;
import org.jclouds.vcloud.options.CloneVAppOptions;
import org.jclouds.vcloud.options.InstantiateVAppTemplateOptions;

/**
 * Provides access to VCloud resources via their REST API.
 * <p/>
 * 
 * @see <a
 *      href="http://communities.vmware.com/community/developer/forums/vcloudapi"
 *      />
 * @author Adrian Cole
 */
@Timeout(duration = 300, timeUnit = TimeUnit.SECONDS)
public interface VCloudClient extends CommonVCloudClient {

   /**
    * Get a Screen Thumbnail for a Virtual Machine
    * 
    * @param vm
    *           to snapshot
    */
   InputStream getThumbnailOfVm(URI vm);

   /**
    * The response to a login request includes a list of the organizations to
    * which the authenticated user has access.
    * 
    * @return organizations indexed by name
    */
   Map<String, ReferenceType> listOrgs();

   VApp instantiateVAppTemplateInVDC(URI vDC, URI template, String appName, InstantiateVAppTemplateOptions... options);

   Task cloneVAppInVDC(URI vDC, URI toClone, String newName, CloneVAppOptions... options);

   /**
    * The captureVApp request creates a vApp template from an instantiated vApp.
    * <h4>Note</h4> Before it can be captured, a vApp must be undeployed
    * 
    * @param vDC
    * @param toClone
    * @param templateName
    * @param options
    * @return template in progress
    */
   VAppTemplate captureVAppInVDC(URI vDC, URI toClone, String templateName, CaptureVAppOptions... options);

   VAppTemplate getVAppTemplate(URI vAppTemplate);

   Envelope getOvfEnvelopeForVAppTemplate(URI vAppTemplate);

   /**
    * Modify the Guest Customization Section of a Virtual Machine
    * 
    * @param vm
    *           uri to modify
    * @param updated
    *           guestCustomizationSection
    * @return task in progress
    */
   Task updateGuestCustomizationOfVm(URI vm, GuestCustomizationSection guestCustomizationSection);

   /**
    * Modify the Network Connection Section of a Virtual Machine
    * 
    * @param vm
    *           uri to modify
    * @param updated
    *           networkConnectionSection
    * @return task in progress
    */
   Task updateNetworkConnectionOfVm(URI vm, NetworkConnectionSection guestCustomizationSection);

   /**
    * returns the vapp template corresponding to a catalog item in the catalog
    * associated with the specified name. Note that the org and catalog
    * parameters can be null to choose default.
    * 
    * @param orgName
    *           organization name, or null for the default
    * @param catalogName
    *           catalog name, or null for the default
    * @param itemName
    *           item you wish to lookup
    * 
    * @throws NoSuchElementException
    *            if you specified an org, catalog, or catalog item name that
    *            isn't present
    */
   VAppTemplate findVAppTemplateInOrgCatalogNamed(@Nullable String orgName, @Nullable String catalogName,
         String itemName);

   VApp findVAppInOrgVDCNamed(@Nullable String orgName, @Nullable String catalogName, String vAppName);

   VApp getVApp(URI vApp);

   Vm getVm(URI vm);

   /**
    * update the cpuCount of an existing VM
    * 
    * @param vm
    *           to update
    * @param cpuCount
    *           count to change the primary cpu to
    */
   Task updateCPUCountOfVm(URI vm, int cpuCount);

   /**
    * update the memoryInMB of an existing VM
    * 
    * @param vm
    *           to update
    * @param memoryInMB
    *           memory in MB to assign to the VM
    */
   Task updateMemoryMBOfVm(URI vm, int memoryInMB);

   /**
    * To deploy a vApp, the client makes a request to its action/deploy URL.
    * Deploying a vApp automatically deploys all of the virtual machines it
    * contains. To deploy a virtual machine, the client makes a request to its
    * action/deploy URL.
    * <p/>
    * Deploying a Vm implicitly deploys the parent vApp if that vApp is not
    * already deployed.
    */
   Task deployVAppOrVm(URI vAppOrVmId);

   /**
    * like {@link #deployVAppOrVm(URI)}, except deploy transistions to power on
    * state
    * 
    */
   Task deployAndPowerOnVAppOrVm(URI vAppOrVmId);

   /**
    * Undeploying a vApp powers off or suspends any running virtual machines it
    * contains, then frees the resources reserved for the vApp and sets the
    * vApp’s deploy attribute to a value of false to indicate that it is not
    * deployed.
    * <p/>
    * Undeploying a virtual machine powers off or suspends the virtual machine,
    * then frees the resources reserved for it and sets the its deploy attribute
    * to a value of false to indicate that it is not deployed. This operation
    * has no effect on the containing vApp.
    * <h4>NOTE</h4>
    * Using this method will simply power off the vms. In order to save their
    * state, use {@link #undeployAndSaveStateOfVAppOrVm}
    * 
    */
   Task undeployVAppOrVm(URI vAppOrVmId);

   /**
    * like {@link #undeployVAppOrVm(URI)}, where the undeployed virtual machines
    * are suspended and their suspend state saved
    * 
    */
   Task undeployAndSaveStateOfVAppOrVm(URI vAppOrVmId);

   /**
    * A powerOn request to a vApp URL powers on all of the virtual machines in
    * the vApp, as specified in the vApp’s StartupSection field.
    * <p/>
    * A powerOn request to a virtual machine URL powers on the specified virtual
    * machine and forces deployment of the parent vApp.
    * <p/>
    * <h4>NOTE</h4> A powerOn request to a vApp or virtual machine that is
    * undeployed forces deployment.
    */
   Task powerOnVAppOrVm(URI vAppOrVmId);

   /**
    * A powerOff request to a vApp URL powers off all of the virtual machines in
    * the vApp, as specified in its StartupSection field.
    * <p/>
    * A powerOff request to a virtual machine URL powers off the specified
    * virtual machine.
    */
   Task powerOffVAppOrVm(URI vAppOrVmId);

   /**
    * A shutdown request to a vApp URL shuts down all of the virtual machines in
    * the vApp, as specified in its StartupSection field.
    * <p/>
    * A shutdown request to a virtual machine URL shuts down the specified
    * virtual machine.
    * <p/>
    * <h4>NOTE</h4Because this request sends a signal to the guest OS, the
    * vCloud API cannot track the progress or verify the result of the requested
    * operation. Hence, void is returned
    */
   void shutdownVAppOrVm(URI vAppOrVmId);

   /**
    * A reset request to a vApp URL resets all of the virtual machines in the
    * vApp, as specified in its StartupSection field.
    * <p/>
    * A reset request to a virtual machine URL resets the specified virtual
    * machine.
    */
   Task resetVAppOrVm(URI vAppOrVmId);

   /**
    * A reboot request to a vApp URL reboots all of the virtual machines in the
    * vApp, as specified in its StartupSection field.
    * <p/>
    * A reboot request to a virtual machine URL reboots the specified virtual
    * machine.
    * <p/>
    * <h4>NOTE</h4> Because this request sends a signal to the guest OS, the
    * vCloud API cannot track the progress or verify the result of the requested
    * operation. Hence, void is returned
    */
   void rebootVAppOrVm(URI vAppOrVmId);

   /**
    * A suspend request to a vApp URL suspends all of the virtual machines in
    * the vApp, as specified in its StartupSection field.
    * <p/>
    * A suspend request to a virtual machine URL suspends the specified virtual
    * machine.
    */
   Task suspendVAppOrVm(URI vAppOrVmId);

   /**
    * delete a vAppTemplate, vApp, or media image. You cannot delete an object
    * if it is in use. Any object that is being copied or moved is in use. Other
    * criteria that determine whether an object is in use depend on the object
    * type.
    * <ul>
    * <li>A vApptemplate is in use if it is being instantiated. After
    * instantiation is complete, the template is no longer in use.</li>
    * <li>A vApp is in use if it is deployed.</li>
    * <li>A media image is in use if it is inserted in a Vm.</li>
    * </ul>
    * 
    * @param id
    *           href of the vAppTemplate, vApp, or media image
    * @return task of the operation in progress
    */
   Task deleteVAppTemplateVAppOrMediaImage(URI id);

   /**
    * 
    * @see deleteVAppTemplateVAppOrMediaImage
    */
   @Deprecated
   Task deleteVApp(URI vAppId);

   /**
    * A catalog can contain references to vApp templates and media images that
    * have been uploaded to any vDC in an organization. A vApp template or media
    * image can be listed in at most one catalog.
    * 
    * @param catalog
    *           URI of the catalog to add the resourceEntity from
    * @param name
    *           name of the entry in the catalog
    * @param description
    *           description of the entry in the catalog
    * @param entity
    *           the reference to the item from the VDC
    * @param properties
    *           metadata to associate with this item
    * @return the new catalog item
    */
   CatalogItem addResourceEntitytoCatalog(URI catalog, String name, String description, URI entity,
         Map<String, String> properties);

   CatalogItem addResourceEntitytoCatalog(URI catalog, String name, String description, URI entity);

}

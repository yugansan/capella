/*******************************************************************************
 * Copyright (c) 2006, 2015 THALES GLOBAL SERVICES.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *  
 * Contributors:
 *    Thales - initial API and implementation
 *******************************************************************************/
package org.polarsys.capella.core.data.migration.contribution;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.xmi.XMIException;
import org.eclipse.emf.ecore.xmi.XMLHelper;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.polarsys.capella.common.ef.ExecutionManager;
import org.polarsys.capella.core.data.migration.context.MigrationContext;
import org.xml.sax.Attributes;

/**
 * 
 */
public interface IMigrationContribution {

  /**
   * This method is called for each files that needs to be migrated (each files given per contributor)
   */
  public IStatus preMigrationExecute(IResource fileToMigrate, MigrationContext context, boolean checkVersion);

  /**
   * This method allows to do additional migration on the resourceSet before resources are saved (this is run in a transactional command)
   */
  public void postMigrationExecute(ExecutionManager executionManager, ResourceSet resourceSet, MigrationContext context);

  /**
   * This method allows to do additional commands on the resourceSet before resources are saved
   */
  public void postMigrationExecuteCommands(ExecutionManager executionManager, ResourceSet resourceSet, MigrationContext context);

  /**
   * This method allows to do additional processing before saving any resources
   */
  public void preSaveResource(ExecutionManager executionManager, Resource resource, MigrationContext context);

  /**
   * This method allows to retrieve the EFactory for the given prefix
   */
  public EFactory getEFactory(String prefix, Resource resource, MigrationContext context);

  /**
   * This method allows to rename the type that will be created for the given typeQName into peekObject.eGet(feature)
   */
  public String getQName(EObject peekObject, String typeQName, EStructuralFeature feature, Resource resource, XMLHelper helper, MigrationContext context);

  /**
   * This method allows to rename a feature
   */
  public String getFeatureName(String prefix, String name, boolean isElement, EObject peekObject, String value, Resource resource, MigrationContext context);

  /**
   * This methods allows to retrieve another feature for the given feature
   */
  public EStructuralFeature getFeature(EObject peekObject, EStructuralFeature feature, Resource resource, MigrationContext context);

  /**
   * This method allows to retrieve another value for the given object and the given feature
   */
  public Object getValue(EObject peekObject, EStructuralFeature feature, Object value, int position, Resource resource, MigrationContext context);

  /**
   * This method allows to do additional stuff to the given element created
   */
  public void updateElement(EObject peekObject, String typeName, EObject createdElement, EStructuralFeature feature, Resource resource, MigrationContext context);

  /**
   * This method is called at the end of the migration
   */
  public void dispose(MigrationContext context);

  /**
   * This method is called at the end of each MigrationRunnable
   */
  public void dispose(ExecutionManager manager, ResourceSet resourceSet, MigrationContext context);

  /**
   * This method allows to rename a prefix to another Should return "xmlns:prefix"
   */
  public String getNSPrefix(String prefix, MigrationContext context);

  /**
   * This method allows to rename an nsURI to another
   */
  public String getNSURI(String prefix, String nsUri, MigrationContext context);

  /**
   * This method allows to contribute to the packageRegistry if someone wants to
   */
  public void contributePackageRegistry(org.eclipse.emf.ecore.EPackage.Registry packageRegistry, MigrationContext context);

  /**
   * This method will be called before browsing each elements of the given resource
   */
  public void unaryStartMigrationExecute(ExecutionManager executionManager, Resource resource, MigrationContext context);

  /**
   * This method will be called for each elements (this allows to avoid each contribution to browse each resources, browsing is made only one time)
   */
  public void unaryMigrationExecute(EObject currentElement, MigrationContext context);

  /**
   * This method will be called after browsing each elements of the given resource
   */
  public void unaryEndMigrationExecute(ExecutionManager executionManager, Resource resource, MigrationContext context);

  /**
   * This method is called to let contributions know that the given resource is created
   */
  public void newResource(Resource resource, MigrationContext context);

  /**
   * This method is used to rename a given proxy to another
   */
  public String getHandleProxy(InternalEObject proxy, String uriLiteral, Resource resource, XMLHelper helper, MigrationContext context);

  /**
   * This method is used to change priority of a given EMF error
   */
  public IStatus handleError(XMIException e, Resource resource, MigrationContext context);

  /**
   * This method allows to perform modification after SAX-parsing the peekObject
   */
  public void endElement(EObject peekEObject, Attributes attribs, String uri, String localName, String name, Resource resource, MigrationContext context);

  /**
   * This methods allows to ignore setting the given value in the given feature
   * @return true whether the value of the given feature should be ignored
   */
  public boolean ignoreSetFeatureValue(EObject peekObject, EStructuralFeature feature, Object value, int position, XMLResource resource,
      MigrationContext context);

  /**
   * This method allows to ignore the given unknown feature
   */
  public boolean ignoreUnknownFeature(String prefix, String name, boolean isElement, EObject peekObject, String value, XMLResource resource,
      MigrationContext context);

  /**
   * This method is a notification to let contribution know that the given helper will be used for the given resource
   */
  public void createdXMLHelper(XMLResource resource, XMLHelper result);

}
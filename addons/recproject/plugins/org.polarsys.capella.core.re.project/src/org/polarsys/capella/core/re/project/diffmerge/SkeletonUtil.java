/*******************************************************************************
 * Copyright (c) 2017 THALES GLOBAL SERVICES.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *   
 * Contributors:
 *    Thales - initial API and implementation
 *******************************************************************************/
package org.polarsys.capella.core.re.project.diffmerge;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Predicate;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.diffmerge.api.IComparison;
import org.eclipse.emf.diffmerge.api.Role;
import org.eclipse.emf.diffmerge.api.diff.IDifference;
import org.eclipse.emf.diffmerge.api.diff.IElementPresence;
import org.eclipse.emf.diffmerge.api.scopes.IEditableModelScope;
import org.eclipse.emf.diffmerge.diffdata.impl.EComparisonImpl;
import org.eclipse.emf.diffmerge.impl.scopes.FragmentedModelScope;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.polarsys.capella.common.ef.ExecutionManager;
import org.polarsys.capella.common.ef.ExecutionManagerRegistry;
import org.polarsys.capella.core.data.capellamodeller.CapellamodellerPackage;
import org.polarsys.capella.core.re.project.ReProjectActivator;

public class SkeletonUtil {

  /**
   * Find all elements in model that are not part of the
   * capella template.
   */
  public static Collection<EObject> getAllNonSkeletonElements(EObject context, Predicate<EObject> filter){

    EObject first = context;

    Resource res = first.eResource();
    URI skeletonURI = null;
    if (EcoreUtil.getRootContainer(first).eClass() == CapellamodellerPackage.Literals.LIBRARY) {
      skeletonURI = ReProjectActivator.SKELETON_LIBRARY_URI;
    } else if (EcoreUtil.getRootContainer(first).eClass() == CapellamodellerPackage.Literals.PROJECT) {
      skeletonURI = ReProjectActivator.SKELETON_PROJECT_URI;
    }

    Collection<EObject> elementsForRec = new ArrayList<EObject>();

    if (skeletonURI != null) {

      ExecutionManager skeletonManager = ExecutionManagerRegistry.getInstance().addNewManager();

      try {

        ResourceSet set = skeletonManager.getEditingDomain().getResourceSet();
        Resource skeletonResource = set.getResource(skeletonURI, true);

        IEditableModelScope targetScope = new FragmentedModelScope(res, true); // For example
        IEditableModelScope referenceScope = new FragmentedModelScope(skeletonResource, true); // For example

        IComparison comparison = new EComparisonImpl(targetScope, referenceScope);
        comparison.compute(new SkeletonMatchPolicy(), null, null, new NullProgressMonitor());

        for (IDifference diff : comparison.getRemainingDifferences()) {
          if (diff instanceof IElementPresence && ((IElementPresence) diff).getPresenceRole() == Role.TARGET) {
            IElementPresence presence = (IElementPresence) diff;
            EObject element = presence.getElement();
            if (filter.test(element)) {
              elementsForRec.add(element);
            }
          }
        }

      } finally {
        ExecutionManagerRegistry.getInstance().removeManager(skeletonManager);
      }

  }

    return elementsForRec;
  }

}

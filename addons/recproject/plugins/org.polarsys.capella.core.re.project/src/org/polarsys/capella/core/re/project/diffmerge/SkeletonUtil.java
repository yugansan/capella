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
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.polarsys.capella.common.ef.ExecutionManager;
import org.polarsys.capella.common.ef.ExecutionManagerRegistry;
import org.polarsys.capella.core.data.capellamodeller.CapellamodellerPackage;
import org.polarsys.capella.core.data.capellamodeller.Project;
import org.polarsys.capella.core.libraries.ui.wizard.newLibrary.CreateCapellaLibraryCommand;
import org.polarsys.capella.core.model.handler.helpers.CapellaProjectHelper.ProjectApproach;
import org.polarsys.capella.core.model.skeleton.EngineeringDomain;
import org.polarsys.capella.core.model.skeleton.impl.SkeletonServicesImpl;
import org.polarsys.capella.core.model.skeleton.impl.cmd.CreateCapellaProjectCmd;

public class SkeletonUtil {

  private static Project createSkeleton(EClass root, ExecutionManager manager) {

    Project result = null;
    ResourceSet set = manager.getEditingDomain().getResourceSet();
    Resource skeletonResource = set.createResource(URI.createURI("skeleton.melodymodeller")); //$NON-NLS-1$

    CreateCapellaProjectCmd cmd = null;
    if (root == CapellamodellerPackage.Literals.PROJECT) {
      cmd = new CreateCapellaProjectCmd(skeletonResource, "skeleton", ProjectApproach.SingletonComponents); //$NON-NLS-1$
    } else if (root == CapellamodellerPackage.Literals.LIBRARY) {
      cmd = new CreateCapellaLibraryCommand(skeletonResource, "skeleton", ProjectApproach.SingletonComponents); //$NON-NLS-1$
    }

    if (cmd != null) {
      manager.execute(cmd);
      result = cmd.getProject();
      new SkeletonServicesImpl().doSystemEngineering(result, result.getName(), EngineeringDomain.System, true);
    }

    return result;
  }

  /**
   * Find all elements in model that are not part of the capella template.
   */
  public static Collection<EObject> getAllNonSkeletonElements(EObject context, Predicate<EObject> filter) {

    ExecutionManager skeletonManager = ExecutionManagerRegistry.getInstance().addNewManager();
    Collection<EObject> elementsForRec = new ArrayList<EObject>();

    try {

      EObject first = context;
      Project project = createSkeleton(EcoreUtil.getRootContainer(first).eClass(), skeletonManager);

      if (project != null) {
        IEditableModelScope targetScope = new FragmentedModelScope(first.eResource(), true);
        IEditableModelScope referenceScope = new FragmentedModelScope(project.eResource(), true);

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
      }

    } finally {
      ExecutionManagerRegistry.getInstance().removeManager(skeletonManager);
    }

    return elementsForRec;
  }

}

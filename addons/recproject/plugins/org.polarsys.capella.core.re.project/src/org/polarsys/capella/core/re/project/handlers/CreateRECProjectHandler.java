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
package org.polarsys.capella.core.re.project.handlers;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
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
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;
import org.polarsys.capella.common.ef.ExecutionManager;
import org.polarsys.capella.common.ef.ExecutionManagerRegistry;
import org.polarsys.capella.common.helpers.EcoreUtil2;
import org.polarsys.capella.common.re.RePackage;
import org.polarsys.capella.common.re.RecCatalog;
import org.polarsys.capella.common.re.ui.handlers.uihead.UIHeadHandler;
import org.polarsys.capella.core.data.capellamodeller.CapellamodellerPackage;
import org.polarsys.capella.core.data.ctx.SystemFunction;
import org.polarsys.capella.core.data.fa.ComponentFunctionalAllocation;
import org.polarsys.capella.core.re.commands.CreateRecCommand;
import org.polarsys.capella.core.re.project.ReProjectActivator;
import org.polarsys.capella.core.re.project.diffmerge.SkeletonMatchPolicy;

public class CreateRECProjectHandler extends AbstractHandler {

  @Override
  public Object execute(ExecutionEvent event) throws ExecutionException {

    IStructuredSelection selection = (IStructuredSelection) HandlerUtil.getCurrentSelection(event);
    EObject first = (EObject) selection.getFirstElement();

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
            if (accept(element)) {
              elementsForRec.add(element);
            }
          }
        }

      } finally {
        ExecutionManagerRegistry.getInstance().removeManager(skeletonManager);
      }

    }

    if (elementsForRec.size() > 0) {

      ExecutionManager manager = ExecutionManagerRegistry.getInstance().getExecutionManager(TransactionUtil.getEditingDomain(first));
      CreateRecCommand command = new CreateRecCommand(elementsForRec, new NullProgressMonitor());
      command.addParameters(new UIHeadHandler(false));
      manager.execute(command);
    }

    return null;
  }


  private boolean accept(EObject candidate) {



    /*
     * We don't want to add technical rec stuff
     */
    if (candidate instanceof RecCatalog || EcoreUtil2.getFirstContainer(candidate, RePackage.Literals.REC_CATALOG) != null) {
      return false;
    }

    /*
     * By requirement
     */
    if (candidate instanceof ComponentFunctionalAllocation && ((ComponentFunctionalAllocation) candidate).getTargetElement() instanceof SystemFunction) {
      return false;
    }

    return true;

  }

}

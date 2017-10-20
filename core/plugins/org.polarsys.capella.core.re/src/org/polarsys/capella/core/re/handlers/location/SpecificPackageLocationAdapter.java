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
package org.polarsys.capella.core.re.handlers.location;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.EObject;
import org.polarsys.capella.common.re.CatalogElement;
import org.polarsys.capella.common.re.RePackage;
import org.polarsys.capella.core.data.capellacore.NamedElement;

class SpecificPackageLocationAdapter extends AdapterImpl {

  /**
   * Stores all created packages so we can delete empty ones on disposal
   * and to keep the package names in sync with the rpl name.
   */
  Collection<EObject> created = new HashSet<EObject>();

  /**
   * The factory that actually creates the packages for this adapter.
   */
  SpecificPackageFactory factory = new SpecificPackageFactory();

  @Override
  public void notifyChanged(Notification msg) {
    if (msg.getFeature() == RePackage.Literals.RE_NAMED_ELEMENT__NAME) {
      for (EObject pkg : created) {
        if (pkg instanceof NamedElement) {
          ((NamedElement) pkg).setName((String) msg.getNewValue());
        }
      }
    }
  }

  /**
   * Get the specific package in which the packagedElement should be stored
   * @param packagedElement
   */
  public EObject getSpecificPackage(EObject packagedElement) {
    EObject pkg = factory.getSpecificPackage(packagedElement);
    if (pkg != null) {
      created.add(pkg);
      if (pkg instanceof NamedElement) {
        ((NamedElement) pkg).setName(((CatalogElement)getTarget()).getName());
      }
    }
    return pkg;
  }

  /**
   * Disposes this adapter and deletes all empty created packages. Created packages
   * may be empty if the user picked a custom location for an element afterwards.
   */
  public void dispose() {
    getTarget().eAdapters().remove(this);
    for (Iterator<EObject> it = created.iterator(); it.hasNext();) {
      EObject next = it.next();
      if (next.eContents().isEmpty()) {
        EObject container = next.eContainer();
        if (container != null) {
          ((Collection<?>) container.eGet(next.eContainingFeature())).remove(next);
        }
      }
    }
  }

  @Override
  public boolean isAdapterForType(Object type) {
    return type == SpecificPackageLocationAdapter.class;
  }

}
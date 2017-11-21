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
package org.polarsys.capella.core.re.project;

import java.util.function.Predicate;

import org.eclipse.emf.ecore.EObject;
import org.polarsys.capella.common.helpers.EcoreUtil2;
import org.polarsys.capella.common.re.RePackage;
import org.polarsys.capella.common.re.RecCatalog;
import org.polarsys.capella.core.data.ctx.SystemFunction;
import org.polarsys.capella.core.data.fa.ComponentFunctionalAllocation;

public class ReProjectSkeletonFilter implements Predicate<EObject> {

  @Override
  public boolean test(EObject e) {

    /*
     * We don't want to add technical rec stuff
     */
    if (e instanceof RecCatalog || EcoreUtil2.getFirstContainer(e, RePackage.Literals.REC_CATALOG) != null) {
      return false;
    }

    /*
     * By requirement
     */
    if (e instanceof ComponentFunctionalAllocation && ((ComponentFunctionalAllocation) e).getTargetElement() instanceof SystemFunction) {
      return false;
    }

    return true;

  }

}

/*******************************************************************************
 * Copyright (c) 2006, 2016 THALES GLOBAL SERVICES.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *  
 * Contributors:
 *    Thales - initial API and implementation
 *******************************************************************************/
package org.polarsys.capella.core.refinement.commands;

import org.eclipse.core.expressions.PropertyTester;
import org.polarsys.capella.common.data.modellingcore.ModelElement;
import org.polarsys.capella.common.ui.actions.ModelAdaptation;

public class RefinementCommandTester extends PropertyTester {
  /**
   * @see org.eclipse.core.expressions.IPropertyTester#test(java.lang.Object, java.lang.String, java.lang.Object[], java.lang.Object)
   */
  public boolean test(Object object, String propertyName, Object[] params, Object testedValue) {
    if (propertyName.equals("refinementMode") || propertyName.equals("graphicalRefinementMode")) { //$NON-NLS-1$ //$NON-NLS-2$
      ModelElement element = ModelAdaptation.adaptToCapella(object);
      
    }
    return false;
  }
}
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

import java.util.Collection;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.emf.ecore.EObject;
import org.polarsys.capella.common.flexibility.properties.schema.IProperty;
import org.polarsys.capella.common.flexibility.wizards.schema.IRendererContext;
import org.polarsys.capella.common.flexibility.wizards.ui.util.ExecutionEventUtil;
import org.polarsys.capella.common.re.constants.IReConstants;
import org.polarsys.capella.common.re.ui.subcommands.handlers.SubCommandHandler;
import org.polarsys.capella.core.re.project.ReProjectSkeletonFilter;
import org.polarsys.capella.core.re.project.diffmerge.SkeletonUtil;

public class AllNonTemplateElementsScopeHandler extends SubCommandHandler {

  @Override
  public Object execute(ExecutionEvent event) throws ExecutionException {

    IRendererContext context = ExecutionEventUtil.getRendererContext(event);
    IProperty property = context.getPropertyContext().getProperties().getProperty(IReConstants.PROPERTY__SCOPE);

    Collection<EObject> currentValue = (Collection<EObject>) context.getPropertyContext().getCurrentValue(property);

    if (currentValue.size() > 0) {

      EObject first = currentValue.iterator().next();

      currentValue.clear(); // make sure to remove possible template elements or other filtered elements
      currentValue.addAll(SkeletonUtil.getAllNonSkeletonElements(first, new ReProjectSkeletonFilter()));
      context.getPropertyContext().setCurrentValue(property, currentValue);
    }
    return null;
  }

}

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
package org.polarsys.capella.core.transition.common.handlers.resolver;

import java.util.List;

import org.eclipse.emf.ecore.EObject;

import org.polarsys.capella.core.transition.common.handlers.IHandler;
import org.polarsys.kitalpha.transposer.rules.handler.rules.api.IContext;

/**
 *
 */
public interface IResolverHandler extends IHandler {

  /**
   * Performs a selection of one or some elements from the given items
   * @param source the element which require the selection
   * @param items a list of choices
   * @param title a short description for the selection
   * @param message a detailed description for the selection
   * @param multipleSelection defines if the returned list should contain only one or many elements
   * @param transfo the current transfo
   * @return a list which can contains one or many elements according to the multiple_selection parameter
   */
  List<EObject> resolve(EObject source, List<EObject> items, final String title, final String message, boolean multipleSelection,
      IContext context, EObject[] contexts);

}

/*******************************************************************************
 * Copyright (c) 2006, 2014 THALES GLOBAL SERVICES.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *  
 * Contributors:
 *    Thales - initial API and implementation
 *******************************************************************************/
package org.polarsys.capella.common.re.ui.subcommands.handlers;

import java.util.Collection;
import java.util.HashSet;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.expressions.IEvaluationContext;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;
import org.polarsys.capella.common.flexibility.properties.schema.IProperty;
import org.polarsys.capella.common.flexibility.wizards.schema.IRenderer;
import org.polarsys.capella.common.flexibility.wizards.schema.IRendererContext;
import org.polarsys.capella.common.flexibility.wizards.ui.util.ExecutionEventUtil;
import org.polarsys.capella.common.re.CatalogElementLink;
import org.polarsys.capella.common.re.constants.IReConstants;
import org.polarsys.kitalpha.transposer.rules.handler.rules.api.IContext;

/**
 *
 */
public class DeleteHandler extends SubCommandHandler {

  /**
   * {@inheritDoc}
   */
  @Override
  public Object execute(ExecutionEvent event_p) throws ExecutionException {
    ISelection selection = HandlerUtil.getCurrentSelection(event_p);
    IRenderer renderer = ExecutionEventUtil.getRenderer(event_p);
    IRendererContext context = ExecutionEventUtil.getRendererContext(event_p);

    Collection<Object> selectiona =
        ((selection == null) || (selection.isEmpty())) ? context.getPropertyContext().getSourceAsList() : ((IStructuredSelection) selection).toList();

    IProperty property = context.getPropertyContext().getProperties().getProperty(IReConstants.PROPERTY__SCOPE);
    Collection<EObject> currentValue = (Collection<EObject>) context.getPropertyContext().getCurrentValue(property);
    delete(currentValue, selectiona);
    context.getPropertyContext().setCurrentValue(property, currentValue);

    return null;
  }

  /**
   * @param currentValue_p
   * @param selectiona_p
   */
  protected void delete(Collection<EObject> currentValue_p, Collection<Object> selectiona_p) {
    currentValue_p.removeAll(selectiona_p);
  }

  @Override
  public void setEnabled(Object evaluationContext_p) {
    Object variable = ((IEvaluationContext) evaluationContext_p).getDefaultVariable();

    if (!(variable instanceof Collection)) {
      setBaseEnabled(false);
    } else {
      Collection selection = (Collection) variable;
      if (selection.isEmpty()) {
        setBaseEnabled(false);
      } else {
        if (selection.iterator().next() instanceof CatalogElementLink) {

          setBaseEnabled(true);
          super.setEnabled(evaluationContext_p);
          return;

        }

        IRendererContext rendererContext = ExecutionEventUtil.getRendererContext((IEvaluationContext) evaluationContext_p);
        if (rendererContext == null) {

          setBaseEnabled(false);
        } else {

          IContext context = (IContext) rendererContext.getPropertyContext().getSource();
          Collection scopeElements =
              (Collection) rendererContext.getPropertyContext().getCurrentValue(
                  rendererContext.getPropertyContext().getProperties().getProperty(IReConstants.PROPERTY__SCOPE));

          Collection<Object> values = new HashSet<Object>(selection);
          if (values != null) {
            if (scopeElements != null) {
              values.removeAll(scopeElements);
            }
            setBaseEnabled(values.isEmpty());
          }
        }

      }
    }

    super.setEnabled(evaluationContext_p);
  }
}

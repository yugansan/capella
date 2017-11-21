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

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.ecore.EObject;
import org.polarsys.capella.common.ef.command.ICommand;
import org.polarsys.capella.common.re.ui.handlers.uihead.UIHeadHandler;
import org.polarsys.capella.core.re.commands.CreateRecCommand;
import org.polarsys.capella.core.re.commands.UpdateCurCommand;
import org.polarsys.capella.core.re.project.ReProjectSkeletonFilter;
import org.polarsys.capella.core.re.project.diffmerge.SkeletonUtil;
import org.polarsys.capella.core.transition.common.commands.CommandHandler;
import org.polarsys.capella.core.transition.common.commands.DefaultCommand;

public abstract class ProjectRecHandler extends CommandHandler {


  @Override
  protected ICommand createCommand(Collection<?> selection, IProgressMonitor progressMonitor) {
    EObject first = (EObject) selection.iterator().next();
    return createRecProjectCommand(SkeletonUtil.getAllNonSkeletonElements(first, new ReProjectSkeletonFilter()));
  }

  protected abstract DefaultCommand createRecProjectCommand(Collection<EObject> elementsForRec);

    public static class Create extends ProjectRecHandler {
      @Override
      protected DefaultCommand createRecProjectCommand(Collection<EObject> elementsForRec) {
        DefaultCommand c = new CreateRecCommand(elementsForRec, new NullProgressMonitor());
        c.addParameters(new UIHeadHandler(false));
        return c;
      }
    }

    public static class Update extends ProjectRecHandler {
      @Override
      protected DefaultCommand createRecProjectCommand(Collection<EObject> elementsForRec) {
        DefaultCommand c = new UpdateCurCommand(elementsForRec, new NullProgressMonitor());
        c.addParameters(new UIHeadHandler(true));
        return c;
      }
    }

}

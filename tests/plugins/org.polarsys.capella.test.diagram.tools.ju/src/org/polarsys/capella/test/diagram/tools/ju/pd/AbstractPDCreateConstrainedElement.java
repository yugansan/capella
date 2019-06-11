/*******************************************************************************
 * Copyright (c) 2019 THALES GLOBAL SERVICES.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *  
 * Contributors:
 *    Thales - initial API and implementation
 *******************************************************************************/
package org.polarsys.capella.test.diagram.tools.ju.pd;

import org.eclipse.sirius.business.api.session.Session;
import org.polarsys.capella.test.diagram.common.ju.api.AbstractDiagramTestCase;
import org.polarsys.capella.test.diagram.common.ju.context.PDDiagram;
import org.polarsys.capella.test.framework.context.SessionContext;
import org.polarsys.capella.test.framework.model.GenericModel;

public class AbstractPDCreateConstrainedElement extends AbstractDiagramTestCase {

  protected String diagramName;

  protected String dataPkgID;
  protected String interfacePkgID;

  @Override
  protected String getRequiredTestModel() {
    return "DiagramToolsModel";
  }

  @Override
  public void test() throws Exception {

    Session session = getSession(getRequiredTestModel());
    SessionContext context = new SessionContext(session);

    PDDiagram pd = PDDiagram.openDiagram(context, diagramName);

    pd.insertDataPackages(pd.getDiagramId(), dataPkgID);
    pd.insertInterfacePackages(pd.getDiagramId(), interfacePkgID);

    pd.createConstraint(GenericModel.CONSTRAINT_1);

    pd.createConstrainedElement(GenericModel.CONSTRAINT_1, dataPkgID);
    pd.createConstrainedElement(GenericModel.CONSTRAINT_1, interfacePkgID);
  }
}
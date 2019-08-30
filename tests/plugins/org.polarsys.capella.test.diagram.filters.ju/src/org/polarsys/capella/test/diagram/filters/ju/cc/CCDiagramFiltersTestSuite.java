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
package org.polarsys.capella.test.diagram.filters.ju.cc;

import java.util.ArrayList;
import java.util.List;

import org.polarsys.capella.test.framework.api.BasicTestArtefact;
import org.polarsys.capella.test.framework.api.BasicTestSuite;

public class CCDiagramFiltersTestSuite extends BasicTestSuite {

  @Override
  protected List<BasicTestArtefact> getTests() {
    List<BasicTestArtefact> tests = new ArrayList<>();
    tests.add(new HideActorGeneralizationsForCC());
    tests.add(new HideActorInvolvementsForCC());
    tests.add(new HideActorsForCC());
    tests.add(new HideCapabilitiesForCC());
    tests.add(new HideCapabilityExploitationsForCC());
    tests.add(new HideCapabilityExtendsForCC());
    tests.add(new HideCapabilityIncludesForCC());
    tests.add(new HideMissionsForCC());
    tests.add(new HidePropertyValuesForCC());
    return tests;
  }

}
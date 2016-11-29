/*******************************************************************************
 * Copyright (c) 2006, 2015 THALES GLOBAL SERVICES.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *  
 * Contributors:
 *    Thales - initial API and implementation
 *******************************************************************************/
package org.polarsys.capella.test.libraries.ju.testcases.basic;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.polarsys.capella.common.libraries.IModel;
import org.polarsys.capella.common.libraries.manager.LibraryManagerExt;
import org.polarsys.capella.core.libraries.model.CapellaModel;
import org.polarsys.capella.test.framework.api.BasicTestCase;
import org.polarsys.capella.test.framework.helpers.SessionHelper;

/**
 * @author Erwan Brottier
 */
public class LibraryManager_removeReferenceToLibrary_cacheFocus extends BasicTestCase {

  @SuppressWarnings("nls")
  @Override
  public List<String> getRequiredTestModels() {
    return Arrays.asList("libraries/MyProject1", "libraries/MyLibrary1", "libraries/MyLibrary2", "libraries/MyLibrary3");
  }

  @SuppressWarnings("nls")
  @Override
  public void test() {
    // -- SCENARIO -- //
    CapellaModel monProjet1 = getTestModel("libraries/MyProject1");
    CapellaModel maLibrairie1 = (CapellaModel) getTestModel("libraries/MyLibrary1");
    CapellaModel maLibrairie2 = (CapellaModel) getTestModel("libraries/MyLibrary2");
    CapellaModel maLibrairie3 = (CapellaModel) getTestModel("libraries/MyLibrary3");
    // -- ORACLE -- //
    monProjet1.addReference(maLibrairie1);
    SessionHelper.saveSession(monProjet1);
    maLibrairie1.addReference(maLibrairie2);
    SessionHelper.saveSession(maLibrairie1);
    maLibrairie2.addReference(maLibrairie3);
    SessionHelper.saveSession(maLibrairie2);
    Collection<IModel> libs = LibraryManagerExt.getAllReferences(monProjet1);
    assertTrue(libs.size() == 3);
    assertTrue(libs.contains(maLibrairie1));
    assertTrue(libs.contains(maLibrairie2));
    assertTrue(libs.contains(maLibrairie3));
    libs = LibraryManagerExt.getAllReferences(maLibrairie1);
    assertTrue(libs.size() == 2);
    assertTrue(libs.contains(maLibrairie2));
    assertTrue(libs.contains(maLibrairie3));
    libs = LibraryManagerExt.getAllReferences(maLibrairie2);
    assertTrue(libs.size() == 1);
    assertTrue(libs.contains(maLibrairie3));
    libs = LibraryManagerExt.getAllReferences(maLibrairie3);
    assertTrue(libs.size() == 0);
    maLibrairie1.removeReference(maLibrairie2);
    SessionHelper.saveSession(maLibrairie1);
    libs = LibraryManagerExt.getAllReferences(monProjet1);
    assertTrue(libs.size() == 1);
    assertTrue(libs.contains(maLibrairie1));
    libs = LibraryManagerExt.getAllReferences(maLibrairie1);
    assertTrue(libs.size() == 0);
    libs = LibraryManagerExt.getAllReferences(maLibrairie2);
    assertTrue(libs.size() == 1);
    assertTrue(libs.contains(maLibrairie3));
    libs = LibraryManagerExt.getAllReferences(maLibrairie3);
    assertTrue(libs.size() == 0);
  }
}
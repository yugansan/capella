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
package org.polarsys.capella.test.ui.search;

import static org.junit.Assert.assertNotEquals;

import org.eclipse.core.runtime.IStatus;
import org.polarsys.capella.core.ui.search.CapellaSearchField;
import org.polarsys.capella.core.ui.search.CapellaSearchSettings;
import org.polarsys.capella.core.ui.search.Messages;
import org.polarsys.capella.test.framework.api.BasicTestCase;

public class CapellaSearchSettingsTest extends BasicTestCase {

  public void testValidate() throws Exception {
    CapellaSearchSettings capellaSearchSettings = new CapellaSearchSettings();

    IStatus validateStatus = capellaSearchSettings.validate();
    assertEquals(IStatus.ERROR, validateStatus.getSeverity());
    assertEquals(Messages.CapellaSearchPage_Validation_Message_Project_Selection, validateStatus.getMessage());

    capellaSearchSettings.addProject("Toto");
    validateStatus = capellaSearchSettings.validate();
    assertEquals(IStatus.ERROR, validateStatus.getSeverity());
    assertEquals(Messages.CapellaSearchPage_Validation_Message_SearchField_Selection, validateStatus.getMessage());

    capellaSearchSettings.addSearchField(CapellaSearchField.DESCRIPTION);
    validateStatus = capellaSearchSettings.validate();
    assertEquals(IStatus.ERROR, validateStatus.getSeverity());
    assertEquals(Messages.CapellaSearchPage_Validation_Message_Pattern_Empty, validateStatus.getMessage());

    capellaSearchSettings.setTextPattern("(");
    validateStatus = capellaSearchSettings.validate();
    assertEquals(IStatus.OK, validateStatus.getSeverity());

    capellaSearchSettings.setRegExSearch(true);
    capellaSearchSettings.setTextPattern("(");
    validateStatus = capellaSearchSettings.validate();
    assertEquals(IStatus.ERROR, validateStatus.getSeverity());

    capellaSearchSettings.setRegExSearch(true);
    capellaSearchSettings.setTextPattern("(a)");
    validateStatus = capellaSearchSettings.validate();
    assertEquals(IStatus.OK, validateStatus.getSeverity());
  }

  public void testEqualsHashCode() throws Exception {
    CapellaSearchSettings c1 = new CapellaSearchSettings();
    CapellaSearchSettings c2 = new CapellaSearchSettings();
    CapellaSearchSettings c3 = new CapellaSearchSettings();
    assertFalse(c1.equals(null));
    assertTrue(c1.equals(c1));
    assertTrue(c1.equals(c2));
    assertEquals(c1.hashCode(), c2.hashCode());

    c1.setTextPattern("aa");
    c1.setRegExSearch(true);
    c1.addProject("bobo");
    c1.addProject("toto");
    c1.addSearchField(CapellaSearchField.NAME);
    c1.addSearchField(CapellaSearchField.DESCRIPTION);

    c2.setTextPattern("aa");
    c2.setRegExSearch(true);
    c2.addProject("bobo");
    c2.addProject("toto");
    c2.addSearchField(CapellaSearchField.NAME);
    c2.addSearchField(CapellaSearchField.DESCRIPTION);

    c3.setTextPattern("aa");
    c3.setRegExSearch(true);
    c3.addProject("toto");
    c3.addProject("bobo");
    c3.addSearchField(CapellaSearchField.DESCRIPTION);
    c3.addSearchField(CapellaSearchField.NAME);

    assertTrue(c1.equals(c2));
    assertTrue(c2.equals(c1));
    assertTrue(c2.equals(c3));
    assertTrue(c1.equals(c3));

    assertEquals(c1.hashCode(), c2.hashCode());
    assertEquals(c2.hashCode(), c3.hashCode());

    c2.addProject("yolo");
    assertFalse(c1.equals(c2));
    assertNotEquals(c1.hashCode(), c2.hashCode());

    c3.addSearchField(CapellaSearchField.SUMMARY);
    assertFalse(c1.equals(c3));
    assertNotEquals(c1.hashCode(), c3.hashCode());
  }

  @Override
  public void test() throws Exception {
    testValidate();
    testEqualsHashCode();
  }
}

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

import java.util.Arrays;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.sirius.business.api.dialect.DialectManager;
import org.eclipse.sirius.business.api.session.Session;
import org.eclipse.sirius.viewpoint.DRepresentationDescriptor;
import org.polarsys.capella.common.helpers.TransactionHelper;
import org.polarsys.capella.core.data.oa.OperationalActivity;
import org.polarsys.capella.core.libraries.model.ICapellaModel;
import org.polarsys.capella.core.libraries.utils.ScopeModelWrapper;
import org.polarsys.capella.core.ui.search.CapellaSearchField;
import org.polarsys.capella.core.ui.search.CapellaSearchQuery;
import org.polarsys.capella.core.ui.search.CapellaSearchSettings;
import org.polarsys.capella.shared.id.handler.IScope;
import org.polarsys.capella.shared.id.handler.IdManager;
import org.polarsys.capella.test.framework.api.BasicTestCase;

public class CapellaSearchTest extends BasicTestCase {

  public static String PROJECT_NAME = "In-Flight Entertainment System"; //$NON-NLS-1$

  public static final String DIAGRAM_ID = "_yYiSlXjqEea__MYrXGSERA"; //$NON-NLS-1$
  public static final String CAPELLA_ELEMENT_ID = "01a4afcf-b2ef-4562-aa09-6f6fda9f0b93"; //$NON-NLS-1$

  DRepresentationDescriptor diagram;
  OperationalActivity oa;
  IProject project;

  @Override
  public List<String> getRequiredTestModels() {
    return Arrays.asList(PROJECT_NAME);
  }

  @Override
  protected void setUp() throws Exception {
    super.setUp();

    ICapellaModel model = getTestModel(PROJECT_NAME);
    IScope scope = new ScopeModelWrapper(model);

    Session session = getSessionForTestModel(getRequiredTestModels().get(0));

    diagram = DialectManager.INSTANCE.getAllRepresentationDescriptors(session) //
        .stream() //
        .filter(d -> DIAGRAM_ID.equals(d.getUid())) //
        .findAny() //
        .orElse(null);

    assertNotNull(diagram);

    oa = (OperationalActivity) IdManager.getInstance().getEObject(CAPELLA_ELEMENT_ID, scope);

    assertNotNull(oa);

    project = ResourcesPlugin.getWorkspace().getRoot().getProject(PROJECT_NAME);

    assertNotNull(project);
  }

  @Override
  public void test() throws Exception {
    String lorem = "Lorem";
    String startWithIpsum = "^Ipsum";
    String startWithLorem = "^Lorem";
    String invalidRegex = "[";

    CapellaSearchSettings capellaSearchSettings = new CapellaSearchSettings();
    capellaSearchSettings.setCaseSensitive(false);
    capellaSearchSettings.setRegExSearch(true);
    CapellaSearchQuery searchQuery = new CapellaSearchQuery(capellaSearchSettings);

    capellaSearchSettings.setTextPattern(null);
    assertEquals("The search failed because no search text is set", IStatus.ERROR,
        searchQuery.run(new NullProgressMonitor()).getSeverity());

    capellaSearchSettings.setTextPattern(invalidRegex);
    assertEquals(String.format("The search failed because '%s' is invalid regular expression", invalidRegex),
        IStatus.ERROR, searchQuery.run(new NullProgressMonitor()).getSeverity());

    capellaSearchSettings.setTextPattern("");
    runSearchQuery(searchQuery);
    assertEquals("No match because no search project, no search field and search text is empty", 0,
        searchQuery.getSearchResult().getDisplayedOccurrenceCount());

    capellaSearchSettings.setTextPattern("a");
    runSearchQuery(searchQuery);
    assertEquals("No match because no search project and no search scope is set", 0,
        searchQuery.getSearchResult().getDisplayedOccurrenceCount());

    capellaSearchSettings.addProject(PROJECT_NAME);
    runSearchQuery(searchQuery);
    assertEquals("No match because no search field", 0, searchQuery.getSearchResult().getDisplayedOccurrenceCount());

    // === Search for elements containing "Lorem" ===
    capellaSearchSettings.setTextPattern(lorem);
    capellaSearchSettings.addSearchField(CapellaSearchField.NAME);
    capellaSearchSettings.addSearchField(CapellaSearchField.DESCRIPTION);
    capellaSearchSettings.addSearchField(CapellaSearchField.SUMMARY);

    runSearchQuery(searchQuery);
    assertEquals(String.format("No match because no element containing '%s'", lorem), 0,
        searchQuery.getSearchResult().getDisplayedOccurrenceCount());

    setTextValueForAttribute(oa, CapellaSearchField.NAME.getEAttribute(oa), "Lorem Ipsum Ipsum Lorem");
    runSearchQuery(searchQuery);
    assertEquals(String.format("There should be 2 not-filtered occurrences matching '%s'", lorem), 2,
        searchQuery.getSearchResult().getDisplayedOccurrenceCount());
    assertEquals(String.format("There should be 2 not-filtered occurrences matching '%s' for '%s'", lorem, oa), 2,
        searchQuery.getSearchResult().getDisplayedOccurrenceCount(oa));
    assertEquals(String.format("There should be 0 not-filtered occurrences matching '%s' for '%s'", lorem, diagram), 0,
        searchQuery.getSearchResult().getDisplayedOccurrenceCount(diagram));
    assertEquals(String.format("There should be 2 not-filtered occurrences matching '%s' for '%s'", lorem, project), 2,
        searchQuery.getSearchResult().getDisplayedOccurrenceCount(project));

    assertEquals(String.format("There should be 1 not-filtered matches matching '%s'", lorem), 1,
        searchQuery.getSearchResult().getDisplayedMatches().size());
    assertEquals(String.format("There should be 1 not-filtered matches matching '%s' for '%s'", lorem, oa), 1,
        searchQuery.getSearchResult().getDisplayedMatches(oa).size());
    assertEquals(String.format("There should be 0 not-filtered matches matching '%s' for '%s'", lorem, diagram), 0,
        searchQuery.getSearchResult().getDisplayedMatches(diagram).size());
    assertEquals(String.format("There should be 1 not-filtered matches matching '%s' for '%s'", lorem, project), 1,
        searchQuery.getSearchResult().getDisplayedMatches(project).size());

    assertEquals(
        String.format("The match of '%s' that matching '%s' must be on search field '%s'", oa, lorem,
            CapellaSearchField.NAME),
        CapellaSearchField.NAME,
        searchQuery.getSearchResult().getDisplayedMatches(oa).iterator().next().getSearchField());

    // === Search for elements starting with "Lorem" ===
    capellaSearchSettings.setTextPattern(startWithLorem);
    runSearchQuery(searchQuery);
    assertEquals(String.format("There should be 1 not-filtered occurrences matching '%s'", startWithLorem), 1,
        searchQuery.getSearchResult().getDisplayedOccurrenceCount());
    assertEquals(String.format("There should be 1 not-filtered occurrences matching '%s' for '%s'", startWithLorem, oa),
        1, searchQuery.getSearchResult().getDisplayedOccurrenceCount(oa));
    assertEquals(
        String.format("There should be 0 not-filtered occurrences matching '%s' for '%s'", startWithLorem, diagram), 0,
        searchQuery.getSearchResult().getDisplayedOccurrenceCount(diagram));
    assertEquals(
        String.format("There should be 1 not-filtered occurrences matching '%s' for '%s'", startWithLorem, project), 1,
        searchQuery.getSearchResult().getDisplayedOccurrenceCount(project));

    // === Search for elements starting with "Ipsum" ===
    capellaSearchSettings.setTextPattern(startWithIpsum);
    runSearchQuery(searchQuery);
    assertEquals(String.format("There should be 0 not-filtered occurrences matching '%s'", startWithIpsum), 0,
        searchQuery.getSearchResult().getDisplayedOccurrenceCount());
    assertEquals(String.format("There should be 0 not-filtered occurrences matching '%s' for '%s'", startWithIpsum, oa),
        0, searchQuery.getSearchResult().getDisplayedOccurrenceCount(oa));
    assertEquals(
        String.format("There should be 0 not-filtered occurrences matching '%s' for '%s'", startWithLorem, diagram), 0,
        searchQuery.getSearchResult().getDisplayedOccurrenceCount(diagram));
    assertEquals(
        String.format("There should be 0 not-filtered occurrences matching '%s' for '%s'", startWithIpsum, project), 0,
        searchQuery.getSearchResult().getDisplayedOccurrenceCount(project));

    // === Search for elements starting with "Ipsum" ===

    setTextValueForAttribute(diagram, CapellaSearchField.DESCRIPTION.getEAttribute(diagram),
        "Lorem Ipsum Ipsum Lorem" + System.lineSeparator() + "Lorem Ipsum Ipsum Lorem"); // 2 lines

    capellaSearchSettings.setTextPattern(lorem);
    runSearchQuery(searchQuery);

    assertEquals(String.format("There should be 6 not-filtered occurrences matching '%s'", lorem), 6,
        searchQuery.getSearchResult().getDisplayedOccurrenceCount());
    assertEquals(String.format("There should be 2 not-filtered occurrences matching '%s' for '%s'", lorem, oa), 2,
        searchQuery.getSearchResult().getDisplayedOccurrenceCount(oa));
    assertEquals(String.format("There should be 4 not-filtered occurrences matching '%s' for '%s'", lorem, diagram), 4,
        searchQuery.getSearchResult().getDisplayedOccurrenceCount(diagram));
    assertEquals(String.format("There should be 6 not-filtered occurrences matching '%s' for '%s'", lorem, project), 6,
        searchQuery.getSearchResult().getDisplayedOccurrenceCount(project));

    assertEquals(String.format("There should be 3 not-filtered matches matching '%s'", lorem), 3,
        searchQuery.getSearchResult().getDisplayedMatches().size());
    assertEquals(String.format("There should be 1 not-filtered matches matching '%s' for '%s'", lorem, oa), 1,
        searchQuery.getSearchResult().getDisplayedMatches(oa).size());
    assertEquals(String.format("There should be 2 not-filtered matches matching '%s' for '%s'", lorem, diagram), 2,
        searchQuery.getSearchResult().getDisplayedMatches(diagram).size());
    assertEquals(String.format("There should be 3 not-filtered matches matching '%s' for '%s'", lorem, project), 3,
        searchQuery.getSearchResult().getDisplayedMatches(project).size());

    assertEquals(
        String.format("The match of '%s' that matching '%s' must be on search field '%s'", oa, lorem,
            CapellaSearchField.NAME),
        CapellaSearchField.NAME,
        searchQuery.getSearchResult().getDisplayedMatches(oa).iterator().next().getSearchField());

    assertEquals(
        String.format("The of '%s' that matching '%s' must be on search field '%s'", diagram, lorem,
            CapellaSearchField.DESCRIPTION),
        CapellaSearchField.DESCRIPTION,
        searchQuery.getSearchResult().getDisplayedMatches(diagram).iterator().next().getSearchField());

  }

  private void runSearchQuery(CapellaSearchQuery searchQuery) {
    IStatus status = searchQuery.run(new NullProgressMonitor());
    assertTrue(status.isOK());
  }

  private void setTextValueForAttribute(EObject element, EAttribute attribute, String value) {
    TransactionalEditingDomain domain = TransactionHelper.getEditingDomain(element);
    Command setCommand = SetCommand.create(domain, element, attribute, value);
    domain.getCommandStack().execute(setCommand);
    assertEquals(value, element.eGet(attribute));
  }
}

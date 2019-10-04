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
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
import org.polarsys.capella.core.ui.search.CapellaReplaceQuery;
import org.polarsys.capella.core.ui.search.CapellaSearchField;
import org.polarsys.capella.core.ui.search.CapellaSearchMatch;
import org.polarsys.capella.core.ui.search.CapellaSearchQuery;
import org.polarsys.capella.core.ui.search.CapellaSearchSettings;
import org.polarsys.capella.shared.id.handler.IScope;
import org.polarsys.capella.shared.id.handler.IdManager;
import org.polarsys.capella.test.framework.api.BasicTestCase;

public class CapellaReplaceTest extends BasicTestCase {

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
    setTextValueForAttribute(oa, CapellaSearchField.NAME.getEAttribute(oa), "a ipsum A ipsum");
    setTextValueForAttribute(oa, CapellaSearchField.SUMMARY.getEAttribute(oa), "a ipsum A ipsum");
    setTextValueForAttribute(oa, CapellaSearchField.DESCRIPTION.getEAttribute(oa), "a ipsum A ipsum");

    setTextValueForAttribute(diagram, CapellaSearchField.NAME.getEAttribute(diagram), "A ipsum a ipsum");
    setTextValueForAttribute(diagram, CapellaSearchField.DESCRIPTION.getEAttribute(diagram), "A ipsum a ipsum");

    String a = "a";
    String A = "A";
    String replacement = "lorem";

    CapellaSearchSettings capellaSearchSettings = new CapellaSearchSettings();
    // Test regex false here
    capellaSearchSettings.setRegExSearch(false);
    capellaSearchSettings.setCaseSensitive(true);
    capellaSearchSettings.addSearchField(CapellaSearchField.NAME);
    capellaSearchSettings.addSearchField(CapellaSearchField.SUMMARY);
    capellaSearchSettings.addSearchField(CapellaSearchField.DESCRIPTION);
    capellaSearchSettings.addProject(PROJECT_NAME);

    CapellaSearchQuery searchQuery = new CapellaSearchQuery(capellaSearchSettings);
    CapellaReplaceQuery replaceQuery = new CapellaReplaceQuery(capellaSearchSettings);

    // === BEFORE REPLACEMENT ===
    capellaSearchSettings.setTextPattern(a);
    runSearchQuery(searchQuery);
    assertTrue(String.format("Number of occurrences matching '%s' before replacement must be greater than 0", a),
        searchQuery.getSearchResult().getDisplayedOccurrenceCount() > 0);

    capellaSearchSettings.setTextPattern(A);
    runSearchQuery(searchQuery);
    assertTrue(String.format("Number of occurrences matching '%s' before replacement must be greater than 0", A),
        searchQuery.getSearchResult().getDisplayedOccurrenceCount() > 0);

    // === REPLACEMENT NULL ===
    assertEquals("The replace query failed because the replacement is null", IStatus.ERROR, replaceQuery
        .run(new NullProgressMonitor(), searchQuery.getSearchResult().getDisplayedMatches(), null).getSeverity());

    // === REPLACEMENT a ===
    capellaSearchSettings.setTextPattern(a);
    runSearchQuery(searchQuery);
    runReplaceQuery(replaceQuery, searchQuery.getSearchResult().getDisplayedMatches(), replacement);
    runSearchQuery(searchQuery);
    assertEquals(String.format("Number of occurrences matching '%s' after replacement must be 0", a), 0,
        searchQuery.getSearchResult().getDisplayedOccurrenceCount());

    assertEquals("lorem ipsum A ipsum", oa.eGet(CapellaSearchField.NAME.getEAttribute(oa)));
    assertEquals("lorem ipsum A ipsum", oa.eGet(CapellaSearchField.SUMMARY.getEAttribute(oa)));
    assertEquals("lorem ipsum A ipsum", oa.eGet(CapellaSearchField.DESCRIPTION.getEAttribute(oa)));

    assertEquals("A ipsum lorem ipsum", diagram.eGet(CapellaSearchField.NAME.getEAttribute(diagram)));
    assertEquals("A ipsum lorem ipsum", diagram.eGet(CapellaSearchField.DESCRIPTION.getEAttribute(diagram)));

    // === REPLACEMENT A ===
    capellaSearchSettings.setTextPattern(A);
    runSearchQuery(searchQuery);
    runReplaceQuery(replaceQuery, searchQuery.getSearchResult().getDisplayedMatches(), replacement);
    runSearchQuery(searchQuery);
    assertEquals(String.format("Number of occurrences matching '%s' after replacement must be 0", A), 0,
        searchQuery.getSearchResult().getDisplayedOccurrenceCount());

    assertEquals("lorem ipsum lorem ipsum", oa.eGet(CapellaSearchField.NAME.getEAttribute(oa)));
    assertEquals("lorem ipsum lorem ipsum", oa.eGet(CapellaSearchField.SUMMARY.getEAttribute(oa)));
    assertEquals("lorem ipsum lorem ipsum", oa.eGet(CapellaSearchField.DESCRIPTION.getEAttribute(oa)));

    assertEquals("lorem ipsum lorem ipsum", diagram.eGet(CapellaSearchField.NAME.getEAttribute(diagram)));
    assertEquals("lorem ipsum lorem ipsum", diagram.eGet(CapellaSearchField.DESCRIPTION.getEAttribute(diagram)));

    // === Replace only selected ===
    setTextValueForAttribute(diagram, CapellaSearchField.DESCRIPTION.getEAttribute(diagram),
        "ipsum toto 1" + System.lineSeparator() + "ipsum toto 2");
    capellaSearchSettings.setTextPattern("toto");
    runSearchQuery(searchQuery);

    Set<CapellaSearchMatch> matchesOnFirstLine = Stream.of(searchQuery.getSearchResult().getMatches(diagram))
        .filter(m -> {
          return m instanceof CapellaSearchMatch //
              && diagram.equals(m.getElement()) //
              && "ipsum toto 1".equals(((CapellaSearchMatch) m).getLineContent()) //
              && ((CapellaSearchMatch) m).getLineNumber() == 1; //
        }).map(CapellaSearchMatch.class::cast).collect(Collectors.toSet());

    assertEquals(1, matchesOnFirstLine.size());
    runReplaceQuery(replaceQuery, matchesOnFirstLine, replacement);

    assertEquals("Only the 'toto' in the first line is replaced by 'lorem'",
        "ipsum lorem 1" + System.lineSeparator() + "ipsum toto 2",
        diagram.eGet(CapellaSearchField.DESCRIPTION.getEAttribute(diagram)));
  }

  private void runSearchQuery(CapellaSearchQuery searchQuery) {
    IStatus status = searchQuery.run(new NullProgressMonitor());
    assertTrue(status.isOK());
  }

  private void runReplaceQuery(CapellaReplaceQuery replaceQuery, Set<CapellaSearchMatch> allMatches,
      String replacement) {
    IStatus status = replaceQuery.run(new NullProgressMonitor(), allMatches, replacement);
    assertTrue(status.isOK());
  }

  private void setTextValueForAttribute(EObject element, EAttribute attribute, String value) {
    TransactionalEditingDomain domain = TransactionHelper.getEditingDomain(element);
    Command setCommand = SetCommand.create(domain, element, attribute, value);
    domain.getCommandStack().execute(setCommand);
    assertEquals(value, element.eGet(attribute));
  }
}

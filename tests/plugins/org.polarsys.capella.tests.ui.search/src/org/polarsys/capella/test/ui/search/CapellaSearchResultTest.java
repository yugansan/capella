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
import java.util.HashSet;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.sirius.business.api.dialect.DialectManager;
import org.eclipse.sirius.business.api.session.Session;
import org.eclipse.sirius.viewpoint.DRepresentationDescriptor;
import org.polarsys.capella.core.data.oa.OperationalActivity;
import org.polarsys.capella.core.libraries.model.ICapellaModel;
import org.polarsys.capella.core.libraries.utils.ScopeModelWrapper;
import org.polarsys.capella.core.ui.search.CapellaSearchField;
import org.polarsys.capella.core.ui.search.CapellaSearchMatch;
import org.polarsys.capella.core.ui.search.CapellaSearchMatchFilter;
import org.polarsys.capella.core.ui.search.CapellaSearchMatchOccurrence;
import org.polarsys.capella.core.ui.search.CapellaSearchResult;
import org.polarsys.capella.shared.id.handler.IScope;
import org.polarsys.capella.shared.id.handler.IdManager;
import org.polarsys.capella.test.framework.api.BasicTestCase;

public class CapellaSearchResultTest extends BasicTestCase {

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
    CapellaSearchResult capellaSearchResult = new CapellaSearchResult(null);

    CapellaSearchMatch matchOAName = new CapellaSearchMatch(oa, project, 1, "Lorem ipsum name", "Lorem ipsum name",
        CapellaSearchField.NAME);
    matchOAName.addMatchOccurrence(new CapellaSearchMatchOccurrence(1, 2));
    CapellaSearchMatch matchOaSummary = new CapellaSearchMatch(oa, project, 1, "Lorem ipsum summary",
        "Lorem ipsum summary", CapellaSearchField.SUMMARY);
    matchOaSummary.addMatchOccurrence(new CapellaSearchMatchOccurrence(1, 2));
    CapellaSearchMatch matchOADesc = new CapellaSearchMatch(oa, project, 2, "Lorem ipsum decription 1",
        "Lorem ipsum decription 1\nLorem ipsum decription 2", CapellaSearchField.DESCRIPTION);
    matchOADesc.addMatchOccurrence(new CapellaSearchMatchOccurrence(1, 2));
    matchOADesc.addMatchOccurrence(new CapellaSearchMatchOccurrence(3, 4));

    CapellaSearchMatch matchDiaName = new CapellaSearchMatch(diagram, project, 1, "Lorem ipsum name",
        "Lorem ipsum name", CapellaSearchField.NAME);
    matchDiaName.addMatchOccurrence(new CapellaSearchMatchOccurrence(1, 2));
    CapellaSearchMatch matchDiaDesc = new CapellaSearchMatch(diagram, project, 2, "Lorem ipsum decription 1",
        "Lorem ipsum decription 1\nLorem ipsum decription 2", CapellaSearchField.DESCRIPTION);
    matchDiaDesc.addMatchOccurrence(new CapellaSearchMatchOccurrence(1, 2));
    matchDiaDesc.addMatchOccurrence(new CapellaSearchMatchOccurrence(3, 4));

    capellaSearchResult.addMatch(matchOAName);
    capellaSearchResult.addMatch(matchOaSummary);
    capellaSearchResult.addMatch(matchOADesc);

    capellaSearchResult.addMatch(matchDiaName);
    capellaSearchResult.addMatch(matchDiaDesc);

    assertEquals(
        new HashSet<>(Arrays.asList(CapellaSearchMatchFilter.CAPELLA_ELEMENT, CapellaSearchMatchFilter.NOT_MODIFIABLE,
            CapellaSearchMatchFilter.REPRESENTATION)),
        new HashSet<>(Arrays.asList(capellaSearchResult.getAllMatchFilters())));
    assertEquals(0, capellaSearchResult.getActiveMatchFilters().length);

    assertEquals(new HashSet<>(Arrays.asList(project)), capellaSearchResult.getProjects());

    assertEquals(new HashSet<>(Arrays.asList(oa, diagram)),
        new HashSet<>(Arrays.asList(capellaSearchResult.getElements())));
    assertEquals(new HashSet<>(Arrays.asList(oa, diagram)), capellaSearchResult.getElements(project));
    assertEquals(new HashSet<>(Arrays.asList(oa, diagram)), capellaSearchResult.getDisplayedElements(project));

    assertEquals(5, capellaSearchResult.getMatchCount());
    assertEquals(5, capellaSearchResult.getMatchCount(project));
    assertEquals(3, capellaSearchResult.getMatchCount(oa));
    assertEquals(2, capellaSearchResult.getMatchCount(diagram));

    assertEquals(new HashSet<>(Arrays.asList(matchOAName, matchOaSummary, matchOADesc, matchDiaName, matchDiaDesc)),
        capellaSearchResult.getMatches());
    assertEquals(new HashSet<>(Arrays.asList(matchOAName, matchOaSummary, matchOADesc, matchDiaName, matchDiaDesc)),
        capellaSearchResult.getMatches(project));
    assertEquals(new HashSet<>(Arrays.asList(matchOAName, matchOaSummary, matchOADesc)),
        new HashSet<>(Arrays.asList(capellaSearchResult.getMatches(oa))));
    assertEquals(new HashSet<>(Arrays.asList(matchDiaName, matchDiaDesc)),
        new HashSet<>(Arrays.asList(capellaSearchResult.getMatches(diagram))));

    assertEquals(new HashSet<>(Arrays.asList(matchOAName, matchOaSummary, matchOADesc, matchDiaName, matchDiaDesc)),
        capellaSearchResult.getDisplayedMatches());
    assertEquals(new HashSet<>(Arrays.asList(matchOAName, matchOaSummary, matchOADesc, matchDiaName, matchDiaDesc)),
        capellaSearchResult.getDisplayedMatches(project));
    assertEquals(new HashSet<>(Arrays.asList(matchOAName, matchOaSummary, matchOADesc)),
        capellaSearchResult.getDisplayedMatches(oa));
    assertEquals(new HashSet<>(Arrays.asList(matchDiaName, matchDiaDesc)),
        capellaSearchResult.getDisplayedMatches(diagram));

    assertEquals(7, capellaSearchResult.getOccurrenceCount());
    assertEquals(7, capellaSearchResult.getOccurrenceCount(project));
    assertEquals(4, capellaSearchResult.getOccurrenceCount(oa));
    assertEquals(3, capellaSearchResult.getOccurrenceCount(diagram));

    assertEquals(7, capellaSearchResult.getDisplayedOccurrenceCount());
    assertEquals(7, capellaSearchResult.getDisplayedOccurrenceCount(project));
    assertEquals(4, capellaSearchResult.getDisplayedOccurrenceCount(oa));
    assertEquals(3, capellaSearchResult.getDisplayedOccurrenceCount(diagram));

    // Activate capella element filter
    capellaSearchResult
        .setActiveMatchFilters(new CapellaSearchMatchFilter[] { CapellaSearchMatchFilter.CAPELLA_ELEMENT });

    assertEquals(new HashSet<>(Arrays.asList(CapellaSearchMatchFilter.CAPELLA_ELEMENT)),
        new HashSet<>(Arrays.asList(capellaSearchResult.getActiveMatchFilters())));

    assertEquals(new HashSet<>(Arrays.asList(diagram)), capellaSearchResult.getDisplayedElements(project));

    assertEquals(3, capellaSearchResult.getDisplayedOccurrenceCount());
    assertEquals(3, capellaSearchResult.getDisplayedOccurrenceCount(project));
    assertEquals(0, capellaSearchResult.getDisplayedOccurrenceCount(oa));
    assertEquals(3, capellaSearchResult.getDisplayedOccurrenceCount(diagram));

    assertEquals(new HashSet<>(Arrays.asList(matchDiaName, matchDiaDesc)), capellaSearchResult.getDisplayedMatches());
    assertEquals(new HashSet<>(Arrays.asList(matchDiaName, matchDiaDesc)),
        capellaSearchResult.getDisplayedMatches(project));
    assertTrue(capellaSearchResult.getDisplayedMatches(oa).isEmpty());

    assertEquals(new HashSet<>(Arrays.asList(matchDiaName, matchDiaDesc)),
        capellaSearchResult.getDisplayedMatches(diagram));

    // Activate capella representation filter
    capellaSearchResult
        .setActiveMatchFilters(new CapellaSearchMatchFilter[] { CapellaSearchMatchFilter.REPRESENTATION });

    assertEquals(new HashSet<>(Arrays.asList(CapellaSearchMatchFilter.REPRESENTATION)),
        new HashSet<>(Arrays.asList(capellaSearchResult.getActiveMatchFilters())));

    assertEquals(new HashSet<>(Arrays.asList(oa)), capellaSearchResult.getDisplayedElements(project));

    assertEquals(4, capellaSearchResult.getDisplayedOccurrenceCount());
    assertEquals(4, capellaSearchResult.getDisplayedOccurrenceCount(project));
    assertEquals(4, capellaSearchResult.getDisplayedOccurrenceCount(oa));
    assertEquals(0, capellaSearchResult.getDisplayedOccurrenceCount(diagram));

    assertEquals(new HashSet<>(Arrays.asList(matchOAName, matchOaSummary, matchOADesc)),
        capellaSearchResult.getDisplayedMatches());
    assertEquals(new HashSet<>(Arrays.asList(matchOAName, matchOaSummary, matchOADesc)),
        capellaSearchResult.getDisplayedMatches(project));
    assertEquals(new HashSet<>(Arrays.asList(matchOAName, matchOaSummary, matchOADesc)),
        capellaSearchResult.getDisplayedMatches(oa));
    assertTrue(capellaSearchResult.getDisplayedMatches(diagram).isEmpty());

    // Activate both filters
    capellaSearchResult.setActiveMatchFilters(new CapellaSearchMatchFilter[] { CapellaSearchMatchFilter.CAPELLA_ELEMENT,
        CapellaSearchMatchFilter.REPRESENTATION });

    assertEquals(
        new HashSet<>(Arrays.asList(CapellaSearchMatchFilter.CAPELLA_ELEMENT, CapellaSearchMatchFilter.REPRESENTATION)),
        new HashSet<>(Arrays.asList(capellaSearchResult.getActiveMatchFilters())));

    assertTrue(capellaSearchResult.getDisplayedElements(project).isEmpty());

    assertEquals(0, capellaSearchResult.getDisplayedOccurrenceCount());
    assertEquals(0, capellaSearchResult.getDisplayedOccurrenceCount(project));
    assertEquals(0, capellaSearchResult.getDisplayedOccurrenceCount(oa));
    assertEquals(0, capellaSearchResult.getDisplayedOccurrenceCount(diagram));

    assertTrue(capellaSearchResult.getDisplayedMatches().isEmpty());
    assertTrue(capellaSearchResult.getDisplayedMatches(project).isEmpty());
    assertTrue(capellaSearchResult.getDisplayedMatches(oa).isEmpty());
    assertTrue(capellaSearchResult.getDisplayedMatches(diagram).isEmpty());
  }

}

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
package org.polarsys.capella.core.ui.search;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.DialogPage;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.fieldassist.ComboContentAdapter;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.resource.JFaceColors;
import org.eclipse.jface.text.FindReplaceDocumentAdapterContentProposalProvider;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.search.ui.IReplacePage;
import org.eclipse.search.ui.ISearchPage;
import org.eclipse.search.ui.ISearchPageContainer;
import org.eclipse.search.ui.NewSearchUI;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.fieldassist.ContentAssistCommandAdapter;
import org.eclipse.ui.navigator.NavigatorContentServiceFactory;
import org.eclipse.ui.texteditor.ITextEditorActionDefinitionIds;
import org.polarsys.capella.core.model.handler.command.CapellaResourceHelper;
import org.polarsys.capella.core.platform.sirius.ui.navigator.view.CapellaCommonNavigator;
import org.polarsys.capella.core.sirius.ui.helper.SessionHelper;

public class CapellaSearchPage extends DialogPage implements ISearchPage, IReplacePage {
  private List<CapellaSearchSettings> previousSearchSettings = new ArrayList<>();

  private Combo comboSearchPattern;
  private Label labelForComboSearchPattern;

  private CLabel labelValidationStatus;
  private ContentAssistCommandAdapter comboSearchPatternRegexContentAssist;

  private Button checkboxCaseSensitive;
  private Button checkboxRegex;

  private Map<CapellaSearchField, Button> mapSearchFieldToCheckbox = new EnumMap<>(CapellaSearchField.class);

  private Group projectsSelectionGroup;
  private CheckboxTableViewer checkboxProjectsSelection;

  private ISearchPageContainer searchPageContainer;

  private final CapellaSearchSettings capellaSearchSettings = new CapellaSearchSettings();

  private static final Predicate<IProject> IS_VALID_PROJECT = project -> {
    if (!CapellaResourceHelper.isCapellaProject(project)) {
      return false;
    }
    if (!project.isOpen()) {
      return false;
    }
    return !SessionHelper.getExistingSessions(project).isEmpty();
  };

  @Override
  public boolean performAction() {
    // The action is enabled if only the form is validated.
    CapellaSearchQuery searchQuery = new CapellaSearchQuery(capellaSearchSettings);
    NewSearchUI.runQueryInBackground(searchQuery);
    // If the action is enabled, that means the form is validated. We can save it in search history to propose it for
    // next usages in future.
    CapellaSearchSettingsHistory.appendSearchSettings(capellaSearchSettings);
    return true;
  }

  @Override
  public boolean performReplace() {
    // The action is enabled if only the form is validated.
    CapellaSearchQuery searchQuery = new CapellaSearchQuery(capellaSearchSettings);
    IStatus searchStatus = NewSearchUI.runQueryInForeground(searchPageContainer.getRunnableContext(), searchQuery);

    if (searchStatus.isOK()) {
      CapellaSearchResult searchResult = searchQuery.getSearchResult();
      if (searchResult.getMatchCount() > 0) {
        Set<CapellaSearchMatch> allMatches = searchResult.getMatches();
        CapellaReplaceRunnable capellaReplaceRunnable = new CapellaReplaceRunnable(searchQuery, allMatches, true);
        new CapellaReplaceRunnableWrapper(capellaReplaceRunnable).run();
      } else {
        MessageDialog.openInformation(getShell(), Messages.ReplaceDialog_Title, String.format(
            Messages.ReplaceDialog_No_Match_Found_Message, searchQuery.getCapellaSearchSettings().getTextPattern()));
      }
    }

    // If the action is enabled, that means the form is validated. We can save it in search history to propose it for
    // next usages in future.
    CapellaSearchSettingsHistory.appendSearchSettings(capellaSearchSettings);
    return true;
  }

  @Override
  public void createControl(Composite parent) {
    initializeDialogUnits(parent);

    // init history searches
    previousSearchSettings.addAll(CapellaSearchSettingsHistory.getAllSearchSettings());

    Composite composite = new Composite(parent, SWT.NONE);
    composite.setFont(parent.getFont());
    GridLayoutFactory.swtDefaults().numColumns(2).applyTo(composite);

    createSearchPatternControls(composite);

    createSearchFieldsSelectionControl(composite);

    createProjectsSelectionControl(composite);
    setControl(composite);

    Dialog.applyDialogFont(composite);
  }

  @Override
  public void setContainer(ISearchPageContainer container) {
    this.searchPageContainer = container;
  }

  @Override
  public void setVisible(boolean visible) {
    if (visible && comboSearchPattern != null) {
      if (!previousSearchSettings.isEmpty()) {
        String[] previousSearchPatterns = previousSearchSettings.stream() //
            .map(CapellaSearchSettings::getTextPattern) //
            .toArray(String[]::new);
        comboSearchPattern.setItems(previousSearchPatterns);
        comboSearchPattern.select(0);
        applySearchSettings(previousSearchSettings.get(0));
      }
      comboSearchPattern.setFocus();
    }
    super.setVisible(visible);
  }

  private void validate() {
    // Update the search settings from UI
    capellaSearchSettings.setTextPattern(comboSearchPattern.getText());

    capellaSearchSettings.setCaseSensitive(checkboxCaseSensitive.getSelection());
    capellaSearchSettings.setRegExSearch(checkboxRegex.getSelection());

    capellaSearchSettings.clearSearchFields();
    for (Entry<CapellaSearchField, Button> entry : mapSearchFieldToCheckbox.entrySet()) {
      CapellaSearchField capellaSearchField = entry.getKey();
      Button searchFieldCheckbox = entry.getValue();
      if (searchFieldCheckbox.getSelection()) {
        capellaSearchSettings.addSearchField(capellaSearchField);
      }
    }

    capellaSearchSettings.clearProjects();
    for (Object checkedElement : checkboxProjectsSelection.getCheckedElements()) {
      if (checkedElement instanceof IProject) {
        capellaSearchSettings.addProject(((IProject) checkedElement).getName());
      }
    }

    // Validate
    IStatus validateStatus = capellaSearchSettings.validate();
    if (validateStatus.isOK()) {
      searchPageContainer.setPerformActionEnabled(true);
      labelValidationStatus.setText(Messages.CapellaSearchPage_Validation_Message_OK);
    } else {
      labelValidationStatus.setText(validateStatus.getMessage());
      searchPageContainer.setPerformActionEnabled(false);
    }
  }

  private void applySearchSettings(CapellaSearchSettings settings) {
    checkboxCaseSensitive.setSelection(settings.isCaseSensitive());
    comboSearchPattern.setText(settings.getTextPattern());

    boolean regexEnabled = settings.isRegExSearch();
    checkboxRegex.setSelection(regexEnabled);
    comboSearchPatternRegexContentAssist.setEnabled(regexEnabled);
    if (regexEnabled) {
      labelForComboSearchPattern.setText(Messages.CapellaSearchPage_Combo_Pattern_Label_Regex_Enabled);
    } else {
      labelForComboSearchPattern.setText(Messages.CapellaSearchPage_Combo_Pattern_Label_Regex_Disabled);
    }

    for (CapellaSearchField searchField : CapellaSearchField.values()) {
      mapSearchFieldToCheckbox.get(searchField).setSelection(settings.containSearchField(searchField));
    }

    // Need to check whether previously selected projects are still available at the current moment before checking it
    Set<IProject> projectsToCheck = settings.getProjects() //
        .stream() //
        .map(ResourcesPlugin.getWorkspace().getRoot()::getProject) //
        .filter(IS_VALID_PROJECT) //
        .collect(Collectors.toSet());
    checkboxProjectsSelection.setCheckedElements(projectsToCheck.toArray());
    projectsSelectionGroup.setText(getProjectSelectionsText());

    validate();
  }

  private void createSearchPatternControls(Composite group) {
    createLabelForComboSearchPattern(group);
    createComboSearchPattern(group);
    createCheckboxCaseSensitive(group);
    createCheckboxRegex(group);
    createLabelValidationStatus(group);
  }

  private void createLabelForComboSearchPattern(Composite group) {
    labelForComboSearchPattern = new Label(group, SWT.LEAD);
    labelForComboSearchPattern.setText(Messages.CapellaSearchPage_Combo_Pattern_Label_Regex_Disabled);
    GridDataFactory.swtDefaults() //
        .align(SWT.FILL, SWT.CENTER) //
        .span(2, 1) //
        .applyTo(labelForComboSearchPattern);
    labelForComboSearchPattern.setFont(group.getFont());
  }

  private void createComboSearchPattern(Composite group) {
    // Pattern combo
    comboSearchPattern = new Combo(group, SWT.SINGLE | SWT.BORDER);
    comboSearchPattern.setFont(group.getFont());
    GridDataFactory.fillDefaults() //
        .grab(false, false) //
        .span(1, 2) //
        .hint(convertWidthInCharsToPixels(50), SWT.DEFAULT) //
        .applyTo(comboSearchPattern);

    comboSearchPattern.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent e) {
        int selectionIndex = comboSearchPattern.getSelectionIndex();
        if (-1 < selectionIndex && selectionIndex < previousSearchSettings.size()) {
          CapellaSearchSettings previous = previousSearchSettings.get(selectionIndex);
          if (previous != null) {
            applySearchSettings(previous);
          }
        }
      }
    });
    // add some listeners for regex syntax checking
    comboSearchPattern.addModifyListener(e -> validate());
    comboSearchPattern.setToolTipText(Messages.CapellaSearchPage_Combo_Pattern_Label_Regex_Enabled);

    ComboContentAdapter contentAdapter = new ComboContentAdapter();
    FindReplaceDocumentAdapterContentProposalProvider findProposer = new FindReplaceDocumentAdapterContentProposalProvider(
        true);
    comboSearchPatternRegexContentAssist = new ContentAssistCommandAdapter(comboSearchPattern, contentAdapter,
        findProposer, ITextEditorActionDefinitionIds.CONTENT_ASSIST_PROPOSALS, new char[0], true);
    comboSearchPatternRegexContentAssist.setEnabled(false);
  }

  private void createLabelValidationStatus(Composite group) {
    labelValidationStatus = new CLabel(group, SWT.LEAD);
    GridDataFactory.swtDefaults().align(SWT.FILL, SWT.TOP).grab(true, false).applyTo(labelValidationStatus);
    labelValidationStatus.setFont(group.getFont());
    labelValidationStatus.setAlignment(SWT.LEFT);
    labelValidationStatus.setForeground(JFaceColors.getErrorText(labelValidationStatus.getDisplay()));
  }

  private void createCheckboxRegex(Composite group) {
    checkboxRegex = new Button(group, SWT.CHECK);
    checkboxRegex.setText(Messages.CapellaSearchPage_Checkbox_Regex_Label);
    GridDataFactory.swtDefaults().align(SWT.FILL, SWT.CENTER).span(1, 2).applyTo(checkboxRegex);
    checkboxRegex.setFont(group.getFont());
    checkboxRegex.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent e) {
        boolean regexEnabled = checkboxRegex.getSelection();
        comboSearchPatternRegexContentAssist.setEnabled(regexEnabled);
        if (regexEnabled) {
          labelForComboSearchPattern.setText(Messages.CapellaSearchPage_Combo_Pattern_Label_Regex_Enabled);
        } else {
          labelForComboSearchPattern.setText(Messages.CapellaSearchPage_Combo_Pattern_Label_Regex_Disabled);
        }
        validate();
      }
    });
  }

  private void createCheckboxCaseSensitive(Composite group) {
    checkboxCaseSensitive = new Button(group, SWT.CHECK);
    checkboxCaseSensitive.setText(Messages.CapellaSearchPage_Checkbox_CaseSensitive_Label);
    GridDataFactory.swtDefaults().align(SWT.FILL, SWT.CENTER).applyTo(checkboxCaseSensitive);
    checkboxCaseSensitive.setFont(group.getFont());
  }

  private void createSearchFieldsSelectionControl(Composite group) {
    Group searchInGroup = new Group(group, SWT.NONE);
    searchInGroup.setText(Messages.CapellaSearchPage_Scope_Group_Label);
    GridDataFactory.swtDefaults().align(SWT.FILL, SWT.CENTER).grab(true, false).span(3, 1).applyTo(searchInGroup);
    GridLayoutFactory.swtDefaults().numColumns(3).applyTo(searchInGroup);

    for (CapellaSearchField capellaSearchField : CapellaSearchField.values()) {

      Button searchFieldCheckbox = new Button(searchInGroup, SWT.CHECK);
      searchFieldCheckbox.setText(capellaSearchField.getLabel());
      searchFieldCheckbox.setSelection(true);

      searchFieldCheckbox.addSelectionListener(new SelectionAdapter() {
        @Override
        public void widgetSelected(SelectionEvent e) {
          validate();
        }
      });
      GridDataFactory.swtDefaults().applyTo(searchFieldCheckbox);

      searchFieldCheckbox.setFont(searchInGroup.getFont());

      mapSearchFieldToCheckbox.put(capellaSearchField, searchFieldCheckbox);
    }
  }

  private void createProjectsSelectionControl(Composite parent) {
    projectsSelectionGroup = new Group(parent, SWT.NONE);
    GridDataFactory.fillDefaults().grab(true, true).span(2, 1).applyTo(projectsSelectionGroup);
    GridLayoutFactory.swtDefaults().numColumns(2).applyTo(projectsSelectionGroup);

    checkboxProjectsSelection = CheckboxTableViewer.newCheckList(projectsSelectionGroup,
        SWT.SINGLE | SWT.V_SCROLL | SWT.H_SCROLL | SWT.BORDER);

    checkboxProjectsSelection.setLabelProvider(NavigatorContentServiceFactory.INSTANCE
        .createContentService(CapellaCommonNavigator.ID).createCommonLabelProvider());

    checkboxProjectsSelection.setContentProvider(new ArrayContentProvider() {
      @Override
      public Object[] getElements(Object inputElement) {
        if (inputElement instanceof IWorkspaceRoot) {
          IProject[] projects = ((IWorkspaceRoot) inputElement).getProjects();
          return Stream.of(projects).filter(IS_VALID_PROJECT).toArray();
        }
        return super.getElements(inputElement);
      }
    });

    checkboxProjectsSelection.setInput(ResourcesPlugin.getWorkspace().getRoot());
    checkboxProjectsSelection.setAllChecked(true);
    checkboxProjectsSelection.addCheckStateListener(e -> {
      projectsSelectionGroup.setText(getProjectSelectionsText());
      validate();
    });
    GridDataFactory.fillDefaults().grab(true, true).hint(SWT.DEFAULT, 120)
        .applyTo(checkboxProjectsSelection.getControl());

    ToolBar toolbar = new ToolBar(projectsSelectionGroup, SWT.FLAT | SWT.VERTICAL);
    createCheckboxCheckAllProjects(toolbar);
    createCheckboxUnCheckAllProjects(toolbar);
  }

  private void createCheckboxCheckAllProjects(ToolBar toolbar) {
    GridDataFactory.swtDefaults().align(SWT.BEGINNING, SWT.TOP).grab(false, true).applyTo(toolbar);

    // Check All button
    ToolItem checkAllItem = new ToolItem(toolbar, SWT.PUSH);
    checkAllItem.setToolTipText(Messages.CapellaSearchPage_ProjectsSelection_CheckedAll_Label);
    Image checkImage = Activator.getDefault().getImage("check_all.png"); //$NON-NLS-1$
    checkAllItem.setImage(checkImage);
    checkAllItem.addSelectionListener(new SelectionAdapter() {

      @Override
      public void widgetSelected(SelectionEvent e) {
        checkboxProjectsSelection.setAllChecked(true);
        projectsSelectionGroup.setText(getProjectSelectionsText());
        validate();
      }

    });
  }

  private void createCheckboxUnCheckAllProjects(ToolBar toolbar) {
    ToolItem unCheckAllItem = new ToolItem(toolbar, SWT.PUSH);
    unCheckAllItem.setToolTipText(Messages.CapellaSearchPage_ProjectsSelection_UnCheckedAll_Label);
    Image uncheckImage = Activator.getDefault().getImage("uncheck_all.png"); //$NON-NLS-1$
    unCheckAllItem.setImage(uncheckImage);
    unCheckAllItem.addSelectionListener(new SelectionAdapter() {

      @Override
      public void widgetSelected(SelectionEvent e) {
        checkboxProjectsSelection.setAllChecked(false);
        projectsSelectionGroup.setText(getProjectSelectionsText());
        validate();
      }
    });
  }

  private String getProjectSelectionsText() {
    int countSelectedProjects = checkboxProjectsSelection.getCheckedElements().length;
    int countAllProjects = checkboxProjectsSelection.getTable().getItemCount();
    return String.format(Messages.CapellaSearchPage_ProjectsSelection_Group_Label, countSelectedProjects,
        countAllProjects);
  }
}

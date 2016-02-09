/*******************************************************************************
 * Copyright (c) 2006, 2016 THALES GLOBAL SERVICES.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *  
 * Contributors:
 *    Thales - initial API and implementation
 *******************************************************************************/
package org.polarsys.capella.core.transition.system.topdown.preferences;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.polarsys.capella.common.flexibility.properties.property.AbstractProperty;
import org.polarsys.capella.common.flexibility.properties.property.IDefaultValueProperty;
import org.polarsys.capella.common.flexibility.properties.schema.IEditableProperty;
import org.polarsys.capella.common.flexibility.properties.schema.IPropertyContext;
import org.polarsys.capella.common.flexibility.properties.schema.PropertiesSchemaConstants;
import org.polarsys.capella.core.commands.preferences.service.ScopedCapellaPreferencesStore;
import org.polarsys.capella.core.commands.preferences.util.PreferencesHelper;

/**
 *
 */
public class BooleanPropertyPreference extends AbstractProperty implements IEditableProperty, IDefaultValueProperty {

  public BooleanPropertyPreference() {
    super();
  }

  /**
   * {@inheritDoc}
   */
  public Object getType() {
    return Boolean.class;
  }

  /**
   * {@inheritDoc}
   */
  public Object getValue(IPropertyContext context) {
    boolean projectScope = false;
    if (context instanceof TopDownPropertyContext) {
      projectScope = ((TopDownPropertyContext) context).isProject();
    }
    String preferenceId = getId();
    if (isArgumentSet(PropertiesSchemaConstants.PropertiesSchema_PROPERTY_PREFERENCE__PREFERENCE_ID)) {
      preferenceId = getParameter(PropertiesSchemaConstants.PropertiesSchema_PROPERTY_PREFERENCE__PREFERENCE_ID);
    }
    return ScopedCapellaPreferencesStore.getBoolean(projectScope, preferenceId);
  }

  /**
   * {@inheritDoc}
   */
  public void setValue(IPropertyContext context) {
    Object value = context.getCurrentValue(this);
    String preferenceId = getId();
    if (isArgumentSet(PropertiesSchemaConstants.PropertiesSchema_PROPERTY_PREFERENCE__PREFERENCE_ID)) {
      preferenceId = getParameter(PropertiesSchemaConstants.PropertiesSchema_PROPERTY_PREFERENCE__PREFERENCE_ID);
    }

    boolean value2 = ((Boolean) this.toType(value, context)).booleanValue();
    boolean projectScope = false;
    if (context instanceof TopDownPropertyContext) {
      projectScope = ((TopDownPropertyContext) context).isProject();
    }
    if (projectScope) {
      IProject project = PreferencesHelper.getSelectedCapellaProject();
      ScopedCapellaPreferencesStore.getOptions().put(preferenceId, String.valueOf(value2));
      if (project != null) {
        ScopedCapellaPreferencesStore.putBoolean(project, preferenceId, value2);
      }
    } else {
      ScopedCapellaPreferencesStore.putBoolean(null, preferenceId, value2);
    }
  }

  /**
   * {@inheritDoc}
   */
  public Object toType(Object value, IPropertyContext context) {
    Boolean bval = Boolean.TRUE;
    try {
      if (value instanceof Boolean) {
        bval = (Boolean) value;
      } else if (value instanceof String) {
        bval = Boolean.valueOf((String) value);
      }
    } catch (Exception e) {
      // Nothing here
    }
    return bval;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IStatus validate(Object newValue, IPropertyContext context) {
    if (newValue == null) {
      return Status.CANCEL_STATUS;
    }
    return Status.OK_STATUS;
  }

  /**
   * {@inheritDoc}
   */
  public Object getDefaultValue(IPropertyContext context) {
    String argument = getParameter(PropertiesSchemaConstants.PropertiesSchema_PROPERTY_PREFERENCE__DEFAULT);
    return ((Boolean) toType(argument, context)).booleanValue();
  }
}

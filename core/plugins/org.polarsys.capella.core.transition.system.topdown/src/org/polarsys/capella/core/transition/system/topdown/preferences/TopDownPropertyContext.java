/*******************************************************************************
 * Copyright (c) 2016 THALES GLOBAL SERVICES.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *  
 * Contributors:
 *    Thales - initial API and implementation
 *******************************************************************************/
package org.polarsys.capella.core.transition.system.topdown.preferences;

import java.util.HashSet;

import org.polarsys.capella.common.flexibility.properties.property.PropertyContext;
import org.polarsys.capella.common.flexibility.properties.schema.IProperties;
import org.polarsys.capella.common.flexibility.properties.schema.IProperty;
import org.polarsys.capella.common.flexibility.properties.schema.PropertiesSchemaConstants;
import org.polarsys.capella.core.commands.preferences.service.ScopedCapellaPreferencesStore;

/**
 *
 */
public class TopDownPropertyContext extends PropertyContext {

  boolean propertyPage = false;

  public TopDownPropertyContext(IProperties properties, boolean propertyPage) {
    super(properties);
    this.propertyPage = propertyPage;

  }

  /**
   * @see org.polarsys.capella.common.flexibility.properties.property.PropertyContext#write(org.polarsys.capella.common.flexibility.properties.schema.IProperty)
   */
  @Override
  public void write(IProperty property) {
    super.write(property);
    String preferenceId = property
        .getParameter(PropertiesSchemaConstants.PropertiesSchema_PROPERTY_PREFERENCE__PREFERENCE_ID);
    if (preferenceId == null) {
      preferenceId = property.getId();
    }
  }

  /**
   * @see org.polarsys.capella.common.flexibility.properties.property.PropertyContext#writeAll()
   */
  @Override
  public void writeAll() {
    super.writeAll();
    HashSet<String> scopes = new HashSet<String>();
    for (IProperty property : getProperties().getAllItems()) {
      String scope = property.getParameter(PropertiesSchemaConstants.PropertiesSchema_PROPERTY_PREFERENCE__SCOPE);
      if (scope != null) {
        scopes.add(scope);
      }
    }

    for (String scope : scopes) {
      ScopedCapellaPreferencesStore.getInstance(scope).save();
    }
  }

  public boolean isProject() {
    return propertyPage;
  }
}

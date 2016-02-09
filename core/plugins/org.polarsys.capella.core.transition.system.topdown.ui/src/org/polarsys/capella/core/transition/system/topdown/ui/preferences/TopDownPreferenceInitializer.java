/*******************************************************************************
 * Copyright (c) 2015, 2016 THALES GLOBAL SERVICES.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *  
 * Contributors:
 *    Thales - initial API and implementation
 *******************************************************************************/
package org.polarsys.capella.core.transition.system.topdown.ui.preferences;

import org.polarsys.capella.common.flexibility.properties.loader.PropertiesLoader;
import org.polarsys.capella.common.flexibility.properties.property.IDefaultValueProperty;
import org.polarsys.capella.common.flexibility.properties.schema.IProperties;
import org.polarsys.capella.common.flexibility.properties.schema.IProperty;
import org.polarsys.capella.common.flexibility.properties.schema.IPropertyContext;
import org.polarsys.capella.core.commands.preferences.service.AbstractPreferencesInitializer;
import org.polarsys.capella.core.transition.system.topdown.constants.ITopDownConstants;
import org.polarsys.capella.core.transition.system.topdown.preferences.TopDownPropertyContext;

/**
 *
 */
public class TopDownPreferenceInitializer extends AbstractPreferencesInitializer {
  /**
	 *
	 */
  public TopDownPreferenceInitializer() {
    super(org.polarsys.capella.core.transition.system.topdown.ui.Activator.PLUGIN_ID);
  }

  /**
   * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#initializeDefaultPreferences()
   */
  @Override
  public void initializeDefaultPreferences() {
    IProperties properties = new PropertiesLoader().getProperties(ITopDownConstants.OPTIONS_SCOPE__PREFERENCES);
    IPropertyContext context = new TopDownPropertyContext(properties, false);
    for (IProperty property : properties.getAllItems()) {
      if (property instanceof IDefaultValueProperty) {
        context.setCurrentValue(property, ((IDefaultValueProperty) property).getDefaultValue(context));
      }
    }
    context.writeAll();
  }
}

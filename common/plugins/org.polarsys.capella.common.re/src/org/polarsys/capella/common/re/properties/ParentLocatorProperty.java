/*******************************************************************************
 * Copyright (c) 2017 THALES GLOBAL SERVICES.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Thales - initial API and implementation
 *******************************************************************************/
package org.polarsys.capella.common.re.properties;

import org.polarsys.capella.common.flexibility.properties.schema.IPropertyContext;
import org.polarsys.capella.common.re.constants.IReConstants;

/**
 * This stores the current value of the parent locator option
 * in the transposer context.
 * FIXME store this in preferences, like BooleanPreferenceProperty
 */
public class ParentLocatorProperty extends AbstractContextProperty {

  @Override
  protected Object getInitialValue(IPropertyContext context) {
    return IReConstants.LOCATOR_OPTION_DEFAULT;
  }

}

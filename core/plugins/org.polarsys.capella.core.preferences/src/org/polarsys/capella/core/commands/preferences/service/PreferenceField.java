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
package org.polarsys.capella.core.commands.preferences.service;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

/**
 *
 */
public class PreferenceField extends BooleanFieldEditor {

  protected static final String POINT_SEPARATOR = "."; //$NON-NLS-1$

  /**
   * Creates a boolean field editor in the default style.
   * @param name the name of the preference this field editor works on
   * @param label the label text of the field editor
   * @param parent the parent of the field editor's control
   */
  public PreferenceField(String name, String label, Composite parent) {
    super(name, label, DEFAULT, parent);
  }

  /**
   * @see org.eclipse.jface.preference.BooleanFieldEditor#getChangeControl(Composite)
   */
  @Override
  public Button getChangeControl(Composite parent) {
    return super.getChangeControl(parent);
  }

  @Override
  protected void valueChanged(boolean oldValue, boolean newValue) {
    super.valueChanged(oldValue, newValue);
  }

  @Override
  protected void fireValueChanged(String property, Object oldValue, Object newValue) {
    super.fireValueChanged(property, oldValue, newValue);
  }

  /**
   * Set whether or not the controls in the field editor are enabled.
   * @param enabled The enabled state.
   * @param parent The parent of the controls in the group. Used to create the controls if required.
   */
  @Override
  public void setEnabled(boolean enabled, Composite parent) {
    parent.setEnabled(enabled);
  }
}

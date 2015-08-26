/*******************************************************************************
 * Copyright (c) 2006, 2015 THALES GLOBAL SERVICES.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *  
 * Contributors:
 *    Thales - initial API and implementation
 *******************************************************************************/
package org.polarsys.capella.core.explorer.activity.ui.pages;

import java.util.Set;

import org.eclipse.emf.ecore.EClass;
import org.polarsys.capella.core.data.pa.PaPackage;
import org.polarsys.capella.core.sirius.analysis.IViewpointNameConstants;

public class PhysicalArchitecturePage extends AbstractCapellaPage {
	

	@Override
	public EClass getFilteringMetaClassForCommonViewpoint() {
		return PaPackage.Literals.PHYSICAL_ARCHITECTURE;
	}

	@Override
	public Set<String> getHandledViewpoint() {
		if (!handledViewpoint.contains(IViewpointNameConstants.PHYSICAL_ARCHITECTURE_VIEWPOINT_NAME))
			handledViewpoint.add(IViewpointNameConstants.PHYSICAL_ARCHITECTURE_VIEWPOINT_NAME);
		return handledViewpoint;
	}
}

/*******************************************************************************
 * Copyright (c) 2006, 2014 THALES GLOBAL SERVICES.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *  
 * Contributors:
 *    Thales - initial API and implementation
 *******************************************************************************/
package org.polarsys.capella.core.sequencediag.datas;

import org.eclipse.sirius.business.api.featureextensions.AbstractFeatureExtensionServices;
import org.eclipse.sirius.viewpoint.DFeatureExtension;
import org.eclipse.sirius.viewpoint.description.FeatureExtensionDescription;

public class SequenceDiagramAspect extends AbstractFeatureExtensionServices {
	private Class<? extends DFeatureExtension> _featureExtension = SequenceLabelFeatureExtension.class;
	private Class<? extends FeatureExtensionDescription> _featureExtensionDescription = SequenceLabelFeatureExtensionDescription.class;

	@Override
	protected Class<? extends FeatureExtensionDescription> getFeatureExtensionDescriptionClass() {
		return _featureExtensionDescription;
	}

	@Override
	protected Class<? extends DFeatureExtension> getFeatureExtensionClass() {
		return _featureExtension;
	}

}

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
package org.polarsys.capella.core.re.project;

import org.eclipse.emf.common.util.URI;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class ReProjectActivator implements BundleActivator {

  public static final String PLUGIN_ID = "org.polarsys.capella.core.re.project";
	private static BundleContext context;

	public static final URI SKELETON_PROJECT_URI = URI.createPlatformPluginURI("/" + PLUGIN_ID + "/skeleton/skeletonProject.melodymodeller", false);

	static BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	@Override
  public void start(BundleContext bundleContext) throws Exception {
		ReProjectActivator.context = bundleContext;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	@Override
  public void stop(BundleContext bundleContext) throws Exception {
		ReProjectActivator.context = null;
	}

}

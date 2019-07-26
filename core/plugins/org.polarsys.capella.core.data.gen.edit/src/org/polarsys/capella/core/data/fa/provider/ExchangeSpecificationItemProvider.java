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

package org.polarsys.capella.core.data.fa.provider;

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.CopyCommand.Helper;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;
import org.polarsys.capella.common.data.activity.ActivityPackage;
import org.polarsys.capella.common.data.modellingcore.ModellingcorePackage;
import org.polarsys.capella.common.model.copypaste.SharedInitializeCopyCommand;
import org.polarsys.capella.core.data.capellacore.provider.NamedElementItemProvider;
import org.polarsys.capella.core.data.fa.ExchangeSpecification;
import org.polarsys.capella.core.data.fa.FaPackage;
import org.polarsys.kitalpha.emde.extension.ExtensionModelManager;
import org.polarsys.kitalpha.emde.extension.ModelExtensionHelper;

/**
 * This is the item provider adapter for a {@link org.polarsys.capella.core.data.fa.ExchangeSpecification} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class ExchangeSpecificationItemProvider
	extends NamedElementItemProvider
	implements
		IEditingDomainItemProvider,
		IStructuredItemContentProvider,
		ITreeItemContentProvider,
		IItemLabelProvider,
		IItemPropertySource {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected IItemPropertyDescriptor realizedFlowPropertyDescriptor;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected IItemPropertyDescriptor sourcePropertyDescriptor;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected IItemPropertyDescriptor targetPropertyDescriptor;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected IItemPropertyDescriptor containingLinkPropertyDescriptor;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected IItemPropertyDescriptor linkPropertyDescriptor;

	/**
	 * This constructs an instance from a factory and a notifier.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ExchangeSpecificationItemProvider(AdapterFactory adapterFactory) {
		super(adapterFactory);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void checkChildCreationExtender(Object object) {
		super.checkChildCreationExtender(object);
		if (object instanceof EObject) {
			EObject eObject = (EObject) object;
			// Process ModellingcorePackage.Literals.ABSTRACT_RELATIONSHIP__REALIZED_FLOW
			if (realizedFlowPropertyDescriptor != null) {
				Object realizedFlowValue = eObject.eGet(ModellingcorePackage.Literals.ABSTRACT_RELATIONSHIP__REALIZED_FLOW, true);
				if (realizedFlowValue != null && realizedFlowValue instanceof EObject && ModelExtensionHelper.getInstance(eObject).isExtensionModelDisabled((EObject) realizedFlowValue)) {
					itemPropertyDescriptors.remove(realizedFlowPropertyDescriptor);
				} else if (realizedFlowValue == null && ExtensionModelManager.getAnyType(eObject, ModellingcorePackage.Literals.ABSTRACT_RELATIONSHIP__REALIZED_FLOW) != null) {
					itemPropertyDescriptors.remove(realizedFlowPropertyDescriptor);				  					
				} else if (itemPropertyDescriptors.contains(realizedFlowPropertyDescriptor) == false) {
					itemPropertyDescriptors.add(realizedFlowPropertyDescriptor);
				}
			}
			// Process ModellingcorePackage.Literals.ABSTRACT_INFORMATION_FLOW__SOURCE
			if (sourcePropertyDescriptor != null) {
				Object sourceValue = eObject.eGet(ModellingcorePackage.Literals.ABSTRACT_INFORMATION_FLOW__SOURCE, true);
				if (sourceValue != null && sourceValue instanceof EObject && ModelExtensionHelper.getInstance(eObject).isExtensionModelDisabled((EObject) sourceValue)) {
					itemPropertyDescriptors.remove(sourcePropertyDescriptor);
				} else if (sourceValue == null && ExtensionModelManager.getAnyType(eObject, ModellingcorePackage.Literals.ABSTRACT_INFORMATION_FLOW__SOURCE) != null) {
					itemPropertyDescriptors.remove(sourcePropertyDescriptor);				  					
				} else if (itemPropertyDescriptors.contains(sourcePropertyDescriptor) == false) {
					itemPropertyDescriptors.add(sourcePropertyDescriptor);
				}
			}
			// Process ModellingcorePackage.Literals.ABSTRACT_INFORMATION_FLOW__TARGET
			if (targetPropertyDescriptor != null) {
				Object targetValue = eObject.eGet(ModellingcorePackage.Literals.ABSTRACT_INFORMATION_FLOW__TARGET, true);
				if (targetValue != null && targetValue instanceof EObject && ModelExtensionHelper.getInstance(eObject).isExtensionModelDisabled((EObject) targetValue)) {
					itemPropertyDescriptors.remove(targetPropertyDescriptor);
				} else if (targetValue == null && ExtensionModelManager.getAnyType(eObject, ModellingcorePackage.Literals.ABSTRACT_INFORMATION_FLOW__TARGET) != null) {
					itemPropertyDescriptors.remove(targetPropertyDescriptor);				  					
				} else if (itemPropertyDescriptors.contains(targetPropertyDescriptor) == false) {
					itemPropertyDescriptors.add(targetPropertyDescriptor);
				}
			}
			// Process FaPackage.Literals.EXCHANGE_SPECIFICATION__CONTAINING_LINK
			if (containingLinkPropertyDescriptor != null) {
				Object containingLinkValue = eObject.eGet(FaPackage.Literals.EXCHANGE_SPECIFICATION__CONTAINING_LINK, true);
				if (containingLinkValue != null && containingLinkValue instanceof EObject && ModelExtensionHelper.getInstance(eObject).isExtensionModelDisabled((EObject) containingLinkValue)) {
					itemPropertyDescriptors.remove(containingLinkPropertyDescriptor);
				} else if (containingLinkValue == null && ExtensionModelManager.getAnyType(eObject, FaPackage.Literals.EXCHANGE_SPECIFICATION__CONTAINING_LINK) != null) {
					itemPropertyDescriptors.remove(containingLinkPropertyDescriptor);				  					
				} else if (itemPropertyDescriptors.contains(containingLinkPropertyDescriptor) == false) {
					itemPropertyDescriptors.add(containingLinkPropertyDescriptor);
				}
			}
			// Process FaPackage.Literals.EXCHANGE_SPECIFICATION__LINK
			if (linkPropertyDescriptor != null) {
				Object linkValue = eObject.eGet(FaPackage.Literals.EXCHANGE_SPECIFICATION__LINK, true);
				if (linkValue != null && linkValue instanceof EObject && ModelExtensionHelper.getInstance(eObject).isExtensionModelDisabled((EObject) linkValue)) {
					itemPropertyDescriptors.remove(linkPropertyDescriptor);
				} else if (linkValue == null && ExtensionModelManager.getAnyType(eObject, FaPackage.Literals.EXCHANGE_SPECIFICATION__LINK) != null) {
					itemPropertyDescriptors.remove(linkPropertyDescriptor);				  					
				} else if (itemPropertyDescriptors.contains(linkPropertyDescriptor) == false) {
					itemPropertyDescriptors.add(linkPropertyDescriptor);
				}
			}
		}		
	}

	/**
	 * This returns the property descriptors for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public List<IItemPropertyDescriptor> getPropertyDescriptors(Object object) {
		if (itemPropertyDescriptors == null) {
			super.getPropertyDescriptors(object);

			addRealizedFlowPropertyDescriptor(object);
			addRealizationsPropertyDescriptor(object);
			addConvoyedInformationsPropertyDescriptor(object);
			addSourcePropertyDescriptor(object);
			addTargetPropertyDescriptor(object);
			addRealizingActivityFlowsPropertyDescriptor(object);
			addContainingLinkPropertyDescriptor(object);
			addLinkPropertyDescriptor(object);
			addOutgoingExchangeSpecificationRealizationsPropertyDescriptor(object);
			addIncomingExchangeSpecificationRealizationsPropertyDescriptor(object);
		}
		// begin-extension-code
		checkChildCreationExtender(object);
		// end-extension-code
		return itemPropertyDescriptors;
	}

	/**
	 * This adds a property descriptor for the Realized Flow feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addRealizedFlowPropertyDescriptor(Object object) {
		// begin-extension-code
		realizedFlowPropertyDescriptor = createItemPropertyDescriptor
		// end-extension-code		
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_AbstractRelationship_realizedFlow_feature"), //$NON-NLS-1$
				 getString("_UI_PropertyDescriptor_description", "_UI_AbstractRelationship_realizedFlow_feature", "_UI_AbstractRelationship_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				 ModellingcorePackage.Literals.ABSTRACT_RELATIONSHIP__REALIZED_FLOW,
				 true,
				 false,
				 true,
				 null,
				 null,
		// begin-extension-code
				 null);
		itemPropertyDescriptors.add(realizedFlowPropertyDescriptor);
		// end-extension-code
	}

	/**
	 * This adds a property descriptor for the Realizations feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addRealizationsPropertyDescriptor(Object object) {

		// begin-extension-code
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
		// end-extension-code
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_AbstractInformationFlow_realizations_feature"), //$NON-NLS-1$
				 getString("_UI_PropertyDescriptor_description", "_UI_AbstractInformationFlow_realizations_feature", "_UI_AbstractInformationFlow_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				 ModellingcorePackage.Literals.ABSTRACT_INFORMATION_FLOW__REALIZATIONS,
				 true,
				 false,
				 true,
				 null,
				 null,
		// begin-extension-code
				 null));
		// end-extension-code
	}

	/**
	 * This adds a property descriptor for the Convoyed Informations feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addConvoyedInformationsPropertyDescriptor(Object object) {

		// begin-extension-code
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
		// end-extension-code
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_AbstractInformationFlow_convoyedInformations_feature"), //$NON-NLS-1$
				 getString("_UI_PropertyDescriptor_description", "_UI_AbstractInformationFlow_convoyedInformations_feature", "_UI_AbstractInformationFlow_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				 ModellingcorePackage.Literals.ABSTRACT_INFORMATION_FLOW__CONVOYED_INFORMATIONS,
				 true,
				 false,
				 true,
				 null,
				 null,
		// begin-extension-code
				 null));
		// end-extension-code
	}

	/**
	 * This adds a property descriptor for the Source feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addSourcePropertyDescriptor(Object object) {
		// begin-extension-code
		sourcePropertyDescriptor = createItemPropertyDescriptor
		// end-extension-code		
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_AbstractInformationFlow_source_feature"), //$NON-NLS-1$
				 getString("_UI_PropertyDescriptor_description", "_UI_AbstractInformationFlow_source_feature", "_UI_AbstractInformationFlow_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				 ModellingcorePackage.Literals.ABSTRACT_INFORMATION_FLOW__SOURCE,
				 true,
				 false,
				 true,
				 null,
				 null,
		// begin-extension-code
				 null);
		itemPropertyDescriptors.add(sourcePropertyDescriptor);
		// end-extension-code
	}

	/**
	 * This adds a property descriptor for the Target feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addTargetPropertyDescriptor(Object object) {
		// begin-extension-code
		targetPropertyDescriptor = createItemPropertyDescriptor
		// end-extension-code		
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_AbstractInformationFlow_target_feature"), //$NON-NLS-1$
				 getString("_UI_PropertyDescriptor_description", "_UI_AbstractInformationFlow_target_feature", "_UI_AbstractInformationFlow_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				 ModellingcorePackage.Literals.ABSTRACT_INFORMATION_FLOW__TARGET,
				 true,
				 false,
				 true,
				 null,
				 null,
		// begin-extension-code
				 null);
		itemPropertyDescriptors.add(targetPropertyDescriptor);
		// end-extension-code
	}

	/**
	 * This adds a property descriptor for the Realizing Activity Flows feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addRealizingActivityFlowsPropertyDescriptor(Object object) {

		// begin-extension-code
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
		// end-extension-code
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_ActivityExchange_realizingActivityFlows_feature"), //$NON-NLS-1$
				 getString("_UI_PropertyDescriptor_description", "_UI_ActivityExchange_realizingActivityFlows_feature", "_UI_ActivityExchange_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				 ActivityPackage.Literals.ACTIVITY_EXCHANGE__REALIZING_ACTIVITY_FLOWS,
				 false,
				 false,
				 false,
				 null,
				 null,
		// begin-extension-code
				 null));
		// end-extension-code
	}

	/**
	 * This adds a property descriptor for the Containing Link feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addContainingLinkPropertyDescriptor(Object object) {
		// begin-extension-code
		containingLinkPropertyDescriptor = createItemPropertyDescriptor
		// end-extension-code		
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_ExchangeSpecification_containingLink_feature"), //$NON-NLS-1$
				 getString("_UI_PropertyDescriptor_description", "_UI_ExchangeSpecification_containingLink_feature", "_UI_ExchangeSpecification_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				 FaPackage.Literals.EXCHANGE_SPECIFICATION__CONTAINING_LINK,
				 false,
				 false,
				 false,
				 null,
				 null,
		// begin-extension-code
				 null);
		itemPropertyDescriptors.add(containingLinkPropertyDescriptor);
		// end-extension-code
	}

	/**
	 * This adds a property descriptor for the Link feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addLinkPropertyDescriptor(Object object) {
		// begin-extension-code
		linkPropertyDescriptor = createItemPropertyDescriptor
		// end-extension-code		
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_ExchangeSpecification_link_feature"), //$NON-NLS-1$
				 getString("_UI_PropertyDescriptor_description", "_UI_ExchangeSpecification_link_feature", "_UI_ExchangeSpecification_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				 FaPackage.Literals.EXCHANGE_SPECIFICATION__LINK,
				 true,
				 false,
				 true,
				 null,
				 null,
		// begin-extension-code
				 null);
		itemPropertyDescriptors.add(linkPropertyDescriptor);
		// end-extension-code
	}

	/**
	 * This adds a property descriptor for the Outgoing Exchange Specification Realizations feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addOutgoingExchangeSpecificationRealizationsPropertyDescriptor(Object object) {

		// begin-extension-code
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
		// end-extension-code
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_ExchangeSpecification_outgoingExchangeSpecificationRealizations_feature"), //$NON-NLS-1$
				 getString("_UI_PropertyDescriptor_description", "_UI_ExchangeSpecification_outgoingExchangeSpecificationRealizations_feature", "_UI_ExchangeSpecification_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				 FaPackage.Literals.EXCHANGE_SPECIFICATION__OUTGOING_EXCHANGE_SPECIFICATION_REALIZATIONS,
				 false,
				 false,
				 false,
				 null,
				 null,
		// begin-extension-code
				 null));
		// end-extension-code
	}

	/**
	 * This adds a property descriptor for the Incoming Exchange Specification Realizations feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addIncomingExchangeSpecificationRealizationsPropertyDescriptor(Object object) {

		// begin-extension-code
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
		// end-extension-code
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_ExchangeSpecification_incomingExchangeSpecificationRealizations_feature"), //$NON-NLS-1$
				 getString("_UI_PropertyDescriptor_description", "_UI_ExchangeSpecification_incomingExchangeSpecificationRealizations_feature", "_UI_ExchangeSpecification_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				 FaPackage.Literals.EXCHANGE_SPECIFICATION__INCOMING_EXCHANGE_SPECIFICATION_REALIZATIONS,
				 false,
				 false,
				 false,
				 null,
				 null,
		// begin-extension-code
				 null));
		// end-extension-code
	}

	/**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getText(Object object) {
	 String[] result = new String[] { null };

    	//begin-capella-code
		String label = ((ExchangeSpecification)object).getName();
		//end-capella-code
	  
	
			result[0] = label == null || label.length() == 0 ?
			//begin-capella-code
			"[" + getString("_UI_ExchangeSpecification_type") + "]" : label; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			//end-capella-code

		return result[0];

	}

	/**
	 * This handles model notifications by calling {@link #updateChildren} to update any cached
	 * children and by creating a viewer notification, which it passes to {@link #fireNotifyChanged}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void notifyChanged(Notification notification) {
		updateChildren(notification);
		super.notifyChanged(notification);
	}

	/**
	 * This adds {@link org.eclipse.emf.edit.command.CommandParameter}s describing the children
	 * that can be created under this object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected void collectNewChildDescriptors(Collection<Object> newChildDescriptors, Object object) {
		super.collectNewChildDescriptors(newChildDescriptors, object);
	}

	// begin-capella-code
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected Command createInitializeCopyCommand(EditingDomain domain, EObject owner, Helper helper) {
		return new SharedInitializeCopyCommand(domain, owner, helper);
	}
	// end-capella-code
}

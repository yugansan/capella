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
package org.polarsys.capella.core.re.handlers.location;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.polarsys.capella.core.data.capellacommon.StateEvent;
import org.polarsys.capella.core.data.capellacore.Structure;
import org.polarsys.capella.core.data.cs.AbstractActor;
import org.polarsys.capella.core.data.cs.BlockArchitecture;
import org.polarsys.capella.core.data.cs.Component;
import org.polarsys.capella.core.data.cs.CsPackage;
import org.polarsys.capella.core.data.cs.Interface;
import org.polarsys.capella.core.data.cs.InterfacePkg;
import org.polarsys.capella.core.data.ctx.ActorPkg;
import org.polarsys.capella.core.data.ctx.Capability;
import org.polarsys.capella.core.data.ctx.CapabilityPkg;
import org.polarsys.capella.core.data.ctx.CtxPackage;
import org.polarsys.capella.core.data.ctx.Mission;
import org.polarsys.capella.core.data.ctx.MissionPkg;
import org.polarsys.capella.core.data.ctx.SystemAnalysis;
import org.polarsys.capella.core.data.ctx.SystemFunction;
import org.polarsys.capella.core.data.fa.AbstractFunction;
import org.polarsys.capella.core.data.information.DataPkg;
import org.polarsys.capella.core.data.information.InformationPackage;
import org.polarsys.capella.core.data.information.KeyPart;
import org.polarsys.capella.core.data.information.Unit;
import org.polarsys.capella.core.data.information.communication.Message;
import org.polarsys.capella.core.data.information.communication.Signal;
import org.polarsys.capella.core.data.information.datatype.DataType;
import org.polarsys.capella.core.data.la.CapabilityRealization;
import org.polarsys.capella.core.data.la.CapabilityRealizationPkg;
import org.polarsys.capella.core.data.la.LaPackage;
import org.polarsys.capella.core.data.la.LogicalActorPkg;
import org.polarsys.capella.core.data.la.LogicalComponent;
import org.polarsys.capella.core.data.la.LogicalFunction;
import org.polarsys.capella.core.data.oa.Entity;
import org.polarsys.capella.core.data.oa.EntityPkg;
import org.polarsys.capella.core.data.oa.OaFactory;
import org.polarsys.capella.core.data.oa.OaPackage;
import org.polarsys.capella.core.data.oa.OperationalActivity;
import org.polarsys.capella.core.data.oa.OperationalAnalysis;
import org.polarsys.capella.core.data.oa.OperationalCapability;
import org.polarsys.capella.core.data.oa.OperationalCapabilityPkg;
import org.polarsys.capella.core.data.oa.Role;
import org.polarsys.capella.core.data.oa.RolePkg;
import org.polarsys.capella.core.data.pa.PaPackage;
import org.polarsys.capella.core.data.pa.PhysicalActorPkg;
import org.polarsys.capella.core.data.pa.PhysicalComponent;
import org.polarsys.capella.core.data.pa.PhysicalFunction;
import org.polarsys.capella.core.model.helpers.BlockArchitectureExt;
import org.polarsys.capella.core.model.helpers.ComponentExt;
import org.polarsys.capella.core.model.helpers.SystemAnalysisExt;
import org.polarsys.capella.core.model.helpers.naming.NamingConstants;

/**
 * This class creates 'packages' for created rpl elements stores the packages in the model.
 * It is guaranteed that only one package is created for multiple invocations
 * with elements that should go to the same package, but this is limited to invocations on a single instance.
 */
public class SpecificPackageSupplierFactory {

  /* this stores already created packages so for each setting only one package per instance is created */
  private final Map<Map.Entry<EObject,EStructuralFeature>, Supplier<EObject>> createdSuppliers = new HashMap<Map.Entry<EObject,EStructuralFeature>,Supplier<EObject>>();

  public Supplier<EObject> getSpecificPackageSupplier(EObject packagedElement) {

    Supplier<EObject> result = null;

    BlockArchitecture block = BlockArchitectureExt.getRootBlockArchitecture(packagedElement);

    if (packagedElement instanceof AbstractFunction) {

      AbstractFunction rootFunction = BlockArchitectureExt.getRootFunction(block, true);

      if (rootFunction instanceof PhysicalFunction) {
        result = getSpecificPackageSupplier(rootFunction, PaPackage.Literals.PHYSICAL_FUNCTION__OWNED_PHYSICAL_FUNCTION_PKGS);
      } else if (rootFunction instanceof LogicalFunction) {
        result = getSpecificPackageSupplier(rootFunction, LaPackage.Literals.LOGICAL_FUNCTION__OWNED_LOGICAL_FUNCTION_PKGS);
      } else if (rootFunction instanceof SystemFunction) {
        result = getSpecificPackageSupplier(rootFunction, CtxPackage.Literals.SYSTEM_FUNCTION__OWNED_SYSTEM_FUNCTION_PKGS);
      } else if (rootFunction instanceof OperationalActivity) {
        result = getSpecificPackageSupplier(rootFunction, OaPackage.Literals.OPERATIONAL_ACTIVITY__OWNED_OPERATIONAL_ACTIVITY_PKGS);
      }

    } else if (packagedElement instanceof AbstractActor) {

      Structure actorPkg = BlockArchitectureExt.getActorPkg(block, true);
      if (actorPkg instanceof ActorPkg) {
        result = getSpecificPackageSupplier(actorPkg, CtxPackage.Literals.ACTOR_PKG__OWNED_ACTOR_PKGS);
      } else if (actorPkg instanceof LogicalActorPkg) {
        result = getSpecificPackageSupplier(actorPkg, LaPackage.Literals.LOGICAL_ACTOR_PKG__OWNED_LOGICAL_ACTOR_PKGS);
      } else if (actorPkg instanceof PhysicalActorPkg) {
        result = getSpecificPackageSupplier(actorPkg, PaPackage.Literals.PHYSICAL_ACTOR_PKG__OWNED_PHYSICAL_ACTOR_PKGS);
      }

    } else if (packagedElement instanceof Component) {
      Component rootComponent = ComponentExt.getRootComponent((Component)packagedElement);
      if (rootComponent instanceof PhysicalComponent) {
        result = getSpecificPackageSupplier(rootComponent, PaPackage.Literals.PHYSICAL_COMPONENT__OWNED_PHYSICAL_COMPONENT_PKGS);
      } else if (rootComponent instanceof LogicalComponent) {
        result = getSpecificPackageSupplier(rootComponent, LaPackage.Literals.LOGICAL_COMPONENT__OWNED_LOGICAL_COMPONENT_PKGS);
      }

    } else if (packagedElement instanceof Interface) {
      InterfacePkg interfacePkg = BlockArchitectureExt.getInterfacePkg(block, true);
      result = getSpecificPackageSupplier(interfacePkg, CsPackage.Literals.INTERFACE_PKG__OWNED_INTERFACE_PKGS);

    } else if (
        packagedElement instanceof org.polarsys.capella.core.data.information.Class
        || packagedElement instanceof DataType
        || packagedElement instanceof org.polarsys.capella.core.data.information.Collection
        || packagedElement instanceof Unit
        || packagedElement instanceof KeyPart
        || packagedElement instanceof StateEvent
        || packagedElement instanceof Signal
        || packagedElement instanceof Exception
        || packagedElement instanceof Message
        ) {

      DataPkg dataPkg = BlockArchitectureExt.getDataPkg(block, true);
      result = getSpecificPackageSupplier(dataPkg, InformationPackage.Literals.DATA_PKG__OWNED_DATA_PKGS);

    } else if (packagedElement instanceof Mission) {
      if (block instanceof SystemAnalysis) {
        MissionPkg missionPkg = SystemAnalysisExt.getMissionPkg((SystemAnalysis) block);
        result = getSpecificPackageSupplier(missionPkg, CtxPackage.Literals.MISSION_PKG__OWNED_MISSION_PKGS);
      }
    } else if (packagedElement instanceof OperationalCapability) {

      OperationalCapabilityPkg pkg = (OperationalCapabilityPkg) BlockArchitectureExt.getAbstractCapabilityPkg(block, true);
      result = getSpecificPackageSupplier(pkg, OaPackage.Literals.OPERATIONAL_CAPABILITY_PKG__OWNED_OPERATIONAL_CAPABILITY_PKGS);

    } else if (packagedElement instanceof Capability) {
      CapabilityPkg pkg = (CapabilityPkg) BlockArchitectureExt.getAbstractCapabilityPkg(block, true);
      result = getSpecificPackageSupplier(pkg, CtxPackage.Literals.CAPABILITY_PKG__OWNED_CAPABILITY_PKGS);

    } else if (packagedElement instanceof CapabilityRealization) {
      CapabilityRealizationPkg pkg = (CapabilityRealizationPkg) BlockArchitectureExt.getAbstractCapabilityPkg(block, true);
      result = getSpecificPackageSupplier(pkg, LaPackage.Literals.CAPABILITY_REALIZATION_PKG__OWNED_CAPABILITY_REALIZATION_PKGS);

    } else if (packagedElement instanceof Entity) {

      if (block instanceof OperationalAnalysis) {

        EntityPkg pkg = ((OperationalAnalysis) block).getOwnedEntityPkg();
        if (pkg == null) {
          // FIXME move this to OperationalAnalysisExt
          pkg = OaFactory.eINSTANCE.createEntityPkg(NamingConstants.CreateOpAnalysisCmd_operationalEntities_pkg_name);
          ((OperationalAnalysis) block).setOwnedEntityPkg(pkg);
        }

        result = getSpecificPackageSupplier(pkg, OaPackage.Literals.ENTITY_PKG__OWNED_ENTITY_PKGS);

      }

    } else if (packagedElement instanceof Role) {

      if (block instanceof OperationalAnalysis) {

        RolePkg pkg = ((OperationalAnalysis) block).getOwnedRolePkg();
        if (pkg == null) {
          // FIXME move this to OperationalAnalysisExt
          pkg = OaFactory.eINSTANCE.createRolePkg(NamingConstants.CreateOpAnalysisCmd_roles_pkg_name);
          ((OperationalAnalysis) block).setOwnedRolePkg(pkg);
        }

        result = getSpecificPackageSupplier(pkg, OaPackage.Literals.ROLE_PKG__OWNED_ROLE_PKGS);

      }
    }

    return result;

  }

  // feature type expected to be must be concrete and isMany must be true
  @SuppressWarnings("unchecked")
  private Supplier<EObject> getSpecificPackageSupplier(EObject container, EReference feature) {

    final Map.Entry<EObject, EStructuralFeature> key = new SimpleImmutableEntry<EObject, EStructuralFeature>(container, feature);
    Supplier<EObject> created = createdSuppliers.get(key);

    if (created == null) {

      created = new Supplier<EObject>() {
        EObject suppliedObject;
        @Override
        public EObject get() {
          if (suppliedObject == null) {
            suppliedObject = EcoreUtil.create(feature.getEReferenceType());
            ((Collection<EObject>) container.eGet(feature)).add(suppliedObject);
          }
          return suppliedObject;
        }
      };

      createdSuppliers.put(key, created);
    }
    return created;
  }

}

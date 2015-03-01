package org.opendolphin.core;

import groovy.lang.Closure;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Created by hendrikebbers on 20.01.15.
 */
public interface Dolphin<A extends Attribute, P extends PresentationModel<A>> {

    boolean add(P model);

    boolean remove(P model);

    A findAttributeById(String id);

    List<A> findAllAttributesByQualifier(String qualifier);

    Set<String> listPresentationModelIds();

    Collection<P> listPresentationModels();

    List<P> findAllPresentationModelsByType(String presentationModelType);

    P getAt(String id);

    P findPresentationModelById(String id);

    void removeModelStoreListener(ModelStoreListener listener);

    void removeModelStoreListener(String presentationModelType, ModelStoreListener listener);

    boolean hasModelStoreListener(ModelStoreListener listener);

    void addModelStoreListener(String presentationModelType, ModelStoreListener listener);

    void addModelStoreListener(String presentationModelType, Closure listener);

    boolean hasModelStoreListener(String presentationModelType, ModelStoreListener listener);

    void addModelStoreListener(ModelStoreListener listener);

    void addModelStoreListener(Closure listener);

    void updateQualifiers(P presentationModel);
}

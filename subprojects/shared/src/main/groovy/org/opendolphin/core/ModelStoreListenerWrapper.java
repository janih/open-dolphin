package org.opendolphin.core;

import org.opendolphin.StringUtil;

public class ModelStoreListenerWrapper<A extends Attribute, P extends PresentationModel<A>> implements ModelStoreListener<A, P> {
    private static final String ANY_PRESENTATION_MODEL_TYPE = "*";
    private final String presentationModelType;
    private final ModelStoreListener delegate;

    ModelStoreListenerWrapper(String presentationModelType, ModelStoreListener<A, P> delegate) {
        this.presentationModelType = !StringUtil.isBlank(presentationModelType) ? presentationModelType : ANY_PRESENTATION_MODEL_TYPE;
        this.delegate = delegate;
    }

    private boolean presentationModelTypeMatches(String presentationModelType) {
        return ANY_PRESENTATION_MODEL_TYPE.equals(this.presentationModelType) || this.presentationModelType.equals(presentationModelType);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (null == o) return false;

        if (o instanceof ModelStoreListenerWrapper) {
            ModelStoreListenerWrapper that = (ModelStoreListenerWrapper) o;
            return delegate.equals(that.delegate) && presentationModelType.equals(that.presentationModelType);
        }
        return false;
    }

    @Override
    public int hashCode() {
        int result = presentationModelType.hashCode();
        result = 31 * result + delegate.hashCode();
        return result;
    }

    @Override
    public void modelStoreChanged(ModelStoreEvent<A, P> event) {
        String pmType = event.getPresentationModel().getPresentationModelType();
        if (presentationModelTypeMatches(pmType)) {
            delegate.modelStoreChanged(event);
        }
    }
}

package org.opendolphin.core.client.comm

import org.opendolphin.core.client.ClientPresentationModel

/**
 * Convenience class for OnFinishedData
 */
abstract public class OnFinishedDataAdapter implements OnFinishedData {
    @Override
    void onFinished(List<ClientPresentationModel> presentationModels) {
            // ignore
    }
    static OnFinishedData withAction (Closure cl) {
        return new OnFinishedDataAdapter() {
            @Override
            void onFinishedData(List<Map> data) {
                cl.call(data)
            }
        }
    }
}

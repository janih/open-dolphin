package org.opendolphin.core.client.comm

class OnFinishedHandlerAdapterTests extends GroovyTestCase{

    void testAdapter() {
        new OnFinishedHandlerAdapter().onFinished([])
        def dataAdapter = new OnFinishedDataAdapter(){
            @Override
            void onFinishedData(List<Map> data) {
                // nothing
            }
        }
        dataAdapter.onFinished([])
        dataAdapter.onFinishedData([])
    }

}

/// <reference path="../../testsuite/tsUnit.ts"/>
/// <reference path="../../js/dolphin/ClientAttribute.ts"/>
/// <reference path="../../js/dolphin/ClientPresentationModel.ts"/>


module opendolphin {

    interface TestEvent {
    }

    export class EventBusTests extends tsUnit.TestClass {

        registeredHandlerShouldGetCalled() {
            var eventBus = new EventBus<TestEvent>();

            var spoofedCalled = false;
            eventBus.onEvent((evt: TestEvent) => {
                spoofedCalled = true;
            } );

            eventBus.trigger({});

            this.isTrue(spoofedCalled);
        }

        checkUnregisterHandler() {
            var eventBus = new EventBus<TestEvent>();

            var callCount = 0;
            var dispose = eventBus.onEvent((evt: TestEvent) => {
                ++callCount;
            } );

            eventBus.trigger({});
            dispose();
            eventBus.trigger({});

            this.areIdentical(1, callCount);
        }

    }
}
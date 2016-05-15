module opendolphin {

    export class EventBus<T> {
        private eventHandlers = [];

        /**
         * Registers eventHandler on this EventBus.
         * The returned function can be used to unregister the handler later on.
         *
         * @param eventHandler
         * @returns {function(): undefined} to unregister eventHandler
         */
        onEvent(eventHandler: (event : T) => void ) : ()=> void {
            this.eventHandlers.push(eventHandler);
            return () => {
                this.unregister(eventHandler);
            }
        }

        /**
         * Unregisters eventHandler from this EventBus.
         * @param eventHandler
        */
        unregister(eventHandler) {
          let idx = this.eventHandlers.indexOf(eventHandler);
          if (idx > -1) {
            this.eventHandlers.splice(idx, 1);
          }
        };

        trigger(event : T ) {
            this.eventHandlers.forEach(handle => handle(event));
        }
    }
}
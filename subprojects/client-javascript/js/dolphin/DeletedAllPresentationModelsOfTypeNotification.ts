/// <reference path="Command.ts" />
module opendolphin {

    export class DeletedAllPresentationModelsOfTypeNotification extends Command {

        className:string;

        constructor(public pmType:string) {
            super();
            this.id = 'DeletedAllPresentationModelsOfType';
            this.className = "org.opendolphin.core.comm.DeletedAllPresentationModelsOfTypeNotification";
        }
    }
}
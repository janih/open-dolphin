/// <reference path="Command.ts" />
module opendolphin {

    export class DeleteAllPresentationModelsOfTypeCommand extends Command {

        className:string;

        constructor(public pmType:string) {
            super();
            this.id = 'DeleteAllPresentationModelsOfType';
            this.className = "org.opendolphin.core.comm.DeleteAllPresentationModelsOfTypeCommand";
        }
    }
}
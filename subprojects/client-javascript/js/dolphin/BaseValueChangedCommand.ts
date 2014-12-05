/// <reference path="Command.ts" />

module opendolphin {

    export class BaseValueChangedCommand extends Command {

        className:string;

        constructor(public attributeId:string) {
            super();
            this.id = 'BaseValueChanged';
            this.className = "org.opendolphin.core.comm.BaseValueChangedCommand";
        }
    }
}
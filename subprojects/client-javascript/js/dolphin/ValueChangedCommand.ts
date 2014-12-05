/// <reference path="Command.ts"/>

module opendolphin {

    export class ValueChangedCommand extends Command{

        className:string;

        constructor(public attributeId:string, public oldValue:any, public newValue:any) {
            super();
            this.id = "ValueChanged";
            this.className ="org.opendolphin.core.comm.ValueChangedCommand";
        }

    }

}
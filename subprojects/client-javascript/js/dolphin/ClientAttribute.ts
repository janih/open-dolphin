/// <reference path="ClientPresentationModel.ts" />
/// <reference path="EventBus.ts" />
/// <reference path="Tag.ts" />
///<reference path="Dispose.ts"/>
///<reference path="Map.ts"/>

module opendolphin {

    export interface ValueChangedEvent {
        oldValue;
        newValue;
    }

    export const enum ValueChangedEventType {
        valueChange,
        qualifierChange,
        dirtyValueChange,
        baseValueChange,
    }

    export class ClientAttribute {
        private static SUPPORTED_VALUE_TYPES:string[] = ["string", "number", "boolean"];
        static clientAttributeInstanceCount : number = 0;
        id                          : string;
        private value               : any;
        private dirty               : boolean = false;
        private baseValue           : any;
        private qualifier           : string;
        private presentationModel   : ClientPresentationModel;
        private eventBuses          : Map<ValueChangedEventType,EventBus<ValueChangedEvent>>;

        constructor(public propertyName:string, qualifier:string, value:any, public tag:string = Tag.value()) {
            this.id = "" + (ClientAttribute.clientAttributeInstanceCount++) + "C";

            this.eventBuses = new Map<ValueChangedEventType, EventBus<ValueChangedEvent>>();
            // Would love to do this in a loop, but TS Enums do not support this :-(
            this.eventBuses.put(ValueChangedEventType.valueChange,      new EventBus());
            this.eventBuses.put(ValueChangedEventType.qualifierChange,  new EventBus());
            this.eventBuses.put(ValueChangedEventType.dirtyValueChange, new EventBus());
            this.eventBuses.put(ValueChangedEventType.baseValueChange,  new EventBus());

            this.setValue(value);
            this.setBaseValue(value);
            this.setQualifier(qualifier);
        }

        /** a copy constructor with new id and no presentation model */
        copy() {
            var result = new ClientAttribute(this.propertyName, this.getQualifier(), this.getValue(), this.tag);
            result.setBaseValue(this.getBaseValue());
            return result;
        }

        isDirty():boolean {
            return this.dirty;
        }

        getBaseValue() {
            return this.baseValue;
        }

        setPresentationModel(presentationModel:ClientPresentationModel) {
            if (this.presentationModel) {
                alert("You can not set a presentation model for an attribute that is already bound.");
            }
            this.presentationModel = presentationModel;
        }

        getPresentationModel():ClientPresentationModel {
            return this.presentationModel;
        }

        getValue():any {
            return this.value;
        }

        setValue(newValue) {
            var verifiedValue = ClientAttribute.checkValue(newValue);
            if (this.value == verifiedValue) return;
            var oldValue = this.value;
            this.value = verifiedValue;
            this.setDirty(this.calculateDirty(this.baseValue, verifiedValue));
            this.eventBus(ValueChangedEventType.valueChange).
                trigger({ 'oldValue': oldValue, 'newValue': verifiedValue });
        }

        private eventBus(eventType: ValueChangedEventType):EventBus<ValueChangedEvent> {
            return this.eventBuses.get(eventType);
        }

        private calculateDirty(baseValue:any, value:any):boolean {
            if (baseValue == null) {
                return value != null;
            } else {
                return baseValue != value;
            }
        }

        updateDirty() {
            this.setDirty(this.calculateDirty(this.baseValue, this.value));
        }

        private setDirty(dirty:boolean) {
            var oldVal = this.dirty;
            this.dirty = dirty;
            this.eventBus(ValueChangedEventType.dirtyValueChange)
                .trigger({ 'oldValue': oldVal, 'newValue': this.dirty });
            if (this.presentationModel) this.presentationModel.updateDirty();
        }

        setQualifier(newQualifier) {
            if (this.qualifier == newQualifier) return;
            var oldQualifier = this.qualifier;
            this.qualifier = newQualifier;
            this.eventBus(ValueChangedEventType.qualifierChange)
                .trigger({ 'oldValue': oldQualifier, 'newValue': newQualifier });
        }

        getQualifier(): string{
            return this.qualifier;
        }

        setBaseValue(baseValue:any) {
            if (this.baseValue == baseValue) return;
            var oldBaseValue = this.baseValue;
            this.baseValue = baseValue;
            this.setDirty(this.calculateDirty(baseValue, this.value));
            this.eventBus(ValueChangedEventType.baseValueChange)
                .trigger({ 'oldValue': oldBaseValue, 'newValue': baseValue });
        }

        rebase() {
            this.setBaseValue(this.value);
            this.setDirty(false); // this is not superfluous!
        }

        reset() {
            this.setValue(this.baseValue);
            this.setDirty(false); // this is not superfluous!
        }

        static checkValue(value:any) : any {
            if (value == null || value == undefined) {
                return null;
            }
            var result = value;
            if (result instanceof String || result instanceof Boolean || result instanceof Number) {
                result = value.valueOf();
            }
            if (result instanceof ClientAttribute) {
                console.log("An Attribute may not itself contain an attribute as a value. Assuming you forgot to call value.")
                result = this.checkValue((<ClientAttribute>value).value);
            }
            var ok:boolean = false;
            if (this.SUPPORTED_VALUE_TYPES.indexOf(typeof result) > -1 || result instanceof Date) {
                ok = true;
            }
            if (!ok) {
                throw new Error("Attribute values of this type are not allowed: " + typeof value);
            }
            return result;
        }

        onValueChange(eventHandler:(event:ValueChangedEvent) => void) : Dispose {
            let disposeFunc = this.eventBus(ValueChangedEventType.valueChange).onEvent(eventHandler);
            eventHandler({"oldValue": this.value, "newValue": this.value});
            return disposeFunc;
        }

        onQualifierChange(eventHandler:(event:ValueChangedEvent) => void) : Dispose {
            return this.eventBus(ValueChangedEventType.qualifierChange).onEvent(eventHandler);
        }

        onDirty(eventHandler:(event:ValueChangedEvent) => void): Dispose {
            return this.eventBus(ValueChangedEventType.dirtyValueChange).onEvent(eventHandler);
        }

        onBaseValueChange(eventHandler:(event:ValueChangedEvent) => void) : Dispose {
            return this.eventBus(ValueChangedEventType.baseValueChange).onEvent(eventHandler);
        }

        syncWith(sourceAttribute:ClientAttribute) {
            if (sourceAttribute) {
                this.setQualifier(sourceAttribute.getQualifier());     // sequence is important
                this.setBaseValue(sourceAttribute.getBaseValue());
                this.setValue(sourceAttribute.value);
                // syncing propertyName and tag is not needed since they must be identical anyway
            }
        }
    }
}
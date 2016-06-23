/// <reference path="../../testsuite/tsUnit.ts"/>
/// <reference path="../../js/dolphin/ClientAttribute.ts"/>
/// <reference path="../../js/dolphin/ClientPresentationModel.ts"/>


module opendolphin {
    export class ClientAttributeTests extends tsUnit.TestClass {

        attributesShouldGetUniqueIds() {
            var ca1 = new ClientAttribute("prop","qual","tag");
            var ca2 = new ClientAttribute("prop","qual","tag");
            this.areNotIdentical(ca1.id, ca2.id);
        }

        valueChangeListenersGetDisposed() {
            var attr = new ClientAttribute("prop", "qual", 0);
            var callCount = -1; // Should be zero to begin with, but onValueChange does trigger event on registration!

            var dispose = attr.onValueChange( (evt: ValueChangedEvent) => {
                ++callCount;
            } );

            dispose();
            attr.setValue(1);

            this.areIdentical(0, callCount);
        }

        valueChangeListenersAreCalled() {
            var attr = new ClientAttribute("prop", "qual", 0);

            var spoofedOld = -1;
            var spoofedNew = -1;
            attr.onValueChange( (evt: ValueChangedEvent) => {
                spoofedOld = evt.oldValue;
                spoofedNew = evt.newValue;
            } )

            this.areIdentical(spoofedOld, 0)
            this.areIdentical(spoofedNew, 0)

            attr.setValue(1);

            this.areIdentical(spoofedOld, 0)
            this.areIdentical(spoofedNew, 1)

        }

        qualifierChangeListenersGetDisposed() {
            var attr = new ClientAttribute("prop", "qual", 0);
            var called = false;
            var dispose = attr.onQualifierChange((evt:ValueChangedEvent) => {
                called = true;
            })

            dispose();
            attr.setQualifier("qual_change");

            this.isFalse(called);
        }

        qualifierChangeListenersAreCalled() {
            var attr = new ClientAttribute("prop", "qual", 0);

            var spoofedOldQfr;
            var spoofedNewQfr;
            attr.onQualifierChange((evt:ValueChangedEvent) => {
                spoofedOldQfr = evt.oldValue;
                spoofedNewQfr = evt.newValue;
            })
            attr.setQualifier("qual_change");

            this.areIdentical(spoofedOldQfr, "qual")
            this.areIdentical(spoofedNewQfr, "qual_change")
        }

        baseValueChangeListenersGetDisposed() {
            var attr = new ClientAttribute("prop", "qual", "base");

            var called = false;
            var dispose = attr.onBaseValueChange((evt:ValueChangedEvent) => {
                called = true;
            })
            dispose();

            attr.setBaseValue("baseValue change");

            this.isFalse(called);
        }

        baseValueChangeListenersAreCalled() {
            var attr = new ClientAttribute("prop", "qual", "base");

            var spoofedOldBaseValue = "";
            var spoofedNewBaseValue = "";

            attr.onBaseValueChange((evt:ValueChangedEvent) => {
                spoofedOldBaseValue = evt.oldValue;
                spoofedNewBaseValue = evt.newValue;
            })

            attr.setBaseValue("baseValue change");

            this.areIdentical(spoofedOldBaseValue, "base")
            this.areIdentical(spoofedNewBaseValue, "baseValue change")
        }

        valueChangeListenersDoNotInterfere() {
            var attr1 = new ClientAttribute("prop", "qual1", 0);
            var attr2 = new ClientAttribute("prop", "qual2", 0);

            var spoofedNew1 = -1;
            attr1.onValueChange( (evt: ValueChangedEvent) => {
                spoofedNew1 = evt.newValue;
            } )
            attr1.setValue(1);

            var spoofedNew2 = -1;
            attr2.onValueChange( (evt: ValueChangedEvent) => {
                spoofedNew2 = evt.newValue;
            } )
            attr2.setValue(2);

            this.areIdentical(spoofedNew1, 1)
            this.areIdentical(spoofedNew2, 2)

        }

        dirtyChangeListenersGetDisposed() {
            var attr = new ClientAttribute("prop", "qual1", 0);

            var called = false;
            var dispose = attr.onDirty((evt:ValueChangedEvent) => {
                called = true;
            });
            dispose();
            attr.setValue(1);

            this.isFalse(called);
        }

        checkDirtyChangeListener() {
            var attr = new ClientAttribute("prop", "qual1", false);

            var dirtyFirst = false;
            attr.onDirty((evt:ValueChangedEvent) => {
                dirtyFirst = evt.newValue;
            });

            var dirtySecond = false;
            attr.onDirty((evt:ValueChangedEvent) => {
                dirtySecond = evt.newValue;
            });

            attr.setValue(true);
            this.areIdentical(dirtyFirst, dirtySecond);
        }

        checkDirtyWhenValueAndBaseValueAreUndefinedOrNull() {
            var attr = new ClientAttribute("prop", "qual1", 0);

            var dirtyValue = false;
            attr.onDirty((evt:ValueChangedEvent) => {
                dirtyValue = evt.newValue;
            });
            // value and baseValue are undefined
            this.isFalse(attr.isDirty());

            // value and baseValue are null
            attr.setValue(null);
            this.isTrue(attr.isDirty());
            this.areIdentical(true, dirtyValue);
        }

        checkDirtyWhenValueAndBaseValueAreDifferent() {
            var attr = new ClientAttribute("prop", "qual1", 0);

            var dirtyValue = false;
            attr.onDirty((evt:ValueChangedEvent) => {
                dirtyValue = evt.newValue;
            });
            // value and baseValue are different
            attr.setValue(5);
            this.isTrue(attr.isDirty());
            this.areIdentical(true, dirtyValue);

        }

        checkDirtyAfterRebase() {
            var attr = new ClientAttribute("prop", "qual1", 0);
            attr.setValue(5);
            attr.rebase();// Make base value 5
            this.isFalse(attr.isDirty());
        }

        checkValue() {
            //valid values
            this.areIdentical(5, ClientAttribute.checkValue(5));
            this.areIdentical(0, ClientAttribute.checkValue(0));
            this.areIdentical("test", ClientAttribute.checkValue("test"));

            var date = new Date();
            this.areIdentical(date,ClientAttribute.checkValue(date));

            var attr = new ClientAttribute("prop", "qual1", 0);
            attr.setValue(15);
            this.areIdentical(15, ClientAttribute.checkValue(attr));

            //Wrapper classes
            this.areIdentical("test", ClientAttribute.checkValue(new String("test")));
            this.areIdentical(false, ClientAttribute.checkValue(new Boolean(false)));
            this.areIdentical(15, ClientAttribute.checkValue(new Number(15)));

            //invalid values
            this.areIdentical(null, ClientAttribute.checkValue(null));
            this.areIdentical(null, ClientAttribute.checkValue(undefined)); // null is treated as undefined
            try {
                ClientAttribute.checkValue(new ClientPresentationModel(undefined, "type"))
                this.fail()
            } catch (error) {
                this.isTrue(error instanceof Error);
            }
        }

        simpleCopy() {
            var ca1 = new ClientAttribute("prop","qual","tag");
            var ca2 = ca1.copy();

            this.areNotIdentical(ca1.id, ca2.id); // id must not be copied
            this.areIdentical(undefined, ca2.getPresentationModel()); // no pm must be set

            this.areIdentical(ca1.getBaseValue(), ca2.getBaseValue());
            this.areIdentical(ca1.getValue(),     ca2.getValue());
            this.areIdentical(ca1.getQualifier(), ca2.getQualifier());
            this.areIdentical(ca1.tag,            ca2.tag);          // todo dk: for consistency, there should be getTag()
            this.areIdentical(ca1.propertyName,   ca2.propertyName); // todo dk: for consistency, there should be getPropertyName()
        }


    }
}
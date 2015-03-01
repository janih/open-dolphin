package org.opendolphin.core

import spock.lang.Specification

class DolphinTest extends Specification {

    def "finding all attributes per qualifier must delegate to the model store"() {
        given:
        def attributeMock = Mock(Attribute)
        def storeStub = new ModelStore() {
            @Override
            List<Attribute> findAllAttributesByQualifier(String qualifier) {
                return [attributeMock]
            }
        }
        def dolphin = new AbstractDolphin() {
            @Override
            protected ModelStore getModelStore() {
                return storeStub
            }
        }
        when:
        def result = dolphin.findAllAttributesByQualifier("whatever")

        then:
        result.size() == 1
        result[0] == attributeMock
    }
}

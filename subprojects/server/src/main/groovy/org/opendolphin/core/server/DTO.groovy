package org.opendolphin.core.server

import groovy.transform.CompileStatic

//CompileStatic
/**
 * A DTO is a <a href="http://en.wikipedia.org/wiki/Data_transfer_object">data transfer object</a>, used to
 * transfer the contents of a presentation model in a single Dolphin data command.
 * <p/>
 * A DTO consists of a list of Slot objects, where each slot corresponds to an attribute in the presentation model.
 * A slot consists of a propertyName (String), a value, a baseValue, a qualifier, and a tag.
 * @see Slot
 */
final class DTO {
    List<Slot> slots

    DTO(List<Slot> newSlots) {
        slots = newSlots
    }

    DTO(Slot... newSlots) {
        slots = newSlots as LinkedList

    }

    /**
     * Create the representation that is used within commands.
     */
    List<Map<String, Object>> encodable() {
        (List<Map<String, Object>>) slots.collect(new LinkedList()) {Slot slot -> slot.toMap() }
    }

}

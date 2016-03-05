package org.opendolphin.core.server

import org.opendolphin.core.Tag
import groovy.transform.CompileStatic

// should be immutable?
//CompileStatic
/**
 * A Slot corresponds to an attribute in the presentation model.
 * A slot consists of a propertyName (String), a value, a baseValue, a qualifier, and a tag.
 * A DTO (data transfer object) consists of a list of slots; the DTO is the equivalent of a presentation model.
 * @see DTO
 */
final class Slot {
    String propertyName
    Object value
    Object baseValue
    String qualifier
    Tag    tag

    /**
     * Convenience method with positional parameters to create an attribute specification from name/value pairs.
     * Especially useful when creating DTO objects.
     */
    Slot (String propertyName, Object value, String qualifier = null, Tag tag = Tag.VALUE) {
        this.propertyName = propertyName
        this.value        = value
        this.baseValue    = value
        this.qualifier    = qualifier
        this.tag          = tag
    }

    /**
     * Converts a data map like <tt>[a:1, b:2]</tt> into a list of attribute-Maps.
     * Especially useful when a service returns data that an action puts into presentation models.
     */
    static List<Slot> slots(Map<String, Object> data) {
        (List<Slot>) data.collect(new LinkedList()) { String key, Object value -> new Slot(key, value) }
    }


    Map<String, Object> toMap() {
        [propertyName: propertyName, value: value, baseValue: baseValue, qualifier: qualifier, tag:tag]
    }

}

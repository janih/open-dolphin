package org.opendolphin.core

/**
 * Tags are used to differentiate various data and metadata associated with an Attribute.
 * Every attribute has a tag; by default, the VALUE tag is used.
 * <p/>
 * By creating multiple attributes with the same property name but different tags, any amount of metadata can be kept
 * for the property.  This metadata can consist of any additional information that the view layer might find useful,
 * for example, the localized label to be displayed next to the property, the tooltip for the property, validation
 * criteria for the property, whether the UI control for the property should be visible or enabled, and so on.
 * <p/>
 * Note that Dolphin itself does not take any special action based on the tag type, except to assume a tag type of VALUE
 * if none is otherwise specified.  The view code must supply the logic to enforce tag meanings.
 * Therefore, additional tags may be created as needed with no change to Dolphin itself.
 * The UI toolkit can bind against these other tag attributes just as it can against the "value" attribute.
 * <p/>
 * Tags are essentially statically typed Strings, and you can make your own by using the "tagFor" factory or
 * by subclassing.
 * <p/>
 * Several tags are predeclared; see documentation for suggested usage.
 * @see Attribute
 */
final class Tag {

    final String name;

    protected Tag(String name) { this.name = name }

    public final String toString() { name }

    /** Factory method with flyweight pattern */
    public static final Map<String,Tag> tagFor = [:].withDefault { String key -> new Tag(key) }

    /** The actual value of the attribute. This is the default if no tag is given.*/
    public static final Tag VALUE = tagFor["VALUE"]

    /** the to-be-displayed String, not the key. Internationalization happens on the server. */
    public static final Tag LABEL = tagFor["LABEL"]

    /** a single text; e.g. "textArea" if the String value should be displayed in a text area instead of a textField */
    public static final Tag WIDGET_HINT = tagFor["WIDGET_HINT"]

    /** a single text; e.g. "java.util.Date" if the value String represents a date */
    public static final Tag VALUE_TYPE = tagFor["VALUE_TYPE"]

    /** regular expression for local, syntactical constraints like in "rejectField" */
    public static final Tag REGEX = tagFor["REGEX"]

    /** Url.toExternalForm()*/
    public static final Tag HELP_URL = tagFor["HELP_URL"]

    /** Tooltip text for the attribute. */
    public static final Tag TOOLTIP = tagFor["TOOLTIP"]

    /** "true" or "false"; maps to Grails constraint nullable:false */
    public static final Tag MANDATORY = tagFor["MANDATORY"]

    /** "true" or "false"; maps to Grails constraint display:true */
    public static final Tag VISIBLE = tagFor["VISIBLE"]

    /** "true" or "false" */
    public static final Tag ENABLED = tagFor["ENABLED"]
}

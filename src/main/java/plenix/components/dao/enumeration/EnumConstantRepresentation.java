package plenix.components.dao.enumeration;

public @interface EnumConstantRepresentation {
    String name() default "";

    String value();
}

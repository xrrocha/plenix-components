package plenix.components.dao.enumeration;

@GenerateEnumerationHibernateUserType
public enum Gender {
    @EnumConstantRepresentation(name = "male", value = "M")
    MALE,
    @EnumConstantRepresentation(name = "female", value = "F")
    FEMALE
}

package plenix.components.dao.enumeration;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import java.util.*;

public class EnumRepresentationProcessor extends AbstractProcessor {
    private static final Set<String> supportedAnnotationTypes = new HashSet<>(Arrays.asList(
            "plenix.components.dao.enumeration.GenerateEnumerationHibernateUserType",
            "plenix.components.dao.enumeration.EnumConstantRepresentations",
            "plenix.components.dao.enumeration.EnumConstantRepresentation"));

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return supportedAnnotationTypes;
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> annotatedElements =
                roundEnv.getElementsAnnotatedWith(GenerateEnumerationHibernateUserType.class);
        for (Element annotatedElement : annotatedElements) {
            if (annotatedElement.getKind() == ElementKind.ENUM) {
                try {
                    TypeElement classElement = (TypeElement) annotatedElement;
                    PackageElement packageElement = (PackageElement) classElement.getEnclosingElement();
                } catch (Exception ioe) {
                    throw new RuntimeException(ioe);
                }
            }
        }
        return false;
    }

    private List<String> getEnumConstants(TypeElement classElement) {
        List<String> enumConstants = new ArrayList<>();
        for (Element enclosedElement : classElement.getEnclosedElements()) {
            if (enclosedElement.getKind() == ElementKind.ENUM_CONSTANT) {
                enumConstants.add(enclosedElement.getSimpleName().toString());
            }
        }
        return enumConstants;
    }
}

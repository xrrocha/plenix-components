package plenix.components.dao.enumeration;

import com.sun.mirror.apt.*;
import com.sun.mirror.declaration.AnnotationTypeDeclaration;
import com.sun.mirror.declaration.EnumDeclaration;
import com.sun.mirror.declaration.TypeDeclaration;
import com.sun.mirror.declaration.EnumConstantDeclaration;
import com.sun.mirror.util.DeclarationVisitors;
import com.sun.mirror.util.SimpleDeclarationVisitor;

import java.util.*;
import java.io.PrintWriter;
import java.io.IOException;

import plenix.components.dao.enumeration.EnumConstantRepresentation;
import plenix.components.dao.enumeration.EnumConstantRepresentations;
import plenix.components.dao.enumeration.EnumConstantRepresentation;
import plenix.components.dao.enumeration.EnumConstantRepresentations;

public class GenerateEnumerationHibernateUserTypeProcessorFactory implements AnnotationProcessorFactory {
    private Collection<String> supportedAnnotationTypes = Arrays.asList(
                    "plenix.components.dao.enumeration.GenerateEnumerationHibernateUserType",
                    "plenix.components.dao.enumeration.EnumConstantRepresentations",
                    "plenix.components.dao.enumeration.EnumConstantRepresentation");

    private static final String DESTINATION_PACKAGE_OPTION = "-AdestinationPackage";
    private Collection<String> supportedOptions = Arrays.asList(DESTINATION_PACKAGE_OPTION);

    public Collection<String> supportedAnnotationTypes() {
        return supportedAnnotationTypes;
    }

    public Collection<String> supportedOptions() {
        return supportedOptions;
    }

    public AnnotationProcessor getProcessorFor(Set<AnnotationTypeDeclaration> atds,
                                               AnnotationProcessorEnvironment env)
    {
        if (atds.isEmpty()) {
            return AnnotationProcessors.NO_OP;
        }
        return new EnumRepresentationProcessor(env);
    }

    public static class EnumRepresentationProcessor implements AnnotationProcessor {
        private AnnotationProcessorEnvironment env;
        public EnumRepresentationProcessor(AnnotationProcessorEnvironment env) {
            this.env = env;
        }

        // TODO: Check for duplicate constant representations!
        // TODO: Support constant representation types other than VARCHAR
        // TODO: Support multi-column constant representations
        public void process() {
            for (TypeDeclaration typeDeclaration: env.getSpecifiedTypeDeclarations()) {
                typeDeclaration.accept(
                        DeclarationVisitors.getDeclarationScanner(new EnumRepresentationVisitor(),
                        DeclarationVisitors.NO_OP));
            }
        }

        private class EnumRepresentationVisitor extends SimpleDeclarationVisitor {
            public void visitEnumDeclaration(EnumDeclaration ed) {
                GenerateEnumerationHibernateUserType gehuts = (GenerateEnumerationHibernateUserType) ed.getAnnotation(GenerateEnumerationHibernateUserType.class);
                if (gehuts != null) { // @GenerateEnumerationUserType present
                    String packageName = ed.getPackage().getQualifiedName();

                    String destinationPackage = getOptionValueFor(DESTINATION_PACKAGE_OPTION);
                    if (destinationPackage == null) {
                        destinationPackage = packageName;
                    }

                    EnumConstantDeclaration[] ecds = ed.getEnumConstants().toArray(new EnumConstantDeclaration[0]);

                    if (gehuts.value().length == 1 && "".equals(gehuts.value()[0])) {
                        EnumConstantRepresentation[] ecrs = new EnumConstantRepresentation[ecds.length];
                        for (int i = 0; i < ecrs.length; i++) {
                            ecrs[i] = ecds[i].getAnnotation(EnumConstantRepresentation.class);
                            if (ecrs[i] == null) {
                                throw new IllegalStateException(ed.getQualifiedName() + ": No representation specified for enumeration constant " + ecds[i].getConstantValue());
                            }
                        }
                        generateSourceFile(destinationPackage, ed.getQualifiedName(), ed.getSimpleName(), "", ecds, ecrs);
                    } else {
                        for (String representationName: gehuts.value()) {
                            int cnt = 0;
                            EnumConstantRepresentation[] constants = new EnumConstantRepresentation[ecds.length];
                            for (EnumConstantDeclaration ecd: ecds) {
                                EnumConstantRepresentations ecrs = (EnumConstantRepresentations) ecd.getAnnotation(EnumConstantRepresentations.class);
                                if (ecrs == null) {
                                    throw new IllegalStateException(ed.getQualifiedName() + ": No representations specified for enumeration constant " + ecd.getConstantValue());
                                }

                                for (EnumConstantRepresentation ecr: ecrs.value()) {
                                    if (ecr.name().equals(representationName)) {
                                        constants[cnt++] = ecr;
                                    }
                                }
                            }
                            if (cnt != ecds.length) {
                                throw new IllegalStateException(ed.getQualifiedName() + ": One or more enumeration constants lack representation (" + cnt + ")");
                            }
                            generateSourceFile(destinationPackage, ed.getQualifiedName(), ed.getSimpleName(), representationName, ecds, constants);
                        }
                    }
                }
            }

            private void generateSourceFile(String packageName,
                                            String enumFqn,
                                            String enumName,
                                            String reprName,
                                            EnumConstantDeclaration[] ecds,
                                            EnumConstantRepresentation[] ecrs)
            {
                try {
                    Filer filer = env.getFiler();
                    String className = enumName + reprName + "UserType";
                    PrintWriter out = filer.createSourceFile(packageName + "." + className);

                    out.println("package " + packageName + ";");
                    out.println("import java.util.HashMap;");
                    out.println("import java.util.Map;");
                    out.println("import plenix.components.dao.enumeration.EnumHibernateUserType;");
                    out.println("import " + enumFqn + ";");
                    out.println("import static " + enumFqn + ".*;");
                    out.println("// Automatically generated from annotations in " + enumName + ". Do  not edit");
                    out.println("public class " + className + " extends EnumHibernateUserType {");
                    out.println("    private static final Map<String, Object> values = new HashMap<String, Object>();");
                    out.println("    static {");
                    for (int i = 0; i < ecds.length; i++) {
                        out.println("        values.put(\"" + ecrs[i].value() + "\", " + ecds[i].getSimpleName() + ");");
                    }
                    out.println("    }");
                    out.println("    protected String getName() { return \"" + enumName + "\"; }");
                    out.println("    protected Class getReturnedClass() { return " + enumName + ".class; }");
                    out.println("    protected Map<String, Object> getValues() { return values; }");
                    out.println("}");

                    out.flush();
                    out.close();
                } catch (IOException e) {
                    throw new RuntimeException("Exception while generating enumeration user name source file: " + e.getMessage(), e);
                }
            }

            // FIXME: Apparently, there's a bug in apt that returns option keys along with option values
            private String getOptionValueFor(String option) {
                if (env.getOptions().containsKey(option)) {
                    return env.getOptions().get(option);
                }
                int length = option.length();
                for (String optionKey: env.getOptions().keySet()) {
                    if (optionKey.startsWith(option) && optionKey.charAt(length) == '=') {
                        StringTokenizer st = new StringTokenizer(optionKey, "=");
                        st.nextToken();
                        return st.nextToken();
                    }
                }
                return null;
            }
        }
    }
}

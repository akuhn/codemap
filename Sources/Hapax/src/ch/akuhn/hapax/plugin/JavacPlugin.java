package ch.akuhn.hapax.plugin;

import java.io.IOException;
import java.io.Writer;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ElementVisitor;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.StandardLocation;

import ch.akuhn.hapax.corpus.CamelCaseScanner;
import ch.akuhn.hapax.corpus.Terms;

import com.sun.tools.javac.code.Symbol.MethodSymbol;

@SupportedAnnotationTypes("*")
@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class JavacPlugin extends AbstractProcessor {

    private Terms executableTerms = new Terms();
    private Terms packageTerms = new Terms();
    private Terms typeTerms = new Terms();
    private Terms typeParameterTerms = new Terms();
    private Terms variableTerms = new Terms();
    
    
    private class Visitor implements ElementVisitor<Void, Void> {

        //@Override
        public Void visit(Element e) {
            System.out.println("visit\t" + e);
            return null;
        }

        //@Override
        public Void visit(Element e, Void p) {
            System.out.println("visit\t" + e);
            return null;
        }

        //@Override
        public Void visitExecutable(ExecutableElement e, Void p) {
            new CamelCaseScanner().client(executableTerms).onString(e.getSimpleName()).run();
            for (Element each: e.getEnclosedElements()) each.accept(this, null);
            for (Element each: e.getParameters()) each.accept(this, null);
            for (Element each: e.getTypeParameters()) each.accept(this, null);
            return null;
        }

        //@Override
        public Void visitPackage(PackageElement e, Void p) {
            new CamelCaseScanner().client(packageTerms).onString(e.getSimpleName()).run();
            for (Element each: e.getEnclosedElements()) each.accept(this, null);
            return null;
        }

        //@Override
        public Void visitType(TypeElement e, Void p) {
            new CamelCaseScanner().client(typeTerms).onString(e.getSimpleName()).run();
            for (Element each: e.getEnclosedElements()) each.accept(this, null);
            for (Element each: e.getTypeParameters()) each.accept(this, null);
            return null;
        }

        //@Override
        public Void visitTypeParameter(TypeParameterElement e, Void p) {
            new CamelCaseScanner().client(typeParameterTerms).onString(e.getSimpleName()).run();
            //for (Element each: e.getEnclosedElements()) each.accept(this, null);
            return null;
        }

        //@Override
        public Void visitUnknown(Element e, Void p) {
            System.out.println("visitUnknown\t" + e);
            return null;
        }

        //@Override
        public Void visitVariable(VariableElement e, Void p) {
            new CamelCaseScanner().client(variableTerms).onString(e.getSimpleName()).run();
            for (Element each: e.getEnclosedElements()) each.accept(this, null);
            return null;
        }
        
    }
    
    private Filer filer;
    private Elements elements;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        filer = processingEnv.getFiler();
        elements = processingEnv.getElementUtils();
        super.init(processingEnv);
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean process(Set annotations, RoundEnvironment roundEnv) {
        if (!roundEnv.processingOver()) {
            proccessingRound(roundEnv);
        } else {
            processingDone();
        }
        return false;
    }

    private void processingDone() {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "Done.");
        typeTerms.toLowerCase().storeOn(System.out);
        System.out.println(executableTerms.toLowerCase());
        // processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE,
        // catched.sum() +
        // " catched exception types logged, see `catch.txt`.");
        // fileOut("throw.txt", thrown.toString());
        // fileOut("catch.txt", catched.toString());
    }

    private void proccessingRound(RoundEnvironment roundEnv) {
        Set<PackageElement> packages = new HashSet<PackageElement>();
        for (Element each: roundEnv.getRootElements()) {
            packages.add(elements.getPackageOf(each));
        }
        for (PackageElement each: packages) {
            each.accept(new Visitor(), null);
        }
    }

    @SuppressWarnings("unused")
    private void fileOut(String filename, String content) {
        try {
            FileObject file = filer.createResource(StandardLocation.CLASS_OUTPUT, "", filename, (Element[]) null);
            Writer writer = file.openWriter();
            writer.append(content);
            writer.close();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

}

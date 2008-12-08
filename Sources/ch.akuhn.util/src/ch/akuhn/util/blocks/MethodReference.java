/**
 * 
 */
package ch.akuhn.util.blocks;

import static java.lang.Character.isJavaIdentifierPart;
import static java.lang.Character.isJavaIdentifierStart;
import static java.lang.Character.isWhitespace;

import java.lang.reflect.Method;
import java.nio.CharBuffer;
import java.util.ArrayList;

import ch.akuhn.util.Separator;
import ch.akuhn.util.Throw;

@SuppressWarnings("unchecked")
public class MethodReference {

    public static class InvalidMethodReference extends RuntimeException {

        private static String message(CharBuffer buf) {
            String message = buf.hasRemaining() ? "Illegal charater at %d." : "Unexpected end of declaration.";
            return String.format(message, buf.position());
        }
        public final int position;

        public final String string;

        public InvalidMethodReference(CharBuffer buf) {
            super(message(buf));
            this.position = buf.position();
            this.string = buf.rewind().toString();
        }

    }
    private static class Parser {

        public String[] args = null;

        private CharBuffer buf;
        public String path, simple = null;

        private Parser(String string) {
            buf = CharBuffer.wrap(string);
        }

        private InvalidMethodReference error() {
            return new InvalidMethodReference(buf);
        }

        private String scanFullname() {
            buf.mark();
            while (true) {
                if (!buf.hasRemaining()) return null;
                if (!isJavaIdentifierStart(buf.charAt(0))) return null;
                buf.get();
                while (buf.hasRemaining() && isJavaIdentifierPart(buf.charAt(0)))
                    buf.get();
                if (!(buf.hasRemaining() && buf.charAt(0) == '.')) break;
                buf.get();
            }
            return yank();
        }

        public void scanMethodReference() {
            path = scanFullname();
            if (buf.hasRemaining() && buf.charAt(0) == '#') {
                buf.get();
                simple = scanName();
                if (simple == null) throw error();
            } else { // be lenient, Javadoc does the same!
                if (path == null) throw error();
                int n = path.lastIndexOf('.');
                simple = n < 0 ? path : path.substring(n + 1);
                path = n < 0 ? null : path.substring(0, n);
            }
            skipWhitespace();
            if (buf.hasRemaining() && buf.charAt(0) == '(') {
                buf.get();
                skipWhitespace();
                args = scanParamlist();
                if (!(buf.hasRemaining() && buf.charAt(0) == ')')) throw error();
                buf.get();
                if (args == null) args = new String[0];
            }
        }

        private String scanName() {
            buf.mark();
            if (!buf.hasRemaining()) return null;
            if (!isJavaIdentifierStart(buf.charAt(0))) return null;
            buf.get();
            while (buf.hasRemaining() && isJavaIdentifierPart(buf.charAt(0)))
                buf.get();
            return yank();
        }

        private String[] scanParamlist() {
            ArrayList<String> $ = new ArrayList();
            while (true) {
                String param = scanFullname();
                if (param == null) return null;
                $.add(param);
                skipWhitespace();
                if (!(buf.hasRemaining() && buf.charAt(0) == ',')) break;
                buf.get();
                skipWhitespace();
            }
            return $.toArray(new String[$.size()]);
        }

        private void skipWhitespace() {
            while (buf.hasRemaining() && isWhitespace(buf.charAt(0)))
                buf.get();
        }

        private String yank() {
            int pos = buf.position();
            buf.reset();
            String $ = buf.subSequence(0, pos - buf.position()).toString();
            buf.position(pos);
            return $;
        }

    }
    private static class Resolver {

        private Class context;

        public Resolver(Class context) {
            this.context = context;
        }

        public Method get(String path, String simple, String[] args) {
            try {
                return resolveMethod(resolveClass(path), simple, args);
            } catch (Exception ex) {
                throw Throw.exception(ex);
            }
        }

        private Class[] getParameterClasses(String[] args) throws ClassNotFoundException {
            Class[] array = new Class[args.length];
            int index = 0;
            for (String name : args) {
                Class c = resolveClass(name);
                if (c == null) {
                    c = resolveClass("java.lang." + name);
                    if (c == null) {
                        if (name.equals("int")) {
                            c = int.class;
                        } else if (name.equals("long")) {
                            c = long.class;
                        } else if (name.equals("double")) {
                            c = double.class;
                        } else if (name.equals("float")) {
                            c = float.class;
                        } else if (name.equals("char")) {
                            c = char.class;
                        } else if (name.equals("boolean")) {
                            c = boolean.class;
                        } else {
                            throw new ClassNotFoundException(name);
                        }
                    }
                }
                array[index++] = c;
            }
            return array;
        }

        private Method resolveAmbigousMethod(Class jclass, String simple) throws NoSuchMethodException {
            Method found = null;
            for (Method m : jclass.getMethods()) {
                if (m.getName().equals(simple)) {
                    if (found != null) throw new NoSuchMethodException("Ambigous reference, please specify parameters");
                    found = m;
                }
            }
            if (found == null) throw new NoSuchMethodException(this.toString());
            return found;
        }

        private Class resolveClass(String path) throws ClassNotFoundException {
            if (path == null) return context;
            String name = path;
            Class $ = Methods.findClass(name);
            if ($ != null) return $;
            name = context.getName() + "$" + path;
            $ = Methods.findClass(name);
            if ($ != null) return $;
            name = context.getPackage().getName() + "." + path;
            $ = Methods.findClass(name);
            if ($ != null) return $;
            throw new ClassNotFoundException(path);
        }

        private Method resolveMethod(Class jclass, String simple, String[] args) throws Exception {
            return args == null ? this.resolveAmbigousMethod(jclass, simple) : jclass.getMethod(simple, this
                    .getParameterClasses(args));
        }

    }

    private final String[] args;

    private final String path;

    private final String simple;

    public MethodReference(String reference) {
        Parser p = new Parser(reference);
        p.scanMethodReference();
        this.path = p.path;
        this.simple = p.simple;
        this.args = p.args;
    }

    public Method resolve(Class context) {
        return new Resolver(context).get(path, simple, args);
    }

    @Override
    public String toString() {
        StringBuilder $ = new StringBuilder();
        if (path != null) $.append(path);
        $.append('#').append(simple);
        if (args != null) {
            $.append('(');
            Separator s = new Separator(",");
            for (String each : args)
                $.append(s).append(each);
            $.append(')');
        }
        return $.toString();
    }

}
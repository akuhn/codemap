//  Copyright (c) 2008 Adrian Kuhn <akuhn(a)iam.unibe.ch>
//  
//  This file is part of Jamix.
//  
//  Jamix is free software: you can redistribute it and/or modify it under the
//  terms of the GNU Affero General Public License as published by the Free
//  Software Foundation, either version 3 of the License, or (at your option)
//  any later version.
//  
//  Jamix is distributed in the hope that it will be useful, but WITHOUT ANY
//  WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
//  FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more
//  details.
//  
//  You should have received a copy of the GNU Affero General Public License
//  along with Jamix. If not, see <http://www.gnu.org/licenses/>.
//  
package example;

import java.io.File;
import java.util.List;

import javax.tools.JavaCompiler;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import javax.tools.JavaCompiler.CompilationTask;

import ch.akuhn.hapax.plugin.JavacPlugin;
import ch.akuhn.util.As;
import ch.akuhn.util.Files;
import ch.akuhn.util.Strings;

public class PluginRunner implements Runnable {

    public static void main(String[] args) {
        Runnable self = new PluginRunner();
        self.run();
    }

    public void run() {

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

        StandardJavaFileManager fileman = compiler.getStandardFileManager(null, null, null);

        List<File> files = As.list(Files.find(".", ".java"));
        
        CompilationTask task = compiler.getTask(null, // out
                fileman, // fileManager
                null, // diagnosticsListener
                Strings.words("-proc:only -d build"), // options
                null, // classes
                fileman.getJavaFileObjectsFromFiles(files));

        task.setProcessors(As.list(new JavacPlugin()));

        task.call();

    }

}

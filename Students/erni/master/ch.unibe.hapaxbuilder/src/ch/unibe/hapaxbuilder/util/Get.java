package ch.unibe.hapaxbuilder.util;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaModelException;

import ch.unibe.scg.util.As;

public class Get {

    public static List<IPackageFragmentRoot> sourcePackageRoots(IPackageFragmentRoot[] packageFragments) {
        List<IPackageFragmentRoot> list = As.list(packageFragments);
        List<IPackageFragmentRoot> result = new ArrayList<IPackageFragmentRoot>();
        for (IPackageFragmentRoot each : list) {
            try {
                if (each.getKind() == IPackageFragmentRoot.K_SOURCE)
                    result.add(each);
            } catch (JavaModelException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

}

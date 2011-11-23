package ch.akuhn.edu.mit.tedlab;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ch.akuhn.edu.mit.tedlab.DMat;
import ch.akuhn.edu.mit.tedlab.SMat;
import ch.akuhn.edu.mit.tedlab.SVDRec;
import ch.akuhn.edu.mit.tedlab.Svdlib;

// # CAUTION DO NOT USE !!!
// 
// This implementation of SVDLIB is not correct!
// 
// For correct code please refer to [1]
// 
// This code has been manually translated the code from C to Java. We have been 
// reported that results different from the original code for some problem sets. 
// We have a better implementation of SVD in Java, based on a version of ARPACK 
// that was automatically transcoded from the Fortran sources using f2j [2] it
// is faster, works on offline matrices and results should be accurate since the
// translation to Java was automated.
// 
// [1] http://bender.unibe.ch/svn/codemap/ch.akuhn.matrix/src/ch/akuhn/matrix/eigenvalues/
// [2] http://icl.cs.utk.edu/f2j
//
// --Nov 2011

public class SmallMatricesTest {

    @Test
    public void emptyMatrix() {
        DMat d = new DMat(0,0);
        SMat s = new Svdlib().svdConvertDtoS(d);
        SVDRec rec = new Svdlib().svdLAS2A(s, 20);
        assertEquals(0, rec.Ut.value.length);
        assertEquals(0, rec.Vt.value.length);
        assertEquals(0, rec.S.length);
    }
    
    @Test
    public void oneOnOneMatrix() {
        DMat d = new DMat(1,1);
        SMat s = new Svdlib().svdConvertDtoS(d);
        SVDRec rec = new Svdlib().svdLAS2A(s, 20);
        assertEquals(1, rec.Ut.value.length);
        assertEquals(1, rec.Vt.value.length);
        assertEquals(1, rec.S.length);
    }

    @Test
    public void ThreeOnThreeMatrix() {
        DMat d = new DMat(3,3);
        SMat s = new Svdlib().svdConvertDtoS(d);
        SVDRec rec = new Svdlib().svdLAS2A(s, 20);
        assertEquals(3, rec.Ut.value.length);
        assertEquals(3, rec.Vt.value.length);
        assertEquals(3, rec.S.length);
    }
    
    
}

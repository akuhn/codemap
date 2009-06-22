package org.codemap.svdlib;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.codemap.svdlib.Svdlib.*;

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

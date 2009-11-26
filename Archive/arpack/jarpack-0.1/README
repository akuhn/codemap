
JARPACK 0.1
-----------

This is an automatic Java translation of the ARPACK library:

http://www.caam.rice.edu/software/ARPACK

The translation was done using f2j version 0.8.1:

http://icl.cs.utk.edu/f2j
http://sourceforge.net/projects/f2j

This directory should contain the following files:

  README               - this file
  README.orig          - original ARPACK readme file
  INSTALL              - installation details
  CHANGES              - what has changed in this version
  EXAMPLES             - directory containing examples of calling JARPACK

The following jar files should exist:

  arpack_combined.jar  - for convenience, this jar file contains all the 
                         following jar files combined into one
  blas.jar             - the BLAS library
  lapack.jar           - the LAPACK library
  xerbla.jar           - LAPACK error reporting routine
  arpack.jar           - the ARPACK library
  arpack_util.jar      - the ARPACK utilities
  f2jutil.jar          - utilities required for running f2j translated code

Notes:

1.  This release has not been tuned for performance - it is a simple,
    automatic translation.

2.  Some scalars must be wrapped in objects.  The wrapper classes are
    located in the org.netlib.util package.  Therefore, your code 
    should contain "import org.netlib.util.*;" to have access to the
    wrappers.

    In addition, your code should import edu.rice.caam.arpack.Blah
    where Blah represents the routine your code calls.  See the
    example code in the EXAMPLES subdirectory

3.  See the warnings on recompilation in the INSTALL file.

4.  If you are using a JVM with a JIT complier and encounter a
    fault in calling JARPACK, try turning off the JIT and report
    the problem to f2j@cs.utk.edu.

5.  The appropriate jar files should be in your CLASSPATH:
      f2jutil.jar
      blas.jar
      lapack.jar
      xerbla.jar
      arpack.jar
      arpack_util.jar
 
    You may customize your error handling by replacing xerbla.jar
    with your own error reporting package.

6.  All array arguments are followed by an extra "offset" argument.
    This allows passing array subsections.

7.  It is important to keep this in mind when interfacing Java code
    to the JARPACK routines:  all multidimensional arrays are mapped 
    to one-dimensional arrays in the translated code and the original 
    column-major layout is maintained.

Contact f2j@cs.utk.edu with any questions, comments, or suggestions.

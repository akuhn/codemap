#!/bin/sh

ARPACK_CP=.:../../blas.jar:../../lapack.jar:../../arpack_util.jar:../../arpack.jar:../../f2jutil.jar

java  -cp $ARPACK_CP:Dsvd.jar Dsvd
java  -cp $ARPACK_CP:Ssvd.jar Ssvd

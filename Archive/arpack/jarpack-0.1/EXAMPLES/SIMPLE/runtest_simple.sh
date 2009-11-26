#!/bin/sh

ARPACK_CP=.:../../blas.jar:../../lapack.jar:../../arpack_util.jar:../../arpack.jar:../../f2jutil.jar

java  -cp $ARPACK_CP:Dssimp.jar Dssimp
java  -cp $ARPACK_CP:Dnsimp.jar Dnsimp
java  -cp $ARPACK_CP:Snsimp.jar Snsimp
java  -cp $ARPACK_CP:Sssimp.jar Sssimp

#!/bin/sh

ARPACK_CP=.:../../blas.jar:../../lapack.jar:../../arpack_util.jar:../../arpack.jar:../../f2jutil.jar

java  -cp $ARPACK_CP:Dsbdr1.jar Dsbdr1
java  -cp $ARPACK_CP:Dsbdr2.jar Dsbdr2
java  -cp $ARPACK_CP:Dsbdr3.jar Dsbdr3
java  -cp $ARPACK_CP:Dsbdr4.jar Dsbdr4
java  -cp $ARPACK_CP:Dsbdr5.jar Dsbdr5
java  -cp $ARPACK_CP:Dsbdr6.jar Dsbdr6
java  -cp $ARPACK_CP:Ssbdr1.jar Ssbdr1
java  -cp $ARPACK_CP:Ssbdr2.jar Ssbdr2
java  -cp $ARPACK_CP:Ssbdr3.jar Ssbdr3
java  -cp $ARPACK_CP:Ssbdr4.jar Ssbdr4
java  -cp $ARPACK_CP:Ssbdr5.jar Ssbdr5
java  -cp $ARPACK_CP:Ssbdr6.jar Ssbdr6

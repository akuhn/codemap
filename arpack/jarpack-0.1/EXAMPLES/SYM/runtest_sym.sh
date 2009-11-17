#!/bin/sh

ARPACK_CP=.:../../blas.jar:../../lapack.jar:../../arpack_util.jar:../../arpack.jar:../../f2jutil.jar

java  -cp $ARPACK_CP:Dsdrv1.jar Dsdrv1
java  -cp $ARPACK_CP:Dsdrv2.jar Dsdrv2
java  -cp $ARPACK_CP:Dsdrv3.jar Dsdrv3
java  -cp $ARPACK_CP:Dsdrv4.jar Dsdrv4
java  -cp $ARPACK_CP:Dsdrv5.jar Dsdrv5
java  -cp $ARPACK_CP:Dsdrv6.jar Dsdrv6
java  -cp $ARPACK_CP:Ssdrv1.jar Ssdrv1
java  -cp $ARPACK_CP:Ssdrv2.jar Ssdrv2
java  -cp $ARPACK_CP:Ssdrv3.jar Ssdrv3
java  -cp $ARPACK_CP:Ssdrv4.jar Ssdrv4
java  -cp $ARPACK_CP:Ssdrv5.jar Ssdrv5
java  -cp $ARPACK_CP:Ssdrv6.jar Ssdrv6

#!/bin/sh

ARPACK_CP=.:../../blas.jar:../../lapack.jar:../../arpack_util.jar:../../arpack.jar:../../f2jutil.jar

java  -cp $ARPACK_CP:Dndrv1.jar Dndrv1
java  -cp $ARPACK_CP:Dndrv2.jar Dndrv2
java  -cp $ARPACK_CP:Dndrv3.jar Dndrv3
java  -cp $ARPACK_CP:Dndrv4.jar Dndrv4
java  -cp $ARPACK_CP:Sndrv1.jar Sndrv1
java  -cp $ARPACK_CP:Sndrv2.jar Sndrv2
java  -cp $ARPACK_CP:Sndrv3.jar Sndrv3
java  -cp $ARPACK_CP:Sndrv4.jar Sndrv4

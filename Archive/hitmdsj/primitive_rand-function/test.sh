echo "compiling ..."
javac Rand.java
gcc -o rand rand.c
echo "testing ..."
java Rand > output_java
./rand > output_c

python diff.py
echo "done"
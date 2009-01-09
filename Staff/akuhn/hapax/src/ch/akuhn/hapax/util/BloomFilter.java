package ch.akuhn.hapax.util;

import static java.lang.Math.pow;

import java.util.BitSet;

import ch.akuhn.util.Lorem;
import ch.akuhn.util.Out;

public class BloomFilter {

    private long mask;
    private BitSet bits;
    private int count = 0;

    public BloomFilter() {
        this(12);
    }
    
    public BloomFilter(int m) {
        mask = (1 << m) - 1;
        bits = new BitSet();
    }
    
    public void add(String element) {
        for (Hash each: Hash.values()) {
            bits.set(hash(each, element));
        }
        count++;
    }
    
    public double falsePositiveProbability() {
        double k = 10, m = (mask + 1), n = count;
        return pow(1 - pow(1 - 1/m, k*n), k);
    }
    
    private int hash(Hash hash, String element) {
        return (int) (hash.hash(element) & mask);
    }

    public boolean contains(String element) {
        for (Hash each: Hash.values()) {
            if (!bits.get(hash(each, element))) return false;
        }
        return true;
    }

    public enum Hash {

        // The hashing function had been distributed under:
        //
        // Copyright (c) 2008, Zbigniew Jerzak, Dresden University of Technology
        //
        // All rights reserved.
        //
        // Redistribution and use in source and binary forms, with or without
        // modification, are permitted provided that the following conditions
        // are met:
        //
        // * Redistributions of source code must retain the above copyright
        // notice, this list of conditions and the following disclaimer.
        // * Redistributions in binary form must reproduce the above copyright
        // notice, this list of conditions and the following disclaimer in the
        // documentation and/or other materials provided with the distribution.
        // * Neither the name of the Dresden University of Technology nor the
        // names of its contributors may be used to endorse or promote products
        // derived from this software without specific prior written permission.
        //
        // THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
        // "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
        // LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
        // A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
        // OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
        // SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
        // LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
        // DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
        // THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
        // (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
        // OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

        /**
         * An algorithm produced by Arash Partow. Based on ideas from all of the
         * above hash functions making a hybrid rotative and additive hash
         * function algorithm. Resembles the design as close as possible to a
         * simple LFSR. An empirical result which demonstrated the distributive
         * abilities of the hash algorithm was obtained using a hash-table with
         * 100003 buckets, hashing The Project Gutenberg Etext of Webster's
         * Unabridged Dictionary, the longest encountered chain length was 7,
         * the average chain length was 2, the number of empty buckets was 4579.
         * 
         * @author Arash Partow
         * @author Zbigniew Jerzak
         */
        AP {
            @Override
            public long hash(final String data) {
                long hash = 0xAAAAAAAA;
                for (int i = 0; i < data.length(); i++) {
                    if ((i & 1) == 0) {
                        hash ^= ((hash << 7) ^ data.charAt(i) ^ (hash >> 3));
                    } else {
                        hash ^= (~((hash << 11) ^ data.charAt(i) ^ (hash >> 5)));
                    }
                }
                return hash;
            }
        },
        /**
         * This hash function comes from Brian Kernighan and Dennis Ritchie's
         * book "The C Programming Language". It is a simple hash function using
         * a strange set of possible seeds which all constitute a pattern of
         * 31....31...31 etc, it seems to be very similar to the DJB hash
         * function.
         * 
         * @author Zbigniew Jerzak
         * @author Arash Partow
         */
        BKDR {
            private final static long seed = 131;

            @Override
            public long hash(final String data) {
                long hash = 0;
                for (int i = 0; i < data.length(); i++) {
                    hash = (hash * seed) + data.charAt(i);
                }
                return hash;
            }
        },
        /**
         * Yet another hash implementation
         * 
         * @author Zbigniew Jerzak
         * @author Arash Partow
         * 
         */
        BP {
            @Override
            public long hash(final String data) {
                long hash = 0;

                for (int i = 0; i < data.length(); i++) {
                    hash = hash << 7 ^ data.charAt(i);
                }
                return hash;
            }
        },
        /**
         * An algorithm proposed by Donald E. Knuth in The Art Of Computer
         * Programming Volume 3, under the topic of sorting and search chapter
         * 6.4.
         * 
         * @author Zbigniew Jerzak
         * @author Arash Partow
         */
        DEK {
            @Override
            public long hash(final String data) {
                long hash = data.length();
                for (int i = 0; i < data.length(); i++) {
                    hash = ((hash << 5) ^ (hash >> 27)) ^ data.charAt(i);
                }
                return hash;
            }
        },
        /**
         * An algorithm produced by Professor Daniel J. Bernstein and shown
         * first to the world on the usenet newsgroup comp.lang.c. It is one of
         * the most efficient hash functions ever published.
         * 
         * @author Zbigniew Jerzak
         * @author Arash Partow
         */
        DJB {
            @Override
            public long hash(final String data) {
                long hash = 5381;

                for (int i = 0; i < data.length(); i++) {
                    hash = ((hash << 5) + hash) + data.charAt(i);
                }
                return hash;
            }
        },
        /**
         * Yet another hash implementation.
         * 
         * @author Zbigniew Jerzak
         * @author Arash Partow
         * 
         */
        FNV {
            private final static long fnv_prime = 0x811C9DC5;

            @Override
            public long hash(final String data) {
                long hash = 0;

                for (int i = 0; i < data.length(); i++) {
                    hash *= fnv_prime;
                    hash ^= data.charAt(i);
                }

                return hash;
            }
        },
        /**
         * A bitwise hash function written by Justin Sobel
         * 
         * @author Zbigniew Jerzak
         * @author Arash Partow
         */
        JS {
            @Override
            public long hash(final String data) {
                long hash = 1315423911;

                for (int i = 0; i < data.length(); i++) {
                    hash ^= ((hash << 5) + data.charAt(i) + (hash >> 2));
                }

                return hash;
            }
        },
        /**
         * This hash algorithm is based on work by Peter J. Weinberger of AT&T
         * Bell Labs. The book Compilers (Principles, Techniques and Tools) by
         * Aho, Sethi and Ulman, recommends the use of hash functions that
         * employ the hashing methodology found in this particular algorithm.
         * 
         * @author Zbigniew Jerzak
         * @author Arash Partow
         */
        PJW {
            private final static long BitsInUnsignedInt = 32L;
            private final static long ThreeQuarters = (long) ((BitsInUnsignedInt * 3) / 4);
            private final static long OneEighth = (long) (BitsInUnsignedInt / 8);
            private final static long HighBits = (long) (0xFFFFFFFF) << (BitsInUnsignedInt - OneEighth);

            @Override
            public long hash(final String data) {
                long hash = 0;
                long test = 0;

                for (int i = 0; i < data.length(); i++) {
                    hash = (hash << OneEighth) + data.charAt(i);

                    if ((test = hash & HighBits) != 0) {
                        hash = ((hash ^ (test >> ThreeQuarters)) & (~HighBits));
                    }
                }
                return hash;
            }
        },
        /**
         * A simple hash function from Robert Sedgwicks Algorithms in C book.
         * Added some simple optimizations to the algorithm in order to speed up
         * its hashing process.
         * 
         * @author Zbigniew Jerzak
         * @author Arash Partow
         */
        RS {

            private final static long b = 378551;

            /*
             * (non-Javadoc)
             * 
             * @see bloomfilter.hashes.IHash#hash(java.lang.String)
             */
            @Override
            public long hash(final String data) {
                long a = 63689;
                long hash = 0;

                for (int i = 0; i < data.length(); i++) {
                    hash = hash * a + data.charAt(i);
                    a = a * b;
                }
                return hash;
            }
        },
        /**
         * This is the algorithm of choice which is used in the open source SDBM
         * project. The hash function seems to have a good over-all distribution
         * for many different data sets. It seems to work well in situations
         * where there is a high variance in the MSBs of the elements in a data
         * set.
         * 
         * @author Zbigniew Jerzak
         * @author Arash Partow
         */
        SDBM {

            /*
             * (non-Javadoc)
             * 
             * @see bloomfilter.hashes.IHash#hash(java.lang.String)
             */
            @Override
            public long hash(final String data) {
                long hash = 0;

                for (int i = 0; i < data.length(); i++) {
                    hash = data.charAt(i) + (hash << 6) + (hash << 16) - hash;
                }

                return hash;
            }
        };

        public long hash(String data) {
            return -1l;
        }

    }
    
    public static void main(String[] args) {
        
        BloomFilter bf = new BloomFilter();
        
        for (String each: Lorem.ipsum()) bf.add(each);
        
        Out.puts( bf.contains("Lorem") );
        Out.puts( bf.contains("Foo") );
        
        Out.puts( bf.falsePositiveProbability() );
        
    }

}

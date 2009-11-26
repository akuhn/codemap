# tornk.awk
#
# turn row vectors into ranks,
# compatible with Spearman rank correlation
# Fri Jun 10 08:34:39     2005
#
# Modified towards Numerical Recipes in C:
# Tue Aug  8 08:26:51     2006
#
#

{
  if(NF==0 || $0 ~ /^[ \t]*#/)
    print

  else {
    
#    print NR | "cat >& 2"
    
    for(i = 1; i <= NF; i++) {
      tmp =  0+$i
      a[i] = tmp
      ++multi[tmp]
    }

    n = asort(a,srt)                # numeric sort (gawk ver >= 3)

    for(i = n; i >= 1; i--) 
      b[srt[i]] = i
    
    for(i in multi) 
      rnks[i] = b[i] + .5 * (1 + multi[i]) - 1

    s = ""
    for(i = 1; i <= NF; i++) {      # replace numerics by ranks
      s = s  "" rnks[a[i]] " "
      delete a[i]
    }

    print s

    for(i in multi) {
      delete multi[i]
      delete rnks[i]
      delete b[i]
    }
  }
}


# just a text matrix transpose (in memory) for most frequent row field numbers

NF>0 {
  ++nr
  for(i=1; i<=NF; i++) d[nr,i]=$i
  if(++c[NF] > j) j = c[mxi = NF] 
  n[nr] = NF
}

END{

  for(i=1; i <= mxi; i++) {

    l = ""

    for(j=1; j <= nr; j++) 
      if(n[j] == mxi)
        l = l " " d[j,i]

    print l
  }
}

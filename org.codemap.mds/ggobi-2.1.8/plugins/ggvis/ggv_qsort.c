typedef int (*CompareFunc)(const void *, const void *);
void Myqsort(void* bot, int nmemb, int size, CompareFunc compar);

/*
 * MTHRESH is the smallest partition for which we compare for a median
 * value instead of using the middle value.
 * THRESH is the minimum number of entries in a partition for continued
 * partitioning.
 */
const int MTHRESH = 6;
const int THRESH = 4;


static void quick_sort(char *bot, int nmemb, int size, CompareFunc compar);
static void insertion_sort(char *bot, int nmemb, int size, CompareFunc compar);


void Myqsort(void* bot, int nmemb, int size, CompareFunc compar)
{
  if (nmemb <= 1) return;

  if (nmemb >= THRESH) quick_sort((char*)bot, nmemb, size, compar);
  else insertion_sort((char*)bot, nmemb, size, compar);
}


/*
 * Swap two areas of size number of bytes.  Although qsort(3) permits random
 * blocks of memory to be sorted, sorting pointers is almost certainly the
 * common case (and, were it not, could easily be made so).  Regardless, it
 * isn't worth optimizing; the SWAP's get sped up by the cache, and pointer
 * arithmetic gets lost in the time required for comparison function calls.
 */
#define  SWAP(a, b) { \
  cnt = size; \
  do { \
    ch = *a; \
    *a++ = *b; \
    *b++ = ch; \
  } while (--cnt); \
}

/*
 * Knuth, Vol. 3, page 116, Algorithm Q, step b, argues that a single pass
 * of straight insertion sort after partitioning is complete is better than
 * sorting each small partition as it is created.  This isn't correct in this
 * implementation because comparisons require at least one (and often two)
 * function calls and are likely to be the dominating expense of the sort.
 * Doing a final insertion sort does more comparisons than are necessary
 * because it compares the "edges" and medians of the partitions which are
 * known to be already sorted.
 *
 * This is also the reasoning behind selecting a small THRESH value (see
 * Knuth, page 122, equation 26), since the quicksort algorithm does less
 * comparisons than the insertion sort.
 */
#define  SORT(bot, n) { \
  if (n > 1) {\
    if (n == 2) {\
      t1 = bot + size;\
      if (compar(t1, bot) < 0) {\
        SWAP(t1, bot);\
      }\
    } else {\
      insertion_sort(bot, n, size, compar);\
    }\
  }\
}

static void quick_sort(char *bot, int nmemb, int size, CompareFunc compar)
{
  register int cnt;
  register unsigned char ch;
  register char *top, *mid, *t1, *t2;
  register int n1, n2;
  char *bsv;

  /* bot and nmemb must already be set. */
partition:

  /* find mid and top elements */
  mid = bot + size * (nmemb >> 1);
  top = bot + (nmemb - 1) * size;

  /*
   * Find the median of the first, last and middle element (see Knuth,
   * Vol. 3, page 123, Eq. 28).  This test order gets the equalities
   * right.
   */
  if (nmemb >= MTHRESH) {
    n1 = compar(bot, mid);
    n2 = compar(mid, top);
    if (n1 < 0 && n2 > 0)
      t1 = compar(bot, top) < 0 ? top : bot;
    else if (n1 > 0 && n2 < 0)
      t1 = compar(bot, top) > 0 ? top : bot;
    else
      t1 = mid;

    /* if mid element not selected, swap selection there */
    if (t1 != mid) {
      SWAP(t1, mid);
      mid -= size;
    }
  }

  /* Standard quicksort, Knuth, Vol. 3, page 116, Algorithm Q. */
#define  didswap  n1
#define  newbot  t1
#define  replace  t2
  didswap = 0;
  for (bsv = bot;;) {
    for (; bot < mid && compar(bot, mid) <= 0; bot += size) ;
    while (top > mid) {
      if (compar(mid, top) <= 0) {
        top -= size;
        continue;
      }
      newbot = bot + size;  /* value of bot after swap */
      if (bot == mid)    /* top <-> mid, mid == top */
        replace = mid = top;
      else {      /* bot <-> top */
        replace = top;
        top -= size;
      }
      goto swap;
    }
    if (bot == mid)
      break;

    /* bot <-> mid, mid == bot */
    replace = mid;
    newbot = mid = bot;    /* value of bot after swap */
    top -= size;

swap:    SWAP(bot, replace);
    bot = newbot;
    didswap = 1;
  }

  /*
   * Quicksort behaves badly in the presence of data which is already
   * sorted (see Knuth, Vol. 3, page 119) going from O N lg N to O N^2.
   * To avoid this worst case behavior, if a re-partitioning occurs
   * without swapping any elements, it is not further partitioned and
   * is insert sorted.  This wins big with almost sorted data sets and
   * only loses if the data set is very strangely partitioned.  A fix
   * for those data sets would be to return prematurely if the insertion
   * sort routine is forced to make an excessive number of swaps, and
   * continue the partitioning.
   */
  if (!didswap) {
    insertion_sort(bsv, nmemb, size, compar);
    return;
  }

  /*
   * Re-partition or sort as necessary.  Note that the mid element
   * itself is correctly positioned and can be ignored.
   */
#define  nlower  n1
#define  nupper  n2
  bot = bsv;
  nlower = (mid - bot) / size;  /* size of lower partition */
  mid += size;
  nupper = nmemb - nlower - 1;  /* size of upper partition */

  /*
   * If must call recursively, do it on the smaller partition; this
   * bounds the stack to lg N entries.
   */
  if (nlower > nupper) {
    if (nupper >= THRESH)
      quick_sort(mid, nupper, size, compar);
    else {
      SORT(mid, nupper);
      if (nlower < THRESH) {
        SORT(bot, nlower);
        return;
      }
    }
    nmemb = nlower;
  } else {
    if (nlower >= THRESH)
      quick_sort(bot, nlower, size, compar);
    else {
      SORT(bot, nlower);
      if (nupper < THRESH) {
        SORT(mid, nupper);
        return;
      }
    }
    bot = mid;
    nmemb = nupper;
  }
  goto partition;
  /* NOTREACHED */
}

static void insertion_sort(char *bot, int nmemb, int size, CompareFunc compar)
{
  register int cnt;
  register unsigned char ch;
  register char *s1, *s2, *t1, *t2, *top;

  /*
   * A simple insertion sort (see Knuth, Vol. 3, page 81, Algorithm
   * S).  Insertion sort has the same worst case as most simple sorts
   * (O N^2).  It gets used here because it is (O N) in the case of
   * sorted data.
   */
  top = bot + nmemb * size;
  for (t1 = bot + size; t1 < top;) {
    for (t2 = t1; (t2 -= size) >= bot && compar(t1, t2) < 0;) ;
    if (t1 != (t2 += size)) {
      /* Bubble bytes up through each element. */
      for (cnt = size; cnt--; ++t1) {
        ch = *t1;
        for (s1 = s2 = t1; (s2 -= size) >= t2; s1 = s2)
          *s1 = *s2;
        *s1 = ch;
      }
    } else
      t1 += size;
  }
}

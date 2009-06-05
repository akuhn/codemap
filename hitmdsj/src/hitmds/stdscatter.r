stdscatter <- function(y) {
#STDSCATTER - normalize shift/scale/rotation invariant scatter plots by PCA

# move to origin
  y <- (t(y) - apply(y, 2, mean))

# get PCA projection (=rotation) using left eigenvectors of SVD
  y <- crossprod(svd(y, nv = 0)$u, y)

# flip heavier (larger) second moment to the left (in general: to minus)
# and rescale dimensions by overall maximum coordinate veriance
  t(y * sign(apply(sign(y) * y * y, 1, sum))) / max(apply(y, 1, sd))
}

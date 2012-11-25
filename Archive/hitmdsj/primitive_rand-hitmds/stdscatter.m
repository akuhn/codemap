function y = stdscatter(x) 
%STDSCATTER - normalize shift/scale/rotation invariant scatter plots by PCA

  [nol ndi] = size(x);
  
% move to origin
  y = x - repmat(mean(x), nol, 1);

% get PCA projection (=rotation) using left eigenvectors of SVD
  [u x x] = svd(y.', 'econ');
  y = y * u(:,1:ndi);

% flip heavier (larger) second moment to the left (in general: to minus)
% and rescale dimensions by overall maximum coordinate veriance
  x = sign(sum(sign(y) .* y .* y, 1));
  y = y .* repmat(x,nol,1) / sqrt(max(var(y)));


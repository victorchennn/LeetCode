def activateFountains(A):
    if not A: return 0
    # For all left end of interval, store the largest right end
    n = len(A)
    aux = list(range(n + 1))
    for i, x in enumerate(A, 1):
        aux[max(i - x, 1)] = min(i + x, n)

    ans, l, r = 0, 1, aux[1]
    while r <= n:
        new_r, ans = r, ans + 1
        # If the fountain ranges overlap
        # get the rightmost bound for next search
        while l <= r:
            new_r = max(new_r, aux[l])
            l += 1
        if l > n: break
        # If the fountain ranges don't overlap
        r = max(new_r, aux[l])
    return ans

if __name__ ==  '__main__':
    print(activateFountains([0,0,0,3,0,0,2,0,0]), 2)
    print(activateFountains([3,0,2,0,1,0]), 2)
    print(activateFountains([3,0,1,0,1,0]), 2)
    print(activateFountains([3,0,1,0,0,1]), 2)
    print(activateFountains([2,0,2,0,1,0]), 2)
    print(activateFountains([2,0,0,0,0]), 3)
    print(activateFountains([0,0,0,0,0]), 5)
    print(activateFountains([1,2,1]), 1)
    print(activateFountains([0,1,0]), 1)
import itertools

def permutations_generator():
    crypto = ('s', 'e', 'n', 'd', 'm', 'o', 'r', 'y')
    # Generate all permutations possible for; backtrack to get next permutation
    for perm in itertools.permutations(range(10), len(crypto)):
        yield dict(zip(crypto, perm))

def crypto():
    for sol in permutations_generator():
        # For unique solution, 's' and 'm' cannot be 0
        if sol['s'] == 0 or sol['m'] == 0:
            continue
        send = 1000 * sol['s'] + 100 * sol['e'] + 10 * sol['n'] + sol['d']
        more = 1000 * sol['m'] + 100 * sol['o'] + 10 * sol['r'] + sol['e']
        money = 10000 * sol['m'] + 1000 * sol['o'] + 100 * sol['n'] + 10 * sol['e'] + sol['y']
        if send + more == money:
            return send, more, money

if __name__ == "__main__":
    print("\n====================================")
    print("Start program...")
    solution = crypto()
    if solution is not None:
        print("  SEND\t ", str(solution[0]))
        print("+ MORE\t+", str(solution[1]))
        print("------\t-------")
        print(" MONEY\t", str(solution[2]))
    else:
        print("No Solution")
    print("End")
    print("====================================", end='\n\n')

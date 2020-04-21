# The script plots the sequence of Collatz
# starting from a specific n_0

import sys
import matplotlib.pyplot as plt
from matplotlib import style
style.use("Solarize_Light2")

def f(n):
    return int(n/2) if (n % 2 == 0) else (3*n + 1)

if len(sys.argv) != 2:
    sys.exit("USAGE : Collatz.py n_0")

# n_0 : starting value
n_0 = int(sys.argv[1])

seq = [n_0]

# The algorithm ends when 1 is reached
# after 1, the sequence enters a loop
while(seq[len(seq) - 1] != 1):
    # A new value is added to the list
    # as the result of the f function
    # applied to the last element of
    # the sequence
    seq.append(f(seq[len(seq) - 1]))

print(seq)

# Diagram visualization
plt.plot([j for j in range(len(seq))], seq, "mo--")
plt.axis([0, len(seq) + int(0.1 * len(seq)), 0, int(max(seq)) + int(0.1 * max(seq))])
plt.suptitle("Visualizing Collatz sequence for " + "n_0 = " + str(n_0))
plt.show()
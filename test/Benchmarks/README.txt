The attached synthetic benchmarks were written to measure the performance
of our branch predictor. What to follow is a brief description of the naming 
convention of our files which describes the benchmark's behavior.
********************************************************************************
Naming convention: BranchType_Pattern_Repetitions

Branch types used are BEQ and BNE.

Pattern: T stands for a taken branch, and N stands for a not taken branch.
	 for example, a NTT pattern implies a Nottaken-taken-taken pattern of branching that is repeated for a certain number of times.

Repetitions: the number of times that our pattern has been repeated in our code.


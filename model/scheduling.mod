set JOBS;									#set of jobs j

param processing {JOBS};					# processing time per job j (p_j)
param due {JOBS};							# due date per job j (d_j)


var Tardiness {JOBS} >= 0;
var StartingTime {JOBS} >= 0;
var ordering {j in JOBS, i in JOBS: i!=j} binary;				# vincoli di precedenza
var M := sum {j in JOBS} processing[j];						# big M

minimize TotalTardiness: sum {j in JOBS} Tardiness[j];

subject to TardinessAtLeast {j in JOBS}: Tardiness[j] >= StartingTime[j] + processing[j] - due[j];

#ordering
subject to OrderingConstraint {i in JOBS, j in JOBS: i != j}: StartingTime[j] + processing[j] <= M*ordering[i, j] + StartingTime[i];
subject to OrderingConstraint2 {i in JOBS, j in JOBS: i != j}: StartingTime[i] + processing[i] <= M*(1 - ordering[i, j]) + StartingTime[j];

subject to OrderingCoherency {i in JOBS, j in JOBS: i != j}: ordering[i, j] = 1 - ordering[j, i];
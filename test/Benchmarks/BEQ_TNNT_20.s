.code
	daddi r1,r1,20		;20 loops of loop1
loop1: daddi r3,r3,234	;some random number to be added to r3
	daddi r2,r2,5		;another random number to be added to r2
	dmult r3,r2			;wasting some cycles
	ddiv  r3,r2			;wasting some more cycles
	daddi r1,r1,-1
	daddi r4,r4,8		;always ensuring that r4 and r5 are at the same value
	daddi r5,r5,8		;
	beq R4, R5, loop2;

	dmult r3,r2			;wasting some cycles
	ddiv  r3,r2			;wasting some more cycles

loop2: daddi r2,r3,25
	ddiv r3,r2
	beq r1,r0,END 
	daddi r2,r2,5
	dmult r3,r2 ;
	ddiv  r3,r2	  
	beq r1,r0,END 
	beq R4,R5,loop1

	END: SYSCALL 0
.code
	daddi r1,r1,20 
loop1: daddi r3,r3,234
	daddi r2,r2,5
	dmult r3,r2 ;
	ddiv  r3,r2	    ;
	daddi r1,r1,-1
	daddi r4,r4,8	
	daddi r5,r5,7 	
	bne R4, R5, loop2;

loop2: daddi r2,r3,25
	ddiv r3,r2
	bne R4,R5,loop3

	dmult r3,r2 
	ddiv  r3,r2	    
	daddi r1,r1,-1

loop3: daddi r2,r3,25
	ddiv r3,r2
	bne R4,R5,loop4

	dmult r3,r2 ; extra code
	ddiv  r3,r2	    
	daddi r1,r1,-1

loop4: daddi r2,r3,25
	ddiv r3,r2
	bne R4,R5,loop5

	dmult r3,r2 ;
	ddiv  r3,r2	    ;
	daddi r1,r1,-1

loop5: daddi r2,r3,25
	ddiv r3,r2
	bne R4,R5,loop6

	dmult r3,r2 ;
	ddiv  r3,r2	    
	daddi r1,r1,-1

loop6: daddi r2,r3,25
	ddiv r3,r2
	bne R4,R5,END

	
	dmult r3,r2 ;
	ddiv  r3,r2	    
	daddi r1,r1,-1

	END: SYSCALL 0
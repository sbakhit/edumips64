.code
	daddi r1,r1,20 
loop: daddi r3,r3,234
	daddi r2,r2,5
	dmult r3,r2 ;
	ddiv  r3,r2	    ;
	daddi r1,r1,-1
	daddi r4,r4,8	;
	daddi r5,r5,8 	;
	beq r1,r0,END	;
	beq R4, R5, loop;


	END: SYSCALL 0